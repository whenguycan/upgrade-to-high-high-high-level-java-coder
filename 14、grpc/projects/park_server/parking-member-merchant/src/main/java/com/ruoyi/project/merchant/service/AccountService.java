package com.ruoyi.project.merchant.service;

import java.math.BigDecimal;

public interface AccountService {

    public BigDecimal recharge(Long accountId, BigDecimal rechargeAmount);

    public BigDecimal refund(Long accountId, BigDecimal refundAmount);

    public BigDecimal consume(Long accountId, BigDecimal consumeAmount);



}
