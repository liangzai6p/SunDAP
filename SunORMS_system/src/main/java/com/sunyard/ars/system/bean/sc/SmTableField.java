package com.sunyard.ars.system.bean.sc;

public class SmTableField {


    // Fields

    private Integer tableId;
    private String fieldName;
    private String FIsPk;
    private String FIsIndex;
    private String indexType;
    private String location;


    // Property accessors
    public Integer getTableId() {
        return this.tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFIsPk() {
        return this.FIsPk;
    }

    public void setFIsPk(String FIsPk) {
        this.FIsPk = FIsPk;
    }

    public String getFIsIndex() {
        return this.FIsIndex;
    }

    public void setFIsIndex(String FIsIndex) {
        this.FIsIndex = FIsIndex;
    }

    public String getIndexType() {
        return this.indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}
