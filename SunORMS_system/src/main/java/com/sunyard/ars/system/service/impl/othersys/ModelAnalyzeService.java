package com.sunyard.ars.system.service.impl.othersys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.system.common.MacroUtil;
import com.sunyard.ars.system.pojo.mc.McField;
import com.sunyard.ars.system.pojo.mc.TableInfo;

/**
 * 模型计算公共方法提取
 * @author 20190506e
 *
 */
public class ModelAnalyzeService {
	
	/** 
	 * @Title: getColumnByTableInfoAndSystemField 
	 * @Description: 根据模型表信息和模型表系统字段信息拼装字段字符串
	 * @param tableInfos
	 * @param systemFields
	 * @return String
	 */
	public String getColumnByTableInfoAndSystemField(List<TableInfo> tableInfos,
			List<McField> systemFields) {
		McField systeField = null;
		StringBuffer field = new StringBuffer();
		String fieldSQL="";
		for (int i = 0; i < tableInfos.size(); i++) {
			TableInfo tableInfo = tableInfos.get(i);
			field.append(tableInfo.getColumnName().toUpperCase());
			// 如果不是最后一位就加逗号
			if (i != tableInfos.size() - 1) {
				field.append(",");
			}
		}
		if (systemFields.size() > 0) {
			field.append(",");
			// 展现字段名
			List<String> baseFields = new ArrayList<String>();
			for (TableInfo t : tableInfos) {
				baseFields.add(t.getColumnName().toUpperCase());
			}
			// 系统字段
			for (int i = 0; i < systemFields.size(); i++) {
				systeField = systemFields.get(i);
				// 过滤重复字段
				if (!baseFields.contains(systeField.getName().toUpperCase())) {
					field.append(systeField.getName().toUpperCase() + " ");
					// 如果不是最后一位就加逗号
					if (i != systemFields.size() - 1) {
						field.append(",");
					}
				}
			}
			fieldSQL=field.toString();
			if(fieldSQL.length()>0 && fieldSQL.endsWith(",")){
				fieldSQL=fieldSQL.substring(0, fieldSQL.length()-1);
			}
		}
		return fieldSQL;
	}
	
	
	/**
	 * 拼装自定义插入sql
	 * @return
	 */
	public List<String> getSelfDefInertSql(String modelId, String modelName, String dataDate, int listFlag,
			int modelrowid, String sql, String modelLevel, String tableName) {
		List<String> sqlList = new ArrayList<String>();
		sql = sql.replaceAll(" group ", " GROUP ");
		if(sql.contains(" GROUP ")){
			String insertSql="";
			String defaultField ="ENTRY_ID,ENTRY_NAME,ENTRY_LEVEL,LIST_FLAG,ISHANDLE,CREATE_DATE,BUSI_DATA_DATE,";
			sql = sql.replaceFirst("\\(", "(" + defaultField);
			String defaultFieldValue = modelId + ",";       //模型编号ENTRY_ID
			defaultFieldValue += "'" + modelName + "',";                   //模型名称ENTRY_NAME
			defaultFieldValue += "'" + modelLevel + "',";//模型级别ENTRY_LEVEL
			defaultFieldValue += "'" + Integer.toString(listFlag) + "',";  //统计明细标志LIST_FLAG
			defaultFieldValue += "'0',";                                                   //数据处理标志ISHANDLE
			defaultFieldValue += "to_char(SYSDATE,'yyyyMMddHH24Miss'),";  //创建日期CREATE_DATE
			defaultFieldValue += "'" + dataDate + "',";    //业务日期BUSI_DATA_DATE
			String [] tmpSql = sql.split("[uU][nN][iI][oO][nN]");
			sql = "";
			for (int k = 0; k < tmpSql.length; k++) {
				tmpSql[k] = tmpSql[k].replaceFirst("[sS][eE][lL][eE][cC][tT]", "select"); 
				tmpSql[k] = tmpSql[k].replaceFirst("[fF][rR][oO][mM]", "from"); 
				if(tmpSql[k].contains("*/")
				    && tmpSql[k].indexOf("*/")>tmpSql[k].indexOf("select")
				    && tmpSql[k].indexOf("*/")<tmpSql[k].indexOf("from")){
					tmpSql[k] = tmpSql[k].replaceFirst("\\*/", "\\*/ " + defaultFieldValue);
				}else{
					tmpSql[k] = tmpSql[k].replaceFirst("select", "select " + defaultFieldValue); 
				}
				sql += "union " + tmpSql[k];
			}
			insertSql += sql.replaceFirst("union", "").trim();
			sqlList.add(insertSql);
			
			String updateSql ="";
			if (ARSConstants.DB_TYPE.equals(ARSConstants.DATABASE_TYPE_DB2)) {
				updateSql = "UPDATE "+ tableName+" SET ENTRYROW_ID = row_number() over ()+"+modelrowid+" WHERE ENTRY_ID="+modelId + " AND ENTRYROW_ID IS NULL";
			}else{
				updateSql = "UPDATE "+ tableName+" SET ENTRYROW_ID = rownum+"+modelrowid+" WHERE ENTRY_ID="+modelId + " AND ENTRYROW_ID IS NULL";
			}
			sqlList.add(updateSql);
		}else {
			String eachSql="";
			String defaultField ="ENTRY_ID,ENTRY_NAME,ENTRYROW_ID,ENTRY_LEVEL,LIST_FLAG,ISHANDLE,CREATE_DATE,BUSI_DATA_DATE,";
			sql = sql.replaceFirst("\\(", "(" + defaultField);
			String defaultFieldValue = modelId + ",";       //模型编号ENTRY_ID
			defaultFieldValue += "'" + modelName + "',";                   //模型名称ENTRY_NAME
			defaultFieldValue +="rownum+"+modelrowid+",";                  //模型行号 ENTRYROW_ID
			defaultFieldValue += "'" + modelLevel + "',";//模型级别ENTRY_LEVEL
			defaultFieldValue += "'" + Integer.toString(listFlag) + "',";  //统计明细标志LIST_FLAG
			defaultFieldValue += "'0',";                                                   //数据处理标志ISHANDLE
			defaultFieldValue += "to_char(SYSDATE,'yyyyMMddHH24Miss'),";  //创建日期CREATE_DATE
			defaultFieldValue += "'" + dataDate + "',";    //业务日期BUSI_DATA_DATE
			
			String [] tmpSql = sql.split("[uU][nN][iI][oO][nN]");
			sql = "";
			for (int k = 0; k < tmpSql.length; k++) {
				tmpSql[k] = tmpSql[k].replaceFirst("[sS][eE][lL][eE][cC][tT]", "select"); 
				tmpSql[k] = tmpSql[k].replaceFirst("[fF][rR][oO][mM]", "from"); 
				if(tmpSql[k].contains("*/")
				    && tmpSql[k].indexOf("*/")>tmpSql[k].indexOf("select")
				    && tmpSql[k].indexOf("*/")<tmpSql[k].indexOf("from")){
					tmpSql[k] = tmpSql[k].replaceFirst("\\*/", "\\*/ " + defaultFieldValue);
				}else{
					tmpSql[k] = tmpSql[k].replaceFirst("select", "select " + defaultFieldValue); 
				}
				sql += "union " + tmpSql[k];
			}
			eachSql += sql.replaceFirst("union", "").trim();
			sqlList.add(eachSql);
		}
		return sqlList;
	}
	
