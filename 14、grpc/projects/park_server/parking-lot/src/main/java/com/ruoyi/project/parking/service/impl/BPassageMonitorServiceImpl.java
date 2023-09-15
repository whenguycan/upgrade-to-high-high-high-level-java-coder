package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.parking.domain.*;
import com.ruoyi.project.parking.mapper.BSettingLotMapper;
import com.ruoyi.project.parking.service.BPassageMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BPassageMonitorServiceImpl implements BPassageMonitorService {
    @Autowired
    private WhiteListServiceImpl whiteListService;
    @Autowired
    private BlackListServiceImpl blackListService;
    @Autowired
    private BPassageServiceImpl bPassageService;
    @Autowired
    private BSettingLotMapper bSettingLotMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RegularCarServiceImpl regularCarService;
    @Autowired
    private BSettingRegularCarCategoryServiceImpl bSettingRegularCarCategoryPriceService;


    @Override
    public boolean openByOpenType(String carNumber, String parkNo, Integer passageId,String carType) {
        BPassage bPassage=bPassageService.queryById(passageId);
        //开闸方式
        String openType=bPassage.getOpenType();
        //人工开闸直接返回false
        //如果是黑名单直接返回false
        if ("manual".equals(openType)||judgeBlack(carNumber,parkNo)){
            return false;
        }
        //如果在白名单里直接放行
        if (judgeWhite(carNumber,parkNo)){
            return true;
        }
        //余位
        String remainder = redisCache.getCacheObject(CacheConstants.PARKNO_ACCOUNT_KEY + parkNo);

        LambdaQueryWrapper<BSettingLot> settingLotQueryWrapper = new LambdaQueryWrapper<>();
        settingLotQueryWrapper.eq(BSettingLot::getParkNo, parkNo);
        BSettingLot bSettingLot = bSettingLotMapper.selectOne(settingLotQueryWrapper);

        List<String> bandCarTypes = Arrays.asList(bSettingLot.getBandCarTypes().split(","));

        //不在黑白名单内
        //车辆类型不匹配
        if (!bandCarTypes.contains(carType)) {
            return false;
        } else {
            //车辆类型匹配，但是余位不足，且余位不足不放行
            if ("0".equals(bSettingLot.getIsPermitEntry()) && Integer.valueOf(remainder) < 1) {
                return false;
            }
        }


        //联网计算机控制开闸
        if ("Auto".equals(openType)){
            return true;
        }else if ("Vip".equals(openType)){
            RegularCar regularCar=new RegularCar();
            regularCar.setCarNumber(carNumber);
            regularCar.setParkNo(parkNo);
            List<RegularCar> regularCarList=regularCarService.listUnsafe(regularCar);
            if (regularCarList.size()>0){
                //固定车类型和通道绑定的固定车类型一致则放行
                BSettingRegularCarCategory bSettingRegularCarCategory=bSettingRegularCarCategoryPriceService.getById(regularCarList.get(0).getCarCategoryId());
                if (bPassage.getBandRegularCodes().contains(bSettingRegularCarCategory.getCode())){
                    return true;
                }
            }
        }
        return false;
    }

    //判断是否黑名单
    private boolean judgeBlack(String carNumber, String parkNo){
        BlackList blackList=new BlackList();
        blackList.setCarNumber(carNumber);
        blackList.setParkNo(parkNo);
        List<BlackList> list= blackListService.listByParkNoAndCarNumberUnsafe(blackList);
        if (list.size()>0){
            return true;
        }
        return false;
    }

    //判断是否黑名单
    private boolean judgeWhite(String carNumber, String parkNo){
        WhiteList whiteList=new WhiteList();
        whiteList.setCarNumber(carNumber);
        whiteList.setParkNo(parkNo);
        List<WhiteList> list= whiteListService.listByParkNoAndCarNumber(whiteList);
        if (list.size()>0){
            return true;
        }
        return false;
    }
}
