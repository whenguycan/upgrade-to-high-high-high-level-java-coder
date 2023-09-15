package com.ruoyi.project.merchant.controller.app;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.common.CouponEnums;
import com.ruoyi.project.merchant.domain.TCouponDetail;
import com.ruoyi.project.merchant.domain.vo.CarUserCoupon;
import com.ruoyi.project.merchant.domain.vo.TCouponCarnoRelationVo;
import com.ruoyi.project.merchant.domain.vo.TCouponDetailVo;
import com.ruoyi.project.merchant.service.ITCouponCarnoRelationService;
import com.ruoyi.project.merchant.service.ITCouponDetailService;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = " H5平台优惠券分发车辆客户信息")
@RestController
@RequestMapping("/merchant/app/carCoupon")
public class CouponCarController extends BaseController {
    @Resource
    private ITCouponCarnoRelationService itCouponCarnoRelationService;

    @Autowired
    private ITCouponDetailService tCouponDetailService;

    /**
     * 发放优惠券
     */
    @ApiOperation(value = "发放优惠券", notes = "null")
    @Log(title = "发放优惠券", businessType = BusinessType.INSERT)
    @PostMapping("/distributeCoupon")
    public AjaxResult distributeCoupon(@RequestBody TCouponCarnoRelationVo tCouponCarnoRelation) {

        tCouponCarnoRelation.setValidStartTime(DateUtils.getNowDate());
        tCouponCarnoRelation.setValidEndTime(DateUtils.tomorrowMorning());
        if (-1 == itCouponCarnoRelationService.insertTCouponCarnoRelation(tCouponCarnoRelation)) {
            return AjaxResult.warn("无可用的优惠券，请购买");
        }
        return toAjax(1);
    }





    /**
     * 发放优惠券
     */
    @ApiOperation(value = "生成优惠券二维码", notes = "null")
    @Log(title = "生成优惠券二维码", businessType = BusinessType.INSERT)
    @PostMapping("/QrcodeCouponDetail")
    public AjaxResult qrcodeCouponDetail(@RequestBody TCouponCarnoRelationVo tCouponCarnoRelation) {

        TCouponDetailVo couponDetailVo = new TCouponDetailVo();
        couponDetailVo.setCouponStatus(CouponEnums.COUPON_STATUS.CREATED.getValue());
        couponDetailVo.setCouponId(tCouponCarnoRelation.getCouponId());
        List<TCouponDetail> list = tCouponDetailService.selectAppPlatformCouponDetailList(couponDetailVo);
        if (Collections.isEmpty(list)) {
            return AjaxResult.warn("无可用的优惠券");
        }
        TCouponDetail tCouponDetail = list.get(0);
        return AjaxResult.success(tCouponDetail);
    }

}
