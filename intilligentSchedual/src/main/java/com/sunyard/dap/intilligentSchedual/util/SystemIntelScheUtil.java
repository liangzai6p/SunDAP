package com.sunyard.dap.intilligentSchedual.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.sunyard.dap.common.model.ReturnT;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class SystemIntelScheUtil {

	/** 日志记录器 */
	protected static Log log = LogFactory.getLog(SystemIntelScheUtil.class);
	
	
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
				log.error("getMonthLastDate根据月份获取月的最后一天异常",e);
			}
			return lastDate;
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
		
		//HTTP请求方法
		public static String sendPost(String url, Object param) {
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
	        
	        // 要求请求返回的数据格式化等价于ResponseBean
	        /*JSONObject json = (JSONObject) JSON.parse(result.toString());
	        ResponseBean responseBean = JSON.toJavaObject(json, ResponseBean.class);*/
	        
	        return result;
	    }
		
}
