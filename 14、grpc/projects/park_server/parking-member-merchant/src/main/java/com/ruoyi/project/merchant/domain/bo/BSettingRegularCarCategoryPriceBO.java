package com.ruoyi.project.merchant.domain.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BSettingRegularCarCategoryPriceBO {
    private Integer id;
    // 场库编号
    private String parkNo;
    // 固定车类型id
    private Integer regularCarCategoryId;
    // 月数
    private Integer month;
    // 价格
    private BigDecimal price;
}
