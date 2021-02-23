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
import java.util.Map;

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

    /**
     * 线性拟合
     * @Author yey.he
     * @Date 2:05 PM 2020/9/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("lineFitting")
    public ReturnT<List> lineFitting(@RequestBody String params){
        return service.lineFitting(params);
    }

    /**
     * 线性拟合
     * @Author xiao.xie
     * @Date 2021-1-13 10:52:26
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("lineFittingXY")
    public ReturnT<Map> lineFittingXY(@RequestBody String params){
        return service.lineFittingXY(params);
    }

    /**
     * 线性拟合(电子渠道替代路)
     * @Author xiao.xie
     * @Date 2021-1-13 10:52:26
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("lineFittingElcXY")
    public ReturnT<Map> lineFittingElcXY(@RequestBody String params){
        return service.lineFittingElcXY(params);
    }

    /**
     * 多项式拟合
     * @Author yey.he
     * @Date 2:05 PM 2020/9/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("polyFitting")
    public ReturnT<List> polyFitting(@RequestBody String params){
        return service.polyFitting(params);
    }

    /**
     * 标准误差
     * @Author yey.he
     * @Date 2:05 PM 2020/9/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.lang.String>
     **/
    @PostMapping("standardDeviation")
    public ReturnT<String> standardDeviation(@RequestBody String params){
        return service.standardDeviation(params);
    }

    /**
     * 周线
     * @Author jie1.zheng
     * @Date 10:01 AM 2020/12/15
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("contourFitting")
    public ReturnT<Map> contourFitting(@RequestBody String params){
        return service.contourFitting(params);
    }

    /**
     * 月线
     * @Author jie1.zheng
     * @Date 10:01 AM 2020/12/15
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("onLineFitting")
    public ReturnT<Map> onLineFitting(@RequestBody String params){
        return service.onLineFitting(params);
    }

    /**
     * 年线
     * @Author jie1.zheng
     * @Date 10:01 AM 2020/12/15
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("annualLineFitting")
    public ReturnT<Map> annualLineFitting(@RequestBody String params){
        return service.annualLineFitting(params);
    }

}
