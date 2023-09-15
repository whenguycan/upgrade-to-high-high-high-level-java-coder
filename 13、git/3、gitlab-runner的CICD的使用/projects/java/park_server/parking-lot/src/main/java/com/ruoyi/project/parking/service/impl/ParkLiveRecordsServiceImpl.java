package com.ruoyi.project.parking.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.parking.domain.BField;
import com.ruoyi.project.parking.domain.BPassage;
import com.ruoyi.project.parking.domain.TEntryRecords;
import com.ruoyi.project.parking.domain.TExitRecords;
import com.ruoyi.project.parking.domain.bo.ParkLiveRecordsBO;
import com.ruoyi.project.parking.domain.notification.CarEntryNotificationData;
import com.ruoyi.project.parking.domain.notification.CarExitNotificationData;
import com.ruoyi.project.parking.domain.param.ParkLiveRecordsAddParam;
import com.ruoyi.project.parking.domain.param.ParkLiveRecordsParam;
import com.ruoyi.project.parking.domain.vo.*;
import com.ruoyi.project.parking.domain.vo.parkingorder.*;
import com.ruoyi.project.parking.entity.ParkLiveRecords;
import com.ruoyi.project.parking.entity.TAbnormalOrder;
import com.ruoyi.project.parking.enums.PayMethodEnum;
import com.ruoyi.project.parking.enums.SettleTypeEnum;
import com.ruoyi.project.parking.mapper.ParkLiveRecordsMapper;
import com.ruoyi.project.parking.mq.model.CashPayNotificationData;
import com.ruoyi.project.parking.mq.model.PaymentData;
import com.ruoyi.project.parking.service.*;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import com.ruoyi.project.system.service.ISysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.ruoyi.common.constant.CacheConstants.PARKING_ORDER_EXPIRE_DURATION_MINUTES;
import static com.ruoyi.common.constant.Constants.LIVERECORD_MANUAL_EXIT_PASSAGE_ID;
import static com.ruoyi.common.constant.Constants.LIVERECORD_MANUAL_EXIT_PASSAGE_NAME;

/**
 * <p>
 * 在场记录表 服务实现类
 * </p>
 *
 * @author yinwen
 * @since 2023-02-22
 */
@Slf4j
@Service
public class ParkLiveRecordsServiceImpl extends ServiceImpl<ParkLiveRecordsMapper, ParkLiveRecords> implements IParkLiveRecordsService {

    @Autowired
    private ITEntryRecordsService tEntryRecordsService;

    @Autowired
    private ITExitRecordsService tExitRecordsService;

    @Autowired
    private ISettingCarTypeService settingCarTypeService;

    @Autowired
    private ParkingOrderGrpcServiceImpl parkingOrderGrpcService;

    @Autowired
    private BPassageService bPassageService;

    @Autowired
    private BFieldService bFieldService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RabbitmqService rabbitmqService;

    @Autowired
    ITAbnormalOrderService abnormalOrderService;

    // region 私有方法 封装

    // region 1基础数据相关 通道、区域

    /**
     * 查询 通道名称 通过 通道id
     *
     * @param passageId 通道id
     */
    private String selectPassageNameByPassageId(Integer passageId) {
        if (LIVERECORD_MANUAL_EXIT_PASSAGE_ID == passageId) {
            return LIVERECORD_MANUAL_EXIT_PASSAGE_NAME;
        }
        BPassage bPassage = bPassageService.queryById(passageId);
        return bPassage.getPassageName();
    }

    /**
     * 通过 在场id 查询 通道编号
     *
     * @param id 在场id
     */
    private String selectPassageNoByParkLiveId(Integer id) {
        List<TExitRecords> list = selectExitRecordsByParkLiveId(id);
        if (!list.isEmpty()) {
            BPassage bPassage = bPassageService.queryById(list.get(0).getPassageId());
            if (bPassage == null) {
                return null;
            }
            return bPassage.getPassageNo();
        } else {
            return null;
        }
    }

    /**
     * 查询 场地名称 通过 场地id
     *
     * @param fieldId 场地id
     */
    private String selectFieldNameByFieldId(Integer fieldId) {
        BField bField = bFieldService.queryById(fieldId);
        if(bField == null){
            return StringUtils.EMPTY;
        }
        return bField.getFieldName();
    }

    /**
     * 查询场地信息 通过通道id
     *
     * @param passageId 通道id
     * @return 通道指向区域信息 from to
     */
    private Pair<Pair<Integer, String>, Pair<Integer, String>> selectFiledNameByPassageId(Integer passageId) {
        BPassage bPassage = bPassageService.queryById(passageId);
        // 通过手动离场的岗亭信息
        if (LIVERECORD_MANUAL_EXIT_PASSAGE_ID == passageId) {
            bPassage = new BPassage();
            bPassage.setPassageName(LIVERECORD_MANUAL_EXIT_PASSAGE_NAME);
            bPassage.setId(passageId);
        }
        Pair<Integer, String> from = new ImmutablePair<>(bPassage.getFromFieldId(), selectFieldNameByFieldId(bPassage.getFromFieldId()));
        Pair<Integer, String> to = new ImmutablePair<>(bPassage.getToFieldId(), selectFieldNameByFieldId(bPassage.getToFieldId()));
        return new ImmutablePair<>(from, to);
    }

    // endregion

    // region 2进出场记录相关

    /**
     * 查询出场记录 时间倒序 通过在场id
     *
     * @param id 在场id
     */
    private List<TExitRecords> selectExitRecordsByParkLiveId(Integer id) {
        TExitRecordsVo paramExitRecords = new TExitRecordsVo();
        paramExitRecords.setParkLiveId(id);
        return tExitRecordsService.selectTExitRecordsList(paramExitRecords).stream().
                sorted(Comparator.comparing(TExitRecords::getCreateTime).reversed()).toList();
    }