	/**
	 * @Title: getColumnByViewColumnInfo
	 * @Description: 根据视图信息获取计算列
	 * @param isGroup
	 * @param viewColumnInfos
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public String getColumnByViewColumnInfo(Boolean isGroup,
			List<Map> viewColumnInfos) {
		StringBuffer column = new StringBuffer();
		Map viewColumnInfo = null;
		for (int i = 0; i < viewColumnInfos.size(); i++) {
			viewColumnInfo = viewColumnInfos.get(i);
			String tableColumn = viewColumnInfo.get("TABLE_NAME") + "."
					+ viewColumnInfo.get("NAME");
			// 聚合方式
			if (isGroup) {
				switch (Integer.parseInt((String) viewColumnInfo.get("GROUP_COMPUTE"))) {
				// count不分组
				case 10:
					column.append("COUNT(" + tableColumn + ")");
					break;
				// count分组
				case 11:
					column.append("COUNT(" + tableColumn + ")");
					break;
				// sum不分组
				case 20:
					column.append("SUM(" + tableColumn + ")");
					break;
				// sum分组
				case 21:
					column.append("SUM(" + tableColumn + ")");
					break;
				// count(distinct)组
				case 30:
					column.append("COUNT(DISTINCT " + tableColumn + ")");
					break;
				// sum(distinct)
				case 40:
					column.append("SUM(DISTINCT " + tableColumn + ")");
					break;
				// 分组
				case 50:
					column.append(tableColumn);
					break;
				default:
					column.append(tableColumn);
					break;
				}
			} else {
				column.append(tableColumn);
			}
			// 为了取值时模型字段和取值字段能够一一对应
			if (Integer.parseInt((String) viewColumnInfo.get("MODEL_FIELD_ID")) > 0) {
				column.append(" AS " + viewColumnInfo.get("NAME"));
			}
			column.append(",");
		}
		return column.toString();
	}
	
	/**
	 * @Title: getConditionByViewConditionInfo
	 * @Description: 根据视图条件信息获取视图条件
	 * @param isGroup
	 * @param viewConditionInfos
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public String getConditionByViewConditionInfo(Boolean isGroup,
			List<Map> viewConditionInfos) {
		Map viewConditionInfo = null;
		StringBuffer condition = new StringBuffer();
		boolean bTag = false;
		for (int i = 0; i < viewConditionInfos.size(); i++) {
			viewConditionInfo = viewConditionInfos.get(i);
			String tableColumn = viewConditionInfo.get("TABLE_NAME") + "."
					+ viewConditionInfo.get("NAME");
			if (BaseUtil.isBlank((String) viewConditionInfo.get("TABLE_NAME"))
					&& BaseUtil.isBlank((String)viewConditionInfo.get("NAME"))) {
				bTag = true;
			}
			
			// 组合方式
			switch (Integer.parseInt((String) viewConditionInfo.get("COMPOSITE_TYPE"))) {
			case 1:
				condition.append(" AND ");
				break;
			case 2:
				condition.append(" OR ");
				break;
			case 3:
				condition.append(" (");
				break;
			case 4:
				condition.append(") ");
				break;
			default:
				break;
			}
			
			
			// 聚合方式
			if (isGroup) {
				switch (Integer.parseInt((String) viewConditionInfo.get("COMPUTE_FLAG"))) {
				case 1:
					condition.append("COUNT(" + tableColumn + ")");
					break;
				case 2:
					condition.append("COUNT( DISTINCT " + tableColumn + ")");
					break;
				case 3:
					condition.append("SUM(" + tableColumn + ")");
					break;
				case 4:
					condition.append("SUM( DISTINCT " + tableColumn + ")");
					break;
				case 5:
					condition.append("AVG(" + tableColumn + ")");
					break;
				default:
					break;
				}
			} else {
				condition.append(tableColumn);
			}
			
			String appendStr = "";
			if (((String)viewConditionInfo.get("VALUE")).indexOf("YYYY") >= 0) {
				appendStr = "'"
						+ MacroUtil.ParseMacro("#&"
								+ viewConditionInfo.get("VALUE") + "&#") + "'";
			}else{
				appendStr = String.valueOf(viewConditionInfo.get("VALUE"));
			}
			
			// 计算方式
			switch (Integer.parseInt((String) viewConditionInfo.get("OPERATOR"))) {
			case 0:
				condition.append(" = ");
				condition.append(appendStr);
				break;
			case 1:
				condition.append(" > ");
				condition.append(appendStr);
				break;
			case 2:
				condition.append(" < ");
				condition.append(appendStr);
				break;
			case 3:
				condition.append(" >= ");
				condition.append(appendStr);
				break;
			case 4:
				condition.append(" <= ");
				condition.append(appendStr);
				break;
			case 5:
				condition.append(" IN (");
				condition.append(appendStr);
				condition.append(") ");
				break;
			case 6:
				condition.append(" LIKE %");
				condition.append(appendStr);
				condition.append("%) ");
				break;
			case 7:
				condition.append(" IS NULL ");
				break;
			default:
				break;
			}
			

			// 反条件con_not
			if (Integer.parseInt((String) viewConditionInfo.get("CON_NOT")) == 0) {
				condition = new StringBuffer(" NOT (" + condition + ")");
			}
			if (bTag) {
				condition = new StringBuffer("");
			}

		}
		return condition.toString();
	}

	/** 
	 * @Title: getHavingByViewConditionInfo 
	 * @Description: 根据视图条件获取分组条件
	 * @param isGroup
	 * @param viewConditionInfos
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public String getHavingByViewConditionInfo(Boolean isGroup,
			List<Map> viewConditionInfos) {
		Map viewConditionInfo = null;
		StringBuffer condition = new StringBuffer();
		StringBuffer having = new StringBuffer();
		boolean bTag = false;
		for (int i = 0; i < viewConditionInfos.size(); i++) {
			viewConditionInfo = viewConditionInfos.get(i);
			String tableColumn = viewConditionInfo.get("TABLE_NAME") + "."
					+ viewConditionInfo.get("NAME");
			if (BaseUtil.isBlank((String) viewConditionInfo.get("TABLE_NAME"))
					&& BaseUtil.isBlank((String) viewConditionInfo.get("NAME"))) {
				bTag = true;
			}
			// 聚合方式
			if (isGroup) {
				switch (Integer.parseInt((String) viewConditionInfo.get("COMPUTE_FLAG"))) {
				case 1:
					condition.append("COUNT(" + tableColumn + ")");
					break;
				case 2:
					condition.append("COUNT( DISTINCT " + tableColumn + ")");
					break;
				case 3:
					condition.append("SUM(" + tableColumn + ")");
					break;
				case 4:
					condition.append("SUM( DISTINCT " + tableColumn + ")");
					break;
				case 5:
					condition.append("AVG(" + tableColumn + ")");
					break;
				default:
					break;
				}
			} else {
				condition.append(tableColumn);
			}
			// 计算方式
			switch (Integer.parseInt((String) viewConditionInfo.get("COMPUTE_FLAG"))) {
			case 0:
				condition.append(" = ");
				break;
			case 1:
				condition.append(" > ");
				break;
			case 2:
				condition.append(" < ");
				break;
			case 3:
				condition.append(" >= ");
				break;
			case 4:
				condition.append(" <= ");
				break;
			case 5:
				condition.append(" IN ");
				break;
			case 6:
				condition.append(" LIKE ");
				break;
			case 7:
				condition.append(" IS NULL ");
				break;
			default:
				break;
			}
			if (((String)viewConditionInfo.get("VALUE")).indexOf("YYYY") >= 0) {
				condition.append("'"
						+ MacroUtil.ParseMacro("#&"
								+ viewConditionInfo.get("VALUE") + "&#") + "'");
			}else{
//				condition.append("'"
//								+ viewConditionInfo.getValue() + "'");
				condition.append(viewConditionInfo.get("VALUE"));
			}
			// 反条件con_not
			if (Integer.parseInt((String) viewConditionInfo.get("CON_NOT")) == 0) {
				condition = new StringBuffer(" NOT (" + condition + ")");
			}
			if (bTag) {
				condition = new StringBuffer("");
			}
			// 组合方式
			switch (Integer.parseInt((String) viewConditionInfo.get("COMPOSITE_TYPE"))) {
			case 1:
				condition.append(" AND ");
				break;
			case 2:
				condition.append(" OR ");
				break;
			case 3:
				condition.append(" (");
				break;
			case 4:
				condition.append(") ");
				break;
			default:
				break;
			}
			if (Integer.parseInt((String) viewConditionInfo.get("COMPUTE_FLAG")) > 0) {
				having.append(condition);
			}
		}
		return having.toString();
	}
	
	/**
	 * @Title: getTableNameByColumnsAndConditions
	 * @Description: 根据字段信息和条件信息获取表名
	 * @param viewColumnInfos
	 * @param viewConditionInfos
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public String getTableNameByColumnsAndConditions(
			List<Map> viewColumnInfos,
			List<Map> viewConditionInfos) {
		StringBuffer tableName = new StringBuffer();
		Map viewColumnInfo = null;
		Map viewConditionInfo = null;
		for (int i = 0; i < viewColumnInfos.size(); i++) {
			viewColumnInfo = viewColumnInfos.get(i);
			if (tableName.indexOf((String) viewColumnInfo.get("TABLE_NAME")) == -1) {
				tableName.append(viewColumnInfo.get("TABLE_NAME")).append(",");
			}
		}
		for (int i = 0; i < viewConditionInfos.size(); i++) {
			viewConditionInfo = viewConditionInfos.get(i);
			if (tableName.indexOf((String) viewConditionInfo.get("TABLE_NAME")) == -1) {
				tableName.append(viewConditionInfo.get("TABLE_NAME")).append(",");
			}
		}
		return tableName.toString();
	}
	
	/**
	 * @Title: getGroupByColumns
	 * @Description: 根据视图列获取分组列
	 * @param viewColumnInfos
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public String getGroupByColumns(List<Map> viewColumnInfos) {
		StringBuffer groups = new StringBuffer();
		Map viewColumnInfo = null;
		for (int i = 0; i < viewColumnInfos.size(); i++) {
			viewColumnInfo = viewColumnInfos.get(i);
			String tableColumn = viewColumnInfo.get("TABLE_NAME") + "."
					+ viewColumnInfo.get("NAME");
			switch (Integer.parseInt((String) viewColumnInfo.get("GROUP_COMPUTE"))) {
			// count不分组
			case 10:
				break;
			// count分组
			case 11:
				groups.append(tableColumn).append(",");
				break;
			// sum不分组
			case 20:
				break;
			// sum分组
			case 21:
				groups.append(tableColumn).append(",");
				break;
			// count(distinct)组
			case 30:
				break;
			// sum(distinct)
			case 40:
				break;
			// 分组
			case 50:
				groups.append(tableColumn).append(",");
				break;
			default:
				groups.append(tableColumn).append(",");
				break;
			}
		}
		return groups.toString();
	}
	
	/**
	 * @Title: getColumnByViewColumnInfo
	 * @Description: 根据视图信息获取计算列
	 * @param viewColumnInfos
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public String getInsertColumnByViewColumnInfo(List<Map> viewColumnInfos) {
		StringBuffer column = new StringBuffer();
		Map viewColumnInfo = null;
		for (int i = 0; i < viewColumnInfos.size(); i++) {
			viewColumnInfo = viewColumnInfos.get(i);
			String tableColumn = (String) viewColumnInfo.get("NAME");
				column.append(tableColumn);
			  column.append(",");
		}
		return column.toString();
	}
	
}
