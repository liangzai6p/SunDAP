package com.sunyard.ars.system.controller.othersys;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.system.service.impl.othersys.OtherSysSpringContextFactory;
import com.sunyard.ars.system.service.othersys.IOtherSystemService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.controller.BaseController;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;

/**
 * 外系统调用接口
 * @author zgz
 *
 */
@Controller
public class OtherSystemController extends BaseController {
	
	@Resource
	private OtherSysSpringContextFactory otherSysSpringContextFactory;
	
	@RequestMapping(value = "/otherSystem.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="增删改查", operationName="操作公告信息")
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 执行请求，附带请求参数Bean类型
		return super.excuteRequest(request, Object.class);
	}
	
	/**
	 * @author:		 zgz
	 * @date:		 2017年12月18日 下午2:35:14
	 * @description: TODO(调用服务接口，执行具体操作)
	 */
	@Override
	protected ResponseBean doAction(HttpServletRequest request, RequestBean requestBean, ResponseBean responseBean) {
		try {
			String tranCode = (String)requestBean.getSysMap().get("tranCode");
			if(tranCode == null || "".equals(tranCode.trim())) {
				throw new Exception("交易码：tranCode不能为空！");
			}
			IOtherSystemService otherSystemService = otherSysSpringContextFactory.getBean(tranCode);
			if(otherSystemService == null) {
				throw new Exception("未定义交易码："+tranCode+"对应的service类！");
			}else {
				responseBean = otherSystemService.execute(requestBean);
			}
		} catch (Exception e) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			log.error("调用接口方法出错： " + e.getMessage(), e);
		}
		return responseBean;
	}

}
