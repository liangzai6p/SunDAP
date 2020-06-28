/**
 * 
 */
package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

/**
 * 
 * @author YZ
 * 2017年3月21日  上午10:15:17
 */
public class User implements Serializable {
	
	private static final long serialVersionUID = 152748028277541160L;

	private String userNo;

    private String password;

    private String organNo;

    private String userName;

    private String userEnable;

    private String loginMode;

    private String loginState;

    private String lastModiDate;

    private String undersigned;
    
    private String tellerlvl;
    
    private String userStatus;
    
    private String lastLoginTime;
    
    private String lastLogoutTime;
    
    private String terminalIp;
    
    private String terminalMac;
    
    private String loginPCServer;
    
    private String loginMobileServer;
    
	private String bankNo;
    
    private String systemNo;

    private String projectNo;
    
    private String loginTime;
    
    private String loginType;
    
    private String loginTerminal;
    
    private String roleNo;

    private  String errorCount;

    private String singleLogin;

    public User() {
		
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOrganNo() {
		return organNo;
	}

	public void setOrganNo(String organNo) {
		this.organNo = organNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEnable() {
		return userEnable;
	}

	public void setUserEnable(String userEnable) {
		this.userEnable = userEnable;
	}

	public String getLoginMode() {
		return loginMode;
	}

	public void setLoginMode(String loginMode) {
		this.loginMode = loginMode;
	}

	public String getLoginState() {
		return loginState;
	}

	public void setLoginState(String loginState) {
		this.loginState = loginState;
	}

	public String getLastModiDate() {
		return lastModiDate;
	}

	public void setLastModiDate(String lastModiDate) {
		this.lastModiDate = lastModiDate;
	}

	public String getUndersigned() {
		return undersigned;
	}

	public void setUndersigned(String undersigned) {
		this.undersigned = undersigned;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getSystemNo() {
		return systemNo;
	}

	public void setSystemNo(String systemNo) {
		this.systemNo = systemNo;
	}

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginTerminal() {
		return loginTerminal;
	}

	public void setLoginTerminal(String loginTerminal) {
		this.loginTerminal = loginTerminal;
	}
	
	public String getRoleNo() {
		return roleNo;
	}

	public void setRoleNo(String roleNo) {
		this.roleNo = roleNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTellerlvl() {
		return tellerlvl;
	}

	public void setTellerlvl(String tellerlvl) {
		this.tellerlvl = tellerlvl;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLogoutTime() {
		return lastLogoutTime;
	}

	public void setLastLogoutTime(String lastLogoutTime) {
		this.lastLogoutTime = lastLogoutTime;
	}

	public String getTerminalIp() {
		return terminalIp;
	}

	public void setTerminalIp(String terminalIp) {
		this.terminalIp = terminalIp;
	}

	public String getTerminalMac() {
		return terminalMac;
	}

	public void setTerminalMac(String terminalMac) {
		this.terminalMac = terminalMac;
	}

	public String getLoginPCServer() {
		return loginPCServer;
	}

	public void setLoginPCServer(String loginPCServer) {
		this.loginPCServer = loginPCServer;
	}

	public String getLoginMobileServer() {
		return loginMobileServer;
	}

	public void setLoginMobileServer(String loginMobileServer) {
		this.loginMobileServer = loginMobileServer;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

    public String getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(String errorCount) {
        this.errorCount = errorCount;
    }

    public String getSingleLogin() {
        return singleLogin;
    }

    public void setSingleLogin(String singleLogin) {
        this.singleLogin = singleLogin;
    }


}
