package com.sunyard.dap.intilligentSchedual.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sunyard.dap.intilligentSchedual.entity.RequestDO;
import com.sunyard.dap.intilligentSchedual.entity.ResponseDO;

public class MonitorIntelScheUtil {
	/** 日志记录器 */
	public static Log log = LogFactory.getLog(MonitorIntelScheUtil.class);
	
	public static void main(String[] args) {
		int dayTotal = 0;
		try {
			String aa = getCurDateAheadYearFirstMonth("yyyyMM");
			
			System.out.println("上个月："+aa);

		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("天数："+dayTotal);
	}
	//根据开始日期结束日期获取相隔天数(日期输入格式：)
	public static int getDayTotalStartDateEndDate(String startDate,String endDate,String inputFormat){
		int dayTotal = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(inputFormat);
			Date startDateLong = sdf.parse(startDate);
			Date endDateLong = sdf.parse(endDate);
			dayTotal = (int)((endDateLong.getTime()-startDateLong.getTime())/(60*60*24*1000))+1;
		} catch (Exception e) {
			log.error("getDayTotalStartDateEndDate根据开始日期结束日期获取相隔天数异常",e);
		}
		return dayTotal;
	}
	
	//根据月份获取月份的第一天(输入格式：2020-04)
	public static String getMonthFirstDate(String monthStr){
		String firstDate = "";
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		try{
			int setYear = Integer.parseInt(monthStr.split("-")[0]);
			int setMonth = Integer.parseInt(monthStr.split("-")[1]);
			Calendar cale =  Calendar.getInstance();
			//设置年份
			cale.set(Calendar.YEAR,setYear);
			//设置月份
			cale.set(Calendar.MONTH,setMonth-1);
			//获取某月最小天数
			int firstDay = cale.getActualMinimum(Calendar.DAY_OF_MONTH);
			//设置日历中月份的最小天数
			cale.set(Calendar.DAY_OF_MONTH, firstDay);
			
			firstDate = formatDate.format(cale.getTime());
		}catch(Exception e){
			log.error("getMonthFirstDate根据月份获取月的第一天异常",e);
		}
		return firstDate;
	}
	
