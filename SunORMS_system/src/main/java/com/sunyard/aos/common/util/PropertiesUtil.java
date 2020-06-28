package com.sunyard.aos.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author:		 lewe
 * @date:		 2017年3月17日 下午7:32:33
 * @description: TODO(属性文件操作工具类)
 */
public class PropertiesUtil {
	
	/** 日志记录器 */
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

	/** 属性文件 WEB-INF/AosConfig.properties 对象 */
	private static Properties aosConfigProp = new Properties();
	
	static {
		// 初始加载AosConfig.properties
		InputStream in = null;
		try {
			String webInfPath = HttpUtil.getAbsolutePath("WEB-INF");
			String fileName = webInfPath + File.separator + "AosConfig.properties";
			File file = new File(fileName);
			if (!file.exists()) {
				logger.error(webInfPath.concat("目录下无AosConfig.properties配置文件"));
			} else {
				logger.debug("AosConfig.properties配置文件路径为：".concat(fileName));
				 in = new FileInputStream(file);
				aosConfigProp.load(in);
				in.close();
			}
		} catch (Exception e) {
			logger.error("获取WEB-INF/AosConfig.properties配置文件信息异常：",e);
		}finally {
			FileUtil.safeClose(in);
		}
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月17日 下午7:46:33
	 * @description: TODO(获取指定属性值)
	 */
	public static String getValue(String name) {
		return aosConfigProp.getProperty(name, "");
	}
}
