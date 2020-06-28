package com.sunyard.ars.system.controller.busm;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.ars.system.bean.busm.Emp;

import com.sunyard.ars.system.service.busm.IEmpService;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.controller.BaseController;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;

@Controller
public class EmpController extends BaseController {

		// 服务接口
		@Resource
		private IEmpService empService;
		

		@RequestMapping(value = "/empController.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
		@ResponseBody
		@ArchivesLog(operationType="增删改查", operationName="操作用户授权配置信息")
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			// 执行请求，附带请求参数Bean类型
			return super.excuteRequest(request, Emp.class);
		}
		
		@Override
		protected ResponseBean doAction(HttpServletRequest request, RequestBean requestBean, ResponseBean responseBean) {
			try {	
				responseBean = empService.execute(requestBean);
			} catch (Exception e) {
				responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
				responseBean.setRetMsg(e.getMessage());
				log.error("调用接口方法出错： " + e.getMessage(), e);
			}
			return responseBean;
		}
	}


