package com.ruoyi.project.parking.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class BVisitorCodeManageVO {

    /** 逻辑ID */
    private Integer id;

    /** 车场编号 */
    private String parkNo;

    /** 车场名称 */
    private String parkName;

    /** 访客码名称 */
    private String codeName;

    /** 绑定次数 */
    private Integer codeUseNumber;

    /** 已绑定次数 */
    private Integer codeUsedNumber;

    /** 免费天数 */
    private Integer codeFreeDay;

    /** 有效时间-开始 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /** 有效时间-结束 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /** 是否永久有效期(0-否,1-是) */
    private String timeLimit;

    /** 备注 */
    private String remark;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date applyTime;

    /** 码状态(1-启用 2-停用) */
    private String status;

    /** 二维码编号 */
    private String code;
}
