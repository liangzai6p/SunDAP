package com.sunyard.ars.common.util;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ParamUtil {
    protected static Logger log = LoggerFactory.getLogger(ParamUtil.class);

    /**
     * ParameterList获取值
     * @param requestBean
     * @param param_field
     * @return
     */
    public static  String getParamValue(RequestBean requestBean,String param_field){
            try {
                return ((Map)requestBean.getParameterList().get(0)).get(param_field).toString();
            }catch (Exception e){
                log.error("从requestBean的ParameterList中"+param_field+"的值发生异常");
                return "";
            }
    }
}
