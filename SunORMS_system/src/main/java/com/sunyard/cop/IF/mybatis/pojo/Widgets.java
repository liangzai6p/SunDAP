package com.sunyard.cop.IF.mybatis.pojo;

import java.io.Serializable;

/**
 * 组件对象
 * @author zxx
 */
public class Widgets implements Serializable {
	
	private static final long serialVersionUID = 6718014174195524933L;

	private String widgetId;

    private String parentWidget;

    private String widgetName;
    
    private String widget;
    
    private String bankNo;

    private String systemNo;

    private String projectNo;

	public String getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}

	public String getParentWidget() {
		return parentWidget;
	}

	public void setParentWidget(String parentWidget) {
		this.parentWidget = parentWidget;
	}

	public String getWidgetName() {
		return widgetName;
	}

	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
	}

	public String getWidget() {
		return widget;
	}

	public void setWidget(String widget) {
		this.widget = widget;
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

}
