package com.sunyard.ars.system.controller.notice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.ars.system.bean.notice.NoticeBean;
import com.sunyard.ars.system.service.notice.INoticeService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.controller.BaseController;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;

/**
 * @author:		 wwx
 * @date:		 2017年12月18日 下午2:34:48
 * @description: TODO(NoticeController)
 */
@Controller
public class NoticeController extends BaseController {

	// 服务接口
	@Resource
	private INoticeService noticeService;
	
	/**
	 * @author:		 wwx
	 * @date:		 2017年12月18日 下午2:35:03
	 * @description: TODO(执行操作请求)
	 */
	@RequestMapping(value = "/notice.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="增删改查", operationName="操作公告信息")
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 执行请求，附带请求参数Bean类型
		return super.excuteRequest(request, NoticeBean.class);
	}
	
	/**
	 * @author:		 wwx
	 * @date:		 2017年12月18日 下午2:35:14
	 * @description: TODO(调用服务接口，执行具体操作)
	 */
	@Override
	protected ResponseBean doAction(HttpServletRequest request, RequestBean requestBean, ResponseBean responseBean) {
		try {			
			responseBean = noticeService.execute(requestBean);
		} catch (Exception e) {
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			log.error("调用接口方法出错： " + e.getMessage(), e);
		}
		return responseBean;
	}
}