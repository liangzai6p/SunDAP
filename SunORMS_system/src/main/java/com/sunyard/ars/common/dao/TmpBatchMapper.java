package com.sunyard.ars.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.common.pojo.TmpBatch;

public interface TmpBatchMapper {

    int deleteByPrimaryKey(String batchId);

    int insert(TmpBatch record);

    int insertSelective(TmpBatch record);

    TmpBatch selectByPrimaryKey(String batchId);

    int updateByPrimaryKeySelective(TmpBatch record);

    int updateByPrimaryKey(TmpBatch record);
    
    /**
     * 获取补扫列表
     * @param map
     * @return
     */
    List<HashMap<String,Object>> getPatchTaskList(HashMap<String,Object> map);
    
    /**
     * 更新批次
     * @return
     */
	int updateTmpBatch(HashMap<String,Object> map);
	
	/**
	 * 更新补扫任务标示
	 * @param batchId
	 */
	public void updateBatchTask(@Param("batchId")String batchId);
	
	/**
	 * 更新批次总数
	 * @param map
	 * @return
	 */
	int updateTotalPage(HashMap<String, Object> map);
	
	/**
	 * 插入补扫任务
	 * @param condMap
	 * @return
	 */
	int insertPatchTask(HashMap<String, Object> condMap);
	
	/**
	 * 根据柜员号，网点号，日期获得未勾对凭证数
	 * @param condMap
	 * @return
	 */
	int getUncheckImgCount(HashMap<String, Object> condMap);
	
	
	/**
	 * 查询批次补扫任务
	 * @param batchId
	 * @return
	 */
	List<HashMap<String,Object>> selectPatchTask(@Param("batchId")String batchId);
    
	/**
	 * 查询批次信息
	 * @param condMap
	 * @return
	 */
	List<HashMap<String,Object>> selectBatchList(HashMap<String, Object> condMap);

	/**
	 * 获取机构存储表名
	 * @param condMap
	 * @return
	 */
	List<HashMap<String,Object>> getTableName(HashMap<String, Object> condMap);

	/**
	 * 根据条件查询符合批次信息
	 * @param condMap
	 * @return
	 */
	List<HashMap<String,Object>> getBatchInfo(HashMap<String, Object> condMap);

	/**
	 * 获取扫描上来可以进自动识别的批次队列
	 * @return
	 */
	List<HashMap<String,Object>> getScanBatch();
	
	/**
	 * 更新工作流实例id
	 * @param map
	 * @return
	 */
	int updateFlowId(HashMap<String, Object> map);
	
	/**
	 * 获取扫描上来可以进自动识别的批次队列
	 * @return
	 */
	List<HashMap<String,Object>> getFlowUserList(HashMap<String, Object> condMap);
	List<HashMap<String,Object>> getFlowUserList2(HashMap<String, Object> condMap);
	
	//条件下是否有轧账批次
	int hasArchiveBatch(HashMap<String, Object> condMap);
	//条件下可轧账批次
	List<TmpBatch> getArchiveBatchs(HashMap<String, Object> condMap);
	//是否是凭证
	int isVouh(List ids);
	//为凭证时的批次
	List<TmpBatch> getVouh(HashMap<String, Object> condMap);
	//删除历史表进行回滚
	int deletehisBatchData(List ids);
	int tmpbatchBackArchive(List ids);
	//更新批次表为轧账状态
	int updateBatchArchive(List ids);
	
	int archiveCurrentBatch();
	
	
	
	
	int updateArchiveBatch(HashMap<String, Object> condMap);
	
	int existsBatch(HashMap<String, Object> condMap);
	
	int patchBatchLock(String batchID);
	int patchBatchUnlock(String batchID);
 
	
	List getPatchBatchs(HashMap<String, Object> condMap);
	
	int updateSendstatus(@Param("batchId")String batchId,@Param("contentId")String contentId);
	
    List getBatchCount(@Param("siteNo")List<String> siteNo, @Param("occurDate")String occurDate);
    List getBatchCount2(@Param("tellers")List<String> tellers, @Param("occurDate")String occurDate);

    
    
	/**
	 * 批次进工作流加锁
	 * @param batchId
	 * @return
	 */
	int batchToFlowLock(@Param("batchId")String batchId,@Param("batchLockTime") int batchLockTime);


    /**
     * 通过网点 柜员  业务日期 流水号 获取批次号和扫描日期
     * @param condMap
     * @return
     */
    public List<HashMap<String, Object>> getBatchIdandInputDate(HashMap<String, Object> condMap);
    /**
	 * 通过批次号和流水获取file_name,back_file_name
	 */
	public List<HashMap<String, Object>> getFileNamesList(HashMap<String, Object> condMap);
	
	/**
	 * 按条件查询用户，权限范围内的批次
	 * @param condMap
	 * @return
	 */
	public List<TmpBatch> selectBySelective(HashMap<String, Object> condMap);

}