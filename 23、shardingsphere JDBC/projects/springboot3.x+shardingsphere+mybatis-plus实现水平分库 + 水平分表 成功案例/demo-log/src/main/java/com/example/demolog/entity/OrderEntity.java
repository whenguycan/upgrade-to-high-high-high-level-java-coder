package com.example.demolog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Auther: tangwei
 * @Date: 2023/7/20 10:22 AM
 * @Description: 类描述信息
 */
@TableName("t_order")
@Data
public class OrderEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer user;

    private Integer num;

    private String content;
}
