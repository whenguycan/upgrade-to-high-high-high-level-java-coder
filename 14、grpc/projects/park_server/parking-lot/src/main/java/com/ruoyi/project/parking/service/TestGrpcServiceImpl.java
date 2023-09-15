package com.ruoyi.project.parking.service;

import com.czdx.grpc.lib.school.StudentServiceGrpc;
import com.czdx.grpc.lib.school.StudentListRequest;
import com.czdx.grpc.lib.school.StudentListReply;
import com.ruoyi.project.parking.domain.Student;
import com.ruoyi.project.parking.domain.MyPageInfo;
import com.ruoyi.project.parking.utils.ProtoJsonUtil;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import io.grpc.StatusRuntimeException;

@Slf4j
@Service("testGrpcService")
public class TestGrpcServiceImpl {

    @GrpcClient("parking-charge-server")
    private StudentServiceGrpc.StudentServiceBlockingStub studentServiceBlockingStub;

    public MyPageInfo<Student> getStudentList(int pageNo, int pageSize, String name, String no) {
        MyPageInfo<Student> pageInfo = new MyPageInfo<>();
        try {
            // 组装查询参数，可以采用protojson工具类转换
            StudentListRequest listRequest = StudentListRequest.newBuilder()
                    .setPageNum(pageNo)
                    .setPageSize(pageSize)
                    .setName(name)
                    .setNo(no).build();
            StudentListReply studentList = studentServiceBlockingStub.getStudentList(listRequest);
            pageInfo = ProtoJsonUtil.toPojoBean(MyPageInfo.class, studentList);
        } catch (StatusRuntimeException e) {
            log.error( "FAILED with " + e.getStatus().getCode().name());
        } catch (Exception e) {
            log.error( "FAILED with " + e.getMessage());
        }
        return pageInfo;

    }


}
