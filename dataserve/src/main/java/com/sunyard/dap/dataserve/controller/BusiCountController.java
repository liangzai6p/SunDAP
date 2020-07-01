package com.sunyard.dap.dataserve.controller;


import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yey.he
 * @since 2020-06-12
 */
@RestController
@RequestMapping("/busiCount")
@Slf4j
@RefreshScope
public class BusiCountController {

    @Autowired
    private BusiCountService service;

    /**
     * 离线运营业务量总数据
     * @Author yey.he
     * @Date 9:41 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/info")
    public ReturnT<List> getInfo(@RequestBody Map<String,Object> params){
        return service.list(params);
    }

    /**
     * 分渠道离线运营业务量
     * @Author yey.he
     * @Date 9:41 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/channel")
    public ReturnT<List> getChannelInfo(@RequestBody Map<String,Object> params){
        return service.listChannel(params);
    }

    /**
     * 分行离线运营业务量
     * @Author yey.he
     * @Date 9:58 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/branch")
    public ReturnT<List> getBranchInfo(@RequestBody Map<String,Object> params){
        return service.listBranch(params);
    }

    /**
     * 网点离线运营业务量
     * @Author yey.he 
     * @Date 9:59 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/site")
    public ReturnT<List> getSiteInfo(@RequestBody Map<String,Object> params){
        return service.listSite(params);
    }

    /**
     * 区域离线运营业务量
     * @Author yey.he
     * @Date 10:19 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/zone")
    public ReturnT<List> getZoneInfo(@RequestBody Map<String,Object> params){
        return service.listZone(params);
    }

    /**
     * 月度离线运营业务量
     * @Author yey.he
     * @Date 11:26 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/infoMonthly")
    public ReturnT<List> getInfoMonthly(@RequestBody Map<String,Object> params){
        return service.listMonthly(params);
    }

    /**
     * 分行月度离线运营业务量
     * @Author yey.he
     * @Date 11:27 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/branchMonthly")
    public ReturnT<List> getBranchMonthly(@RequestBody Map<String,Object> params){
        return service.listBranchMonthly(params);
    }

    /**
     * 区域月度离线运营业务量
     * @Author yey.he
     * @Date 11:28 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/zoneMonthly")
    public ReturnT<List> getZoneMonthly(@RequestBody Map<String,Object> params){
        return service.listZoneMonthly(params);
    }

    /**
     * 网点月度离线运营业务量
     * @Author yey.he
     * @Date 11:28 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/siteMonthly")
    public ReturnT<List> getSiteMonthly(@RequestBody Map<String,Object> params){
        return service.listSiteMonthly(params);
    }

    /**
     * 渠道月度离线运营业务量
     * @Author yey.he
     * @Date 11:28 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/channelMonthly")
    public ReturnT<List> getChannelMonthly(@RequestBody Map<String,Object> params){
        return service.listChannelMonthly(params);
    }

    /**
     * 实时分钟总运营业务量
     * @Author yey.he 
     * @Date 10:05 AM 2020/6/30
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/totalMinRT")
    public ReturnT<List> getTotalMinRT(@RequestBody Map<String,Object> params){
        return service.listTotalMinRT(params);
    }

    /**
     * 实时小时总运营业务量
     * @Author yey.he 
     * @Date 10:06 AM 2020/6/30
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/totalHourRT")
    public ReturnT<List> getTotalHourRT(@RequestBody Map<String,Object> params){
        return service.listTotalHourRT(params);
    }

    /**
     * 实时日度总运营业务量
     * @Author yey.he 
     * @Date 10:55 AM 2020/6/30
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/totalDayRT")
    public ReturnT<List> getTotalDayRT(@RequestBody Map<String,Object> params){
        return service.listTotalDayRT(params);
    }
}

