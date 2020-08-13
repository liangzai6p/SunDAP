package com.sunyard.dap.consumer.dataserve.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.consumer.dataserve.client.DataServeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: SunDAP
 * @description: 自助设备
 * @author: yey.he
 * @create: 2020-08-13 09:26
 **/
@RestController
@RequestMapping("/sundap/mac")
@Slf4j
@RefreshScope
public class MacController {
    @Autowired
    private DataServeClient client;

    @PostMapping("/listDetail")
    public ReturnT<Page<HashMap<String, Object>>> getListDetail(@RequestBody Map<String,Object> params){return client.getMacListDetail(params);}

    @PostMapping("/listFaultByTime")
    public ReturnT<List> getListFaultByTime(@RequestBody Map<String,Object> params){return client.getMacListFaultByTime(params);}

    @PostMapping("/listStatus")
    public ReturnT<List> getListStatus(@RequestBody Map<String,Object> params){return client.getMacListStatus(params);}

}
