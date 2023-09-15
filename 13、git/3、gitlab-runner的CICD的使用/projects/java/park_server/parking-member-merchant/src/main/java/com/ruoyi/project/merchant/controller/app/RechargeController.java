package com.ruoyi.project.merchant.controller.app;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.JsonUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.common.CommonConstants;
import com.ruoyi.project.common.CouponConstants;
import com.ruoyi.project.grpclient.MerchantOrderGrpcClientServiceImpl;
import com.ruoyi.project.grpclient.RechargeGrpcClientServiceImpl;
import com.ruoyi.project.grpclient.domain.MerchantOrderDetail;
import com.ruoyi.project.merchant.domain.BChargeSet;
import com.ruoyi.project.merchant.domain.TOperRecords;
import com.ruoyi.project.merchant.domain.vo.RechargeParam;
import com.ruoyi.project.merchant.domain.vo.RechargeParamVo;
import com.ruoyi.project.merchant.domain.vo.RechargeResVo;
import com.ruoyi.project.merchant.domain.vo.TOperRecordsVo;
import com.ruoyi.project.merchant.service.IBChargeSetService;
import com.ruoyi.project.merchant.service.IRechargePayService;
import com.ruoyi.project.merchant.service.ITOperRecordsService;
import com.ruoyi.project.parking.domain.vo.ConfirmPayRequestVO;
import com.ruoyi.project.parking.domain.vo.ConfirmPayResponseVO;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * 支付系统,订单系统调用
 *
 * @author mzl
 * @date 2023-03-02
 */
@Api(tags = "H5平台充值接口")
@RestController
@RequestMapping("/merchant/app/payment")
public class RechargeController extends BaseController {

//    @Resource
//    RechargeGrpcClientServiceImpl rechargeGrpcClientService;

    @Resource
    ITOperRecordsService operRecordsService;

//    @Resource
//    MerchantOrderGrpcClientServiceImpl merchantOrderGrpcClientService;
//    @Resource
//    IBChargeSetService ibChargeSetService;

    @Resource
    ISysUserService userService;

    @Resource
    IRechargePayService rechargePayService;

