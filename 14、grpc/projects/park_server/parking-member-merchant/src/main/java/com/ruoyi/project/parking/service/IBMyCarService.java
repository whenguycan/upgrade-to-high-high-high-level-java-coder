package com.ruoyi.project.parking.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.BMyCar;
import com.ruoyi.project.system.domain.vo.SysUserVO;

import java.util.List;

/**
 * H5我的车辆管理Service接口
 * 
 * @author fangch
 * @date 2023-03-02
 */
public interface IBMyCarService 
{
    /**
     * 查询H5我的车辆管理
     * 
     * @param id H5我的车辆管理主键
     * @return H5我的车辆管理
     */
    BMyCar selectBMyCarById(Integer id);

    /**
     * 查询H5我的车辆管理列表
     * 
     * @param bMyCar H5我的车辆管理
     * @return H5我的车辆管理集合
     */
    List<BMyCar> selectBMyCarList(BMyCar bMyCar);

    /**
     * 新增H5我的车辆管理
     * 
     * @param bMyCar H5我的车辆管理
     * @return 结果
     */
    AjaxResult insertBMyCar(BMyCar bMyCar);

    /**
     * 修改H5我的车辆管理
     * 
     * @param bMyCar H5我的车辆管理
     * @return 结果
     */
    int updateBMyCar(BMyCar bMyCar);

    /**
     * 设为默认
     *
     * @param id 逻辑ID
     * @param userId 绑定人
     * @param updateBy 操作人
     * @return 结果
     */
    int setDefault(Integer id, String userId, String updateBy);

    /**
     * 批量删除H5我的车辆管理
     * 
     * @param ids 需要删除的H5我的车辆管理主键集合
     * @return 结果
     */
    int deleteBMyCarByIds(Integer[] ids);

    /**
     * 删除H5我的车辆管理信息
     * 
     * @param id H5我的车辆管理主键
     * @return 结果
     */
    int deleteBMyCarById(Integer id);

    /**
     * 查询用户使用过的车牌历史记录
     */
    List<String> queryHistoryCarNumberUsedByUserId(Long userId);

    /**
     * 根据车牌号查询用户信息
     *
     * @param carNo 车牌号
     * @return 用户信息
     */
    SysUserVO getMemberByCar(String carNo);
}
