package com.sunyard.aos.common.dao;

import java.util.HashMap;

/**
 * 公共基础 数据库操作接口
 * 
 * @author:	
 * @date:	2017年12月7日 下午5:11:45
 */
public interface BaseDao {

	/**
	 * 新增 日志 信息
	 */
	public int insertLogInfo(HashMap<String, Object> map);
	
	/**
	 * 用户登出，更新登出时间、清空登录服务地址（区分pc、移动）等
	 */
	public int userLogout(HashMap<String, Object> map);
    public int userLogout2(HashMap<String, Object> map);
	
	/**
	 * 更新用户登录状态
	 */
	public int updateUserLoginState(HashMap<String, Object> map);
	
	/**
	 * 查询系统参数值
	 */
	public String getSysParamValue(HashMap<String, Object> map);
	
	/**
	 * 新增 操作日志 信息
	 */
	public int insertOperLogInfo(HashMap<String, Object> map);
}
