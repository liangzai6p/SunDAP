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
     * 分行设备故障率（直接返回处理好的数据）
     * @Author xiao.xie
     * @Date 2021-1-12 10:18:52
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/listBranchFaultRateXY")
    public ReturnT<Map> listBranchFaultRateXY(@RequestBody Map<String,Object> params){
        return service.listBranchFaultRateXY(params);
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
     * 网点设备故障率
     * @Author yey.he
     * @Date 2:36 PM 2020/8/24
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listSiteFaultRateBar")
    public ReturnT<Map> listSiteFaultRateBar(@RequestBody Map<String,Object> params){
        return service.listSiteFaultRateBar(params);
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
     * 分行设备在线情况(返回处理好的图表数据)
     * @Author xiao.xie
     * @Date 2021-1-12 15:12:14
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/listBranchOnlineMacMap")
    public ReturnT<Map> listBranchOnlineMacMap(@RequestBody Map<String,Object> params){return service.listBranchOnlineMacMap(params);}

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
     * 分行自助设备替代率(进过处理后的echarts图表数据)
     * @Author yey.he
     * @Date 11:33 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listBranchMacReplaceRateXY")
    public ReturnT<Map> listBranchMacReplaceRateXY(@RequestBody Map<String,Object> params){
        return service.listBranchMacReplaceRateXY(params);
    }
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
     * 月度自助设备替代率
     * @Author xiao.xie
     * @Date 2021-1-13 08:59:56
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/listMacReplaceRateMonthlyXY")
    public ReturnT<Map> listMacReplaceRateMonthlyXY(@RequestBody Map<String,Object> params){return service.listMacReplaceRateMonthlyXY(params);}

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
     * 设备综合评价
     * @Author xiao.xie
     * @Date 2021-1-28 10:41:59
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/listMacTypeAssessRadar")
    public ReturnT<Map> listMacTypeAssessRadar(@RequestBody Map<String,Object> params){return service.listMacTypeAssessRadar(params);}

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
     * 设备类型故障率
     * @Author yey.he
     * @Date 11:35 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacTypeFaultRatePie")
    public ReturnT<Map> listMacTypeFaultRateLine(@RequestBody Map<String,Object> params){return service.listMacTypeFaultRatePie(params);}

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
     * 设备品牌故障率
     * @Author xiao.xie
     * @Date 2021-1-28 15:45:32
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/listMacBrandFaultRatePie")
    public ReturnT<Map> listMacBrandFaultRatePie(@RequestBody Map<String,Object> params){return service.listMacBrandFaultRatePie(params);}

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
     * 月度自助设备替代高柜率
     * @Author yey.he
     * @Date 11:35 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacReplaceOtcRateMonthlyLine")
    public ReturnT<Map> listMacReplaceOtcRateMonthlyLine(@RequestBody Map<String,Object> params){return service.listMacReplaceOtcRateMonthlyLine(params);}


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
     * 设备类型月度交易量
     * @Author xiao.xie
     * @Date 2021-1-28 14:38:52
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacTypeBusiMonthlyLine")
    public ReturnT<Map> listMacTypeBusiMonthlyLine(@RequestBody Map<String,Object> params){return service.listMacTypeBusiMonthlyLine(params);}

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
     * 设备类型交易成败率
     * @Author xiao.xie
     * @Date 2021-1-28 16:46:14
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacTypeSuccessAndFaultRateBar")
    public ReturnT<Map> listMacTypeSuccessAndFaultRateBar(@RequestBody Map<String,Object> params){return service.listMacTypeSuccessAndFaultRateBar(params);}


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
     * 自助设备报修率分析
     * @Author jie.zheng
     * @Date 11:40 AM 2021/01/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacMaintainSuccessRateMonthly")
    public ReturnT<List> listMacMaintainSuccessRateMonthly(@RequestBody Map<String,Object> params){return service.listMacMaintainSuccessRateMonthly(params);}


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
     * 自助设备类型交易排名
     * @Author yey.he
     * @Date 11:40 AM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listMacTypeBusiCountBar")
    public ReturnT<Map> listMacTypeBusiCountBar(@RequestBody Map<String,Object> params){return service.listMacTypeBusiCountBar(params);}

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
     * 自助设备交易排名
     * @Author xiao.xie
     * @Date 2021-1-28 17:42:07
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.HashMap<java.lang.String,java.lang.Object>>>
     **/
    @PostMapping("/listMacBusiCountBar")
    public ReturnT<Map> listMacBusiCountBar(@RequestBody Map<String, Object> params){return service.listMacBusiCountBar(params);}


    /**
     * 网点设备故障量
     * @Author yey.he
     * @Date 1:57 PM 2020/8/24
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listSiteFaultCount")
    public ReturnT<List> listSiteFaultCount(@RequestBody Map<String, Object> params){return service.listSiteFaultCount(params);}

    /**
     * @Description: 支行设备情况
     * @Param: [params]
     * @return: com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.extension.plugins.pagination.Page<java.util.HashMap<java.lang.String,java.lang.Object>>>
     * @Author: shen
     * @Date: 2021/9/24
     */
    @PostMapping("/listSiteMacDetails")
    public ReturnT<Page<HashMap<String, Object>>> listSiteMacDetails(@RequestBody Map<String,Object> params){
        return service.listSiteMacDetails(params);
    }

}

