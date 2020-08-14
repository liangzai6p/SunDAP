package com.sunyard.dap.consumer.dataserve.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: SunDAP
 * @description: 数据服务模块
 * @author: yey.he
 * @create: 2020-06-12 16:01
 **/
@Component
@FeignClient("sundap-dataserve")
public interface DataServeClient {

    /**
     * 离线运营业务量
     **/
    @PostMapping("/busiCount/info")
    ReturnT<List> getBusiCountInfo(Map<String, Object> params);

    @PostMapping("/busiCount/channel")
    ReturnT<List> getBusiCountChannelInfo(Map<String, Object> params);

    @PostMapping("/busiCount/branch")
    ReturnT<List> getBusiCountBranchInfo(Map<String, Object> params);

    @PostMapping("/busiCount/site")
    ReturnT<List> getBusiCountSiteInfo(Map<String, Object> params);

    @PostMapping("/busiCount/zone")
    ReturnT<List> getBusiCountZoneInfo(Map<String, Object> params);

    @PostMapping("/busiCount/infoMonthly")
    ReturnT<List> getBusiCountInfoMonthly(Map<String, Object> params);

    @PostMapping("/busiCount/branchMonthly")
    ReturnT<List> getBusiCountBranchMonthly(Map<String, Object> params);

    @PostMapping("/busiCount/zoneMonthly")
    ReturnT<List> getBusiCountZoneMonthly(Map<String, Object> params);

    @PostMapping("/busiCount/siteMonthly")
    ReturnT<List> getBusiCountSiteMonthly(Map<String, Object> params);

    @PostMapping("/busiCount/channelMonthly")
    ReturnT<List> getBusiCountChannelMonthly(Map<String, Object> params);

    @PostMapping("/busiCount/branchCount")
    ReturnT<List> getBusiCountBranchCount(Map<String, Object> params);

    @PostMapping("/busiCount/zoneCount")
    ReturnT<List> getBusiCountZoneCount(Map<String, Object> params);

    @PostMapping("/busiCount/siteCount")
    ReturnT<List> getBusiCountSiteCount(Map<String, Object> params);

    @PostMapping("/busiCount/channelCount")
    ReturnT<List> getBusiCountChannelCount(Map<String, Object> params);


    /**
     * 实时运营业务量
     **/

    @PostMapping("/busiRT/totalH")
    ReturnT<List> getBusiHRT(Map<String, Object> params);

    @PostMapping("/busiRT/branchH")
    ReturnT<List> getBusiBranchHRT(Map<String, Object> params);

    @PostMapping("/busiRT/zoneH")
    ReturnT<List> getBusiZoneHRT(Map<String, Object> params);

    @PostMapping("/busiRT/siteH")
    ReturnT<List> getBusiSiteHRT(Map<String, Object> params);

    @PostMapping("/busiRT/channelH")
    ReturnT<List> getBusiChannelHRT(Map<String, Object> params);

    @PostMapping("/busiRT/totalD")
    ReturnT<List> getBusiDRT(Map<String, Object> params);

    @PostMapping("/busiRT/branchD")
    ReturnT<List> getBusiBranchDRT(Map<String, Object> params);

    @PostMapping("/busiRT/zoneD")
    ReturnT<List> getBusiZoneDRT(Map<String, Object> params);

    @PostMapping("/busiRT/siteD")
    ReturnT<List> getBusiSiteDRT(Map<String, Object> params);

    @PostMapping("/busiRT/channelD")
    ReturnT<List> getBusiChannelDRT(Map<String, Object> params);

    @PostMapping("/busiRT/listByState")
    ReturnT<List> getBusiListByState(Map<String, Object> params);

    @PostMapping("/busiRT/countState")
    ReturnT<List> getBusiRtCountState(Map<String, Object> params);

    /**
     * 历史业务种类业务量
     **/
    @PostMapping("/busiTcount/list")
    ReturnT<List> getBusiTcountListType(Map<String, Object> params);

    @PostMapping("/busiTcount/channel")
    ReturnT<List> getBusiTcountChannelInfo(Map<String, Object> params);

