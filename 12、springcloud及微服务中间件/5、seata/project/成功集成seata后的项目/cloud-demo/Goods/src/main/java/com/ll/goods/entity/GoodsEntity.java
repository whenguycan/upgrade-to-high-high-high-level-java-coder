package com.ll.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Auther: tangwei
 * @Date: 2023/4/28 8:56 AM
 * @Description: 类描述信息
 */
@Data
@TableName("goods")
public class GoodsEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String goodsName;
}
