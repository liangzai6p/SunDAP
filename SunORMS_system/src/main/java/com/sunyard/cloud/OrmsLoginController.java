package com.sunyard.cloud;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.cloud.util.JwtUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.controller.support.LoginController;
import com.sunyard.cop.IF.model.support.ILoginService;
import com.sunyard.cop.IF.mybatis.pojo.User;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;

@Controller
public class OrmsLoginController {
	
	@Resource
	private ILoginService loginService;
	protected Logger log = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping(value = { "/ormsLoginController.do" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST }, produces = { "text/html;charset=UTF-8" })
	@ResponseBody
	@ArchivesLog(operationType = "查询操作", operationName = "登陆验证")
	public String loginDo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return excuteRequest(request, response, User.class);
	}
	
	protected ResponseBean doAction(HttpServletRequest req, HttpServletResponse response, RequestBean requestBean, ResponseBean responseBean) {
		try {
			responseBean = this.loginService.userService(requestBean);
			User user = (User) responseBean.getRetMap().get("loginUser");
			if (user != null) {
				String key = ARSConstants.SYSTEM_PARAMETER.get("TOKEN_KEY").getParamValue();
				ObjectMapper objectMapper = new ObjectMapper();
				String ip = req.getRemoteAddr();
				Map<String, Object> userMap = new HashMap<String, Object>();
				userMap.put("userNo", user.getUserNo());
				userMap.put("userName", user.getUserName());
				userMap.put("organNo", user.getOrganNo());
				userMap.put("roleNo", user.getRoleNo());
				userMap.put("bankNo", user.getBankNo());
				userMap.put("systemNo", user.getSystemNo());
				userMap.put("projectNo", user.getProjectNo());
				String jws = JwtUtil.createJWT(key, 30, ip, user.getUserNo(), user.getOrganNo(),  objectMapper.writeValueAsString(userMap));
				response.setHeader("Authorization", jws);
			}
		} catch (Exception e) {
			responseBean.setRetCode("IF0002");
			responseBean.setRetMsg("未知异常");
			this.log.error("登录方法出错  " + e.getMessage(), e);
		}
		return responseBean;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected RequestBean beforeAction(HttpServletRequest request, Class paramClass, RequestBean requestBean)
			throws Exception {
		String requestJsonStr = request.getParameter("message");
		this.log.info("前台发送数据:  " + requestJsonStr);
		ObjectMapper objectMapper = new ObjectMapper();
		requestBean = (RequestBean) objectMapper.readValue(requestJsonStr, RequestBean.class);

		List tempLists = requestBean.getParameterList();
		if (!"[]".equals(String.valueOf(tempLists))) {
			List requestList = new ArrayList();
			int i = 0;
			for (int n = tempLists.size(); i < n; i++) {
				requestList.add(mapToEntity(tempLists.get(i), paramClass));
			}
			requestBean.setParameterList(requestList);
		}

		return requestBean;
	}

	@SuppressWarnings("rawtypes")
	public String excuteRequest(HttpServletRequest req, HttpServletResponse response, Class paramClass) {
		String responseJsonStr = "";
		RequestBean requestBean = new RequestBean();
		ResponseBean responseBean = new ResponseBean();
		try {
			HttpSession session = req.getSession();
			if (session.getAttribute("user") != null) {
				User user = (User) session.getAttribute("user");
				MDC.put("userid", "OrganNo_" + user.getOrganNo() + "_UserNo_" + user.getUserNo());
			}
			requestBean = beforeAction(req, paramClass, requestBean);
			responseBean = doAction(req, response, requestBean, responseBean);
			responseJsonStr = afterAction(responseBean);
		} catch (Exception e) {
			this.log.error("IF执行出错 " + e.getMessage(), e);
			responseJsonStr = "{\"retCode\":IF0002,\"retMsg\":未知异常}";
		} finally {
		}
		return responseJsonStr;
	}
	
	protected String afterAction(ResponseBean responseBean) throws JsonProcessingException {
		return transObj2Json(responseBean);
	}
	
	
	protected String transObj2Json(Object object) throws JsonProcessingException {
		String retJson = "";
		ObjectMapper objectMapper = new ObjectMapper();
		retJson = objectMapper.writeValueAsString(object);
		return retJson;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Object mapToEntity(Object object, Class paramClass) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String str = objectMapper.writeValueAsString(object);
		Object paramBean = objectMapper.readValue(str, paramClass);
		return paramBean;
	}

	public static void main(String[] args) throws Exception {
//		for(int i=0; i<10; i++) {
			
//			System.out.println("111==="+System.currentTimeMillis());
			String key = "mWH4KKS4auHfC1dm43WoDpp8DwC3hLDuHB2p5WTDtU3l5LXHWCEtKVclfkcP0PTlpoPN6AvP62Kn5JSZIDa9vorD94xAwN3qdGsW";
			String jws = JwtUtil.createJWT(key, 30, "127.0.0.1", "admin", "9999", null);
			System.out.println("222==="+System.currentTimeMillis());
			System.out.println(jws);
			
//			Claims claims = JwtUtil.parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyTm8iOiJhZG1pbiIsIm9yZ2FuTm8iOiI5OTk5IiwiaXNzIjoic3VueWFyZCIsImV4cCI6MTU5MTc3MTAyNSwiaWF0IjoxNTkxNzcwOTk1LCJqdGkiOiI1MGQ4ZjA5ZC1lYWFhLTQ3M2EtYjkzYS1iYWZmMzQ0NjBiZWIifQ.OTPSSTWpj0Y-b3QiHNOMYHDNzCn3wQKuAEX_zYuxg5Q", key);
//			System.out.println(claims.toString());
			com.sunyard.cloud.util.JwtCheckResult result = JwtUtil.checkToken(jws, key);
			System.out.println(result.getClaims());
			System.out.println(result.getClaims().getExpiration().getTime());
			System.out.println(result.getClaims().getIssuedAt().getTime());
			System.out.println("333==="+System.currentTimeMillis());
//		}
		
	}

}
