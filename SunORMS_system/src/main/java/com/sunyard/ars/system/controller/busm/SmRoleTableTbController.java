package com.sunyard.ars.system.controller.busm;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.system.bean.busm.RoleBean;
import com.sunyard.ars.system.bean.busm.SmRoleTableTB;
import com.sunyard.ars.system.service.busm.IRoleService;
import com.sunyard.ars.system.service.busm.SmRoleTableTbService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.controller.BaseController;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;

@Controller
public class SmRoleTableTbController extends BaseController  {
	
	@Resource
	private SmRoleTableTbService smRoleTableTbService;

	@RequestMapping(value = "/smRoleTableTbController.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="增删改查", operationName="操作角色和表信息")
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 执行请求，附带请求参数Bean类型
		return super.excuteRequest(request,SmRoleTableTB.class);
	}
	
	@Override
	protected ResponseBean doAction(HttpServletRequest request, RequestBean requestBean, ResponseBean responseBean) {
		try {			
			responseBean = smRoleTableTbService.execute(requestBean);
		} catch (Exception e) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			log.error("调用接口方法出错： " + e.getMessage(), e);
		}
		return responseBean;
	}
}
