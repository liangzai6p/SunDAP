package com.sunyard.ars.system.bean.et;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.sunyard.aos.common.util.BaseUtil;

public class BusiForm {
    
	private String formId; // 通知书编号
	private String formType; // 通知书类型
	private String operationClass; // 业务种类(在全局变量表中定义)
	private String operationClassText; // 业务种类(在全局变量表中定义)
	private String workDate; // 监督日期
	private String workTime; // 监督时间
	private String xfDate; // 处理单下发日期
	private String xfTime; // 处理单下发时间
	private String netNo; // 网点号
	private String netName; // 网点名称
	private String tellerNo; // 经办柜员号
	private String tellerNo2; // 复核柜员号
	private String tellerNo3; // 授权柜员号
	private String tellerName; // 经办柜员名称
	private String tellerName2; // 复核柜员名称
	private String tellerName3; // 授权柜员名称
	private String operationDate; // 业务日期
	private String accountNo; // 账 号
	private String accountName; // 户 名
	private Double accountMoney; // 金 额
	private String accountMoneyStr; // 金 额
	private String currencyType; // 币 种
	private String voucherNo; // 凭证种类编号
	private String voucherName; // 凭证种类名称
	private String voucherDate; // 凭证日期
	private String subpenaNo; // 传 票 号
	private String flowNo; // 流 水 号
	private String backDate; // 反馈日期
	private String overDays;
	private String slipNo; // 差错编号(差错定义表中)
	private String operationType; // 业务类别(差错定义表中)
	private String slipType; // 差错归属,差错类别(差错定义表中)
	private String slipName; // 差错名称(差错定义表中)
	private String slipLevel; // 差错级别(差错定义表中)
	private String slipLevelText; // 差错级别(差错定义表中)中文描述，一般，较严重，严重
	private String slipNoBak; // 差错编号(差错定义表中)备份降级前的差错类型
	private Double amerceMoney; // 处罚金额(差错定义表中)
	private Double amerceScore;//
	private String detailurl; // 预警单明细的URL
	private String amerceMoneyStr; // 处罚金额
	private String formDescription; // 详细描述
	private String interiorEtNo; // 内部差错人号： interior_et_no varchar(10)
	private String interiorEtName; // 内部差错人名称： interior_et_name varchar(20)
	private String checkerNo; // 监督员号
	private String checkerName; // 监督员名称
	private String checkerNo2; // 重点监督员号
	private String checkerName2; // 重点监督员名称
	private String checkerOrganNo; // 填报机构（监督员所在机构） checker_organ_no
	private String checkerOrganName;// 填报机构（监督员所在机构）
	private String imageIndex; // 图片索引
	private String annexPath; // 业务情况说明书的文件索引
	private String annexName; // 业务情况说明书的文件索引
	private String sourceFlag; // 通知书来源
	private String processNodes; // 走过的流程
	private String nodeNo; // 当前流程节点号
	private String overdueFlag1; // 查看逾期状态
	private String overdueFlag2; // 回复逾期状态
	private String overdueFlag3; // 整改逾期状态
	private String overdueRemark; // 逾期备注
	private String activeFlag; // 可用性状态
	private String interiorFormId; // 内部差错单编号：（外键）
	private String correlativeId; // 关联编号 用于转差错的源单编号记录
	private String degrade; // 降级
	private String activeDate; // 通知单有效期（主要是说明书）
	private String tableName; // 预警系统：预警表名 table_name varchar(20)
	private String entryId; // 预警系统：预警号 entry_id varchar(20)
	private String entryName; // 预警系统：预警名称 entry_name varchar(100)
	private String entryrowId; // 预警系统：预警行内序号 entryrow_id varchar(20)
	private List<Integer> entryrowIds; // 预警系统：预警行内序号 entryrow_id varchar(20)
	private String ishandle; // 在影像查询中，是否归档，值从影像查询中传入
	private String netNoTellerNoInfo; // netNo-TellerNo;netNo-TellerNo;netNo-TellerNo
	private String ccLeader; // 抄送领导查阅
	private String seanLeader; // 领导已查阅
	private String leaderRemark; // 领导指示
	private String isSearch; // 是否为查询，在查询处理单时用到	
	private String brasrulecontext;//预警展现
	private String alarmLevel;//预警等级
	private String riskDetail;//风险点描述
	private String busiNoCt; // 传统业务编号
	private String busiNoCtName;//传统业务名称
	private String dealNo;//处理单上一节点处理人
	private String deal_state;//获得转发状态
	private String procDate;//获得预警提取日期
	// ------------登记触发系统-----------------------------
	private String addFromSYS;
	// ------------查询条件时用到-----------------------------
	private String workDateS;
	private String workDateE;
	private String xfDateS; // 处理单下发日期
	private String xfDateE; // 处理单下发日期
	private String returnDateS;//退回日期
	private String returnDateE;//退回日期
	private String operationDateS;
	private String operationDateE;
	private String activeDateS; // 通知单有效期（主要是说明书）
	private String activeDateE; // 通知单有效期（主要是说明书）
	
