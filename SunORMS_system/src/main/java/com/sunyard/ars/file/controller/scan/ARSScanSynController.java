package com.sunyard.ars.file.controller.scan;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunyard.aos.common.util.BaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.dao.TmpBatchMapper;
import com.sunyard.ars.common.pojo.TmpBatch;
import com.sunyard.ars.common.util.ECMUtil;
import com.sunyard.ars.system.bean.sc.OrganData;
import com.sunyard.ars.system.bean.sc.SCDatasource;
import com.sunyard.ars.system.bean.sc.ServiceReg;
import com.sunyard.ars.system.dao.sc.OrganDataMapper;
import com.sunyard.ars.system.dao.sc.SCDatasourceMapper;
import com.sunyard.ars.system.dao.sc.ServiceRegMapper;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;

@Controller
public class ARSScanSynController {
	// 日志记录器
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	TmpBatchMapper tmpBatchMapper;
	
	@Resource
	private SCDatasourceMapper scDatasourceMapper;
	
	@Resource
	private ServiceRegMapper serviceRegMapper;
	
	@Resource
	private OrganDataMapper organDataMapper;
	
	@RequestMapping(value="/sendstatus",produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="控件回调", operationName="控件回调")
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly=false)
	public void sendstatus(HttpServletRequest request, HttpServletResponse response) {
		ResponseBean responseBean = new ResponseBean();
        try {
        	String batchId = BaseUtil.filterHeader(request.getParameter("keyvalue"));
    		String status = BaseUtil.filterHeader(request.getParameter("status"));
			String contentId = BaseUtil.filterHeader(request.getParameter("batchid"));
    		if(batchId!=null && !"".equals(batchId)){
    		if(status != null && "0".equals(status)) {
    		int k= 0;
    			int count = 0;
    			while(k<1 && count<10){
    				k= tmpBatchMapper.updateSendstatus(batchId,contentId);
    				if(k>0){
    					logger.info("控件回调，批次："+batchId+"状态置为有效成功");
    				}else{
    					count++;
    					logger.info("批次："+batchId+"未更新到到批次，10秒后开始第"+count+"次重试");
    					Thread.sleep(10000);
    				}
    				
    			}
    			if(count == 10){
    				logger.info("控件回调，批次："+batchId+"尝试"+count+"次后,依然回调失败,批次数据可能出现异常");
    			}
    			
    		}else{
    			logger.error("控件回调，批次："+batchId+"控件上传未成功,状态不置为有效");
    		}
        	}
        } catch (Exception e) {
			// TODO: handle exception
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			logger.error("控件回调出错，" + e.getMessage(), e);
		}
		
	}

}
