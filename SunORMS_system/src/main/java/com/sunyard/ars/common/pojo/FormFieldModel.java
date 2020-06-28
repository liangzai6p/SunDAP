package com.sunyard.ars.common.pojo;


/**
* @ClassName: FormFieldModel
* @Description: TODO(这里用一句话描述这个类的作用)
* @author baibing
* @date 2017年4月11日 上午9:36:57
*/
public class FormFieldModel {

	
	/**  * @Fields  fieldName : 字段名次  */
	private String fieldName;
	/**  * @Fields  RectArea :   识别区域*/
	private String RectArea;
	/**  * @Fields  fieldValue : 字段结果(多用于识别反回报文)  */
	private String fieldValue;
	
	public FormFieldModel( String fieldName,String fieldValue){
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	public FormFieldModel(){
		
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getRectArea() {
		return RectArea;
	}
	public void setRectArea(String rectArea) {
		RectArea = rectArea;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	
	
	

}
