package com.sunyard.ars.common.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;




public class DelRelate implements java.io.Serializable {

	/**
	 * @名称: 删除表记录时做的操作 <br>
	 * @描述: 因为不采用外键索引，通过程序解决删除记录保持数据完整性一致;
	 * @创建日期: 2007-10-09 <br>
	 * @修改日期: <br>
	 * @编写人员：wangyangyi <br>
	 * @版本号： 1.00
	 */
	// 数据源表
	public static List<Relate> datasource;
	public static List<Relate> table;
	public static List<Relate> dicInfo;
	public static List<Relate> field;
	public static List<Relate> tableField;
	// 非实时/展现
	public static List<Relate> model;
	public static List<Relate> exhibitField;
	public static List<Relate> view;
	public static List<Relate> viewCols;
	public static List<Relate> viewCondition;
	// 关联项
	public static List<Relate> exhibitid_relatingid;
	public static List<Relate> exhibit_relating;
	public static List<Relate> relating_field_tb;
	public static List<Relate> exhibit_relating_fieldmap;
	// 实时配置/展现

	public static List<Relate> realDataDef;
	public static List<Relate> realdataRelate;
	public static List<Relate> realAlarm;
	public static List<Relate> realCondition;
	public static List<Relate> realCols;
	public static List<Relate> realConditionGroup;
	public static List<Relate> realGroupCondition;
	public static List<Relate> realFieldGroup;
	// 个性化配置表;
	public static List<Relate> individ_setting;
	public static List<Relate> individ_match;
	

	private final static long serialVersionUID = 1l;

	public DelRelate() {
		initDatasource();
		initDataAnalyse();
		initRelating();
		initRealTb();
	}

	// 初始化数据源
	public void initDatasource() {
		// 数据源表
		datasource = new ArrayList<Relate>();
		datasource.add(new Relate("MC_TABLE_TB", "DS_ID"));
		datasource.add(new Relate("MC_VIEW_TB", "DS_ID"));
		datasource.add(new Relate("MC_MODEL_TB", "DS_ID"));
		
		// 表定义表
		table = new ArrayList<Relate>();
		// 数据库中没有对应的表 juek.wu
		table.add(new Relate("MC_VIEW_COLS_TB", "TABLE_ID"));
		table.add(new Relate("MC_TABLE_FIELD_TB", "TABLE_ID"));
		// 字段定义
		field = new ArrayList<Relate>();
		field.add(new Relate("MC_TABLE_FIELD_TB", "FIELD_ID"));
		// 字段映射
		tableField = new ArrayList<Relate>();
//		tableField.add(new Relate("Mc_View_Cols_Tb", "entryFieldId,fieldId"));
		tableField.add(new Relate("MC_EXHIBIT_FIELD_TB", "FIELD_ID"));
//		tableField.add(new Relate("RELATING_FIELD_TB", "FIELD_ID"));

	}

	// 初始化非实时
	public void initDataAnalyse() {
		// 视图类
		view = new ArrayList<Relate>();
		view.add(new Relate("Mc_View_Cols_Tb", "view_Id"));
		view.add(new Relate("Mc_View_Condition_Tb", "view_Id"));
		
		viewCols = new ArrayList<Relate>();
		viewCols.add(new Relate("Mc_View_Condition_Tb", "view_Cols_Id"));
		viewCondition = new ArrayList<Relate>();
		// 展现项
		model = new ArrayList<Relate>();
		model.add(new Relate("Mc_Exhibit_Field_Tb", "model_Id"));
		model.add(new Relate("MC_MODEL_LINE_TB", "LINEID"));
		model.add(new Relate("Mc_Ocr_Field_Tb", "model_Id"));
	}

	// 关联项
	public void initRelating() {/*
		// 关联项设置
		exhibit_relating = new ArrayList<Relate>();
		exhibit_relating.add(new Relate("ExhibitidRelatingid", "relateId"));
		exhibit_relating.add(new Relate("RelatingFieldTb", "relateId"));

		relating_field_tb = new ArrayList<Relate>();

		exhibit_relating_fieldmap = new ArrayList<Relate>();

	*/}

	public void initRealTb() {
		// 实时流水字段表;
		realDataDef = new ArrayList<Relate>();
		realDataDef.add(new Relate("RealdataRelate", "realfieldId"));
		// 实 时 数 据 保 存 关 系 设 置
		realdataRelate = new ArrayList<Relate>();
		// 实时配置项
		realAlarm = new ArrayList<Relate>();
		// 实时条件
		realCondition = new ArrayList<Relate>();
		// 实时存储字段
		realCols = new ArrayList<Relate>();
		// 条件组
		realConditionGroup = new ArrayList<Relate>();
		realConditionGroup.add(new Relate("RealGroupCondition", "groupId"));
		realConditionGroup.add(new Relate("RealConditionTb", "statId"));
		// 条件组合
		realGroupCondition = new ArrayList<Relate>();
		// 统计字段列表
		realFieldGroup = new ArrayList<Relate>();
	}
	
	
}
