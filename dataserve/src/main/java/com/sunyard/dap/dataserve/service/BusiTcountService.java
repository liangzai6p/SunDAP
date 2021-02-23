package com.sunyard.dap.dataserve.service;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.BusiTcountDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yey.he
 * @since 2020-07-14
 */
public interface BusiTcountService extends IService<BusiTcountDO> {
    ReturnT<List> listType(Map<String,Object> params);

    ReturnT<Map> listTypePie(Map<String,Object> params);

    ReturnT<List> listBranch(Map<String,Object> params);

    ReturnT<List> listSite(Map<String,Object> params);

    ReturnT<List> listZone(Map<String,Object> params);

    ReturnT<List> listChannel(Map<String,Object> params);

    ReturnT<Map> listChannelPie(Map<String,Object> params);

    ReturnT<List> listTypeMonthly(Map<String,Object> params);
}
