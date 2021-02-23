package com.sunyard.dap.dataserve.service.impl;

import com.alibaba.fastjson.JSON;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.BusiCountDO;
import com.sunyard.dap.dataserve.entity.ZoneCount;
import com.sunyard.dap.dataserve.mapper.BusiCountMapper;
import com.sunyard.dap.dataserve.service.BusiCountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yey.he
 * @since 2020-06-12
 */
@Service
@Slf4j
public class BusiCountServiceImpl extends ServiceImpl<BusiCountMapper, BusiCountDO> implements BusiCountService {

    @Override
    public ReturnT<List> allCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.allCount(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }

    }

    @Override
    public ReturnT<List> allDaily(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.allDaily(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }

    }

    @Override
    public ReturnT<Map> allDailyMsg(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> data = baseMapper.allDaily(params);
            List x = new ArrayList();
            List y1 = new ArrayList();
            List y2 = new ArrayList();
            List chart2SeriesData = new ArrayList();
            String temp1="";
            String temp2="";
            for (int i=0;i<data.size()-1;i++) {
                x.add(data.get(i).get("RECORD_TIME"));
            }
            LinkedHashSet set=new LinkedHashSet();
            set.addAll(x);
            x.clear();
            x.addAll(set);
            for (int i = 0; i < x.size(); i++) {
                for (int j = 0; j < data.size()-1; j++) {
                    if (data.get(j).get("RECORD_TIME").toString().equals(x.get(i).toString())) {
                        temp1=data.get(j).get("BUSI_COUNT").toString();
                        temp2=data.get(j).get("AMOUNT").toString();
                    }
                }
                y1.add(temp1);
                y2.add(temp2);
                List a = new ArrayList();
                a.add(0,x.get(i).toString().substring(0,4)+"-"+x.get(i).toString().substring(4,6)+"-"+x.get(i).toString().substring(6,8));
                a.add(1,temp1);
                chart2SeriesData.add(a);
                temp1="";
                temp2="";
                x.set(i,x.get(i).toString().substring(4,8));
            }
            List doubleX = new ArrayList();
            for (int i = 0; i < x.size(); i++) {
                doubleX.add(i);
            }
            HashMap returnMap = new HashMap();
            returnMap.put("x",x);
            returnMap.put("y1",y1);
            returnMap.put("y2",y2);
            returnMap.put("chart2SeriesData", chart2SeriesData);
            returnMap.put("doubleX", doubleX);
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> branchDaily(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.branchDaily(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> branchDailyLine(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> data = baseMapper.branchDaily(params);
            List x = new ArrayList();
            List y1 = new ArrayList();
            List y2 = new ArrayList();
            List chart2SeriesData = new ArrayList();
            Long temp=0L;
            for (HashMap m:
                 data) {
                x.add(m.get("RECORD_TIME"));
            }
            LinkedHashSet set=new LinkedHashSet();
            set.addAll(x);
            x.clear();
            x.addAll(set);
            for (int i = 0; i < x.size(); i++) {
                for (int j = 0; j < data.size(); j++) {
                    if (data.get(j).get("RECORD_TIME").toString().equals(x.get(i).toString())) {
                        y1.add(data.get(j).get("BUSI_COUNT"));
                        y2.add(data.get(j).get("AMOUNT"));
                        temp=temp+Long.parseLong(data.get(j).get("BUSI_COUNT").toString());
                    }
                }
                List a = new ArrayList();
                a.add(0,x.get(i).toString().substring(0,4)+"-"+x.get(i).toString().substring(4,6)+"-"+x.get(i).toString().substring(6,8));
                a.add(1,temp);
                chart2SeriesData.add(a);
                temp=0L;
                x.set(i,x.get(i).toString().substring(4,8));
            }
            HashMap returnMap = new HashMap();
            returnMap.put("x",x);
            returnMap.put("y1",y1);
            returnMap.put("y2",y2);
            returnMap.put("chart2SeriesData",chart2SeriesData);
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> siteDaily(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.siteDaily(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> zoneDaily(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.zoneDaily(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> channelDaily(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.channelDaily(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> allMonthly(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.allMonthly(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> branchMonthly(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.branchMonthly(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> zoneMonthly(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.zoneMonthly(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> siteMonthly(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.siteMonthly(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> channelMonthly(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.channelMonthly(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> channelMonthlyLine(Map<String, Object> params) {
        try {
            List xAxis = JSON.parseArray(params.get("time").toString(), String.class);
            List<HashMap<String, Object>> ajaxData = baseMapper.channelMonthly(params);
            List seriesData = new ArrayList();
            List padData = new ArrayList();//pad
            List phoneData = new ArrayList();//手机银行
            List weChatData = new ArrayList();//微信银行
            List machineData = new ArrayList();//自助设备
            List guimianData = new ArrayList();//柜面
            List netData = new ArrayList();//网上银行
            int index=0;
            //全部置0，匹配到月份下有数据再修改数值
            for(int i = 0;i<xAxis.size();i++){
                padData.add(0);//pad
                phoneData.add(0);//手机银行
                weChatData.add(0);//微信银行
                machineData.add(0);//自助设备
                guimianData.add(0);//柜面
                netData.add(0);//网上银行
            }
            for(int i = 0;i<xAxis.size();i++){
                for(int j = 0;j<ajaxData.size();j++){
                    if (ajaxData.get(j).get("MONTH").toString().equals(xAxis.get(i))) {
                        if(ajaxData.get(j).get("CHANNEL_NAME").toString().equals("网上银行")){
                            netData.set(i,ajaxData.get(j).get("BUSI_COUNT"));
                        }else if(ajaxData.get(j).get("CHANNEL_NAME").toString().equals("柜面")){
                            guimianData.set(i,ajaxData.get(j).get("BUSI_COUNT"));
                        }else if(ajaxData.get(j).get("CHANNEL_NAME").toString().equals("手机")){
                            phoneData.set(i,ajaxData.get(j).get("BUSI_COUNT"));
                        }else if(ajaxData.get(j).get("CHANNEL_NAME").toString().equals("微信银行")){
                            weChatData.set(i,ajaxData.get(j).get("BUSI_COUNT"));
                        }else if(ajaxData.get(j).get("CHANNEL_NAME").toString().equals("PAD")){
                            padData.set(i,ajaxData.get(j).get("BUSI_COUNT"));
                        }else if(ajaxData.get(j).get("CHANNEL_NAME").toString().equals("自助设备")){
                            machineData.set(i,ajaxData.get(j).get("BUSI_COUNT"));
                        }
                        index ++;
                    }
                }
            }
            HashMap returnMap = new HashMap();
            returnMap.put("netData",netData);
            returnMap.put("guimianData",guimianData);
            returnMap.put("phoneData",phoneData);
            returnMap.put("weChatData",weChatData);
            returnMap.put("padData",padData);
            returnMap.put("machineData",machineData);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> branchCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.branchCount(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> zoneCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.zoneCount(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> zoneCountTable(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> busiRTzone = baseMapper.zoneCount(params);
//            List<HashMap<String, Object>> mydata = new ArrayList<>();
            String str = "[\n" +
                    "                {name: '北京', value: 0, amount: 0, max: 0, city_no: '11'}, {\n" +
                    "                    name: '天津',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '12'\n" +
                    "                },\n" +
                    "                {name: '上海', value: 0, amount: 0, max: 0, city_no: '31'}, {\n" +
                    "                    name: '重庆',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '50'\n" +
                    "                },\n" +
                    "                {name: '河北', value: 0, amount: 0, max: 0, city_no: '13'}, {\n" +
                    "                    name: '河南',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '41'\n" +
                    "                },\n" +
                    "                {name: '云南', value: 0, amount: 0, max: 0, city_no: '53'}, {\n" +
                    "                    name: '辽宁',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '21'\n" +
                    "                },\n" +
                    "                {name: '黑龙江', value: 0, amount: 0, max: 0, city_no: '23'}, {\n" +
                    "                    name: '湖南',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '43'\n" +
                    "                },\n" +
                    "                {name: '安徽', value: 0, amount: 0, max: 0, city_no: '34'}, {\n" +
                    "                    name: '山东',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '37'\n" +
                    "                },\n" +
                    "                {name: '新疆', value: 0, amount: 0, max: 0, city_no: '65'}, {\n" +
                    "                    name: '江苏',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '32'\n" +
                    "                },\n" +
                    "                {name: '浙江', value: 0, amount: 0, max: 0, city_no: '33'}, {\n" +
                    "                    name: '江西',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '36'\n" +
                    "                },\n" +
                    "                {name: '湖北', value: 0, amount: 0, max: 0, city_no: '42'}, {\n" +
                    "                    name: '广西',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '45'\n" +
                    "                },\n" +
                    "                {name: '甘肃', value: 0, amount: 0, max: 0, city_no: '62'}, {\n" +
                    "                    name: '山西',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '14'\n" +
                    "                },\n" +
                    "                {name: '内蒙古', value: 0, amount: 0, max: 0, city_no: '15'}, {\n" +
                    "                    name: '陕西',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '61'\n" +
                    "                },\n" +
                    "                {name: '吉林', value: 0, amount: 0, max: 0, city_no: '22'}, {\n" +
                    "                    name: '福建',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '35'\n" +
                    "                },\n" +
                    "                {name: '贵州', value: 0, amount: 0, max: 0, city_no: '52'}, {\n" +
                    "                    name: '广东',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '44'\n" +
                    "                },\n" +
                    "                {name: '青海', value: 0, amount: 0, max: 0, city_no: '63'}, {\n" +
                    "                    name: '西藏',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '54'\n" +
                    "                },\n" +
                    "                {name: '四川', value: 0, amount: 0, max: 0, city_no: '51'}, {\n" +
                    "                    name: '宁夏',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '64'\n" +
                    "                },\n" +
                    "                {name: '海南', value: 0, amount: 0, max: 0, city_no: '46'}, {\n" +
                    "                    name: '台湾',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '103'\n" +
                    "                },\n" +
                    "                {name: '香港', value: 0, amount: 0, max: 0, city_no: '101'}, {\n" +
                    "                    name: '澳门',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '102'\n" +
                    "                }\n" +
                    "            ]";
//            List<ZoneCount> shenfen= JSON.parseArray(str,ZoneCount.class);
            List<HashMap> mydata = JSON.parseArray(str, HashMap.class);

            for (int i = 0; i < busiRTzone.size(); i++) {
                for (int j = 0; j < mydata.size(); j++) {

                    if (busiRTzone.get(i).get("ZONE_NAME").equals("香港") || busiRTzone.get(i).get("ZONE_NAME").equals("台湾") || busiRTzone.get(i).get("ZONE_NAME").equals("澳门")) {
                        if (busiRTzone.get(i).get("ZONE_NO").toString().substring(0, 3).equals(mydata.get(j).get("city_no"))) {
                            Integer value = Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString()) + Integer.parseInt(mydata.get(j).get("value").toString());
                            mydata.get(j).put("value",value);
                            Integer amount = Integer.parseInt(busiRTzone.get(i).get("AMOUNT").toString()) + Integer.parseInt(mydata.get(j).get("amount").toString());
                            mydata.get(j).put("amount",amount);
                        }
                    } else if (busiRTzone.get(i).get("ZONE_NO").toString().substring(0, 2).equals(mydata.get(j).get("city_no"))) {
                        Long value = Long.parseLong(busiRTzone.get(i).get("BUSI_COUNT").toString()) + Long.parseLong(mydata.get(j).get("value").toString());
                        mydata.get(j).put("value",value);
                        Long amount = Long.parseLong(busiRTzone.get(i).get("AMOUNT").toString()) + Long.parseLong(mydata.get(j).get("amount").toString());
                        mydata.get(j).put("amount",amount);
                    }
                }
            }
            for (int i = 0; i < mydata.size(); i++) {
                int max=i;
                for (int j = i + 1; j < mydata.size(); j++) {
                    if (Long.parseLong(mydata.get(j).get("amount").toString()) > Long.parseLong(mydata.get(i).get("amount").toString())) {
                        max=j;
                    }
                }
                if (i!=max){
                    HashMap<String,Object> temp=mydata.get(i);
                    mydata.set(i,mydata.get(max));
                    mydata.set(max,temp);
                }
            }
            List<HashMap> arr = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                arr.add(mydata.get(i));
            }
            HashMap map = new HashMap();
            map.put("arr",arr);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> zoneCountBVDTable(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> busiRTzone = baseMapper.zoneCount(params);
//            List<HashMap<String, Object>> mydata = new ArrayList<>();
            String str = "[{\n" +
                    "\t\t\t\t'name': '北京',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '11'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '天津',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '12'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '上海',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '31'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '重庆',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '50'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '河北',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '13'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '河南',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '41'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '云南',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '53'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '辽宁',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '21'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '黑龙江',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '23'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '湖南',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '43'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '安徽',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '34'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '山东',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '37'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '新疆',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '65'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '江苏',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '32'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '浙江',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '33'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '江西',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '36'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '湖北',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '42'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '广西',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '45'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '甘肃',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '62'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '山西',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '14'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '内蒙古',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '15'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '陕西',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '61'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '吉林',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '22'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '福建',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '35'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '贵州',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '52'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '广东',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '44'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '青海',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '63'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '西藏',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '54'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '四川',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '51'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '宁夏',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '64'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '海南',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '46'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '台湾',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '103'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '香港',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '101'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '澳门',\n" +
                    "\t\t\t\t'BUSI_COUNT': 0,\n" +
                    "\t\t\t\t'AMOUNT': 0,\n" +
                    "\t\t\t\t'CUS_COUNT': 0,\n" +
                    "\t\t\t\t'city_no': '102'\n" +
                    "\t\t\t}\n" +
                    "\t\t]";
            List<HashMap> mydata = JSON.parseArray(str, HashMap.class);

            for (int i = 0; i < busiRTzone.size(); i++) {
                for (int j = 0; j < mydata.size(); j++) {
                    if (busiRTzone.get(i).get("ZONE_NAME").equals("香港") || busiRTzone.get(i).get("ZONE_NAME").equals("台湾") || busiRTzone.get(i).get("ZONE_NAME").equals("澳门")) {
                        if (busiRTzone.get(i).get("ZONE_NO").toString().substring(0, 3).equals(mydata.get(j).get("city_no"))) {
                            Integer BUSI_COUNT = Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString()) + Integer.parseInt(mydata.get(j).get("BUSI_COUNT").toString());
                            mydata.get(j).put("BUSI_COUNT",BUSI_COUNT);
                            Integer amount = Integer.parseInt(busiRTzone.get(i).get("AMOUNT").toString()) + Integer.parseInt(mydata.get(j).get("AMOUNT").toString());
                            mydata.get(j).put("AMOUNT",amount);
//                            Integer CUS_COUNT = Integer.parseInt(busiRTzone.get(i).get("CUS_COUNT").toString()) + Integer.parseInt(mydata.get(j).get("CUS_COUNT").toString());
//                            mydata.get(j).put("CUS_COUNT",amount);
                        }
                    } else if (busiRTzone.get(i).get("ZONE_NO").toString().substring(0, 2).equals(mydata.get(j).get("city_no"))) {
                        Long BUSI_COUNT = Long.parseLong(busiRTzone.get(i).get("BUSI_COUNT").toString()) + Long.parseLong(mydata.get(j).get("BUSI_COUNT").toString());
                        mydata.get(j).put("BUSI_COUNT",BUSI_COUNT);
                        Long amount = Long.parseLong(busiRTzone.get(i).get("AMOUNT").toString()) + Long.parseLong(mydata.get(j).get("AMOUNT").toString());
                        mydata.get(j).put("AMOUNT",amount);
//                        Long CUS_COUNT = Long.parseLong(busiRTzone.get(i).get("CUS_COUNT").toString()) + Long.parseLong(mydata.get(j).get("CUS_COUNT").toString());
//                        mydata.get(j).put("CUS_COUNT",CUS_COUNT);

                    }
                }
            }
            for (int i = 0; i < mydata.size(); i++) {//按业务量排序排序
                for (int j = i + 1; j < mydata.size(); j++) {
                    int max=i;
                    if (Long.parseLong(mydata.get(j).get("BUSI_COUNT").toString()) > Long.parseLong(mydata.get(i).get("BUSI_COUNT").toString())) {
                        max=j;

                    }
                    if (i!=max){
                        HashMap<String,Object> temp=mydata.get(i);
                        mydata.set(i,mydata.get(max));
                        mydata.set(max,temp);
                    }
                }
            }
            List<HashMap> arr = new ArrayList<>();
            if (params.get("cutFlag")!=null && params.get("cutFlag").toString().equals("on")){
                for (int i = 0; i < 10; i++) {
                    arr.add(mydata.get(i));
                }
            }else {
                arr=mydata;
            }
            HashMap map = new HashMap();
            map.put("arr",arr);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<Map> zoneCountMap(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> busiRTzone = baseMapper.zoneCount(params);
//            List<HashMap<String, Object>> mydata = new ArrayList<>();
            String str = "[\n" +
                    "                {name: '北京', value: 0, amount: 0, max: 0, city_no: '11'}, {\n" +
                    "                    name: '天津',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '12'\n" +
                    "                },\n" +
                    "                {name: '上海', value: 0, amount: 0, max: 0, city_no: '31'}, {\n" +
                    "                    name: '重庆',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '50'\n" +
                    "                },\n" +
                    "                {name: '河北', value: 0, amount: 0, max: 0, city_no: '13'}, {\n" +
                    "                    name: '河南',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '41'\n" +
                    "                },\n" +
                    "                {name: '云南', value: 0, amount: 0, max: 0, city_no: '53'}, {\n" +
                    "                    name: '辽宁',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '21'\n" +
                    "                },\n" +
                    "                {name: '黑龙江', value: 0, amount: 0, max: 0, city_no: '23'}, {\n" +
                    "                    name: '湖南',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '43'\n" +
                    "                },\n" +
                    "                {name: '安徽', value: 0, amount: 0, max: 0, city_no: '34'}, {\n" +
                    "                    name: '山东',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '37'\n" +
                    "                },\n" +
                    "                {name: '新疆', value: 0, amount: 0, max: 0, city_no: '65'}, {\n" +
                    "                    name: '江苏',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '32'\n" +
                    "                },\n" +
                    "                {name: '浙江', value: 0, amount: 0, max: 0, city_no: '33'}, {\n" +
                    "                    name: '江西',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '36'\n" +
                    "                },\n" +
                    "                {name: '湖北', value: 0, amount: 0, max: 0, city_no: '42'}, {\n" +
                    "                    name: '广西',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '45'\n" +
                    "                },\n" +
                    "                {name: '甘肃', value: 0, amount: 0, max: 0, city_no: '62'}, {\n" +
                    "                    name: '山西',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '14'\n" +
                    "                },\n" +
                    "                {name: '内蒙古', value: 0, amount: 0, max: 0, city_no: '15'}, {\n" +
                    "                    name: '陕西',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '61'\n" +
                    "                },\n" +
                    "                {name: '吉林', value: 0, amount: 0, max: 0, city_no: '22'}, {\n" +
                    "                    name: '福建',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '35'\n" +
                    "                },\n" +
                    "                {name: '贵州', value: 0, amount: 0, max: 0, city_no: '52'}, {\n" +
                    "                    name: '广东',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '44'\n" +
                    "                },\n" +
                    "                {name: '青海', value: 0, amount: 0, max: 0, city_no: '63'}, {\n" +
                    "                    name: '西藏',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '54'\n" +
                    "                },\n" +
                    "                {name: '四川', value: 0, amount: 0, max: 0, city_no: '51'}, {\n" +
                    "                    name: '宁夏',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '64'\n" +
                    "                },\n" +
                    "                {name: '海南', value: 0, amount: 0, max: 0, city_no: '46'}, {\n" +
                    "                    name: '台湾',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '103'\n" +
                    "                },\n" +
                    "                {name: '香港', value: 0, amount: 0, max: 0, city_no: '101'}, {\n" +
                    "                    name: '澳门',\n" +
                    "                    value: 0,\n" +
                    "                    amount: 0,\n" +
                    "                    max: 0,\n" +
                    "                    city_no: '102'\n" +
                    "                }\n" +
                    "            ]";
//            List<ZoneCount> shenfen= JSON.parseArray(str,ZoneCount.class);
            List<HashMap> mydata = JSON.parseArray(str, HashMap.class);

            for (int i = 0; i < busiRTzone.size(); i++) {
                for (int j = 0; j < mydata.size(); j++) {//香港，澳门，台湾的编号为3位数所以要截取3位对比
                    if (busiRTzone.get(i).get("ZONE_NAME").equals("香港") || busiRTzone.get(i).get("ZONE_NAME").equals("台湾") || busiRTzone.get(i).get("ZONE_NAME").equals("澳门")) {
                        if (busiRTzone.get(i).get("ZONE_NO").toString().substring(0, 3).equals(mydata.get(j).get("city_no"))) {
                            Integer value = Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString()) + Integer.parseInt(mydata.get(j).get("value").toString());
                            mydata.get(j).put("value",value);
                            if (Integer.parseInt(mydata.get(j).get("max").toString())<Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString())){
                                mydata.get(j).put("max",Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString()));
                            }
                        }
                    } else if (busiRTzone.get(i).get("ZONE_NO").toString().substring(0, 2).equals(mydata.get(j).get("city_no"))) {
                        Long value = Long.parseLong(busiRTzone.get(i).get("BUSI_COUNT").toString()) + Long.parseLong(mydata.get(j).get("value").toString());
                        mydata.get(j).put("value",value);
                        if (Integer.parseInt(mydata.get(j).get("max").toString())<Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString())){
                            mydata.get(j).put("max",Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString()));
                        }
                    }
                }
            }
//            for (int i = 0; i < mydata.size(); i++) {//此处数据用于地图所以不用排序
//                for (int j = i + 1; j < mydata.size() - i; j++) {
//                    if (Long.parseLong(mydata.get(j).get("amount").toString()) > Long.parseLong(mydata.get(i).get("amount").toString())) {
//                        HashMap<String,Object> temp=mydata.get(i);
//                        mydata.set(i,mydata.get(j));
//                        mydata.set(j,temp);
//                    }
//                }
//            }
            Double max1 = 0.00;
            for (int i = 0; i < mydata.size(); i++) {
                if (max1 < Double.parseDouble(mydata.get(i).get("value").toString())) {
                    max1 = Double.parseDouble(mydata.get(i).get("value").toString());
                }
            }
            max1 = max1 - Math.ceil(max1 % 100) + 100;
//            List<HashMap> arr = new ArrayList<>(); //截取前十条数据
//            for (int i = 0; i < 10; i++) {
//                arr.add(mydata.get(i));
//            }
            HashMap map = new HashMap();
            map.put("max1",max1);
            map.put("mydata", mydata);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<Map> zoneCountBVDMap(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> busiRTzone = baseMapper.zoneCount(params);
//            List<HashMap<String, Object>> mydata = new ArrayList<>();
            String str = "[{\n" +
                    "\t\t\t\t'name': '北京',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '11'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '天津',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '12'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '上海',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '31'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '重庆',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '50'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '河北',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '13'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '河南',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '41'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '云南',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '53'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '辽宁',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '21'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '黑龙江',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '23'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '湖南',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '43'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '安徽',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '34'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '山东',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '37'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '新疆',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '65'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '江苏',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '32'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '浙江',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '33'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '江西',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '36'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '湖北',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '42'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '广西',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '45'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '甘肃',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '62'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '山西',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '14'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '内蒙古',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '15'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '陕西',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '61'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '吉林',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '22'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '福建',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '35'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '贵州',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '52'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '广东',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '44'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '青海',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '63'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '西藏',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '54'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '四川',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '51'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '宁夏',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '64'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '海南',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '46'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '台湾',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '103'\n" +
                    "\t\t\t},\n" +
                    "\t\t\t{\n" +
                    "\t\t\t\t'name': '香港',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '101'\n" +
                    "\t\t\t}, {\n" +
                    "\t\t\t\t'name': '澳门',\n" +
                    "\t\t\t\t'value': 0,\n" +
                    "\t\t\t\t'city_no': '102'\n" +
                    "\t\t\t}\n" +
                    "\t\t]";
//            List<ZoneCount> shenfen= JSON.parseArray(str,ZoneCount.class);
            List<HashMap> mydata = JSON.parseArray(str, HashMap.class);

            for (int i = 0; i < busiRTzone.size(); i++) {
                for (int j = 0; j < mydata.size(); j++) {//香港，澳门，台湾的编号为3位数所以要截取3位对比
                    if (busiRTzone.get(i).get("ZONE_NAME").equals("香港") || busiRTzone.get(i).get("ZONE_NAME").equals("台湾") || busiRTzone.get(i).get("ZONE_NAME").equals("澳门")) {
                        if (busiRTzone.get(i).get("ZONE_NO").toString().substring(0, 3).equals(mydata.get(j).get("city_no"))) {
                            Integer value = Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString()) + Integer.parseInt(mydata.get(j).get("value").toString());
                            mydata.get(j).put("value",value);
//                            if (Integer.parseInt(mydata.get(j).get("max").toString())<Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString())){ 填入最大值
//                                mydata.get(j).put("max",Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString()));
//                            }
                        }
                    } else if (busiRTzone.get(i).get("ZONE_NO").toString().substring(0, 2).equals(mydata.get(j).get("city_no"))) {
                        Long value = Long.parseLong(busiRTzone.get(i).get("BUSI_COUNT").toString()) + Long.parseLong(mydata.get(j).get("value").toString());
                        mydata.get(j).put("value",value);
//                        if (Integer.parseInt(mydata.get(j).get("max").toString())<Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString())){
//                            mydata.get(j).put("max",Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString()));
//                        }
                    }
                }
            }
            Double max1 = 0.00;
            for (int i = 0; i < mydata.size(); i++) {
                if (max1 < Double.parseDouble(mydata.get(i).get("value").toString())) {
                    max1 = Double.parseDouble(mydata.get(i).get("value").toString());
                }
            }
            max1 = max1 - Math.ceil(max1 % 100) + 100;
//            List<HashMap> arr = new ArrayList<>(); //截取前十条数据
//            for (int i = 0; i < 10; i++) {
//                arr.add(mydata.get(i));
//            }
            HashMap map = new HashMap();
            map.put("Max",max1);
            map.put("MyData", mydata);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> siteCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.siteCount(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> channelCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.channelCount(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> channelCountPie(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> data=baseMapper.channelCount(params);
            List<HashMap<String,Object>> seriesDatas1=new ArrayList<>();
            List BQ = new ArrayList();
            Long busi_count_all1=0L;
            for (HashMap item:
                 data) {
                HashMap temp = new HashMap();
                temp.put("name", item.get("CHANNEL_NAME") + ":" + item.get("BUSI_COUNT"));
                temp.put("value", item.get("BUSI_COUNT"));
                seriesDatas1.add(temp);
                busi_count_all1+=Long.parseLong(item.get("BUSI_COUNT").toString());
                BQ.add(item.get("CHANNEL_NAME") + ":" + item.get("BUSI_COUNT"));
            }
            HashMap returnMap = new HashMap();
            returnMap.put("seriesDatas1", seriesDatas1);
            returnMap.put("busi_count_all1", busi_count_all1);
            returnMap.put("BQ", BQ);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<Map> channelCountElcPie(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> data=baseMapper.channelCount(params);
            List<HashMap<String,Object>> seriesDatas1=new ArrayList<>();
//            Long busi_count_all1=0L;
            for (HashMap item:
                    data) {
                if (!item.get("CHANNEL_NO").toString().equals("CH000")){
                    HashMap temp = new HashMap();
                    temp.put("name", item.get("CHANNEL_NAME") + ":" + item.get("BUSI_COUNT"));
                    temp.put("value", item.get("BUSI_COUNT"));
                    seriesDatas1.add(temp);
//                    busi_count_all1+=Long.parseLong(item.get("BUSI_COUNT").toString());
                }

            }
            HashMap returnMap = new HashMap();
            returnMap.put("seriesDatas1", seriesDatas1);
//            returnMap.put("busi_count_all1", busi_count_all1);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<Map> channelCountElcBar(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> data=baseMapper.channelCount(params);
             List xAxis=new ArrayList();
            List rankData=new ArrayList();
//            Long busi_count_all1=0L;
            for (int i=data.size()-1;i>=0;i--) {
                if (!data.get(i).get("CHANNEL_NO").toString().equals("CH000")){
                    xAxis.add(data.get(i).get("CHANNEL_NAME"));
                    rankData.add(data.get(i).get("BUSI_COUNT"));
                }

            }
            HashMap returnMap = new HashMap();
            returnMap.put("xAxis", xAxis);
            returnMap.put("rankData", rankData);
//            returnMap.put("busi_count_all1", busi_count_all1);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> branchRank(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.branchRank(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> operateCost(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.operateCost(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

}
