package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.SelfPay;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.bo.SelfPayBO;
import com.ruoyi.project.parking.domain.vo.SelfPayVO;

import java.util.List;

/**
 * @author 琴声何来
 * @description 针对表【t_self_pay(自主缴费表)】的数据库操作Service
 * @since 2023-03-01 10:29:55
 */
public interface ISelfPayService extends IService<SelfPay> {

    /**
     * @apiNote 获取自主缴费审核列表 根据场库编号、车牌号、审核状态、创建时间
     */
    List<SelfPay> listByParkNoAndCarNumberAndStatusAndCreateTime(SelfPay selfPay);

    /**
     * @apiNote 申请自主缴费
     */
    boolean add(SelfPayVO selfPayVO);

    /**
     * @apiNote 审核通过、不通过自主缴费
     */
    boolean switchStatusById(Integer id, String status);

    /**
     * @param selfPayBOList               导入数据列表
     * @param updateSupport               是否更新数据
     * @param settingRegularCarCategoryId 固定车类型id
     * @apiNote 自主缴费列表导入
     */
    String importSelfPay(List<SelfPayBO> selfPayBOList, boolean updateSupport, String settingRegularCarCategoryId);
}
