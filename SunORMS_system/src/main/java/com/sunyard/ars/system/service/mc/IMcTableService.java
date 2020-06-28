package com.sunyard.ars.system.service.mc;

import java.util.HashMap;
import java.util.List;

import com.sunyard.ars.system.pojo.mc.McField;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * 
 * @date   2018年6月19日
 * @Description 字段配置
 */
public interface IMcTableService {
	ResponseBean execute(RequestBean requestBean) throws Exception;
	
	public void createTable(List<HashMap<String,Object>> tableDefInfo, String tableName,
			int tableType, String tableSpace, String indexSpace,
			List<McField> systemFields) throws Exception;
	
	public void createOrUpdateIndex(List<HashMap<String,Object>> tableDefInfo,
			String tableName, int tableType, String tableSpace,
			String indexSpace, List<McField> systemFields)
			throws Exception;
	
	public void createComment(List<HashMap<String,Object>> tableDefInfo, String tableName,int tableType) throws Exception;
	
	
}
