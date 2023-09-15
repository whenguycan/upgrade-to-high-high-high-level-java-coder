package com.czdx.parkingnotification.mapper;

import com.czdx.parkingnotification.system.domain.SysDept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HomePageMapper {
    /**
     * 查询所有场库id
     *
     * @return 场库信息
     */
    List<SysDept> getAllParkIds();
}
