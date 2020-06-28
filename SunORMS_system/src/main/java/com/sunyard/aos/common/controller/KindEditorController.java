package com.sunyard.aos.common.controller;

import java.io.File;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.FileUtil;
import com.sunyard.aos.common.util.PropertiesUtil;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;

/**
 * @author:		 wwx
 * @date:		 2017年9月14日 下午5:25:46
 * @description: TODO(KindEditor上传文件控制器)
 */
@Controller
public class KindEditorController {
	
	// 日志记录器
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * @author:		 wwx
	 * @return 
	 * @throws 		 Exception 
	 * @date:		 2017年9月14日 下午5:25:46
	 * @description: TODO(执行KindEditor插件的文件上传请求)
	 */
	@RequestMapping(value = "/kindEditorUpload.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="KindEditor文件操作", operationName="文件上传")
	public String kindEditorUpload(HttpServletRequest request, HttpServletResponse response) throws FileUploadException, Exception {
		// 文件保存目录路径
		String savePath = PropertiesUtil.getValue("noticeContentFilePath");
		if (BaseUtil.isBlank(savePath)) {
			savePath = PropertiesUtil.getValue("defaultFilePath");
			if (BaseUtil.isBlank(savePath)) {
				return getError("上传路径为空，请检查相关配置文件 AosConfig.properties ！");
			}
		}
		
		// 定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,txt,zip,rar,gz,bz2");

		// 最大文件大小
		long maxSize = AOSConstants.FILE_SIZE;
		
		// 根据当前上下文初始化  CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		
		// 检查form表单中是否提交了 enctype="multipart/form-date" 类型数据
		if (multipartResolver.isMultipart(request)) {
			
			// 将 request 转换为 MultipartHttpServletRequest
	        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
	     
	        response.setContentType("text/html; charset=UTF-8");
	
			if(!ServletFileUpload.isMultipartContent(request)){
				return getError("请选择文件。");
			}
			
			// 检查目录
			// 校验本地上传目录
	    	FileUtil.checkDirectory(savePath);
			File uploadDir = new File(FileUtil.pathManipulation(savePath));
			
			// 检查目录写权限
			if(!uploadDir.canWrite()){
				return getError("上传目录没有写权限。");
			}
	
			String dirName = request.getParameter("dir");
			dirName = BaseUtil.filterSqlParam(dirName);
			if (dirName == null) {
				dirName = "image";
			}
			if(!extMap.containsKey(dirName)){
				return getError("目录名不正确。");
			}
			// 创建文件夹
			savePath += "/" + dirName + "/";
			File saveDirFile = new File(FileUtil.pathManipulation(savePath));
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String ymd = sdf.format(new Date());
			savePath += ymd;
			File dirFile = new File(FileUtil.pathManipulation(savePath));
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			
			// 获取上传文件列表
	        Iterator<String> fileNames = multiRequest.getFileNames(); 
	        while (fileNames.hasNext()) {
	        	//  一个input可能一次性提交多个文件上传
	        	List<MultipartFile> fileList = multiRequest.getFiles(fileNames.next());
	        	for (MultipartFile mpFile : fileList) {
	        		if (mpFile == null || BaseUtil.isBlank(mpFile.getOriginalFilename())) {
	            		continue;
	            	}
	        		String fileName = mpFile.getOriginalFilename();
	        		// 检查文件大小
					if(mpFile.getSize() > maxSize){
						return getError("上传文件大小超过限制。");
					}
					// 检查扩展名
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){
						return getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
					}
	
					SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
					SecureRandom random = new SecureRandom();
					String newFileName = df.format(new Date()) + "_" + random.nextInt(1000) + "." + fileExt;
					String newFileName2 = FileUtil.saveLocalFile(mpFile, savePath, newFileName);
					savePath = "./servlet/DownLoadServlet?fileName="+newFileName+"&saveFileName="+newFileName2;
					JSONObject obj = new JSONObject();
					obj.put("error", 0);
					obj.put("url", savePath);
					return String.valueOf(obj);
	            }
	        }  	
		} else {
			return getError("form表单enctype类型不是multipart/form-date，当前没有需要上传的文件！");
		}
		return null;
	}
	
	/**
	 * @author: 	wwx
	 * @Description:TODO(封装错误信息)
	 * @date:		2017年9月18日上午9:40:52
	 */
	private String getError(String message) {
		JSONObject obj = new JSONObject();
		obj.put("error", 1);
		obj.put("message", message);
		return String.valueOf(obj);
	}
}
