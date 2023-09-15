package com.czdx.parkingorder.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingorder.enums.NNEnums;
import com.czdx.parkingorder.project.dao.ParkingBillDao;
import com.czdx.parkingorder.project.dao.ParkingOrderItemDao;
import com.czdx.parkingorder.project.entity.ParkingBillEntity;
import com.czdx.parkingorder.project.entity.ParkingOrderEntity;
import com.czdx.parkingorder.project.entity.ParkingOrderItemEntity;
import com.czdx.parkingorder.project.service.ParkingBillService;
import com.czdx.parkingorder.project.service.ParkingOrderItemService;
import com.czdx.parkingorder.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * @Auther: tangwei
 * @Date: 2023/4/10 5:24 PM
 * @Description: 类描述信息
 */
@Slf4j
@Service("ParkingBillService")
public class ParkingBillServiceImpl extends ServiceImpl<ParkingBillDao, ParkingBillEntity> implements ParkingBillService {

    /**
     * @apiNote 更新发票状态
     * @author 琴声何来
     * @since 2023/4/13 15:28
     * @param orderNo 发票订单号
     * @param pdfUrl 发票pdf地址
     */
    @Override
    public void updateByOrderNo(String orderNo, String pdfUrl) {
        ParkingBillEntity one = getOneBillRecordByOrderNo(orderNo);
        if (one == null) {
            log.error("发票订单号{}不存在", orderNo);
        } else {
            //设置为开票完成状态
            one.setBillPdfUrl(pdfUrl);
            one.setBillStatus(NNEnums.BillStatus.SUCCESS.getValue());
            updateById(one);
            //发送邮件通知
            try {
                MailUtil.send(one.getBuyerEmail(), new URL(one.getBillPdfUrl()));
            } catch (Exception e) {
                log.error("发送邮件异常", e);
                throw new RuntimeException(e);
            }
        }
    }

    private ParkingBillEntity getOneBillRecordByOrderNo(String orderNo) {
        LambdaQueryWrapper<ParkingBillEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(ParkingBillEntity::getBillNo, orderNo)
                .last("limit 1");
        return getOne(qw);
    }

    @Override
    public IPage<ParkingBillEntity> searchByUserId(Integer userId, Integer pageNum, Integer pageSize) {
        IPage<ParkingBillEntity> page = Page.of(pageNum, pageSize);
        this.baseMapper.selectPage(page, new QueryWrapper<ParkingBillEntity>().eq("user_id", userId));
        return page;
    }

    @Override
    public ParkingBillEntity getOneBillRecordByOutTradeNo(String billOutTradeNo) {
        return this.baseMapper.selectOne(new QueryWrapper<ParkingBillEntity>().eq("bill_out_trade_no", billOutTradeNo));
    }
}
