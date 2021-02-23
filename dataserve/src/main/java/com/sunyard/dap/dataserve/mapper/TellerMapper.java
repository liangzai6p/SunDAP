package com.sunyard.dap.dataserve.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.dataserve.entity.DmTellerTb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yey.he
 * @since 2020-08-14
 */
public interface TellerMapper extends BaseMapper<DmTellerTb> {
    Page<HashMap<String, Object>> listTeller(Page<?> page, @Param("params") Map<String, Object> params);

    Page<HashMap<String, Object>> listTellerAssess(Page<?> page, @Param("params") Map<String, Object> params);

    Page<HashMap<String, Object>> listTellerGrade(Page<?> page, @Param("params") Map<String, Object> params);

    List<HashMap<String,Object>> listStatus(@Param("params") Map<String,Object> params);

    List<HashMap<String, Object>> listBranchOffline(@Param("params") Map<String,Object> params);

    List<HashMap<String, Object>> listSiteOffline(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listRoleStatus(@Param("params") Map<String,Object> params);

    Page<HashMap<String, Object>> listTellerRank(Page<?> page, @Param("params") Map<String, Object> params);

    List<HashMap<String,Object>> listByBranch(@Param("params") Map<String, Object> params);
}
