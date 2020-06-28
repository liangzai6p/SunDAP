package com.sunyard.ars.common.comm;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sunyard.aos.common.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunyard.cop.IF.mybatis.pojo.SysParameter;

public class HttpClient {
	
	private static Logger logger = LoggerFactory.getLogger(HttpClient.class);
	// 连接超时，单位毫秒
	private static int CONNECT_TIME_OUT = 10000;
	// 读取超时，单位毫秒
	private static int READ_TIME_OUT = 10000;
	// 通信编码
	public static String CHAR_SET = ARSConstants.ENCODE;
	
	public static String sendPost(String url, String param) throws Exception {
		try {
			SysParameter paramConnTime = ARSConstants.SYSTEM_PARAMETER.get("HTTP_CONN_TIME_OUT");
			SysParameter paramReadTime = ARSConstants.SYSTEM_PARAMETER.get("HTTP_READ_TIME_OUT");
			if(paramConnTime != null) {
				CONNECT_TIME_OUT = Integer.parseInt(paramConnTime.getParamValue().trim());
			}
			if(paramReadTime != null) {
				READ_TIME_OUT = Integer.parseInt(paramReadTime.getParamValue().trim());
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("设置HTTP超时时间失败！", e);
		}
		
		String result = "";
		PrintWriter out = null;
		OutputStreamWriter or=null;
		InputStreamReader ir=null;
		BufferedReader in = null;
		InputStream is =null;
		OutputStream os =null;
		try {
			URL httpUrl = new URL(url);
			// 获取URL连接
			HttpURLConnection httpConn = (HttpURLConnection) httpUrl.openConnection();
			// 设置通用的请求属性
			httpConn.setRequestProperty("accept", "*/*");
			httpConn.setRequestProperty("CHAR_SET", CHAR_SET);
			httpConn.setRequestProperty("connection", "Keep-Alive");
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConn.setConnectTimeout(CONNECT_TIME_OUT);
			httpConn.setReadTimeout(READ_TIME_OUT);
			httpConn.setRequestMethod("POST");
			
			// 发送POST请求必须设置如下两行
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			// POST请求不能使用缓存
			httpConn.setUseCaches(false);
			// 建立URL连接
			httpConn.connect();
			// 获取URLConnection对象对应的输出流
			os = httpConn.getOutputStream();
			or = new OutputStreamWriter(os,CHAR_SET);
			out = new PrintWriter(or);
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应，请求异常时读取错误信息
			if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				logger.error("http请求异常");
				is = httpConn.getErrorStream();
				ir = new InputStreamReader(is, CHAR_SET);
				in = new BufferedReader(ir);
			} else {
				is = httpConn.getInputStream();
				ir = new InputStreamReader(is, CHAR_SET);
				in = new BufferedReader(ir);
			}
			// 读取返回信息
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			result = httpConn.getResponseCode() + ",@," + result;
		} finally {
			// 关闭输出流、输入流
			FileUtil.safeClose(is,os);
			FileUtil.safeClose(in,out);
			FileUtil.safeClose(ir,or);

		}
		return result;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
