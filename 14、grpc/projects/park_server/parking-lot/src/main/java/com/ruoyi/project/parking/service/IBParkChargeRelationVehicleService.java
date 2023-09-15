package com.ruoyi.project.parking.service;

import java.util.List;
import com.ruoyi.project.parking.domain.BParkChargeRelationVehicle;
import com.ruoyi.project.parking.domain.vo.BParkChargeRelationVehicleVO;

/**
 * 区域-车类型-车型-收费规则关联Service接口
 * 
 * @author fangch
 * @date 2023-02-23
 */
public interface IBParkChargeRelationVehicleService 
{
    /**
     * 查询区域-车类型-车型-收费规则关联
     * 
     * @param id 区域-车类型-车型-收费规则关联主键
     * @return 区域-车类型-车型-收费规则关联
     */
    BParkChargeRelationVehicleVO selectBParkChargeRelationVehicleById(Integer id);

    /**
     * 查询区域-车类型-车型-收费规则关联列表
     * 
     * @param bParkChargeRelationVehicle 区域-车类型-车型-收费规则关联
     * @return 区域-车类型-车型-收费规则关联集合
     */
    List<BParkChargeRelationVehicleVO> selectBParkChargeRelationVehicleList(BParkChargeRelationVehicle bParkChargeRelationVehicle);

    /**
     * 新增区域-车类型-车型-收费规则关联
     * 
     * @param bParkChargeRelationVehicles 区域-车类型-车型-收费规则关联
     * @return 结果
     */
    int setRelation(List<BParkChargeRelationVehicle> bParkChargeRelationVehicles, String createBy, String parkNo);

    /**
     * 批量删除区域-车类型-车型-收费规则关联
     * 
     * @param ids 需要删除的区域-车类型-车型-收费规则关联主键集合
     * @return 结果
     */
    int deleteBParkChargeRelationVehicleByIds(Integer[] ids);

    /**
     * 删除区域-车类型-车型-收费规则关联信息
     * 
     * @param id 区域-车类型-车型-收费规则关联主键
     * @return 结果
     */
    int deleteBParkChargeRelationVehicleById(Integer id);

    /**
     * 查询未关联列表
     * @param parkNo 车场编号
     * @return
     */
    List<BParkChargeRelationVehicleVO> notRelatedList(String parkNo);
}
