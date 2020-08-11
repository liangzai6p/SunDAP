package com.sunyard.dap.dataserve.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface LiaoNingBusiCountMapper{
		/**
		 * 获取机构业务量
		 * @return
		 */
		List<Map<String,Object>> select(@Param("date") String date);
		
		/**
		 * 获取地图中地区（机构）业务量
		 * @return
		 */
		List<HashMap<String , Object>> selectOrganBusi(@Param("params") HashMap<String, Object> map);
		
		/**
		 * 获取当日每小时业务量
		 * @return
		 */
		List<Map<String , Object>> getHourBusiCount(@Param("date") String date);
		
		/**
		 * 获取分状态业务量
		 * @return
		 */
		List<Map<String , Object>> getTransStateCount(@Param("date") String date);
		
		/**
		 * 获取当日总业务量
		 * @return
		 */
		Map<String , Object> getDayBusiCount(@Param("date") String date);
		
		/**
		 * 获取当日每小时平均处理时长
		 * @return
		 */
		List<Map<String , Object>> getHourAvgTime(@Param("params") Map<String, String> map);
		
		/**
		 * 获取柜员处理业务数
		 * @return
		 */
		List<Map<String , Object>> getTellerBusiCount(@Param("date") String date);
}
