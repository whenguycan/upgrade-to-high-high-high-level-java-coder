package com.ruoyi.project.parking.controller;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.BVisitorApply;
import com.ruoyi.project.parking.domain.vo.BVisitorApplyVO;
import com.ruoyi.project.parking.service.IBVisitorApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 停车预约申请Controller
 * 
 * @author fangch
 * @date 2023-03-07
 */
@RestController
@RequestMapping("/parking/BVisitorApply")
public class BVisitorApplyController extends BaseController
{
    @Autowired
    private IBVisitorApplyService bVisitorApplyService;

    /**
     * 查询停车预约申请列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BVisitorApply bVisitorApply)
    {
        startPage();
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bVisitorApply.setCreateBy(String.valueOf(loginUser.getUserId()));
        List<BVisitorApplyVO> list = bVisitorApplyService.selectBVisitorApplyList(bVisitorApply);
        return getDataTable(list);
    }

    /**
     * 导出停车预约申请列表
     */
//    @Log(title = "停车预约申请", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, BVisitorApply bVisitorApply)
//    {
//        List<BVisitorApply> list = bVisitorApplyService.selectBVisitorApplyList(bVisitorApply);
//        ExcelUtil<BVisitorApply> util = new ExcelUtil<BVisitorApply>(BVisitorApply.class);
//        util.exportExcel(response, list, "停车预约申请数据");
//    }

    /**
     * 获取停车预约申请详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(bVisitorApplyService.selectBVisitorApplyById(id));
    }

    /**
     * 新增停车预约申请
     */
    @Log(title = "停车预约申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BVisitorApply bVisitorApply)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bVisitorApply.setCreateBy(String.valueOf(loginUser.getUserId()));
        return bVisitorApplyService.insertBVisitorApply(bVisitorApply);
    }

    /**
     * 修改停车预约申请
     */
    @Log(title = "停车预约申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody BVisitorApply bVisitorApply)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bVisitorApply.setUpdateBy(String.valueOf(loginUser.getUserId()));
        return bVisitorApplyService.updateBVisitorApply(bVisitorApply);
    }

    /**
     * 删除停车预约申请
     */
//    @Log(title = "停车预约申请", businessType = BusinessType.DELETE)
//    @PostMapping("/delete/{ids}")
//    public AjaxResult remove(@PathVariable Integer[] ids)
//    {
//        return toAjax(bVisitorApplyService.deleteBVisitorApplyByIds(ids));
//    }

    /**
     * 获取访客码信息
     */
    @GetMapping(value = "/getCodeInfo/{code}")
    public AjaxResult getCodeInfo(@PathVariable("code") String code) {
        if (StringUtils.isEmpty(code)) {
            return AjaxResult.warn("访客码不可为空");
        }
        return bVisitorApplyService.getCodeInfo(code);
    }
}
