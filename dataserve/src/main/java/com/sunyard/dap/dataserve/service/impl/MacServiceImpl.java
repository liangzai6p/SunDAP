package com.sunyard.dap.dataserve.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.common.util.CommonUtil;
import com.sunyard.dap.dataserve.entity.MacDO;
import com.sunyard.dap.dataserve.mapper.MacMapper;
import com.sunyard.dap.dataserve.service.MacService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yey.he
 * @since 2020-08-12
 */
@Service
public class MacServiceImpl extends ServiceImpl<MacMapper, MacDO> implements MacService {

    @Override
    public ReturnT<List> listStatus(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listStatus(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listFaultByTime(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listFaultByTime(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Page<HashMap<String, Object>>> listDetail(Map<String, Object> params) {
        Page<HashMap> page = new Page<>();
        CommonUtil.setPageByParams(page, params);
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listDetail(page, params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE, "查询失败", null);
        }
    }

    @Override
    public ReturnT<List> listBranchFaultRate(Map<String, Object> params) {
        try {
            if (params.get("QUICK")==null){
                params.put("BRANCH_NO",params.get("BRANCH_NO").toString());
            }
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listBranchFaultRate(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listBranchFaultRateXY(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> list = baseMapper.listBranchFaultRate(params);
            List xAxis = new ArrayList();
            List yAxis = new ArrayList();
            List machineErrorRateOrganData = new ArrayList();
            int limit = 0;
            if (list.size() > 10) {
                limit = 9;
            } else {
                limit = list.size() - 1;
            }
            int index = 0;
            for (int i = limit; i >= 0; i--) {
                if (list.get(i).get("SITE_NO") != null){
                    xAxis.add(list.get(i).get("SITE_NAME"));
                    yAxis.add(list.get(i).get("FAULT_RATE"));
                    machineErrorRateOrganData.add(list.get(i).get("SITE_NO"));
                    index++;
                }else {
                    xAxis.add(list.get(i).get("BRANCH_NAME"));
                    yAxis.add(list.get(i).get("FAULT_RATE"));
                    machineErrorRateOrganData.add(list.get(i).get("BRANCH_NO"));
                    index++;
                }
            }
            Map map = new HashMap();
            map.put("xAxis", xAxis);
            map.put("yAxis", yAxis);
            map.put("machineErrorRateOrganData", machineErrorRateOrganData);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listSiteFaultRate(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listSiteFaultRate(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listSiteFaultRateBar(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> list = baseMapper.listSiteFaultRate(params);
            List xAxis = new ArrayList();
            List yAxis = new ArrayList();
            List busiCountOrgan = new ArrayList();
            int limit = 0;
            if (list.size() > 10) {
                limit = 9;
            } else {
                limit = list.size() - 1;
            }
            int index = 0;
            for (int i = limit; i >= 0; i--) {
                xAxis.add(list.get(i).get("SITE_NAME"));
                yAxis.add(list.get(i).get("FAULT_RATE"));
                busiCountOrgan.add(list.get(i).get("SITE_NAME"));
                index++;
            }
            Map map = new HashMap();
            map.put("xAxis", xAxis);
            map.put("yAxis", yAxis);
            map.put("busiCountOrgan", busiCountOrgan);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }


    @Override
    public ReturnT<List> listBranchOnlineMac(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listBranchOnlineMac(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listBranchOnlineMacMap(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> list = baseMapper.listBranchOnlineMac(params);
            List<HashMap<String, Object>> initMap = new ArrayList<>();
            int max = 0;
            for (HashMap m :
                    list) {
                HashMap temp = new HashMap();
                if (m.get("BRANCH_NAME").toString().substring(0, 3).equals("黑龙江")) {
                    temp.put("name", m.get("BRANCH_NAME").toString().substring(0, 3));
                } else if (m.get("BRANCH_NAME").toString().substring(0, 2).equals("内蒙")) {
                    temp.put("name", "内蒙古");
                } else {
                    temp.put("name", m.get("BRANCH_NAME").toString().substring(0, 2));
                }
                temp.put("value", m.get("ONLINE_COUNT"));
                if (max < Integer.parseInt(m.get("ONLINE_COUNT").toString())) {
                    max = Integer.parseInt(m.get("ONLINE_COUNT").toString());
                }
                initMap.add(temp);
            }
            HashMap nx = new HashMap();//补上数据库中没有的分行
            nx.put("name", "宁夏");
            nx.put("value", 0);
            initMap.add(nx);
            HashMap map = new HashMap();
            map.put("initdata", initMap);
            map.put("max", max);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listBranchMacFaultCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listBranchMacFaultCount(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listBranchMacReplaceRate(Map<String, Object> params) {
        try {
            if (params.get("QUICK")==null){
                params.put("BRANCH_NO",params.get("BRANCH_NO").toString());
            }
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listBranchMacReplaceRate(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listBranchMacReplaceRateXY(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> list = baseMapper.listBranchMacReplaceRate(params);
            List xAxis = new ArrayList();
            List yAxis = new ArrayList();
            List machineInsteadRankOrganData = new ArrayList();
            int limit = 0;
            if (list.size() > 10) {
                limit = 9;
            } else {
                limit = list.size() - 1;
            }
            int index = 0;
            for (int i = limit; i >= 0; i--) {
                if (list.get(i).get("SITE_NO") != null){
                    xAxis.add(list.get(i).get("SITE_NAME"));
                    yAxis.add(list.get(i).get("REPLACE_RATE"));
                    machineInsteadRankOrganData.add(list.get(i).get("SITE_NO"));
                    index++;
                }else {
                    xAxis.add(list.get(i).get("BRANCH_NAME"));
                    yAxis.add(list.get(i).get("REPLACE_RATE"));
                    machineInsteadRankOrganData.add(list.get(i).get("BRANCH_NO"));
                    index++;
                }
            }
            Map map = new HashMap();
            map.put("xAxis", xAxis);
            map.put("yAxis", yAxis);
            map.put("machineErrorRateOrganData", machineInsteadRankOrganData);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<List> listMacReplaceRateMonthly(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listMacReplaceRateMonthly(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listMacReplaceRateMonthlyXY(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> list = baseMapper.listMacReplaceRateMonthly(params);
            List time = new ArrayList();
            List yAxis = new ArrayList();
            for (int i = 1; i <= 12; i++) {
                yAxis.add(0);
            }
            time.add("202001");
            time.add("202002");
            time.add("202003");
            time.add("202004");
            time.add("202005");
            time.add("202006");
            time.add("202007");
            time.add("202008");
            time.add("202009");
            time.add("202010");
            time.add("202011");
            time.add("202012");
            int index = 0;//machineInsteadRateData循环指数
            for (int i = 0; i < time.size(); i++) {
                if (index < list.size()) {
                    if (list.get(index).get("MONTH").equals(time.get(i))) {
                        yAxis.set(i, list.get(index).get("REPLACE_RATE"));
                        index++;
                    }
                }
            }
            List doubleX = new ArrayList();
            List doubleY = new ArrayList();
            for (int i = 0; i < index; i++) {
                doubleY.add(yAxis.get(i));
                doubleX.add(i);
            }
            HashMap map = new HashMap();
            map.put("doubleX", doubleX);
            map.put("doubleY", doubleY);
            map.put("index", index);
            map.put("time", time);
            map.put("yAxis", yAxis);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> macProtrayal(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.macProtrayal(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listMacTypeCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listMacTypeCount(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listMacTypeAssess(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listMacTypeAssess(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listMacTypeAssessRadar(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> data=baseMapper.listMacTypeAssess(params);
            List<HashMap<String,Object>> raderData=new ArrayList<>();
            for (HashMap m:
                    data) {
                HashMap temp = new HashMap();
                temp.put("name",m.get("MAC_TYPE"));
                List value = new ArrayList();
                value.add(m.get("TRANS"));
                value.add(m.get("BRAND"));
                value.add(m.get("SAFETY"));
                value.add(m.get("STABILITY"));
                value.add(m.get("QUALITY"));
                temp.put("value", value);
                HashMap areaStyle=new HashMap();
                areaStyle.put("opacity",0.2);
                temp.put("areaStyle",areaStyle);
                raderData.add(temp);
            }
            HashMap returnMap=new HashMap();
            returnMap.put("radarData",raderData);
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",returnMap);
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listMacTypeFaultRate(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listMacTypeFaultRate(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listMacTypeFaultRatePie(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> macEventAnalyzeAjaxData=baseMapper.listMacTypeFaultRate(params);
            List seriesData = new ArrayList();
            for (int i = 0; i < macEventAnalyzeAjaxData.size(); i++) {
                if (macEventAnalyzeAjaxData.get(i).get("MAC_TYPE").toString().equals("自助发卡机")) {
                    HashMap temp=new HashMap();
                    temp.put("name","自助发卡机");
                    temp.put("value",macEventAnalyzeAjaxData.get(i).get("COUNT"));
                    seriesData.add(temp);
                }
                macEventAnalyzeAjaxData.get(i).get("MAC_TYPE").toString().equals("自助缴款机");
                if (macEventAnalyzeAjaxData.get(i).get("MAC_TYPE").toString().equals("自助查询机")) {
                    HashMap temp=new HashMap();
                    temp.put("name","自助查询机");
                    temp.put("value",macEventAnalyzeAjaxData.get(i).get("COUNT"));
                    seriesData.add(temp);
                }
                if (macEventAnalyzeAjaxData.get(i).get("MAC_TYPE").toString().equals("自助存取款机")) {

                    HashMap temp=new HashMap();
                    temp.put("name","自助存取款机");
                    temp.put("value",macEventAnalyzeAjaxData.get(i).get("COUNT"));
                    seriesData.add(temp);
                }
                if (macEventAnalyzeAjaxData.get(i).get("MAC_TYPE").toString().equals("自助缴款机")) {
                    HashMap temp=new HashMap();
                    temp.put("name","自助缴款机");
                    temp.put("value",macEventAnalyzeAjaxData.get(i).get("COUNT"));
                    seriesData.add(temp);
                }
            }
            HashMap returnMap = new HashMap();
            returnMap.put("seriesData", seriesData);
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listMacBrandFaultRate(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listMacBrandFaultRate(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listMacBrandFaultRatePie(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> machineBrandAnalyzeAjaxData=baseMapper.listMacBrandFaultRate(params);
            List machineBrandAnalyzeData = new ArrayList();
            List legendData = new ArrayList();
            for (HashMap m:
                 machineBrandAnalyzeAjaxData) {
                HashMap temp = new HashMap();
                temp.put("name",m.get("MAC_BRAND"));
                temp.put("value",m.get("COUNT"));
                machineBrandAnalyzeData.add(temp);
                legendData.add(m.get("MAC_BRAND"));
            }
            HashMap returnMap = new HashMap();
            returnMap.put("machineBrandAnalyzeData", machineBrandAnalyzeData);
            returnMap.put("legendData", legendData);
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listMacReplaceOtcRateMonthly(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listMacReplaceOtcRateMonthly(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listMacReplaceOtcRateMonthlyLine(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> InsteadAjaxData=baseMapper.listMacReplaceOtcRateMonthly(params);
            List cardValue=new ArrayList();//自助发卡机数据
            List payValue=new ArrayList();//自助缴款机数据
            int InsteadAjaxDataIndex = 0;//InsteadAjaxData循环指数
            //1到12月循环
            for (Integer i = 1; i <= 12; i++) {
                String mon = i.toString();//月份
                if (i < 10)//月份不足10，前补0
                    mon = '0' + mon;
                if (InsteadAjaxData.get(InsteadAjaxDataIndex).get("MONTH").toString().substring(4).equals(mon)) {
                    cardValue.add(InsteadAjaxData.get(InsteadAjaxDataIndex).get("CARD_RATE"));
                    payValue.add(InsteadAjaxData.get(InsteadAjaxDataIndex).get("PAY_COUNT"));
                    //防止最后一次匹配到时循环指数超过数组长度
                    if (InsteadAjaxDataIndex < (InsteadAjaxData.size() - 1)) {
                        InsteadAjaxDataIndex++;
                    }
                } else {
                    cardValue.add(0);
                    payValue.add(0);
                }
            }
            HashMap returnMap = new HashMap();
            returnMap.put("cardValue",cardValue);
            returnMap.put("payValue",payValue);
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listMacTypeBusiMonthly(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listMacTypeBusiMonthly(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listMacTypeBusiMonthlyLine(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> InsteadAjaxData=baseMapper.listMacTypeBusiMonthly(params);
            List cardValue=new ArrayList();//自助发卡机数据
            List payValue=new ArrayList();//自助缴款机数据
            List searchValue=new ArrayList();//自助查询机数据
            List cashValue=new ArrayList();//自助存取款机
            int InsteadAjaxDataIndex = 0;//InsteadAjaxData循环指数
            //1到12月循环
            for (Integer i = 1; i <= 12; i++) {
                String mon = i.toString();//月份
                if (i < 10)//月份不足10，前补0
                    mon = '0' + mon;
                if (InsteadAjaxData.get(InsteadAjaxDataIndex).get("MONTH").toString().substring(4).equals(mon)) {
                    cardValue.add(InsteadAjaxData.get(InsteadAjaxDataIndex).get("CARD_COUNT"));
                    payValue.add(InsteadAjaxData.get(InsteadAjaxDataIndex).get("PAY_COUNT"));
                    searchValue.add(InsteadAjaxData.get(InsteadAjaxDataIndex).get("SEARCH_COUNT"));
                    cashValue.add(InsteadAjaxData.get(InsteadAjaxDataIndex).get("CASH_COUNT"));
                    //防止最后一次匹配到时循环指数超过数组长度
                    if (InsteadAjaxDataIndex < (InsteadAjaxData.size() - 1)) {
                        InsteadAjaxDataIndex++;
                    }
                } else {
                    cardValue.add(0);
                    payValue.add(0);
                }
            }
            HashMap returnMap = new HashMap();
            returnMap.put("cardValue",cardValue);
            returnMap.put("payValue",payValue);
            returnMap.put("searchValue",searchValue);
            returnMap.put("cashValue",cashValue);
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listMacTypeSuccessAndFaultRate(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listMacTypeSuccessAndFaultRate(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listMacTypeSuccessAndFaultRateBar(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> macTransactionAnalyzeAjaxData=baseMapper.listMacTypeSuccessAndFaultRate(params);
            List success=new ArrayList();
            List error=new ArrayList();
            List xAxis=new ArrayList();
            for (HashMap m: macTransactionAnalyzeAjaxData) {
                HashMap temp = new HashMap();
                temp.put("value",m.get("MAC_TYPE"));
                HashMap textStyle = new HashMap();
                textStyle.put("fontSize",11);
                temp.put("textStyle",textStyle);
                xAxis.add(temp);
                success.add(m.get("SUCCESS_RATE"));
                error.add(m.get("ERROR_RATE"));
            }
            HashMap returnMap = new HashMap();
            returnMap.put("xAxis",xAxis);
            returnMap.put("success",success);
            returnMap.put("error",error);
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listMacMaintainMonthly(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listMacMaintainMonthly(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listMacMaintainMonthlyBar(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> macPreserverAnalyzeAjaxData=baseMapper.listMacMaintainMonthly(params);
            List CARD_COUNT = new ArrayList();
            List PAY_COUNT = new ArrayList();
            List SEARCH_COUNT = new ArrayList();
            List CASH_COUNT = new ArrayList();
            List xAxisData = new ArrayList();
            for (HashMap m:
                 macPreserverAnalyzeAjaxData) {
                CARD_COUNT.add(m.get("CARD_COUNT"));
                PAY_COUNT.add(m.get("PAY_COUNT"));
                SEARCH_COUNT.add(m.get("SEARCH_COUNT"));
                CASH_COUNT.add(m.get("CASH_COUNT"));
                xAxisData.add(m.get("MONTH"));
            }
            HashMap returnMap = new HashMap();
            returnMap.put("CARD_COUNT",CARD_COUNT);
            returnMap.put("PAY_COUNT",PAY_COUNT);
            returnMap.put("SEARCH_COUNT",SEARCH_COUNT);
            returnMap.put("CASH_COUNT",CASH_COUNT);
            returnMap.put("xAxisData",xAxisData);
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listMacMaintainSuccessRateMonthly(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listMacMaintainSuccessRateMonthly(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listMacTypeBusiCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listMacTypeBusiCount(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listMacTypeBusiCountBar(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> data=baseMapper.listMacTypeBusiCount(params);
            List xAxis = new ArrayList();
            List yAxis = new ArrayList();
            for (int i = data.size() - 1; i >= 0; i--) {
                xAxis.add(data.get(i).get("MAC_TYPE"));
                yAxis.add(data.get(i).get("BUSI_COUNT"));
            }
            HashMap returnMap = new HashMap();
            returnMap.put("xAxis",xAxis);
            returnMap.put("yAxis",yAxis);
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<Page<HashMap<String, Object>>> listMacBusiCount(Map<String, Object> params) {
        Page<HashMap> page = new Page<>();
        CommonUtil.setPageByParams(page, params);
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listMacBusiCount(page, params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE, "查询失败", null);
        }
    }

    @Override
    public ReturnT<Map> listMacBusiCountBar(Map<String, Object> params) {
        Page<HashMap> page = new Page<>();
        CommonUtil.setPageByParams(page, params);

        try {
            Page<HashMap<String,Object>> dataPage=baseMapper.listMacBusiCount(page, params);
            List<HashMap<String,Object>> data=dataPage.getRecords();
            List xAxis = new ArrayList();
            List yAxis = new ArrayList();
            for (int i = data.size() - 1; i >= 0; i--) {
                xAxis.add(data.get(i).get("MAC_NO").toString().substring(8));
                yAxis.add(data.get(i).get("BUSI_COUNT"));
            }
            HashMap returnMap = new HashMap();
            returnMap.put("xAxis",xAxis);
            returnMap.put("yAxis",yAxis);
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE, "查询失败", null);
        }
    }

    @Override
    public ReturnT<List> listSiteFaultCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listSiteFaultCount(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Page<HashMap<String, Object>>> listSiteMacDetails(Map<String, Object> params) {
        Page<HashMap> page = new Page<>();
        CommonUtil.setPageByParams(page,params);
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listSiteMacDetails(page,params));
        }catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"查询失败",null);
        }
    }
}
