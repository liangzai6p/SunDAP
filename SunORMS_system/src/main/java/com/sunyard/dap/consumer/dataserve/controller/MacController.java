package com.sunyard.dap.consumer.dataserve.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.consumer.dataserve.client.DataServeClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
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
 * @program: SunDAP
 * @description: 自助设备
 * @author: yey.he
 * @create: 2020-08-13 09:26
 **/
@RestController
@RequestMapping("/sundap/mac")
@Slf4j
@RefreshScope
public class MacController {
    @Autowired
    private DataServeClient client;

    @PostMapping("/listDetail")
    public ReturnT<Page<HashMap<String, Object>>> getListDetail(@RequestBody Map<String,Object> params){return client.getMacListDetail(params);}

    @PostMapping("/listFaultByTime")
    public ReturnT<List> getListFaultByTime(@RequestBody Map<String,Object> params){return client.getMacListFaultByTime(params);}

    @PostMapping("/listStatus")
    public ReturnT<List> getListStatus(@RequestBody Map<String,Object> params){return client.getMacListStatus(params);}

    @PostMapping("/listBranchFaultRate")
    public ReturnT<List> getListBranchFaultRate(@RequestBody Map<String,Object> params){
        return client.getMacListBranchFaultRate(params);
    }
    @PostMapping("/listBranchFaultRateXY")
    public ReturnT<Map> listBranchFaultRateXY(@RequestBody Map<String,Object> params){
        return client.listBranchFaultRateXY(params);
    }
    @PostMapping("/listSiteFaultRate")
    public ReturnT<List> getListSiteFaultRate(@RequestBody Map<String,Object> params){
        return client.getMacListSiteFaultRate(params);
    }
    @PostMapping("/listSiteFaultRateBar")
    public ReturnT<Map> getListSiteFaultRateBar(@RequestBody Map<String,Object> params){
        return client.getMacListSiteFaultRateBar(params);
    }
    @PostMapping("/listBranchMacReplaceRateXY")
    public ReturnT<Map> listBranchMacReplaceRateXY(@RequestBody Map<String,Object> params){
        return client.listBranchMacReplaceRateXY(params);
    };
    @PostMapping("/listBranchOnlineMacMap")
    public ReturnT<Map> listBranchOnlineMacMap(@RequestBody Map<String,Object> params){
        return client.listBranchOnlineMacMap(params);
    }
    /**
     * 设备地区分布相关
     **/

    @PostMapping("/listBranchOnlineMac")
    public ReturnT<List> getListBranchOnlineMac(@RequestBody Map<String,Object> params){return client.getMacListBranchOnlineMac(params);}

    @PostMapping("/listBranchMacFaultCount")
    public ReturnT<List> getListBranchMacFaultCount(@RequestBody Map<String,Object> params){return client.getMacListBranchMacFaultCount(params);}

    @PostMapping("/listBranchMacReplaceRate")
    public ReturnT<List> getListBranchMacReplaceRate(@RequestBody Map<String,Object> params){return client.getMacListBranchMacReplaceRate(params);}

    @PostMapping("/listMacReplaceRateMonthly")
    public ReturnT<List> getListMacReplaceRateMonthly(@RequestBody Map<String,Object> params){return client.getMacListMacReplaceRateMonthly(params);}

    /**
     * 设备画像相关
     **/

    @PostMapping("/macProtrayal")
    public ReturnT<List> getMacProtrayal(@RequestBody Map<String,Object> params){return client.getMacProtrayal(params);}

    @PostMapping("/listMacTypeCount")
    public ReturnT<List> getListMacTypeCount(@RequestBody Map<String,Object> params){return client.getMacListMacTypeCount(params);}

    @PostMapping("/listMacTypeAssess")
    public ReturnT<List> getListMacTypeAssess(@RequestBody Map<String,Object> params){return client.getMacListMacTypeAssess(params);}

    @PostMapping("/listMacTypeAssessRadar")
    public ReturnT<Map> getListMacTypeAssessRadar(@RequestBody Map<String,Object> params){return client.getMacListMacTypeAssessRadar(params);}

    @PostMapping("/listMacTypeFaultRate")
    public ReturnT<List> getListMacTypeFaultRate(@RequestBody Map<String,Object> params){return client.getMacListMacTypeFaultRate(params);}

