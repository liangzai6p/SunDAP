package com.sunyard.config;

import java.io.File;
import java.io.FileNotFoundException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import com.sunyard.ars.common.filter.SessionListener;
import com.sunyard.sunflow.SunflowEngine_WebService;

/**
 * Listener配置
 * 
 * @author sunyard
 *
 */
@Configuration
public class ListenerConfig {

	/**
	 * sunFlowEngineListener
	 * 
	 * @return
	 */
//	@Bean
//	public ServletListenerRegistrationBean<SunFlowEngineListener> sunFlowEngineListener() {
//		return new ServletListenerRegistrationBean<SunFlowEngineListener>(new SunFlowEngineListener());
//	}

//	/**
//	 * sessionListener
//	 * 
//	 * @return
//	 */
//	@Bean
//	public ServletListenerRegistrationBean<SessionListener> sessionListener() {
//		return new ServletListenerRegistrationBean<SessionListener>(new SessionListener());
//	}

}

class SunFlowEngineListener implements ServletContextListener {
	private final Logger logger = LoggerFactory.getLogger(SunFlowEngineListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent paramServletContextEvent) {
		try {
			SunflowEngine_WebService.shutdownService();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent paramServletContextEvent) {
		String str1 = "";
		try {
			str1 = getClass().getClassLoader().getResource("/").getPath();
			System.out.println("---获取系统系统路径：" + str1);
			str1 = getWebContextPath(str1);
		} catch (Exception localException) {
			try {
				str1 = ResourceUtils.getURL("classpath:").getPath();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				str1 = paramServletContextEvent.getServletContext().getRealPath("/");
			}
			System.out.println("获取系统系统路径---：" + str1);
		}
		try {
			System.setProperty("SUNFLOW_HOME", str1);
			SunflowEngine_WebService.start();
		} catch (Throwable localThrowable) {
			String str2 = "SunFlowWebService服务启动报错";
			this.logger.error(str2, localThrowable);
			throw new RuntimeException(str2, localThrowable);
		}
	}

	private String getWebContextPath(String paramString) {
		return new File(paramString).getParentFile().getParent();
	}
}
