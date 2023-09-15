package com.ruoyi.project.merchant.controller.app;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.framework.aspectj.lang.annotation.ChangedAmount;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.common.CouponConstants;
import com.ruoyi.project.common.CouponEnums;
import com.ruoyi.project.merchant.domain.BDurationAmount;
import com.ruoyi.project.merchant.domain.TCouponDetail;
import com.ruoyi.project.merchant.domain.TCouponType;
import com.ruoyi.project.merchant.domain.vo.*;
import com.ruoyi.project.merchant.service.IBDurationAmountService;
import com.ruoyi.project.merchant.service.ITCouponDetailService;
import com.ruoyi.project.merchant.service.ITCouponTypeService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


@Api(tags = "H5商户平台操作优惠券")
@RestController
@RequestMapping("/merchant/app")
public class MerchantPlatformCouponController extends BaseController {
    @Resource
    private ITCouponTypeService tCouponTypeService;

    @Resource
    private ITCouponDetailService tCouponDetailService;

    @Resource
    private IBDurationAmountService ibDurationAmountService;

    @Resource
    private ISysUserService userService;

    /**
     * 查询商户优惠券数量 优惠券状态；0-未分配;1-已分配;2-已使用;3-失效
     */
    @ApiOperation(value = "统计商户优惠券数量", notes = "null")
    @GetMapping("/distinctStatusCouponTotal")
    public AjaxResult couponNumCollection(Long userId) {

        TCouponDetailVo tCouponDetailVo = new TCouponDetailVo();
        if (userId == null) {
            tCouponDetailVo.setUserId(SecurityUtils.getUserId());
        } else {
            tCouponDetailVo.setUserId(userId);
        }
        List<TCouponDetail> tCouponDetails = tCouponDetailService.selectAppPlatformCouponDetailList(tCouponDetailVo);
        //List<TCouponDetail> allocatedList = tCouponDetails.stream().filter(o -> CouponEnums.COUPON_STATUS.ALLOCATED.getValue().equals(o.getCouponStatus())).toList();
        List<TCouponDetail> usedList = tCouponDetails.stream().filter(o -> CouponEnums.COUPON_STATUS.USED.getValue().equals(o.getCouponStatus())).toList();
        List<TCouponDetail> expiredList = tCouponDetails.stream().filter(o -> CouponEnums.COUPON_STATUS.EXPIRED.getValue().equals(o.getCouponStatus())).toList();
        List<TCouponDetail> createdList = tCouponDetails.stream().filter(o -> CouponEnums.COUPON_STATUS.CREATED.getValue().equals(o.getCouponStatus())).toList();
        TCouponType tCouponType = new TCouponType();
        tCouponType.setUserId(Long.valueOf(userId));
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.put("ownAmount", createdList.size());
        ajaxResult.put("created", tCouponTypeService.countCouponType(userId));
        ajaxResult.put("used", usedList.size());
        ajaxResult.put("expired", expiredList.size());
        return ajaxResult;
    }

    @ApiOperation(value = "统计作废的不同价格的相同的小时券优惠券数量", notes = "null")
    @PostMapping("/cancelCouponDetail")
    public AjaxResult countCancelHourCoupon(@RequestBody OrderCouponVoParam cancelCouponOrder) {
        TCouponDetailVo couponDetailVo = new TCouponDetailVo();
        couponDetailVo.setCouponNum(cancelCouponOrder.getCouponNum());
        couponDetailVo.setCouponStatus(CouponEnums.COUPON_STATUS.CREATED.getValue());
        couponDetailVo.setCouponType(CouponConstants.COUPON_TYPE_HOUR);
        couponDetailVo.setCouponId(cancelCouponOrder.getCouponId());
        return AjaxResult.success(tCouponDetailService.countCancelHourCoupon(couponDetailVo));
    }

    /**
     * 统计已经创建的优惠券
     */
    @ApiOperation(value = "统计已经创建的优惠券", notes = "null")
    @GetMapping("/countCreatedCoupon")
    public TableDataInfo countCreatedCoupon() {
        startPage();
        CouponTypeVo tCouponType = new CouponTypeVo();
        tCouponType.setUserId(SecurityUtils.getUserId());
        tCouponType.setCouponStatus(CouponEnums.COUPON_STATUS.CREATED.getValue());
        List<CouponTypeVo> list = tCouponTypeService.countAppCoupon(tCouponType);
        return getDataTable(list);
    }


    /**
     * 统计已经作废的优惠券
     */
    @ApiOperation(value = "统计已经作废的优惠券", notes = "null")
    @GetMapping("/countCancelCoupon")
    public TableDataInfo countCancelCoupon() {
        startPage();
        CouponTypeVo tCouponType = new CouponTypeVo();
        tCouponType.setUserId(SecurityUtils.getUserId());
        tCouponType.setCouponStatus(CouponEnums.COUPON_STATUS.CANCEL.getValue());
        List<CouponTypeVo> list = tCouponTypeService.countAppCoupon(tCouponType);
        return getDataTable(list);
    }


