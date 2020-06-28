package com.sunyard.ars.file.controller.scan;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.file.service.scan.IScanEcmForUrlService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.controller.BaseController;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;
/**
 * 
 * @date   2018年10月17日
 * @Description ScanEcmForUrlController.java
 */
@Controller
public class ScanEcmForUrlController extends BaseController {
	
	@Resource
	public IScanEcmForUrlService scanEcmForUrlService;
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/scanForUrl.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="增删改查", operationName="影像查询")
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 执行请求，附带请求参数Bean类型
		return super.excuteRequest(request, Object.class);
	}
	
	@Override
	protected ResponseBean doAction(HttpServletRequest req, RequestBean requestBean, ResponseBean responseBean) {
		try {
			log.info("SCAN FOR URL调用接口："+req.getLocalAddr()+":"+req.getLocalPort());
			Map sysMap = requestBean.getSysMap();
			sysMap.put("LocalAddr",req.getLocalAddr());
			responseBean = scanEcmForUrlService.execute(requestBean);
		} catch (Exception e) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			log.error("SCAN FOR URL调用接口方法出错： " + e.getMessage(), e);
		}
		return responseBean;
	}
}
