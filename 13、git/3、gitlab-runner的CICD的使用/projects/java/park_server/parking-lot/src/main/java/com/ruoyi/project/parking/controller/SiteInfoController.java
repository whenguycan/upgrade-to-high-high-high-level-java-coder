package com.ruoyi.project.parking.controller;

import com.alibaba.fastjson2.JSONObject;
import com.dahuatech.icc.exception.ClientException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.common.config.IccIPConfig;
import com.ruoyi.project.parking.dahua.icc.ICCUtils;
import com.ruoyi.project.parking.dahua.ipms.device.IpmsDevice;
import com.ruoyi.project.parking.domain.TEntryRecords;
import com.ruoyi.project.parking.domain.dahuavo.*;
import com.ruoyi.project.parking.domain.param.ManualOpenSiteParam;
import com.ruoyi.project.parking.domain.vo.BPassageDeviceVo;
import com.ruoyi.project.parking.enums.ColorEnums;
import com.ruoyi.project.parking.service.IBPassageDeviceService;
import com.ruoyi.project.parking.service.IParkLiveRecordsService;
import com.ruoyi.project.parking.service.SiteManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * 对接大华设备信息的接口
 * mzl
 */
@Api("开闸")
@RestController
@RequestMapping("/SiteInfo")
public class SiteInfoController {


    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String EVENT_CAPTURE = "car.capture";
    public static final String EVENT_ACCESS = "car.access";
    public static final String METHOD_NAME = "method";
    public static final String PARKING_CAR_TYPE = "parking_car_type";
    //进场标识
    public static final String ENTRY_STATUS = "1";
    //出场标识
    public static final String LEAVE_STATUS = "2";
    public static final int CAR_DIRECT_ENTER = 8;
    public static final int CAR_DIRECT_EXIT = 9;
    public static final String URL_URL_LABEL = "fileUrl=";
    @Resource
    private IBPassageDeviceService ibPassageDeviceService;

    @Resource
    SiteManageService siteManageService;
    @Resource
    private IpmsDevice ipmsDevice;

    @Resource
    IParkLiveRecordsService iParkLiveRecordsService;
    @Resource
    IccIPConfig iccIPConfig;

    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation("手动付钱开闸门")
    @PostMapping("/manualOpenSite")
    public AjaxResult manualPayOpenSite(@RequestBody ManualOpenSiteParam manualOpenSiteParam) {

        iParkLiveRecordsService.chargeOpenGate(manualOpenSiteParam.getParkNo(),manualOpenSiteParam.getPassageNo(),
                manualOpenSiteParam.getCarNumber(),manualOpenSiteParam.getOrderNo(), manualOpenSiteParam.getNeedPayAmount());
        return AjaxResult.success(1);
    }

    @ApiOperation("免费开闸门")
    @PostMapping("/freeChargeOpenSite")
    public AjaxResult freeChargeOpenSite(@RequestBody ManualOpenSiteParam manualOpenSiteParam) {
        iParkLiveRecordsService.freeOpenGate(manualOpenSiteParam.getParkNo(),manualOpenSiteParam.getPassageNo(),
                manualOpenSiteParam.getCarNumber(),manualOpenSiteParam.getOrderNo(), manualOpenSiteParam.getFreeReason());
        return AjaxResult.success(1);
    }


    @ApiOperation("开闸门")
    @PostMapping("/openSite")
    public AjaxResult openSite(String devChnId) {
        try {
            ipmsDevice.OpenCloseChannel(devChnId, 1, "");
        } catch (ClientException e) {
            logger.info(e.getErrorDescription());
        }
        return AjaxResult.success(1);
    }

