package com.sunyard.ars.system.dao.busm;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @author:		zheng.jw
 * @date:		2017年12月18日 上午9:56:13
 * @Description:机构信息数据库操作接口
 */
public interface OrganInfoDao {

	/**
	 * 新增机构信息信息
	 */
	public int insert(HashMap<String, Object> map);
	
	/**
	 * 新增机构信息
	 */
	public int insertOrganParent(HashMap<String, Object> map);
	
	/**
	 * 新增父子关系
	 */
	public int insertOrganParents(HashMap<String, Object> map);
	
	/**
	 * 重构机构父子关系
	 */
	public int insertResetOrganParents(HashMap<String, Object> map);
	
	/**
	 * 删除机构信息信息
	 */
	public int delete(HashMap<String, Object> map);
	
	/**
	 * 删除机构父子表里原机构的关系信息
	 */
	public int deleteOrganParents(HashMap<String, Object> map);
	
	/**
	 * 删除机构父子信息
	 */
	public int deleteOrganParent(HashMap<String, Object> map);
	
	/**
	 * 修改机构信息信息
	 */
	public int update(HashMap<String, Object> map);
	/**
	 * 修改McOrganTbKj表
	 * @param map
	 * @return
	 */
	public int updateMcOrganTbKj(HashMap<String, Object> map);
	
	
	/**
	 * 更新机构表里父机构字段
	 */
	public int updateOrganParent(HashMap<String, Object> map);
	
	/**
	 * 判断该organ_no机构是否已经存在
	 */
	public List<HashMap<String, Object>> getTemp(HashMap<String, Object> map);
	
	/**
	 * 查询机构信息信息
	 */
	public List<HashMap<String, Object>> select(HashMap<String, Object> map);
	
	/**
	 * 查询修改前机构信息
	 */
	public List<HashMap<String, Object>> selectOldOrgan(HashMap<String, Object> map);
	
	/**
	 * 查询机构详细信息
	 */
	public List<HashMap<String, Object>> selectOrganDetail(HashMap<String, Object> map);
	
	/**
	 * 用户表中查看该机构下是否有用户
	 */
	public List<HashMap<String, Object>> selectUserCount(HashMap<String, Object> map);
	
	/**
	 * 查看是否存在子机构
	 */
	public List<HashMap<String, Object>> selectSonOrganCount(HashMap<String, Object> map);
	
	/**
	 * 查询新父机构的原父子关系信息
	 */
	public List<HashMap<String, Object>> selectOrganInfo(HashMap<String, Object> map);

	/**
	 * 通过机构号获取机构信息
	 * @param siteNo
	 * @return
	 */
	public List<HashMap<String, Object>> selectSiteInfo(@Param("siteNo") String siteNo);
	
	/**
	 * 获取当前用户权限机构列表
	 */
	public List<HashMap<String, Object>> getOrganList(HashMap<String, Object> map);
	
	/**
	 * 获取当前用户权限机构列表(用于机构树展现)
	 */
	public List<HashMap<String, Object>> getUserPrivOrganList(@Param("userNo")String userNo,@Param("hasPrivOrganFlag")String hasPrivOrganFlag);
	/**
	 * 获取当前用户权限机构列表(用于机构树展现)
	 */
	/**
	 * @param organNo
	 * @param dbType
	 * @author jun2.hu改,添加dbtype 2020-01-07
	 * @return
	 */
	public List<HashMap<String, Object>> getChildOrganList(@Param("organNo")String organNo,@Param("dbType")String dbType);

	List getThisLevelOrganLisr(@Param("organ_level") Integer organ_level,@Param("userOrganNo") String userOrganNo);

	List getLowerOrgans(@Param("userOrganNo")String userOrganNo, @Param("userNo")String userNo,@Param("hasPrivOrganFlag")String hasPrivOrganFlag);

	/**
	 * 获取所有机构树
	 * @return
	 */
	public List<HashMap<String, Object>> getAllOrganList();


	/**
	 * 根据级别查询分行支行  数据！
	 * @author:YHLZS
	 * @date:  2018年11月30日 下午6:08:24
	 * @tags  @param organlevelList
	 * @tags  @return
	 * @description:
	 */
	public List<HashMap<String, Object>> selectAllOrgans(List<String> organlevelList);

	public List<HashMap<String, Object>>  getChildByOrganNo(String parentOrganNo);

	public List<HashMap<String, Object>> getUserPrivChildOrganList(@Param("organNo")String belongOrganNo, @Param("userNo")String userNo);

	/**
	 * 获取机构级别
	 * @param map
	 * @return
	 */
	public List<HashMap<String, Object>> getOrganLevel(HashMap<String, Object> map);

	/**
	 * getOrganLevel 的 sql中用in 超出 1000 会报错
	 * @param map
	 * @return
	 */
	public List<HashMap<String, Object>> getOrganLevel2(HashMap<String, Object> map);
	
	/**
	 * 获取用户一级分行，总行获取总行信息
	 * @param organ_no
	 * @return
	 */
	public List<HashMap<String, Object>> getCenterOrganLevel(@Param("organ_no")String organ_no);
	
	public List<HashMap<String, Object>> getprivOgranOfMine(@Param("userNo")String userNo,@Param("hasPrivOrganFlag")String hasPrivOrganFlag);
	
	/**
	 * 增加用户权限机构表
	 * @param organNo
	 * @param userNo
	 * @return
	 */
	public int addUserOrgan(@Param("organNo")String organNo,@Param("userNo")String userNo);
	
	/**
	 * 删除制定用户号的所有授权机构
	 * @param userNo
	 * @return
	 */
	public int deleteUserAllOrgan(@Param("userNo")String userNo);
	public HashMap<String, Object> getUserPrivOrganCount(@Param("userNo")String userNo,@Param("organNo")String organNo,@Param("hasPrivOrganFlag") String hasPrivOrganFlag);
	public List<HashMap<String, Object>> belongOrganList(@Param("organNo")String organNo, @Param("userNo")String userNo,@Param("hasPrivOrganFlag")String hasPrivOrganFlag);

    /**
     * 判断是否有权限机构
     * @param map
     * @return
     */
	public int hasPrivOrgan(HashMap<String, Object> map);
}
