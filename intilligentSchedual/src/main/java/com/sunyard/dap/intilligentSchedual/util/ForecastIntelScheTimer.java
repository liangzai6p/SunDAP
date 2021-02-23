/*
package com.sunyard.dap.intilligentSchedual.util;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;


public class ForecastIntelScheTimer extends TimerTask {

	*/
/** 日志记录器 *//*

	protected static Log log = LogFactory.getLog(ForecastIntelScheTimer.class);
	
	@Override
	public void run() {
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String todayStr = sdf.format(new Date());
			String monthLastDateStr = MonitorIntelScheUtil.getMonthLastDate(todayStr.substring(0, 7));
			String inputFormat = "yyyy-MM-dd";
			int excuDateLong = MonitorIntelScheUtil.getDayTotalStartDateEndDate(todayStr, monthLastDateStr, inputFormat);
			
			if(excuDateLong == 5){//判断是否是当月最后第五天
				ForecastTask forecast = new ForecastTask();
				IntelScheTask sche = new IntelScheTask();
				
				Thread forecastThread = new Thread(forecast);
				Thread scheThread = new Thread(sche);
				//业务量线程预测线程启动
				log.debug("每月自动业务量预测任务线程启动");
				forecastThread.start();
				//智能排班线程启动
				log.debug("每月自动排班任务线程启动");
				scheThread.start();
				*/
/*//*
/客服中心预测排班线程
				ServiceCenterForcastTask serviceForecast = new ServiceCenterForcastTask();
				Thread serviceThread = new Thread(serviceForecast);
				//客服中心预测排班线程启动
				logger.info("客服中心每月自动预测排班任务线程启动");
				serviceThread.start();*//*

			}else{
				System.out.println("不启动线程");
				log.debug("非每月最后第五天不执行自动任务");
			}
			*/
/*//*
/客服中心预测排班线程
			ServiceCenterForcastTask serviceForecast = new ServiceCenterForcastTask();
			Thread serviceThread = new Thread(serviceForecast);
			//客服中心预测排班线程启动
			logger.info("客服中心每月自动预测排班任务线程启动");
			serviceThread.start();*//*

		}catch(Exception e){
			log.error("预测任务或者智能排班定时任务启动异常", e);
		}
	}
}
*/
