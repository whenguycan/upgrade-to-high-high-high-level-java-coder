package com.ruoyi.project.merchant.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.merchant.service.IMerchantService;
import com.ruoyi.project.system.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商户管理Controller
 */
@Api(tags = "商户管理")
@RestController
@RequestMapping("/merchant/merchantQuery")
public class MerchantController extends BaseController {
    @Autowired
    IMerchantService iMerchantService;
    /**
     * 商户管理列表
     */
    @ApiOperation(value = "商户管理列表",notes = "null")
    @GetMapping("/list")
    public TableDataInfo list(SysUser sysUser)
    {
        startPage();
        List<SysUser> list = iMerchantService.selectMerchantList(sysUser);
        return getDataTable(list);
    }

    /**
     * 根据商户编号获取详细信息
     */
    @GetMapping(value = { "/{merchantId}" })
    public AjaxResult getInfo(@PathVariable(value = "merchantId", required = true) Long merchantId)
    {
        return success(iMerchantService.selectMerchantById(merchantId));
    }
}
