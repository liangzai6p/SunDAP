package com.sunyard.ars.system.dao.mc;

import java.util.List;

import com.sunyard.ars.system.pojo.mc.ViewCondition;

public interface ViewConditionMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ViewCondition record);

    int insertSelective(ViewCondition record);

    ViewCondition selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ViewCondition record);

    int updateByPrimaryKey(ViewCondition record);
    
    List<ViewCondition> selectBySelective(ViewCondition record);
}