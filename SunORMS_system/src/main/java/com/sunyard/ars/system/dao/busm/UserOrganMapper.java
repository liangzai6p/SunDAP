package com.sunyard.ars.system.dao.busm;

import com.sunyard.ars.system.bean.busm.UserOrgan;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface UserOrganMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SM_USER_ORGAN_TB
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(@Param("userNo") String userNo, @Param("organNo") String organNo);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SM_USER_ORGAN_TB
     *
     * @mbggenerated
     */
    int insert(UserOrgan record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SM_USER_ORGAN_TB
     *
     * @mbggenerated
     */
    int insertSelective(UserOrgan record);
    
    /**
     * 删除指定用户机构权限数据
     * @param userNo
     * @return
     */
    int deleteByUserNo(String userNo);
    
    /**
     * 获取指定用户的权限机构列表
     * @param userNo
     * @return
     */
    List<String> getPrivOrganByUserNo(@Param("userNo")String userNo,@Param("hasPrivOrganFlag")String hasPrivOrganFlag);

	List<Map<String, Object>> getprivOgranOfMine(String userNo);
}
