package com.sunyard.ars.common.controller;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunyard.aos.common.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.util.UUidUtil;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;

@Controller
public class ARSFileController {
	// 日志记录器
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/arsFileUpload.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="文件操作", operationName="文件上传")
	public String fileUpload(HttpServletRequest request, HttpServletResponse response) {
		ResponseBean responseBean = new ResponseBean();
        try {
        	// 将 request 转换为 MultipartHttpServletRequest
        	MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
        	
        	// 获取上传路径标识
        	String saveFilePath = multiRequest.getParameter("filePath");
        	saveFilePath = URLDecoder.decode(saveFilePath, "UTF-8");
        	String filter = multiRequest.getParameter("filter");

        	File folder = new File(FileUtil.pathManipulation(saveFilePath));
        	if(!folder.exists()) {
        		folder.mkdirs();
        	}
        	// 获取上传文件列表
        	List<String> fileNameList = new ArrayList<String>();
        	Iterator<String> fileNames = multiRequest.getFileNames(); // 此处对应的是前台form表单提交的type="file"的input标签名称
        	while (fileNames.hasNext()) {
        		// 一个input可能一次性提交多个文件上传
        		List<MultipartFile> fileList = multiRequest.getFiles(fileNames.next());
        		for (MultipartFile mpFile : fileList) {
        			if (mpFile == null || BaseUtil.isBlank(mpFile.getOriginalFilename())) {
        				continue;
        			}
        			// 文件原始名称（不带路径）
        			String fileName = mpFile.getOriginalFilename();
        			if("true".equalsIgnoreCase(filter)){
        				fileName = UUidUtil.getShortUuid() + fileName.substring(fileName.lastIndexOf("."));//保存文件名时为uuid，防止重复，返回uuid
        			}
        			fileNameList.add(fileName);
        			File localFile = new File(FileUtil.pathManipulation(saveFilePath+"/"+fileName));
        			mpFile.transferTo(localFile);
        		}
        	}
        	Map retMap = new HashMap();
        	retMap.put("fileNameList", fileNameList);
        	responseBean.setRetMap(retMap);
        	responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
        	responseBean.setRetMsg("文件上传成功！");
		} catch (Exception e) {
			// TODO: handle exception
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			logger.error("文件上传出错，" + e.getMessage(), e);
		}
		
		String responseJsonStr = BaseUtil.transObj2Json(responseBean);
		logger.info("后台返回数据：" + responseJsonStr);
		return responseJsonStr;
	}

}
