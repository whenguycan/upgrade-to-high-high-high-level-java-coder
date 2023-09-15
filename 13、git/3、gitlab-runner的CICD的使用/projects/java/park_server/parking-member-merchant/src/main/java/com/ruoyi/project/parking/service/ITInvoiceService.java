package com.ruoyi.project.parking.service;

import com.ruoyi.project.merchant.domain.bo.MonthlyCarRentalOrderBO;
import com.ruoyi.project.parking.domain.vo.VehicleParkOrderVO;
import com.ruoyi.project.parking.domain.vo.invoice.ApplyBillRequestVO;
import com.ruoyi.project.parking.domain.vo.invoice.BillDetailResponseVO;
import com.ruoyi.project.parking.domain.vo.invoice.ParkingOrderInvoiceInfoVO;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * <p>
 * 发票记录表 服务类
 * </p>
 *
 * @author yinwen
 * @since 2023-04-06
 */
public interface ITInvoiceService {

    Pair<List<ParkingOrderInvoiceInfoVO>, Long> pageHistoryByUseId(Integer paegeNum, Integer pageSize, Long userId);

    Pair<List<VehicleParkOrderVO>, Long> pageParkingOrderCanInvoiceByUserId(Integer paegeNum, Integer pageSize, Long userId);

    /**
     * @apiNote 获取可开票月租订单列表
     */
    Pair<List<MonthlyCarRentalOrderBO>, Long> pageMonthlyOrderCanInvoiceByUserId(Integer paegeNum, Integer pageSize, Long userId);

    String confirm(ApplyBillRequestVO requestVO);

    BillDetailResponseVO info(String tradeNo);
}
