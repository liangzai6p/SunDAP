package com.sunyard.ars.common.comm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.ExcelUtil;
import com.sunyard.aos.common.util.FileUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;

public class ExportForQuery {
	
	private static long maxLen = 1000;
	
	/**
	 * 分析是否导出excel(包括是否需要导出，以及数据量是否超出最大可导出数)
	 * @param requestBean
	 * @param responseBean
	 * @param totalCount 符合条件的总数据条数
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean exportExcelAnalyse(RequestBean requestBean, ResponseBean responseBean, long totalCount) {
		Map sysMap = requestBean.getSysMap();
		String exportName = (String)sysMap.get("export_excel");
		if(exportName != null && exportName.length()>0) {
			SysParameter sys = ARSConstants.SYSTEM_PARAMETER.get("MAX_EXPORT_EXCEL_LEN");
			if(sys != null) {
				maxLen = Long.parseLong(sys.getParamValue());
			}
			if(totalCount <= maxLen) {
				return true;
			}else {
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("总数为："+totalCount+",大于最大可导出数："+maxLen+",无法全部导出！");
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 公共导出方法
	 * @param requestBean
	 * @param responseBean
	 * @param dataList 要导出的数据
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void exportExcle(RequestBean requestBean, ResponseBean responseBean, List dataList) throws Exception {
		Map sysMap = requestBean.getSysMap();
		String exportName = (String)sysMap.get("export_excel");
		String title = exportName.substring(0, exportName.lastIndexOf("."));
		List<Map> columnList = (List<Map>)sysMap.get("export_columns");
		String fileName = System.currentTimeMillis()+exportName;
		LinkedHashMap<String, String> headMap = new LinkedHashMap<String, String>();
		for(Map map : columnList) {
			String key = String.valueOf(map.get("name"));
			if(!headMap.containsKey(key) && !"gridCheckbox".equals(key)) {
				headMap.put(key, String.valueOf(map.get("label")));
			}
		}
		List<Map> data = new ArrayList<Map>();
		ObjectMapper objectMapper = new ObjectMapper();
		for(int i=0; i<dataList.size(); i++) {
			Object obj = dataList.get(i);
		    String contents = objectMapper.writeValueAsString(obj);
			Map map = objectMapper.readValue(contents, Map.class);
			data.add(map);
		}
		boolean retFlag = ExcelUtil.createExcelFile(ARSConstants.FILE_EXCEL_PATH, fileName, title, headMap, data);
		if(retFlag) {
			responseBean.getRetMap().put("filePath", FileUtil.pathManipulation(ARSConstants.FILE_EXCEL_PATH+ fileName));
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("生成成功");
		}else {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("xls文件生成失败！");
		}
	}

}
