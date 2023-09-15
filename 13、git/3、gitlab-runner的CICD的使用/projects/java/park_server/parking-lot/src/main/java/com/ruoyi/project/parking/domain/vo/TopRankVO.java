package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

@Data
public class TopRankVO {

    /** 车场编号 */
    private String parkNo;

    /** 车场名称 */
    private String parkName;

    /** 泊车数 */
    private Integer count;

    /** 总共车位 */
    private Integer totalSpaceCount;

    /** 剩余车位 */
    private Integer remainSpaceCount;
}
