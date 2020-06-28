package com.sunyard.ars.system.dao.hx;

import com.sunyard.ars.system.bean.hx.RiskSourceDef;
import java.util.*;

import org.apache.ibatis.annotations.Param;

public interface RiskSourceDefMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ET_RISK_SOURCE_DEF
     * 根据主键删除数据
     * @mbggenerated
     */
    int deleteByPrimaryKey(String sourceNo);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ET_RISK_SOURCE_DEF
     *
     * @mbggenerated
     */
    int insert(RiskSourceDef record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ET_RISK_SOURCE_DEF
     * 新增操作，非空属性插入
     * @mbggenerated
     */
    int insertSelective(RiskSourceDef record);
    
    /**
     * @author:		 SUNYARD-NGQ
     * @date:		 2018年9月19日 上午11:09:49
     * @description: TODO(查询所有的数据，根据sourceNo )
     */
    List<RiskSourceDef> selectAllBySourceNo(@Param("sourceNo")String sourceNo);

	/**
	 * @author:		 SUNYARD-NGQ
	 * @param        sourceType 
	 * @date:		 2018年9月27日 上午10:03:33
	 * @description: TODO(根据sourceType条件选择 )
	 */
	List selectAllBySourceType(@Param("sourceType") String sourceType);

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月27日 上午10:52:12
	 * @description: TODO(查询所有因素子类名称  )
	 */
	List selectAllSourceName();
	
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月26日 下午5:17:41
	 * @description: TODO(查询所有因素大类名称 )
	 */
	List selectAllSourceType();

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月28日 上午11:40:20
	 * @description: TODO(查询驱动因素大类 )
	 */
//	List selectAllDisSouType();
    
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月28日 下午5:27:25
	 * @description: TODO(查询所有的数据-忽略属性是否为空 )
	 */
	List<RiskSourceDef> selectAllIgno();
    /**
     * @author:		 SUNYARD-NGQ
     * @date:		 2018年9月19日 上午11:09:49
     * @description: TODO(查询所有的数据-属性不为空 )
     */
    List<RiskSourceDef> selectAll(RiskSourceDef riskSourceDefBean);

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月28日 上午9:22:41
	 * @description: TODO(检测名称是否存在 函数 )
	 */
	List selectBySourceName(@Param("sourceName") String sourceName);
    
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年10月10日 下午5:30:28
	 * @description: TODO(条件查询 )
	 */
	List<RiskSourceDef> selectByCondition(@Param("riskType")String riskTypeSel, @Param("sourceType")String sourceTypeSel,
			@Param("sourceName")String sourceNameSel, @Param("isNeed")String isNeedSel);
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月20日 下午2:27:45
	 * @description: TODO(条件查询 )
	 */
//	List<RiskSourceDef> selectByCondition(@Param("sourceType")String sourceType,@Param("isNeed")String isNeed);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ET_RISK_SOURCE_DEF
     *
     * @mbggenerated
     */
    RiskSourceDef selectByPrimaryKey(String sourceNo);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ET_RISK_SOURCE_DEF
     * 根据主键sourceNo唯一标识，更新数据
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(RiskSourceDef record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ET_RISK_SOURCE_DEF
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(RiskSourceDef record);

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年10月8日 下午7:29:44
	 * @description: TODO(获取主键的最大值 )
	 */
	Integer getMaxId();

}