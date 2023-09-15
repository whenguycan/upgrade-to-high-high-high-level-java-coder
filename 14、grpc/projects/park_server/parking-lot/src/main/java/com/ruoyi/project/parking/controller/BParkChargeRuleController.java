package com.ruoyi.project.parking.controller;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.*;
import com.ruoyi.project.parking.domain.bo.BSettingRegularCarCategoryBO;
import com.ruoyi.project.parking.domain.param.BParkChargeRuleParam;
import com.ruoyi.project.parking.domain.param.BParkChargeRuleTestParam;
import com.ruoyi.project.parking.domain.vo.BParkChargeRelationVehicleVO;
import com.ruoyi.project.parking.domain.vo.BParkChargeRuleVO;
import com.ruoyi.project.parking.entity.SettingCarType;
import com.ruoyi.project.parking.enums.BFieldEnums;
import com.ruoyi.project.parking.enums.BSettingRegularCarCategoryEnums;
import com.ruoyi.project.parking.enums.SettingCarTypeEnums;
import com.ruoyi.project.parking.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 车场收费规则Controller
 * 
 * @author fangch
 * @date 2023-02-21
 */
@RestController
@RequestMapping("/parking/BParkChargeRule")
public class BParkChargeRuleController extends BaseController
{
    @Autowired
    private IBParkChargeRuleService bParkChargeRuleService;

    @Autowired
    private BFieldService bFieldService;

    @Autowired
    private IBSettingRegularCarCategoryService settingRegularCarCategoryService;

    @Autowired
    private ISettingCarTypeService settingCarTypeService;

    @Autowired
    private IBParkChargeRelationVehicleService bParkChargeRelationVehicleService;

    @Autowired
    private IBParkChargeRelationHolidayService bParkChargeRelationHolidayService;

    /**
     * 查询车场收费规则列表
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRule:list')")
    @GetMapping("/list")
    public TableDataInfo list(BParkChargeRule bParkChargeRule)
    {
        startPage();
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bParkChargeRule.setParkNo(loginUser.getUser().getDept().getParkNo());
        List<BParkChargeRuleVO> list = bParkChargeRuleService.selectBParkChargeRuleList(bParkChargeRule);
        return getDataTable(list);
    }

    /**
     * 导出车场收费规则列表
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRule:export')")
//    @Log(title = "车场收费规则", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, BParkChargeRule bParkChargeRule)
//    {
//        List<BParkChargeRule> list = bParkChargeRuleService.selectBParkChargeRuleList(bParkChargeRule);
//        ExcelUtil<BParkChargeRule> util = new ExcelUtil<BParkChargeRule>(BParkChargeRule.class);
//        util.exportExcel(response, list, "车场收费规则数据");
//    }

    /**
     * 获取车场收费规则详细信息
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRule:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(bParkChargeRuleService.selectBParkChargeRuleById(id));
    }

    /**
     * 新增车场收费规则
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRule:add')")
    @Log(title = "车场收费规则", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BParkChargeRuleParam bParkChargeRule)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bParkChargeRule.setCreateBy(String.valueOf(loginUser.getUserId()));
        bParkChargeRule.setParkNo(loginUser.getUser().getDept().getParkNo());
        return bParkChargeRuleService.insertBParkChargeRule(bParkChargeRule);
    }

    /**
     * 修改车场收费规则
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRule:edit')")
    @Log(title = "车场收费规则", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody BParkChargeRuleParam bParkChargeRule)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bParkChargeRule.setUpdateBy(String.valueOf(loginUser.getUserId()));
        bParkChargeRule.setParkNo(loginUser.getUser().getDept().getParkNo());
        return bParkChargeRuleService.updateBParkChargeRule(bParkChargeRule);
    }

    /**
     * 重置车场收费规则
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRule:edit')")
    @Log(title = "重置车场收费规则", businessType = BusinessType.UPDATE)
    @PostMapping("/reset")
    public AjaxResult reset(@RequestBody BParkChargeRuleParam bParkChargeRule) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bParkChargeRule.setUpdateBy(String.valueOf(loginUser.getUserId()));
        bParkChargeRule.setParkNo(loginUser.getUser().getDept().getParkNo());
        return bParkChargeRuleService.updateBParkChargeRule(bParkChargeRule);
    }

    /**
     * 删除车场收费规则
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRule:remove')")
    @Log(title = "车场收费规则", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult remove(@RequestParam Integer[] ids)
    {
        List<Integer> idList = Arrays.asList(ids);
        for (Integer id : idList) {
            BParkChargeRelationVehicle bParkChargeRelationVehicle = new BParkChargeRelationVehicle();
            // 获取当前的用户
            LoginUser loginUser = SecurityUtils.getLoginUser();
            bParkChargeRelationVehicle.setParkNo(loginUser.getUser().getDept().getParkNo());
            bParkChargeRelationVehicle.setRuleId(id);
            List<BParkChargeRelationVehicleVO> list1 = bParkChargeRelationVehicleService.selectBParkChargeRelationVehicleList(bParkChargeRelationVehicle);
            BParkChargeRelationHoliday bParkChargeRelationHoliday = new BParkChargeRelationHoliday();
            bParkChargeRelationHoliday.setParkNo(loginUser.getUser().getDept().getParkNo());
            bParkChargeRelationHoliday.setRuleId(id);
            List<BParkChargeRelationHoliday> list2 = bParkChargeRelationHolidayService.selectBParkChargeRelationHolidayList(bParkChargeRelationHoliday);
            if (CollectionUtils.isNotEmpty(list1) || CollectionUtils.isNotEmpty(list2)) {
                String ruleName = bParkChargeRuleService.selectBParkChargeRuleById(id).getRuleName();
                return AjaxResult.error(ruleName + "已设置关联关系，不可删除");
            }
        }
        return toAjax(bParkChargeRuleService.deleteBParkChargeRuleByIds(ids));
    }

    /**
     * 测试车场收费规则
     */
//    @PreAuthorize("@ss.hasPermi('parking:BParkChargeRule:query')")
    @GetMapping("/testParkRate")
    public AjaxResult testParkRate(@RequestBody BParkChargeRuleTestParam bParkChargeRuleTestParam) {
        if (null == bParkChargeRuleTestParam.getId() || bParkChargeRuleTestParam.getId() <= 0) {
            return AjaxResult.error("请选择收费规则！");
        }
        return success(bParkChargeRuleService.testParkRate(bParkChargeRuleTestParam));
    }

