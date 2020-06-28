package com.sunyard.aos.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excel表格操作工具类
 * 
 * @author zgz
 */
public class ExcelUtil {

	private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	/**
	 * 创建Excel文件
	 * @param path 文件存放路径(不含文件名)
	 * @param fileName 保存的文件名称
	 * @param title 标题
	 * @param head 表头
	 * @param data 文件数据
	 */
	@SuppressWarnings("rawtypes")
	public static boolean createExcelFile(String path, String fileName, String title, LinkedHashMap<String, String> head,  List<Map> data) {
		// 创建工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建工作表  
		HSSFSheet sheet =null;
		int  num=(data.size())%10000;  
		if(num==0){
			int  sheetNum=(data.size())/10000;
			for (int i = 0; i < sheetNum; i++) {
				sheet=workbook.createSheet("sheet"+(i+1));
				setTitleAndHead(workbook, sheet, head, title);
				getData(workbook, head, sheet, data, 10000*i, 10000*(i+1));
			}
		}else{
			int  sheetNum= (data.size())/10000+1;//1
			//如果当前也大于>全部数据！ 那么此时也可以这么解决
			for (int i = 0; i < sheetNum; i++) {
				sheet=workbook.createSheet("sheet"+(i+1));
				if(i==sheetNum-1){
					setTitleAndHead(workbook, sheet, head, title);
					getLastData(workbook, head, sheet, data, 10000*i);//取出最后一页！
				}else{
					//打印开始和中间页
					setTitleAndHead(workbook, sheet, head, title);
					getFirstData(workbook, head, sheet, data, 10000*i);
				}
			}
		}		
		//创建文件
		//File file = new File(FileUtil.pathManipulation(path+File.separator+fileName));
		File file = new File(FileUtil.pathManipulation(path+File.separator+fileName));
		File folder = file.getParentFile();
		if(!folder.exists()) {
			folder.mkdirs();
		}
		FileOutputStream xlsStream = null;
		try {
			xlsStream = new FileOutputStream(file);
			workbook.write(xlsStream);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("生成excel文件失败！", e);
			return false;
		}finally {
			if(xlsStream != null) {
				try {
					xlsStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 创建单元格样式
	 * @param workbook 工作薄
	 * @param fontType 字体
	 * @param fontSize 字号
	 * @param bold 是否加粗
	 * @param align 对其方式
	 * @return
	 */
	private static HSSFCellStyle getCellStyle(HSSFWorkbook workbook, String fontType, short fontSize, boolean bold, short align) {
		HSSFCellStyle style = workbook.createCellStyle(); // 样式对象   
		HSSFFont font = workbook.createFont(); //字体格式对象
		font.setFontName(fontType);
		font.setFontHeightInPoints(fontSize); //字体大小
		if(bold) {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //加粗
		}
		style.setFont(font); //设置样式字体
		style.setBorderBottom((short)1);//下边框
		style.setBottomBorderColor(HSSFColor.BLACK.index);//下边框颜色
		style.setBorderRight((short)1);//右边框
		style.setRightBorderColor(HSSFColor.BLACK.index);//右边框颜色
		style.setAlignment(align);
		style.setWrapText(true);
		return style;
	}
	public static void setTitleAndHead(HSSFWorkbook workbook,HSSFSheet sheet,LinkedHashMap<String, String> head,String title){
		//设置标题
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, head.size()-1));//单元格合并（开始行，开始列，结束行， 结束列）
		HSSFCellStyle styleTitle = getCellStyle(workbook, "宋体", (short)15, true, HSSFCellStyle.ALIGN_CENTER);
		HSSFRow titleRow = sheet.createRow(0); //创行
		HSSFCell cellTitle = titleRow.createCell(0);//创建单元格
		cellTitle.setCellValue(title);//设置单元格值
		cellTitle.setCellStyle(styleTitle);//设置单元格格式
		for(int i=1; i<head.size(); i++) {
			titleRow.createCell(i).setCellStyle(styleTitle);//补齐被合并的单元格格式。
		}
		//设置表头
		HSSFCellStyle styleHead = getCellStyle(workbook, "宋体", (short)11, true, HSSFCellStyle.ALIGN_LEFT);
		Set<String> keys = head.keySet();
		HSSFRow headRow = sheet.createRow(1);
		int headIndex = 0;
		for(String key : keys) {
			HSSFCell cellHead = headRow.createCell(headIndex);
			cellHead.setCellValue(head.get(key));
			cellHead.setCellStyle(styleHead);
			headIndex++;
		}
	}
	
	//1：获取全部数据!!! 当前数据为当前页的整数倍！！！
	public static void  getData(HSSFWorkbook workbook,LinkedHashMap<String, String> head,HSSFSheet sheet,List<Map> data,int start,int end){
		Set<String> keys = head.keySet();//表格数据
		HSSFCellStyle styleCell = getCellStyle(workbook, "宋体", (short)11, false, HSSFCellStyle.ALIGN_LEFT);
		for (int row =start; row <end ; row++) {
			Map dataRow = data.get(row);
			HSSFRow rows =sheet.createRow(row+2);
			int dataIndex = 0;
			for(String key : keys) {
				// 向工作表中添加数据
				HSSFCell cell = rows.createCell(dataIndex);
				if(dataRow.get(key) == null) {
					cell.setCellValue("");
				}else {
					cell.setCellValue(String.valueOf(dataRow.get(key)));
				}
				cell.setCellStyle(styleCell);
				dataIndex++;
			}
		}
		//宽度自适应（根据表头宽度适应，可给表头加空格控制宽度。）
		int cwIndex = 0;
		for(String key : keys) {
			sheet.setColumnWidth(cwIndex, head.get(key).getBytes().length*430);
			cwIndex ++;
		}
	}
	
	//2：非整除情况下！取出前几页
	public static void  getFirstData(HSSFWorkbook workbook,LinkedHashMap<String, String> head,HSSFSheet sheet,List<Map> data,int offset){
		Set<String> keys = head.keySet();//表格数据
		HSSFCellStyle styleCell = getCellStyle(workbook, "宋体", (short)11, false, HSSFCellStyle.ALIGN_LEFT);
		for (int row =0; row <10000 ;row++) {
			Map dataRow = data.get(row+offset);//
			HSSFRow rows =sheet.createRow(row+2);
			int dataIndex = 0;
			for(String key : keys) {
				// 向工作表中添加数据
				HSSFCell cell = rows.createCell(dataIndex);
				if(dataRow.get(key) == null) {
					cell.setCellValue("");
				}else {
					cell.setCellValue(String.valueOf(dataRow.get(key)));
				}
				cell.setCellStyle(styleCell);
				dataIndex++;
			}
		}
		//宽度自适应（根据表头宽度适应，可给表头加空格控制宽度。）
		int cwIndex = 0;
		for(String key : keys) {
			sheet.setColumnWidth(cwIndex, head.get(key).getBytes().length*430);
			cwIndex ++;
		}
	}
	
	//3: 非整除情况下！取出最后页面！
	public static void  getLastData(HSSFWorkbook workbook,LinkedHashMap<String, String> head,HSSFSheet sheet,List<Map> data,int offset){
		Set<String> keys = head.keySet();
		HSSFCellStyle styleCell = getCellStyle(workbook, "宋体", (short)11, false, HSSFCellStyle.ALIGN_LEFT);
		for (int row =0; row <data.size()-offset ; row++) {
			Map dataRow = data.get(row+offset);
			HSSFRow rows =sheet.createRow(row+2);
			int dataIndex = 0;
			for(String key : keys) {
				// 向工作表中添加数据
				HSSFCell cell = rows.createCell(dataIndex);
				if(dataRow.get(key) == null) {
					cell.setCellValue("");
				}else {
					cell.setCellValue(String.valueOf(dataRow.get(key)));
				}
				cell.setCellStyle(styleCell);
				dataIndex++;
			}
		}
		//宽度自适应（根据表头宽度适应，可给表头加空格控制宽度。）
		int cwIndex = 0;
		for(String key : keys) {
			sheet.setColumnWidth(cwIndex, head.get(key).getBytes().length*430);
			cwIndex ++;
		}
	}
	//取出最后一页！
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LinkedHashMap<String, String> head = new LinkedHashMap<String, String>();
		head.put("name", "姓名");
		head.put("age", "年龄");
		head.put("sex", "性别");
		List<Map> data = new java.util.ArrayList<Map>();
		Map map1 = new java.util.HashMap();
		map1.put("name", "张三");
		map1.put("age", "20222222222222");
		map1.put("sex", "男");
		data.add(map1);
		
		Map map2 = new java.util.HashMap();
		map2.put("name", "李四");
		map2.put("age", "30");
		map2.put("sex", "男");
		data.add(map2);
		
		Map map3 = new java.util.HashMap();
		map3.put("name", "小丽");
		map3.put("age", "20");
		map3.put("sex", "女2111111111111");
		data.add(map3);
		
		Map map4 = new java.util.HashMap();
		map4.put("name", "小X");
		map4.put("age", "20");
		map4.put("sex", "女");
		data.add(map4);
		
		Map map5 = new java.util.HashMap();
		map5.put("name", "小Y");
		map5.put("age", "20");
		map5.put("sex", "女");
		data.add(map5);
		createExcelFile("C:\\Users\\YHLZSAAA\\Desktop", "people.xls", "测试文件",head, data);
	}

}
