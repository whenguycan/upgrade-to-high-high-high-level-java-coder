package com.ruoyi.project.parking.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 * 白名单 新增传参
 * </p>
 *
 * @author 琴声何来
 * @since 2023/2/27 17:32
 */
@Data
public class WhiteListVO {

    /**
     * 固定车车牌号
     */
    @NotNull(message = "车牌号不能为空")
    private String carNumber;


    /**
     * 备注
     */
    private String remark;

    /**
     * 起始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 场库编号
     */
    private String parkNo;

    /**
     * 创建者
     */
    private String createBy;
}
