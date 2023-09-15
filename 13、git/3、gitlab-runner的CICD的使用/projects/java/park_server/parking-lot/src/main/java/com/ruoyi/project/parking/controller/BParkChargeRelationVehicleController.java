package com.ruoyi.project.parking.controller;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.BParkChargeRelationVehicle;
import com.ruoyi.project.parking.domain.vo.BParkChargeRelationVehicleVO;
import com.ruoyi.project.parking.service.IBParkChargeRelationVehicleService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 区域-车类型-车型-收费规则关联Controller
 * 
 * @author fangch
 * @date 2023-02-23
 */
@RestController
@RequestMapping("/parking/BParkChargeRelationVehicle")
public class BParkChargeRelationVehicleController extends BaseController
{
    @Autowired
    private IBParkChargeRelationVehicleService bParkChargeRelationVehicleService;

    /**
     * 查询区域-车类型-车型-收费规则关联列表
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRelationVehicle:list')")
    @GetMapping("/list")
    public TableDataInfo list(BParkChargeRelationVehicle bParkChargeRelationVehicle)
    {
        startPage();
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bParkChargeRelationVehicle.setParkNo(loginUser.getUser().getDept().getParkNo());
        List<BParkChargeRelationVehicleVO> list = bParkChargeRelationVehicleService.selectBParkChargeRelationVehicleList(bParkChargeRelationVehicle);
        return getDataTable(list);
    }

    /**
     * 导出区域-车类型-车型-收费规则关联列表
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRelationVehicle:export')")
//    @Log(title = "区域-车类型-车型-收费规则关联", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, BParkChargeRelationVehicle bParkChargeRelationVehicle)
//    {
//        List<BParkChargeRelationVehicle> list = bParkChargeRelationVehicleService.selectBParkChargeRelationVehicleList(bParkChargeRelationVehicle);
//        ExcelUtil<BParkChargeRelationVehicle> util = new ExcelUtil<BParkChargeRelationVehicle>(BParkChargeRelationVehicle.class);
//        util.exportExcel(response, list, "区域-车类型-车型-收费规则关联数据");
//    }

    /**
     * 获取区域-车类型-车型-收费规则关联详细信息
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRelationVehicle:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(bParkChargeRelationVehicleService.selectBParkChargeRelationVehicleById(id));
    }

    /**
     * 设置关联关系
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRelationVehicle:add')")
    @Log(title = "设置关联关系", businessType = BusinessType.INSERT)
    @PostMapping("/setRelation")
    public AjaxResult setRelation(@RequestBody List<BParkChargeRelationVehicle> bParkChargeRelationVehicles) {
        if (CollectionUtils.isEmpty(bParkChargeRelationVehicles)) {
            return AjaxResult.error("请至少设置一条关联关系！");
        }
        if (null == bParkChargeRelationVehicles.get(0).getRuleId()) {
            return AjaxResult.error("收费规则ID不可为空！");
        }
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String parkNo = loginUser.getUser().getDept().getParkNo();
        return bParkChargeRelationVehicleService.setRelation(bParkChargeRelationVehicles, String.valueOf(loginUser.getUserId()), parkNo);
    }

    /**
     * 删除区域-车类型-车型-收费规则关联
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRelationVehicle:remove')")
    @Log(title = "区域-车类型-车型-收费规则关联", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult remove(@RequestParam Integer[] ids)
    {
        return toAjax(bParkChargeRelationVehicleService.deleteBParkChargeRelationVehicleByIds(ids));
    }

    /**
     * 查询未关联列表
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRelationVehicle:list')")
    @GetMapping("/notRelatedList")
    public AjaxResult notRelatedList()
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String parkNo = loginUser.getUser().getDept().getParkNo();
        return AjaxResult.success(bParkChargeRelationVehicleService.notRelatedList(parkNo));
    }
}
