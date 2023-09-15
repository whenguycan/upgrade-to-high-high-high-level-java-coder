package com.ruoyi.project.parking.domain.param;

import lombok.Data;

@Data
public class BVisitorApplyParam {

    /** 访客码 */
    private String code;

    /** 车场编号 */
    private String parkNo;

    /** 姓名 */
    private String name;

    /** 手机号 */
    private String phone;

    /** 状态(0-审核中,1-已通过,2-已驳回) */
    private String status;

    /** 页码 */
    private Integer pageNum;

    /** 页大小 */
    private Integer pageSize;
}
