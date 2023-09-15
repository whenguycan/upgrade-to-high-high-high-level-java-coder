package com.ruoyi.project.merchant.controller.app;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.JsonUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.common.CouponConstants;
import com.ruoyi.project.grpclient.MerchantOrderGrpcClientServiceImpl;
import com.ruoyi.project.grpclient.RechargeGrpcClientServiceImpl;
import com.ruoyi.project.grpclient.domain.MerchantOrderDetail;
import com.ruoyi.project.merchant.domain.BChargeSet;
import com.ruoyi.project.merchant.domain.TCouponOrder;
import com.ruoyi.project.merchant.domain.TOperRecords;
import com.ruoyi.project.merchant.domain.vo.PayResponseVo;
import com.ruoyi.project.merchant.domain.vo.RechargeVo;
import com.ruoyi.project.merchant.domain.vo.RechargeVoParam;
import com.ruoyi.project.merchant.service.AccountService;
import com.ruoyi.project.merchant.service.IBChargeSetService;
import com.ruoyi.project.merchant.service.ITCouponOrderService;
import com.ruoyi.project.merchant.service.ITOperRecordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

/**
 * 支付系统,订单系统调用
 *
 * @author mzl
 * @date 2023-03-02
 */
@Api(tags = "H5平台充值接口")
@RestController
@RequestMapping("/merchant/payment")
public class RechargeController extends BaseController {

    @Resource
    RechargeGrpcClientServiceImpl rechargeGrpcClientService;

    @Resource
    ITOperRecordsService operRecordsService;

    @Resource
    MerchantOrderGrpcClientServiceImpl merchantOrderGrpcClientService;
    @Resource
    IBChargeSetService ibChargeSetService;

    @ApiOperation(value = "账户充值", notes = "null")
    @PostMapping("/recharge")
    @Log
    public AjaxResult recharge(@RequestBody @Validated RechargeVoParam rechargeVoParam) throws ParseException {


        RechargeVo rechargeVo = new RechargeVo();
        rechargeVo.setParkNo(rechargeVoParam.getParkNo());
        rechargeVo.setPayAmount(rechargeVoParam.getPayAmount());
        rechargeVo.setErchantId(SecurityUtils.getUserId());
        BChargeSet bChargeSet = ibChargeSetService.selectBChargeSetByRechargeAmount(rechargeVoParam);
        if (bChargeSet != null) {
            rechargeVo.setDiscountAmount(bChargeSet.getGiveAmount());
        }
        MerchantOrderDetail merchantOrder = rechargeGrpcClientService.createMerchantOrder(rechargeVo);
        //充值成功
        TOperRecords tOperRecords = new TOperRecords();
        if (merchantOrder != null) {
            String url = merchantOrderGrpcClientService.pullOrderToReadyPay(merchantOrder.getOrderNo(), rechargeVoParam.getPayType());
            tOperRecords.setOperId(SecurityUtils.getUserId());
            tOperRecords.setOperTime(DateUtils.getNowDate());
            tOperRecords.setOperName(SecurityUtils.getUsername());
            tOperRecords.setOperatorType(CouponConstants.RECHARGE_STATUS);
            tOperRecords.setAmount(new BigDecimal(String.valueOf(rechargeVo.getPayAmount())));
            tOperRecords.setGiveAmount(rechargeVo.getDiscountAmount());
            tOperRecords.setJsonResult(JsonUtil.bean2Json(merchantOrder));
            tOperRecords.setOperParam(JsonUtil.bean2Json(rechargeVo));
            tOperRecords.setOrderNo(merchantOrder.getOrderNo());
            tOperRecords.setParkNo(merchantOrder.getParkNo());
            tOperRecords.setStatus(merchantOrder.getPayStatus() != null ? Integer.parseInt(merchantOrder.getPayStatus()) :
                    null);
            operRecordsService.insertTOperRecords(tOperRecords);
            PayResponseVo payResponseVo = new PayResponseVo();
            payResponseVo.setPayUrl(url);
            payResponseVo.setOrderRecordId(tOperRecords.getId());
            return AjaxResult.success(payResponseVo);
        } else {
            return AjaxResult.warn("订单生成失败");
        }
    }

    //todo grpc 的接口 ，更新TOperRecords的数据
}
