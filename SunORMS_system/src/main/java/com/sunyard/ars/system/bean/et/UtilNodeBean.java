package com.sunyard.ars.system.bean.et;

import java.util.List;

public class UtilNodeBean implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	private String formType; //	通知书类型	form_type	varchar(10)	
	private String formName; //通知书名称
	private String isForSearch; //	是否列入查询中的处理单类型选择
	private String searchIscenter;//search_iscenter="no" 是否只有中心的人才能查询，默认no
	private String isForAdd;  // 是否列入填写处理单的处理单类型选择
	private String addPurview;//是否有权限填写这种类型的处理单
	private String searchPurview;//是否有权限查询这种类型的处理单
	private String nodeNo; //	流程节点号	node_no	varchar(32)			
	private String nodeName; //	流程节点名称	node_name	varchar(50)		
	private String nodeValue;
	private String nodeText; 
	private String stateText;      //状态描述
	private String nodeDescription; //	流程节点描述	node_description	varchar(500)			
	private List<UtilNodeActionBean> nodeActions; //	节点动作	node_action	varchar(300)			
	private String nodeLevel; //	节点级别	node_level	varchar(10)			
	private String createDate; //	添加日期	create_date	varchar(20)			
	private String formStateText; //	所在节点处理单业务状态描述	form_state_text	varchar(50)			
	private String isStoreHistory; //	本节点是否要保留原处理意见	is_store_history	varchar(10)			
//	private String isShowChoice; //	本节点是否要显示流程选择	is_show_choice	varchar(10)			
	private String onlyShowLastprocess;// <only_show_lastprocess>no</only_show_lastprocess>  //是否只显示最后一个过程处理信息
	private String labelProcess; //	处理过程标签	label_deal_result	varchar(30)			
	private String purview; //	权限定义
	private String isShowActionModel; //是否显示节点动作处理模块, 默认为“yes-显示, no-不显示”，当没有<action>节点时，则赋值为“no”
	
	
	
	public String getOnlyShowLastprocess() {
		return onlyShowLastprocess;
	}
	public void setOnlyShowLastprocess(String onlyShowLastprocess) {
		this.onlyShowLastprocess = onlyShowLastprocess;
	}
	public String getStateText() {
		return stateText;
	}
	public void setStateText(String stateText) {
		this.stateText = stateText;
	}
	public String getNodeText() {
		return nodeText;
	}
	public void setNodeText(String nodeText) {
		this.nodeText = nodeText;
	}
	public String getFormType() {
		return formType;
	}
	public void setFormType(String formType) {
		this.formType = formType;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
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
	public String getSearchIscenter() {
		return searchIscenter;
	}
	public void setSearchIscenter(String searchIscenter) {
		this.searchIscenter = searchIscenter;
	}
	public String getNodeValue() {
		return nodeValue;
	}
	public void setNodeValue(String nodeValue) {
		this.nodeValue = nodeValue;
	}
	public String getNodeDescription() {
		return nodeDescription;
	}
	public void setNodeDescription(String nodeDescription) {
		this.nodeDescription = nodeDescription;
	}
	public List getNodeActions() {
		return nodeActions;
	}
	public void setNodeActions(List nodeActions) {
		this.nodeActions = nodeActions;
	}
	public String getNodeLevel() {
		return nodeLevel;
	}
	public void setNodeLevel(String nodeLevel) {
		this.nodeLevel = nodeLevel;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
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
//	public String getIsShowChoice() {
//		return isShowChoice;
//	}
//	public void setIsShowChoice(String isShowChoice) {
//		this.isShowChoice = isShowChoice;
//	}
	public String getIsForSearch() {
		return isForSearch;
	}
	public void setIsForSearch(String isForSearch) {
		this.isForSearch = isForSearch;
	}
	public String getLabelProcess() {
		return labelProcess;
	}
	public void setLabelProcess(String labelProcess) {
		this.labelProcess = labelProcess;
	}
	public String getIsForAdd() {
		return isForAdd;
	}
	public void setIsForAdd(String isForAdd) {
		this.isForAdd = isForAdd;
	}
	public String getPurview() {
		return purview;
	}
	public void setPurview(String purview) {
		this.purview = purview;
	}
	public String getIsShowActionModel() {
		return isShowActionModel;
	}
	public void setIsShowActionModel(String isShowActionModel) {
		this.isShowActionModel = isShowActionModel;
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
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	
	
	
}
