package com.sunyard.dap.dataserve.service;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.ChannelGradeDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yey.he
 * @since 2020-08-28
 */
public interface ChannelService extends IService<ChannelGradeDO> {
    ReturnT<List> findEleAllRplRate(Map<String,Object> params);

    ReturnT<List> listEleRplRateByBranch(Map<String,Object> params);

    ReturnT<Map> listEleRplRateByBranchMap(Map<String,Object> params);

    ReturnT<List> listEleRplRateByBranchTable(Map<String,Object> params);

    ReturnT<List> listEleAllRplRateDaily(Map<String,Object> params);

    ReturnT<Map> listEleAllRplRateDailyLineChat(Map<String,Object> params);

    ReturnT<List> listEleAllRplRateMonthly(Map<String,Object> params);

    ReturnT<Map> listEleAllRplRateMonthlyLine(Map<String,Object> params);

    ReturnT<List> listEleGradeByChannel(Map<String,Object> params);

    ReturnT<Map> listEleGradeByChannelRadar(Map<String,Object> params);

    ReturnT<List> listEleSatisByChannel(Map<String,Object> params);

    ReturnT<Map> listEleSatisByChannelBar(Map<String,Object> params);

    ReturnT<List> listEleRplRateByChannel(Map<String,Object> params);

    ReturnT<Map> listEleRplRateByChannelBar(Map<String,Object> params);

    ReturnT<List> listCusCountByChannel(Map<String,Object> params);

    ReturnT<Map> listCusCountByChannelBar(Map<String,Object> params);

    ReturnT<List> listChannelAmountRate(Map<String, Object> params);
}
