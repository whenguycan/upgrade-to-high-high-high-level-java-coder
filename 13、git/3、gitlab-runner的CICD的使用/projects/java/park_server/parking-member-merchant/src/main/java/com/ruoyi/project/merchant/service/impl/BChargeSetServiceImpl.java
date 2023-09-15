package com.ruoyi.project.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.merchant.domain.BChargeSet;
import com.ruoyi.project.merchant.domain.vo.BChargeSetVo;
import com.ruoyi.project.merchant.domain.vo.RechargeParamVo;
import com.ruoyi.project.merchant.mapper.BChargeSetMapper;
import com.ruoyi.project.merchant.service.IBChargeSetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 充值优惠配置Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@Service
public class BChargeSetServiceImpl implements IBChargeSetService {
    @Resource
    private BChargeSetMapper bChargeSetMapper;

    @Override
    public BChargeSet selectBChargeSetByRechargeAmount(RechargeParamVo rechargeVoParam) {
        return bChargeSetMapper.selectChargeSetByRechargeAmount(rechargeVoParam);
    }

    /**
     * 查询充值优惠配置
     *
     * @param id 充值优惠配置主键
     * @return 充值优惠配置
     */
    @Override
    public BChargeSet selectBChargeSetById(Long id) {
        return bChargeSetMapper.selectBChargeSetById(id);
    }

    /**
     * 查询充值优惠配置列表
     *
     * @param bChargeSet 充值优惠配置
     * @return 充值优惠配置
     */
    @Override
    public BChargeSetVo selectBChargeSetList(BChargeSet bChargeSet) {
        BChargeSetVo bChargeSetVo = new BChargeSetVo();
        bChargeSetVo.setList(bChargeSetMapper.selectBChargeSetList(bChargeSet));
        return bChargeSetVo;
    }

    /**
     * 新增充值优惠配置
     *
     * @param bChargeSetVo 充值优惠配置
     * @return 结果
     */
    @Override
    public int insertBChargeSet(BChargeSetVo bChargeSetVo) {
        int count = 0;
        //先删除
        Map<String, Object> map = new HashMap<>();
        map.put("park_no", bChargeSetVo.getParkNo());
        bChargeSetMapper.deleteByMap(map);
        for (BChargeSet bChargeSet : bChargeSetVo.getList()) {
            bChargeSet.setCreateBy(getUsername());
            bChargeSet.setCreateTime(DateUtils.getNowDate());
            count += bChargeSetMapper.insertBChargeSet(bChargeSet);
        }
        return count;
    }

    /**
     * 修改充值优惠配置
     *
     * @param bChargeSet 充值优惠配置
     * @return 结果
     */
    @Override
    public int updateBChargeSet(BChargeSet bChargeSet) {
        bChargeSet.setUpdateTime(DateUtils.getNowDate());
        return bChargeSetMapper.updateBChargeSet(bChargeSet);
    }

    /**
     * 批量删除充值优惠配置
     *
     * @param ids 需要删除的充值优惠配置主键
     * @return 结果
     */
    @Override
    public int deleteBChargeSetByIds(Long[] ids) {
        return bChargeSetMapper.deleteBChargeSetByIds(ids);
    }

    /**
     * 删除充值优惠配置信息
     *
     * @param id 充值优惠配置主键
     * @return 结果
     */
    @Override
    public int deleteBChargeSetById(Long id) {
        return bChargeSetMapper.deleteBChargeSetById(id);
    }

    @Override
    public BChargeSet selectOneBChargeSet(BChargeSet bChargeSet) {

        LambdaQueryWrapper<BChargeSet> bChargeSetQuery = new LambdaQueryWrapper<>();
        bChargeSetQuery.eq(BChargeSet::getParkNo, bChargeSet.getParkNo());
        bChargeSetQuery.eq(BChargeSet::getChargeAmount, bChargeSet.getChargeAmount());
        return bChargeSetMapper.selectOne(bChargeSetQuery);
    }
}
