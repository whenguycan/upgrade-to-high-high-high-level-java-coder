package com.ruoyi.project.parking.service.grpcclient;

import com.czdx.grpc.lib.charge.*;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.project.parking.domain.param.BParkChargeRuleTestParam;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.TimerTask;

/**
 *
 * description: 调用计费服务的GRPC客户端服务类
 * @author mingchenxu
 * @date 2023/3/21 11:18
 */
@Slf4j
@Service("parkingChargeGrpcClient")
public class ParkingChargeGrpcClientServiceImpl {

    @GrpcClient("parking-charge-server")
    ParkingChargeServiceGrpc.ParkingChargeServiceBlockingStub parkingChargeServiceBlockingStub;

    /**
     *
     * description: 刷新计费规则
     * @author mingchenxu
     * @date 2023/3/21 09:56
     * @param parkNo 车场编号
     * @param ruleId 规则ID
     */
    public void refreshChargeRule(String parkNo, Integer ruleId) {
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                RefreshParkingRuleRequest request = RefreshParkingRuleRequest.newBuilder().setParkNo(parkNo).setRuleId(ruleId).build();
                RefreshParkingRuleResponse response = parkingChargeServiceBlockingStub.refreshParkingRule(request);
                log.info("车场[{}]刷新计费规则成功数：[{}]，指定规则ID[{}]", parkNo, response.getSucNum(), ruleId);
            }
        }, 2);
    }

    /**
     * 测试车场收费规则
     *
     * @param bParkChargeRuleTestParam 测试车场收费规则参数
     * @return 停车费
     */
    public double testChargeParkingFee(BParkChargeRuleTestParam bParkChargeRuleTestParam) {
        // 组装查询参数，可以采用protojson工具类转换
        TestChargeParkingRuleRequest ruleRequest = TestChargeParkingRuleRequest.newBuilder()
                .setParkNo(bParkChargeRuleTestParam.getParkNo())
                .setRuleId(bParkChargeRuleTestParam.getId())
                .setEntryTime(bParkChargeRuleTestParam.getEntryTime())
                .setExitTime(bParkChargeRuleTestParam.getExitTime())
                .build();
        TestChargeParkingRuleResponse ruleResponse = parkingChargeServiceBlockingStub.testChargeParkingRule(ruleRequest);
        return ruleResponse.getPayableAmount();
    }

    /**
     *
     * description: 刷新车场计费约束
     * @author mingchenxu
     * @date 2023/3/21 11:25
     * @param parkNo 车场编号
     */
    public void refreshParkLotChargeScheme(String parkNo) {
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                RefreshParkLotChargeSchemeRequest request = RefreshParkLotChargeSchemeRequest.newBuilder().setParkNo(parkNo).build();
                RefreshParkLotChargeSchemeResponse response = parkingChargeServiceBlockingStub.refreshParkLotChargeScheme(request);
                log.info("车场[{}]刷新计费约束成功数：[{}]", parkNo, response.getSucNum());
            }
        }, 2);

    }

    /**
     *
     * description: 刷新计费关联关系
     * @author mingchenxu
     * @date 2023/3/31 16:43
     * @param parkNo 车场编号
     */
    public void refreshParkLotChargeRelation(String parkNo) {
        AsyncManager.me().execute(new TimerTask() {
            @Override
            public void run() {
                RefreshParkLotChargeRelationRequest request = RefreshParkLotChargeRelationRequest.newBuilder().setParkNo(parkNo).build();
                RefreshParkLotChargeRelationResponse response = parkingChargeServiceBlockingStub.refreshParkLotChargeRelation(request);
                log.info("车场[{}]刷新计费关联关系成功数：[{}]", parkNo, response.getSucNum());
            }
        }, 2);

    }

}
