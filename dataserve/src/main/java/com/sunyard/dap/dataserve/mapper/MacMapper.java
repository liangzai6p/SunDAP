package com.sunyard.dap.dataserve.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.dataserve.entity.MacDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yey.he
 * @since 2020-08-12
 */
public interface MacMapper extends BaseMapper<MacDO> {
    List<HashMap<String,Object>> listStatus(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listFaultByTime(@Param("params") Map<String,Object> params);

    Page<HashMap<String, Object>> listDetail(Page<?> page, @Param("params") Map<String, Object> params);

    List<HashMap<String,Object>> listBranchFaultRate(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listSiteFaultRate(@Param("params") Map<String,Object> params);

    /**
     * 设备地区分布相关
     **/
    List<HashMap<String,Object>> listBranchOnlineMac(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listBranchMacFaultCount(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listBranchMacReplaceRate(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listMacReplaceRateMonthly(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listSiteFaultCount(@Param("params") Map<String,Object> params);

    /**
     * 设备画像相关
     **/
    List<HashMap<String,Object>> macProtrayal(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listMacTypeCount(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listMacTypeAssess(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listMacTypeFaultRate(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listMacBrandFaultRate(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listMacReplaceOtcRateMonthly(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listMacTypeBusiMonthly(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listMacTypeSuccessAndFaultRate(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listMacMaintainMonthly(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listMacMaintainSuccessRateMonthly(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listMacTypeBusiCount(@Param("params") Map<String,Object> params);

    Page<HashMap<String, Object>> listMacBusiCount(Page<?> page, @Param("params") Map<String, Object> params);


}
