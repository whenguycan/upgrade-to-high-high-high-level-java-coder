package com.ruoyi.project.parking.domain.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 编辑消息推送模板入参
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/10 14:11
 */
@Data
public class BNotifyTemplateParam {
    @NotNull(message = "消息推送模板id不为空")
    private Integer id;

    /**
     * 场库编号
     */
    private String parkNo;

    /**
     * 微信公众号消息模板id
     */
    private String templateId;

    /**
     * 头部信息
     */
    private String firstData;

    /**
     * 足部信息
     */
    private String remarkData;

    /**
     * 状态  '0'-停用 '1'-启用
     */
    private String status;
}
