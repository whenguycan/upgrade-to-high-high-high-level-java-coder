package com.ruoyi.project.parking.controller;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.BVisitorCodeManage;
import com.ruoyi.project.parking.domain.param.BVisitorCodeManageQueryParam;
import com.ruoyi.project.parking.domain.param.BVisitorCodeUpdateStatusParam;
import com.ruoyi.project.parking.enums.BVisitorCodeManageEnums;
import com.ruoyi.project.parking.service.IBVisitorCodeManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 访客码管理Controller
 * 
 * @author fangch
 * @date 2023-03-06
 */
@RestController
@RequestMapping("/parking/BVisitorCodeManage")
public class BVisitorCodeManageController extends BaseController
{
    @Autowired
    private IBVisitorCodeManageService bVisitorCodeManageService;

    /**
     * 查询访客码管理列表
     */
//    @PreAuthorize("@ss.hasPermi('parking:BVisitorCodeManage:list')")
    @GetMapping("/list")
    public TableDataInfo list(BVisitorCodeManageQueryParam param)
    {
        startPage();
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        param.setParkNo(loginUser.getUser().getDept().getParkNo());
        List<BVisitorCodeManage> list = bVisitorCodeManageService.selectBVisitorCodeManageList(param);
        return getDataTable(list);
    }

    /**
     * 导出访客码管理列表
     */
//    @PreAuthorize("@ss.hasPermi('parking:BVisitorCodeManage:export')")
    @Log(title = "访客码管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BVisitorCodeManageQueryParam param)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        param.setParkNo(loginUser.getUser().getDept().getParkNo());
        List<BVisitorCodeManage> list = bVisitorCodeManageService.selectBVisitorCodeManageList(param);
        ExcelUtil<BVisitorCodeManage> util = new ExcelUtil<BVisitorCodeManage>(BVisitorCodeManage.class);
        util.exportExcel(response, list, "访客码管理数据");
    }

    /**
     * 导入访客码
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Log(title = "访客码管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<BVisitorCodeManage> util = new ExcelUtil<BVisitorCodeManage>(BVisitorCodeManage.class);
        List<BVisitorCodeManage> codeManageList = util.importExcel(file.getInputStream());
        String userId = String.valueOf(getUserId());
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String parkNo = loginUser.getUser().getDept().getParkNo();
        String message = bVisitorCodeManageService.importData(codeManageList, userId, parkNo);
        return success(message);
    }

    /**
     * 下载模板
     *
     * @param response
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<BVisitorCodeManage> util = new ExcelUtil<BVisitorCodeManage>(BVisitorCodeManage.class);
        util.importTemplateExcel(response, "访客码数据");
    }

    /**
     * 获取访客码管理详细信息
     */
//    @PreAuthorize("@ss.hasPermi('parking:BVisitorCodeManage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(bVisitorCodeManageService.selectBVisitorCodeManageById(id));
    }

    /**
     * 新增访客码管理
     */
//    @PreAuthorize("@ss.hasPermi('parking:BVisitorCodeManage:add')")
    @Log(title = "访客码管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody BVisitorCodeManage bVisitorCodeManage)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bVisitorCodeManage.setCreateBy(String.valueOf(loginUser.getUserId()));
        bVisitorCodeManage.setParkNo(loginUser.getUser().getDept().getParkNo());
        return bVisitorCodeManageService.insertBVisitorCodeManage(bVisitorCodeManage);
    }

    /**
     * 修改访客码管理
     */
//    @PreAuthorize("@ss.hasPermi('parking:BVisitorCodeManage:edit')")
    @Log(title = "访客码管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody BVisitorCodeManage bVisitorCodeManage)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bVisitorCodeManage.setUpdateBy(String.valueOf(loginUser.getUserId()));
        bVisitorCodeManage.setParkNo(loginUser.getUser().getDept().getParkNo());
        return bVisitorCodeManageService.updateBVisitorCodeManage(bVisitorCodeManage);
    }

    /**
     * 启用访客码
     */
//    @PreAuthorize("@ss.hasPermi('parking:BVisitorCodeManage:edit')")
    @Log(title = "启用访客码", businessType = BusinessType.UPDATE)
    @GetMapping("/activate/{id}")
    public AjaxResult activate(@PathVariable @NotNull(message = "逻辑ID不可为空") Integer id)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        BVisitorCodeUpdateStatusParam param = new BVisitorCodeUpdateStatusParam();
        param.setId(id);
        param.setUpdateBy(String.valueOf(loginUser.getUserId()));
        param.setParkNo(loginUser.getUser().getDept().getParkNo());
        param.setStatus(BVisitorCodeManageEnums.STATUS.ACTIVATED.getValue());
        return bVisitorCodeManageService.updateStatus(param);
    }

    /**
     * 停用访客码
     */
//    @PreAuthorize("@ss.hasPermi('parking:BVisitorCodeManage:edit')")
    @Log(title = "停用访客码", businessType = BusinessType.UPDATE)
    @GetMapping("/deactivate/{id}")
    public AjaxResult deactivate(@PathVariable @NotNull(message = "逻辑ID不可为空") Integer id)
    {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        BVisitorCodeUpdateStatusParam param = new BVisitorCodeUpdateStatusParam();
        param.setId(id);
        param.setUpdateBy(String.valueOf(loginUser.getUserId()));
        param.setParkNo(loginUser.getUser().getDept().getParkNo());
        param.setStatus(BVisitorCodeManageEnums.STATUS.DEACTIVATED.getValue());
        return bVisitorCodeManageService.updateStatus(param);
    }

    /**
     * 删除访客码管理
     */
//    @PreAuthorize("@ss.hasPermi('parking:BVisitorCodeManage:remove')")
    @Log(title = "访客码管理", businessType = BusinessType.DELETE)
	@GetMapping("/delete/{ids}")
    public AjaxResult remove(@PathVariable @NotNull(message = "逻辑ID不可为空") Integer[] ids) {
        return bVisitorCodeManageService.deleteBVisitorCodeManageById(ids[0]);
    }
}
