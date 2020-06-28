package com.sunyard.ars.system.pojo.mc;
/**
 * @ClassName: TableInfo 
 * @Description: TODO 创建模型表信息
 * @Author：陈慧民
 * @Date： 2012-10-12 下午3:18:44 (创建文件的精确时间)
 */
public class TableInfo {
	
	private String tableName;

	private Integer tableFieldId;

	private String columnName;
	private String columnChName;
	private Integer columnType;
	private Integer columnLength;
	private Integer columnIsNull;
	private String columnDefValue;
	private String columnLimited;
	private String columnSprec;
	private String columnIsImportant;
	
	// 是否为系统字段
	private Integer isSystemField;
	//字段注释
	private String columnMark = "";
	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	/**
	 * @return the columnChName
	 */
	public String getColumnChName() {
		return columnChName;
	}
	/**
	 * @param columnChName the columnChName to set
	 */
	public void setColumnChName(String columnChName) {
		this.columnChName = columnChName;
	}
	/**
	 * @return the columnType
	 */
	public Integer getColumnType() {
		return columnType;
	}
	/**
	 * @param columnType the columnType to set
	 */
	public void setColumnType(Integer columnType) {
		this.columnType = columnType;
	}
	/**
	 * @return the columnLength
	 */
	public Integer getColumnLength() {
		return columnLength;
	}
	/**
	 * @param columnLength the columnLength to set
	 */
	public void setColumnLength(Integer columnLength) {
		this.columnLength = columnLength;
	}
	/**
	 * @return the columnIsNull
	 */
	public Integer getColumnIsNull() {
		return columnIsNull;
	}
	/**
	 * @param columnIsNull the columnIsNull to set
	 */
	public void setColumnIsNull(Integer columnIsNull) {
		this.columnIsNull = columnIsNull;
	}
	/**
	 * @return the columnDefValue
	 */
	public String getColumnDefValue() {
		return columnDefValue;
	}
	/**
	 * @param columnDefValue the columnDefValue to set
	 */
	public void setColumnDefValue(String columnDefValue) {
		this.columnDefValue = columnDefValue;
	}
	/**
	 * @return the columnLimited
	 */
	public String getColumnLimited() {
		return columnLimited;
	}
	/**
	 * @param columnLimited the columnLimited to set
	 */
	public void setColumnLimited(String columnLimited) {
		this.columnLimited = columnLimited;
	}
	/**
	 * @return the columnSprec
	 */
	public String getColumnSprec() {
		return columnSprec;
	}
	/**
	 * @param columnSprec the columnSprec to set
	 */
	public void setColumnSprec(String columnSprec) {
		this.columnSprec = columnSprec;
	}
	/**
	 * @return the isSystemField
	 */
	public Integer getIsSystemField() {
		return isSystemField;
	}
	/**
	 * @param isSystemField the isSystemField to set
	 */
	public void setIsSystemField(Integer isSystemField) {
		this.isSystemField = isSystemField;
	}
	public String getColumnIsImportant() {
		return columnIsImportant;
	}
	public void setColumnIsImportant(String columnIsImportant) {
		this.columnIsImportant = columnIsImportant;
	}
	public void setColumnMark(String columnMark) {
		this.columnMark = columnMark;
	}
	public String getColumnMark() {
		return columnMark;
	}

	/**
	 * @return the tableFieldId
	 */
	public Integer getTableFieldId() {
		return tableFieldId;
	}

	/**
	 * @param tableFieldId the tableFieldId to set
	 */
	public void setTableFieldId(Integer tableFieldId) {
		this.tableFieldId = tableFieldId;
	}
}
