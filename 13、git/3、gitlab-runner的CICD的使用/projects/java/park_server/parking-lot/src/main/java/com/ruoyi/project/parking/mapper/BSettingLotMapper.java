package com.ruoyi.project.parking.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ruoyi.project.parking.domain.BSettingLot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 场库基础配置表;(b_setting_lot)表数据库访问层
 * @author : http://www.chiner.pro
 * @date : 2023-3-14
 */
@Mapper
public interface BSettingLotMapper  extends BaseMapper<BSettingLot>{
    /**
     * 查询场库基础配置
     *
     * @param id 场库基础配置主键
     * @return 场库基础配置
     */
    public BSettingLot selectBSettingLotById(Long id);

    /**
     * 查询场库基础配置列表
     *
     * @param bSettingLot 场库基础配置
     * @return 场库基础配置集合
     */
    public List<BSettingLot> selectBSettingLotList(BSettingLot bSettingLot);

    /**
     * 新增场库基础配置
     *
     * @param bSettingLot 场库基础配置
     * @return 结果
     */
    public int insertBSettingLot(BSettingLot bSettingLot);

    /**
     * 修改场库基础配置
     *
     * @param bSettingLot 场库基础配置
     * @return 结果
     */
    public int updateBSettingLot(BSettingLot bSettingLot);

    /**
     * 删除场库基础配置
     *
     * @param id 场库基础配置主键
     * @return 结果
     */
    public int deleteBSettingLotById(Long id);

    /**
     * 批量删除场库基础配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBSettingLotByIds(Long[] ids);
}