    /**
     * 作废优惠券
     */
    @ApiOperation(value = "作废优惠券", notes = "null")
    @PostMapping("/cancelCoupon")
    @ChangedAmount(handleAccountType = CouponConstants.CANCEL_STATUS)
    public AjaxResult cancelCoupon(@RequestBody OrderCouponVoParam cancelCouponOrder) {
        TCouponType couponType = tCouponTypeService.selectTCouponTypeById(cancelCouponOrder.getCouponId());
        if (couponType == null) {
            return AjaxResult.warn("没有此类优惠券");
        }
        TCouponDetail tCouponDetail = new TCouponDetail();
        tCouponDetail.setCouponId(cancelCouponOrder.getCouponId());
        tCouponDetail.setCouponStatus(CouponEnums.COUPON_STATUS.CREATED.getValue());
        Long aLong = tCouponDetailService.countCouponDetail(tCouponDetail);
        if (cancelCouponOrder.getCouponNum().compareTo(new BigDecimal(aLong.toString())) > 0) {
            return AjaxResult.warn("此优惠券作废数量大于最大拥有值，请减少数量");
        }
        //当退款的时候，并不需要获取最新的价格，直接获取购买的单个优惠券的金额；
        OrderCouponVo orderCouponVo = new OrderCouponVo();
        BeanUtils.copyProperties(cancelCouponOrder, orderCouponVo);
        orderCouponVo.setCouponType(couponType.getCouponType());
        orderCouponVo.setCouponName(couponType.getCouponName());
        return AjaxResult.success(tCouponDetailService.cancelCoupon(orderCouponVo));
    }

    /**
     * 新增H5商户购买优惠券,返回的订单中有余额
     */
    @ApiOperation(value = "新增H5商户优惠券购买订单", notes = "null")
    @Log(title = "H5商户优惠券购买订单", businessType = BusinessType.INSERT)
    @PostMapping("/purchaseCoupon")
    @ChangedAmount(handleAccountType = CouponConstants.CONSUME_STATUS)
    public AjaxResult purchaseCoupon(@RequestBody OrderCouponVoParam purchaseCoupon) {
        TCouponType tCouponType = tCouponTypeService.selectTCouponTypeById(purchaseCoupon.getCouponId());
        if (tCouponType == null) {
            return AjaxResult.warn("没有此类优惠券");
        }
        //计算金额,数据库改了金额，用户必须重新登录；
        OrderCouponVo orderCouponVo = calculateAmount(purchaseCoupon, tCouponType);
        if (orderCouponVo.getCalculateAmount().compareTo(SecurityUtils.getLoginUser().getUser().getAccountTotal()) > 0) {
            return AjaxResult.warn("账户余额不足，请充值");
        }
        return AjaxResult.success(tCouponDetailService.purchaseCoupon(orderCouponVo));
    }

    //生成订单需要的数据
    private OrderCouponVo calculateAmount(OrderCouponVoParam originOrderCouponVo, TCouponType tCouponType) {
        OrderCouponVo orderCouponVo = new OrderCouponVo();
        BeanUtils.copyProperties(originOrderCouponVo, orderCouponVo);
        orderCouponVo.setCouponType(tCouponType.getCouponType());
        orderCouponVo.setCouponName(tCouponType.getCouponName());
        //计算得到单价价格
        if (CouponConstants.COUPON_TYPE_FACE_VALUE.equals(orderCouponVo.getCouponType())) {
            orderCouponVo.setUnitValue(new BigDecimal(String.valueOf(tCouponType.getCouponValue())));
        } else {
            BDurationAmount bDurationAmountQryParam = new BDurationAmount();
            bDurationAmountQryParam.setParkNo(orderCouponVo.getParkNo());
            bDurationAmountQryParam.setDuration(tCouponType.getCouponValue().longValue());
            BDurationAmount bDurationAmount = ibDurationAmountService.selectOneBDurationAmount(bDurationAmountQryParam);
            orderCouponVo.setUnitValue(bDurationAmount.getAmount());
        }
        orderCouponVo.setCalculateAmount(orderCouponVo.getUnitValue().multiply(orderCouponVo.getCouponNum()));
        return orderCouponVo;
    }

    /**
     * 账户余额
     */
    @ApiOperation(value = "商户的余额", notes = "null")
    @GetMapping("/merchantBalance")
    public AjaxResult merchantBalance() {
        return AjaxResult.success(SecurityUtils.getLoginUser().getUser().getAccountTotal());
    }

    /**
     * 账户信息
     */
    @ApiOperation(value = "商户的账户信息", notes = "null")
    @GetMapping("/merchantAccount")
    public AjaxResult merchantAccount() {
        SysUser sysUser = userService.selectUserById(SecurityUtils.getUserId());
        AccountVo accountVo = new AccountVo();
        accountVo.setBalance(sysUser.getAccountTotal());
        accountVo.setGivenAmount(sysUser.getGiveValue());
        accountVo.setRechargeAmount(sysUser.getAccountValue());
        BigDecimal allTotal = accountVo.getRechargeAmount().add(accountVo.getGivenAmount());
        BigDecimal usedAmount = allTotal.subtract(accountVo.getBalance());
        //可体现的金额
        accountVo.setDisposableAmount(accountVo.getRechargeAmount().subtract(usedAmount));
        //冻结状态
        accountVo.setFreezeFlag(sysUser.getFreezeFlag());
        return AjaxResult.success(accountVo);
    }


}
