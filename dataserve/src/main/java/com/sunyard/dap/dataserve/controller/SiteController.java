package com.sunyard.dap.dataserve.controller;


import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.service.SiteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统银行信息表 前端控制器
 * </p>
 *
 * @author yey.he
 * @since 2020-08-26
 */
@RestController
@RequestMapping("/site")
@Slf4j
@RefreshScope
public class SiteController {
    @Autowired
    private SiteService service;

    /**
     * 机构基础信息
     * @Author yey.he
     * @Date 5:02 PM 2020/8/26
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listBaseInfo")
    public ReturnT<List> listBaseInfo(@RequestBody Map<String,Object> params){
        return service.listBaseInfo(params);
    }

    /**
     * 机构评分及排名
     * @Author yey.he
     * @Date 5:02 PM 2020/8/26
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listSiteGrade")
    public ReturnT<List> listSiteGrade(@RequestBody Map<String,Object> params){
        return service.listSiteGrade(params);
    }

    /**
     * 机构交易信息
     * @Author yey.he
     * @Date 5:02 PM 2020/8/26
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listTransStatus")
    public ReturnT<List> listTransStatus(@RequestBody Map<String,Object> params){
        return service.listTransStatus(params);
    }

    /**
     * 厅堂信息
     * @Author yey.he
     * @Date 5:02 PM 2020/8/26
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listHallInfo")
    public ReturnT<List> listHallInfo(@RequestBody Map<String,Object> params){
        return service.listHallInfo(params);
    }

    /**
     * 现金管理信息
     * @Author yey.he
     * @Date 5:30 PM 2020/8/26
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listCashInfo")
    public ReturnT<List> listCashInfo(@RequestBody Map<String,Object> params){
        return service.listCashInfo(params);
    }

    /**
     * 现金管理信息
     * @Author yey.he
     * @Date 5:30 PM 2020/8/26
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listCashInfoBar")
    public ReturnT<Map> listCashInfoBar(@RequestBody Map<String,Object> params){
        return service.listCashInfoBar(params);
    }

    /**
     * 分时排队信息
     * @Author yey.he
     * @Date 5:30 PM 2020/8/26
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listQueHourly")
    public ReturnT<List> listQueHourly(@RequestBody Map<String,Object> params){
        return service.listQueHourly(params);
    }

    /**
     * 分时排队信息
     * @Author xiao.xie
     * @Date 2021-1-25 17:00:00
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/listQueHourlyLine")
    public ReturnT<Map> listQueHourlyLine(@RequestBody Map<String,Object> params){
        return service.listQueHourlyLine(params);
    }


    /**
     * 网点分时分类业务量
     * @Author yey.he
     * @Date 2:34 PM 2020/9/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacBusiTypeCountHourly")
    public ReturnT<List> listMacBusiTypeCountHourly(@RequestBody Map<String,Object> params){
        return service.listMacBusiTypeCountHourly(params);
    }
    /**
     * 网点分时分类业务量
     * @Author xiao.xie
     * @Date 2021-1-26 14:53:53
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/listMacBusiTypeCountHourlyLine")
    public ReturnT<Map> listMacBusiTypeCountHourlyLine(@RequestBody Map<String,Object> params){
        return service.listMacBusiTypeCountHourlyLine(params);
    }

    /**
    * @Description: 分行排名
    * @Param: [params]
    * @return: com.sunyard.dap.common.model.ReturnT<java.util.List>
    * @Author: shen
    * @Date: 2021/9/24
    */
    @PostMapping("/listBranchGrade")
    public ReturnT<List> listBranchGrade(@RequestBody Map<String,Object> params){
        return service.listBranchGrade(params);
    }

    /**
    * @Description: 支行评分排名
    * @Param: [params]
    * @return: com.sunyard.dap.common.model.ReturnT<java.util.List>
    * @Author: shen
    * @Date: 2021/9/24
    */
    @PostMapping("/listSiteSGrade")
    public ReturnT<List> listSiteSGrade(@RequestBody Map<String,Object> params){
        return service.listSiteSGrade(params);
    }
}