    @ApiOperation(value = "账户充值", notes = "null")
    @PostMapping("/recharge")
    @Log
    public AjaxResult recharge(@RequestBody @Validated RechargeParamVo rechargeParamVo) throws ParseException {
        ConfirmPayResponseVO confirmPayResponseVO = rechargePayService.finishRecharge(rechargeParamVo);
        if (confirmPayResponseVO == null) {
            return error("支付失败");
        } else {
            if (rechargeParamVo.getPayType() == 1 && StringUtils.isEmpty(confirmPayResponseVO.getPayUrl())) {
                return error("支付失败");
            }
        }
        return success(confirmPayResponseVO);
//        rechargeParamVo.setErchantId(SecurityUtils.getUserId());
//        BChargeSet bChargeSet = ibChargeSetService.selectBChargeSetByRechargeAmount(rechargeParamVo);
//        if (bChargeSet != null) {
//            rechargeParamVo.setDiscountAmount(bChargeSet.getGiveAmount());
//        }
//        MerchantOrderDetail merchantOrder = rechargeGrpcClientService.createMerchantOrder(rechargeParamVo);
//        //充值成功
//        TOperRecords tOperRecords = new TOperRecords();
//        ConfirmPayResponseVO responsePayVO = null;
//        if (merchantOrder != null) {
//            initOperRecords(tOperRecords, merchantOrder, rechargeParamVo);
//            if (rechargeParamVo.getPayType() == 1) {
//                String url = merchantOrderGrpcClientService.pullOrderToReadyPay(merchantOrder.getOrderNo(), rechargeParamVo.getPayType());
//                if (StringUtils.isEmpty(url)) {
//                    return warn("支付失败");
//                }
//                responsePayVO = new ConfirmPayResponseVO();
//                responsePayVO.setOrderNo(merchantOrder.getOrderNo());
//                responsePayVO.setPayUrl(url);
//            } else {
//                ConfirmPayRequestVO confirmPayRequestVO = new ConfirmPayRequestVO();
//                confirmPayRequestVO.setOrderNo(merchantOrder.getOrderNo());
//                //如果是微信支付，需要确认是jsapi支付还是h5支付
//                confirmPayRequestVO.setWeChatPayMethod(rechargeParamVo.getWeChatPayMethod());
//                confirmPayRequestVO.setPayType(rechargeParamVo.getPayType());
//                //如果是jsapi支付，则需要获取openId
//                if (confirmPayRequestVO.getWeChatPayMethod() == 1) {
//                    SysUser sysUser = userService.selectUserById(SecurityUtils.getUserId());
//                    confirmPayRequestVO.setOpenid(sysUser.getOpenId());
//                } else if (confirmPayRequestVO.getWeChatPayMethod() == 2) {
//                    //如果是h5支付，则需要获取payer_client_ip和h5_type
//                    confirmPayRequestVO.setPayerClientIp(rechargeParamVo.getPayerClientIp());
//                    confirmPayRequestVO.setH5Type(rechargeParamVo.getH5Type());
//                }
//                logger.info("confirmPayRequestVO:" + confirmPayRequestVO);
//                responsePayVO = merchantOrderGrpcClientService.pullOrderToReadyPay(confirmPayRequestVO);
//                if (responsePayVO == null) {
//                    return warn("支付失败");
//                }
//                responsePayVO.setOrderNo(merchantOrder.getOrderNo());
//                logger.info("responsePayVO:" + responsePayVO);
//            }
//            operRecordsService.insertTOperRecords(tOperRecords);
//            return AjaxResult.success(responsePayVO);
//
//        } else {
//            return AjaxResult.warn("订单生成失败");
//        }
    }

//    private void initOperRecords(TOperRecords tOperRecords, MerchantOrderDetail merchantOrder, RechargeParamVo rechargeParamVo) {
//        tOperRecords.setOperId(SecurityUtils.getUserId());
//        tOperRecords.setOperTime(DateUtils.getNowDate());
//        tOperRecords.setOperName(SecurityUtils.getUsername());
//        tOperRecords.setOperatorType(CouponConstants.RECHARGE_STATUS);
//        tOperRecords.setAmount(new BigDecimal(String.valueOf(rechargeParamVo.getPayAmount())));
//        tOperRecords.setGiveAmount(rechargeParamVo.getDiscountAmount());
//        tOperRecords.setJsonResult(JsonUtil.bean2Json(merchantOrder));
//        tOperRecords.setOperParam(JsonUtil.bean2Json(rechargeParamVo));
//        tOperRecords.setOrderNo(merchantOrder.getOrderNo());
//        tOperRecords.setParkNo(merchantOrder.getParkNo());
//        tOperRecords.setStatus(CommonConstants.SUCCESS_STATUS);
//    }

    @ApiOperation(value = "充值详情", notes = "null")
    @PostMapping("/rechargeDetail")
    @Log
    public AjaxResult rechargeDetail(@RequestBody TOperRecordsVo operRecords) throws ParseException {
        List<TOperRecords> tOperRecordsList = operRecordsService.selectTOperRecordsList(operRecords);
        if (CollectionUtils.isEmpty(tOperRecordsList)) {
            return error("订单不存在");
        }
        TOperRecords tOperRecords = tOperRecordsList.get(0);
        RechargeResVo rechargeResVo = new RechargeResVo();
        rechargeResVo.setPayDate(com.alibaba.fastjson2.util.DateUtils.format(tOperRecords.getOperTime()));
        rechargeResVo.setOrderNo(tOperRecords.getOrderNo());
        rechargeResVo.setRechargeAmount(tOperRecords.getAmount());
        rechargeResVo.setGivenAmount(tOperRecords.getGiveAmount());
        if (tOperRecords.getGiveAmount() != null) {
            rechargeResVo.setActualAcquireAmount(tOperRecords.getAmount().add(tOperRecords.getGiveAmount()));
        } else {
            rechargeResVo.setActualAcquireAmount(tOperRecords.getAmount());
        }
        rechargeResVo.setBalance(userService.selectUserById(SecurityUtils.getUserId()).getAccountTotal());
        return AjaxResult.success(rechargeResVo);
    }


}
