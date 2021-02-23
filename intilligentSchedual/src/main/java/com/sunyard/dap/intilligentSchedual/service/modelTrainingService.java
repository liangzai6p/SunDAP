package com.sunyard.dap.intilligentSchedual.service;

import com.sunyard.dap.common.model.ReturnT;

import java.util.List;
import java.util.Map;

public interface modelTrainingService {
    /**
     * 查询结果信息
     * @param params
     * @return
     */
    ReturnT<Map> select(Map<String , Object> params);

    /**
     * 中心端模型训练
     * @param params
     * @return
     */
    ReturnT<Map> centerTraining(Map<String , Object> params);

    /**
     *
     * @param params
     * @return
     */
    ReturnT<Map> busiForecast(Map<String , Object> params);

    /**
     *
     * @param params
     * @return
     */
    ReturnT<Map> serverCallSchedule(Map<String , Object> params);

    /**
     *
     * @param params
     * @return
     */
    ReturnT<Map> centerScheduleMethod(Map<String , Object> params);
}
