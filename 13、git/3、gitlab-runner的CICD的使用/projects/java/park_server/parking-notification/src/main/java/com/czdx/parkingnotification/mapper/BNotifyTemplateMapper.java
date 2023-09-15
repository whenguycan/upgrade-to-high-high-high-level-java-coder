package com.czdx.parkingnotification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czdx.parkingnotification.domain.BNotifyTemplate;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 琴声何来
* @description 针对表【b_notify_template(消息推送模板表)】的数据库操作Mapper
* @since 2023-03-10 13:59:57
* @Entity com.ruoyi.project.parking.domain.BNotifyTemplate
*/
@Mapper
public interface BNotifyTemplateMapper extends BaseMapper<BNotifyTemplate> {

}




