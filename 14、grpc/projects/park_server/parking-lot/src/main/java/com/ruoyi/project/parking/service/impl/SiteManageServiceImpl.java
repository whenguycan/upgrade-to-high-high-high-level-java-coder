package com.ruoyi.project.parking.service.impl;

import com.dahuatech.hutool.core.collection.CollectionUtil;
import com.dahuatech.icc.exception.ClientException;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.parking.dahua.ipms.device.IpmsDevice;
import com.ruoyi.project.parking.domain.BPassageDevice;
import com.ruoyi.project.parking.domain.TEntryRecords;
import com.ruoyi.project.parking.domain.TExitRecords;
import com.ruoyi.project.parking.domain.dahuavo.EntrySiteInfo;
import com.ruoyi.project.parking.domain.dahuavo.LeaveSiteInfo;
import com.ruoyi.project.parking.domain.vo.BPassageDeviceVo;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkOrderVO;
import com.ruoyi.project.parking.entity.ParkLiveRecords;
import com.ruoyi.project.parking.service.*;
import com.ruoyi.project.parking.websocket.Entry.WebSocketServerEntryPool;
import com.ruoyi.project.parking.websocket.Exit.WebSocketServerExitPool;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 场地闸门管理 mzl
 */
@Service
public class SiteManageServiceImpl implements SiteManageService {


    public static final String SITE_OUTER = "场库外";
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ITEntryRecordsService tEntryRecordsService;

    @Resource
    private IBPassageDeviceService ibPassageDeviceService;

    @Resource
    private IParkLiveRecordsService itParkLiveRecordsService;

    @Resource
    private ITExitRecordsService exitRecordsService;

    @Resource
    private IpmsDevice ipmsDevice;

    @Resource
    RedisCache redisCache;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void manageArriveSiteGate(EntrySiteInfo entrySiteInfo) throws EncodeException, IOException {
        //获取需要的通道信息；
        BPassageDeviceVo passageObj = getPassageObj(entrySiteInfo.getSluiceDevChnId());
        if (passageObj == null) {
            logger.info("该设备没有绑定通道");
            return;
        }
        //第一次进场
        entrySiteInfo.setPassageId(passageObj.getPassageId());
        TEntryRecords tEntryRecord = initEntryRecord(entrySiteInfo);
        //车在场地临时状态
        LocalDateTime localDateTime = DateUtils.toLocalDateTime(entrySiteInfo.getArriveTime(), DateUtils.YYYY_MM_DD_HH_MM_SS);
        //该车在车场内是否有记录
        Integer parkLiveId = null;
        ParkLiveRecords parkLiveRecords = itParkLiveRecordsService.queryByParkNoCarNumber(entrySiteInfo.getParkingLot(), entrySiteInfo.getCarNum());
        if (parkLiveRecords != null) {
            parkLiveId = parkLiveRecords.getId();
        } else {
            parkLiveId = itParkLiveRecordsService.insertParkLiveRecords(tEntryRecord.getParkNo(), tEntryRecord.getCarNumber(), tEntryRecord.getCarType(), localDateTime);
        }
        tEntryRecord.setParkLiveId(parkLiveId);
        tEntryRecordsService.insertTEntryRecords(tEntryRecord);
        //建立websocket链接
        Collection<String> keys = redisCache.keys
                (CacheConstants.SENTRYBOX_PASSAGE_KEY + "*" + "_" + tEntryRecord.getPassageId());
        for (String key : keys) {
            String sentryBoxName = key.replace("sentrybox_passage:", "").replace("_" + tEntryRecord.getPassageId(), "");
            WebSocketServerEntryPool.sendMessageByPassageId(sentryBoxName, tEntryRecord);
        }
        //从外面进入场内
        if (SITE_OUTER.equals(passageObj.getFromFieldName())) {
            String key = CacheConstants.PARKNO_ACCOUNT_KEY + tEntryRecord.getParkNo();
            StringBuilder stringBuilder = getluaDecrbyStr();
            RedisScript<Long> redisScript = RedisScript.of(stringBuilder.toString(), Long.class);
            //执行结果
            Long aLong = redisTemplate.execute(redisScript, CollectionUtil.newArrayList(key), "1");
            if (aLong > 0) {
                //调用闸门控制器，打开闸门;
                try {
                    ipmsDevice.OpenCloseChannel(entrySiteInfo.getSluiceDevChnId(), 1, null);
                } catch (ClientException e) {
                    logger.info(e.getErrorDescription());
                }
            }
            return;
        }
        //如果不是从厂库外进入场地，直接抬杆
        try {
            ipmsDevice.OpenCloseChannel(entrySiteInfo.getSluiceDevChnId(), 1, null);
        } catch (ClientException e) {
            logger.info(e.getErrorDescription());
        }

    }

