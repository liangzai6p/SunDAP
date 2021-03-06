package com.sunyard.dap.modelserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunyard.dap.modelserver.entity.MoAllIndex;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MoAllIndexMapper extends BaseMapper<MoAllIndex> {

    List<String> serachIndexName(@Param("params") Map<String, Object> params);

    List<HashMap<String,Object>> searchTellerOperation(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> searchTellerCusMoney(@Param("params") Map<String, Object> params);
}
