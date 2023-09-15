package com.ruoyi.common.utils.sms;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Slf4j
public class SendMessage {

    public static void send(SendMessageParam sendMessageParam) {
        doPostJson("https://swj.cz189.cn/sms-service/sendMessage/send", JSON.toJSONString(sendMessageParam));
    }

    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
            log.info("发送短信返回-----------------------"+resultString);
        } catch (Exception e) {
            log.error("发送短信异常: {}", e.getMessage());
            throw new ServiceException("发送短信异常",6666);
        } finally {
            try {
                if(response!=null){
                    response.close();
                }

            } catch (IOException e) {
                throw new ServiceException("关闭CloseableHttpResponse异常",6666);
            }
        }

        return resultString;
    }
}
