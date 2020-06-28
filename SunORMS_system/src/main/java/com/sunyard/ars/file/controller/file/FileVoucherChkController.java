package com.sunyard.ars.file.controller.file;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.file.pojo.scan.VoucherChk;
import com.sunyard.ars.file.service.file.IFileVoucherChkService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.controller.BaseController;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;
/**
 * 
 * @date   2018年5月24日
 */
@Controller
public class FileVoucherChkController extends BaseController {
	
	@Resource
	private IFileVoucherChkService fileVoucherChkService;
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/fileVoucherChkController.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="增删改查", operationName="批次登记")
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 执行请求，附带请求参数Bean类型
		return super.excuteRequest(request, VoucherChk.class);
	}
	
	@Override
	protected ResponseBean doAction(HttpServletRequest req, RequestBean requestBean, ResponseBean responseBean) {
		try {			
			responseBean = fileVoucherChkService.execute(requestBean);
		} catch (Exception e) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			log.error("批次登记调用接口方法出错： " + e.getMessage(), e);
		}
		return responseBean;
	}
}

