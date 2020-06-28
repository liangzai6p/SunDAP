package com.sunyard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.sunyard.cop.IF.spring.websocket.WebSocketHandler;
import com.sunyard.cop.IF.spring.websocket.WebSocketHandshakeInterceptor;

/**
 * WebSocket配置
 * @author zgz19
 *
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	/**
	 * 配置webSocket
	 * @param registry
	 */
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addHandler(webSocketHandler(), "/websocket.do").setAllowedOrigins("*").addInterceptors(webSocketHandshakeInterceptor());
	}
	
	@Bean
	public WebSocketHandler webSocketHandler() {
		return new WebSocketHandler();
	}
	
	@Bean
	public WebSocketHandshakeInterceptor webSocketHandshakeInterceptor() {
		return new WebSocketHandshakeInterceptor();
	}
	
	@Bean
	public ServletServerContainerFactoryBean servletServerContainerFactoryBean(	
			@Value("${maxTextMessageBufferSize}") Integer maxTextMessageBufferSize,
			@Value("${maxBinaryMessageBufferSize}") Integer maxBinaryMessageBufferSize,
			@Value("${maxSessionIdleTimeout}") Long maxSessionIdleTimeout, 
			@Value("${asyncSendTimeout}") Long asyncSendTimeout) {
		ServletServerContainerFactoryBean factionBean = new ServletServerContainerFactoryBean();
		factionBean.setMaxTextMessageBufferSize(maxTextMessageBufferSize);
		factionBean.setMaxBinaryMessageBufferSize(maxBinaryMessageBufferSize);
		factionBean.setMaxSessionIdleTimeout(maxSessionIdleTimeout);
		factionBean.setAsyncSendTimeout(asyncSendTimeout);
		return factionBean;
	}
	

}
