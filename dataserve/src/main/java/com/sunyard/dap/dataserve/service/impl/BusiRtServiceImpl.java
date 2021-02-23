package com.sunyard.dap.dataserve.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.common.util.CommonUtil;
import com.sunyard.dap.dataserve.entity.BusiRtDO;
import com.sunyard.dap.dataserve.mapper.BusiRtMapper;
import com.sunyard.dap.dataserve.service.BusiRtService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
 * @since 2020-07-03
 */
@Service
@Slf4j
public class BusiRtServiceImpl extends ServiceImpl<BusiRtMapper, BusiRtDO> implements BusiRtService {

    @Override
    public ReturnT<List> listHRT(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listHRT(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listBranchHRT(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listBranchHRT(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listZoneHRT(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listZoneHRT(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listSiteHRT(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listSiteHRT(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listChannelHRT(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listChannelHRT(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listChannelHRTLine(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> data=baseMapper.listChannelHRT(params);
            List record_time = new ArrayList();
            List AMOUNT = new ArrayList();
            List CUS_COUNT = new ArrayList();
            List BUSI_COUNT = new ArrayList();
//            List busi_cpint = new ArrayList();
            for (int i = 0; i < data.size(); i++) {
                record_time.add(data.get(i).get("RECORD_TIME"));
                AMOUNT.add(data.get(i).get("AMOUNT"));
                CUS_COUNT.add(data.get(i).get("CUS_COUNT"));
                BUSI_COUNT.add(data.get(i).get("BUSI_COUNT"));
            }
            HashMap returnMap = new HashMap();
            returnMap.put("record_time",record_time);
            returnMap.put("AMOUNT",AMOUNT);
            returnMap.put("CUS_COUNT",CUS_COUNT);
            returnMap.put("BUSI_COUNT",BUSI_COUNT);
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listDRT(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listDRT(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listBranchDRT(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listBranchDRT(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listBranchDRTBar(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> data = baseMapper.listBranchDRT(params);
            List yAxis = new ArrayList();
            List xAxis = new ArrayList();
            for (int i = 0; i < data.size() - 1; i++) {
                int max = i;
                for (int j = i+1; j < data.size(); j++) {//对筛选出的数据进行选择排序 从大到小
                    if (Integer.valueOf(data.get(max).get("BUSI_COUNT").toString()) < Integer.valueOf(data.get(j).get("BUSI_COUNT").toString())) {
                        max = j;
                    }
                }
                if (max != i) {
                    HashMap temp = new HashMap();
                    temp = data.get(i);
                    data.set(i, data.get(max));
                    data.set(max, temp);
                }
            }
            for (int i = 9; i >= 0; i--) {
                yAxis.add(data.get(i).get("BUSI_COUNT"));
                xAxis.add(data.get(i).get("BRANCH_NAME"));
            }
            HashMap returnMap = new HashMap();
            returnMap.put("yAxis", yAxis);
            returnMap.put("xAxis", xAxis);
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listZoneDRT(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listZoneDRT(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listZoneDRTMap(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> busiRTzone = baseMapper.listZoneDRT(params);
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
                            mydata.get(j).put("value", value);
//                            if (Integer.parseInt(mydata.get(j).get("max").toString())<Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString())){ 填入最大值
//                                mydata.get(j).put("max",Integer.parseInt(busiRTzone.get(i).get("BUSI_COUNT").toString()));
//                            }
                        }
                    } else if (busiRTzone.get(i).get("ZONE_NO").toString().substring(0, 2).equals(mydata.get(j).get("city_no"))) {
                        Long value = Long.parseLong(busiRTzone.get(i).get("BUSI_COUNT").toString()) + Long.parseLong(mydata.get(j).get("value").toString());
                        mydata.get(j).put("value", value);
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
            map.put("Max", max1);
            map.put("MyData", mydata);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<Map> listZoneDRTTable(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> busiRTzone = baseMapper.listZoneDRT(params);
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
                            mydata.get(j).put("BUSI_COUNT", BUSI_COUNT);
                            Integer amount = Integer.parseInt(busiRTzone.get(i).get("AMOUNT").toString()) + Integer.parseInt(mydata.get(j).get("AMOUNT").toString());
                            mydata.get(j).put("AMOUNT", amount);
                            Integer CUS_COUNT = Integer.parseInt(busiRTzone.get(i).get("CUS_COUNT").toString()) + Integer.parseInt(mydata.get(j).get("CUS_COUNT").toString());
                            mydata.get(j).put("CUS_COUNT", amount);
                        }
                    } else if (busiRTzone.get(i).get("ZONE_NO").toString().substring(0, 2).equals(mydata.get(j).get("city_no"))) {
                        Long BUSI_COUNT = Long.parseLong(busiRTzone.get(i).get("BUSI_COUNT").toString()) + Long.parseLong(mydata.get(j).get("BUSI_COUNT").toString());
                        mydata.get(j).put("BUSI_COUNT", BUSI_COUNT);
                        Long amount = Long.parseLong(busiRTzone.get(i).get("AMOUNT").toString()) + Long.parseLong(mydata.get(j).get("AMOUNT").toString());
                        mydata.get(j).put("AMOUNT", amount);
                        Long CUS_COUNT = Long.parseLong(busiRTzone.get(i).get("CUS_COUNT").toString()) + Long.parseLong(mydata.get(j).get("CUS_COUNT").toString());
                        mydata.get(j).put("CUS_COUNT", CUS_COUNT);

                    }
                }
            }
            for (int i = 0; i < mydata.size(); i++) {//按业务量排序排序
                for (int j = i + 1; j < mydata.size(); j++) {
                    int max = i;
                    if (Long.parseLong(mydata.get(j).get("BUSI_COUNT").toString()) > Long.parseLong(mydata.get(i).get("BUSI_COUNT").toString())) {
                        max = j;

                    }
                    if (i != max) {
                        HashMap<String, Object> temp = mydata.get(i);
                        mydata.set(i, mydata.get(max));
                        mydata.set(max, temp);
                    }
                }
            }
            List<HashMap> arr = new ArrayList<>();
            if (params.get("cutFlag") != null && params.get("cutFlag").toString().equals("on")) {
                for (int i = 0; i < 10; i++) {
                    arr.add(mydata.get(i));
                }
            } else {
                arr = mydata;
            }
            HashMap map = new HashMap();
            map.put("arr", arr);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listSiteDRT(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listSiteDRT(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listChannelDRT(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listChannelDRT(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listChannelDRTPie(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> data = baseMapper.listChannelDRT(params);
            List<HashMap<String, Object>> seriesDatas1 = new ArrayList<>();
            Long busi_count_all1 = 0L;
            for (HashMap item :
                    data) {
                HashMap temp = new HashMap();
                temp.put("name", item.get("CHANNEL_NAME") + ":" + item.get("BUSI_COUNT"));
                temp.put("value", item.get("BUSI_COUNT"));
                seriesDatas1.add(temp);
                busi_count_all1 += Long.parseLong(item.get("BUSI_COUNT").toString());
            }
            HashMap returnMap = new HashMap();
            returnMap.put("seriesDatas1", seriesDatas1);
            returnMap.put("busi_count_all1", busi_count_all1);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<Page<HashMap<String, Object>>> listByState(Map<String, Object> params) {
        Page<HashMap> page = new Page<>();
        CommonUtil.setPageByParams(page, params);
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listByState(page, params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE, "查询失败", null);
        }
    }

    @Override
    public ReturnT<List> countState(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.countState(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }
}
