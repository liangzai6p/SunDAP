package com.sunyard.ars.common.dao;

import java.util.HashMap;
import java.util.List;

public interface SmFieldDefTbMapper {
	/**
	 * 获取流水字段信息
	 * @return
	 */
	public List<HashMap<String, Object>> selectFlowFieldName();
	
	public List<HashMap<String, Object>> getFieldsByTableId(int tableId);

	public List<HashMap<String, Object>> getCheckFields(String formName);

	public List<HashMap<String, Object>> getFlowFieldsInfo(HashMap<String, Object> map);
	
	public List<HashMap<String, Object>> getFieldDicByModel(int modelId);
}