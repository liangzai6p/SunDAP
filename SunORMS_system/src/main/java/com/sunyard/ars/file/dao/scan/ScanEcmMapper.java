package com.sunyard.ars.file.dao.scan;

import java.util.HashMap;
import java.util.List;


public interface ScanEcmMapper {
    
	/**
	 * 获取batch_id,input_date
	 */
	public List<HashMap<String, Object>> getBatchIdandInputDate(HashMap<String, Object> condMap);
	
	/**
	 * 获取file_name,back_file_name
	 */
	public List<HashMap<String, Object>> getFileNamesList(HashMap<String, Object> condMap);

	/**
	 * 获取华夏影像系统IP httpPort 等
	 */
	public List<HashMap<String, Object>> getImageIPConfig(HashMap<String, Object> condMap);
	
	/**
	 * 获取file_name,back_file_name
	 */
	public List<String> getFileNames(String batchNo);

}