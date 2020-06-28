package com.sunyard.ars.system.dao.brss;

import java.util.List;
import java.util.Map;

public interface ResuperviseMapper {

	/**查询货币信息*/
	List<Map<String,Object>> selectCurrentTypeInfo();
	
	
	/** 当抽取不是100时，删除BRSS_TEMP_SAMPLE_TB中数据 */
	int deleteBrssTempSampleByUserNo(String userNo);
	
	/** 查询BRSS_TEMP_SAMPLE_TB中数据 */
	List<Map<String,Object>> selectBrssTempSampleByUserNo(String userNo);
	
	
	/** 插入临时表 */
	int insertToBrssTempSample(Map<String,Object> map);
	
	
	/** 当抽取不是100并第一次抽取时，根据比率查询flowId */
	List<Map<String,Object>> selectFlowIdByConfig(Map<String,Object> conf);
	
	
	/** 根据临时表中的流水来查询数据 */
	List<Map<String,Object>> selectImageBySomeFlow(Map<String,Object> conf);
	
	
	/** 根据业务日期、柜员id、网点查询批次  PROGRESS_FLAG <= 99  IS_INVALID ='1' 查询批次*/
	List<Map<String,Object>> selectBatchInfo(Map<String,Object> conf);
	
	
	/**从临时数据表（$注入）中，根据图像序号 查询不重复的图像序号 */
	List<String> selectImgSerialNo(Map<String,Object> conf);
	
	
	/** 根据流水id、柜员id、机构代号、业务日期 查询单据 */
	List<Map<String,Object>> selectBusiFormBySelective(Map<String,Object> conf);
}
