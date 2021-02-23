package com.sunyard.dap.intilligentSchedual.controller;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.intilligentSchedual.service.CenterCounterMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jie.zheng
 * @since 2021-02-19*/


@RestController
@RequestMapping("/centerCounterMonitor")
@Slf4j
@RefreshScope
public class CenterCounterMonitorController {
    @Autowired
    private CenterCounterMonitorService service;

/**
     * 根据机构号查询获取机构信息
     * @Author jie.zheng
     * @Date 9:41 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/

@PostMapping("/depBelongCenterQuery")
    public ReturnT<Map> depBelongCenterQuery(@RequestBody Map<String,Object> params){
        return service.depBelongCenterQuery(params);
    }


}
