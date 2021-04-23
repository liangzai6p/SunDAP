package com.sunyard.dap.consumer.dataserve.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    @PostMapping("/busiCount/allCount")
    ReturnT<List> getBusiCountAllCount(Map<String, Object> params);

    @PostMapping("/busiCount/allDaily")
    ReturnT<List> getBusiCountAllDaily(Map<String, Object> params);

    @PostMapping("/busiCount/allDailyMsg")
    ReturnT<Map> getBusiCountAllDailyMsg(Map<String, Object> params);

    @PostMapping("/busiCount/channelDaily")
    ReturnT<List> getBusiCountChannelDaily(Map<String, Object> params);

    @PostMapping("/busiCount/branchDaily")
    ReturnT<List> getBusiCountBranchDaily(Map<String, Object> params);

    @PostMapping("/busiCount/branchDailyLine")
    ReturnT<Map> getBusiCountBranchDailyLine(Map<String, Object> params);

    @PostMapping("/busiCount/siteDaily")
    ReturnT<List> getBusiCountSiteDaily(Map<String, Object> params);

    @PostMapping("/busiCount/zoneDaily")
    ReturnT<List> getBusiCountZoneDaily(Map<String, Object> params);

    @PostMapping("/busiCount/allMonthly")
    ReturnT<List> getBusiCountAllMonthly(Map<String, Object> params);

    @PostMapping("/busiCount/branchMonthly")
    ReturnT<List> getBusiCountBranchMonthly(Map<String, Object> params);

    @PostMapping("/busiCount/zoneMonthly")
    ReturnT<List> getBusiCountZoneMonthly(Map<String, Object> params);

    @PostMapping("/busiCount/siteMonthly")
    ReturnT<List> getBusiCountSiteMonthly(Map<String, Object> params);

    @PostMapping("/busiCount/channelMonthly")
    ReturnT<List> getBusiCountChannelMonthly(Map<String, Object> params);

    @PostMapping("/busiCount/channelMonthlyLine")
    ReturnT<Map> getBusiCountChannelMonthlyLine(Map<String, Object> params);

    @PostMapping("/busiCount/branchCount")
    ReturnT<List> getBusiCountBranchCount(Map<String, Object> params);

    @PostMapping("/busiCount/zoneCount")
    ReturnT<List> getBusiCountZoneCount(Map<String, Object> params);

    @PostMapping("/busiCount/zoneCountTable")
    public ReturnT<Map> zoneCountTable(Map<String,Object> params);

    @PostMapping("/busiCount/zoneCountBVDTable")
    public ReturnT<Map> zoneCountBVDTable(Map<String,Object> params);

    @PostMapping("/busiCount/zoneCountMap")
    public ReturnT<Map> zoneCountMap(Map<String,Object> params);

    @PostMapping("/busiCount/zoneCountBVDMap")
    public ReturnT<Map> zoneCountBVDMap(Map<String,Object> params);

    @PostMapping("/busiCount/siteCount")
    ReturnT<List> getBusiCountSiteCount(Map<String, Object> params);

    @PostMapping("/busiCount/channelCount")
    ReturnT<List> getBusiCountChannelCount(Map<String, Object> params);

    @PostMapping("/busiCount/channelCountPie")
    ReturnT<Map> channelCountPie(Map<String, Object> params);

    @PostMapping("/busiCount/channelCountElcPie")
    ReturnT<Map> channelCountElcPie(Map<String, Object> params);

    @PostMapping("/busiCount/channelCountElcBar")
    ReturnT<Map> channelCountElcBar(Map<String, Object> params);

    @PostMapping("/busiCount/branchRank")
    ReturnT<List> getBusiCountBranchRank(Map<String, Object> params);

    @PostMapping("/busiCount/operateCost")
    ReturnT<List> getBusiCountOperateCost(Map<String, Object> params);

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

    @PostMapping("/busiRT/channelHLine")
    ReturnT<Map> getBusiChannelHRTLine(Map<String, Object> params);

    @PostMapping("/busiRT/totalD")
    ReturnT<List> getBusiDRT(Map<String, Object> params);

    @PostMapping("/busiRT/branchD")
    ReturnT<List> getBusiBranchDRT(Map<String, Object> params);

    @PostMapping("/busiRT/branchDBar")
    ReturnT<Map> getBusiBranchDRTBar(Map<String, Object> params);

    @PostMapping("/busiRT/zoneD")
    ReturnT<List> getBusiZoneDRT(Map<String, Object> params);

    @PostMapping("/busiRT/zoneDTable")
    ReturnT<Map> getBusiZoneDRTTable(Map<String, Object> params);

    @PostMapping("/busiRT/zoneDMap")
    ReturnT<Map> getBusiZoneDRTMap(Map<String, Object> params);

    @PostMapping("/busiRT/siteD")
    ReturnT<List> getBusiSiteDRT(Map<String, Object> params);

    @PostMapping("/busiRT/channelD")
    ReturnT<List> getBusiChannelDRT(Map<String, Object> params);

    @PostMapping("/busiRT/channelDPie")
    ReturnT<Map> getBusiChannelDRTPie(Map<String, Object> params);

    @PostMapping("/busiRT/listByState")
    ReturnT<Page<HashMap<String, Object>>> getBusiListByState(Map<String, Object> params);

    @PostMapping("/busiRT/countState")
    ReturnT<List> getBusiRtCountState(Map<String, Object> params);

    /**
     * 历史业务种类业务量
     **/
    @PostMapping("/busiTcount/list")
    ReturnT<List> getBusiTcountListType(Map<String, Object> params);

    @PostMapping("/busiTcount/listPie")
    ReturnT<Map> getListTypePie(Map<String, Object> params);

    @PostMapping("/busiTcount/channel")
    ReturnT<List> getBusiTcountChannelInfo(Map<String, Object> params);

    @PostMapping("/busiTcount/channelPie")
    ReturnT<Map> getBusiTcountChannelInfoPie(Map<String, Object> params);

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

    @PostMapping("/busiTrt/listCurrencyIO")
    ReturnT<List> getBusiTrtListCurrencyIO(Map<String, Object> params);

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

    @PostMapping("/mac/listBranchFaultRateXY")
    public ReturnT<Map> listBranchFaultRateXY( Map<String,Object> params);

    @PostMapping("/mac/listBranchMacReplaceRateXY")
    public ReturnT<Map> listBranchMacReplaceRateXY( Map<String,Object> params);

    @PostMapping("/mac/listSiteFaultRate")
    ReturnT<List> getMacListSiteFaultRate(Map<String, Object> params);

    @PostMapping("/mac/listSiteFaultRateBar")
    ReturnT<Map> getMacListSiteFaultRateBar(Map<String, Object> params);

    @PostMapping("/mac/listBranchOnlineMacMap")
    public ReturnT<Map> listBranchOnlineMacMap(@RequestBody Map<String,Object> params);



    /**
     * 设备地区分布相关
     **/

    @PostMapping("/mac/listBranchOnlineMac")
    ReturnT<List> getMacListBranchOnlineMac(Map<String,Object> params);

    @PostMapping("/mac/listBranchMacFaultCount")
    ReturnT<List> getMacListBranchMacFaultCount(Map<String,Object> params);

    @PostMapping("/mac/listBranchMacReplaceRate")
    ReturnT<List> getMacListBranchMacReplaceRate(Map<String,Object> params);

    @PostMapping("/mac/listMacReplaceRateMonthly")
    ReturnT<List> getMacListMacReplaceRateMonthly(Map<String,Object> params);

    /**
     * 设备画像相关
     **/

    @PostMapping("/mac/macProtrayal")
    ReturnT<List> getMacProtrayal(Map<String,Object> params);

    @PostMapping("/mac/listMacTypeCount")
    ReturnT<List> getMacListMacTypeCount(Map<String,Object> params);

    @PostMapping("/mac/listMacTypeAssess")
    ReturnT<List> getMacListMacTypeAssess(Map<String,Object> params);

    @PostMapping("/mac/listMacTypeAssessRadar")
    ReturnT<Map> getMacListMacTypeAssessRadar(Map<String,Object> params);

    @PostMapping("/mac/listMacTypeFaultRate")
    ReturnT<List> getMacListMacTypeFaultRate(Map<String,Object> params);

    @PostMapping("/mac/listMacTypeFaultRatePie")
    ReturnT<Map> getMacListMacTypeFaultRatePie(Map<String,Object> params);

    @PostMapping("/mac/listMacBrandFaultRate")
    ReturnT<List> getMacListMacBrandFaultRate(Map<String,Object> params);

    @PostMapping("/mac/listMacBrandFaultRatePie")
    ReturnT<Map> getMacListMacBrandFaultRatePie(Map<String,Object> params);

    @PostMapping("/mac/listMacReplaceOtcRateMonthly")
    ReturnT<List> getMacListMacReplaceOtcRateMonthly(Map<String,Object> params);

    @PostMapping("/mac/listMacReplaceOtcRateMonthlyLine")
    ReturnT<Map> getMacListMacReplaceOtcRateMonthlyLine(Map<String,Object> params);

    @PostMapping("/mac/listMacTypeBusiMonthly")
    ReturnT<List> getMacListMacTypeBusiMonthly(Map<String,Object> params);

    @PostMapping("/mac/listMacTypeBusiMonthlyLine")
    ReturnT<Map> getMacListMacTypeBusiMonthlyLine(Map<String,Object> params);

    @PostMapping("/mac/listMacTypeSuccessAndFaultRate")
    ReturnT<List> getMacListMacTypeSuccessAndFaultRate(Map<String,Object> params);

    @PostMapping("/mac/listMacTypeSuccessAndFaultRateBar")
    ReturnT<Map> getMacListMacTypeSuccessAndFaultRateBar(Map<String,Object> params);

    @PostMapping("/mac/listMacMaintainMonthly")
    ReturnT<List> getMacListMacMaintainMonthly(Map<String,Object> params);

    @PostMapping("/mac/listMacMaintainMonthlyBar")
    ReturnT<Map> getMacListMacMaintainMonthlyBar(Map<String,Object> params);

    @PostMapping("/mac/listMacTypeBusiCount")
    ReturnT<List> getMacListMacTypeBusiCount(Map<String,Object> params);

    @PostMapping("/mac/listMacTypeBusiCountBar")
    ReturnT<Map> getMacListMacTypeBusiCountBar(Map<String,Object> params);

    @PostMapping("/mac/listMacMaintainSuccessRateMonthly")
    ReturnT<List> getMacListMacMaintainSuccessRateMonthly(Map<String,Object> params);

    @PostMapping("/mac/listMacBusiCount")
    ReturnT<Page<HashMap<String, Object>>> getMacListMacBusiCount(Map<String, Object> params);

    @PostMapping("/mac/listMacBusiCountBar")
    ReturnT<Map> getMacListMacBusiCountBar(Map<String, Object> params);

    @PostMapping("/mac/listMacReplaceRateMonthlyXY")
    public ReturnT<Map> listMacReplaceRateMonthlyXY(Map<String,Object> params);

    @PostMapping("/mac/listSiteFaultCount")
    ReturnT<List> getMacListSiteFaultCount(Map<String,Object> params);




    /**
     * 柜员信息
     **/
    @PostMapping("/teller/listTeller")
    ReturnT<Page<HashMap<String, Object>>> getTellerListTeller(Map<String, Object> params);

    @PostMapping("/teller/listTellerAssess")
    ReturnT<Page<HashMap<String, Object>>> getTellerListTellerAssess(Map<String, Object> params);
    @PostMapping("/teller/listTellerAssessCompreScore")
    public ReturnT<Page<HashMap<String, Object>>> listTellerAssessCompreScore( Map<String,Object> params);
    @PostMapping("/teller/listTellerAssessChartData")
    public ReturnT<Map> listTellerAssessChartData( Map<String,Object> params);
    @PostMapping("/teller/listTellerGrade")
    ReturnT<Page<HashMap<String, Object>>> getTellerListTellerGrade(Map<String, Object> params);

    @PostMapping("/teller/listStatus")
    ReturnT<List> getTellerListStatus(Map<String, Object> params);

    @PostMapping("/teller/listBranchOffline")
    ReturnT<List> getTellerListBranchOffline(Map<String, Object> params);

    @PostMapping("/teller/listBranchOfflineBarChart")
    public ReturnT<Map> listBranchOfflineBarChart(Map<String,Object> params);

    @PostMapping("/teller/listSiteOffline")
    ReturnT<List> getTellerListSiteOffline(Map<String, Object> params);

    @PostMapping("/teller/listSiteOfflineBar")
    ReturnT<Map> getTellerListSiteOfflineBar(Map<String, Object> params);

    @PostMapping("/teller/listRoleStatus")
    ReturnT<List> getTellerListRoleStatus(Map<String, Object> params);

    @PostMapping("/teller/listRoleStatusBar")
    ReturnT<Map> getTellerListRoleStatusBar(Map<String, Object> params);

    @PostMapping("/teller/listTellerRank")
    ReturnT<Page<HashMap<String, Object>>> getTellerListTellerRank(Map<String, Object> params);

    @PostMapping("/teller/listTellerRankBar")
    ReturnT<Map> getTellerListTellerRankBar(Map<String, Object> params);

    @PostMapping("/teller/listByBranch")
    ReturnT<List> listByBranch(Map<String, Object> params);



    /**
     * 业务差错
     **/
    @PostMapping("/busiError/listBranchErrorCount")
    ReturnT<List> getBusiErrorListBranchErrorCount(Map<String, Object> params);

    @PostMapping("/busiError/listBranchErrorCountBar")
    ReturnT<Map> getBusiErrorListBranchErrorCountBar(Map<String, Object> params);

    @PostMapping("/busiError/listSiteErrorCount")
    ReturnT<List> getBusiErrorListSiteErrorCount(Map<String, Object> params);

    @PostMapping("/busiError/listSiteErrorCountBar")
    ReturnT<Map> getBusiErrorListSiteErrorCountBar(Map<String, Object> params);

    /**
     * 网点机构
     **/
    @PostMapping("/site/listBaseInfo")
    ReturnT<List> getSiteListBaseInfo(Map<String,Object> params);

    @PostMapping("/site/listSiteGrade")
    ReturnT<List> getSiteListSiteGrade(Map<String,Object> params);

    @PostMapping("/site/listTransStatus")
    ReturnT<List> getSiteListTransStatus(Map<String,Object> params);

    @PostMapping("/site/listHallInfo")
    ReturnT<List> getSiteListHallInfo(Map<String,Object> params);


    @PostMapping("/site/listCashInfo")
    ReturnT<List> getSiteListCashInfo(Map<String,Object> params);

    @PostMapping("/site/listCashInfoBar")
    ReturnT<Map> getSiteListCashInfoMap(Map<String,Object> params);

    @PostMapping("/site/listQueHourly")
    ReturnT<List> getSiteListQueHourly(Map<String,Object> params);

    @PostMapping("/site/listQueHourlyLine")
    ReturnT<Map> getSiteListQueHourlyLine(Map<String,Object> params);

    @PostMapping("/site/listMacBusiTypeCountHourly")
    ReturnT<List> getSiteListMacBusiTypeCountHourly(Map<String,Object> params);

    @PostMapping("/site/listMacBusiTypeCountHourlyLine")
    ReturnT<Map> getSiteListMacBusiTypeCountHourlyLine(Map<String,Object> params);

    /**
     * 渠道
     **/
    @PostMapping("/channel/findEleAllRplRate")
    ReturnT<List> getChannelFindEleAllRplRate(Map<String,Object> params);

    @PostMapping("/channel/listEleRplRateByBranch")
    ReturnT<List> getChannelListEleRplRateByBranch(Map<String,Object> params);

    @PostMapping("/channel/listEleRplRateByBranchMap")
    ReturnT<Map> getChannelListEleRplRateByBranchMap(Map<String,Object> params);

    @PostMapping("/channel/listEleRplRateByBranchTable")
    ReturnT<List> getChannelListEleRplRateByBranchTable(Map<String,Object> params);

    @PostMapping("/channel/listEleAllRplRateDaily")
    ReturnT<Map> getChannelListEleAllRplRateDaily(Map<String,Object> params);

    @PostMapping("/channel/listEleAllRplRateDailyLineChat")
    ReturnT<Map> listEleAllRplRateDailyLineChat(Map<String,Object> params);

    @PostMapping("/channel/listEleAllRplRateMonthly")
    ReturnT<List> getChannelListEleAllRplRateMonthly(Map<String,Object> params);

    @PostMapping("/channel/listEleAllRplRateMonthlyLine")
    ReturnT<Map> getChannelListEleAllRplRateMonthlyLine(Map<String,Object> params);

    @PostMapping("/channel/listEleGradeByChannel")
    ReturnT<List> getChannelListEleGradeByChannel(Map<String,Object> params);

    @PostMapping("/channel/listEleGradeByChannelRadar")
    ReturnT<Map> getChannelListEleGradeByChannelRadar(Map<String,Object> params);

    @PostMapping("/channel/listEleSatisByChannel")
    ReturnT<List> getChannelListEleSatisByChannel(Map<String,Object> params);

    @PostMapping("/channel/listEleSatisByChannelBar")
    ReturnT<Map> getChannelListEleSatisByChannelBar(Map<String,Object> params);

    @PostMapping("/channel/listEleRplRateByChannel")
    ReturnT<List> getChannelListEleRplRateByChannel(Map<String,Object> params);

    @PostMapping("/channel/listEleRplRateByChannelBar")
    ReturnT<Map> getChannelListEleRplRateByChannelBar(Map<String,Object> params);

    @PostMapping("/channel/listCusCountByChannel")
    ReturnT<List> getChannelListCusCountByChannel(Map<String,Object> params);

    @PostMapping("/channel/listCusCountByChannelBar")
    ReturnT<Map> getChannelListCusCountByChannelBar(Map<String,Object> params);

    /**
     * 机构数据（银行）
     **/
    @PostMapping("/banks/listBank")
    ReturnT<List> listBank(Map<String,Object> params);
    @PostMapping("/banks/listBankDiaLogData")
    ReturnT<List> listBankDiaLogData(Map<String, Object> params);
}
