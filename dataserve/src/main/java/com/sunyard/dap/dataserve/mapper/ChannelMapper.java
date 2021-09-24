package com.sunyard.dap.dataserve.mapper;

import com.sunyard.dap.dataserve.entity.ChannelGradeDO;
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
 * @since 2020-08-28
 */
public interface ChannelMapper extends BaseMapper<ChannelGradeDO> {
    List<HashMap<String,Object>> findEleAllRplRate(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listEleRplRateByBranch(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listEleAllRplRateDaily(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listEleAllRplRateMonthly(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listEleGradeByChannel(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listEleSatisByChannel(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listEleRplRateByChannel(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listCusCountByChannel(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listChannelAmountRate(@Param("params") Map<String, Object> params);
}
