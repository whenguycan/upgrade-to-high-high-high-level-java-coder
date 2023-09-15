package com.ruoyi.project.parking.service.impl;

import com.czdx.grpc.lib.lot.*;
import com.google.protobuf.ProtocolStringList;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.BVisitorApply;
import com.ruoyi.project.parking.domain.bo.VisitorApplySheetBO;
import com.ruoyi.project.parking.domain.param.BVisitorApplyParam;
import com.ruoyi.project.parking.domain.vo.BVisitorApplyVO;
import com.ruoyi.project.parking.domain.vo.BVisitorCodeManageVO;
import com.ruoyi.project.parking.enums.BVisitorApplyManageEnums;
import com.ruoyi.project.parking.enums.BVisitorCodeManageEnums;
import com.ruoyi.project.parking.mapper.BVisitorApplyMapper;
import com.ruoyi.project.parking.service.IBVisitorApplyService;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 停车预约申请Service业务层处理
 *
 * @author fangch
 * @date 2023-03-07
 */
@Slf4j
@Service
public class BVisitorApplyServiceImpl implements IBVisitorApplyService {
    @Autowired
    private BVisitorApplyMapper bVisitorApplyMapper;

    @GrpcClient("parking-lot-server")
    private BVisitorCodeManageServiceGrpc.BVisitorCodeManageServiceBlockingStub bVisitorCodeManageServiceBlockingStub;

    /**
     * 查询停车预约申请
     *
     * @param id 停车预约申请主键
     * @return 停车预约申请
     */
    @Override
    public BVisitorApplyVO selectBVisitorApplyById(Integer id) {
        return bVisitorApplyMapper.selectBVisitorApplyById(id);
    }

    /**
     * 查询停车预约申请列表
     *
     * @param bVisitorApply 停车预约申请
     * @return 停车预约申请
     */
    @Override
    public List<BVisitorApplyVO> selectBVisitorApplyList(BVisitorApply bVisitorApply) {
        return bVisitorApplyMapper.selectBVisitorApplyList(bVisitorApply);
    }

    /**
     * 查询停车预约申请列表
     *
     * @param bVisitorApply 停车预约申请
     * @return 停车预约申请
     */
    @Override
    public List<BVisitorApplyVO> getBVisitorApplyList(BVisitorApplyParam bVisitorApply) {
        return bVisitorApplyMapper.getBVisitorApplyList(bVisitorApply);
    }

    /**
     * 查询访客待审批数
     *
     * @param parkNos
     * @param status
     * @return
     */
    @Override
    public Integer getNotDisposedCount(List<String> parkNos) {
        return bVisitorApplyMapper.getNotDisposedCount(parkNos, BVisitorApplyManageEnums.STATUS.NOT_AUDIT.getValue());
    }

    /**
     * 新增停车预约申请
     *
     * @param bVisitorApply 停车预约申请
     * @return 结果
     */
    @Override
    public AjaxResult insertBVisitorApply(BVisitorApply bVisitorApply) {
        if (StringUtils.isEmpty(bVisitorApply.getCode())) {
            return AjaxResult.warn("访客码不可为空");
        }
        AjaxResult ajaxResult = getCodeInfo(bVisitorApply.getCode());
        if (ajaxResult.get("code").equals(HttpStatus.WARN)) {
            return AjaxResult.warn(String.valueOf(ajaxResult.get("msg")));
        } else {
            BVisitorCodeManageVO codeManageVO = (BVisitorCodeManageVO) ajaxResult.get("data");
            Date startTime = codeManageVO.getStartTime();
            Date endTime = codeManageVO.getEndTime();
            if (bVisitorApply.getDay() == null) {
                return AjaxResult.warn("来访日期不可为空");
            }
            Date now = bVisitorApply.getDay();
            if ((startTime.after(now) || endTime.before(now)) && codeManageVO.getTimeLimit().equals(BVisitorCodeManageEnums.TIME_LIMIT.NO.getValue())) {
                return AjaxResult.warn("来访日期不在访客码有效期内");
            } else {
                bVisitorApply.setCreateTime(LocalDateTime.now());
                int result = bVisitorApplyMapper.insertBVisitorApply(bVisitorApply);
                return result > 0 ? AjaxResult.success() : AjaxResult.error();
            }
        }
    }

