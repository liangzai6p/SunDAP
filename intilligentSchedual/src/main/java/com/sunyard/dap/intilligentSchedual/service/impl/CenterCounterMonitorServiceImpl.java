package com.sunyard.dap.intilligentSchedual.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.intilligentSchedual.entity.CenterCounterMonitorDO;
import com.sunyard.dap.intilligentSchedual.mapper.CenterCounterMonitorMapper;
import com.sunyard.dap.intilligentSchedual.service.CenterCounterMonitorService;
import com.sunyard.dap.intilligentSchedual.util.BaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("CenterCounterMonitor")
@Slf4j
public class CenterCounterMonitorServiceImpl extends ServiceImpl<CenterCounterMonitorMapper, CenterCounterMonitorDO> implements CenterCounterMonitorService {
    @Override
    public ReturnT<Map> depBelongCenterQuery(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("depBelongCenterQuery-Start");
            // 前台参数集合
            Map sysMap = params;
            String query_organ_no = sysMap.get("organ_no")+"";
            HashMap condMap = new HashMap();
            condMap.put("query_organ_no", query_organ_no);

            Map dataMap = BaseUtil.convertMapKeyValue(baseMapper.depBelongCenterQuery(condMap));
            retMap.put("dataMap", dataMap);

            log.debug("depBelongCenterQuery-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }
}
