package com.ruoyi.framework.security.service;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.RefreshTokenExpiredException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.WxRegisterBody;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.framework.security.sms.SmsCodeAuthenticationToken;
import com.ruoyi.framework.web.domain.LoginResult;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysConfigService;
import com.ruoyi.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public LoginResult login(String username, String password, String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(username, code, uuid);
        }
        // 用户验证
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        String token = tokenService.createToken(loginUser);
        return new LoginResult(token, loginUser.getToken());
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }

    /**
     * @param refreshToken
     * @return java.lang.String
     * @description: 刷新认证Token
     * @author mingchenxu
     * @date 2021/10/27 15:57
     */
    public String refreshToken(String refreshToken) {
        LoginUser loginUser = tokenService.getLoginUserByRefreshToken(refreshToken);
        if (loginUser == null) {
            throw new RefreshTokenExpiredException();
        }
        // 重新登录，获取token
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, loginUser.getToken());
        return tokenService.createToken(claims);
    }

    /**
     * 微信登录验证
     *
     * @param phoneNumber 手机号
     * @param passWord    密码
     * @return 结果
     */
    public String wxLogin(String phoneNumber, String passWord) {
        //1、用户验证
        Authentication authentication = null;
        try {
            //该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, passWord));
        } catch (
                Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        recordLoginInfo(loginUser.getUserId());
        // 生成token
        String token = tokenService.createToken(loginUser);
        return token;
    }

    /**
     * 手机验证码登录验证
     *
     * @param wxRegisterBody
     * @return 结果
     */
    public Map<String, String> loginBySmsCode(WxRegisterBody wxRegisterBody) {
        String captcha = redisCache.getCacheObject(wxRegisterBody.getPhonenumber());
        if (captcha == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(wxRegisterBody.getPhonenumber(), Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!wxRegisterBody.getSmsCode().equalsIgnoreCase(captcha)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(wxRegisterBody.getPhonenumber(), Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        //验证通过再删除
        redisCache.deleteObject(wxRegisterBody.getPhonenumber());
        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsBySmsCodeServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new SmsCodeAuthenticationToken(wxRegisterBody));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(wxRegisterBody.getPhonenumber(), Constants.LOGIN_FAIL, MessageUtils.message("user.smsCode.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(wxRegisterBody.getPhonenumber(), Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(wxRegisterBody.getPhonenumber(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNotEmpty(loginUser.getMsg())) {
            map.put("msg", loginUser.getMsg());
            return map;
        }
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        map.put(Constants.TOKEN, tokenService.createToken(loginUser));
        return map;
    }

    public SysUser getByOpenId(String openId) {
        return userService.getByOpenId(openId);
    }

}
