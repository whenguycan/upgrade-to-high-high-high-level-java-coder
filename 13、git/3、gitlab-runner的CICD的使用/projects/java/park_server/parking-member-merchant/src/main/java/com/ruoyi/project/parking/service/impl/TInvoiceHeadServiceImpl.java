package com.ruoyi.project.parking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.project.parking.domain.TInvoiceHead;
import com.ruoyi.project.parking.domain.param.InvoiceHeadParam;
import com.ruoyi.project.parking.domain.vo.InvoiceHeadVO;
import com.ruoyi.project.parking.mapper.TInvoiceHeadMapper;
import com.ruoyi.project.parking.service.ITInvoiceHeadService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 琴声何来
 * @description 针对表【t_invoice_head(发票抬头记录表)】的数据库操作Service实现
 * @since 2023-04-13 15:42:38
 */
@Service
public class TInvoiceHeadServiceImpl extends ServiceImpl<TInvoiceHeadMapper, TInvoiceHead>
        implements ITInvoiceHeadService {

    @Override
    public List<TInvoiceHead> listByUseId(Long userId, String type) {
        LambdaQueryWrapper<TInvoiceHead> qw = new LambdaQueryWrapper<>();
        qw.eq(TInvoiceHead::getUserId, userId)
                .eq(StringUtils.isNotEmpty(type), TInvoiceHead::getType, type);
        return baseMapper.selectList(qw);
    }

    @Override
    public boolean add(InvoiceHeadVO param) {
        TInvoiceHead tInvoiceHead = new TInvoiceHead();
        BeanUtils.copyBeanProp(tInvoiceHead, param);
        tInvoiceHead.setCreateBy(SecurityUtils.getUsername());
        tInvoiceHead.setCreateTime(LocalDateTime.now());
        save(tInvoiceHead);
        return setdefault(tInvoiceHead.getId(), 1);
    }

    @Override
    public boolean editById(InvoiceHeadParam param) {
        TInvoiceHead tInvoiceHead = new TInvoiceHead();
        BeanUtils.copyBeanProp(tInvoiceHead, param);
        tInvoiceHead.setUpdateBy(SecurityUtils.getUsername());
        tInvoiceHead.setUpdateTime(LocalDateTime.now());
        return updateById(tInvoiceHead);
    }

    @Override
    public boolean setdefault(Integer id, Integer IntDefault) {
        TInvoiceHead head = getById(id);
        if (IntDefault == 1) {
            //设置默认，需要把其他的都设成非默认
            LambdaQueryWrapper<TInvoiceHead> qw = new LambdaQueryWrapper<>();
            qw.eq(TInvoiceHead::getUserId, head.getUserId());
            TInvoiceHead tInvoiceHead = new TInvoiceHead();
            tInvoiceHead.setFlagDefault(0);
            update(tInvoiceHead, qw);
        }
        head.setFlagDefault(IntDefault);
        head.setUpdateBy(SecurityUtils.getUsername());
        head.setUpdateTime(LocalDateTime.now());
        return updateById(head);
    }


}
