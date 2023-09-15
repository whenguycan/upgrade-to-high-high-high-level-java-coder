package com.ruoyi.project.parking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.ruoyi.project.parking.domain.bo.ParkLiveRecordsBO;
import com.ruoyi.project.parking.domain.param.ParkLiveRecordsAddParam;
import com.ruoyi.project.parking.domain.param.ParkLiveRecordsParam;
import com.ruoyi.project.parking.domain.vo.ParkLiveRecordsVO;
import com.ruoyi.project.parking.domain.vo.ParkSettlementRecordsDetailVO;
import com.ruoyi.project.parking.domain.vo.ParkSettlementRecordsVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.HistoryParkingOrderVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkCreateOrderRequestVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkOrderVO;
import com.ruoyi.project.parking.entity.ParkLiveRecords;
import com.ruoyi.project.parking.enums.SettleTypeEnum;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 在场记录表 服务类
 * </p>
 *
 * @author yinwen
 * @since 2023-02-22
 */
public interface IParkLiveRecordsService extends IService<ParkLiveRecords> {

    // region 在场记录 操作： 新增 查询 更新 删除

    /**
     * 查询 唯一在场记录 通过 场库编号 车牌号
     *
     * @param parkNo    场库编号
     * @param carNumber 车牌号
     * @return 在场记录
     */
    ParkLiveRecords queryByParkNoCarNumber(String parkNo, String carNumber);

    /**
     * 查询 在场记录信息 带订单信息 通过 场库编号 车牌号
     *
     * @param parkNo    场库编号
     * @param carNumber 车牌号
     * @return 在场记录 带订单信息
     */
    ParkLiveRecordsBO queryWithOrdersByParkNoCarNumber(String parkNo, String carNumber);

    /**
     * 查询 在场订单 记录列表
     *
     * @param parkLiveRecordsParam 查询条件
     * @return 在场订单记录列表
     */
    List<ParkLiveRecordsVO> selectParkLiveRecordsVOList(ParkLiveRecordsParam parkLiveRecordsParam);

    /**
     * 车辆进场 新增 在场记录 并 发送进场通知
     * 如果当前车辆已经在场状态 则 不新增 并返回在场记录id
     *
     * @param parkNo    场库编号
     * @param carNumber 车牌号
     * @param carType   车型
     * @param entryTime 进场时间
     * @return 在场记录id
     */
    Integer insertParkLiveRecords(String parkNo, String carNumber, String carType, LocalDateTime entryTime);


    /**
     * 车辆进场 新增 在场记录
     * 如果当前车辆已经在场状态 则 不新增 并返回在场记录id
     *
     * @param parkNo    场库编号
     * @param carNumber 车牌号
     * @param carType   车型
     * @param exitTime 出场时间
     * @return 在场记录id
     */
    Integer insertParkLiveRecordsWithExit(String parkNo, String carNumber, String carType, LocalDateTime exitTime);


    /**
     * 更新 在场记录 进场时间
     *
     * @param id        在场记录id
     * @param entryTime 进场时间
     */
    boolean updateEntryTimeById(Integer id, LocalDateTime entryTime);

    /**
     * 更新 在场记录 出场时间
     *
     * @param id       在场记录id
     * @param exitTime 出场时间
     */
    boolean updateExitTimeById(Integer id, LocalDateTime exitTime);

    /**
     * 更新 在场记录 缴费时间
     *
     * @param id      在场记录id
     * @param payTime 缴费时间
     */
    boolean updatePayTimeById(Integer id, LocalDateTime payTime);

    /**
     * 车辆离场 在场记录 更新状态 并 发送离场通知
     * 更新当前时间为离场时间
     *
     * @param parkNo    场库编号
     * @param carNumber 车牌号
     * @param exitTime  离场时间
     */
    boolean departureByParkNoCarNumber(String parkNo, String carNumber, LocalDateTime exitTime);

    /**
     * 更新 入场时间 通过 车场编号 车牌号
     *
     * @param entryTime 入场时间
     * @param parkNo    车场编号
     * @param carNumber 车牌号
     */
    boolean updateEntryTimeByParkNoCarNumber(LocalDateTime entryTime, String parkNo, String carNumber);


