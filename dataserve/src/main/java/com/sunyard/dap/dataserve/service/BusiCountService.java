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

    ReturnT<List> list(Map<String,Object> params);

    ReturnT<List> listBranch(Map<String,Object> params);

    ReturnT<List> listSite(Map<String,Object> params);

    ReturnT<List> listZone(Map<String,Object> params);

    ReturnT<List> listChannel(Map<String,Object> params);

    ReturnT<List> listMonthly(Map<String,Object> params);

    ReturnT<List> listBranchMonthly(Map<String,Object> params);

    ReturnT<List> listZoneMonthly(Map<String,Object> params);

    ReturnT<List> listSiteMonthly(Map<String,Object> params);

    ReturnT<List> listChannelMonthly(Map<String,Object> params);

    ReturnT<List> listTotalMinRT(Map<String,Object> params);

    ReturnT<List> listTotalHourRT(Map<String,Object> params);

    ReturnT<List> listTotalDayRT(Map<String,Object> params);

}
