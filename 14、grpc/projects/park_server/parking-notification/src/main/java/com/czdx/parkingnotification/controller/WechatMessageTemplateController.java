package com.czdx.parkingnotification.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/wechat/message")
public class WechatMessageTemplateController {

    @PostMapping("/verify")
    public String verify(@RequestBody String data) {
        log.info("收到微信公众号推送消息结果，data：{}", data);
        return "success";
    }
}
