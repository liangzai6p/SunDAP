package com.sunyard.ars.file.dao.ma;

import com.sunyard.ars.file.pojo.ma.HotKey;
import java.util.HashMap;
import java.util.List;

public interface HotKeyMapper {
    
    int deleteByPrimaryKey(Integer id);

    
    int insert(HotKey record);

    
    int insertSelective(HotKey record);

    
    HotKey selectByPrimaryKey(Integer id);

    
    int updateByPrimaryKeySelective(HotKey record);

    
    int updateByPrimaryKey(HotKey record);
    
    /**
     * 按条件查询快捷键
     * @param codeMap
     * @return
     */
    List<HotKey> selectBySelective(HashMap<String, Object> codeMap);
    
    /**
     * 按条件删除
     * @param codeMap
     * @return
     */
    int deleteBySelective(HashMap<String, Object> codeMap);
    
}