package com.sunyard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.sunyard.ars.common.comm.ARSInit;
import com.sunyard.ars.common.comm.ARSOcrWork;
import com.sunyard.ars.common.comm.FlowTaskInit;
import com.sunyard.ars.system.init.SystemInitialize;
import com.sunyard.cop.IF.common.Init;

/**
 * 初始化配置
 * @author sunyard
 *
 */
@Configuration
public class InitConfig {
	
	/**
	 * 启动时记载初始信息
	 * @return
	 */
	@Bean(initMethod = "init")
	@Lazy(false)
    public Init inti(){
        return new Init();
    }
	
	@Bean(initMethod = "init")
	@Lazy(false)
	public ARSInit arsInit() {
		return new ARSInit();
	}
	
	/**
	 * 系统初始化（一些需要从数据库加载的初始化参数）
	 * @return
	 */
	@Bean(initMethod = "systemInit")
	@Lazy(false)
	public SystemInitialize systemInitialize() {
		return new SystemInitialize();
	}
	
	/**
	 * 识别后自动勾对任务
	 * @return
	 */
	@Bean(initMethod = "ocrBackWork", destroyMethod="destroyTimer")
	@Lazy(false)
	public ARSOcrWork arsOcrWork() {
		return new ARSOcrWork();
	}
	
	/**
	 * 加载需要启动工作流的批次
	 * @return
	 */
	@Bean(initMethod = "flowInit")
	@Lazy(false)
	public FlowTaskInit flowTaskInit() {
		return new FlowTaskInit();
	}
	
	

}
