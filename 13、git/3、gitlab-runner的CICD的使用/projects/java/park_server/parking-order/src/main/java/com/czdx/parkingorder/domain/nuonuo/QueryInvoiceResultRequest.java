package com.czdx.parkingorder.domain.nuonuo;

import lombok.Data;

import java.util.List;

@Data
public class QueryInvoiceResultRequest {

    /**
     * 发票流水号
     */
    private List<String> serialNos;

    /**
     * 是否需要提供明细(不填默认false)
     */
    private String isOfferInvoiceDetail;
}
