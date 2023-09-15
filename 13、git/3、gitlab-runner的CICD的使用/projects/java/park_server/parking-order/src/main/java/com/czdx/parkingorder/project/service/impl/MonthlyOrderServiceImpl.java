package com.czdx.parkingorder.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingorder.common.utils.StringUtils;
import com.czdx.parkingorder.project.dao.MonthlyOrderDao;
import com.czdx.parkingorder.project.entity.MerchantOrderEntity;
import com.czdx.parkingorder.project.entity.MonthlyOrderEntity;
import com.czdx.parkingorder.project.entity.ParkingOrderEntity;
import com.czdx.parkingorder.project.service.MonthlyOrderService;
import com.czdx.parkingorder.utils.SnowflakeIdWorker;
import com.google.protobuf.ProtocolStringList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Auther: tangwei
 * @Date: 2023/3/13 2:10 PM
 * @Description: 类描述信息
 */
@Service("MonthlyOrderService")
public class MonthlyOrderServiceImpl extends ServiceImpl<MonthlyOrderDao, MonthlyOrderEntity> implements MonthlyOrderService {

    @Autowired
    SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public MonthlyOrderEntity createOrder(String parkNo, Integer orderUserId, Double discountAmount, Double payAmount) {
        MonthlyOrderEntity monthlyOrderEntity = new MonthlyOrderEntity();

        String orderNum = String.valueOf(snowflakeIdWorker.nextId());

        monthlyOrderEntity.setOrderNo("MO" + orderNum);

        monthlyOrderEntity.setOrderType("1");
        monthlyOrderEntity.setOrderStatus("01");
        monthlyOrderEntity.setOrderParam("");
        monthlyOrderEntity.setParkNo(parkNo);
        monthlyOrderEntity.setOrderUserId(orderUserId);
        monthlyOrderEntity.setPayableAmount(BigDecimal.valueOf(payAmount));
        monthlyOrderEntity.setDiscountAmount(BigDecimal.valueOf(discountAmount));
        monthlyOrderEntity.setPaidAmount(BigDecimal.valueOf(0L));
        monthlyOrderEntity.setPayAmount(BigDecimal.valueOf(payAmount));
        monthlyOrderEntity.setPayStatus("01");
        monthlyOrderEntity.setPayNumber("");
        monthlyOrderEntity.setExpireTime(new Date());
        monthlyOrderEntity.setRemark("");
        monthlyOrderEntity.setCreateTime(new Date());
        monthlyOrderEntity.setUpdateTime(new Date());
        this.baseMapper.insert(monthlyOrderEntity);

        return monthlyOrderEntity;
    }

    @Override
    public IPage<MonthlyOrderEntity> searchOrder(Integer orderUserId, String orderStatus, String payStatus, Integer pageNum, Integer pageSize, String orderNo, boolean billable) {
        IPage<MonthlyOrderEntity> page = Page.of(pageNum, pageSize);
        this.baseMapper.selectPage(page, new QueryWrapper<MonthlyOrderEntity>()
                .like(StringUtils.isNotEmpty(orderNo), "order_no", orderNo)
                .eq(orderUserId != 0, "order_user_id", orderUserId)
                .eq(StringUtils.isNotEmpty(orderStatus), "order_status", orderStatus)
                .eq(StringUtils.isNotEmpty(payStatus), "pay_status", payStatus)
                .eq(billable, "bill_out_trade_no", "")
                .ne(billable, "pay_amount", 0.00));
        return page;
    }

    /**
     * @apiNote 查询指定日期指定车场月租车订单总实付金额
     */
    @Override
    public BigDecimal sumOnlineIncome(ProtocolStringList parkNos, String day) {
        return baseMapper.sumOnlineIncome(parkNos, day);
    }

    /**
     * @apiNote 查询指定日期指定车场月租车订单总优惠金额
     */
    @Override
    public BigDecimal sumOnlineDeduction(ProtocolStringList parkNos, String day) {
        return baseMapper.sumOnlineDeduction(parkNos, day);
    }

    /**
     * @apiNote 根据订单号查询月租车订单
     */
    @Override
    public List<MonthlyOrderEntity> searchMonthlyOrderByOrderNo(List<String> orderNo) {
        return this.baseMapper.selectList(new QueryWrapper<MonthlyOrderEntity>().eq("order_status", "03").in("order_no", orderNo));
    }

    /**
     * @apiNote 更新发票流水号
     */
    @Override
    public void updateToBilled(List<String> orderNoList, String out_trade_no) {
        this.update(new UpdateWrapper<MonthlyOrderEntity>().in("order_no", orderNoList).set("bill_out_trade_no", out_trade_no));
    }
}
