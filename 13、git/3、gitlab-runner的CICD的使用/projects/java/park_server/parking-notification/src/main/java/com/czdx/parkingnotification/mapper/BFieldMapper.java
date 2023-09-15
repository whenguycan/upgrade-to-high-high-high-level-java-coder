package com.czdx.parkingnotification.mapper;

import com.czdx.parkingnotification.domain.BField;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 琴声何来
* @description 针对表【b_field(区域管理表)】的数据库操作Mapper
* @since 2023-04-04 09:41:14
* @Entity com.czdx.parkingnotification.domain.BField
*/
@Mapper
public interface BFieldMapper extends BaseMapper<BField> {

}




