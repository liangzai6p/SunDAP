package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;
import java.util.List;

import com.sunyard.cop.IF.mybatis.pojo.PageModel;

/**
 * 拖拽页面模板相关相关
 * 
 * @author zxx
 *
 */
public interface PageModelMapper {

	/**
	 * 得到所有的页面模板
	 * 
	 * @param page
	 * @return
	 */
	public ArrayList<PageModel> selectAllActivePageModels(PageModel pageModel);

	/**
	 * 插入一个页面模板
	 * 
	 * @param page
	 * @return
	 */
	public Integer insertPageModel(PageModel pageModel);

	/**
	 * 根据modelId得到模板信息
	 * 
	 * @param pageModel
	 * @return
	 */
	public List<PageModel> queryPageModelById(PageModel pageModel);

	/**
	 * 根据modelId及日期，得到该日期范围内有效的最新版本模板
	 * 
	 * @param pageModel
	 * @return
	 */
	public List<PageModel> queryPageModelByIdAndDate(PageModel pageModel);

	/**
	 * 通过modelId及version将模板信息更新至删除(modelDel=0)
	 * 
	 * @param pageModel
	 * @return
	 */
	public Integer updatePageModelToDel(PageModel pageModel);
}
