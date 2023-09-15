package com.ruoyi.project.parking.controller;

import com.czdx.grpc.lib.merchant.CouponProvider;
import com.czdx.grpc.lib.merchant.CouponProviderServiceGrpc;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.MyPageInfo;
import com.ruoyi.project.parking.domain.Student;
import com.ruoyi.project.parking.domain.rgpcmodel.AssignedCouponResponseEntity;
import com.ruoyi.project.parking.service.TestGrpcServiceImpl;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/testGrpc")
@RestController
public class TestGrpcController {

    @Autowired
    private TestGrpcServiceImpl testGrpcService;

    /**
     *
     * description: 测试GRPC服务调用
     * @author mingchenxu
     * @date 2023/2/20 15:10
     * @return com.ruoyi.framework.web.domain.AjaxResult
     */
    @GetMapping("/order")
    public AjaxResult orderGrpc() {
        MyPageInfo<Student> studentList = testGrpcService.getStudentList(1, 10, "mingchenxu", "");
        return AjaxResult.success(studentList);
    }


    @GrpcClient("parking-member-merchant-server")
    private CouponProviderServiceGrpc.CouponProviderServiceBlockingStub couponProviderServiceBlockingStub;



    @GetMapping("/doit")
    public void doIt() throws IOException {
        CouponProvider.CouponRequest couponRequest = CouponProvider.CouponRequest
                .newBuilder()
                .setCarNumber("苏A91898").setParkNo("200").build();

        CouponProvider.CouponResponse couponResponse = couponProviderServiceBlockingStub.acquireCouponList(couponRequest);
        if ("200".equals(couponResponse.getStatus())) {
            AssignedCouponResponseEntity  pojoBean = ProtoJsonUtil.toPojoBean(AssignedCouponResponseEntity.class, couponResponse);
            System.out.println(pojoBean);
        }

    }

}
