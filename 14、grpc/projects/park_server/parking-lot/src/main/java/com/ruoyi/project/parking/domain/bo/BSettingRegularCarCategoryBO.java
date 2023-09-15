package com.ruoyi.project.parking.domain.bo;

import com.ruoyi.project.parking.domain.BSettingRegularCarCategory;
import com.ruoyi.project.parking.domain.BSettingRegularCarCategoryPrice;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BSettingRegularCarCategoryBO extends BSettingRegularCarCategory {

    // 分组类型名称（字典项）
    private String groupName;

    // 价格标准列表
    private List<BSettingRegularCarCategoryPrice> priceList;
}
