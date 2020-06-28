package com.sunyard.ars.system.dao.sc;

import java.util.List;
import java.util.Map;

import com.sunyard.ars.system.bean.sc.BlackListBean;

public interface BlackListMapper {
		//添加
		void save(Map<String,Object> map);
		//删除
		void  delete(Map<String,Object> map);
		//修改
		void update(Map<String,Object> map);
		//查询
		List<Map<String,Object>>  select(Map<String,Object> map);
		
		Integer getMaxId();
		
		//查询全部，导出使用
		List<Map<String,Object>>  selectAll();
		
		/**
		 * 获取白名单字段信息
		 * @param modelId
		 * @return
		 */
		List<Map<String,Object>> getWhiteFieldList(Map<String,Object> map);
		
		/**
		 * 白名单导入前先判断是否有重复数据
		 */
		List<Map<String,Object>> selectExist(BlackListBean model);
		
	
	
}
