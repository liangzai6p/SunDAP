package com.sunyard.dap.intilligentSchedual.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

/**
 * @author:		 lewe
 * @date:		 2017年3月17日 下午7:32:33
 * @description: TODO(属性文件操作工具类)
 */
public class PropertiesUtil {
	
	/** 日志记录器 */
	protected static Log log = LogFactory.getLog(PropertiesUtil.class);
	/** 属性文件 WEB-INF/AosConfig.properties 对象 */
	private static Properties aosConfigProp = new Properties();

	static {
		// 初始加载AosConfig.properties
		InputStream in = null;
		InputStreamReader isr = null;
		try {
			String webInfPath = HttpUtil.getAbsolutePath("WEB-INF");
			System.out.println("webInfPath："+webInfPath);
			String fileName = webInfPath + File.separator + "AosConfig.properties";
			File file = new File(fileName);
			if (!file.exists()) {
				log.error(webInfPath.concat("目录下无AosConfig.properties配置文件"));
			} else {
				log.debug("AosConfig.properties配置文件路径为：".concat(fileName));
				System.out.println("AosConfig.properties配置文件路径为：".concat(fileName));
				in = new FileInputStream(file);
				System.out.println("file："+file);
				System.out.println("in："+in);
				isr = new InputStreamReader(in, "utf-8");
				System.out.println("isr："+isr);
				aosConfigProp.load(isr);
			}
		} catch (Exception e) {
			log.error("获取WEB-INF/AosConfig.properties配置文件信息异常：",e);
		} finally {
			try {
				if (isr != null) {
					isr.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				log.error("关闭IO流异常:",e);
			}
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
