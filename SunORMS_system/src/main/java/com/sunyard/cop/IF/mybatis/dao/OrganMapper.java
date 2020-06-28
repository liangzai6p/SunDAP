/**
 * 
 */
package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;
import java.util.Map;

import com.sunyard.cop.IF.mybatis.pojo.Organ;

/**
 * 
 * @author YZ 2017年3月22日 上午10:27:01
 */
public interface OrganMapper {

	@SuppressWarnings("rawtypes")
	public ArrayList<Map> getAllOrgans(Organ organ);
	
	/**
	 * 查询指定机构信息
	 */
	@SuppressWarnings("rawtypes")
	public Organ getOrganByNo(Map map);
}
