package com.ruoyi.project.parking.service;

import com.czdx.grpc.lib.merchant.VisitorApply;
import com.ruoyi.project.parking.domain.PayTypeDayFact;
import com.ruoyi.project.parking.domain.bo.CarVolumeSheetBO;
import com.ruoyi.project.parking.domain.bo.VisitorApplySheetBO;
import com.ruoyi.project.parking.domain.vo.CarVolumeSheetVO;
import com.ruoyi.project.parking.domain.vo.StatisticsSheetVO;

import java.util.List;

public interface ISheetService {

    /**
     * @apiNote 支付类型报表
     */
    List<PayTypeDayFact> payTypeStatisticsSheet(StatisticsSheetVO statisticsSheetVO);

    /**
     * @apiNote 车流量报表
     */
    List<CarVolumeSheetBO> carVolumeSheet(CarVolumeSheetVO carVolumeSheetVO);

    List<VisitorApplySheetBO> visitorApplyStatisticsSheet(StatisticsSheetVO statisticsSheetVO);
}
