package com.sunyard.dap.forecast.service;

import com.sunyard.dap.common.model.ReturnT;

import java.util.List;

/**
 * @program: SunDAP
 * @description: 拟合运算
 * @author: yey.he
 * @create: 2020-09-11 11:35
 **/
public interface FittingService {
    ReturnT<List> lineFitting(String params);

    ReturnT<List> polyFitting(String params);

    ReturnT<String> standardDeviation(String params);



}