    /**
     * 查询停车记录(进出场) 通过在场id
     *
     * @param id 在场id
     */
    private List<VehicleParkFieldRecordVO> selectParkRecordsByParkLiveId(Integer id) {
        List<VehicleParkFieldRecordVO> vehicleParkFieldRecordVOList = new ArrayList<>();
        // 进场记录
        TEntryRecordsVo paramEntryRecords = new TEntryRecordsVo();
        paramEntryRecords.setParkLiveId(id);
        List<VehicleParkFieldRecordVO> entryRecords = tEntryRecordsService.selectTEntryRecordsList(paramEntryRecords)
                .stream()
                .map(m -> {
                    VehicleParkFieldRecordVO vehicleParkFieldRecordVO = new VehicleParkFieldRecordVO();
                    Pair<Pair<Integer, String>, Pair<Integer, String>> pair = selectFiledNameByPassageId(m.getPassageId());
                    vehicleParkFieldRecordVO.setFieldIdFrom(pair.getLeft().getLeft());
                    vehicleParkFieldRecordVO.setFieldNameFrom(pair.getLeft().getRight());
                    vehicleParkFieldRecordVO.setFieldIdTo(pair.getRight().getLeft());
                    vehicleParkFieldRecordVO.setFieldNameTo(pair.getRight().getRight());
                    vehicleParkFieldRecordVO.setRecordTime(m.getEntryTime());
                    vehicleParkFieldRecordVO.setPassageNo(m.getParkNo());
                    vehicleParkFieldRecordVO.setPassageName(selectPassageNameByPassageId(m.getPassageId()));
                    vehicleParkFieldRecordVO.setCarType(m.getCarType());
                    vehicleParkFieldRecordVO.setCarTypeName(settingCarTypeService.queryTypeNameByPartNoAndCode(m.getParkNo(), m.getCarType()));
                    // 车牌号以edit尾缀为准
                    vehicleParkFieldRecordVO.setCarNumber(m.getCarNumberEdit());
                    vehicleParkFieldRecordVO.setCarImgUrl(m.getCarImgUrl());
                    return vehicleParkFieldRecordVO;
                }).toList();
        // 出场记录
        TExitRecordsVo paramExitRecords = new TExitRecordsVo();
        paramExitRecords.setParkLiveId(id);
        List<VehicleParkFieldRecordVO> exitRecords = tExitRecordsService.selectTExitRecordsList(paramExitRecords)
                .stream()
                .map(m -> {
                    VehicleParkFieldRecordVO vehicleParkFieldRecordVO = new VehicleParkFieldRecordVO();
                    Pair<Pair<Integer, String>, Pair<Integer, String>> pair = selectFiledNameByPassageId(m.getPassageId());
                    vehicleParkFieldRecordVO.setFieldIdFrom(pair.getLeft().getLeft());
                    vehicleParkFieldRecordVO.setFieldNameFrom(pair.getLeft().getRight());
                    vehicleParkFieldRecordVO.setFieldIdTo(pair.getRight().getLeft());
                    vehicleParkFieldRecordVO.setFieldNameTo(pair.getRight().getRight());
                    vehicleParkFieldRecordVO.setRecordTime(m.getExitTime());
                    vehicleParkFieldRecordVO.setPassageNo(m.getParkNo());
                    vehicleParkFieldRecordVO.setPassageName(selectPassageNameByPassageId(m.getPassageId()));
                    vehicleParkFieldRecordVO.setCarType(m.getCarType());
                    vehicleParkFieldRecordVO.setCarTypeName(settingCarTypeService.queryTypeNameByPartNoAndCode(m.getParkNo(), m.getCarType()));
                    vehicleParkFieldRecordVO.setCarNumber(m.getCarNumberEdit());
                    vehicleParkFieldRecordVO.setCarImgUrl(m.getCarImgUrl());
                    return vehicleParkFieldRecordVO;
                }).toList();
        vehicleParkFieldRecordVOList.addAll(entryRecords);
        vehicleParkFieldRecordVOList.addAll(exitRecords);
        return vehicleParkFieldRecordVOList.stream().sorted(Comparator.comparing(VehicleParkFieldRecordVO::getRecordTime)).toList();
    }

    // endregion

    // region 3订单相关

    /**
     * 查询 对应的订单号 通过在场记录id
     *
     * @param id 在场记录id
     */
    private List<VehicleParkOrderVO> queryParkingOrderVOById(Integer id) {
        List<String> orderNoList = baseMapper.selectOrderNoByParkLiveId(id)
                .stream().map(VehicleParkOrderVO::getOrderNo).toList();
        return parkingOrderGrpcService.queryParkingOrderListByOrderNoList(orderNoList);
    }

    /**
     * 计算经历过的场区详情 通过 在场车库id
     *
     * @param id         在场车库id
     * @param settleType 结算类型
     * @param recordTime 记录时间（当前时间）
     */
    private List<VehicleParkOrderItemVO> calculateParkFieldTime(Integer id, SettleTypeEnum settleType, LocalDateTime recordTime) {
        List<VehicleParkOrderItemVO> vehicleParkOrderItemVOList = new ArrayList<>();
        List<VehicleParkFieldRecordVO> vehicleParkFieldRecordVOList = selectParkRecordsByParkLiveId(id);
        // 转化为新List
        vehicleParkFieldRecordVOList = new ArrayList<>(vehicleParkFieldRecordVOList);
        if (settleType.equals(SettleTypeEnum.PREPAY_CODE_PAY)) {
            VehicleParkFieldRecordVO vehicleParkFieldRecordVO = new VehicleParkFieldRecordVO();
            vehicleParkFieldRecordVO.setRecordTime(recordTime);
            vehicleParkFieldRecordVOList.add(vehicleParkFieldRecordVO);
        } else if (settleType.equals(SettleTypeEnum.PAVILION_CODE_PAY)) {
            // 岗亭支付的时候 最后一次的出场记录 位于缓存中的预离场 加入计算
            VehicleParkFieldRecordVO vehicleParkFieldRecordVO = new VehicleParkFieldRecordVO();
            vehicleParkFieldRecordVO.setRecordTime(recordTime);
            vehicleParkFieldRecordVOList.add(vehicleParkFieldRecordVO);
        }
        VehicleParkFieldRecordVO previousNode = null;
        for (VehicleParkFieldRecordVO currentNode : vehicleParkFieldRecordVOList) {
            if (previousNode != null) {
                // 记录当前节点 from区域的时长
                VehicleParkOrderItemVO vehicleParkOrderItemVO = new VehicleParkOrderItemVO();
                vehicleParkOrderItemVO.setParkFieldId(previousNode.getFieldIdTo());
//                vehicleParkOrderItemVO.setFieldName(previousNode.getFieldNameTo());
                vehicleParkOrderItemVO.setEntryTime(DateUtils.toMilliString(previousNode.getRecordTime()));
                vehicleParkOrderItemVO.setExitTime(DateUtils.toMilliString(currentNode.getRecordTime()));
                vehicleParkOrderItemVOList.add(vehicleParkOrderItemVO);
            }
            // 记录上一个节点
            previousNode = currentNode;
        }

        return vehicleParkOrderItemVOList;
    }


