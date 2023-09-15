package com.ruoyi.project.merchant.service;

import java.util.List;
import com.ruoyi.project.merchant.domain.TCouponOperLogs;
import com.ruoyi.project.merchant.domain.vo.TCouponOperLogsVo;

/**
 * 优惠券日志Service接口
 * 
 * @author ruoyi
 * @date 2023-03-02
 */
public interface ITCouponOperLogsService 
{
    /**
     * 查询优惠券日志
     * 
     * @param id 优惠券日志主键
     * @return 优惠券日志
     */
    public TCouponOperLogs selectTCouponOperLogsById(Long id);

    /**
     * 查询优惠券日志列表
     * 
     * @param tCouponOperLogs 优惠券日志
     * @return 优惠券日志集合
     */
    public List<TCouponOperLogsVo> selectTCouponOperLogsList(TCouponOperLogs tCouponOperLogs);

    /**
     * 新增优惠券日志
     * 
     * @param tCouponOperLogs 优惠券日志
     * @return 结果
     */
    public int insertTCouponOperLogs(TCouponOperLogs tCouponOperLogs);

    /**
     * 修改优惠券日志
     * 
     * @param tCouponOperLogs 优惠券日志
     * @return 结果
     */
    public int updateTCouponOperLogs(TCouponOperLogs tCouponOperLogs);

    /**
     * 批量删除优惠券日志
     * 
     * @param ids 需要删除的优惠券日志主键集合
     * @return 结果
     */
    public int deleteTCouponOperLogsByIds(Long[] ids);

    /**
     * 删除优惠券日志信息
     * 
     * @param id 优惠券日志主键
     * @return 结果
     */
    public int deleteTCouponOperLogsById(Long id);
}
