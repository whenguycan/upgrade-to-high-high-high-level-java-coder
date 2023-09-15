package com.ruoyi.project.merchant.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.JsonUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.common.CommonConstants;
import com.ruoyi.project.common.CouponConstants;
import com.ruoyi.project.grpclient.MerchantOrderGrpcClientServiceImpl;
import com.ruoyi.project.grpclient.RechargeGrpcClientServiceImpl;
import com.ruoyi.project.grpclient.domain.MerchantOrderDetail;
import com.ruoyi.project.merchant.domain.BChargeSet;
import com.ruoyi.project.merchant.domain.TOperRecords;
import com.ruoyi.project.merchant.domain.vo.RechargeParamVo;
import com.ruoyi.project.merchant.service.IBChargeSetService;
import com.ruoyi.project.merchant.service.IRechargePayService;
import com.ruoyi.project.merchant.service.ITOperRecordsService;
import com.ruoyi.project.parking.domain.vo.ConfirmPayRequestVO;
import com.ruoyi.project.parking.domain.vo.ConfirmPayResponseVO;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 充值支付
 */
@Service
public class RechargePayServiceImpl implements IRechargePayService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    MerchantOrderGrpcClientServiceImpl merchantOrderGrpcClientService;

    @Resource
    RechargeGrpcClientServiceImpl rechargeGrpcClientService;

    @Resource
    IBChargeSetService ibChargeSetService;

    @Resource
    ISysUserService userService;

    @Resource
    private ITOperRecordsService operRecordsService;

    /**
     * -1 订单没有生成，0 支付失败  1  成功支付
     *
     * @param rechargeParamVo
     * @return
     */
    @Override
    public ConfirmPayResponseVO finishRecharge(RechargeParamVo rechargeParamVo) {
        TOperRecords tOperRecords = new TOperRecords();
        ConfirmPayResponseVO responsePayVO = null;
        rechargeParamVo.setErchantId(SecurityUtils.getUserId());
        //设置折扣或赠送价格
        initDiscountPrice(rechargeParamVo);
        MerchantOrderDetail merchantOrder = rechargeGrpcClientService.createMerchantOrder(rechargeParamVo);
        //充值成功
        if (merchantOrder != null) {
            initOperRecords(tOperRecords, merchantOrder, rechargeParamVo);
            if (rechargeParamVo.getPayType() == 1) {
                String url = aliPay(merchantOrder.getOrderNo(), rechargeParamVo.getPayType());
                responsePayVO = new ConfirmPayResponseVO();
                responsePayVO.setPayUrl(url);
                responsePayVO.setOrderNo(merchantOrder.getOrderNo());
            } else {
                SysUser sysUser = userService.selectUserById(rechargeParamVo.getErchantId());
                responsePayVO = weChatPay(merchantOrder.getOrderNo(), sysUser.getOpenId(), rechargeParamVo);
                if (responsePayVO != null) {
                    responsePayVO.setOrderNo(merchantOrder.getOrderNo());
                }
            }
            operRecordsService.insertTOperRecords(tOperRecords);
        }
        logger.info("responsePayVO:" + responsePayVO);
        return responsePayVO;
    }

    //支付宝支付
    private String aliPay(String orderNo, int payType) {
        return merchantOrderGrpcClientService.pullOrderToReadyPay(orderNo, payType);
    }

    //微信支付
    private ConfirmPayResponseVO weChatPay(String orderNo, String openId, RechargeParamVo rechargeParamVo) {
        ConfirmPayRequestVO confirmPayRequestVO = new ConfirmPayRequestVO();
        ConfirmPayResponseVO responsePayVO = null;
        confirmPayRequestVO.setOrderNo(orderNo);
        //如果是微信支付，需要确认是jsapi支付还是h5支付
        confirmPayRequestVO.setWeChatPayMethod(rechargeParamVo.getWeChatPayMethod());
        confirmPayRequestVO.setPayType(rechargeParamVo.getPayType());
        //如果是jsapi支付，则需要获取openId
        if (confirmPayRequestVO.getWeChatPayMethod() == 1) {
            confirmPayRequestVO.setOpenid(openId);
        } else if (confirmPayRequestVO.getWeChatPayMethod() == 2) {
            //如果是h5支付，则需要获取payer_client_ip和h5_type
            confirmPayRequestVO.setPayerClientIp(rechargeParamVo.getPayerClientIp());
            confirmPayRequestVO.setH5Type(rechargeParamVo.getH5Type());
        }
        logger.info("confirmPayRequestVO:" + confirmPayRequestVO);
        responsePayVO = merchantOrderGrpcClientService.pullOrderToReadyPay(confirmPayRequestVO);
        if (responsePayVO != null) {
            responsePayVO.setOrderNo(orderNo);
            return responsePayVO;
        }
        return null;
    }

    private void initDiscountPrice(RechargeParamVo rechargeParamVo) {
        BChargeSet bChargeSet = ibChargeSetService.selectBChargeSetByRechargeAmount(rechargeParamVo);
        if (bChargeSet != null) {
            rechargeParamVo.setDiscountAmount(bChargeSet.getGiveAmount());
        }

    }

    private void initOperRecords(TOperRecords tOperRecords, MerchantOrderDetail merchantOrder, RechargeParamVo rechargeParamVo) {
        tOperRecords.setOperId(rechargeParamVo.getErchantId());
        tOperRecords.setOperTime(DateUtils.getNowDate());
        tOperRecords.setOperName(SecurityUtils.getUsername());
        tOperRecords.setOperatorType(CouponConstants.RECHARGE_STATUS);
        tOperRecords.setAmount(new BigDecimal(String.valueOf(rechargeParamVo.getPayAmount())));
        tOperRecords.setGiveAmount(rechargeParamVo.getDiscountAmount());
        tOperRecords.setJsonResult(JsonUtil.bean2Json(merchantOrder));
        tOperRecords.setOperParam(JsonUtil.bean2Json(rechargeParamVo));
        tOperRecords.setOrderNo(merchantOrder.getOrderNo());
        tOperRecords.setParkNo(merchantOrder.getParkNo());
        tOperRecords.setStatus(CommonConstants.NO_UPDATE_STATUS);
    }
}
