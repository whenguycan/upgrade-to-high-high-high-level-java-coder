package com.ll.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ll.user.dao.UserDao;
import com.ll.user.entity.UserEntity;
import com.ll.user.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Auther: tangwei
 * @Date: 2023/4/28 8:51 AM
 * @Description: 类描述信息
 */
@Service("UserService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {
}
