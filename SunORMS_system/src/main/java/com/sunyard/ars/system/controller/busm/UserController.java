package com.sunyard.ars.system.controller.busm;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.system.bean.busm.UserBean;
import com.sunyard.ars.system.service.busm.IUserService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.controller.BaseController;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;

/**
 * @author:		zheng.jw
 * @date:		2017年12月18日 下午2:01:51
 * @Description:用户信息Controller
 */
@Controller
public class UserController extends BaseController {

	// 服务接口
	@Resource
	private IUserService userService;
	
	/**
	 * @author:		zheng.jw
	 * @date:		2017年12月18日 下午2:04:29
	 * @Description:执行操作请
	 */
	@RequestMapping(value = "/userController.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="增删改查", operationName="操作用户信息")
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 执行请求，附带请求参数Bean类型
		return super.excuteRequest(request, UserBean.class);
	}
	
	/**
	 * @author:		zheng.jw
	 * @date:		2017年12月18日 下午2:04:14
	 * @Description:(调用服务接口，执行具体操作)
	 */
	@Override
	protected ResponseBean doAction(HttpServletRequest request, RequestBean requestBean, ResponseBean responseBean) {
		try {			
			responseBean = userService.execute(requestBean);
		} catch (Exception e) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			log.error("调用接口方法出错： " + e.getMessage(), e);
		}
		return responseBean;
	}
}
