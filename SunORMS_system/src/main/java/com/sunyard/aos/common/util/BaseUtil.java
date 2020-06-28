package com.sunyard.aos.common.util;

import java.io.BufferedReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.cloud.util.JwtUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.rowset.CachedRowSetImpl;
import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.dao.BaseDao;
import com.sunyard.cop.IF.common.SunIFConstant;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.pojo.User;

import io.jsonwebtoken.Claims;

/**
 * @author:		 lewe
 * @date:		 2017年2月24日 下午07:03:28
 * @description: TODO(基础工具类)
 */
@SuppressWarnings("restriction")
public class BaseUtil {
	
	/** 日志记录器 */
	private static Logger logger = LoggerFactory.getLogger(BaseUtil.class);
	
	/** 初始编号 */
	private static long startVaue = 0;
	
	/** Json转换操作对象 */
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * json类型字符串转化为指定类型
	 * @param str
	 * @param objClass
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object transJson2Obj(String str, Class objClass) {
		Object retObj = new Object();
		try {
			retObj = objectMapper.readValue(str, objClass);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return retObj;
	}
	 /**
     * 获取当前登录用户信息
     * @return
     * @throws Exception
     */

//	public static User getLoginUserForNoLogger() throws Exception {
//		User user = null;
//		try {
//			HttpSession session = RequestUtil.getRequest().getSession();
//			user  = (User) session.getAttribute("user");
//		} catch (Exception e) {
////			logger.error("获取登录用户对象出错！", e);
//		}
//		if (user == null) {
//			user = new User();
//			user.setProjectNo("AOS");
//			user.setSystemNo("AOS");
//			user.setBankNo("SUNYARD");
//		}
//		return user;
//	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:43:14
	 * @description: TODO(对指定字符串判空)
	 */
	public static boolean isBlank(String str) {
		if (str == null || str.trim().equals("")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 转换空值，为null时返回空字符串，其他情况原值去空格返回
	 * 
	 * @author:	lewe
	 * @date:	2017年12月7日 下午3:34:37
	 */
	public static String convetNullValue(String str) {
		if (isBlank(str)) {
			return "";
		}
		return str.trim();
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:43:25
	 * @description: TODO(按逗号分割字符串str，给每一项前后加上英文单引号，再返回用逗号拼接后的字符串)
	 */
	public static String addSingleQuote(String str) {
		if (isBlank(str)) {
			return "''";
		}
		String[] arr = str.split("\\,");
		String result = "'" + arr[0] + "'";
		for (int i=1; i<arr.length; i++) {
			result = result + ",'" + arr[i] + "'";
		}
		return result;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:43:40
	 * @description: TODO(去除字符串str中的所有英文单引号)
	 */
	public static String deleteSingleQuote(String str) {
		return str.replaceAll("'", "");
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:45:58
	 * @description: TODO(获取rs中指定名称的值，并非Null化（即如果获得的结果为Null，返回“”空字符串）)
	 */
	public static String getStringByName(ResultSet rs, String name) throws SQLException {
		String str = rs.getString(name);
		return str == null ? "" : str.trim();
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:46:18
	 * @description: TODO(获取rs中指定索引的值，并非Null化（即如果获得的结果为Null，返回“”空字符串）)
	 */
	public static String getStringByIndex(ResultSet rs, int index) throws SQLException {
		String str = rs.getString(index);
		return str == null ? "" : str.trim();
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:46:29
	 * @description: TODO(判断是否是单列查询)
	 */
	public static boolean isSingleColumnSelecte(String sql) {
		int fromIndex = sql.toLowerCase().indexOf("from");
		String temp = sql.substring(0, fromIndex);
		if (temp.indexOf(",") > 0) { // 有点片面
			return false;
		}
		return true;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:46:49
	 * @description: TODO(根据不同的类型返回不同的对象)
	 */
	@SuppressWarnings("deprecation")
	public static Object getValue(String type, String value) {
		if (type.equalsIgnoreCase("string")) {
			return value;
		} else if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("integer")) {
			return Integer.parseInt(value);
		} else if (type.equalsIgnoreCase("float")) {
			return Float.parseFloat(value);
		} else if (type.equalsIgnoreCase("double")) {
			return Double.parseDouble(value);
		} else if (type.equalsIgnoreCase("boolean")) {
			return value.equalsIgnoreCase("true") ? true : false;
		} else if (type.equalsIgnoreCase("short")) {
			return Short.parseShort(value);
		} else if (type.equalsIgnoreCase("long")) {
			return Long.parseLong(value);
		} else if (type.equalsIgnoreCase("byte")) {
			return Byte.parseByte(value);
		} else if (type.equalsIgnoreCase("date")) {
			return Date.parse(value);
		} else if (type.equalsIgnoreCase("clob")) {
			return (Clob)((Object)value);
		} else if (type.equalsIgnoreCase("timestamp")) {
			return Timestamp.parse(value);
		}
		return value;
	}
	
	/**
	 * @author:		lewe
	 * @date:		2017年2月24日 下午07:47:05
	 * @description:TODO(把RS的一条记录组成Map，key为小写)
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void createRSMap(HashMap map, CachedRowSetImpl rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		for (int i = 1; i <= md.getColumnCount(); i++) {
			String colname = md.getColumnName(i).toLowerCase();
			Object colvalue = rs.getObject(i);
			if (colvalue == null) {
				colvalue = "";
			} else if (md.getColumnType(i) == Types.CLOB) {
				colvalue = clob2String(colvalue);
			}
			map.put(colname, colvalue);
		}
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:48:46
	 * @description: TODO(把RS的一条记录组成Map，key为小写)
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void createRSMap(HashMap map, ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		for (int i = 1; i <= md.getColumnCount(); i++) {
			String colname = md.getColumnName(i).toLowerCase();
			Object colvalue = rs.getObject(i);
			if (colvalue == null) {
				int colType = md.getColumnType(i);
				if (colType == Types.INTEGER || colType == Types.NUMERIC 
						|| colType == Types.DECIMAL || colType == Types.DOUBLE
						|| colType == Types.FLOAT) {
					colvalue = 0;
				} else {
					colvalue = "";
				}
			} else if (md.getColumnType(i) == Types.CLOB) {
				colvalue = clob2String(colvalue);
			}
			map.put(colname, colvalue);
		}
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:51:57
	 * @description: TODO(转换Clob为字符串)
	 */
	public static String clob2String(Object cbObj) throws SQLException{
		String retStr = "";
		Clob cb = (Clob)cbObj;
	/*	try {
			Reader in = cb.getCharacterStream();
			BufferedReader br = new BufferedReader(in);
			String s = br.readLine();
			StringBuffer sb = new StringBuffer();
			while (s != null) {
				sb.append(s);
				s = br.readLine();
			}
			retStr = sb.toString();
		} catch (Exception e) {
			logger.error("转换CLOB出错");
		}
		return retStr;*/

		return  cb.getSubString(1,(int)(cb.length()-1));
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:50:57
	 * @description: TODO(获得指定的年月份包含的天数（区分闰年），如"201210"返回的天数为31)
	 */
	public static int getDayNumOfMonth(String yearMonth) {
		int daysNum = 30;
		if (yearMonth == null || yearMonth.equals("") || yearMonth.length() != 6) {
			return daysNum;
		}
		String month = Integer.valueOf(yearMonth.substring(4)).toString();
		if (",1,3,5,7,8,10,12,".indexOf("," + month + ",") != -1) {
			daysNum = 31;
			
		} else if ("2".equals(month)) {
			int year = Integer.valueOf(yearMonth.substring(0,4));
			if ((year%4 == 0 && year%100 != 0) || year%400 == 0) {
				daysNum = 29;
			} else {
				daysNum = 28;
			}
		}
		return daysNum;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:51:09
	 * @description: TODO(获取一个流水号)
	 */
	public static synchronized String getSerialNumber() {
		if (startVaue >= 999) {
			startVaue = 0;
		}
		startVaue ++;
		startVaue = startVaue % 1000;
		DecimalFormat df = new DecimalFormat("000");
		String sStartVaue = df.format(startVaue);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date today = new Date();
		String sDate = sdf.format(today);
		
		return sDate + sStartVaue;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:51:20
	 * @description: TODO(获取指定操作标识对应的中文描述)
	 */
	public static String getOperateDesc(String opeType) {
		String operateDesc = "";
		
		if (opeType.equals(AOSConstants.OPERATE_ADD)) {
			operateDesc = "新增";
		} else if (opeType.equals(AOSConstants.OPERATE_DELETE)) {
			operateDesc = "删除";
		} else if (opeType.equals(AOSConstants.OPERATE_MODIFY)) {
			operateDesc = "修改";
		} else if (opeType.equals(AOSConstants.OPERATE_QUERY)) {
			operateDesc = "查询";
		}
		
		return operateDesc;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:51:34
	 * @description: TODO(获取当前时间字符串，格式为 yyyyMMddHHmmss)
	 */
	public static String getCurrentTimeStr() {
		SimpleDateFormat format = new SimpleDateFormat(AOSConstants.FORMAT_DATE_TIME);
		return format.format(new Date());
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月8日 下午07:51:34
	 * @description: TODO(获取当前日期字符串，格式为 yyyyMMdd)
	 */
	public static String getCurrentDateStr() {
		SimpleDateFormat format = new SimpleDateFormat(AOSConstants.FORMAT_DATE);
		return format.format(new Date());
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月9日 下午5:21:06
	 * @description: TODO(获取当前日期指定格式的字符串)
	 */
	public static String getCurrentDateByFormat(String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(new Date());
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:52:35
	 * @description: TODO(转换指定对象为Json字符串)
	 */
	public static String transObj2Json(Object object) {
		String retJson = "";
		try {
			retJson = objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), e);
		}
		return retJson;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年6月18日 上午10:29:02
	 * @description: TODO(格式化json，便于输出日志查看)
	 */
	public static String formatJson(String jsonStr) {
		if (isBlank(jsonStr)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		char last = '\0';
		char current = '\0';
		int indent = 0;
		boolean isInQuotationMarks = false;
		for (int i = 0; i < jsonStr.length(); i++) {
			last = current;
			current = jsonStr.charAt(i);
			switch (current) {
				case '"':
	                if (last != '\\') {
	                	isInQuotationMarks = !isInQuotationMarks;
	                }
	                sb.append(current);
	                break;
				case '{':
				case '[':
					sb.append(current);
					if (!isInQuotationMarks) {
						sb.append('\n');
						indent++;
						addIndentBlank(sb, indent);
					}
					break;
				case '}':
				case ']':
					if (!isInQuotationMarks) {
						sb.append('\n');
						indent--;
						addIndentBlank(sb, indent);
					}
					sb.append(current);
					break;
				case ',':
					sb.append(current);
					if (last != '\\' && !isInQuotationMarks) {
						sb.append('\n');
						addIndentBlank(sb, indent);
					}
					break;
				default:
					sb.append(current);
			}
		}
		return sb.toString();
	}

	/**
	 * @author:		 lewe
	 * @date:		 2017年6月18日 上午10:32:27
	 * @description: TODO(添加缩进)
	 */
	private static void addIndentBlank(StringBuilder sb, int indent) {
		for (int i = 0; i < indent; i++) {
			sb.append('\t');
		}
	}
	
	/**
	 * @author:		zheng.jw
	 * @date:		2017年8月31日 上午9:31:19
	 * @Description:传入文件路径，返回文件名
	 */
	public static String getAttachmentName(String path) {
		int index = path.lastIndexOf("\\");
		if (index == -1) {
			index = path.lastIndexOf("/");
		}
		String name = path.substring(index);
		index = name.indexOf("_");
		name = name.substring(index + 1);
		return name;
	}
	
	/**
	 * 获取本地服务器IP
	 * 
	 * @author:	lewe
	 * @date:	2017年12月6日 下午4:35:49
	 */
	public static String getHostAddress() throws Exception {
		return InetAddress.getLocalHost().getHostAddress();
	}
	
	/**
	 * 获取本地服务器的所有IP
	 * 
	 * @author:	lewe
	 * @date:	2017年12月6日 下午4:35:38
	 */
	public static List<String> getHostAllAddress() throws Exception {
		List<String> ipList = new ArrayList<String>();
		Enumeration<NetworkInterface> netInterfaces = null;
        netInterfaces = NetworkInterface.getNetworkInterfaces();
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> ips = ni.getInetAddresses();
            while (ips.hasMoreElements()) {
                ipList.add(ips.nextElement().getHostAddress());     
            }
        }
        return ipList;
	}
	
	/** 
     * 获取指定时间范围内的所有日期 
     *  
     * @param startDate 
     *            (format="20160601") 
     * @param endDate 
     *            (format="20160712") 
     * @return 
     * @throws ParseException 
     */  
	public static List<String> getDaysBetween(String startDate, String endDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        List<String> result = new ArrayList<String>();
        result.add(startDate);
        try {
            Calendar calBegin = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间 
            Date begin = format.parse(startDate);
            calBegin.setTime(begin);
            Calendar calEnd = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间 
            Date end = format.parse(endDate);
            calEnd.setTime(end);
            // 测试此日期是否在指定日期之后 
            while (end.after(calBegin.getTime())) {
                // 根据日历的规则，为给定的日历字段添加或减去指定的时间量 
                calBegin.add(Calendar.DAY_OF_MONTH, 1);
                result.add(format.format(calBegin.getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	
	/**
     * 判断map是否为空（对象为空，或值全为空）
     * 
     * @author:	kuir
     * @date:	2017年9月30日 下午4:45:10
     * 
     * @param	map	map数据源
     * @return  boolean
     * @throws Exception 
     */
	public static boolean mapIsAllNull(Map<String, String> map) throws Exception {
    	 // 容量大时表现优秀
        for (Map.Entry<String, String> entry : map.entrySet()) {
        	if (!BaseUtil.isBlank(entry.getValue())){
        		return false;
        	}
        }
		return true;
    }
	
    /**
	 * 获取当前登录用户信息
	 * 
	 * @author:	lewe
	 * @date:	2017年12月6日 下午5:26:55
	 */
	public static User getLoginUser() {
		User user = null;
		try {/*
			HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
			//HttpSession session = RequestUtil.getRequest().getSession();
			user  = (User) session.getAttribute("user");*/
			
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			String token = request.getHeader("Authorization");
			String key = ARSConstants.SYSTEM_PARAMETER.get("TOKEN_KEY").getParamValue();
			Claims claims = JwtUtil.checkToken(token, key).getClaims();
			ObjectMapper objectMapper = new ObjectMapper();
			user = objectMapper.readValue(claims.getSubject(), User.class);
		} catch (Exception e) {
			logger.error("获取登录用户对象出错！", e);
		}
		if (user == null) {
			user = new User();
			user.setProjectNo("AOS");
			user.setSystemNo("AOS");
			user.setBankNo("SUNYARD");
		}
		return user;
	}
	
	/**
	 * 获取两个日期的间隔天数
	 * 
	 * @author:	lewe
	 * @date:	2017年12月29日 下午5:11:43
	 */
	public static int getDiffDays(Date date1, Date date2) {
		int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
		return Math.abs(days);
	}
	
	/**
	 * 获取系统参数
	 * 
	 * @author:	lewe
	 * @date:	2018年1月3日 下午2:21:27
	 */
	public static String getSysParamValue(BaseDao baseDao, String param_item) throws Exception {
		String param_value = SunIFConstant.PARAM_MAP.get(param_item);
		if (param_value == null) {
			// 缓存中无该系统参数时，从数据库查询
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("param_item", param_item);
			User user = getLoginUser();
			map.put("bank_no", user == null ? "SUNYARD" : user.getBankNo());
			map.put("system_no", user == null ? "AOS" : user.getSystemNo());
			map.put("project_no", user == null ? "AOS" : user.getProjectNo());
			param_value = baseDao.getSysParamValue(map);
			logger.debug("查询系统参数 " + param_item + " 值为：" + param_value);
			SunIFConstant.PARAM_MAP.put(param_item, param_value);
		}
		return param_value;
	}
	
	/**
	 * 批量转换list中map键值，默认key为小写，指定类型value赋予默认值
	 * 
	 * @author:	lewe
	 * @date:	2018年1月3日 下午6:32:03
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List convertListMapKeyValue(List list) throws SQLException{
		for (int i = 0; i < list.size(); i++) {
			list.set(i, convertMapKeyValue((Map) list.get(i)));
		}
		return list;
	}
	
	/**
	 * 转换map键值，默认key为小写，指定类型value赋予默认值
	 * 
	 * @author:	lewe
	 * @date:	2018年1月3日 下午6:32:15
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> convertMapKeyValue(Map map) throws SQLException{
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (map == null || map.isEmpty()) {
			return retMap;
		}
		for (Object key : map.keySet()) {
			String newKey = (key + "").toLowerCase();
			Object newValue = map.get(key);
			if (newValue == null) {
				newValue = "";
			} else if (newValue instanceof Clob) {
				newValue = clob2String(newValue);
			}
			retMap.put(newKey, newValue);
		}
		return retMap;
	}
	
	/**
	 * 设置session属性
	 * @param key
	 * @param value
	 */
	public static void setSessionAttribute(String key, Object value) {
		HttpSession session = RequestUtil.getRequest().getSession();
		session.setAttribute(key, value);
	}
	
	/**
	 * 获取session属性
	 * @param key
	 * @return
	 */
	public static Object getSessionAttribute(String key) {
		HttpSession session = RequestUtil.getRequest().getSession();
		return session.getAttribute(key);
	}

	public static String filterJson(String header)throws Exception{
		if(header==null){
			return header;
		}
		String regex = "[`~!@#$%^&*()\\+\\=\\{}|\"?><【】\\/r\\/n]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(header);
		if(matcher.find()){
			throw new Exception("存在特殊字符"+matcher.group()+"，操作无法正常进行");
//			header = matcher.replaceAll("");
		}
		return header;
	}


	public static String filterHeader(String header)throws Exception{
		if(header==null){
			return header;
		}
		String regex = ARSConstants.REGEX_HEADER;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(header);
		if(matcher.find()){
			throw new Exception("存在特殊字符"+matcher.group()+"，操作无法正常进行");
//			header = matcher.replaceAll("");
		}
		return header;
	}


	public static String filterLog(String header)throws Exception{
		if(header==null){
			return header;
		}
		String regex = ARSConstants.REGEX_LOG;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(header);
		if(matcher.find()){
//			throw new Exception("存在异常字符，操作无法正常进行");
			header = matcher.replaceAll("");
		}
		return header;
	}

	public static String filterSplit(String splitChar)throws Exception{
		if(splitChar==null){
			return splitChar;
		}
		String regex = ARSConstants.REGEX_SPLIT;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(splitChar);
		if(matcher.find()){
//			splitChar = matcher.replaceAll("");
			throw new Exception("存在特殊字符"+matcher.group()+"，操作无法正常进行");
		}
		return splitChar;
	}

	public static String filterSqlParam(String header)throws Exception{
		if(header==null){
			return header;
		}
		String regex = ARSConstants.REGEX_SQLPARAM;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(header);
		if(matcher.find()){
			throw new Exception("存在特殊字符"+matcher.group()+"，操作无法正常进行");
//			header = matcher.replaceAll("");
		}
		return header;
	}




}
