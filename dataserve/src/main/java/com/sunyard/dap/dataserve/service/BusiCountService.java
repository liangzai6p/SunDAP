package com.sunyard.dap.dataserve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.BusiCountDO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yey.he
 * @since 2020-06-12
 */
public interface BusiCountService extends IService<BusiCountDO>{

    ReturnT<List> allCount(Map<String,Object> params);

    ReturnT<List> allDaily(Map<String,Object> params);

    ReturnT<List> branchDaily(Map<String,Object> params);

    ReturnT<List> siteDaily(Map<String,Object> params);

    ReturnT<List> zoneDaily(Map<String,Object> params);

    ReturnT<List> channelDaily(Map<String,Object> params);

    ReturnT<List> allMonthly(Map<String,Object> params);

    ReturnT<List> branchMonthly(Map<String,Object> params);

    ReturnT<List> zoneMonthly(Map<String,Object> params);

    ReturnT<List> siteMonthly(Map<String,Object> params);

    ReturnT<List> channelMonthly(Map<String,Object> params);

    ReturnT<List> branchCount(Map<String,Object> params);

    ReturnT<List> zoneCount(Map<String,Object> params);

    ReturnT<List> siteCount(Map<String,Object> params);

    ReturnT<List> channelCount(Map<String,Object> params);
}
