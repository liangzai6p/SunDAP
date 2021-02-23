package com.sunyard.dap.intilligentSchedual.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.intilligentSchedual.entity.CenterIntelScheDO;
import com.sunyard.dap.intilligentSchedual.entity.VolumeForcastDO;
import com.sunyard.dap.intilligentSchedual.mapper.CenterIntelScheMapper;
import com.sunyard.dap.intilligentSchedual.mapper.VolumeForcastMapper;
import com.sunyard.dap.intilligentSchedual.service.CenterIntelScheService;
import com.sunyard.dap.intilligentSchedual.service.VolumeForcastService;
import com.sunyard.dap.intilligentSchedual.util.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("VolumeForcastService")
@Slf4j
public class VolumeForcastServiceImpl extends ServiceImpl<VolumeForcastMapper, VolumeForcastDO> implements VolumeForcastService {
    @Override
    public ReturnT<Map> depSelectDataQuery(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("depSelectDataQuery-Start");
            // 前台参数集合
            Map sysMap = params;
            String query_organ_no = sysMap.get("organ_no")+"";
            HashMap condMap = new HashMap();
            condMap.put("query_organ_no", query_organ_no);

            List mapList = BaseUtil.convertListMapKeyValue(baseMapper.depSelectDataQuery(condMap));
            retMap.put("dataMap", mapList);

            log.debug("depSelectDataQuery-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }
}
