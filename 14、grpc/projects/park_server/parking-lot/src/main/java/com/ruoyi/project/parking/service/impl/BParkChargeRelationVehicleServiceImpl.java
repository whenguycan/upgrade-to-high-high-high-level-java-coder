package com.ruoyi.project.parking.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.parking.domain.vo.BParkChargeRelationVehicleVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.parking.mapper.BParkChargeRelationVehicleMapper;
import com.ruoyi.project.parking.domain.BParkChargeRelationVehicle;
import com.ruoyi.project.parking.service.IBParkChargeRelationVehicleService;

/**
 * 区域-车类型-车型-收费规则关联Service业务层处理
 * 
 * @author fangch
 * @date 2023-02-23
 */
@Service
public class BParkChargeRelationVehicleServiceImpl implements IBParkChargeRelationVehicleService 
{
    @Autowired
    private BParkChargeRelationVehicleMapper bParkChargeRelationVehicleMapper;

    /**
     * 查询区域-车类型-车型-收费规则关联
     * 
     * @param id 区域-车类型-车型-收费规则关联主键
     * @return 区域-车类型-车型-收费规则关联
     */
    @Override
    public BParkChargeRelationVehicleVO selectBParkChargeRelationVehicleById(Integer id)
    {
        return bParkChargeRelationVehicleMapper.selectBParkChargeRelationVehicleById(id);
    }

    /**
     * 查询区域-车类型-车型-收费规则关联列表
     * 
     * @param bParkChargeRelationVehicle 区域-车类型-车型-收费规则关联
     * @return 区域-车类型-车型-收费规则关联
     */
    @Override
    public List<BParkChargeRelationVehicleVO> selectBParkChargeRelationVehicleList(BParkChargeRelationVehicle bParkChargeRelationVehicle)
    {
        return bParkChargeRelationVehicleMapper.selectBParkChargeRelationVehicleList(bParkChargeRelationVehicle);
    }

    /**
     * 新增区域-车类型-车型-收费规则关联
     * 
     * @param bParkChargeRelationVehicles 区域-车类型-车型-收费规则关联
     * @return 结果
     */
    @Override
    public int setRelation(List<BParkChargeRelationVehicle> bParkChargeRelationVehicles, String createBy, String parkNo) {
        int count = 0;
        for (BParkChargeRelationVehicle item : bParkChargeRelationVehicles) {
            if (bParkChargeRelationVehicleMapper.checkRelationExist(item) == 0) {
                item.setParkNo(parkNo);
                item.setCreateBy(createBy);
                item.setCreateTime(LocalDateTime.now());
                count += bParkChargeRelationVehicleMapper.insertBParkChargeRelationVehicle(item);
            }
        }
        return count;
    }

    /**
     * 批量删除区域-车类型-车型-收费规则关联
     * 
     * @param ids 需要删除的区域-车类型-车型-收费规则关联主键
     * @return 结果
     */
    @Override
    public int deleteBParkChargeRelationVehicleByIds(Integer[] ids)
    {
        return bParkChargeRelationVehicleMapper.deleteBParkChargeRelationVehicleByIds(ids);
    }

    /**
     * 删除区域-车类型-车型-收费规则关联信息
     * 
     * @param id 区域-车类型-车型-收费规则关联主键
     * @return 结果
     */
    @Override
    public int deleteBParkChargeRelationVehicleById(Integer id)
    {
        return bParkChargeRelationVehicleMapper.deleteBParkChargeRelationVehicleById(id);
    }

    /**
     * 查询未关联列表
     * @param parkNo 车场编号
     * @return
     */
    @Override
    public List<BParkChargeRelationVehicleVO> notRelatedList(String parkNo) {
        BParkChargeRelationVehicle param = new BParkChargeRelationVehicle();
        param.setParkNo(parkNo);
        List<BParkChargeRelationVehicleVO> relatedList = bParkChargeRelationVehicleMapper.selectBParkChargeRelationVehicleList(param);
        List<String> relatedStrs = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(relatedList)) {
            for (BParkChargeRelationVehicleVO relatedItem : relatedList) {
                List<String> relatedStrList = bParkChargeRelationVehicleMapper.selectRelatedStrList(relatedItem);
                relatedStrs.addAll(relatedStrList);
            }
        }
        return bParkChargeRelationVehicleMapper.selectNotRelatedList(parkNo, relatedStrs);
    }
}
