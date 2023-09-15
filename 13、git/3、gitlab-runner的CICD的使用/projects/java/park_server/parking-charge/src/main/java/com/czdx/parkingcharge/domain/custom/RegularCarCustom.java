package com.czdx.parkingcharge.domain.custom;

import lombok.Data;

/**
 *
 * description: 固定车扩展类
 * @author mingchenxu
 * @date 2023/3/30 14:44
 */
@Data
public class RegularCarCustom {

    private Integer id;

    /**
     * 车牌
     */
    private String carNumber;

    /**
     * 车类型ID（固定车所属ID）
     */
    private Integer carCategoryId;

    /**
     * 固定车类型分组ID（1=月租车；2=储值车）
     */
    private Integer categoryGroupId;

}
