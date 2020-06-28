package com.sunyard.ars.file.pojo.ma;
/**(凭证信息分布在2张表，该类用于收集必要的字段)
 * 补录页面展现时调整表单的辅助类
 * */
public class FormInfoDetail {
	private long voucherNumber;//版面编号
	private String voucherName;//版面名称
	private String checkFields = "";//补录字段
	private int attachmentFlag = 1;//主附件标志־
	private int isMulti = 1;//套勾/单勾
	private String elseName = "";//中文显示名
	
	

	public long getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(long voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	
	public String getCheckFields() {
		return checkFields;
	}
	public void setCheckFields(String checkFields) {
		this.checkFields = checkFields;
	}
	public int getAttachmentFlag() {
		return attachmentFlag;
	}
	public void setAttachmentFlag(int attachmentFlag) {
		this.attachmentFlag = attachmentFlag;
	}
	public int getIsMulti() {
		return isMulti;
	}
	public void setIsMulti(int isMulti) {
		this.isMulti = isMulti;
	}
	public String getElseName() {
		return elseName;
	}
	public void setElseName(String elseName) {
		this.elseName = elseName;
	}
	
	
}
