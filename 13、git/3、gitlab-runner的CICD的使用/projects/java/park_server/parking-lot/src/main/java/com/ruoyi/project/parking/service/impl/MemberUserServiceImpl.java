package com.ruoyi.project.parking.service.impl;

import com.czdx.grpc.lib.merchant.*;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.BMyCar;
import com.ruoyi.project.parking.domain.MemberUser;
import com.ruoyi.project.parking.domain.param.BMyCarParam;
import com.ruoyi.project.parking.domain.param.MemberUserParam;
import com.ruoyi.project.parking.service.IMemberUserService;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import com.ruoyi.project.system.service.impl.SysUserServiceImpl;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 会员 业务层处理
 *
 * @author fangch
 */
@Service
public class MemberUserServiceImpl implements IMemberUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @GrpcClient("parking-member-merchant-server")
    private MemberCarServiceGrpc.MemberCarServiceBlockingStub memberCarServiceBlockingStub;

    /**
     * 查询会员详情
     *
     * @param id 会员主键
     * @return 会员详情
     */
    @Override
    public MemberUser selectUserById(Integer id) {
        MemberUser result = new MemberUser();
        try {
            // 组装查询参数，可以采用protojson工具类转换
            SelectUserByIdRequest.Builder request = SelectUserByIdRequest.newBuilder().setId(id);
            SelectUserByIdResponse response = memberCarServiceBlockingStub.selectUserById(request.build());
            result = ProtoJsonUtil.toPojoBean(MemberUser.class, response);
        } catch (StatusRuntimeException e) {
            log.error( "selectUserById 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error( "selectUserById 2 FAILED with " + e.getMessage());
        }
        return result;
    }

    /**
     * 根据条件分页查询会员列表
     *
     * @param memberUser 会员信息
     * @return 会员信息集合信息
     */
    @Override
    public List<MemberUser> selectUserList(MemberUserParam memberUser) {
        List<MemberUser> result = new ArrayList<>();
        try {
            // 组装查询参数，可以采用protojson工具类转换
            SelectUserListRequest.Builder request = SelectUserListRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(request, memberUser);
            SelectUserListResponse response = memberCarServiceBlockingStub.selectUserList(request.build());
            List<MemberUserVOProto> memberUserVOProtoList = response.getRowsList();
            memberUserVOProtoList.forEach(item -> {
                try {
                    MemberUser memberItem = ProtoJsonUtil.toPojoBean(MemberUser.class, item);
                    result.add(memberItem);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (StatusRuntimeException e) {
            log.error( "selectUserList 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error( "selectUserList 2 FAILED with " + e.getMessage());
        }
        return result;
    }

    /**
     * 通过会员ID查询我的车辆
     *
     * @param userId 会员ID
     * @return 我的车辆对象信息
     */
    @Override
    public List<BMyCar> selectCarList(Long userId) {
        List<BMyCar> result = new ArrayList<>();
        try {
            // 组装查询参数，可以采用protojson工具类转换
            SelectCarListRequest.Builder request = SelectCarListRequest.newBuilder().setUserId(String.valueOf(userId));
            SelectCarListResponse response = memberCarServiceBlockingStub.selectCarList(request.build());
            List<BMyCarProto> bMyCarProtoList = response.getBMyCarsList();
            bMyCarProtoList.forEach(item -> {
                try {
                    BMyCar bMyCar = ProtoJsonUtil.toPojoBean(BMyCar.class, item);
                    result.add(bMyCar);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (StatusRuntimeException e) {
            log.error( "selectCarList 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error( "selectCarList 2 FAILED with " + e.getMessage());
        }
        return result;
    }

    /**
     * 绑定车辆
     *
     * @param bMyCarParam 我的车辆管理
     * @return 结果
     */
    @Override
    public AjaxResult insertBMyCar(BMyCarParam bMyCarParam) {
        AjaxResult result = null;
        try {
            // 组装查询参数，可以采用protojson工具类转换
            InsertBMyCarRequest.Builder request = InsertBMyCarRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(request, bMyCarParam);
            InsertBMyCarResponse response = memberCarServiceBlockingStub.insertBMyCar(request.build());
            result = new AjaxResult(response.getCode(), response.getMsg(), response.getData());
        } catch (StatusRuntimeException e) {
            log.error( "insertBMyCar 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error( "insertBMyCar 2 FAILED with " + e.getMessage());
        }
        return result;
    }

    /**
     * 解绑车辆
     *
     * @param ids 需要删除的我的车辆主键
     * @return 结果
     */
    @Override
    public int deleteBMyCarByIds(Integer[] ids) {
        int result = 0;
        try {
            // 组装查询参数，可以采用protojson工具类转换
            DeleteBMyCarByIdsRequest.Builder request = DeleteBMyCarByIdsRequest.newBuilder().addAllIds(Arrays.asList(ids));
            DeleteBMyCarByIdsResponse response = memberCarServiceBlockingStub.deleteBMyCarByIds(request.build());
            result = response.getResult();
        } catch (StatusRuntimeException e) {
            log.error( "deleteBMyCarByIds 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error( "deleteBMyCarByIds 2 FAILED with " + e.getMessage());
        }
        return result;
    }

    /**
     * 设为默认
     *
     * @param bMyCarParam 我的车辆
     * @return 结果
     */
    @Override
    public int setDefault(BMyCarParam bMyCarParam) {
        int result = 0;
        try {
            // 组装查询参数，可以采用protojson工具类转换
            SetDefaultRequest.Builder request = SetDefaultRequest.newBuilder()
                    .setUserId(bMyCarParam.getUserId())
                    .setId(bMyCarParam.getId())
                    .setUpdateBy(bMyCarParam.getUpdateBy());
            SetDefaultResponse response = memberCarServiceBlockingStub.setDefault(request.build());
            result = response.getResult();
        } catch (StatusRuntimeException e) {
            log.error( "setDefault 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error( "setDefault 2 FAILED with " + e.getMessage());
        }
        return result;
    }
}
