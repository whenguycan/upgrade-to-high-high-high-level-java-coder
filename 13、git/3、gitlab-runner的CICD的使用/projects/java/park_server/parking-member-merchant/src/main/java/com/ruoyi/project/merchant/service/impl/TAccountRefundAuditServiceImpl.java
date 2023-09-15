package com.ruoyi.project.merchant.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.common.CouponConstants;
import com.ruoyi.project.common.OrderNoGenerator;
import com.ruoyi.project.merchant.enums.RefundAuditing;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysUserMapper;
import com.ruoyi.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.merchant.mapper.TAccountRefundAuditMapper;
import com.ruoyi.project.merchant.domain.TAccountRefundAudit;
import com.ruoyi.project.merchant.service.ITAccountRefundAuditService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 商户账户余额退款审核Service业务层处理
 *
 * @author ruoyi
 * @date 2023-03-08
 */
@Service
public class TAccountRefundAuditServiceImpl implements ITAccountRefundAuditService {
    @Resource
    private TAccountRefundAuditMapper tAccountRefundAuditMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private ISysUserService sysUserService;

    private static final String ACCOUNT_REFUND_ORDER_PRE = "ACC";

    /**
     * 查询商户账户余额退款审核
     *
     * @param id 商户账户余额退款审核主键
     * @return 商户账户余额退款审核
     */
    @Override
    public TAccountRefundAudit selectTAccountRefundAuditById(Long id) {
        return tAccountRefundAuditMapper.selectTAccountRefundAuditById(id);
    }

    /**
     * 查询商户账户余额退款审核列表
     *
     * @param tAccountRefundAudit 商户账户余额退款审核
     * @return 商户账户余额退款审核
     */
    @Override
    public List<TAccountRefundAudit> selectTAccountRefundAuditList(TAccountRefundAudit tAccountRefundAudit) {
        tAccountRefundAudit.setParkNo(SecurityUtils.getParkNo());
        return tAccountRefundAuditMapper.selectTAccountRefundAuditList(tAccountRefundAudit);
    }

    /**
     * 新增商户账户余额退款审核
     *
     * @param tAccountRefundAudit 商户账户余额退款审核
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long insertTAccountRefundAudit(TAccountRefundAudit tAccountRefundAudit) {
        tAccountRefundAudit.setCreateTime(DateUtils.getNowDate());
        tAccountRefundAudit.setCreateBy(SecurityUtils.getUsername());
        tAccountRefundAudit.setOrderNo(OrderNoGenerator.generateNo(ACCOUNT_REFUND_ORDER_PRE));
        tAccountRefundAudit.setRefundStatus(RefundAuditing.RefundStatus.APPLY.getCode());
        tAccountRefundAudit.setApplyTime(new Date());
        tAccountRefundAudit.setApplyUserId(SecurityUtils.getUserId());
        tAccountRefundAudit.setApplyUserName(SecurityUtils.getUsername());
        if (tAccountRefundAuditMapper.insert(tAccountRefundAudit) > 0) {
            SysUser sysUser = new SysUser();
            sysUser.setUserId(SecurityUtils.getUserId());
            sysUser.setFreezeFlag(CouponConstants.FREEZE_STATUS);
            //更新为冻结状态
            sysUserService.updateUser(sysUser);
            return tAccountRefundAudit.getId();
        }
        return -1L;
    }

    /**
     * 修改商户账户余额退款审核
     *
     * @param tAccountRefundAudit 商户账户余额退款审核
     * @return 结果
     */
    @Override
    public int updateTAccountRefundAudit(TAccountRefundAudit tAccountRefundAudit) {
        tAccountRefundAudit.setUpdateTime(DateUtils.getNowDate());
        return tAccountRefundAuditMapper.updateById(tAccountRefundAudit);
    }

    /**
     * 批量删除商户账户余额退款审核
     *
     * @param ids 需要删除的商户账户余额退款审核主键
     * @return 结果
     */
    @Override
    public int deleteTAccountRefundAuditByIds(Long[] ids) {
        return tAccountRefundAuditMapper.deleteTAccountRefundAuditByIds(ids);
    }

    /**
     * 删除商户账户余额退款审核信息
     *
     * @param id 商户账户余额退款审核主键
     * @return 结果
     */
    @Override
    public int deleteTAccountRefundAuditById(Long id) {
        return tAccountRefundAuditMapper.deleteTAccountRefundAuditById(id);
    }

    @Override
    public void auditRefund(TAccountRefundAudit tAccountRefundAudit) {
        UpdateWrapper<TAccountRefundAudit> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(TAccountRefundAudit::getId, tAccountRefundAudit.getId())
                .set(TAccountRefundAudit::getAuditRemark, tAccountRefundAudit.getAuditRemark())
                .set(TAccountRefundAudit::getRefundStatus, tAccountRefundAudit.getRefundStatus())
                .set(TAccountRefundAudit::getAuditTime, new Date())
                .set(TAccountRefundAudit::getAuditUserId, SecurityUtils.getUserId())
                .set(TAccountRefundAudit::getAuditUserName, SecurityUtils.getUsername());
        tAccountRefundAuditMapper.update(tAccountRefundAudit, updateWrapper);
        //审核通过，清空账户余额，解冻账户
        SysUser sysUser = new SysUser();
        if (!"0".equals(tAccountRefundAudit.getRefundStatus())) {
            sysUser.setUserId(tAccountRefundAudit.getApplyUserId());
            if ("1".equals(tAccountRefundAudit.getRefundStatus())) {
                sysUser.setAccountValue(new BigDecimal(0));
                sysUser.setGiveValue(new BigDecimal(0));
                sysUser.setAccountTotal(new BigDecimal(0));
            }
            sysUser.setFreezeFlag("0");
            sysUserMapper.updateSysuserAccount(sysUser);
        }
    }
}
