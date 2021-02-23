package com.sunyard.dap.intilligentSchedual.entity;

import java.util.HashMap;
import java.util.List;

public class modelTrainingDO {
    /**
     *
     */
    private static final long serialVersionUID = 2334666070169750748L;
    private List<HashMap<String,Object>> modelTrainingBean;
    public List<HashMap<String, Object>> getModelTrainingBean() {
        return modelTrainingBean;
    }
    public void setModelTrainingBean(List<HashMap<String, Object>> modelTrainingBean) {
        this.modelTrainingBean = modelTrainingBean;
    }
}
