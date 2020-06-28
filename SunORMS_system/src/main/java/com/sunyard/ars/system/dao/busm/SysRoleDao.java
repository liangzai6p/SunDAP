package com.sunyard.ars.system.dao.busm;

import java.util.HashMap;
import java.util.List;

import com.sunyard.ars.system.bean.busm.RoleBean;

/**
 * 角色管理数据库操作接口
 * @author:		 wwx
 * @date:		 2017年12月19日 下午5:46:46
 */
public interface SysRoleDao {

	/**
	 * 新增角色信息
	 */
	public int insert(HashMap<String, Object> map);
	
	/**
	 * 删除角色信息
	 */
	public int delete(HashMap<String, Object> map);
	
	/**
	 * 修改角色信息
	 */
	public int update(HashMap<String, Object> map);
	
	/**
	 * 查询角色信息
	 */
	public List<HashMap<String, Object>> select(HashMap<String, Object> map);
	
	/**
	 * 查询角色号是否已存在
	 */
	public int selectRole(HashMap<String, Object> map);
	
	String selectByRoleNo(HashMap<String, Object> map);

	public RoleBean selectAllByRoleNo(String string);

}
