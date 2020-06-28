package com.sunyard.cop.IF.mybatis.dao;

import com.sunyard.cop.IF.mybatis.pojo.MenuCustompage;

public interface MenuCustompageMapper {

	public void insertMenuPage(MenuCustompage menupage);

	public Integer selectMenuIdCount(MenuCustompage menupage);

	public void updateMenupage(MenuCustompage menupage);

	public void deleteMenupage(MenuCustompage menupage);

}
