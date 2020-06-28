package com.sunyard.aos.common.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.FileUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sunyard.aos.common.util.ExportUtil;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;

/**
 * @author:		 lewe
 * @date:		 2017年3月10日 下午7:57:46
 * @description: TODO(数据导出控制器基类)
 */
@Controller
public class ExportController {
	
	// 日志记录器
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月10日 下午8:10:30
	 * @description: TODO(执行导出请求)
	 */
	@RequestMapping(value = "/exportExcel.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="数据导出", operationName="导出文件")
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			// 导出excel
			long startTime = System.currentTimeMillis();
			exportExcel(request, response);
			long endTime = System.currentTimeMillis();
			logger.debug("导出excel成功，耗时：" + (endTime - startTime) + " ms");
		} catch (Exception e) {
			logger.error("导出excel出错，" + e.getMessage(), e);
		}
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月10日 下午8:31:01
	 * @description: TODO(导出excel)
	 */
	protected void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取导出信息
		String fileName = request.getParameter("fileName");
		fileName = new String(fileName.getBytes("gb2312"), "iso-8859-1");
		String header = request.getParameter("headerInfo");
		String data = request.getParameter("dataInfo");
		// 生成Excel工作簿
		HSSFWorkbook wkb = ExportUtil.exportExcel(header, data, getDataList(request));
		// 返回前台提供下载
		response.setContentType("application/x-download;charset=utf-8");

        response.addHeader("Content-Disposition", "attachment;filename=" + BaseUtil.filterHeader(fileName));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        wkb.write(baos);
        OutputStream os = response.getOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        byte[] b = new byte[2048];
        while ((bais.read(b)) > 0) { // 分段读写，否则IE浏览器无法下载
            os.write(b);
        }
        bais.close();
        os.flush();
        os.close();
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月13日 下午4:20:24
	 * @description: TODO(获取需要导出的数据列表)
	 */
	protected List<Map<String, Object>> getDataList(HttpServletRequest request) {
		List<Map<String, Object>> dataList = null;
		
		// 子类继承时，添加具体的实现逻辑
		
		return dataList;
	}
}
