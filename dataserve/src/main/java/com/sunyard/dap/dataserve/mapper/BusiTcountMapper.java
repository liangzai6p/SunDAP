package com.sunyard.dap.dataserve.mapper;

import com.sunyard.dap.dataserve.entity.BusiTcountDO;
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
 * @since 2020-07-14
 */
public interface BusiTcountMapper extends BaseMapper<BusiTcountDO> {
    List<HashMap<String,Object>> listType(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listBranch(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listZone(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listSite(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listChannel(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listTypeMonthly(@Param("params") Map<String,Object> params);



}
