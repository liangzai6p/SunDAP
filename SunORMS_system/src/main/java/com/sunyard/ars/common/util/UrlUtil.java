package com.sunyard.ars.common.util;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class UrlUtil {
	/**
	    * 下载文件到本地
	    *
	    * @param urlString
	    *          被下载的文件地址
	    * @param filename
	    *          本地文件名
	    * @throws Exception
	    *           各种异常
	    */
	 public void download(String urlString, String filename) throws Exception {
	     // 构造URL
	     // 输入流
		InputStream is= null;
		// 输出的文件流
		OutputStream os = null;
		try {
			URL url = new URL(urlString);
			 // 打开连接
			 URLConnection con = url.openConnection();
			 is = con.getInputStream();


			 is.available();
//		 System.out.println(con.getContentLength());
//		 System.out.println(is.available());

			 // 1K的数据缓冲
			 byte[] bs = new byte[1024];
			 // 读取到的数据长度
			 int len;
			 os = new FileOutputStream(filename);
			 // 开始读取
			 while ((len = is.read(bs)) != -1) {
			   os.write(bs, 0, len);
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
		    
				try {
					if (os!=null) {
						os.close();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 try {
					if (is!=null) {
						 is.close();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
			
		}
	     // 完毕，关闭所有链接
	
	 }



}
