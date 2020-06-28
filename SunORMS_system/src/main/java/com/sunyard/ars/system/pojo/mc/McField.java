package com.sunyard.ars.system.pojo.mc;

/**
 * 字段定义表MC_FIELD_TB
 *
 */
public class McField {

	/**
	 * 编号
	 */
    private Integer id;

    /**
     * 字段名
     */
    private String name;

    /**
     * 中文名
     */
    private String chName;

    /**
     * 数据类型0:整型INT,1:数值型NUMERIC,2:字符型CHAR,3:字符串型VARCHAR,4:时间型,5:日期型
     */
    private String type;

    /**
     * 长度
     */
    private String length;

    /**
     * 范围
     */
    private String scale;

    /**
     * 精度
     */
    private String sprec;

    /**
     * 是否可为空0:不能为空1:可为空
     */
    private String isnull;

    /**
     * 缺省值
     */
    private String defvalue;

    /**
     * 是否定长0:否1:是
     */
    private String limited;

    /**
     * 表类型0:参数表 1:配置中间表 2:预警展现表 3:重点监督表 4:强制补录表 5:普通字段
     */
    private String tableType;

    private String hasDic;
    private String dicName;
    
    /**
     * 是否索引字段
     */
    private String isIndex;
    
    
    public String getHasDic() {
        return hasDic;
    }

    public void setHasDic(String hasDic) {
        this.hasDic =hasDic == null ? null : hasDic.trim();
    }
    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName == null ? null : dicName.trim();
    }
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName == null ? null : chName.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length == null ? null : length.trim();
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale == null ? null : scale.trim();
    }

    public String getSprec() {
        return sprec;
    }

    public void setSprec(String sprec) {
        this.sprec = sprec == null ? null : sprec.trim();
    }

    public String getIsnull() {
        return isnull;
    }

    public void setIsnull(String isnull) {
        this.isnull = isnull == null ? null : isnull.trim();
    }

    public String getDefvalue() {
        return defvalue;
    }

    public void setDefvalue(String defvalue) {
        this.defvalue = defvalue == null ? null : defvalue.trim();
    }

    public String getLimited() {
        return limited;
    }

    public void setLimited(String limited) {
        this.limited = limited == null ? null : limited.trim();
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType == null ? null : tableType.trim();
    }

	public String getIsIndex() {
		return isIndex;
	}

	public void setIsIndex(String isIndex) {
		this.isIndex = isIndex;
	}
}