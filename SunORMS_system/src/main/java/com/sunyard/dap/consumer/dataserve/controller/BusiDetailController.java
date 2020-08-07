package com.sunyard.dap.consumer.dataserve.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.consumer.dataserve.client.DataServeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: SunDAP
 * @description: 交易明细
 * @author: yey.he
 * @create: 2020-08-06 14:47
 **/
@RestController
@Slf4j
@RequestMapping("/sundap/busiDetail")
public class BusiDetailController {
    @Autowired
    private DataServeClient client;

    @PostMapping("listRt")
    public ReturnT<Page<HashMap<String, Object>>> listRt(@RequestBody Map<String,Object> params){
        return client.getBusiDetailListRt(params);
    }

    @PostMapping("listHistory")
    public ReturnT<Page<HashMap<String, Object>>> listHistory(@RequestBody Map<String,Object> params){
        return client.getBusiDetailListHistory(params);
    }
}
