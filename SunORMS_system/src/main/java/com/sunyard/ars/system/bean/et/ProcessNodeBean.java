package com.sunyard.ars.system.bean.et;

public class ProcessNodeBean {
    
	private String nodeNo; // 流程节点号 node_no varchar(32)
	private String nodeName; // 流程节点名称 node_name varchar(50)
	private String searchIscenter;// search_iscenter="no" 是否只有中心的人才能查询，默认no
	private String nodeDescription; // 流程节点描述 node_description varchar(500)
	private String formStateText; // 所在节点处理单业务状态描述 form_state_text varchar(50)
	private String isStoreHistory; // 本节点是否要保留原处理意见 is_store_history varchar(10)
	private String isForSearch; // 是否列入查询中的处理单类型选择
	private String labelProcess; // 处理过程标签 label_deal_result varchar(30)
	
	private String isForAdd; // 是否列入填写处理单的处理单类型选择
	private String purview; //
	private String onlyShowLastprocess;// <only_show_lastprocess>no</only_show_lastprocess>
	// //是否只显示最后一个过程处理信息
	private String stateText; // 状态描述
	private String isShowActionModel; // 是否显示节点动作处理模块, 默认为“yes-显示,
	// no-不显示”，当没有<action>节点时，则赋值为“no”
	private String addPurview;// 是否有权限填写这种类型的处理单
	private String searchPurview;// 是否有权限查询这种类型的处理单
	private String roleName; //
	private String flowChart;//流程类型

	public String getIsForSearch() {
		return isForSearch;
	}
	public void setIsForSearch(String isForSearch) {
		this.isForSearch = isForSearch;
	}
	public String getSearchIscenter() {
		return searchIscenter;
	}
	public void setSearchIscenter(String searchIscenter) {
		this.searchIscenter = searchIscenter;
	}
	public String getIsForAdd() {
		return isForAdd;
	}
	public void setIsForAdd(String isForAdd) {
		this.isForAdd = isForAdd;
	}
	public String getAddPurview() {
		return addPurview;
	}
	public void setAddPurview(String addPurview) {
		this.addPurview = addPurview;
	}
	public String getSearchPurview() {
		return searchPurview;
	}
	public void setSearchPurview(String searchPurview) {
		this.searchPurview = searchPurview;
	}
	public String getNodeNo() {
		return nodeNo;
	}
	public void setNodeNo(String nodeNo) {
		this.nodeNo = nodeNo;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getPurview() {
		return purview;
	}
	public void setPurview(String purview) {
		this.purview = purview;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getStateText() {
		return stateText;
	}
	public void setStateText(String stateText) {
		this.stateText = stateText;
	}
	public String getNodeDescription() {
		return nodeDescription;
	}
	public void setNodeDescription(String nodeDescription) {
		this.nodeDescription = nodeDescription;
	}
	public String getFormStateText() {
		return formStateText;
	}
	public void setFormStateText(String formStateText) {
		this.formStateText = formStateText;
	}
	public String getIsStoreHistory() {
		return isStoreHistory;
	}
	public void setIsStoreHistory(String isStoreHistory) {
		this.isStoreHistory = isStoreHistory;
	}
	public String getOnlyShowLastprocess() {
		return onlyShowLastprocess;
	}
	public void setOnlyShowLastprocess(String onlyShowLastprocess) {
		this.onlyShowLastprocess = onlyShowLastprocess;
	}
	public String getLabelProcess() {
		return labelProcess;
	}
	public void setLabelProcess(String labelProcess) {
		this.labelProcess = labelProcess;
	}
	public String getIsShowActionModel() {
		return isShowActionModel;
	}
	public void setIsShowActionModel(String isShowActionModel) {
		this.isShowActionModel = isShowActionModel;
	}
	public String getFlowChart() {
		return flowChart;
	}
	public void setFlowChart(String flowChart) {
		this.flowChart = flowChart;
	}

}