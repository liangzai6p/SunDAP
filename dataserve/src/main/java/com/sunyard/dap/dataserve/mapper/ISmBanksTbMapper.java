package com.sunyard.dap.dataserve.mapper;

import com.sunyard.dap.dataserve.entity.SmBanksTb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统银行信息表 Mapper 接口
 * </p>
 *
 * @author xiao.xie
 * @since 2021-03-01
 */
public interface ISmBanksTbMapper extends BaseMapper<SmBanksTb> {
    List<HashMap<String,Object>> listBank(@Param("params") Map<String,Object> params);
}
