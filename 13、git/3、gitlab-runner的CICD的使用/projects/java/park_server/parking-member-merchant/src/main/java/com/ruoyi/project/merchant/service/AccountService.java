package com.ruoyi.project.merchant.service;

import com.ruoyi.project.merchant.domain.vo.AccountVo;

import java.math.BigDecimal;

public interface AccountService {

    /**
     * 充值
     *
     * @param accountId
     * @param rechargeAmount
     * @return
     */
    public BigDecimal recharge(Long accountId, BigDecimal rechargeAmount);

    /**
     * 退款
     *
     * @param accountId
     * @param refundAmount
     * @return
     */
    public BigDecimal refund(Long accountId, BigDecimal refundAmount);

    /**
     * 消费
     *
     * @param accountId
     * @param consumeAmount
     * @return
     */
    public BigDecimal consume(Long accountId, BigDecimal consumeAmount);

    /**
     * 可提现的金额
     *
     * @param accountVo
     * @return
     */
    public BigDecimal getDisposableAmount(AccountVo accountVo);


}
