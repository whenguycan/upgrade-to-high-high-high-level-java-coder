package com.ruoyi.project.merchant.service;

import java.util.List;
import com.ruoyi.project.merchant.domain.TAccountRefundAudit;

/**
 * 商户账户余额退款审核Service接口
 *
 * @author ruoyi
 * @date 2023-03-08
 */
public interface ITAccountRefundAuditService
{
    /**
     * 查询商户账户余额退款审核
     *
     * @param id 商户账户余额退款审核主键
     * @return 商户账户余额退款审核
     */
    public TAccountRefundAudit selectTAccountRefundAuditById(Long id);

    /**
     * 查询商户账户余额退款审核列表
     *
     * @param tAccountRefundAudit 商户账户余额退款审核
     * @return 商户账户余额退款审核集合
     */
    public List<TAccountRefundAudit> selectTAccountRefundAuditList(TAccountRefundAudit tAccountRefundAudit);

    /**
     * 新增商户账户余额退款审核
     *
     * @param tAccountRefundAudit 商户账户余额退款审核
     * @return 结果
     */
    public Long insertTAccountRefundAudit(TAccountRefundAudit tAccountRefundAudit);

    /**
     * 修改商户账户余额退款审核
     *
     * @param tAccountRefundAudit 商户账户余额退款审核
     * @return 结果
     */
    public int updateTAccountRefundAudit(TAccountRefundAudit tAccountRefundAudit);

    /**
     * 批量删除商户账户余额退款审核
     *
     * @param ids 需要删除的商户账户余额退款审核主键集合
     * @return 结果
     */
    public int deleteTAccountRefundAuditByIds(Long[] ids);

    /**
     * 删除商户账户余额退款审核信息
     *
     * @param id 商户账户余额退款审核主键
     * @return 结果
     */
    public int deleteTAccountRefundAuditById(Long id);
}