	public static String getTomorrowDate(){
		String tomorrowDate = "";
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Calendar cale =  Calendar.getInstance();
			cale.setTime(new Date());
			cale.add(Calendar.DATE, 1);
			tomorrowDate = formatDate.format(cale.getTime());
		}catch(Exception e){
			log.error("getTomorrowDate获取明天日期字符串异常",e);
		}
		return tomorrowDate;
	}
	
	public static String getYesterdayDate(){
		String yesterdayDate = "";
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		try{
			Calendar cale =  Calendar.getInstance();
			cale.setTime(new Date());
			cale.add(Calendar.DATE, -1);
			yesterdayDate = formatDate.format(cale.getTime());
		}catch(Exception e){
			log.error("getYesterdayDate获取昨天日期字符串异常",e);
		}
		return yesterdayDate;
	}
	
	//根据月份获取月份的最后一天(输入格式：2020-04)
	public static String getMonthLastDate(String monthStr){
		String lastDate = "";
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		try{
			int setYear = Integer.parseInt(monthStr.split("-")[0]);
			int setMonth = Integer.parseInt(monthStr.split("-")[1]);
			Calendar cale =  Calendar.getInstance();
			//设置年份
			cale.set(Calendar.YEAR,setYear);
			//设置月份
			cale.set(Calendar.MONTH,setMonth-1);
			//获取某月最大天数
			int lastDay = cale.getActualMaximum(Calendar.DAY_OF_MONTH);
			//设置日历中日历的最大天数
			cale.set(Calendar.DAY_OF_MONTH, lastDay);
			lastDate = formatDate.format(cale.getTime());
		}catch(Exception e){
			log.error("getMonthLastDate根据月份获取月的最后一天异常"+"***"+monthStr+"***",e);
		}
		return lastDate;
	}
	
	//获取当前日期的上个月月份字符串
	public static String getCurDateAheadMonth(String aheadMonthFormat){
		String lastMonth = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(aheadMonthFormat);
			Calendar cale =  Calendar.getInstance();
			cale.setTime(new Date());
			cale.add(cale.MONTH, -1);
			lastMonth = sdf.format(cale.getTime());
		}catch(Exception e){
			log.error("getCurDateAheadMonth获取当前日期的上个月月份字符串异常",e);
		}
		return lastMonth;
	}
	
	//获取当前日期的下个月月份字符串
	public static String getCurDateNextMonth(String nextMonthFormat){
		String nextMonth = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(nextMonthFormat);
			Calendar cale =  Calendar.getInstance();
			cale.setTime(new Date());
			cale.add(cale.MONTH, 1);
			nextMonth = sdf.format(cale.getTime());
		}catch(Exception e){
			log.error("getCurDateNextMonth获取当前日期的下个月月份字符串异常",e);
		}
		return nextMonth;
	}
	
	public static String getCurDateAheadYearFirstMonth(String yearMonthFormat){
		String aheadYear = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(yearMonthFormat);
			SimpleDateFormat yearsdf = new SimpleDateFormat("yyyy");
			Calendar cale =  Calendar.getInstance();
			int curYear = Integer.parseInt(yearsdf.format(new Date()))-1;
			cale.set(cale.YEAR, curYear);
			cale.set(cale.MONTH, 0);
			aheadYear = sdf.format(cale.getTime());
		}catch(Exception e){
			log.error("getCurDateAheadYear根据月份获取月的最后一天异常",e);
		}
		return aheadYear;
	}
	
	//日期数组去空格
	public static String [] headArrayReplace(String [] headArray){
		String [] result = new String[headArray.length];
		try{
			for(int i=0;i<headArray.length;i++){
				result[i] = headArray[i].replaceAll("-", "");
			}
		}catch(Exception e){
			log.error("日期去掉横杠方法异常",e);
		}
		return result;
	}
	
	//业务量预测模型训练接口调用方法
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseDO  busiForecastModelTrain(){
		RequestDO request = new RequestDO();
		ResponseDO  ResponseDO = new ResponseDO();
		try{
			log.debug("模型训练接口调用开始执行");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			List list = new ArrayList();
			String aheadYearFirstMonth = getCurDateAheadYearFirstMonth("yyyyMM");//模型训练使用数据的开始月份
			/*String startMonth = "201901";//模型训练使用数据的开始月份
			String endMonth = "202012";//模型训练使用数据的结束月份*/			
			String curMonth = sdf.format(new Date());//模型训练使用数据的结束月份
			list.add(aheadYearFirstMonth);
			list.add(curMonth);
			list.add(null);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("parameterList", list);
			request.setParameterList(list);
			
			String foreScheServerAddress = PropertiesUtil.getValue("foreScheServerAddress");//业务量预测智能排班服务部署的IP地址
			String foreScheServerPort = PropertiesUtil.getValue("foreScheServerPort");//业务量预测智能排班服务部署的端口号
			String serverStr = "http://"+foreScheServerAddress+":"+foreScheServerPort;//http://25.66.133.59:8090
			
			ResponseDO = sendPost(serverStr+"/task/modelTrain",jsonObject);
			//ResponseDO = sendPost("http://10.10.68.99:8090/task/modelTrain",jsonObject);
		}catch(Exception e){
			log.error("模型训练接口调用异常",e);
		}
		return ResponseDO;
	}
	
	//业务量预测每月自动执行接口调用
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseDO  busiMonthAutoForecast(){
		ResponseDO  ResponseDO = new ResponseDO();
		RequestDO request = new RequestDO();
		try{
			List list = new ArrayList();
			String aheadMonthFormat = "yyyyMM";
			String aheadMonthStr = getCurDateAheadMonth(aheadMonthFormat);//预测使用数据的月份
			String nextMonthFormat = "yyyy-MM";
			String nextMonth = getCurDateNextMonth(nextMonthFormat);
			
			String nextMonthFirstDate = getMonthFirstDate(nextMonth);//"20200301";//预测的开始日期
			String nextMonthLastDate = getMonthLastDate(nextMonth);
			
			String inputFormat = "yyyy-MM-dd";
			int nextDaySum = getDayTotalStartDateEndDate(nextMonthFirstDate, nextMonthLastDate, inputFormat);//预测的天数
			
			list.add(aheadMonthStr);//预测使用数据的月份
			list.add(nextMonthFirstDate.replaceAll("-", ""));//预测的开始日期
			list.add(nextDaySum);//预测的天数
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("parameterList", list);
			request.setParameterList(list);
			
			String foreScheServerAddress = PropertiesUtil.getValue("foreScheServerAddress");//业务量预测智能排班服务部署的IP地址
			String foreScheServerPort = PropertiesUtil.getValue("foreScheServerPort");//业务量预测智能排班服务部署的端口号
			String serverStr = "http://"+foreScheServerAddress+":"+foreScheServerPort;//http://25.66.133.59:8090
			
			ResponseDO = sendPost(serverStr+"/task/taskForecast",jsonObject);
			//ResponseDO = sendPost("http://10.10.68.99:8090/task/taskForecast",jsonObject);
		}catch(Exception e){
			log.error("busiMonthAutoForecast务量预测每月自动执行接口调用方法异常",e);
		}
		return ResponseDO;
	}
	
	//客服中心预测每月自动执行接口调用
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseDO  serviceCenterMonthAutoForecast(){
		ResponseDO  ResponseDO = new ResponseDO();
		RequestDO request = new RequestDO();
		try{
			List list = new ArrayList();
			String aheadMonthFormat = "yyyy-MM";
			String aheadMonthStr = getCurDateAheadMonth(aheadMonthFormat);//预测使用数据的月份
			String nextMonthFormat = "yyyy-MM";
			String nextMonth = getCurDateNextMonth(nextMonthFormat);//预测的月份
			
			String aheadMonthFirstDate = getMonthFirstDate(aheadMonthStr);
			String aheadMonthLastDate = getMonthLastDate(aheadMonthStr);
			
			String nextMonthFirstDate = getMonthFirstDate(nextMonth);//"20200301";//预测的开始日期
			String nextMonthLastDate = getMonthLastDate(nextMonth);
			
			String inputFormat = "yyyy-MM-dd";
			int nextDaySum = getDayTotalStartDateEndDate(nextMonthFirstDate, nextMonthLastDate, inputFormat);//预测的天数
			
			list.add(aheadMonthFirstDate.replaceAll("-", "/"));//预测使用数据的月份第一天
			list.add(aheadMonthLastDate.replaceAll("-", "/"));//预测使用数据的月份最后一天
			list.add(nextMonthFirstDate.replaceAll("-", ""));//预测的开始日期
			list.add(nextDaySum);//预测的天数
			//list.add("1");//预测的类型
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("parameterList", list);
			request.setParameterList(list);
			
			String foreScheServerAddress = PropertiesUtil.getValue("foreScheServerAddress");//业务量预测智能排班服务部署的IP地址
			String foreScheServerPort = PropertiesUtil.getValue("foreScheServerPort");//业务量预测智能排班服务部署的端口号
			String serverStr = "http://"+foreScheServerAddress+":"+foreScheServerPort;//http://25.66.133.59:8090
			
			ResponseDO = sendPost(serverStr+"/call/callForecast",jsonObject);
			//ResponseDO = sendPost("http://10.10.68.99:8090/call/callForecast",jsonObject);
		}catch(Exception e){
			log.error("serviceCenterMonthAutoForecast客服中心预测每月自动执行接口调用方法异常",e);
		}
		return ResponseDO;
	}
	
	//客服中心每月自动排班方法
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseDO serviceCenterMonthAutoSchedule(){
		RequestDO scheRequest = new RequestDO();
		ResponseDO  scheResponse = new ResponseDO();
		try{
			List list = new ArrayList();
			//排班的月份时间段
			String nextMonthFormat = "yyyy-MM";
			String nextMonth = getCurDateNextMonth(nextMonthFormat);//下个月月份字符串
			String nextMonthFirstDate = getMonthFirstDate(nextMonth);//下个月第一天
			String nextMonthLastDate = getMonthLastDate(nextMonth);//下个月最后一天
			String scheStartDate = nextMonthFirstDate.replaceAll("-", "");//排班的开始日期
			//String scheEndDate = nextMonthLastDate.replaceAll("-", "");//排班的结束日期
			int scheDaySum = getDayTotalStartDateEndDate(nextMonthFirstDate, nextMonthLastDate, "yyyy-MM-dd");//排班天数
			
			list.add("CNKF20001");
			list.add(scheStartDate);
			list.add(scheDaySum);
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("parameterList", list);
			scheRequest.setParameterList(list);
			
			String foreScheServerAddress = PropertiesUtil.getValue("foreScheServerAddress");//业务量预测智能排班服务部署的IP地址
			String foreScheServerPort = PropertiesUtil.getValue("foreScheServerPort");//业务量预测智能排班服务部署的端口号
			String serverStr = "http://"+foreScheServerAddress+":"+foreScheServerPort;//http://25.66.133.59:8090
			
			scheResponse = sendPost(serverStr+"/call/callScheduleForecast",jsonObject);
			//scheResponse = sendPost("http://10.10.68.99:8090/call/callScheduleForecast",jsonObject);
		}catch(Exception e){
			log.error("serviceCenterMonthAutoSchedule客服中心每月自动排班异常",e);
		}
		return scheResponse;
	}
	
	//智能排班每月自动排班方法
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseDO intelScheAutoSchedule(){
		RequestDO scheRequest = new RequestDO();
		ResponseDO  scheResponse = new ResponseDO();
		try{
			
			List list = new ArrayList();
			//排班的月份时间段
			String nextMonthFormat = "yyyy-MM";
			String nextMonth = getCurDateNextMonth(nextMonthFormat);//下个月月份字符串
			String nextMonthFirstDate = getMonthFirstDate(nextMonth);//下个月第一天
			String nextMonthLastDate = getMonthLastDate(nextMonth);//下个月最后一天
			String scheStartDate = nextMonthFirstDate.replaceAll("-", "");//排班的开始日期
			String scheEndDate = nextMonthLastDate.replaceAll("-", "");//排班的结束日期
			int scheDaySum = getDayTotalStartDateEndDate(nextMonthFirstDate, nextMonthLastDate, "yyyy-MM-dd");//排班天数
			
			//排班统计工作时长时间段
			String aheadMonthFormat = "yyyy-MM";
			String aheadMonthStr = getCurDateAheadMonth(aheadMonthFormat);//上个月月份字符串
			String aheadMonthMonthFirstDate = getMonthFirstDate(aheadMonthStr);//上个月第一天
			String aheadMonthMonthLastDate = getMonthLastDate(aheadMonthStr);//上个月最后一天
			String workTimeStartDate = aheadMonthMonthFirstDate.replaceAll("-", "");//"20200301";//排班工作时长统计的开始日期
			String workTimeEndDate = aheadMonthMonthLastDate.replaceAll("-", "");//"20200331";//排班工作时长统计的结束日期
			//排班使用源数据区间
			String aheadMonth = getCurDateAheadMonth("yyyy-MM");//上个月月份字符串
			String scheDataStartDate = getMonthFirstDate(aheadMonth).replaceAll("-", "");//"20190301";//排班使用历史数据的开始日期
			String scheDataEndDate = getMonthLastDate(aheadMonth).replaceAll("-", "");//"20200331";//排班使用历史数据的结束日期
			
			list.add(scheStartDate);
			list.add(scheEndDate);
			list.add(workTimeStartDate);
			list.add(workTimeEndDate);
			list.add(scheDataStartDate);
			list.add(scheDataEndDate);
			list.add(scheDaySum);
			list.add(null);
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("parameterList", list);
			scheRequest.setParameterList(list);
			
			String foreScheServerAddress = PropertiesUtil.getValue("foreScheServerAddress");//业务量预测智能排班服务部署的IP地址
			String foreScheServerPort = PropertiesUtil.getValue("foreScheServerPort");//业务量预测智能排班服务部署的端口号
			String serverStr = "http://"+foreScheServerAddress+":"+foreScheServerPort;//http://25.66.133.59:8090
			
			scheResponse = sendPost(serverStr+"/task/scheduleForecast",jsonObject);
			//scheResponse = MonitorIntelScheUtil.sendPost("http://10.10.68.99:8090/task/scheduleForecast",jsonObject);
		}catch(Exception e){
			log.error("intelScheAutoSchedule智能排班每月自动排班异常",e);
		}
		return scheResponse;
	}
	
	//智能排班本月剩余日期排班方法
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseDO  intelScheScheduleForecast(List scheItermList){
		RequestDO scheRequest = new RequestDO();
		ResponseDO  scheResponse = new ResponseDO();
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			String todayStr = sdf.format(new Date());
			List list = new ArrayList();
			String tomorrowDate = getTomorrowDate();//获取明天日期字符串
			String yestodayDate = getYesterdayDate();//获取昨天日期字符串
			String firstDate = getMonthFirstDate(todayStr);//传入月份获取第一天(例如：2020-04;返回：2020-04-01)
			String lastDate = getMonthLastDate(todayStr);//传入月份获取最后(例如：2020-04;返回：2020-04-30)
			
			String scheStartDate = tomorrowDate.replaceAll("-", "");//排班的开始日期
			String scheEndDate = lastDate.replaceAll("-", "");//排班的结束日期
			String workTimeStartDate = firstDate.replaceAll("-", "");//"20200301";//排班工作时长统计的开始日期
			String workTimeEndDate = yestodayDate.replaceAll("-", "");//"20200331";//排班工作时长统计的结束日期
			
			int scheDaySum = getDayTotalStartDateEndDate(tomorrowDate, lastDate, "yyyy-MM-dd");//排班天数
			
			String aheadMonth = getCurDateAheadMonth("yyyy-MM");//上个月月份字符串
			String scheDataStartDate = getMonthFirstDate(aheadMonth).replaceAll("-", "");//"20190301";//排班使用历史数据的开始日期
			String scheDataEndDate = getMonthLastDate(aheadMonth).replaceAll("-", "");//"20200331";//排班使用历史数据的结束日期
			
			Map<Object, Object> scheSysMap = new HashMap<Object, Object>();
			list.add(scheStartDate);
			list.add(scheEndDate);
			list.add(workTimeStartDate);
			list.add(workTimeEndDate);
			list.add(scheDataStartDate);
			list.add(scheDataEndDate);
			list.add(scheDaySum);
			list.add(scheItermList);
			
			scheSysMap.put("parameterList", list);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("parameterList", list);
			scheRequest.setParameterList(list);
			
			String foreScheServerAddress = PropertiesUtil.getValue("foreScheServerAddress");//业务量预测智能排班服务部署的IP地址
			String foreScheServerPort = PropertiesUtil.getValue("foreScheServerPort");//业务量预测智能排班服务部署的端口号
			String serverStr = "http://"+foreScheServerAddress+":"+foreScheServerPort;//http://25.66.133.59:8090
			
			scheResponse = sendPost(serverStr+"/task/scheduleForecast",jsonObject);
			
		}catch(Exception e){
			log.error("intelScheScheduleForecast智能排班异常",e);
		}
		return scheResponse;
	}
	
	//HTTP请求方法
	public static ResponseDO sendPost(String url, Object param) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");
            conn.setConnectTimeout(50000);
            conn.setReadTimeout(3600000);
            // 获取URLConnection对象对应的输出流
            // out = new PrintWriter(conn.getOutputStream());
            out = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            // 发送请求参数
            out.write(param.toString());
            // flush输出流的缓冲
            out.flush();
            System.out.print("请求已发送");
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
        	try {
        		if (out != null) {
					out.close();
        		}
        		
        		if (in != null) {
                    in.close();
                }
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
        
        // 要求请求返回的数据格式化等价于ResponseDO
        JSONObject json = (JSONObject) JSON.parse(result.toString());
        ResponseDO ResponseDO = JSON.toJavaObject(json, ResponseDO.class);
        
        return ResponseDO;
    }
}
