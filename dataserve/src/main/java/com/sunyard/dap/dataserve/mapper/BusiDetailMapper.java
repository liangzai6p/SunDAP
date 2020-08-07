package com.sunyard.dap.dataserve.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.dataserve.entity.BusiDetailDO;
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
 * @since 2020-08-05
 */
public interface BusiDetailMapper extends BaseMapper<BusiDetailDO> {

    Page<HashMap<String, Object>> listRtDetail(Page<?> page, @Param("params") Map<String, Object> params);

    Page<HashMap<String, Object>> listHistoryDetail(Page<?> page, @Param("params") Map<String, Object> params);

}
