package com.ruoyi.project.merchant.controller.app;

import com.ruoyi.common.utils.DateUtils;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = " 停车用户扫码信息")
@RestController
@RequestMapping("/merchant/carCoupon")
public class AppUserCouponController extends BaseController {
    @Resource
    private ITCouponCarnoRelationService itCouponCarnoRelationService;

    @Resource
    private ITCouponDetailService itCouponDetailService;

    /**
     * 用户扫码获取优惠券
     */
    @ApiOperation(value = "用户扫码获取优惠券", notes = "null")
    @Log(title = "用户扫码获取优惠券", businessType = BusinessType.INSERT)
    @PostMapping("/carUserAchieveCoupon")
    public AjaxResult carUserAchieveCoupon(@RequestBody TCouponCarnoRelationVo tCouponCarnoRelation) {
        TCouponDetailVo tCouponDetail = new TCouponDetailVo();
        tCouponDetail.setCouponCode(tCouponCarnoRelation.getCouponCode());
        tCouponDetail.setCarNumber(tCouponCarnoRelation.getCarNumber());
        tCouponDetail.setCouponStatus(CouponEnums.COUPON_STATUS.ALLOCATED.getValue());
        List<TCouponDetail> tCouponDetails = itCouponDetailService.selectAppPlatformCouponDetailList(tCouponDetail);
        if (!CollectionUtils.isEmpty(tCouponDetails)) {
            return AjaxResult.warn("该券已被领取");
        }
        tCouponCarnoRelation.setValidStartTime(DateUtils.getNowDate());
        tCouponCarnoRelation.setValidEndTime(DateUtils.tomorrowMorning());
        //车用户信息和优惠券信息
        CarUserCoupon carUserCoupon = new CarUserCoupon();
        carUserCoupon.setTCouponDetail(tCouponDetail);
        carUserCoupon.setCouponCarnoRelation(tCouponCarnoRelation);
        return toAjax(itCouponCarnoRelationService.addCarUserCouponInfo(carUserCoupon));
    }
}
