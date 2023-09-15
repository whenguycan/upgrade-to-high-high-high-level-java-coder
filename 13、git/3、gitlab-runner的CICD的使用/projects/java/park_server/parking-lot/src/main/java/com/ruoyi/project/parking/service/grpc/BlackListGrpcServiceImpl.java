package com.ruoyi.project.parking.service.grpc;

import com.czdx.grpc.lib.lot.BlackListServiceGrpc;
import com.czdx.grpc.lib.lot.InBlackListRequestProto;
import com.czdx.grpc.lib.lot.InBlackListResponseProto;
import com.ruoyi.project.parking.domain.BlackList;
import com.ruoyi.project.parking.service.IBlackListService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@GrpcService
public class BlackListGrpcServiceImpl extends BlackListServiceGrpc.BlackListServiceImplBase {

    @Autowired
    IBlackListService blackListService;

    /**
     * @apiNote 验证当前车牌号在当前场库是否在黑名单中
     */
    @Override
    public void inBlackList(InBlackListRequestProto request, StreamObserver<InBlackListResponseProto> responseObserver) {
        String parkNo = request.getParkNo();
        String carNumber = request.getCarNumber();
        BlackList blackList = new BlackList();
        blackList.setParkNo(parkNo);
        blackList.setCarNumber(carNumber);
        List<BlackList> blackLists = blackListService.listByParkNoAndCarNumberUnsafe(blackList);
        //返回结果
        responseObserver.onNext(InBlackListResponseProto.newBuilder()
                .setFlag(!blackLists.isEmpty())
                .build());
        responseObserver.onCompleted();
    }
}