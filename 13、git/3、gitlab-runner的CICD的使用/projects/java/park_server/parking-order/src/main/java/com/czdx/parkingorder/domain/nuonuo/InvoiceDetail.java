package com.czdx.parkingorder.domain.nuonuo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 发票明细，支持填写商品明细最大2000行（包含折扣行、被折扣行）
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/7 9:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDetail {

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 单价含税标志：0:不含税,1:含税
     */
    private String withTaxFlag;

    /**
     * 税额
     */
    private String tax;

    /**
     * 含税金额
     */
    private String taxIncludedAmount;

    /**
     * 不含税金额
     */
    private String taxExcludedAmount;

    /**
     * 税率，注：纸票清单红票存在为null的情况
     */
    private String taxRate;
}
