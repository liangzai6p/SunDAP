package com.sunyard.dap.forecast.service.impl;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.common.util.CommonUtil;
import com.sunyard.dap.common.util.DapJsonUtil;
import com.sunyard.dap.common.util.FitUtils;
import com.sunyard.dap.forecast.service.FittingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
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
}
