package com.sunyard.ars.system.pojo.mc;

/**
 * 字段映射表
 * MC_TABLE_FIELD_TB
 *
 */
public class McTableField {
	/**
	 * 字典编号
	 */
    private Integer id;

	/**
	 * 表ID
	 */
    private Integer tableId;

    /**
     * 字段名
     */
    private String name;

    /**
     * 映射字段ID
     */
    private Integer fieldId;

    /**
     * 是否重要字段
     */
    private String isimportant;

    /**
     * 表字段注释
     */
    private String mark;
    
    /**
     * 字段定义
     */
    private McField field;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getIsimportant() {
        return isimportant;
    }

    public void setIsimportant(String isimportant) {
        this.isimportant = isimportant == null ? null : isimportant.trim();
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark == null ? null : mark.trim();
    }

	public McField getField() {
		return field;
	}

	public void setField(McField field) {
		this.field = field;
	}
    
    
}