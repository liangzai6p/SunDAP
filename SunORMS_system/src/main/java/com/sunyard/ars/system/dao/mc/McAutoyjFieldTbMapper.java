package com.sunyard.ars.system.dao.mc;



import com.sunyard.ars.system.pojo.mc.McAutoyjFieldTb;

public interface McAutoyjFieldTbMapper {

    int deleteByPrimaryKey(String modelId);

    int insertSelective(McAutoyjFieldTb record);

    McAutoyjFieldTb selectByPrimaryKey(String modelId);

    int updateByPrimaryKeySelective(McAutoyjFieldTb record);

    int updateByPrimaryKey(McAutoyjFieldTb record);
}