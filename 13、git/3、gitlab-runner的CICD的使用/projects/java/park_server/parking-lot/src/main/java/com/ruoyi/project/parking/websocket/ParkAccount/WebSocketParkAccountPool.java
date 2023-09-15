package com.ruoyi.project.parking.websocket.ParkAccount;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.parking.domain.TEntryRecords;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author ps
 * Websocket连接池、对连接池内连接操作 和数据推送方法
 */
@Slf4j
public class WebSocketParkAccountPool {


    /**
     * WebSocket连接池
     */
    private static ConcurrentMap<String, Session> dataConnect = new ConcurrentHashMap<>();

    /**
     * 将websocket连接，放入连接池
     * @param session websocket连接
     * @param parkNo 车场
     */
    public static void addDataConnect(Session session, String parkNo){
        dataConnect.put(parkNo, session);
        Iterator<Map.Entry<String, Session>> iterator = dataConnect.entrySet().iterator();
        synchronized (iterator){
            //移除失效连接
            while(iterator.hasNext()){
                Map.Entry<String, Session> entry = iterator.next();
                Session sessionNew = entry.getValue();
                Map<String, Object> userProperties = sessionNew.getUserProperties();
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
        Iterator<Map.Entry<String, Session>> iterator = dataConnect.entrySet().iterator();
        synchronized (iterator){
            //主动移除连接
            while (iterator.hasNext()){
                if(session==iterator.next().getValue()){
                    //关闭链接
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 获取连接池中所有连接
     * @return 连接池所有数据
     */
    public static ConcurrentMap<String, Session> getDataConnect(){
        return dataConnect;
    }

    /**
     * Websocket消息推送
     * @param session 连接
     * @param account 消息主体
     * @throws IOException I/O异常
     */
    public static void sendMessage(Session session, String account) throws IOException, EncodeException {
        session.getBasicRemote().sendObject(account);
    }

    /**
     * Websocket消息推送
     * @param parkNo 名称
     * @param account 消息主体
     * @throws IOException I/O异常
     */
    public static void sendMessageByparkNo(String parkNo, String account) throws IOException, EncodeException {
        if (dataConnect.get(parkNo)!=null){
            sendMessage(dataConnect.get(parkNo),account);
        }else {
            log.info(parkNo+"已离线");
        }

    }



}
