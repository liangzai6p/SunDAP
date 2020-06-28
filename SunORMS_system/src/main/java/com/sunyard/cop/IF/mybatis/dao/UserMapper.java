package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sunyard.cop.IF.mybatis.pojo.User;
import com.sunyard.cop.IF.mybatis.pojo.UserRole;

public interface UserMapper {

	public User selectByUserNo(String userNo);

	public User findUserByPassword(User user);

	@SuppressWarnings("rawtypes")
	public ArrayList getUserRole(UserRole userRole);

	@SuppressWarnings("rawtypes")
	public ArrayList getUserRoleMode(UserRole userRole);

	@SuppressWarnings("rawtypes")
	public String getSublicenseRoles(UserRole userRole);
	
	@SuppressWarnings("rawtypes")
	public ArrayList getUserRoleModeFromSublicense(HashMap<String, Object> condMap);
	
	@SuppressWarnings("rawtypes")
	public ArrayList<Map> chatSearch(User user);
	
	/**
	 * 更新用户登录信息
	 */
	public int updateUserLoginInfo(HashMap<String, Object> map);

    /**
     * 更新用户状态和错误次数
     */
    public int updateStateAndErrorCount(HashMap<String, Object> map);
    /**
     * 查询默认输入密码次数上限
     */
    public String selectDefaultErrorCount();
}