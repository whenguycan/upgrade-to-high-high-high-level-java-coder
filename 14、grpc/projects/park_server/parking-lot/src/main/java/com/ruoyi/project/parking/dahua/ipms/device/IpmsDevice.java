package com.ruoyi.project.parking.dahua.ipms.device;

import com.dahuatech.hutool.json.JSONUtil;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.ipms.model.v202208.device.BarrieraControlRequest;
import com.dahuatech.icc.ipms.model.v202208.device.BarrieraControlResponse;
import com.dahuatech.icc.oauth.http.DefaultClient;
import com.dahuatech.icc.oauth.http.IClient;
import com.dahuatech.icc.oauth.model.v202010.GeneralResponse;
import com.ruoyi.project.parking.dahua.ipms.util.ResponseUtil;
import com.ruoyi.project.parking.dahua.model.IpmsConstants;
import com.ruoyi.project.parking.dahua.model.device.DeviceChannelOpenCloseRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Component;

/**
 * 控制闸道设备
 */
@Slf4j
@Component
public class IpmsDevice {


    /**
     * 控制闸道开关
     * @param channelId 道闸通道ID-格式为"设备编号$14$0$0"(当设备为道闸一体机时,设备列表获取的设备编号+1,例如获取设备编号为1000000,此处则为1000001)
     * @param operateType 1:起，2:落，3:停止，4:常开，5:恢复正常
     * @param displayInfo 	道闸led显示信息
     * @return
     * @throws ClientException
     */
    public Long OpenCloseChannel(String channelId,int operateType,String displayInfo) throws ClientException {
        IClient iClient = new DefaultClient();
//        BarrieraControlRequest barrieraControlRequest=new BarrieraControlRequest();
//        barrieraControlRequest.setChannelId(channelId);
//        barrieraControlRequest.setOperateType(operateType);
//        log.info("req channle :{}", JSONUtil.toJsonStr(barrieraControlRequest));
//        BarrieraControlResponse response = iClient.doAction(barrieraControlRequest, barrieraControlRequest.getResponseClass());
//        log.info("channel detail:{},{} success",channelId,operateType);
//        return ResponseUtil.getLongFromResponse(response);
        //-----------------------------------------第二种方式
        DeviceChannelOpenCloseRequest.Builder builder = new DeviceChannelOpenCloseRequest.Builder();
        builder.setChannelId(channelId);
        builder.setOperateType(operateType);
        builder.setDisplayInfo(displayInfo);
        DeviceChannelOpenCloseRequest request = builder.build(IpmsConstants.OPEN_CLOSE_CHANNEL);
        log.info("req channle :{}", JSONUtil.toJsonStr(request));
        GeneralResponse response = iClient.doAction(request, request.getResponseClass());
        log.info(response.getResult());
        return ResponseUtil.getLongFromResponse(response);

    }

    @Test
    public void OpenCloseChannelTest() throws ClientException {
        IClient iClient = new DefaultClient();
//        BarrieraControlRequest barrieraControlRequest=new BarrieraControlRequest();
//        barrieraControlRequest.setChannelId("channelId");
//        barrieraControlRequest.setOperateType(1);
        DeviceChannelOpenCloseRequest.Builder builder = new DeviceChannelOpenCloseRequest.Builder();
        builder.setChannelId("channelId");
        builder.setOperateType(1);
//        builder.setDisplayInfo(displayInfo);
        DeviceChannelOpenCloseRequest request = builder.build(IpmsConstants.OPEN_CLOSE_CHANNEL);
        log.info("req channle :{}", JSONUtil.toJsonStr(request));
        GeneralResponse response = iClient.doAction(request, request.getResponseClass());
        log.info(response.getResult());
//        return ResponseUtil.getLongFromResponse(response);
    }
}
