package com.ruoyi.project.parking.service.grpc;

import com.czdx.grpc.lib.lot.*;
import com.ruoyi.project.parking.domain.BVisitorCodeManage;
import com.ruoyi.project.parking.domain.vo.BVisitorCodeManageVO;
import com.ruoyi.project.parking.domain.vo.WhiteListVO;
import com.ruoyi.project.parking.service.IBVisitorCodeManageService;
import com.ruoyi.project.parking.service.IWhiteListService;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@GrpcService
public class BVisitorCodeManageGrpcServiceImpl extends BVisitorCodeManageServiceGrpc.BVisitorCodeManageServiceImplBase {

    @Autowired
    private IBVisitorCodeManageService ibVisitorCodeManageService;

    @Autowired
    private IWhiteListService whiteListService;

    /**
     * 获取访客码信息
     */
    @Override
    public void getCodeInfo(GetCodeInfoRequestProto request, StreamObserver<GetCodeInfoResponseProto> responseObserver) {
        BVisitorCodeManageVO bVisitorCodeManage = ibVisitorCodeManageService.getCodeInfo(request.getCode());
        GetCodeInfoResponseProto builder = assembleGetCodeInfoResponse(bVisitorCodeManage);
        responseObserver.onNext(builder);
        responseObserver.onCompleted();
    }

    /**
     * 刷新绑定次数
     */
    @Override
    public void updateNumber(UpdateNumberRequestProto request, StreamObserver<UpdateNumberResponseProto> responseObserver) {
        BVisitorCodeManage bVisitorCodeManage = new BVisitorCodeManage();
        bVisitorCodeManage.setId(request.getId());
        bVisitorCodeManage.setCodeUsedNumber(request.getCodeUsedNumber());
        bVisitorCodeManage.setUpdateBy(request.getUpdateBy());
        bVisitorCodeManage.setUpdateTime(LocalDateTime.now());
        int result = ibVisitorCodeManageService.updateNumber(bVisitorCodeManage);
        UpdateNumberResponseProto builder = UpdateNumberResponseProto.newBuilder()
                .setResult(result).build();
        responseObserver.onNext(builder);
        responseObserver.onCompleted();
    }

    /**
     * 设置白名单
     */
    @Override
    public void addWhiteList(AddWhiteListRequestProto request, StreamObserver<AddWhiteListResponseProto> responseObserver) {
        WhiteListVO whiteListVO = null;
        try {
            whiteListVO = ProtoJsonUtil.toPojoBean(WhiteListVO.class, request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean result = whiteListService.addWhiteList(whiteListVO);
        AddWhiteListResponseProto builder = AddWhiteListResponseProto.newBuilder()
                .setResult(result).build();
        responseObserver.onNext(builder);
        responseObserver.onCompleted();
    }

    /**
     * 组装访客码回复对象
     *
     * @param bVisitorCodeManage 访客码信息
     * @return com.czdx.grpc.lib.lot.GetCodeInfoResponseProto
     */
    private GetCodeInfoResponseProto assembleGetCodeInfoResponse(BVisitorCodeManageVO bVisitorCodeManage) {
        // 组装返回对象
        GetCodeInfoResponseProto.Builder responseBuilder = GetCodeInfoResponseProto.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(responseBuilder, bVisitorCodeManage);
        } catch (IOException e) {
            log.error("组装访客码异常，{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return responseBuilder.build();
    }
}
