package com.ruoyi.project.parking.controller;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.parking.domain.BMyCar;
import com.ruoyi.project.parking.domain.MemberUser;
import com.ruoyi.project.parking.domain.param.BMyCarParam;
import com.ruoyi.project.parking.domain.param.MemberUserParam;
import com.ruoyi.project.parking.service.IMemberUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 会员信息
 * 
 * @author fangch
 */
@RestController
@RequestMapping("/member/user")
public class MemberUserController extends BaseController
{
    @Autowired
    private IMemberUserService memberUserService;

    /**
     * 获取会员列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MemberUserParam memberUser) {
        List<MemberUser> memberUsers = memberUserService.selectUserList(memberUser);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        memberUser.setPageNum(null);
        memberUser.setPageSize(null);
        rspData.setTotal(memberUserService.selectUserList(memberUser).size());
        rspData.setRows(memberUsers);
        return rspData;
    }

    /**
     * 导出
     */
    @Log(title = "会员信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MemberUserParam memberUser) {
        List<MemberUser> list = memberUserService.selectUserList(memberUser);
        ExcelUtil<MemberUser> util = new ExcelUtil<MemberUser>(MemberUser.class);
        util.exportExcel(response, list, "会员数据");
    }

    /**
     * 通过会员ID查询会员详情
     */
    @GetMapping("/getInfo/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return success(memberUserService.selectUserById(id));
    }

    /**
     * 通过会员ID查询我的车辆
     */
    @GetMapping("/{userId}")
    public AjaxResult selectCarList(@PathVariable Long userId) {
        List<BMyCar> cars = memberUserService.selectCarList(userId);
        return AjaxResult.success(cars);
    }

    /**
     * 绑定车辆
     */
    @Log(title = "绑定车辆", businessType = BusinessType.INSERT)
    @PostMapping("/bindVehicle")
    public AjaxResult add(@RequestBody @Validated BMyCarParam bMyCarParam) {
        return memberUserService.insertBMyCar(bMyCarParam);
    }

    /**
     * 解绑车辆
     */
    @Log(title = "解绑车辆", businessType = BusinessType.DELETE)
    @PostMapping("/unbindVehicle/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(memberUserService.deleteBMyCarByIds(ids));
    }

    /**
     * 设为默认
     */
    @Log(title = "设为默认", businessType = BusinessType.UPDATE)
    @PostMapping("/setDefault")
    public AjaxResult setDefault(@RequestBody @Validated BMyCarParam bMyCarParam) {
        // 获取当前的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        bMyCarParam.setUpdateBy(String.valueOf(loginUser.getUserId()));
        return toAjax(memberUserService.setDefault(bMyCarParam));
    }
}
