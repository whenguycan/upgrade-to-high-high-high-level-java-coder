package com.ruoyi.project.parking.websocket.Entry;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.parking.domain.TEntryRecords;
import com.ruoyi.project.parking.domain.dahuavo.CarCaptureData;
import com.ruoyi.project.parking.domain.dahuavo.ValidChannelVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author ps
 * Websocket连接池、对连接池内连接操作 和数据推送方法
 */
@Slf4j
public class WebSocketServerEntryPool {


    /**
     * WebSocket连接池
     */
    private static ConcurrentMap<Session, String> dataConnect = new ConcurrentHashMap<>();

    /**
     * 将websocket连接，放入连接池
     * @param session websocket连接
     * @param passageId 通道编号
     */
    public static void addDataConnect(Session session, String passageId){
        dataConnect.put(session, passageId);
        Iterator<Map.Entry<Session, String>> iterator = dataConnect.entrySet().iterator();
        synchronized (iterator){
            //移除失效连接
            while(iterator.hasNext()){
                Map.Entry<Session, String> entry = iterator.next();
                Session key=entry.getKey();
                Map<String, Object> userProperties = key.getUserProperties();
                if(null != userProperties && null != userProperties.get("ReadyState") && "0" != String.valueOf(userProperties.get("ReadyState"))){
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 将websocket连接从连接池中移除
     * @param session websocket连接
     */
    public static void removeConnect(Session session){
         Iterator<Map.Entry<Session, String>> iterator = dataConnect.entrySet().iterator();
        synchronized (iterator){
            //主动移除连接
            while (iterator.hasNext()){
                Map.Entry<Session,String> map=iterator.next();
                if(session==map.getKey()){
                    //关闭链接
                    iterator.remove();
                    //链接关闭时获取该链接对应的所有通道，并删除
//                    Collection<String> keys = SpringUtils.getBean(RedisCache.class).keys
//                            (CacheConstants.SENTRYBOX_PASSAGE_KEY +"*"+passageId);
//                    SpringUtils.getBean(RedisCache.class).deleteObject(keys);
                }
            }
        }
    }

    /**
     * 获取连接池中所有连接
     * @return 连接池所有数据
     */
    public static ConcurrentMap<Session,String > getDataConnect(){
        return dataConnect;
    }

    /**
     * Websocket消息推送
     * @param session 连接
     * @param message 消息主体
     * @throws IOException I/O异常
     */
    public static void sendMessage(Session session, ValidChannelVo message) throws IOException, EncodeException {
        session.getBasicRemote().sendObject(message);
    }

    /**
     * Websocket消息推送
     * @param passageId 岗亭名称
     * @param message 消息主体
     * @throws IOException I/O异常
     */
    public static void sendMessageByPassageId(String passageId, ValidChannelVo message) throws IOException, EncodeException {
        Iterator<Map.Entry<Session, String>> iterator = dataConnect.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Session,String> map=iterator.next();
            String value= map.getValue();
            if(passageId.equals(value)){
                sendMessage(map.getKey(),message);
            }
        }
    }



}
