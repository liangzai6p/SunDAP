package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.sunyard.cop.IF.mybatis.pojo.SysParameter;

/**
 * 
 * @author YZ 2017年3月21日 下午2:12:51
 */
public interface SysParameterMapper {

	public List<SysParameter> getSysParameters();

	@SuppressWarnings("rawtypes")
	public ArrayList<Map> selectSysParam(SysParameter sysParam);

	public void updateSysParam(SysParameter sysParam);
	
	//public SysParameter selectSysParamByprimaryKey(@Param("paramValue")String paramItem);

}
