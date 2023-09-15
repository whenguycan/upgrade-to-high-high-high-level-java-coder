package com.ruoyi.project.parking.service.impl;

import com.czdx.grpc.lib.merchant.GetNotDisposedCountRequest;
import com.czdx.grpc.lib.merchant.GetNotDisposedCountResponse;
import com.czdx.grpc.lib.merchant.VisitorApplyServiceGrpc;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.ruoyi.project.parking.domain.vo.HomePageHeaderVo;
import com.ruoyi.project.parking.mapper.HomePageMapper;
import com.ruoyi.project.parking.service.IHomePageService;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 首页Service业务层处理
 *
 * @author fangch
 * @date 2023-03-20
 */
@Slf4j
@Service
public class HomePageServiceImpl implements IHomePageService
{

    @Autowired
    private HomePageMapper homePageMapper;

    @GrpcClient("parking-order-server")
    private ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub parkingOrderServiceBlockingStub;

    @GrpcClient("parking-member-merchant-server")
    private VisitorApplyServiceGrpc.VisitorApplyServiceBlockingStub visitorApplyServiceBlockingStub;

    /**
     * 首页顶部
     *
     * @param deptId 场库id
     * @return 首页顶部
     */
    @Override
    public HomePageHeaderVo homePageHeader(Long deptId) {
        HomePageHeaderVo result = new HomePageHeaderVo();
        try {
            List<String> parkNos = homePageMapper.getChildParkNos(deptId);
            // 今日收入（元）
            // 总收入（元）
            ParkingOrder.StatisticIncomeRequest request = ParkingOrder.StatisticIncomeRequest.newBuilder()
                    .addAllParkNos(parkNos)
                    .build();
            ParkingOrder.StatisticIncomeResponse response = parkingOrderServiceBlockingStub.statisticIncome(request);
            result.setTodayIncome(BigDecimal.valueOf(response.getTodayIncome()));
            result.setTotalIncome(BigDecimal.valueOf(response.getTotalIncome()));

            // 剩余车位数
            Integer remainSpaceCount = homePageMapper.getRemainSpaceCount(parkNos);
            result.setRemainSpaceCount(remainSpaceCount < 0 ? 0 : remainSpaceCount);

            // 访客待审批数
            GetNotDisposedCountRequest getNotDisposedCountRequest = GetNotDisposedCountRequest.newBuilder()
                    .addAllParkNos(parkNos)
                    .build();
            GetNotDisposedCountResponse getNotDisposedCountResponse = visitorApplyServiceBlockingStub.getNotDisposedCount(getNotDisposedCountRequest);
            result.setNotDisposedCount(getNotDisposedCountResponse.getCount());

            // 固定车辆数
            result.setRegularCarCount(homePageMapper.getRegularCarCount(parkNos));
        } catch (StatusRuntimeException e) {
            log.error( "homePageHeader 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error( "homePageHeader 2 FAILED with " + e.getMessage());
        }
        return result;
    }
}
