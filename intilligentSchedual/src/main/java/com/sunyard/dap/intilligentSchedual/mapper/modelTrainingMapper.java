package com.sunyard.dap.intilligentSchedual.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunyard.dap.intilligentSchedual.entity.modelTrainingDO;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface modelTrainingMapper extends BaseMapper<modelTrainingDO> {
    /**查询
     * @param condMap
     * @return
     */
    List<HashMap<String,Object>> select(@Param("params") HashMap<String,Object> condMap);

    /**查询中心端模型训练item_name
     * @param condMap
     * @return
     */
    List<HashMap<String,Object>> selectItemName(@Param("params") HashMap<String,Object> condMap);

    /**新增日志
     * @param condMap
     */
    void insert(@Param("params") HashMap<String,Object> condMap);
}
