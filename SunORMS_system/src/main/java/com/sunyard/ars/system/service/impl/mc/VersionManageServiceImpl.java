package com.sunyard.ars.system.service.impl.mc;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sunyard.aos.common.util.BaseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.dao.CommonMapper;
import com.sunyard.ars.system.dao.mc.LabExhibitFieldMapper;
import com.sunyard.ars.system.dao.mc.LabMcVersionMapper;
import com.sunyard.ars.system.dao.mc.LabRuleMapper;
import com.sunyard.ars.system.dao.mc.McFieldMapper;
import com.sunyard.ars.system.dao.mc.McTableFieldMapper;
import com.sunyard.ars.system.dao.mc.McTableMapper;
import com.sunyard.ars.system.pojo.mc.LabExhibitField;
import com.sunyard.ars.system.pojo.mc.LabMcVersion;
import com.sunyard.ars.system.pojo.mc.LabRule;
import com.sunyard.ars.system.pojo.mc.McField;
import com.sunyard.ars.system.pojo.mc.McTable;
import com.sunyard.ars.system.pojo.mc.McTableField;
import com.sunyard.ars.system.service.mc.IMcTableService;
import com.sunyard.ars.system.service.mc.IVersionManageService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@SuppressWarnings("unused")
@Service("versionManageService")
@Transactional
public class VersionManageServiceImpl extends BaseService  implements IVersionManageService{
	
	
	@Resource
	private LabMcVersionMapper labMcVersionMapper;
	
	@Resource
	private McTableMapper tableMapper;
	
	@Resource
	private McTableFieldMapper tableFieldMapper;
	
	@Resource
	private McFieldMapper fieldMapper;
	
	@Resource
	private LabRuleMapper labRuleMapper;
	
	@Resource
	private LabExhibitFieldMapper labExhibitFieldMapper;
	
	@Resource
	private CommonMapper commonMapper;
	
