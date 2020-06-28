package com.sunyard.cop.IF.mybatis.dao;

import java.util.Map;

import com.sunyard.cop.IF.mybatis.pojo.User;

public interface UserPasswordConfigMapper {

	int selectChangeUser(User user);

	int selectResetUser(User u);

	int selectGrantUser(Map<String, Object> params);

	int updatePassword(User u);

	String checkPassword(User uer);
}
