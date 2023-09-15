package com.ruoyi.project.parking.dahua.icc;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

@Data
public class IccEvent {

    private long id;//序号

    private String category;//事件大类

    private String method;//方法名

    private JSONObject info;//具体业务信息

    private String subsystem;//子系统名称

    private String domainId;//级联消息下级发给mac时需要带上
}
