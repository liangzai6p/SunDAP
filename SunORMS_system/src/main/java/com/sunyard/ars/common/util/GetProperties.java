package com.sunyard.ars.common.util;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
/**
 * 用于读取配置文件
 *
 */
public class GetProperties {
	
	private  static Properties pro;
	
    //参数
//	public static String totalpktsize;
//	public static String putblksize;
//	public static String src;
//	public static String snd;
//	public static String rcv;
//	public static String optcode;
//	public static String ysyid;
//	public static String loginum;
	

	static {
		InputStream in = null;
		 pro = new Properties();
		try {
			in = new GetProperties().getClass().getResourceAsStream("/SinoTParm.properties");
			pro.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		totalpktsize = pro.getProperty("sino.totalpktsize");
//		putblksize = pro.getProperty("sino.putblksize");
//		src = pro.getProperty("sino.src");;
//		snd = pro.getProperty("sino.snd");
//		rcv = pro.getProperty("sino.rcv");
//		optcode = pro.getProperty("sino.optcode");
//		ysyid = pro.getProperty("sino.ysyid");
//		loginum = pro.getProperty("sino.loginum");
	
	}
	
	public static String get(String key){
		return pro.getProperty(key);
	}

}
