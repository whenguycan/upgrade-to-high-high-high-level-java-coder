package com.ruoyi.project.parking.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.LoginUser;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.parking.domain.BParkChargeRelationHoliday;
import com.ruoyi.project.parking.service.IBParkChargeRelationHolidayService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 节假日-区域-车类型-车型-收费规则关联Controller
 * 
 * @author fangch
 * @date 2023-02-23
 */
@RestController
@RequestMapping("/parking/BParkChargeRelationHoliday")
public class BParkChargeRelationHolidayController extends BaseController
{
    @Autowired
    private IBParkChargeRelationHolidayService bParkChargeRelationHolidayService;

    /**
     * 查询节假日-区域-车类型-车型-收费规则关联列表
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRelationHoliday:list')")
    @GetMapping("/list")
    public TableDataInfo list(BParkChargeRelationHoliday bParkChargeRelationHoliday)
    {
        startPage();
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bParkChargeRelationHoliday.setParkNo(loginUser.getUser().getDept().getParkNo());
        List<BParkChargeRelationHoliday> list = bParkChargeRelationHolidayService.selectBParkChargeRelationHolidayList(bParkChargeRelationHoliday);
        return getDataTable(list);
    }

    /**
     * 导出节假日-区域-车类型-车型-收费规则关联列表
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRelationHoliday:export')")
//    @Log(title = "节假日-区域-车类型-车型-收费规则关联", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, BParkChargeRelationHoliday bParkChargeRelationHoliday)
//    {
//        List<BParkChargeRelationHoliday> list = bParkChargeRelationHolidayService.selectBParkChargeRelationHolidayList(bParkChargeRelationHoliday);
//        ExcelUtil<BParkChargeRelationHoliday> util = new ExcelUtil<BParkChargeRelationHoliday>(BParkChargeRelationHoliday.class);
//        util.exportExcel(response, list, "节假日-区域-车类型-车型-收费规则关联数据");
//    }

    /**
     * 获取节假日-区域-车类型-车型-收费规则关联详细信息
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRelationHoliday:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(bParkChargeRelationHolidayService.selectBParkChargeRelationHolidayById(id));
    }

    /**
     * 设置关联关系
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRelationHoliday:add')")
    @Log(title = "设置关联关系", businessType = BusinessType.INSERT)
    @PostMapping("/setRelation")
    public AjaxResult setRelation(@RequestBody List<BParkChargeRelationHoliday> bParkChargeRelationHolidays) {
        if (CollectionUtils.isEmpty(bParkChargeRelationHolidays)) {
            return AjaxResult.error("请至少设置一条关联关系！");
        }
        if (null == bParkChargeRelationHolidays.get(0).getRuleId()
                || StringUtils.isEmpty(bParkChargeRelationHolidays.get(0).getHolidayType())) {
            return AjaxResult.error("收费规则ID和节假日类型不可为空！");
        }
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String parkNo = loginUser.getUser().getDept().getParkNo();
        return AjaxResult.success(bParkChargeRelationHolidayService.setRelation(bParkChargeRelationHolidays, String.valueOf(loginUser.getUserId()), parkNo));
    }

    /**
     * 删除节假日-区域-车类型-车型-收费规则关联
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRelationHoliday:remove')")
    @Log(title = "节假日-区域-车类型-车型-收费规则关联", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult remove(@RequestParam Integer[] ids)
    {
        return toAjax(bParkChargeRelationHolidayService.deleteBParkChargeRelationHolidayByIds(ids));
    }
}
