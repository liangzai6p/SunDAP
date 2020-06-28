package com.sunyard.ars.system.bean.sc;

import java.math.BigDecimal;

public class FieldComp {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SM_FIELD_COMP_TB.OUT_SOURCE_ID
     *
     * @mbggenerated
     */
    private BigDecimal outSourceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SM_FIELD_COMP_TB.OUTER_FIELD
     *
     * @mbggenerated
     */
    private String outerField;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SM_FIELD_COMP_TB.IS_KEY
     *
     * @mbggenerated
     */
    private String isKey;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SM_FIELD_COMP_TB.LOCAL_FIELD
     *
     * @mbggenerated
     */
    private String localField;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SM_FIELD_COMP_TB.OUT_SOURCE_ID
     *
     * @return the value of SM_FIELD_COMP_TB.OUT_SOURCE_ID
     *
     * @mbggenerated
     */
    public BigDecimal getOutSourceId() {
        return outSourceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SM_FIELD_COMP_TB.OUT_SOURCE_ID
     *
     * @param outSourceId the value for SM_FIELD_COMP_TB.OUT_SOURCE_ID
     *
     * @mbggenerated
     */
    public void setOutSourceId(BigDecimal outSourceId) {
        this.outSourceId = outSourceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SM_FIELD_COMP_TB.OUTER_FIELD
     *
     * @return the value of SM_FIELD_COMP_TB.OUTER_FIELD
     *
     * @mbggenerated
     */
    public String getOuterField() {
        return outerField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SM_FIELD_COMP_TB.OUTER_FIELD
     *
     * @param outerField the value for SM_FIELD_COMP_TB.OUTER_FIELD
     *
     * @mbggenerated
     */
    public void setOuterField(String outerField) {
        this.outerField = outerField == null ? null : outerField.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SM_FIELD_COMP_TB.IS_KEY
     *
     * @return the value of SM_FIELD_COMP_TB.IS_KEY
     *
     * @mbggenerated
     */
    public String getIsKey() {
        return isKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SM_FIELD_COMP_TB.IS_KEY
     *
     * @param isKey the value for SM_FIELD_COMP_TB.IS_KEY
     *
     * @mbggenerated
     */
    public void setIsKey(String isKey) {
        this.isKey = isKey == null ? null : isKey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SM_FIELD_COMP_TB.LOCAL_FIELD
     *
     * @return the value of SM_FIELD_COMP_TB.LOCAL_FIELD
     *
     * @mbggenerated
     */
    public String getLocalField() {
        return localField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SM_FIELD_COMP_TB.LOCAL_FIELD
     *
     * @param localField the value for SM_FIELD_COMP_TB.LOCAL_FIELD
     *
     * @mbggenerated
     */
    public void setLocalField(String localField) {
        this.localField = localField == null ? null : localField.trim();
    }
}