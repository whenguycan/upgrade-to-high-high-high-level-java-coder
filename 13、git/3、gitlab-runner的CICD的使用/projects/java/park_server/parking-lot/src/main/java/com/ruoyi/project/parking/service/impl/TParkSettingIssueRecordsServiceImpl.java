package com.ruoyi.project.parking.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.parking.domain.TParkSettingIssueRecords;
import com.ruoyi.project.parking.mapper.TParkSettingIssueRecordsMapper;
import com.ruoyi.project.parking.service.ITParkSettingIssueRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 车场配置下发记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-03-27
 */
@Service
public class TParkSettingIssueRecordsServiceImpl extends ServiceImpl<TParkSettingIssueRecordsMapper, TParkSettingIssueRecords> implements ITParkSettingIssueRecordsService
{
    @Autowired
    private TParkSettingIssueRecordsMapper tParkSettingIssueRecordsMapper;

    /**
     * 查询车场配置下发记录
     * 
     * @param id 车场配置下发记录主键
     * @return 车场配置下发记录
     */
    @Override
    public TParkSettingIssueRecords selectTParkSettingIssueRecordsById(Long id)
    {
        return tParkSettingIssueRecordsMapper.selectTParkSettingIssueRecordsById(id);
    }

    /**
     * 查询车场配置下发记录列表
     * 
     * @param tParkSettingIssueRecords 车场配置下发记录
     * @return 车场配置下发记录
     */
    @Override
    public List<TParkSettingIssueRecords> selectTParkSettingIssueRecordsList(TParkSettingIssueRecords tParkSettingIssueRecords)
    {
        return tParkSettingIssueRecordsMapper.selectTParkSettingIssueRecordsList(tParkSettingIssueRecords);
    }

    /**
     * 新增车场配置下发记录
     * 
     * @param tParkSettingIssueRecords 车场配置下发记录
     * @return 结果
     */
    @Override
    public int insertTParkSettingIssueRecords(TParkSettingIssueRecords tParkSettingIssueRecords)
    {
        tParkSettingIssueRecords.setCreateTime(DateUtils.getNowDate());
        tParkSettingIssueRecords.setIssueTime(DateUtils.getNowDate());
        tParkSettingIssueRecords.setIssueStatus("1");
        return tParkSettingIssueRecordsMapper.insertTParkSettingIssueRecords(tParkSettingIssueRecords);
    }

    /**
     * 修改车场配置下发记录
     * 
     * @param tParkSettingIssueRecords 车场配置下发记录
     * @return 结果
     */
    @Override
    public int updateTParkSettingIssueRecords(TParkSettingIssueRecords tParkSettingIssueRecords)
    {
        tParkSettingIssueRecords.setUpdateTime(DateUtils.getNowDate());
        return tParkSettingIssueRecordsMapper.updateTParkSettingIssueRecords(tParkSettingIssueRecords);
    }

    /**
     * 批量删除车场配置下发记录
     * 
     * @param ids 需要删除的车场配置下发记录主键
     * @return 结果
     */
    @Override
    public int deleteTParkSettingIssueRecordsByIds(Long[] ids)
    {
        return tParkSettingIssueRecordsMapper.deleteTParkSettingIssueRecordsByIds(ids);
    }

    /**
     * 删除车场配置下发记录信息
     * 
     * @param id 车场配置下发记录主键
     * @return 结果
     */
    @Override
    public int deleteTParkSettingIssueRecordsById(Long id)
    {
        return tParkSettingIssueRecordsMapper.deleteTParkSettingIssueRecordsById(id);
    }

    @Override
    public String importTParkSettingIssueRecords(List<TParkSettingIssueRecords> tParkSettingIssueRecordsList, boolean updateSupport) {
        if (StringUtils.isNull(tParkSettingIssueRecordsList) || tParkSettingIssueRecordsList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (TParkSettingIssueRecords tParkSettingIssueRecords:tParkSettingIssueRecordsList){
            if (StringUtils.isEmpty(tParkSettingIssueRecords.getParkNo())) {
                failureMsg.append("<br/>").append(failureNum).append("、场库编号为空");
                continue;
            }
            LambdaQueryWrapper<TParkSettingIssueRecords> qw = new LambdaQueryWrapper<>();
            // 验证是否存在这个配置
            TParkSettingIssueRecords records = getOne(qw.eq(TParkSettingIssueRecords::getParkNo, tParkSettingIssueRecords.getParkNo()));
            try {
                if (StringUtils.isNull(records)) {
                   //不存在则直接插入
                    save(tParkSettingIssueRecords);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、下发配置 ").append(tParkSettingIssueRecords.getParkNo()).append(" 导入成功");
                } else if (updateSupport) {
                    tParkSettingIssueRecords.setId(records.getId());
                    updateById(tParkSettingIssueRecords);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、下发配置 ").append(tParkSettingIssueRecords.getParkNo()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、下发配置 ").append(tParkSettingIssueRecords.getParkNo()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、下发配置 " + tParkSettingIssueRecords.getParkNo() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
