package com.sunyard.dap.intilligentSchedual.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunyard.dap.intilligentSchedual.entity.CenterCounterMonitorDO;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CenterCounterMonitorMapper extends BaseMapper<CenterCounterMonitorDO> {
    HashMap<String,Object> depBelongCenterQuery(@Param("params") Map<String,Object> params);
}
