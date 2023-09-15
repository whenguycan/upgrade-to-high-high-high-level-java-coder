package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.enums.DelFlag;
import com.ruoyi.common.enums.OnlineFlag;
import com.ruoyi.common.enums.TimeLimit;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.common.constant.DictConstants;
import com.ruoyi.project.parking.domain.BSettingRegularCarCategory;
import com.ruoyi.project.parking.domain.BSettingRegularCarCategoryPrice;
import com.ruoyi.project.parking.domain.RegularCar;
import com.ruoyi.project.parking.domain.bo.BSettingRegularCarCategoryBO;
import com.ruoyi.project.parking.domain.param.BSettingRegularCarCategoryParam;
import com.ruoyi.project.parking.domain.vo.BSettingRegularCarCategoryVO;
import com.ruoyi.project.parking.mapper.BSettingRegularCarCategoryMapper;
import com.ruoyi.project.parking.service.IBSettingRegularCarCategoryPriceService;
import com.ruoyi.project.parking.service.IBSettingRegularCarCategoryService;
import com.ruoyi.project.parking.service.IRegularCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 琴声何来
 * @description 针对表【b_setting_regular_car_category(固定车类型表)】的数据库操作Service实现
 * @since 2023-03-01 14:07:40
 */
@Service
public class BSettingRegularCarCategoryServiceImpl extends ServiceImpl<BSettingRegularCarCategoryMapper, BSettingRegularCarCategory>
        implements IBSettingRegularCarCategoryService {

    @Autowired
    private IBSettingRegularCarCategoryPriceService settingRegularCarCategoryPriceService;

    @Autowired
    private IRegularCarService regularCarService;

    /**
     * @apiNote 查询固定车类型列表 通过场地编号和固定车类型识别码和分组类型编号
     */
    @Override
    public List<BSettingRegularCarCategoryBO> listByParkNoAndCodeAndGroupIdAndOnlineFlag(BSettingRegularCarCategory settingRegularCarCategory) {
        LambdaQueryWrapper<BSettingRegularCarCategory> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(SecurityUtils.getParkNo()), BSettingRegularCarCategory::getParkNo, SecurityUtils.getParkNo())
                .eq(StringUtils.isNotEmpty(settingRegularCarCategory.getCode()), BSettingRegularCarCategory::getCode, settingRegularCarCategory.getCode())
                .eq(StringUtils.isNotEmpty(settingRegularCarCategory.getGroupId()), BSettingRegularCarCategory::getGroupId, settingRegularCarCategory.getGroupId())
                .eq(settingRegularCarCategory.getOnlineFlag() != null, BSettingRegularCarCategory::getOnlineFlag, settingRegularCarCategory.getOnlineFlag())
                .eq(BSettingRegularCarCategory::getDelFlag, DelFlag.NORMAL.getCode());
        List<BSettingRegularCarCategory> list = list(qw);
        // 增加分组类型字典项描述
        List<BSettingRegularCarCategoryBO> result = new ArrayList<>();
        list.forEach(item -> {
            BSettingRegularCarCategoryBO bSettingRegularCarCategoryBO = new BSettingRegularCarCategoryBO();
            BeanUtils.copyBeanProp(bSettingRegularCarCategoryBO, item);
            bSettingRegularCarCategoryBO.setGroupName(DictUtils.getDictLabel(DictConstants.MOD_CAR_OWNER_GROUP_TYPE_KEY, item.getGroupId()));
            result.add(bSettingRegularCarCategoryBO);
        });
        return result;
    }

    /**
     * @apiNote 获取当前线上可续费的固定车类型
     */
    @Override
    public List<BSettingRegularCarCategoryBO> listOnline(BSettingRegularCarCategory settingRegularCarCategory) {
        List<BSettingRegularCarCategoryBO> list=new ArrayList<>();
        LambdaQueryWrapper<BSettingRegularCarCategory> qw = new LambdaQueryWrapper<>();
        qw.eq(BSettingRegularCarCategory::getParkNo, settingRegularCarCategory.getParkNo())
                .and(wrapper -> {
                    wrapper.eq(BSettingRegularCarCategory::getTimeLimit, TimeLimit.UNLIMITED.getCode())
                            .or(timeWrapper -> {
                                timeWrapper.le(BSettingRegularCarCategory::getStartTime, LocalDateTime.now())
                                        .ge(BSettingRegularCarCategory::getEndTime, LocalDateTime.now());
                            });
                })
                .eq(BSettingRegularCarCategory::getOnlineFlag, OnlineFlag.ALL.getCode())
                .eq(BSettingRegularCarCategory::getDelFlag, DelFlag.NORMAL.getCode());
        List<BSettingRegularCarCategory> categories = list(qw);
        categories.forEach(category->{
            BSettingRegularCarCategoryBO carCategoryBO = new BSettingRegularCarCategoryBO();
            BeanUtils.copyBeanProp(carCategoryBO, category);
            // 查询价格子表
            LambdaQueryWrapper<BSettingRegularCarCategoryPrice> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BSettingRegularCarCategoryPrice::getRegularCarCategoryId, category.getId());
            carCategoryBO.setPriceList(settingRegularCarCategoryPriceService.list(queryWrapper));
            list.add(carCategoryBO);
        });
        return list;
    }

    /**
     * @apiNote 获取单个固定车类型
     */
    @Override
    public BSettingRegularCarCategoryBO getBSettingRegularCarCategoryById(Integer id) {
        BSettingRegularCarCategory carCategory = getById(id);
        BSettingRegularCarCategoryBO carCategoryBO = new BSettingRegularCarCategoryBO();
        BeanUtils.copyBeanProp(carCategoryBO, carCategory);
        // 查询价格子表
        LambdaQueryWrapper<BSettingRegularCarCategoryPrice> qw = new LambdaQueryWrapper<>();
        qw.eq(BSettingRegularCarCategoryPrice::getRegularCarCategoryId, id);
        carCategoryBO.setPriceList(settingRegularCarCategoryPriceService.list(qw));
        return carCategoryBO;
    }

    /**
     * @apiNote 新增 固定车类型
     */
    @Override
    public boolean add(BSettingRegularCarCategoryVO settingRegularCarCategoryVO) {
        BSettingRegularCarCategory settingRegularCarCategory = new BSettingRegularCarCategory();
        BeanUtils.copyBeanProp(settingRegularCarCategory, settingRegularCarCategoryVO);
        settingRegularCarCategory.setParkNo(SecurityUtils.getParkNo());
        settingRegularCarCategory.setCreateBy(SecurityUtils.getUsername());
        settingRegularCarCategory.setCreateTime(LocalDateTime.now());
        save(settingRegularCarCategory);
        settingRegularCarCategoryVO.getPriceList().forEach(price -> {
            price.setParkNo(SecurityUtils.getParkNo());
            price.setRegularCarCategoryId(settingRegularCarCategory.getId());
            price.setCreateBy(settingRegularCarCategory.getCreateBy());
            price.setCreateTime(settingRegularCarCategory.getCreateTime());
        });
        return settingRegularCarCategoryPriceService.saveBatch(settingRegularCarCategoryVO.getPriceList());
    }

    /**
     * @apiNote 编辑 固定车类型
     */
    @Override
    public boolean editById(BSettingRegularCarCategoryParam settingRegularCarCategoryParam) {
        BSettingRegularCarCategory settingRegularCarCategory = new BSettingRegularCarCategory();
        BeanUtils.copyBeanProp(settingRegularCarCategory, settingRegularCarCategoryParam);
        settingRegularCarCategory.setUpdateBy(SecurityUtils.getUsername());
        settingRegularCarCategory.setUpdateTime(LocalDateTime.now());
        //删除原有价格数据
        settingRegularCarCategoryPriceService.remove(new LambdaQueryWrapper<BSettingRegularCarCategoryPrice>()
                .eq(BSettingRegularCarCategoryPrice::getRegularCarCategoryId, settingRegularCarCategoryParam.getId()));
        //重新新增价格数据
        settingRegularCarCategoryParam.getPriceList().forEach(price -> {
            price.setParkNo(SecurityUtils.getParkNo());
            price.setRegularCarCategoryId(settingRegularCarCategory.getId());
            price.setCreateBy(settingRegularCarCategory.getCreateBy());
            price.setCreateTime(settingRegularCarCategory.getCreateTime());
        });
        settingRegularCarCategoryPriceService.saveBatch(settingRegularCarCategoryParam.getPriceList());
        return updateById(settingRegularCarCategory);
    }

    /**
     * @apiNote 逻辑删除 固定车类型
     */
    @Override
    public boolean removeById(Integer id) {
        BSettingRegularCarCategory settingRegularCarCategory = getById(id);
        if (settingRegularCarCategory == null) {
            return true;
        }
        // 判断该固定车类型下有无固定车，如有，则不允许删除
        LambdaQueryWrapper<RegularCar> qw = new LambdaQueryWrapper<>();
        qw.eq(RegularCar::getCarCategoryId, id)
                .eq(RegularCar::getParkNo, settingRegularCarCategory.getParkNo())
                .eq(RegularCar::getDelFlag, DelFlag.NORMAL.getCode());
        if (regularCarService.count(qw) > 0) {
            throw new ServiceException("固定车记录不为空，删除失败");
        }
        LambdaUpdateWrapper<BSettingRegularCarCategory> uw = new LambdaUpdateWrapper<>();
        uw.set(BSettingRegularCarCategory::getDelFlag, DelFlag.DELETED.getCode())
                .set(BSettingRegularCarCategory::getUpdateBy, SecurityUtils.getUsername())
                .set(BSettingRegularCarCategory::getUpdateTime, LocalDateTime.now())
                .eq(BSettingRegularCarCategory::getId, id);
        return update(uw);
    }

    /**
     * @apiNote 启用、停用 固定车类型
     */
    @Override
    public boolean switchStatusById(Integer id, String status) {
        BSettingRegularCarCategory settingRegularCarCategory = getById(id);
        if (settingRegularCarCategory == null) {
            throw new ServiceException("获得固定车类型异常");
        }
        // 状态一致时不进行更新
        if (status.equals(settingRegularCarCategory.getStatus())) {
            return true;
        } else {
            LambdaUpdateWrapper<BSettingRegularCarCategory> uw = new LambdaUpdateWrapper<>();
            uw.set(BSettingRegularCarCategory::getStatus, status)
                    .set(BSettingRegularCarCategory::getUpdateBy, SecurityUtils.getUsername())
                    .set(BSettingRegularCarCategory::getUpdateTime, LocalDateTime.now())
                    .eq(BSettingRegularCarCategory::getId, id);
            return update(uw);
        }
    }

}




