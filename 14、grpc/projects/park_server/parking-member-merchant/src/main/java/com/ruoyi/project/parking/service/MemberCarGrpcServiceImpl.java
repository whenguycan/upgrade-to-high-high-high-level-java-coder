package com.ruoyi.project.parking.service;

import com.czdx.grpc.lib.merchant.*;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.BMyCar;
import com.ruoyi.project.parking.domain.param.BMyCarParam;
import com.ruoyi.project.parking.domain.param.MemberUserParam;
import com.ruoyi.project.parking.domain.vo.MemberUserVO;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import com.ruoyi.project.system.domain.vo.SysUserVO;
import com.ruoyi.project.system.service.ISysUserService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@GrpcService
public class MemberCarGrpcServiceImpl extends MemberCarServiceGrpc.MemberCarServiceImplBase {

    @Autowired
    private IBMyCarService ibMyCarService;

    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 查询会员详情
     */
    @Override
    public void selectUserById(SelectUserByIdRequest request, StreamObserver<SelectUserByIdResponse> responseObserver) {
        MemberUserVO userVO = iSysUserService.selectMemberById((long) request.getId());
        SelectUserByIdResponse builder = assembleSelectUserByIdResponse(userVO);
        responseObserver.onNext(builder);
        responseObserver.onCompleted();
    }

    /**
     * 根据条件分页查询会员列表
     */
    @Override
    public void selectUserList(SelectUserListRequest request, StreamObserver<SelectUserListResponse> responseObserver) {
        MemberUserParam memberUser = null;
        try {
            memberUser = ProtoJsonUtil.toPojoBean(MemberUserParam.class, request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<MemberUserVO> memberUserVOS = iSysUserService.selectMemberList(memberUser);
        List<MemberUserVOProto> builder = new ArrayList<>();
        memberUserVOS.forEach(item -> {
            MemberUserVOProto builderItem = assembleMemberUserVOProto(item);
            builder.add(builderItem);
        });
        responseObserver.onNext(SelectUserListResponse.newBuilder().addAllRows(builder).build());
        responseObserver.onCompleted();
    }

    /**
     * 通过会员ID查询我的车辆
     */
    @Override
    public void selectCarList(SelectCarListRequest request, StreamObserver<SelectCarListResponse> responseObserver) {
        BMyCar bMyCar = new BMyCar();
        bMyCar.setCreateBy(request.getUserId());
        List<BMyCar> bMyCars = ibMyCarService.selectBMyCarList(bMyCar);
        List<BMyCarProto> builder = new ArrayList<>();
        bMyCars.forEach(item -> {
            BMyCarProto builderItem = assembleBMyCarProto(item);
            builder.add(builderItem);
        });
        responseObserver.onNext(SelectCarListResponse.newBuilder().addAllBMyCars(builder).build());
        responseObserver.onCompleted();
    }

    /**
     * 绑定车辆
     */
    @Override
    public void insertBMyCar(InsertBMyCarRequest request, StreamObserver<InsertBMyCarResponse> responseObserver) {
        BMyCar bMyCar = new BMyCar();
        BMyCarParam param = null;
        try {
            param = ProtoJsonUtil.toPojoBean(BMyCarParam.class, request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BeanUtils.copyProperties(param, bMyCar);
        bMyCar.setCreateBy(param.getUserId());
        InsertBMyCarResponse builder = assembleInsertBMyCarResponse(ibMyCarService.insertBMyCar(bMyCar));
        responseObserver.onNext(builder);
        responseObserver.onCompleted();
    }

    /**
     * 解绑车辆
     */
    @Override
    public void deleteBMyCarByIds(DeleteBMyCarByIdsRequest request, StreamObserver<DeleteBMyCarByIdsResponse> responseObserver) {
        int result = ibMyCarService.deleteBMyCarByIds(request.getIdsList().toArray(new Integer[0]));
        DeleteBMyCarByIdsResponse builder = DeleteBMyCarByIdsResponse.newBuilder()
                .setResult(result).build();
        responseObserver.onNext(builder);
        responseObserver.onCompleted();
    }

    /**
     * 设为默认
     */
    @Override
    public void setDefault(SetDefaultRequest request, StreamObserver<SetDefaultResponse> responseObserver) {
        int result = ibMyCarService.setDefault(request.getId(), request.getUserId(), request.getUpdateBy());
        SetDefaultResponse builder = SetDefaultResponse.newBuilder()
                .setResult(result).build();
        responseObserver.onNext(builder);
        responseObserver.onCompleted();
    }

    /**
     * 根据车牌号查询用户信息
     */
    @Override
    public void getMemberByCar(GetMemberByCarRequest request, StreamObserver<GetMemberByCarResponse> responseObserver) {
        SysUserVO sysUser = ibMyCarService.getMemberByCar(request.getCarNo());
        GetMemberByCarResponse builder = assembleGetMemberByCarResponse(sysUser);
        responseObserver.onNext(builder);
        responseObserver.onCompleted();
    }

    /**
     * 组装会员详情回复对象
     *
     * @param userVO 会员详情
     * @return com.czdx.grpc.lib.merchant.SelectUserByIdResponse
     */
    private SelectUserByIdResponse assembleSelectUserByIdResponse(MemberUserVO userVO) {
        // 组装返回对象
        SelectUserByIdResponse.Builder responseBuilder = SelectUserByIdResponse.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(responseBuilder, userVO);
        } catch (IOException e) {
            log.error("组装会员详情异常，{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return responseBuilder.build();
    }

    /**
     * 组装会员列表回复对象
     *
     * @param memberUserVO 会员列表
     * @return com.czdx.grpc.lib.merchant.MemberUserVOProto
     */
    private MemberUserVOProto assembleMemberUserVOProto(MemberUserVO memberUserVO) {
        // 组装返回对象
        MemberUserVOProto.Builder responseBuilder = MemberUserVOProto.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(responseBuilder, memberUserVO);
        } catch (IOException e) {
            log.error("组装会员列表异常，{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return responseBuilder.build();
    }

    /**
     * 组装我的车辆回复对象
     *
     * @param bMyCar 我的车辆
     * @return com.czdx.grpc.lib.merchant.BMyCarProto
     */
    private BMyCarProto assembleBMyCarProto(BMyCar bMyCar) {
        // 组装返回对象
        BMyCarProto.Builder responseBuilder = BMyCarProto.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(responseBuilder, bMyCar);
        } catch (IOException e) {
            log.error("组装我的车辆异常，{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return responseBuilder.build();
    }

    /**
     * 组装绑定车辆回复对象
     *
     * @param result 结果
     * @return com.czdx.grpc.lib.merchant.InsertBMyCarResponse
     */
    private InsertBMyCarResponse assembleInsertBMyCarResponse(AjaxResult result) {
        // 组装返回对象
        InsertBMyCarResponse.Builder responseBuilder = InsertBMyCarResponse.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(responseBuilder, result);
        } catch (IOException e) {
            log.error("组装绑定车辆异常，{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return responseBuilder.build();
    }

    /**
     * 组装根据车牌号查询用户信息回复对象
     *
     * @param sysUser 用户信息
     * @return com.czdx.grpc.lib.merchant.GetMemberByCarResponse
     */
    private GetMemberByCarResponse assembleGetMemberByCarResponse(SysUserVO sysUser) {
        // 组装返回对象
        GetMemberByCarResponse.Builder responseBuilder = GetMemberByCarResponse.newBuilder();
        try {
            ProtoJsonUtil.toProtoBean(responseBuilder, sysUser);
        } catch (IOException e) {
            log.error("组装根据车牌号查询用户信息异常，{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return responseBuilder.build();
    }
}
