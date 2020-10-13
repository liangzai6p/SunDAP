package com.sunyard.dap.dataserve.controller;


import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.service.BusiTcountService;
import com.sunyard.dap.dataserve.service.BusiTrtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yey.he
 * @since 2020-07-17
 */
@RestController
@RequestMapping("/busiTrt")
@Slf4j
@RefreshScope
public class BusiTrtController {
    @Autowired
    private BusiTrtService service;

    /**
     * 业务种类实时业务量
     * @Author yey.he
     * @Date 9:29 AM 2020/7/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/list")
    public ReturnT<List> listType(@RequestBody Map<String,Object> params){
        return service.listType(params);
    }

    /**
     * 渠道业务种类实时业务量
     * @Author yey.he
     * @Date 9:30 AM 2020/7/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/channel")
    public ReturnT<List> listChannel(@RequestBody Map<String,Object> params){
        return service.listChannel(params);
    }

    /**
     * 分行业务种类实时业务量
     * @Author yey.he
     * @Date 9:30 AM 2020/7/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/branch")
    public ReturnT<List> listBranch(@RequestBody Map<String,Object> params){
        return service.listBranch(params);
    }

    /**
     * 网点业务种类实时业务量
     * @Author yey.he
     * @Date 9:30 AM 2020/7/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/site")
    public ReturnT<List> listSite(@RequestBody Map<String,Object> params){
        return service.listSite(params);
    }

    /**
     * 区域业务种类实时业务量
     * @Author yey.he
     * @Date 9:30 AM 2020/7/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/zone")
    public ReturnT<List> listZone(@RequestBody Map<String,Object> params){
        return service.listZone(params);
    }

    /**
     * 小时业务种类实时业务量
     * @Author yey.he
     * @Date 9:30 AM 2020/7/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/typeHourly")
    public ReturnT<List> listTypeHourly(@RequestBody Map<String,Object> params){
        return service.listTypeHourly(params);
    }

    /**
     * 现金收支
     * @Author yey.he
     * @Date 2:33 PM 2020/9/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listCurrencyIO")
    public ReturnT<List> listCurrencyIO(@RequestBody Map<String,Object> params){
        return service.listCurrencyIO(params);
    }
}

