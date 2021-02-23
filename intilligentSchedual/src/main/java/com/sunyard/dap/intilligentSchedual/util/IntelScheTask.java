/*
package com.sunyard.dap.intilligentSchedual.util;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sunyard.aos.monitor.bean.ResponseBean;
import com.sunyard.aos.monitor.dao.CenterIntelScheDao;

@Component
public class IntelScheTask implements Runnable {

	*/
/** 日志记录器 *//*

	private static  Logger logger = Logger.getLogger(IntelScheTask.class);
	@Autowired
	private CenterIntelScheDao centerIntelScheDao;
	
	public static IntelScheTask intelScheTask;
	
	@PostConstruct
	public void init(){
		intelScheTask = this;
	}
	
	@Override
	public void run() {
		try{
			ResponseBean responseBean = new ResponseBean();
			logger.info("每月自动触发智能排班任务开始执行");
			responseBean =  MonitorIntelScheUtil.intelScheAutoSchedule();
			
			String retCode = responseBean.getRetCode();
			String retMsg = responseBean.getRetMsg();
			
			HashMap<String, Object> condMap = new HashMap<String, Object>();
			condMap.put("run_type", "9");
			condMap.put("run_content",retCode+"-"+retMsg);
			
			intelScheTask.centerIntelScheDao.busiForeShcduleRunLogSave(condMap);
		}catch(Exception e){
			logger.error("每月自动触发智能排班任务执行异常", e);
		}
		
	}

}
*/
