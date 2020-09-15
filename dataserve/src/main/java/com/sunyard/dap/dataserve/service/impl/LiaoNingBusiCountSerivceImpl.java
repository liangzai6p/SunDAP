package com.sunyard.dap.dataserve.service.impl;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.mapper.LiaoNingBusiCountMapper;
import com.sunyard.dap.dataserve.service.LiaoNingBusiCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("LiaoNingBusiCountService")
@Slf4j
public class LiaoNingBusiCountSerivceImpl implements LiaoNingBusiCountService {

    @Resource
    private LiaoNingBusiCountMapper liaoNingBusiCountMapper;

    /**
     * 获取机构业务量数据
     */
    @Override
    public ReturnT<Map> selectBusiNum(Map<String,Object> params){
        try {
            String date = (String) params.get("date");

            List<Map<String, Object>> list = liaoNingBusiCountMapper.select(date);


            List<String> xAxis = new ArrayList<String>();
            List<Object> yAxis = new ArrayList<Object>();

            for (Map<String, Object> map : list) {
                xAxis.add((String) map.get("ORGNAME"));
                yAxis.add(map.get("BUSINUM"));
            }

            Map<String , Object> retList = new HashMap<String , Object>();
                retList.put("yAxis" , yAxis);
                retList.put("xAxis" , xAxis);

            return new ReturnT<Map>(ReturnT.SUCCESS_CODE,"查询成功", retList);
        }catch (Exception e){
            System.out.print(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    /**
     * 获取地图中地区（机构）日业务量
     */
    @Override
    public ReturnT<List> selectOrganBusi(Map<String,Object> params){
        
        try{
            String date = (String)params.get("date");

            String[] organNo = {"2232","2241","2280","2291","2292","2330"};
            String organStr = "'2232','2241','2280','2291','2292','2330'";
            HashMap<String , Object> conMap = new HashMap<String , Object>();
                conMap.put("organNo", organStr);
                conMap.put("date", date);
    
            List<HashMap<String , Object>> list = liaoNingBusiCountMapper.selectOrganBusi(conMap);

                List<Map<String , Object>> organMap = new ArrayList<Map<String , Object>>();
    
                for(Map<String , Object> map:list){
                    Map<String , Object> organBusiMap = new HashMap<String , Object>();
                    //将Map数据格式造成前台所需  {'name' : '','value' : ''}格式
                    organBusiMap.put("name" , map.get("BRNRGNCOD"));
                    organBusiMap.put("value", map.get("busiCount"));
                    organMap.add(organBusiMap);
                }
            return new ReturnT<List>(ReturnT.SUCCESS_CODE,"查询成功", organMap);
        }catch (Exception e){
            System.out.print(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    /**
     * 获取当日每小时业务量
     */
    @Override
    public ReturnT<Map> getHourBusiCount(Map<String,Object> params){
        try{

            String date = (String)params.get("date");

            List<Map<String, Object>> list = liaoNingBusiCountMapper.getHourBusiCount(date);

            String[] hourStr = {"09","10","11","12","13","14","15","16","17","18"};

            List<String> xAxis = new ArrayList<String>();
            List<Object> yAxis = new ArrayList<Object>();
            //sql查询出的时间没有业务量的，补充0
            for(int i = 0;i<hourStr.length;i++){
                xAxis.add(hourStr[i]);
                Map<String , Object> record = new HashMap<String , Object>();
                //循环判断是否有这个时间点的数据，有则取数，无则补0
                for(Map<String , Object> map : list){
                    if(!map.get("hour").equals(hourStr[i])){
                        record.put(hourStr[i], 0);
                    }else{
                        record.put(hourStr[i], map.get("busiCount"));
                        break;
                    }
                }
                yAxis.add(record.get(hourStr[i]));
            }

            Map<String , Object> retMap = new HashMap<>();
                retMap.put("xAxis", xAxis);
                retMap.put("yAxis", yAxis);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE,"查询成功", retMap);
        }catch (Exception e){
            System.out.print(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }
    /**
     * 获取分状态业务量
     */
    @Override
    public ReturnT<Map> getTransStateCount(Map<String,Object> params){
        try{
            String date = (String)params.get("date");

            List<Map<String, Object>> list = liaoNingBusiCountMapper.getTransStateCount(date);
            Map<String, Object> busiCount = liaoNingBusiCountMapper.getDayBusiCount(date);

            int[] stateStr = {6,5,9};

            List<String> xAxis = new ArrayList<String>();
            xAxis.add("总业务量");
            List<Object> yAxis = new ArrayList<Object>();
            yAxis.add(busiCount.get("busiCount"));
            //sql查询出的状态没有业务量的，补充0
            for(int i = 0;i<stateStr.length;i++){
                if(stateStr[i] ==9){
                    xAxis.add("待处理");
                }else if(stateStr[i] == 6){
                    xAxis.add("通过");
                }else if(stateStr[i]== 5){
                    xAxis.add("拒绝");
                }

                Map<String , Object> record = new HashMap<String , Object>();
                for(Map<String , Object> map : list){
                    String str = ((BigDecimal) map.get("state")).toString();
                    if(!str.equals(new Integer(stateStr[i]).toString())){
                        record.put(new Integer(stateStr[i]).toString(), 0);
                    }else{
                        record.put(new Integer(stateStr[i]).toString(), map.get("busiCount"));
                        break;
                    }
                }
                yAxis.add(record.get(new Integer(stateStr[i]).toString()));
            }

            Map<String , Object> retMap = new HashMap<>();
                retMap.put("xAxis", xAxis);
                retMap.put("yAxis", yAxis);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE,"查询成功", retMap);
        }catch (Exception e){
            System.out.print(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    /**
     * 获取当日每小时业务量
     */
    @Override
    public ReturnT<Map> getHourAvgTime(Map<String,Object> params){
        try{
            String date = (String)params.get("date");

            //获取当前时间
            SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
            String currTime = sdf.format(new Date());
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.HOUR , -1);
            //创建时间比对数组，当前时间到之前一小时每10分钟一个点，一共7个点，用于与各时段走势数据进行时间比对
            String[] timeSlice = new String[7];

            for(int i = 0;i<timeSlice.length;i++){
                timeSlice[i] = sdf.format(cal.getTime());
                cal.add(Calendar.MINUTE, 10);
            }

            Map<String , String> conMap = new HashMap<String , String>();
            conMap.put("date", date);
            conMap.put("startTime", timeSlice[0].substring(0,3));
            conMap.put("endTime", timeSlice[timeSlice.length - 1].substring(0, 3));
            List<Map<String, Object>> list = liaoNingBusiCountMapper.getHourAvgTime(conMap);

            if(list.size() == 0){//记录条数为0时证明没有数据，给数组全部添加0
                List<String> xAxis = new ArrayList<String>();
                List<Object> yAxis = new ArrayList<Object>();
                for(int i = 0;i<timeSlice.length;i++){
                    String time = timeSlice[i].substring(0, 2) + ":" + timeSlice[i].substring(2 ,3) + "0";//分钟只取十位数，分钟补0，以整点10分钟为间隔
                    xAxis.add(time);
                    yAxis.add(0);
                }

                Map retMap = new HashMap();
                    retMap.put("xAxis", xAxis);
                    retMap.put("yAxis", yAxis);
                return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
            }else {
                List<String> xAxis = new ArrayList<String>();
                List<Object> yAxis = new ArrayList<Object>();
                //sql查询出的时间没有业务量的，补充0
                for (int i = 0; i < timeSlice.length; i++) {
                    String time = timeSlice[i].substring(0, 2) + ":" + timeSlice[i].substring(2, 3) + "0";//分钟只取十位数，分钟补0，以整点10分钟为间隔
                    xAxis.add(time);
                    Map<String, Object> record = new HashMap<String, Object>();
                    //循环判断是否有这个时间点的数据，有则取数，无则补0
                    for (Map<String, Object> map : list) {
                        if (!map.get("time").equals(timeSlice[i].substring(0, 3))) {
                            record.put(timeSlice[i], 0);
                        } else {
                            record.put(timeSlice[i], map.get("avg"));
                            break;
                        }
                    }

                    yAxis.add(record.get(timeSlice[i]));
                }

                Map<String , Object> retMap = new HashMap<>();
                    retMap.put("xAxis", xAxis);
                    retMap.put("yAxis", yAxis);
                return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
            }
        }catch (Exception e){
            System.out.print(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    /**
     * 获取柜员处理业务数
     */
    @Override
    public ReturnT<List> getTellerBusiCount(Map<String,Object> params){
        try{
            String date = (String)params.get("date");

            List<Map<String, Object>> list = liaoNingBusiCountMapper.getTellerBusiCount(date);

            List<Map<String , Object>> retList = new ArrayList<Map<String , Object>>();

            for(int i = 0;i<list.size();i ++){
                Map<String , Object> record = new HashMap<String , Object>();
                Map<String , Object> innerMap = list.get(i);
                Integer count = new Integer(0);
                if(innerMap.get("STATE_6") != null&& innerMap.get("STATE_5") != null){
                    count = ((BigDecimal)innerMap.get("STATE_5")).intValue() + ((BigDecimal)(innerMap.get("STATE_6"))).intValue();
                    record.put("finished", innerMap.get("STATE_5"));
                    record.put("refuse", innerMap.get("STATE_6"));
                }else if(innerMap.get("STATE_6") == null){
                    count = ((BigDecimal)innerMap.get("STATE_5")).intValue() + 0;
                    record.put("finished", innerMap.get("STATE_5"));
                    record.put("refuse", 0);
                }else if(innerMap.get("STATE_5") == null){
                    count = 0 + ((BigDecimal)(innerMap.get("STATE_6"))).intValue();
                    record.put("finished", 0);
                    record.put("refuse", innerMap.get("STATE_6"));
                }
                record.put("userNo", innerMap.get("userNo"));
                record.put("count", count.toString());
                retList.add(record);
            }

            return new ReturnT<List>(ReturnT.SUCCESS_CODE, "查询成功", retList);
        }catch (Exception e){
            System.out.print(e.getMessage());
            return ReturnT.listFAIL;
        }
    }
    
}
