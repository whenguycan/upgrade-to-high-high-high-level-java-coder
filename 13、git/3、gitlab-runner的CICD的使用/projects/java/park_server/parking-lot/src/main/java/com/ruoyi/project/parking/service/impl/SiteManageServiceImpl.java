package com.ruoyi.project.parking.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.dahuatech.icc.exception.ClientException;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.common.CommonConstants;
import com.ruoyi.project.parking.dahua.ipms.device.IpmsDevice;
import com.ruoyi.project.parking.domain.RegularCar;
import com.ruoyi.project.parking.domain.TEntryRecords;
import com.ruoyi.project.parking.domain.TExitRecords;
import com.ruoyi.project.parking.domain.dahuavo.CarCaptureData;
import com.ruoyi.project.parking.domain.dahuavo.ValidChannelVo;
import com.ruoyi.project.parking.domain.vo.BPassageDeviceVo;
import com.ruoyi.project.parking.domain.vo.TEntryRecordsVo;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkOrderVO;
import com.ruoyi.project.parking.entity.ParkLiveRecords;
import com.ruoyi.project.parking.enums.PayStatusEnums;
import com.ruoyi.project.parking.grpc.model.vo.CaptureDeviceInfoModelVo;
import com.ruoyi.project.parking.mq.model.PaymentData;
import com.ruoyi.project.parking.service.*;
import com.ruoyi.project.parking.service.grpcclient.DeviceGrpcClientService;
import com.ruoyi.project.parking.service.grpcclient.model.OpenCloseChannelRequest;
import com.ruoyi.project.parking.service.grpcclient.model.SendLedScreenMessageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 场地闸门管理 mzl
 */
@Service
public class SiteManageServiceImpl implements SiteManageService {


    public static final String SITE_OUTER = "场库外";
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private DeviceGrpcClientService deviceGrpcClientService;
    @Resource
    private ITEntryRecordsService tEntryRecordsService;

    @Resource
    private IBPassageDeviceService ibPassageDeviceService;

    @Resource
    private IRegularCarService regularCarService;


    @Resource
    private IParkLiveRecordsService itParkLiveRecordsService;

    @Resource
    private ITExitRecordsService exitRecordsService;


    @Resource
    RedisCache redisCache;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource
    BPassageMonitorService bPassageMonitorService;

    //已经入库
    public void addEntryRecord(ValidChannelVo validChannelVo) {
        //将接口传来的数据转化为业务系统的进场数据；
        TEntryRecords tEntryRecord = initEntryRecord(validChannelVo);
        Integer parkLiveId = null;
        ParkLiveRecords parkLiveRecords = itParkLiveRecordsService.queryByParkNoCarNumber(validChannelVo.getParkNo(), validChannelVo.getCarNum());
        if (parkLiveRecords != null) {
            parkLiveId = parkLiveRecords.getId();
        } else {
            parkLiveId = itParkLiveRecordsService.insertParkLiveRecords(validChannelVo.getParkNo(), validChannelVo.getCarNum(), validChannelVo.getCarType(), validChannelVo.getActTime());
        }
        tEntryRecord.setParkLiveId(parkLiveId);
        //车在场地临时状态
        tEntryRecordsService.insertTEntryRecords(tEntryRecord);
    }

    @Override
    public void initValidCarData(ValidChannelVo validChannelVo, CaptureDeviceInfoModelVo info) {
        logger.info("----info:" + JSONObject.toJSONString(info));
        BPassageDeviceVo passageObj = getPassageObj(info.getDeviceLocalIp(), info.getParkNo());
        if (passageObj == null) {
            return;
        }
        validChannelVo.setPassageFlag(passageObj.getPassageFlag());
        validChannelVo.setCarNumPic(info.getCarNumPic());
        validChannelVo.setPassageId(passageObj.getPassageId());
        validChannelVo.setCarImgUrl(info.getCarImgUrl());
        //通道编号
        validChannelVo.setPassageNo(passageObj.getPassageNo());
        validChannelVo.setPassageName(passageObj.getPassageName());
        validChannelVo.setParkNo(passageObj.getParkNo());
        validChannelVo.setCarNum(info.getCarNum());
        //设置拍照时间
        validChannelVo.setActTime(DateUtils.toLocalDateTime(info.getCapTime().replaceAll("/", "-"), DateUtils.YYYY_MM_DD_HH_MM_SS));
        validChannelVo.setParkingCarColor(info.getCarColor());
        validChannelVo.setFromFieldName(passageObj.getFromFieldName());
        validChannelVo.setToFieldName(passageObj.getToFieldName());
        validChannelVo.setCarType(info.getParkingCarType());
        validChannelVo.setDeviceIp(info.getDeviceLocalIp());
        validChannelVo.setSluiceDevChnId(info.getDevChnId());
    }

