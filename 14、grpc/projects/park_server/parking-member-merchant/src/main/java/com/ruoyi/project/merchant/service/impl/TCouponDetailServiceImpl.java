package com.ruoyi.project.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.common.CouponConstants;
import com.ruoyi.project.common.CouponEnums;
import com.ruoyi.project.common.OrderNoGenerator;
import com.ruoyi.project.merchant.domain.TCouponDetail;
import com.ruoyi.project.merchant.domain.TCouponOrder;
import com.ruoyi.project.merchant.domain.vo.HourCouponVo;
import com.ruoyi.project.merchant.domain.vo.OrderCouponVo;
import com.ruoyi.project.merchant.domain.vo.TCouponDetailVo;
import com.ruoyi.project.merchant.domain.vo.TCouponOrderVo;
import com.ruoyi.project.merchant.mapper.TCouponDetailMapper;
import com.ruoyi.project.merchant.service.AccountService;
import com.ruoyi.project.merchant.service.ITCouponDetailService;
import com.ruoyi.project.merchant.service.ITCouponOrderService;
import com.ruoyi.project.parking.domain.vo.MemberCouponVO;
import com.ruoyi.project.parking.enums.CouponStatusEnum;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 优惠券明细Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@Service
public class TCouponDetailServiceImpl extends ServiceImpl<TCouponDetailMapper, TCouponDetail> implements ITCouponDetailService {
    @Resource
    private TCouponDetailMapper tCouponDetailMapper;
    private static AtomicInteger atomicInteger = new AtomicInteger(1);

    @Resource
    private ITCouponOrderService couponOrderService;

    @Resource
    private AccountService accountService;
    private static final String COUPON_ORDER_PRE = "COU";

    @Override
    public boolean batchEditCoupon(OrderCouponVo orderCouponVo) {
        TCouponDetailVo tCouponDetailVo = new TCouponDetailVo();
        tCouponDetailVo.setCouponId(orderCouponVo.getCouponId());
        tCouponDetailVo.setCouponStatus(CouponEnums.COUPON_STATUS.CREATED.getValue());
        tCouponDetailVo.setCouponNum(orderCouponVo.getCouponNum());
        List<TCouponDetail> tCouponDetails = selectAppPlatformCouponDetailList(tCouponDetailVo);
        //特殊情况小时券
        HourCouponVo hourCouponVo = null;
        BigDecimal totalPrice = null;
        if (CouponConstants.COUPON_TYPE_HOUR.equals(orderCouponVo.getCouponType())) {
            hourCouponVo = hourCouponPriceStr(tCouponDetails);
            orderCouponVo.setHourCouponMark(hourCouponVo.getHourCouponRemark());
            totalPrice = hourCouponVo.getTotalAmount();
        } else {
            totalPrice = tCouponDetails.get(0).getCouponPrice().multiply(orderCouponVo.getCouponNum());
        }
        for (TCouponDetail tCouponDetail : tCouponDetails) {
            initCouponDetail(tCouponDetail);
            tCouponDetail.setCouponStatus(CouponEnums.COUPON_STATUS.CANCEL.getValue());
        }
        orderCouponVo.setCalculateAmount(totalPrice);

        return saveOrUpdateBatch(tCouponDetails);
    }

    @Override
    public boolean batchAddCoupon(OrderCouponVo orderCouponVo) {
        List<TCouponDetail> couponDetailList = new ArrayList();
        TCouponDetail tCouponDetail = null;
        for (int i = 0; i < orderCouponVo.getCouponNum().intValue(); i++) {
            tCouponDetail = new TCouponDetail();
            tCouponDetail.setCouponId(orderCouponVo.getCouponId());
            tCouponDetail.setCouponPrice(orderCouponVo.getUnitValue());
            tCouponDetail.setCouponStatus(CouponEnums.COUPON_STATUS.CREATED.getValue());
            initCouponDetail(tCouponDetail);
            couponDetailList.add(tCouponDetail);
        }
        return saveOrUpdateBatch(couponDetailList);
    }

