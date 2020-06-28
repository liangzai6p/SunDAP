package com.sunyard.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sunyard.aos.common.controller.DownLoadServlet;

/**
 * Servlet配置
 * @author sunyard
 *
 */
@Configuration
public class ServletConfig {
	
	/**
	 * 文件下载Servlet
	 * @return
	 */
	@Bean
    public ServletRegistrationBean<DownLoadServlet> downLoadServlet() {
        ServletRegistrationBean<DownLoadServlet> registration = new ServletRegistrationBean<DownLoadServlet>(new DownLoadServlet());
        registration.addUrlMappings("/servlet/DownLoadServlet");
        return registration;
    }

}
