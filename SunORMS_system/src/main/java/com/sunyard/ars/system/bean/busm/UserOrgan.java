package com.sunyard.ars.system.bean.busm;

public class UserOrgan {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SM_USER_ORGAN_TB.USER_NO
     *
     * @mbggenerated
     */
    private String userNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SM_USER_ORGAN_TB.ORGAN_NO
     *
     * @mbggenerated
     */
    private String organNo;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SM_USER_ORGAN_TB.USER_NO
     *
     * @return the value of SM_USER_ORGAN_TB.USER_NO
     *
     * @mbggenerated
     */
    public String getUserNo() {
        return userNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SM_USER_ORGAN_TB.USER_NO
     *
     * @param userNo the value for SM_USER_ORGAN_TB.USER_NO
     *
     * @mbggenerated
     */
    public void setUserNo(String userNo) {
        this.userNo = userNo == null ? null : userNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SM_USER_ORGAN_TB.ORGAN_NO
     *
     * @return the value of SM_USER_ORGAN_TB.ORGAN_NO
     *
     * @mbggenerated
     */
    public String getOrganNo() {
        return organNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SM_USER_ORGAN_TB.ORGAN_NO
     *
     * @param organNo the value for SM_USER_ORGAN_TB.ORGAN_NO
     *
     * @mbggenerated
     */
    public void setOrganNo(String organNo) {
        this.organNo = organNo == null ? null : organNo.trim();
    }
}