package com.sunyard.dap.dataserve.service;

import com.sunyard.dap.common.model.ReturnT;

import java.util.List;
import java.util.Map;

/**
 * 辽宁大屏
 */
public interface LiaoNingBusiCountService {

    /**
     * 获取辽宁大屏机构业务量数据
     */
    ReturnT<Map> selectBusiNum(Map<String,Object> params);

    /**
     * 获取辽宁大屏地图中地区（机构）日业务量
     * @param params
     * @return
     */
    ReturnT<List> selectOrganBusi(Map<String,Object> params);

    /**
     * 获取当日每小时业务量
     * @param params
     * @return
     */
    ReturnT<Map> getHourBusiCount(Map<String,Object> params);

    /**
     * 获取分状态业务量
     * @param params
     * @return
     */
    ReturnT<Map> getTransStateCount(Map<String,Object> params);

    /**
     * 获取当日每小时业务量
     * @param params
     * @return
     */
    ReturnT<Map> getHourAvgTime(Map<String,Object> params);

    /**
     * 获取柜员处理业务数
     * @param params
     * @return
     */
    ReturnT<List> getTellerBusiCount(Map<String,Object> params);
}
