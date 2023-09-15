package com.ruoyi.project.merchant.service.impl;

import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.merchant.domain.param.CouponReportParam;
import com.ruoyi.project.merchant.domain.vo.RecordReportResultVo;
import com.ruoyi.project.merchant.domain.vo.TOperRecordsVo;
import com.ruoyi.project.merchant.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.merchant.mapper.TOperRecordsMapper;
import com.ruoyi.project.merchant.domain.TOperRecords;
import com.ruoyi.project.merchant.service.ITOperRecordsService;

import javax.annotation.Resource;

/**
 * 流水信息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-02
 */
@Service
public class TOperRecordsServiceImpl implements ITOperRecordsService {
    @Resource
    private TOperRecordsMapper tOperRecordsMapper;

    @Resource
    private AccountService accountService;

    /**
     * 查询流水信息
     *
     * @param id 流水信息主键
     * @return 流水信息
     */
    @Override
    public TOperRecords selectTOperRecordsById(Long id) {
        return tOperRecordsMapper.selectTOperRecordsById(id);
    }

    /**
     * 查询流水信息列表
     *
     * @param tOperRecordsVo 流水信息
     * @return 流水信息
     */
    @Override
    public List<TOperRecords> selectTOperRecordsList(TOperRecordsVo tOperRecordsVo) {
        return tOperRecordsMapper.selectTOperRecordsList(tOperRecordsVo);
    }

    /**
     * 新增流水信息
     *
     * @param tOperRecords 流水信息
     * @return 结果
     */
    @Override
    public int insertTOperRecords(TOperRecords tOperRecords) {
        return tOperRecordsMapper.insert(tOperRecords);
    }

    /**
     * 修改流水信息
     *
     * @param tOperRecords 流水信息
     * @return 结果
     */
    @Override
    public int updateTOperRecords(TOperRecords tOperRecords) {
        //余额金额叠加
        accountService.recharge(tOperRecords.getOperId(), tOperRecords.getAmount());
        return tOperRecordsMapper.updateById(tOperRecords);
    }

    /**
     * 批量删除流水信息
     *
     * @param ids 需要删除的流水信息主键
     * @return 结果
     */
    @Override
    public int deleteTOperRecordsByIds(Long[] ids) {
        return tOperRecordsMapper.deleteTOperRecordsByIds(ids);
    }

    /**
     * 删除流水信息信息
     *
     * @param id 流水信息主键
     * @return 结果
     */
    @Override
    public int deleteTOperRecordsById(Long id) {
        return tOperRecordsMapper.deleteTOperRecordsById(id);
    }

    @Override
    public List<RecordReportResultVo> selectRecordReportList(CouponReportParam param) {
        return tOperRecordsMapper.selectRecordReportList(param);
    }
}
