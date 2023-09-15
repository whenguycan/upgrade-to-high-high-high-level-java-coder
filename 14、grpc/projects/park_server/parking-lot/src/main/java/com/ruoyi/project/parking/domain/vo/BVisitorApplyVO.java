package com.ruoyi.project.parking.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class BVisitorApplyVO {

    /** 逻辑ID */
    private Integer id;

    /** 访客码 */
    private String code;

    /** 访客码名称 */
    private String codeName;

    /** 车场编号 */
    private String parkNo;

    /** 车场名称 */
    private String parkName;

    /** 姓名 */
    private String name;

    /** 手机号 */
    private String phone;

    /** 车牌号 */
    private String carNo;

    /** 来访日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date day;

    /** 申请时间 */
    private String applyTime;

    /** 来访事由 */
    private String visitReason;

    /** 状态(0-审核中,1-已通过,2-已驳回) */
    private String status;

    /** 驳回理由 */
    private String rejectReason;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
