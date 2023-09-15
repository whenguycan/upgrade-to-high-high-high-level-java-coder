package com.ruoyi.project.merchant.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.merchant.domain.TAccountRefundAudit;

/**
 * 商户账户余额退款审核Mapper接口
 *
 * @author ruoyi
 * @date 2023-03-08
 */
public interface TAccountRefundAuditMapper extends BaseMapper<TAccountRefundAudit>
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
     * 删除商户账户余额退款审核
     *
     * @param id 商户账户余额退款审核主键
     * @return 结果
     */
    public int deleteTAccountRefundAuditById(Long id);

    /**
     * 批量删除商户账户余额退款审核
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAccountRefundAuditByIds(Long[] ids);
}
