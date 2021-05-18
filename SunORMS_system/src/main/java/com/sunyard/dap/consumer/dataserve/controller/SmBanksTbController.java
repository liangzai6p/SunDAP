package com.sunyard.dap.consumer.dataserve.controller;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.consumer.dataserve.client.DataServeClient;
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
 * @description: 网点机构
 * @author: yey.he
 * @create: 2020-08-26 17:18
 **/
@RestController
@RequestMapping("/sundap/banks")
@Slf4j
@RefreshScope
public class SmBanksTbController {
    @Autowired
    private DataServeClient client;

    @PostMapping("/listBank")
    public ReturnT<List> listBank(@RequestBody Map<String,Object> params){
        return client.listBank(params);
    }

    @PostMapping("/listBankDiaLogData")
    public ReturnT<List> listBankDiaLogData(@RequestBody Map<String,Object> params){
        return client.listBankDiaLogData(params);
    }

    @PostMapping("/listsubBranchBankDiaLogData")
    public ReturnT<List> listsubBranchBankDiaLogData(@RequestBody Map<String,Object> params){
        return client.listsubBranchBankDiaLogData(params);
    }

    @PostMapping("/listBranchBankDiaLogData")
    public ReturnT<List> listBranchBankDiaLogData(@RequestBody Map<String,Object> params){
        return client.listBranchBankDiaLogData(params);
    }

}
