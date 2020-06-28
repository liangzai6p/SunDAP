package com.sunyard.ars.system.bean.study;

public class SmStudyFileTbBean {

	private int id;   //文件ID
	/*@Fields fileType : 文件类别*/ 
	private String fileType;
	/*@Fields fileSource : 文件来源*/ 
	private String fileSource;
	/*@Fields fileYear : 文件年份*/ 
	private String fileYear;
	/*@Fields fileProof : 文件文号*/ 
	private String fileProof;
	/*@Fields fileName : 文件名称*/ 
	private String fileName;
	/*@Fields fileCreator 创建人: */ 
	private String fileCreator;
	/*@Fields createDate : 创建日期*/ 
	private String createDate;
	/*@Fields filePath : 文件存放路径*/ 
	private String filePath;
	/*@Fields 文件备注 : */ 
	private String description;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileSource() {
		return fileSource;
	}
	public void setFileSource(String fileSource) {
		this.fileSource = fileSource;
	}
	public String getFileYear() {
		return fileYear;
	}
	public void setFileYear(String fileYear) {
		this.fileYear = fileYear;
	}
	public String getFileProof() {
		return fileProof;
	}
	public void setFileProof(String fileProof) {
		this.fileProof = fileProof;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileCreator() {
		return fileCreator;
	}
	public void setFileCreator(String fileCreator) {
		this.fileCreator = fileCreator;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
