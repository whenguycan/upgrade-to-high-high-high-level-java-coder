package com.example.demolog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demolog.aop.MyAnnotation;
import com.example.demolog.config.CurrentUser;
import com.example.demolog.dao.OrderDao;
import com.example.demolog.entity.OrderEntity;
import com.example.demolog.entity.UserEntity;
import com.example.demolog.event.MyEventPublisher;
import com.example.demolog.log.BaseLog;
import com.example.demolog.service.OrderService;
import com.example.demolog.utils.SnowUtils;
import com.example.demolog.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.ServiceLoader;
import java.util.UUID;


/**
 * @Auther: tangwei
 * @Date: 2023/6/30 1:45 PM
 * @Description: 类描述信息
 */
@Slf4j
@RestController
public class IndexController {

    @Autowired
    private MyEventPublisher myEventPublisher;


    @MyAnnotation
    @GetMapping("/index")
    public String index(String a, String b){

        ServiceLoader<BaseLog> load = ServiceLoader.load(BaseLog.class);
        load.forEach((item)->{
            System.out.println(item.getClass().getName());
        });


        return "";

    }

    @CurrentUser
    @PostMapping("/add-user")
    public UserVo addUser(@CurrentUser UserVo userInfo){

        return userInfo;
    }


    @Autowired
    OrderService orderService;

    @GetMapping("/add-order")
    public void addOrder(){


        OrderEntity orderEntity = new OrderEntity();


        orderEntity.setContent("yyyyy");
        orderService.save(orderEntity);
    }

    @GetMapping("/get-order")
    public List<OrderEntity> getOrder(){
        return orderService.getBaseMapper().selectList(new QueryWrapper<OrderEntity>().ge("num", 3).orderByDesc("num"));
    }

    @GetMapping("/delete-order")
    public void deleteOrder(){
        orderService.getBaseMapper().delete(new QueryWrapper<OrderEntity>().le("num", 2));
    }

    @GetMapping("/update-order")
    public void updateOrder(){
        var orderEntity = new OrderEntity();
        orderEntity.setContent("mmm");
        orderService.getBaseMapper().update(orderEntity, new QueryWrapper<OrderEntity>().lt("num", 4));
    }

}
