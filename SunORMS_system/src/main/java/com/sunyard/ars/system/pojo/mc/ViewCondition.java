package com.sunyard.ars.system.pojo.mc;

/**
 * 
 * 视图条件表
 * MC_VIEW_CONDITION_TB
 *
 */
public class ViewCondition {

	/**
	 * 视图条件编号
	 */
    private Integer id;

	/**
	 * 条件序号
	 */
    private Integer rowno;

    /**
     * 相应的预警项的ID
     */
    private Integer modelId;

    /**
     * 视图ID
     */
    private Integer viewId;

    /**
     * 视图列表编号
     */
    private Integer viewColsId;

    /**
     * 组合类型0：无1：AND2：OR3：(4：)
     */
    private String compositeType;

    /**
     * 操作符-1：无0: 等于1：大于2：小于3：大于等于4：小于等于5：IN6：LIKE7: ISNULL
     */
    private String operator;

    /**
     * NOT操作0:NOT1:无
     */
    private String conNot;

    /**
     * 值
     */
    private String value;

    /**
     * 聚合操作类型0：无聚合操作1：COUNT操作2：COUNT(DISTINCT)操作3：SUM操作4：SUM(DISTINCT) 5: AVG取平均值
     */
    private String computeFlag;

    /**
     * 是否为可调整0：不1：是
     */
    private String isparam;

    /**
     * 参数名
     */
    private String paramName;

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

    public Integer getViewId() {
        return viewId;
    }

    public void setViewId(Integer viewId) {
        this.viewId = viewId;
    }

    public Integer getViewColsId() {
        return viewColsId;
    }

    public void setViewColsId(Integer viewColsId) {
        this.viewColsId = viewColsId;
    }

    public String getCompositeType() {
        return compositeType;
    }

    public void setCompositeType(String compositeType) {
        this.compositeType = compositeType == null ? null : compositeType.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getConNot() {
        return conNot;
    }

    public void setConNot(String conNot) {
        this.conNot = conNot == null ? null : conNot.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public String getComputeFlag() {
        return computeFlag;
    }

    public void setComputeFlag(String computeFlag) {
        this.computeFlag = computeFlag == null ? null : computeFlag.trim();
    }

    public String getIsparam() {
        return isparam;
    }

    public void setIsparam(String isparam) {
        this.isparam = isparam == null ? null : isparam.trim();
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName == null ? null : paramName.trim();
    }
}