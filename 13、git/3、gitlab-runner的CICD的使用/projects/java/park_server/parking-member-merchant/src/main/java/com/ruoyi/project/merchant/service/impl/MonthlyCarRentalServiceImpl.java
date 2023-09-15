package com.ruoyi.project.merchant.service.impl;

import com.czdx.grpc.lib.lot.*;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.merchant.domain.bo.BSettingRegularCarCategoryBO;
import com.ruoyi.project.merchant.domain.bo.BSettingRegularCarCategoryPriceBO;
import com.ruoyi.project.merchant.domain.bo.RegularCarBO;
import com.ruoyi.project.merchant.domain.vo.BSettingRegularCarCategoryVO;
import com.ruoyi.project.merchant.service.IMonthlyCarRentalService;
import com.ruoyi.project.parking.domain.BMyCar;
import com.ruoyi.project.parking.service.IBMyCarService;
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
 * <p>
 * 月租车管理Service实现类
 * </p>
 *
 * @author 琴声何来
 * @since 2023/3/3 16:01
 */
@Service
public class MonthlyCarRentalServiceImpl implements IMonthlyCarRentalService {
    @Autowired
    IBMyCarService myCarService;

    @GrpcClient("parking-lot-server")
    private RegularCarServiceGrpc.RegularCarServiceBlockingStub regularCarServiceBlockingStub;

    @GrpcClient("parking-lot-server")
    private BSettingRegularCarCategoryServiceGrpc.BSettingRegularCarCategoryServiceBlockingStub settingRegularCarCategoryServiceBlockingStub;

    /**
     * @apiNote 通过用户信息获取月租车信息
     */
    @Override
    public List<RegularCarBO> selectRegularCarByUserId() {
        // 获取该用户下的车辆
        BMyCar bMyCar = new BMyCar();
        bMyCar.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        List<BMyCar> myCars = myCarService.selectBMyCarList(bMyCar);
        // 调用grpc根据车牌号获取固定车记录
        SelectRegularCarByCarNumberRequestProto.Builder builder = SelectRegularCarByCarNumberRequestProto.newBuilder();
        myCars.forEach(myCar -> {
            builder.addCarNumberList(myCar.getCarNo());
        });
        SelectRegularCarByCarNumberRequestProto build = builder.build();
        SelectRegularCarByCarNumberResponseProto responseProto = regularCarServiceBlockingStub.selectRegularCarByCarNumber(build);
        List<RegularCarBO> regularCarBOList = new ArrayList<>();
        responseProto.getRegularCarProtoListList().forEach(proto -> {
            RegularCarBO regularCarBO = new RegularCarBO();
            regularCarBO.setId(proto.getId());
            regularCarBO.setParkNo(proto.getParkNo());
            regularCarBO.setParkName(proto.getParkName());
            regularCarBO.setCarNumber(proto.getCarNumber());
            regularCarBO.setCarColor(proto.getCarColor());
            regularCarBO.setCarRemark(proto.getCarRemark());
            regularCarBO.setCarCategoryId(proto.getCarCategoryId());
            regularCarBO.setOwnerCardId(proto.getOwnerCardId());
            regularCarBO.setOwnerName(proto.getOwnerName());
            regularCarBO.setOwnerAddress(proto.getOwnerAddress());
            regularCarBO.setOwnerPhone(proto.getOwnerPhone());
            regularCarBO.setFlowPlaceNumber(proto.getFlowPlaceNumber());
            if (StringUtils.isNotEmpty(proto.getStartTime())) {
                regularCarBO.setStartTime(LocalDate.parse(proto.getStartTime()));
            }
            if (StringUtils.isNotEmpty(proto.getEndTime())) {
                regularCarBO.setEndTime(LocalDate.parse(proto.getEndTime()));
            }
            regularCarBO.setRemark(proto.getRemark());
            regularCarBO.setRenewFlag(proto.getRenewFlag());
            regularCarBOList.add(regularCarBO);
        });
        return regularCarBOList;
    }

    /**
     * @apiNote 获取当前线上可续费的固定车类型
     */
    @Override
    public List<BSettingRegularCarCategoryBO> listOnlineCategory(BSettingRegularCarCategoryVO settingRegularCarCategoryVO) {
        List<BSettingRegularCarCategoryBO> list = new ArrayList<>();
        ListOnlineCategoryRequestProto listOnlineCategoryRequestProto = ListOnlineCategoryRequestProto.newBuilder()
                .setParkNo(settingRegularCarCategoryVO.getParkNo())
                .build();
        ListOnlineCategoryResponseProto listOnlineCategoryResponseProto = settingRegularCarCategoryServiceBlockingStub.listOnlineCategory(listOnlineCategoryRequestProto);
        listOnlineCategoryResponseProto.getSettingRegularCarCategoryProtoListList().forEach(proto -> {
            BSettingRegularCarCategoryBO settingRegularCarCategoryBO = new BSettingRegularCarCategoryBO();
            settingRegularCarCategoryBO.setId(proto.getId());
            settingRegularCarCategoryBO.setParkNo(proto.getParkNo());
            settingRegularCarCategoryBO.setName(proto.getName());
            settingRegularCarCategoryBO.setTimeLimit(proto.getTimeLimit());
            if (StringUtils.isNotEmpty(proto.getStartTime())){
                settingRegularCarCategoryBO.setStartTime(LocalDateTime.parse(proto.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            if (StringUtils.isNotEmpty(proto.getEndTime())){
                settingRegularCarCategoryBO.setEndTime(LocalDateTime.parse(proto.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            // 价格标准
            List<BSettingRegularCarCategoryPriceBO> priceList = new ArrayList<>();
            proto.getPriceListList().forEach(item -> {
                BSettingRegularCarCategoryPriceBO priceBO = new BSettingRegularCarCategoryPriceBO();
                BeanUtils.copyBeanProp(priceBO, item);
                priceBO.setPrice(BigDecimal.valueOf(item.getPrice()));
                priceList.add(priceBO);
            });
            settingRegularCarCategoryBO.setPriceList(priceList);
            list.add(settingRegularCarCategoryBO);
        });
        return list;
    }

}
