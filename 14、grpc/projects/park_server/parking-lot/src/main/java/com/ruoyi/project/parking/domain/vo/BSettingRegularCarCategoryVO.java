package com.ruoyi.project.parking.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.project.parking.domain.BSettingRegularCarCategoryPrice;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 琴声何来
 * @apiNote 固定车类型
 * @since 2023/2/24 9:54
 */
@Data
public class BSettingRegularCarCategoryVO {

    /**
     * 固定车类型名称
     */
    private String name;

    /**
     * 固定车类型码
     */
    private String code;

    /**
     * 分组类型编号
     */
    private String groupId;

    /**
     * 购买时限  '0'-不限制 '1'-限制
     */
    private String timeLimit;

    /**
     * 购买生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 购买失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 是否允许线上购买 0否 1 是
     */
    private Integer onlineFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 价格列表
     */
    private List<BSettingRegularCarCategoryPrice> priceList;
}
