package com.sunyard.ars.system.service.impl.mc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.chainsaw.Main;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.ImportUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.pojo.DelRelate;
import com.sunyard.ars.common.pojo.PropertyFilter;
import com.sunyard.ars.common.util.DateUtil;
import com.sunyard.ars.system.dao.mc.McFieldMapper;
import com.sunyard.ars.system.dao.mc.McTableFieldMapper;
import com.sunyard.ars.system.dao.mc.McTableMapper;
import com.sunyard.ars.system.dao.mc.ParameterMapper;
import com.sunyard.ars.system.pojo.mc.McTable;
import com.sunyard.ars.system.pojo.mc.ViewCols;
import com.sunyard.ars.system.service.mc.IModelService;
import com.sunyard.ars.system.service.mc.IParameterService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.chat.IShowChatList;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;

@Service("parameterService")
@Transactional
public class ParameterServiceImpl extends BaseService  implements IParameterService{
	
	@Resource
	private ParameterMapper paramMapper;
	
	@Resource
	private McTableMapper tableMapper;

	@Resource
	private McTableFieldMapper tableFieldMapper;
	
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
		}else if("load".equals(oper_type)){
			load(requestBean, responseBean);
		}else if("submit".equals(oper_type)){
			//提交操作
			submit(requestBean, responseBean);
		}else if("queryVersion".equals(oper_type)){
			//查询参数版本
			queryVersion(requestBean, responseBean);
		}else if("changeVersion".equals(oper_type)){
			//恢复参数版本
			changeVersion(requestBean, responseBean);
		} else if (AOSConstants.OPERATE_IMPORT.equals(oper_type)) {
			// 导入
			importOperation(requestBean,responseBean);
		} else if("getParameterType".equalsIgnoreCase(oper_type)){//查询参数表菜单
			getParameterType(requestBean,responseBean);
		}
	}
	
	private void getParameterType(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		//查询参数表
		List<HashMap<String,Object>> tableTypeList = paramMapper.selectParamTableType(BaseUtil.getLoginUser().getUserNo());

		Map retMap = new HashMap();
		retMap.put("tableTypeList", tableTypeList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * 查询参数
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void load(RequestBean requestBean, ResponseBean responseBean)throws Exception{

		Map sysMap = requestBean.getSysMap();
		// 获取页面参数
		int tableId = (int) sysMap.get("tableId");
		//查询参数表
		McTable table = tableMapper.selectByPrimaryKey(tableId);
		
		//查询参数表是否已经创建
		List<HashMap<String,Object>> tableInfo = tableMapper.selectTableInfoByName(table.getTableName().toUpperCase());
		if(tableInfo.size() ==0){
			//表未创建
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("表未创建");
			return;
		}
		
		//获取参数表字段
		List<HashMap<String,Object>> tableCols = getTableCols(tableId);
		
		//查询重要字段
		List<HashMap<String,Object>> impCols = tableMapper.selectImpCol(tableId);
		
		Map retMap = new HashMap();
		retMap.put("impCols", impCols);
		retMap.put("tableCols", tableCols);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
		
	
	} 
	
	/**
	 * 查询参数
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		Map sysMap = requestBean.getSysMap();
		// 获取页面参数
		int tableId = (int) sysMap.get("tableId");
		//查询参数表
		McTable table = tableMapper.selectByPrimaryKey(tableId);
		
		//获取参数表字段
		List<HashMap<String,Object>> tableCols = getTableCols(tableId);
		
		boolean hasBankcode = false;
		
		for (HashMap<String, Object> hashMap : tableCols) {
			if("BANKCODE".equalsIgnoreCase(hashMap.get("NAME")+"")){
				hasBankcode = true;
				break;
			}
		}
				
		//获取页面查询条件		
		List<PropertyFilter> filterList =  JSON.parseArray((String)sysMap.get("filterList"),PropertyFilter.class);
		String startDate = (String)sysMap.get("startDate");
		String endDate = (String)sysMap.get("endDate");
		HashMap condMap = new HashMap();
		condMap.put("tableName", table.getTableName());	
		condMap.put("tableCols", tableCols);	
		condMap.put("filterList", filterList);
		condMap.put("startDate", startDate);
		condMap.put("endDate", endDate);
		condMap.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
		if("13".equals(table.getTableType()) || "12".equals(table.getTableType())){//支行参数
			condMap.put("userNo", BaseUtil.getLoginUser().getUserNo());
		}
		if(hasBankcode){
			condMap.put("orderby", "ORDER BY BANKCODE");	
		}else{
			condMap.put("orderby", "ORDER BY ROWID");	
		}
		
		//查询参数列表
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
		List<HashMap<String,Object>> resultList = paramMapper.selectParamData(condMap);	
		for(Map map:resultList){//修改日期的格式把Date型转换为String型
			for(Object obj:map.keySet()){
				if(obj.equals("QYRQ")||obj.equals("TYRQ")){
				Date value=(Date) map.get(obj);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date=sdf.format(value);
				map.put(BaseUtil.filterSqlParam(obj.toString()), date);
				}
			}
		}
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
	 * 操作参数--增删改
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private void submit(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		List<Map> addList = JSON.parseArray((String)sysMap.get("addList"),Map.class);
		List<Map> editList =  JSON.parseArray((String)sysMap.get("editList"),Map.class);
		List<Map> delList =  JSON.parseArray((String)sysMap.get("delList"),Map.class);
		// 获取页面参数
		int tableId = (int) sysMap.get("tableId");
		//查询参数表
		McTable table = tableMapper.selectByPrimaryKey(tableId);
		
		//获取参数表字段
		List<HashMap<String,Object>> tableCols = getTableCols(tableId);
		//获取当前时间yyyy-MM-dd HH:mm:ss
		String date=DateUtil.getTime19();
		//yyyy-MM-dd-HH.mi.ss.000000
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.000000").format(new Date());
		//String date=DateUtil.getDate10();//获取当前日期 yyyy-MM-dd

		//执行表字段新增,由于要拼接日期所以在这里写sql语句
		if(addList!=null && addList.size()>0){
			for(int i=0;i<addList.size();i++){//遍历从前端传过来的集合
				Map<String,Object> map =addList.get(i);//获得集合中的map
				StringBuffer addSql = new StringBuffer();//定义一个StringBuffer容器用来存放拼接的sql
				addSql.append("insert into "+table.getTableName()+" ( ");
				for(int k=0;k<tableCols.size();k++){//遍历表字段的list
					HashMap<String,Object> tableCol = tableCols.get(k);//得到存放表英文字段的map集合
					addSql.append(tableCol.get("NAME"));//从map集合中获得英文名,拼接上去
					if(k != tableCols.size()-1){//拼接逗号(最后一个逗号刚好没有)
						addSql.append(",");
					}
				}
				addSql.append(") values (");
				for(int k=0;k<tableCols.size();k++){
					HashMap<String,Object> tableCol = tableCols.get(k);
					/*if("QYRQ".equals(tableCol.get("NAME"))||"TYRQ".equals(tableCol.get("NAME"))){//判断字段英文名是否等于TYRQ QYRQ
						addSql.append("to_date("+"'"+map.get(tableCol.get("NAME"))+"'"+",'yyyy-MM-dd')");//如果等于就拼接,在sql中把Sting型转化为date型
						if(k != tableCols.size()-1){
							addSql.append(",");
						}*/
					if("QYRQ".equals(tableCol.get("NAME"))){//判断字段英文名是否等于TYRQ QYRQ
						if(ARSConstants.DATABASE_TYPE_DB2.equals(ARSConstants.DB_TYPE)){
							// 这里db2表字段类型没改（页面没有用到具体时间），db2 date 也能存 timestamp不过是yyyy-MM-dd 形式
							//db2
							addSql.append("TIMESTAMP('").append(timeStamp).append("')");
						}else{
							//oracle
							addSql.append("to_date("+"'"+date+"'"+",'yyyy-MM-dd HH24:mi:ss')");//如果等于就拼接,在sql中把Sting型转化为date型
						}
						if(k != tableCols.size()-1){
							addSql.append(",");
						}
					}else if("TYRQ".equals(tableCol.get("NAME"))){
						if(ARSConstants.DATABASE_TYPE_DB2.equals(ARSConstants.DB_TYPE)){
							addSql.append("TIMESTAMP('3000-12-31')");
						}else{
							addSql.append("to_date('3000-12-31','yyyy-MM-dd HH24:mi:ss')");//如果等于就拼接,在sql中把Sting型转化为date型
						}
						if(k != tableCols.size()-1){
							addSql.append(",");
						}
					}else{
						addSql.append("'"+map.get(tableCol.get("NAME"))+"'");//如果英文名不等于TYRQ QYRQ就执行
						if(k != tableCols.size()-1){
							addSql.append(",");
						}
					}
			
				}
				addSql.append(")");
				paramMapper.addParamData(addSql.toString());
				//新增参数
				//paramMapper.addParamData(table.getTableName(),tableCols, fieldValues);
			}
		
		}
		
		//执行参数修改
		if(editList!=null && editList.size()>0){//遍历前端传过来的要修改的list集合
			for(int j=0;j<editList.size();j++){
				Map<String,Object> editMap =editList.get(j);//获得list集合中的map
				String rowId=null;
				for(String str:editMap.keySet()){//获得map中真实存在的rowid
					if(str.equals("ROWID_EXIST")){
						rowId=(String) editMap.get(str);
					}
				}
				StringBuffer modifySql = new StringBuffer();
				modifySql.append("update "+table.getTableName()+" set ");//拼接修改语句
				//组装更新内容
				for(int k=0;k<tableCols.size();k++){
					HashMap<String,Object> tableCol = tableCols.get(k);
					if(tableCol.get("NAME").equals("QYRQ")||tableCol.get("NAME").equals("TYRQ")){//在sql中把QYRQ和TYRQ转换为日期型(date)
						/*modifySql.append(tableCol.get("NAME")).append("=").append("to_date('"+editMap.get(tableCol.get("NAME"))+"','yyyy-MM-dd')").append("");
						if(k != tableCols.size()-1){
							modifySql.append(",");
						}*/
						continue;
					}else{
						String value = (String)editMap.get(tableCol.get("NAME"));
						if(value == null ){
							modifySql.append(tableCol.get("NAME")).append("=null");
						}else {
							modifySql.append(tableCol.get("NAME")).append("='").append(value).append("'");
						}
						/*if(k != tableCols.size()-1){*/
							modifySql.append(",");
						/*}*/
					}
				 }
				String modifySqlStr=modifySql.toString();
				String str=modifySqlStr.substring(0,modifySqlStr.length()-1);
				String modifySqlSqlF= str+" where rowid = "+"'"+rowId+"'";
				//modifySqlSqlF.append(" where rowid = ").append("'"+rowId+"'");
				
				paramMapper.modifyParamData(modifySqlSqlF);
			}
		}
		
		//执行参数删除
		if(delList!=null && delList.size()>0){
			for(Map delMap : delList){
				paramMapper.deleteParamData(table.getTableName(), String.valueOf(delMap.get("ROWID_EXIST")).trim());
			}
			
		}
		
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("操作成功！");
		
	}
	
	/**
	 * 获取表字段
	 * @param tableId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private List<HashMap<String,Object>> getTableCols(int tableId)throws Exception{
		//查询已配置表字段
		List<HashMap<String,Object>> tableCols = tableFieldMapper.selectTableFieldByTableId(tableId);
		//查询表系统字段
		List<HashMap<String,Object>> sysFields = fieldMapper.selectSysField(tableId);
		
		List<HashMap<String,Object>> cols = new ArrayList<HashMap<String,Object>>();
		List<HashMap<String,Object>> cols2 = new ArrayList<HashMap<String,Object>>();
		//ROW_ID需排在第一个
		for(HashMap<String,Object> sysField : sysFields){
			if("ROWID_EXIST".equals(sysField.get("NAME"))){
				cols.add(sysField);
			}else{
				cols2.add(sysField);
			}
		}
		
		cols.addAll(cols2);
		cols.addAll(tableCols);
		
		return cols;
		
	}
	
	/**
	 * 查询参数历史版本
	 * @param requestBean
	 * @param responseBean
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private void queryVersion(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		String vStartDate =(String)sysMap.get("vStartDate");
		String vEndDate =(String)sysMap.get("vEndDate");

		int tableId = (int) sysMap.get("tableId");
		String bankCode = (String)sysMap.get("bankCode");
		if(isZHOrgan(bankCode)) {
			bankCode = null;
		}

		
		//查询参数表
		McTable table = tableMapper.selectByPrimaryKey(tableId);
		
		
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
		List<HashMap<String,Object>> resultList = paramMapper.selectParamVersion(table.getTableName(),vStartDate,vEndDate, bankCode);
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
	 * 恢复历史版本
	 * @param requestBean
	 * @param responseBean
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private void changeVersion(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		Integer tableId = (Integer) sysMap.get("tableId");
		String versionNo = (String)sysMap.get("versionNo");
		String bankCode = (String)sysMap.get("bankCode");
		if(isZHOrgan(bankCode)) {
			bankCode = null;
		}
		//查询参数表
		McTable table = tableMapper.selectByPrimaryKey(tableId);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date=df.format(new Date());
		
		 //更新参数表结束日期为系统日期
		paramMapper.setParameterEndTime(table.getTableName(),date,bankCode);
		 //将参数表数据插入历史表中
		paramMapper.addParameterInHisTable(table.getTableName(),bankCode);

	    //删除参数表数据
		paramMapper.deleteParameter(table.getTableName(),bankCode);
		
		//历史版本转入参数表
		paramMapper.insertParameter(table.getTableName(), versionNo, bankCode);
		
		//删除历史的历史版本
		paramMapper.deleteHisParameter(table.getTableName(), versionNo, bankCode);
		
		 //结束日期清空
		//paramMapper.setParameterEndTime(table.getTableName(),"", bankCode);
		// 跟新增默认一样吧
        paramMapper.setParameterEndTime(table.getTableName(),"3000-12-31", bankCode);

		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("操作成功");
	}
	
	
	/**
	 * @author:		zheng.jw
	 * @throws Exception 
	 * @date:		2017年12月20日 下午4:19:16
	 * @Description:导入节假日信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void importOperation(RequestBean requestBean,ResponseBean responseBean) throws Exception {
		//获取前台传入参数
		int failNum = 0 ;
		int successNum = 0;
		List nameList = (List)requestBean.getSysMap().get("uploadFileList");
		Map map = (Map)nameList.get(0);
		String filePath = (String)map.get("saveFileName");
		int headerRowNum = (int)requestBean.getSysMap().get("headerRowNum");
		List<HashMap<String,String>> list = ImportUtil.importExcel(filePath,headerRowNum,true); //导入excel数据
		// 获取页面参数
		int tableId = (int) requestBean.getSysMap().get("tableId");
		String bankCode = (String)requestBean.getSysMap().get("bankCode");
		if(isZHOrgan(bankCode)) {
			bankCode = null;
		}
		//查询参数表
		McTable table = tableMapper.selectByPrimaryKey(tableId);		
		//获取参数表字段
		List<HashMap<String,Object>> tableCols = getTableCols(tableId);
		//获取当前时间yyyy-MM-dd HH:mm:ss
		String date=DateUtil.getTime19();
		List fieldValues = null;
		long start_time = System.currentTimeMillis();
		//备份当前参数表至历史表
		 //更新参数表结束日期为系统日期
		paramMapper.setParameterEndTime(table.getTableName(),date,bankCode);
		 //将参数表数据插入历史表中
		paramMapper.addParameterInHisTable(table.getTableName(),bankCode);
	    //删除参数表数据
		paramMapper.deleteParameter(table.getTableName(),bankCode);
		//Map countSite=new HashMap();
		for (int i = 0; i < list.size(); i++) { // 循环插入导入数据
			Map qMap = list.get(i); // 逐个定义变量，看是否需要处理
			if (BaseUtil.mapIsAllNull(qMap)) {
				logger.info("第" + i + "条数据为空");
				continue;
			}
			fieldValues = new ArrayList();
			//int RQCOUNT=0;//用来记录启用日期和停用日期执行的次数
			
			for(int j=0;j<tableCols.size();j++){
				//ROW_ID由程序自动生成
					if("QYRQ".equals(tableCols.get(j).get("NAME"))){
						//启用日期，获取当前时间
						fieldValues.add("to_date(" + "'" + date + "'" + ",'yyyy-MM-dd HH24:mi:ss')");
						//RQCOUNT++;//执行次数加1
					}else if("TYRQ".equals(tableCols.get(j).get("NAME"))){
						//停用日期
						fieldValues.add("to_date('3000-12-31','yyyy-MM-dd HH24:mi:ss')");
						//RQCOUNT++;//执行次数加1
					}else{
						String value = (String)qMap.get(j+"");
//						if(value.matches("^[0-9]*$")) {
//							fieldValues.add(value);//减去启用日期和停用日期的执行次数,最后得到正确的value
//						}else {
							fieldValues.add("'"+value+"'");
//						}
					}
			}
			StringBuffer addSql = new StringBuffer();
			addSql.append("insert into " + table.getTableName() + " ( ");
			for(int k=0;k<tableCols.size();k++){//遍历英文字段然后append
				HashMap<String, Object> tableCol = tableCols.get(k);
				addSql.append(tableCol.get("NAME"));
				if (k != tableCols.size() - 1) {
					addSql.append(",");
				}
			}
			addSql.append(") values(");
			for(int h=0;h<fieldValues.size();h++){//遍历数据然后append
				if(!BaseUtil.isBlank((String)fieldValues.get(h))){//判断导入的单元格的数据是否为空
					addSql.append(fieldValues.get(h));
				}else{
					addSql.append("null");//为空就插入null,为了防止两个逗号同时出现而出错
				}
				
				
				addSql.append(",");
			}
			String addSqlStr=addSql.toString();
			String str=addSqlStr.substring(0,addSqlStr.length()-1);
			String addSqlF= str+")";
			paramMapper.addParamData(addSqlF.toString());
			//新增参数
			//paramMapper.addParamData(table.getTableName(),tableCols, fieldValues);
			/*StringBuffer addSql = new StringBuffer();
			addSql.append("insert into " + table.getTableName() + " ( ");
			for (int j = 0; j < tableCols.size(); j++) {
				HashMap<String, Object> tableCol = tableCols.get(j);
				addSql.append(tableCol.get("NAME"));
				if("QYRQ".equals(tableCol.get("NAME"))||"TYRQ".equals(tableCol.get("NAME"))){
					countSite.put(j, tableCol.get("NAME"));
				}
				if (j != tableCols.size() - 1) {
					addSql.append(",");
				}
				
			}
			addSql.append(") values (");
			w:for (Object obj:qMap.keySet()) {
			//for (int k = 0; k < qMap.size(); k++) {
				// for(HashMap<String,Object> tableCol : tableCols){
				//if ("QYRQ".equals(colMap.get("NAME")) ||"TYRQ".equals(colMap.get("NAME"))) {
				for (Object count : countSite.keySet()) {
					if ((obj + "").equals(count.toString())) {
						
						addSql.append("to_date(" + "'" + qMap.get(obj) + "'" + ",'yyyy-MM-dd')");
						addSql.append(",");
						continue w;
					}

				}
				addSql.append("'" + qMap.get(obj) + "'");
				addSql.append(",");
		    }
				String addSqlStr=addSql.toString();
				String str=addSqlStr.substring(0,addSqlStr.length()-1);
				String addSqlF= str+")";
				paramMapper.addParamData(addSqlF.toString());*/
				// ROW_ID由程序自动生成
				/*
				 * if(!"ROW_ID".equals(tableCols.get(j).get("NAME"))){
				 * if("START_DATE".equals(tableCols.get(j).get("NAME"))){
				 * //启用日期，获取当前时间 fieldValues.add(date); }else
				 * if("START_DATE".equals(tableCols.get(j).get("NAME"))){ //停用日期
				 * fieldValues.add(""); }else{ fieldValues.add(qMap.get(j+""));
				 * } }
				 */
			

			// 新增参数
			// paramMapper.addParamData(table.getTableName(),tableCols,
			// fieldValues);
			successNum++;
			// 记录日志
		}
		long end_time = System.currentTimeMillis();
		logger.info("批量导入用时：" + (end_time - start_time)) ;
		String retMsg = "";
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		if (successNum == 0) {
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			retMsg = "导入失败,无信息导入";
			//回滚参数表数据
			//历史版本转入参数表
			paramMapper.insertParameter(table.getTableName(), date,bankCode);
			//删除历史的历史版本
			paramMapper.deleteHisParameter(table.getTableName(), date,bankCode);
			 //结束日期清空
			paramMapper.setParameterEndTime(table.getTableName(),"",bankCode);
			
		} else if (failNum == 0) {
			retMsg = "成功导入" + successNum + "条数据。";
		} else {
			retMsg = "成功导入" + successNum + "条数据，有" + failNum + "条数据无法导入。";
		}
		
		responseBean.setRetMsg(retMsg);		
	}
	
	/**
	 * 判断机构号是否总行机构号
	 * @param organNo
	 * @return
	 */
	private boolean isZHOrgan(String organNo) {
		if(organNo == null || "".equals(organNo)) {
			return false;
		}
		SysParameter sp = ARSConstants.SYSTEM_PARAMETER.get("PARENT_ORGAN_NO");
		if(sp != null) {
			return organNo.equals(sp.getParamValue());
		}else {
			return false;
		}
	}
	
	
	
	
	/**
	 * 新增参数
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void add(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
	
	/**
	 * 修改参数
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void update(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
	
	/**
	 * 删除参数
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void delete(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
	
	
		
	/**
	 * 其他操作
	 * @param requestBean
	 * @param responseBean
	 */
	private void otherOperation(RequestBean requestBean, ResponseBean responseBean)throws Exception{
			
		}


}
