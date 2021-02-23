package com.sunyard.dap.dataserve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.DmTellerTb;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yey.he
 * @since 2020-08-14
 */
public interface TellerService extends IService<DmTellerTb> {
    ReturnT<Page<HashMap<String, Object>>> listTeller(Map<String,Object> params);

    ReturnT<Page<HashMap<String, Object>>> listTellerAssess(Map<String,Object> params);

    ReturnT<Page<HashMap<String, Object>>> listTellerAssessCompreScore(Map<String,Object> params);

    ReturnT<Map> listTellerAssessChartDate(Map<String,Object> params);

    ReturnT<Page<HashMap<String, Object>>> listTellerGrade(Map<String,Object> params);

    ReturnT<List> listStatus(Map<String,Object> params);

    ReturnT<List> listBranchOffline(Map<String,Object> params);

    //经过处理的echarts数据
    ReturnT<Map> listBranchOfflineBarChart(Map<String,Object> params);

    ReturnT<List> listSiteOffline(Map<String,Object> params);

    ReturnT<Map> listSiteOfflineBar(Map<String,Object> params);

    ReturnT<List> listRoleStatus(@Param("params") Map<String,Object> params);

    ReturnT<Map> listRoleStatusBar(@Param("params") Map<String,Object> params);

    ReturnT<Page<HashMap<String, Object>>> listTellerRank(Map<String,Object> params);

    ReturnT<Map> listTellerRankBar(Map<String,Object> params);

    ReturnT<List> listByBranch(Map<String,Object> params);

}
