package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.CouponDetailDayFact;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.parking.domain.vo.CouponDetailStatisticsVO;

import java.util.List;

/**
* @author 琴声何来
* @description 针对表【coupon_detail_day_fact(首页月发放优惠券统计事实表)】的数据库操作Service
* @since 2023-03-27 10:16:38
*/
public interface ICouponDetailDayFactService extends IService<CouponDetailDayFact> {

    /**
     * 查询月发放优惠券数据统计
     *
     * @param parkNo 车场编号
     * @return 月发放优惠券数据统计
     */
    List<CouponDetailStatisticsVO> getCouponDetailStatistics(String parkNo);

    /**
     * 定时任务更新月发放优惠券数据统计
     *
     * @param userId 若依定时任务更新
     */
    void analyseCouponDetailDayFact(String userId);
}
