package com.ruoyi.project.parking.service;

import com.czdx.grpc.lib.merchant.*;
import com.google.protobuf.ProtocolStringList;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.bo.VisitorApplySheetBO;
import com.ruoyi.project.parking.domain.param.BVisitorApplyParam;
import com.ruoyi.project.parking.domain.vo.BVisitorApplyVO;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import com.ruoyi.project.system.service.ISysDeptService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@GrpcService
public class VisitorApplyGrpcServiceImpl extends VisitorApplyServiceGrpc.VisitorApplyServiceImplBase {

    @Autowired
    private IBVisitorApplyService ibVisitorApplyService;

    @Autowired
    private ISysDeptService sysDeptService;

    /**
     * 查询停车预约申请列表
     */
    @Override
    public void selectBVisitorApplyList(SelectBVisitorApplyListRequest request, StreamObserver<SelectBVisitorApplyListResponse> responseObserver) {
        BVisitorApplyParam param = null;
        try {
            param = ProtoJsonUtil.toPojoBean(BVisitorApplyParam.class, request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<BVisitorApplyVO> list = ibVisitorApplyService.getBVisitorApplyList(param);
        List<BVisitorApplyVOProto> builder = new ArrayList<>();
        list.forEach(item -> {
            BVisitorApplyVOProto builderItem = assembleBVisitorApplyVOProto(item);
            builder.add(builderItem);
        });
        responseObserver.onNext(SelectBVisitorApplyListResponse.newBuilder().addAllRows(builder).build());
        responseObserver.onCompleted();
    }

    /**
     * 批量审核和驳回
     */
    @Override
    public void updateStatus(UpdateStatusRequest request, StreamObserver<UpdateStatusResponse> responseObserver) {
        AjaxResult result = ibVisitorApplyService.updateStatus(request.getIdsList().toArray(new Integer[0]), request.getStatus(), request.getRejectReason(), request.getUpdateBy());
        UpdateStatusResponse builder = assembleUpdateStatusResponse(result);
        responseObserver.onNext(builder);
        responseObserver.onCompleted();
    }

    /**
     * 查询访客待审批数
     */
    @Override
    public void getNotDisposedCount(GetNotDisposedCountRequest request, StreamObserver<GetNotDisposedCountResponse> responseObserver) {
        Integer count = ibVisitorApplyService.getNotDisposedCount(request.getParkNosList());
        responseObserver.onNext(GetNotDisposedCountResponse.newBuilder().setCount(count).build());
        responseObserver.onCompleted();
    }

    /**
     * 组装停车预约申请列表回复对象
     *
     * @param bVisitorApplyVO 停车预约申请列表
     * @return com.czdx.grpc.lib.merchant.BVisitorApplyVOProto
     */
    private BVisitorApplyVOProto assembleBVisitorApplyVOProto(BVisitorApplyVO bVisitorApplyVO) {
        // 组装返回对象
        BVisitorApplyVOProto.Builder responseBuilder = BVisitorApplyVOProto.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(responseBuilder, bVisitorApplyVO);
        } catch (IOException e) {
            log.error("组装停车预约申请列表异常，{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return responseBuilder.build();
    }

    /**
     * 组装批量审核和驳回回复对象
     *
     * @param result 结果
     * @return com.czdx.grpc.lib.merchant.UpdateStatusResponse
     */
    private UpdateStatusResponse assembleUpdateStatusResponse(AjaxResult result) {
        // 组装返回对象
        UpdateStatusResponse.Builder responseBuilder = UpdateStatusResponse.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(responseBuilder, result);
        } catch (IOException e) {
            log.error("组装批量审核和驳回异常，{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return responseBuilder.build();
    }

    /**
     * 查询指定日期访客数量
     */
    @Override
    public void countBVisitorApply(CountBVisitorApplyRequest request, StreamObserver<CountBVisitorApplyResponse> responseObserver) {
        ProtocolStringList parkNosList = request.getParkNosList();
        String day = request.getDay();
        String status = request.getStatus();
        int count = ibVisitorApplyService.countBVisitorApply(day, status, parkNosList);
        responseObserver.onNext(CountBVisitorApplyResponse.newBuilder().setCount(count).build());
        responseObserver.onCompleted();
    }

    @Override
    public void visitorApplyStatisticsSheet(VisitorApplyStatisticsSheetRequestProto request, StreamObserver<VisitorApplyStatisticsSheetResponseProto> responseObserver) {
        String parkNo = request.getParkNo();
        String startDate = request.getStartDate();
        String endDate = request.getEndDate();
        List<VisitorApplySheetBO> visitorApplySheetBOS = ibVisitorApplyService.statisticsSheet(parkNo, startDate, endDate);
        VisitorApplyStatisticsSheetResponseProto.Builder builder = VisitorApplyStatisticsSheetResponseProto.newBuilder();
        visitorApplySheetBOS.forEach(item -> {
            builder.addVisitApply(VisitorApplyStatisticsSheet.newBuilder()
                    .setDay(item.getDay())
                    .setApplyNum(item.getApplyNum())
                    .setVisitNum(item.getVisitNum())
                    .build());
        });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
