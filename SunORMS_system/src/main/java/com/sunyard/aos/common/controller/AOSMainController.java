package com.sunyard.aos.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;
import com.sunyard.cop.IF.spring.websocket.WebSocketSessionUtils;

/**
 * @author:		 lewe
 * @date:		 2017年6月18日 下午7:57:46
 * @description: TODO(AOS主控制器，一般用于系统间交互)
 */
@Controller
public class AOSMainController {
	
	// 日志记录器
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月17日 下午8:10:30
	 * @description: TODO(执行外部请求)
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/aosMain.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="主控制器", operationName="系统间交互")
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String requestJsonStr = request.getParameter("message");
		logger.info("接收数据：" + BaseUtil.formatJson(requestJsonStr));
		ResponseBean responseBean = new ResponseBean();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			RequestBean requestBean = objectMapper.readValue(requestJsonStr, RequestBean.class);
			Map<String, Object> sysMap = requestBean.getSysMap();
			// 获取操作标识
			String oper_type = (String)sysMap.get("oper_type");
			// 强制登出操作，推送消息告知，使其被迫下线
			String user_no = (String)sysMap.get("user_no");
			String login_terminal = (String)sysMap.get("login_terminal");
			if (WebSocketSessionUtils.hasConnection(user_no, login_terminal)) {
				HashMap<String, Object> msgMap = (HashMap<String, Object>) sysMap;
            	msgMap.put("msg_type",oper_type);
            	WebSocketSessionUtils.sendMessage(user_no, login_terminal, BaseUtil.transObj2Json(msgMap));
		    }
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("操作成功");
		} catch (Exception e) {
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			logger.error("执行异常，" + e.getMessage(), e);
		}
		String responseJsonStr = BaseUtil.transObj2Json(responseBean);
		logger.info("返回数据：" + BaseUtil.formatJson(responseJsonStr));
		return responseJsonStr;
	}
}
