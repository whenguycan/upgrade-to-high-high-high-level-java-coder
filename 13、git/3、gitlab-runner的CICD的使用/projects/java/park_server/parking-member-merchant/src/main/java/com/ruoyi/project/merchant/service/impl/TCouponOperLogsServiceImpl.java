package com.ruoyi.project.merchant.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.project.merchant.domain.vo.TCouponOperLogsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.merchant.mapper.TCouponOperLogsMapper;
import com.ruoyi.project.merchant.domain.TCouponOperLogs;
import com.ruoyi.project.merchant.service.ITCouponOperLogsService;

/**
 * 优惠券日志Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-03-02
 */
@Service
public class TCouponOperLogsServiceImpl implements ITCouponOperLogsService 
{
    @Autowired
    private TCouponOperLogsMapper tCouponOperLogsMapper;

    /**
     * 查询优惠券日志
     * 
     * @param id 优惠券日志主键
     * @return 优惠券日志
     */
    @Override
    public TCouponOperLogs selectTCouponOperLogsById(Long id)
    {
        return tCouponOperLogsMapper.selectTCouponOperLogsById(id);
    }

    /**
     * 查询优惠券日志列表
     * 
     * @param tCouponOperLogs 优惠券日志
     * @return 优惠券日志
     */
    @Override
    public List<TCouponOperLogsVo> selectTCouponOperLogsList(TCouponOperLogs tCouponOperLogs)
    {
        return tCouponOperLogsMapper.selectTCouponOperLogsList(tCouponOperLogs);
    }

    /**
     * 新增优惠券日志
     * 
     * @param tCouponOperLogs 优惠券日志
     * @return 结果
     */
    @Override
    public int insertTCouponOperLogs(TCouponOperLogs tCouponOperLogs)
    {
        tCouponOperLogs.setCreateTime(DateUtils.getNowDate());
        return tCouponOperLogsMapper.insertTCouponOperLogs(tCouponOperLogs);
    }

    /**
     * 修改优惠券日志
     * 
     * @param tCouponOperLogs 优惠券日志
     * @return 结果
     */
    @Override
    public int updateTCouponOperLogs(TCouponOperLogs tCouponOperLogs)
    {
        tCouponOperLogs.setUpdateTime(DateUtils.getNowDate());
        return tCouponOperLogsMapper.updateTCouponOperLogs(tCouponOperLogs);
    }

    /**
     * 批量删除优惠券日志
     * 
     * @param ids 需要删除的优惠券日志主键
     * @return 结果
     */
    @Override
    public int deleteTCouponOperLogsByIds(Long[] ids)
    {
        return tCouponOperLogsMapper.deleteTCouponOperLogsByIds(ids);
    }

    /**
     * 删除优惠券日志信息
     * 
     * @param id 优惠券日志主键
     * @return 结果
     */
    @Override
    public int deleteTCouponOperLogsById(Long id)
    {
        return tCouponOperLogsMapper.deleteTCouponOperLogsById(id);
    }
}
