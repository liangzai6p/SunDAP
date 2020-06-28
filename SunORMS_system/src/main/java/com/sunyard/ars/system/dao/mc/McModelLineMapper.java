package com.sunyard.ars.system.dao.mc;

import java.util.HashMap;
import java.util.List;

import com.sunyard.ars.system.pojo.mc.McModelLine;

public interface McModelLineMapper {

    int insertSelective(McModelLine record);
    
    /**
     * 
     * @Description: 自定义查询
     * @param @param mcModelLine
     * @param @return   
     * @return List<McModelLine>  
     * @throws
     * @author zs
     * @date 2018年10月18日
     */
    List<McModelLine> selectBySelective(McModelLine mcModelLine);
    
    
    /**
     * 
     * @Description: 只查询模型id和关联模型id  并去重
     * @param @param mcModelLine
     * @param @return   
     * @return List<McModelLine>  
     * @throws
     * @author zs
     * @date 2018年10月20日
     */
    List<McModelLine> selectIdAndLineid(McModelLine mcModelLine);
    
    
    int deleteByLineFiled(Integer lineid);
    
    int deleteByfileds(McModelLine mcModelLine);

    /**
     * 查询关联模型信息
     * @param modelId
     * @return
     */
	List<McModelLine> selectRelateModel(HashMap<String,Object> condMap);
    
}