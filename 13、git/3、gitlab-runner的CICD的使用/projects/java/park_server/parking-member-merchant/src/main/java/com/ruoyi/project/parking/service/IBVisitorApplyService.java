package com.ruoyi.project.parking.service;

import com.google.protobuf.ProtocolStringList;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.BVisitorApply;
import com.ruoyi.project.parking.domain.bo.VisitorApplySheetBO;
import com.ruoyi.project.parking.domain.param.BVisitorApplyParam;
import com.ruoyi.project.parking.domain.vo.BVisitorApplyVO;

import java.util.List;

/**
 * 停车预约申请Service接口
 * 
 * @author fangch
 * @date 2023-03-07
 */
public interface IBVisitorApplyService 
{
    /**
     * 查询停车预约申请
     * 
     * @param id 停车预约申请主键
     * @return 停车预约申请
     */
    BVisitorApplyVO selectBVisitorApplyById(Integer id);

    /**
     * 查询停车预约申请列表
     * 
     * @param bVisitorApply 停车预约申请
     * @return 停车预约申请集合
     */
    List<BVisitorApplyVO> selectBVisitorApplyList(BVisitorApply bVisitorApply);

    /**
     * 查询停车预约申请列表
     *
     * @param bVisitorApply 停车预约申请
     * @return 停车预约申请集合
     */
    List<BVisitorApplyVO> getBVisitorApplyList(BVisitorApplyParam bVisitorApply);

    /**
     * 查询访客待审批数
     *
     * @param parkNos
     * @return
     */
    Integer getNotDisposedCount(List<String> parkNos);

    /**
     * 新增停车预约申请
     * 
     * @param bVisitorApply 停车预约申请
     * @return 结果
     */
    AjaxResult insertBVisitorApply(BVisitorApply bVisitorApply);

    /**
     * 修改停车预约申请
     * 
     * @param bVisitorApply 停车预约申请
     * @return 结果
     */
    AjaxResult updateBVisitorApply(BVisitorApply bVisitorApply);

    /**
     * 批量审核和驳回
     *
     * @param ids
     * @param status
     * @param rejectReason
     * @param updateBy
     * @return
     */
    AjaxResult updateStatus(Integer[] ids, String status, String rejectReason, String updateBy);

    /**
     * 批量删除停车预约申请
     * 
     * @param ids 需要删除的停车预约申请主键集合
     * @return 结果
     */
    int deleteBVisitorApplyByIds(Integer[] ids);

    /**
     * 删除停车预约申请信息
     * 
     * @param id 停车预约申请主键
     * @return 结果
     */
    int deleteBVisitorApplyById(Integer id);

    AjaxResult getCodeInfo(String code);

    /**
     * 查询指定日期访客数量
     */
    int countBVisitorApply(String day, String status, ProtocolStringList parkNosList);

    List<VisitorApplySheetBO> statisticsSheet(String parkNo, String startDate, String endDate);
}
