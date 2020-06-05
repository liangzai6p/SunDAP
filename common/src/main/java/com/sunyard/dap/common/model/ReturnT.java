package com.sunyard.dap.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: SunDAP
 * @description: 统一Restful返回格式
 * @author: yey.he
 * @create: 2020-06-03 11:06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnT<T> implements Serializable {
    public static final long serialVersionUID = 1L;

    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;
    public static final ReturnT<String> SUCCESS = new ReturnT<String>(null);
    public static final ReturnT<String> FAIL = new ReturnT<String>(FAIL_CODE, null);

    private int code;
    private String msg;
    private int error_code;
    private T content;
    private long time;

    public ReturnT(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public ReturnT(int code, int error_code,String msg) {
        this.code = code;
        this.error_code = error_code;
        this.msg = msg;
    }

    public ReturnT(int code, String msg, T content) {
        this.code = code;
        this.msg = msg;
        this.content = content;
    }

    public ReturnT(T content) {
        this.code = SUCCESS_CODE;
        this.content = content;
    }

}
