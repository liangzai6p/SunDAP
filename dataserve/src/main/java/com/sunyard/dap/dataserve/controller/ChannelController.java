package com.sunyard.dap.dataserve.controller;


import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yey.he
 * @since 2020-08-28
 */
@RestController
@RequestMapping("/channel")
@Slf4j
@RefreshScope
public class ChannelController {
    @Autowired
    private ChannelService service;

    /**
     * 电子渠道总替代率
     * @Author yey.he
     * @Date 4:34 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/findEleAllRplRate")
    public ReturnT<List> findEleAllRplRate(@RequestBody Map<String,Object> params){
        return service.findEleAllRplRate(params);
    }

    /**
     * 分行电子渠道替代率排名
     * @Author yey.he
     * @Date 4:34 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleRplRateByBranch")
    public ReturnT<List> listEleRplRateByBranch(@RequestBody Map<String,Object> params){
        return service.listEleRplRateByBranch(params);
    }

    /**
     * 分行电子渠道替代率排名
     * @Author xiao.xie
     * @Date 2021-1-19 10:03:32
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleRplRateByBranchTable")
    public ReturnT<List> listEleRplRateByBranchTable(@RequestBody Map<String,Object> params){
        return service.listEleRplRateByBranchTable(params);
    }

    /**
     * 分行电子渠道替代率排名
     * @Author xiao.xie
     * @Date 2021-1-19 10:03:32
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleRplRateByBranchMap")
    public ReturnT<Map> listEleRplRateByBranchMap(@RequestBody Map<String,Object> params){
        return service.listEleRplRateByBranchMap(params);
    }
    /**
     * 日度电子渠道总替代率趋势
     * @Author xiao.xie
     * @Date 2021-1-19 17:04:30
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleAllRplRateDailyLineChat")
    public ReturnT<Map> listEleAllRplRateDailyLineChat(@RequestBody Map<String,Object> params){
        return service.listEleAllRplRateDailyLineChat(params);
    }



    /**
     * 月度电子渠道总替代率
     * @Author yey.he
     * @Date 4:34 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleAllRplRateMonthly")
    public ReturnT<List> listEleAllRplRateMonthly(@RequestBody Map<String,Object> params){
        return service.listEleAllRplRateMonthly(params);
    }

    /**
     * 月度电子渠道总替代率
     * @Author xiao.xie
     * @Date 2021-1-28 09:52:10
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/listEleAllRplRateMonthlyLine")
    public ReturnT<Map> listEleAllRplRateMonthlyLine(@RequestBody Map<String,Object> params){
        return service.listEleAllRplRateMonthlyLine(params);
    }

    /**
     * 各电子渠道综合评价
     * @Author yey.he
     * @Date 4:34 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleGradeByChannel")
    public ReturnT<List> listEleGradeByChannel(@RequestBody Map<String,Object> params){
        return service.listEleGradeByChannel(params);
    }

    /**
     * 各电子渠道综合评价
     * @Author yey.he
     * @Date 4:34 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleGradeByChannelRadar")
    public ReturnT<Map> listEleGradeByChannelRadar(@RequestBody Map<String,Object> params){
        return service.listEleGradeByChannelRadar(params);
    }

    /**
     * 各电子渠道客户满意度
     * @Author yey.he
     * @Date 4:35 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleSatisByChannel")
    public ReturnT<List> listEleSatisByChannel(@RequestBody Map<String,Object> params){
        return service.listEleSatisByChannel(params);
    }

    /**
     * 各电子渠道客户满意度
     * @Author xiao.xie
     * @Date 2021-1-27 15:34:18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleSatisByChannelBar")
    public ReturnT<Map> listEleSatisByChannelBar(@RequestBody Map<String,Object> params){
        return service.listEleSatisByChannelBar(params);
    }

    /**
     * 各电子渠道替代率排名
     * @Author yey.he
     * @Date 4:35 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listEleRplRateByChannel")
    public ReturnT<List> listEleRplRateByChannel(@RequestBody Map<String,Object> params){
        return service.listEleRplRateByChannel(params);
    }

    /**
     * 各电子渠道替代率排名（柱状图数据）
     * @Author xiao.xie
     * @Date 2021-1-27 15:56:19
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/listEleRplRateByChannelBar")
    public ReturnT<Map> listEleRplRateByChannelBar(@RequestBody Map<String,Object> params){
        return service.listEleRplRateByChannelBar(params);
    }

    /**
     * 各电子渠道客户覆盖量
     * @Author yey.he
     * @Date 4:35 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listCusCountByChannel")
    public ReturnT<List> listCusCountByChannel(@RequestBody Map<String,Object> params){
        return service.listCusCountByChannel(params);
    }

    /**
     * 各电子渠道客户覆盖量
     * @Author yey.he
     * @Date 4:35 PM 2020/8/28
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listCusCountByChannelBar")
    public ReturnT<Map> listCusCountByChannelBar(@RequestBody Map<String,Object> params){
        return service.listCusCountByChannelBar(params);
    }

    /**
     * @Description: 渠道金额占比
     * @Param: [params]
     * @return: com.sunyard.dap.common.model.ReturnT<java.util.List>
     * @Author: shen
     * @Date: 2021/9/24
     */
    @PostMapping("/listChannelAmountRate")
    public ReturnT<List> listChannelAmountRate(@RequestBody Map<String,Object> params){
        return service.listChannelAmountRate(params);
    }
}

