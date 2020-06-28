package com.sunyard.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.sunyard.aos.common.filter.UserSessionFilter;
import com.sunyard.cop.IF.filter.GetRequestFilter;

/**
 * 过滤器配置
 * @author sunyard
 *
 */
@Configuration
public class FilterConfig {
	
	/**
	 * 编码过滤器
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<CharacterEncodingFilter> characterEncodingFilterARS() {
		FilterRegistrationBean<CharacterEncodingFilter> registration = new FilterRegistrationBean<CharacterEncodingFilter>();
		registration.setFilter(new CharacterEncodingFilter());
		registration.addUrlPatterns("/*");
		registration.setAsyncSupported(true);
		registration.addInitParameter("encoding", "UTF-8");
		registration.addInitParameter("forceEncoding", "true");
		registration.setName("characterEncodingFilter");
		registration.setOrder(1);
		return registration;
	}
	
	
	/**
	 * userSession过滤器
	 * @return
	 */
	@Bean
    public FilterRegistrationBean<UserSessionFilter> userSessionFilter() {
        FilterRegistrationBean<UserSessionFilter> registration = new FilterRegistrationBean<UserSessionFilter>();
        registration.setFilter(new UserSessionFilter());
        registration.addUrlPatterns("/*");
        registration.setName("userSessionFilter");
        registration.setOrder(2);
        return registration;
    }
	
	/**
	 * getRequest过滤器, IF包中@ArchivesLog注解解析器会使用，拆分微服务后仍不能去掉
	 * @return
	 */
	@Bean
    public FilterRegistrationBean<GetRequestFilter> getRequest() {
        FilterRegistrationBean<GetRequestFilter> registration = new FilterRegistrationBean<GetRequestFilter>();
        registration.setFilter(new GetRequestFilter());
        registration.addUrlPatterns("/*");
        registration.setName("getRequest");
        registration.setOrder(3);
        return registration;
    }
	

}
