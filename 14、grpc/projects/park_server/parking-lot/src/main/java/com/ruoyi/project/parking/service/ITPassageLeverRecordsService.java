package com.ruoyi.project.parking.service;

import java.util.List;
import com.ruoyi.project.parking.domain.TPassageLeverRecords;
import com.ruoyi.project.parking.domain.vo.TPassageLeverRecordsVo;

/**
 * 起落杆记录Service接口
 *
 * @author ruoyi
 * @date 2023-02-23
 */
public interface ITPassageLeverRecordsService
{
    /**
     * 查询起落杆记录
     *
     * @param id 起落杆记录主键
     * @return 起落杆记录
     */
    public TPassageLeverRecords selectTPassageLeverRecordsById(Long id);

    /**
     * 查询起落杆记录列表
     *
     * @param tPassageLeverRecords 起落杆记录
     * @return 起落杆记录集合
     */
    public List<TPassageLeverRecordsVo> selectTPassageLeverRecordsList(TPassageLeverRecords tPassageLeverRecords);

    /**
     * 新增起落杆记录
     *
     * @param tPassageLeverRecords 起落杆记录
     * @return 结果
     */
    public int insertTPassageLeverRecords(TPassageLeverRecords tPassageLeverRecords);

    /**
     * 修改起落杆记录
     *
     * @param tPassageLeverRecords 起落杆记录
     * @return 结果
     */
    public int updateTPassageLeverRecords(TPassageLeverRecords tPassageLeverRecords);

    /**
     * 批量删除起落杆记录
     *
     * @param ids 需要删除的起落杆记录主键集合
     * @return 结果
     */
    public int deleteTPassageLeverRecordsByIds(Long[] ids);

    /**
     * 删除起落杆记录信息
     *
     * @param id 起落杆记录主键
     * @return 结果
     */
    public int deleteTPassageLeverRecordsById(Long id);
}