    /**
     * 预离开停车场：发送前端， 生成订单，开闸门
     * 预进场： 发送前端websocket ，开闸门
     *
     * @param msg
     * @return 不开闸门
     */
    /*@ApiOperation("capture")
    @PostMapping("/capture")
    public AjaxResult capture(@RequestBody String msg) throws EncodeException, IOException {
        logger.info("capture data:" + msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String methodEventName = (String) jsonObject.get(METHOD_NAME);
        DaHuaCarCaptureSendData daHuaCarCaptureSendData = JSONObject.parseObject(msg, DaHuaCarCaptureSendData.class);
        CarCaptureData info = daHuaCarCaptureSendData.getInfo();
        //业务通道数据，离场无汽车类型
        ValidChannelVo validChannelVo = new ValidChannelVo();
        BPassageDeviceVo passageObj = getPassageObj(info.getDevChnId());
        if (passageObj == null) {
            return AjaxResult.warn("无此通道");
        }
        validChannelVo.setCarImgUrl(info.getCarImgUrl());
        validChannelVo.setCarNumPic(info.getCarNumPic());
        validChannelVo.setPassageId(passageObj.getPassageId());
        //通道编号
        validChannelVo.setPassageNo(passageObj.getPassageNo());
        validChannelVo.setPassageName(passageObj.getPassageName());
        validChannelVo.setParkNo(passageObj.getParkNo());
        validChannelVo.setCarNum(info.getCarNum());
        validChannelVo.setSluiceDevChnId(info.getDevChnId());
        validChannelVo.setSluiceDevChnName(info.getDevChnName());
        //设置拍照时间
       // validChannelVo.setActTime(info.getCapTime());
        validChannelVo.setParkingCarColor(ColorEnums.Color_Kinds.getByValue(info.getCarColor()).getDesc());
        //设置应用业务索要的数据
        info.setValidChannelVo(validChannelVo);
        //处理图片IP
        intranetIpConventInternet(validChannelVo);

        if (EVENT_CAPTURE.equals(methodEventName)) {
            if (CAR_DIRECT_ENTER == info.getCarDirect()) {
                logger.info("捕获预进场车牌号：" + info.getCarNum());
                //  }
            } else {
                siteManageService.managePreLeaveSiteGate(info.getValidChannelVo());
                logger.info("捕获预离场车牌号：" + info.getCarNum());
            }
            //推送websocket
            String passageIdStr =  String.valueOf(validChannelVo.getPassageId());
            AsyncManager.me().execute(AsyncFactory.sendPassageMessage(passageIdStr,validChannelVo));
            Object carInfo = redisTemplate.opsForValue().get(passageIdStr);
            if (carInfo != null) {
                //拿出最新的一条记录
                List<ValidChannelVo> validChannelVoList = redisTemplate.opsForList().range(passageIdStr, -1, -1);
                if (CollectionUtils.isNotEmpty(validChannelVoList)) {
                    String actTime = DateUtils.parseLocalDateToStr(validChannelVoList.get(0).getActTime(), DateUtils.YYYY_MM_DD);
                    String day = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    //不是同一天就删除
                    if (actTime.indexOf(day) == -1) {
                        redisTemplate.delete(passageIdStr);
                    }
                }
                //将预进场的信息存入到当前的通道口中
                redisTemplate.opsForList().rightPush(passageIdStr, info.getValidChannelVo());
                logger.info("推送的通道Id：" + validChannelVo.getPassageId());
            }
        }
        return AjaxResult.success(info.getValidChannelVo());

    }*/


    /**
     * 预进场闸门信息
     *
     * @param param
     * @return
     */
    @ApiOperation("预进场闸门信息")
    @PostMapping("/getPreArriveICCInfo")
    public AjaxResult getPreArriveICCInfo(@RequestBody String param, HttpServletRequest
            request, HttpServletResponse response) throws Exception {
        //解密后的数据
        String achieveStr = ICCUtils.decode(param, request, response);
        PreAdmission preAdmission = JSONObject.parseObject(achieveStr, PreAdmission.class);
        ValidChannelVo validChannelVo = new ValidChannelVo();
        if (preAdmission != null) {
            initValidBusinessData(validChannelVo, preAdmission);
            //预进场，判断车辆类型，开门
            validChannelVo.setPreArriveTime(DateUtils.toLocalDateTime(preAdmission.getPreArriveTime()));
            siteManageService.managePreArriveSiteGate(validChannelVo);
            logger.info("预进场车牌号：" + preAdmission.getCarNum());
        } else {
            AjaxResult.warn("预进场：数据捕获异常");
        }
        return new AjaxResult();
    }

    //确认进场
    @ApiOperation("进场闸门信息")
    @PostMapping("/getICCInfo")
    public AjaxResult getICCInfo(@RequestBody String param, HttpServletRequest request, HttpServletResponse
            response) throws Exception {
        //解密后的数据
        String achieveStr = ICCUtils.decode(param, request, response);
        // String achieveStr = param;
        logger.info("【确认进场】getICCInfo:{}", achieveStr);
        PreAdmission preAdmission = JSONObject.parseObject(achieveStr, PreAdmission.class);
        ValidChannelVo validChannelVo = new ValidChannelVo();
        if (preAdmission != null) {
            initValidBusinessData(validChannelVo, preAdmission);
            //处理图片IP
            validChannelVo.setCarImgUrl(dealAfterEntryCarImgUrl(validChannelVo.getCarImgUrl()));
            intranetIpConventInternet(validChannelVo);
            //进场
            validChannelVo.setPreArriveTime(DateUtils.toLocalDateTime(preAdmission.getPreArriveTime()));
            siteManageService.manageAfterEnterSiteGate(
                    validChannelVo);
            logger.info("进场车牌号：" + preAdmission.getCarNum());

            // }
        } else {
            AjaxResult.warn("进场：数据捕获异常");
        }
        return AjaxResult.success();

    }


