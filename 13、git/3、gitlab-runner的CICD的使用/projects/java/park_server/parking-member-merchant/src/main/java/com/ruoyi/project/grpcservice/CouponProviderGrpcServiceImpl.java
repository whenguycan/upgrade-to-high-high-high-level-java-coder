package com.ruoyi.project.grpcservice;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czdx.grpc.lib.merchant.CouponProvider;
import com.czdx.grpc.lib.merchant.CouponProviderServiceGrpc;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.project.common.CommonConstants;
import com.ruoyi.project.merchant.domain.TCouponDetail;
import com.ruoyi.project.merchant.domain.TCouponType;
import com.ruoyi.project.merchant.domain.vo.AssignedCoupon;
import com.ruoyi.project.merchant.service.ITCouponCarnoRelationService;
import com.ruoyi.project.merchant.service.ITCouponDetailService;
import com.ruoyi.project.merchant.service.ITCouponTypeService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * description: 计算停车费服务类
 *
 * @author mingchenxu
 * @date 2023/2/25 10:55
 */
@Slf4j
@GrpcService
public class CouponProviderGrpcServiceImpl extends CouponProviderServiceGrpc.CouponProviderServiceImplBase {

    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    ITCouponCarnoRelationService assignedCouponService;

    @Resource
    ITCouponDetailService couponDetailService;

    @Autowired
    private ITCouponTypeService couponTypeService;

