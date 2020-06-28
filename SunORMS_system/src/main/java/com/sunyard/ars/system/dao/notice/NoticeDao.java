package com.sunyard.ars.system.dao.notice;

import java.util.HashMap;
import java.util.List;

/**
 * 公告模块数据库操作接口
 * @author:		 wwx
 * @date:		 2017年12月18日 下午2:52:34
 */
public interface NoticeDao {

	/**
	 * 新增公告信息
	 */
	public int insert(HashMap<String, Object> map);
	
	/**
	 * 新增公告用户关联信息
	 */
	public int insert2(HashMap<String, Object> map);
	
	/**
	 * 删除公告信息
	 */
	public int deleteNotice(HashMap<String, Object> map);
	
	/**
	 * 删除公告用户关联信息
	 */
	public int deleteUsers(HashMap<String, Object> map);
	
	/**
	 * 更新阅读状态
	 */
	public int update(HashMap<String, Object> map);
	
	/**
	 * 查询发布公告信息
	 */
	public List<HashMap<String, Object>> selectNotice(HashMap<String, Object> map);
	
	/**
	 * 查询可查看公告信息
	 */
	public List<HashMap<String, Object>> selectNoticeInfo(HashMap<String, Object> map);
	
	/**
	 * 查询公告详细信息
	 */
	public List<HashMap<String, Object>> selectNoticeDetails(HashMap<String, Object> map);
	
	/**
	 * 查询可查看公告数量
	 */
	public int selectNoticeNum(HashMap<String, Object> map);
	
	/**
	 * 查询阅读情况信息
	 */
	public List<HashMap<String, Object>> selectRead(HashMap<String, Object> map);
	
	/**
	 * 查询阅读人数占比信息
	 */
	public List<HashMap<String, Object>> selectReadNum(HashMap<String, Object> map);
	
	/**
	 * 查询在线可阅用户信息
	 */
	public List<HashMap<String, Object>> selectUsers(HashMap<String, Object> map);
	
	/**
	 * 首页展示查询公告信息，以时间排序
	 */
	public List<HashMap<String, Object>> initIndexPageNoticeInfoQuery(HashMap<String, Object> map);
	
	
	/**
	 * 更新阅读量
	 */
	public int updateReadNum(HashMap<String, Object> map); 
	
	
	
	/**
	 * 查询阅读量 
	 */
	
	public Integer selecthaveReadNum(HashMap<String, Object> map);
}
