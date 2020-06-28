package com.sunyard.ars.common.util;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sunyard.ars.common.comm.ARSConstants;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * Date Utility Class This is used to convert Strings to Dates and Timestamps
 * 
 * @version $Revision: 1.1 $ $Date: 2012/11/26 07:12:09 $
 */
public class DateUtil {
	// ~ Static fields/initializers
	// =============================================

	private static Log log = LogFactory.getLog(DateUtil.class);

	private static String defaultDatePattern = null;

	private static String timePattern = "HH:mm";

	public static final String FORMATE_DATE = "yyyy-MM-dd";
	public static final String FORMATE_SECONDS = "HH:mm:ss";
	public static final String FORMATE_DATE_yyyyMMdd = "yyyyMMdd";
	public static final String FORMATE_FULL = FORMATE_DATE.concat(" ").concat(FORMATE_SECONDS);

	public static synchronized String getDatePattern() {
		Locale locale = Locale.getDefault();// LocaleContextHolder.getLocale();
		try {
			defaultDatePattern = ResourceBundle.getBundle(ARSConstants.BUNDLE_KEY,
					locale).getString("date.format");
		} catch (MissingResourceException mse) {
			defaultDatePattern = "MM/dd/yyyy";
		}

		return defaultDatePattern;
	}
	
	
	/**
	 * @Title: detailTime
	 * @Description: 获取当值格式化的时间
	 * @return
	 * @return String
	 */
	public static String detailTimeSSS() {
		String date = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		date = sdf.format(new Date(System.currentTimeMillis()));
		return date;
	}

