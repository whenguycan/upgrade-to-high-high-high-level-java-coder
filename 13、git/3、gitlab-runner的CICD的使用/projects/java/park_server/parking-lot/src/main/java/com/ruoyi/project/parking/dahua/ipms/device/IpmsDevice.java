package com.ruoyi.project.parking.dahua.ipms.device;

import com.dahuatech.hutool.json.JSONUtil;
import com.dahuatech.icc.brm.model.v202010.device.BrmDevicePageRequest;
import com.dahuatech.icc.brm.model.v202010.device.BrmDevicePageResponse;
import com.dahuatech.icc.brm.model.v202010.device.BrmDeviceQueryRequest;
import com.dahuatech.icc.brm.model.v202010.device.BrmDeviceQueryResponse;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.ipms.model.v202208.device.BarrieraControlRequest;
import com.dahuatech.icc.ipms.model.v202208.device.BarrieraControlResponse;
import com.dahuatech.icc.oauth.http.DefaultClient;
import com.dahuatech.icc.oauth.http.IClient;
import com.dahuatech.icc.oauth.model.v202010.GeneralResponse;
import com.ruoyi.project.parking.dahua.ipms.util.ResponseUtil;
import com.ruoyi.project.parking.dahua.model.IpmsConstants;
import com.ruoyi.project.parking.dahua.model.device.DeviceChannelOpenCloseRequest;
import com.ruoyi.project.parking.domain.TPassageLeverRecords;
import com.ruoyi.project.parking.domain.vo.BPassageDeviceVo;
import com.ruoyi.project.parking.service.IBPassageDeviceService;
import com.ruoyi.project.parking.service.ITPassageLeverRecordsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 控制闸道设备
 */
@Slf4j
@Component
public class IpmsDevice {

    @Autowired
    private IBPassageDeviceService ibPassageDeviceService;

    @Autowired
    private ITPassageLeverRecordsService itPassageLeverRecordsService;

    /**
     * 控制闸道开关
     * @param channelId 道闸通道ID-格式为"设备编号$14$0$0"(当设备为道闸一体机时,设备列表获取的设备编号+1,例如获取设备编号为1000000,此处则为1000001)
     * @param operateType 1:起，2:落，3:停止，4:常开，5:恢复正常
     * @param displayInfo 	道闸led显示信息
     * @return
     * @throws ClientException
     */
    public void OpenCloseChannel(String channelId,int operateType,String displayInfo) throws ClientException {
        IClient iClient = new DefaultClient();
        int index=channelId.indexOf("$");
        String before=channelId.substring(0,index);
        String after=channelId.substring(index);
        int i=Integer.parseInt(before)+1;
        String newChannelId=i+"$14$0$0";
//        BarrieraControlRequest barrieraControlRequest=new BarrieraControlRequest();
//        barrieraControlRequest.setChannelId(channelId);
//        barrieraControlRequest.setOperateType(operateType);
//        log.info("req channle :{}", JSONUtil.toJsonStr(barrieraControlRequest));
//        BarrieraControlResponse response = iClient.doAction(barrieraControlRequest, barrieraControlRequest.getResponseClass());
//        log.info("channel detail:{},{} success",channelId,operateType);
//        return ResponseUtil.getLongFromResponse(response);
        //-----------------------------------------第二种方式
        DeviceChannelOpenCloseRequest.Builder builder = new DeviceChannelOpenCloseRequest.Builder();
        builder.setChannelId(newChannelId);
        builder.setOperateType(operateType);
        builder.setDisplayInfo(displayInfo);
        DeviceChannelOpenCloseRequest request = builder.build(IpmsConstants.OPEN_CLOSE_CHANNEL);
        log.info("req channle :{}", JSONUtil.toJsonStr(request));
        GeneralResponse response = iClient.doAction(request, request.getResponseClass());
        log.info(response.getResult());
//        return ResponseUtil.getBooleanFromResponse(response);
        //插入线上开关闸记录表
        insertPassageLeverRecords(channelId, operateType);
    }

    private void insertPassageLeverRecords(String channelId, int operType){
        BPassageDeviceVo  bPassageDeviceVo = new BPassageDeviceVo();
        TPassageLeverRecords tPassageLeverRecords = new TPassageLeverRecords();
        bPassageDeviceVo.setDeviceId(channelId);
        List<BPassageDeviceVo> list = ibPassageDeviceService.selectBPassageDeviceList(bPassageDeviceVo);
        if(!list.isEmpty()){
            BPassageDeviceVo b = list.get(0);
            tPassageLeverRecords.setOperateType(String.valueOf(operType));
            tPassageLeverRecords.setParkNo(b.getParkNo());
            tPassageLeverRecords.setPassageNo(b.getPassageNo());
            tPassageLeverRecords.setCreateTime(new Date());
            itPassageLeverRecordsService.insertTPassageLeverRecords(tPassageLeverRecords);
        }
    }

    public BrmDeviceQueryResponse QueryDevice(String deviceCode) throws ClientException{
        DefaultClient iClient=new DefaultClient();
        BrmDeviceQueryRequest brmDeviceQueryRequest = new BrmDeviceQueryRequest(deviceCode);
        return iClient.doAction(brmDeviceQueryRequest, brmDeviceQueryRequest.getResponseClass());
    }

//    @Test
//    public void QueryDeviceTest() throws ClientException{
//        DefaultClient iClient=new DefaultClient();
//
//        BrmDevicePageRequest brmDevicePageRequest = new BrmDevicePageRequest();
//        brmDevicePageRequest.setPageNum(1);
//        brmDevicePageRequest.setPageSize(10);
//        brmDevicePageRequest.setShowChildNodeData(1);
//        BrmDevicePageResponse brmDevicePageResponse = iClient.doAction(brmDevicePageRequest,brmDevicePageRequest.getResponseClass());
//        log.info(brmDevicePageResponse.getResult());
//
//        BrmDeviceQueryRequest brmDeviceQueryRequest = new BrmDeviceQueryRequest("1000001");
//        BrmDeviceQueryResponse brmDeviceQueryResponse=iClient.doAction(brmDeviceQueryRequest, brmDeviceQueryRequest.getResponseClass());
//        log.info(brmDeviceQueryResponse.getResult());
//    }




    @Test
    public void OpenCloseChannelTest() throws ClientException {
        IClient iClient = new DefaultClient();
//        BarrieraControlRequest barrieraControlRequest=new BarrieraControlRequest();
//        barrieraControlRequest.setChannelId("channelId");
//        barrieraControlRequest.setOperateType(1);
        DeviceChannelOpenCloseRequest.Builder builder = new DeviceChannelOpenCloseRequest.Builder();
        String channelId="1000003$14$0$0";
        int index=channelId.indexOf("$");
        String before=channelId.substring(0,index);
        String after=channelId.substring(index);
        int i=Integer.parseInt(before)+1;
        String newChannelId=i+after;
        builder.setChannelId(newChannelId);
        builder.setOperateType(1);
        builder.setDisplayInfo("测试");
//        builder.setDisplayInfo(displayInfo);
        DeviceChannelOpenCloseRequest request = builder.build(IpmsConstants.OPEN_CLOSE_CHANNEL);
        log.info("req channle :{}", JSONUtil.toJsonStr(request));
        GeneralResponse response = iClient.doAction(request, request.getResponseClass());
        log.info(response.getResult());
//        return ResponseUtil.getLongFromResponse(response);
    }
}
