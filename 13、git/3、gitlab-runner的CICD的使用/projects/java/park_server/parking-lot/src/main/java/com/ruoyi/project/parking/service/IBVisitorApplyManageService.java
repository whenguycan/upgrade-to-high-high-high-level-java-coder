package com.ruoyi.project.parking.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.param.BVisitorApplyParam;
import com.ruoyi.project.parking.domain.vo.BVisitorApplyVO;

import java.util.List;

/**
 * 访客码管理Service接口
 * 
 * @author fangch
 * @date 2023-03-06
 */
public interface IBVisitorApplyManageService {

    /**
     * 查询访客码管理列表
     */
    List<BVisitorApplyVO> selectBVisitorApplyManageList(BVisitorApplyParam param);

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
}
