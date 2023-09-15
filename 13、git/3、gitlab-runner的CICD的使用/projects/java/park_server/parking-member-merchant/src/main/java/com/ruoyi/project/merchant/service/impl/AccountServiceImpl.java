package com.ruoyi.project.merchant.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.project.common.CouponConstants;
import com.ruoyi.project.merchant.domain.BChargeSet;
import com.ruoyi.project.merchant.domain.vo.AccountVo;
import com.ruoyi.project.merchant.service.IBChargeSetService;
import com.ruoyi.project.merchant.service.accountabstract.AbstractCouponAccountService;
import com.ruoyi.project.system.domain.SysUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class AccountServiceImpl extends AbstractCouponAccountService {

    @Resource
    private TokenService tokenService;
    @Resource
    private IBChargeSetService bChargeSetService;

    @Override
    public BigDecimal recharge(Long accountId, BigDecimal rechargeAmount) {
        if (rechargeAmount == null) {
            return null;
        }
        if (accountId == null) {
            return null;
        }
        SysUser sysUser = userMapper.selectUserById(accountId);
        if (sysUser == null) {
            return null;
        }
        BChargeSet bChargeSet = new BChargeSet();
        bChargeSet.setParkNo(sysUser.getDept().getParkNo());
        bChargeSet.setChargeAmount(rechargeAmount);
        BChargeSet giveMoneyObject = bChargeSetService.selectOneBChargeSet(bChargeSet);
        BigDecimal giveAmount = new BigDecimal(0);
        if (giveMoneyObject != null) {
            giveAmount = giveMoneyObject.getGiveAmount();
        }
        //更新账户余额
        return handleMoney(sysUser, CouponConstants.RECHARGE_STATUS, giveAmount, rechargeAmount);
    }

    @Override
    public BigDecimal refund(Long accountId, BigDecimal refundAmount) {
        //计算的时候查一下账户库，由于这里的账户库和用户库是一样的，可以选择缓存获取；这里是数据库获取
        SysUser sysUser = userMapper.selectUserById(accountId);
        if (refundAmount == null) {
            return null;
        }
        if (accountId == null) {
            return null;
        }
        return handleMoney(sysUser, CouponConstants.CANCEL_STATUS, null, refundAmount);
    }

    @Override
    public BigDecimal consume(Long accountId, BigDecimal consumeAmount) {
        if (consumeAmount == null) {
            return null;
        }
        if (accountId == null) {
            return null;
        }
        //计算的时候再查一下库
        SysUser sysUser = userMapper.selectUserById(accountId);
        return handleMoney(sysUser, CouponConstants.CONSUME_STATUS, null, consumeAmount);
    }

    @Override
    public BigDecimal getDisposableAmount(AccountVo accountVo) {
        if (accountVo.getRechargeAmount() == null || accountVo.getBalance() == null) {
            return new BigDecimal(0);
        }
        if (accountVo.getGivenAmount() == null) {
            return accountVo.getBalance();
        }
        BigDecimal allTotal = accountVo.getRechargeAmount().add(accountVo.getGivenAmount());
        //已经使用的金额
        BigDecimal usedAmount = allTotal.subtract(accountVo.getBalance());
        if (usedAmount.compareTo(accountVo.getRechargeAmount()) > 0) {
            return new BigDecimal(0);
        }
        return accountVo.getRechargeAmount().subtract(usedAmount);
    }

    //由于目前充值的订单信息是从mq中获取的，使用支付者信息
    protected void refreshAccountCache(BigDecimal accumulateChargeAmount, BigDecimal accumulateGivenAmount, BigDecimal totalAmount) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        loginUser.getUser().setAccountTotal(totalAmount);
        loginUser.getUser().setGiveValue(accumulateGivenAmount);
        loginUser.getUser().setAccountValue(accumulateChargeAmount);
        //刷新缓存的总额
        tokenService.refreshLoginUser(loginUser);
    }
}
