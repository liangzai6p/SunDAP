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
 * @description: 实时运营业务量
 * @author: yey.he
 * @create: 2020-07-06 10:47
 **/
@RestController
@Slf4j
@RequestMapping("/sundap/busiRT")
public class BusiRTController {
    @Autowired
    private DataServeClient client;

    @PostMapping("/totalH")
    public ReturnT<List> listHRT(@RequestBody Map<String,Object> params){
        return client.listHRT(params);
    }
    
    @PostMapping("/branchH")
    public ReturnT<List> listBranchHRT(@RequestBody Map<String,Object> params){
        return client.listBranchHRT(params);
    }

    @PostMapping("/zoneH")
    public ReturnT<List> listZoneHRT(@RequestBody Map<String,Object> params){
        return client.listZoneHRT(params);
    }

    @PostMapping("/siteH")
    public ReturnT<List> listSiteHRT(@RequestBody Map<String,Object> params){
        return client.listSiteHRT(params);
    }

    @PostMapping("/channelH")
    public ReturnT<List> listChannelHRT(@RequestBody Map<String,Object> params){
        return client.listChannelHRT(params);
    }

    @PostMapping("/totalD")
    public ReturnT<List> listDRT(@RequestBody Map<String,Object> params){
        return client.listDRT(params);
    }

    @PostMapping("/branchD")
    public ReturnT<List> listBranchDRT(@RequestBody Map<String,Object> params){
        return client.listBranchDRT(params);
    }

    @PostMapping("/zoneD")
    public ReturnT<List> listZoneDRT(@RequestBody Map<String,Object> params){
        return client.listZoneDRT(params);
    }

    @PostMapping("/siteD")
    public ReturnT<List> listSiteDRT(@RequestBody Map<String,Object> params){
        return client.listSiteDRT(params);
    }

    @PostMapping("/channelD")
    public ReturnT<List> listChannelDRT(@RequestBody Map<String,Object> params){
        return client.listChannelDRT(params);
    }
}
