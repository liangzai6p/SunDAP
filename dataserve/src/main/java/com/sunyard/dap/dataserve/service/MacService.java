package com.sunyard.dap.dataserve.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.MacDO;
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
 * @since 2020-08-12
 */
public interface MacService extends IService<MacDO> {
    ReturnT<List> listStatus(Map<String,Object> params);

    ReturnT<List> listFaultByTime(Map<String,Object> params);

    ReturnT<Page<HashMap<String, Object>>> listDetail(Map<String,Object> params);

}
