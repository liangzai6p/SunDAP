package com.sunyard.ars.system.service.impl.mc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sunyard.aos.common.util.BaseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.pojo.TmpData;
import com.sunyard.ars.system.dao.mc.McFieldMapper;
import com.sunyard.ars.system.dao.mc.McTableFieldMapper;
import com.sunyard.ars.system.dao.mc.McTableMapper;
import com.sunyard.ars.system.pojo.mc.McTable;
import com.sunyard.ars.system.pojo.mc.McTableField;
import com.sunyard.ars.system.service.mc.IMcTableFieldService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

import ch.qos.logback.core.joran.conditional.ElseAction;

@Service("mcTableFieldService")
@Transactional
public class McTableFieldServiceImpl extends BaseService  implements IMcTableFieldService{
	
	@Resource
	private McTableFieldMapper tableFieldMapper;
	
	@Resource
	private McFieldMapper fieldMapper;
	
	@Resource
	private McTableMapper tableMapper;
	
	
	
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
		} else if("fieldNames".equals(oper_type)){
			//获取所有模型字段和系统字段名
			queryFieldNames(requestBean, responseBean);
		}else if("tableFields".equals(oper_type)){
			//获取所有模型字段和系统字段
			queryTableFields(requestBean, responseBean);
		}else if("submit".equals(oper_type)){
			submit(requestBean, responseBean);
		}else if ("selectTableInfoByTableId".equals(oper_type)) {
			selectTableInfoByTableId(requestBean, responseBean);
		}
	}
	private void selectTableInfoByTableId(RequestBean requestBean, ResponseBean responseBean) {
		McTableField tableField = (McTableField)requestBean.getParameterList().get(0);
		List<McTableField> list = tableFieldMapper.selectBySelective(tableField);
		Map retMap = new HashMap();
		retMap.put("returnList", list);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * 查询表字段
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		McTableField tableField = (McTableField)requestBean.getParameterList().get(0);
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
		//执行查询
		List resultList = tableFieldMapper.selectBySelective(tableField);
		Map retMap = new HashMap();
		long totalCount = page.getTotal();
		retMap.put("pageSize", pageSize);
		retMap.put("allRow", totalCount);
		retMap.put("resultList", resultList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	
	/**
	 * 新增表字段
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void submit(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		McTableField tableField =null;
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		List<McTableField> addList = JSON.parseArray((String)sysMap.get("addList"),McTableField.class);
		List<McTableField> editList =  JSON.parseArray((String)sysMap.get("editList"),McTableField.class);
		List<McTableField> delList =  JSON.parseArray((String)sysMap.get("delList"),McTableField.class);
		//获取表id
		Integer tableId = (Integer)sysMap.get("tableId");
		//获取表名
		McTable table = tableMapper.selectByPrimaryKey(tableId);
		
		//判断是否可以删除
		if(delList!=null){
			List<McTableField> canDelList = new ArrayList<McTableField>();
			//判断字段是否可以被删除，表未创建或者表已创建但表内不存在该字段时可以删除
			for(int i=0;i<delList.size();i++){
				tableField = (McTableField)delList.get(i);
				int count =	fieldMapper.selectTableCol(ARSConstants.DB_TYPE, BaseUtil.filterSqlParam(table.getTableName().toUpperCase()), BaseUtil.filterSqlParam(tableField.getName().toUpperCase()));
				if(count == 0){
					canDelList.add(tableField);
				}
			}
			
			if(delList.size() != canDelList.size()){
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("包含已创建表字段，无法删除");
				return;
			}
		}
		
		//判断表字段是否存在重复
		boolean flag = canModify(tableId, delList, editList, addList);
		if(!flag){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("存在重复字段，请检查");
			return;
		}
		
		//执行表字段新增
		if(addList!=null){
			for(int j=0;j<addList.size();j++){
				tableField = (McTableField)addList.get(j);
				tableFieldMapper.insertSelective(tableField);
				//添加日志
				String log = "警报规则配置的表定义的字段配置，执行新增表id为" + tableField.getTableId()
					+ "字段名为" + tableField.getName() + "的字段";
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
			}
		}
		
		//执行字段修改
		if(editList!=null){
			for(int k=0;k<editList.size();k++){
				tableField = (McTableField)editList.get(k);
				tableFieldMapper.updateByPrimaryKeySelective(tableField);
				//添加日志
				String log = "警报规则配置的表定义的字段配置，执行修改表id为" + tableField.getTableId()
					+"的字段";
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
			}
		}
		
		//执行字段删除
		if(delList!=null){
			for(int h=0;h<delList.size();h++){
				tableField = (McTableField)delList.get(h);
				tableFieldMapper.deleteByPrimaryKey(tableField.getId());
				//添加日志
				String log = "警报规则配置的表定义的字段配置，执行删除表id为" + tableField.getTableId()
					+ "字段名为" + tableField.getName() + "的字段";
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
			}
		}
		
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("操作成功！");
		
	}
	
	
	
	
	
	
	
	/**
	 * 新增表字段
	 * @param requestBean
	 * @param responseBean
	 */
	private void add(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
	
	/**
	 * 修改表字段
	 * @param requestBean
	 * @param responseBean
	 */
	private void update(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
	
	/**
	 * 删除表字段
	 * @param requestBean
	 * @param responseBean
	 */
	private void delete(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
	
	/**
	 * 其他操作
	 * @param requestBean
	 * @param responseBean
	 */
	private void otherOperation(RequestBean requestBean, ResponseBean responseBean)throws Exception{
			
	}
	
	/**
	 * 查询表字段
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void queryTableFields(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		String queryType=(String)sysMap.get("queryType");
		McTableField tableField = (McTableField)requestBean.getParameterList().get(0);
		
		//查询已配置表字段
		List<HashMap<String,Object>> fields = tableFieldMapper.selectTableFieldByTableId(tableField.getTableId());
		if("0".equals(queryType)){
			//查询表系统字段
			List<HashMap<String,Object>> sysFields = fieldMapper.selectSysField(tableField.getTableId());
			fields.addAll(sysFields);
		}
		Map retMap = new HashMap();
		retMap.put("resultList", fields);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	
	/**
	 * 查询表字段名
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void queryFieldNames(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
		//获取页面参数
		McTableField tableField = (McTableField)requestBean.getParameterList().get(0);
		
		//查询已配置表字段
		List<HashMap<String,Object>> fields = tableFieldMapper.selectTableFieldByTableId(tableField.getTableId());
		//查询表系统字段
		List<HashMap<String,Object>> modelFields = fieldMapper.selectSysField(tableField.getTableId());
		fields.addAll(modelFields);
		//装载所有字段
		List<String> fieldNames = new ArrayList<String>();
		for(HashMap map : fields){
			fieldNames.add(map.get("NAME")+"");
		}
		Map retMap = new HashMap();
		retMap.put("fieldNames", fieldNames);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
		

	
	@SuppressWarnings("unused")
	private boolean canModify(Integer tableId,
			List<McTableField> delTableFields,
			List<McTableField> updateTableFields,
			List<McTableField> saveTableFields) {
		//先查询mc_table_field_tb，查询已配置的字段
		McTableField tf = new McTableField();
		tf.setTableId(tableId);
		List<McTableField> oldTableFields = tableFieldMapper.selectBySelective(tf);
		
		List<McTableField> changeTableFiedlList = new ArrayList<McTableField>();
		if(updateTableFields !=null){
			changeTableFiedlList.addAll(updateTableFields);
		}
		if(delTableFields !=null){
			changeTableFiedlList.addAll(delTableFields);
		}				
		
		//删除的字段和修改的字段都移除
		for (McTableField mcTableFieldTb : changeTableFiedlList) {
			for (McTableField oldmcTableFieldTb : oldTableFields) {
				if(mcTableFieldTb.getId().equals(oldmcTableFieldTb.getId())){
					oldTableFields.remove(oldmcTableFieldTb);
					break;
				}
			}
		}
		//把修改后的字段加入队列
		if(updateTableFields != null){
			oldTableFields.addAll(updateTableFields);
		}
		//放入新增字段
		if(saveTableFields != null){
			oldTableFields.addAll(saveTableFields);
		}
		
	
		HashMap<String,String> newFieldMap = new HashMap<String,String>();
		for (McTableField mcTableFieldTb : oldTableFields) {
			newFieldMap.put(mcTableFieldTb.getName(), mcTableFieldTb.getName());
		}
		
		//查询系统配置表
		List<HashMap<String,Object>> map =  fieldMapper.selectSysField(tableId);
		for(HashMap<String,Object> sysField : map){
			newFieldMap.put(sysField.get("NAME")+"", sysField.get("NAME")+"");
		}
		
		if(newFieldMap.size() != (oldTableFields.size()+map.size())){
			return false;
		}
		return true;
	}
	

}