    /**
     * - 1
     *
     * @return
     */
    private StringBuilder getluaDecrbyStr() {
        StringBuilder luasb = new StringBuilder();
        //空格不能少
        //书写lua脚本语言    local定义变量
        luasb.append(" local key=KEYS[1] ");
        luasb.append(" local qty=redis.call('get',key); ");
        //库存量大于需求量
        luasb.append(" if tonumber(qty)>=tonumber(ARGV[1])");
        luasb.append(" then ");
        luasb.append(" redis.call('decrby',KEYS[1],ARGV[1]) ");
        luasb.append(" return 1 ");
        luasb.append(" else ");
        luasb.append(" return 0 ");
        luasb.append(" end ");
        return luasb;
    }


    /**
     * + 1
     *
     * @return
     */
    private StringBuilder getluaIncrbyStr() {
        StringBuilder lua = new StringBuilder();
        //空格不能少
        //书写lua脚本语言    local定义变量
        lua.append(" local key=KEYS[1] ");
        lua.append(" local qty=redis.call('get',key); ");
        //库存量大于需求量
        lua.append(" if tonumber(qty)>=tonumber(ARGV[1])");
        lua.append(" then ");
        lua.append(" redis.call('incrby',KEYS[1],ARGV[1]) ");
        lua.append(" return 1 ");
        lua.append(" else ");
        lua.append(" return 0 ");
        lua.append(" end ");
        return lua;
    }


    /**
     * 当要出厂库外，再场内已经支付成功，直接开闸门；
     * 其他情况，将数据插入到在场记录表中， 订单系统获在场记录表里的数据
     *
     * @param leaveSiteInfo 出场信息
     * @throws EncodeException
     * @throws IOException
     */
    @Override
    public void manageLeaveSiteGate(LeaveSiteInfo leaveSiteInfo) throws EncodeException, IOException {
        //获取需要的通道信息；
        BPassageDeviceVo passageObj = getPassageObj(leaveSiteInfo.getSluiceDevChnId());
        if (passageObj == null) {
            logger.info("该设备没有绑定通道");
            return;
        }
        leaveSiteInfo.setPassageId(passageObj.getPassageId());
        //将接口传来的数据转化为业务系统的离场数据；
        TExitRecords tExitRecords = initExitRecord(leaveSiteInfo);
        LocalDateTime localDateTime = DateUtils.toLocalDateTime(leaveSiteInfo.getLeaveTime(), DateUtils.YYYY_MM_DD_HH_MM_SS);
        //该车在车场内是否有记录
        ParkLiveRecords parkLiveRecord = itParkLiveRecordsService.queryByParkNoCarNumber(leaveSiteInfo.getParkingLot(), leaveSiteInfo.getCarNum());
        //该车在车场内是否有记录
        Integer parkLiveId = null;
        if (parkLiveRecord != null) {
            parkLiveId = parkLiveRecord.getId();
        } else {
            parkLiveId = itParkLiveRecordsService.insertParkLiveRecords(tExitRecords.getParkNo(), tExitRecords.getCarNumber(), tExitRecords.getCarType(), localDateTime);
        }

        //预立场状态
        if (true) {
            //没有记录,就插入一条记录；针对入场时，车牌号可能入错的情况，几乎不会发生

            //更新在场车的预离开时间；
            itParkLiveRecordsService.updateExitTimeById(parkLiveId, localDateTime);
            tExitRecords.setParkLiveId(parkLiveId);
            //订单信息展示
            VehicleParkOrderVO vehicleParkOrderVO = itParkLiveRecordsService.queryOrCreatePavilionOrderByParkNoPassageNo(tExitRecords.getParkNo(), passageObj.getPassageNo());
            //场内付款，生成订单，查到订单，直接开闸门
            if (vehicleParkOrderVO != null) {
                Collection<String> keys = redisCache.keys
                        (CacheConstants.SENTRYBOX_PASSAGE_KEY + "*" + "_" + passageObj.getPassageId());
                for (String key : keys) {
                    String sentryBoxName = key.replace("sentrybox_passage:", "").replace("_" + passageObj.getPassageId(), "");
                    WebSocketServerExitPool.sendMessageByPassageId(sentryBoxName, tExitRecords);
                }
                //调用闸门控制器，打开闸门;
                try {
                    ipmsDevice.OpenCloseChannel(leaveSiteInfo.getSluiceDevChnId(), 1, null);
                    String key = CacheConstants.PARKNO_ACCOUNT_KEY + tExitRecords.getParkNo();
                    StringBuilder stringBuilder1 = getluaIncrbyStr();
                    RedisScript<Long> redisScript = RedisScript.of(stringBuilder1.toString(), Long.class);
                    //出场后加1
                    Long aLong = redisTemplate.execute(redisScript, CollectionUtil.newArrayList(key), "1");

                } catch (ClientException e) {
                    logger.info(e.getErrorDescription());
                }
                return;
            }
            // 车到达预离口闸道，将车、通道、设备信息写入缓存，以便算费；
            redisCache.setCacheObject(CacheConstants.PARKNO_PASSAGE_KEY + tExitRecords.getParkNo()
                    + "_" + passageObj.getPassageNo() + "_" + tExitRecords.getCarNumber(), 1);
        } else {
            //离场：压到场外的感应器，插入数据库，更新在场表为离开状态
            Date date = new Date();
            tExitRecords.setExitTime(date);
            exitRecordsService.insertTExitRecords(tExitRecords);
            //更新在场记录为离开状态
            itParkLiveRecordsService.departureByParkNoCarNumber(tExitRecords.getParkNo(), tExitRecords.getCarNumber(), DateUtils.toLocalDateTime(date));
            //删除缓存
            redisCache.deleteObject(CacheConstants.ADVANCE_PAYMENT + leaveSiteInfo.getCarNum());
            redisCache.deleteObject(CacheConstants.PARKNO_PASSAGE_KEY + tExitRecords.getParkNo()
                    + "_" + passageObj.getPassageNo() + "_" + tExitRecords.getCarNumber());
        }


    }


