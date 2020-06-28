package com.sunyard.ars.system.pojo.mc;

/**
 * 
 *视图列表
 *MC_VIEW_COLS_TB
 */
public class ViewCols {
	
	/**
	 * 视图编号
	 */
    private Integer id;

	/**
	 * 对应的视图的ID
	 */
    private Integer viewId;

    /**
     * 相应的表定义ID
     */
    private Integer modelFieldId;

    /**
     * 相应的字段ID
     */
    private Integer tableId;

    /**
     * 相应的预警项ID
     */
    private Integer fieldId;

    /**
     * 操作符0:= 1:+ 2:- 3:* 4:/ 5:-- 6:-/ 7:=ABS 8:+ABS 9:-ABS
     */
    private String operator;

    /**
     * 类别标志 0：赋值关系 1：更新条件关系
     */
    private String flag;

    /**
     * 分组运算0：无聚合操作10：COUNT不分组11: COUNT分组20: SUM不分组21: SUM分组30：COUNT(DISTINCT)40: SUM(DISTINCT)50:分组
     */
    private String groupCompute;

    /**
     * 值
     */
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getViewId() {
        return viewId;
    }

    public void setViewId(Integer viewId) {
        this.viewId = viewId;
    }

    public Integer getModelFieldId() {
        return modelFieldId;
    }

    public void setModelFieldId(Integer modelFieldId) {
        this.modelFieldId = modelFieldId;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }

    public String getGroupCompute() {
        return groupCompute;
    }

    public void setGroupCompute(String groupCompute) {
        this.groupCompute = groupCompute == null ? null : groupCompute.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }
}