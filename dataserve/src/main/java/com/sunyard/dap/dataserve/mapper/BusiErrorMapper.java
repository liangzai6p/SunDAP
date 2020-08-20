package com.sunyard.dap.dataserve.mapper;

import com.sunyard.dap.dataserve.entity.BusiErrorDetailDo;
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
 * @since 2020-08-20
 */
public interface BusiErrorMapper extends BaseMapper<BusiErrorDetailDo> {
    List<HashMap<String,Object>> listBranchErrorCount(@Param("params") Map<String,Object> params);

}
