package com.ruoyi.project.parking.service;

import java.util.List;
import com.ruoyi.project.parking.domain.BParkChargeScheme;

/**
 * 停车场收费方案Service接口
 * 
 * @author fangch
 * @date 2023-02-21
 */
public interface IBParkChargeSchemeService 
{
    /**
     * 查询停车场收费方案
     * 
     * @param id 停车场收费方案主键
     * @return 停车场收费方案
     */
    BParkChargeScheme selectBParkChargeSchemeById(String parkNo);

    /**
     * 查询停车场收费方案列表
     * 
     * @param bParkChargeScheme 停车场收费方案
     * @return 停车场收费方案集合
     */
    List<BParkChargeScheme> selectBParkChargeSchemeList(BParkChargeScheme bParkChargeScheme);

    /**
     * 新增停车场收费方案
     * 
     * @param bParkChargeScheme 停车场收费方案
     * @return 结果
     */
    int insertBParkChargeScheme(BParkChargeScheme bParkChargeScheme);

    /**
     * 修改停车场收费方案
     * 
     * @param bParkChargeScheme 停车场收费方案
     * @return 结果
     */
    int updateBParkChargeScheme(BParkChargeScheme bParkChargeScheme);

    /**
     * 批量删除停车场收费方案
     * 
     * @param ids 需要删除的停车场收费方案主键集合
     * @return 结果
     */
    int deleteBParkChargeSchemeByIds(Integer[] ids);

    /**
     * 删除停车场收费方案信息
     * 
     * @param id 停车场收费方案主键
     * @return 结果
     */
    int deleteBParkChargeSchemeById(Integer id);
}
