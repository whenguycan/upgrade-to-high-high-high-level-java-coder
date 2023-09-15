package com.ruoyi.project.parking.domain.vo;

import lombok.Data;

@Data
public class ParkingLotNumStatisticsVO {

    /** 停车场总数 */
    private Integer parkingLotNum;

    /** 停车位总数 */
    private Integer parkingSpaceNum;
}
