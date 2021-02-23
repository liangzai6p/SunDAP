package com.sunyard.dap.intilligentSchedual.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.intilligentSchedual.entity.VolumeForcastDO;

import java.util.Map;

public interface VolumeForcastService extends IService<VolumeForcastDO> {
    ReturnT<Map> depSelectDataQuery(Map<String,Object> params);
}
