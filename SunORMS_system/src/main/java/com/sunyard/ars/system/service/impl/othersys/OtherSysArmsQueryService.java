package com.sunyard.ars.system.service.impl.othersys;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.dao.CommonMapper;
import com.sunyard.ars.common.util.date.DateUtil;
import com.sunyard.ars.system.bean.othersys.ArmsColumn;
import com.sunyard.ars.system.bean.othersys.SmRealarmsSet;
import com.sunyard.ars.system.common.EscapeChar;
import com.sunyard.ars.system.common.OtherSysConstants;
import com.sunyard.ars.system.dao.mc.McFieldMapper;
import com.sunyard.ars.system.dao.mc.McTableMapper;
import com.sunyard.ars.system.dao.mc.ModelMapper;
import com.sunyard.ars.system.dao.mc.ViewMapper;
import com.sunyard.ars.system.dao.othersys.ModelAnalyzeMapper;
import com.sunyard.ars.system.dao.othersys.SmRealarmsSetMapper;
import com.sunyard.ars.system.pojo.mc.McField;
import com.sunyard.ars.system.pojo.mc.McTable;
import com.sunyard.ars.system.pojo.mc.Model;
import com.sunyard.ars.system.pojo.mc.TableInfo;
import com.sunyard.ars.system.pojo.mc.View;
import com.sunyard.ars.system.service.othersys.IOtherSystemService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service
@Transactional
public class OtherSysArmsQueryService extends BaseService implements IOtherSystemService{
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private SmRealarmsSetMapper smRealarmsSetMapper;
	
	@Resource
	private CommonMapper commonMapper;
	
	@Resource
	private ModelMapper modelMapper;
	
	@Resource
	private McTableMapper mcTableMapper;
	
	@Resource
	private McFieldMapper mcFieldMapper;
	
	@Resource
	private ViewMapper viewMapper;
	
	@Resource
	private ModelAnalyzeMapper modelAnalyzeMapper;
	
