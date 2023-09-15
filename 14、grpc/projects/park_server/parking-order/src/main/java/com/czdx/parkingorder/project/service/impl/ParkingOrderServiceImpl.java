package com.czdx.parkingorder.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingorder.common.utils.StringUtils;
import com.czdx.parkingorder.project.dao.ParkingOrderDao;
import com.czdx.parkingorder.project.entity.ParkingOrderEntity;
import com.czdx.parkingorder.project.entity.ParkingOrderItemEntity;
import com.czdx.parkingorder.project.service.ParkingOrderItemService;
import com.czdx.parkingorder.project.service.ParkingOrderService;
import com.czdx.parkingorder.project.vo.CreateParkingOrderVo;
import com.czdx.parkingorder.project.vo.ParkingOrderDetailVo;
import com.czdx.parkingorder.utils.SnowflakeIdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Auther: tangwei
 * @Date: 2023/2/27 10:33 AM
 * @Description: 类描述信息
 */
@Service("ParkingOrderService")
public class ParkingOrderServiceImpl extends ServiceImpl<ParkingOrderDao, ParkingOrderEntity> implements ParkingOrderService {

    @Autowired
    private ParkingOrderItemService parkingOrderItemService;

    @Override
    @Transactional
    public ParkingOrderEntity createParkingOrder(CreateParkingOrderVo createParkingOrderVo) {
        ParkingOrderEntity parkingOrderEntity = new ParkingOrderEntity();
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1, 2);
        String orderNum = String.valueOf(snowflakeIdWorker.nextId());


        if(createParkingOrderVo.getPassageNo().equals("")){//如果传入的岗亭编号为空，证明预支付订单
            parkingOrderEntity.setOrderType("2");
            parkingOrderEntity.setOrderNo("PP" + orderNum);
        }else{
            parkingOrderEntity.setOrderType("1");
            parkingOrderEntity.setOrderNo("PL" + orderNum);
        }

        parkingOrderEntity.setOrderStatus(createParkingOrderVo.getPayed() ? "03" : "01");
        parkingOrderEntity.setOrderParam("");
        parkingOrderEntity.setParkNo(createParkingOrderVo.getParkNo());
        parkingOrderEntity.setPassageNo(createParkingOrderVo.getPassageNo());
        parkingOrderEntity.setCarNumber(createParkingOrderVo.getCarNumber());
        parkingOrderEntity.setCarTypeCode(createParkingOrderVo.getCarTypeCode());
        String time = createParkingOrderVo.getEntryTime();
        parkingOrderEntity.setEntryTime(new Date(Long.valueOf(time)));
        parkingOrderEntity.setPayableAmount(BigDecimal.valueOf(createParkingOrderVo.getPayableAmount()));
        parkingOrderEntity.setDiscountAmount(BigDecimal.valueOf(createParkingOrderVo.getDiscountAmount()));
        parkingOrderEntity.setPaidAmount(BigDecimal.valueOf(0));
        parkingOrderEntity.setPayAmount(BigDecimal.valueOf(createParkingOrderVo.getPayAmount()));
        parkingOrderEntity.setPayMethod("1");
        parkingOrderEntity.setPayTime(new Date());
        parkingOrderEntity.setPayStatus(createParkingOrderVo.getPayed() ? "3" : "1");
        parkingOrderEntity.setPayNumber("");
        parkingOrderEntity.setExpireTime(new Date());
        parkingOrderEntity.setRemark("");
        parkingOrderEntity.setCreateTime(new Date());
        parkingOrderEntity.setUpdateTime(new Date());

        AtomicReference<String> coupons = new AtomicReference<>("");
        createParkingOrderVo.getCouponList().stream().forEach(everyCoupon->{
            coupons.compareAndExchange(coupons.get(), coupons.get()+ everyCoupon.getCouponMold()+"_"+everyCoupon.getCouponCode()+",");
        });
        parkingOrderEntity.setCoupons(coupons.get());

        this.baseMapper.insert(parkingOrderEntity);


        //TODO 这儿为了偷懒就不写sql语句一次性插入数据了，而是写个循环，后面如果有人看到，希望你来改，我就不改了，写这玩意儿太浪费时间
        createParkingOrderVo.getItemList().stream().forEach(item->{
            ParkingOrderItemEntity parkingOrderItemEntity = new ParkingOrderItemEntity();
            parkingOrderItemEntity.setOrderNo(orderNum);
            parkingOrderItemEntity.setParkFieldId(item.getParkFieldId());
            parkingOrderItemEntity.setEntryTime(new Date(Long.valueOf(item.getEntryTime())));
            parkingOrderItemEntity.setExitTime(new Date(Long.valueOf(item.getExitTime())));
            parkingOrderItemEntity.setParkingTime(item.getParkingTimet().intValue());
            parkingOrderItemEntity.setPayableAmount(BigDecimal.valueOf(item.getPayableAmount()));
            parkingOrderItemEntity.setCreateTime(new Date());
            parkingOrderItemEntity.setUpdateTime(new Date());
            parkingOrderItemService.save(parkingOrderItemEntity);
        });

        return parkingOrderEntity;
    }

    @Override
    public ParkingOrderEntity getParkingOrderByOrderNo(String orderNo) {
        return this.baseMapper.selectOne(new QueryWrapper<ParkingOrderEntity>().eq("order_no", orderNo));
    }

    @Override
    public void cancleRepeatedOrder(String parkNo, String carNumber, String entryTime) {
        List<ParkingOrderEntity> orderInfoList = this.baseMapper.selectList(new QueryWrapper<ParkingOrderEntity>()
                        .and(
                                wp->wp.eq("park_no", parkNo)
                                        .eq("car_number", carNumber)
                                        .eq("entry_time", new Date(Long.valueOf(entryTime)))
                        )
                        .or(
                        wp -> wp.eq("order_status","01").eq("order_status", "02")
                )
                );

        orderInfoList.stream().forEach(everyOrder->{
            everyOrder.setOrderStatus("05");
            this.baseMapper.updateById(everyOrder);//更新订单状态为已取消！
        });

    }

    @Override
    public IPage<ParkingOrderEntity> searchOrder(String orderNo, Integer payMethod, Integer pageNum, Integer pageSize, String carNumber) {
        IPage<ParkingOrderEntity> page = Page.of(pageNum, pageSize);
        this.baseMapper.selectPage(page, new QueryWrapper<ParkingOrderEntity>()
                .like(StringUtils.isNotEmpty(orderNo), "order_no", orderNo)
                .eq(StringUtils.isNotEmpty(payMethod.toString()), "pay_method", payMethod)
                .eq(StringUtils.isNotEmpty(carNumber), "car_number", carNumber));
        return page;
    }
}
