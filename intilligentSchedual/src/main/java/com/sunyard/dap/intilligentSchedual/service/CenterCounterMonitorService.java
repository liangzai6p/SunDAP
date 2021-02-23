package com.sunyard.dap.intilligentSchedual.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.intilligentSchedual.entity.CenterCounterMonitorDO;

import java.util.List;
import java.util.Map;

public interface CenterCounterMonitorService extends IService<CenterCounterMonitorDO> {
    ReturnT<Map> depBelongCenterQuery(Map<String,Object> params);
}
