package com.sunyard.dap.dataserve.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.service.TellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yey.he
 * @since 2020-08-14
 */
@RestController
@RequestMapping("/teller")
@Slf4j
@RefreshScope
public class TellerController {
    @Autowired
    private TellerService service;

    /**
     * 柜员基本信息
     * @Author yey.he
     * @Date 10:32 AM 2020/8/14
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.HashMap<java.lang.String,java.lang.Object>>>
     **/
    @PostMapping("/listTeller")
    public ReturnT<Page<HashMap<String, Object>>> listTeller(@RequestBody Map<String,Object> params){
        return service.listTeller(params);
    }

    /**
     * 柜员月度考核结果
     * @Author yey.he
     * @Date 10:32 AM 2020/8/14
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.HashMap<java.lang.String,java.lang.Object>>>
     **/
    @PostMapping("/listTellerAssess")
    public ReturnT<Page<HashMap<String, Object>>> listTellerAssess(@RequestBody Map<String,Object> params){
        return service.listTellerAssess(params);
    }

    /**
     * 柜员综合评价
     * @Author yey.he
     * @Date 10:32 AM 2020/8/14
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.HashMap<java.lang.String,java.lang.Object>>>
     **/
    @PostMapping("/listTellerGrade")
    public ReturnT<Page<HashMap<String, Object>>> listTellerGrade(@RequestBody Map<String,Object> params){
        return service.listTellerGrade(params);
    }

    /**
     * 柜员状态
     * @Author yey.he
     * @Date 10:37 AM 2020/8/14
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listStatus")
    public ReturnT<List> listStatus(@RequestBody Map<String,Object> params){
        return service.listStatus(params);
    }

    /**
     * 分行不在岗人员排名
     * @Author yey.he
     * @Date 3:01 PM 2020/8/14
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listBranchOffline")
    public ReturnT<List> listBranchOffline(@RequestBody Map<String,Object> params){
        return service.listBranchOffline(params);
    }
}

