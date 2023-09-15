package com.ruoyi.project.parking.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.parking.domain.BParkChargeRelationVehicle;
import com.ruoyi.project.parking.domain.vo.BParkChargeRelationVehicleVO;
import com.ruoyi.project.parking.mapper.BParkChargeRelationVehicleMapper;
import com.ruoyi.project.parking.service.IBParkChargeRelationVehicleService;
import com.ruoyi.project.parking.service.grpcclient.ParkingChargeGrpcClientServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private ParkingChargeGrpcClientServiceImpl pCGrpcClientService;

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
    public AjaxResult setRelation(List<BParkChargeRelationVehicle> bParkChargeRelationVehicles, String createBy, String parkNo) {
        int count = 0;
        for (BParkChargeRelationVehicle item : bParkChargeRelationVehicles) {
            item.setParkNo(parkNo);
            if (bParkChargeRelationVehicleMapper.checkRelationExistOther(item) > 0) {
                return AjaxResult.error("关联关系已绑定其他收费规则，请检查");
            }
            if (bParkChargeRelationVehicleMapper.checkRelationExist(item) == 0) {
                item.setCreateBy(createBy);
                item.setCreateTime(LocalDateTime.now());
                count += bParkChargeRelationVehicleMapper.insertBParkChargeRelationVehicle(item);
            }
        }
        // 刷新收费规则关联
        if (count > 0) {
            pCGrpcClientService.refreshParkLotChargeRelation(parkNo);
        }
        return AjaxResult.success(count);
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
        int num = bParkChargeRelationVehicleMapper.deleteBParkChargeRelationVehicleByIds(ids);
        if (num > 0) {
            // 刷新收费规则关联
            pCGrpcClientService.refreshParkLotChargeRelation(SecurityUtils.getParkNo());
        }
        return num;
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
        int num = bParkChargeRelationVehicleMapper.deleteBParkChargeRelationVehicleById(id);
        if (num > 0) {
            // 刷新收费规则关联
            pCGrpcClientService.refreshParkLotChargeRelation(SecurityUtils.getParkNo());
        }
        return num;
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
