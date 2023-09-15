package com.ruoyi.project.parking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.parking.domain.param.ParkLiveRecordsParam;
import com.ruoyi.project.parking.domain.vo.ParkLiveRecordsVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.HistoryParkingOrderVO;
import com.ruoyi.project.parking.domain.vo.parkingorder.VehicleParkOrderVO;
import com.ruoyi.project.parking.entity.ParkLiveRecords;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 在场记录表 Mapper 接口
 * </p>
 *
 * @author yinwen
 * @since 2023-02-22
 */
public interface ParkLiveRecordsMapper extends BaseMapper<ParkLiveRecords> {

    List<ParkLiveRecordsVO> selectParkLiveRecordsVOList(ParkLiveRecordsParam parkLiveRecordsParam);

    List<VehicleParkOrderVO> selectOrderNoByParkLiveId(Integer id);

    @Select("SELECT t1.* FROM t_park_live_records t1 " +
            "LEFT JOIN t_park_live_records_parking_order t2 ON t2.park_live_id = t1.id " +
            "WHERE t2.order_no = #{orderNo} ")
    ParkLiveRecords queryParkLiveRecordsByOrderNo(String orderNo);

    List<HistoryParkingOrderVO> selectHistoryParkingOrderVO(ParkLiveRecordsParam parkLiveRecordsParam);

    int insertOrderNoWithLiveId(List<VehicleParkOrderVO> list);
}
