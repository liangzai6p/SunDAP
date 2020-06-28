package com.sunyard.ars.system.bean.othersys;

/**
 * @ClassName: Column
 * @Description: 数据库表字段
 * @Author：
 * @Date： 
 */
public class ArmsColumn {
//	private int columnType; //字段类型
	private String columnName; //字段名称
	private int columnSize; //字段最大值
//	private int decimalDigit; //
	private String typeName; //类型名称
	private String columnDef; //默认值
	private String isNullable; //可为空

	public String getColumnDef() {
		return this.columnDef;
	}

	public void setColumnDef(String columnDef) {
		this.columnDef = columnDef;
	}

	public String getIsNullable() {
		return this.isNullable;
	}

	public void setIsNullAble(String isNullable) {
		this.isNullable = isNullable;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

//	public int getDecimalDigit() {
//		return this.decimalDigit;
//	}
//
//	public void setDecimalDigit(int decimalDigit) {
//		this.decimalDigit = decimalDigit;
//	}
//
//	public int getColumnType() {
//		return this.columnType;
//	}

	public int getColumnSize() {
		return this.columnSize;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

//	public void setColumnType(int columnType) {
//		this.columnType = columnType;
//	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
}