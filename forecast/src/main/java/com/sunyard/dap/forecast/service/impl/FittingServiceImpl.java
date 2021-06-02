package com.sunyard.dap.forecast.service.impl;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.common.util.CommonUtil;
import com.sunyard.dap.common.util.DapJsonUtil;
import com.sunyard.dap.common.util.FitUtils;
import com.sunyard.dap.forecast.service.FittingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: SunDAP
 * @description: 拟合运算实现类
 * @author: yey.he
 * @create: 2020-09-11 11:38
 **/
@Service
@Slf4j
public class FittingServiceImpl implements FittingService {


    @Override
    public ReturnT<List> lineFitting(String params) {
        try {
            double[] xArray = DapJsonUtil.getJsonDoubleArray(params,"X-AXIS");
            double[] yArray = DapJsonUtil.getJsonDoubleArray(params,"Y-AXIS");
            double[] results = FitUtils.lineFitting(xArray, yArray);
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"计算成功", CommonUtil.toDoubleList(results));
        }catch (NumberFormatException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"数字格式异常\n"+e.getMessage(),null);
        }catch (JSONException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"JSON转换异常\n"+e.getLocalizedMessage(),null);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"计算异常\n"+e.getMessage(),null);
        }

    }

    @Override
    public ReturnT<Map> lineFittingXY(String params) {
        try {
            double[] xArray = DapJsonUtil.getJsonDoubleArray(params,"X-AXIS");
            double[] yArray = DapJsonUtil.getJsonDoubleArray(params,"Y-AXIS");
            Integer timeLength=DapJsonUtil.getJsonInteger(params,"timeLength");
            Integer index=0;
            if (params.indexOf("index")!=-1){
                 index=DapJsonUtil.getJsonInteger(params,"index");
            }
            double[] results = FitUtils.lineFitting(xArray, yArray);
            ArrayList<Double> list=CommonUtil.toDoubleList(results);
            List dottedData = new ArrayList();
            List solidData = new ArrayList();
            for (int i = 0; i < timeLength; i++) {
                dottedData.add(list.get(1) * (i + 1) + list.get(0));
                if (params.indexOf("index")!=-1){
                    if (i < index) {
                        solidData.add(list.get(1) * (i + 1) + list.get(0));
                    }
                }

            }
            HashMap map = new HashMap();
            map.put("dottedData", dottedData);
            map.put("solidData", solidData);
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"计算成功",map);
        }catch (NumberFormatException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"数字格式异常\n"+e.getMessage(),null);
        }catch (JSONException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"JSON转换异常\n"+e.getLocalizedMessage(),null);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"计算异常\n"+e.getMessage(),null);
        }
    }

    @Override
    public ReturnT<Map> lineFittingElcXY(String params) {
        try {
            double[] xArray = DapJsonUtil.getJsonDoubleArray(params,"X-AXIS");
            double[] yArray = DapJsonUtil.getJsonDoubleArray(params,"Y-AXIS");
            Integer length=DapJsonUtil.getJsonInteger(params,"fitLineDataLength");
            double[] results = FitUtils.lineFitting(xArray, yArray);
            ArrayList<Double> list=CommonUtil.toDoubleList(results);
            List fitData = new ArrayList();
            for (int i = 0; i < length; i++) {
                fitData.add(list.get(1) * (i + 1) + list.get(0));

            }
            HashMap map = new HashMap();
            map.put("fitData", fitData);
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"计算成功",map);
        }catch (NumberFormatException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"数字格式异常\n"+e.getMessage(),null);
        }catch (JSONException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"JSON转换异常\n"+e.getLocalizedMessage(),null);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"计算异常\n"+e.getMessage(),null);
        }
    }

    @Override
    public ReturnT<List> polyFitting(String params) {
        try {
            int times = Integer.parseInt(JSONUtil.parseObj(params).getStr("TIMES"));
            double[] xArray = DapJsonUtil.getJsonDoubleArray(params,"X-AXIS");
            double[] yArray = DapJsonUtil.getJsonDoubleArray(params,"Y-AXIS");
            double[] results = FitUtils.polyFitting(times, xArray, yArray);
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"计算成功", CommonUtil.toDoubleList(results));
        }catch (NumberFormatException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"数字格式异常\n"+e.getMessage(),null);
        }catch (JSONException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"JSON转换异常\n"+e.getLocalizedMessage(),null);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"计算异常\n"+e.getMessage(),null);
        }
    }

    @Override
    public ReturnT<String> standardDeviation(String params) {
        try {
            double[] values = DapJsonUtil.getJsonDoubleArray(params, "VALUES");
            double rs = FitUtils.standardDeviation(values);
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"计算成功",String.valueOf(rs));
        }catch (NumberFormatException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"数字格式异常\n"+e.getMessage(),null);
        }catch (JSONException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"JSON转换异常\n"+e.getLocalizedMessage(),null);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"计算异常\n"+e.getMessage(),null);
        }

    }

    /**
     * 周线
     * @Author jie1.zheng
     * @Date 10:01 AM 2020/12/15
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @Override
    public ReturnT<Map> contourFitting(String params) {
        try {
            String[] xArray = DapJsonUtil.getJsonStrArray(params,"X-AXIS");
            double[] yArray = DapJsonUtil.getJsonDoubleArray(params,"Y-AXIS");
            double[] results = FitUtils.contourFitting(yArray);
            List<String> xAxis = new ArrayList<String>();
            List<Double> yAxis = new ArrayList<Double>();
            for(int i=0;i<xArray.length;i++){
                xAxis.add(xArray[i]);
                yAxis.add(results[i]);
            }
            Map<String , Object> retList = new HashMap<String , Object>();
            retList.put("yAxis" , yAxis);
            retList.put("xAxis" , xAxis);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE,"计算成功", retList);
        }catch (NumberFormatException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"数字格式异常\n"+e.getMessage(),null);
        }catch (JSONException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"JSON转换异常\n"+e.getLocalizedMessage(),null);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"计算异常\n"+e.getMessage(),null);
        }

    }

    /**
     * 月线
     * @Author jie1.zheng
     * @Date 10:01 AM 2020/12/15
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @Override
    public ReturnT<Map> onLineFitting(String params) {
        try {
            String[] xArray = DapJsonUtil.getJsonStrArray(params,"X-AXIS");
            double[] yArray = DapJsonUtil.getJsonDoubleArray(params,"Y-AXIS");
            double[] results = FitUtils.onLineFitting(yArray);
            System.out.println(Arrays.toString(results));
            List<String> xAxis = new ArrayList<String>();
            List<Double> yAxis = new ArrayList<Double>();
            for(int i=0;i<xArray.length;i++){
                xAxis.add(xArray[i]);
                yAxis.add(results[i]);
            }
            Map<String , Object> retList = new HashMap<String , Object>();
            retList.put("yAxis" , yAxis);
            retList.put("xAxis" , xAxis);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE,"计算成功", retList);
        }catch (NumberFormatException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"数字格式异常\n"+e.getMessage(),null);
        }catch (JSONException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"JSON转换异常\n"+e.getLocalizedMessage(),null);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"计算异常\n"+e.getMessage(),null);
        }

    }

    /**
     * 年线
     * @Author jie1.zheng
     * @Date 10:01 AM 2020/12/15
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @Override
    public ReturnT<Map> annualLineFitting(String params) {
        try {
            String[] xArray = DapJsonUtil.getJsonStrArray(params,"X-AXIS");
            double[] yArray = DapJsonUtil.getJsonDoubleArray(params,"Y-AXIS");
            double[] results = FitUtils.annualLineFitting(yArray);
            System.out.println(Arrays.toString(results));
            List<String> xAxis = new ArrayList<String>();
            List<Double> yAxis = new ArrayList<Double>();
            for(int i=0;i<xArray.length;i++){
                xAxis.add(xArray[i]);
                yAxis.add(results[i]);
            }
            Map<String , Object> retList = new HashMap<String , Object>();
            retList.put("yAxis" , yAxis);
            retList.put("xAxis" , xAxis);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE,"计算成功", retList);
        }catch (NumberFormatException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"数字格式异常\n"+e.getMessage(),null);
        }catch (JSONException e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"JSON转换异常\n"+e.getLocalizedMessage(),null);
        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"计算异常\n"+e.getMessage(),null);
        }

    }
}
