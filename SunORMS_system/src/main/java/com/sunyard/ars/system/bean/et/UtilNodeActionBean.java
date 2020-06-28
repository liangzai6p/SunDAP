package com.sunyard.ars.system.bean.et;

public class UtilNodeActionBean implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//	<action toNodeNo="89" text="同意降级" is_selected="yes" >				
	private String toNodeNo;
	private String value;     //暂时没有用到
	private String actionText;
	private String actionMethod;
	private String isSelected;  //是否被选中
	private String purview;  //权限
	private String onclickMethod; //点击的方法 
	private String target;    //跳转的页面
	private String processShowLevel="all"; //显示级别，处理流程信息在展现时的级别配置；中心 还是 非中心 ,默认全部
	private String isSeanLeader; //是否已经审阅过
	private String signFlag;//审阅标志
	private String deptMatch;//hjf 2013-03-14 部门匹配 ，目前抄送领导中使用
	
	public String getToNodeNo() {
		return toNodeNo;
	}
	public void setToNodeNo(String toNodeNo) {
		this.toNodeNo = toNodeNo;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getActionText() {
		return actionText;
	}
	public void setActionText(String actionText) {
		this.actionText = actionText;
	}
	public String getActionMethod() {
		return actionMethod;
	}
	public void setActionMethod(String actionMethod) {
		if(actionMethod!=null){
			this.actionMethod = actionMethod.replaceAll(";and;","&");
		}
	}
	public String getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}
	public String getPurview() {
		return purview;
	}
	public void setPurview(String purview) {
		this.purview = purview;
	}
	public String getOnclickMethod() {
		return onclickMethod;
	}
	public void setOnclickMethod(String onclickMethod) {
		this.onclickMethod = onclickMethod;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getProcessShowLevel() {
		return processShowLevel;
	}
	public void setProcessShowLevel(String processShowLevel) {
		this.processShowLevel = processShowLevel;
	}
	public String getIsSeanLeader() {
		return isSeanLeader;
	}
	public void setIsSeanLeader(String isSeanLeader) {
		this.isSeanLeader = isSeanLeader;
	}
	public String getSignFlag() {
		return signFlag;
	}
	public void setSignFlag(String signFlag) {
		this.signFlag = signFlag;
	}
	public String getDeptMatch() {
		return deptMatch;
	}
	public void setDeptMatch(String deptMatch) {
		this.deptMatch = deptMatch;
	}
	
	
	
}
