package com.sunyard.ars.risk.init;


import com.sunyard.ars.risk.bean.arms.ModelFieldResulet;
import com.sunyard.ars.risk.comm.ARMSConstants;
import com.sunyard.ars.system.dao.mc.McFieldMapper;
import com.sunyard.ars.system.init.SystemInitialize;
import com.sunyard.ars.system.pojo.mc.McField;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统初始化
 * @author zgz
 */
public class ArmsSystemInitialize {
    private final Logger logger = Logger.getLogger(SystemInitialize.class);

    @Resource
    public McFieldMapper mcFieldMapper ;

    public void systemInit() {
        logger.info("预警模型开始系统初始化。。。。");
        List<McField> fields  = mcFieldMapper.getArmsMustFiedl();
        List<ModelFieldResulet> MODEL_MUST_FIELD = new ArrayList<>();

        ModelFieldResulet modelFieldResulet = null;
        for (int i = 0 ; i < fields.size() ;i++){
            modelFieldResulet = new ModelFieldResulet();
            modelFieldResulet.setFieldId(fields.get(i).getId());
            modelFieldResulet.setName(fields.get(i).getName());
            modelFieldResulet.setChName(fields.get(i).getChName());
            modelFieldResulet.setType(Integer.parseInt(fields.get(i).getType()));
            modelFieldResulet.setRowno(100);
            modelFieldResulet.setFormat(0);
            modelFieldResulet.setIsfind(0);
            modelFieldResulet.setIsdropdown(0);
            modelFieldResulet.setIsimportant(0);
            modelFieldResulet.setRelateId(0);
            modelFieldResulet.setIsShow("0");//查询字段不显示
            ARMSConstants.MODEL_MUST_FIELD.add(modelFieldResulet);
        }


        logger.info("预警模型系统初始化完成。。。。");
    }
}
