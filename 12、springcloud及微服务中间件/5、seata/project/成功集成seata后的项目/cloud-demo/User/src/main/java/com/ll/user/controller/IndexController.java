package com.ll.user.controller;

import com.ll.user.entity.UserEntity;
import com.ll.user.feign.GoodsFeign;
import com.ll.user.service.UserService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    UserService userService;

    @GlobalTransactional
    @GetMapping("/index")
    public void idnex(){

        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("tangwei");

        userService.save(userEntity);

        goodsFeign.getGoodsIndex();

        int a = 1/0;

    }
}


