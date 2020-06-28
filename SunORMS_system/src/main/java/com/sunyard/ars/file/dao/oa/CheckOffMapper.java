package com.sunyard.ars.file.dao.oa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


import com.sunyard.ars.file.pojo.oa.CheckOff;

public interface CheckOffMapper {
    
    int deleteByPrimaryKey(@Param("occurDate") String occurDate, @Param("siteNo") String siteNo, @Param("operatorNo") String operatorNo);

    
    int insert(CheckOff record);

    
    int insertSelective(CheckOff record);

    
    CheckOff selectByPrimaryKey(@Param("occurDate") String occurDate, @Param("siteNo") String siteNo, @Param("operatorNo") String operatorNo);

    
    int updateByPrimaryKeySelective(CheckOff record);

    
    int updateByPrimaryKey(CheckOff record);
    
    /**
	 * 获取轧账状态
	 */
	public List<HashMap<String,String>> ifRollFlow(@Param("occurDate") String occurDate, @Param("siteNo") String siteNo, @Param("operatorNo") String operatorNo);
    
	/**
	 * 获取轧账状态
	 */
	public List<HashMap<String, Object>> getCheckOffState(HashMap<String, Object> condMap);
	
	public int deleteArchiveCheckOffData(HashMap<String, Object> condMap);

	/**
	 * 获取扎帐日期
	 * @return
	 */
	public List<HashMap<String, Object>> getCheckOffDate(HashMap<String, Object> condMap);
	/**
	 * 获取可归档的日期
	 * @return
	 */
	public List<HashMap<String, Object>> getArchiveDate(String organNo);

	/**
	 * 获取已归档的日期
	 * @return
	 */
	public List<HashMap<String, Object>> getAllArchivedDate(String organNo);
	/**
	 * 获取扎帐机构柜员列表
	 * @param condMap
	 * @return
	 */
	public List<HashMap<String, Object>> getRollInfo(HashMap<String, Object> condMap);
	
	/**
	 * 获取扎帐机构柜员列表
	 * @param condMap
	 * @return
	 */
	public List<HashMap<String, Object>> getArchiveRollInfo(HashMap<String, Object> condMap);
	/**
	 * 归档锁
	 * @param8 condMap
	 * @return
	 */
	public int checkOnLock(HashMap<String, Object> condMap);
	
	public int checkUnLock(HashMap<String, Object> condMap);
	//更新轧账表为归档状态
	public List<CheckOff> getArchiveCheckoff(HashMap<String, Object> condMap);
	public int updateCheckoffArchive(HashMap<String, Object> condMap);
	
	 /**
		 * 获取机构列表
		 * @param condMap
		 * @return
		 */
	public List<HashMap<String, Object>> getSiteNoByOper(HashMap<String, Object> condMap);
	
	//获取机构日期条件下的归档状态
	public List<HashMap<String, Object>> getOrganArchiveInfo(HashMap<String, Object> condMap);
	public List<HashMap<String, Object>> getArchiveInfo(HashMap<String, Object> condMap);
	
	Map getArchivedDate(@Param("occurDate") String occurDate, @Param("siteNo") String siteNo); 
	//自动轧账
	public int updateNormalAutoCheckoff(HashMap<String, Object> condMap);
	public List<HashMap<String, Object>> getNormalAutoCheckoff(HashMap<String, Object> condMap);
	//获取轧账信息
	public List<HashMap<String, Object>> getUncheckCheckoff(HashMap<String, Object> condMap);
	//强制轧账
	 int updateForceSelective(HashMap<String, Object> condMap);
	
}