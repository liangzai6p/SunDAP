package com.sunyard.dap.intilligentSchedual.entity;

import java.util.Map;

/**
 *
 * @version 1.0
 * @CreateDate 2021/02/22
 * @Copyright Sunyard
 * @ClassName RequestBean
 * @Package com.sunyard.dap.intilligentSchedual.entity
 * @Description 服务间参数配置
 * @author jie.zheng
 *
 */
public class ResponseDO {

    private String retCode;
    private String retMsg;
    private String token;
    private Map<Object, Object> retMap;
    public ResponseDO(){}

    public String getRetCode() {
        return retCode;
    }
    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }
    public String getRetMsg() {
        return retMsg;
    }
    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Map<Object, Object> getRetMap() {
        return retMap;
    }
    public void setRetMap(Map<Object, Object> retMap) {
        this.retMap = retMap;
    }

}
