package com.ruoyi.project.parking.service.grpc;

import com.czdx.grpc.lib.lot.BSelfPaySchemeServiceGrpc;
import com.czdx.grpc.lib.lot.GetBSelfPaySchemeRequestProto;
import com.czdx.grpc.lib.lot.GetBSelfPaySchemeResponseProto;
import com.ruoyi.project.parking.domain.BSelfPayScheme;
import com.ruoyi.project.parking.service.IBSelfPaySchemeService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@GrpcService
public class BSelfPaySchemeGrpcServiceImpl extends BSelfPaySchemeServiceGrpc.BSelfPaySchemeServiceImplBase {
    @Autowired
    IBSelfPaySchemeService selfPaySchemeService;

    /**
     * @apiNote 根据parkNo获取自主缴费方案，无需认证
     */
    @Override
    public void getBSelfPayScheme(GetBSelfPaySchemeRequestProto request, StreamObserver<GetBSelfPaySchemeResponseProto> responseObserver) {
        String parkNo = request.getParkNo();
        BSelfPayScheme selfPayScheme = selfPaySchemeService.getBSelfPaySchemeByParkNo(parkNo);
        GetBSelfPaySchemeResponseProto getBSelfPaySchemeResponseProto= GetBSelfPaySchemeResponseProto.newBuilder()
                .setId(selfPayScheme.getId())
                .setParkNo(selfPayScheme.getParkNo())
                .setRenewStatus(selfPayScheme.getRenewStatus())
                .setSystemVerify(selfPayScheme.getSystemVerify())
                .setNewOwnerVerify(selfPayScheme.getNewOwnerVerify())
                .setOldOwnerVerify(selfPayScheme.getOldOwnerVerify())
                .setMaxRenewDays(selfPayScheme.getMaxRenewDays())
                .setRenewDeadlineDays(selfPayScheme.getRenewDeadlineDays())
                .build();
        responseObserver.onNext(getBSelfPaySchemeResponseProto);
        responseObserver.onCompleted();
    }
}
