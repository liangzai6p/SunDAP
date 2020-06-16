package com.sunyard.dap.consumer.controller.dataserve;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.consumer.feign.dataserve.DmBusiOfflineClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: SunDAP
 * @description: 离线运营业务量
 * @author: yey.he
 * @create: 2020-06-12 16:00
 **/
@RestController
@Slf4j
@RequestMapping("/client/dmBusiOffline")
public class DmBusiOfflineController {
    @Autowired
    private DmBusiOfflineClient client;

    @GetMapping("/info")
    public ReturnT<List> getInfo(){return client.getInfo();}

    @GetMapping("/channel")
    public ReturnT<List> getChannelInfo(){
        return client.getChannelInfo();
    }

    @GetMapping("/branch")
    public ReturnT<List> getBranchInfo(){
        return client.getBranchInfo();
    }

    @GetMapping("/site")
    public ReturnT<List> getSiteInfo(){
        return client.getSiteInfo();
    }

    @GetMapping("/zone")
    public ReturnT<List> getZoneInfo(){
        return client.getZoneInfo();
    }
}