    /**
     * 获取停车场区域列表
     *
     * @param bfield 筛选条件
     * @param
     * @return 查询结果
     */
    @ApiOperation("获取停车场区域列表")
    @GetMapping("/bFieldList")
    public TableDataInfo bFieldList(BField bfield) {
        startPage();
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bfield.setParkNo(loginUser.getUser().getDept().getParkNo());
        bfield.setFieldStatus(BFieldEnums.FIELD_STATUS.ACTIVATED.getValue());
        List<BField> list = bParkChargeRuleService.bFieldList(bfield);
        return getDataTable(list);
    }

    /**
     * @apiNote 获取固定车类型列表
     */
    @GetMapping("/regularCarCategoryList")
    public TableDataInfo regularCarCategoryList(BSettingRegularCarCategory settingRegularCarCategory) {
        startPage();
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        settingRegularCarCategory.setParkNo(loginUser.getUser().getDept().getParkNo());
        settingRegularCarCategory.setStatus(BSettingRegularCarCategoryEnums.STATUS.ACTIVATED.getValue());
        settingRegularCarCategory.setDelFlag(BSettingRegularCarCategoryEnums.DEL_FLAG.NORMAL.getValue());
        List<BSettingRegularCarCategoryBO> list = bParkChargeRuleService.regularCarCategoryList(settingRegularCarCategory);
        return getDataTable(list);
    }

    /**
     * 获取车型列表
     *
     * @param settingCarType
     * @return
     */
    @GetMapping("/carTypeList")
    public TableDataInfo carTypeList(SettingCarType settingCarType) {
        startPage();
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        settingCarType.setParkNo(loginUser.getUser().getDept().getParkNo());
        settingCarType.setStatus(SettingCarTypeEnums.STATUS.ACTIVATED.getValue());
        settingCarType.setDelFlag(SettingCarTypeEnums.DEL_FLAG.NORMAL.getValue());
        List<SettingCarType> list = bParkChargeRuleService.carTypeList(settingCarType);
        return getDataTable(list);
    }
}
