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
     * 全行日度离线运营业务量
     * @Author yey.he
     * @Date 10:39 AM 2020/9/22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/allDailyMsg")
    public ReturnT<Map> allDailyMsg(@RequestBody Map<String,Object> params){
        return service.allDailyMsg(params);
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
     * 分行日度离线运营业务量
     * @Author xiao.xie
     * @Date 2021-1-29 10:15:21
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/branchDailyLine")
    public ReturnT<Map> branchDailyLine(@RequestBody Map<String,Object> params){
        return service.branchDailyLine(params);
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
     * 渠道月度离线运营业务量
     * @Author xiao.xie
     * @Date 11:28 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/channelMonthlyLine")
    public ReturnT<Map> channelMonthlyLine(@RequestBody Map<String,Object> params){
        return service.channelMonthlyLine(params);
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
     * 区域业务总量统计表格数据
     * @Author xiao.xie
     * @Date 2021-1-14 09:06:49
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/zoneCountTable")
    public ReturnT<Map> zoneCountTable(@RequestBody Map<String,Object> params){
        return service.zoneCountTable(params);
    }

    /**
     * 区域业务总量统计表格数据(业务地区分布量表格)
     * @Author xiao.xie
     * @Date 2021-1-14 09:06:49
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/zoneCountBVDTable")
    public ReturnT<Map> zoneCountBVDTable(@RequestBody Map<String,Object> params){
        return service.zoneCountBVDTable(params);
    }

    /**
     * 区域业务总量统计表格数据
     * @Author xiao.xie
     * @Date 2021-1-14 09:06:49
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/zoneCountMap")
    public ReturnT<Map> zoneCountMap(@RequestBody Map<String,Object> params){
        return service.zoneCountMap(params);
    }

    /**
     * 区域业务总量统计表格数据
     * @Author xiao.xie
     * @Date 2021-1-14 09:06:49
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/zoneCountBVDMap")
    public ReturnT<Map> zoneCountBVDMap(@RequestBody Map<String,Object> params){
        return service.zoneCountBVDMap(params);
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

    /**
     * 渠道业务总量统计柱状图数据
     * @Author xiao.xie
     * @Date 2021-1-27 10:33:51
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/channelCountElcBar")
    public ReturnT<Map> channelCountElcBar(@RequestBody Map<String,Object> params){
        return service.channelCountElcBar(params);
    }

    /**
     * 渠道业务总量统计
     * @Author xiao.xie
     * @Date 2021-1-18 14:48:30
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/channelCountPie")
    public ReturnT<Map> channelCountPie(@RequestBody Map<String,Object> params){
        return service.channelCountPie(params);
    }

    /**
     * 渠道业务总量统计
     * @Author xiao.xie
     * @Date 2021-1-18 14:48:30
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/channelCountElcPie")
    public ReturnT<Map> channelCountElcPie(@RequestBody Map<String,Object> params){
        return service.channelCountElcPie(params);
    }

    /**
     * 机构活跃度排名
     * @Author jie.zheng
     * @Date 3:59 PM 2021/1/25
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/branchRank")
    public ReturnT<List> branchRank(@RequestBody Map<String,Object> params){
        return service.branchRank(params);
    }

    /**
     * 运营业务成本分析
     * @Author jie.zheng
     * @Date 3:59 PM 2021/1/25
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/operateCost")
    public ReturnT<List> operateCost(@RequestBody Map<String,Object> params){
        return service.operateCost(params);
    }

}

