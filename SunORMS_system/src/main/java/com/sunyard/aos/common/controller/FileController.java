package com.sunyard.aos.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.sunyard.aos.common.util.HttpUtil;
//import com.sunyard.aos.common.util.PropertiesUtil;
import com.sunyard.aos.common.util.net.ftp.FtpException;
import com.sunyard.aos.common.util.net.ftp.FtpUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;
import com.sunyard.cop.IF.mybatis.pojo.User;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;
import com.sunyard.cop.IF.spring.websocket.WebSocketSessionUtils;

/**
 * @author:		 lewe
 * @date:		 2017年3月17日 下午7:57:46
 * @description: TODO(文件操作控制器，包括文件上传、下载，支持本地和ftp形式)
 */
@Controller
public class FileController {
	
	// 日志记录器
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	// ftp操作类
	private FtpUtil ftpUtil = null;
	//key=文件名  value=文件重复次数
	@SuppressWarnings("rawtypes")
	private  Map map = new HashMap();
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月17日 下午8:10:30
	 * @description: TODO(执行文件上传请求，支持批量，上传成功后以列表形式返回所有上传文件的名称及完整的实际存储路径（包括实际存储名称）)
	 */
	@RequestMapping(value = "/fileUpload.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="文件操作", operationName="文件上传")
	public String fileUpload(HttpServletRequest request, HttpServletResponse response) {
		ResponseBean responseBean = new ResponseBean();
		Map<Object, Object> retMap = new HashMap<Object, Object>();
		// 存储上传成功的文件信息，包括 上传文件名称（不带路径）、文件存储名称（带路径）
        List<Map<String, String>> uploadFileList = new ArrayList<Map<String,String>>();
        // 读取配置属性：是否使用ftp操作
        boolean useFtp = false;// "true".equalsIgnoreCase(PropertiesUtil.getValue("useFtp")) ? true : false;
        // 本地临时目录（与 WEB-INF同级的temp目录）
        String tempFilePath = HttpUtil.getAbsoluteTempPath();
        // 本地临时文件名称
        String tempFileName = "";
        //使用配置的路径和实际文件名称（不添加子目录文件名也不加前缀）
        boolean useTruePathName = false;
		try {
			// 根据当前上下文初始化  CommonsMutipartResolver （多部分解析器）
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
			
			// 检查form表单中是否提交了 enctype="multipart/form-date" 类型数据
			if (multipartResolver.isMultipart(request)) {
				long startTime = System.currentTimeMillis();
				logger.info("开始上传文件");
				
				// 将 request 转换为 MultipartHttpServletRequest
	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
	            
	            // 获取上传路径标识
	            String saveFilePath = multiRequest.getParameter("filePath");
	            String useTruePathNameStr = multiRequest.getParameter("useTruePathName");
	            if("true".equals(useTruePathNameStr)) {
	            	useTruePathName = true;
	            }
	            logger.info("前台传入文件路径：" + FileUtil.pathManipulation(saveFilePath));
	            boolean isAddChildDir = false;
	            if (BaseUtil.isBlank(saveFilePath)) {
	            	saveFilePath = tempFilePath;
	            	useFtp = false;	// 默认为临时文件，存储于本地临时目录，无需上传ftp
	            } else {
	            	SysParameter sp = ARSConstants.SYSTEM_PARAMETER.get(saveFilePath);
	            	if(sp == null) {
	            		if(saveFilePath.contains("@")) {
	            			//saveFilePath = saveFilePath.replace("@", File.separatorChar+"");
							//防止 @/filepath => \/filepath  FileUtil.saveLocalFile报错
							saveFilePath = saveFilePath.replace("@","");
						}else {
	            			throw new Exception("系统参数表未配置"+saveFilePath+"文件保存路径！");
	            		}
	            	}else {
	            		saveFilePath = sp.getParamValue();//PropertiesUtil.getValue(saveFilePath);
	            	}
	            	if(!useTruePathName) {
	            		isAddChildDir = true;
	            	}
	            }
	            if (BaseUtil.isBlank(saveFilePath)) {
	            	throw new Exception("上传路径为空，请检查系统参数相关配置 ！");
	            }
	            // 添加月份（yyyyMM）标识子目录
	            if (isAddChildDir) {
	            	String month = BaseUtil.getCurrentDateByFormat("yyyyMM");
	            	saveFilePath = saveFilePath + FileUtil.getSeparator(saveFilePath) + month;
	            }
	            
	            if (!useFtp) {
	            	// 校验本地上传目录
	            	FileUtil.checkDirectory(saveFilePath);
	            } else {
	            	// 校验本地临时目录
	            	FileUtil.checkDirectory(tempFilePath);
	            }
	            
	            // 获取上传文件列表
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
		            	// 文件重命名（随机生成一个UUID字符串为文件名前缀，避免文件重复）
		            	String newFileName = useTruePathName ?fileName : UUID.randomUUID() + "_" + fileName;
		            	// 文件存储名称（带路径）
		            	String saveFileName = "";
		            	if (!useFtp) {
		            		// 保存文件到本地上传目录
		            		saveFileName = FileUtil.saveLocalFile(mpFile, saveFilePath, newFileName);
		            	} else {
		            		// 保存文件到本地临时目录
		            		tempFileName = FileUtil.saveLocalFile(mpFile, tempFilePath, newFileName);
		            		// 上传文件到ftp服务器
		            		if (ftpUtil == null) {
		            			initFtpUtil();
		            			ftpUtil.checkDirectory(saveFilePath);
		            			ftpUtil.changeDirectory(saveFilePath);
		            		}
			            	boolean flag = putFileToFtp(tempFileName, newFileName);
			            	if (flag) {
			            		saveFileName = saveFilePath + FileUtil.getSeparator(saveFilePath) + newFileName;
			            	}
			            	// 删除本地临时文件
			            	FileUtil.deleteFile(tempFileName);
			            	tempFileName = "";
		            	}
		            	// 记录上传成功的文件信息
		            	if (!BaseUtil.isBlank(saveFileName)) {
		            		Map<String, String> map = new HashMap<String, String>();
			            	map.put("fileName", fileName);
			            	map.put("saveFileName", saveFileName);
			            	uploadFileList.add(map);
			            	logger.info("上传文件 " + uploadFileList.size() +  " 成功：" + fileName);
		            	} else {
		            		logger.error("上传文件[" + fileName + "]失败");
		            	}
	            	}
	            }
	            long endTime = System.currentTimeMillis();
	            logger.info("文件上传完成，保存路径：" + FileUtil.pathManipulation(saveFilePath));
				logger.info("本次上传文件耗时：" + (endTime - startTime) + " ms");
				if (uploadFileList.size() > 0) {
					responseBean.setRetMsg("文件上传成功");
				} else {
					responseBean.setRetMsg("当前没有需要上传的文件");
				}
	        } else {
	        	responseBean.setRetMsg("form表单enctype类型不是multipart/form-date，当前没有需要上传的文件！");
	        }
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			retMap.put("uploadFileList", uploadFileList);
            responseBean.setRetMap(retMap);
			
		} catch (Exception e) {
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			logger.error("文件上传出错，" + e.getMessage(), e);
		} finally {
			// 关闭ftp连接
			if (ftpUtil != null) {
				ftpUtil.disconnect();
//				ftpUtil = null;
			}
			// 删除本地可能存在的临时文件
			if (!BaseUtil.isBlank(tempFileName)) {
				FileUtil.deleteFile(tempFileName);
				tempFileName = "";
			}
		}
		String responseJsonStr = BaseUtil.transObj2Json(responseBean);
		logger.info("后台返回数据：" + responseJsonStr);
		return responseJsonStr;
	}
	
	/**
	 * @author:		 lewe
	 * @throws Exception 
	 * @date:		 2017年3月17日 下午8:30:30
	 * @description: TODO(执行文件下载请求，请求参数为需要下载文件的原始名称（不带路径）、存储名称（带路径）)
	 */
	@RequestMapping(value = "/fileDownload.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="文件操作", operationName="文件下载")
	public void fileDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = request.getParameter("fileName");
		String saveFileName = request.getParameter("saveFileName");
		String isDelete = request.getParameter("isDelete");
		logger.debug("文件名称：" + fileName + "，下载路径：" + saveFileName);
		User user = BaseUtil.getLoginUser();
		if (BaseUtil.isBlank(fileName) || BaseUtil.isBlank(saveFileName)) {
			// 通过WebSocket推送消息提醒至前台，下同
			sendMsg(user,"前台传入的文件名称或者下载路径为空，请检查！");
			return;
		}
		// 读取配置属性：是否使用ftp操作
        boolean useFtp = false;//"true".equalsIgnoreCase(PropertiesUtil.getValue("useFtp")) ? true : false;
		ZipOutputStream zipos = null;
		InputStream is =null;
		OutputStream os =null;
		try {
			long startTime = System.currentTimeMillis();
			logger.info("开始下载文件");
			logger.info("下载文件：" + FileUtil.pathManipulation(saveFileName));
			File file = null;
			File zipFile = null;
			//zip中文件个数
			int temp = 0;
			String[] fileNames = saveFileName.split("\\,");
			if (fileNames.length > 1) {
				//生成临时zip文件
				logger.info("下载方式：zip打包下载");
				String tempFilePath = HttpUtil.getAbsoluteTempPath();
				FileUtil.checkDirectory(tempFilePath);
				String tempFileName = tempFilePath + FileUtil.getSeparator(tempFilePath) + UUID.randomUUID() + ".zip";
				zipFile = new File(tempFileName);
				FileOutputStream outStream = new FileOutputStream(zipFile);
				zipos = new ZipOutputStream(outStream);
			}
			for (int i = 0; i < fileNames.length; i++) {
				if (!useFtp) { // 下载本地文件
					file = new File(FileUtil.pathManipulation(fileNames[i]));
				} else { // 下载ftp文件
					String tempFilePath = HttpUtil.getAbsoluteTempPath();
					String fileName1 = BaseUtil.getAttachmentName(fileNames[i]);
					String tempFileName = tempFilePath + FileUtil.getSeparator(tempFilePath) + UUID.randomUUID() + "_" + fileName1;
					logger.info("tempFileName" + tempFileName);
					if (ftpUtil == null) {
						initFtpUtil();
					}
					useFtp = getFileFromFtp(tempFileName, fileNames[i]);
					if (!useFtp) {
						logger.info("下载文件失败：" + FileUtil.pathManipulation(fileNames[i]));
					} else {
						temp++;
						file = new File(FileUtil.pathManipulation(tempFileName));
					}
					useFtp = true;
				}
				if (file == null || !file.exists()) {
					if (fileNames.length == 1) {
						sendMsg(user,"文件下载失败，指定文件不存在：" + fileNames[0]);
						logger.error("文件下载失败，指定文件不存在：" + FileUtil.pathManipulation(fileNames[0]));
						return;
					}
				} else if (fileNames.length > 1) {
					// 写入zip
					temp ++;
					writeZipFile(file,zipos);
					// 删除临时文件
					if(useFtp) {
						file.delete();
						logger.info("文件删除成功！");
					}
				}
			}
			if (fileNames.length > 1) {
				if(zipos!=null){
					zipos.close();
				}
				if (temp == 0) {
					sendMsg(user,"文件下载失败，指定文件不存在：" + saveFileName);
					logger.error("文件下载失败，指定文件不存在：" + FileUtil.pathManipulation(saveFileName));
					return;
				}
				file = zipFile;
			}
			
			// 设置响应头，以附件形式下载
			response.setContentType("application/x-download;charset=utf-8");
			//判断浏览器类型
			String userAgent = request.getHeader("USER-AGENT");
			//IE 浏览器
			if(userAgent.contains("MSIE") || userAgent.contains("Trident") ){
				fileName = URLEncoder.encode(fileName,"UTF8");
			}else{//火狐，google, 新Edge等其他浏览器
			   fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
	        response.addHeader("Content-Disposition", "attachment;filename=" + BaseUtil.filterHeader(fileName));
	        // 文件写入响应输出流
			 is = new FileInputStream(file);
			 os = response.getOutputStream();
			byte[] b = new byte[2048];
			/*while ((is.read(b)) != -1) {
				os.write(b);
			}*/
			int len = 0;
			while((len = is.read(b)) != -1) {
				os.write(b, 0, len);
			}
			os.flush();
	        // 下载ftp文件完成或者下载多个文件后，删除本地临时文件
			if (useFtp || fileNames.length > 1 || "delete".equals(isDelete)) {
				FileUtil.deleteFile(file);
			}
			
			long endTime = System.currentTimeMillis();
            logger.info("文件下载完成");
			logger.info("本次下载文件耗时：" + (endTime - startTime) + " ms");
			
		} catch (Exception e) {
			sendMsg(user,"文件下载失败！请查看日志信息！");
			logger.error("文件下载失败，", e);
		} finally {
			if(is!=null){
				FileUtil.safeClose(is);
			}

			if(os!=null){
				FileUtil.safeClose(os);
			}

			if(zipos!=null){
				zipos.close();
			}
			// 关闭ftp连接
			if (ftpUtil != null) {
				ftpUtil.disconnect();
//				ftpUtil = null;
			}
		}
	}
	
	/**
	 * @author:		 wwx
	 * @throws Exception 
	 * @date:		 2017年12月12日 下午8:30:30
	 * @description: TODO(执行一些文件操作请求，请求参数为需要操作的文件的存储名称（带路径）)
	 */
	@RequestMapping(value = "/fileOperate.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="文件操作", operationName="文件其他操作")
	public String fileOperate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		Map<Object, Object> retMap = new HashMap<Object, Object>();
		String operate = request.getParameter("operate");
		String saveFileName = request.getParameter("saveFileName");
		// 存储操作成功的文件信息，文件名称（不带路径）、文件存储名称（带路径）
        List<Map<String, String>> operateFileList = new ArrayList<Map<String,String>>();
		if ("del".equals(operate)) {
			String[] paths = saveFileName.split(",");
			for (int i = 0; i < paths.length; i++) {
				Map<String, String> map = new HashMap<String, String>();
				FileUtil.deleteFile(paths[i]);
				String fileName = paths[i].split("_")[1];
            	map.put("fileName", fileName);
            	map.put("saveFileName", paths[i]);
            	operateFileList.add(map);
            	logger.info("删除文件 " + FileUtil.pathManipulation(fileName) +  " 成功");
			}
		}
		
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		retMap.put("operateFileList", operateFileList);
        responseBean.setRetMap(retMap);
        responseBean.setRetMsg("删除成功");
        String responseJsonStr = BaseUtil.transObj2Json(responseBean);
		logger.info("后台返回数据：" + responseJsonStr);
		return responseJsonStr;
	}
	
    /**
     * @author:		zheng.jw
     * @date:		2017年8月29日 上午11:23:23
     * @Description:TODO将文件写入到zip文件中
     */
    @SuppressWarnings("unchecked")
	public void writeZipFile(File inputFile, ZipOutputStream outputstream) throws Exception {
        
    	// 3. 获取名字，结合map构造加入zip的文件名称（动态更新map）
    	String file_name = inputFile.getName();
    	file_name = file_name.substring(file_name.lastIndexOf("_") + 1);
    	logger.info("file_name:" + file_name);
    	int count = 0;
    	String file_name_new = "";
    	if (map.get(file_name) == null) {
    		count++;
    		map.put(file_name, count);
    		file_name_new = file_name;
    	} else {
    		count = (int)map.get(file_name);
    		int temp = file_name.lastIndexOf(".");
    		file_name_new = file_name.substring(0,temp) + " (" + count + ")" + file_name.substring(temp);
    		count++;
    		map.put(file_name, count);
    	}
    	// 4. ZipEntry
    	ZipEntry entry = new ZipEntry(file_name_new);
		outputstream.putNextEntry(entry);
		InputStream is =null;
		try{
			 is = new FileInputStream(inputFile);
			byte[] b = new byte[4096];
			int len = 0;
		/*while ((is.read(b)) != -1) {
			outputstream.write(b);
		}*/
			while((len = is.read(b)) != -1) {
				outputstream.write(b,0,len);
			}
			// 关闭流
			is.close();
			outputstream.flush();
		}catch (Exception e){
			logger.error("",e);
		}finally {
			if(is!=null){
				is.close();
			}
		}

	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月19日 下午4:22:22
	 * @description: TODO（初始化默认ftp连接）
	 */
	private void initFtpUtil() throws Exception {
		if (ftpUtil == null) {
			ftpUtil = new FtpUtil();
			// 默认为AosConfig.properties中配置的ftp连接信息
			ftpUtil.connectAndLogin();
		}
	}
	
	/**
	 * @author:		 lewe
	 * @throws FtpException 
	 * @date:		 2017年4月19日 下午12:23:35
	 * @description: TODO（上传本地文件到ftp服务器）
	 */
	private boolean putFileToFtp(String localFile, String remoteFileName) throws Exception {
		return ftpUtil.putFile(localFile, remoteFileName);
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月19日 下午4:23:52
	 * @description: TODO（下载ftp服务器文件）
	 */
	private boolean getFileFromFtp(String localFile, String remoteFileName) throws FtpException {
		return ftpUtil.getFile(localFile, remoteFileName);
	}
	
	/**
	 * @author:		zheng.jw
	 * @date:		2017年8月29日 上午11:24:21
	 * @Description:推送前台消息
	 */
	private void sendMsg(User user, String msg) {
		try {
			if (WebSocketSessionUtils.hasConnection(user.getUserNo(), user.getLoginTerminal())) {
				HashMap<String, Object> msgMap = new HashMap<String, Object>();
	        	msgMap.put("msg_type", AOSConstants.MSG_TYPE_ALERT);
	        	msgMap.put("alert_msg", msg);
	        	msgMap.put("alert_type", "error");
				WebSocketSessionUtils.sendMessage(user.getUserNo(), user.getLoginTerminal(), BaseUtil.transObj2Json(msgMap));
			}
		} catch (Exception e) {
			logger.error("推送websocket消息异常：" + e.getMessage(), e);
		}
	}
}
