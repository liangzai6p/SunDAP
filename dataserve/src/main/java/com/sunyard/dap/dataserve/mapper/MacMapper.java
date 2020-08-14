package com.sunyard.dap.dataserve.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.dataserve.entity.MacDO;
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
 * @since 2020-08-12
 */
public interface MacMapper extends BaseMapper<MacDO> {
    List<HashMap<String,Object>> listStatus(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listFaultByTime(@Param("params") Map<String,Object> params);

    Page<HashMap<String, Object>> listDetail(Page<?> page, @Param("params") Map<String, Object> params);

    List<HashMap<String,Object>> listBranchFaultRate(@Param("params") Map<String,Object> params);





}
