package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;
import java.util.List;

import com.sunyard.cop.IF.mybatis.pojo.Button;
import com.sunyard.cop.IF.mybatis.pojo.Menu;

public interface ButtonMapper {

	public int insertButton(Button button);

	public int deleteButton(Button button);

	public Integer getMenuButtonCount(Menu menu);

	public ArrayList<Button> selectButtonByMenuId(Menu menu);
	
	public String select(Button button);
	
}
