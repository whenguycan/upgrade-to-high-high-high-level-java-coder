package com.ruoyi.project.parking.dahua.model.device;


import com.dahuatech.hutool.http.Method;
import com.dahuatech.hutool.json.JSONUtil;
import com.dahuatech.icc.exception.ClientException;
import com.ruoyi.project.parking.dahua.model.DefaultRequest;
import lombok.Setter;

public class DeviceChannelOpenCloseRequest extends DefaultRequest {
    public DeviceChannelOpenCloseRequest(Builder builder,String url) throws ClientException {
        super(url, Method.POST);
        super.body(JSONUtil.toJsonStr(builder));
    }
    @Setter
    public static class Builder {
        private String channelId;//道闸通道ID-格式为"设备编号$14$0$0"(当设备为道闸一体机时,设备列表获取的设备编号+1,例如获取设备编号为1000000,此处则为1000001)
        private Integer operateType;//1:起，2:落，3:停止，4:常开，5:恢复正常
        private String displayInfo;//道闸led显示信息

        public DeviceChannelOpenCloseRequest build(String url) throws ClientException {
            return new DeviceChannelOpenCloseRequest(this,url);
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public Integer getOperateType() {
            return operateType;
        }

        public void setOperateType(Integer operateType) {
            this.operateType = operateType;
        }

        public String getDisplayInfo() {
            return displayInfo;
        }

        public void setDisplayInfo(String displayInfo) {
            this.displayInfo = displayInfo;
        }
    }

}
