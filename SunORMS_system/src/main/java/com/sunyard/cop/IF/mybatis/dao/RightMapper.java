package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sunyard.cop.IF.mybatis.pojo.Right;

public interface RightMapper {
	public ArrayList<Right> selectRight(Right right);

	public void insertRight(Right right);

	public void deleteRight(Right right);
	
	public void updateRight(Right right);

	public Integer selectMenuIdCount(Right right);

	public void deleteByMenuId(Right right);

	public List<Map<String, Object>> getMenuByroleNo(String roleNo);

	public List<Map<String, Object>> getButtonByroleNo(String roleNo);

	public void deleteByRoleNo(String roleNo);

}