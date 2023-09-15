package com.czdx.parkingnotification.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czdx.parkingnotification.domain.BNotifyTemplate;
import com.czdx.parkingnotification.mapper.BNotifyTemplateMapper;
import com.czdx.parkingnotification.service.IBNotifyTemplateService;
import org.springframework.stereotype.Service;


/**
 * @author 琴声何来
 * @description 针对表【b_notify_template(消息推送模板表)】的数据库操作Service实现
 * @since 2023-03-10 13:59:57
 */
@Service
public class BNotifyTemplateServiceImpl extends ServiceImpl<BNotifyTemplateMapper, BNotifyTemplate>
        implements IBNotifyTemplateService {

}




