package com.sunyard.dap.dataserve.service;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.BusiTrtDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yey.he
 * @since 2020-07-17
 */
public interface BusiTrtService extends IService<BusiTrtDO> {
    ReturnT<List> listType(Map<String,Object> params);

    ReturnT<List> listBranch(Map<String,Object> params);

    ReturnT<List> listSite(Map<String,Object> params);

    ReturnT<List> listZone(Map<String,Object> params);

    ReturnT<List> listChannel(Map<String,Object> params);

    ReturnT<List> listTypeHourly(Map<String,Object> params);

    ReturnT<List> listCurrencyIO(Map<String,Object> params);
}