    @PostMapping("/listMacTypeFaultRatePie")
    public ReturnT<Map> getListMacTypeFaultRatePie(@RequestBody Map<String,Object> params){return client.getMacListMacTypeFaultRatePie(params);}

    @PostMapping("/listMacBrandFaultRate")
    public ReturnT<List> getListMacBrandFaultRate(@RequestBody Map<String,Object> params){return client.getMacListMacBrandFaultRate(params);}

    @PostMapping("/listMacBrandFaultRatePie")
    public ReturnT<Map> getListMacBrandFaultRatePie(@RequestBody Map<String,Object> params){return client.getMacListMacBrandFaultRatePie(params);}

    @PostMapping("/listMacReplaceOtcRateMonthly")
    public ReturnT<List> getListMacReplaceOtcRateMonthly(@RequestBody Map<String,Object> params){return client.getMacListMacReplaceOtcRateMonthly(params);}

    @PostMapping("/listMacReplaceOtcRateMonthlyLine")
    public ReturnT<Map> getListMacReplaceOtcRateMonthlyLine(@RequestBody Map<String,Object> params){return client.getMacListMacReplaceOtcRateMonthlyLine(params);}

    @PostMapping("/listMacTypeBusiMonthly")
    public ReturnT<List> getListMacTypeBusiMonthly(@RequestBody Map<String,Object> params){return client.getMacListMacTypeBusiMonthly(params);}

    @PostMapping("/listMacTypeBusiMonthlyLine")
    public ReturnT<Map> getListMacTypeBusiMonthlyLine(@RequestBody Map<String,Object> params){return client.getMacListMacTypeBusiMonthlyLine(params);}

    @PostMapping("/listMacTypeSuccessAndFaultRate")
    public ReturnT<List> getListMacTypeSuccessAndFaultRate(@RequestBody Map<String,Object> params){return client.getMacListMacTypeSuccessAndFaultRate(params);}

    @PostMapping("/listMacTypeSuccessAndFaultRateBar")
    public ReturnT<Map> getListMacTypeSuccessAndFaultRateBar(@RequestBody Map<String,Object> params){return client.getMacListMacTypeSuccessAndFaultRateBar(params);}

    @PostMapping("/listMacMaintainMonthly")
    public ReturnT<List> getListMacMaintainMonthly(@RequestBody Map<String,Object> params){return client.getMacListMacMaintainMonthly(params);}

    @PostMapping("/listMacMaintainMonthlyBar")
    public ReturnT<Map> getListMacMaintainMonthlyBar(@RequestBody Map<String,Object> params){return client.getMacListMacMaintainMonthlyBar(params);}

    @PostMapping("/listMacTypeBusiCount")
    public ReturnT<List> getListMacTypeBusiCount(@RequestBody Map<String,Object> params){return client.getMacListMacTypeBusiCount(params);}

    @PostMapping("/listMacTypeBusiCountBar")
    public ReturnT<Map> getListMacTypeBusiCountBar(@RequestBody Map<String,Object> params){return client.getMacListMacTypeBusiCountBar(params);}

    @PostMapping("/listMacMaintainSuccessRateMonthly")
    public ReturnT<List> getListMacMaintainSuccessRateMonthly(@RequestBody Map<String,Object> params){return client.getMacListMacMaintainSuccessRateMonthly(params);}

    @PostMapping("/listMacBusiCount")
    public ReturnT<Page<HashMap<String, Object>>> getListMacBusiCount(@RequestBody Map<String, Object> params){return client.getMacListMacBusiCount(params);}

    @PostMapping("/listMacBusiCountBar")
    public ReturnT<Map> getListMacBusiCountBar(@RequestBody Map<String, Object> params){return client.getMacListMacBusiCountBar(params);}

    @PostMapping("/listMacReplaceRateMonthlyXY")
    public ReturnT<Map> listMacReplaceRateMonthlyXY(@RequestBody Map<String,Object> params){
        return client.listMacReplaceRateMonthlyXY(params);
    }

    @PostMapping("/listSiteFaultCount")
    public ReturnT<List> getListSiteFaultCount(@RequestBody Map<String,Object> params){return client.getMacListSiteFaultCount(params);}


    @PostMapping("/listSiteMacDetails")
    public ReturnT<Page<HashMap<String, Object>>> getListSiteMacDetails(@RequestBody Map<String,Object> params){
        return client.getListSiteMacDetails(params);
    }
}
