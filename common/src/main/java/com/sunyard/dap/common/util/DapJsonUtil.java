package com.sunyard.dap.common.util;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * @program: SunDAP
 * @description: SunDAP-JSON工具类
 * @author: yey.he
 * @create: 2020-09-11 14:07
 **/
public class DapJsonUtil {
    public static double[] getJsonDoubleArray(String json,String key) throws Exception{
        JSONObject paramsObj = JSONUtil.parseObj(json);
        JSONArray rsJson = JSONUtil.parseArray(paramsObj.getStr(key));
        double[] rsArray = new double[rsJson.size()];
        for (int i = 0 ; i < rsJson.size() ; i++){
            rsArray[i] = Double.parseDouble(rsJson.getStr(i));
        }
        return rsArray;
    }

    public static String[] getJsonStrArray(String json,String key) throws Exception{
        JSONObject paramsObj = JSONUtil.parseObj(json);
        JSONArray rsJson = JSONUtil.parseArray(paramsObj.getStr(key));
        String[] rsArray = new String[rsJson.size()];
        for (int i = 0 ; i < rsJson.size() ; i++){
            rsArray[i] = rsJson.getStr(i);
        }
        return rsArray;
    }

    /**提取JSON中的整数数值
     * @author: xiao.xie
     * @param json
     * @param key
     * @return
     * @throws Exception
     */
    public static Integer getJsonInteger(String json,String key) throws Exception{
        JSONObject paramsObj = JSONUtil.parseObj(json);
//        Integer test=Integer.parseInt(paramsObj.getStr(key));
        return Integer.parseInt(paramsObj.getStr(key));
    }

    public static String parseObjToJsonStr(Object obj,String key){
        JSONObject rsObj = JSONUtil.createObj();
        rsObj.put(key,obj);
        return rsObj.toString();
    }

    public static String parseArrayToJsonStr(Object array,String key){
        JSONArray rsArray = JSONUtil.parseArray(array);
        JSONObject rsObj = JSONUtil.createObj();
        rsObj.put(key,rsArray);
        return rsObj.toString();
    }
}
