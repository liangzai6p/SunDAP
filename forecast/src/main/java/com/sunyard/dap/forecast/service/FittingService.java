package com.sunyard.dap.forecast.service;

import com.sunyard.dap.common.model.ReturnT;

import java.util.List;
import java.util.Map;

/**
 * @program: SunDAP
 * @description: 拟合运算
 * @author: yey.he
 * @create: 2020-09-11 11:35
 **/
public interface FittingService {
    ReturnT<List> lineFitting(String params);

    ReturnT<Map> lineFittingXY(String params);

    ReturnT<Map> lineFittingElcXY(String params);

    ReturnT<List> polyFitting(String params);

    ReturnT<String> standardDeviation(String params);

    /**
     * 周线
     * @Author jie1.zheng
     * @Date 10:01 AM 2020/12/15
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    ReturnT<Map> contourFitting(String params);

    /**
     * 月线
     * @Author jie1.zheng
     * @Date 10:01 AM 2020/12/15
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    ReturnT<Map> onLineFitting(String params);

    /**
     * 年线
     * @Author jie1.zheng
     * @Date 10:01 AM 2020/12/15
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    ReturnT<Map> annualLineFitting(String params);

}
