package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;
import java.util.List;

import com.sunyard.cop.IF.mybatis.pojo.PageDetail;

/**
 * 拖拽页面相关相关
 * 
 * @author zxx
 *
 */
public interface PageDetailMapper {

	/**
	 * 得到所有的页面
	 * 
	 * @param pageDetail
	 * @return
	 */
	public ArrayList<PageDetail> selectAllActivePages(PageDetail pageDetail);

	/**
	 * 插入一个页面
	 * 
	 * @param pageDetail
	 * @return
	 */
	public Integer insertPage(PageDetail pageDetail);

	/**
	 * 根据pageId得到page信息
	 * 
	 * @param pageDetail
	 * @return
	 */
	public List<PageDetail> queryPageById(PageDetail pageDetail);

	/**
	 * 通过pageId及时间，找到该时间范围内有效的页面
	 * 
	 * @param pageDetail
	 * @return
	 */
	public List<PageDetail> queryPageByIdAndDate(PageDetail pageDetail);

	/**
	 * 通过pageId及version将page信息更新至删除(pageDel=0)
	 * 
	 * @param pageDetail
	 * @return
	 */
	public Integer updatePageToDel(PageDetail pageDetail);

	/**
	 * 通过modelId得到有效的页面个数
	 * 
	 * @param pageDetail
	 * @return
	 */
	public Integer countByModelId(PageDetail pageDetail);
}
