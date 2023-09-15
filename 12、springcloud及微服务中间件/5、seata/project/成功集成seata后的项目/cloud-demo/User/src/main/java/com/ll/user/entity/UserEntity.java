package com.ll.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Auther: tangwei
 * @Date: 2023/4/28 8:46 AM
 * @Description: 类描述信息
 */
@TableName("user")
@Data
public class UserEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String userName;
}
