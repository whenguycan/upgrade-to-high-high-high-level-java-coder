package com.ruoyi.project.parking.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class BVisitorCodeManageQueryParam {

    /** 车场编号 */
    private String parkNo;

    /** 访客码名称 */
    private String codeName;

    /** 申请开始 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date applyStartTime;

    /** 申请结束 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date applyEndTime;

    /** 码状态(1-启用 2-停用) */
    private String status;
}
