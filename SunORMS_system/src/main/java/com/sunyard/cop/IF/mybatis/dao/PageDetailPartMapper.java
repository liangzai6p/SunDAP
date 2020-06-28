package com.sunyard.cop.IF.mybatis.dao;

import java.util.List;

import com.sunyard.cop.IF.mybatis.pojo.PageDetailPart;

/**
 * 拖拽页面相关
 * 
 * @author zxx
 *
 */
public interface PageDetailPartMapper {

	/**
	 * 插入一个页面部分
	 * 
	 * @param pageDetail
	 * @return
	 */
	public Integer insert(PageDetailPart pageDetailPart);

	/**
	 * 根据页面ID及version，得到属于该页面的组成部分
	 * 
	 * @param pageDetailPart
	 * @return
	 */
	public List<PageDetailPart> queryByPageId(PageDetailPart pageDetailPart);

}
