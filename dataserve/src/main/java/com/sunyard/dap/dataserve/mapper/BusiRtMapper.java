package com.sunyard.dap.dataserve.mapper;

import com.sunyard.dap.dataserve.entity.BusiRtDO;
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
 * @since 2020-07-03
 */
public interface BusiRtMapper extends BaseMapper<BusiRtDO> {
    List<HashMap<String,Object>> listHRT(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listBranchHRT(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listZoneHRT(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listSiteHRT(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listChannelHRT(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listDRT(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listBranchDRT(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listZoneDRT(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listSiteDRT(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listChannelDRT(@Param("params") Map<String,Object> params);

}
