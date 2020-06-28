package com.sunyard.ars.system.pojo.mc;

/**
 * MC_OCR_FIELD_TB
 * 自动监督字段配置表
 */
public class OcrField {

	/**
	 * 字段编号
	 */
    private Integer id;

	/**
	 * 字段序号
	 */
    private Integer rowno;

    /**
     * 模型ID
     */
    private Integer modelId;

    /**
     * 表字段ID
     */
    private Integer tableFieldId;

    /**
     * 表字段名称
     */
    private String tableFieldName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRowno() {
        return rowno;
    }

    public void setRowno(Integer rowno) {
        this.rowno = rowno;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getTableFieldId() {
        return tableFieldId;
    }

    public void setTableFieldId(Integer tableFieldId) {
        this.tableFieldId = tableFieldId;
    }

    public String getTableFieldName() {
        return tableFieldName;
    }

    public void setTableFieldName(String tableFieldName) {
        this.tableFieldName = tableFieldName == null ? null : tableFieldName.trim();
    }
}