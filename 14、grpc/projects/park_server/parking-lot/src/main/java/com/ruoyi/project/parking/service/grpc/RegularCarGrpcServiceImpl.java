package com.ruoyi.project.parking.service.grpc;

import com.czdx.grpc.lib.lot.RegularCarProtoInfo;
import com.czdx.grpc.lib.lot.RegularCarServiceGrpc;
import com.czdx.grpc.lib.lot.SelectRegularCarByCarNumberRequestProto;
import com.czdx.grpc.lib.lot.SelectRegularCarByCarNumberResponseProto;
import com.ruoyi.project.parking.domain.BSelfPayScheme;
import com.ruoyi.project.parking.domain.RegularCar;
import com.ruoyi.project.parking.service.IBSelfPaySchemeService;
import com.ruoyi.project.parking.service.IRegularCarService;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.service.ISysDeptService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@GrpcService
public class RegularCarGrpcServiceImpl extends RegularCarServiceGrpc.RegularCarServiceImplBase {
    @Autowired
    IRegularCarService regularCarService;

    @Autowired
    IBSelfPaySchemeService selfPaySchemeService;

    @Autowired
    ISysDeptService sysDeptService;

    @Override
    public void selectRegularCarByCarNumber(SelectRegularCarByCarNumberRequestProto request, StreamObserver<SelectRegularCarByCarNumberResponseProto> responseObserver) {
        //根据场库编号获取自助续费规则
        List<BSelfPayScheme> schemeList = selfPaySchemeService.list();
        //获取场库名称信息
        List<SysDept> sysDepts = sysDeptService.selectDeptListUnsafe(null);
        SelectRegularCarByCarNumberResponseProto.Builder builder = SelectRegularCarByCarNumberResponseProto.newBuilder();
        request.getCarNumberListList().forEach(carNumber -> {
            RegularCar regularCar = new RegularCar();
            regularCar.setCarNumber(carNumber);
            //根据车牌号查询当前未被删除的所有固定车记录
            List<RegularCar> regularCarList = regularCarService.listUnsafe(regularCar);
            if (regularCarList.size() > 0) {
                regularCarList.forEach(item -> {
                    BSelfPayScheme selfPayScheme = schemeList.stream().filter(scheme -> scheme.getParkNo().equals(item.getParkNo())).toList().get(0);
                    SysDept sysDept = sysDepts.stream().filter(dept -> dept.getParkNo().equals(item.getParkNo())).toList().get(0);
                    builder.addRegularCarProtoList(
                            RegularCarProtoInfo.newBuilder()
                                    .setId(item.getId())
                                    .setParkNo(item.getParkNo())
                                    .setParkName(sysDept.getDeptName())
                                    .setCarNumber(item.getCarNumber())
                                    .setCarColor(item.getCarColor())
                                    .setCarRemark(item.getCarRemark())
                                    .setCarCategoryId(item.getCarCategoryId())
                                    .setOwnerCardId(item.getOwnerCardId())
                                    .setOwnerName(item.getOwnerName())
                                    .setOwnerAddress(item.getOwnerAddress())
                                    .setOwnerPhone(item.getOwnerPhone())
                                    .setFlowPlaceNumber(item.getFlowPlaceNumber())
                                    .setStartTime(item.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                                    .setEndTime(item.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                                    .setRemark(item.getRemark())
                                    //结束日期-当前日期超过最大续费天数不能续费，即结束日期-最大续费天数在当前日期之后不能续费
                                    //结束日期-当前日期超过续费临期天数不能续费，即结束日期-续费临期天数在当前日期之后不能续费
                                    .setRenewFlag(!(item.getEndTime().minus(selfPayScheme.getMaxRenewDays(), ChronoUnit.DAYS).isAfter(LocalDate.now()) ||
                                            item.getEndTime().minus(selfPayScheme.getRenewDeadlineDays(), ChronoUnit.DAYS).isAfter(LocalDate.now())))
                                    .build());
                });
            }
        });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
