package com.sunyard.ars.system.service.impl.mc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.dao.mc.ExhibitFieldMapper;
import com.sunyard.ars.system.pojo.mc.ExhibitField;
import com.sunyard.ars.system.pojo.mc.McTable;
import com.sunyard.ars.system.pojo.mc.ExhibitField;
import com.sunyard.ars.system.service.mc.IExhibitFieldService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service("exhibitFieldService")
@Transactional
public class ExhibitFieldServiceImpl extends BaseService  implements IExhibitFieldService{
	
	@Resource
	private ExhibitFieldMapper exhibitFieldMapper;
	
	
	
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
		}else if("submit".equals(oper_type)){
			submit(requestBean, responseBean);
		}else if("selectEnName".equals(oper_type)){	//查找英文名		
			selectEnName(requestBean, responseBean);
		}else if("showIsTuoMin".equals(oper_type)){//判断是否脱敏
			showIsTuoMin(requestBean, responseBean);
		}else if("queryExhibitFieldCNT".equals(oper_type)){//查找是否有展现字段
            queryExhibitFieldCNT(requestBean, responseBean);
        }
	}

	/**
	 * 查询展现字段
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		ExhibitField exhibitField = (ExhibitField)requestBean.getParameterList().get(0);
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
		List resultList = exhibitFieldMapper.selectBySelective(exhibitField);
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
	 * 操作展现字段--增删改
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void submit(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		ExhibitField exhibitField =null;
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		List<ExhibitField> addList = JSON.parseArray((String)sysMap.get("addList"),ExhibitField.class);
		List<ExhibitField> editList =  JSON.parseArray((String)sysMap.get("editList"),ExhibitField.class);
		List<ExhibitField> delList =  JSON.parseArray((String)sysMap.get("delList"),ExhibitField.class);
	    
		Integer modelId = Integer.parseInt(sysMap.get("modelId").toString());
		exhibitField = new ExhibitField();
		exhibitField.setModelId(modelId);
		//获取模型已配置的展现字段
		List<ExhibitField> oldList = exhibitFieldMapper.selectBySelective(exhibitField);
		
		//判断表字段是否存在重复
		boolean flag = canModify(oldList, addList, delList, editList );
		if(!flag){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("存在重复字段，请检查");
			return;
		}
		
		//执行表字段新增
		if(addList!=null){
			for(int j=0;j<addList.size();j++){
				exhibitField = addList.get(j);
				exhibitFieldMapper.insertSelective(exhibitField);
				
				//添加日志
				String log = "警报规则配置中模型的展现字段配置新增字段" + exhibitField.getEnName();
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
			}
		}
		
		//执行字段修改
		if(editList!=null){
			for(int k=0;k<editList.size();k++){
				exhibitField = editList.get(k);
				exhibitFieldMapper.updateByPrimaryKeySelective(exhibitField);
				
				//添加日志
				String log = "警报规则配置中模型的展现字段配置修改id为" + exhibitField.getId() + "的字段!";
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
			}
		}
		
		//执行字段删除
		if(delList!=null){
			for(int h=0;h<delList.size();h++){
				exhibitField = delList.get(h);
				exhibitFieldMapper.deleteByPrimaryKey(exhibitField.getId());
				
				//添加日志
				String log = "警报规则配置中模型的展现字段配置删除id为" + exhibitField.getId() + "的字段!";
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
			}
		}
		//更新展现字段中文名，如果为空用字段中文名填充
		exhibitFieldMapper.updateExhibitChName(modelId);
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("操作成功！");
		
	}
	
	
	
	/**
	 * 新增展现字段
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void add(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
	
	/**
	 * 修改展现字段
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void update(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
	
	/**
	 * 删除展现字段
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
	
	/**
	 * 判断是否存在重复
	 * @param oldExhibitFieldList
	 * @param saveExhibitFields
	 * @param delExhibitFields
	 * @param updateExhibitFields
	 * @return
	 */
	private boolean canModify(List<ExhibitField> oldExhibitFieldList,List<ExhibitField> saveExhibitFields,
			List<ExhibitField> delExhibitFields,
			List<ExhibitField> updateExhibitFields) {
		// TODO Auto-generated method stub
		List<ExhibitField> changeExhibitFieldlList = new ArrayList<ExhibitField>();
		changeExhibitFieldlList.addAll(delExhibitFields);
		changeExhibitFieldlList.addAll(updateExhibitFields);
		//删除的字段和修改的字段都移除
		for (ExhibitField mcExhibitFieldTb : changeExhibitFieldlList) {
			for (ExhibitField oldExhibitField : oldExhibitFieldList) {
				if(mcExhibitFieldTb.getId().equals(oldExhibitField.getId())){
					oldExhibitFieldList.remove(oldExhibitField);
					break;
				}
			}
		}
		oldExhibitFieldList.addAll(updateExhibitFields);
		oldExhibitFieldList.addAll(saveExhibitFields);
		HashMap<Integer,ExhibitField> newFieldMap = new HashMap<Integer,ExhibitField>();
		for (ExhibitField mcExhibitFieldTb : oldExhibitFieldList) {
			newFieldMap.put(mcExhibitFieldTb.getTableFieldId(), mcExhibitFieldTb);
		}
		if(newFieldMap.size() != oldExhibitFieldList.size()){
			return false;
		}
		return true;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void selectEnName(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		ExhibitField exhibitField = (ExhibitField)requestBean.getParameterList().get(0);
		/*int currentPage = (int)sysMap.get("currentPage");
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
		Page page = PageHelper.startPage(currentPage, pageSize);*/
		//执行查询
		List resultList = exhibitFieldMapper.selectEnName(exhibitField);
		Map retMap = new HashMap();
		//long totalCount = page.getTotal();
		//retMap.put("pageSize", pageSize);
		//retMap.put("allRow", totalCount);
		retMap.put("resultList", resultList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void showIsTuoMin(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		ExhibitField exhibitField = (ExhibitField)requestBean.getParameterList().get(0);
		/*int currentPage = (int)sysMap.get("currentPage");
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
		Page page = PageHelper.startPage(currentPage, pageSize);*/
		//执行查询
		List resultList = exhibitFieldMapper.showIsTuoMin(exhibitField);
		Map retMap = new HashMap();
		//long totalCount = page.getTotal();
		//retMap.put("pageSize", pageSize);
		//retMap.put("allRow", totalCount);
		retMap.put("resultList", resultList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	//查找展现字段个数,如果有则不允许修改模型种类和存放数据的表名
    private void queryExhibitFieldCNT(RequestBean requestBean, ResponseBean responseBean)throws Exception{
        //获取页面参数
        ExhibitField exhibitField = (ExhibitField)requestBean.getParameterList().get(0);
        //执行查询
        List resultList = exhibitFieldMapper.selectBySelective(exhibitField);
        Map retMap = new HashMap();
        retMap.put("size", resultList.size());
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
    }

}
