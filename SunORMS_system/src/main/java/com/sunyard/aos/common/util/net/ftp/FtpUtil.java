package com.sunyard.aos.common.util.net.ftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.FileUtil;
import com.sunyard.aos.common.util.HttpUtil;
import com.sunyard.aos.common.util.PropertiesUtil;
import com.sunyard.aos.common.util.net.ftp.FtpException;

/**
 * @author:		 lewe
 * @date:		 2017年4月6日 下午5:20:13
 * @description: TODO(FTP操作工具类，实现文件读取，采用apache的FTPClient作为基础类库)
 */
public class FtpUtil {
	
	// 日志记录器
	private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);
	
	// FTP客户端
	private FTPClient ftpClient = null;
	// 临时存储路径
	private String localPath = HttpUtil.getAbsolutePath("temp");
	// FTP连接超时时长
	private final int TIMEOUT = 10*1000;
	// 文件名称编码
	private String fileNameEncode = "GBK";
	// ftp文件转码
	private String ftpEncode = "ISO-8859-1";
	// 当前连接的用户名
	private String userName = "";
	// ftp服务器文件分隔符
	private String fileSeparator = "/";
	
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午5:38:22
	 * @description: TODO（连接ftp服务器，并建立用户会话，默认为AosConfig.properties中配置的ftp连接信息）
	 */
	public void connectAndLogin() throws Exception {
		String ip = PropertiesUtil.getValue("ftp.ip").trim();
		int port = Integer.parseInt(PropertiesUtil.getValue("ftp.port").trim());
		String user = PropertiesUtil.getValue("ftp.user").trim();
		String password = PropertiesUtil.getValue("ftp.password").trim();
		connectAndLogin(ip, port, user, password);
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午5:39:22
	 * @description: TODO(连接ftp服务器，并建立用户会话)
	 */
	public void connectAndLogin(String ip, int port, String user, String password) throws Exception {
		// 建立ftp连接
		connect(ip, port);
		// 建立用户会话
		login(user, password);
		// 设置超时时长
		setTimeout(TIMEOUT);
		// 设置文件传输类型
		setFileType(FTP.BINARY_FILE_TYPE);
		// 设置传输编码
		try {
			if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
				fileNameEncode = "UTF-8";
				logger.debug("已设置FTP服务传输编码为 " + fileNameEncode);
			} else {
				logger.debug("默认FTP服务传输编码为 " + fileNameEncode);
			}
		} catch (IOException e) {
			logger.error("设置FTP服务传输编码异常", e);
		}
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午5:38:22
	 * @description: TODO(建立ftp连接)
	 */
	public void connect(String ip, int port) throws Exception {
		try {
			ftpClient = new FTPClient();
			ftpClient.connect(ip, port);
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				ftpClient.disconnect();
				throw new FtpException("FTP服务器拒绝连接！");
			}
		} catch (Exception e) {
			ftpClient = null;
			throw new FtpException("FTP连接异常，IP[" + ip +"],PORT[" + port + "]", e);
		}
		logger.info("已连接到FTP服务器，IP[" + BaseUtil.filterLog(ip) + "],PORT[" + BaseUtil.filterLog(port+"") + "] " + ftpClient.getReplyString().trim());
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午6:32:55
	 * @description: TODO(建立用户会话)
	 */
	public void login(String user, String password) throws Exception {
		this.userName = user;
		boolean isOk;
		try {
			isOk = ftpClient.login(user, password);
		} catch (IOException e) {
			throw new FtpException("FTP操作|用户[" + user + "]登录异常！", e);
		}
		if (isOk) {
			logger.info("FTP操作|用户[" + BaseUtil.filterLog(user) + "]登录成功, 当前路径为：" + FileUtil.pathManipulation(getCurrentDirectory()));
		} else {
			throw new FtpException("FTP操作|用户[" + user + "]登录失败！");
			//throw new FtpException("FTP操作|用户[" + user + "]登录失败，密码[" + password + "]");
		}
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午6:17:44
	 * @description: TODO(设置文件传输类型，包括 FTP.BINARY_FILE_TYPE 、FTP.ASCII_FILE_TYPE)
	 */
	public void setFileType(int fileType) throws FtpException {
		try {
			ftpClient.setFileType(fileType);
		} catch (IOException e) {
			throw new FtpException("FTP操作|文件传输类型[" + fileType + "]设置异常！", e);
		}
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午6:19:31
	 * @description: TODO(设置客户端连接超时时间，单位毫秒)
	 */
	public void setTimeout(int mill) throws FtpException {
		try {
			ftpClient.setSoTimeout(mill);
		} catch (SocketException e) {
			throw new FtpException("FTP操作|设置连接超时参数异常，时间[" + mill + "毫秒]", e);
		}
		//apacheFTP.setDataTimeout(mill);
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午6:17:44
	 * @description: TODO(设置文件传输编码，包括 GBK 或 UTF-8，一般用于编码文件名称为 ISO-8859-1 格式，避免名称乱码)
	 */
	public void setFileCode(String fileCode) throws FtpException {
		if (!BaseUtil.isBlank(fileCode)) {
			fileNameEncode = fileCode;
		}
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午6:08:17
	 * @description: TODO(获取当前工作目录)
	 */
	public String getCurrentDirectory() {
		String currentPath = null; 
		try {
			currentPath = decodeName(ftpClient.printWorkingDirectory());
		} catch (IOException e) {
			logger.error("FTP操作|获取当前路径出错！", e);
		}
		return currentPath;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午5:39:47
	 * @description: TODO(切换工作目录)
	 */
	public boolean changeDirectory(String path) throws Exception {
		if (ftpClient == null || BaseUtil.isBlank(path)) {
			return false;
		}
		boolean isOk;
		try {
			if ("..".equals(path)) {
				isOk= ftpClient.changeToParentDirectory();
			} else {
				isOk = ftpClient.changeWorkingDirectory(encodeName(path));
			}
		} catch (IOException e) {
			throw new FtpException("FTP操作|更改工作目录[" + path + "]异常！", e);
		}
		if (isOk) {
			logger.info("FTP操作|已进入工作目录[" + FileUtil.pathManipulation(path) + "]");
		} else {
			logger.info("FTP操作|更改工作目录[" + FileUtil.pathManipulation(path) + "]失败！");
		}
		return isOk;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午5:42:06
	 * @description: TODO(创建子目录，默认在当前工作目录下面)
	 * 
	 * @param path   子目录名称，支持多级目录，如 11/22/33 。需要创建完整路径时，可调用 checkDirectory 方法。
	 */
	public boolean makeDirectory(String path) throws Exception {
		if (ftpClient == null || BaseUtil.isBlank(path)) {
			return false;
		}
		boolean isOk = false;
		try {
			String createPath = getCurrentDirectory();
			String[] pathArr = path.split(fileSeparator);
			for (int i = 0; i < pathArr.length; i++) {
				if (BaseUtil.isBlank(pathArr[i])) {
					continue;
				}
				createPath += fileSeparator + pathArr[i];
				isOk = ftpClient.makeDirectory(encodeName(createPath));
			}
			ftpClient.sendSiteCommand("chmod 755 " + encodeName(path));
			logger.info("FTP操作|已创建子目录[" + path + "]");
		} catch (IOException e) {
			throw new FtpException("FTP操作|创建子目录[" + path + "]异常！", e);
		}
		return isOk;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午6:44:19
	 * @description: TODO(校验指定目录，对于不存在的目录层级会自动创建)
	 * 
	 * @param path	 完整路径
	 */
	public boolean checkDirectory(String path) throws FtpException {
		if (ftpClient == null || BaseUtil.isBlank(path)) {
			return false;
		}
		boolean isOk = false;
		try {
			FTPFile[] ff = ftpClient.listFiles(encodeName(path));
			if (ff != null && ff.length > 0) {
				// path 对应的目录已存在
				return true;
			}
			String _path = path;
			String childPath = "";
			while ((isOk = ftpClient.makeDirectory(encodeName(_path))) == false) {
				int index = _path.lastIndexOf(fileSeparator);
				if (index == _path.length() - 1) {
					_path = _path.substring(0, index);
					index = _path.lastIndexOf(fileSeparator);
				}
				if (index == -1) {
					break;
				}
				childPath = fileSeparator + _path.substring(index + 1) + childPath;
				_path = _path.substring(0, index);
				if (BaseUtil.isBlank(_path) || fileSeparator.equals(_path)) {
					break;
				}
			}
			if (isOk) {
				if (!"".equals(childPath)) {
					childPath = childPath.substring(1);
					String[] childPathArr = childPath.split(fileSeparator);
					String createPath = _path;
					for (int i = 0; i < childPathArr.length; i++) {
						if (BaseUtil.isBlank(childPathArr[i])) {
							continue;
						}
						createPath += fileSeparator + childPathArr[i];
						ftpClient.makeDirectory(encodeName(createPath));
					}
				}
				logger.info("FTP操作|已创建子目录[" + FileUtil.pathManipulation(path) + "]");
				ftpClient.sendSiteCommand("chmod 755 " + path);
			} else {
				logger.error("FTP操作|创建子目录[" + FileUtil.pathManipulation(path) + "]失败！");
			}
		} catch (Exception e) {
			throw new FtpException("FTP操作|创建子目录[" + path + "]异常！", e);
		}
		
		return isOk;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午5:42:22
	 * @description: TODO(获取ftp服务器文件)
	 * 
	 * @param localFile 		完整的本地文件存储路径名称
	 * @param remoteFileName	远程ftp文件名称
	 */
	public boolean getFile(String localFile, String remoteFileName) throws FtpException {
		if (ftpClient == null || BaseUtil.isBlank(localFile) || BaseUtil.isBlank(remoteFileName)) {
			return false;
		}
		boolean isOk = false;
	    FileOutputStream fos = null;
	    try {
	    	fos = new FileOutputStream(FileUtil.pathManipulation(localFile));
	    	// 文件名含有中文时，需要转码
	    	isOk = ftpClient.retrieveFile(encodeName(remoteFileName), fos);
	    	if (!isOk) {
				logger.error("FTP操作|文件[" + FileUtil.pathManipulation(remoteFileName) + "]下载失败！");
			}
	    } catch (Exception e) {
	    	throw new FtpException("FTP操作|文件[" + remoteFileName + "]下载异常！", e); 
	    } finally {  
	    	try {
	    		if (fos != null) {
	    			fos.close();
	    			fos = null;
	    		}
			} catch (Exception e) {
				logger.error("FTP操作|关闭下载文件流异常！", e);
			}
	    }  
	    return isOk; 
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午5:43:26
	 * @description: TODO(上传文件至ftp服务器)
	 * 
	 * @param localFile 		完整的本地文件存储路径名称
	 * @param remoteFileName	远程ftp文件名称，为空时默认为本地文件名称
	 */
	public boolean putFile(String localFile, String remoteFileName) throws Exception {
		if (ftpClient == null || BaseUtil.isBlank(localFile)) {
			return false;
		}
		boolean isOk = false;
		FileInputStream fis = null;
		try {
			File file = new File(localFile);
			fis = new FileInputStream(file);
			if (BaseUtil.isBlank(remoteFileName)){
				remoteFileName = file.getName();
			}
			// 文件名含有中文时，需要转码
			isOk = ftpClient.storeFile(encodeName(remoteFileName), fis);
			if (!isOk) {
				logger.error("FTP操作|文件[" + localFile + "]上传失败！");
			}
		} catch (FileNotFoundException e) {
			throw new FtpException("FTP操作|本地文件[" + localFile + "]不存在！");
		} catch (IOException e) {
			throw new FtpException("FTP操作|文件[" + localFile + "]上传异常！",e);
		} finally {
			try {
				if (fis != null) {
					fis.close();
					fis = null;
				}
			} catch (Exception e) {
				logger.error("FTP操作|关闭上传文件流异常！", e);
			}
		}
		return isOk;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月18日 上午11:11:45
	 * @description: TODO（删除FTP服务器上指定文件）
	 */
	public boolean deleteFile(String fileName) throws Exception{
		if (ftpClient == null || BaseUtil.isBlank(fileName)) {
			return false;
		}
		boolean flag = false;
		try {
			flag = ftpClient.deleteFile(encodeName(fileName));
		} catch (IOException e) {
			logger.error("FTP操作|删除文件[" + fileName + "]异常", e);
		}
		return flag;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月19日 上午9:18:06
	 * @description: TODO（获取指定路径的文件信息列表，包括文件名、是否是文件或目录、大小、类型等）
	 */
	public FTPFile[] getFileList(String path) throws Exception{
		/*
		  FTPClient对象获取文件列表信息方法说明：
		  
		  listFiles() 		获取当前工作目录的文件信息列表，包含文件、目录，返回 数组对象 FTPFile[]
		  listFiles(path) 	获取path路径对应的文件信息列表，包含文件、目录，path路径不存在时，返回 数组对象 FTPFile[]长度为0
		  listNames()		获取当前工作目录下的文件名称列表，只包含文件，不包含目录，返回 数组对象 String[]
		  listNames(path)	获取path路径对应的文件名称列表，只包含文件，不包含目录，path路径不存在时，返回 null
		 */
		if (ftpClient == null) {
			return null;
		}
		FTPFile[] ff = null;
		try {
			if (BaseUtil.isBlank(path)) {
				// 获取当前工作目录的文件信息列表
				ff = ftpClient.listFiles();
			} else {
				// 获取指定路径path的文件信息列表
				ff = ftpClient.listFiles(encodeName(path));
			}
		} catch (IOException e) {
			logger.error("获取指定路径[" + path + "]的文件信息列表异常", e);
		}
		return ff;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午5:40:10
	 * @description: TODO(读取当前工作目录下指定txt文件内容)
	 * 
	 * @param fileName	文件名称
	 * @param separator	数据分隔符
	 * @param encode	文件编码
	 */
	public String[][] readTxtData(String fileName, String separator, String encode) throws FtpException {
		if (ftpClient == null || BaseUtil.isBlank(fileName)) {
			return null;
		}
		if (BaseUtil.isBlank(encode)) {
			encode = AOSConstants.ENCODE;
		}
		// 本地临时目录
		try {
			localPath = HttpUtil.getAbsoluteTempPath();
		} catch (Exception e) { }
		// 本地临时文件名称
		String localFileName = localPath + File.separator + fileName;
		
		String[][] retData = null;
		File file = null;
		InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
		try {
			// 创建本地临时目录
			file = new File(localPath);
			if (!file.exists()) {
				file.mkdirs();
				logger.debug("创建本地临时目录：" + localPath);
			}
			// 创建本地临时文件
			file = new File(localFileName);
			if (!file.exists()) {
				file.createNewFile();
				logger.debug("创建本地临时文件：" + localFileName);
			}
			// 获取ftp文件，读取数据
			getFile(localFileName, fileName);
			logger.debug("获取远程ftp站点文件：" + fileName + ",开始读取数据:");
	        is = new FileInputStream(localFileName);
	        isr = new InputStreamReader(is, encode);// 考虑编码格式
	        br = new BufferedReader(isr);
	        String line = null;
	        ArrayList<String[]> list = new ArrayList<String[]>();
	        while ((line = br.readLine()) != null) {
	        	if (BaseUtil.isBlank(line)) {
	        		continue;
	        	}
	            list.add(line.split(separator));
	        }
	        // 构造返回数组
	        retData = new String[list.size()][];
	        for (int m = 0; m < list.size(); m++) {
	        	retData[m] = list.get(m);
	        }
			logger.debug("数据读取完毕，共读取 " + retData.length + " 行 ");
		} catch (Exception e) {
			throw new FtpException("FTP操作|读取txt数据异常", e);
		} finally {
			try {
				// 关闭文件流
				FileUtil.safeClose(br,null);
				FileUtil.safeClose(isr,null);
				FileUtil.safeClose(is);
				// 删除本地临时文件
				FileUtil.deleteFile(file);
				logger.debug("删除本地临时文件：" + localFileName);
			} catch (Exception e) {
				logger.error("FTP操作|关闭文件流异常", e);
			}
		}
        return retData;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午6:24:40
	 * @description: TODO（对指定名称进行编码）
	 */
	public String encodeName(String name) throws Exception{
		String retName = name;
		try {
			retName = new String(name.getBytes(fileNameEncode), ftpEncode);
		} catch (UnsupportedEncodingException e) {
			logger.error("指定名称[" + BaseUtil.filterLog(name) + "]编码异常", e);
		}
		return retName;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午6:26:40
	 * @description: TODO（对指定名称进行解码）
	 */
	public String decodeName(String name) {
		String retName = name;
		try {
			retName = new String(name.getBytes(ftpEncode), fileNameEncode);
		} catch (UnsupportedEncodingException e) {
			logger.error("指定名称[" + name + "]解码异常", e);
		}
		return retName;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午5:42:40
	 * @description: TODO（关闭ftp服务连接）
	 */
	public void disconnect() {
		if (ftpClient != null) {
			try {
				ftpClient.logout();
				ftpClient.disconnect();
				ftpClient = null;
				logger.info("FTP操作|用户[" + BaseUtil.filterLog(userName) + "]已退出, FTP连接已关闭！");
			} catch (Exception e) {
				logger.error("FTP操作|关闭FTP连接异常！", e);
			}
		}
	}
	

}