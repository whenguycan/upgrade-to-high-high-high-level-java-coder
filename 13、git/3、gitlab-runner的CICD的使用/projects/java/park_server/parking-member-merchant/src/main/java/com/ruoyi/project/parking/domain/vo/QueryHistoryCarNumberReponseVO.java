package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 历史停车订单 响应
 */
@Data
public class QueryHistoryCarNumberReponseVO {
    /**
     * 状态码
     */
    private String status;
    /**
     * 成功/错误消息
     */
    private String mess;
    /**
     * 数据
     */
    private List<String> data;

}
