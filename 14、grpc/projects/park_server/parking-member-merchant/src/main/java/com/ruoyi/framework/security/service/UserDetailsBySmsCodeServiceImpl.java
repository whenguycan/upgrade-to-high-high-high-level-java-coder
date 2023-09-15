package com.ruoyi.framework.security.service;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.WxRegisterBody;
import com.ruoyi.project.parking.enums.UserEnums;
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
 * @author gmk
 */
@Service("userDetailsBySmsCodeServiceImpl")
public class UserDetailsBySmsCodeServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsBySmsCodeServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WxRegisterBody wxRegisterBody = JSONObject.parseObject(username, WxRegisterBody.class);
        SysUser user = userService.selectUserByPhonenumber(wxRegisterBody.getPhonenumber());
        if (StringUtils.isNull(user)) {
            //商户的用户都是平台导入，无需入库，只允许更新
            if (UserEnums.MEMBER_TYPE.MERCHANT.getValue().equals(wxRegisterBody.getMemberType())) {
                return new LoginUser("平台不存在此用户");
            }
            //用户不存在，新建用户，2普通角色
            SysUser newUesr = new SysUser();
            newUesr.setOpenId(wxRegisterBody.getOpenId());
            newUesr.setUserName(wxRegisterBody.getPhonenumber());
            newUesr.setPhonenumber(wxRegisterBody.getPhonenumber());
            newUesr.setCreateBy(wxRegisterBody.getPhonenumber());
            newUesr.setCreateTime(DateUtils.getNowDate());
            //默认密码rjzx@1209
            newUesr.setPassword(SecurityUtils.encryptPassword("rjzx@1209"));
            //默认2普通角色
            newUesr.setRoleIds(new Long[]{2L});
            //默认手机号
            newUesr.setNickName(wxRegisterBody.getPhonenumber());
            newUesr.setMemberType(UserEnums.MEMBER_TYPE.MEMBER.getValue());
            userService.insertUser(newUesr);
            user = userService.selectUserByPhonenumber(wxRegisterBody.getPhonenumber());
        } else {
            user.setOpenId(wxRegisterBody.getOpenId());
            user.setUpdateBy(wxRegisterBody.getPhonenumber());
            user.setUpdateTime(DateUtils.getNowDate());
            userService.updateUser(user);
        }
        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }
}
