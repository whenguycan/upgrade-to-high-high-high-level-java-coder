package com.ruoyi.project.merchant.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayResponseVo implements Serializable {

    private String payUrl;

    private Long orderRecordId;
}
