package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.enums.DelFlag;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.parking.domain.RegularCar;
import com.ruoyi.project.parking.domain.bo.RegularCarBO;
import com.ruoyi.project.parking.domain.param.RegularCarParam;
import com.ruoyi.project.parking.domain.vo.RegularCarVO;
import com.ruoyi.project.parking.service.IRegularCarService;
import com.ruoyi.project.parking.mapper.RegularCarMapper;
import com.ruoyi.project.parking.utils.DateLocalDateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 琴声何来
 * @description 针对表【t_regular_car(固定车记录表)】的数据库操作Service实现
 * @since 2023-03-06 13:44:10
 */
@Service
public class RegularCarServiceImpl extends ServiceImpl<RegularCarMapper, RegularCar>
        implements IRegularCarService {

    /**
     * @apiNote 获取固定车记录列表 通过场地编号、车牌号、车位编号、车主联系方式
     */
    @Override
    public List<RegularCar> listByParkNoAndCarNumberAndPlaceNoAndOwnerAndCategoryIdAndCarType(RegularCar regularCar) {
        LambdaQueryWrapper<RegularCar> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.isNotEmpty(SecurityUtils.getParkNo()), RegularCar::getParkNo, SecurityUtils.getParkNo())
                .like(StringUtils.isNotEmpty(regularCar.getCarNumber()), RegularCar::getCarNumber, regularCar.getCarNumber())
                .like(StringUtils.isNotEmpty(regularCar.getOwnerName()), RegularCar::getOwnerName, regularCar.getOwnerName())
                .like(StringUtils.isNotEmpty(regularCar.getOwnerPhone()), RegularCar::getOwnerPhone, regularCar.getOwnerPhone())
                .eq(null != regularCar.getCarCategoryId(), RegularCar::getCarCategoryId, regularCar.getCarCategoryId())
                .eq(StringUtils.isNotEmpty(regularCar.getCarType()), RegularCar::getCarType, regularCar.getCarType())
                .eq(RegularCar::getDelFlag, DelFlag.NORMAL.getCode());
        return list(qw);
    }

    /**
     * @apiNote 获取固定车记录列表 不带认证
     */
    @Override
    public List<RegularCar> listUnsafe(RegularCar regularCar) {
        LambdaQueryWrapper<RegularCar> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotEmpty(regularCar.getCarNumber()), RegularCar::getCarNumber, regularCar.getCarNumber())
                .like(StringUtils.isNotEmpty(regularCar.getOwnerName()), RegularCar::getOwnerName, regularCar.getOwnerName())
                .like(StringUtils.isNotEmpty(regularCar.getOwnerPhone()), RegularCar::getOwnerPhone, regularCar.getOwnerPhone())
                .eq(null != regularCar.getCarCategoryId(), RegularCar::getCarCategoryId, regularCar.getCarCategoryId())
                .eq(StringUtils.isNotEmpty(regularCar.getCarType()), RegularCar::getCarType, regularCar.getCarType())
                .eq(StringUtils.isNotEmpty(regularCar.getParkNo()), RegularCar::getParkNo, regularCar.getParkNo())
                .eq(RegularCar::getDelFlag, DelFlag.NORMAL.getCode());
        return list(qw);
    }

    /**
     * @apiNote 新增单个固定车记录（线下）
     */
    @Override
    public boolean add(RegularCarVO regularCarVO) {
        //查询本固定车类型是否已有该固定车
        LambdaQueryWrapper<RegularCar> qw = new LambdaQueryWrapper<>();
        qw.eq(RegularCar::getParkNo, SecurityUtils.getParkNo())
                .eq(RegularCar::getCarCategoryId, regularCarVO.getCarCategoryId())
                .eq(RegularCar::getCarNumber, regularCarVO.getCarNumber())
                .eq(RegularCar::getDelFlag, DelFlag.NORMAL.getCode());
        if (count(qw) > 0) {
            throw new ServiceException("已有该车牌");
        }
        RegularCar settingRegularCarCategory = new RegularCar();
        BeanUtils.copyBeanProp(settingRegularCarCategory, regularCarVO);
        // 默认接口新增为线下类型
        settingRegularCarCategory.setCarType("0");
        settingRegularCarCategory.setParkNo(SecurityUtils.getParkNo());
        settingRegularCarCategory.setCreateBy(SecurityUtils.getUsername());
        settingRegularCarCategory.setCreateTime(LocalDateTime.now());
        return save(settingRegularCarCategory);
    }

    /**
     * @apiNote 新增单个固定车记录（线上）
     */
    @Override
    public boolean addOnline(RegularCarVO regularCarVO, String parkNo, String userName) {
        RegularCar regularCar = new RegularCar();
        BeanUtils.copyBeanProp(regularCar, regularCarVO);
        //线上不需认证
        regularCar.setParkNo(parkNo);
        regularCar.setCarType("1");
        regularCar.setCreateBy(userName);
        regularCar.setCreateTime(LocalDateTime.now());
        return save(regularCar);
    }

    /**
     * @apiNote 编辑单个固定车记录
     */
    @Override
    public boolean editById(RegularCarParam regularCarParam) {
        RegularCar settingRegularCarCategory = new RegularCar();
        BeanUtils.copyBeanProp(settingRegularCarCategory, regularCarParam);
        settingRegularCarCategory.setUpdateBy(SecurityUtils.getUsername());
        settingRegularCarCategory.setUpdateTime(LocalDateTime.now());
        return updateById(settingRegularCarCategory);
    }

    /**
     * @apiNote 逻辑删除单个固定车记录
     */
    @Override
    public boolean removeById(Integer id) {
        RegularCar settingRegularCarCategory = getById(id);
        if (settingRegularCarCategory == null) {
            return true;
        }
        LambdaUpdateWrapper<RegularCar> uw = new LambdaUpdateWrapper<>();
        uw.set(RegularCar::getDelFlag, DelFlag.DELETED.getCode())
                .set(RegularCar::getUpdateBy, SecurityUtils.getUsername())
                .set(RegularCar::getUpdateTime, LocalDateTime.now())
                .eq(RegularCar::getId, id);
        return update(uw);
    }

    /**
     * @apiNote 启用、禁用单个固定车记录
     */
    @Override
    public boolean switchStatusById(Integer id, String status) {
        RegularCar settingRegularCarCategory = getById(id);
        if (settingRegularCarCategory == null) {
            throw new ServiceException("获得固定车记录异常");
        }
        // 状态一致时不进行更新
        if (status.equals(settingRegularCarCategory.getStatus())) {
            return true;
        } else {
            LambdaUpdateWrapper<RegularCar> uw = new LambdaUpdateWrapper<>();
            uw.set(RegularCar::getStatus, status)
                    .set(RegularCar::getUpdateBy, SecurityUtils.getUsername())
                    .set(RegularCar::getUpdateTime, LocalDateTime.now())
                    .eq(RegularCar::getId, id);
            return update(uw);
        }
    }

    /**
     * @param regularCarList 导入数据内容
     * @param updateSupport  是否更新支持，如果已存在，则进行更新数据
     * @param carCategoryId  导入的分组类型编号
     * @return java.lang.String 导入信息
     * @apiNote 固定车列表导入
     * @author 琴声何来
     * @since 2023/2/28 14:53
     */
    @Override
    public String importRegularCar(List<RegularCarBO> regularCarList, boolean updateSupport, Integer carCategoryId) {
        if (StringUtils.isNull(regularCarList) || regularCarList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (RegularCarBO regularCarBO : regularCarList) {
            if (StringUtils.isEmpty(regularCarBO.getCarNumber())) {
                failureMsg.append("<br/>").append(failureNum).append("、固定车车牌号为空");
                continue;
            }
            LambdaQueryWrapper<RegularCar> qw = new LambdaQueryWrapper<>();
            // 验证是否存在这个固定车
            RegularCar regularCar = getOne(qw
                    .eq(StringUtils.isNotEmpty(SecurityUtils.getParkNo()), RegularCar::getParkNo, SecurityUtils.getParkNo())
                    .eq(RegularCar::getCarCategoryId, carCategoryId)
                    .eq(RegularCar::getCarNumber, regularCarBO.getCarNumber())
                    .eq(RegularCar::getDelFlag, DelFlag.NORMAL.getCode()));
            try {
                if (StringUtils.isNull(regularCar)) {
                    // 默认接口新增为线下类型
                    RegularCar addRegularCar = new RegularCar();
                    BeanUtils.copyBeanProp(addRegularCar, regularCarBO);
                    addRegularCar.setCarType("0");
                    addRegularCar.setParkNo(SecurityUtils.getParkNo());
                    addRegularCar.setStartTime(DateLocalDateUtils.dateToLocalDate(regularCarBO.getStartTime()));
                    addRegularCar.setEndTime(DateLocalDateUtils.dateToLocalDate(regularCarBO.getEndTime()));
                    addRegularCar.setCarCategoryId(carCategoryId);
                    addRegularCar.setCreateBy(SecurityUtils.getUsername());
                    addRegularCar.setCreateTime(LocalDateTime.now());
                    save(addRegularCar);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、固定车 ").append(regularCarBO.getCarNumber()).append(" 导入成功");
                } else if (updateSupport) {
                    RegularCar updateRegularCar = new RegularCar();
                    BeanUtils.copyBeanProp(updateRegularCar, regularCarBO);
                    updateRegularCar.setStartTime(DateLocalDateUtils.dateToLocalDate(regularCarBO.getStartTime()));
                    updateRegularCar.setEndTime(DateLocalDateUtils.dateToLocalDate(regularCarBO.getEndTime()));
                    updateRegularCar.setId(regularCar.getId());
                    updateRegularCar.setUpdateBy(SecurityUtils.getUsername());
                    updateRegularCar.setUpdateTime(LocalDateTime.now());
                    updateById(updateRegularCar);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、固定车 ").append(regularCarBO.getCarNumber()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、固定车 ").append(regularCarBO.getCarNumber()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、固定车 " + regularCarBO.getCarNumber() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}




