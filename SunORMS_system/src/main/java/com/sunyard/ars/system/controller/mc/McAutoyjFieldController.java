package com.sunyard.ars.system.controller.mc;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.system.pojo.mc.McAutoyjFieldTb;
import com.sunyard.ars.system.service.mc.IMcAutoyjFieldService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.controller.BaseController;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;

@Controller
public class McAutoyjFieldController extends BaseController {

	
	@Resource
	private IMcAutoyjFieldService mcAutoyjFieldService;
	
	@RequestMapping(value = "/mcAutoyjFieldController.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="增删改查", operationName="自动预警配置")
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.excuteRequest(request, McAutoyjFieldTb.class);
	}
	
	@Override
	protected ResponseBean doAction(HttpServletRequest request, RequestBean requestBean, ResponseBean responseBean) {
		try {			
			responseBean = mcAutoyjFieldService.execute(requestBean);
		} catch (Exception e) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			log.error("调用接口方法出错： " + e.getMessage(), e);
		}
		return responseBean;
	}


}
