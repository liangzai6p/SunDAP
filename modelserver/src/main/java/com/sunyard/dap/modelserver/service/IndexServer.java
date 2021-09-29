package com.sunyard.dap.modelserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.modelserver.entity.MoAllIndex;

import java.util.List;
import java.util.Map;

public interface IndexServer extends IService<MoAllIndex> {
    ReturnT<List> serachIndexName( Map<String, Object> params);

    ReturnT<List> searchTellerOperation(Map<String, Object> params);

    ReturnT<List> searchTellerCusMoney(Map<String, Object> params);
}