    /**
     * 确认离场
     *
     * @param param
     * @return
     */
    @ApiOperation("确认离场信息")
    @PostMapping("/getLeaveICCInfo")
    public AjaxResult getLeaveICCInfo(@RequestBody String param, HttpServletRequest request, HttpServletResponse
            response) throws Exception {
        //解密后的数据
        String achieveStr = ICCUtils.decode(param, request, response);
        //String achieveStr =param;
        logger.info("【确认离场】getLeaveICCInfo:{}", achieveStr);
        PreLeaveInfo preLeaveInfo = JSONObject.parseObject(achieveStr, PreLeaveInfo.class);
        ValidChannelVo validChannelVo = new ValidChannelVo();
        if (preLeaveInfo != null) {
            initValidBusinessData(validChannelVo, preLeaveInfo);
            LocalDateTime localDateTime = DateUtils.toLocalDateTime(preLeaveInfo.getLeaveTime(), DateUtils.YYYYMMDDHHMMSS);
            validChannelVo.setLeaveTime(localDateTime);
            //预离场
            siteManageService.manageLeaveSiteGate(validChannelVo);
            logger.info("离场车牌号：" + preLeaveInfo.getCarNum());
        } else {
            AjaxResult.warn("离场：数据捕获异常");
        }
        return AjaxResult.success();
    }


    TEntryRecords initEntryRecord(ValidChannelVo entrySiteInfo) {
        TEntryRecords entryRecord = new TEntryRecords();
        entryRecord.setEntryTime(entrySiteInfo.getActTime());
        //设置车牌号
        entryRecord.setCarNumberEdit(entrySiteInfo.getCarNum());
        entryRecord.setParkNo(entrySiteInfo.getParkNo());
        entryRecord.setDeviceId(entrySiteInfo.getSluiceDevChnId());
        entryRecord.setCarType(entrySiteInfo.getCarType());
        entryRecord.setCarNumberColor(entrySiteInfo.getCarNumColor());
        entryRecord.setCreateTime(new Date());
        entryRecord.setCarColor(entrySiteInfo.getParkingCarColor());
        entryRecord.setPassageId(entrySiteInfo.getPassageId());
        entryRecord.setCarImgUrl(entrySiteInfo.getCarImgUrl());
        entryRecord.setCarNumber(entrySiteInfo.getCarNum());
        return entryRecord;
    }

    BPassageDeviceVo getPassageObj(String deviceId) {
        BPassageDeviceVo bPassageDevice = new BPassageDeviceVo();
        bPassageDevice.setDeviceId(deviceId);
        List<BPassageDeviceVo> bPassageDevices = ibPassageDeviceService.selectBPassageDeviceList(bPassageDevice);
        if (!CollectionUtils.isEmpty(bPassageDevices)) {
            return bPassageDevices.get(0);
        }
        return null;
    }


    private String dealAfterEntryCarImgUrl(String imgUrl) {
        return imgUrl.substring(imgUrl.indexOf(URL_URL_LABEL) + URL_URL_LABEL.length());

    }

    private void intranetIpConventInternet(ValidChannelVo info) {
        String intranetIpAndPort = iccIPConfig.getIntranetIpAndPort();
        String carPictureUrl = iccIPConfig.getCarPictureUrl();
        if (StringUtils.isNotEmpty(carPictureUrl)) {
            if (info.getCarImgUrl() != null && info.getCarImgUrl().contains(intranetIpAndPort)) {
                info.setCarImgUrl(info.getCarImgUrl().replace(intranetIpAndPort, carPictureUrl).replace("http", iccIPConfig.getHttp()));
            }
            if (info.getCarNumPic() != null && info.getCarNumPic().contains(intranetIpAndPort)) {
                info.setCarNumPic(info.getCarNumPic().replace(intranetIpAndPort, carPictureUrl).replace("http", iccIPConfig.getHttp()));
            }
        } else {
            String internetIpAndSport = iccIPConfig.getInternetIpAndSport();
            if (info.getCarImgUrl() != null && info.getCarImgUrl().contains(intranetIpAndPort)) {
                info.setCarImgUrl(info.getCarImgUrl().replace(intranetIpAndPort, internetIpAndSport));
            }
            if (info.getCarNumPic() != null && info.getCarNumPic().contains(intranetIpAndPort)) {
                info.setCarNumPic(info.getCarNumPic().replace(intranetIpAndPort, internetIpAndSport));
            }
        }
    }

    //业务系统用到参数
    private void initValidBusinessData(ValidChannelVo validChannelVo, BusinessBaseICCEntity businessBaseICCEntity) {
        validChannelVo.setCarImgUrl(businessBaseICCEntity.getCarImgUrl());
        validChannelVo.setSluiceDevChnId(plusDevChnId(businessBaseICCEntity.getSluiceDevChnId(), -1));
        validChannelVo.setSluiceDevChnName(businessBaseICCEntity.getSluiceDevChnName());
        validChannelVo.setCarType(businessBaseICCEntity.getParkingCarType());
        validChannelVo.setCarNum(businessBaseICCEntity.getCarNum());
      //  validChannelVo.setActTime(businessBaseICCEntity.getActTime());
        validChannelVo.setParkingCarColor(businessBaseICCEntity.getParkingCarColor());
    }

    private String plusDevChnId(String devChnId, int plus) {
        int index = devChnId.indexOf("$");
        String before = devChnId.substring(0, index);
        String after = devChnId.substring(index);
        int i = Integer.parseInt(before) + plus;
        return i + after;
    }
}

