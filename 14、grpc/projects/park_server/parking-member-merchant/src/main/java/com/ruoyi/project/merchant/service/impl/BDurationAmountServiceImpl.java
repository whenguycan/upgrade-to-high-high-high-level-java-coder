package com.ruoyi.project.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.merchant.domain.BDurationAmount;
import com.ruoyi.project.merchant.domain.BDurationAmountHistory;
import com.ruoyi.project.merchant.domain.vo.BDurationAmountVo;
import com.ruoyi.project.merchant.mapper.BDurationAmountHistoryMapper;
import com.ruoyi.project.merchant.mapper.BDurationAmountMapper;
import com.ruoyi.project.merchant.service.IBDurationAmountService;
import com.ruoyi.project.system.mapper.SysDeptMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 优惠抵扣时长金额配置Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@Service
public class BDurationAmountServiceImpl implements IBDurationAmountService {
    @Autowired
    private BDurationAmountMapper bDurationAmountMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private BDurationAmountHistoryMapper bDurationAmountHistoryMapper;

    /**
     * 查询优惠抵扣时长金额配置
     *
     * @param id 优惠抵扣时长金额配置主键
     * @return 优惠抵扣时长金额配置
     */
    @Override
    public BDurationAmount selectBDurationAmountById(Long id) {
        return bDurationAmountMapper.selectBDurationAmountById(id);
    }

    /**
     * 查询优惠抵扣时长金额配置列表
     *
     * @param bDurationAmount 优惠抵扣时长金额配置
     * @return 优惠抵扣时长金额配置
     */
    @Override
    public BDurationAmountVo selectBDurationAmountList(BDurationAmount bDurationAmount) {
        BDurationAmountVo bDurationAmountVo = new BDurationAmountVo();
        bDurationAmountVo.setList(bDurationAmountMapper.selectBDurationAmountList(bDurationAmount));
        return bDurationAmountVo;
    }

    /**
     * 新增优惠抵扣时长金额配置
     *
     * @param bDurationAmountVo 优惠抵扣时长金额配置
     * @return 结果
     */
    @Override
    public int insertBDurationAmount(BDurationAmountVo bDurationAmountVo) {
        int count = 0;
        //先删除
        Map<String, Object> map = new HashMap<>();
        map.put("park_no", bDurationAmountVo.getParkNo());
        List<BDurationAmount> oldlist=bDurationAmountMapper.selectByMap(map);
        for (BDurationAmount bDurationAmount:oldlist){
            BDurationAmountHistory bDurationAmountHistory=new BDurationAmountHistory();
            BeanUtils.copyProperties(bDurationAmount,bDurationAmountHistory);
            bDurationAmountHistory.setId(null);
            bDurationAmountHistory.setEnterTime(DateUtils.getNowDate());
            bDurationAmountHistory.setSource("M");
            bDurationAmountHistoryMapper.insert(bDurationAmountHistory);
        }
        bDurationAmountMapper.deleteByMap(map);
        //后插入
        for (BDurationAmount bDurationAmount : bDurationAmountVo.getList()) {
            bDurationAmount.setCreateBy(getUsername());
            bDurationAmount.setCreateTime(DateUtils.getNowDate());
            count += bDurationAmountMapper.insertBDurationAmount(bDurationAmount);
        }
        return count;
    }

    /**
     * 修改优惠抵扣时长金额配置
     *
     * @param bDurationAmount 优惠抵扣时长金额配置
     * @return 结果
     */
    @Override
    public int updateBDurationAmount(BDurationAmount bDurationAmount) {
        bDurationAmount.setUpdateTime(DateUtils.getNowDate());
        return bDurationAmountMapper.updateBDurationAmount(bDurationAmount);
    }

    /**
     * 批量删除优惠抵扣时长金额配置
     *
     * @param ids 需要删除的优惠抵扣时长金额配置主键
     * @return 结果
     */
    @Override
    public int deleteBDurationAmountByIds(Long[] ids) {
        return bDurationAmountMapper.deleteBDurationAmountByIds(ids);
    }

    /**
     * 删除优惠抵扣时长金额配置信息
     *
     * @param id 优惠抵扣时长金额配置主键
     * @return 结果
     */
    @Override
    public int deleteBDurationAmountById(Long id) {
        return bDurationAmountMapper.deleteBDurationAmountById(id);
    }

    @Override
    public BDurationAmount selectOneBDurationAmount(BDurationAmount bDurationAmount) {
        LambdaQueryWrapper<BDurationAmount> bChargeSetQuery = new LambdaQueryWrapper<>();
        bChargeSetQuery.eq(BDurationAmount::getParkNo, bDurationAmount.getParkNo())
                .eq(BDurationAmount::getDuration, bDurationAmount.getDuration());
        return bDurationAmountMapper.selectOne(bChargeSetQuery);
    }
}