	//文件上传需要保存的属性值
	private String fileName;
	private String realName;
	private String filePath;
	private String fileExt;
	private String realBackDate;//20160706 lj add 逾期单据真实反馈日期
	//20160918 lj add 在监督处理
	private String returnDate;//再监督退回日期
	private String corrUserNo;//再监督处理人
	private String corrBackDate;//需要反馈日期
	private String corrDealDate;//需要反馈日期
	private String returnState;//是否已再监督
	private String brasrulecontextJm;//预警展现加密
	private String isBf;//是否补发 1是0或null否
	private String bakeUserNo;//再监督退回人
	private String isSee;//2017.2.6  lh add 是否查阅（1是0，null否）
	private String seeDesc;//2017.2.6  lh add 查阅描述（查阅人，查阅时间）
	private String createTime;//预警信息生成时间，格式如：20180316 11:11:11,取自模型表的create_date字段
	private String proc_date_new;//预警信息生成日期，格式如：20180316，取自模型表的proc_date字段	
	//2018 0314 czd add 差错单办结时 ：“影响账务结果－会计条线”、“影响账务结果－非会计条线” 、“不影响账务结果－会计条线”和“不影响账务结果－非会计条线”。
	private String riskType;
	
	//2018 0314 czd add 驱动因素相关信息
	private String riskSourceNo;		//驱动因素代码
	private String riskSourceType;		//驱动因素类型
	private String riskSourceName;		//驱动因素名称
	private String riskSourceRemak;	//驱动因素备注
	
	//20180326 czd add 逾期处理单 作业机构分行机构辨别
	private String temporary;
	
	//20180709 dadong  分行逾期重新增加了4个字段
	private String parOverDays;     //分行逾期天数
	private String parBackDate;     //分行应处理日期
	private String parDealDate;     //分行实际处理日期
	private String orgDealDate;     //提交到分行的日期
	
	private String approveNo;//风险处置审批员
	private String dealResultF;//第一次处置结果
	private String dealResultL;//最后一次处置结果
	private String dealOrgan;//处置机构
	private String busiNoTx;//业务条线
	private String alarmCreateTime;//警报信息生成日期时间
	private String formState;//警报单当前状态:待处理、待下发、撤销待录入、处置待录入、待处置、退回重核、撤销待审批、处置待审批、已撤销、确认风险办结、无风险办结、确认差错办结、无差错办结
	private String fxFlag;//有无风险标志(办结时写入)
	private String riskOrgan;//风险机构
	private String dealOrganList;//处置机构列表
	
	private String ccOrgan;//抄送机构
	private String ccRole;//抄送角色
	private String modifyType;//办结修改状态
	private String modifyNode;//办结修改节点
	private String ccType;//抄送类型
	private String seanRole;//查看角色
	
