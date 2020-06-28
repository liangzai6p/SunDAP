package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sunyard.cop.IF.mybatis.pojo.Role;

public interface RoleMapper {
	public ArrayList<Role> selectRole(Role role);
	
	ArrayList<Role> queryRoleByRoleType(Role role);

	public List<Map<String, Object>> selectRoleByRoleMode(List<String> rolelevelList);

	public List<Map<String, Object>> selectRoleNameByUserNo(String userNo);

	public List<Map<String, Object>> selectRoleNameByUserNoAndModel(String userNo);
}