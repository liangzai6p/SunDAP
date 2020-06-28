package com.sunyard.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sunyard.ars.common.util.SpringContextUtils;

/**
 * WebMvc配置
 * @author zgz19
 *
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class MvcConfig implements WebMvcConfigurer{

	/**
	 * 首页
	 * @param registry
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:login.html");
	}
	
	/**
	 * 支持上传文件， 设置上传文件的最大尺寸为2MB
	 * @param maxUploadSize
	 * @return
	 */
	@Bean
	public CommonsMultipartResolver multipartResolver(@Value("${maxUploadSize}") Long maxUploadSize) {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxUploadSize(maxUploadSize);
		return resolver;
	}
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SpringContextUtils springContextUtils() {
		return new SpringContextUtils();
	}

}
