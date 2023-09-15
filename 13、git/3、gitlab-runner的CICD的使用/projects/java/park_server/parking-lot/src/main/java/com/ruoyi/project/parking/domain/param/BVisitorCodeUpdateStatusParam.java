package com.ruoyi.project.parking.domain.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BVisitorCodeUpdateStatusParam {

    /** 逻辑ID */
    @NotNull(message = "逻辑ID不可为空")
    private Integer id;

    /** 车场编号 */
    private String parkNo;

    /** 码状态(1-启用 2-停用) */
    private String status;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
