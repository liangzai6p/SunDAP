package com.sunyard.ars.system.dao.hx;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.system.bean.hx.AdFavorite;

/**
 * @author:		 SUNYARD-NGQ
 * @date:		 2018年9月15日 下午12:05:42
 * @description: TODO(操作外部案例数据库接口 )
 */
public interface AdFavoriteMapper {

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月15日 下午1:20:17
	 * @description: TODO(获取最大id )
	 */
	Integer getMaxId() throws Exception;
	
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AD_FAVORITE_TB
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String formId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AD_FAVORITE_TB
     *
     * @mbggenerated
     */
    int insert(AdFavorite record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AD_FAVORITE_TB
     *
     * @mbggenerated
     */
    int insertSelective(AdFavorite record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AD_FAVORITE_TB
     * 根据主键查询-formId 查看详情
     * @mbggenerated
     */
    List<AdFavorite> selectByPrimaryKey(@Param("formId") String formId);
    
    /**
     * @author:		 SUNYARD-NGQ
     * @date:		 2018年9月15日 下午12:07:17
     * @description: TODO(根据AdFavorite对象的非空值查询所有属性值并返回结果集合 )
     */
    List<AdFavorite> selectBySelective(AdFavorite adFavorite);

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月28日 下午2:14:56
	 * @description: TODO(条件联合查询 )
	 */
	List selectByCondition(@Param("organName") String organName, @Param("transDate") String transDate,@Param("userNo")String userNo,@Param("hasPrivOrganFlag") String hasPrivOrganFlag);

    /**
     * @author:		 SUNYARD-NGQ
     * @date:		 2018年9月14日 下午3:32:14
     * @description: TODO(根据用户的机构号条件查询 )
     */
    List selectByConditionSelective(@Param("userNo")String userNo,@Param("hasPrivOrganFlag") String hasPrivOrganFlag);
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月14日 下午3:32:14
	 * @description: TODO(条件查询 )
	 */
	List selectByConditionSelective1(AdFavorite adFavoriteBean);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AD_FAVORITE_TB
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(String formId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table AD_FAVORITE_TB
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(AdFavorite record);
    
    /**
     * 将adFavoriteOld更新为adFavoriteNew（更新adFavoriteNew中的非空字段）
     * @param adFavoriteOld
     * @param adFavoriteNew
     * @return
     */
    int updateAdFavoriteSelective(@Param("adFavoriteOld")AdFavorite adFavoriteOld, @Param("adFavoriteNew")AdFavorite adFavoriteNew);

}