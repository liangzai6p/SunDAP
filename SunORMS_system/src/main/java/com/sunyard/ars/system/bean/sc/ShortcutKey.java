package com.sunyard.ars.system.bean.sc;


/**快捷键辅助类
 * 存放配置文件中的属性及页面展示所需字段*/
public class ShortcutKey {
	private Integer id;
	private String functionId;
	private String desc;
	private String keyCode1 = "";
	private String keyCode2 = "";
	private String type = "0";
	private String purview ="";
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getKeyCode1() {
		return keyCode1;
	}
	public void setKeyCode1(String keyCode1) {
		this.keyCode1 = keyCode1;
	}
	public String getKeyCode2() {
		return keyCode2;
	}
	public void setKeyCode2(String keyCode2) {
		this.keyCode2 = keyCode2;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPurview() {
		return purview;
	}
	public void setPurview(String purview) {
		this.purview = purview;
	}
	
	
	
}
