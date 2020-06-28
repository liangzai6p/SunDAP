package com.sunyard.ars.system.pojo.mc;

/**
 * 展现字段表
 *
 */
public class ExhibitField {
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
     * 表字段ID,对应的FIELD_MAP表里的ID
     */
    private Integer tableFieldId;

    /**
     * 字段显示格式,0:正常格式1:带分号带小数点2:百分号
     */
    private String format;

    /**
     *是否查询字段,0:不是查询字段1:作为查询字段
     */
    private String isfind;

    /**
     * 是否下拉框,0:不是下拉字段1:作为下拉字段
     */
    private String isdropdown;

    /**
     * 是否重要字段0:不是重要字段1:作为重要字段
     */
    private String isimportant;

    /**
     * 关联用户属性ID,关联到用户扩展属性表的ID
     */
    private Integer relateId;

    /**
     * 操作符,0:等于1:大于2:小于3:大于等于4:小于等于5:IN6:LIKE7:BETWEEN
     */
    private String operator;
    /**
     * 黑白名单过滤,0是无,2是白名单
     */
    private String filterType;

    private String isTuoMin;
    private String isPictureField;
    
    private String exhibitName;
    private String enName;
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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format == null ? null : format.trim();
    }

    public String getIsfind() {
        return isfind;
    }

    public void setIsfind(String isfind) {
        this.isfind = isfind == null ? null : isfind.trim();
    }

    public String getIsdropdown() {
        return isdropdown;
    }

    public void setIsdropdown(String isdropdown) {
        this.isdropdown = isdropdown == null ? null : isdropdown.trim();
    }

    public String getIsimportant() {
        return isimportant;
    }

    public void setIsimportant(String isimportant) {
        this.isimportant = isimportant == null ? null : isimportant.trim();
    }

    public Integer getRelateId() {
        return relateId;
    }

    public void setRelateId(Integer relateId) {
        this.relateId = relateId;
    }
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType == null ? null : filterType.trim();
	}

	public String getIsTuoMin() {
		return isTuoMin;
	}

	public void setIsTuoMin(String isTuoMin) {
		this.isTuoMin = isTuoMin== null ? null : isTuoMin.trim();
	}

	public String getIsPictureField() {
		return isPictureField;
	}

	public void setIsPictureField(String isPictureField) {
		this.isPictureField = isPictureField== null ? null : isPictureField.trim();
	}
	

	public String getExhibitName() {
		return exhibitName;
	}

	public void setExhibitName(String exhibitName) {
		this.exhibitName = exhibitName== null ? null : exhibitName.trim();
	}
	
	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName== null ? null : enName.trim();
	}
}