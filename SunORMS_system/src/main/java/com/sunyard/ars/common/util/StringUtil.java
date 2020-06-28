package com.sunyard.ars.common.util;

import javax.servlet.http.HttpServletRequest;

public class StringUtil {
	/**
	 * 获取客户端IP
	 * @param request
	 * @return
	 */
	public static String getIP(HttpServletRequest request) throws Exception{
		String loginIp = request.getHeader("X-Forwarded-For");
		if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) {
			loginIp = request.getHeader("Proxy-Client-IP");
		}
		if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) {
			loginIp = request.getHeader("WL-Proxy-Client-IP");
		}
		if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp)) {
			loginIp = request.getRemoteAddr();
		}
		if (loginIp == null || loginIp.length() == 0 || "unknown".equalsIgnoreCase(loginIp ) || "127.0.0.1".equals(loginIp) || "0:0:0:0:0:0:0:1".equals(loginIp)) {
			
			java.net.InetAddress addr = null;
			addr = java.net.InetAddress.getLocalHost();
			loginIp = addr.getHostAddress();// 获得本机IP
			
		}
		return loginIp;
	}
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean checkNull(String str){
		if(null==str || "".equals(str.trim()) || "null".equals(str.trim()))
			return true;
		return false;
	}
}