    /**
     * 修改停车预约申请
     *
     * @param bVisitorApply 停车预约申请
     * @return 结果
     */
    @Override
    public AjaxResult updateBVisitorApply(BVisitorApply bVisitorApply) {
        if (bVisitorApply.getId() == null) {
            return AjaxResult.warn("逻辑ID不可为空");
        }
        int cnt = bVisitorApplyMapper.checkStatus(bVisitorApply.getId(), BVisitorApplyManageEnums.STATUS.APPROVED.getValue());
        if (cnt > 0) {
            return AjaxResult.warn("不可修改" + BVisitorApplyManageEnums.STATUS.APPROVED.getDesc() + "的数据");
        } else {
            BVisitorApplyVO applyVO = bVisitorApplyMapper.selectBVisitorApplyById(bVisitorApply.getId());
            AjaxResult ajaxResult = getCodeInfo(applyVO.getCode());
            if (ajaxResult.get("code").equals(HttpStatus.WARN)) {
                return AjaxResult.warn(String.valueOf(ajaxResult.get("msg")));
            } else {
                BVisitorCodeManageVO codeManageVO = (BVisitorCodeManageVO) ajaxResult.get("data");
                Date startTime = codeManageVO.getStartTime();
                Date endTime = codeManageVO.getEndTime();
                Date now = bVisitorApply.getDay();
                if ((startTime.after(now) || endTime.before(now)) && codeManageVO.getTimeLimit().equals(BVisitorCodeManageEnums.TIME_LIMIT.NO.getValue())) {
                    return AjaxResult.warn("来访日期不在访客码有效期内");
                } else {
                    bVisitorApply.setStatus(BVisitorApplyManageEnums.STATUS.NOT_AUDIT.getValue());
                    bVisitorApply.setUpdateTime(LocalDateTime.now());
                    int result = bVisitorApplyMapper.updateBVisitorApply(bVisitorApply);
                    return result > 0 ? AjaxResult.success() : AjaxResult.error();
                }
            }
        }
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
    @Transactional
    public AjaxResult updateStatus(Integer[] ids, String status, String rejectReason, String updateBy) {
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (int i = 0; i < ids.length; i++) {
            int cnt = bVisitorApplyMapper.checkStatus(ids[i], BVisitorApplyManageEnums.STATUS.NOT_AUDIT.getValue());
            if (cnt <= 0) { // 审核中才可以操作
                failureNum++;
                failureMsg.append("<br/>第").append(i + 1).append("条数据：只可操作").append(BVisitorApplyManageEnums.STATUS.NOT_AUDIT.getDesc()).append("的数据");
            } else if (status.equals(BVisitorApplyManageEnums.STATUS.APPROVED.getValue())) { // 审核通过校验和处理
                BVisitorApplyVO applyVO = bVisitorApplyMapper.selectBVisitorApplyById(ids[i]);
                AjaxResult ajaxResult = getCodeInfo(applyVO.getCode());
                if (ajaxResult.get("code").equals(HttpStatus.WARN)) { // 访客码校验提示
                    failureNum++;
                    failureMsg.append("<br/>第").append(i + 1).append("条数据：").append(ajaxResult.get("msg"));
                } else {
                    BVisitorCodeManageVO codeManageVO = (BVisitorCodeManageVO) ajaxResult.get("data");
                    Date startTime = codeManageVO.getStartTime();
                    Date endTime = codeManageVO.getEndTime();
                    Date now = applyVO.getDay();
                    if ((startTime.after(now) || endTime.before(now)) && codeManageVO.getTimeLimit().equals(BVisitorCodeManageEnums.TIME_LIMIT.NO.getValue())) { // 来访日期校验
                        failureNum++;
                        failureMsg.append("<br/>第").append(i + 1).append("条数据：来访日期不在访客码有效期内");
                    } else {
                        int result = bVisitorApplyMapper.updateStatus(ids[i], status, rejectReason, updateBy, LocalDateTime.now());
                        int numberRes = 0;
                        int whiteRes = 0;
                        // 组装更新绑定次数参数
                        UpdateNumberRequestProto request = UpdateNumberRequestProto.newBuilder()
                                .setId(codeManageVO.getId())
                                .setCodeUsedNumber((codeManageVO.getCodeUsedNumber() == null ? 0 : codeManageVO.getCodeUsedNumber()) + 1)
                                .setUpdateBy(updateBy)
                                .build();
                        UpdateNumberResponseProto response = bVisitorCodeManageServiceBlockingStub.updateNumber(request);
                        numberRes = response.getResult();
                        // 组装设置白名单参数
                        Calendar rightNow = Calendar.getInstance();
                        rightNow.setTime(applyVO.getDay());
                        rightNow.add(Calendar.DAY_OF_YEAR, codeManageVO.getCodeFreeDay());
                        String deadline = "";
                        if (codeManageVO.getTimeLimit().equals(BVisitorCodeManageEnums.TIME_LIMIT.NO.getValue())) {
                            deadline = DateUtils.dateTime(rightNow.getTime().after(codeManageVO.getEndTime()) ? codeManageVO.getEndTime() : rightNow.getTime());
                        } else {
                            deadline = "9999-12-31";
                        }
                        AddWhiteListRequestProto addWhiteListRequestProto = AddWhiteListRequestProto.newBuilder()
                                .setCarNumber(applyVO.getCarNo())
                                .setRemark("")
                                .setStartTime(DateUtils.dateTime(applyVO.getDay()))
                                .setEndTime(deadline)
                                .setParkNo(applyVO.getParkNo())
                                .setCreateBy(updateBy)
                                .build();
                        AddWhiteListResponseProto addWhiteListResponseProto = bVisitorCodeManageServiceBlockingStub.addWhiteList(addWhiteListRequestProto);
                        whiteRes = addWhiteListResponseProto.getResult() ? 1 : 0;
                        if (result <= 0 || numberRes <= 0 || whiteRes <= 0) {
                            failureNum++;
                            failureMsg.append("<br/>第").append(i + 1).append("条数据：操作异常");
                        } else {
                            successNum += result;
                        }
                    }
                }
            } else { // 驳回处理
                int result = bVisitorApplyMapper.updateStatus(ids[i], status, rejectReason, updateBy, LocalDateTime.now());
                if (result <= 0) {
                    failureNum++;
                    failureMsg.append("<br/>第").append(i + 1).append("条数据：操作异常");
                } else {
                    successNum += result;
                }
            }
        }
        successMsg.append("成功").append(successNum).append("条，")
                .append("失败").append(failureNum).append("条。")
                .append(failureNum > 0 ? "原因如下：" : "")
                .append(failureNum > 0 ? failureMsg.toString() : "");
        return failureNum > 0 ? new AjaxResult(HttpStatus.WARN_BATCH, successMsg.toString()) : AjaxResult.success(successMsg.toString());
    }

    /**
     * 批量删除停车预约申请
     *
     * @param ids 需要删除的停车预约申请主键
     * @return 结果
     */
    @Override
    public int deleteBVisitorApplyByIds(Integer[] ids) {
        return bVisitorApplyMapper.deleteBVisitorApplyByIds(ids);
    }

    /**
     * 删除停车预约申请信息
     *
     * @param id 停车预约申请主键
     * @return 结果
     */
    @Override
    public int deleteBVisitorApplyById(Integer id) {
        return bVisitorApplyMapper.deleteBVisitorApplyById(id);
    }

    /**
     * 获取访客码信息
     *
     * @param code 访客码
     * @return 访客码信息
     */
    @Override
    public AjaxResult getCodeInfo(String code) {
        BVisitorCodeManageVO result = new BVisitorCodeManageVO();
        try {
            // 组装查询参数，可以采用protojson工具类转换
            GetCodeInfoRequestProto request = GetCodeInfoRequestProto.newBuilder()
                    .setCode(code)
                    .build();
            GetCodeInfoResponseProto response = bVisitorCodeManageServiceBlockingStub.getCodeInfo(request);
            result = ProtoJsonUtil.toPojoBean(BVisitorCodeManageVO.class, response);
            if (BVisitorCodeManageEnums.STATUS.DEACTIVATED.getValue().equals(result.getStatus())) {
                return AjaxResult.warn("该访客码已停用");
            }
            Date startTime = result.getStartTime();
            Date endTime = result.getEndTime();
            Date now = new Date();
            if ((startTime.after(now) || endTime.before(now)) && result.getTimeLimit().equals(BVisitorCodeManageEnums.TIME_LIMIT.NO.getValue())) {
                return AjaxResult.warn("该访客码已过期");
            }
            if (null == result.getCodeUseNumber() || (null != result.getCodeUseNumber() && null != result.getCodeUsedNumber() && result.getCodeUseNumber() == result.getCodeUsedNumber())) {
                return AjaxResult.warn("该访客码绑定次数已用完");
            }
        } catch (StatusRuntimeException e) {
            log.error("FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error("FAILED with " + e.getMessage());
        }
        if (StringUtils.isEmpty(result.getParkNo())) {
            return AjaxResult.warn("该访客码不存在");
        }
        return AjaxResult.success(result);
    }

    /**
     * 查询指定日期访客数量
     */
    @Override
    public int countBVisitorApply(String day, String status, ProtocolStringList parkNos) {
        return bVisitorApplyMapper.countBVisitorApply(day, status, parkNos);
    }

    @Override
    public List<VisitorApplySheetBO> statisticsSheet(String parkNo, String startDate, String endDate) {
        return bVisitorApplyMapper.statisticsSheet(parkNo,startDate,endDate);
    }
}
