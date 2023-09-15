package com.ruoyi.project.merchant.domain.vo;

import com.ruoyi.project.merchant.domain.BChargeSet;
import lombok.Data;

import java.util.List;

@Data
public class BChargeSetVo {

    private String parkNo;

    List<BChargeSet> list;
}
