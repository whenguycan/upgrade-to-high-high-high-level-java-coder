package com.ruoyi.project.parking.service;

import com.ruoyi.project.parking.domain.vo.GISStatisticsVO;
import com.ruoyi.project.parking.domain.vo.ParkVO;

import java.util.List;

public interface IBigScreenService {

    /**
     * GIS数据统计
     */
    GISStatisticsVO getGISStatistics(String parkNo);

    /**
     * 车场状态数据统计
     */
    List<ParkVO> getParkInfo(String parkNo);
}