    /**
     * @param parkNo
     * @param siteFlag
     */
    public void updateCacheParkSpaceAmount(String parkNo, int siteFlag) {

        String key = CacheConstants.PARKNO_ACCOUNT_KEY + parkNo;
        if (redisTemplate.opsForValue().get(key) != null) {
            Integer remain = (Integer) redisTemplate.opsForValue().get(key);
//            int remain = Integer.parseInt((String) redisTemplate.opsForValue().get(key));
            if (siteFlag == Integer.parseInt(CommonConstants.LEAVE_STATUS)) {
                // redisTemplate.opsForValue().increment(key);
                updateCacheParkSpaceAmount(getluaIncrbyStr(), parkNo, 1000);
            } else {
                if (remain > 0) {
                    updateCacheParkSpaceAmount(getluaDecrbyStr(), parkNo, 1);
                    //  redisTemplate.opsForValue().decrement(key);
                }
            }
        }
    }

    //已经离场
    public void addExitRecord(ValidChannelVo validChannelVo) {
        //将接口传来的数据转化为业务系统的进场数据；
        TExitRecords tExitRecords = initExitRecord(validChannelVo);
        ParkLiveRecords parkLiveRecords = itParkLiveRecordsService.queryByParkNoCarNumber(validChannelVo.getParkNo(), validChannelVo.getCarNum());
        Integer parkLiveId = null;
        if (parkLiveRecords != null) {
            parkLiveId = parkLiveRecords.getId();
        } else {
            parkLiveId = itParkLiveRecordsService.insertParkLiveRecordsWithExit(validChannelVo.getParkNo(), validChannelVo.getCarNum(), validChannelVo.getCarType(), tExitRecords.getExitTime());
        }
        tExitRecords.setParkLiveId(parkLiveId);
        //车在场地临时状态
        exitRecordsService.insertTExitRecords(tExitRecords);
        //更新在场记录为离开状态
        itParkLiveRecordsService.departureByParkNoCarNumber(tExitRecords.getParkNo(), tExitRecords.getCarNumber(), DateUtils.toLocalDateTime(new Date()));
        clearCarData(validChannelVo.getPassageNo(), tExitRecords);
    }


    @Override
    public void manageEnterGate(ValidChannelVo entrySiteInfo) {
        logger.info("---------entrySiteInfo:"+JSONObject.toJSONString(entrySiteInfo));
        deviceGrpcClientService.sendLedScreenMessage(getInitializedLedMessage(entrySiteInfo));
        logger.info("-----------是否开闸："+ (bPassageMonitorService.openByOpenType(entrySiteInfo.getCarNum(), entrySiteInfo.getParkNo(), entrySiteInfo.getPassageId(), entrySiteInfo.getCarType())));
        if (bPassageMonitorService.openByOpenType(entrySiteInfo.getCarNum(), entrySiteInfo.getParkNo(), entrySiteInfo.getPassageId(), entrySiteInfo.getCarType())) {
            //通过grpc开门砸口
            openSite(entrySiteInfo);
        }
    }

    private void openSite(ValidChannelVo validChannelVo){

        OpenCloseChannelRequest openCloseChannelRequest = new OpenCloseChannelRequest();
        openCloseChannelRequest.setStatus("1");
        openCloseChannelRequest.setDeviceIp(validChannelVo.getDeviceIp());
        openCloseChannelRequest.setParkNo(validChannelVo.getParkNo());
        openCloseChannelRequest.setCarNum(validChannelVo.getCarNum());
        deviceGrpcClientService.openCloseChannel(openCloseChannelRequest);
        logger.info("闸门已开");
    }
    /**
     * @param entrySiteInfo 预进场
     * @throws EncodeException
     * @throws IOException
     */
    @Override
    public void managePreArriveSiteGate(ValidChannelVo entrySiteInfo) {

//        if (bPassageMonitorService.openByOpenType(entrySiteInfo.getCarNum(), entrySiteInfo.getParkNo(), entrySiteInfo.getPassageId(), entrySiteInfo.getCarType())) {
//            //todo 通过grpc 调用接口
//            OpenCloseChannelRequest openCloseChannelRequest = new OpenCloseChannelRequest();
//            openCloseChannelRequest.setStatus("1");
//            openCloseChannelRequest.setDeviceIp(entrySiteInfo.getDeviceIp());
//            openCloseChannelRequest.setParkNo(entrySiteInfo.getParkNo());
//            deviceGrpcClientService.openCloseChannel(openCloseChannelRequest);
//            //ipmsDevice.OpenCloseChannel(entrySiteInfo.getSluiceDevChnId(), 1, "");
//            addEntryRecord(entrySiteInfo);
//            logger.info("已线上开闸");
//        }


    }

