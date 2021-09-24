package com.sunyard.dap.dataserve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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


    List<HashMap<String,Object>> allCount(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> allDaily(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> branchDaily(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> siteDaily(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> zoneDaily(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> channelDaily(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> allMonthly(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> branchMonthly(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> siteMonthly(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> zoneMonthly(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> channelMonthly(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> branchCount(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> zoneCount(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> siteCount(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> channelCount(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> branchRank(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> operateCost(@Param("params") Map<String,Object> params);

    Page<HashMap<String, Object>> branchBusiDetails(@Param("page") Page<HashMap> page ,@Param("params") Map<String, Object> params);
}
