package com.ruoyi.project.system.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.security.LoginBody;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.WxLoginBody;
import com.ruoyi.framework.security.WxRegisterBody;
import com.ruoyi.framework.security.service.SysLoginService;
import com.ruoyi.framework.security.service.SysPermissionService;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.domain.LoginResult;
import com.ruoyi.project.system.domain.SysMenu;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.domain.vo.WxLoginVO;
import com.ruoyi.project.system.service.ISmsSendApplyService;
import com.ruoyi.project.system.service.ISysMenuService;
import com.ruoyi.project.system.service.WxOpenComponentService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    private TokenService tokenService;
    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private CaptchaService captchaService;

    @Value("${aj.enabled}")
    private boolean isVerifyCaptcha;

    @Autowired
    private WxOpenComponentService wxOpenComponentService;

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.appSecret}")
    private String appSecret;


    @Value("${wx-bd.appId}")
    private String bdAppId;

    @Value("${wx-bd.appSecret}")
    private String bdAppSecret;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISmsSendApplyService smsSendApplyService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        // 增加滑动/点选校验
//        if (isVerifyCaptcha) {
//            verifyCaptcha(loginBody.getUsername(), loginBody.getVerification());
//        }
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        LoginResult loginResult = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, loginResult.getToken());
        ajax.put(Constants.REFRESH_TOKEN, loginResult.getRefreshToken());
        return ajax;
    }


    @GetMapping("/getUserInfoByToken")
    public AjaxResult getInfo(HttpServletRequest request) throws IOException {
        LoginUser loginUser = null;
        AjaxResult ajax = AjaxResult.success();
        try {
            loginUser = tokenService.getLoginUser(request);
        } catch (ExpiredJwtException e) {
            // 捕获Jwt Token 过期异常，需告知前端Token过期
            return AjaxResult.error("token异常");
        }
        ajax.put("user", loginUser.getUser());
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

    /**
     * 微信公众号登录
     *
     * @return
     */
    @PostMapping("/wx/login")
    public AjaxResult wxLogin(@RequestBody WxLoginBody wxLoginBody) {
        String prefix = "【微信公众号登录】";
        try {
            // 用户登录凭证（有效期五分钟）
            if (StringUtils.isEmpty(wxLoginBody.getLoginCode())) {
                return AjaxResult.error("登录凭证不能为空");
            }
            String result = wxOpenComponentService.miniappJscode2Session(appId, appSecret, wxLoginBody.getLoginCode());
            JSONObject code2SessionRes = JSON.parseObject(result);
            String accessToken = code2SessionRes.getString("access_token");
            String openId = code2SessionRes.getString("openid");
            SysUser sysUser = loginService.getByOpenId(openId);
            if (null == sysUser) {
                WxLoginVO data = new WxLoginVO();
                data.setOpenId(openId);
                data.setHasNumberFlag(false);
                return AjaxResult.success("登录失败，该用户未注册", data);
            }
            //拉取用户信息
            String userInfoStr = wxOpenComponentService.getUserInfo(accessToken, openId);
            JSONObject userInfo = JSON.parseObject(userInfoStr);
            sysUser.setNickName(userInfo.getString("nickname"));
            sysUser.setAvatar(userInfo.getString("headimgurl"));
            loginService.updateUserInfo(sysUser);
            AjaxResult ajax = AjaxResult.success();
            // 生成令牌
            String token = loginService.wxLogin(sysUser.getPhonenumber(), Constants.CUSTOM_LOGIN_SMS);
            ajax.put(Constants.TOKEN, token);
            ajax.put(Constants.OPENID, openId);
            ajax.put(Constants.HASNUMBERFLAG, true);
            log.info("{}生成的token：{}", prefix, token);
            log.info("{}生成的OPENID：{}", prefix, openId);
            return ajax;
        } catch (Exception e) {
            String msg = "接口异常";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            log.error("{}接口发生异常：{}", prefix, e);
            return AjaxResult.error(msg);
        }
    }

    /**
     * 微信公众号登录
     *
     * @return
     */
    @PostMapping("/wx/bdlogin")
    public AjaxResult wxBdLogin(@RequestBody WxLoginBody wxLoginBody) {
        String prefix = "【微信公众号登录】";
        try {
            // 用户登录凭证（有效期五分钟）
            if (StringUtils.isEmpty(wxLoginBody.getLoginCode())) {
                return AjaxResult.error("登录凭证不能为空");
            }
            String result = wxOpenComponentService.miniappJscode2Session(bdAppId, bdAppSecret, wxLoginBody.getLoginCode());
            JSONObject code2SessionRes = JSON.parseObject(result);
            String accessToken = code2SessionRes.getString("access_token");
            String openId = code2SessionRes.getString("openid");
            SysUser sysUser = loginService.getByOpenId(openId);
            if (null == sysUser) {
                WxLoginVO data = new WxLoginVO();
                data.setOpenId(openId);
                data.setHasNumberFlag(false);
                return AjaxResult.success("登录失败，该用户未注册", data);
            }
            //拉取用户信息
            String userInfoStr = wxOpenComponentService.getUserInfo(accessToken, openId);
            JSONObject userInfo = JSON.parseObject(userInfoStr);
            sysUser.setNickName(userInfo.getString("nickname"));
            sysUser.setAvatar(userInfo.getString("headimgurl"));
            loginService.updateUserInfo(sysUser);
            AjaxResult ajax = AjaxResult.success();
            // 生成令牌
            String token = loginService.wxLogin(sysUser.getUserName(), Constants.CUSTOM_LOGIN_SMS);
            ajax.put(Constants.TOKEN, token);
            WxLoginVO data = new WxLoginVO();
            data.setOpenId(openId);
            data.setHasNumberFlag(true);
            log.info("{}生成的token：{}", prefix, token);
            log.info("{}生成的OPENID：{}", prefix, openId);
            ajax.put(AjaxResult.DATA_TAG, data);
            return ajax;
        } catch (Exception e) {
            String msg = "接口异常";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            log.error("{}接口发生异常：{}", prefix, e);
            return AjaxResult.error(msg);
        }
    }


    /**
     * 微信公众号绑定手机号
     *
     * @return
     */
    @PostMapping("/wx/register")
    public AjaxResult register(@RequestBody @Validated WxRegisterBody wxRegisterBody) {
        String prefix = "【微信公众号绑定手机号】";
        try {
            AjaxResult ajax = AjaxResult.success();
            // 设置验证码1小时之内有效——本地测试使用
//            /** startRegion */
//            String redisKey = wxRegisterBody.getPhonenumber();
//            String captcha = redisCache.getCacheObject(redisKey);
//
//            if(StringUtils.isEmpty(captcha)) {
//                redisCache.setCacheObject(redisKey, wxRegisterBody.getSmsCode(), 600, TimeUnit.MINUTES);
//            } else {
//                Long timeout = redisCache.redisTemplate.getExpire(redisKey,TimeUnit.SECONDS);
//                redisCache.setCacheObject(redisKey, captcha, timeout.intValue(), TimeUnit.SECONDS);
//            }
//            /** endRegion */
            // 生成令牌
            Map<String, String> token = loginService.loginBySmsCode(wxRegisterBody);
            String msg = token.get("msg");
            if (StringUtils.isNotEmpty(msg)) {
                ajax.put("msg", msg);
                return ajax;
            }
            ajax.put(Constants.TOKEN, token.get(Constants.TOKEN));
            log.info("{}生成的token：{}", prefix, token);
            return ajax;
        } catch (Exception e) {
            String msg = "接口异常";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            log.error("{}接口发生异常：{}", prefix, e);
            return AjaxResult.error(msg);
        }
    }

    /**
     * 获取短信验证码
     */
    @ApiOperation("获取短信验证码")
    @PostMapping("/captchaSms")
    public AjaxResult getCaptcha(@RequestBody LoginBody loginBody) {
        // 增加滑动/点选校验
//        verifyCaptcha(loginBody.getUsername(), loginBody.getCode());
        // 验证1小时之内只能发10次
        String countKey = Constants.HOUR_SEND_COUNT + loginBody.getUsername();
        Integer sendCount = redisCache.getCacheObject(countKey);

        if (sendCount != null && sendCount == 10) {
            throw new ServiceException("1小时内最多只能发送10次验证码", 6666);
        }
        if (sendCount == null) {
            redisCache.setCacheObject(countKey, 1, 1, TimeUnit.HOURS);
        } else {
            Long timeout = redisCache.redisTemplate.getExpire(countKey, TimeUnit.SECONDS);
            redisCache.setCacheObject(countKey, sendCount + 1, timeout.intValue(), TimeUnit.SECONDS);
        }

        // 随机六位数验证码
//        String captcha = RandomUtil.generateDigitalString(6);
        String captcha = "888888";
        Map<String, String> content = new HashMap<>();
        content.put("code", captcha);
//        new Thread(() -> {
//            System.out.println("异步线程 =====> 发送短信开始 =====> " + System.currentTimeMillis());
//            smsSendApplyService.smsSend(loginBody.getUsername(), "template_000064", content, "智慧停车管理平台");
//            System.out.println("异步线程 =====> 发送短信结束 =====> " + System.currentTimeMillis());
//        }).start();
        //设置验证码超时5分钟
        int minute = 5;
        //登录手机验证码
        String redisKey = loginBody.getUsername();
        //非登录业务申请手机验证码,password可设置验证码超时多少分钟，reidskey加前缀区分
        if (loginBody.getPassword() != null) {
            minute = Integer.valueOf(loginBody.getPassword());
            redisKey = Constants.CAPTCHA_APPLY_CODE + loginBody.getUsername();
        }
        redisCache.setCacheObject(redisKey, captcha, minute, TimeUnit.MINUTES);
        return AjaxResult.success();
    }

}
