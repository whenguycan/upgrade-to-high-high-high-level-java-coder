package com.ll.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Auther: tangwei
 * @Date: 2023/4/28 8:50 AM
 * @Description: 接口描述信息
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
}
