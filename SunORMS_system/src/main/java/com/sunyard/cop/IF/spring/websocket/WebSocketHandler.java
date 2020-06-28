package com.sunyard.cop.IF.spring.websocket;

import java.util.HashMap;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.util.BaseUtil;

/**
 * 功能说明：WebSocket处理器
 * 可以继承 {@link TextWebSocketHandler}/{@link BinaryWebSocketHandler}，
 * 或者简单的实现{@link WebSocketHandler}接口
 * @author YZ
 * 2017年1月16日  上午10:20:27
 */
public class WebSocketHandler extends TextWebSocketHandler{

	private Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    /**
     * 建立连接
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String webSocketId = MapUtils.getString(session.getAttributes(), "webSocketId");
        String loginTag = MapUtils.getString(session.getAttributes(), "loginTag");
        String single_login = MapUtils.getString(session.getAttributes(), "single_login");
        if (WebSocketSessionUtils.hasConnection(webSocketId, loginTag)) {
        	WebSocketSession existSession = WebSocketSessionUtils.get(webSocketId, loginTag);
        	String existSessionId = existSession.getId();
        	String sessionId = session.getId();
        	if (!sessionId.equals(existSessionId)) {
                if ("1".equals(single_login)) { // 启用单一登录
                    // 表示当前用户同类型终端已经登录过，推送消息告知，使其被迫下线
                    HashMap<String, Object> msgMap = new HashMap<String, Object>();
                    msgMap.put("msg_type", AOSConstants.MSG_TYPE_LOGOUT);
                    WebSocketSessionUtils.sendMessage(webSocketId, loginTag, BaseUtil.transObj2Json(msgMap));
                }
        	}
        }
        WebSocketSessionUtils.add(webSocketId, loginTag, session);
        logger.info(webSocketId + "_" + loginTag + " 加入Websocket连接");
    }

    /**
     * 收到客户端消息
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String webSocketId = MapUtils.getString(session.getAttributes(), "webSocketId");
        String loginTag = MapUtils.getString(session.getAttributes(), "loginTag");
        WebSocketSessionUtils.sendMessage(webSocketId, loginTag, "服务端返回：" + message.getPayload().toString());
    }

    /**
     * 出现异常
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String webSocketId = MapUtils.getString(session.getAttributes(), "webSocketId");
        String loginTag = MapUtils.getString(session.getAttributes(), "loginTag");
        logger.error("Websocket 连接异常: " + WebSocketSessionUtils.getKey(webSocketId, loginTag));
        logger.error(exception.getMessage(), exception);

        WebSocketSessionUtils.remove(webSocketId, loginTag);
    }

    /**
     * 连接关闭
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String webSocketId = MapUtils.getString(session.getAttributes(), "webSocketId");
        String loginTag = MapUtils.getString(session.getAttributes(), "loginTag");
        if (WebSocketSessionUtils.hasConnection(webSocketId, loginTag)) {
        	WebSocketSession existSession = WebSocketSessionUtils.get(webSocketId, loginTag);
        	String existSessionId = existSession.getId();
        	String sessionId = session.getId();
        	if (sessionId.equals(existSessionId)) {
        		WebSocketSessionUtils.remove(webSocketId, loginTag);
        		logger.info(webSocketId + "_" + loginTag + " 断开Websocket连接");
        	}
        }
    }

    /**
     * 是否分段发送消息
     * @return
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
