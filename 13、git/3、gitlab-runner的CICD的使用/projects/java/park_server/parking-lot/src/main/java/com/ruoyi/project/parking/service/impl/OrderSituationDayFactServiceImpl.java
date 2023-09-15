package com.ruoyi.project.parking.service.impl;

import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.ruoyi.project.parking.domain.OrderSituationDayFact;
import com.ruoyi.project.parking.domain.vo.OrderSituationFactVO;
import com.ruoyi.project.parking.mapper.HomePageMapper;
import com.ruoyi.project.parking.mapper.OrderSituationDayFactMapper;
import com.ruoyi.project.parking.service.IOrderSituationDayFactService;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import com.ruoyi.project.system.domain.SysDept;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 首页订单情况事实Service业务层处理
 *
 * @author fangch
 * @date 2023-03-20
 */
@Slf4j
@Service
public class OrderSituationDayFactServiceImpl implements IOrderSituationDayFactService
{
    @Autowired
    private OrderSituationDayFactMapper orderSituationDayFactMapper;

    @Autowired
    private HomePageMapper homePageMapper;

    @GrpcClient("parking-order-server")
    private ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub parkingOrderServiceBlockingStub;

    /**
     * 查询首页订单情况事实
     *
     * @param id 首页订单情况事实主键
     * @return 首页订单情况事实
     */
    @Override
    public OrderSituationDayFact selectOrderSituationDayFactById(Integer id)
    {
        return orderSituationDayFactMapper.selectOrderSituationDayFactById(id);
    }

    /**
     * 查询首页订单情况事实列表
     *
     * @param orderSituationDayFact 首页订单情况事实
     * @return 首页订单情况事实
     */
    @Override
    public List<OrderSituationDayFact> selectOrderSituationDayFactList(OrderSituationDayFact orderSituationDayFact)
    {
        return orderSituationDayFactMapper.selectOrderSituationDayFactList(orderSituationDayFact);
    }

    /**
     * 新增首页订单情况事实
     *
     * @param orderSituationDayFact 首页订单情况事实
     * @return 结果
     */
    @Override
    public int insertOrderSituationDayFact(OrderSituationDayFact orderSituationDayFact)
    {
        orderSituationDayFact.setCreateTime(LocalDateTime.now());
        return orderSituationDayFactMapper.insertOrderSituationDayFact(orderSituationDayFact);
    }

    /**
     * 修改首页订单情况事实
     *
     * @param orderSituationDayFact 首页订单情况事实
     * @return 结果
     */
    @Override
    public int updateOrderSituationDayFact(OrderSituationDayFact orderSituationDayFact)
    {
        orderSituationDayFact.setUpdateTime(LocalDateTime.now());
        return orderSituationDayFactMapper.updateOrderSituationDayFact(orderSituationDayFact);
    }

    /**
     * 批量删除首页订单情况事实
     *
     * @param ids 需要删除的首页订单情况事实主键
     * @return 结果
     */
    @Override
    public int deleteOrderSituationDayFactByIds(Integer[] ids)
    {
        return orderSituationDayFactMapper.deleteOrderSituationDayFactByIds(ids);
    }

    /**
     * 删除首页订单情况事实信息
     *
     * @param id 首页订单情况事实主键
     * @return 结果
     */
    @Override
    public int deleteOrderSituationDayFactById(Integer id)
    {
        return orderSituationDayFactMapper.deleteOrderSituationDayFactById(id);
    }

    /**
     * 分析首页订单情况事实
     */
    @Override
    public void analyseOrderSituationDayFact(String userId) {
        try {
            // 查询所有车场id和编号
            List<SysDept> parks = homePageMapper.getAllParkIds();
            for (SysDept park : parks) {
                // 查询所有归属子车场编号
                List<String> parkNos = homePageMapper.getChildParkNos(park.getDeptId());
                String day = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                ParkingOrder.AnalyseOrderSituationDayFactRequest request = ParkingOrder.AnalyseOrderSituationDayFactRequest.newBuilder()
                        .setDay(day)
                        .addAllParkNos(parkNos)
                        .build();
                ParkingOrder.AnalyseOrderSituationDayFactResponse response = parkingOrderServiceBlockingStub.analyseOrderSituationDayFact(request);
                List<ParkingOrder.AnalyseOrderSituationDayFactProto> orderSituationDayFactProtos = response.getRowsList();
                orderSituationDayFactProtos.forEach(item -> {
                    try {
                        // 订单模块分析远程调用返参转pojo
                        OrderSituationDayFact orderSituationDayFact = ProtoJsonUtil.toPojoBean(OrderSituationDayFact.class, item);
                        orderSituationDayFact.setParkNo(park.getParkNo());
                        // 删除同维度的分析数据，然后新增
                        orderSituationDayFactMapper.deleteOrderSituationDayFact(orderSituationDayFact);
                        orderSituationDayFact.setCreateBy(userId);
                        orderSituationDayFact.setCreateTime(LocalDateTime.now());
                        orderSituationDayFactMapper.insertOrderSituationDayFact(orderSituationDayFact);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (StatusRuntimeException e) {
            log.error( "analyseOrderSituationDayFact 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error( "analyseOrderSituationDayFact 2 FAILED with " + e.getMessage());
        }
    }

    /**
     * 查询首页订单情况事实
     *
     * @param orderSituationDayFact 首页订单情况事实
     * @return 首页订单情况事实集合
     */
    @Override
    public List<OrderSituationFactVO> getOrderSituationDayFact(OrderSituationDayFact orderSituationDayFact) {
        return orderSituationDayFactMapper.getOrderSituationDayFact(orderSituationDayFact);
    }
}
