package com.ruoyi.project.merchant.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BSettingRegularCarCategoryBO {
    private Integer id;
    // 场库编号
    private String parkNo;
    // 固定车类型名称
    private String name;
    // 购买时限  ''0''-不限制 ''1''-限制
    private String timeLimit;
    // 购买生效时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    // 购买失效时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    // 价格参数列表
    private List<BSettingRegularCarCategoryPriceBO> priceList;

}
