package com.ruoyi.project.merchant.service;

import com.ruoyi.project.merchant.domain.vo.RechargeParamVo;
import com.ruoyi.project.parking.domain.vo.ConfirmPayResponseVO;

public interface IRechargePayService {

    ConfirmPayResponseVO finishRecharge( RechargeParamVo rechargeParamVo);

}
