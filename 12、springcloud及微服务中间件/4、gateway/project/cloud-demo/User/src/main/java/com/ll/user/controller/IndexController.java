package com.ll.user.controller;

import com.ll.user.feign.GoodsFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: tangwei
 * @Date: 2023/4/21 3:07 PM
 * @Description: 类描述信息
 */
@RestController
public class IndexController {

    @Autowired
    GoodsFeign goodsFeign;

    @GetMapping("/index")
    public void idnex(){
        System.out.println(goodsFeign.getGoodsIndex());
    }
}


