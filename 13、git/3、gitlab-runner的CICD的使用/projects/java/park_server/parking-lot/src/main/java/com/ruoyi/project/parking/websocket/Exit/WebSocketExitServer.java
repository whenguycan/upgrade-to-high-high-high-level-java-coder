package com.ruoyi.project.parking.websocket.Exit;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.project.parking.domain.dahuavo.ValidChannelVo;
import com.ruoyi.project.parking.websocket.Entry.WebSocketServerEntryPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;


@Slf4j
@Component
@ServerEndpoint(value = "/websocket/Exit/{passageId}",encoders = { ServerEncoderExit.class })
public class WebSocketExitServer {
    /**
     * 建立连接成功调用 (Session + 场景)
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("passageId") String passageId) throws IOException {
        log.info("[onOpen][session({}) 接入, [passageId: {}]", session, passageId);
        WebSocketServerExitPool.addDataConnect(session, passageId);
        //建立连接成功的时候，就要往前端web推送数据
        List<ValidChannelVo> validChannelVoList = SpringUtils.getBean(RedisCache.class).redisTemplate.opsForList().range(passageId, -1, -1);
        for (ValidChannelVo validChannelVo : validChannelVoList) {
            try {
                WebSocketServerExitPool.sendMessage(session, validChannelVo);
            } catch (EncodeException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 关闭连接时调用
     * @param session 连接
     */
    @OnClose
    public void onClose(Session session) {
        log.info("[onClose][session({}) 连接关闭。]", session);
        WebSocketServerExitPool.removeConnect(session);
    }

    /**
     * 错误时调用
     * @param session 连接
     * @param throwable 异常
     */
    @OnError
    public void onError(Session session, Throwable throwable) throws IOException {
        log.info("[onClose][session({}) 发生异常]", session, throwable);
        if (session.isOpen())
        {
            // 关闭连接
            session.close();
        }
        WebSocketServerExitPool.removeConnect(session);
    }

    /**
     * 收到客户端信息后，根据接收到的信息进行处理
     * @param session 连接
     * @param message 数据消息
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        log.info("[onOpen][session({}) 接收到一条消息({})]", session, message);
        session.getBasicRemote().sendText("HeartBeat");
    }
}
