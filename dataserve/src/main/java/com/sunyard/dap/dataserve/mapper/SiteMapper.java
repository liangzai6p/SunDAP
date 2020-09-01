package com.sunyard.dap.dataserve.mapper;

import com.sunyard.dap.dataserve.entity.SiteDO;
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
 * @author yey.he
 * @since 2020-08-26
 */
public interface SiteMapper extends BaseMapper<SiteDO> {
    List<HashMap<String,Object>> listBaseInfo(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listSiteGrade(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listTransStatus(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listHallInfo(@Param("params") Map<String,Object> params);


    List<HashMap<String,Object>> listCashInfo(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> listQueHourly(@Param("params") Map<String,Object> params);




}
