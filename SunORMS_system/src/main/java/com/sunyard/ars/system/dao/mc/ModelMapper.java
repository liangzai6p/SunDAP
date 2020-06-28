package com.sunyard.ars.system.dao.mc;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.system.pojo.mc.Model;




public interface ModelMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Model record);

    int insertSelective(Model record);

    Model selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Model record);

    int updateByPrimaryKey(Model record);
    
    List<Model> selectBySelective(Model record);
    
    /** 
	 * @Title: getModelByModelType 
	 * @Description: 按照模型类型获取模型
	 * @param modelType
	 * @return List<Model>
	 */
	public List<Model> getModelByModelTypeDao(@Param("modelType")String modelType);
	
	public List<Model> getAllForTree();
	
	/**
	 * @Title getModelRowIdsDao
	 * @Description 根据三大要素MODEL_ROW_ID
	 * @param data
	 * @return modelRowId
	 * @throws Exception
	 */
//	public String getModelRowIdsDao(DataTransmission data);
	
	public List<Model> getRealModelDao();


	/**
	 * 根据参数获取模型
	 * @param modelLevel
	 * @param modelBusiType
	 * @param userOrganNo
	 * @return
	 */
	List getUserModelInfos(HashMap<String,Object> condMap);

	Model getModelInfo(@Param("modelId") Integer modelId);
	
	List<HashMap<String,Object>> selectAllBusiInfo();
	
	/**
	 * 查询过滤模型角色和用户业务范围的模型ID
	 * @param condMap
	 * @return
	 */
	List<HashMap<String,Object>> selectFilterModel(@Param("userNo") String userNo);

	/**
     * 获取同组的模型
     * @param id
     * @return
     */
    List<Model> selectGroupModel(Integer id);
    
    /**
     * 实时预警模型查询
     * @param record
     * @return
     */
    List<Model> selectSmRealarmsModelId(Model record);
    
    List getUserBusiInfos(HashMap<String,Object> condMap);
}