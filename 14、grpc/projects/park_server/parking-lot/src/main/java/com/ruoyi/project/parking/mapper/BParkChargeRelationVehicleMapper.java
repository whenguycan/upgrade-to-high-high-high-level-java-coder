package com.ruoyi.project.parking.mapper;

import java.util.List;
import com.ruoyi.project.parking.domain.BParkChargeRelationVehicle;
import com.ruoyi.project.parking.domain.vo.BParkChargeRelationVehicleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 区域-车类型-车型-收费规则关联Mapper接口
 * 
 * @author fangch
 * @date 2023-02-23
 */
@Mapper
public interface BParkChargeRelationVehicleMapper 
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
     * @param bParkChargeRelationVehicle 区域-车类型-车型-收费规则关联
     * @return 结果
     */
    int insertBParkChargeRelationVehicle(BParkChargeRelationVehicle bParkChargeRelationVehicle);

    /**
     * 删除区域-车类型-车型-收费规则关联
     * 
     * @param id 区域-车类型-车型-收费规则关联主键
     * @return 结果
     */
    int deleteBParkChargeRelationVehicleById(Integer id);

    /**
     * 批量删除区域-车类型-车型-收费规则关联
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBParkChargeRelationVehicleByIds(Integer[] ids);

    /**
     * 检查同关联设置存在否
     *
     * @param item 区域-车类型-车型-收费规则关联
     * @return
     */
    int checkRelationExist(BParkChargeRelationVehicle item);

    /**
     * 查询该条关联设定所有关联可能性列表
     * @param relatedItem
     * @return
     */
    List<String> selectRelatedStrList(BParkChargeRelationVehicleVO relatedItem);

    /**
     * 查询所有未关联列表
     * @param relatedStrs
     * @return
     */
    List<BParkChargeRelationVehicleVO> selectNotRelatedList(@Param("parkNo") String parkNo, @Param("relatedStrs") List<String> relatedStrs);
}
