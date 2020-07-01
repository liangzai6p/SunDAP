package com.sunyard.dap.dataserve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunyard.dap.dataserve.entity.BusiCountDO;
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
 * @since 2020-06-12
 */
public interface BusiCountMapper extends BaseMapper<BusiCountDO>{


    List<HashMap<String,Object>> listByTime(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> listBranch(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> listSite(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> listZone(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> listChannel(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> listMonthly(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> listBranchMonthly(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> listSiteMonthly(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> listZoneMonthly(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> listChannelMonthly(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listTotalMinRT(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listTotalHourRT(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listTotalDayRT(@Param("params") Map<String,Object> params);

}