	private ModelAnalyzeService modelAnalyzeService = new ModelAnalyzeService();
	
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		// TODO Auto-generated method stub
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Object data = requestBean.getParameterList().get(0);
		Map sysMap = requestBean.getSysMap();
		String codeId = String.valueOf(sysMap.get("codeId"));
		SmRealarmsSet smRealarmsSet = smRealarmsSetMapper.selectByPrimaryKey(BaseUtil.filterSqlParam(codeId));
		if(smRealarmsSet == null) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("未配置codeId:"+codeId+"对应的实时预警配置！");
			return;
		}
		String tableName = smRealarmsSet.getTableName().trim();
		tableName = BaseUtil.filterSqlParam(tableName);
		List<ArmsColumn> columnList = smRealarmsSetMapper.selectTableColumns(tableName.toUpperCase());
		String splitChar = smRealarmsSet.getSeparator().trim();
		String splitStrCHar = "";
		if (splitChar.length() > 1) {
			for (int i = 0; i < splitChar.length(); i++) {
				char[] sc = splitChar.toCharArray();
				splitStrCHar += "\\" + sc[i];
			}
		} else if (splitChar.length() == 1) {
			if (EscapeChar.ESCAPE_CHAR_SET
					.contains(splitChar)) {
				splitStrCHar = "\\" + splitChar;
			} else {
				splitStrCHar = splitChar;
			}
		}
		String valueType = (String)sysMap.get("valueType");
		int insertCount = insertArmsValues(columnList,tableName, valueType, data, splitStrCHar);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("模型计算完成！");
		return;
		/*if(insertCount > 0) {//数据插入成功，开始模型计算
			String armsFlag = smRealarmsSet.getArmsFlag();
			String modelIds = smRealarmsSet.getModeIds();
			if("1".equals(armsFlag)){
				Map retMap = new HashMap();
				String[] modelArr = modelIds.split(",");
				List modelResultList = new ArrayList();
				int modelListCount = 0;
				for (String modelId : modelArr) {
					Map result = modelAnalyze(modelId);
					modelResultList.add(result);
					//有模型运算失败直接返回实时预警失败，不再往下执行。
					if(ARSConstants.HANDLE_FAIL.equals(result.get("retCode"))) {
						retMap.put("modelResultList", modelResultList);
						responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
						responseBean.setRetMsg((String)result.get("retMsg"));
						responseBean.setRetMap(retMap);
						commonMapper.executeUpdate("update "+tableName+" set ssyj_status = '1' where ssyj_status = '0' ");
						return;
					}
					int modelGroupCount = (Integer)result.get("modelGroupCount");
					modelListCount += modelGroupCount;
				}
				retMap.put("modelListCount", modelListCount);
				retMap.put("modelResultList", modelResultList);
				logger.info(BaseUtil.filterLog(modelResultList.toString()));
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
				responseBean.setRetMsg("模型计算完成！");
				responseBean.setRetMap(retMap);
				commonMapper.executeUpdate("update "+tableName+" set ssyj_status = '1' where ssyj_status = '0' ");
			} else {
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
				responseBean.setRetMsg("数据已插入，但配置未计算模型！");
			}
		}else {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("数据未插入，未计算模型！");
		}*/
	}

	@Override
	public String getTranCode() {
		// TODO Auto-generated method stub
		return OtherSysConstants.OTHER_SYS_ARMS_SAVE; 
	}
	
	/**
	 * 插入表数据
	 * @param columnList 表字段
	 * @param tableName 表名
	 * @param valueType 数据格式 KEYVALUE-key_value或 SPLIT-以分隔符分隔(默认分隔符)
	 * @param data 预警数据
	 * @param splitStrCHar 分隔符
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	private int insertArmsValues(List<ArmsColumn> columnList, String tableName,String valueType, Object data, String splitStrCHar) throws Exception {
		boolean isSplit = "SPLIT".equals(valueType);
		boolean hasStatus = false;
		StringBuilder sbField = new StringBuilder();
		StringBuilder sbValue = new StringBuilder();
		String[] valueArr = null;
		Map dataMap = null;
		if(isSplit) {
			valueArr = data.toString().split(BaseUtil.filterSplit(splitStrCHar));
		}else {
			dataMap = (Map)data;
		}
		for(int i=0; i<columnList.size(); i++) {
			ArmsColumn column = columnList.get(i);
			String columnName = column.getColumnName();
			String value = null;
			if(isSplit) {
				if(valueArr !=null){
					value = valueArr[i];
				}
			}else {
				if(dataMap !=null){
					Object obj = dataMap.get(columnName);
					if(obj != null) {
						value = String.valueOf(obj);
					}
				}
			}
			if("SSYJ_STATUS".equals(columnName)) {
				hasStatus = true;
				if(BaseUtil.isBlank(value)) {
					value = "0";
				}
			}
			if(!BaseUtil.isBlank(value)) {
				sbField.append(","+columnName);
				sbValue.append(","+getInsertValue(column, value));
			}else {
				//必输项又没有默认值的字段，值不能为空！
				if("N".equals(column.getIsNullable()) && column.getColumnDef() == null) {
					throw new Exception("字段："+columnName+"的值不能为空！");
				}
			}
		}
		if(sbField.length() > 0) {
			if(!hasStatus) {
				sbField.append(",SSYJ_STATUS");
				sbValue.append(",'0'");
			}
			String sql = "insert into "+tableName+" ("+sbField.substring(1)+")values("+sbValue.substring(1)+")";
			logger.info("执行插入sql:"+BaseUtil.filterLog(sql));
			return commonMapper.executeUpdate(sql);
		}else {
			logger.info("没有需要添加的列数据！");
		}
		return 0;
	}
	
	/**
	 * 根据字段和给定的值，放回拼接到sql的字符串。
	 * @param column
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	private String getInsertValue(ArmsColumn column, String value) throws Exception {
		String columnType = column.getTypeName();
		if(columnType.startsWith("TIMESTAMP")) {
			columnType = "TIMESTAMP";
		}
		int maxLen = column.getColumnSize();
		switch (columnType) {
		case "NUMBER":
			return value;
		case "CHAR": case "VARCHAR2":
			if(value.getBytes().length > maxLen) {
				throw new Exception("字段："+column.getColumnName()+"值超长，最大值："+maxLen+",实际值："+value.getBytes().length);
			}else {
				return "'"+value+"'";
			}
		case "DATE": case "TIMESTAMP":
			return "to_date('"+value+"','yyyy-mm-dd hh24:mi:ss')";
		default:
			return "'"+value+"'";
		}
	}
	
	/**
	 * 模型计算
	 * @param modelId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map modelAnalyze(String modelId) throws Exception{
		long startTime = System.currentTimeMillis();
		Map analyzeResult = new HashMap();
		modelId = BaseUtil.filterSqlParam(modelId);
		analyzeResult.put("modelId", modelId);
		Model model = modelMapper.selectByPrimaryKey(Integer.parseInt(modelId));
		
		McTable mcTableTb = null;
		if (model == null) {
			analyzeResult.put("retCode", ARSConstants.HANDLE_FAIL);
			analyzeResult.put("retMsg", "获取不到模型信息");
			return analyzeResult;
		}
		String modelName = model.getName();
		String modelLevel = model.getAlarmLevel();
		analyzeResult.put("modelName", modelName);
		//获取数据日期(实时预警，数据日期为当天)
		String dataDate = DateUtil.getNow();
		// 统计项listFlag=0; 明细项 listFlag=1
		int listFlag = 1;
		//获取同一个组的所有模型
		List<Model> modelList =null;
		// 模型表信息
		List<TableInfo> tableInfos = null;
		// 模型表系统字段信息
		List<McField> systemFieldTbs = null;
		// 模型视图信息
		List<View> views = null;
		int modelrowid = 0;
		try {
			modelList = modelMapper.selectGroupModel(Integer.parseInt(modelId));
		} catch (Exception e) {
			logger.error("查询模型信息出现异常",e);
		}
		
		
		if (modelList == null || modelList.size() == 0) {
			analyzeResult.put("retCode", ARSConstants.HANDLE_FAIL);
			analyzeResult.put("retMsg", "没有可执行的模型！");
			return analyzeResult;
		} else {
			logger.info("==========================================================");
			logger.info("====|||=====模型编号:" + BaseUtil.filterLog(modelId) + "模型计算时间符合频度设定,开始计算！" + "====|||=====");
			logger.info("==========================================================");
		}
		
		//根据模型编号查询是否为统计模型
		listFlag = Integer.parseInt(model.getDetailType());
		int modelGroupCount = 0;
		List<Map> modelGroupResultList = new ArrayList<Map>();
		//循环处理模型信息
		for (int i = 0; i < modelList.size(); i++) {
			int modelCount = 0;
			model = modelList.get(i);
			modelId = String.valueOf(model.getId());
			mcTableTb = mcTableMapper.selectByPrimaryKey(Integer.valueOf(BaseUtil.filterSqlParam(model.getTableId()+"")));
			String tableName = mcTableTb.getTableName();
			modelId = BaseUtil.filterSqlParam(modelId);
			tableName = BaseUtil.filterSqlParam(tableName);

			Map modelGroupResult = new HashMap();
			modelGroupResult.put("modelId", modelId);
			modelGroupResult.put("modelName", model.getName());
			// 如果当天数据删除
			/*if (OtherSysConstants.TO_DATE_DEL.equals(model.getTodateDel())) {
				 try {
					 modelAnalyzeMapper.deleteModelDataByModelInfo(tableName, modelId, dataDate);
				} catch (Exception e) {
					logger.error("删除当天模型数据失败",e);
					// TODO Auto-generated catch block
					analyzeResult.put("retCode", ARSConstants.HANDLE_FAIL);
					analyzeResult.put("retMsg", "删除当天模型数据失败！");
					return analyzeResult;
				}
			}*/
			// 处理历史数据开始 由模型保留日期决定 删除那些日期在保留日期前的数据 且数据必须是已经处理过的 没处理过的不删除
			// 9999表示永远保留数据
			/*if (Integer.parseInt(model.getHisdataDays()) >= 0
					&& Integer.parseInt(model.getHisdataDays()) != 9999) {
				try {
					String dateDay = DateUtil.minusBeforeDays(Integer.parseInt(model.getHisdataDays()),DateUtil.FORMATE_DATE);
					modelAnalyzeMapper.deleteHisDataByModelInfo(tableName, modelId, dateDay);
				}catch (Exception e) {
					logger.error("删除历史数据执行失败",e);
					analyzeResult.put("retCode", ARSConstants.HANDLE_FAIL);
					analyzeResult.put("retMsg", "删除历史数据执行失败！");
					return analyzeResult;
				}
			}*/
			tableInfos = modelAnalyzeMapper.getTableInfoByTableId(Integer.valueOf(BaseUtil.filterSqlParam(mcTableTb.getId()+"")));
			systemFieldTbs = mcFieldMapper.getMcFieldByTableType(BaseUtil.filterSqlParam(mcTableTb.getTableType()));
			// 临时展现表中的数据迁移到历史展现表中
			/*if (tableInfos ==null || tableInfos.size() == 0) {
				logger.info("当前模型配置的模型字段为空,请配置模型字段!");
				analyzeResult.put("retCode", ARSConstants.HANDLE_FAIL);
				analyzeResult.put("retMsg", "当前模型配置的模型字段为空,请配置模型字段!");
				return analyzeResult;
			}*/
			/*if (tableInfos.size() > 0 || systemFieldTbs.size() > 0) {
				String modelColumns = modelAnalyzeService.getColumnByTableInfoAndSystemField(tableInfos,systemFieldTbs);
				try {
					if(modelColumns == null || modelColumns.trim().length() == 0) {
						modelAnalyzeMapper.insertHistTable(tableName + ARSConstants.HIS_TABLE_SUFFIX, tableName, modelId);
					}else {
						modelAnalyzeMapper.insertHistTableColums(tableName + ARSConstants.HIS_TABLE_SUFFIX, 
								tableName,modelId,modelColumns);
					}
					modelAnalyzeMapper.deleteTempTable(tableName, modelId);
				} catch (Exception e) {
					logger.error("迁移历史数据执行失败",e);
					analyzeResult.put("retCode", ARSConstants.HANDLE_FAIL);
					analyzeResult.put("retMsg", "迁移历史数据执行失败!");
					return analyzeResult;
				}
			}*/
			View viewSel = new View();
			viewSel.setModelId(Integer.parseInt(modelId));
			// 得到视图信息的集合
			views = viewMapper.selectViewBySelective(viewSel);
			if (views==null || views.size() == 0) {
				logger.info("模型" + modelId + "" + "为配置计算试图,无法提取数据,请在web页面中配置试图信息", modelId + "");
				analyzeResult.put("retCode", ARSConstants.HANDLE_FAIL);
				analyzeResult.put("retMsg", "模型" + modelId + "" + "为配置计算试图,无法提取数据,请在web页面中配置试图信息");
				return analyzeResult;
			}
			//模型视图计算
			List<Map> viewResultList = new ArrayList<Map>();
			String sql = "";
			for (int j = 0; j < views.size(); j++) {
				View view = views.get(j);
				Map viewResult = new HashMap();
				int viewCount = 0;
				viewResult.put("viewId", view.getId());
				viewResult.put("viewName", view.getName());
				// 取信息
				switch (Integer.parseInt(view.getCompositeType())) {
				// 若为自定义则直接执行sql语句
				case 0: case 4:
					sql = view.getQueryStr().replaceAll("\r", " ");
					if (!BaseUtil.isBlank(sql)) {
						try{
							String[] sqlArr = sql.split(";");
							for(String viewSql : sqlArr) {
								viewSql = viewSql.trim();
								if(!"commit".equals(viewSql) && !"COMMIT".equals(viewSql) && !"".equals(viewSql)) {
									viewCount += commonMapper.executeUpdate(viewSql);
								}
							}
						}catch (Exception e) {
							// TODO: handle exception
							logger.error(modelId+"-"+model.getName()+"自定义语句发生异常", e);
							analyzeResult.put("retCode", ARSConstants.HANDLE_FAIL);
							analyzeResult.put("retMsg", modelId+"-"+model.getName()+"自定义语句发生异常!");
							return analyzeResult;
						}
					} else {
						logger.info(BaseUtil.filterLog(modelId+"-"+model.getName()+"视图自定义语句为空：" + view.getName()));
						continue;
					}
					break;
				// 自定义插入
				case 1: case 7:
					sql = view.getQueryStr().replaceAll("\r", " ");
					if (BaseUtil.isBlank(sql)) {
						logger.info(BaseUtil.filterLog(modelId+"-"+model.getName()+"视图自定义插入语句为空：" + view.getName()));
						continue;
					}
					try{
						modelrowid = modelAnalyzeMapper.getMaxRowNum(tableName, tableName + ARSConstants.HIS_TABLE_SUFFIX);
						logger.info("查询最大行号结束,开始自定义插入");
						List<String> sqlList = modelAnalyzeService.getSelfDefInertSql(modelId, model.getName(), dataDate, listFlag,
								modelrowid, sql, modelLevel, tableName);
						for(String s : sqlList) {
							viewCount += commonMapper.executeUpdate(s);
						}
					}catch (Exception e) {
						// TODO: handle exception
						logger.error("模型"+modelId+"-"+model.getName()+" ，自定义插入异常!",e);
						analyzeResult.put("retCode", ARSConstants.HANDLE_FAIL);
						analyzeResult.put("retMsg", "模型"+modelId+"-"+model.getName()+" ，自定义插入异常!");
						return analyzeResult;
					}
					break;
				// 纵向插入
				case 2:
					try {
						modelrowid = modelAnalyzeMapper.getMaxRowNum(tableName, tableName + ARSConstants.HIS_TABLE_SUFFIX);
						sql = getSqlbyViewInfos(view, model, listFlag, dataDate, modelrowid);
						if (sql == null || sql.length() == 0) {
							logger.info("纵向插入语句为空：" + BaseUtil.filterLog(view.getName()));
							continue;
						}
						viewCount = commonMapper.executeUpdate(sql);
					}catch (Exception e) {
						// TODO: handle exception
						logger.error(BaseUtil.filterLog("模型"+modelId+"-"+model.getName()+" ，纵向插入异常!"),e);
						analyzeResult.put("retCode", ARSConstants.HANDLE_FAIL);
						analyzeResult.put("retMsg", "模型"+modelId+"-"+model.getName()+" ，纵向插入异常!");
						return analyzeResult;
					}
					break;
				// 存储过程
				case 3: case 9:
					// 此处存放存储过程名
					/*
					sql = view.getQueryStr().replaceAll("\r", " ");
					if (!BaseUtil.isBlank(sql)) {
						try {
							commonMapper.executeSelect(sql);
						} catch (Exception e) {
							logger.error("模型"+modelId+"-"+model.getName()+" ，存储过程插入异常!",e);
							analyzeResult.put("retCode", ARSConstants.HANDLE_FAIL);
							analyzeResult.put("retMsg", "模型"+modelId+"-"+model.getName()+" ，存储过程插入异常!");
							return analyzeResult;
						}
					} else {
						logger.info("存储过程名为空：" + view.getName());
					}
					*/
					break;
				}
				viewResult.put("viewCount", viewCount);
				modelCount += viewCount;
				viewResultList.add(viewResult);
			}
			modelGroupResult.put("modelCount", modelCount);
			modelGroupResult.put("viewResultList", viewResultList);
			modelGroupCount += modelCount;
			modelGroupResultList.add(modelGroupResult);
		}
		logger.info("模型"+BaseUtil.filterLog(modelName)+" ，执行成功!,共耗时："+(System.currentTimeMillis() - startTime)+"(ms)");
		analyzeResult.put("modelGroupCount", modelGroupCount);
		analyzeResult.put("modelGroupResultList", modelGroupResultList);
		analyzeResult.put("retCode", ARSConstants.HANDLE_SUCCESS);
		analyzeResult.put("retMsg", "模型:"+modelName+" ，执行成功!");
		return analyzeResult;
	}
	
	
	
	
	/**
	 * 获取纵向插入sql
	 * @param view
	 * @param model
	 * @param listFlag
	 * @param dataDate
	 * @param modelrowid
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private String getSqlbyViewInfos(View view, Model model, int listFlag, String dataDate, int modelrowid) throws Exception{
		String sql = "";
		String columns = "";
		String conditions = "";
		String groups = "";
		String havings = "";
		String tables = "";
		boolean isGroup = "0".equals(view.getIsgroupby()) ? false : true;
		List<Map> viewColumnInfos = modelAnalyzeMapper.getViewColsInfoByViewId(Integer.valueOf(BaseUtil.filterSqlParam(view.getId()+"")));
		columns = modelAnalyzeService.getColumnByViewColumnInfo(isGroup, viewColumnInfos);
		if (columns.length() == 0) {
			return sql;
		}
		// 视图条件
		List<Map> viewConditionInfos = modelAnalyzeMapper.getViewConditionInfoByViewId(Integer.valueOf(BaseUtil.filterSqlParam(view.getId()+"")));
		conditions = modelAnalyzeService.getConditionByViewConditionInfo(isGroup, viewConditionInfos);
		havings = modelAnalyzeService.getHavingByViewConditionInfo(isGroup, viewConditionInfos);
		tables = modelAnalyzeService.getTableNameByColumnsAndConditions(viewColumnInfos, viewConditionInfos);
		if (isGroup) {
			groups = modelAnalyzeService.getGroupByColumns(viewColumnInfos);
		}

		// 生成SQL语句
		if (columns.endsWith(",")) {
			columns = columns.substring(0, columns.length() - 1);
			// 加入系统字段
			// columns += "," + mcModelTb.getId() + ",'"
			// + mcModelTb.getName()
			// + "',0,TO_CHAR(SYSDATE,'YYYYMMDD'),TO_DATE('" + dataDate
			// + "','YYYY-MM-DD')," +
			// listFlag+","+mcModelTb.getAlarmLevel()+",0";

			if (ARSConstants.DB_TYPE.equals(ARSConstants.DATABASE_TYPE_DB2)) { // modify bai bing
																// 业务日期
																// occur_date
																// 有配置生成程序不去添加

				columns += "," + model.getId() + ",'" + model.getName() + "', row_number() over ()+"
						+ modelrowid + ",0,TO_CHAR(SYSDATE,'YYYYMMDD')," + listFlag + "," + model.getAlarmLevel()
						+ ",0";

			} else {

				columns += "," + model.getId() + ",'" + model.getName() + "', rownum+" + modelrowid
						+ ",0,TO_CHAR(SYSDATE,'YYYYMMDD')," + listFlag + "," + model.getAlarmLevel() + ",0";
			}
		}

		if (tables.endsWith(",")) {
			tables = tables.substring(0, tables.length() - 1);
		}

		if (groups.endsWith(",")) {
			groups = groups.substring(0, groups.length() - 1);
		}

		conditions = conditions.trim();
		havings = havings.trim();
		// 形成sql
		if (!isGroup) {
			sql = "select " + columns + " from " + tables;
			if (conditions.length() > 0) {
				sql += " where " + conditions;
			}
		} else {
			sql = "select " + columns + " from " + tables;
			if (conditions.length() > 0) {
				sql += " where " + conditions;
			}
			if (groups.length() > 0) {
				sql += " group by " + groups;
			}
			if (havings.length() > 0) {
				sql += " having " + havings;
			}
		}

		String insertColumns = modelAnalyzeService.getInsertColumnByViewColumnInfo(viewColumnInfos);
		// TODO 表名
		String tableName = mcTableMapper.selectByPrimaryKey(Integer.valueOf(BaseUtil.filterSqlParam(model.getTableId()+""))).getTableName();

		StringBuffer insertSql = new StringBuffer();
		insertSql.append("INSERT INTO ").append(" " + tableName + "(");
		// insertSql.append(insertColumns).append("MODEL_ID,MODEL_NAME,ISHANDLE,CREATE_DATE,OCCUR_DATE,LIST_FLAG)");
		insertSql.append(insertColumns)
				.append("MODEL_ID,MODEL_NAME,MODELROW_ID,ISHANDLE,CREATE_DATE,LIST_FLAG,MODEL_LEVEL,MODEL_LOCK)");
		insertSql.append(sql);

		return insertSql.toString();
	}
	
	
}