    TEntryRecords initEntryRecord(EntrySiteInfo entrySiteInfo) {
        TEntryRecords entryRecord = new TEntryRecords();
        entryRecord.setEntryTime(DateUtils.parseDate(entrySiteInfo.getArriveTime()));
        entryRecord.setCarColor(entrySiteInfo.getParkingCarColor());
        entryRecord.setCarImgUrl(entrySiteInfo.getCarImgUrl());
        //设置车牌号
        entryRecord.setCarNumber(entrySiteInfo.getCarNum());
        entryRecord.setCarNumberEdit(entrySiteInfo.getCarNum());
        entryRecord.setParkNo(entrySiteInfo.getParkingLot());
        entryRecord.setDeviceId(entrySiteInfo.getSluiceDevChnId());
        entryRecord.setCarType(entrySiteInfo.getCarType());
        entryRecord.setCarNumberColor(entrySiteInfo.getCarNumColor());
        entryRecord.setPlatformCode(entrySiteInfo.getPlatFormCode());
        entryRecord.setCreateTime(new Date());
        entryRecord.setPassageId(entrySiteInfo.getPassageId());
        return entryRecord;
    }

    BPassageDeviceVo getPassageObj(String deviceId) {
        BPassageDevice bPassageDevice = new BPassageDevice();
        bPassageDevice.setDeviceId(deviceId);
        List<BPassageDeviceVo> bPassageDevices = ibPassageDeviceService.selectBPassageDeviceList(bPassageDevice);
        if (!CollectionUtils.isEmpty(bPassageDevices)) {
            return bPassageDevices.get(0);
        }
        return null;
    }


    TExitRecords initExitRecord(LeaveSiteInfo leaveSiteInfo) {
        TExitRecords exitRecords = new TExitRecords();
        exitRecords.setCarColor(leaveSiteInfo.getParkingCarColor());
        exitRecords.setCarImgUrl(leaveSiteInfo.getCarImgUrl());
        exitRecords.setDeviceId(leaveSiteInfo.getSluiceDevChnId());
        exitRecords.setParkNo(leaveSiteInfo.getParkingLot());
        exitRecords.setCarNumber(leaveSiteInfo.getCarNum());
        exitRecords.setCarType(leaveSiteInfo.getCarType());
        exitRecords.setPlatformCode(leaveSiteInfo.getPlatFormCode());
        //这个该车到闸口的时间，并不是真正离开的时间
        exitRecords.setExitTime(DateUtils.parseDate(leaveSiteInfo.getLeaveTime()));
        exitRecords.setCreateTime(new Date());
        BPassageDevice bPassageDevice = new BPassageDevice();
        bPassageDevice.setDeviceId(exitRecords.getDeviceId());
        exitRecords.setPassageId(leaveSiteInfo.getPassageId());
        return exitRecords;
    }


}
