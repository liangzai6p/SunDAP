package com.sunyard.dap.dataserve.controller;


import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.service.BusiTcountService;
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
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yey.he
 * @since 2020-07-14
 */
@RestController
@RequestMapping("/busiTcount")
@Slf4j
@RefreshScope
public class BusiTcountController {
    @Autowired
    private BusiTcountService service;

    /**
     * 业务种类历史业务量
     * @Author yey.he 
     * @Date 9:29 AM 2020/7/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/list")
    public ReturnT<List> getListType(@RequestBody Map<String,Object> params){
        return service.listType(params);
    }

    /**
     * 渠道业务种类历史业务量
     * @Author yey.he 
     * @Date 9:30 AM 2020/7/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/channel")
    public ReturnT<List> getChannelInfo(@RequestBody Map<String,Object> params){
        return service.listChannel(params);
    }

    /**
     * 分行业务种类历史业务量
     * @Author yey.he
     * @Date 9:30 AM 2020/7/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/branch")
    public ReturnT<List> getBranchInfo(@RequestBody Map<String,Object> params){
        return service.listBranch(params);
    }

    /**
     * 网点业务种类历史业务量
     * @Author yey.he
     * @Date 9:30 AM 2020/7/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/site")
    public ReturnT<List> getSiteInfo(@RequestBody Map<String,Object> params){
        return service.listSite(params);
    }

    /**
     * 区域业务种类历史业务量
     * @Author yey.he
     * @Date 9:30 AM 2020/7/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/zone")
    public ReturnT<List> getZoneInfo(@RequestBody Map<String,Object> params){
        return service.listZone(params);
    }

    /**
     * 月度业务种类历史业务量
     * @Author yey.he
     * @Date 9:30 AM 2020/7/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/typeMonthly")
    public ReturnT<List> getTypeMonthly(@RequestBody Map<String,Object> params){
        return service.listTypeMonthly(params);
    }

}