    /**
     * 更新 车牌号(新) 通过 车场编号 车牌号(旧)
     * 修改入场车牌信息
     * 1. 新车牌无在场记录，直接更新车牌信息
     * 2. 新车牌有在场记录，合并信息; 删除新车牌信息，合并新车牌的出场信息至原先信息
     *
     * @param carNumberNew   车牌号(新)
     * @param parkNo         车场编号
     * @param carNumberEntry 车牌号(原先进场识别出的车牌)
     */
    boolean updateEntryCarNumberByParkNoCarNumber(String carNumberNew, String parkNo, String carNumberEntry);

    /**
     * 更新 车牌号(新) 通过 车场编号 车牌号(旧)
     * 修改出场车牌信息
     * 1. 新车牌无在场记录，直接更新车牌信息
     * 2. 新车牌有在场记录，合并信息; 删除新车牌信息，合并新车牌的出场信息至原先信息
     *
     * @param carNumberNew  车牌号(新)
     * @param parkNo        车场编号
     * @param carNumberExit 车牌号(出场识别出的车牌)
     */
    boolean updateExitCarNumberByParkNoCarNumber(String carNumberNew, String parkNo, String carNumberExit);

    /**
     * 更新 用户成员 通过 车场编号 车牌号
     *
     * @param memberId  用户成员
     * @param parkNo    车场编号
     * @param carNumber 车牌号
     */
    boolean updateMemberIdByParkNoCarNumber(String memberId, String parkNo, String carNumber);


    /**
     * 删除 离场记录
     *
     * @param id 离场记录id
     */
    boolean removeParkLiveRecordsById(Integer id);

    // endregion

    // region 订单相关


    /**
     * 构建创建订单参数
     *
     * @param settleType     支付类型
     * @param parkNo         车场编号
     * @param carNumber      车牌号
     * @param passageNo      通道编号
     * @param discountReason 优惠原因
     *                       车辆不在车场内则 则抛出异常
     */
    VehicleParkCreateOrderRequestVO buildCreateOrderRequest(SettleTypeEnum settleType, String parkNo, String carNumber, String passageNo, String discountReason);

    /**
     * 创建订单 - 一般订单
     *
     * @param settleType 支付类型
     * @param parkNo     车场编号
     * @param carNumber  车牌号
     * @param passageNo  通道编号
     */
    Pair<VehicleParkOrderVO, List<ParkingOrder.CouponInfo>> createParkingGeneralOrder(SettleTypeEnum settleType, String parkNo, String carNumber, String passageNo);

    /**
     * 免费开闸 - 更新订单状态
     *
     * @param parkNo     车场编号
     * @param passageNo  通道号
     * @param CarNumber  车牌号
     * @param orderNo    订单号 (正常的岗亭订单号)
     * @param freeReason 免费原因
     */
    void freeOpenGate(String parkNo, String passageNo, String CarNumber, String orderNo, String freeReason);

    /**
     * 收费开闸 - 更新订单状态
     *
     * @param parkNo       车场编号
     * @param passageNo    通道号
     * @param CarNumber    车牌号
     * @param orderNo      订单号 (正常的岗亭订单号)
     * @param manualAmount 收费金额
     */
    void chargeOpenGate(String parkNo, String passageNo, String CarNumber, String orderNo, Double manualAmount);

    /**
     * 查询订单 通过订单号
     *
     * @param orderNo 订单号
     */
    VehicleParkOrderVO queryParkingOrderByOrderNo(String orderNo);

    /**
     * 完成订单 更新 订单系统订单状态
     *
     * @param orderNo 订单号
     */
    boolean completeOrder(String orderNo);


    /**
     * 在场记录 批量 修改入场时间
     *
     * @param liveIdList 在场记录id列表
     * @param entryTime  入场时间
     */
    boolean editEntryTimeBatch(List<Integer> liveIdList, LocalDateTime entryTime);

