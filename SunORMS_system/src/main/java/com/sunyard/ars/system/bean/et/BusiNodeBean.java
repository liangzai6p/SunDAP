package com.sunyard.ars.system.bean.et;

import java.util.List;

public class BusiNodeBean {
    
	private String text;			//节点描述，显示在左边菜单中
	private String index;			//节点索引
	private String isCenterUser = "no";  //是否是中心用户
	private String purview;			//权限字符
	private String isTaskList="no"; // 是否是任务列表的节点
	private int taskCount = 0;		//任务数，
	private String purviewType;		//权限字符的类型（用固定的常量‘AMEND’）
	private String target;			//暂不用到，保留
	private String methodURL;		//方法
	private List<UtilNodeBean> utilNodes;			//该业务节点所包含的单元节点集合
	private String isShowSecondSearch = "no"; //是否显示再查询页面.值yes/no.  默认为no.
	private List isSSSFormTypeList;  //再查询中，处理单类型的配置。因为每个节点，所包含的类型不同。
	private String needOrganCondition = "yes"; //显示任务列表时，是否包含网点号这个条件。默认为“yes”
	private String needActiveDateCondition = "no"; //显示任务列表时，是否包含有效期这个条件。默认为“no”。用于“业务情况说明书”的任务列表查询时。
	private String isCheckerMatch = "no"; //显示任务列表时，是否谁登记谁查看。 is_checker_match， yes-是  no-否
	private String isTellerMatch = "no"; //显示任务列表时，是否柜员查看自己的任务。 is_teller_match， yes-是  no-否
	private String isLeaderMatch = "no"; //显示任务列表时，是否领导查看自己的任务列表。is_leader_match， yes-是  no-否
	private String isCheckerNotUserNoMatch = "no"; //显示任务列表时，查看不是自己登记的任务。 is_checker_not_userno_match， yes-是  no-否
	private String isLeaderNotSeanMatch = "no"; //显示任务列表时，是否领导查看自己的任务列表 且没有查看过。is_leader_not_sean_match， yes-是  no-否
	private String isLeaderHasSeanMatch = "no"; //显示任务列表时，是否领导查看自己的任务列表 且已经查看过。is_leader_has_sean_match， yes-是  no-否
	//添加处理单由网点整改,预警员,中心主管的指定人处理 modify by miaoky
	private String isAppointNetUser="no";//显示任务列表时，对直接下发到网点的处理单是否指定网点处理人。  yes-是  no-否
	private String isAppointNetArms="no";//显示任务列表时，处理单是否指定监察员处理人。  yes-是  no-否
	private String isAppointNetManager="no";//显示任务列表时，处理单是否指定中心主管处理人。yes-是  no-否
	//20160921 lj add 再监督标识
	private String corrFlag;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getIsCenterUser() {
		return isCenterUser;
	}
	public void setIsCenterUser(String isCenterUser) {
		this.isCenterUser = isCenterUser;
	}
	public String getPurview() {
		return purview;
	}
	public void setPurview(String purview) {
		this.purview = purview;
	}
	public String getIsTaskList() {
		return isTaskList;
	}
	public void setIsTaskList(String isTaskList) {
		this.isTaskList = isTaskList;
	}
	public int getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}
	public String getPurviewType() {
		return purviewType;
	}
	public void setPurviewType(String purviewType) {
		this.purviewType = purviewType;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getMethodURL() {
		return methodURL;
	}
	public void setMethodURL(String methodURL) {
		this.methodURL = methodURL;
	}
	public List getUtilNodes() {
		return utilNodes;
	}
	public void setUtilNodes(List utilNodes) {
		this.utilNodes = utilNodes;
	}
	public String getIsShowSecondSearch() {
		return isShowSecondSearch;
	}
	public void setIsShowSecondSearch(String isShowSecondSearch) {
		this.isShowSecondSearch = isShowSecondSearch;
	}
	public List getIsSSSFormTypeList() {
		return isSSSFormTypeList;
	}
	public void setIsSSSFormTypeList(List isSSSFormTypeList) {
		this.isSSSFormTypeList = isSSSFormTypeList;
	}
	public String getNeedOrganCondition() {
		return needOrganCondition;
	}
	public void setNeedOrganCondition(String needOrganCondition) {
		this.needOrganCondition = needOrganCondition;
	}
	public String getNeedActiveDateCondition() {
		return needActiveDateCondition;
	}
	public void setNeedActiveDateCondition(String needActiveDateCondition) {
		this.needActiveDateCondition = needActiveDateCondition;
	}
	public String getIsCheckerMatch() {
		return isCheckerMatch;
	}
	public void setIsCheckerMatch(String isCheckerMatch) {
		this.isCheckerMatch = isCheckerMatch;
	}
	public String getIsTellerMatch() {
		return isTellerMatch;
	}
	public void setIsTellerMatch(String isTellerMatch) {
		this.isTellerMatch = isTellerMatch;
	}
	public String getIsCheckerNotUserNoMatch() {
		return isCheckerNotUserNoMatch;
	}
	public void setIsCheckerNotUserNoMatch(String isCheckerNotUserNoMatch) {
		this.isCheckerNotUserNoMatch = isCheckerNotUserNoMatch;
	}
	public String getIsLeaderMatch() {
		return isLeaderMatch;
	}
	public void setIsLeaderMatch(String isLeaderMatch) {
		this.isLeaderMatch = isLeaderMatch;
	}
	public String getIsLeaderNotSeanMatch() {
		return isLeaderNotSeanMatch;
	}
	public void setIsLeaderNotSeanMatch(String isLeaderNotSeanMatch) {
		this.isLeaderNotSeanMatch = isLeaderNotSeanMatch;
	}
	public String getIsLeaderHasSeanMatch() {
		return isLeaderHasSeanMatch;
	}
	public void setIsLeaderHasSeanMatch(String isLeaderHasSeanMatch) {
		this.isLeaderHasSeanMatch = isLeaderHasSeanMatch;
	}
	public String getIsAppointNetUser() {
		return isAppointNetUser;
	}
	public void setIsAppointNetUser(String isAppointNetUser) {
		this.isAppointNetUser = isAppointNetUser;
	}
	public String getIsAppointNetArms() {
		return isAppointNetArms;
	}
	public void setIsAppointNetArms(String isAppointNetArms) {
		this.isAppointNetArms = isAppointNetArms;
	}
	public String getIsAppointNetManager() {
		return isAppointNetManager;
	}
	public void setIsAppointNetManager(String isAppointNetManager) {
		this.isAppointNetManager = isAppointNetManager;
	}
	public String getCorrFlag() {
		return corrFlag;
	}
	public void setCorrFlag(String corrFlag) {
		this.corrFlag = corrFlag;
	}
}