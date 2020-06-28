/**
 * 
 */
package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

/**
 * 
 * @author YZ
 * 2017年3月21日  下午1:30:49
 */
public class Menu implements Serializable {
	
	private static final long serialVersionUID = -7840420350797840965L;

	private String menuId;
	
	private String menuName;
	
	private String parentId;
	
	private String menuDesc;

    private String menuUrl;

    private String menuLevel;
    
    private String isOpen;
    
    private String lastModiDate;
    
    private String editEnable;
    
    private String systemNo;

    private String bankNo;

    private String projectNo;
    
    private String menuOrder;
    
    private String menuBelong;
    
    private String menuPage;
    
    private String isParent;
    
    private String addBtn;
    
    private String delBtn;
    
    private String isLock;
    
    private String menuClass;
    
    private String menuType;
    
    private String menuAttr;
    
    private String homeShow;

	/**
	 * 
	 */
	public Menu() {
		// TODO Auto-generated constructor stub
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getMenuDesc() {
		return menuDesc;
	}

	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(String menuLevel) {
		this.menuLevel = menuLevel;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	public String getLastModiDate() {
		return lastModiDate;
	}

	public void setLastModiDate(String lastModiDate) {
		this.lastModiDate = lastModiDate;
	}

	public String getEditEnable() {
		return editEnable;
	}

	public void setEditEnable(String editEnable) {
		this.editEnable = editEnable;
	}

	public String getSystemNo() {
		return systemNo;
	}

	public void setSystemNo(String systemNo) {
		this.systemNo = systemNo;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	public String getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(String menuOrder) {
		this.menuOrder = menuOrder;
	}

	public String getMenuBelong() {
		return menuBelong;
	}

	public void setMenuBelong(String menuBelong) {
		this.menuBelong = menuBelong;
	}

	public String getMenuPage() {
		return menuPage;
	}

	public void setMenuPage(String menuPage) {
		this.menuPage = menuPage;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public String getAddBtn() {
		return addBtn;
	}

	public void setAddBtn(String addBtn) {
		this.addBtn = addBtn;
	}

	public String getDelBtn() {
		return delBtn;
	}

	public void setDelBtn(String delBtn) {
		this.delBtn = delBtn;
	}

	public String getIsLock() {
		return isLock;
	}

	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}

	public String getMenuClass() {
		return menuClass;
	}

	public void setMenuClass(String menuClass) {
		this.menuClass = menuClass;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getMenuAttr() {
		return menuAttr;
	}

	public void setMenuAttr(String menuAttr) {
		this.menuAttr = menuAttr;
	}

	public String getHomeShow() {
		return homeShow;
	}

	public void setHomeShow(String homeShow) {
		this.homeShow = homeShow;
	}
	
}