    @PostMapping("/busiTcount/branch")
    ReturnT<List> getBusiTcountBranchInfo(Map<String, Object> params);

    @PostMapping("/busiTcount/site")
    ReturnT<List> getBusiTcountSiteInfo(Map<String, Object> params);

    @PostMapping("/busiTcount/zone")
    ReturnT<List> getBusiTcountZoneInfo(Map<String, Object> params);

    @PostMapping("/busiTcount/typeMonthly")
    ReturnT<List> getBusiTcountInfoMonthly(Map<String, Object> params);

    /**
     * 实时业务种类业务量
     **/
    @PostMapping("/busiTrt/list")
    ReturnT<List> getBusiTrtListType(Map<String, Object> params);

    @PostMapping("/busiTrt/channel")
    ReturnT<List> getBusiTrtChannelInfo(Map<String, Object> params);

    @PostMapping("/busiTrt/branch")
    ReturnT<List> getBusiTrtBranchInfo(Map<String, Object> params);

    @PostMapping("/busiTrt/site")
    ReturnT<List> getBusiTrtSiteInfo(Map<String, Object> params);

    @PostMapping("/busiTrt/zone")
    ReturnT<List> getBusiTrtZoneInfo(Map<String, Object> params);

    @PostMapping("/busiTrt/typeHourly")
    ReturnT<List> getBusiTrtInfoHourly(Map<String, Object> params);

    /**
     * 交易明细
     **/
    @PostMapping("/busiDetail/listRt")
    ReturnT<Page<HashMap<String, Object>>> getBusiDetailListRt(Map<String, Object> params);

    @PostMapping("/busiDetail/listHistory")
    ReturnT<Page<HashMap<String, Object>>> getBusiDetailListHistory(Map<String, Object> params);

    /**
     * 辽宁大屏后台
     **/
    @PostMapping("/liaoning/selectBusiNum")
    ReturnT<Map> selectBusiNum(Map<String,Object> params);

    @PostMapping("/liaoning/selectOrganBusi")
    ReturnT<List> selectOrganBusi(Map<String,Object> params);

    @PostMapping("/liaoning/getHourBusiCount")
    ReturnT<Map> getHourBusiCount(Map<String,Object> params);

    @PostMapping("/liaoning/getTransStateCount")
    ReturnT<Map> getTransStateCount(Map<String,Object> params);

    @PostMapping("/liaoning/getHourAvgTime")
    ReturnT<Map> getHourAvgTime(Map<String,Object> params);

    @PostMapping("/liaoning/getTellerBusiCount")
    ReturnT<List> getTellerBusiCount(Map<String,Object> params);

    /**
     * 自助设备
     **/
    @PostMapping("/mac/listDetail")
    ReturnT<Page<HashMap<String, Object>>> getMacListDetail(Map<String, Object> params);

    @PostMapping("/mac/listFaultByTime")
    ReturnT<List> getMacListFaultByTime(Map<String, Object> params);

    @PostMapping("/mac/listStatus")
    ReturnT<List> getMacListStatus(Map<String, Object> params);

    @PostMapping("/mac/listBranchFaultRate")
    ReturnT<List> getMacListBranchFaultRate(Map<String, Object> params);



    /**
     * 柜员信息
     **/
    @PostMapping("/teller/listTeller")
    ReturnT<Page<HashMap<String, Object>>> getTellerListTeller(Map<String, Object> params);

    @PostMapping("/teller/listTellerAssess")
    ReturnT<Page<HashMap<String, Object>>> getTellerListTellerAssess(Map<String, Object> params);

    @PostMapping("/teller/listTellerGrade")
    ReturnT<Page<HashMap<String, Object>>> getTellerListTellerGrade(Map<String, Object> params);

    @PostMapping("/teller/listStatus")
    ReturnT<List> getTellerListStatus(Map<String, Object> params);

    @PostMapping("/teller/listBranchOffline")
    ReturnT<List> getTellerListBranchOffline(Map<String, Object> params);


}
