package com.sunyard.ars.risk.comm;

import com.sunyard.ars.risk.bean.arms.ModelFieldResulet;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 风险预警长常量类
 */
public class ARMSConstants {


    public static List<ModelFieldResulet> MODEL_MUST_FIELD = new ArrayList<>();


    /* @Fields FIELD_TYPE_INT : TODO 字段类型int */
    public static final int FIELD_TYPE_INT = 0;

    /* @Fields FIELD_TYPE_NUMBER : TODO 字段类型number */
    public static final int FIELD_TYPE_NUMBER = 1;

    /* @Fields FIELD_TYPE_CHAR : TODO 字段类型char */
    public static final int FIELD_TYPE_CHAR = 2;

    /* @Fields FIELD_TYPE_VARCHAR : TODO 字段类型varchar */
    public static final int FIELD_TYPE_VARCHAR = 3;

    /* @Fields FIELD_TYPE_DATE : TODO 字段类型date */
    public static final int FIELD_TYPE_DATE = 4;

    /* @Fields FIELD_TYPE_TIME : TODO 字段类型time */
    public static final int FIELD_TYPE_TIME = 5;


    /** 风险预警 * */
    public static final String SOURCE_FLAG_2 = "2"; // 风险预警

    public static final Map<String,String> OPERATOR_MAPS =  new HashMap<>();

    static {
        OPERATOR_MAPS.put("1"," = ");
        OPERATOR_MAPS.put("2"," > ");
        OPERATOR_MAPS.put("3"," < ");
        OPERATOR_MAPS.put("4",">=");
        OPERATOR_MAPS.put("5","<=");
        OPERATOR_MAPS.put("6"," IN ");
        OPERATOR_MAPS.put("7"," LIKE ");
    }
}