    /**
     * 计算 已结算车辆 经历过的场区详情展示
     *
     * @param id 在场车库id
     * @return List<ParkRecordsAccordFieldInfoShowVO>
     */
    private ParkSettlementRecordsDetailVO calcParkFieldInfo(Integer id) {
        ParkSettlementRecordsDetailVO parkSettlementRecordsDetailVO = new ParkSettlementRecordsDetailVO();
        List<ParkSettlementRecordsFieldVO> vehicleParkOrderItemVOList = new ArrayList<>();
        List<VehicleParkFieldRecordVO> vehicleParkFieldRecordVOList = selectParkRecordsByParkLiveId(id);
        // 转化为新List
        vehicleParkFieldRecordVOList = new ArrayList<>(vehicleParkFieldRecordVOList);
        VehicleParkFieldRecordVO previousNode = null;
        for (VehicleParkFieldRecordVO currentNode : vehicleParkFieldRecordVOList) {
            if (previousNode != null) {
                // 记录当前节点 from区域的时长
                ParkSettlementRecordsFieldVO parkSettlementRecordsFieldVO = new ParkSettlementRecordsFieldVO();
                parkSettlementRecordsFieldVO.setParkFieldId(previousNode.getFieldIdTo());
                parkSettlementRecordsFieldVO.setParkFieldName(previousNode.getFieldNameTo());
                parkSettlementRecordsFieldVO.setEntryTime(previousNode.getRecordTime());
                parkSettlementRecordsFieldVO.setExitTime(currentNode.getRecordTime());
                parkSettlementRecordsFieldVO.setDurationTime(DateUtils.getDatePoor(previousNode.getRecordTime(), currentNode.getRecordTime()));
                parkSettlementRecordsFieldVO.setEntryPassageName(previousNode.getPassageName());
                parkSettlementRecordsFieldVO.setExitPassageName(currentNode.getPassageName());
                // 填充 车辆信息的时候 以一个节点车辆信息为准，因为手动离场的节点可能没有对应车辆信息
                parkSettlementRecordsFieldVO.setCarNumber(previousNode.getCarNumber());
                parkSettlementRecordsFieldVO.setCarType(previousNode.getCarType());
                parkSettlementRecordsFieldVO.setCarTypeName(previousNode.getCarTypeName());
                parkSettlementRecordsFieldVO.setImgUrl(previousNode.getCarImgUrl());
                vehicleParkOrderItemVOList.add(parkSettlementRecordsFieldVO);
            }
            // 记录上一个节点
            previousNode = currentNode;
        }

        parkSettlementRecordsDetailVO.setFieldInfo(vehicleParkOrderItemVOList);
        // 进出场图片
        if (!vehicleParkOrderItemVOList.isEmpty()) {
            parkSettlementRecordsDetailVO.setCarImgUrlFrom(vehicleParkOrderItemVOList.get(0).getImgUrl());
            parkSettlementRecordsDetailVO.setCarImgUrlTo(vehicleParkOrderItemVOList.get(vehicleParkOrderItemVOList.size() - 1).getImgUrl());
        }
        return parkSettlementRecordsDetailVO;
    }


    @Transactional
    public boolean insertOrderNoWithLiveId(Integer parkLiveId, List<VehicleParkOrderVO> list) {
        baseMapper.clearOrderNoWithLiveId(parkLiveId);
        list.forEach(i -> i.setParkLiveId(parkLiveId));
        return baseMapper.insertOrderNoWithLiveId(list) > 0;
    }

    // endregion

    // endregion

    public ParkLiveRecords queryByParkNoCarNumber(String parkNo, String carNumber) {
        // 存在多条在场记录时 取最新一条记录
        LambdaQueryWrapper<ParkLiveRecords> qw = new LambdaQueryWrapper<>();
        qw.eq(ParkLiveRecords::getParkNo, parkNo)
                .eq(ParkLiveRecords::getCarNumber, carNumber)
                .eq(ParkLiveRecords::getCarStatus, "1")
                .orderByDesc(ParkLiveRecords::getCreateTime)
                .last("limit 1 ");
        return getOne(qw);
    }

    public ParkLiveRecordsBO queryWithOrdersByParkNoCarNumber(String parkNo, String carNumber) {
        ParkLiveRecordsBO parkLiveRecordsBO = new ParkLiveRecordsBO();
        // 查询 在场记录 信息
        ParkLiveRecords parkLiveRecords = queryByParkNoCarNumber(parkNo, carNumber);
        if (parkLiveRecords == null) {
            throw new ServiceException("当前车牌号不在停车场内");
        }
        BeanUtils.copyBeanProp(parkLiveRecordsBO, parkLiveRecords);
        // 查询 订单信息
        parkLiveRecordsBO.setOrderList(queryParkingOrderVOById(parkLiveRecords.getId()));
        return parkLiveRecordsBO;
    }

