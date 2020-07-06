package com.sunyard.dap.dataserve.service;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.BusiRtDO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yey.he
 * @since 2020-07-03
 */
public interface BusiRtService extends IService<BusiRtDO> {
    ReturnT<List> listHRT(Map<String,Object> params);

    ReturnT<List> listBranchHRT(Map<String,Object> params);

    ReturnT<List> listZoneHRT(Map<String,Object> params);

    ReturnT<List> listSiteHRT(Map<String,Object> params);

    ReturnT<List> listChannelHRT(Map<String,Object> params);

    ReturnT<List> listDRT(Map<String,Object> params);

    ReturnT<List> listBranchDRT(Map<String,Object> params);

    ReturnT<List> listZoneDRT(Map<String,Object> params);

    ReturnT<List> listSiteDRT(Map<String,Object> params);

    ReturnT<List> listChannelDRT(Map<String,Object> params);

}