    @Override
    public void acquireCouponList(CouponProvider.CouponRequest request,
                                  StreamObserver<CouponProvider.CouponResponse> responseObserver) {

        String parkNo = request.getParkNo();
        String carNumber = request.getCarNumber();
        //有效期内且是分配状态的优惠券
        List<AssignedCoupon> assignedCouponVos = assignedCouponService.selectAssignedCouponList(parkNo, carNumber, null);
        //构建相应信息；
        CouponProvider.CouponResponse.Builder response = CouponProvider.CouponResponse.newBuilder();
        if (CollectionUtils.isEmpty(assignedCouponVos)) {
            response.setMess("无优惠券可使用");
            response.setStatus("500");
            response.addItemList(CouponProvider.AssignedCouponVo.newBuilder());
        } else {
            response.setMess("success");
            response.setStatus("200");
            assignedCouponVos.stream().forEach(eachAssignedCouponVo -> {
                CouponProvider.AssignedCouponVo.Builder addedAssignedCouponVo = CouponProvider.AssignedCouponVo.newBuilder();
                addedAssignedCouponVo.setCouponCode(eachAssignedCouponVo.getCouponCode());
                addedAssignedCouponVo.setCouponMold(DictUtils.getDictLabel(CommonConstants.COUPON_MOLD, eachAssignedCouponVo.getCouponMold()));
                addedAssignedCouponVo.setCouponStatus(DictUtils.getDictLabel(CommonConstants.COUPON_STATUS, eachAssignedCouponVo.getCouponStatus()));
                addedAssignedCouponVo.setCouponType(DictUtils.getDictLabel(CommonConstants.COUPON_TYPE, eachAssignedCouponVo.getCouponType()));
                addedAssignedCouponVo.setCarNumber(eachAssignedCouponVo.getCarNumber());
                addedAssignedCouponVo.setAllocatedTime(eachAssignedCouponVo.getAllocatedTime());
                addedAssignedCouponVo.setParkNo(eachAssignedCouponVo.getParkNo());
                addedAssignedCouponVo.setValidEndTime(eachAssignedCouponVo.getValidEndTime());
                addedAssignedCouponVo.setCouponValue(eachAssignedCouponVo.getCouponValue());
                addedAssignedCouponVo.setId(eachAssignedCouponVo.getId());
                response.addItemList(addedAssignedCouponVo.build());
            });
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateCouponStatus(CouponProvider.UsedCarCouponRequest request,
                                   StreamObserver<CouponProvider.CouponStatusResponse> responseObserver) {
        String usedTimeStr = request.getUsedTime();
        String couponDetailId = request.getCouponDetailId();
        List<AssignedCoupon> assignedCoupons = assignedCouponService.selectAssignedCouponList(null, null, Long.parseLong(couponDetailId));
        //构建相应信息；
        CouponProvider.CouponStatusResponse.Builder response = CouponProvider.CouponStatusResponse.newBuilder();
        if (CollectionUtils.isEmpty(assignedCoupons)) {
            response.setMess("此优惠券不存在");
            response.setStatus("500");
            response.setUsedCoupon(CouponProvider.AssignedCouponVo.newBuilder());
        } else {
            response.setMess("success");
            response.setStatus("200");
            //更新优惠券状态
            int result = 0;
            try {
                Date usedTime = DateUtils.parseDate(usedTimeStr, DateUtils.YYYY_MM_DD_HH_MM_SS);
                result = assignedCouponService.updateCouponStatus(Long.parseLong(couponDetailId), usedTime);
                if (result > 0) {
                    AssignedCoupon eachAssignedCouponVo = assignedCoupons.get(0);
                    CouponProvider.AssignedCouponVo.Builder assignedCouponVo = CouponProvider.AssignedCouponVo.newBuilder();
                    assignedCouponVo.setCouponCode(eachAssignedCouponVo.getCouponCode());
                    assignedCouponVo.setCouponMold(DictUtils.getDictLabel(CommonConstants.COUPON_MOLD, eachAssignedCouponVo.getCouponMold()));
                    assignedCouponVo.setCarNumber(eachAssignedCouponVo.getCarNumber());
                    assignedCouponVo.setAllocatedTime(eachAssignedCouponVo.getAllocatedTime());
                    assignedCouponVo.setParkNo(eachAssignedCouponVo.getParkNo());
                    assignedCouponVo.setValidEndTime(eachAssignedCouponVo.getValidEndTime());
                    assignedCouponVo.setCouponValue(eachAssignedCouponVo.getCouponValue());
                    assignedCouponVo.setId(eachAssignedCouponVo.getId());
                    assignedCouponVo.setCouponStatus(DictUtils.getDictLabel(CommonConstants.COUPON_STATUS, eachAssignedCouponVo.getCouponStatus()));
                    assignedCouponVo.setCouponType(DictUtils.getDictLabel(CommonConstants.COUPON_TYPE, eachAssignedCouponVo.getCouponType()));
                    response.setUsedCoupon(assignedCouponVo);
                }
                responseObserver.onNext(response.build());
                responseObserver.onCompleted();
            } catch (ParseException e) {
                log.info(e.getMessage());
            }
        }
    }

    @Override
    public void analyseCouponDetailDayFact(CouponProvider.AnalyseCouponDetailRequest request, StreamObserver<CouponProvider.AnalyseCouponDetailResponse> responseObserver) {
        String day = request.getDay();
        LocalDate date = LocalDate.parse(day);
        LocalDateTime atStartOfMonth = date.withDayOfMonth(1).atStartOfDay();
        CouponProvider.AnalyseCouponDetailResponse.Builder builder = CouponProvider.AnalyseCouponDetailResponse.newBuilder();
        builder.setMonth(date.getMonthValue())
                .setYear(date.getYear());
        //获取车场对应的优惠券类型
        String parkNo = request.getParkNo();
        List<TCouponType> typeList = couponTypeService.list(new LambdaQueryWrapper<TCouponType>().eq(TCouponType::getParkNo, parkNo));
        if (typeList.isEmpty()) {
            //如果该车场没有优惠券类型，则优惠券数量必定为0
            builder.setCount(0);
        } else {
            //获取车场对应的优惠券总数
            LambdaQueryWrapper<TCouponDetail> qw = new LambdaQueryWrapper<>();
            qw.ge(TCouponDetail::getCreateTime, atStartOfMonth)
                    .in(TCouponDetail::getCouponId, typeList.stream().map(TCouponType::getId).toList());
            long count = couponDetailService.count(qw);
            builder.setCount(Math.toIntExact(count));
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void analyseMerchantVoucher(CouponProvider.AnalyseMerchantVoucherRequest request, StreamObserver<CouponProvider.AnalyseMerchantVoucherResponse> responseObserver) {
        String day = request.getDay();
        LocalDate date = LocalDate.parse(day);
        LocalDateTime atStartOfMonth = date.withDayOfMonth(1).atStartOfDay();
        CouponProvider.AnalyseMerchantVoucherResponse.Builder builder = CouponProvider.AnalyseMerchantVoucherResponse.newBuilder();
        //获取车场对应的优惠券类型
        List<String> parkNos = new ArrayList<>(request.getParkNosList());
        List<TCouponType> typeList = couponTypeService.list(new LambdaQueryWrapper<TCouponType>().in(TCouponType::getParkNo, parkNos)
                .eq(TCouponType::getCouponMold, "2"));
        if (typeList.isEmpty()) {
            //如果该车场没有优惠券类型，则优惠券数量必定为0
            builder.setMerchantVoucher(0);
        } else {
            //获取车场对应的优惠券总数
            LambdaQueryWrapper<TCouponDetail> qw = new LambdaQueryWrapper<>();
            qw.ge(TCouponDetail::getCreateTime, atStartOfMonth)
                .in(TCouponDetail::getCouponId, typeList.stream().map(TCouponType::getId).toList());
            long count = couponDetailService.count(qw);
            builder.setMerchantVoucher(Math.toIntExact(count));
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
