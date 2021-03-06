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
        return client.getBusiHRT(params);
    }

    @PostMapping("/branchH")
    public ReturnT<List> listBranchHRT(@RequestBody Map<String,Object> params){
        return client.getBusiBranchHRT(params);
    }

    @PostMapping("/zoneH")
    public ReturnT<List> listZoneHRT(@RequestBody Map<String,Object> params){
        return client.getBusiZoneHRT(params);
    }

    @PostMapping("/siteH")
    public ReturnT<List> listSiteHRT(@RequestBody Map<String,Object> params){
        return client.getBusiSiteHRT(params);
    }

    @PostMapping("/channelH")
    public ReturnT<List> listChannelHRT(@RequestBody Map<String,Object> params){
        return client.getBusiChannelHRT(params);
    }
    @PostMapping("/channelHLine")
    public ReturnT<Map> listChannelHRTLine(@RequestBody Map<String,Object> params){
        return client.getBusiChannelHRTLine(params);
    }
    @PostMapping("/totalD")
    public ReturnT<List> listDRT(@RequestBody Map<String,Object> params){
        return client.getBusiDRT(params);
    }

    @PostMapping("/branchD")
    public ReturnT<List> listBranchDRT(@RequestBody Map<String,Object> params){
        return client.getBusiBranchDRT(params);
    }
    @PostMapping("/branchDBar")
    public ReturnT<Map> listBranchDRTBar(@RequestBody Map<String,Object> params){
        return client.getBusiBranchDRTBar(params);
    }
    @PostMapping("/zoneD")
    public ReturnT<List> listZoneDRT(@RequestBody Map<String,Object> params){
        return client.getBusiZoneDRT(params);
    }

    @PostMapping("/zoneDTable")
    public ReturnT<Map> listZoneDRTTable(@RequestBody Map<String,Object> params){
        return client.getBusiZoneDRTTable(params);
    }

    @PostMapping("/zoneDMap")
    public ReturnT<Map> listZoneDRTMap(@RequestBody Map<String,Object> params){
        return client.getBusiZoneDRTMap(params);
    }

    @PostMapping("/siteD")
    public ReturnT<List> listSiteDRT(@RequestBody Map<String,Object> params){
        return client.getBusiSiteDRT(params);
    }

    @PostMapping("/channelD")
    public ReturnT<List> listChannelDRT(@RequestBody Map<String,Object> params){
        return client.getBusiChannelDRT(params);
    }

    @PostMapping("/channelDPie")
    public ReturnT<Map> listChannelDRTPie(@RequestBody Map<String,Object> params){
        return client.getBusiChannelDRTPie(params);
    }

    @PostMapping("/listByState")
    public ReturnT<Page<HashMap<String, Object>>> listByState(@RequestBody Map<String,Object> params){
        return client.getBusiListByState(params);
    }

    @PostMapping("/countState")
    public ReturnT<List> countState(@RequestBody Map<String,Object> params){
        return client.getBusiRtCountState(params);
    }
}
