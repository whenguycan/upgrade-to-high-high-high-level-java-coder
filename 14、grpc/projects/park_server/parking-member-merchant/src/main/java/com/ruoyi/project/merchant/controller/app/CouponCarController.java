package com.ruoyi.project.merchant.controller.app;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.merchant.domain.vo.TCouponCarnoRelationVo;
import com.ruoyi.project.merchant.service.ITCouponCarnoRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = " H5平台优惠券分发车辆客户信息")
@RestController
@RequestMapping("/merchant/app/carCoupon")
public class CouponCarController extends BaseController {
    @Resource
    private ITCouponCarnoRelationService itCouponCarnoRelationService;

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

}
