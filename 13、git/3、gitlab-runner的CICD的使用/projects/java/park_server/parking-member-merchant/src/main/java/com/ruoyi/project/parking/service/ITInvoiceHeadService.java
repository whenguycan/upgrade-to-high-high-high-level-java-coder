package com.ruoyi.project.parking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.TInvoiceHead;
import com.ruoyi.project.parking.domain.param.InvoiceHeadParam;
import com.ruoyi.project.parking.domain.vo.InvoiceHeadVO;

import java.util.List;

/**
 * <p>
 * 发票抬头记录表 服务类
 * </p>
 *
 * @author yinwen
 * @since 2023-04-06
 */
public interface ITInvoiceHeadService extends IService<TInvoiceHead> {

    List<TInvoiceHead> listByUseId(Long userId,String type);

    /**
     * 新增
     *
     * @param param 新增参数
     */
    boolean add(InvoiceHeadVO param);

    /**
     * 修改
     *
     * @param param 修改参数
     */
    boolean editById(InvoiceHeadParam param);

    /**
     * 设为开票头为默认
     * @param id 开票头
     * @param IntDefault 默认标记
     */
    boolean setdefault(Integer id,Integer IntDefault);
}
