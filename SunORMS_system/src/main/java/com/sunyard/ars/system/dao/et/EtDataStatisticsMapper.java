package com.sunyard.ars.system.dao.et;


import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface EtDataStatisticsMapper {

    /**
     * 处理单
     * @param updateCount
     * @param modelId
     * @param organNo
     * @param procDate
     * @return
     */
    int updateStatistic(@Param("updateCount") Integer updateCount,
                        @Param("dealState") String dealState,
                        @Param("modelId") String modelId,
                        @Param("organNo") String organNo,
                        @Param("procDate") String procDate
    );

    void spArmsEntrydataHs(@Param("modelId") String modelId,
                      @Param("organNo") String organNo,
                      @Param("procDate") String procDate);

    /**
     * 查询
     * @param condMap
     * @return
     */
	int selectCountSame(HashMap<String,Object> condMap);
}
