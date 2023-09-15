package com.ruoyi.project.merchant.service.accountabstract;

import com.ruoyi.project.common.CouponConstants;
import com.ruoyi.project.merchant.service.AccountService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysUserMapper;

import javax.annotation.Resource;
import java.math.BigDecimal;

public abstract class AbstractCouponAccountService implements AccountService {

    @Resource
    protected SysUserMapper userMapper;

    /**
     * 返回 可支配的总额，更新账户数据表
     *
     * @param user
     * @param operateMethod
     * @param givenMoney
     * @param needHandleAmount
     * @return
     */
    public BigDecimal handleMoney(SysUser user, Integer operateMethod, BigDecimal givenMoney, BigDecimal needHandleAmount) {
        BigDecimal balance = null;
        if (CouponConstants.RECHARGE_STATUS == operateMethod) {
            //累计的充值金额
            BigDecimal ownChargeTotal = user.getAccountValue().add(needHandleAmount);
            //累计的赠送金额
            BigDecimal ownAdditionalTotal = user.getGiveValue().add(givenMoney);
            //累计的总金额
            balance = user.getAccountTotal().add(needHandleAmount).add(givenMoney);
            user.setAccountValue(ownChargeTotal);
            user.setGiveValue(ownAdditionalTotal);
        } else if (CouponConstants.CONSUME_STATUS == operateMethod) {
            balance = user.getAccountTotal().subtract(needHandleAmount);
        } else {
            balance = user.getAccountTotal().add(needHandleAmount);

        }
        user.setAccountTotal(balance);
        int result = userMapper.updateUser(user);
        if (result > 0) {
            refreshAccountCache(user.getAccountValue(), user.getGiveValue(), user.getAccountTotal());
        }
        return balance;
    }

    protected void refreshAccountCache(BigDecimal accumulateChargeAmount, BigDecimal accumulateGivenAmount, BigDecimal totalAmount) {

    }

}
