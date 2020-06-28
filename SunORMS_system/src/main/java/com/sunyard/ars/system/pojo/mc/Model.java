package com.sunyard.ars.system.pojo.mc;

import java.text.DecimalFormat;

/**
 * 
 * 模型表
 * MC_MODEL_TB
 *
 */
public class Model {

	/**
	 * 预警项编号
	 */
    private Integer id;
    
    /**
     * 预警项名称
     */
    private String name;

    /**
     * 预警项中文说明
     */
    private String description;

    /**
     * 预警级别
     */
    private String alarmLevel;

    /**
     * 数据源ID
     */
    private Integer dsId;

    /**
     * 关联模型ID
     */
    private Integer relatingId;

    /**
     * 模型类型
     */
    private String modelType;

    /**
     * 模型查看方式
     */
    private String modelCheckWay;
    
    /**
     * 模型数据查看方式
     */
    private String modelDataCheckWay;

    /**
     * 表ID,预警项数据的存放表ID
     */
    private Integer tableId;
    
    /**
     * 用于区分预警组
     */
    private Integer groupId;
    
    /**
     * 预警项组内序号,用于确定预警项组内序号,通过其决定运算的先后关系
     */
    private Integer groupNo;

    /**
     * 权限名
     */
    private String privname;

    /**
     * 所属文件夹ID
     */
    private String folderid;

    /**
     * 展现编号
     */
    private String exhiRelId;

    /**
     * 预留字段
     */
    private String messageGroupid;

    /**
     * 是否保存,0：否 1：是
     */
    private String issave;

    /**
     * 是否重新计算,0:当天数据不删除 1:当天数据删除
     */
    private String iscomputer;

    /**
     * 当天数据的处理办法
     */
    private String todateDel;

    /**
     * 数据保留日期
     */
    private String hisdataDays;

    /**
     * 处理方式,0：不启用 1：启用
     */
    private String dealtype;

    /**
     * 预留字段
     */
    private String noticetype;

    /**
     * 预留，模型状态,1：每日2：某日3：每月某日4:每段日期5:每间隔
     */
    private String status;

    /**
     * 预留字段
     */
    private Double cost;

    /**
     * 预留字段
     */
    private String exctype;

    /**
     * 预留字段
     */
    private String oneday;

    /**
     * 开始日期
     */
    private String startday;

    /**
     * 结束日期
     */
    private String endday;

    /**
     * 是否发送邮件,0：否 1：是
     */
    private String ismail;

    /**
     * 是否发送短信,0：否 1：是
     */
    private String issms;

    /**
     * 展现组名
     */
    private String showName;

    /**
     * 业务处理规则描述
     */
    private String selectCondition;

    /**
     * 是否实时模型,0：否 1：是
     */
    private String isCurModel;

    /**
     * 是否自动下发预警单,0：否 1：是
     */
    private String isAutoYj;

    /**
     * 自动下发预警的参数
     */
    private String autoYjParam;

    /**
     * 是否自动监督 0：否 1：是
     */
    private String isautosupervise;
    
    private String detailType;

    private String modelBusiType;

    private Integer feedbackDays;
    private String tableName;
    
    // 是否金融交易 ISFINANCE 传统业务TRAD_BUSI VARCHAR2(30)  数据过滤方式 FILTER_TYPE  是否开通短信 ISSMS
    /**
     * 是否金融交易
     */
    private String isFinance;
    /**
     * 传统业务
     */
    private String tradBusi;
    /**
     * 数据过滤方式
     */
    private String filterType; 
    
