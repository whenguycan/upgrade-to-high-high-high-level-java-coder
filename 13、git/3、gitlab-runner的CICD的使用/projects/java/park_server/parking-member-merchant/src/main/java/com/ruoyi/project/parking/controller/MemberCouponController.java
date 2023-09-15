package com.ruoyi.project.parking.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.merchant.service.ITCouponDetailService;
import com.ruoyi.project.parking.domain.vo.MemberCouponVO;
import com.ruoyi.project.parking.enums.CouponStatusEnum;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 会员优惠券
 */
@RestController
@RequestMapping("/parking/coupon")
public class MemberCouponController extends BaseController {
    @Autowired
    private ITCouponDetailService tCouponDetailService;

    /**
     * 查询指定类型 优惠券
     *
     * @param couponStatusEnum 优惠券类型
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam("status") CouponStatusEnum couponStatusEnum) {
        startPage();
        Pair<List<MemberCouponVO>, Long> pair = tCouponDetailService.selectMemberCouponVOList(getUserId(), couponStatusEnum);
        TableDataInfo tableDataInfo = getDataTable(pair.getLeft());
        tableDataInfo.setTotal(pair.getRight());
        return tableDataInfo;
    }

    /**
     * 车牌号对应的优惠券
     *
     * @param carNumber 车牌号
     */
    @GetMapping("/listbycarnumber")
    public AjaxResult listByCarNumber(@RequestParam String carNumber) {
        return success(tCouponDetailService.selectMemberCouponVOListByCarNumber(carNumber));
    }
}
