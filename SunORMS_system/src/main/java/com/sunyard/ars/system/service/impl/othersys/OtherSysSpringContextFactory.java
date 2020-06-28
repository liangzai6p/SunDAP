package com.sunyard.ars.system.service.impl.othersys;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.sunyard.ars.system.service.othersys.IOtherSystemService;


/**
 * 外系统接口实现类工厂
 * @author 20190506e
 *
 */
@Service("otherSysSpringContextFactory")
public class OtherSysSpringContextFactory implements ApplicationContextAware {
	private static Map<String, IOtherSystemService> otherSysServiceMap = new HashMap<>();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		Map<String, IOtherSystemService> beanMap = applicationContext.getBeansOfType(IOtherSystemService.class);
		for (IOtherSystemService serviceImpl : beanMap.values()) {
			otherSysServiceMap.put(serviceImpl.getTranCode(), serviceImpl);
		}
	}
	
	public IOtherSystemService getBean(String tranCode){
		return otherSysServiceMap.get(tranCode);
	}
}
