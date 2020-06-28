package com.sunyard.ars.system.dao.monitor;

import com.sunyard.cop.IF.mybatis.pojo.Menu;

import java.util.HashMap;
import java.util.List;

/**
 * 自定义模板管理	数据库操作接口
 * 
 * @author:	lx
 * @date:	2019年05月14日 下午2:05:45
 */
public interface MrModuleDao {
	
	/**
	 * 查询 ... 信息
	 */
	public List<HashMap<String, Object>> select(HashMap<String, Object> map);
	/**
	 * 修改
	 */
	public int upload(HashMap<String, Object> map);

	/**
	 * 查询menu_url是否存在
	 */
	public int selectCount(Menu menu);

	//查询报表展现menu
	public List<HashMap<String, Object>> selectPar(HashMap<String, Object> map);
}
