package com.sunyard.ars.system.dao.mc;

import java.util.HashMap;
import java.util.List;

import com.sunyard.ars.system.pojo.mc.ViewCols;

public interface ViewColsMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ViewCols record);

    int insertSelective(ViewCols record);

    ViewCols selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ViewCols record);

    int updateByPrimaryKey(ViewCols record);
    
    /**
     * 按条件查询
     * @param record
     * @return
     */
    List<ViewCols> selectBySelective(ViewCols record);
    
    /**
     * 查询视图列对应mc_field_tb中字段
     * @param record
     * @return
     */
    List<HashMap<String,Object>> selectViewColsInfo(ViewCols record);
}