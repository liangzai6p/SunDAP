package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;
import java.util.Map;

import com.sunyard.cop.IF.mybatis.pojo.Menu;

public interface MenuTreeMapper {

	@SuppressWarnings("rawtypes")
	ArrayList<Map> selectAdminMenu(Menu menu);
	
	@SuppressWarnings("rawtypes")
	ArrayList<Map> selectdefaultQueryMenu(Map<String, Object> params);
	
	@SuppressWarnings("rawtypes")
	ArrayList<Map> initMenu(Menu menu);
	
	@SuppressWarnings("rawtypes")
	ArrayList<Map> initButton(Map<String, Object> params);
	
	@SuppressWarnings("rawtypes")
	ArrayList<Map> selectButtonByMenu(Map<String, Object> params);
}
