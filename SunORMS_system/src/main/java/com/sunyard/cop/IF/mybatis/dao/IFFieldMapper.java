package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;
import java.util.Map;

import com.sunyard.cop.IF.mybatis.pojo.IFField;

/**
 * 
 * @author YZ 2017年3月22日 上午9:27:13
 */
public interface IFFieldMapper {

	@SuppressWarnings("rawtypes")
	public ArrayList<Map> getAllField(IFField field);

	@SuppressWarnings("rawtypes")
	public ArrayList<Map> getFieldsByModifyTime(IFField field);

	@SuppressWarnings("rawtypes")
	public ArrayList<Map> asyscSelect(IFField field);

	public int countParent(IFField field);

	public String maxField(IFField field);

	public int countField(IFField field);

	public void insertField(IFField field);

	public void updateParent(IFField field);

	public void updateParentTime(IFField field);

	public void deleteField(IFField field);

	public void updateField(IFField field);
}