	public static final String getDate(Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";
		if (aDate != null) {
			df = new SimpleDateFormat(getDatePattern());
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	public static final Date convertStringToDate(String aMask, String strDate)
			throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);

		if (log.isDebugEnabled()) {
			log.debug("converting '" + strDate + "' to date with mask '"
					+ aMask + "'");
		}

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			// log.error("ParseException: " + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}

	
	
	public static String getAfrDateByGap(String startDate, int gap) {
		String day = startDate.trim().substring(6);
		String month = startDate.trim().substring(4, 6);
		String year = startDate.trim().substring(0, 4);
		int daytemp = Integer.parseInt(day);
		int monthtemp = Integer.parseInt(month);
		// logger.info("month:"+monthtemp);
		int yeartemp = Integer.parseInt(year);
		int daystart = daytemp - gap;
		if (daystart < 0) {
			monthtemp = monthtemp - 1;
			if (monthtemp == 1 || monthtemp == 3 || monthtemp == 5
					|| monthtemp == 7 || monthtemp == 8 || monthtemp == 10
					|| monthtemp == 12) {
				daystart = daystart + 31;
			} else if (monthtemp == 2) {
				if (CheckLeap(yeartemp)) {
					daystart = daystart + 29;
				} else {
					daystart = daystart + 28;
				}
			} else if (monthtemp < 1) {
				yeartemp = yeartemp - 1;
				monthtemp = monthtemp + 12;
				daystart = daystart + 31;
			} else {
				daystart = daystart + 30;
			}

		} else {
		}
		day = String.valueOf(daystart);
		if (day.length() < 2) {
			day = "0" + String.valueOf(daystart);
		}
		if (String.valueOf(monthtemp).length() < 2) {
			month = "0" + String.valueOf(monthtemp);
		} else {
			month = String.valueOf(monthtemp);// wangyangyi
												// 逻辑错误，如果等于两位的话应该赋值给month
		}
		year = String.valueOf(yeartemp);
		return year + month + day;
	}

	/**
	 * 合并处理 后时间 20050830
	 * 
	 * @param strvalue
	 *            String
	 * @return String
	 */
	public static String getBefDateByGap(String startRegTime, int gap) {
		String day = startRegTime.trim().substring(6);
		String month = startRegTime.trim().substring(4, 6);
		String year = startRegTime.trim().substring(0, 4);
		int daytemp = Integer.parseInt(day);
		int monthtemp = Integer.parseInt(month);
		int yeartemp = Integer.parseInt(year);

		int dayend = daytemp + gap;
		if (monthtemp == 1 || monthtemp == 3 || monthtemp == 5
				|| monthtemp == 7 || monthtemp == 8 || monthtemp == 10
				|| monthtemp == 12) {
			if (dayend > 31) {
				monthtemp = monthtemp + 1;
				if (monthtemp > 12) {
					yeartemp = yeartemp + 1;
					monthtemp = monthtemp - 12;
				}
				dayend = dayend - 31;
			}
		} else if (monthtemp == 2) {
			if (CheckLeap(yeartemp)) {
				if (dayend > 29) {
					monthtemp = monthtemp + 1;
					if (monthtemp > 12) {
						yeartemp = yeartemp + 1;
						monthtemp = monthtemp - 12;
					}
					dayend = dayend - 29;
				}
			} else {
				if (dayend > 28) {
					monthtemp = monthtemp + 1;
					if (monthtemp > 12) {
						yeartemp = yeartemp + 1;
						monthtemp = monthtemp - 12;
					}
					dayend = dayend - 28;
				}
			}
		} else {
			if (dayend > 30) {
				monthtemp = monthtemp + 1;
				if (monthtemp > 12) {
					yeartemp = yeartemp + 1;
					monthtemp = monthtemp - 12;
				}
				dayend = dayend - 30;
			}
		}

		day = String.valueOf(dayend);
		if (day.length() < 2) {
			day = "0" + String.valueOf(dayend);
		}

		if (String.valueOf(monthtemp).length() < 2) {
			month = "0" + String.valueOf(monthtemp);
		}

		year = String.valueOf(yeartemp);
		return year + month + day;
	}

	// 判断闰年
	private static boolean CheckLeap(int year) {
		if ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)) {
			return true;
		} else {
			return false;
		}
	}

	// 删除时间的"-"
	public static String Delete(String something) {
		String dealwithsomething = "";
		int tempflag = 0;
		int temp = 0;
		for (int i = 0; i < 2; i++) {
			tempflag = something.indexOf("-", temp);
			dealwithsomething = dealwithsomething
					+ something.substring(temp, tempflag).trim();
			temp = tempflag + 1;
			if (i == 1) {
				dealwithsomething = dealwithsomething
						+ something.substring(temp).trim();
			}
		}
		return dealwithsomething;
	}

	// 增加时间的"-"
	public static String addDateRow(String something) {

		if(null == something || "".equals(something)){
			return something;
		}

		String dateTime = "";
		try {
			dateTime = something.substring(0, 4) + "-"
					+ something.substring(4, 6) + "-" + something.substring(6);
		} catch (Exception e) {
			log.error("",e);
		}
		return dateTime;
	}
	/**
	 *	将字符串时间yyyyMMddHHmmss转化成yyyy-MM-dd HH:mm:ss.例如：20161212151445转换成2016-12-12 15:14:45
	 * @param datetime
	 * @return
	 */
	public static String addDateTimeRow(String datetime){
		
		if(StringUtil.checkNull(datetime)){
			return datetime;
		}
		String str_date="";
		String str_time="";
		str_date = datetime.substring(0, 4)+"-"+datetime.substring(4, 6)+"-"+datetime.substring(6, 8);
		str_time = datetime.substring(8, 10)+":"+datetime.substring(10, 12)+":"+datetime.substring(12);
		return str_date+" "+str_time;
	}

	/**
	 *	获取字符串时间yyyyMMddHHmmss的时间，以HH:mm:ss格式返回，例如：20161212151445返回15:14:45
	 * @param datetime
	 * @return
	 */
	public static String addTimeRow(String datetime){

		if(StringUtil.checkNull(datetime)){
			return datetime;
		}
		String str_time="";
		str_time = datetime.substring(8, 10)+":"+datetime.substring(10, 12)+":"+datetime.substring(12);
		return str_time;
	}


	public static String getTimeNow(Date theTime) {
		return getDateTime(timePattern, theTime);
	}

	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));

		return cal;
	}
	/**
	 * 获取今天日期，格式由参数传入
	 * 
	 * @param reg返回日期的格式
	 * @return
	 */
	public static String getToday(String reg) {
		DateFormat dateFormat = new SimpleDateFormat(reg);
		String today = dateFormat.format((new Date())).toString();
		return today;
	}
	
	
	public static final String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {
			log.error("aDate is null!");
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	public static final String convertDateToString(Date aDate) {
		return getDateTime(getDatePattern(), aDate);
	}

	public static Date convertStringToDate(String strDate)
			throws ParseException {
		Date aDate = null;

		try {
			if (log.isDebugEnabled()) {
				log.debug("converting date with pattern: " + getDatePattern());
			}
			aDate = convertStringToDate(getDatePattern(), strDate);
		} catch (ParseException pe) {
			log.error("Could not convert '" + strDate
					+ "' to a date, throwing exception");
			log.error("",pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());

		}

		return aDate;
	}

	public static String getYMD() {
		return new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(
				new Date()).toString();
	}

	public static String getHMS() {
		return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(
				new Date()).toString();
	}

	public static String getHMS2() {
		return new SimpleDateFormat("HHmmss", Locale.getDefault()).format(
				new Date()).toString();
	}

	//获取当前日期yyyy-MM-dd
	public static String getNow(){
		String date = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		date = sdf.format(new Date(System.currentTimeMillis()));
		return date;
	}
	
	public static String getYMDHMS() {
		return new SimpleDateFormat("yyyyMMdd HHmmss", Locale.getDefault())
				.format(new Date()).toString();
	}

	public static String getYMDHMS2() {
		return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
				.format(new Date()).toString();
	}

	/**
	 * @Title: geteYMDHMSS
	 * @Description: 精确到时间毫秒
	 * @return String
	 */
	public static String geteYMDHMSS() {
		return new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault())
				.format(new Date()).toString();
	}

	public static String changeYMD(String date) {
		return new SimpleDateFormat("yyyyMMdd").format(DateUtil.getYMD())
				.toString();
	}

	// 得到当前工作日的后三个工作日的日期
	public static String getTodayByGap(String startDate, int gap) {
		String today = "", afterThreeDate = "";
		SimpleDateFormat dateInput = new SimpleDateFormat("yyyymmdd");
		today = dateInput.format(new Date()).toString();
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		try {
			date = dateInput.parse(today);
		} catch (ParseException e) {
			log.error("",e);
		}
		calendar.setTime(date);
		// logger.info(today);
//		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//		if (dayOfWeek > 1) {
			afterThreeDate = getAfrDateByGap(startDate, gap);// wangyangyi去掉+2的周末
//		} else {
//			afterThreeDate = getAfrDateByGap(startDate, gap);
//		}
		return afterThreeDate;
	}

	// 得到当前日期后的若干年的日期；
	public static String getDateByGap(int months) {
		String afterMonthsDate = "";
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		calendar.add(Calendar.MONTH, 16);

		// logger.info(calendar.getTime());
		// logger.info(df.format(calendar.getTime()));

		return afterMonthsDate;
	}

	/**
	 * 传入一个日历和一个 int偏移量n个月的日期，可以得到相应的下一个日期的字符串
	 * 
	 * @param calendar
	 * @param n
	 * @return
	 * 
	 */
	public static String getNextDate(Calendar calendar, int n) {
		// Calendar calendar = Calendar.getInstance();
		calendar.add(calendar.MONTH, +n);
		return new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(
				calendar.getTime()).toString();
	}

	/**
	 * 功能：传入一个日历得到偏移量n个天数的日期 创建者：zhangbo 创建时间：2011-11-04
	 * 
	 * @param calendar
	 * @param n
	 * @return
	 */
	public static String getNextDay(Calendar calendar, int n) {
		// Calendar calendar = Calendar.getInstance();
		calendar.add(calendar.DATE, +n);
		return new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(
				calendar.getTime()).toString();
	}

	public static Date parseDate(String strDate) {
		Date date = null;

		DateFormat df = DateFormat.getDateInstance();
		try {
			date = df.parse(strDate);
		} catch (ParseException e) {
			log.error("",e);
		}

		return date;
	}

	/**
	 * 功能：传入一个日历得到偏移量n个月份的日期
	 * 创建者：hjf
	 * 创建时间：2012-11-09
	 * @param calendar
	 * @param n
	 * @return
	 */
	public static String getBeforeDate(Calendar calendar,int n){
		//Calendar calendar = Calendar.getInstance();
		calendar.add(calendar.MONTH, -n);
		return new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(
				calendar.getTime()).toString();
	}
	
	//hjf 获取指定分钟前的时间
	/**
	 * 
	 * @param calendar
	 * @param n
	 * @return
	 */
	public static String getBeforeTime(Calendar calendar,int n){
		//Calendar calendar = Calendar.getInstance();
		calendar.add(calendar.MINUTE, -n);
		return new SimpleDateFormat("HHmmss", Locale.getDefault()).format(
				calendar.getTime()).toString();
	}
	
    /**
     * 获取前一天日期
     * author hjf
     * @param args
     * @throws ParseException
     */
	public static String getSpecifiedDayBefore(String specifiedDay){
		  Calendar c=Calendar.getInstance();
		  Date date=null;
		  try{
			  date=new SimpleDateFormat("yyyyMMdd").parse(specifiedDay);
			  
		  }
		  catch(ParseException e){
			  log.error("",e);
		  }
		  c.setTime(date);
		  int day=c.get(Calendar.DATE);
		  c.set(Calendar.DATE, day-1);
		  String dayBefore=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		  return dayBefore;
	}

	
    /**获取指定日期前一月的日期
    * hjf 
    * @param specifiedDay 
    * @param n >0表示隔多少月
    * @return
    */
	public static String getSpecifiedDayBeforeMonth(String specifiedDay,int n){
		  Calendar c=Calendar.getInstance();
		  Date date=null;
		  try{
			  date=new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		 }
		  catch(ParseException e){
			  log.error("",e);
		  }
		  c.setTime(date);
		  int month=c.get(Calendar.MONTH);
		  c.set(Calendar.MONTH, month-n);
		  String dayBefore=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		  return dayBefore;
	}
	
	
	/**
	 * hjf 根据毫秒数 返回日期时间
	 * @return
	 */
	public static String getDateTime(String mark,long seconds){
		Date date =new Date(seconds);
		DateFormat df=new SimpleDateFormat(mark);
		String strDate=df.format(date);
		return strDate;
	}
	public static String getYM() {
		return new SimpleDateFormat("yyyyMM", Locale.getDefault()).format(
				new Date()).toString();
	}
	public static String tenToEight(String date) {
		if(date != null && !"".equals(date)){
			return date.replaceAll("-", "");
		}else{
			return date;
		}
	}
	
	
	public static String eightToSix(String date) {
		if(date != null && !"".equals(date)){
			return date.replaceAll(":", "");
		}else{
			return date;
		}
	}
	
	
	public static String eightToTen(String date) {
		if(date != null && !"".equals(date)){
			String year=date.substring(0, 4);
			String month=date.substring(4, 6);
			String day=date.substring(6);
			return year + "-" + month + "-" + day;
		}else{
			return date;
		}
	}
	
	

	
	/**
	 * 获取某日期，格式由参数传入
	 * 
	 * @param reg返回日期的格式
	 * @return
	 */
	public String getDay(String reg, int days) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new java.util.Date());
		cal1.add(Calendar.DATE, days);
		SimpleDateFormat formatter = new SimpleDateFormat(reg);
		String day = formatter.format(cal1.getTime());
		return day;
	}
	
	public static String getNowTime_CH(){
		String date = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		date = sdf.format(new Date(System.currentTimeMillis()));
		return date;
	}
	
	
	/**
	 * 系统时间毫秒转化为字符串时间，格式为:yyyy-MM-dd HH:mm:ss。如2016-12-01 12:12:45
	 * @param datetime
	 * @return
	 */
	public static String getStringByLong(long datetime){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String datestr = df.format(new Date(datetime));
		return datestr;
	}
	

	
	// 取得时间串//"yyyy-MM-dd HH:mm:ss:SSS"
	public static String getTimeString(String strFormat, int ibefore) {
		// "yyyy.MM.dd G 'at' HH:mm:ss z"
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -ibefore);
		java.util.Date date = calendar.getTime();
		SimpleDateFormat sFormat = new SimpleDateFormat(strFormat);
		StringBuffer buftmp = new StringBuffer(10);
		FieldPosition fp = new FieldPosition(0);
		StringBuffer strbuf = sFormat.format(date, buftmp, fp);
		return strbuf.toString();
	}
	
	
	public static String getDate10() {
		try {
			Date date1 = new Date();
			SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd");
			String strDay = fd.format(date1);
			return strDay;
		} catch (Exception e) {
			return "99999999";
		}
	}

	public static String getTime8() {
		try {
			Date date1 = new Date();
			SimpleDateFormat fd = new SimpleDateFormat("HH:mm:ss");
			String strTime = fd.format(date1);
			return strTime;
		} catch (Exception e) {
			return "99999999";
		}
	}
	public static String getTime6() {
		try {
			Date date1 = new Date();
			SimpleDateFormat fd = new SimpleDateFormat("HHmmss");
			String strTime = fd.format(date1);
			return strTime;
		} catch (Exception e) {
			return "99999999";
		}
	}
	public static String getTime19() {
		try {
			Date date1 = new Date();
			SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String strTime = fd.format(date1);
			return strTime;
		} catch (Exception e) {
			return "99999999";
		}
	}
	
	public static String addMinute(String beginDate, int minute) {
		String reStr = "";
		try {
			Date dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.parse(beginDate);
			long lgTime = dt.getTime();
			lgTime += minute * 60 * 1000;
			dt = new Date(lgTime);
			reStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dt);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			log.error("",e);
		}
		return reStr;
	}
	
	/**
	 * 函数:getDateBeforeNDayOrAfter() 输入参数：int n 正的提前取得日期;负的取历史日期; 输出：8位 日期;
	 */

	public static String getDateBeforOrAfter(String date,
			int dayBeforeOrAfter) {
		try {
			Date date1 = convertStringToDate("yyyyMMdd", date);
			long day = date1.getTime();
			long tt = day / (24 * 60 * 60 * 1000);
			long ss = (tt + dayBeforeOrAfter) * 24 * 60 * 60 * 1000;
			Date ssdd = new Date(ss);
			SimpleDateFormat fd = new SimpleDateFormat("yyyyMMdd");
			String strDay = fd.format(ssdd);
			return strDay;
		} catch (Exception e) {
			return null;
		}
	}
	
	/** 
	 * @Title: detailTime 
	 * @Description: 获取当值格式化的时间
	 * @return
	 * @return String
	 */
	public static String detailTime() {
		String date = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date = sdf.format(new Date(System.currentTimeMillis()));
		return date;
	}

	//获取的当前日期推迟或提前指定时间秒数
	public static String  getWantDay(int ss){
		String date = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		date = sdf.format(new Date(System.currentTimeMillis()+ss*1000));
		return date;	
	}
		
	/**
	 * 获取当前时间提前几个月或者未来几个月
	 * @param i
	 * @return
	 */
	public static String getMonth(int i){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, i);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(cal.getTime());
	}



	/**
	 * 当前日期long
	 * @param date
	 * @return
	 */
	public static long getLongTime(String date,String formate_type){
		DateTimeFormatter fmt = DateTimeFormat.forPattern(formate_type);
		return  DateTime.parse(date,fmt).getMillis();
	}

	/**
	 * 格式化字符串获取系统日期
	 * @param formate_type
	 * @return
	 */
	public static String getNewDate(String formate_type){
		return DateTime.now().toString(formate_type);
	}

	/**
	 * 格式化字符串获取指定日期
	 * @param date
	 * @param formate_type
	 * @return
	 */
	public static String getDateFormate(String date,String formate_type){

		DateTimeFormatter fmt = DateTimeFormat.forPattern(formate_type);
		return DateTime.parse(date, fmt).toString();

	}


	public static void main(String[] args) {
		DateUtil.getDateFormate("20170119",FORMATE_DATE_yyyyMMdd);
		DateTimeFormatter format = DateTimeFormat .forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime dateTime = DateTime.parse("2017-01-19 11:32:50", format);

	}

	/**
	 * 判断闰月
	 * @return
	 */
	public static boolean checkLeapMonth(String date){
		DateTime dt4 = new DateTime(date);
		org.joda.time.DateTime.Property month = dt4.monthOfYear();
		return month.isLeap();

	}


	/**
	 * 判断闰年
	 * @return
	 */
	public static boolean checkLeapYear(String date){
		DateTime dt4 = new DateTime(date);
		org.joda.time.DateTime.Property year = dt4.year();
		return year.isLeap();

	}



	/**
	 * 2个日期间隔多少天
	 * @param date
	 * @param edate
	 * @return
	 */
	public static int dayBetween(String date,String edate){
		return Days.daysBetween(DateTime.parse(addDateRow(date)), DateTime.parse(addDateRow(edate))).getDays();
	}



	/**
	 * 当前日期周一日期
	 * @param type
	 * @return
	 */
	public static String firstDayOfWeek(String type){
		return DateTime.now().dayOfWeek().withMinimumValue().toString(type);
	}
	/**
	 * 指定日期周一日期
	 * @param date
	 * @return
	 */
	public static String firstDayOfWeek(String date,String type){
		return DateTime.parse(addDateRow(date)).dayOfWeek().withMinimumValue().toString(type);
	}


	/**
	 * 当前日期周一日期
	 * @param type
	 * @return
	 */
	public static String lastDayOfWeek(String type){
		return DateTime.now().dayOfWeek().withMaximumValue().toString(type);
	}
	/**
	 * 指定日期周一日期
	 * @param date
	 * @return
	 */
	public static String lastDayOfWeek(String date,String type){
		return DateTime.parse(addDateRow(date)).dayOfWeek().withMaximumValue().toString(type);
	}


	/**
	 * 当前日期的月份第一天
	 * @param type
	 * @return
	 */
	public static String firstDayOfMonth(String type){
		return DateTime.now().dayOfMonth().withMinimumValue().toString(type);
	}

	/**
	 * 当前日期的月份第一天
	 * @param type
	 * @return
	 */
	public static String firstDayOfMonth(String date,String type){
		return DateTime.parse(addDateRow(date)).dayOfMonth().withMinimumValue().toString(type);
	}


	/**
	 * 当前日期的月份最后一天
	 * @param type
	 * @return
	 */
	public static String lastDayOfMonth(String type){
		return DateTime.now().dayOfMonth().withMaximumValue().toString(type);
	}

	/**
	 * 当前日期的月份最后一天
	 * @param type
	 * @return
	 */
	public static String lastDayOfMonth(String date,String type){
		return DateTime.parse(addDateRow(date)).dayOfMonth().withMaximumValue().toString(type);
	}



	/**
	 * 当前日期N天前日期
	 *
	 * @param days
	 * @param type
	 * @return
	 */
	public static String minusBeforeDays(int days,String type){
		return DateTime.now().minusDays(days).toString(type);
	}
	/**
	 * 指定日期N天前日期
	 *
	 * @param days
	 * @param type
	 * @return
	 */
	public static String minusBeforeDays(String date,int days,String type){
		return DateTime.parse(addDateRow(date)).minusDays(days).toString(type);
	}

	/**
	 * 当前日期N天后日期
	 *
	 * @param days
	 * @param type
	 * @return
	 */
	public static String minusAfterDays(int days,String type){
		return DateTime.now().plusDays(days).toString(type);
	}
	/**
	 * 指定日期N天后日期
	 *
	 * @param days
	 * @param type
	 * @return
	 */
	public static String minusAfterDays(String date,int days,String type){
		return DateTime.parse(addDateRow(date)).plusDays(days).toString(type);
	}

	/**
	 * 当前时间N小时后日期
	 *
	 * @param hours
	 * @param type
	 * @return
	 */
	public static String minusAfterHours(int hours,String type){
		return 	DateTime.now().minusHours(hours).toString(type);
	}

	/**
	 * 当前时间N分钟前后日期
	 *
	 * @param min
	 * @param type
	 * @return
	 */
	public static String minusAfterMin(int min,String type){
		return 	DateTime.now().minusMinutes(min).toString(type);
	}


	/**
	 * 当前时间N小时后日期
	 *
	 * @param hours
	 * @param type
	 * @return
	 */
	public static String minusAfterHours(String time,int hours,String type){
		return DateTime.parse(addDateRow(time)).minusHours(hours).toString(type);
	}

	/**
	 * 2个日期间隔天数
	 * @param satrtDate
	 * @param endDate
	 * @return
	 */
	public static int dateDaybetween(String satrtDate,String endDate){
		return Days.daysBetween(DateTime.parse(addDateRow(satrtDate)), DateTime.parse(addDateRow(endDate))).getDays();
	}


	/**
	 * @Title: nDayBeforeToday
	 * @Description: 获取当前系统时间前 N天的时间
	 * @param nday
	 * @return
	 * @return String
	 */
	public static String nDayBeforeToday(int nday){
		long now = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date(now-nday*24*60*60*1000));
	}

	//



	/**
	 * @Title: getDate
	 * @Description:获取格式化日期
	 * @param date
	 * @param fromat
	 * @return
	 * @return Date
	 */
	public static Date getDate(String date, String fromat)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(fromat);
		Date returnDate = null;
		try {
			returnDate = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return returnDate;
	}


	/**
	 * 获取日期集合
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getDatesBetweenTwoDate(Date startDate, Date endDate) {
		List<Date> dateList = new ArrayList<Date>();
		List<String> stringList = new ArrayList<String>();
		dateList.add(startDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		while (true) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
			if (endDate.after(cal.getTime())) {
				dateList.add(cal.getTime());
			} else {
				break;
			}
		}
//		dateList.add(endDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		for (Date i : dateList){
			stringList.add(sdf.format(i).toString());
		}
		return stringList;
	}




	public static String generateBatchId(){
		Date date = new Date();
		String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(date);
		String HHmmssSSS = new SimpleDateFormat("HHmmssSSS").format(date);
		SecureRandom random = new SecureRandom();
		return (yyyyMMdd + HHmmssSSS + random.nextInt(10) + random.nextInt(10) + random.nextInt(10));
	}

	/**
	 * @Title: theDayBeforeToday
	 * @Description:  获取当前系统时间前N天的时间
	 * @param nday
	 * @return
	 * @return long
	 */
	public static long theDayBeforeToday(int nday){
		return System.currentTimeMillis()-nday*24*60*60*1000;
	}

	public static int theDaysBeforeToday(String simpleDate){
		int days=0;
		String day = simpleDate.trim().substring(6, 8);
		String month = simpleDate.trim().substring(4, 6);
		String year = simpleDate.trim().substring(0, 4);
		Calendar calendar = new GregorianCalendar(new Integer(year), new Integer(month)-1, new Integer(day));
		long millis = calendar.getTimeInMillis();
		long rangMillis = System.currentTimeMillis()-millis;
		days = ((Long)(rangMillis/86400000)).intValue();
		return days;
	}

	/**
	 * 获取某日期，格式由参数传入 剔除节假日时间的计算
	 */
	public static String getDayNotIncludeHoliday(String reg, int days, String holiday, boolean type) {
		String returnDate = "";
		try{
			if(type){
				String jjrDate = holiday;
				//节假日过滤
				if(jjrDate != null){
					returnDate = jjrDate;
				}else{
					returnDate = SkipWeekend(reg, days);
				}
			}else{
				returnDate = SkipWeekend(reg, days);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			returnDate = SkipWeekend(reg, days);
		}
		return returnDate;
	}
	/**
	 * 跳过周末获取工作日
	 * @param format
	 * @param days
	 * @return
	 */
	public static String SkipWeekend(String format, Integer days) {
		String dataString = null;
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.setFirstDayOfWeek(1);
		for (int i = 1; i <= days; i++) {
			calendar.add(Calendar.DATE, 1);
			if(calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7) {
				calendar.add(Calendar.DATE, 1);
				if(calendar.get(Calendar.DAY_OF_WEEK) == 1 || calendar.get(Calendar.DAY_OF_WEEK) == 7) {
					calendar.add(Calendar.DATE, 1);
				}
			}
		}
		dataString = sdf.format(calendar.getTime());
		return dataString;
	}

	/**
	 * 校验日期是否合法
	 * @param str
	 * @return
	 */
	public static boolean isDate(String str) {
		boolean result = true;

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		try {
			format.setLenient(false);
			format.parse(str);
		}catch (Exception e){
			result = false;
		}
		return result;

	}

}
