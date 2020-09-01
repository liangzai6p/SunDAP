package com.sunyard.dap.dataserve.controller;


import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yey.he
 * @since 2020-08-28
 */
@RestController
@RequestMapping("/channel")
@Slf4j
@RefreshScope
public class ChannelController {
    @Autowired
    private ChannelService service;

    /**
     * 电子渠道总替代率
     * @Author yey.he 
     * @Date 4:34 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/findEleAllRplRate")
    public ReturnT<List> findEleAllRplRate(@RequestBody Map<String,Object> params){
        return service.findEleAllRplRate(params);
    }

    /**
     * 分行电子渠道替代率排名
     * @Author yey.he
     * @Date 4:34 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleRplRateByBranch")
    public ReturnT<List> listEleRplRateByBranch(@RequestBody Map<String,Object> params){
        return service.listEleRplRateByBranch(params);
    }

    /**
     * 日度电子渠道总替代率
     * @Author yey.he
     * @Date 4:34 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleAllRplRateDaily")
    public ReturnT<List> listEleAllRplRateDaily(@RequestBody Map<String,Object> params){
        return service.listEleAllRplRateDaily(params);
    }

    /**
     * 月度电子渠道总替代率
     * @Author yey.he
     * @Date 4:34 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleAllRplRateMonthly")
    public ReturnT<List> listEleAllRplRateMonthly(@RequestBody Map<String,Object> params){
        return service.listEleAllRplRateMonthly(params);
    }

    /**
     * 各电子渠道综合评价
     * @Author yey.he
     * @Date 4:34 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleGradeByChannel")
    public ReturnT<List> listEleGradeByChannel(@RequestBody Map<String,Object> params){
        return service.listEleGradeByChannel(params);
    }

    /**
     * 各电子渠道客户满意度
     * @Author yey.he
     * @Date 4:35 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleSatisByChannel")
    public ReturnT<List> listEleSatisByChannel(@RequestBody Map<String,Object> params){
        return service.listEleSatisByChannel(params);
    }

    /**
     * 各电子渠道替代率排名
     * @Author yey.he
     * @Date 4:35 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleRplRateByChannel")
    public ReturnT<List> listEleRplRateByChannel(@RequestBody Map<String,Object> params){
        return service.listEleRplRateByChannel(params);
    }

    /**
     * 各电子渠道客户覆盖量
     * @Author yey.he
     * @Date 4:35 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listCusCountByChannel")
    public ReturnT<List> listCusCountByChannel(@RequestBody Map<String,Object> params){
        return service.listCusCountByChannel(params);
    }
}