    /**
     * 在场记录 批量 手动结算
     * 1.维护在场记录的订单状态
     * 2.创建order 离场订单
     * 3.离场记录表里增加一条离场数据
     *
     * @param liveIdList     在场记录id列表
     * @param payAmount      实际支付金额
     * @param liftGateReason 抬杆原因
     */
    boolean manualComputationBatch(List<Integer> liveIdList, Double payAmount, String liftGateReason);

    /**
     * 查询缓存里的订单信息 通过车场编号车牌号
     * 没有订单则新建 预支付订单
     *
     * @param parkNo    停车场编号
     * @param carNumber 车牌号
     */
    VehicleParkOrderVO queryOrCreatePrepayOrderByParkNoCarNumber(String parkNo, String carNumber);

    /**
     * 查询缓存里的订单信息 通过车场编号岗亭编号
     * 没有订单则新建 岗亭订单
     * 通道内没有对应车牌时 抛出异常 ServiceException
     *
     * @param parkNo    车场编号
     * @param passageNo 岗亭编号
     */
    VehicleParkOrderVO queryOrCreatePavilionOrderByParkNoPassageNo(String parkNo, String passageNo);


    /**
     * 关联在场订单与订单信息
     * 先清空旧数据，再插入
     *
     * @param parkLiveId 在场订单id
     * @param list 订单号以及支付信息
     */
    boolean insertOrderNoWithLiveId(Integer parkLiveId, List<VehicleParkOrderVO> list);

    // endregion

    // region 已结算 的 在场记录 查询

    /**
     * 查询 已结算订单 记录列表
     *
     * @param parkLiveRecordsParam 查询条件
     * @return 离场订单记录列表
     */
    List<ParkLiveRecordsVO> selectParkSettlementRecordsVOList(ParkLiveRecordsParam parkLiveRecordsParam);


    /**
     * 查询 已结算订单 记录列表 分页
     *
     * @param parkLiveRecordsParam 查询条件
     * @return 离场订单记录列表
     */
    Pair<List<ParkSettlementRecordsVO>, Long> selectParkSettlementRecordsVOPage(ParkLiveRecordsParam parkLiveRecordsParam);

    /**
     * 查询 已结算订单 明细信息
     *
     * @param id 在场订单id
     */
    ParkSettlementRecordsDetailVO queryParkSettlementRecordsVOById(Integer id);


    /**
     * 新增离场记录
     *
     * @param param 离场记录
     */
    boolean addSettlementRecords(ParkLiveRecordsAddParam param);

    /**
     * 导入离场记录
     *
     * @param list 离场记录列表
     */
    boolean importSettlementRecords(List<ParkLiveRecordsAddParam> list);

    /**
     * 更新 离场车辆 车牌信息
     *
     * @param id        离场记录id
     * @param carNumber 车牌号
     */
    boolean updateSettlementRecordsCarNumberById(Integer id, String carNumber);

    /**
     * 删除 离场记录
     *
     * @param id 离场记录id
     */
    boolean removeSettlementRecordsById(Integer id);


    /**
     * 查询用户车牌停车记录 通过会员id 车牌号
     *
     * @param memberId  用户id
     * @param carNumber 车牌号
     * @param pageNum   页数
     * @param pageSize  分页大小
     */
    Pair<List<HistoryParkingOrderVO>, Long> queryHistoryParkingOrderByMemberIdCarNumber(String memberId, String carNumber, Integer pageNum, Integer pageSize);

    /**
     * 查询在场记录 通过订单号
     *
     * @param orderNo 订单号
     */
    ParkLiveRecords queryParkLiveRecordsByOrderNo(String orderNo);

    /**
     * 更新 停车订单 支付信息
     *
     * @param payableAmount  应付金额（元）
     * @param payAmount      实付金额（元）
     * @param discountAmount 优惠金额（元）
     * @param orderNo        订单号
     */
    boolean updareParkLiveParkingOrderByOrderNo(Double payableAmount, Double payAmount, Double discountAmount, String orderNo);

    /**
     * 查询会员停车订单历史车牌号
     *
     * @param memberId 会员id
     */
    List<String> queryHistoryCarNumberByMemberId(String memberId);
    // endregion
}
