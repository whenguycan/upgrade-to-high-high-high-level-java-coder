package com.ruoyi.project.merchant.domain.vo;

import com.ruoyi.project.merchant.domain.TCouponOperLogs;
import lombok.Data;

@Data
public class TCouponOperLogsVo extends TCouponOperLogs {
    private String allocatedTime;
    private String usedTime;
}
