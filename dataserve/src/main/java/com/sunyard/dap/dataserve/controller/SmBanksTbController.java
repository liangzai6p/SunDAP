package com.sunyard.dap.dataserve.controller;


import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.service.ISmBanksTbService;
import com.sunyard.dap.dataserve.service.MacService;
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
 * 系统银行信息表 前端控制器
 * </p>
 *
 * @author xiao.xie
 * @since 2021-03-01
 */
@RestController
@RequestMapping("/banks")
@Slf4j
@RefreshScope
public class SmBanksTbController {

    @Autowired
    private ISmBanksTbService service;
    /**
     * 根据条件查询机构数据
     * @Author yey.he
     * @Date 9:23 AM 2020/8/13
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listBank")
    public ReturnT<List> listBank(@RequestBody Map<String,Object> params){
        return service.listBank(params);
    }

    /**
     * 根据条件查询机构数据,并返回弹出下拉框所需的data数据
     * @Author yey.he
     * @Date 9:23 AM 2020/8/13
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listBankDiaLogData")
    public ReturnT<List> listBankDiaLogData(@RequestBody Map<String,Object> params){
        return service.listBankDiaLogData(params);
    }
}

