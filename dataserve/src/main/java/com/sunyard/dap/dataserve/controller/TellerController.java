package com.sunyard.dap.dataserve.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.service.TellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yey.he
 * @since 2020-08-14
 */
@RestController
@RequestMapping("/teller")
@Slf4j
@RefreshScope
public class TellerController {
    @Autowired
    private TellerService service;

    /**
     * 柜员基本信息
     * @Author yey.he
     * @Date 10:32 AM 2020/8/14
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.HashMap<java.lang.String,java.lang.Object>>>
     **/
    @PostMapping("/listTeller")
    public ReturnT<Page<HashMap<String, Object>>> listTeller(@RequestBody Map<String,Object> params){
        return service.listTeller(params);
    }

    /**
     * 柜员月度考核结果
     * @Author yey.he
     * @Date 10:32 AM 2020/8/14
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.HashMap<java.lang.String,java.lang.Object>>>
     **/
    @PostMapping("/listTellerAssess")
    public ReturnT<Page<HashMap<String, Object>>> listTellerAssess(@RequestBody Map<String,Object> params){
        return service.listTellerAssess(params);
    }
    /**
     * 获取对比分数
     * @Author xiao.xie
     * @Date 2021-1-11 15:28:06
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.HashMap<java.lang.String,java.lang.Object>>>
     **/
    @PostMapping("/listTellerAssessCompreScore")
    public ReturnT<Page<HashMap<String, Object>>> listTellerAssessCompreScore(@RequestBody Map<String,Object> params){

        return service.listTellerAssessCompreScore(params);
    }
    /**
     * 获取对比分数
     * @Author xiao.xie
     * @Date 2021-1-11 15:28:06
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.HashMap<java.lang.String,java.lang.Object>>>
     **/
    @PostMapping("/listTellerAssessChartData")
    public ReturnT listTellerAssessChartData(@RequestBody Map<String,Object> params){
        return service.listTellerAssessChartDate(params);
    }
    /**
     * 柜员综合评价
     * @Author yey.he
     * @Date 10:32 AM 2020/8/14
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.HashMap<java.lang.String,java.lang.Object>>>
     **/
    @PostMapping("/listTellerGrade")
    public ReturnT<Page<HashMap<String, Object>>> listTellerGrade(@RequestBody Map<String,Object> params){
        return service.listTellerGrade(params);
    }

    /**
     * 柜员状态
     * @Author yey.he
     * @Date 10:37 AM 2020/8/14
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listStatus")
    public ReturnT<List> listStatus(@RequestBody Map<String,Object> params){
        return service.listStatus(params);
    }

    /**
     * 分行不在岗人员排名
     * @Author yey.he
     * @Date 3:01 PM 2020/8/14
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listBranchOffline")
    public ReturnT<List> listBranchOffline(@RequestBody Map<String,Object> params){
        return service.listBranchOffline(params);
    }
    /**
     * 分行不在岗人员排名柱状图相关数据
     * @Author yey.he
     * @Date 3:01 PM 2020/8/14
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listBranchOfflineBarChart")
    public ReturnT<Map> listBranchOfflineBarChart(Map<String, Object> params){
        return service.listBranchOfflineBarChart(params);
    }

    /**
     * 网点不在岗人员排名
     * @Author yey.he
     * @Date 9:17 AM 2020/8/24
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listSiteOffline")
    public ReturnT<List> listSiteOffline(@RequestBody Map<String,Object> params){
        return service.listSiteOffline(params);
    }

    /**
     * 网点不在岗人员排名
     * @Author yey.he
     * @Date 9:17 AM 2020/8/24
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listSiteOfflineBar")
    public ReturnT<Map> listSiteOfflineBar(@RequestBody Map<String,Object> params){
        return service.listSiteOfflineBar(params);
    }

    /**
     * 柜台角色状态
     * @Author yey.he
     * @Date 5:03 PM 2020/8/26
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listRoleStatus")
    public ReturnT<List> listRoleStatus(@RequestBody Map<String,Object> params){
        return service.listRoleStatus(params);
    }
    /**
     * 柜台角色状态
     * @Author yey.he
     * @Date 5:03 PM 2020/8/26
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listRoleStatusBar")
    public ReturnT<Map> listRoleStatusBar(@RequestBody Map<String,Object> params){
        return service.listRoleStatusBar(params);
    }
    /**
     * 柜员排名
     * @Author yey.he
     * @Date 11:25 AM 2020/8/27
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.HashMap<java.lang.String,java.lang.Object>>>
     **/
    @PostMapping("/listTellerRank")
    public ReturnT<Page<HashMap<String, Object>>> listTellerRank(@RequestBody Map<String,Object> params){
        return service.listTellerRank(params);
    }
    /**
     * 柜员排名
     * @Author yey.he
     * @Date 11:25 AM 2020/8/27
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.HashMap<java.lang.String,java.lang.Object>>>
     **/
    @PostMapping("/listTellerRankBar")
    public ReturnT<Map> listTellerRankBar(@RequestBody Map<String,Object> params){
        return service.listTellerRankBar(params);
    }
    /**
     * 柜员基本信息
     * @Author
     * @Date
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listByBranch")
    public ReturnT<List> listByBranch(@RequestBody Map<String,Object> params){
//        ReturnT<List> returnT=service.listByBranch(params);
        return service.listByBranch(params);
    }

    /**
     * @Description: 人员差错明细
     * @Param: [params]
     * @return: com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.HashMap<java.lang.String,java.lang.Object>>>
     * @Author: shen
     * @Date: 2021/9/24
     */
    @PostMapping("/listTellerErrorDetails")
    public ReturnT<Page<HashMap<String, Object>>> listTellerErrorDetails(@RequestBody Map<String,Object> params){
        return service.listTellerErrorDetails(params);
    }
}

