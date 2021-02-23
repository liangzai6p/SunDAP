package com.sunyard.dap.intilligentSchedual.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunyard.dap.intilligentSchedual.entity.VolumeForcastDO;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface VolumeForcastMapper extends BaseMapper<VolumeForcastDO> {
    List<HashMap<String,Object>> depSelectDataQuery(@Param("params") Map<String,Object> params);
}
