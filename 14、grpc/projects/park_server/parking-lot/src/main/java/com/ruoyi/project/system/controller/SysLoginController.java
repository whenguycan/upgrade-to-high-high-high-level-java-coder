package com.ruoyi.project.system.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.domain.LoginResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.security.LoginBody;
import com.ruoyi.framework.security.service.SysLoginService;
import com.ruoyi.framework.security.service.SysPermissionService;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.domain.SysMenu;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Value;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@Slf4j
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private CaptchaService captchaService;

    @Value("${aj.enabled}")
    private boolean isVerifyCaptcha;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        // 增加滑动/点选校验
        if (isVerifyCaptcha) {
            verifyCaptcha(loginBody.getUsername(), loginBody.getVerification());
        }
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        LoginResult loginResult = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, loginResult.getToken());
        ajax.put(Constants.REFRESH_TOKEN, loginResult.getRefreshToken());
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    /**
     * @param params 刷新Token
     * @return com.ruoyi.common.core.domain.AjaxResult
     * @description: 刷新Token
     * @author mingchenxu
     * @date 2021/10/27 15:55
     */
    @PostMapping("refreshToken")
    public AjaxResult refreshToken(@RequestBody Map<String, String> params) {
        String refreshToken = params.get("refreshToken");
        return AjaxResult.success("刷新成功", loginService.refreshToken(refreshToken));
    }

    /**
     * @param captchaVerification 认证参数
     * @return boolean
     * @description: 滑动/点选认证
     * @author mingchenxu
     * @date 2022/1/24 17:34
     */
    private void verifyCaptcha(String username, String captchaVerification) {
        // 调用滑动/点选进行认证
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(captchaVerification);
        ResponseModel response = captchaService.verification(captchaVO);
        if (!response.isSuccess()) {
            log.error("验证码验证失败，错误码：{}", response.getRepCode());
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }

}
