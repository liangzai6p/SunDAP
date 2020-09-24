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
     * 全行业务总量统计
     * @Author yey.he
     * @Date 9:41 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/allCount")
    public ReturnT<List> allCount(@RequestBody Map<String,Object> params){
        return service.allCount(params);
    }

    /**
     * 全行日度离线运营业务量
     * @Author yey.he
     * @Date 10:39 AM 2020/9/22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/allDaily")
    public ReturnT<List> allDaily(@RequestBody Map<String,Object> params){
        return service.allDaily(params);
    }


    /**
     * 分渠道日度离线运营业务量
     * @Author yey.he
     * @Date 9:41 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/channelDaily")
    public ReturnT<List> channelDaily(@RequestBody Map<String,Object> params){
        return service.channelDaily(params);
    }

    /**
     * 分行日度离线运营业务量
     * @Author yey.he
     * @Date 9:58 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/branchDaily")
    public ReturnT<List> branchDaily(@RequestBody Map<String,Object> params){
        return service.branchDaily(params);
    }

    /**
     * 网点日度离线运营业务量
     * @Author yey.he 
     * @Date 9:59 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/siteDaily")
    public ReturnT<List> siteDaily(@RequestBody Map<String,Object> params){
        return service.siteDaily(params);
    }

    /**
     * 区域日度离线运营业务量
     * @Author yey.he
     * @Date 10:19 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/zoneDaily")
    public ReturnT<List> zoneDaily(@RequestBody Map<String,Object> params){
        return service.zoneDaily(params);
    }

    /**
     * 月度离线运营业务量
     * @Author yey.he
     * @Date 11:26 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/allMonthly")
    public ReturnT<List> allMonthly(@RequestBody Map<String,Object> params){
        return service.allMonthly(params);
    }

    /**
     * 分行月度离线运营业务量
     * @Author yey.he
     * @Date 11:27 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/branchMonthly")
    public ReturnT<List> branchMonthly(@RequestBody Map<String,Object> params){
        return service.branchMonthly(params);
    }

    /**
     * 区域月度离线运营业务量
     * @Author yey.he
     * @Date 11:28 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/zoneMonthly")
    public ReturnT<List> zoneMonthly(@RequestBody Map<String,Object> params){
        return service.zoneMonthly(params);
    }

    /**
     * 网点月度离线运营业务量
     * @Author yey.he
     * @Date 11:28 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/siteMonthly")
    public ReturnT<List> siteMonthly(@RequestBody Map<String,Object> params){
        return service.siteMonthly(params);
    }

    /**
     * 渠道月度离线运营业务量
     * @Author yey.he
     * @Date 11:28 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/channelMonthly")
    public ReturnT<List> channelMonthly(@RequestBody Map<String,Object> params){
        return service.channelMonthly(params);
    }

    /**
     * 分行业务总量统计
     * @Author yey.he
     * @Date 3:58 PM 2020/7/27
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/branchCount")
    public ReturnT<List> branchCount(@RequestBody Map<String,Object> params){
        return service.branchCount(params);
    }

    /**
     * 区域业务总量统计
     * @Author yey.he
     * @Date 3:59 PM 2020/7/27
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/zoneCount")
    public ReturnT<List> zoneCount(@RequestBody Map<String,Object> params){
        return service.zoneCount(params);
    }

    /**
     * 网点业务总量统计
     * @Author yey.he
     * @Date 3:59 PM 2020/7/27
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/siteCount")
    public ReturnT<List> siteCount(@RequestBody Map<String,Object> params){
        return service.siteCount(params);
    }

    /**
     * 渠道业务总量统计
     * @Author yey.he
     * @Date 3:59 PM 2020/7/27
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/channelCount")
    public ReturnT<List> channelCount(@RequestBody Map<String,Object> params){
        return service.channelCount(params);
    }
}