    @Override
    public List<ParkLiveRecordsVO> selectParkLiveRecordsVOList(ParkLiveRecordsParam parkLiveRecordsParam) {
        // 查询在场车辆
        parkLiveRecordsParam.setCarStatus("1");
        parkLiveRecordsParam.setOrderByType("1");
        return baseMapper.selectParkLiveRecordsVOList(parkLiveRecordsParam);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer insertParkLiveRecords(String parkNo, String carNumber, String carType, LocalDateTime entryTime) {
        // 发送通知 - 进场通知
        CarEntryNotificationData carEntryNotificationData = new CarEntryNotificationData();
        carEntryNotificationData.setParkNo(parkNo);
        carEntryNotificationData.setParkName(deptService.selectParkNameByParkNo(parkNo));
        carEntryNotificationData.setCarNumber(carNumber);
        carEntryNotificationData.setEntryTime(entryTime);
        rabbitmqService.pushCarEntryNotification(carEntryNotificationData);

        // 校验 在场记录存在
        ParkLiveRecords liveRecordsDb = queryByParkNoCarNumber(parkNo, carNumber);
        if (liveRecordsDb == null) {
            ParkLiveRecords parkLiveRecords = new ParkLiveRecords();
            parkLiveRecords.setParkNo(parkNo);
            parkLiveRecords.setCarNumber(carNumber);
            parkLiveRecords.setCarType(carType);
            parkLiveRecords.setEntryTime(entryTime);
            parkLiveRecords.setCarStatus("1");
            parkLiveRecords.setCreateTime(LocalDateTime.now());
            save(parkLiveRecords);
            return parkLiveRecords.getId();
        }
        return liveRecordsDb.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertParkLiveRecordsWithExit(String parkNo, String carNumber, String carType, LocalDateTime exitTime) {
        // 校验 在场记录存在
        ParkLiveRecords liveRecordsDb = queryByParkNoCarNumber(parkNo, carNumber);
        if (liveRecordsDb == null) {
            ParkLiveRecords parkLiveRecords = new ParkLiveRecords();
            parkLiveRecords.setParkNo(parkNo);
            parkLiveRecords.setCarNumber(carNumber);
            parkLiveRecords.setCarType(carType);
            parkLiveRecords.setExitTime(exitTime);
            parkLiveRecords.setCarStatus("1");
            parkLiveRecords.setCreateTime(LocalDateTime.now());
            save(parkLiveRecords);
            return parkLiveRecords.getId();
        }
        return liveRecordsDb.getId();
    }

    @Override
    public boolean updateEntryTimeById(Integer id, LocalDateTime entryTime) {
        LambdaUpdateWrapper<ParkLiveRecords> uw = new LambdaUpdateWrapper<>();
        uw.set(ParkLiveRecords::getEntryTime, entryTime)
                .set(ParkLiveRecords::getUpdateTime, LocalDateTime.now())
                .eq(ParkLiveRecords::getId, id);
        return update(uw);
    }

    @Override
    public boolean updateExitTimeById(Integer id, LocalDateTime exitTime) {
        LambdaUpdateWrapper<ParkLiveRecords> uw = new LambdaUpdateWrapper<>();
        uw.set(ParkLiveRecords::getExitTime, exitTime)
                .set(ParkLiveRecords::getUpdateTime, LocalDateTime.now())
                .eq(ParkLiveRecords::getId, id);
        return update(uw);
    }

    @Override
    public boolean updatePayTimeById(Integer id, LocalDateTime payTime) {
        LambdaUpdateWrapper<ParkLiveRecords> uw = new LambdaUpdateWrapper<>();
        uw.set(ParkLiveRecords::getPayTime, payTime)
                .set(ParkLiveRecords::getUpdateTime, LocalDateTime.now())
                .eq(ParkLiveRecords::getId, id);
        return update(uw);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean departureByParkNoCarNumber(String parkNo, String carNumber, LocalDateTime exitTime) {
        CarExitNotificationData carExitNotificationData = new CarExitNotificationData();
        carExitNotificationData.setParkNo(parkNo);
        carExitNotificationData.setCarNumber(carNumber);
        carExitNotificationData.setExitTime(exitTime);

        // 从缓存中读取应付金额
        String parkOrderKey = CacheConstants.PARKING_ORDER + parkNo + "_" + carNumber;
        VehicleParkOrderVO vehicleParkOrderVO = redisCache.getCacheObject(parkOrderKey);
        if (vehicleParkOrderVO != null) {
            carExitNotificationData.setParkName(vehicleParkOrderVO.getParkName());
            carExitNotificationData.setEntryTime(vehicleParkOrderVO.getEntryTime());
            carExitNotificationData.setAmount(BigDecimal.valueOf(vehicleParkOrderVO.getPayableAmount()));
        } else {
            carExitNotificationData.setParkName(deptService.selectParkNameByParkNo(parkNo));
            ParkLiveRecords parkLiveRecordsWillLeave = queryByParkNoCarNumber(parkNo, carNumber);
            carExitNotificationData.setEntryTime(parkLiveRecordsWillLeave.getEntryTime());
        }

        // 出场变更状态
        LambdaUpdateWrapper<ParkLiveRecords> uw = new LambdaUpdateWrapper<>();
        uw.set(ParkLiveRecords::getCarStatus, "2")
                .set(ParkLiveRecords::getUpdateTime, LocalDateTime.now())
                .set(ParkLiveRecords::getExitTime, exitTime)
                .eq(ParkLiveRecords::getParkNo, parkNo)
                .eq(ParkLiveRecords::getCarNumber, carNumber)
                .eq(ParkLiveRecords::getCarStatus, "1");
        // 发送通知 - 出场通知
        rabbitmqService.pushCarExitNotification(carExitNotificationData);
        return update(uw);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateEntryTimeByParkNoCarNumber(LocalDateTime entryTime, String parkNo, String carNumber) {
        ParkLiveRecords parkLiveRecords = queryByParkNoCarNumber(parkNo, carNumber);
        if (parkLiveRecords == null) {
            // 不存在 在场记录
            return false;
        }
        LambdaUpdateWrapper<ParkLiveRecords> uw = new LambdaUpdateWrapper<>();
        uw.set(entryTime != null, ParkLiveRecords::getEntryTime, entryTime)
                .set(ParkLiveRecords::getUpdateTime, LocalDateTime.now())
                .eq(ParkLiveRecords::getId, parkLiveRecords.getId());
        return update(uw);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateEntryCarNumberByParkNoCarNumber(String carNumberNew, String parkNo, String carNumberEntry) {
        if (StringUtils.isEmpty(carNumberNew)) {
            throw new ServiceException("车牌号不为空");
        }
        // 检查是否存在 新车牌记录
        ParkLiveRecords parkLiveRecordsEntry = queryByParkNoCarNumber(parkNo, carNumberEntry);
        ParkLiveRecords parkLiveRecordsNew = queryByParkNoCarNumber(parkNo, carNumberNew);
        if (parkLiveRecordsEntry == null) {
            // 不存在 在场记录
            throw new ServiceException("车辆:" + carNumberEntry + "未在停车场内");
        }
        LambdaUpdateWrapper<ParkLiveRecords> uw = new LambdaUpdateWrapper<>();
        uw.set(ParkLiveRecords::getCarNumber, carNumberNew)
                .set(ParkLiveRecords::getUpdateTime, LocalDateTime.now())
                .eq(ParkLiveRecords::getId, parkLiveRecordsEntry.getId());
        if (parkLiveRecordsNew == null) {
            // 一条在场记录 更新 入场信息时加入的 车牌号(新)
            return update(uw);
        } else {
            // 二条在场记录 合并 出场时间 至 入场时插入的在场记录 ；删除 出场时 插入的在场记录
            uw.set(ParkLiveRecords::getExitTime, parkLiveRecordsNew.getExitTime());
            update(uw);
            return removeById(parkLiveRecordsNew.getId());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateExitCarNumberByParkNoCarNumber(String carNumberNew, String parkNo, String carNumberExit) {
        if (StringUtils.isEmpty(carNumberNew)) {
            throw new ServiceException("车牌号不为空");
        }
        // 检查是否存在 新车牌记录
        ParkLiveRecords parkLiveRecordsExit = queryByParkNoCarNumber(parkNo, carNumberExit);
        ParkLiveRecords parkLiveRecordsNew = queryByParkNoCarNumber(parkNo, carNumberNew);
        if (parkLiveRecordsExit == null) {
            // 不存在 在场记录
            throw new ServiceException("车辆:" + carNumberExit + "未在停车场内");
        }
        LambdaUpdateWrapper<ParkLiveRecords> uw = new LambdaUpdateWrapper<>();
        uw.set(ParkLiveRecords::getCarNumber, carNumberNew)
                .set(ParkLiveRecords::getUpdateTime, LocalDateTime.now())
                .eq(ParkLiveRecords::getId, parkLiveRecordsExit.getId());
        if (parkLiveRecordsNew == null) {
            // 一条在场记录 更新 出场信息时加入的 车牌号(新)
            return update(uw);
        } else {
            // 二条在场记录 合并 进场时间 至 出场时插入的在场记录 ；删除 进场时插入的在场记录
            uw.set(ParkLiveRecords::getEntryTime, parkLiveRecordsNew.getEntryTime());
            update(uw);
            return removeById(parkLiveRecordsNew.getId());
        }

    }

    @Transactional
    @Override
    public boolean updateMemberIdByParkNoCarNumber(String memberId, String parkNo, String carNumber) {
        LambdaUpdateWrapper<ParkLiveRecords> uw = new LambdaUpdateWrapper<>();
        uw.set(ParkLiveRecords::getMemberId, memberId)
                .set(ParkLiveRecords::getUpdateTime, LocalDateTime.now())
                .eq(ParkLiveRecords::getParkNo, parkNo)
                .eq(ParkLiveRecords::getCarNumber, carNumber)
                .eq(ParkLiveRecords::getCarStatus, "1");
        return update(uw);
    }

    @Override
    public boolean removeParkLiveRecordsById(Integer id) {
        // 检查是否 为在场记录
        ParkLiveRecords parkLiveRecords = getById(id);
        if (parkLiveRecords == null) {
            return true;
        }
        if (!"1".equals(parkLiveRecords.getCarStatus())) {
            throw new ServiceException("当前停车记录车辆不在场!");
        }
        return removeById(id);
    }

    /**
     * 构建创建订单参数
     */
    @Override
    public VehicleParkCreateOrderRequestVO buildCreateOrderRequest(SettleTypeEnum settleType, String parkNo, String carNumber, String passageNo, String discountReason) {
        ParkLiveRecords parkLiveRecords = queryByParkNoCarNumber(parkNo, carNumber);
        if (parkLiveRecords == null) {
            throw new ServiceException("当前车辆" + carNumber + "不在停车场内！");
        }
        VehicleParkCreateOrderRequestVO param = new VehicleParkCreateOrderRequestVO();
        param.setParkNo(parkNo);
        param.setCarNumber(carNumber);
        param.setPassageNo(passageNo);
        param.setEntryTime(DateUtils.toMilliString(parkLiveRecords.getEntryTime()));
        // 计费时间 获取当前时间，此时的离场时间未更新
        LocalDateTime calcDate = LocalDateTime.now();
        param.setExitTime(DateUtils.toMilliString(calcDate));
        param.setCarTypeCode(parkLiveRecords.getCarType());
        param.setItemList(calculateParkFieldTime(parkLiveRecords.getId(), settleType, calcDate));
        param.setDiscountReason(discountReason);
        return param;
    }


    /**
     * 创建订单 - 常州智慧停车订单
     *
     * @param parkNo     停车场编号
     * @param carNumber  车牌号
     * @param settleTime 结算时间
     */
    public VehicleParkOrderVO createParkingGeneralOrderForCZZHTC(String parkNo, String carNumber, LocalDateTime settleTime) {
        // todo 常州智慧停车接入
        return null;
    }

    /**
     * 创建订单
     */
    @Override
    public Pair<VehicleParkOrderVO, List<ParkingOrder.CouponInfo>> createParkingGeneralOrder(SettleTypeEnum settleType, String parkNo, String carNumber, String passageNo) {
        VehicleParkCreateOrderRequestVO param = null;
        try {
            param = buildCreateOrderRequest(settleType, parkNo, carNumber, passageNo, StringUtils.EMPTY);
        } catch (NullPointerException e) {
            AsyncManager.me().execute(new TimerTask() {
                @Override
                public void run() {
                    //保存异常记录
                    TAbnormalOrder tAbnormalOrder = new TAbnormalOrder();
                    tAbnormalOrder.setCarNumber(carNumber);
                    //停车订单
                    tAbnormalOrder.setOrderType(1);
                    tAbnormalOrder.setAbnormalType(Integer.parseInt(DictUtils.getDictValue("abnormal_type", "未找到在场记录")));
                    tAbnormalOrder.setAbnormalReason("未找到在场记录");
                    tAbnormalOrder.setParkNo(parkNo);
                    tAbnormalOrder.setCreateTime(LocalDateTime.now());
                    abnormalOrderService.save(tAbnormalOrder);
                }
            });
            throw new ServiceException("异常:闸道订单出现不创建订单情况");
        }
        // 手动订单不创建停车订单
        if (SettleTypeEnum.MANUAL_PAY.getValue() == settleType.getValue()) {
            log.error("当前接口不支持手动结算");
            return null;
        }
        // 调用订单系统服务 创建订单
        Triple<Boolean, VehicleParkOrderVO, List<ParkingOrder.CouponInfo>> triple = parkingOrderGrpcService.createParkingOrderGrpc(param);
        if (!triple.getLeft()) {
            throw new ServiceException("异常:闸道订单出现不创建订单情况");
        }
        VehicleParkOrderVO vehicleParkOrderVO = triple.getMiddle();
        log.info("创建岗亭订单{}", JSONObject.toJSONString(vehicleParkOrderVO));
        if (vehicleParkOrderVO == null) {
            log.error("创建订单失败");
            return null;
        }
        vehicleParkOrderVO.setParkName(deptService.selectParkNameByParkNo(parkNo));
        // 这边是岗亭调用 没有会员id 无需更新
        return new ImmutablePair<>(vehicleParkOrderVO, triple.getRight());
    }

    @Override
    public void freeOpenGate(String parkNo, String passageNo, String CarNumber, String orderNo, String freeReason) {
        CashPayNotificationData cashPayNotificationData = new CashPayNotificationData();
        // 停车订单号
        cashPayNotificationData.setOutTradeNo(orderNo);
        cashPayNotificationData.setTradeNo(orderNo);
        cashPayNotificationData.setTradeStatus("03");
        // 支付时间格式 yyyy-MM-dd HH:mm:ss
        cashPayNotificationData.setPayTime(DateUtils.getTime());
        cashPayNotificationData.setPayMethod(PayMethodEnum.CASH_PAY.getValue());
        cashPayNotificationData.setFreeReason(freeReason);
        rabbitmqService.pushCashPayToOrder(cashPayNotificationData);

        // 推送至闸道
        PaymentData paymentData = new PaymentData();
        paymentData.setCarNumber(CarNumber);
        // 通道信息由前端传
        paymentData.setPassageNo(passageNo);
        paymentData.setParkNo(parkNo);
        paymentData.setPayAmount(BigDecimal.valueOf(0));
        paymentData.setOutTradeNo(orderNo);
        rabbitmqService.pushOrderCompleteToDevice(paymentData);
    }


    private void pushCashPayToOrder(String orderNo, Double manualAmount) {
        CashPayNotificationData cashPayNotificationData = new CashPayNotificationData();
        // 停车订单号
        cashPayNotificationData.setOutTradeNo(orderNo);
        cashPayNotificationData.setTradeNo(orderNo);
        cashPayNotificationData.setTotalAmount(BigDecimal.valueOf(manualAmount));
        cashPayNotificationData.setTradeStatus("03");
        // 支付时间格式 yyyy-MM-dd HH:mm:ss
        cashPayNotificationData.setPayTime(DateUtils.getTime());
        cashPayNotificationData.setPayMethod(PayMethodEnum.CASH_PAY.getValue());
        rabbitmqService.pushCashPayToOrder(cashPayNotificationData);
    }

    @Override
    public void chargeOpenGate(String parkNo, String passageNo, String CarNumber, String orderNo, Double manualAmount) {
        pushCashPayToOrder(orderNo, manualAmount);
        // 推送至闸道
        PaymentData paymentData = new PaymentData();
        paymentData.setCarNumber(CarNumber);
        // 通道信息由前端传
        paymentData.setPassageNo(passageNo);
        paymentData.setParkNo(parkNo);
        paymentData.setPayAmount(BigDecimal.valueOf(manualAmount));
        paymentData.setOutTradeNo(orderNo);
        rabbitmqService.pushOrderCompleteToDevice(paymentData);
    }

    @Override
    public VehicleParkOrderVO queryParkingOrderByOrderNo(String orderNo) {
        return parkingOrderGrpcService.queryParkingOrderByOrderNo(orderNo);
    }

    @Override
    public List<ParkLiveRecordsVO> selectParkSettlementRecordsVOList(ParkLiveRecordsParam parkLiveRecordsParam) {
        // 查询离场车辆
        parkLiveRecordsParam.setCarStatus("2");
        parkLiveRecordsParam.setOrderByType("2");
        return baseMapper.selectParkLiveRecordsVOList(parkLiveRecordsParam);
    }

    @Override
    public Pair<List<ParkSettlementRecordsVO>, Long> selectParkSettlementRecordsVOPage(ParkLiveRecordsParam parkLiveRecordsParam) {
        // 查询离场车辆
        parkLiveRecordsParam.setCarStatus("2");
        parkLiveRecordsParam.setOrderByType("2");
        List<ParkLiveRecordsVO> list = baseMapper.selectParkLiveRecordsVOList(parkLiveRecordsParam);
        return new ImmutablePair<>(list.stream().map(
                m -> {
                    ParkSettlementRecordsVO parkSettlementRecordsVO = new ParkSettlementRecordsVO();
                    BeanUtils.copyBeanProp(parkSettlementRecordsVO, m);
                    return parkSettlementRecordsVO;
                }
        ).toList(), new PageInfo(list).getTotal());
    }

    @Override
    public boolean completeOrder(String orderNo) {
        return parkingOrderGrpcService.updateParkingOrderStatus(orderNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editEntryTimeBatch(List<Integer> liveIdList, LocalDateTime entryTime) {
        LambdaUpdateWrapper<ParkLiveRecords> uw = new LambdaUpdateWrapper<>();
        uw.set(ParkLiveRecords::getEntryTime, entryTime)
                .set(ParkLiveRecords::getUpdateTime, LocalDateTime.now())
                .set(ParkLiveRecords::getUpdateBy, SecurityUtils.getUsername())
                .in(ParkLiveRecords::getId, liveIdList);
        // 同步更新 入场记录表数据的首条 入场时间
        for (Integer liveId : liveIdList) {
            List<TEntryRecords> list = tEntryRecordsService.selectEntryRecordsByParkLiveId(liveId);
            if (!list.isEmpty()) {
                TEntryRecords headRecords = list.get(0);
                TEntryRecords upateParam = new TEntryRecords();
                upateParam.setId(headRecords.getId());
                upateParam.setEntryTime(entryTime);
                tEntryRecordsService.updateTEntryRecords(upateParam);
            }
        }
        return update(uw);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean manualComputationBatch(List<Integer> liveIdList, Double payAmount, String liftGateReason) {
        LocalDateTime now = LocalDateTime.now();
        StopWatch stopWatch = new StopWatch();
        for (Integer liveId : liveIdList) {
            ParkLiveRecords record = getById(liveId);
            stopWatch = new StopWatch();
            stopWatch.start();
            // 创建离场订单
            record.setExitTime(now);
            createParkingManualOrder(record, payAmount, liftGateReason);
            stopWatch.stop();
            log.info("创建手动离场订单  执行{} ms", stopWatch.getTotalTimeMillis());
            // 创建离场记录
            stopWatch = new StopWatch();
            stopWatch.start();
            TExitRecords tExitRecords = new TExitRecords();
            tExitRecords.setExitTime(now);
            tExitRecords.setParkNo(record.getParkNo());
            // 离场通道id 固定值
            tExitRecords.setPassageId(LIVERECORD_MANUAL_EXIT_PASSAGE_ID);
            tExitRecords.setCarNumber(record.getCarNumber());
            tExitRecords.setCarNumberEdit(record.getCarNumber());
            tExitRecords.setParkLiveId(liveId);
            tExitRecords.setRemark("手动结算离场");
            tExitRecordsService.insertTExitRecords(tExitRecords);
            stopWatch.stop();
            log.info("插入离场订单 执行{} ms", stopWatch.getTotalTimeMillis());
        }
        // 更新在场记录信息
        LambdaUpdateWrapper<ParkLiveRecords> uw = new LambdaUpdateWrapper<>();
        uw.set(ParkLiveRecords::getCarStatus, "2")
                .set(ParkLiveRecords::getManualFlag, 1)
                .set(ParkLiveRecords::getManualAmount, payAmount)
                .set(ParkLiveRecords::getManualReason, liftGateReason)
                .set(ParkLiveRecords::getExitTime, now)
                .set(ParkLiveRecords::getUpdateTime, now)
                .set(ParkLiveRecords::getUpdateBy, SecurityUtils.getUsername())
                .in(ParkLiveRecords::getId, liveIdList);
        return update(uw);
    }

    /**
     * 创建离场订单（手动结算订单）
     *
     * @param parkLiveRecords 在场记录
     * @param manualAmount    手动结算金额
     * @param manualReason    手动结算
     */
    private void createParkingManualOrder(ParkLiveRecords parkLiveRecords, Double manualAmount, String manualReason) {
        ManualParkingOrderRequestVO param = new ManualParkingOrderRequestVO();
        param.setParkNo(parkLiveRecords.getParkNo());
        param.setCarNumber(parkLiveRecords.getCarNumber());
        param.setEntryTime(DateUtils.toMilliString(parkLiveRecords.getEntryTime()));
        param.setExitTime(DateUtils.toMilliString(parkLiveRecords.getExitTime()));
        param.setCarTypeCode(parkLiveRecords.getCarType());
        param.setManualAmount(manualAmount);
        param.setManualReason(manualReason);
        // 调用订单系统服务 创建手动结算订单
        log.info("调用 createManualParkingOrderGrpc");
        String manualOrderNo = parkingOrderGrpcService.createManualParkingOrderGrpc(param);
        if (StringUtils.isEmpty(manualOrderNo)) {
            throw new ServiceException("创建手动结算订单失败");
        }
        log.info("推送 pushCashPayToOrder");
        // 创建手动订单后，自动推送支付成功
        pushCashPayToOrder(manualOrderNo, manualAmount);
    }

    @Override
    public VehicleParkOrderVO queryOrCreatePrepayOrderByParkNoCarNumber(String parkNo, String carNumber) {
//        String parkOrderKey = CacheConstants.PARKING_ORDER + parkNo + "_" + carNumber;
//        // 从redis 中查询 订单，没有则新建
//        String key = CacheConstants.ADVANCE_PAYMENT + carNumber;
//        String orderNo = redisCache.getCacheObject(key);
//        if (StringUtils.isEmpty(orderNo)) {
//            VehicleParkOrderVO vehicleParkOrderVO = createParkingGeneralOrder(SettleTypeEnum.PREPAY_CODE_PAY, parkNo, carNumber, null);
//            // 缓存预支付订单 订单号
//            redisCache.setCacheObject(key, vehicleParkOrderVO.getOrderNo(), 30, TimeUnit.MINUTES);
//            // 缓存预支付订单信息
//            redisCache.setCacheObject(parkOrderKey, vehicleParkOrderVO, 30, TimeUnit.MINUTES);
//            return vehicleParkOrderVO;
//        }
//        // 预支付订单每次 都去查询新的订单
//        return queryParkingOrderByOrderNo(orderNo);
        // 预支付不走这边逻辑，走会员模块
        return null;
    }

    public VehicleParkOrderVO queryOrCreatePavilionOrderByParkNoPassageNo(String parkNo, String passageNo) {
        // 仅查询 岗亭创建的订单，不查询 预支付订单信息
        // 岗亭中的车牌号 key
        String carNumberKey = CacheConstants.PARKNO_PASSAGE_KEY +
                parkNo +
                "_" +
                passageNo;
        String carNumber = redisCache.getCacheObject(carNumberKey);
        if (StringUtils.isEmpty(carNumber)) {
            log.error("当前车辆不在车场通道内");
            return null;
        } else {
            // 停车场 岗亭订单编号 key
            String parkPavilionOrderKey = CacheConstants.PARKING_ORDER + parkNo + "_" + carNumber;
            String parkPavilionOrderCouponKey = parkPavilionOrderKey + "_coupon";
            VehicleParkOrderVO vehicleParkOrderVO = redisCache.getCacheObject(parkPavilionOrderKey);
            if (vehicleParkOrderVO == null) {
                // 创建岗亭订单
                Pair<VehicleParkOrderVO, List<ParkingOrder.CouponInfo>> pair = createParkingGeneralOrder(SettleTypeEnum.PAVILION_CODE_PAY, parkNo, carNumber, passageNo);
                vehicleParkOrderVO = pair.getLeft();
                // 缓存岗亭订单信息
                redisCache.setCacheObject(parkPavilionOrderKey, vehicleParkOrderVO, PARKING_ORDER_EXPIRE_DURATION_MINUTES, TimeUnit.MINUTES);
                List<ParkingOrderCouponVO> couponVOList = pair.getRight().stream().map(m -> {
                    try {
                        return ProtoJsonUtil.toPojoBean(ParkingOrderCouponVO.class, m);
                    } catch (IOException e) {
                        log.error("解析失败!");
                    }
                    return null;
                }).toList();
                redisCache.setCacheObject(parkPavilionOrderCouponKey, couponVOList, PARKING_ORDER_EXPIRE_DURATION_MINUTES, TimeUnit.MINUTES);
                log.info("创建岗亭订单并缓存:{}", JSONObject.toJSONString(vehicleParkOrderVO));
            }
            return vehicleParkOrderVO;
        }
    }


    @Override
    public ParkSettlementRecordsDetailVO queryParkSettlementRecordsVOById(Integer id) {
        ParkSettlementRecordsDetailVO parkSettlementRecordsDetailVO = calcParkFieldInfo(id);
        parkSettlementRecordsDetailVO.setParkingOrderInfo(queryParkingOrderVOById(id));
        return parkSettlementRecordsDetailVO;
    }


    @Override
    public boolean addSettlementRecords(ParkLiveRecordsAddParam param) {
        ParkLiveRecords settlementRecords = new ParkLiveRecords();
        BeanUtils.copyBeanProp(settlementRecords, param);
        settlementRecords.setCarStatus("2");
        settlementRecords.setCreateTime(LocalDateTime.now());
        settlementRecords.setCreateBy(SecurityUtils.getUsername());
        return save(settlementRecords);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean importSettlementRecords(List<ParkLiveRecordsAddParam> list) {
        if (list.isEmpty()) {
            throw new ServiceException("请导入非空文件!");
        }
        LocalDateTime createTime = LocalDateTime.now();
        for (ParkLiveRecordsAddParam record : list) {
            ParkLiveRecords settlementRecords = new ParkLiveRecords();
            BeanUtils.copyBeanProp(settlementRecords, record);
            settlementRecords.setCarStatus("2");
            settlementRecords.setCreateTime(createTime);
            settlementRecords.setCreateBy(SecurityUtils.getUsername());
            save(settlementRecords);
        }
        return true;
    }

    @Override
    public boolean updateSettlementRecordsCarNumberById(Integer id, String carNumber) {
        LambdaUpdateWrapper<ParkLiveRecords> uw = new LambdaUpdateWrapper<>();
        uw.set(ParkLiveRecords::getCarNumber, carNumber)
                .set(ParkLiveRecords::getUpdateTime, LocalDateTime.now())
                .eq(ParkLiveRecords::getCarStatus, "2")
                .eq(ParkLiveRecords::getId, id);
        return update(uw);
    }

    @Override
    public boolean removeSettlementRecordsById(Integer id) {
        // 检查是否 为离场记录
        ParkLiveRecords settlementRecords = getById(id);
        if (settlementRecords == null) {
            return true;
        }
        if (!"2".equals(settlementRecords.getCarStatus())) {
            throw new ServiceException("当前停车记录车辆未离场!");
        }
        return removeById(id);
    }

    @Override
    public Pair<List<HistoryParkingOrderVO>, Long> queryHistoryParkingOrderByMemberIdCarNumber(String memberId, String carNumber, Integer pageNum, Integer pageSize) {
        ParkLiveRecordsParam param = new ParkLiveRecordsParam();
        param.setMemberId(memberId);
        param.setCarNumber(carNumber);
        PageHelper.startPage(pageNum, pageSize, "");
        List<HistoryParkingOrderVO> list = baseMapper.selectHistoryParkingOrderVO(param);
        for (HistoryParkingOrderVO m : list) {
            m.setDurationTime(DateUtils.getDatePoor(m.getEntryTime(), m.getExitTime()));
        }
        return new ImmutablePair<>(list, new PageInfo(list).getTotal());
    }

    @Override
    public ParkLiveRecords queryParkLiveRecordsByOrderNo(String orderNo) {
        return baseMapper.queryParkLiveRecordsByOrderNo(orderNo);
    }

    @Override
    public boolean updareParkLiveParkingOrderByOrderNo(Double payableAmount, Double payAmount, Double discountAmount, String orderNo) {
        return baseMapper.updareParkLiveParkingOrderByOrderNo(payableAmount, payAmount, discountAmount, orderNo) > 0;
    }

    @Override
    public List<String> queryHistoryCarNumberByMemberId(String memberId) {
        QueryWrapper<ParkLiveRecords> qw = new QueryWrapper<>();
        qw.select("DISTINCT car_number")
                .lambda()
                .eq(ParkLiveRecords::getMemberId, memberId);
        return list(qw).stream().map(ParkLiveRecords::getCarNumber).toList();
    }
}
