/*
package com.sunyard.dap.intilligentSchedual.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class ForecastIntelScheTimerManager {
	//执行定时任务的时间间隔
	private static final long PERIOD_DAY = 24 * 60 * 60 *1000;
	
	public ForecastIntelScheTimerManager() {
		Calendar cale = Calendar.getInstance(); 
		cale.set(cale.HOUR_OF_DAY, 4);
		cale.set(cale.MINUTE, 0);
		cale.set(cale.SECOND, 0);
		
		Date date = cale.getTime();
		*/
/*如果第一次执行定时任务的时间小于当前时间,
		 * 则要在第一次执行定时任务的时间加一天,以便使定时任务在下个时间点执行;
		 * 否则定时任务会立即执行且循环周期以当前时间为准
		*//*

		if(date.before(new Date())){
			date = this.addDay(date, 1);
			System.out.println("第一次启动的日期");
		}
		
		Timer timer = new Timer();
		ForecastIntelScheTimer forecastScheTimer = new ForecastIntelScheTimer();
		timer.schedule(forecastScheTimer, date,PERIOD_DAY);
	}

	public Date addDay(Date date,int num){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendar.DAY_OF_MONTH, num);
		return calendar.getTime();
	}
	
	
	
}
*/
