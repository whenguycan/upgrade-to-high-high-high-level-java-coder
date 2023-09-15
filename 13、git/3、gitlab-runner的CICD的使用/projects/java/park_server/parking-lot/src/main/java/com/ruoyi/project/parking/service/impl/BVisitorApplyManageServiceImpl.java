package com.ruoyi.project.parking.service.impl;

import com.czdx.grpc.lib.merchant.*;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.param.BVisitorApplyParam;
import com.ruoyi.project.parking.domain.vo.BVisitorApplyVO;
import com.ruoyi.project.parking.domain.vo.BVisitorCodeManageVO;
import com.ruoyi.project.parking.service.IBVisitorApplyManageService;
import com.ruoyi.project.parking.service.IBVisitorCodeManageService;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 访客码管理Service业务层处理
 *
 * @author fangch
 * @date 2023-03-06
 */
@Slf4j
@Service
public class BVisitorApplyManageServiceImpl implements IBVisitorApplyManageService {

    @GrpcClient("parking-member-merchant-server")
    private VisitorApplyServiceGrpc.VisitorApplyServiceBlockingStub visitorApplyServiceBlockingStub;

    @Autowired
    private IBVisitorCodeManageService ibVisitorCodeManageService;

    /**
     * 查询访客码管理列表
     */
    @Override
    public List<BVisitorApplyVO> selectBVisitorApplyManageList(BVisitorApplyParam param) {
        List<BVisitorApplyVO> result = new ArrayList<>();
        try {
            // 组装查询参数，可以采用protojson工具类转换
            SelectBVisitorApplyListRequest.Builder request = SelectBVisitorApplyListRequest.newBuilder();
            ProtoJsonUtil.toProtoBean(request, param);
            SelectBVisitorApplyListResponse response = visitorApplyServiceBlockingStub.selectBVisitorApplyList(request.build());
            List<BVisitorApplyVOProto> visitorApplyVOProtoList = response.getRowsList();
            visitorApplyVOProtoList.forEach(item -> {
                try {
                    BVisitorApplyVO applyVO = ProtoJsonUtil.toPojoBean(BVisitorApplyVO.class, item);
                    BVisitorCodeManageVO codeManageVO = ibVisitorCodeManageService.getCodeInfo(item.getCode());
                    applyVO.setCodeName(null == codeManageVO ? "" : codeManageVO.getCodeName());
                    result.add(applyVO);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (StatusRuntimeException e) {
            log.error( "selectBVisitorApplyManageList 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error( "selectBVisitorApplyManageList 2 FAILED with " + e.getMessage());
        }
        return result;
    }

    /**
     * 批量审核和驳回
     *
     * @param ids
     * @param status
     * @param rejectReason
     * @param updateBy
     * @return
     */
    @Override
    public AjaxResult updateStatus(Integer[] ids, String status, String rejectReason, String updateBy){
        AjaxResult result = null;
        try {
            // 组装查询参数，可以采用protojson工具类转换
            UpdateStatusRequest.Builder request = UpdateStatusRequest.newBuilder()
                    .addAllIds(Arrays.asList(ids))
                    .setStatus(status)
                    .setRejectReason(StringUtils.isEmpty(rejectReason) ? "" : rejectReason)
                    .setUpdateBy(updateBy);
            UpdateStatusResponse response = visitorApplyServiceBlockingStub.updateStatus(request.build());
            result = new AjaxResult(response.getCode(), response.getMsg(), response.getData());
        } catch (StatusRuntimeException e) {
            log.error( "updateStatus 1 FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error( "updateStatus 2 FAILED with " + e.getMessage());
        }
        return result;
    }
}
