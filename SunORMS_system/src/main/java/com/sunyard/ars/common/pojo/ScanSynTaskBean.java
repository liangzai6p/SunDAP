package com.sunyard.ars.common.pojo;

public class ScanSynTaskBean {
	private String taskId;
	private String message;
	private String taskState;
	private String errResult;
	private String taskDate;
	public String getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(String taskDate) {
		this.taskDate = taskDate;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getMessage() {
		return message;
	}
	
	public String getTaskState() {
		return taskState;
	}
	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}
	public String getErrResult() {
		return errResult;
	}
	public void setErrResult(String errResult) {
		this.errResult = errResult;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	
	
}
