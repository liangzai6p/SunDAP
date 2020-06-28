package com.sunyard.ars.risk.dao.arms;


import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ArmsDataStatisticsMapper {
    /**
     * 根据查询条件查询出预警统计信息
     * @param tjParam
     * @return
     */
    List<HashMap<String,Object>> getMonitorData(Map<String,Object> tjParam);

    /**
     * 根据查询条件查询出提示单统计信息
     * @param promptTjParam
     * @return
     */
    List<HashMap<String,Object>> promptStatisticsQuery(Map<String,Object> promptTjParam);

    /**
     * 提示单
     * @param updateCount
     * @param modelId
     * @param organNo
     * @param procDate
     * @return
     */
    int updatePromptStatistic(@Param("updateCount")Integer updateCount,
                              @Param("modelId")Integer modelId,
                              @Param("organNo")String organNo,
                              @Param("procDate")String procDate
                              );
    /**
     * 处理单
     * @param updateCount
     * @param modelId
     * @param organNo
     * @param procDate
     * @return
     */
    int updateStatistic(@Param("updateCount")Integer updateCount,
                        @Param("dealState")String dealState,
                        @Param("modelId")Integer modelId,
                        @Param("organNo")String organNo,
                        @Param("procDate")String procDate
                              );


    /**
     * 处理单
     * @param updateCount
     * @param modelId
     * @param organNo
     * @param procDate
     * @return
     */
    int updateStatisticFromRowId(@Param("updateCount")Integer updateCount,
                        @Param("modelId")Integer modelId,
                        @Param("organNo")String organNo,
                        @Param("procDate")String procDate
    );

    /**
     * 关联统计查询
     * @param query_map
     * @return
     */
	List<HashMap<String, Object>> relateStatisticsQuery(Map<String, Object> query_map);

	/**
	 * 删除统计信息
	 * @param query_map
	 * @return
	 */
	int deleteArmsCount(HashMap<String, Object> query_map);

	/**
	 * 插入统计信息
	 * @param query_map
	 * @return
	 */
	int insertArmsCount(HashMap<String, Object> query_map);

	/**
	 * 删除统计信息
	 * @param query_map
	 * @return
	 */
	int deleteArmsHsCount(HashMap<String, Object> query_map);

	/**
	 * 
	 * @param query_map
	 * @return
	 */
	int insertArmsHsCount(HashMap<String, Object> query_map);

}
