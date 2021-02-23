/*
package com.sunyard.dap.intilligentSchedual.util;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.log4j.Logger;import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sunyard.aos.monitor.bean.ResponseBean;
import com.sunyard.aos.monitor.dao.CenterIntelScheDao;

@Component
public class ForecastTask implements Runnable {
	
	*/
/** 日志记录器 *//*

	protected static Log log = LogFactory.getLog(ForecastTask.class);
	
	@Autowired
	private CenterIntelScheDao centerIntelScheDao;
	
	public static ForecastTask forecastTask;
	
	@PostConstruct
	public void init(){
		forecastTask = this;
	}
	
	@Override
	public void run() {
		try{
			ResponseBean responseBean = new ResponseBean();
			logger.info("每月自动触发业务量预测任务开始执行");
			responseBean =  MonitorIntelScheUtil.busiMonthAutoForecast();
			String retCode = responseBean.getRetCode();
			String retMsg = responseBean.getRetMsg();
			
			HashMap<String, Object> condMap = new HashMap<String, Object>();
			condMap.put("run_type", "8");
			condMap.put("run_content",retCode+"-"+retMsg);
			
			forecastTask.centerIntelScheDao.busiForeShcduleRunLogSave(condMap);
			*/
/*if(!"BUSINESS_EXECUTE_SUCCESS".equals(retCode) && "BUSINESS_NO_MODEL".equals(retCode)){
				//模型训练执行
				ResponseBean responseModelTrain = MonitorIntelScheUtil.busiForecastModelTrain();
				
				String trainCode = responseModelTrain.getRetCode();
				if("MODEL_TRAIN_SUCCESS".equals(trainCode)){
					responseBean = MonitorIntelScheUtil.busiMonthAutoForecast();
				}else{
					logger.info("模型训练失败");
				}
			}*//*

		}catch(Exception e){
			logger.error("每月自动触发业务量预测任务执行异常", e);
		}
		
	}

}
*/
