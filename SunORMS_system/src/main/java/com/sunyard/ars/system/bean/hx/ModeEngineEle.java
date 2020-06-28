package com.sunyard.ars.system.bean.hx;

import java.io.Serializable;

/**
 * @author:		 SUNYARD-NGQ
 * @date:		 2018年9月25日 下午3:43:08
 * @description: TODO(模型驱动因子显示列表对应的实体类 )
 */
public class ModeEngineEle implements Serializable{

	/**
	 * 序号-主键id
	 */
	private Integer id;
	
	/**
	 * 模型id
	 */
	private String entryid;
	
	/**
	 * 模型名称
	 */
	private String name;
	
	/**
	 * 驱动因子序号
	 */
	private String sourceNo;
	
	/**
	 * 驱动因子大类
	 */
	private String sourceType;
	
	/**
	 * 驱动因子子类
	 */
	private String sourceName;
	
	/**
	 * 维护人员
	 */
	private String optuser;
	
	/**
	 * 维护时间
	 */
	private String createTime;
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the entryid
	 */
	public String getEntryid() {
		return entryid;
	}
	/**
	 * @param entryid the entryid to set
	 */
	public void setEntryid(String entryid) {
		this.entryid = entryid;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the sourceNo
	 */
	public String getSourceNo() {
		return sourceNo;
	}
	/**
	 * @param sourceNo the sourceNo to set
	 */
	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}
	/**
	 * @return the sourceType
	 */
	public String getSourceType() {
		return sourceType;
	}
	/**
	 * @param sourceType the sourceType to set
	 */
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	/**
	 * @return the sourceName
	 */
	public String getSourceName() {
		return sourceName;
	}
	/**
	 * @param sourceName the sourceName to set
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	/**
	 * @return the optuser
	 */
	public String getOptuser() {
		return optuser;
	}
	/**
	 * @param optuser the optuser to set
	 */
	public void setOptuser(String optuser) {
		this.optuser = optuser;
	}
	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
