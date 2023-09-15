package com.ruoyi.project.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IccIPConfig {

    /**
     * ip地址
     */
    @Value("${icc.sdk.host}")
    private String hostName;

    /**
     * 端口
     */
    @Value("${icc.sdk.port}")
    private String port;

    @Value("${icc.Intranet.ip}")
    private String intranetIp;

    @Value("${icc.Intranet.port}")
    private String intranetPort;

    @Value("${icc.carPicture.url}")
    private String carPictureUrl;

    @Value("${icc.carPicture.http}")
    private String http;

    public String getHttp() {
        return http;
    }

    public String getCarPictureUrl() {
        return carPictureUrl;
    }

    public String getInternetIpAndSport() {
        return hostName+":"+intranetPort;
    }

    public String getIntranetIpAndPort() {
        return intranetIp+":"+intranetPort;
    }
}
