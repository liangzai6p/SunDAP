package com.sunyard.ars.system.dao.mc;

import java.util.List;

import com.sunyard.ars.system.pojo.mc.OcrField;

public interface OcrFieldMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(OcrField record);

    int insertSelective(OcrField record);

    OcrField selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OcrField record);

    int updateByPrimaryKey(OcrField record);
    
    /**
     * 查询模型识别字段
     * @param record
     * @return
     */
    List<OcrField> selectBySelective(OcrField record);
}