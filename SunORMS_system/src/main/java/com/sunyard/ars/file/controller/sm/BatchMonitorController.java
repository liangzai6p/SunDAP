package com.sunyard.ars.file.controller.sm;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.pojo.TmpBatch;
import com.sunyard.ars.file.service.scan.IScanBatchService;
import com.sunyard.ars.file.service.sm.IBatchMonitorService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.controller.BaseController;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;
/**
 * 
 * @date   2018年6月1日
 * @Description BatchController.java
 */
@Controller
public class BatchMonitorController extends BaseController {
	
	@Resource
	public IBatchMonitorService batchMonitorService;
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/batchMonitor.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="增删改查", operationName="批次监控")
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 执行请求，附带请求参数Bean类型
		return super.excuteRequest(request, TmpBatch.class);
	}
	
	@Override
	protected ResponseBean doAction(HttpServletRequest req, RequestBean requestBean, ResponseBean responseBean) {
		try {			
			responseBean = batchMonitorService.execute(requestBean);
		} catch (Exception e) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			log.error("批次扫描调用接口方法出错： " + e.getMessage(), e);
		}
		return responseBean;
	}
}
