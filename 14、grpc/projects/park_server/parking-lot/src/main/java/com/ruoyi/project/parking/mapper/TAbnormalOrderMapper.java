package com.ruoyi.project.parking.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ruoyi.project.parking.domain.vo.UnusualOrderVO;
import com.ruoyi.project.parking.entity.TAbnormalOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 异常订单记录 Mapper 接口
 * </p>
 *
 * @author yinwen
 * @since 2023-03-10
 */
public interface TAbnormalOrderMapper extends BaseMapper<TAbnormalOrder> {

    @Select("SELECT t1.park_live_id,t1.park_no,t1.order_type,t1.order_no, " +
            "t1.abnormal_type AS unusual_type,t1.abnormal_reason AS unusual_reason,t1.remark, " +
            "t2.car_number,t2.car_type,t4.name AS car_type_name, " +
            "t2.entry_time,t2.exit_time, " +
            "t3.payable_amount,t3.pay_amount " +
            "FROM t_abnormal_order t1 " +
            "LEFT JOIN t_park_live_records t2 ON t2.id = t1.park_live_id " +
            "LEFT JOIN t_park_live_records_parking_order t3 ON t3.effect_flag = 1 AND  t3.order_no = t1.order_no " +
            "LEFT JOIN b_setting_car_type t4 ON t4.park_no = t2.park_no AND t4.code = t2.car_type " +
            "${ew.customSqlSegment} " +
            "ORDER BY t1.create_time ")
    List<UnusualOrderVO> queryUnusualOrderVOCondition(@Param(Constants.WRAPPER) QueryWrapper<TAbnormalOrder> qw);
}
