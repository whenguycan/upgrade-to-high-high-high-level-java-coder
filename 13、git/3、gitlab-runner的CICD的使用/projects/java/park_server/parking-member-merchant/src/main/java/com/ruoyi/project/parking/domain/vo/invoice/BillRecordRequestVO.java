package com.ruoyi.project.parking.domain.vo.invoice;

import lombok.Data;

/**
 * 查询 开票历史记录 - 请求参数
 */
@Data
public class BillRecordRequestVO {
    Integer userId;
    Integer pageNum;
    Integer pageSize;
}
