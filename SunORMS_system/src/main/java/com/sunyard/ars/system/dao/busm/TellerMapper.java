package com.sunyard.ars.system.dao.busm;

import com.sunyard.ars.system.bean.busm.Teller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TellerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SM_TELLER_TB
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(@Param("tellerNo") String tellerNo);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SM_TELLER_TB
     *
     * @mbggenerated
     */
    int insert(Teller record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SM_TELLER_TB
     *
     * @mbggenerated
     */
    int insertSelective(Teller record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SM_TELLER_TB
     *
     * @mbggenerated
     */
    Teller selectByPrimaryKey(@Param("tellerNo") String tellerNo);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SM_TELLER_TB
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Teller record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table SM_TELLER_TB
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Teller record);
    
    /**
     * 根据传入对象的非空属性查询；
     * @param record
     * @return
     */
    List<Teller> selectBySelective(Teller record);

    
    /**
     * 查询用户可操作的柜员
     * @param userNo
     * @return
     */
    @SuppressWarnings("rawtypes")
	List<Map> getTellerList(@Param("userNo")String userNo,@Param("hasPrivOrganFlag")String hasPrivOrganFlag);


    /**
     * 通过柜员名获取柜员信息
     * @param operatorNo
     * @return
     */
    Teller selectTellerInfo(@Param("operatorNo") String operatorNo);

	List<Teller> queryByOrgNos(@Param("orgNoList")ArrayList<String> orgNoList);

	/**
	 * 当orgno的集合超过1000时使用，代替queryByOrgNos方法
	 * @param orgNoList
	 * @return
	 */
	List<Teller> queryByOrgNos2(@Param("orgNoList")List<List<String>> orgNoList);
}