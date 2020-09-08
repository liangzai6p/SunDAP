package com.sunyard.dap.dataserve.service;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.SiteDO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统银行信息表 服务类
 * </p>
 *
 * @author yey.he
 * @since 2020-08-26
 */
public interface SiteService extends IService<SiteDO> {
    ReturnT<List> listBaseInfo(@Param("params") Map<String,Object> params);

    ReturnT<List> listSiteGrade(@Param("params") Map<String,Object> params);

    ReturnT<List> listTransStatus(@Param("params") Map<String,Object> params);

    ReturnT<List> listHallInfo(@Param("params") Map<String,Object> params);


    ReturnT<List> listCashInfo(@Param("params") Map<String,Object> params);

    ReturnT<List> listQueHourly(@Param("params") Map<String,Object> params);

    ReturnT<List> listMacBusiTypeCountHourly(@Param("params") Map<String,Object> params);
}
