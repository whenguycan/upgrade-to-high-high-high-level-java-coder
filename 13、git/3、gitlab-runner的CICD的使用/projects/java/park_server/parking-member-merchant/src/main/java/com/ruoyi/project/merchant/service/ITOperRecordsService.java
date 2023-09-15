package com.ruoyi.project.merchant.service;

import java.util.List;
import com.ruoyi.project.merchant.domain.TOperRecords;
import com.ruoyi.project.merchant.domain.param.CouponReportParam;
import com.ruoyi.project.merchant.domain.vo.RecordReportResultVo;
import com.ruoyi.project.merchant.domain.vo.TOperRecordsVo;

/**
 * 流水信息Service接口
 *
 * @author ruoyi
 * @date 2023-03-02
 */
public interface ITOperRecordsService
{
    /**
     * 查询流水信息
     *
     * @param id 流水信息主键
     * @return 流水信息
     */
    public TOperRecords selectTOperRecordsById(Long id);

    /**
     * 查询流水信息列表
     *
     * @param tOperRecordsVo 流水信息
     * @return 流水信息集合
     */
    public List<TOperRecords> selectTOperRecordsList(TOperRecordsVo tOperRecordsVo);

    /**
     * 新增流水信息
     *
     * @param tOperRecords 流水信息
     * @return 结果
     */
    public int insertTOperRecords(TOperRecords tOperRecords);

    /**
     * 修改流水信息
     *
     * @param tOperRecords 流水信息
     * @return 结果
     */
    public int updateTOperRecords(TOperRecords tOperRecords);

    /**
     * 批量删除流水信息
     *
     * @param ids 需要删除的流水信息主键集合
     * @return 结果
     */
    public int deleteTOperRecordsByIds(Long[] ids);

    /**
     * 删除流水信息信息
     *
     * @param id 流水信息主键
     * @return 结果
     */
    public int deleteTOperRecordsById(Long id);

    public List<RecordReportResultVo> selectRecordReportList(CouponReportParam param);
}
