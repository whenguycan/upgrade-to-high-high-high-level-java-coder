package com.ruoyi.project.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.grpc.lib.lot.*;
import com.czdx.grpc.lib.order.MonthlyOrder;
import com.czdx.grpc.lib.order.MonthlyOrderServiceGrpc;
import com.czdx.grpc.lib.order.ParkingOrder;
import com.czdx.grpc.lib.order.ParkingOrderServiceGrpc;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.merchant.domain.MonthlyCarRentalOrder;
import com.ruoyi.project.merchant.domain.bo.BSettingRegularCarCategoryBO;
import com.ruoyi.project.merchant.domain.bo.MonthlyCarRentalOrderBO;
import com.ruoyi.project.merchant.domain.param.MonthlyCarRentalOrderParam;
import com.ruoyi.project.merchant.domain.vo.PayResponseVo;
import com.ruoyi.project.merchant.enums.BSelfPaySchemeEnums;
import com.ruoyi.project.merchant.service.IMonthlyCarRentalOrderService;
import com.ruoyi.project.merchant.mapper.MonthlyCarRentalOrderMapper;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysDeptService;
import com.ruoyi.project.system.service.ISysUserService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 琴声何来
 * @description 针对表【t_monthly_car_rental_order(H5月租车订单表)】的数据库操作Service实现
 * @since 2023-03-15 11:27:56
 */
