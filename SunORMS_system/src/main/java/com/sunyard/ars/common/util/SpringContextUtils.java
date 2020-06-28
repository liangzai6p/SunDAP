/**
 * 
 */
package com.sunyard.ars.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Administrator
 *
 */
public class SpringContextUtils implements ApplicationContextAware {
	private static ApplicationContext context;
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		SpringContextUtils.context = applicationContext;
	}

	public static ApplicationContext getContext(){
		return context;
	}
}