	private String qdSlipMoid;//QD_SLIP_MOID  质检差错类型1-规范类、2-较严重、3-严重类、4-案件类

	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	public String getFormType() {
		return formType;
	}
	public void setFormType(String formType) {
		this.formType = formType;
	}
	public String getOperationClass() {
		return operationClass;
	}
	public void setOperationClass(String operationClass) {
		this.operationClass = operationClass;
	}
	public String getOperationClassText() {
		return operationClassText;
	}
	public void setOperationClassText(String operationClassText) {
		this.operationClassText = operationClassText;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public String getWorkTime() {
		return workTime;
	}
	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	public String getXfDate() {
		return xfDate;
	}
	public void setXfDate(String xfDate) {
		this.xfDate = xfDate;
	}
	public String getXfTime() {
		return xfTime;
	}
	public void setXfTime(String xfTime) {
		this.xfTime = xfTime;
	}
	public String getNetNo() {
		return netNo;
	}
	public void setNetNo(String netNo) {
		this.netNo = netNo;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getTellerNo() {
		return tellerNo;
	}
	public void setTellerNo(String tellerNo) {
		this.tellerNo = tellerNo;
	}
	public String getTellerNo2() {
		return tellerNo2;
	}
	public void setTellerNo2(String tellerNo2) {
		this.tellerNo2 = tellerNo2;
	}
	public String getTellerNo3() {
		return tellerNo3;
	}
	public void setTellerNo3(String tellerNo3) {
		this.tellerNo3 = tellerNo3;
	}
	public String getTellerName() {
		return tellerName;
	}
	public void setTellerName(String tellerName) {
		this.tellerName = tellerName;
	}
	public String getTellerName2() {
		return tellerName2;
	}
	public void setTellerName2(String tellerName2) {
		this.tellerName2 = tellerName2;
	}
	public String getTellerName3() {
		return tellerName3;
	}
	public void setTellerName3(String tellerName3) {
		this.tellerName3 = tellerName3;
	}
	public String getOperationDate() {
		return operationDate;
	}
	public void setOperationDate(String operationDate) {
		this.operationDate = operationDate;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public Double getAccountMoney() {
		return accountMoney;
	}
	public void setAccountMoney(Double accountMoney) {
		this.accountMoney = accountMoney;
	}
	public String getAccountMoneyStr() {
		return accountMoneyStr;
	}
	public void setAccountMoneyStr(String accountMoneyStr) {
		this.accountMoneyStr = accountMoneyStr;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getVoucherDate() {
		return voucherDate;
	}
	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}
	public String getSubpenaNo() {
		return subpenaNo;
	}
	public void setSubpenaNo(String subpenaNo) {
		this.subpenaNo = subpenaNo;
	}
	public String getFlowNo() {
		return flowNo;
	}
	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}
	public String getBackDate() {
		return backDate;
	}
	public void setBackDate(String backDate) {
		this.backDate = backDate;
	}
	public String getOverDays() {
		/*if(BaseUtil.isBlank(backDate)){
			return overDays;
		}else{
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			try {
				date = df.parse(backDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long i = (System.currentTimeMillis() - date.getTime())
					/ (1000 * 3600 * 24);
			return new Long(i).toString();
		}*/
		return overDays;
	}
	public void setOverDays(String overDays) {
		this.overDays = overDays;
	}
	public String getSlipNo() {
		return slipNo;
	}
	public void setSlipNo(String slipNo) {
		this.slipNo = slipNo;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getSlipType() {
		return slipType;
	}
	public void setSlipType(String slipType) {
		this.slipType = slipType;
	}
	public String getSlipName() {
		return slipName;
	}
	public void setSlipName(String slipName) {
		this.slipName = slipName;
	}
	public String getSlipLevel() {
		return slipLevel;
	}
	public void setSlipLevel(String slipLevel) {
		this.slipLevel = slipLevel;
	}
	public String getSlipLevelText() {
		return slipLevelText;
	}
	public void setSlipLevelText(String slipLevelText) {
		this.slipLevelText = slipLevelText;
	}
	public String getSlipNoBak() {
		return slipNoBak;
	}
	public void setSlipNoBak(String slipNoBak) {
		this.slipNoBak = slipNoBak;
	}
	public Double getAmerceMoney() {
		return amerceMoney;
	}
	public void setAmerceMoney(Double amerceMoney) {
		this.amerceMoney = amerceMoney;
	}
	public String getDetailURL() {
		return detailurl;
	}
	public void setDetailURL(String detailURL) {
		this.detailurl = detailURL;
	}
	public String getAmerceMoneyStr() {
		return amerceMoneyStr;
	}
	public void setAmerceMoneyStr(String amerceMoneyStr) {
		this.amerceMoneyStr = amerceMoneyStr;
	}
	public String getFormDescription() {
		return formDescription;
	}
	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}
	public String getInteriorEtNo() {
		return interiorEtNo;
	}
	public void setInteriorEtNo(String interiorEtNo) {
		this.interiorEtNo = interiorEtNo;
	}
	public String getInteriorEtName() {
		return interiorEtName;
	}
	public void setInteriorEtName(String interiorEtName) {
		this.interiorEtName = interiorEtName;
	}
	public String getCheckerNo() {
		return checkerNo;
	}
	public void setCheckerNo(String checkerNo) {
		this.checkerNo = checkerNo;
	}
	public String getCheckerName() {
		return checkerName;
	}
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}
	public String getCheckerNo2() {
		return checkerNo2;
	}
	public void setCheckerNo2(String checkerNo2) {
		this.checkerNo2 = checkerNo2;
	}
	public String getCheckerName2() {
		return checkerName2;
	}
	public void setCheckerName2(String checkerName2) {
		this.checkerName2 = checkerName2;
	}
	public String getCheckerOrganNo() {
		return checkerOrganNo;
	}
	public void setCheckerOrganNo(String checkerOrganNo) {
		this.checkerOrganNo = checkerOrganNo;
	}
	public String getCheckerOrganName() {
		return checkerOrganName;
	}
	public void setCheckerOrganName(String checkerOrganName) {
		this.checkerOrganName = checkerOrganName;
	}
	public String getImageIndex() {
		return imageIndex;
	}
	public void setImageIndex(String imageIndex) {
		this.imageIndex = imageIndex;
	}
	public String getAnnexPath() {
		return annexPath;
	}
	public void setAnnexPath(String annexPath) {
		this.annexPath = annexPath;
	}
	public String getAnnexName() {
		return annexName;
	}
	public void setAnnexName(String annexName) {
		this.annexName = annexName;
	}
	public String getSourceFlag() {
		return sourceFlag;
	}
	public void setSourceFlag(String sourceFlag) {
		this.sourceFlag = sourceFlag;
	}
	public String getProcessNodes() {
		return processNodes;
	}
	public void setProcessNodes(String processNodes) {
		this.processNodes = processNodes;
	}
	public String getNodeNo() {
		return nodeNo;
	}
	public void setNodeNo(String nodeNo) {
		this.nodeNo = nodeNo;
	}
	public String getOverdueFlag1() {
		return overdueFlag1;
	}
	public void setOverdueFlag1(String overdueFlag1) {
		this.overdueFlag1 = overdueFlag1;
	}
	public String getOverdueFlag2() {
		return overdueFlag2;
	}
	public void setOverdueFlag2(String overdueFlag2) {
		this.overdueFlag2 = overdueFlag2;
	}
	public String getOverdueFlag3() {
		return overdueFlag3;
	}
	public void setOverdueFlag3(String overdueFlag3) {
		this.overdueFlag3 = overdueFlag3;
	}
	public String getOverdueRemark() {
		return overdueRemark;
	}
	public void setOverdueRemark(String overdueRemark) {
		this.overdueRemark = overdueRemark;
	}
	public String getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
	public String getInteriorFormId() {
		return interiorFormId;
	}
	public void setInteriorFormId(String interiorFormId) {
		this.interiorFormId = interiorFormId;
	}
	public String getCorrelativeId() {
		return correlativeId;
	}
	public void setCorrelativeId(String correlativeId) {
		this.correlativeId = correlativeId;
	}
	public String getDegrade() {
		return degrade;
	}
	public void setDegrade(String degrade) {
		this.degrade = degrade;
	}
	public String getActiveDate() {
		return activeDate;
	}
	public void setActiveDate(String activeDate) {
		this.activeDate = activeDate;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getEntryId() {
		return entryId;
	}
	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}
	public String getEntryName() {
		return entryName;
	}
	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}
	public String getEntryrowId() {
		return entryrowId;
	}
	public void setEntryrowId(String entryrowId) {
		this.entryrowId = entryrowId;
	}
	public String getIshandle() {
		return ishandle;
	}
	public void setIshandle(String ishandle) {
		this.ishandle = ishandle;
	}
	public String getNetNoTellerNoInfo() {
		return netNoTellerNoInfo;
	}
	public void setNetNoTellerNoInfo(String netNoTellerNoInfo) {
		this.netNoTellerNoInfo = netNoTellerNoInfo;
	}
	public String getCcLeader() {
		return ccLeader;
	}
	public void setCcLeader(String ccLeader) {
		this.ccLeader = ccLeader;
	}
	public String getSeanLeader() {
		return seanLeader;
	}
	public void setSeanLeader(String seanLeader) {
		this.seanLeader = seanLeader;
	}
	public String getLeaderRemark() {
		return leaderRemark;
	}
	public void setLeaderRemark(String leaderRemark) {
		this.leaderRemark = leaderRemark;
	}
	public String getIsSearch() {
		return isSearch;
	}
	public void setIsSearch(String isSearch) {
		this.isSearch = isSearch;
	}
	public String getBrasRuleContext() {
		return brasrulecontext;
	}
	public void setBrasRuleContext(String brasRuleContext) {
		this.brasrulecontext = brasRuleContext;
	}
	public String getAlarmLevel() {
		return alarmLevel;
	}
	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}
	public String getRiskDetail() {
		return riskDetail;
	}
	public void setRiskDetail(String riskDetail) {
		this.riskDetail = riskDetail;
	}
	public String getBusiNoCt() {
		return busiNoCt;
	}
	public void setBusiNoCt(String busiNoCt) {
		this.busiNoCt = busiNoCt;
	}
	public String getBusiNoCtName() {
		return busiNoCtName;
	}
	public void setBusiNoCtName(String busiNoCtName) {
		this.busiNoCtName = busiNoCtName;
	}
	public String getDealNo() {
		return dealNo;
	}
	public void setDealNo(String dealNo) {
		this.dealNo = dealNo;
	}
	public String getAddFromSYS() {
		return addFromSYS;
	}
	public void setAddFromSYS(String addFromSYS) {
		this.addFromSYS = addFromSYS;
	}
	public String getWorkDateS() {
		return workDateS;
	}
	public void setWorkDateS(String workDateS) {
		this.workDateS = workDateS;
	}
	public String getWorkDateE() {
		return workDateE;
	}
	public void setWorkDateE(String workDateE) {
		this.workDateE = workDateE;
	}
	public String getXfDateS() {
		return xfDateS;
	}
	public void setXfDateS(String xfDateS) {
		this.xfDateS = xfDateS;
	}
	public String getXfDateE() {
		return xfDateE;
	}
	public void setXfDateE(String xfDateE) {
		this.xfDateE = xfDateE;
	}
	public String getReturnDateS() {
		return returnDateS;
	}
	public void setReturnDateS(String returnDateS) {
		this.returnDateS = returnDateS;
	}
	public String getReturnDateE() {
		return returnDateE;
	}
	public void setReturnDateE(String returnDateE) {
		this.returnDateE = returnDateE;
	}
	public String getOperationDateS() {
		return operationDateS;
	}
	public void setOperationDateS(String operationDateS) {
		this.operationDateS = operationDateS;
	}
	public String getOperationDateE() {
		return operationDateE;
	}
	public void setOperationDateE(String operationDateE) {
		this.operationDateE = operationDateE;
	}
	public String getActiveDateS() {
		return activeDateS;
	}
	public void setActiveDateS(String activeDateS) {
		this.activeDateS = activeDateS;
	}
	public String getActiveDateE() {
		return activeDateE;
	}
	public void setActiveDateE(String activeDateE) {
		this.activeDateE = activeDateE;
	}
	public String getRealBackDate() {
		return realBackDate;
	}
	public void setRealBackDate(String realBackDate) {
		this.realBackDate = realBackDate;
	}
	public String getRiskType() {
		return riskType;
	}
	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}
	public String getRiskSourceNo() {
		return riskSourceNo;
	}
	public void setRiskSourceNo(String riskSourceNo) {
		this.riskSourceNo = riskSourceNo;
	}
	public String getRiskSourceType() {
		return riskSourceType;
	}
	public void setRiskSourceType(String riskSourceType) {
		this.riskSourceType = riskSourceType;
	}
	public String getRiskSourceName() {
		return riskSourceName;
	}
	public void setRiskSourceName(String riskSourceName) {
		this.riskSourceName = riskSourceName;
	}
	public String getTemporary() {
		return temporary;
	}
	public void setTemporary(String temporary) {
		this.temporary = temporary;
	}
	public Double getAmerceScore() {
		return amerceScore;
	}
	public void setAmerceScore(Double amerceScore) {
		this.amerceScore = amerceScore;
	}
	public String getApproveNo() {
		return approveNo;
	}
	public void setApproveNo(String approveNo) {
		this.approveNo = approveNo;
	}
	public String getDealResultF() {
		return dealResultF;
	}
	public void setDealResultF(String dealResultF) {
		this.dealResultF = dealResultF;
	}
	public String getDealResultL() {
		return dealResultL;
	}
	public void setDealResultL(String dealResultL) {
		this.dealResultL = dealResultL;
	}
	public String getDealOrgan() {
		return dealOrgan;
	}
	public void setDealOrgan(String dealOrgan) {
		this.dealOrgan = dealOrgan;
	}
	public String getBusiNoTx() {
		return busiNoTx;
	}
	public void setBusiNoTx(String busiNoTx) {
		this.busiNoTx = busiNoTx;
	}
	public String getAlarmCreateTime() {
		return alarmCreateTime;
	}
	public void setAlarmCreateTime(String alarmCreateTime) {
		this.alarmCreateTime = alarmCreateTime;
	}
	public String getFormState() {
		return formState;
	}
	public void setFormState(String formState) {
		this.formState = formState;
	}
	public String getFxFlag() {
		return fxFlag;
	}
	public void setFxFlag(String fxFlag) {
		this.fxFlag = fxFlag;
	}
	public String getRiskOrgan() {
		return riskOrgan;
	}
	public void setRiskOrgan(String riskOrgan) {
		this.riskOrgan = riskOrgan;
	}
	public String getDealOrganList() {
		return dealOrganList;
	}
	public void setDealOrganList(String dealOrganList) {
		this.dealOrganList = dealOrganList;
	}
	public String getDetailurl() {
		return detailurl;
	}
	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}
	public String getBrasrulecontext() {
		return brasrulecontext;
	}
	public void setBrasrulecontext(String brasrulecontext) {
		this.brasrulecontext = brasrulecontext;
	}
	public String getDeal_state() {
		return deal_state;
	}
	public void setDeal_state(String deal_state) {
		this.deal_state = deal_state;
	}
	public String getProcDate() {
		return procDate;
	}
	public void setProcDate(String procDate) {
		this.procDate = procDate;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileExt() {
		return fileExt;
	}
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
	public String getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	public String getCorrUserNo() {
		return corrUserNo;
	}
	public void setCorrUserNo(String corrUserNo) {
		this.corrUserNo = corrUserNo;
	}
	public String getCorrBackDate() {
		return corrBackDate;
	}
	public void setCorrBackDate(String corrBackDate) {
		this.corrBackDate = corrBackDate;
	}
	public String getCorrDealDate() {
		return corrDealDate;
	}
	public void setCorrDealDate(String corrDealDate) {
		this.corrDealDate = corrDealDate;
	}
	public String getReturnState() {
		return returnState;
	}
	public void setReturnState(String returnState) {
		this.returnState = returnState;
	}
	public String getBrasrulecontextJm() {
		return brasrulecontextJm;
	}
	public void setBrasrulecontextJm(String brasrulecontextJm) {
		this.brasrulecontextJm = brasrulecontextJm;
	}
	public String getIsBf() {
		return isBf;
	}
	public void setIsBf(String isBf) {
		this.isBf = isBf;
	}
	public String getBakeUserNo() {
		return bakeUserNo;
	}
	public void setBakeUserNo(String bakeUserNo) {
		this.bakeUserNo = bakeUserNo;
	}
	public String getIsSee() {
		return isSee;
	}
	public void setIsSee(String isSee) {
		this.isSee = isSee;
	}
	public String getSeeDesc() {
		return seeDesc;
	}
	public void setSeeDesc(String seeDesc) {
		this.seeDesc = seeDesc;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getProc_date_new() {
		return proc_date_new;
	}
	public void setProc_date_new(String proc_date_new) {
		this.proc_date_new = proc_date_new;
	}
	public String getRiskSourceRemak() {
		return riskSourceRemak;
	}
	public void setRiskSourceRemak(String riskSourceRemak) {
		this.riskSourceRemak = riskSourceRemak;
	}
	public String getParOverDays() {
		return parOverDays;
	}
	public void setParOverDays(String parOverDays) {
		this.parOverDays = parOverDays;
	}
	public String getParBackDate() {
		return parBackDate;
	}
	public void setParBackDate(String parBackDate) {
		this.parBackDate = parBackDate;
	}
	public String getParDealDate() {
		return parDealDate;
	}
	public void setParDealDate(String parDealDate) {
		this.parDealDate = parDealDate;
	}
	public String getOrgDealDate() {
		return orgDealDate;
	}
	public void setOrgDealDate(String orgDealDate) {
		this.orgDealDate = orgDealDate;
	}

	public List<Integer> getEntryrowIds() {
		return entryrowIds;
	}

	public void setEntryrowIds(List<Integer> entryrowIds) {
		this.entryrowIds = entryrowIds;
	}
	public String getCcOrgan() {
		return ccOrgan;
	}
	public void setCcOrgan(String ccOrgan) {
		this.ccOrgan = ccOrgan;
	}
	public String getCcRole() {
		return ccRole;
	}
	public void setCcRole(String ccRole) {
		this.ccRole = ccRole;
	}
	public String getModifyType() {
		return modifyType;
	}
	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}
	public String getModifyNode() {
		return modifyNode;
	}
	public void setModifyNode(String modifyNode) {
		this.modifyNode = modifyNode;
	}
	public String getCcType() {
		return ccType;
	}
	public void setCcType(String ccType) {
		this.ccType = ccType;
	}
	public String getSeanRole() {
		return seanRole;
	}
	public void setSeanRole(String seanRole) {
		this.seanRole = seanRole;
	}
	public String getQdSlipMoid() {
		return qdSlipMoid;
	}
	public void setQdSlipMoid(String qdSlipMoid) {
		this.qdSlipMoid = qdSlipMoid;
	}
	
}