package com.czdx.parkingpayment.service;

import com.czdx.grpc.lib.school.StudentRequest;
import com.czdx.grpc.lib.school.StudentScoreReply;
import com.czdx.grpc.lib.school.StudentServiceGrpc;
import com.czdx.grpc.lib.school.StudentListRequest;
import com.czdx.grpc.lib.school.StudentListReply;
import com.czdx.parkingpayment.domain.MyPageInfo;
import com.czdx.parkingpayment.domain.Student;
import com.czdx.parkingpayment.utils.ProtoJsonUtil;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * description: 测试GRPC服务调用实现类
 * @author mingchenxu
 * @date 2023/2/20 14:47
 */
@GrpcService
public class TestProvideGrpcServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {

    /**
     *
     * description: 获取学生列表
     * @author mingchenxu
     * @date 2023/2/18 23:32
     * @param request
     * @param responseObserver
     */
    @Override
    public void getStudentList(StudentListRequest request, StreamObserver<StudentListReply> responseObserver) {
        // todo 获取查询条件，查询数据库返回结果
        String stuName = request.getName();
        String stuNo = request.getNo();
        int pageNum = request.getPageNum();
        int pageSize = request.getPageSize();
        // 方式1：自己使用Builder组装数据
        //        Student s1 = Student.newBuilder().setName("学生1").setNo("s1").build();
        //        Student s2 = Student.newBuilder().setName("学生2").setNo("s2").build();
        //
        //        StudentListReply reply = StudentListReply.newBuilder()
        //                .setTotal(100)
        //                .setPageNum(1).setPageSize(10)
        //                .addList(s1).addList(s2)
        //                .build();

        // 方式2：使用ProtoJson工具转换
        Student stu1 = new Student();
        stu1.setName("学生1");
        stu1.setNo("s1");
        Student stu2 = new Student();
        stu2.setName("学生2");
        stu2.setNo("s2");
        List<Student> stuList = Arrays.asList(stu1, stu2);
        MyPageInfo<Student> page = new MyPageInfo<>();
        page.setTotal(100);
        page.setPageNum(1);
        page.setPageSize(2);
        page.setList(stuList);

        StudentListReply.Builder builder = StudentListReply.newBuilder();

        try {
            // 转换为protobuf
            ProtoJsonUtil.toProtoBean(builder, page);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    /**
     *
     * description: 获取学生成绩得分
     * @author mingchenxu
     * @date 2023/2/18 23:32
     * @param request
     * @param responseObserver
     */
    @Override
    public void getStudentScore(StudentRequest request, StreamObserver<StudentScoreReply> responseObserver) {
        super.getStudentScore(request, responseObserver);
    }

}
