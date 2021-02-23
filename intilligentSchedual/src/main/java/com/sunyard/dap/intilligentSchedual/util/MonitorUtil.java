package com.sunyard.dap.intilligentSchedual.util;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;


/**
 * @author ock
 * @date 2020年1月2日
 * @Decs 工具类
 * 
 */
public class MonitorUtil {
	
	/** 日志记录器 */
	public static Log log = LogFactory.getLog(MonitorUtil.class);
	
	
	/**
	 * @author ock
	 * @date 2020年1月2日
	 * @Decs 获取当前时间点所处时段点工具(一个小时五分钟一个点中的最大点,例如:当前时间12:34:23则为1230,若当前时间12:36:23则为1235)
	 * 
	 */
	public static int currentMaxTimePoint(){
		//获取当前时间
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String nowTime = sf.format(new Date());
		String hour = nowTime.substring(8, 10);
		int currMinute = Integer.parseInt(nowTime.substring(10, 12));
		int minuteValue = 0;
		//获取时间点,以每个小时12个点划分(即五分钟一个点),判断当前时间属于哪个点(例如：2019012085433   取到分钟就是54分,当前时间点取50分);
		for(int i=5;i<60;i+=5){
			int tempA = i-5;
			int tempB = i+5;
			if(tempA == currMinute){
				minuteValue = tempA;
				break;
			}else if(currMinute > tempA && currMinute < i){
				minuteValue = tempA;
				break;
			}else if(currMinute > i && currMinute < tempB){
				minuteValue = i;
				break;
			}
		}
		//将小于10的分钟前加0(例如：5  变为05格式)
		String minuteStr = minuteValue < 10 ? "0"+minuteValue : minuteValue+"";
		int maxTime = Integer.parseInt(hour+minuteStr);
		
		return maxTime;
	}
	
	/**
	 * @author ock
	 * @date 2020年1月2日
	 * @Decs 图表折线图数据格式化
	 * 
	 */
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public static List postDataEchartFormat(String[] headArray,List dataList,String item){
		List postArray = new ArrayList();
		try{
			//log.info("postDataEchartFormat-Start");
			//获取当前时间所处的时段时间点值
			int maxTime  = currentMaxTimePoint();
			for(int i=0;i<headArray.length;i++){
				String act_time = headArray[i];
				int currTime = Integer.parseInt(act_time);
				String timeValue = "";
				//判断获取当前数据时间点查询结果是否有值
				for(int j=0;j<dataList.size();j++){
					Map postMapQuery = (Map)dataList.get(j);
					String act_create_time = postMapQuery.get("run_time")+"";
					if(act_create_time.equals(act_time)){
						timeValue = postMapQuery.get(item)+"";
					}
				}
				//判断时间点的值,最大时间点之前的时刻没有值则该时刻业务量为0,最大时间点之后的时刻业务置为空
				if(currTime<= maxTime){
					if("".equals(timeValue)){
						//当前最大时间点处没有值则不置零
						if(currTime == maxTime){
							postArray.add(timeValue);
						}else{
							//当前最大时间点之外没有值则置零
							int value = 0;
							postArray.add(value);
						}
					}else{
						int count = Integer.parseInt(timeValue);
						postArray.add(count);
					}
				}else{
					String afterTime = "";
					postArray.add(afterTime);
				}
			}
			//log.info("postDataEchartFormat-End");
		}catch(Exception e){
			log.error("postDataEchartFormat数据格式化异常",e);
		}
		return postArray;
	}
	
	/**
	 * 获取由开始日期到结束日期中间每一天的日期格式字符串数组
	 * @param startDate 开始时间（包含）
	 * @param endDate 结束时间（包含）
	 * @return
	 */
	public static String[] getTimes(String startDate, String endDate){
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = null;
		try {
			 start = sd.parse(startDate);
			 end = sd.parse(endDate);
		} catch (ParseException e) {
			//System.out.println("日期转换出错！");
			log.error("getTimes日期转换异常",e);
		}
		//两个DATE类型时间差为毫秒级，要转换为天数，并加上首尾一天
		int length = (int) ((end.getTime() - start.getTime())/(60*60*24*1000)+1);
		String[] times = new String[length];
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(start);
		for(int i = 0;i<length;i++){
			times[i] = sd.format(calendar.getTime());
			calendar.add(calendar.DATE, 1);
		}
		
		return times;
	}
	
	/**
	 * @author ock
	 * @date 2020年1月2日
	 * @Decs 单根柱子图表数据格式化
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public static List singleEchartDataFormat(String nameKey,String valueKey,List headNameList,List queryDataList ){
		List resultList = new ArrayList();
		try{
			//log.info("singleEchartDataFormat-Start");
			for(int i=0;i<headNameList.size();i++){
				String currName = headNameList.get(i)+"";
				double value = 0;
				for(int j=0;j<queryDataList.size();j++){
					Map valueMap = (Map)queryDataList.get(j);
					String valueName = valueMap.get(nameKey)+"";
					String value1 = valueMap.get(valueKey)+"";
					if(valueName.equals(currName) && !"".equals(value1)){
						//value = Integer.parseInt(value1);
						value = Double.valueOf(value1);
						break;
					}
				}
				resultList.add(value);
			}
			//log.info("singleEchartDataFormat-End");
		}catch(Exception e){
			log.error("singleEchartDataFormat数据格式化异常",e);
		}
		return resultList;
	}
	
	/**
	 * @author ock
	 * @date 2020年1月2日
	 * @Decs 走势图数据格式化
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List queryTrendEchartDataFormat(String queryHeadKey,String queryValueKey,List dataList,String[] headArray){
		List resultList = new ArrayList();
		try{
			//log.info("queryTrendEchartDataFormat-Start");
			for(int i=0;i<headArray.length;i++){
				String currData = headArray[i].replaceAll("-", "");
				double currValue = 0;
				for(int j=0;j<dataList.size();j++){
					Map currMap = (Map)dataList.get(j);
					String queryDate = currMap.get(queryHeadKey)+"";
					String queryValue = currMap.get(queryValueKey)+"";
					if(currData.equals(queryDate) && !"".equals(queryValue) && !"null".equals(queryValue)){
						//currValue = Integer.parseInt(queryValue);
						currValue = Double.valueOf(queryValue);
						break;
					}
				}
				resultList.add(currValue);
			}
			//log.info("queryTrendEchartDataFormat-End");
		}catch(Exception e){
			log.error("queryTrendEchartDataFormat数据格式化异常",e);
		}
		
		return resultList;
	}
	
	/**
	 * @author ock
	 * @date 2020年1月2日
	 * @Decs 将秒格式的时间转化为XX时XX分XX秒的形式
	 * 
	 */
	public static String secondFormatStamp(int second){
		String time = "";
		try{
			//log.info("secondFormatStamp-End");
			int hour = second/60/60;
			int min = second/60%60;
			int sec = second%60;
			
			//小时
			if (hour == 0) {
				time += "00:";
			} else if(hour < 10){
				time += "0" + hour + ":";
			} else{
				time += hour + ":";
			}
			
			//分钟
			if (min == 0) {
				time += "00:";
			} else if(min < 10){
				time += "0" + min + ":";
			} else{
				time += min + ":";
			}
			
			//秒
			if (sec == 0) {
				time += "00";
			} else if(sec < 10){
				time += "0" + sec;
			} else{
				time += sec;
			}
			//log.info("secondFormatStamp-End");
		}catch(Exception e){
			log.error("secondFormatStamp格式转化异常",e);
		}
		//传入的值为0是
		if("".equals(time)){
			return second+"";
		}
		return time;
	}
	
}
