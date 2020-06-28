package com.sunyard.ars.system.service.impl.mc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sunyard.aos.common.util.BaseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.pojo.DelRelate;
import com.sunyard.ars.common.util.StringUtil;
import com.sunyard.ars.system.dao.mc.McFieldMapper;
import com.sunyard.ars.system.dao.mc.McTableMapper;
import com.sunyard.ars.system.dao.mc.RelateMapper;
import com.sunyard.ars.system.pojo.mc.McField;
import com.sunyard.ars.system.pojo.mc.McTable;
import com.sunyard.ars.system.service.mc.IMcTableService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

import ch.qos.logback.core.joran.conditional.ElseAction;

@Service("mcTableService")
@Transactional
public class McTableServiceImpl extends BaseService  implements IMcTableService{
	
	@Resource
	private McTableMapper tableMapper;
	
	@Resource
	private RelateMapper relateMapper;
	
	@Resource
	private McFieldMapper fieldMapper;
	
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		// TODO Auto-generated method stub
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		// 获取操作标识
		String oper_type = (String) sysMap.get("oper_type");
		if (ARSConstants.OPERATE_QUERY.equals(oper_type)) {
			// 查询
			query(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_ADD.equals(oper_type)) { 
			// 新增
			add(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_MODIFY.equals(oper_type)) { 
			// 修改
			update(requestBean, responseBean);
		} else if(ARSConstants.OPERATE_DELETE.equals(oper_type)) { 
			// 删除
			delete(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_OTHER.equals(oper_type)) {
			// 其他操作
			otherOperation(requestBean, responseBean);
		}else if("alterTable".equals(oper_type)){
			//创建、更改表
			alterTable(requestBean, responseBean);
		}else if ("getTableInfoByType".equals(oper_type)) {
			getTableInfoByType(requestBean, responseBean);
		}else if ("selectTableInfoByName".equals(oper_type)) {
			selectTableInfoByName(requestBean, responseBean);
		}
		
	}
	
	private void selectTableInfoByName(RequestBean requestBean, ResponseBean responseBean) {
		//根据表名称获取表信息
		Map sysMap = requestBean.getSysMap();
		List<HashMap<String,Object>> list = tableMapper.selectTableInfoByName(sysMap.get("tableName").toString());
		Map retMap = new HashMap();
		retMap.put("list", list);
		responseBean.setRetMap(retMap);
	}

	private void getTableInfoByType(RequestBean requestBean, ResponseBean responseBean) {
		Map sysMap = requestBean.getSysMap();
		String   type = sysMap.get("type").toString();
		List<Map<String,Object>> list = tableMapper.getTableInfoByType(type);
		Map retMap = new HashMap();
		retMap.put("list", list);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * 查询表定义表
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		McTable table = (McTable) requestBean.getParameterList().get(0);
		int currentPage = (int)sysMap.get("currentPage");
		int pageSize = 0;
		if(currentPage != -1) {
			Integer initPageSize = (Integer) sysMap.get("pageSize");
			if (initPageSize == null) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageSize;
			}
		}
		
		//执行查询
		Page page = PageHelper.startPage(currentPage, pageSize);
		List resultList = tableMapper.getTableList(table);
		long totalCount = page.getTotal();
		Map retMap = new HashMap();
		retMap.put("pageSize", pageSize);
		retMap.put("allRow", totalCount);
		retMap.put("resultList", resultList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
	}
	
	/**
	 * 新增表定义表
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void add(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		McTable table = (McTable) requestBean.getParameterList().get(0);
		//新增前判断该表定义表是否已经存在
		McTable selTable = new McTable();
		selTable.setTableName(table.getTableName());
		selTable.setTableType(table.getTableType());
		List<McTable> tableList = tableMapper.selectBySelective(selTable);
		if(tableList!=null && tableList.size()>0){
			//数据库中已存在该表定义表，无法新增
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("表定义表已存在，无法新增");
			//返回
			return;
		}
		
		//新增表定义表
		tableMapper.insertSelective(table);

		//mc_table_tb 中 DS_ID 字段 default 1，前台没有传时要返回
		McTable mcTable = tableMapper.selectByPrimaryKey(table.getId());
		Map retMap = new HashMap();
		retMap.put("tableId", table.getId());
		retMap.put("dsId",mcTable.getDsId());
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("添加成功！");
		
		//添加日志
		String log = "警报规则配置新增" + table.getTableName() + "的表!";
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
	}
	
	/**
	 * 修改表定义表
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void update(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		McTable table = (McTable) requestBean.getParameterList().get(0);
		
		//查询旧记录
		McTable oldField = tableMapper.selectByPrimaryKey(table.getId());
		//修改前判断，该表是否已经创建
		int count = tableMapper.selectTable(table.getTableName(),ARSConstants.DB_TYPE);
		String tableOldName = (String)requestBean.getSysMap().get("tableOldName");
        int oldExits = tableMapper.selectTable(tableOldName, ARSConstants.DB_TYPE);
        if(oldExits > 0){
			//该表定义表已经创建，无法修改
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("该表定义表已经创建，无法修改！");
			return;
		}
        if(count > 0){
            //该表定义表已经创建，无法修改
            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
            responseBean.setRetMsg("数据库已有该表，请修改表名");
            return;
        }
		
		//如果表定义表名称改变，需判断修改后的表定义表名称是否已存在
		if(!oldField.getTableName().equals(table.getTableName())){
			McTable selTable = new McTable();
			selTable.setTableName(table.getTableName());
			List<McTable> list = tableMapper.selectBySelective(selTable);
			if(list != null && list.size()>0) {
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("表定义表已存在！");
				return;
			}
		}
		
		//修改表定义表
		tableMapper.updateByPrimaryKeySelective(table);
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
		
		//添加日志
		String log = "警报规则配置修改id为:" + table.getId() + "的表!";
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
	}
	
	/**
	 * 删除表定义表
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void delete(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		List delList =(List)sysMap.get("delList");
		List tableList = requestBean.getParameterList();
		
		//判断表是否已经创建
		int cnt =0;
		for(int k=0;k<tableList.size();k++){
			McTable tb = (McTable)tableList.get(k);
			cnt = tableMapper.selectTable(tb.getTableName(),ARSConstants.DB_TYPE);
			if(cnt>0){
				//表被创建，无法删除
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("表已创建，无法删除");
				return;
			}
		}
		
		//删除前判断是否存在关联
		HashMap condMap =null;
		int count =0;
		for(int j=0;j<DelRelate.table.size();j++){
			condMap = new HashMap<String, Object>();
			condMap.put("relate", DelRelate.table.get(j));
			condMap.put("delList", delList);		
			count = relateMapper.selectRelate(condMap);
			if(count > 0){
				//存在关联信息，不允许删除
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("表被关联，请解除关联关系后再进行删除操作！");
				return;
			}
		}
		
		//删除表定义表
		for(int i=0;i<delList.size();i++){
			tableMapper.deleteByPrimaryKey((int)delList.get(i));
			//添加日志
			String log = "警报规则配置删除id为:" + delList.get(i) + "的表!";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
		}
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("删除成功！");
	}
		
	/**
	 * 其他操作
	 * @param requestBean
	 * @param responseBean
	 */
	private void otherOperation(RequestBean requestBean, ResponseBean responseBean)throws Exception{
			
		}
	
	/**
	 * 创建、更改表
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	private void alterTable(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		McTable table = (McTable) requestBean.getParameterList().get(0);		
		//查询表空间信息
		int tableSpaceCount = tableMapper.selectTableSpaceInfo(table.getTableSpace(),ARSConstants.DB_TYPE);		
		// 如果表空间不存，则用户无法创建表，提示用户表空间不存在
		if(tableSpaceCount == 0){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("表空间不存在,无法创建");
			return;
		}
		
		//查询索引表空间信息
		int indexSpaceCount = tableMapper.selectTableSpaceInfo(table.getIndexSpace(),ARSConstants.DB_TYPE);
				
	   // 如果表空间不存，则用户无法创建表，提示用户表空间不存在
	    if(indexSpaceCount == 0){
	    	responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("索引表空间不存在,无法创建");
			return;
		}
		
		// 模型表定义信息
		List<HashMap<String,Object>> tableDefInfo = tableMapper.selectTableInfoById(Integer.valueOf(BaseUtil.filterSqlParam(table.getId()+"")));
		// 已创建表信息
		List<HashMap<String,Object>> tableInfo = tableMapper.selectTableInfoByName(table.getTableName().toUpperCase());
		List<McField> addFields = null;
		List<McField> dropFields = null;
		McField field = new McField();
		field.setTableType(table.getTableType());
		List<McField> systemFields = fieldMapper.selectBySelective(field);
		
		
		
		// 如果表定义信息和表结构信息都为空说明模型表还未定义，无法创建或者修改
		if (tableDefInfo.size() == 0 && tableInfo.size() == 0) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("表结构未定义,无法创建");
			// 如果表定义信息为空，表结构信息不为空，则删除模型表
		} else if (tableDefInfo.size() == 0 && tableInfo.size() != 0) {
			//删除模型表
			tableMapper.dropTable(table.getTableName());
			//判断表类型，监督，预警模型表还需删除历史表
			if(String.valueOf(ARSConstants.TABLE_TYPE_ARMS_MODEL).equals(table.getTableType()) || String.valueOf(ARSConstants.TABLE_TYPE_SUPERVISE_MODEL).equals(table.getTableType()) ){
				tableMapper.dropTable(table.getTableName()+ARSConstants.HIS_TABLE_SUFFIX);
			}
			// 删除操作日志
//			saveOrDeleteLogDDL(tableName, tableType + "", tableSpace,indexSpace, tableDefInfo, systemFields, request,ARSConstants.OPER_TYPE_DELETE);
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("表删除成功！");
			// 如果表定义信息不为空，表结构信息为空，则创建表
		} else if (tableDefInfo.size() != 0 && tableInfo.size() == 0) {
			createTable(tableDefInfo, table.getTableName(), Integer.parseInt(table.getTableType()),table.getTableSpace(), table.getIndexSpace(), systemFields);
		    //创建索引	
			createOrUpdateIndex(tableDefInfo, table.getTableName(), Integer.parseInt(table.getTableType()),table.getTableSpace(), table.getIndexSpace(), systemFields);
			//创建表字段注释
			createComment(tableDefInfo, table.getTableName(), Integer.parseInt(table.getTableType()));
			//表注释
            updateOrAddCommentTable(table);
			// 新增操作日志
//			saveOrDeleteLogDDL(tableName, tableType + "", tableSpace,indexSpace, tableDefInfo, systemFields, request,ARSConstants.OPER_TYPE_INSERT);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("表创建成功！");
			// 表字段变更
		} else if (tableDefInfo.size() != 0 && tableInfo.size() != 0) {
			addFields = getAddField(tableDefInfo, tableInfo, field);
			dropFields = getDropField(tableDefInfo, tableInfo, field);
			alterTableDao(addFields, dropFields, table.getTableName(), Integer.parseInt(table.getTableType()));
			//创建表字段注释
			createComment(tableDefInfo, table.getTableName(),Integer.parseInt(table.getTableType())); 
			//修改索引
			createOrUpdateIndex(tableDefInfo, table.getTableName(), Integer.parseInt(table.getTableType()),table.getTableSpace(), table.getIndexSpace(), systemFields);
			// 修改操作日志
		//	updateLogDDL(tableName, tableType + "", dropFields, addFields,request, ARSConstants.OPER_TYPE_UPDATE);
            //表注释
            updateOrAddCommentTable(table);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("表字段更新成功！");
		}
	}
	
	/**
	 * 创建表
	 * @param tableDefInfo
	 * @param tableName
	 * @param tableType
	 * @param tableSpace
	 * @param indexSpace
	 * @param systemFields
	 * @throws Exception
	 */
	public void createTable(List<HashMap<String,Object>> tableDefInfo, String tableName,
			int tableType, String tableSpace, String indexSpace,
			List<McField> systemFields) throws Exception {
		// 获取基础建表语句
		String sql = getCreateTableSql(tableDefInfo, tableName, tableSpace,
				indexSpace, systemFields);
		logger.info("建表语句为：" + BaseUtil.filterLog(sql));
		switch (tableType) {
		// 如果为中间表
		case ARSConstants.TABLE_TYPE_MIDDLE:
			tableMapper.createTable(sql);
			break;
		// 如果为模型表
		default:
			// 创建模型表
			tableMapper.createTable(sql);
		
			sql = sql.replace(tableName, tableName
					+ ARSConstants.HIS_TABLE_SUFFIX);
			logger.info("历史模型表建表语句为：" + BaseUtil.filterLog(sql));
			// 创建模型历史表
			tableMapper.createTable(sql);
			break;
		}

	}
	
	/**
	 * @Title: getArmsExhibitTableSql
	 * @Description: 构造建表语句
	 * @param tableDefInfo
	 * @param tableName
	 * @return String
	 */
	private String getCreateTableSql(List<HashMap<String,Object>> tableDefInfo,
			String tableName, String tableSpace, String indexSpace,
			List<McField> systemFields) {
		StringBuffer sql = new StringBuffer();
		
		// 如果有系统字段
		if (systemFields.size() > 0) {
			sql.append(getCreatelTableSystemFieldSql(tableDefInfo, tableName,
					systemFields));
		} else {
			sql.append("CREATE TABLE " + tableName + "(");
			sql.append(getCreateBaseTableSql(tableDefInfo, tableName));
		}
		// 获得表空间
		if (!tableSpace.equals("") && tableSpace != null) {
			// 1为Oracle 2:db2
			// TODO 需要增加 DB2的判断
			if (ARSConstants.DB_TYPE.equals(ARSConstants.DATABASE_TYPE_DB2)) {
				sql.append(" in ");
				sql.append(tableSpace);
			}else{
				sql.append(" tablespace ");
				sql.append(tableSpace);
			}	
		}
		return sql.toString();
	}
	

	/**
	 * @Title: getCreateBaseTableSql
	 * @Description: 基础建表语句拼装
	 * @param tableDefInfo
	 * @param tableName
	 * @return String
	 * 
	 *
	 */
	private String getCreateBaseTableSql(List<HashMap<String,Object>> tableDefInfo,
			String tableName) {
		StringBuffer sql = new StringBuffer();
		for (int i = 0; i < tableDefInfo.size(); i++) {
//			TableInfo table = tableDefInfo.get(i);
			HashMap<String,Object> map = tableDefInfo.get(i);
			sql.append(map.get("COLUMN_NAME") + " ");
			// 判断表列类型
			switch (Integer.parseInt(map.get("COLUMN_TYPE")+"")) {
			case ARSConstants.FIELD_TYPE_INT:
				sql.append(" " + ARSConstants.FIELD_TYPE_INT_SQL + " ");
				break;
			case ARSConstants.FIELD_TYPE_NUMBER:
				sql.append(" " + ARSConstants.FIELD_TYPE_NUMBER_SQL + "("
						+ map.get("COLUMN_LENGTH") + ","
						+ map.get("COLUMN_SPREC") + ")");
				break;
			case ARSConstants.FIELD_TYPE_CHAR:
				sql.append(" " + ARSConstants.FIELD_TYPE_CHAR_SQL + "("
						+ map.get("COLUMN_LENGTH") + ")");
				break;
			case ARSConstants.FIELD_TYPE_VARCHAR:
				sql.append(" " + ARSConstants.FIELD_TYPE_VARCHAR_SQL + "("
						+ map.get("COLUMN_LENGTH") + ")");
				break;
			// 日期和时间固定为varchar型
			 case ARSConstants.FIELD_TYPE_DATE:
			 sql.append(" " + ARSConstants.FIELD_TYPE_DATE_SQL + " ");
			 break;
			case ARSConstants.FIELD_TYPE_TIME:
				sql.append(" " + ARSConstants.FIELD_TYPE_TIME_SQL + " ");
				break;
			default:
				break;
			}
			// 字段默认值
			if (!StringUtil.checkNull(map.get("COLUMN_DEFVALUE")+"")) {
				if (Integer.parseInt(map.get("COLUMN_TYPE")+"") != 0 && Integer.parseInt(map.get("COLUMN_TYPE")+"") != 1) {
					sql.append(" " + ARSConstants.FIELD_DEFAULT_VALUE_SQL + " '"
							+ (map.get("COLUMN_DEFVALUE")+"").replaceAll("'|\"", "")
							+ "'");
				} else {
					sql.append(" " + ARSConstants.FIELD_DEFAULT_VALUE_SQL + " "
							+ map.get("COLUMN_DEFVALUE"));
				}
			}
			if (Integer.parseInt(map.get("COLUMN_ISNULL")+"") == ARSConstants.FIELD_NOT_NULL_FLAG) {
				sql.append(" " + ARSConstants.FIELD_NOT_NULL_SQL + " ");
			}
			// 如果不是最后一位就加逗号
			if (i != tableDefInfo.size() - 1) {
				sql.append(",");
			}
		}
		sql.append(")");
		return sql.toString();
	}
	
	
	
	/**
	 * @Title: getCreatelTableSystemFieldSql
	 * @Description:系统字段建表语句拼装
	 * @param tableDefInfo
	 * @param tableName
	 * @param systemFields
	 * @return String
	 * @author juek.wu
	 */
	private String getCreatelTableSystemFieldSql(List<HashMap<String,Object>> tableDefInfo,
			String tableName, List<McField> systemFields) {
		McField systeField = null;
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TABLE " + tableName + "(");
		for(int i=0;i<systemFields.size();i++){
			if(systemFields.get(i).getName().equals(ARSConstants.PARAMETER_TABLE_ROW_ID_FILED)){
				 sql.append("ROW_ID  INTEGER  NOT NULL,");
				 break;
			}
		}
		if (systemFields.size() > 0) {
			// 去除最后一位括号
			//sql.replace(sql.length() - 1, sql.length(), ",");
			// 展现字段名
			List<String> baseFields = new ArrayList<String>();
			for (HashMap<String,Object>  t : tableDefInfo) {
				baseFields.add(t.get("COLUMN_NAME")+"");
			}
			// 系统字段
			for (int i = 0; i < systemFields.size(); i++) {
				systeField = systemFields.get(i);
				// 过滤重复字段
				if (!baseFields.contains(systeField.getName())) {
					if(!systeField.getName().equals(ARSConstants.PARAMETER_TABLE_ROW_ID_FILED)){
						sql.append(systeField.getName() + " ");
						// 判断表列类型
						switch (Integer.parseInt(systeField.getType())) {
						case ARSConstants.FIELD_TYPE_INT:
							sql.append(" " + ARSConstants.FIELD_TYPE_INT_SQL + " ");
							break;
						case ARSConstants.FIELD_TYPE_NUMBER:
							sql.append(" " + ARSConstants.FIELD_TYPE_NUMBER_SQL + "("
									+ systeField.getLength() + ","
									+ systeField.getSprec() + ")");
							break;
						case ARSConstants.FIELD_TYPE_CHAR:
							sql.append(" " + ARSConstants.FIELD_TYPE_CHAR_SQL + "("
									+ systeField.getLength() + ")");
							break;
						case ARSConstants.FIELD_TYPE_VARCHAR:
							sql.append(" " + ARSConstants.FIELD_TYPE_VARCHAR_SQL + "("
									+ systeField.getLength() + ")");
							break;
						
						 case ARSConstants.FIELD_TYPE_DATE:
						 sql.append(" " + ARSConstants.FIELD_TYPE_DATE_SQL );
						 break;
						 case ARSConstants.FIELD_TYPE_TIME:
							sql.append(" " + ARSConstants.FIELD_TYPE_TIME_SQL + "(6)");
							break;
						 default:
							break;
					}
					
					}
					if(!systeField.getName().equalsIgnoreCase(ARSConstants.PARAMETER_TABLE_ROW_ID_FILED)){
					
					// 系统字段默认值
					if (!StringUtil.checkNull(systeField.getDefvalue())) {
						if (Integer.parseInt(systeField.getType()) != 0
								&& Integer.parseInt(systeField.getType()) != 1) {
							sql.append(" "
									+ ARSConstants.FIELD_DEFAULT_VALUE_SQL
									+ " '"
									+ systeField.getDefvalue().replaceAll(
											"'|\"", "") + "'");
						} else {
							sql.append(" " + ARSConstants.FIELD_DEFAULT_VALUE_SQL
									+ " " + systeField.getDefvalue());
						}
					}
					if (Integer.parseInt(systeField.getIsnull()) == ARSConstants.FIELD_NOT_NULL_FLAG) {
						sql.append(" " + ARSConstants.FIELD_NOT_NULL_SQL + " ");
					}
					// 如果不是最后一位就加逗号
					//if (i != systemFields.size() - 1) {
						sql.append(",");
					//}
					}
				}
			}
			sql.append(getCreateBaseTableSql(tableDefInfo, tableName));
		}
		return sql.toString();
	}
	
	/**
	 * @Title: createTableIndex
	 * @Description: 创建索引
	 * @param tableDefInfo
	 * @param tableName
	 * @param tableType
	 * @return void
	 * @throws Exception
	 */
	public void createOrUpdateIndex(List<HashMap<String,Object>> tableDefInfo,
			String tableName, int tableType, String tableSpace,
			String indexSpace, List<McField> systemFields)
			throws Exception {
		tableName = BaseUtil.filterSqlParam(tableName);
		//查询索引
		int indexCount = tableMapper.selectIndexInfo(tableName,ARSConstants.DB_TYPE);
		if(indexCount >0 ){
		// 删除索引
			tableMapper.deleteIndex(tableName+"_IDX");
		
		}
		
		//拼装创建索引SQL
		String sql = getCreateIndexSql(tableDefInfo, tableName, tableSpace,
				indexSpace, systemFields);
		if(!"".equals(sql)){
			logger.info("创建索引语句为：" + BaseUtil.filterLog(sql.toString()));
			tableMapper.createOrUpdateIndex(sql);
		}else{
			logger.info("不需要创建索引");
		}
		
	}
	
	
	/**
	 * 拼装创建索引的sql
	 * @param tableDefInfo
	 * @param tableName
	 * @param tableSpace
	 * @param indexSpace
	 * @param systemFields
	 * @return
	 */    
	public String getCreateIndexSql(List<HashMap<String,Object>> tableDefInfo,
			String tableName, String tableSpace, String indexSpace,
			List<McField> systemFields) {
		StringBuffer sql = new StringBuffer();
		if (ARSConstants.DB_TYPE.equals(ARSConstants.DATABASE_TYPE_DB2)) {
			return sql.toString();
		}
		sql.append(createIndexSql(tableDefInfo, systemFields));
		if(sql.toString().equals("")){
			return sql.toString();
		}
		if (indexSpace != null&&!indexSpace.equals("") ) {
			sql.append(" tablespace ");
			sql.append(indexSpace);
		}
		return sql.toString();
	}
	
	/**
	 * @Title: createIndexSql
	 * @Description: 拼装索引sql,如果没有重要字段则返回""
	 *               则返回的索引语句为空
	 * @param tableDefInfo
	 * @return String
	 */

	private String createIndexSql(List<HashMap<String,Object>> tableDefInfo, List<McField> systemFields) {
		StringBuffer sql = new StringBuffer();
		StringBuffer indexsql = new StringBuffer();
		for(McField mf : systemFields) {
			if ("1".equals(mf.getIsIndex())) {
				sql.append(mf.getName());
				sql.append(",");
			}
		}
		for (int i = 0; i < tableDefInfo.size(); i++) {
			if ("1".equals((String)tableDefInfo.get(i).get("COLUMN_ISIMPORTANT"))) {
				sql.append(tableDefInfo.get(i).get("COLUMN_NAME"));
				sql.append(",");
			}
		}
		if (sql.toString().equals(""))
			return "";
		sql.replace(sql.length() - 1, sql.length(), "");
		indexsql.append("CREATE INDEX ");
		indexsql.append(tableDefInfo.get(0).get("TABLE_NAME"));
		indexsql.append("_IDX ON ");
		indexsql.append(tableDefInfo.get(0).get("TABLE_NAME") + " (");
		indexsql.insert(indexsql.length(), sql.toString() + ")");
		return indexsql.toString();
	}
	
	/**
	 * 增加表注释
	 * @param tableDefInfo
	 * @param tableName
	 * @param tableType
	 * @throws Exception
	 */
	public void createComment(List<HashMap<String,Object>> tableDefInfo, String tableName,int tableType) throws Exception {
		List<String> addFieldCommentSqls = new ArrayList<String>();
		HashMap<String,Object> map =null;
		StringBuffer addFieldCommentSql = null;
		for (int i = 0; i < tableDefInfo.size(); i++) {
			 map = tableDefInfo.get(i);
			addFieldCommentSql = new StringBuffer();
			addFieldCommentSql.append("comment on column ");
			addFieldCommentSql.append(tableName);
			addFieldCommentSql.append(".");
			addFieldCommentSql.append(map.get("COLUMN_NAME"));
			addFieldCommentSql.append(" is '");
			addFieldCommentSql.append(map.get("COLUMN_MARK"));
			addFieldCommentSql.append("'");
			addFieldCommentSqls.add(addFieldCommentSql.toString());
		}
		switch (tableType) {
		// 如果为中间表
		case ARSConstants.TABLE_TYPE_MIDDLE:
			for (int i = 0; i < addFieldCommentSqls.size(); i++) {
				logger.info("模型表字段注释添加：" + BaseUtil.filterLog(addFieldCommentSqls.get(i)));
				
				tableMapper.createComment(addFieldCommentSqls.get(i));
			}
			break;
		// 如果为模型表则要添加对应历史表注释
		default:
			for (int i = 0; i < addFieldCommentSqls.size(); i++) {
				logger.info("模型表字段注释添加：" + BaseUtil.filterLog(addFieldCommentSqls.get(i)));
				tableMapper.createComment(addFieldCommentSqls.get(i));
				logger.info("历史模型表字段注释添加："+ BaseUtil.filterLog(addFieldCommentSqls.get(i).replace(tableName,tableName + ARSConstants.HIS_TABLE_SUFFIX)));
				// 添加字段到历史表
				tableMapper.createComment(addFieldCommentSqls.get(i).replace(
						tableName, tableName + ARSConstants.HIS_TABLE_SUFFIX));
			}
			break;
		}
	}
	
	/**
	 * @Title: getAddField
	 * @Description: 根据定义表信息和现存表结构，查寻添加字段
	 * @param tableDefInfo
	 * @param tableInfo
	 * @param field
	 * @return List<Field>
	 * @throws Exception
	 * 
	 *  SELECT T1.TABLE_NAME TABLE_NAME,
	T1.COLUMN_NAME COLUMN_NAME,
	T1.DATA_TYPE COLUMN_TYPE,
	T1.NULLABLE COLUMN_ISNULL,
    T1.DATA_DEFAULT COLUMN_DEFVALUE,
	T1.DATA_LENGTH COLUMN_LENGTH,
	T2.COMMENTS COLUMN_MARK
	 */
	private List<McField> getAddField(List<HashMap<String,Object>> tableDefInfo,List<HashMap<String,Object>> tableInfo, McField field) throws Exception {
		boolean flag;
		HashMap<String,Object> tableDef = null;
		HashMap<String,Object> table = null;
		List<McField> addFieldList = new ArrayList<McField>();
		List<McField> systemFields = fieldMapper.selectBySelective(field);
		McField addField = null;
		McField systemField = null;
		// 查找添加字段
		for (int i = 0; i < tableDefInfo.size(); i++) {
			flag = false;
			tableDef = tableDefInfo.get(i);
			for (int j = 0; j < tableInfo.size(); j++) {
				table = tableInfo.get(j);
				if ((tableDef.get("COLUMN_NAME")+"").equalsIgnoreCase(
						table.get("COLUMN_NAME")+"")) {
					flag = true;
					break;
				}
			}
			// 如果在已创建表中不存在该当前字段
			if (!flag) {
				addField = new McField();
				addField.setName(tableDef.get("COLUMN_NAME")+"");
				addField.setType(tableDef.get("COLUMN_TYPE")+"");
				addField.setSprec(tableDef.get("COLUMN_SPREC")+"");
				addField.setLength(tableDef.get("COLUMN_LENGTH")+"");
				addField.setIsnull(tableDef.get("COLUMN_ISNULL")+"");
				addField.setDefvalue(tableDef.get("COLUMN_DEFVALUE")+"");
				addFieldList.add(addField);
			}
		}
		// 如果为模型表或者参数表，判断系统字段是否需要添加
		for (int i = 0; i < systemFields.size(); i++) {
			flag = false;
			systemField = systemFields.get(i);
			for (int j = 0; j < tableInfo.size(); j++) {
				table = tableInfo.get(j);
				if (systemField.getName().equalsIgnoreCase(
						table.get("COLUMN_NAME")+"")) {
					flag = true;
					break;
				}
			}
			// 如果在已创建表中不存在该当前字段
			if (!flag) {
				addField = new McField();
				addField.setName(systemField.getName());
				addField.setType(systemField.getType());
				addField.setSprec(systemField.getSprec());
				addField.setLength(systemField.getLength());
				addField.setIsnull(systemField.getIsnull());
				addField.setDefvalue(systemField.getDefvalue());
				addFieldList.add(addField);
			}
		}
		return addFieldList;
	}
	
	
	/**
	 * @Title: getDropField
	 * @Description: 根据定义表信息和现存表结构，查寻删除字段
	 * @param tableDefInfo
	 * @param tableInfo
	 * @param field
	 * @return List<Field>
	 * @throws Exception
	 */
	private List<McField> getDropField(List<HashMap<String,Object>> tableDefInfo,List<HashMap<String,Object>> tableInfo, McField field) throws Exception {
		boolean flag;
		HashMap<String,Object> table = null;
		HashMap<String,Object> tableDef = null;
		List<McField> dropFieldList = new ArrayList<McField>();
		List<McField> dropList = new ArrayList<McField>();
		List<McField> systemFields = fieldMapper.selectBySelective(field);
		McField dropField = null;
		McField systemField = null;
		// 查找删除字段
		for (int i = 0; i < tableInfo.size(); i++) {
			flag = false;
			table = tableInfo.get(i);
			for (int j = 0; j < tableDefInfo.size(); j++) {
				tableDef = tableDefInfo.get(j);
				// 如果创建表已存在表定义信息字段在，则跳出循环
				if ((table.get("COLUMN_NAME")+"").equalsIgnoreCase(
						tableDef.get("COLUMN_NAME")+"")) {
					flag = true;
					break;
				}
			}
			// 如果在表定义信息中不存在当前字段
			if (!flag) {
				dropField = new McField();
				dropField.setName(table.get("COLUMN_NAME")+"");
				dropFieldList.add(dropField);
			}
		}
		// 如果为模型表或者参数表，判断系统字段是否需要删除
		for (int i = 0; i < dropFieldList.size(); i++) {
			flag = false;
			dropField = dropFieldList.get(i);
			for (int j = 0; j < systemFields.size(); j++) {
				systemField = systemFields.get(j);
				// 查询要被删除的字段是否和系统字段相同
				if (dropField.getName().equalsIgnoreCase(systemField.getName())) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				dropList.add(dropField);
			}
		}
		return dropList;
	}

	
	/**
	 * @Title: alterTableDao
	 * @Description: 更改表结构
	 * @param addFields
	 * @param dropFields
	 * @param tableName
	 * @param tableType
	 * @return void
	 * @throws Exception
	 */
	public void alterTableDao(List<McField> addFields,
			List<McField> dropFields, String tableName, int tableType)
			throws Exception {
		
		List<String> addFieldSqls = getAddFieldSql(addFields, tableName);
		List<String> dropFieldSqls = getDropFieldSql(dropFields, tableName);
		switch (tableType) {
		// 如果为中间表
		case ARSConstants.TABLE_TYPE_MIDDLE:
			for (int i = 0; i < addFieldSqls.size(); i++) {
				logger.info("表字段添加语句为：" + BaseUtil.filterLog(addFieldSqls.get(i)));
				tableMapper.alterTable(addFieldSqls.get(i));
			}
			for (int i = 0; i < dropFieldSqls.size(); i++) {
				logger.info("表字段删除语句为：" + BaseUtil.filterLog(dropFieldSqls.get(i)));
				tableMapper.alterTable(dropFieldSqls.get(i));
			}
			break;
		// 如果为模型表则要添加和删除对应历史表字段
		default:
			for (int i = 0; i < addFieldSqls.size(); i++) {
				logger.info("模型表字段添加语句为：" + BaseUtil.filterLog(addFieldSqls.get(i)));
				tableMapper.alterTable(addFieldSqls.get(i));
				logger.info("历史模型表字段添加语句为："
						+ BaseUtil.filterLog(addFieldSqls.get(i).replace(tableName,
								tableName + ARSConstants.HIS_TABLE_SUFFIX)));
				// 添加字段到历史表
				tableMapper.alterTable(addFieldSqls.get(i).replace(
						tableName, tableName + ARSConstants.HIS_TABLE_SUFFIX));
			}
			for (int i = 0; i < dropFieldSqls.size(); i++) {
				logger.info("模型表字段删除语句为：" + BaseUtil.filterLog(dropFieldSqls.get(i)));
				tableMapper.alterTable(dropFieldSqls.get(i));
				logger.info("历史模型表字段删除语句为："
						+ BaseUtil.filterLog(dropFieldSqls.get(i).replaceAll(tableName,
								tableName + ARSConstants.HIS_TABLE_SUFFIX)));
				// 删除历史表字段
				tableMapper.alterTable(dropFieldSqls.get(i).replaceAll(
						tableName, tableName + ARSConstants.HIS_TABLE_SUFFIX));
			}
			break;
		}
	}
	
	
	/**
	 * @Title: getAddFieldSql
	 * @Description: 获取添加字段SQL语句
	 * @param addFields
	 * @param tableName
	 * @return List<String>
	 */
	private List<String> getAddFieldSql(List<McField> addFields,
			String tableName) {
		List<String> addFieldSqls = new ArrayList<String>();
		McField field = null;
		for (int i = 0; i < addFields.size(); i++) {
			field = addFields.get(i);
			StringBuffer addFieldSql = new StringBuffer();
			addFieldSql.append("ALTER TABLE " + tableName + " ADD ");
			addFieldSql.append(field.getName());
			switch (Integer.parseInt(field.getType())) {
			case ARSConstants.FIELD_TYPE_INT:
				addFieldSql.append(" " + ARSConstants.FIELD_TYPE_INT_SQL + " ");
				break;
			case ARSConstants.FIELD_TYPE_NUMBER:
				addFieldSql.append(" " + ARSConstants.FIELD_TYPE_NUMBER_SQL).append("(")
				.append(field.getLength()).append(",").append(field.getSprec()).append(")");
				break;
			case ARSConstants.FIELD_TYPE_CHAR:
				addFieldSql.append(" ")
				.append(ARSConstants.FIELD_TYPE_CHAR_SQL)
				.append("(")
				.append(field.getLength()).append(")");
				break;
			case ARSConstants.FIELD_TYPE_VARCHAR:
				addFieldSql.append(" " + ARSConstants.FIELD_TYPE_VARCHAR_SQL + "("
						+ field.getLength() + ")");
				break;

//			case ARSConstants.FIELD_TYPE_DATE:
//				addFieldSql.append(" " + ARSConstants.FIELD_TYPE_DATE_SQL + " ");
//				break;
			// case ARSConstants.FIELD_TYPE_TIME:
			// addFieldSql.append(" " + ARSConstants.FIELD_TYPE_TIME_SQL + " ");
			// break;

			 // 日期和时间固定为char型
			 case ARSConstants.FIELD_TYPE_DATE:
			 addFieldSql.append(" " + ARSConstants.FIELD_TYPE_DATE_SQL + " ");
			 break;
			case ARSConstants.FIELD_TYPE_TIME:
				addFieldSql.append(" " + ARSConstants.FIELD_TYPE_TIME_SQL + " ");
				break;

			default:
				break;
			}
			// 字段默认值
			if (!StringUtil.checkNull(field.getDefvalue())) {
				if (Integer.parseInt(field.getType()) != 0
						&& Integer.parseInt(field.getType()) != 1) {
					addFieldSql.append(" " + ARSConstants.FIELD_DEFAULT_VALUE_SQL
							+ " '" + field.getDefvalue().replaceAll("'|\"", "")
							+ "'");
				} else {
					addFieldSql.append(" " + ARSConstants.FIELD_DEFAULT_VALUE_SQL
							+ " " + field.getDefvalue());
				}
			}
			if (Integer.parseInt(field.getIsnull()) == ARSConstants.FIELD_NOT_NULL_FLAG) {
				addFieldSql.append(" " + ARSConstants.FIELD_NOT_NULL_SQL + " ");
			}
			addFieldSqls.add(addFieldSql.toString());
		}
		return addFieldSqls;
	}

	/**
	 * @Title: getDropFieldSql
	 * @Description: 获取删除字段SQL语句
	 * @param dropFields
	 * @param tableName
	 * @return List<String>
	 */
	private List<String> getDropFieldSql(List<McField> dropFields,
			String tableName) {
		List<String> dropFieldSqls = new ArrayList<String>();
		for (McField field : dropFields) {
			StringBuffer dropFieldSql = new StringBuffer();
			dropFieldSql.append("ALTER TABLE " + tableName + " DROP COLUMN ");
			dropFieldSql.append(field.getName());
			dropFieldSqls.add(dropFieldSql.toString());
		}
		return dropFieldSqls;
	}

    /**
     *
     * @param table
     * 创建表描述
     *
     */
	private  void updateOrAddCommentTable(McTable table){

        switch (Integer.parseInt(table.getTableType())) {
            // 如果为中间表
            case ARSConstants.TABLE_TYPE_MIDDLE:
                if(!BaseUtil.isBlank(table.getTableDesc())){
                    String sql = "COMMENT ON TABLE " + table.getTableName() + " IS '" + table.getTableDesc()+ "'";
                    tableMapper.createComment(sql);
                }
                break;
            // 如果为模型表则要添加对应历史表注释
            default:
                if(!BaseUtil.isBlank(table.getTableDesc())){
                    String sql = "COMMENT ON TABLE " + table.getTableName() + " IS '" + table.getTableDesc() +"'";
                    tableMapper.createComment(sql);
                    sql = "COMMENT ON TABLE " + table.getTableName() + "_HIS  IS '" + table.getTableDesc() + "'";
                    tableMapper.createComment(sql);
                }
                break;
        }
    }


}
