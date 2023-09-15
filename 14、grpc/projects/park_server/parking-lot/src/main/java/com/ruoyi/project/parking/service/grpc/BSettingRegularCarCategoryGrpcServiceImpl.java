package com.ruoyi.project.parking.service.grpc;

import com.czdx.grpc.lib.lot.*;
import com.ruoyi.project.parking.domain.BSettingRegularCarCategory;
import com.ruoyi.project.parking.domain.BSettingRegularCarCategoryPrice;
import com.ruoyi.project.parking.domain.bo.BSettingRegularCarCategoryBO;
import com.ruoyi.project.parking.service.IBSettingRegularCarCategoryPriceService;
import com.ruoyi.project.parking.service.IBSettingRegularCarCategoryService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@GrpcService
public class BSettingRegularCarCategoryGrpcServiceImpl extends BSettingRegularCarCategoryServiceGrpc.BSettingRegularCarCategoryServiceImplBase {
    @Autowired
    IBSettingRegularCarCategoryService settingRegularCarCategoryService;

    @Autowired
    IBSettingRegularCarCategoryPriceService settingRegularCarCategoryPriceService;


    /**
     * @apiNote 获取当前线上可续费的固定车类型
     */
    @Override
    public void listOnlineCategory(ListOnlineCategoryRequestProto request, StreamObserver<ListOnlineCategoryResponseProto> responseObserver) {
        BSettingRegularCarCategory category = new BSettingRegularCarCategory();
        category.setParkNo(request.getParkNo());
        List<BSettingRegularCarCategoryBO> categories = settingRegularCarCategoryService.listOnline(category);
        List<BSettingRegularCarCategoryProtoInfo> info = new ArrayList<>();
        categories.forEach(item -> {
            List<BSettingRegularCarCategoryPriceProtoInfo> priceList = new ArrayList<>();
            item.getPriceList().forEach(priceItem -> {
                priceList.add(BSettingRegularCarCategoryPriceProtoInfo.newBuilder()
                        .setId(priceItem.getId())
                        .setParkNo(priceItem.getParkNo())
                        .setRegularCarCategoryId(priceItem.getRegularCarCategoryId())
                        .setMonth(priceItem.getMonth())
                        .setPrice(priceItem.getPrice().doubleValue())
                        .build());
            });
            BSettingRegularCarCategoryProtoInfo.Builder builder = BSettingRegularCarCategoryProtoInfo.newBuilder();
            if (item.getStartTime() != null) {
                builder.setStartTime(item.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            if (item.getEndTime() != null) {
                builder.setEndTime(item.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            BSettingRegularCarCategoryProtoInfo settingRegularCarCategoryProtoInfo = builder
                    .setId(item.getId())
                    .setParkNo(item.getParkNo())
                    .setName(item.getName())
                    .setTimeLimit(item.getTimeLimit())
                    .addAllPriceList(priceList)
                    .build();
            info.add(settingRegularCarCategoryProtoInfo);
        });
        responseObserver.onNext(ListOnlineCategoryResponseProto.newBuilder().addAllSettingRegularCarCategoryProtoList(info).build());
        responseObserver.onCompleted();
    }

    /**
     * @apiNote 根据价格参数id获取价格参数详情
     */
    @Override
    public void getCategoryPriceById(GetCategoryPriceByIdRequestProto request, StreamObserver<GetCategoryPriceByIdResponseProto> responseObserver) {
        BSettingRegularCarCategoryPrice price = settingRegularCarCategoryPriceService.getById(request.getId());
        if (price != null) {
            GetCategoryPriceByIdResponseProto build = GetCategoryPriceByIdResponseProto.newBuilder()
                    .setId(price.getId())
                    .setParkNo(price.getParkNo())
                    .setRegularCarCategoryId(price.getRegularCarCategoryId())
                    .setMonth(price.getMonth())
                    .setPrice(price.getPrice().doubleValue())
                    .build();
            responseObserver.onNext(build);
        }
        responseObserver.onCompleted();
    }

    /**
     * @apiNote 获取所有固定车类型
     */
    @Override
    public void listAllCategory(ListAllCategoryRequestProto request, StreamObserver<ListAllCategoryResponseProto> responseObserver) {
        List<BSettingRegularCarCategory> list = settingRegularCarCategoryService.list();
        List<BSettingRegularCarCategoryProtoInfo> info = new ArrayList<>();
        list.forEach(item -> {
            BSettingRegularCarCategoryProtoInfo.Builder builder = BSettingRegularCarCategoryProtoInfo.newBuilder();
            if (item.getStartTime() != null) {
                builder.setStartTime(item.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            if (item.getEndTime() != null) {
                builder.setEndTime(item.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            BSettingRegularCarCategoryProtoInfo settingRegularCarCategoryProtoInfo = builder
                    .setId(item.getId())
                    .setParkNo(item.getParkNo())
                    .setName(item.getName())
                    .setTimeLimit(item.getTimeLimit())
                    .build();
            info.add(settingRegularCarCategoryProtoInfo);
        });
        responseObserver.onNext(ListAllCategoryResponseProto.newBuilder().addAllSettingRegularCarCategoryProtoList(info).build());
        responseObserver.onCompleted();
    }

    /**
     * @apiNote 根据id获取单个固定车类型
     */
    @Override
    public void getCategoryById(GetCategoryByIdRequestProto request, StreamObserver<GetCategoryByIdResponseProto> responseObserver) {
        BSettingRegularCarCategory regularCarCategory = settingRegularCarCategoryService.getById(request.getId());
        BSettingRegularCarCategoryProtoInfo.Builder builder = BSettingRegularCarCategoryProtoInfo.newBuilder();
        if (regularCarCategory.getStartTime() != null) {
            builder.setStartTime(regularCarCategory.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (regularCarCategory.getEndTime() != null) {
            builder.setEndTime(regularCarCategory.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        builder.setId(regularCarCategory.getId())
                .setParkNo(regularCarCategory.getParkNo())
                .setName(regularCarCategory.getName())
                .setTimeLimit(regularCarCategory.getTimeLimit());
        responseObserver.onNext(GetCategoryByIdResponseProto.newBuilder().setSettingRegularCarCategoryProtoList(builder.build()).build());
        responseObserver.onCompleted();
    }
}
