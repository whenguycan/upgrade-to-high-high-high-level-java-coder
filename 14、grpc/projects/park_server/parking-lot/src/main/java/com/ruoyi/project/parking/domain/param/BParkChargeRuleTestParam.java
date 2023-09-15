package com.ruoyi.project.parking.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.project.parking.domain.BParkChargeRule;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BParkChargeRuleTestParam {

    /** 车场收费规则主键 */
    private Integer id;

    /** 入场时间 */
    private String entryTime;

    /** 出场时间 */
    private String exitTime;
}
