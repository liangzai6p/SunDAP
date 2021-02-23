package com.sunyard.dap.dataserve.service.impl;

import com.alibaba.fastjson.JSON;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.ChannelGradeDO;
import com.sunyard.dap.dataserve.mapper.ChannelMapper;
import com.sunyard.dap.dataserve.service.ChannelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yey.he
 * @since 2020-08-28
 */
@Service
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, ChannelGradeDO> implements ChannelService {

    @Override
    public ReturnT<List> findEleAllRplRate(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.findEleAllRplRate(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listEleRplRateByBranch(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listEleRplRateByBranch(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listEleRplRateByBranchMap(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> busiRTzone = baseMapper.listEleRplRateByBranch(params);
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
                for (int j = 0; j < mydata.size(); j++) {//香港，澳门，台湾的编号为3位数所以要截取3位对比，此处需要根据数据库中的BRANCH_NAME进行灵活修改
                    if (busiRTzone.get(i).get("BRANCH_NAME").equals("香港分行") || busiRTzone.get(i).get("BRANCH_NAME").equals("台湾分行") || busiRTzone.get(i).get("BRANCH_NAME").equals("澳门分行")) {
                        if (busiRTzone.get(i).get("BRANCH_NO").toString().substring(0, 3).equals(mydata.get(j).get("city_no"))) {
                            Integer value = Integer.parseInt(busiRTzone.get(i).get("ELE_COUNT").toString()) + Integer.parseInt(mydata.get(j).get("value").toString());
                            mydata.get(j).put("value",value);
//                            if (Integer.parseInt(mydata.get(j).get("max").toString())<Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString())){
//                                mydata.get(j).put("max",Integer.parseInt(busiRTzone.get(i).get("ELE_COUNT").toString()));//计算mydata.max
//                            }
                        }
                    } else if (busiRTzone.get(i).get("BRANCH_NO").toString().substring(0, 2).equals(mydata.get(j).get("city_no"))) {
                        Long value = Long.parseLong(busiRTzone.get(i).get("ELE_COUNT").toString()) + Long.parseLong(mydata.get(j).get("value").toString());
                        mydata.get(j).put("value",value);
//                        if (Integer.parseInt(mydata.get(j).get("max").toString())<Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString())){
//                            mydata.get(j).put("max",Integer.parseInt(busiRTzone.get(i).get("ELE_COUNT").toString()));//计算mydata.max
//                        }
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
            map.put("max",max1);
            map.put("mydata", mydata);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", map);
        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(e);
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listEleRplRateByBranchTable(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> channelListEleRplRateByChannel = baseMapper.listEleRplRateByBranch(params);
            List<HashMap<String, Object>> data = new ArrayList<>();
            for (HashMap m:
                    channelListEleRplRateByChannel) {
                m.put("ELE_REPLACE_RATE", m.get("ELE_REPLACE_RATE") + "%");
            }
            for (int i=0;i<10;i++){
                data.add(channelListEleRplRateByChannel.get(i));
            }
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",data);
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listEleAllRplRateDaily(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listEleAllRplRateDaily(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listEleAllRplRateDailyLineChat(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> channelListEleAllRplRateDaily = baseMapper.listEleAllRplRateDaily(params);
            List x = new ArrayList();
            List y = new ArrayList();
            List doubleX = new ArrayList();
            int i=0;
            for (HashMap m:
                 channelListEleAllRplRateDaily) {
                doubleX.add(i);
                i++;
                x.add(m.get("RECORD_TIME"));
                y.add(m.get("ELE_REPLACE_RATE"));
            }
            HashMap returnMap = new HashMap();
            returnMap.put("x", x);
            returnMap.put("y", y);
            returnMap.put("doubleX", doubleX);
            returnMap.put("fitLineDataLength", i + 1);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE,"查询成功",returnMap);
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listEleAllRplRateMonthly(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listEleAllRplRateMonthly(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listEleAllRplRateMonthlyLine(Map<String, Object> params) {
        try {
            List xAxis = JSON.parseArray(params.get("time").toString(), String.class);
            List<HashMap<String, Object>> ajaxData = baseMapper.listEleAllRplRateMonthly(params);
            Integer index = 0;//ajaxData循环指数
            List seriesData = new ArrayList();
            for(int i = 0;i<xAxis.size();i++){
                if(index < ajaxData.size()) {
                    if (ajaxData.get(index).get("MONTH").toString().equals(xAxis.get(i).toString())) {
                        seriesData.add(ajaxData.get(index).get("ELE_REPLACE_RATE"));
                        index ++;
                    }else{
                        seriesData.add(0);
                    }
                }else{
                    seriesData.add(0);
                }
            }
            HashMap returnMap = new HashMap();
            returnMap.put("seriesData",seriesData);
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",returnMap);
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listEleGradeByChannel(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listEleGradeByChannel(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listEleGradeByChannelRadar(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> data=baseMapper.listEleGradeByChannel(params);
            List<HashMap<String,Object>> raderData=new ArrayList<>();
            for (HashMap m:
                 data) {
                HashMap temp = new HashMap();
                temp.put("name",m.get("CHANNEL_NAME"));
                List value = new ArrayList();
                value.add(m.get("COVER_RATE"));
                value.add(m.get("BUSI_SCORE"));
                value.add(m.get("SATIS_SCORE"));
                value.add(m.get("QUALITY_SCORE"));
                value.add(m.get("SUCCESS_RATE"));
                temp.put("value", value);
                HashMap areaStyle=new HashMap();
                areaStyle.put("opacity",0.2);
                temp.put("areaStyle",areaStyle);
                raderData.add(temp);
            }
            HashMap returnMap=new HashMap();
            returnMap.put("raderData",raderData);
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",returnMap);
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listEleSatisByChannel(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listEleSatisByChannel(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listEleSatisByChannelBar(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> data=baseMapper.listEleSatisByChannel(params);
            List xAxis=new ArrayList();
            List rankData=new ArrayList();
//            Long busi_count_all1=0L;
            for (int i=data.size()-1;i>=0;i--) {
                if (!data.get(i).get("CHANNEL_NO").toString().equals("CH000")){
                    xAxis.add(data.get(i).get("CHANNEL_NAME"));
                    rankData.add(data.get(i).get("SATIS_SCORE"));
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
    public ReturnT<List> listEleRplRateByChannel(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listEleRplRateByChannel(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listEleRplRateByChannelBar(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> data=baseMapper.listEleRplRateByChannel(params);
            List xAxis=new ArrayList();
            List rateData=new ArrayList();
//            Long busi_count_all1=0L;
            for (int i=data.size()-1;i>=0;i--) {
                if (!data.get(i).get("CHANNEL_NO").toString().equals("CH000")){
                    xAxis.add(data.get(i).get("CHANNEL_NAME"));
                    rateData.add(data.get(i).get("REPLACE_RATE"));
                }

            }
            HashMap returnMap = new HashMap();
            returnMap.put("xAxis", xAxis);
            returnMap.put("rateData", rateData);
//            returnMap.put("busi_count_all1", busi_count_all1);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listCusCountByChannel(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listCusCountByChannel(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listCusCountByChannelBar(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> data = baseMapper.listCusCountByChannel(params);
            List xAxis = new ArrayList();
            List avgData = new ArrayList();
            List countData = new ArrayList();
            for (HashMap m:
                 data) {
                xAxis.add(m.get("CHANNEL_NAME"));
                avgData.add(m.get("AVG_COUNT"));
                countData.add(m.get("COUNT"));
            }
            HashMap returnMap = new HashMap();
            returnMap.put("xAxis", xAxis);
            returnMap.put("avgData", avgData);
            returnMap.put("countData", countData);
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",returnMap);
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }
}
