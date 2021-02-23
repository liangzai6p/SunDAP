package com.sunyard.dap.intilligentSchedual.entity;

import java.util.List;
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
public class RequestDO {
    public Map<Object, Object> getSysMap() {
        return sysMap;
    }
    public void setSysMap(Map<Object, Object> sysMap) {
        this.sysMap = sysMap;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public List<Object> getParameterList() {
        return parameterList;
    }
    public void setParameterList(List<Object> parameterList) {
        this.parameterList = parameterList;
    }
    private Map<Object,Object> sysMap;
    private String token;
    private List<Object> parameterList;
}