    /**
     * 业务范围
     */
    private String busiNo;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel == null ? null : alarmLevel.trim();
    }

    public Integer getDsId() {
        return dsId;
    }

    public void setDsId(Integer dsId) {
        this.dsId = dsId;
    }

    public Integer getRelatingId() {
        return relatingId;
    }

    public void setRelatingId(Integer relatingId) {
        this.relatingId = relatingId;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType == null ? null : modelType.trim();
    }

    public String getModelCheckWay() {
        return modelCheckWay;
    }

    public void setModelCheckWay(String modelCheckWay) {
        this.modelCheckWay = modelCheckWay == null ? null : modelCheckWay.trim();
    }

    public String getModelDataCheckWay() {
        return modelDataCheckWay;
    }

    public void setModelDataCheckWay(String modelDataCheckWay) {
        this.modelDataCheckWay = modelDataCheckWay == null ? null : modelDataCheckWay.trim();
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

    public String getPrivname() {
        return privname;
    }

    public void setPrivname(String privname) {
        this.privname = privname == null ? null : privname.trim();
    }

    public String getFolderid() {
        return folderid;
    }

    public void setFolderid(String folderid) {
        this.folderid = folderid == null ? null : folderid.trim();
    }

    public String getExhiRelId() {
        return exhiRelId;
    }

    public void setExhiRelId(String exhiRelId) {
        this.exhiRelId = exhiRelId == null ? null : exhiRelId.trim();
    }

    public String getMessageGroupid() {
        return messageGroupid;
    }

    public void setMessageGroupid(String messageGroupid) {
        this.messageGroupid = messageGroupid == null ? null : messageGroupid.trim();
    }

    public String getIssave() {
        return issave;
    }

    public void setIssave(String issave) {
        this.issave = issave == null ? null : issave.trim();
    }

    public String getIscomputer() {
        return iscomputer;
    }

    public void setIscomputer(String iscomputer) {
        this.iscomputer = iscomputer == null ? null : iscomputer.trim();
    }

    public String getTodateDel() {
        return todateDel;
    }

    public void setTodateDel(String todateDel) {
        this.todateDel = todateDel == null ? null : todateDel.trim();
    }

    public String getHisdataDays() {
        return hisdataDays;
    }

    public void setHisdataDays(String hisdataDays) {
        this.hisdataDays = hisdataDays == null ? null : hisdataDays.trim();
    }

    public String getDealtype() {
        return dealtype;
    }

    public void setDealtype(String dealtype) {
        this.dealtype = dealtype == null ? null : dealtype.trim();
    }

    public String getNoticetype() {
        return noticetype;
    }

    public void setNoticetype(String noticetype) {
        this.noticetype = noticetype == null ? null : noticetype.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
    	this.cost = cost;
    }

    public String getExctype() {
        return exctype;
    }

    public void setExctype(String exctype) {
        this.exctype = exctype == null ? null : exctype.trim();
    }

    public String getOneday() {
        return oneday;
    }

    public void setOneday(String oneday) {
        this.oneday = oneday == null ? null : oneday.trim();
    }

    public String getStartday() {
        return startday;
    }

    public void setStartday(String startday) {
        this.startday = startday == null ? null : startday.trim();
    }

    public String getEndday() {
        return endday;
    }

    public void setEndday(String endday) {
        this.endday = endday == null ? null : endday.trim();
    }

    public String getIsmail() {
        return ismail;
    }

    public void setIsmail(String ismail) {
        this.ismail = ismail == null ? null : ismail.trim();
    }

    public String getIssms() {
        return issms;
    }

    public void setIssms(String issms) {
        this.issms = issms == null ? null : issms.trim();
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName == null ? null : showName.trim();
    }

    public String getSelectCondition() {
        return selectCondition;
    }

    public void setSelectCondition(String selectCondition) {
        this.selectCondition = selectCondition == null ? null : selectCondition.trim();
    }

    public String getIsCurModel() {
        return isCurModel;
    }

    public void setIsCurModel(String isCurModel) {
        this.isCurModel = isCurModel == null ? null : isCurModel.trim();
    }

    public String getIsAutoYj() {
        return isAutoYj;
    }

    public void setIsAutoYj(String isAutoYj) {
        this.isAutoYj = isAutoYj == null ? null : isAutoYj.trim();
    }

    public String getAutoYjParam() {
        return autoYjParam;
    }

    public void setAutoYjParam(String autoYjParam) {
        this.autoYjParam = autoYjParam == null ? null : autoYjParam.trim();
    }

    public String getIsautosupervise() {
        return isautosupervise;
    }

    public void setIsautosupervise(String isautosupervise) {
        this.isautosupervise = isautosupervise == null ? null : isautosupervise.trim();
    }

	public String getDetailType() {
		return detailType;
	}

	public void setDetailType(String detailType) {
		this.detailType = detailType;
	}

    public String getModelBusiType() {
        return modelBusiType;
    }

    public void setModelBusiType(String modelBusiType) {
        this.modelBusiType = modelBusiType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getFeedbackDays() {
        return feedbackDays;
    }

    public void setFeedbackDays(Integer feedbackDays) {
        this.feedbackDays = feedbackDays;
    }
    
    
    
    public String getIsFinance() {
        return isFinance;
    }

    public void setIsFinance(String isFinance) {
        this.isFinance = isFinance;
    }
    
    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

	public String getTradBusi() {
		return tradBusi;
	}

	public void setTradBusi(String tradBusi) {
		this.tradBusi = tradBusi;
	}

	public String getBusiNo() {
		return busiNo;
	}

	public void setBusiNo(String busiNo) {
		this.busiNo = busiNo;
	}
    
}