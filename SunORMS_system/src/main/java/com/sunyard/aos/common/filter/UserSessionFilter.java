package com.sunyard.aos.common.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.cloud.util.JwtCheckResult;
import com.sunyard.cloud.util.JwtUtil;
import com.sunyard.cop.IF.common.SunIFConstant;

import io.jsonwebtoken.Claims;

/**
 * 请求过滤器
 * 
 * 未登录状态下进行拦截，只能访问 login.html
 */
public class UserSessionFilter extends OncePerRequestFilter {
	
	/**
	 * 构造函数
	 */
	public UserSessionFilter() {
		
	}

	/**
	 * 过滤拦截操作
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		boolean doFilter = false;
		// 不拦截的url。 外系统建议统一使用otherSystem.do，使用交易码区分功能
		String[] notFilter = new String[] { "/ormsLoginController.do", "/B-JUI", "/css","/images", "/aosMain.do",
							"/SunFlowWebService","/webSocket.do","/userRoleSyn.do","/mrModulePath.do","otherSystem.do",
							"/scanForUrl.do","/arsOcr.do","/sendstatus","/arsPdf.do","/fileDownload.do","/sundap","/loginController.do","menuTreeController.do"};
		// 请求的url /SunARS/scanBatch.do
		response.addHeader("Access-Control-Allow-Origin","*");
		response.addHeader("Access-Control-Allow-Credentials","true");
		String url = request.getRequestURI();
		if (!BaseUtil.isBlank(url)) {
			url = url.trim();
			if (url.indexOf("?") != -1) {
				// 剔除请求参数部分
				url = url.substring(0, url.indexOf("?"));
			}
			if (!url.equals("/")) {
				doFilter = check(notFilter, url);
			}
			if (doFilter) {
				String token = request.getHeader("Authorization");
				if(token != null) {
					JwtCheckResult checkResult = JwtUtil.checkToken(token, ARSConstants.SYSTEM_PARAMETER.get("TOKEN_KEY").getParamValue());
					if(checkResult.getRetBoolean()) {
						String clientIp = (String) checkResult.getClaims().get("clientIp");
						//请求客户端ip校验，增加安全性
//						if( !BaseUtil.isBlank(clientIp) && clientIp.equals(request.getRemoteAddr())) {
							doFilter = !updateToken(checkResult.getClaims(), response);
//						}
					}
				}
			}
		}
		if (doFilter) {
			// 如果session中不存在登录者实体，则弹出框提示重新登录
			PrintWriter out = response.getWriter();
			String loginPage = request.getContextPath() + "/error.html";
			StringBuilder builder = new StringBuilder();
			builder.append("<script type=\"text/javascript\">");
			builder.append("window.location.href='");
			builder.append(loginPage);
			builder.append("';");
			builder.append("</script>");
			out.print(builder.toString());
		} else {
			filterChain.doFilter(request, response);
		}
	}

	/**
	 * @param notFilter
	 *            不拦截的url
	 * @param url
	 *            请求的url
	 *            
	 * @return false:不拦截 、true:拦截
	 */
	public boolean check(String[] notFilter, String url) {
		// url以css和js结尾的不进行拦截
		if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".jpg")
				|| url.endsWith(".png") || url.endsWith(".ico") || url.endsWith(".html") || url.endsWith(".svg")) {
			return false;
		}
		// 含有notFilter中的任何一个则不进行拦截
		for (String s : notFilter) {
			if (url.indexOf(s) != -1) {
				return false;
			}
		}
		try {
			//从系统参数表sm_sysparameter_tb中加载不拦截的URL,这些参数在启动时被加载
			String defaultUrl = SunIFConstant.PARAM_MAP.get("UserSessionFilter").trim();
			// 支持逗号分隔的多个url形式
			String[] urlArr = defaultUrl.split("\\,");
			for (String dUrl : urlArr) {
				if (url.replaceAll("/", "").equals(dUrl.replaceAll("/", ""))) {
					return false;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return true;
	}
	
	/**
	 * 定时刷新token，避免正常使用中突然超时
	 * @param claims
	 * @param response
	 * @return 是否更新成功
	 */
	private boolean updateToken(Claims claims, HttpServletResponse response) {
		//距生成token时间大于5分钟，刷新token
		if(System.currentTimeMillis() - claims.getIssuedAt().getTime() > 5*1000*60) {
			String key = ARSConstants.SYSTEM_PARAMETER.get("TOKEN_KEY").getParamValue();
			String jws;
			try {
				jws = JwtUtil.createJWT(key, 30, (String)claims.get("clientIp"), (String)claims.get("userNo"), (String)claims.get("organNo"),  claims.getSubject());
				response.addHeader("Authorization", jws);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage(), e);
				return false;
			}
		}
		return true;
	}
}
