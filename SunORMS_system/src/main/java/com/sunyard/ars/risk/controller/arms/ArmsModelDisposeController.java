package com.sunyard.ars.risk.controller.arms;

import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.risk.service.arms.IArmsModelDisposeService;
import com.sunyard.ars.risk.service.arms.IArmsModelToShowService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.controller.BaseController;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 预警单处理
 */
@Controller
public class ArmsModelDisposeController extends BaseController {

    @Resource
    private IArmsModelDisposeService armsModelDisposeService;

    @RequestMapping(value = "/arms-model-dispose.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @ArchivesLog(operationType="增删改查", operationName="模型处理")
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.excuteRequest(request, Object.class);
    }

    @Override
    protected ResponseBean doAction(HttpServletRequest request, RequestBean requestBean, ResponseBean responseBean) {
        try {
            responseBean = armsModelDisposeService.execute(requestBean);
        } catch (Exception e) {
            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
            responseBean.setRetMsg(e.getMessage());
            log.error("调用接口方法出错： " + e.getMessage(), e);
        }
        return responseBean;
    }
}
