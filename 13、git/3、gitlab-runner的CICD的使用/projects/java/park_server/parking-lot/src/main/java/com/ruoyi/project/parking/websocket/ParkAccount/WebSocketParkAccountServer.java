package com.ruoyi.project.parking.websocket.ParkAccount;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;


@Slf4j
@Component
@ServerEndpoint(value = "/websocket/ParkAccount/{parkNo}")
public class WebSocketParkAccountServer {
    /**
     * 建立连接成功调用 (Session + 场景)
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("parkNo") String parkNo) throws IOException {
        log.info("[onOpen][session({}) 接入, [parkNo: {}]", session, parkNo);
        WebSocketParkAccountPool.addDataConnect(session, parkNo);
    }

    /**
     * 关闭连接时调用
     * @param session 连接
     */
    @OnClose
    public void onClose(Session session) {
        log.info("[onClose][session({}) 连接关闭。]", session);
        WebSocketParkAccountPool.removeConnect(session);
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
        WebSocketParkAccountPool.removeConnect(session);
    }

    /**
     * 收到客户端信息后，根据接收到的信息进行处理
     * @param session 连接
     * @param message 数据消息
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("[onOpen][session({}) 接收到一条消息({})]", session, message);
        // TODO: 对于客户端发送的指令信息，解析后进行对应的逻辑处理
    }
}
