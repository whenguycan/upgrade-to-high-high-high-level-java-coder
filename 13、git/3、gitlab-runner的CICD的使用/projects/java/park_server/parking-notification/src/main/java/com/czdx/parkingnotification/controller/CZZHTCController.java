package com.czdx.parkingnotification.controller;

import com.czdx.parkingnotification.domain.czzhtc.CZZHTCResponse;
import com.czdx.parkingnotification.domain.czzhtc.request.ChargeRequest;
import com.czdx.parkingnotification.domain.czzhtc.response.ChargeResponse;
import com.czdx.parkingnotification.service.ICZZHTCService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 * 常州智慧停车云平台 前端控制器
 * </p>
 *
 * @author 琴声何来
 * @since 2023/4/3 16:26
 */
@Slf4j
@RestController
public class CZZHTCController {

    @Autowired
    ICZZHTCService czzhtcService;

    /**
     * @apiNote 常州智慧停车云平台心跳接口
     * @author 琴声何来
     * @since 2023/4/3 13:54
     */
    @PostMapping("/server/heart.htm")
    public CZZHTCResponse<String> heart() {
        CZZHTCResponse<String> response = new CZZHTCResponse<>();
        response.setSuccess(true);
        response.setMessage("");
        response.setData(null);
        return response;
    }

    @PostMapping("/calfee.htm")
    public CZZHTCResponse<ChargeResponse> calFee(@RequestBody ChargeRequest request) {
        String carNumber = request.getPlate();
        LocalDateTime billTime = request.getTime();
        //根据车牌号和计费时间查询订单信息
        ChargeResponse chargeResponse = czzhtcService.getChargeInfo(carNumber, billTime);
        CZZHTCResponse<ChargeResponse> response = new CZZHTCResponse<>();
        response.setSuccess(true);
        response.setMessage("");
        response.setData(chargeResponse);
        return response;
    }
}
