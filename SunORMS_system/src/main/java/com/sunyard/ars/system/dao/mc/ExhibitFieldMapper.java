package com.sunyard.ars.system.dao.mc;

import java.util.HashMap;
import java.util.List;

import com.sunyard.ars.system.pojo.mc.ExhibitField;
import org.apache.ibatis.annotations.Param;

public interface ExhibitFieldMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExhibitField record);

    int insertSelective(ExhibitField record);

    ExhibitField selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExhibitField record);

    int updateByPrimaryKey(ExhibitField record);
    
    List<ExhibitField> selectBySelective(ExhibitField record);

    /**
     * 获取展现字段
     * @param modelId
     */
    List<HashMap<String,Object>> showExhibitField(@Param("modelId") int modelId);

    /**
     * 获取展现查询字段
     * @param modelId
     */
    List<HashMap<String,Object>> showExhibitQueryField(@Param("modelId") int modelId);
    List<ExhibitField> selectEnName(ExhibitField record);//查找字段的英文和中文名
    List<ExhibitField> showIsTuoMin(ExhibitField record);//判断是否脱敏

	int updateExhibitChName(@Param("modelId") int modelId);
}