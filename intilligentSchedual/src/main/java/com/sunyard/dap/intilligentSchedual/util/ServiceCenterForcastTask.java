/*
package com.sunyard.dap.intilligentSchedual.util;

import org.apache.log4j.Logger;

import com.sunyard.aos.monitor.bean.ResponseBean;

public class ServiceCenterForcastTask implements Runnable {
	*/
/** 日志记录器 *//*

	private static  Logger logger = Logger.getLogger(ServiceCenterForcastTask.class);
	@Override
	public void run() {
		try{
			ResponseBean responseBean = new ResponseBean();
			logger.info("每月自动执行话务预测任务开始执行");
			responseBean =  MonitorIntelScheUtil.serviceCenterMonthAutoForecast();
			String retCode = responseBean.getRetCode();
			if("IF0000".equals(retCode)){
				//客服中心排班执行
				ResponseBean responseSche = MonitorIntelScheUtil.serviceCenterMonthAutoSchedule();
				String scheCode = responseSche.getRetCode();
				if(!"IF0000".equals(scheCode)){
					logger.info("客服中心排班失败:"+responseSche.getRetMsg());
				}
			}else{
				logger.info("客服中心预测失败:"+responseBean.getRetMsg());
			}
		}catch(Exception e){
			logger.error("客服中心每月自动触发预测任务执行异常", e);
		}

	}

}
*/