	@Resource
	private IMcTableService mcTableService;
	
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
		} else if("QUERYDATA".equals(oper_type)){
			queryData(requestBean, responseBean);
		}else if (ARSConstants.OPERATE_ADD.equals(oper_type)) { 
			// 新增
			add(requestBean, responseBean);
		}/* else if (ARSConstants.OPERATE_MODIFY.equals(oper_type)) { 
			// 修改
			update(requestBean, responseBean);
		} else if(ARSConstants.OPERATE_DELETE.equals(oper_type)) { 
			// 删除
			delete(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_OTHER.equals(oper_type)) {
			// 其他操作
			otherOperation(requestBean, responseBean);
		}*/
	}
	
	/**
	 * 查询版本信息
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean){
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		LabMcVersion labMcVersion = (LabMcVersion) requestBean.getParameterList().get(0);
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
		List resultList = labMcVersionMapper.selectBySelective(labMcVersion);
		long totalCount = page.getTotal();
		Map retMap = new HashMap();
		retMap.put("pageSize", pageSize);
		retMap.put("allRow", totalCount);
		retMap.put("resultList", resultList);
		retMap.put("currentPage", currentPage);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
	}
	/**
	 * 查询版本的差异信息
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryData(RequestBean requestBean, ResponseBean responseBean)throws Exception{		
		Map sysMap = requestBean.getSysMap();
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
		LabMcVersion labMcVersion = (LabMcVersion) requestBean.getParameterList().get(0);
		List resultList = labMcVersionMapper.selectBySelective(labMcVersion);
		BigDecimal modelId=(BigDecimal) ((Map)resultList.get(0)).get("MODEL_ID");//根据id获取模型id
		BigDecimal tableId=(BigDecimal) ((Map)resultList.get(0)).get("TABLE_ID");//根据id获取数据表id
		modelId = new BigDecimal(BaseUtil.filterSqlParam(modelId.toString()));
		tableId = new BigDecimal(BaseUtil.filterSqlParam(tableId.toString()));
		List fieldAllList=labMcVersionMapper.selectByModelId(modelId);//根据模型id获取展现字段中文名和英文名存进map
		List fieldNameList = new ArrayList();
		for(int i=0;i<fieldAllList.size();i++){
			String enName =(String)((Map)fieldAllList.get(i)).get("NAME");
			String exhibitName =(String)((Map)fieldAllList.get(i)).get("EXHIBIT_NAME");
			enName = BaseUtil.filterSqlParam(enName);
			fieldNameList.add(enName);
		}
		String tableName=labMcVersionMapper.selectByTableId(tableId);//根据tableid获取表名
		Page page = PageHelper.startPage(currentPage, pageSize);
		List dataList=labMcVersionMapper.queryDatas(tableName,fieldNameList);//按照表名和字段名查询数据
		long totalCount = page.getTotal();
		Map retMap = new HashMap();
		retMap.put("pageSize", pageSize);
		retMap.put("allRow", totalCount);
		retMap.put("resultList", resultList);
		retMap.put("currentPage", currentPage);
		retMap.put("fieldAllList",fieldAllList);
		retMap.put("dataList",dataList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		}
		
	
    /**
     * 版本保存
     */ 
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly=false)
	private void add(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		 Map sysMap = requestBean.getSysMap();
	    //获取查询sql
		String sql = (String)sysMap.get("sql");
	    //拼装插入sql
		String versionSql = "";
		   
		//step1:创建版本
	    LabMcVersion version = (LabMcVersion)requestBean.getParameterList().get(0);
		   //1.1根据模型名称获取modelId
		    Map versionMap = labMcVersionMapper.selectByModelName(version.getModelName());
		    String exFields = "";
		    String labTableName = "";
   	   		List<LabExhibitField> labTableFieldList = new ArrayList<>();
   	   		LabExhibitField labExhibitField = null;
	       if(versionMap!=null){//判断是否创建过版本
	    	   logger.info("该模型已创建过版本,增加新的版本号,modelId:"+ BaseUtil.filterLog(versionMap.get("MODEL_ID")+""));
	    	   version.setModelId(Integer.parseInt(BaseUtil.filterSqlParam(versionMap.get("MODEL_ID").toString())));
	    	   version.setTableId(Integer.parseInt(BaseUtil.filterSqlParam(versionMap.get("TABLE_ID").toString())));

	    	   //获取插入字段,查询lab_exhibit_field_tb
	    	   List<HashMap<String,Object>> exList = labExhibitFieldMapper.showExhibitField(version.getModelId());
	    	   for(HashMap<String,Object> hashMap : exList){
	    		   exFields+=","+hashMap.get("NAME");
	    	   }
	    	   
	    	   //获取页面配置展现字段
	    	  String exFieldsRes=""; 
 	   		  List<McTableField> fieldList = JSON.parseArray((String)sysMap.get("fieldList"),McTableField.class);
 	   		  for(McTableField tableField : fieldList){
 	   			exFieldsRes+=","+tableField.getName().split("-")[0];
 	   		  }
	    	  if(!exFieldsRes.equals(exFields)){
	    		  //两次版本展现字段不一致，返回失败
	    		  logger.info("与之前版本展现字段不一致，exFieldsRes："+BaseUtil.filterLog(exFieldsRes)+",数据库中exFields："+BaseUtil.filterLog(exFields));
	    		  responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
	    		  responseBean.setRetMsg("与之前版本展现字段不一致,重新配置！");
	    		  return;
	    	  } 
	    	   
	    	   logger.info("查询并组装展现字段："+BaseUtil.filterLog(exFields));
	    	   //获取表名
	    	   McTable table = tableMapper.selectByPrimaryKey(Integer.valueOf(BaseUtil.filterSqlParam(version.getTableId().toString())));
	    	   labTableName = table.getTableName();
	    	   logger.info("获取到模型存储表为："+BaseUtil.filterLog(labTableName));
	    	   
	    	   versionSql = "insert into "+labTableName+"("+exFields.substring(1)+")"+sql;
	    	   //创建版本
	    	   version.setVersionSql(versionSql);
	    	   labMcVersionMapper.insert(version);
	    	   version = labMcVersionMapper.selectByPrimaryKey(version.getId());//版本号为自动生成，所以重新查询获取
	       }else{
	    	   
	    	   logger.info("该模型从未创建过版本,开始首次版本创建");
	    	   //新建数据存储表 格式：lab_data_xxxxx_tb
	    	   DecimalFormat df = new DecimalFormat("00000");
	    	   int tableNum = tableMapper.selectLabTableNum();
	    	   labTableName = "LAB_DATA_"+df.format(tableNum)+"_TB";
			   labTableName = BaseUtil.filterSqlParam(labTableName);
	    	   logger.info("自定义实验室模型表名："+labTableName);
	    	   
	    	   //执行建表操作
	    	      //1.2 将表数据插入mc_table_tb
	    	   		McTable table = new McTable();
	    	   		table.setDsId(Integer.valueOf(ARSConstants.SYSTEM_PARAMETER.get("LAB_DS_ID").getParamValue()));
	    	   		table.setIndexSpace(ARSConstants.SYSTEM_PARAMETER.get("LAB_INDEX_SPACE").getParamValue());
	    	   		table.setTableDesc(ARSConstants.SYSTEM_PARAMETER.get("LAB_TABLE_DESC").getParamValue());
	    	   		table.setTableName(labTableName);
	    	   		table.setTableSpace(ARSConstants.SYSTEM_PARAMETER.get("LAB_TABLE_SPACE").getParamValue());
	    	   		table.setTableType(ARSConstants.SYSTEM_PARAMETER.get("LAB_TABLE_TYPE").getParamValue());
	    	   		
	    	     	tableMapper.insertSelective(table);
	    	     	logger.info("实验室模型表信息插入完毕，开始插入表字段信息");
	    	      //1.3插入表字段信息
	    	   		//获取前端页面传过来的展现字段信息

	    	   		  McTableField tableField = null;
//	    	   		  List<String> fieldList = (List)sysMap.get("fieldList");
	    	   		   List<McTableField> fieldList = JSON.parseArray((String)sysMap.get("fieldList"),McTableField.class);
	    	   		  for(int i=0;i<fieldList.size();i++){
	    	   			  //获取mcTableField
	    	   			  tableField = fieldList.get(i);
	    	   			 //存储表字段信息
	    	   			  tableField.setTableId(table.getId());
	    	   			 String [] fieldName = tableField.getName().split("-");//格式：英文名-中文名
	    	   			 tableField.setName(fieldName[0]);
	    	   			 //插入表字段信息
	    	   			  tableFieldMapper.insertSelective(tableField);
	    	   			  
	    	   			  //组装展现字段信息
	    	   			  labExhibitField = new LabExhibitField();
	    	   			  labExhibitField.setRowno(i);
	    	   			  labExhibitField.setTableFieldId(tableField.getFieldId());
	    	   		      labExhibitField.setExhibitName(fieldName[1]);
	    	   			  labTableFieldList.add(labExhibitField);
	    	   			  
	    	   			  exFields+=","+tableField.getName();
	    	   		  }
	    	   		logger.info("实验室模型表字段信息插入完毕，开始进行建表操作");
	    	      //1.4建表
	    	  		// 模型表定义信息
	    	  		List<HashMap<String,Object>> tableDefInfo = tableMapper.selectTableInfoById(table.getId());

	    	  		McField field = new McField();
	    	  		field.setTableType(table.getTableType());
	    	  		List<McField> systemFields = fieldMapper.selectBySelective(field);
	    	   		//建表
	    	  		mcTableService.createTable(tableDefInfo, table.getTableName(), Integer.parseInt(table.getTableType()),table.getTableSpace(), table.getIndexSpace(), systemFields);
	    		    //创建索引	
	    	  		mcTableService.createOrUpdateIndex(tableDefInfo, table.getTableName(), Integer.parseInt(table.getTableType()),table.getTableSpace(), table.getIndexSpace(), systemFields);
	    			//创建表字段注释
	    	  		mcTableService.createComment(tableDefInfo, table.getTableName(), Integer.parseInt(table.getTableType())); 
	    	   	   
	    	  		logger.info("实验室模型建表操作执行完毕，开始创建版本信息");
	    	  		//1.5创建版本
	    	  		version.setTableId(table.getId());//设置tableId
	    	  		versionSql = "insert into "+labTableName+"("+exFields.substring(1)+")"+sql;
	    	  		version.setVersionSql(versionSql);
	    	       labMcVersionMapper.insertNoModelId(version);
	    	       
	    	       //模型号，版本号皆是自动生成，重新查询版本信息
	    	       version = labMcVersionMapper.selectByPrimaryKey(version.getId());
	    	       
	    	       logger.info("实验室模型版本信息创建完毕，模型号：modelId:"+version.getModelId()+",版本号versionId:"+version.getVersionId());
	    	       //1.6插入展现字段
	    	       for(LabExhibitField labExField : labTableFieldList){
	    	    	   labExField.setModelId(version.getModelId());
	    	    	   labExhibitFieldMapper.insertSelective(labExField);
	    	       }
	    	   
	       }
		//step2:保存规则
		List<LabRule> ruleList = JSON.parseArray((String)sysMap.get("ruleList"),LabRule.class);
		for(LabRule labRule : ruleList){
			labRule.setVersionId(version.getVersionId());
			labRule.setModelId(version.getModelId());
			labRuleMapper.insertSelective(labRule);
		}
	    
	    //step3:保存数据
	       //3.1获取跑批日期
		   String dataDate = labRuleMapper.queryJobDate();
		   //3.2获取modelrowid
		   int modelrowid = labRuleMapper.queryMaxRowId(labTableName);
		   //3.3组装自定义插入sql   
		   String insertSql = getSelfDefInsertSqlBySql(version.getModelId(), version.getModelName(), dataDate, 0, modelrowid, versionSql, "0", version.getId()+"");
		   //3.4执行自定义插入
		   commonMapper.executeUpdate(insertSql);
		   responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		   responseBean.setRetMsg("版本创建成功");
	}
		
	/**
	 *获取自定义插入sql 
	 */	
	private String getSelfDefInsertSqlBySql(int modelId, String modelName,
			String dataDate, int listFlag,int modelrowid,String versionSql,String modelLevle,String versionId) {
		String eachSql="";
		String defaultField ="ENTRY_ID,ENTRY_NAME,ENTRYROW_ID,ENTRY_LEVEL,LIST_FLAG,ISHANDLE,CREATE_DATE,BUSI_DATA_DATE,VERSION_ID,";
		versionSql = versionSql.replaceFirst("\\(", "(" + defaultField);
		String defaultFieldValue = Integer.toString(modelId) + ",";       //模型编号ENTRY_ID
		defaultFieldValue += "'" + modelName + "',";                   //模型名称ENTRY_NAME
		defaultFieldValue +="rownum+"+modelrowid+",";                  //模型行号 ENTRYROW_ID
		defaultFieldValue += "'" + modelLevle + "',";                  //模型级别ENTRY_LEVEL
		defaultFieldValue += "'" + Integer.toString(listFlag) + "',";  //统计明细标志LIST_FLAG
		defaultFieldValue += "'0',";                                                   //数据处理标志ISHANDLE
		defaultFieldValue += "to_char(SYSDATE,'yyyyMMddHH24Miss'),";  //创建日期CREATE_DATE
		defaultFieldValue += "'" + dataDate + "',";    //业务日期BUSI_DATA_DATE
		defaultFieldValue += "'"+versionId+"',";          
		
		String [] tmpSql = versionSql.split("[uU][nN][iI][oO][nN]");
		versionSql = "";
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
			versionSql += "union " + tmpSql[k];
		}
		eachSql += versionSql.replaceFirst("union", "").trim();
		return eachSql;
	
	}
		
		
		
}
