package com.ruoyi.project.merchant.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.merchant.domain.TOperRecords;
import com.ruoyi.project.merchant.domain.param.CouponReportParam;
import com.ruoyi.project.merchant.domain.vo.RecordReportResultVo;
import com.ruoyi.project.merchant.domain.vo.TOperRecordsVo;

/**
 * 流水信息Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-02
 */
public interface TOperRecordsMapper extends BaseMapper<TOperRecords>
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

//    /**
//     * 新增流水信息
//     *
//     * @param tOperRecords 流水信息
//     * @return 结果
//     */
//    public int insertTOperRecords(TOperRecords tOperRecords);
//
//    /**
//     * 修改流水信息
//     *
//     * @param tOperRecords 流水信息
//     * @return 结果
//     */
//    public int updateTOperRecords(TOperRecords tOperRecords);

    /**
     * 删除流水信息
     *
     * @param id 流水信息主键
     * @return 结果
     */
    public int deleteTOperRecordsById(Long id);

    /**
     * 批量删除流水信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTOperRecordsByIds(Long[] ids);

    public List<RecordReportResultVo> selectRecordReportList(CouponReportParam param);
}
