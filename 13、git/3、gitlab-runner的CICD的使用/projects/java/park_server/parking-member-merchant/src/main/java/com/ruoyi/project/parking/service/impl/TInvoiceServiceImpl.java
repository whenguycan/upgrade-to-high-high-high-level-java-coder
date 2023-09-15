package com.ruoyi.project.parking.service.impl;

import com.czdx.grpc.lib.lot.BSettingRegularCarCategoryProtoInfo;
import com.czdx.grpc.lib.lot.BSettingRegularCarCategoryServiceGrpc;
import com.czdx.grpc.lib.lot.ListAllCategoryRequestProto;
import com.czdx.grpc.lib.lot.ListAllCategoryResponseProto;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.merchant.domain.MonthlyCarRentalOrder;
import com.ruoyi.project.merchant.domain.bo.BSettingRegularCarCategoryBO;
import com.ruoyi.project.merchant.domain.bo.MonthlyCarRentalOrderBO;
import com.ruoyi.project.merchant.service.IMonthlyCarRentalOrderService;
import com.ruoyi.project.parking.domain.BMyCar;
import com.ruoyi.project.parking.domain.vo.SearchMonthlyOrderResponseVO;
import com.ruoyi.project.parking.domain.vo.SearchOrderResponseVO;
import com.ruoyi.project.parking.domain.vo.VehicleParkOrderVO;
import com.ruoyi.project.parking.domain.vo.invoice.*;
import com.ruoyi.project.parking.service.IBMyCarService;
import com.ruoyi.project.parking.service.ITInvoiceService;
import com.ruoyi.project.parking.service.ParkingInvoiceGrpcServiceImpl;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.service.ISysDeptService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 发票记录表 服务实现类
 * </p>
 *
 * @author yinwen
 * @since 2023-04-06
 */
@Service
public class TInvoiceServiceImpl implements ITInvoiceService {

    @Autowired
    private ParkingInvoiceGrpcServiceImpl parkingInvoiceGrpcService;

    @Autowired
    IBMyCarService bMyCarService;

    @Autowired
    ISysDeptService sysDeptService;

    @GrpcClient("parking-lot-server")
    private BSettingRegularCarCategoryServiceGrpc.BSettingRegularCarCategoryServiceBlockingStub settingRegularCarCategoryServiceBlockingStub;

    @Autowired
    IMonthlyCarRentalOrderService monthlyCarRentalOrderService;

    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Pair<List<ParkingOrderInvoiceInfoVO>, Long> pageHistoryByUseId(Integer pageNum, Integer pageSize, Long userId) {
        BillRecordRequestVO requestVO = new BillRecordRequestVO();
        requestVO.setUserId(userId.intValue());
        requestVO.setPageNum(pageNum);
        requestVO.setPageSize(pageSize);
        BillRecordResponseVO responseVO = parkingInvoiceGrpcService.pageHistoryParkingOrderBill(requestVO);
        return new ImmutablePair<>(responseVO.getBillDetail(), responseVO.getPageTotal().longValue());
    }

    @Override
    public Pair<List<VehicleParkOrderVO>, Long> pageParkingOrderCanInvoiceByUserId(Integer pageNum, Integer pageSize, Long userId) {
        // 用户id 转 车牌
        BMyCar bMyCar = new BMyCar();
        bMyCar.setCreateBy(userId.toString());
        List<String> carNumbers = bMyCarService.selectBMyCarList(bMyCar).stream().map(BMyCar::getCarNo).toList();
        //车场名称
        List<SysDept> sysDepts = sysDeptService.selectDeptListUnsafe(null);

        BillableParkingOrderRequestVO requestVO = new BillableParkingOrderRequestVO();
        requestVO.setCarNums(carNumbers);
        requestVO.setPageNum(pageNum);
        requestVO.setPageSize(pageSize);
        SearchOrderResponseVO responseVO = parkingInvoiceGrpcService.pageBillableParkingOrder(requestVO);
        responseVO.getOrderDetail().forEach(item -> {
            item.setParkName(sysDepts.stream().filter(sysdept -> sysdept.getParkNo().equals(item.getParkNo())).toList().get(0).getDeptName());
        });
        return new ImmutablePair<>(responseVO.getOrderDetail(), responseVO.getPageTotal().longValue());
    }