    //    public void preArriveSite(ValidChannelVo entrySiteInfo){
//
//
//        //获取需要的通道信息；
//        BPassageDeviceVo passageObj = getPassageObj(entrySiteInfo.getSluiceDevChnId());
//        if (passageObj == null) {
//            logger.info("该设备没有绑定通道");
//            return;
//        }
//        try {
//            if (bPassageMonitorService.openByOpenType(entrySiteInfo.getCarNum(), entrySiteInfo.getParkNo(), entrySiteInfo.getPassageId(), entrySiteInfo.getCarType())) {
//                //todo 通过grpc 调用接口
//                ipmsDevice.OpenCloseChannel(entrySiteInfo.getSluiceDevChnId(), 1, "");
//                logger.info("已线上开闸");
//            }
//        } catch (ClientException e) {
//            logger.error(e.getErrorDescription());
//        }
//    }


    /**
     * 预离场
     *
     * @param validChannelVo 出场信息
     * @throws EncodeException
     * @throws IOException
     */
    @Override
    public void managePreLeaveSiteGate(ValidChannelVo validChannelVo) {

        TEntryRecordsVo tEntryRecords = new TEntryRecordsVo();
        tEntryRecords.setCarNumber(validChannelVo.getCarNum());
        //该车在车场内是否有记录
        ParkLiveRecords parkLiveRecord = itParkLiveRecordsService.queryByParkNoCarNumber(validChannelVo.getParkNo(), validChannelVo.getCarNum());
        Integer parkLiveId = null;
        if (parkLiveRecord != null) {
            parkLiveId = parkLiveRecord.getId();
        } else {
            parkLiveId = itParkLiveRecordsService.insertParkLiveRecords(validChannelVo.getParkNo(), validChannelVo.getCarNum(), validChannelVo.getCarType(), validChannelVo.getActTime());
        }
        //更新在场车的预离开时间
        itParkLiveRecordsService.updateExitTimeById(parkLiveId, validChannelVo.getActTime());
        boolean openSiteFlag = false;
        PaymentData cacheObject = redisCache.getCacheObject(CacheConstants.ADVANCE_PAYMENT + validChannelVo.getCarNum());
        //场内付款
        if (cacheObject != null && StringUtils.isNotEmpty(cacheObject.getOutTradeNo())) {
            validChannelVo.setEntryTime(DateUtils.toLocalDateTime(cacheObject.getEntryTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
            validChannelVo.setPayableAmount(cacheObject.getPayAmount().doubleValue());
            validChannelVo.setOrderNo(cacheObject.getOutTradeNo());
            openSiteFlag = true;
            logger.info("离场-场内付款成功");
        } else {    // 岗亭扫码
            String carNumberKey = CacheConstants.PARKNO_PASSAGE_KEY +
                    validChannelVo.getParkNo() +
                    "_" +
                    validChannelVo.getPassageNo();
            //该车预场记录存入缓存中
            redisCache.setCacheObject(carNumberKey, validChannelVo.getCarNum(), 15, TimeUnit.MINUTES);
            //订单信息展示
            VehicleParkOrderVO vehicleParkOrderVO = itParkLiveRecordsService.queryOrCreatePavilionOrderByParkNoPassageNo(validChannelVo.getParkNo(), validChannelVo.getPassageNo());
            //通道闸口扫码
            if (vehicleParkOrderVO != null) {
                validChannelVo.setPayableAmount(vehicleParkOrderVO.getPayableAmount());
                validChannelVo.setEntryTime(vehicleParkOrderVO.getEntryTime());
                validChannelVo.setOrderNo(vehicleParkOrderVO.getOrderNo());
                //这种情况几乎不会发生：就是扫描了码，交了钱，又退回去，然后闸门被关闸门，
                // 然后他又过来，但是已经付款
                if (PayStatusEnums.PAY_STATUS.PAYMENT_SUCCESS.equals(vehicleParkOrderVO.getPayStatus())) {
                    openSiteFlag = true;
                    logger.info("离场-岗亭付款成功");
                }
            }
        }
        //设置停留时间
        validChannelVo.setDurationTime(String.valueOf((Duration.between(validChannelVo.getEntryTime(), validChannelVo.getActTime())).toMinutes()));
        //显示器只要有车离开，就显示
        deviceGrpcClientService.sendLedScreenMessage(getInitializedLedMessage(validChannelVo));
        if (openSiteFlag) {
            openSite(validChannelVo);
        }
    }


    private void clearCarData(String passageNo, TExitRecords tExitRecords) {
        //出场后加1,最大容量默认1000,该系统并没有指定最大的空余容量；
        updateCacheParkSpaceAmount(tExitRecords.getParkNo(), 2);
        //删除缓存
        redisCache.deleteObject(CacheConstants.ADVANCE_PAYMENT + tExitRecords.getCarNumber());
        redisCache.deleteObject(CacheConstants.PARKNO_PASSAGE_KEY + tExitRecords.getParkNo()
                + "_" + passageNo + "_" + tExitRecords.getCarNumber());
        //删除车场+通道 岗亭缓存订单
        redisCache.deleteObject(CacheConstants.PARKING_ORDER + tExitRecords.getParkNo() + "_" + tExitRecords.getCarNumber());
        logger.info("[" + tExitRecords.getCarNumber() + "]--已经离开场库");

    }

    @Override
    public void updateLeaveRecord(Long exitId, String passageNo) {
//        TExitRecords tExitRecords = exitRecordsService.selectTExitRecordsById(exitId);
//        tExitRecords.setExitFlag("1");
//        exitRecordsService.updateTExitRecords(tExitRecords);
//        //更新在场记录为离开状态
//        itParkLiveRecordsService.departureByParkNoCarNumber(tExitRecords.getParkNo(), tExitRecords.getCarNumber(), DateUtils.toLocalDateTime(new Date()));
//        //出场后加1,最大容量默认1000,该系统并没有指定最大的空余容量；
//        Integer maxParkHoldAmount = 1000;
//        updateCacheParkSpaceAmount(getluaIncrbyStr(), tExitRecords.getParkNo(), maxParkHoldAmount - 1);
//        //删除缓存
//        redisCache.deleteObject(CacheConstants.ADVANCE_PAYMENT + tExitRecords.getCarNumber());
//        redisCache.deleteObject(CacheConstants.PARKNO_PASSAGE_KEY + tExitRecords.getParkNo()
//                + "_" + passageNo + "_" + tExitRecords.getCarNumber());
//        //删除车场+通道 岗亭缓存订单
//        redisCache.deleteObject(CacheConstants.PARKING_ORDER + tExitRecords.getParkNo() + "_" + tExitRecords.getCarNumber());
//        logger.info("[" + tExitRecords.getCarNumber() + "]--已经离开场库");

    }


    /**
     * 离开场库，已经到场库外；
     * 缓存 余量+1；
     * redis中的在场记录删除；
     * 更新在场记录表为离开状态；
     * 存入出场表里；
     * 关闸门；
     *
     * @param validChannelVo
     * @throws EncodeException
     * @throws IOException
     */
    @Override
    public void manageLeaveSiteGate(ValidChannelVo validChannelVo) {
//        //获取需要的通道信息；
//        BPassageDeviceVo passageObj = getPassageObj(validChannelVo.getSluiceDevChnId());
//        validChannelVo.setParkNo(passageObj.getParkNo());
//        validChannelVo.setPassageId(passageObj.getPassageId());
        //离场数据初始化；
        TExitRecords tExitRecords = initExitRecord(validChannelVo);
        //离场: 内场 ->场库外
        if (SITE_OUTER.equals(validChannelVo.getToFieldName())) {
            //离场：压到场外的感应器，插入数据库，更新在场表为离开状态
            exitRecordsService.insertTExitRecords(tExitRecords);
            //更新在场记录为离开状态
            itParkLiveRecordsService.departureByParkNoCarNumber(validChannelVo.getParkNo(), validChannelVo.getCarNum(), DateUtils.toLocalDateTime(new Date()));
            //出场后加1,最大容量默认1000,该系统并没有指定最大的空余容量；
            Integer maxParkHoldAmount = 1000;
            updateCacheParkSpaceAmount(getluaIncrbyStr(), validChannelVo.getParkNo(), maxParkHoldAmount - 1);
            //删除缓存
            redisCache.deleteObject(CacheConstants.ADVANCE_PAYMENT + validChannelVo.getCarNum());
            redisCache.deleteObject(CacheConstants.PARKNO_PASSAGE_KEY + tExitRecords.getParkNo()
                    + "_" + validChannelVo.getPassageNo() + "_" + tExitRecords.getCarNumber());
            //删除车场+通道 岗亭缓存订单
            redisCache.deleteObject(CacheConstants.PARKING_ORDER + tExitRecords.getParkNo() + "_" + validChannelVo.getCarNum());
        }
        logger.info("已线上开启闸门");
//
//        try {
//            ipmsDevice.OpenCloseChannel(validChannelVo.getSluiceDevChnId(), 1, null);
//            logger.info("已线上开启闸门");
//        } catch (ClientException e) {
//            logger.error(e.getErrorDescription());
//        }
    }


//    //从场外到场内之后，生成在场记录
//    public void addParkLiveOrder(Long entryId, String parkNo, String carNumber, String carType, String entryFlag) {
//        TEntryRecords tEntryRecords = tEntryRecordsService.selectTEntryRecordsById(entryId);
//        if (tEntryRecords == null) {
//            logger.info("无进场记录");
//            return;
//        }
//        tEntryRecords.setEntryTime(LocalDateTime.now());
//        //该车在车场内是否有记录
//        Integer parkLiveId = null;
//        ParkLiveRecords parkLiveRecords = itParkLiveRecordsService.queryByParkNoCarNumber(parkNo, carNumber);
//        if (parkLiveRecords != null) {
//            parkLiveId = parkLiveRecords.getId();
//        } else {
//            parkLiveId = itParkLiveRecordsService.insertParkLiveRecords(parkNo, carNumber, carType, tEntryRecords.getEntryTime());
//        }
//        tEntryRecords.setParkLiveId(parkLiveId);
//        tEntryRecords.setEntryFlag(entryFlag);
//        tEntryRecordsService.updateTEntryRecords(tEntryRecords);
//        String key = CacheConstants.PARKNO_ACCOUNT_KEY + parkNo;
//        if (redisTemplate.opsForValue().get(key) != null) {
//            updateCacheParkSpaceAmount(getluaDecrbyStr(), parkNo, 1);
//        }
//    }

    /**
     * - 1
     *
     * @return
     */
    private String getluaDecrbyStr() {
        return "local key = KEYS[1]\n" +
                "local count = tonumber(ARGV[1])\n" +
                "local current = redis.call('get', key);\n" +
                "if current and tonumber(current) < count then\n" +
                "    return tonumber(current);\n" +
                "end\n" +
                "current = redis.call('decr', key)\n" +
                "return tonumber(current);";
    }

    /**
     * + 1
     *
     * @return
     */
    private String getluaIncrbyStr() {
        return "local key = KEYS[1]\n" +
                "local count = tonumber(ARGV[1])\n" +
                "local current = redis.call('get', key);\n" +
                "if current and tonumber(current) > count then\n" +
                "    return tonumber(current);\n" +
                "end\n" +
                "current = redis.call('incr', key)\n" +
                "return tonumber(current);";
    }

    /**
     * 更新缓存中的车厂数量
     *
     * @param scriptText
     * @param parkNo
     * @param amount
     * @return
     */
    private Long updateCacheParkSpaceAmount(String scriptText, String parkNo, int amount) {

        String key = CacheConstants.PARKNO_ACCOUNT_KEY + parkNo;
        List<Object> keyList = Collections.singletonList(key);
        DefaultRedisScript<Long> defaultRedisScript = getDefaultRedisScript(scriptText);
        return redisTemplate.execute(defaultRedisScript, keyList, amount);
    }

    private DefaultRedisScript<Long> getDefaultRedisScript(String scriptText) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(scriptText);
        redisScript.setResultType(Long.class);
        return redisScript;
    }


//    BPassageDeviceVo getPassageObj(String deviceId) {
//        BPassageDeviceVo bPassageDevice = new BPassageDeviceVo();
//        bPassageDevice.setDeviceId(deviceId);
//        List<BPassageDeviceVo> bPassageDevices = ibPassageDeviceService.selectBPassageDeviceList(bPassageDevice);
//        if (!CollectionUtils.isEmpty(bPassageDevices)) {
//            return bPassageDevices.get(0);
//        }
//        return null;
//    }

    BPassageDeviceVo getPassageObj(String deviceIP, String parkNo) {
        BPassageDeviceVo bPassageDevice = new BPassageDeviceVo();
        bPassageDevice.setServerIp(deviceIP);
        bPassageDevice.setParkNo(parkNo);
        List<BPassageDeviceVo> bPassageDevices = ibPassageDeviceService.selectBPassageDeviceList(bPassageDevice);
        if (!CollectionUtils.isEmpty(bPassageDevices)) {
            return bPassageDevices.get(0);
        }
        return null;
    }

    //初始化进场时间
    TEntryRecords initEntryRecord(ValidChannelVo entrySiteInfo) {
        TEntryRecords entryRecord = new TEntryRecords();
        entryRecord.setParkNo(entrySiteInfo.getParkNo());
        entryRecord.setPassageId(entrySiteInfo.getPassageId());
        entryRecord.setDeviceId(entrySiteInfo.getSluiceDevChnId());
        entryRecord.setCarColor(entrySiteInfo.getParkingCarColor());
        entryRecord.setCarImgUrl(entrySiteInfo.getCarImgUrl());
        //设置车牌号
        entryRecord.setCarNumber(entrySiteInfo.getCarNum());
        entryRecord.setCarNumberEdit(entrySiteInfo.getCarNum());
        entryRecord.setCarType(entrySiteInfo.getCarType());
        entryRecord.setCarNumberColor(entrySiteInfo.getCarNumColor());
        entryRecord.setCreateTime(new Date());
        entryRecord.setEntryTime(entrySiteInfo.getActTime());
        return entryRecord;
    }

    /**
     * 初始化 led显示的数据
     *
     * @param vo
     */
    public SendLedScreenMessageRequest getInitializedLedMessage(ValidChannelVo vo) {

        SendLedScreenMessageRequest ledScreenMessageRequest = new SendLedScreenMessageRequest();
        ledScreenMessageRequest.setDeviceIp(vo.getDeviceIp());
        ledScreenMessageRequest.setCarNumber(vo.getCarNum());
        ledScreenMessageRequest.setParkNo(vo.getParkNo());
        RegularCar regularCar = new RegularCar();
        regularCar.setCarNumber(vo.getCarNum());
        regularCar.setParkNo(vo.getParkNo());
        if (CommonConstants.Entry_STATUS.equals(vo.getPassageFlag())) {
            ledScreenMessageRequest.setStuInTime(DateUtils.format(vo.getActTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        } else {
            //设置ip
            ledScreenMessageRequest.setStuInTime(DateUtils.format(vo.getEntryTime(), DateUtils.YYYY_MM_DD));
            ledScreenMessageRequest.setStuOutTime(vo.getActTime().format(DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD_HH_MM_SS)));
            ledScreenMessageRequest.setRemainDay("");
            ledScreenMessageRequest.setPassEnable(1);
            String key = CacheConstants.PARKNO_ACCOUNT_KEY + vo.getParkNo();
            ledScreenMessageRequest.setRemainSpace(getSpaceRemain(key));
            ledScreenMessageRequest.setParkingTime(vo.getDurationTime());
            ledScreenMessageRequest.setParkCharges(String.valueOf(vo.getPayableAmount()));
        }
        List<RegularCar> regularCars = regularCarService.listUnsafe(regularCar);
        if (CollectionUtils.isNotEmpty(regularCars)) {
            ledScreenMessageRequest.setUserType(Constants.MONTHLY_CARD_USER);
        } else {
            ledScreenMessageRequest.setUserType(Constants.CASUAL_USER);
        }
        logger.info("ledScreenMessageRequest:" + ledScreenMessageRequest);
        return ledScreenMessageRequest;
    }


    private Integer getSpaceRemain(String key) {
        /*Object remainObject = redisCache.getCacheObject(key);
        if (remainObject != null) {
            String remainStr = (String) remainObject;
            return Integer.parseInt(remainStr);
        } else {
            return -1;
        }*/
        return redisCache.getCacheObject(key);
    }

    //初始化出场数据
    TExitRecords initExitRecord(ValidChannelVo leaveSiteInfo) {
        TExitRecords exitRecords = new TExitRecords();
        exitRecords.setDeviceId(leaveSiteInfo.getSluiceDevChnId());
        exitRecords.setParkNo(leaveSiteInfo.getParkNo());
        exitRecords.setPassageId(leaveSiteInfo.getPassageId());
        exitRecords.setCarColor(leaveSiteInfo.getParkingCarColor());
        exitRecords.setCarType(leaveSiteInfo.getCarType());
        exitRecords.setCarImgUrl(leaveSiteInfo.getCarImgUrl());
        exitRecords.setCarNumber(leaveSiteInfo.getCarNum());
        exitRecords.setCarNumberEdit(leaveSiteInfo.getCarNum());
        //该车在车场内是否有记录
//        ParkLiveRecords parkLiveRecord = itParkLiveRecordsService.queryByParkNoCarNumber(leaveSiteInfo.getParkNo(), leaveSiteInfo.getCarNum());
//        exitRecords.setParkLiveId(parkLiveRecord.getId());
        exitRecords.setExitTime(leaveSiteInfo.getActTime());
        exitRecords.setCreateTime(new Date());
        return exitRecords;
    }
    /**
     * 当要出厂库外，再场内已经支付成功，直接开闸门；
     * 其他情况，将数据插入到在场记录表中， 订单系统获在场记录表里的数据
     *
     * @param
     * @throws EncodeException
     * @throws IOException
     */
//    @Override
//    public void manageLeaveSiteGate(LeaveSiteInfo leaveSiteInfo) throws EncodeException, IOException {
//        //获取需要的通道信息；
//        BPassageDeviceVo passageObj = getPassageObj(leaveSiteInfo.getSluiceDevChnId());
//        if (passageObj == null) {
//            logger.info("该设备没有绑定通道");
//            return;
//        }
//        leaveSiteInfo.setPassageId(passageObj.getPassageId());
//        //将接口传来的数据转化为业务系统的离场数据；
//        TExitRecords tExitRecords = initExitRecord(leaveSiteInfo);
//        LocalDateTime localDateTime = DateUtils.toLocalDateTime(leaveSiteInfo.getLeaveTime(), DateUtils.YYYY_MM_DD_HH_MM_SS);
//        //该车在车场内是否有记录
//        ParkLiveRecords parkLiveRecord = itParkLiveRecordsService.queryByParkNoCarNumber(leaveSiteInfo.getParkingLot(), leaveSiteInfo.getCarNum());
//        //该车在车场内是否有记录
//        Integer parkLiveId = null;
//        if (parkLiveRecord != null) {
//            parkLiveId = parkLiveRecord.getId();
//        } else {
//            parkLiveId = itParkLiveRecordsService.insertParkLiveRecords(tExitRecords.getParkNo(), tExitRecords.getCarNumber(), tExitRecords.getCarType(), localDateTime);
//        }
//
//        //预立场状态
//        if (true) {
//            //没有记录,就插入一条记录；针对入场时，车牌号可能入错的情况，几乎不会发生
//
//            //更新在场车的预离开时间；
//            itParkLiveRecordsService.updateExitTimeById(parkLiveId, localDateTime);
//            tExitRecords.setParkLiveId(parkLiveId);
//            //订单信息展示
//            VehicleParkOrderVO vehicleParkOrderVO = itParkLiveRecordsService.queryOrCreatePavilionOrderByParkNoPassageNo(tExitRecords.getParkNo(), passageObj.getPassageNo());
//            //场内付款，生成订单，查到订单，直接开闸门
//            if (vehicleParkOrderVO != null) {
//                Collection<String> keys = redisCache.keys
//                        (CacheConstants.SENTRYBOX_PASSAGE_KEY + "*" + "_" + passageObj.getPassageId());
//                for (String key : keys) {
//                    String sentryBoxName = key.replace("sentrybox_passage:", "").replace("_" + passageObj.getPassageId(), "");
//                    WebSocketServerExitPool.sendMessageByPassageId(sentryBoxName, tExitRecords);
//                }
//                //调用闸门控制器，打开闸门;
//                try {
//                    ipmsDevice.OpenCloseChannel(leaveSiteInfo.getSluiceDevChnId(), 1, null);
//                    String key = CacheConstants.PARKNO_ACCOUNT_KEY + tExitRecords.getParkNo();
//                    StringBuilder stringBuilder1 = getluaIncrbyStr();
//                    RedisScript<Long> redisScript = RedisScript.of(stringBuilder1.toString(), Long.class);
//                    //出场后加1
//                    Long aLong = redisTemplate.execute(redisScript, CollectionUtil.newArrayList(key), "1");
//
//                } catch (ClientException e) {
//                    logger.info(e.getErrorDescription());
//                }
//                return;
//            }
//            // 车到达预离口闸道，将车、通道、设备信息写入缓存，以便算费；
//            redisCache.setCacheObject(CacheConstants.PARKNO_PASSAGE_KEY + tExitRecords.getParkNo()
//                    + "_" + passageObj.getPassageNo() + "_" + tExitRecords.getCarNumber(), 1);
//        } else {
//            //离场：压到场外的感应器，插入数据库，更新在场表为离开状态
//            Date date = new Date();
//            tExitRecords.setExitTime(date);
//            exitRecordsService.insertTExitRecords(tExitRecords);
//            //更新在场记录为离开状态
//            itParkLiveRecordsService.departureByParkNoCarNumber(tExitRecords.getParkNo(), tExitRecords.getCarNumber(), DateUtils.toLocalDateTime(date));
//            //删除缓存
//            redisCache.deleteObject(CacheConstants.ADVANCE_PAYMENT + leaveSiteInfo.getCarNum());
//            redisCache.deleteObject(CacheConstants.PARKNO_PASSAGE_KEY + tExitRecords.getParkNo()
//                    + "_" + passageObj.getPassageNo() + "_" + tExitRecords.getCarNumber());
//        }
//
//
//    }

    /**
     * 进场
     *
     * @param entrySiteInfo
     * @throws EncodeException
     * @throws IOException
     */
    @Override
    public void manageAfterEnterSiteGate(ValidChannelVo entrySiteInfo) throws EncodeException, IOException {
//        //获取需要的通道信息；
//        BPassageDeviceVo passageObj = getPassageObj(entrySiteInfo.getSluiceDevChnId());
//        if (passageObj == null) {
//            logger.info("该设备没有绑定通道");
//            return;
//        }
//        //第一次进场,设置我们系统自定义的通道id
//        entrySiteInfo.setPassageId(passageObj.getPassageId());
//        //设置场地编号
//        entrySiteInfo.setParkNo(passageObj.getParkNo());
//        //将接口传来的数据转化为业务系统的进场数据；
//        TEntryRecords tEntryRecord = initEntryRecord(entrySiteInfo);
//        //车在场地临时状态
//        LocalDateTime localDateTime = entrySiteInfo.getPreArriveTime();
//        //该车在车场内是否有记录
//        Integer parkLiveId = null;
//        ParkLiveRecords parkLiveRecords = itParkLiveRecordsService.queryByParkNoCarNumber(entrySiteInfo.getParkNo(), entrySiteInfo.getCarNum());
//        if (parkLiveRecords != null) {
//            parkLiveId = parkLiveRecords.getId();
//        } else {
//            parkLiveId = itParkLiveRecordsService.insertParkLiveRecords(tEntryRecord.getParkNo(), tEntryRecord.getCarNumber(), tEntryRecord.getCarType(), localDateTime);
//        }
//        tEntryRecord.setParkLiveId(parkLiveId);
//        tEntryRecordsService.insertTEntryRecords(tEntryRecord);
//        //从外面进入场内
//        if (SITE_OUTER.equals(passageObj.getFromFieldName())) {
//            String key = CacheConstants.PARKNO_ACCOUNT_KEY + tEntryRecord.getParkNo();
//            if (redisTemplate.opsForValue().get(key) != null) {
//                updateCacheParkSpaceAmount(getluaDecrbyStr(), tEntryRecord.getParkNo(), 1);
//            }
//        }
//        try {
//            ipmsDevice.OpenCloseChannel(entrySiteInfo.getSluiceDevChnId(), 2, "");
//            logger.info("已线上关上闸门");
//        } catch (ClientException e) {
//            logger.info(e.getErrorDescription());
//        }
    }


}
