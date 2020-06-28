package com.sunyard.aos.common.util;

import java.io.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author:		 lewe
 * @date:		 2017年2月24日 下午07:03:28
 * @description: TODO(文件操作工具类)
 */
public class FileUtil {
	
	/** 日志记录器 */
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午6:03:14
	 * @description: TODO(删除指定文件)
	 */
	public static void deleteFile(File file) {
		if (file != null && file.exists() && file.isFile()) {
			Boolean flag = file.getAbsoluteFile().delete();
			if (!flag) {
				System.gc();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error("删除文件出错", e);
				}
				file.getAbsoluteFile().delete();
			}
		}
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月17日 上午10:59:07
	 * @description: TODO（删除指定路径文件）
	 */
	public static void deleteFile(String path) {
		if (BaseUtil.isBlank(path)) {
			return;
		}

		File file = new File(pathManipulation(path));
		deleteFile(file);
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下去5:55:33
	 * @description: TODO(获取指定路径的文件分隔符)
	 */
	public static String getSeparator(String path) {
		/*if (BaseUtil.isBlank(path)) {
			return "";
		}
		String separator = "";
		if (path.charAt(0) == '/') { // 服务器为Aix、Linux等
			separator = "/";
		} else { // 服务器为Windows
			//separator = "\\";
			
			// 分隔符"/"应该是各系统都支持的，此处暂时统一使用
			separator = "/";
		}*/
		return BaseUtil.isBlank(path) ?  "" : "/" ;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下去5:55:33
	 * @description: TODO(校验指定目录，不存在的目录会自动创建)
	 */
	public static boolean checkDirectory(String path) {
		if (BaseUtil.isBlank(path)) {
			return false;
		}
		File dir = new File(pathManipulation(path));
		if (!dir.exists()) {
			return dir.mkdirs();
		}
		return true;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月19日 上午10:19:41
	 * @description: TODO（保存文件到本地目录）
	 */
	public static String saveLocalFile(MultipartFile mpFile, String filePath, String fileName) throws Exception {
		// 保存上传文件到本地临时目录
    	String localFileName = filePath + FileUtil.getSeparator(filePath) + fileName;
    	File localFile = new File(pathManipulation(localFileName));
    	mpFile.transferTo(localFile);
    	return localFileName;
	}

	/**
	 * 路径格式化
	 * @param path
	 * @return
	 */
	public static String pathManipulation(String path) {
		return FilenameUtils.normalize(path);
	}

	/**
	 * 关闭输入输出流
	 * @param is
	 * @param os
	 */
	public static void safeClose(InputStream is, OutputStream os) {
		try {
			if(os!=null){
				os.close();
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		try{
			if (is != null) {
				is.close();
			}
		}catch (Exception e) {
			logger.error("",e);
		}
	}

	public static void safeClose(InputStream is) {
		if (is != null) {
			try{
			is.close();
			}catch (Exception e) {
				logger.error("",e);
			}
		}
	}

	public static void safeClose(OutputStream os) {
		try {
			if(os!=null){
				os.close();
			}
		} catch (Exception e) {
			logger.error("",e);
		}
	}

	/**
	 *
	 * @param r
	 * @param w
	 */
	public static void safeClose(Reader r,Writer w) {
		try {
			if(w!=null){
				w.close();
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		try{
			if (r != null) {
				r.close();
			}
		}catch (Exception e) {
			logger.error("",e);
		}
	}

	/**
	 * 关闭输入输出流
	 * @param is
	 * @param os
	 */
	public static void safeClose(InputStream is, RandomAccessFile os) {
		try {
			if(os!=null){
				os.close();
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		try{
			if (is != null) {
				is.close();
			}
		}catch (Exception e) {
			logger.error("",e);
		}


	}

	public static void safeClose(XMLWriter w) {
		try {
			if(w!=null){
				w.close();
			}
		} catch (Exception e) {
			logger.error("",e);
		}
	}

}
