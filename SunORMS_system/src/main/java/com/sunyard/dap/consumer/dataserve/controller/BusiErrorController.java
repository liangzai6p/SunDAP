package com.sunyard.dap.consumer.dataserve.controller;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.consumer.dataserve.client.DataServeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @program: SunDAP
 * @description: 业务差错
 * @author: yey.he
 * @create: 2020-08-20 17:28
 **/
@RestController
@Slf4j
@RequestMapping("/sundap/busiError")
public class BusiErrorController {
    @Autowired
    private DataServeClient client;

    @PostMapping("/listBranchErrorCount")
    public ReturnT<List> getListBranchErrorCount(@RequestBody Map<String,Object> params){
        return client.getBusiErrorListBranchErrorCount(params);
    }

}
