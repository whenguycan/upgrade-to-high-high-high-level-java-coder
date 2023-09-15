package com.czdx.parkingcharge.mapper;

import com.czdx.parkingcharge.domain.RegularCar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czdx.parkingcharge.domain.custom.RegularCarCustom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author mingchenxu
* @description 针对表【t_regular_car(固定车记录表)】的数据库操作Mapper
* @createDate 2023-03-30 14:24:54
* @Entity com.czdx.parkingcharge.domain.RegularCar
*/
@Mapper
public interface RegularCarMapper extends BaseMapper<RegularCar> {

    /**
     * 获取固定车信息
     * @param parkNos
     * @param carNumber
     * @return
     */
    List<RegularCarCustom> getRegularCarInfo(@Param("parkNos") List<String> parkNos, @Param("carNumber") String carNumber);
}




