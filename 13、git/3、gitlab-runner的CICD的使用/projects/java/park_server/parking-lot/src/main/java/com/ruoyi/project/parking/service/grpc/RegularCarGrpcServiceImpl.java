package com.ruoyi.project.parking.service.grpc;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czdx.grpc.lib.lot.*;
import com.ruoyi.project.parking.domain.BSelfPayScheme;
import com.ruoyi.project.parking.domain.RegularCar;
import com.ruoyi.project.parking.domain.vo.RegularCarVO;
import com.ruoyi.project.parking.service.IBSelfPaySchemeService;
import com.ruoyi.project.parking.service.IRegularCarService;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.service.ISysDeptService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    /**
     * @apiNote 根据车牌号查询固定车数据
     */
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
                    RegularCarProtoInfo.Builder regularCarBuilder = RegularCarProtoInfo.newBuilder();
                    if (item.getStartTime() != null) {
                        regularCarBuilder.setStartTime(item.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                    if (item.getEndTime() != null) {
                        regularCarBuilder.setEndTime(item.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                                //结束日期-当前日期超过最大续费天数不能续费，即结束日期-最大续费天数在当前日期之后不能续费
                                //结束日期-当前日期超过续费临期天数不能续费，即结束日期-续费临期天数在当前日期之后不能续费
                                .setRenewFlag(!(item.getEndTime().minus(selfPayScheme.getMaxRenewDays(), ChronoUnit.DAYS).isAfter(LocalDate.now()) ||
                                        item.getEndTime().minus(selfPayScheme.getRenewDeadlineDays(), ChronoUnit.DAYS).isAfter(LocalDate.now())));
                    } else {
                        regularCarBuilder.setRenewFlag(false);
                    }
                    builder.addRegularCarProtoList(
                            regularCarBuilder
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
                                    .setTimeLimit(item.getTimeLimit())
                                    .setRemark(item.getRemark())
                                    .build());
                });
            }
        });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    /**
     * @apiNote 修改固定车时间或新增固定车记录
     */
    @Override
    public void updateRegularCarAfterPay(UpdateRegularCarAfterPayRequestProto request, StreamObserver<UpdateRegularCarAfterPayResponseProto> responseObserver) {
        //获取固定车记录
        LambdaQueryWrapper<RegularCar> qw = new LambdaQueryWrapper<>();
        qw.eq(RegularCar::getParkNo, request.getParkNo())
                .eq(RegularCar::getCarCategoryId, request.getRegularCarCategoryId())
                .eq(RegularCar::getCarNumber, request.getCarNumber())
                .last("limit 1");
        RegularCar regularCar = regularCarService.getOne(qw);
        //更新结果
        boolean status = false;
        if (regularCar == null) {
            //如果原本没有固定车记录，则为新开通车辆，新建一条固定车记录
            RegularCarVO regularCarVO = new RegularCarVO();
            regularCarVO.setCarNumber(request.getCarNumber());
            regularCarVO.setCarColor("");
            regularCarVO.setCarRemark("");
            regularCarVO.setOwnerCardId("");
            regularCarVO.setOwnerName("");
            regularCarVO.setOwnerAddress("");
            regularCarVO.setOwnerPhone(request.getPhoneNumber());
            regularCarVO.setCarCategoryId(request.getRegularCarCategoryId());
            regularCarVO.setFlowPlaceNumber(1);
            regularCarVO.setTimeLimit("0");
            regularCarVO.setRemark("");
            regularCarVO.setStartTime(LocalDate.now());
            regularCarVO.setEndTime(LocalDate.now().plusDays(request.getRentalDays()));
            //新增固定车数据
            status = regularCarService.addOnline(regularCarVO, request.getParkNo(), request.getUserName());
        } else {
            //有固定车记录，则判断当前日期是否在结束日期之后，如果在结束日期之后，则有效期为当前日期+续费天数
            if (LocalDate.now().isAfter(regularCar.getEndTime())) {
                regularCar.setStartTime(LocalDate.now());
                regularCar.setEndTime(LocalDate.now().plusDays(request.getRentalDays()));
            } else {
                //当前日期不在结束日期之后，则直接推迟结束日期
                regularCar.setEndTime(regularCar.getEndTime().plusDays(request.getRentalDays()));
            }
            //更新固定车数据
            status = regularCarService.updateById(regularCar);
        }
        responseObserver.onNext(UpdateRegularCarAfterPayResponseProto.newBuilder().setStatus(status).build());
        responseObserver.onCompleted();
    }
}
