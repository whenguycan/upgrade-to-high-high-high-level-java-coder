package com.ruoyi.project.merchant.domain.vo;

import com.ruoyi.project.merchant.domain.BDurationAmount;
import lombok.Data;

import java.util.List;

@Data
public class BDurationAmountVo {
    String parkNo;
    List<BDurationAmount> list;
}
