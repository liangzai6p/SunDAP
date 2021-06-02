package com.sunyard.dap.consumer.forecast.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.consumer.forecast.client.ForecastClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: SunDAP
 * @description: 拟合运算
 * @author: yey.he
 * @create: 2020-09-11 15:37
 **/
@RestController
@Slf4j
@RequestMapping("/sundap/fitting")
public class FittingController {
    @Autowired
    private ForecastClient client;

    @PostMapping("lineFitting")
    public ReturnT<List> lineFitting(@RequestBody String params){
        return client.getFittingLineFitting(params);
    }

    @PostMapping("lineFittingXY")
    public ReturnT<Map> lineFittingXY(@RequestBody String params){
        return client.getFittingLineFittingXY(params);
    }

    @PostMapping("lineFittingElcXY")
    public ReturnT<Map> lineFittingElcXY(@RequestBody String params){
        return client.lineFittingElcXY(params);
    }

    @PostMapping("polyFitting")
    public ReturnT<List> polyFitting(@RequestBody String params){
        return client.getFittingPolyFitting(params);
    }

    @PostMapping("contourFitting")
    public ReturnT<Map> contourFitting(@RequestBody String params){
        return client.getFittingContourFitting(params);
    }

    @PostMapping("onLineFitting")
    public ReturnT<Map> onLineFitting(@RequestBody String params){
        return client.getFittingOnLineFitting(params);
    }

    @PostMapping("annualLineFitting")
    public ReturnT<Map> annualLineFitting(@RequestBody String params){ return client.getFittingAnnualLineFitting(params); }

    @PostMapping("standardDeviation")
    public ReturnT<String> standardDeviation(@RequestBody String params){
        return client.getFittingStandardDeviation(params);
    }

    @PostMapping("getForecast")
    public ReturnT<double[]> getForecast(@RequestBody Map<String , Object> params){
        return client.getForecast(params);
    }

    @PostMapping("getStandardDeviation")
    public ReturnT<double[]> getStandardDeviation(@RequestBody Map<String , Object> params){
        return client.getStandardDeviation(params);
    }
}
