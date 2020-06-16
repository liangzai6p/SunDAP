package com.sunyard.dap.dataserve.controller;


import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.sunyard.dap.common.entity.DmBusiOfflineTb;
import com.sunyard.dap.common.entity.DmBusiSiteV;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yey.he
 * @since 2020-06-12
 */
@RestController
@RequestMapping("/dmBusiOffline")
@Slf4j
@RefreshScope
public class DmBusiOfflineController {
    @Autowired
    private IDmBusiBranchVService dmBusiBranchVService;

    @Autowired
    private IDmBusiChannelVService dmBusiChannelVService;

    @Autowired
    private IDmBusiSiteVService dmBusiSiteVService;

    @Autowired
    private IDmBusiZoneVService dmBusiZoneVService;

    @Autowired
    private IDmBusiOfflineTbService dmBusiOfflineTbService;

    @GetMapping("/info")
    public ReturnT<List> getInfo(){
        return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",dmBusiOfflineTbService.list());
    }

    @GetMapping("/channel")
    public ReturnT<List> getChannelInfo(){
        return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",dmBusiChannelVService.list());
    }

    @GetMapping("/branch")
    public ReturnT<List> getBranchInfo(){
        return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",dmBusiBranchVService.list());
    }

    @GetMapping("/site")
    public ReturnT<List> getSiteInfo(){
        return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",dmBusiSiteVService.list());
    }

    @GetMapping("/zone")
    public ReturnT<List> getZoneInfo(){
        return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",dmBusiZoneVService.list());
    }


}

