package com.sunyard.aos.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.dao.BaseDao;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.pojo.User;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;

/**
 * @author:		 lewe
 * @date:		 2017年6月17日 下午7:57:46
 * @description: TODO(登出操作控制器)
 */
@Controller
public class LogOutController {
	
	// 日志记录器
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 数据库接口
	@Resource
	private BaseDao baseDao;
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月17日 下午8:10:30
	 * @description: TODO(执行登出请求)
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/logout.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="登出操作", operationName="登出")
	@Transactional(propagation = Propagation.SUPPORTS)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		String requestJsonStr = request.getParameter("message");
		logger.info("接收数据：" + BaseUtil.formatJson(requestJsonStr));
		ResponseBean responseBean = new ResponseBean();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			RequestBean requestBean = objectMapper.readValue(requestJsonStr, RequestBean.class);
			Map<String, Object> sysMap = requestBean.getSysMap();
			// 构造条件参数
			HashMap<String, Object> condMap = new HashMap<String, Object>();
            String flag = (String) sysMap.get("flag");
			condMap.put("user_no", (String) sysMap.get("user_no"));
			condMap.put("logout_time", BaseUtil.getCurrentTimeStr());
			condMap.put("login_terminal", (String) sysMap.get("login_terminal"));
			User user = BaseUtil.getLoginUser();
			condMap.put("bank_no", user.getBankNo());
			condMap.put("system_no", user.getSystemNo());
			condMap.put("project_no", user.getProjectNo());
            if("1".equals(flag)) {
                // 用户登出
                baseDao.userLogout(condMap);
                // 更新用户登录状态
                baseDao.updateUserLoginState(condMap);
            } else if("2".equals(flag)) {
                baseDao.userLogout2(condMap);
            }
			// 拼装返回信息
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("登出成功");
		} catch (Exception e) {
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			logger.error("执行异常，" + e.getMessage(), e);
		}
		// 移除session中的用户信息
		request.getSession().removeAttribute("user");
		String responseJsonStr = BaseUtil.transObj2Json(responseBean);
		logger.info("返回数据：" + BaseUtil.formatJson(responseJsonStr));
		return responseJsonStr;
	}
}