    @Override
    public TCouponOrder cancelCoupon(OrderCouponVo orderCouponVo) {
        //批量保存优惠券
        batchEditCoupon(orderCouponVo);
        TCouponOrderVo tCouponOrder = new TCouponOrderVo();
        tCouponOrder.setOrderType(CouponConstants.CANCEL_ORDER_TYPE);
        if (StringUtils.isNotEmpty(orderCouponVo.getHourCouponMark())) {
            tCouponOrder.setRemark(orderCouponVo.getHourCouponMark());
        } else {
            tCouponOrder.setRemark("自定义购买面值券");
        }
        //生成订单
        addCouponOrder(tCouponOrder, orderCouponVo);
        tCouponOrder.setBalance(accountService.refund(SecurityUtils.getUserId(), tCouponOrder.getPayableAmount()));
        return tCouponOrder;
    }


    private void addCouponOrder(TCouponOrderVo tCouponOrder, OrderCouponVo orderCouponVo) {
        tCouponOrder.setCouponId(orderCouponVo.getCouponId());
        tCouponOrder.setPayNum(orderCouponVo.getCouponNum().longValue());
        tCouponOrder.setParkNo(orderCouponVo.getParkNo());
        tCouponOrder.setOrderNo(OrderNoGenerator.generateNo(COUPON_ORDER_PRE));
        tCouponOrder.setCouponType(orderCouponVo.getCouponType());
        tCouponOrder.setCouponName(orderCouponVo.getCouponName());
        tCouponOrder.setUnitPrice(orderCouponVo.getUnitValue());
        tCouponOrder.setOrderStatus(CouponConstants.PAYED_STATUS);
        tCouponOrder.setCreateTime(DateUtils.getNowDate());
        tCouponOrder.setCreateBy(SecurityUtils.getUsername());
        tCouponOrder.setPayableAmount(orderCouponVo.getCalculateAmount());
        tCouponOrder.setPayAmount(orderCouponVo.getCalculateAmount());
        tCouponOrder.setPaidAmount(orderCouponVo.getCalculateAmount());
        tCouponOrder.setUserId(SecurityUtils.getUserId());
        tCouponOrder.setPayTime(new Date());
        tCouponOrder.setPayStatus("0");
        couponOrderService.insertTCouponOrder(tCouponOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TCouponOrder purchaseCoupon(OrderCouponVo purchaseCouponVo) {
        //批量保存优惠券
        batchAddCoupon(purchaseCouponVo);
        //生成订单
        TCouponOrderVo tCouponOrder = new TCouponOrderVo();
        tCouponOrder.setOrderType(CouponConstants.MERCHANT_PURCHASE_ORDER_TYPE);
        tCouponOrder.setRemark("商户自定义购买优惠券");
        addCouponOrder(tCouponOrder, purchaseCouponVo);
        //更新账户余额，并返回可支配的总额
        tCouponOrder.setBalance(accountService.consume(SecurityUtils.getUserId(), tCouponOrder.getPayAmount()));
        return tCouponOrder;
    }


    /**
     * 查询优惠券明细
     *
     * @param id 优惠券明细主键
     * @return 优惠券明细
     */
    @Override
    public TCouponDetail selectTCouponDetailById(Long id) {
        return tCouponDetailMapper.selectTCouponDetailById(id);
    }

    @Override
    public Long countCouponDetail(TCouponDetail tCouponDetail) {
        LambdaQueryWrapper<TCouponDetail> couponTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (tCouponDetail.getCouponId() != null) {
            couponTypeLambdaQueryWrapper.eq(TCouponDetail::getCouponId, tCouponDetail.getCouponId());
        }
        if (StringUtils.isNotEmpty(tCouponDetail.getCouponStatus())) {
            couponTypeLambdaQueryWrapper.eq(TCouponDetail::getCouponStatus, tCouponDetail.getCouponStatus());
        }
        return tCouponDetailMapper.selectCount(couponTypeLambdaQueryWrapper);
    }

    /**
     * 查询优惠券明细列表
     *
     * @param tCouponDetail 优惠券明细
     * @return 优惠券明细
     */
    @Override
    public List<TCouponDetail> selectTCouponDetailList(TCouponDetailVo tCouponDetail) {
        return tCouponDetailMapper.selectTCouponDetailList(tCouponDetail);
    }

    @Override
    public List<TCouponDetail> selectAppPlatformCouponDetailList(TCouponDetailVo tCouponDetail) {
        return tCouponDetailMapper.selectAppPlatformCouponDetailList(tCouponDetail);
    }

    /**
     * 新增优惠券明细
     *
     * @param tCouponDetail 优惠券明细
     * @return 结果
     */
    @Override
    public int insertTCouponDetail(TCouponDetail tCouponDetail) {
        tCouponDetail.setCouponCode(DateUtils.dateTimeNow() + RandomUtil.generateDigitalString(2));
        tCouponDetail.setCreateTime(DateUtils.getNowDate());
        tCouponDetail.setCouponStatus("0");
        return tCouponDetailMapper.insertTCouponDetail(tCouponDetail);
    }

    private void initCouponDetail(TCouponDetail tCouponDetail) {
        if (tCouponDetail.getId() == null) {
            tCouponDetail.setCouponCode(DateUtils.dateTimeNow() + RandomUtil.generateDigitalString(2));
            tCouponDetail.setCreateTime(DateUtils.getNowDate());
            tCouponDetail.setCreateBy(SecurityUtils.getUsername());

        } else {
            tCouponDetail.setUpdateTime(DateUtils.getNowDate());
            tCouponDetail.setUpdateBy(SecurityUtils.getUsername());
        }
    }

    /**
     * 修改优惠券明细
     *
     * @param tCouponDetail 优惠券明细
     * @return 结果
     */
    @Override
    public int updateTCouponDetail(TCouponDetail tCouponDetail) {
        tCouponDetail.setUpdateTime(DateUtils.getNowDate());
        return tCouponDetailMapper.updateTCouponDetail(tCouponDetail);
    }

    /**
     * 批量删除优惠券明细
     *
     * @param ids 需要删除的优惠券明细主键
     * @return 结果
     */
    @Override
    public int deleteTCouponDetailByIds(Long[] ids) {
        return tCouponDetailMapper.deleteTCouponDetailByIds(ids);
    }

    @Override
    public HourCouponVo countCancelHourCoupon(TCouponDetailVo tCouponDetailVo) {
        if (CouponConstants.COUPON_TYPE_HOUR.equals(tCouponDetailVo.getCouponType())) {
            List<TCouponDetail> tCouponDetails = selectAppPlatformCouponDetailList(tCouponDetailVo);
            return hourCouponPriceStr(tCouponDetails);
        }
        return null;
    }

    private HourCouponVo hourCouponPriceStr(List<TCouponDetail> tCouponDetails) {

        Map<BigDecimal, List<TCouponDetail>> couponDetailMap = tCouponDetails.stream().filter(o -> o.getCouponPrice() != null).collect(Collectors.groupingBy(TCouponDetail::getCouponPrice));
        StringBuilder stringBuilder = new StringBuilder();
        HourCouponVo hourCouponVo = new HourCouponVo();
        BigDecimal totalAmount = new BigDecimal(0);
        for (Map.Entry<BigDecimal, List<TCouponDetail>> entry : couponDetailMap.entrySet()) {
            BigDecimal singleCoupon = entry.getKey().multiply(new BigDecimal(String.valueOf(entry.getValue().size())));
            totalAmount = totalAmount.add(singleCoupon);
            stringBuilder.append("￥");
            stringBuilder.append(entry.getKey());
            stringBuilder.append(" ");
            stringBuilder.append("x");
            stringBuilder.append(entry.getValue().size());
            stringBuilder.append("+");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");
        }
        hourCouponVo.setHourCouponRemark(stringBuilder.toString());
        hourCouponVo.setTotalAmount(totalAmount);
        return hourCouponVo;
    }

    /**
     * 删除优惠券明细信息
     *
     * @param id 优惠券明细主键
     * @return 结果
     */
    @Override
    public int deleteTCouponDetailById(Long id) {
        return tCouponDetailMapper.deleteTCouponDetailById(id);
    }

    @Override
    public Pair<List<MemberCouponVO>, Long> selectMemberCouponVOList(Long memberId, CouponStatusEnum couponStatusEnum) {
        List<MemberCouponVO> list = tCouponDetailMapper.selectMemberCouponVOList(memberId, couponStatusEnum);
        return new ImmutablePair<>(list, new PageInfo(list).getTotal());
    }

    @Override
    public boolean verifyCouponBatchByCouponCode(List<String> couponCodeList) {
        LambdaQueryWrapper<TCouponDetail> qw = new LambdaQueryWrapper<>();
        qw.in(TCouponDetail::getCouponCode, couponCodeList)
                .lt(TCouponDetail::getValidEndTime, LocalDateTime.now())
                .eq(TCouponDetail::getCouponStatus, CouponStatusEnum.ALLOCATED.getValue());
        TCouponDetail tCouponDetail = this.baseMapper.selectOne(qw);
        return tCouponDetail != null;
    }
}
