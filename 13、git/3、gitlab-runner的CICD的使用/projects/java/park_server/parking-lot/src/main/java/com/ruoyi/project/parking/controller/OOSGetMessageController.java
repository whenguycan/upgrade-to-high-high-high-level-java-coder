package com.ruoyi.project.parking.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.tool.OOS.OOSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/showPicture")
public class OOSGetMessageController extends BaseController {

    @GetMapping("/sendmessage")
    public AjaxResult sendMessage(String picName) {
        String base64 = "";
        try {
            base64 = OOSUtil.getObject(OOSUtil.getAmazonS3(), picName);
        } catch (IOException e) {
            log.error("从OOS获取图片失败");
        }
        return success(base64);
    }
}
