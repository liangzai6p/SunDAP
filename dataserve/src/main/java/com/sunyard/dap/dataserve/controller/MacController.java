package com.sunyard.dap.dataserve.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.service.MacService;
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
 * @since 2020-08-12
 */
@RestController
@RequestMapping("/mac")
@Slf4j
@RefreshScope
public class MacController {
    @Autowired
    private MacService service;

    /**
     * 自助设备故障明细
     * @Author yey.he 
     * @Date 9:23 AM 2020/8/13
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listDetail")
    public ReturnT<Page<HashMap<String, Object>>> listDetail(@RequestBody Map<String,Object> params){
        return service.listDetail(params);
    }

    /**
     * 故障次数与故障设备数量
     * @Author yey.he 
     * @Date 9:23 AM 2020/8/13
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listFaultByTime")
    public ReturnT<List> listFaultByTime(@RequestBody Map<String,Object> params){
        return service.listFaultByTime(params);
    }

    /**
     * 实时各状态自助设备数量
     * @Author yey.he
     * @Date 9:23 AM 2020/8/13
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listStatus")
    public ReturnT<List> listStatus(@RequestBody Map<String,Object> params){
        return service.listStatus(params);
    }



}

