package com.ruoyi.project.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.czdx.grpc.lib.merchant.CouponTaskServiceGrpc;
import com.google.protobuf.Empty;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.merchant.domain.TCouponDetail;
import com.ruoyi.project.merchant.mapper.TCouponDetailMapper;
import com.ruoyi.project.merchant.service.ITCouponDetailService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component("CouponTask")
@GrpcService
public class CouponTask extends CouponTaskServiceGrpc.CouponTaskServiceImplBase {
    @Autowired
    TCouponDetailMapper tCouponDetailMapper;
    @Autowired
    ITCouponDetailService itCouponDetailService;
    @Override
    public void refeshCoupon(Empty request, StreamObserver<Empty> responseObserver) {
        //筛选出过期数据
        QueryWrapper<TCouponDetail> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("coupon_status","1").le("vaild_end_time", DateUtils.getNowDate());
        List<TCouponDetail> tCouponDetails=tCouponDetailMapper.selectList(queryWrapper);

        TCouponDetail newTCouponDetail=new TCouponDetail();
        newTCouponDetail.setCouponStatus("0");
        for (TCouponDetail tCouponDetail:tCouponDetails){
            //修改状态
            tCouponDetail.setCouponStatus("3");
            tCouponDetailMapper.updateTCouponDetail(tCouponDetail);
            //重新插入一张未分配的新优惠券
            newTCouponDetail.setCouponId(tCouponDetail.getCouponId());
            itCouponDetailService.insertTCouponDetail(newTCouponDetail);
        }
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();

    }


}
