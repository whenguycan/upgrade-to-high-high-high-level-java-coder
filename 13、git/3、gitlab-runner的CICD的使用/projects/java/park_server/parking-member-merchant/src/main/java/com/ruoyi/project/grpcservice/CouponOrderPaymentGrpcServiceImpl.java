package com.ruoyi.project.grpcservice;

import com.czdx.grpc.lib.merchant.CouponOrderPayment;
import com.czdx.grpc.lib.merchant.CouponOrderPaymentServiceGrpc;
import com.czdx.grpc.lib.merchant.CouponProvider;
import com.czdx.grpc.lib.merchant.CouponProviderServiceGrpc;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.project.common.CommonConstants;
import com.ruoyi.project.merchant.domain.TOperRecords;
import com.ruoyi.project.merchant.domain.vo.AssignedCoupon;
import com.ruoyi.project.merchant.domain.vo.TOperRecordsVo;
import com.ruoyi.project.merchant.service.ITCouponCarnoRelationService;
import com.ruoyi.project.merchant.service.ITOperRecordsService;
import com.ruoyi.project.system.service.ISysDictDataService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * description: 计算停车费服务类
 *
 * @author mingchenxu
 * @date 2023/2/25 10:55
 */
@Slf4j
@GrpcService
public class CouponOrderPaymentGrpcServiceImpl extends CouponOrderPaymentServiceGrpc.CouponOrderPaymentServiceImplBase {

    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    ITOperRecordsService itOperRecordsService;

    @Override
    public void updateCouponOrderPayment(com.czdx.grpc.lib.merchant.CouponOrderPayment.CouponOrderPaymentRequest request,
                                         io.grpc.stub.StreamObserver<com.czdx.grpc.lib.merchant.CouponOrderPayment.CouponOrderPaymentResponse> responseObserver) {

        String parkNo = request.getParkNo();
        String orderNo = request.getOrderNo();
        Integer payStatus = request.getPayStatus();
        String resultMessage = request.getResultMessage();
        TOperRecordsVo tOperRecords = new TOperRecordsVo();
        tOperRecords.setParkNo(parkNo);
        tOperRecords.setOrderNo(orderNo);
        List<TOperRecords> tOperRecordsList = itOperRecordsService.selectTOperRecordsList(tOperRecords);
        //构建相应信息；
        CouponOrderPayment.CouponOrderPaymentResponse.Builder response = CouponOrderPayment.CouponOrderPaymentResponse.newBuilder();
        if (CollectionUtils.isEmpty(tOperRecordsList)) {
            response.setMessage("此订单异常，未存在");
            response.setStatus("500");
        } else {
            TOperRecords operateRecords = tOperRecordsList.get(0);
            if (payStatus == -1) {
                operateRecords.setStatus(1);
                tOperRecords.setErrorMsg(resultMessage);
            } else {
                operateRecords.setStatus(0);
            }
            response.setMessage("success");
            response.setStatus("200");
            itOperRecordsService.updateTOperRecords(operateRecords);
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }


}
