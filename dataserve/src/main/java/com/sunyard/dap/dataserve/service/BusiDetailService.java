package com.sunyard.dap.dataserve.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.BusiDetailDO;
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
 * @since 2020-08-05
 */
public interface BusiDetailService extends IService<BusiDetailDO> {

    ReturnT<Page<HashMap<String, Object>>> listRtDetail(Map<String, Object> params);

    ReturnT<Page<HashMap<String, Object>>> listHistoryDetail(Map<String, Object> params);
}