    /**
     * @apiNote 获取可开票月租订单列表
     */
    @Override
    public Pair<List<MonthlyCarRentalOrderBO>, Long> pageMonthlyOrderCanInvoiceByUserId(Integer pageNum, Integer pageSize, Long userId) {
        BillableMonthlyOrderRequestVO requestVO = new BillableMonthlyOrderRequestVO();
        requestVO.setOrderUserId(userId.intValue());
        requestVO.setPageNum(pageNum);
        requestVO.setPageSize(pageSize);
        SearchMonthlyOrderResponseVO responseVO = parkingInvoiceGrpcService.pageBillableMonthlyOrder(requestVO);
        //获取场库名称
        List<SysDept> sysDepts = sysDeptService.selectDeptListUnsafe(null);
        //获取固定车类型数据
        ListAllCategoryResponseProto listAllCategoryResponseProto = settingRegularCarCategoryServiceBlockingStub.listAllCategory(ListAllCategoryRequestProto.newBuilder().build());
        List<BSettingRegularCarCategoryProtoInfo> categoryProtoList = listAllCategoryResponseProto.getSettingRegularCarCategoryProtoListList();
        for (int i = 0; i < responseVO.getOrderDetail().size(); i++) {
            MonthlyCarRentalOrderBO item = responseVO.getOrderDetail().get(i);
            if (StringUtils.isEmpty(item.getOrderNo())) {
//                responseVO.getOrderDetail().remove(item);
//                i--;
                continue;
            }
            //获取订单信息
            MonthlyCarRentalOrder rentalOrder = monthlyCarRentalOrderService.getMonthlyCarRentalOrderByOrderNo(item.getOrderNo());
            item.setId(rentalOrder.getId());
            item.setRegularCarCategoryId(rentalOrder.getRegularCarCategoryId());
            item.setRentalDays(rentalOrder.getRentalDays());
            item.setRentalPrice(rentalOrder.getRentalPrice());
            item.setCarNumber(rentalOrder.getCarNumber());
            item.setOrderTime(rentalOrder.getCreateTime());
            //封装场库名称
            SysDept sysDept = sysDepts.stream().filter(dept -> dept.getParkNo().equals(item.getParkNo())).toList().get(0);
            item.setParkName(sysDept.getDeptName());
            //封装固定车类型数据
            BSettingRegularCarCategoryProtoInfo categoryProtoInfo = categoryProtoList.stream().filter(category -> category.getId() == item.getRegularCarCategoryId()).toList().get(0);
            BSettingRegularCarCategoryBO settingRegularCarCategoryBO = new BSettingRegularCarCategoryBO();
            BeanUtils.copyBeanProp(settingRegularCarCategoryBO, categoryProtoInfo);
            if (StringUtils.isNotEmpty(categoryProtoInfo.getStartTime())) {
                settingRegularCarCategoryBO.setStartTime(LocalDateTime.parse(categoryProtoInfo.getStartTime(), DEFAULT_DATETIME_FORMATTER));
            }
            if (StringUtils.isNotEmpty(categoryProtoInfo.getEndTime())) {
                settingRegularCarCategoryBO.setEndTime(LocalDateTime.parse(categoryProtoInfo.getEndTime(), DEFAULT_DATETIME_FORMATTER));
            }
            item.setRegularCarCategory(settingRegularCarCategoryBO);
        }
        return new ImmutablePair<>(responseVO.getOrderDetail(), responseVO.getPageTotal().longValue());
    }

    @Override
    public String confirm(ApplyBillRequestVO requestVO) {
        if (StringUtils.isNotEmpty(requestVO.getBillTaxNum()) && (requestVO.getBillTaxNum().length() < 6 || requestVO.getBillTaxNum().length() > 20)) {
            throw new ServiceException("税号只支持6-20位");
        }
        ApplyBillResponseVO applyBillResponseVO = parkingInvoiceGrpcService.confirmParkingOrderInvoice(requestVO);
        if (applyBillResponseVO == null) {
            throw new ServiceException("开票失败!");
        }
        return applyBillResponseVO.getOutTradeNo();
    }

    @Override
    public BillDetailResponseVO info(String tradeNo) {
        BillDetailRequestVO requestVO = new BillDetailRequestVO();
        requestVO.setBillOutTradeNo(tradeNo);
        return parkingInvoiceGrpcService.queryParkingOrderInvoiceDetail(requestVO);
    }

}
