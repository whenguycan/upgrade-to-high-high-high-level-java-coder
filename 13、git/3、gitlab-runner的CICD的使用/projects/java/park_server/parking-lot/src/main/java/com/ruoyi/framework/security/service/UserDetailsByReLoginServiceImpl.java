package com.ruoyi.framework.security.service;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.LoginBody;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户验证处理
 *
 * @author zh
 */
@Service("UserDetailsByReLoginServiceImpl")
public class UserDetailsByReLoginServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsByReLoginServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String loginJson) throws UsernameNotFoundException {
        LoginBody loginBody = JSONObject.parseObject(loginJson, LoginBody.class);
        SysUser user = userService.selectUserByUserName(loginBody.getUsername());
        if (StringUtils.isNull(user))
        {
            log.info("登录用户：{} 不存在.", loginBody.getUsername());
            throw new ServiceException("用户不存在/密码错误");
//            throw new ServiceException("登录用户：" + username + " 不存在");
        }
        else if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            log.info("登录用户：{} 已被删除.", loginBody.getUsername());
            throw new ServiceException("对不起，您的账号：" + loginBody.getUsername() + " 已被删除");
        }
        else if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            log.info("登录用户：{} 已被停用.", loginBody.getUsername());
            throw new ServiceException("对不起，您的账号：" + loginBody.getUsername() + " 已停用");
        }
        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}
