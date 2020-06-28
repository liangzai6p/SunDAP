package com.sunyard.ars.common.pojo;

/*
 *  用于做事务用的程序判断删除数据所在表的记录是否允许删除，判断其数据是否在别的数据表（已经定义好的关联表）中已经用到
 */
public class Relate implements java.io.Serializable {

	public String tabalName;
	public String fieldName;// xxx,yyy
	private final static long serialVersionUID = 1l;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getTabalName() {
		return tabalName;
	}

	public void setTabalName(String tabalName) {
		this.tabalName = tabalName;
	}

	public Relate(String tableName, String fieldName)// 某个表里的某个字段;
	{
		this.tabalName = tableName;
		this.fieldName = fieldName;
	}
}
