package com.ruoyi.project.parking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.parking.domain.BMyCar;
import com.ruoyi.project.system.domain.vo.SysUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * H5我的车辆管理Mapper接口
 * 
 * @author fangch
 * @date 2023-03-02
 */
@Mapper
public interface BMyCarMapper extends BaseMapper<BMyCar> {

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
    int insertBMyCar(BMyCar bMyCar);

    /**
     * 修改H5我的车辆管理
     * 
     * @param bMyCar H5我的车辆管理
     * @return 结果
     */
    int updateBMyCar(BMyCar bMyCar);

    /**
     * 我的车辆全部设为非默认
     *
     * @param userId 操作人
     * @param defaultFlag 是否默认
     * @return 结果
     */
    void setAllNotDefault(@Param("userId") String userId, @Param("defaultFlag") String defaultFlag);

    /**
     * 删除H5我的车辆管理
     * 
     * @param id H5我的车辆管理主键
     * @return 结果
     */
    int deleteBMyCarById(Integer id);

    /**
     * 批量删除H5我的车辆管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBMyCarByIds(Integer[] ids);

    /**
     * 根据车牌号查询用户信息
     *
     * @param carNo 车牌号
     * @return 用户信息
     */
    SysUserVO getMemberByCar(String carNo);
}
