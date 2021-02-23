package com.sunyard.dap.intilligentSchedual.controller;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.intilligentSchedual.service.VolumeForcastService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jie.zheng
 * @since 2021-02-19
 */
@RestController
@RequestMapping("/centerIntelSche")
@Slf4j
@RefreshScope
public class VolumeForcastController {
    @Autowired
    private VolumeForcastService service;
    @PostMapping("/depSelectDataQuery")
    public ReturnT<Map> depSelectDataQuery(@RequestBody Map<String,Object> params){
        return service.depSelectDataQuery(params);
    }
}