@Service
public class MonthlyCarRentalOrderServiceImpl extends ServiceImpl<MonthlyCarRentalOrderMapper, MonthlyCarRentalOrder>
        implements IMonthlyCarRentalOrderService {
    @GrpcClient("parking-lot-server")
    private RegularCarServiceGrpc.RegularCarServiceBlockingStub regularCarServiceBlockingStub;

    @GrpcClient("parking-lot-server")
    private BSettingRegularCarCategoryServiceGrpc.BSettingRegularCarCategoryServiceBlockingStub settingRegularCarCategoryServiceBlockingStub;

    @GrpcClient("parking-lot-server")
    private BSelfPaySchemeServiceGrpc.BSelfPaySchemeServiceBlockingStub selfPaySchemeServiceBlockingStub;

    @GrpcClient("parking-lot-server")
    private BlackListServiceGrpc.BlackListServiceBlockingStub blackListServiceBlockingStub;

    @GrpcClient("parking-order-server")
    private MonthlyOrderServiceGrpc.MonthlyOrderServiceBlockingStub monthlyOrderServiceBlockingStub;

    @GrpcClient("parking-order-server")
    private ParkingOrderServiceGrpc.ParkingOrderServiceBlockingStub parkingOrderServiceBlockingStub;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private ISysUserService sysUserService;

    public static final DateTimeFormatter DEFAULT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final String HTTP_STATUS_OK = "200";

    /**
     * @apiNote 获取我的订单列表
     */
    @Override
    public List<MonthlyCarRentalOrderBO> listOrder(MonthlyCarRentalOrder monthlyCarRentalOrder) {
        //获取订单列表
        LambdaQueryWrapper<MonthlyCarRentalOrder> qw = new LambdaQueryWrapper<>();
        qw.eq(SecurityUtils.getUserId() != null, MonthlyCarRentalOrder::getOrderUserId, SecurityUtils.getUserId())
                .eq(StringUtils.isNotEmpty(monthlyCarRentalOrder.getCarNumber()), MonthlyCarRentalOrder::getCarNumber, monthlyCarRentalOrder.getCarNumber())
//                .eq(StringUtils.isNotEmpty(monthlyCarRentalOrder.getOrderStatus()), MonthlyCarRentalOrder::getOrderStatus, monthlyCarRentalOrder.getOrderStatus())
                .eq(StringUtils.isNotEmpty(monthlyCarRentalOrder.getParkNo()), MonthlyCarRentalOrder::getParkNo, monthlyCarRentalOrder.getParkNo())
                .orderByDesc(MonthlyCarRentalOrder::getCreateTime);
        List<MonthlyCarRentalOrder> list = list(qw);
        //创建返回数据
        List<MonthlyCarRentalOrderBO> result = new ArrayList<>();
        //获取场库名称
        List<SysDept> sysDepts = sysDeptService.selectDeptListUnsafe(null);
        //获取固定车类型数据
        ListAllCategoryResponseProto listAllCategoryResponseProto = settingRegularCarCategoryServiceBlockingStub.listAllCategory(ListAllCategoryRequestProto.newBuilder().build());
        List<BSettingRegularCarCategoryProtoInfo> categoryProtoList = listAllCategoryResponseProto.getSettingRegularCarCategoryProtoListList();
        //获取订单数据
        MonthlyOrder.searchMonthlyOrderRequest request = MonthlyOrder.searchMonthlyOrderRequest.newBuilder()
                .setOrderUserId(Math.toIntExact(SecurityUtils.getUserId()))
                .setPageNum(0)
                .setPageSize(Integer.MAX_VALUE)
                .build();
        MonthlyOrder.searchMonthlyOrderResponse response = monthlyOrderServiceBlockingStub.searchMonthlyOrder(request);
        if (!HTTP_STATUS_OK.equals(response.getStatus())) {
            throw new ServiceException("查询订单失败，请联系管理员");
        }
        List<MonthlyOrder.MonthlyOrderDetail> orderDetailList = response.getOrderDetailList();
        //封装返回数据
        list.forEach(item -> {
            MonthlyCarRentalOrderBO bo = new MonthlyCarRentalOrderBO();
            BeanUtils.copyBeanProp(bo, item);
            bo.setOrderTime(item.getCreateTime());
            //封装场库名称
            SysDept sysDept = sysDepts.stream().filter(dept -> dept.getParkNo().equals(item.getParkNo())).toList().get(0);
            bo.setParkName(sysDept.getDeptName());
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
            bo.setRegularCarCategory(settingRegularCarCategoryBO);
            //封装订单状态、支付状态
            MonthlyOrder.MonthlyOrderDetail monthlyOrderDetail = orderDetailList.stream().filter(detail -> detail.getOrderNo().equals(item.getOrderNo())).toList().get(0);
            bo.setOrderStatus(monthlyOrderDetail.getOrderStatus());
            bo.setPayStatus(monthlyOrderDetail.getPayStatus());
            bo.setPayableAmount(BigDecimal.valueOf(monthlyOrderDetail.getPayableAmount()));
            bo.setDiscountAmount(BigDecimal.valueOf(monthlyOrderDetail.getDiscountAmount()));
            bo.setPaidAmount(BigDecimal.valueOf(monthlyOrderDetail.getPaidAmount()));
            bo.setPayAmount(BigDecimal.valueOf(monthlyOrderDetail.getPayAmount()));
            bo.setPayMethod(monthlyOrderDetail.getPayMethod());
            if (StringUtils.isNotEmpty(monthlyOrderDetail.getPayTime())) {
                bo.setPayTime(LocalDateTime.parse(monthlyOrderDetail.getPayTime(), DEFAULT_DATETIME_FORMATTER));
            }
            result.add(bo);
        });
        return result;
    }

    /**
     * @apiNote 验证下单资格
     * 1.车场是否关闭自主续费
     * 2.车牌号是否在黑名单内
     * 3.套餐是否在有效期内
     * 4.续期是否超过最大续费时间
     * 5.是否已到续费临期时间
     */
    @Override
    public boolean verifyOrderQualification(MonthlyCarRentalOrder monthlyCarRentalOrder) {
        //场库是否允许自主续费
        GetBSelfPaySchemeRequestProto selfPaySchemeRequestProto = GetBSelfPaySchemeRequestProto.newBuilder()
                .setParkNo(monthlyCarRentalOrder.getParkNo())
                .build();
        GetBSelfPaySchemeResponseProto selfPaySchemeResponseProto = selfPaySchemeServiceBlockingStub.getBSelfPayScheme(selfPaySchemeRequestProto);
        if (BSelfPaySchemeEnums.RenewStatus.NO.getCode().equals(selfPaySchemeResponseProto.getRenewStatus())) {
            throw new ServiceException("自主续费功能已关闭，请联系管理员");
        }
        //车牌号是否在黑名单内
        InBlackListRequestProto inBlackListRequestProto = InBlackListRequestProto.newBuilder()
                .setParkNo(monthlyCarRentalOrder.getParkNo())
                .setCarNumber(monthlyCarRentalOrder.getCarNumber())
                .build();
        InBlackListResponseProto inBlackListResponseProto = blackListServiceBlockingStub.inBlackList(inBlackListRequestProto);
        if (inBlackListResponseProto.getFlag()) {
            throw new ServiceException("该车牌号在黑名单中，请联系管理员");
        }
        //获取场库套餐详情
        ListOnlineCategoryRequestProto listOnlineCategoryRequestProto = ListOnlineCategoryRequestProto.newBuilder()
                .setParkNo(monthlyCarRentalOrder.getParkNo())
                .build();
        ListOnlineCategoryResponseProto listOnlineCategoryResponseProto = settingRegularCarCategoryServiceBlockingStub.listOnlineCategory(listOnlineCategoryRequestProto);
        List<BSettingRegularCarCategoryProtoInfo> protoInfoList = listOnlineCategoryResponseProto.getSettingRegularCarCategoryProtoListList().stream()
                .filter(proto -> proto.getId() == monthlyCarRentalOrder.getRegularCarCategoryId()).toList();
        //验证月租套餐在有效期内
        if (protoInfoList.isEmpty()) {
            throw new ServiceException("套餐已过期，请重新选择");
        }
        //验证套餐是否存在
        List<BSettingRegularCarCategoryPriceProtoInfo> priceProtoInfos = protoInfoList.get(0).getPriceListList().stream().filter(price -> price.getId() == monthlyCarRentalOrder.getRegularCarCategoryPriceId()).toList();
        if (priceProtoInfos.isEmpty()) {
            throw new ServiceException("套餐不存在，请重新选择");
        }
        int month = priceProtoInfos.get(0).getMonth();
        //获取该车牌的固定车信息
        SelectRegularCarByCarNumberRequestProto build = SelectRegularCarByCarNumberRequestProto.newBuilder()
                .addCarNumberList(monthlyCarRentalOrder.getCarNumber())
                .build();
        SelectRegularCarByCarNumberResponseProto selectRegularCarByCarNumberResponseProto = regularCarServiceBlockingStub.selectRegularCarByCarNumber(build);
        List<RegularCarProtoInfo> regularCarProtoInfos = selectRegularCarByCarNumberResponseProto.getRegularCarProtoListList().stream().filter(regularCarProtoInfo -> monthlyCarRentalOrder.getRegularCarCategoryId() == regularCarProtoInfo.getCarCategoryId()).toList();
        //如果不存在该类型的固定车记录，则按套餐天数计算续期是否超过最大续费天数
        int maxRenewDays = selfPaySchemeResponseProto.getMaxRenewDays();
        if (regularCarProtoInfos.isEmpty()) {
            if (month * 30 > maxRenewDays) {
                throw new ServiceException("续费时间超过最大续费天数，请重新选择");
            }
            // 没有记录，不需要判断续费临期天数
        } else {
            //如果存在固定车记录，则按记录和套餐天数计算续期是否超过最大续费天数和是否已到续费临期时间
            int renewDeadlineDays = selfPaySchemeResponseProto.getRenewDeadlineDays();
            RegularCarProtoInfo regularCarProtoInfo = regularCarProtoInfos.get(0);
            if ("1".equals(regularCarProtoInfo.getTimeLimit())) {
                throw new ServiceException("永久车辆不需续费");
            }
            if (month * 30 > maxRenewDays || LocalDate.parse(regularCarProtoInfo.getEndTime()).plusDays(month * 30L).isAfter(LocalDate.now().plusDays(maxRenewDays))) {
                throw new ServiceException("续费时间超过最大续费天数，请重新选择");
            }
            if (LocalDate.now().isBefore(LocalDate.parse(regularCarProtoInfo.getEndTime()).minusDays(renewDeadlineDays))) {
                throw new ServiceException("未到续费临期时间，请重新选择");
            }
        }
        return true;
    }

    /**
     * @apiNote 调用订单系统生成订单，返回订单号
     */
    @Override
    public Integer createOrder(MonthlyCarRentalOrder monthlyCarRentalOrder) {
        GetCategoryPriceByIdRequestProto requestProto = GetCategoryPriceByIdRequestProto.newBuilder().setId(monthlyCarRentalOrder.getRegularCarCategoryPriceId()).build();
        GetCategoryPriceByIdResponseProto responseProto = settingRegularCarCategoryServiceBlockingStub.getCategoryPriceById(requestProto);
        if (responseProto == null) {
            throw new ServiceException("价格参数不存在，请重新选择");
        }
        monthlyCarRentalOrder.setRentalDays(responseProto.getMonth() * 30);
        monthlyCarRentalOrder.setRentalPrice(BigDecimal.valueOf(responseProto.getPrice()));
        monthlyCarRentalOrder.setOrderUserId(Math.toIntExact(SecurityUtils.getUserId()));
        monthlyCarRentalOrder.setPayableAmount(BigDecimal.valueOf(responseProto.getPrice()));
        monthlyCarRentalOrder.setDiscountAmount(BigDecimal.ZERO);
        monthlyCarRentalOrder.setPaidAmount(BigDecimal.ZERO);
        monthlyCarRentalOrder.setPayAmount(BigDecimal.valueOf(responseProto.getPrice()));
        //调用订单系统创建订单并获取订单号
        MonthlyOrder.createMonthlyOrderRequest request = MonthlyOrder.createMonthlyOrderRequest.newBuilder()
                .setParkNo(monthlyCarRentalOrder.getParkNo())
                .setOrderUserId(Math.toIntExact(SecurityUtils.getUserId()))
                .setDiscountAmount(0)
                .setPayAmount(responseProto.getPrice())
                .build();
        MonthlyOrder.monthlyOrderResponse response = monthlyOrderServiceBlockingStub.createMonthlyOrder(request);
        if (HTTP_STATUS_OK.equals(response.getStatus())) {
            monthlyCarRentalOrder.setOrderNo(response.getOrderDetail().getOrderNo());
        } else {
            throw new ServiceException("创建订单失败，请重试");
        }
        //
        monthlyCarRentalOrder.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        monthlyCarRentalOrder.setCreateTime(LocalDateTime.now());
        //保存订单
        save(monthlyCarRentalOrder);
        return monthlyCarRentalOrder.getId();
    }

    /**
     * @apiNote 确认支付，调用订单系统拉起支付
     */
    @Override
    public PayResponseVo confirmPay(MonthlyCarRentalOrderParam monthlyCarRentalOrderParam) {
        PayResponseVo payResponseVo = new PayResponseVo();
        //调用订单系统拉起支付
        ParkingOrder.ConfirmPayRequest.Builder builder = ParkingOrder.ConfirmPayRequest.newBuilder();
        //微信支付逻辑
        if (monthlyCarRentalOrderParam.getPayType() == 2) {
            //如果是微信支付，需要确认是jsapi支付还是h5支付
            builder.setWeChatPayMethod(monthlyCarRentalOrderParam.getWeChatPayMethod());
            //如果是jsapi支付，则需要获取openId
            if (monthlyCarRentalOrderParam.getWeChatPayMethod() == 1) {
                SysUser sysUser = sysUserService.selectUserById(SecurityUtils.getUserId());
                builder.setOpenid(sysUser.getOpenId());
            } else if (monthlyCarRentalOrderParam.getWeChatPayMethod() == 2) {
                //如果是h5支付，则需要获取payer_client_ip和h5_type
                builder.setPayerClientIp(monthlyCarRentalOrderParam.getPayerClientIp());
                builder.setH5Type(monthlyCarRentalOrderParam.getH5Type());
            }
        }
        ParkingOrder.ConfirmPayRequest request = builder
                .setOrderNo(monthlyCarRentalOrderParam.getOrderNo())
                .setPayType(monthlyCarRentalOrderParam.getPayType())
                .build();
        ParkingOrder.ConfirmPayResponse response = parkingOrderServiceBlockingStub.confirmPay(request);
        if (HTTP_STATUS_OK.equals(response.getStatus())) {
            if (monthlyCarRentalOrderParam.getPayType() == 1) {
                //支付宝直接返回payUrl
                payResponseVo.setPayUrl(response.getPayUrl());
            } else if (monthlyCarRentalOrderParam.getPayType() == 2) {
                //微信支付如果是jsapi支付，则需要构建参数返回
                if (monthlyCarRentalOrderParam.getWeChatPayMethod() == 1) {
                    payResponseVo.setAppId(response.getAppId());
                    payResponseVo.setTimestamp(response.getTimestamp());
                    payResponseVo.setNonceStr(response.getNonceStr());
                    payResponseVo.setPackageVal(response.getPackageVal());
                    payResponseVo.setSignType(response.getSignType());
                    payResponseVo.setPaySign(response.getPaySign());
                } else if (monthlyCarRentalOrderParam.getWeChatPayMethod() == 2) {
                    //微信支付如果是h5支付，则直接返回payUrl，类似于支付宝
                    payResponseVo.setPayUrl(response.getPayUrl());
                }
            }
            return payResponseVo;
        } else {
            throw new ServiceException("确认支付失败，请重试");
        }
    }

    /**
     * @apiNote 获取单个订单详情
     */
    @Override
    public MonthlyCarRentalOrderBO getMonthlyCarRentalOrderById(Integer id) {
        MonthlyCarRentalOrder monthlyCarRentalOrder = getById(id);
        MonthlyCarRentalOrderBO monthlyCarRentalOrderBO = new MonthlyCarRentalOrderBO();
        BeanUtils.copyBeanProp(monthlyCarRentalOrderBO, monthlyCarRentalOrder);
        monthlyCarRentalOrderBO.setOrderTime(monthlyCarRentalOrder.getCreateTime());
        //获取场库名称
        SysDept sysDept = new SysDept();
        sysDept.setParkNo(monthlyCarRentalOrder.getParkNo());
        monthlyCarRentalOrderBO.setParkName(sysDeptService.selectDeptListUnsafe(sysDept).get(0).getDeptName());
        //获取固定车类型
        GetCategoryByIdRequestProto requestProto = GetCategoryByIdRequestProto.newBuilder().setId(monthlyCarRentalOrder.getRegularCarCategoryId()).build();
        GetCategoryByIdResponseProto responseProto = settingRegularCarCategoryServiceBlockingStub.getCategoryById(requestProto);
        BSettingRegularCarCategoryBO regularCarCategoryBO = new BSettingRegularCarCategoryBO();
        regularCarCategoryBO.setId(responseProto.getSettingRegularCarCategoryProtoList().getId());
        regularCarCategoryBO.setName(responseProto.getSettingRegularCarCategoryProtoList().getName());
        regularCarCategoryBO.setParkNo(responseProto.getSettingRegularCarCategoryProtoList().getParkNo());
        regularCarCategoryBO.setTimeLimit(responseProto.getSettingRegularCarCategoryProtoList().getTimeLimit());
        if (StringUtils.isNotEmpty(responseProto.getSettingRegularCarCategoryProtoList().getStartTime())) {
            regularCarCategoryBO.setStartTime(LocalDateTime.parse(responseProto.getSettingRegularCarCategoryProtoList().getStartTime(), DEFAULT_DATETIME_FORMATTER));
        }
        if (StringUtils.isNotEmpty(responseProto.getSettingRegularCarCategoryProtoList().getEndTime())) {
            regularCarCategoryBO.setEndTime(LocalDateTime.parse(responseProto.getSettingRegularCarCategoryProtoList().getEndTime(), DEFAULT_DATETIME_FORMATTER));
        }
        monthlyCarRentalOrderBO.setRegularCarCategory(regularCarCategoryBO);
        //调用订单系统获取订单状态
        MonthlyOrder.searchMonthlyOrderRequest searchMonthlyOrderRequest = MonthlyOrder.searchMonthlyOrderRequest.newBuilder()
                .setOrderUserId(Math.toIntExact(SecurityUtils.getUserId()))
                .setOrderNo(monthlyCarRentalOrder.getOrderNo())
                .setPageNum(0)
                .setPageSize(10)
                .build();
        MonthlyOrder.searchMonthlyOrderResponse searchMonthlyOrderResponse = monthlyOrderServiceBlockingStub.searchMonthlyOrder(searchMonthlyOrderRequest);
        if (HTTP_STATUS_OK.equals(searchMonthlyOrderResponse.getStatus())) {
            MonthlyOrder.MonthlyOrderDetail orderDetail = searchMonthlyOrderResponse.getOrderDetail(0);
            monthlyCarRentalOrderBO.setPayStatus(orderDetail.getPayStatus());
            if (StringUtils.isNotEmpty(orderDetail.getPayTime())) {
                monthlyCarRentalOrderBO.setPayTime(LocalDateTime.parse(orderDetail.getPayTime(), DEFAULT_DATETIME_FORMATTER));
            }
            monthlyCarRentalOrderBO.setPayMethod(orderDetail.getPayMethod());
            //订单状态
            monthlyCarRentalOrderBO.setOrderStatus(orderDetail.getOrderStatus());
        }
        return monthlyCarRentalOrderBO;
    }

    /**
     * @apiNote 根据订单号获取单个订单信息
     */
    @Override
    public MonthlyCarRentalOrder getMonthlyCarRentalOrderByOrderNo(String orderNo) {
        LambdaQueryWrapper<MonthlyCarRentalOrder> qw = new LambdaQueryWrapper<>();
        qw.eq(MonthlyCarRentalOrder::getOrderNo, orderNo)
                .last("limit 1");
        return getOne(qw);
    }
}




