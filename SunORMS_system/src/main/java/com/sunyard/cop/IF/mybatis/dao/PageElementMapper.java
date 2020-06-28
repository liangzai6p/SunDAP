package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;

import com.sunyard.cop.IF.mybatis.pojo.PageElement;

public interface PageElementMapper {

	public ArrayList<PageElement> selectAllElement(PageElement element);

	public void addElement(PageElement element);

	public void editElement(PageElement element);

	public void delElement(PageElement element);

}
