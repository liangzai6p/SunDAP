package com.sunyard.ars.system.dao.busm;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.system.bean.busm.HomePage;

public interface HomePageMapper {

    int insert(HomePage record);


    int insertSelective(HomePage record);
    
    /**
     * 按条件查询用户主页配置项
     * @param condMap
     * @return
     */
    List<HomePage>selectBySelective(HashMap condMap);
    
    /**
     * 更新用户主页配置项
     * @param record
     * @return
     */
    int updateBySelective(HomePage record);
    
    /**
     * 刪除用户主页配置项
     * @param condMap
     * @return
     */
    int delUserHomePage(HashMap condMap);
    
    /**
     * 删除超级管理员不需要展现的主页配置
     * @param condMap
     * @return
     */
    int delAdminHomePage(HashMap condMap);
    
    /**
     * 新增超级管理员可展现的主页配置项
     * @param condMap
     * @return
     */
    int addAdminHomePage(HashMap condMap);
    
    /**
     * 新增普通用户可展现的主页配置项
     * @param condMap
     * @return
     */
    int addUserHomePage(HashMap condMap);
    
    /**
     * 查询2级父菜单
     * @return
     */
    String selParentMenu(String menuClass);


	String querAllTaskMenu(@Param("userNo")String userNo);
}