package com.sunyard.ars.system.bean.busm;

import java.math.BigDecimal;

public class HomePage {
    
    private BigDecimal id;
    
    private String userNo;
    
    private String pageNo;
    
    private String pageState;
    
    private String pageWidth;
    
    private String pageHeight;
    
    private String menuClass;

	public BigDecimal getId() {
        return id;
    }

    
    public void setId(BigDecimal id) {
        this.id = id;
    }

    
    public String getUserNo() {
        return userNo;
    }

    
    public void setUserNo(String userNo) {
        this.userNo = userNo == null ? null : userNo.trim();
    }

    
    public String getPageNo() {
        return pageNo;
    }

    
    public void setPageNo(String pageNo) {
        this.pageNo = pageNo == null ? null : pageNo.trim();
    }

    
    public String getPageState() {
        return pageState;
    }

    
    public void setPageState(String pageState) {
        this.pageState = pageState == null ? null : pageState.trim();
    }
    
    public String getPageWidth() {
  		return pageWidth;
  	}


  	public void setPageWidth(String pageWidth) {
  		this.pageWidth = pageWidth;
  	}


  	public String getPageHeight() {
  		return pageHeight;
  	}


  	public void setPageHeight(String pageHeight) {
  		this.pageHeight = pageHeight;
  	}


	public String getMenuClass() {
		return menuClass;
	}


	public void setMenuClass(String menuClass) {
		this.menuClass = menuClass;
	}
  	
  	
}