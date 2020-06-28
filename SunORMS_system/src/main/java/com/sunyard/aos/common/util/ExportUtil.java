package com.sunyard.aos.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @author:		 lewe
 * @date:		 2017年3月10日 下午6:15:26
 * @description: TODO(数据导出工具类，支持excel)
 */
public class ExportUtil {
	
	/** 行分隔符 */
	public static String SEPARATOR_ROW = "#R#";
	/** 列分隔符 */
	public static String SEPARATOR_COLUM = "#C#";
	/** 列名分隔符 */
	public static String SEPARATOR_NAME = "#N#";
	
	/** 表头字体 */
	public static HSSFFont headerFont = null;
	/** 表头样式 */
	public static HSSFCellStyle headerStyle = null;
	/** 单元格字体 */
	public static HSSFFont cellFont = null;
	/** 单元格样式 */
	public static HSSFCellStyle cellStyle = null;
	
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月10日 下午8:24:37
	 * @description: TODO(导出excel，依据表头和数据)
	 */
	public static HSSFWorkbook exportExcel(String header, String data, List<Map<String, Object>> dataList) {
		HSSFWorkbook wkb = new HSSFWorkbook();
		int sheetNum = 1;
		HSSFSheet sheet = createSheet(wkb, sheetNum);
		// 初始化字体样式
		initFontStyle(wkb);
		// 创建表头行，获取对应行数
		int rowNum = createSheetData(wkb, sheet, header, true, 0);
		if (BaseUtil.isBlank(data)) {
			return wkb;
		}
		String flag = data.substring(0, 1); // date 以  1| 或  2| 开头
		data = data.substring(2);
		if (BaseUtil.isBlank(data)) {
			return wkb;
		}
		// 创建数据行
		if ("1".equals(flag)) {
			// 直接数据，依据data数据直接创建
			createSheetData(wkb, sheet, data, false, rowNum);
		} else if ("2".equals(flag)) {
			// 间接数据，依据data中的列名顺序（用逗号分隔），依次从dataList中获取数据创建
			if (dataList == null || dataList.size() == 0) {
				return wkb;
			}
			String[] fieldArr = data.split("\\,");
			int startIdx = 0;
			for (int i = 0; i < dataList.size(); i++) {
				int rowIdx = (startIdx++) + rowNum;
				// 创建行
				HSSFRow row = sheet.createRow(rowIdx);
				for (int j = 0; j < fieldArr.length; j++) {
					String value = dataList.get(i).get(fieldArr[j]) == null ? "": dataList.get(i).get(fieldArr[j]) + "";
					// 创建单元格
					HSSFCell cell = createCell(row, j, false);
					cell.setCellValue(value);
				}
				if (rowIdx >= 65534) {
					sheetNum++;
					// 一个sheet表单最多包含 65535 行
					sheet = createSheet(wkb, sheetNum);
					rowNum = createSheetData(wkb, sheet, header, true, 0);
					startIdx = 0;
				}
			}
		}
		return wkb;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月10日 下午8:43:27
	 * @description: TODO(初始化字体样式对象)
	 */
	public static void initFontStyle(HSSFWorkbook wkb) {
		// 表头字体
		headerFont = createFont(wkb);
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 表头默认粗体显示
		// 单元格字体
		cellFont = createFont(wkb);
        cellFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 表头样式
    	headerStyle = createStyle(wkb);
    	headerStyle.setFont(headerFont);
        // 单元格样式
    	cellStyle = createStyle(wkb);
        cellStyle.setFont(cellFont);
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月10日 下午8:52:27
	 * @description: TODO(创建字体对象)
	 */
	public static HSSFFont createFont(HSSFWorkbook wkb) {
		HSSFFont font = wkb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)12); // 字体大小
		return font;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月10日 下午8:53:27
	 * @description: TODO(创建样式对象)
	 */
	public static HSSFCellStyle createStyle(HSSFWorkbook wkb) {
		HSSFCellStyle style = wkb.createCellStyle();
		style = wkb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 上下居中
		style.setLocked(true);
		style.setWrapText(true); // 文本换行
		style.setBorderTop(HSSFCellStyle.BORDER_THIN); // 上边框
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN); // 左边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN); // 右边框
		return style;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月10日 下午8:48:27
	 * @description: TODO(创建excel表单对象)
	 */
	public static HSSFSheet createSheet(HSSFWorkbook wkb, int num) {
		HSSFSheet sheet = wkb.createSheet("Sheet " + num);
		// 默认列宽
		sheet.setDefaultColumnWidth(20);
		return sheet;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月10日 下午7:11:02
	 * @description: TODO(创建excel表单sheet对应的数据行，返回创建的行数)
	 */
	public static int createSheetData(HSSFWorkbook wkb, HSSFSheet sheet, String data, boolean isHeader, int startRowIdx) {
		// 需要创建的数据行信息
		String[] rowArr = data.split(SEPARATOR_ROW);
		// 最新创建的每列跨越的行数信息（多级合并单元格）
		HashMap<Integer, Integer> lastcolMap = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < rowArr.length; i++) {
			// 当前的行索引
			int rowIdx = i + startRowIdx;
			// 创建行
			HSSFRow row = sheet.createRow(rowIdx);
			// 默认行高
			//row.setHeightInPoints(20);
			// 当前创建的列索引（包括跨列数）
			int colIdx = 0;
			String[] colArr = rowArr[i].split(SEPARATOR_COLUM);
			for (int j = 0; j < colArr.length; j++) {
				String[] colInfoArr = colArr[j].split(SEPARATOR_NAME);
				String[] spanInfoArr = colInfoArr[1].split("\\,");
				// 单元格值
				String value = colInfoArr[0];
				// 单元格跨行数
				int rowSpan = Integer.parseInt(spanInfoArr[0]);
				// 单元格跨列数
				int colSpan = Integer.parseInt(spanInfoArr[1]);
				// 创建单元格
				HSSFCell cell = createCell(row, colIdx, isHeader);
				while (lastcolMap.get(colIdx) != null && lastcolMap.get(colIdx) > 1) { // 说明上一行在colIdx对应的这一列是跨行的
					lastcolMap.put(colIdx, lastcolMap.get(colIdx) - 1);
					colIdx++;
					// 此处创建新的单元格便于应用统一样式，下同
					cell = createCell(row, colIdx, isHeader);
				}
				if (rowSpan > 1 || colSpan > 1) {
					// 合并单元格
					sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx + rowSpan - 1, colIdx, colIdx + colSpan - 1));
				}
				// 设置单元格值
                cell.setCellValue(value);
                lastcolMap.put(colIdx, rowSpan);
				for (int k = 1; k < colSpan; k++) {
					cell = createCell(row, colIdx + k, isHeader);
					lastcolMap.put(colIdx + k, rowSpan);
				}
				colIdx += colSpan;
			}
		}
		return rowArr.length;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月10日 下午07:58:26
	 * @description: TODO(创建单元格)
	 */
	public static HSSFCell createCell(HSSFRow row, int colIdx, boolean isHeader) {
        // 创建单元格
        HSSFCell cell = row.createCell(colIdx);
        if (isHeader) {
        	cell.setCellStyle(headerStyle);
        } else {
        	cell.setCellStyle(cellStyle);
        }
		return cell;
	}
}
