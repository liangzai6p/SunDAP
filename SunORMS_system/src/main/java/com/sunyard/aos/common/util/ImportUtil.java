package com.sunyard.aos.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author:		 lewe
 * @date:		 2017年2月27日 上午09:21:50
 * @description: TODO(数据导入工具类)
 */
public class ImportUtil {

	// 日志记录器
	private static Logger logger = LoggerFactory.getLogger(ImportUtil.class);
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月27日 上午09:47:22
	 * @description: TODO(依据内容判断是否为excel2003及以下)
	 */
	public static boolean isExcel2003(String filePath) {
		BufferedInputStream bis =null;
		try {
			bis = new BufferedInputStream(new FileInputStream(filePath));
			if (POIFSFileSystem.hasPOIFSHeader(bis)) {
				bis.close();
				return true;
			}
		} catch (IOException e) {
			logger.error("判断Excel类型出错", e);
		}finally {
			FileUtil.safeClose(bis);
		}
		return false;
	}

	/**
	 * @author:		 lewe
	 * @date:		 2017年2月27日 上午09:49:08
	 * @description: TODO(依据内容判断是否为excel2007及以上)
	 */
	public static boolean isExcel2007(String filePath) {
		BufferedInputStream bis =null;
		try {
			 bis = new BufferedInputStream(new FileInputStream(filePath));
			if (POIXMLDocument.hasOOXMLHeader(bis)) {
				bis.close();
				return true;
			}
		} catch (IOException e) {
			logger.error("判断Excel类型出错", e);
		}finally {
			FileUtil.safeClose(bis);
		}
		return false;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月27日 上午09:49:55
	 * @description: TODO(数据导入，支持2003、2007，返回对应数据的ListMap集合，key为对应的列索引)
	 */
	public static List<HashMap<String, String>> importExcel(String filePath, int headerRowNum, boolean isDelete) throws Exception {
		File file = null;
		InputStream inputStream = null;
		
		// 用于存放Excel中的数据，key为列索引
		List<HashMap<String, String>> dataList = null;
		try {
			// 读取导入文件
			file = new File(filePath);
			inputStream = new FileInputStream(file);
	
			Workbook wb = null;
			// 创建Excel工作簿对象  
			if (isExcel2003(filePath)) {
				wb = new HSSFWorkbook(inputStream);
			} else if (isExcel2007(filePath)) {
				wb = new XSSFWorkbook(inputStream);
			}

			if (wb == null) {
				logger.error("导入文件格式有误！");
				return dataList;
			}
			for (int numSheets = 0; numSheets < wb.getNumberOfSheets(); numSheets++) {
				// 获得一个sheet
				Sheet sheet = wb.getSheetAt(numSheets);
				if (sheet != null) {
					if (dataList == null) {
						dataList = new ArrayList<HashMap<String,String>>();
					}
					for (int rowNumOfSheet = sheet.getFirstRowNum() + headerRowNum; rowNumOfSheet <= sheet.getLastRowNum(); rowNumOfSheet++) {
						// 获得一行
						Row row = sheet.getRow(rowNumOfSheet);
						if (row != null) {
							HashMap<String, String> result = new HashMap<String, String>();
							for (int cellNumOfRow = row.getFirstCellNum(); cellNumOfRow < row.getLastCellNum(); cellNumOfRow++) {
								// 获得列值
								Cell cell = row.getCell(cellNumOfRow);
								if (cell != null) {
									result.put(cellNumOfRow + "", getCellStringValue(cell));
								} else {
									result.put(cellNumOfRow + "", "");
								}
							}
							dataList.add(result);
						}
					}
				}	
			}
		} catch (Exception e) {
			logger.error("解析Excel数据失败", e);
		} finally {
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
			if (isDelete && file.exists() && file.isFile()) {
				Boolean flag = file.getAbsoluteFile().delete();
				if (!flag) {
					System.gc();
					Thread.sleep(1000);
					file.getAbsoluteFile().delete();
				}
				logger.info("文件删除成功");
			}
		}
		
		return dataList;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月27日 上午09:54:21
	 * @description: TODO(获取单元格数据字符串)
	 */
	public static String getCellStringValue(Cell cell) {
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			String value = String.valueOf(cell.getNumericCellValue());
			String[] s = value.split("\\.");
			if (s.length > 1 && s[1].equals("0")) {
				value = s[0];
			}
			return value;
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			return String.valueOf(cell.getCellFormula());
		} else {
			return "";
		}
	}
}
