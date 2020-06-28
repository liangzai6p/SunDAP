package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;

import com.sunyard.cop.IF.mybatis.pojo.Menu;
import com.sunyard.cop.IF.mybatis.pojo.PageDetail;

/**
 * 
 * @author
 *
 */
public interface CustomMenuMapper {

	public ArrayList<Menu> selectCustomMenu(Menu menu);

	public String getMaxMenuId(Menu menu);

	public String getMaxMenuOrder(Menu menu);

	public int insertCustomMenu(Menu menu);
	
	public ArrayList<Menu> selectMenuByMenuId(Menu menu);

	public Integer getMenuIdCount(Menu menu);
	
	public int updateCustomMenu(Menu menu);
	
	public int updateParentMenu(Menu menu);

	public Integer getMenuIdCountByParentId(Menu menu);

	public String getEditEnable(Menu menu);

	public void deleteCustomMenu(Menu menu);

	public void updateMenuOreder(Menu menu);

	public ArrayList<PageDetail> getMenuPage(PageDetail page);
}
