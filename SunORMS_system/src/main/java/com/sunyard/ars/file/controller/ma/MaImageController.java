package com.sunyard.ars.file.controller.ma;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.file.pojo.ma.MaParameterBean;
import com.sunyard.ars.file.service.ma.IMaImageService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.controller.BaseController;

import com.sunyard.cop.IF.spring.aop.ArchivesLog;

/**
 *业务审核请求
 */
@Controller
public class MaImageController extends BaseController {

	// 服务接口
	@Resource
	private IMaImageService maImageService;
	
	/**
	 * @author:		hui.song
	 * @date:		
	 * @Description:执行操作请
	 */
	@RequestMapping(
			value = {"/maImageController.do"},
			method = {RequestMethod.POST},
			produces = {"text/html;charset=UTF-8"}
	)
	@ResponseBody
	@ArchivesLog(operationType="增删改查", operationName="业务审核图像操作")
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 执行请求，附带请求参数Bean类型
		return super.excuteRequest(request, MaParameterBean.class);
	}
	
	/**
	 * @author:		zheng.jw
	 * @date:		2017年12月19日 上午9:25:39
	 * @Description:(调用服务接口，执行具体操作)
	 */
	@Override
	protected ResponseBean doAction(HttpServletRequest request, RequestBean requestBean, ResponseBean responseBean) {
		try {			
			responseBean = maImageService.execute(requestBean);
		} catch (Exception e) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			log.error("调用接口方法出错： " + e.getMessage(), e);
		}
		return responseBean;
	}
}
