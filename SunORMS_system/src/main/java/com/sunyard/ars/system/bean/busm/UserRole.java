package com.sunyard.ars.system.bean.busm;

public class UserRole {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SUNARS.SM_USER_ROLE_TB.USER_NO
     *
     * @mbggenerated
     */
    private String userNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SUNARS.SM_USER_ROLE_TB.ROLE_NO
     *
     * @mbggenerated
     */
    private String roleNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SUNARS.SM_USER_ROLE_TB.IS_OPEN
     *
     * @mbggenerated
     */
    private String isOpen;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SUNARS.SM_USER_ROLE_TB.BANK_NO
     *
     * @mbggenerated
     */
    private String bankNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SUNARS.SM_USER_ROLE_TB.SYSTEM_NO
     *
     * @mbggenerated
     */
    private String systemNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SUNARS.SM_USER_ROLE_TB.PROJECT_NO
     *
     * @mbggenerated
     */
    private String projectNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SUNARS.SM_USER_ROLE_TB.ORGAN_NO
     *
     * @mbggenerated
     */
    private String organNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SUNARS.SM_USER_ROLE_TB.IS_LOCK
     *
     * @mbggenerated
     */
    private String isLock;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column SUNARS.SM_USER_ROLE_TB.LAST_MODI_DATE
     *
     * @mbggenerated
     */
    private String lastModiDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SUNARS.SM_USER_ROLE_TB.USER_NO
     *
     * @return the value of SUNARS.SM_USER_ROLE_TB.USER_NO
     *
     * @mbggenerated
     */
    /**
     * ?????????????????????
     */
    private String isTransmit;
    
    private String roleName;//?????????
    
    public String getIsTransmit() {
		return isTransmit;
	}

	public void setIsTransmit(String isTransmit) {
		this.isTransmit = isTransmit;
	}

	public String getUserNo() {
        return userNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SUNARS.SM_USER_ROLE_TB.USER_NO
     *
     * @param userNo the value for SUNARS.SM_USER_ROLE_TB.USER_NO
     *
     * @mbggenerated
     */
    public void setUserNo(String userNo) {
        this.userNo = userNo == null ? null : userNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SUNARS.SM_USER_ROLE_TB.ROLE_NO
     *
     * @return the value of SUNARS.SM_USER_ROLE_TB.ROLE_NO
     *
     * @mbggenerated
     */
    public String getRoleNo() {
        return roleNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SUNARS.SM_USER_ROLE_TB.ROLE_NO
     *
     * @param roleNo the value for SUNARS.SM_USER_ROLE_TB.ROLE_NO
     *
     * @mbggenerated
     */
    public void setRoleNo(String roleNo) {
        this.roleNo = roleNo == null ? null : roleNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SUNARS.SM_USER_ROLE_TB.IS_OPEN
     *
     * @return the value of SUNARS.SM_USER_ROLE_TB.IS_OPEN
     *
     * @mbggenerated
     */
    public String getIsOpen() {
        return isOpen;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SUNARS.SM_USER_ROLE_TB.IS_OPEN
     *
     * @param isOpen the value for SUNARS.SM_USER_ROLE_TB.IS_OPEN
     *
     * @mbggenerated
     */
    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen == null ? null : isOpen.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SUNARS.SM_USER_ROLE_TB.BANK_NO
     *
     * @return the value of SUNARS.SM_USER_ROLE_TB.BANK_NO
     *
     * @mbggenerated
     */
    public String getBankNo() {
        return bankNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SUNARS.SM_USER_ROLE_TB.BANK_NO
     *
     * @param bankNo the value for SUNARS.SM_USER_ROLE_TB.BANK_NO
     *
     * @mbggenerated
     */
    public void setBankNo(String bankNo) {
        this.bankNo = bankNo == null ? null : bankNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SUNARS.SM_USER_ROLE_TB.SYSTEM_NO
     *
     * @return the value of SUNARS.SM_USER_ROLE_TB.SYSTEM_NO
     *
     * @mbggenerated
     */
    public String getSystemNo() {
        return systemNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SUNARS.SM_USER_ROLE_TB.SYSTEM_NO
     *
     * @param systemNo the value for SUNARS.SM_USER_ROLE_TB.SYSTEM_NO
     *
     * @mbggenerated
     */
    public void setSystemNo(String systemNo) {
        this.systemNo = systemNo == null ? null : systemNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SUNARS.SM_USER_ROLE_TB.PROJECT_NO
     *
     * @return the value of SUNARS.SM_USER_ROLE_TB.PROJECT_NO
     *
     * @mbggenerated
     */
    public String getProjectNo() {
        return projectNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SUNARS.SM_USER_ROLE_TB.PROJECT_NO
     *
     * @param projectNo the value for SUNARS.SM_USER_ROLE_TB.PROJECT_NO
     *
     * @mbggenerated
     */
    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo == null ? null : projectNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SUNARS.SM_USER_ROLE_TB.ORGAN_NO
     *
     * @return the value of SUNARS.SM_USER_ROLE_TB.ORGAN_NO
     *
     * @mbggenerated
     */
    public String getOrganNo() {
        return organNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SUNARS.SM_USER_ROLE_TB.ORGAN_NO
     *
     * @param organNo the value for SUNARS.SM_USER_ROLE_TB.ORGAN_NO
     *
     * @mbggenerated
     */
    public void setOrganNo(String organNo) {
        this.organNo = organNo == null ? null : organNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SUNARS.SM_USER_ROLE_TB.IS_LOCK
     *
     * @return the value of SUNARS.SM_USER_ROLE_TB.IS_LOCK
     *
     * @mbggenerated
     */
    public String getIsLock() {
        return isLock;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SUNARS.SM_USER_ROLE_TB.IS_LOCK
     *
     * @param isLock the value for SUNARS.SM_USER_ROLE_TB.IS_LOCK
     *
     * @mbggenerated
     */
    public void setIsLock(String isLock) {
        this.isLock = isLock == null ? null : isLock.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column SUNARS.SM_USER_ROLE_TB.LAST_MODI_DATE
     *
     * @return the value of SUNARS.SM_USER_ROLE_TB.LAST_MODI_DATE
     *
     * @mbggenerated
     */
    public String getLastModiDate() {
        return lastModiDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column SUNARS.SM_USER_ROLE_TB.LAST_MODI_DATE
     *
     * @param lastModiDate the value for SUNARS.SM_USER_ROLE_TB.LAST_MODI_DATE
     *
     * @mbggenerated
     */
    public void setLastModiDate(String lastModiDate) {
        this.lastModiDate = lastModiDate == null ? null : lastModiDate.trim();
    }

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}