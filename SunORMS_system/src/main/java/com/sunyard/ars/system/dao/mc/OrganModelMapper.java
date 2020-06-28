package com.sunyard.ars.system.dao.mc;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.system.pojo.mc.OrganModel;

public interface OrganModelMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(OrganModel record);

    int insertSelective(OrganModel record);

    OrganModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrganModel record);

    int updateByPrimaryKey(OrganModel record);
    
    /**
     * 机构模型查询
     * @param record
     * @return
     */
    List<OrganModel> selectBySelective(OrganModel record);
    
	/**
	 * @Title: deleteOrganModel
	 * @Description: 机构模型删除
	 */
	public void deleteOrganModel(@Param("organNo")String organNo);
	
	int deleteOrganModelByModel(@Param("modelId")Integer modelId);
	
	int insertBatchOrganModel(@Param("organModelList")List<OrganModel> organModelList); 
}