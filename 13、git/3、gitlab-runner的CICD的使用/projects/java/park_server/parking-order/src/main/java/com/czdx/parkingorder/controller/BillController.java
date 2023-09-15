package com.czdx.parkingorder.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.czdx.parkingorder.project.service.ParkingBillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

@Slf4j
@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    ParkingBillService parkingBillService;

    /**
     * @param operater 方法名
     * @param content  发票内容
     * @param orderno  开票的订单号
     * @apiNote 请求开票接口后，开票完成的回调通知
     * @author 琴声何来
     * @since 2023/4/11 10:21
     */
    @PostMapping("/callback")
    public void callBack(@RequestParam String operater,
                         @RequestParam String content,
                         @RequestParam String orderno) {
        log.info("operater:{}", operater);
        log.info("content:{}", content);
        log.info("orderNo:{}", orderno);
        JSONObject jsonObject = JSON.parseObject(content);
        //更新发票信息状态
        parkingBillService.updateByOrderNo(orderno, jsonObject.getString("c_url"));
    }
}
