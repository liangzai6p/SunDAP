package com.sunyard.dap.forecast.controller;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.forecast.service.FittingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
/**
 * @program: SunDAP
 * @description: 拟合运算
 * @author: yey.he
 * @create: 2020-09-11 11:35
 **/
@RestController
@RequestMapping("/fitting")
@Slf4j
@RefreshScope
public class FittingController {
    @Autowired
    private FittingService service;

    @PostMapping("lineFitting")
    public ReturnT<List> lineFitting(@RequestBody String params){
        return service.lineFitting(params);
    }

    @PostMapping("polyFitting")
    public ReturnT<List> polyFitting(@RequestBody String params){
        return service.polyFitting(params);
    }

    @PostMapping("standardDeviation")
    public ReturnT<String> standardDeviation(@RequestBody String params){
        return service.standardDeviation(params);
    }

}
