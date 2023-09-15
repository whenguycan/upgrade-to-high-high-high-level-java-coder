package com.ruoyi.project.parking.service.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.BMyCar;
import com.ruoyi.project.parking.enums.BMyCarEnums;
import com.ruoyi.project.parking.mapper.BMyCarMapper;
import com.ruoyi.project.parking.service.IBMyCarService;
import com.ruoyi.project.system.domain.vo.SysUserVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * H5我的车辆管理Service业务层处理
 *
 * @author fangch
 * @date 2023-03-02
 */
@Service
public class BMyCarServiceImpl implements IBMyCarService {
    @Autowired
    private BMyCarMapper bMyCarMapper;

    /**
     * 查询H5我的车辆管理
     *
     * @param id H5我的车辆管理主键
     * @return H5我的车辆管理
     */
    @Override
    public BMyCar selectBMyCarById(Integer id) {
        return bMyCarMapper.selectBMyCarById(id);
    }

    /**
     * 查询H5我的车辆管理列表
     *
     * @param bMyCar H5我的车辆管理
     * @return H5我的车辆管理
     */
    @Override
    public List<BMyCar> selectBMyCarList(BMyCar bMyCar) {
        return bMyCarMapper.selectBMyCarList(bMyCar);
    }

    /**
     * 新增H5我的车辆管理
     *
     * @param bMyCar H5我的车辆管理
     * @return 结果
     */
    @Override
    public AjaxResult insertBMyCar(BMyCar bMyCar) {
        if (StringUtils.isEmpty(bMyCar.getCarNo())) {
            return AjaxResult.warn("车牌号不可为空");
        }
        BMyCar queryParam = new BMyCar();
        queryParam.setCarNo(bMyCar.getCarNo());
        List<BMyCar> existCars = bMyCarMapper.selectBMyCarList(queryParam);
        if (CollectionUtils.isNotEmpty(existCars)) {
            return AjaxResult.warn("该车辆已绑定");
        }
        bMyCar.setCreateTime(LocalDateTime.now());
        return AjaxResult.success(bMyCarMapper.insertBMyCar(bMyCar));
    }

    /**
     * 修改H5我的车辆管理
     *
     * @param bMyCar H5我的车辆管理
     * @return 结果
     */
    @Override
    public int updateBMyCar(BMyCar bMyCar) {
        bMyCar.setUpdateTime(LocalDateTime.now());
        return bMyCarMapper.updateBMyCar(bMyCar);
    }

    /**
     * 设为默认
     *
     * @param id     逻辑ID
     * @param userId 绑定人
     * @param updateBy 操作人
     * @return 结果
     */
    @Override
    public int setDefault(Integer id, String userId, String updateBy) {
        BMyCar bMyCar = new BMyCar();
        bMyCar.setId(id);
        bMyCar.setDefaultFlag(BMyCarEnums.DEFAULT_FLAG.YES.getValue());
        bMyCar.setUpdateBy(updateBy);
        bMyCar.setUpdateTime(LocalDateTime.now());
        bMyCarMapper.setAllNotDefault(userId, BMyCarEnums.DEFAULT_FLAG.NO.getValue());
        return bMyCarMapper.updateBMyCar(bMyCar);
    }

    /**
     * 批量删除H5我的车辆管理
     *
     * @param ids 需要删除的H5我的车辆管理主键
     * @return 结果
     */
    @Override
    public int deleteBMyCarByIds(Integer[] ids) {
        return bMyCarMapper.deleteBMyCarByIds(ids);
    }

    /**
     * 删除H5我的车辆管理信息
     *
     * @param id H5我的车辆管理主键
     * @return 结果
     */
    @Override
    public int deleteBMyCarById(Integer id) {
        return bMyCarMapper.deleteBMyCarById(id);
    }

    @Override
    public List<String> queryHistoryCarNumberUsedByUserId(Long userId) {
        BMyCar param = new BMyCar();
        param.setCreateBy(userId.toString());
        return bMyCarMapper.selectBMyCarList(param).stream()
                .sorted(Comparator.comparing(BMyCar::getCreateTime).reversed())
                .map(BMyCar::getCarNo).toList();
    }

    /**
     * 根据车牌号查询用户信息
     *
     * @param carNo 车牌号
     * @return 用户信息
     */
    @Override
    public SysUserVO getMemberByCar(String carNo) {
        return bMyCarMapper.getMemberByCar(carNo);
    }
}
