package com.sunyard.ars.common.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.dao.OcrTaskMapper;
import com.sunyard.ars.common.pojo.OcrTaskBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Controller
public class ARSOcrController {
    @Resource
    OcrTaskMapper ocrTaskMapper;
    // 日志记录器
    protected Logger logger = LoggerFactory.getLogger(this.getClass());


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/arsOcr.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @ArchivesLog(operationType="识别处理", operationName="识别分析")
    @Transactional(propagation = Propagation.REQUIRED)
    public String arsOcr(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        String message = request.getParameter("message");
        logger.info("接收数据：" + BaseUtil.filterLog(message));
        String taskId ="";
       try{
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseBean	ocrRes = objectMapper.readValue(message, ResponseBean.class);
            Map<Object, Object> retMap = ocrRes.getRetMap();
            //获取批次信息
            Map batchInfo = (Map)retMap.get("batchInfo");
            String batchId = (String)batchInfo.get("batchId");

            OcrTaskBean ocrTaskBean = new OcrTaskBean();
            Date date = new Date();
            String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(date);
            String HHmmssSSS = new SimpleDateFormat("HHmmssSSS").format(date);
            SecureRandom random = new SecureRandom();
            taskId = yyyyMMdd + HHmmssSSS + random.nextInt(1000);
            ocrTaskBean.setTaskDate(yyyyMMdd);
            ocrTaskBean.setTaskId(taskId);
            ocrTaskBean.setMessage(message);
            ocrTaskBean.setBatchId(batchId);

            ocrTaskMapper.insert(ocrTaskBean);
        }catch(Exception e){
            logger.error("保存返回结果发生异常",e);
            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
            responseBean.setRetMsg("保存返回结果发生异常 ");
            String responseJsonStr = BaseUtil.transObj2Json(responseBean);
            logger.info("返回数据：" + BaseUtil.formatJson(responseJsonStr));
            return responseJsonStr;
        }

        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("已成功接受并存储 "+taskId);
        String responseJsonStr = BaseUtil.transObj2Json(responseBean);


        logger.info("返回数据：" + BaseUtil.formatJson(responseJsonStr));
        return responseJsonStr;
    }

}