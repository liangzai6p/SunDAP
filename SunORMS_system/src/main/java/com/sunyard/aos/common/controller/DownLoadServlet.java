package com.sunyard.aos.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunyard.aos.common.util.BaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunyard.aos.common.util.FileUtil;

public class DownLoadServlet extends HttpServlet {

	/**
	 * @author linq.cheng
	 * @date 2017年4月27日 上午9:46:25
	 * @description TODO
	 */
	private static final long serialVersionUID = 542699100461985129L;
	// 日志记录器
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 附件预览标识
		String pdf_flag = request.getParameter("pdf_flag");
		
		// 得到要下载的文件名
		String fileName = request.getParameter("fileName"); 
		String saveFileName = request.getParameter("saveFileName");
		
		fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
		
		saveFileName = new String(saveFileName.getBytes("ISO-8859-1"), "UTF-8");
		File file = new File(FileUtil.pathManipulation(saveFileName));
		FileInputStream in =null;
		OutputStream out =null;
		try{
		// 如果文件不存在
		if (!file.exists()) {
			logger.error("文件下载失败，指定文件不存在：" + BaseUtil.filterLog(saveFileName));
			return;
		}

		response.setContentType("application/x-download;charset=utf-8");
		// 设置响应头，控制浏览器下载该文件
		response.setHeader("content-disposition", "attachment;fileName=" + URLEncoder.encode(BaseUtil.filterHeader(fileName), "UTF-8"));
			// 读取要下载的文件，保存到文件输入流
			 in = new FileInputStream(FileUtil.pathManipulation(saveFileName));
			// 创建输出流
			 out = response.getOutputStream();
			// 创建缓冲区
			byte buffer[] = new byte[1024];
			int len = 0;
			// 循环将输入流中的内容读取到缓冲区当中
			while ((len = in.read(buffer)) > 0) {
				// 输出缓冲区的内容到浏览器，实现文件下载
				out.write(buffer, 0, len);
			}
			// 关闭文件输入流
			in.close();
			// 关闭输出流
			out.close();
			if ("true".equals(pdf_flag)) {
				FileUtil.deleteFile(saveFileName);
			}
		}catch(Exception e){
			logger.error("",e);
		}finally {
			if(in !=null ){
				in.close();
			}
			if(out!=null){
				out.close();
			}
		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}