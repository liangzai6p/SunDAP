package com.sunyard.dap.dataserve.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.service.MacService;
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
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yey.he
 * @since 2020-08-12
 */
@RestController
@RequestMapping("/mac")
@Slf4j
@RefreshScope
public class MacController {
    @Autowired
    private MacService service;

    /**
     * 自助设备故障明细
     * @Author yey.he 
     * @Date 9:23 AM 2020/8/13
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listDetail")
    public ReturnT<Page<HashMap<String, Object>>> listDetail(@RequestBody Map<String,Object> params){
        return service.listDetail(params);
    }

    /**
     * 故障次数与故障设备数量
     * @Author yey.he 
     * @Date 9:23 AM 2020/8/13
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listFaultByTime")
    public ReturnT<List> listFaultByTime(@RequestBody Map<String,Object> params){
        return service.listFaultByTime(params);
    }

    /**
     * 实时各状态自助设备数量
     * @Author yey.he
     * @Date 9:23 AM 2020/8/13
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listStatus")
    public ReturnT<List> listStatus(@RequestBody Map<String,Object> params){
        return service.listStatus(params);
    }

    /**
     * 分行设备故障率
     * @Author yey.he 
     * @Date 3:34 PM 2020/8/14
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listBranchFaultRate")
    public ReturnT<List> listBranchFaultRate(@RequestBody Map<String,Object> params){
        return service.listBranchFaultRate(params);
    }

    /**
     * 网点设备故障率
     * @Author yey.he
     * @Date 2:36 PM 2020/8/24
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listSiteFaultRate")
    public ReturnT<List> listSiteFaultRate(@RequestBody Map<String,Object> params){
        return service.listSiteFaultRate(params);
    }


    /**
     * 设备地区分布相关
     **/
    
    /**
     * 分行设备在线情况
     * @Author yey.he 
     * @Date 11:33 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listBranchOnlineMac")
    public ReturnT<List> listBranchOnlineMac(@RequestBody Map<String,Object> params){return service.listBranchOnlineMac(params);}

    /**
     * 分行自助设备故障量
     * @Author yey.he 
     * @Date 11:33 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listBranchMacFaultCount")
    public ReturnT<List> listBranchMacFaultCount(@RequestBody Map<String,Object> params){return service.listBranchMacFaultCount(params);}

    /**
     * 分行自助设备替代率
     * @Author yey.he 
     * @Date 11:33 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listBranchMacReplaceRate")
    public ReturnT<List> listBranchMacReplaceRate(@RequestBody Map<String,Object> params){return service.listBranchMacReplaceRate(params);}

    /**
     * 月度自助设备替代率
     * @Author yey.he 
     * @Date 11:33 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacReplaceRateMonthly")
    public ReturnT<List> listMacReplaceRateMonthly(@RequestBody Map<String,Object> params){return service.listMacReplaceRateMonthly(params);}

    /**
     * 设备画像相关
     **/
    
    /**
     * 画像状态
     * @Author yey.he 
     * @Date 11:34 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/macProtrayal")
    public ReturnT<List> macProtrayal(@RequestBody Map<String,Object> params){return service.macProtrayal(params);}

    /**
     * 各类型设备总量
     * @Author yey.he 
     * @Date 11:34 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacTypeCount")
    public ReturnT<List> listMacTypeCount(@RequestBody Map<String,Object> params){return service.listMacTypeCount(params);}

    /**
     * 设备综合评价
     * @Author yey.he 
     * @Date 11:34 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacTypeAssess")
    public ReturnT<List> listMacTypeAssess(@RequestBody Map<String,Object> params){return service.listMacTypeAssess(params);}

    /**
     * 设备类型故障率
     * @Author yey.he 
     * @Date 11:35 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacTypeFaultRate")
    public ReturnT<List> listMacTypeFaultRate(@RequestBody Map<String,Object> params){return service.listMacTypeFaultRate(params);}

    /**
     * 设备品牌故障率
     * @Author yey.he 
     * @Date 11:35 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacBrandFaultRate")
    public ReturnT<List> listMacBrandFaultRate(@RequestBody Map<String,Object> params){return service.listMacBrandFaultRate(params);}

    /**
     * 月度自助设备替代高柜率
     * @Author yey.he 
     * @Date 11:35 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacReplaceOtcRateMonthly")
    public ReturnT<List> listMacReplaceOtcRateMonthly(@RequestBody Map<String,Object> params){return service.listMacReplaceOtcRateMonthly(params);}

    /**
     * 设备类型月度交易量
     * @Author yey.he 
     * @Date 11:39 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacTypeBusiMonthly")
    public ReturnT<List> listMacTypeBusiMonthly(@RequestBody Map<String,Object> params){return service.listMacTypeBusiMonthly(params);}

    /**
     * 设备类型交易成败率
     * @Author yey.he 
     * @Date 11:40 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacTypeSuccessAndFaultRate")
    public ReturnT<List> listMacTypeSuccessAndFaultRate(@RequestBody Map<String,Object> params){return service.listMacTypeSuccessAndFaultRate(params);}

    /**
     * 自助设备维护分析
     * @Author yey.he 
     * @Date 11:40 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacMaintainMonthly")
    public ReturnT<List> listMacMaintainMonthly(@RequestBody Map<String,Object> params){return service.listMacMaintainMonthly(params);}

    /**
     * 自助设备类型交易排名
     * @Author yey.he 
     * @Date 11:40 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacTypeBusiCount")
    public ReturnT<List> listMacTypeBusiCount(@RequestBody Map<String,Object> params){return service.listMacTypeBusiCount(params);}

    /**
     * 自助设备交易排名
     * @Author yey.he 
     * @Date 11:41 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.HashMap<java.lang.String,java.lang.Object>>>
     **/
    @PostMapping("/listMacBusiCount")
    public ReturnT<Page<HashMap<String, Object>>> listMacBusiCount(@RequestBody Map<String, Object> params){return service.listMacBusiCount(params);}





    /**
     * 网点设备故障量
     * @Author yey.he
     * @Date 1:57 PM 2020/8/24
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listSiteFaultCount")
    public ReturnT<List> listSiteFaultCount(@RequestBody Map<String, Object> params){return service.listSiteFaultCount(params);}







}

