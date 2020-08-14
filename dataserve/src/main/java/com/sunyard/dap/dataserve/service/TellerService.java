package com.sunyard.dap.dataserve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.DmTellerTb;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yey.he
 * @since 2020-08-14
 */
public interface TellerService extends IService<DmTellerTb> {
    ReturnT<Page<HashMap<String, Object>>> listTeller(Map<String,Object> params);

    ReturnT<Page<HashMap<String, Object>>> listTellerAssess(Map<String,Object> params);

    ReturnT<Page<HashMap<String, Object>>> listTellerGrade(Map<String,Object> params);

    ReturnT<List> listStatus(Map<String,Object> params);

    ReturnT<List> listBranchOffline(Map<String,Object> params);


}
