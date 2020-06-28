package com.sunyard.ars.risk.bean.arms;


public class ModelFieldResulet {
    private Integer rowno;
    private Integer fieldId;
    private String name;
    private String chName;
    private Integer isfind;
    private Integer isimportant;
    private Integer isdropdown;
    // 关联项id
    private Integer relateId;
    // 格式化
    private Integer format;
    // 其字段的类型//0:整型1:数值型2:字符型
    private Integer type;
    // 0:系统字段，1:展现字段
    private Integer fieldType;
    // 用于保存再查询里面的比较符
    private String operator = "";
    // 用于保存再查询里面输入的值;
    private String opValue = "";

    //是否配置有字典
    private Integer  hisDic;
    //字典表字段
    private String dicName;


    private String isShow = "1";//默认1显示  0 不显示
    
    private Integer isTuoMin;
    /**
     * @return the rowno
     */
    public Integer getRowno() {
        return rowno;
    }



    /**
     * @param rowno the rowno to set
     */
    public void setRowno(Integer rowno) {
        this.rowno = rowno;
    }



    /**
     * @return the fieldId
     */
    public Integer getFieldId() {
        return fieldId;
    }



    /**
     * @param fieldId the fieldId to set
     */
    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }



    /**
     * @return the name
     */
    public String getName() {
        return name;
    }



    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }



    /**
     * @return the chName
     */
    public String getChName() {
        return chName;
    }



    /**
     * @param chName the chName to set
     */
    public void setChName(String chName) {
        this.chName = chName;
    }



    /**
     * @return the isfind
     */
    public Integer getIsfind() {
        return isfind;
    }



    /**
     * @param isfind the isfind to set
     */
    public void setIsfind(Integer isfind) {
        this.isfind = isfind;
    }



    /**
     * @return the isimportant
     */
    public Integer getIsimportant() {
        return isimportant;
    }



    /**
     * @param isimportant the isimportant to set
     */
    public void setIsimportant(Integer isimportant) {
        this.isimportant = isimportant;
    }



    /**
     * @return the isdropdown
     */
    public Integer getIsdropdown() {
        return isdropdown;
    }



    /**
     * @param isdropdown the isdropdown to set
     */
    public void setIsdropdown(Integer isdropdown) {
        this.isdropdown = isdropdown;
    }



    /**
     * @return the relateId
     */
    public Integer getRelateId() {
        return relateId;
    }



    /**
     * @param relateId the relateId to set
     */
    public void setRelateId(Integer relateId) {
        this.relateId = relateId;
    }



    /**
     * @return the format
     */
    public Integer getFormat() {
        return format;
    }



    /**
     * @param format the format to set
     */
    public void setFormat(Integer format) {
        this.format = format;
    }



    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }



    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }




    /**
     * @return the fieldType
     */
    public Integer getFieldType() {
        return fieldType;
    }



    /**
     * @param fieldType the fieldType to set
     */
    public void setFieldType(Integer fieldType) {
        this.fieldType = fieldType;
    }



    /**
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }



    /**
     * @param operator the operator to set
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * @return the opValue
     */
    public String getOpValue() {
        return opValue;
    }



    /**
     * @param opValue the opValue to set
     */
    public void setOpValue(String opValue) {
        this.opValue = opValue;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getIsShow() {
        return isShow;
    }

    public Integer getHisDic() {
        return hisDic;
    }

    public void setHisDic(Integer hisDic) {
        this.hisDic = hisDic;
    }

    public String getDicName() {
        return dicName;
    }

    public void setDicName(String dicName) {
        this.dicName = dicName;
    }

    public Integer getIsTuoMin() {
        return isTuoMin;
    }


    public void setIsTuoMin(Integer isTuoMin) {
        this.isTuoMin = isTuoMin;
    }
}
