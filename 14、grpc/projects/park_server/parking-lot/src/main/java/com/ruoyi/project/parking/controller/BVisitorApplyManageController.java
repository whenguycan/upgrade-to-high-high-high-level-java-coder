package com.ruoyi.project.parking.controller;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.param.BVisitorApplyParam;
import com.ruoyi.project.parking.domain.vo.BVisitorApplyVO;
import com.ruoyi.project.parking.enums.BVisitorApplyManageEnums;
import com.ruoyi.project.parking.service.IBVisitorApplyManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 访客预约管理Controller
 * 
 * @author fangch
 * @date 2023-03-10
 */
@RestController
@RequestMapping("/parking/BVisitorApplyManage")
public class BVisitorApplyManageController extends BaseController
{
    @Autowired
    private IBVisitorApplyManageService bVisitorApplyManageService;

    /**
     * 查询访客码管理列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BVisitorApplyParam param) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        param.setParkNo(loginUser.getUser().getDept().getParkNo());
        List<BVisitorApplyVO> list = bVisitorApplyManageService.selectBVisitorApplyManageList(param);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        param.setPageNum(null);
        param.setPageSize(null);
        rspData.setTotal(bVisitorApplyManageService.selectBVisitorApplyManageList(param).size());
        rspData.setRows(list);
        return rspData;
    }

    /**
     * 批量审核
     */
    @Log(title = "批量审核", businessType = BusinessType.UPDATE)
    @GetMapping("/approve/{ids}")
    public AjaxResult approve(@PathVariable @NotNull(message = "逻辑ID不可为空") Integer[] ids) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        return bVisitorApplyManageService.updateStatus(ids, BVisitorApplyManageEnums.STATUS.APPROVED.getValue(), "", String.valueOf(loginUser.getUserId()));
    }

    /**
     * 批量驳回
     */
    @Log(title = "批量驳回", businessType = BusinessType.UPDATE)
    @GetMapping("/reject")
    public AjaxResult approve(@NotNull(message = "逻辑ID不可为空") Integer[] ids, String rejectReason) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        return bVisitorApplyManageService.updateStatus(ids, BVisitorApplyManageEnums.STATUS.REJECTED.getValue(), rejectReason, String.valueOf(loginUser.getUserId()));
    }
}
