package com.sunyard.dap.dataserve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.MacDO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yey.he
 * @since 2020-08-12
 */
public interface MacService extends IService<MacDO> {
    ReturnT<List> listStatus(Map<String,Object> params);

    ReturnT<List> listFaultByTime(Map<String,Object> params);

    ReturnT<Page<HashMap<String, Object>>> listDetail(Map<String,Object> params);

    ReturnT<List> listBranchFaultRate(Map<String,Object> params);

    ReturnT<List> listSiteFaultRate(Map<String,Object> params);

    /**
     * 设备地区分布相关
     **/
    ReturnT<List> listBranchOnlineMac(@Param("params") Map<String,Object> params);

    ReturnT<List> listBranchMacFaultCount(@Param("params") Map<String,Object> params);

    ReturnT<List> listBranchMacReplaceRate(@Param("params") Map<String,Object> params);

    ReturnT<List> listMacReplaceRateMonthly(@Param("params") Map<String,Object> params);

    /**
     * 设备画像相关
     **/
    ReturnT<List> macProtrayal(@Param("params") Map<String,Object> params);

    ReturnT<List> listMacTypeCount(@Param("params") Map<String,Object> params);

    ReturnT<List> listMacTypeAssess(@Param("params") Map<String,Object> params);

    ReturnT<List> listMacTypeFaultRate(@Param("params") Map<String,Object> params);

    ReturnT<List> listMacBrandFaultRate(@Param("params") Map<String,Object> params);

    ReturnT<List> listMacReplaceOtcRateMonthly(@Param("params") Map<String,Object> params);

    ReturnT<List> listMacTypeBusiMonthly(@Param("params") Map<String,Object> params);

    ReturnT<List> listMacTypeSuccessAndFaultRate(@Param("params") Map<String,Object> params);

    ReturnT<List> listMacMaintainMonthly(@Param("params") Map<String,Object> params);

    ReturnT<List> listMacTypeBusiCount(@Param("params") Map<String,Object> params);

    ReturnT<Page<HashMap<String, Object>>> listMacBusiCount(@Param("params") Map<String, Object> params);



    ReturnT<List> listSiteFaultCount(@Param("params") Map<String,Object> params);

}
