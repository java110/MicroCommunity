package com.java110.api.websocket;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.WsDataDto;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName 停车场 ws
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/25 12:13
 * @Version 1.0
 * add by wuxw 2020/5/25
 **/
@ServerEndpoint("/ws/parkingArea/{paId}/{clientId}")
@Component
public class ParkingAreaWebsocket {

    private static Logger logger = LoggerFactory.getLogger(ParkingAreaWebsocket.class);

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, ParkingAreaWebsocket> webSocketMap = new ConcurrentHashMap<>();

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, String> clientMachineMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收clientId
     */
    private String clientId = "";

    private String paId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("clientId") String clientId, @PathParam("paId") String paId) {
        this.session = session;
        this.clientId = clientId;
        this.paId = paId;
        if (webSocketMap.containsKey(clientId)) {
            webSocketMap.remove(clientId);
            webSocketMap.put(clientId, this);
            //加入set中
        } else {
            webSocketMap.put(clientId, this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }


        logger.debug("用户连接:" + clientId + ",当前在线人数为:" + getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            logger.error("用户:" + clientId + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(clientId)) {
            webSocketMap.remove(clientId);
            //从set中删除
            subOnlineCount();
        }
        logger.info("用户退出:" + clientId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        logger.info("用户消息:" + paId + ",客户端：" + clientId + ",报文:" + message);
        //可以群发消息
        //消息保存到数据库、redis
        if (StringUtil.isEmpty(message)) {
            ResultVo resultVo = new ResultVo(ResultVo.CODE_ERROR, "未包含内容");
            webSocketMap.get(clientId).sendMessage(resultVo.toString());
            return;
        }

        if (!StringUtil.isJsonObject(message)) {
            ResultVo resultVo = new ResultVo(ResultVo.CODE_ERROR, "不是有效数据格式");
            webSocketMap.get(clientId).sendMessage(resultVo.toString());
            return;
        }

        WsDataDto wsDataDto = JSONObject.parseObject(message, WsDataDto.class);

        switch (wsDataDto.getCmd()) {
            case WsDataDto.CMD_PING:
                //webSocketMap.get(userId).sendMessage(wsDataDto.toString());
                break;
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("用户错误:" + this.clientId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送设备监控信息
     */
    public static void sendInfo(String message, String paId) throws IOException {
        logger.info("发送消息到:" + paId + "，报文:" + message);
        for (ParkingAreaWebsocket server : webSocketMap.values()) {
            if (paId.equals(server.paId)) {
                webSocketMap.get(server.clientId).sendMessage(message);
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        ParkingAreaWebsocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        ParkingAreaWebsocket.onlineCount--;
    }
}
