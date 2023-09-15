package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 历史停车订单 响应
 */
@Data
public class HistoryParkingOrderReponseVO {
    /**
     * 状态码
     */
    private String status;
    /**
     * 成功/错误消息
     */
    private String mess;
    /**
     * 记录总数
     */
    private Integer total;
    /**
     * 数据
     */
    private List<HistoryParkingOrderVO> rows;

}
