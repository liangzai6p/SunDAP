package com.sunyard.dap.intilligentSchedual.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.web.context.ContextLoader;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author:		 lewe
 * @date:		 2017年4月6日 下午5:25:07
 * @description: TODO(HTTP操作工具类)
 */
public class HttpUtil {
	
	// 日志记录器
	protected static Log log = LogFactory.getLog(HttpUtil.class);
	// 连接超时，单位毫秒
	public static int CONNECT_TIME_OUT = 10000;
	// 读取超时，单位毫秒
	public static int READ_TIME_OUT = 10000;
	// 通信编码
	public static String CHAR_SET = "UTF-8";
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年6月18日 下午5:19:13
	 * @description: TODO( 发送 HTTP POST 请求 )
	 * 
	 * @param url	请求地址
	 * @param param	请求参数（格式为 xx=xx&yy=yy ，多个参数以&隔开）
	 * 
	 * @return 请求结果
	 */
	public static String sendPost(String url, String param) throws Exception {
		String result = "";
		PrintWriter out = null;
		BufferedReader in = null;
		OutputStreamWriter ops = null;
		InputStreamReader isr = null;
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
			ops = new OutputStreamWriter(httpConn.getOutputStream(),CHAR_SET);
			out = new PrintWriter(ops);
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应，请求异常时读取错误信息
			if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				log.error("http请求异常");
				isr = new InputStreamReader(httpConn.getErrorStream(), CHAR_SET);
				in = new BufferedReader(isr);
			} else {
				isr = new InputStreamReader(httpConn.getInputStream(), CHAR_SET);
				in = new BufferedReader(isr);
			}
			// 读取返回信息
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			result = httpConn.getResponseCode() + "，@，" + result;
		} finally {
			// 关闭输出流、输入流
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
				if (ops != null) {
					//ops.flush();
					ops.close();
				}
				if (isr != null) {
					isr.close();
				}
			} catch (IOException ex) {
				log.error("关闭http输入、输出流异常",ex);
			}
		}
		return result;
	}
	
	/**
	 * @author:		zheng.jw
	 * @date:		2017年8月14日 下午2:07:02
	 * @description: TODO( 新开线程 - 发送 HTTP POST 请求 )
	 * 
	 * @param url	请求地址
	 * @param param	请求参数（格式为 xx=xx&yy=yy ，多个参数以&隔开）
	 * 
	 * 无返回值
	 */
	public static void sendPostThread(String url, String param) throws Exception {
		log.debug("sendPostThread start 请求地址");
		final String _url = url;
		final String _param = param;
		
		log.debug("sendPostThread url:"+url);
		log.debug("sendPostThread param:"+param);
		
		// 新开一个线程，执行http请求
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HttpUtil.sendPost(_url, _param);
				} catch (Exception e) {
					log.error("HTTP服务器通讯异常，url：" + _url + " ，" ,e);
				}
			}
		}).start();
		log.debug("sendPostThread end");
	}
	/**
	 * @author:		 kuir.chen
	 * @throws： 		 Exception 
	 * @date:		 2017年10月19日 下午16:16:21
	 * @description: TODO(对在线用户发送即时信息)
	 * @param list	  每个对象中必须包含 user_no、login_pc_server、login_mobile_server（用于构建发送对象地址）
	 * @param data	  必须包含title（弹窗标题）、initType、user_name（发布人名）、pub_time(发布时间)及其他初始化弹窗信息
	 /* @param data.initType 不同的弹框对应不同的 initType  计划任务："task"  公告  :"notice"
	 * @param msg_type 消息类型：强制登出、系统消息、提示消息 等
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void sendToDoMessage(List list, Map data, String msg_type) throws Exception {
		if (BaseUtil.isBlank(msg_type)) {
			msg_type = AOSConstants.MSG_TYPE_MESSAGE;
		}
		for (Object obj : list) {
			 //构建发送信息
			 HashMap map = (HashMap) obj;
			 String user_no = map.get("user_no").toString();
			 String login_pc_server = map.get("login_pc_server").toString();
			 String login_mobile_server = map.get("login_mobile_server").toString();
			//返回信息
			Map<String, Object> sysMap =  new HashMap<String, Object>();
			sysMap.put("oper_type", msg_type);
			sysMap.put("user_no", user_no);
			sysMap.put("data", data);
			Map<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put("parameterList", new ArrayList());
			if (!BaseUtil.isBlank(login_pc_server) ) {
				// 表明当前用户已在其他服务器登录，需发送http请求到对应服务器，再通过websocket推送消息到前台提醒.
				String url = "http://" + login_pc_server + "/aosMain.do";
				log.debug("pc端消息推送URL：" + url + "user_no" + user_no);
				sysMap.put("login_terminal", AOSConstants.LOGIN_TERMINAL_PC);
				msgMap.put("sysMap", sysMap);
				ObjectMapper mapper = new ObjectMapper();
				String msg = mapper.writeValueAsString(msgMap);
				log.debug("pc端消息推送内容：" + BaseUtil.formatJson(msg));
				HttpUtil.sendPostThread(url, "message=" + URLEncoder.encode(msg, CHAR_SET));
			}
			if (!BaseUtil.isBlank(login_mobile_server) ) {
				// 表明当前用户已在其他服务器登录，需发送http请求到对应服务器，再通过websocket推送消息到前台提醒.
				String url = "http://" + login_mobile_server + "/aosMain.do";
				log.debug("移动端消息推送URL：" + url);
				sysMap.put("login_terminal", AOSConstants.LOGIN_TERMINAL_MOBILE);
				msgMap.put("sysMap", sysMap);
				ObjectMapper mapper = new ObjectMapper();
				String msg = mapper.writeValueAsString(msgMap);
				log.debug("移动端消息推送内容：" + BaseUtil.formatJson(msg));
				HttpUtil.sendPostThread(url, "message=" + URLEncoder.encode(msg, CHAR_SET));
			}
		}
	}
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午5:29:28
	 * @description: TODO(获取指定文件夹名称（目录）的绝对路径，位于当前应用程序下，与WEB-INF同级)
	 */
	public static String getAbsolutePath(String name) {
		String rootPath = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");
		// modify by wj 2019-03-25(解决weblogic下部署war获取不到文件路径)
		 if(rootPath == null){
	            //如果返回为空，则表示服务器为weblogic，则需要使用另外的方法
	            try{
	            	rootPath = ContextLoader.getCurrentWebApplicationContext().getServletContext().getResource("/").getFile();
	            }catch(MalformedURLException e){
	                log.error("获取项目的根目录出错！", e);
	            }
	        }
		if (rootPath.indexOf("/") != rootPath.length() - 1 && rootPath.indexOf("\\") != rootPath.length() - 1) {
			rootPath += "/";
		}
		return rootPath + name;
	}
	
	/**
	 * @author:		 lewe
	 * @throws MalformedURLException 
	 * @date:		 2017年4月6日 下午5:31:38
	 * @description: TODO(获取默认的临时文件存储路径)
	 */
	public static String getAbsoluteTempPath() {
		return getAbsolutePath("temp");
	}

	
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		String url = "http://localhost:8080/SunAOS_TSHAN/aosMain.do";
//		String url = "http://localhost:8080/SunAOS/irpMain.do";
        //String msg = "{\"parameterList\": [{\"user_no\": \"admin\", \"password\": \"WgmuiVeZRbergKncCPZvqg==\", \"login_terminal\": \"PC\", \"login_type\":\"0\"}],\"sysMap\": {\"oper_type\" : \"0\"}}";
		String msg = "";
		try {
			List parameterList = new ArrayList();
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			parameterMap.put("user_no", "admin");
			parameterMap.put("password", "WgmuiVeZRbergKncCPZvqg==");
			parameterMap.put("login_terminal", "PC");
			parameterMap.put("login_type", "0");
			parameterList.add(parameterMap);
			
			Map<String, Object> sysMap = new HashMap<String, Object>();
//			sysMap.put("oper_type", AOSConstants.MSG_TYPE_LOGOUT);
//			sysMap.put("user_no", "admin");
//			sysMap.put("login_terminal", "PC");
			
			sysMap.put("oper_type", AOSConstants.OPERATE_ADD);
			sysMap.put("chan_id", "001");
			sysMap.put("organ_no", "000001");
			sysMap.put("task_no", "123456789");
			sysMap.put("doc_id", "555666888");
			
			Map<String, Object> msgMap = new HashMap<String, Object>();
			msgMap.put("parameterList", parameterList);
//			msgMap.put("sysMap", sysMap);
			msgMap.put("sysMap", parameterMap);
			
			ObjectMapper mapper = new ObjectMapper();
			msg = mapper.writeValueAsString(msgMap);
			
			System.out.println("发送数据：" + BaseUtil.formatJson(msg));
			System.out.println("接收数据：" + BaseUtil.formatJson(sendPost(url + "?message=" + msg, "")));
			System.out.println("当前服务器IP：" + BaseUtil.getHostAddress());
			System.out.println("当前服务器所有IP：" + BaseUtil.getHostAllAddress());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
}
