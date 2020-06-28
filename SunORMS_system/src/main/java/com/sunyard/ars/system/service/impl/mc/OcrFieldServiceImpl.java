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
import com.sunyard.ars.system.dao.mc.OcrFieldMapper;
import com.sunyard.ars.system.pojo.mc.OcrField;
import com.sunyard.ars.system.pojo.mc.McTable;
import com.sunyard.ars.system.pojo.mc.OcrField;
import com.sunyard.ars.system.service.mc.IOcrFieldService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service("ocrFieldService")
@Transactional
public class OcrFieldServiceImpl extends BaseService  implements IOcrFieldService{
	
	@Resource
	private OcrFieldMapper ocrFieldMapper;
	
	
	
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
		} 
	}
	
	/**
	 * 查询识别字段
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		OcrField ocrField = (OcrField)requestBean.getParameterList().get(0);
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
		List resultList = ocrFieldMapper.selectBySelective(ocrField);
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
	 * 操作识别字段--增删改
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void submit(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		OcrField ocrField =null;
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		List<OcrField> addList = JSON.parseArray((String)sysMap.get("addList"),OcrField.class);
		List<OcrField> editList =  JSON.parseArray((String)sysMap.get("editList"),OcrField.class);
		List<OcrField> delList =  JSON.parseArray((String)sysMap.get("delList"),OcrField.class);
	    
		Integer modelId = (Integer)sysMap.get("modelId");
		ocrField = new OcrField();
		ocrField.setModelId(modelId);
		//获取模型已配置的识别字段
		List<OcrField> oldList = ocrFieldMapper.selectBySelective(ocrField);
		
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
				ocrField = addList.get(j);
				ocrFieldMapper.insertSelective(ocrField);
			}
		}
		
		//执行字段修改
		if(editList!=null){
			for(int k=0;k<editList.size();k++){
				ocrField = editList.get(k);
				ocrFieldMapper.updateByPrimaryKeySelective(ocrField);
			}
		}
		
		//执行字段删除
		if(delList!=null){
			for(int h=0;h<delList.size();h++){
				ocrField = delList.get(h);
				ocrFieldMapper.deleteByPrimaryKey(ocrField.getId());
			}
		}
		
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("操作成功！");
		
	}
	
	
	
	/**
	 * 新增识别字段
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void add(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
	
	/**
	 * 修改识别字段
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void update(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
	
	/**
	 * 删除识别字段
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
	 * @param oldOcrFieldList
	 * @param saveOcrFields
	 * @param delOcrFields
	 * @param updateOcrFields
	 * @return
	 */
	private boolean canModify(List<OcrField> oldOcrFieldList,List<OcrField> saveOcrFields,
			List<OcrField> delOcrFields,
			List<OcrField> updateOcrFields) {
		// TODO Auto-generated method stub
		List<OcrField> changeOcrFieldlList = new ArrayList<OcrField>();
		changeOcrFieldlList.addAll(delOcrFields);
		changeOcrFieldlList.addAll(updateOcrFields);
		//删除的字段和修改的字段都移除
		for (OcrField mcOcrFieldTb : changeOcrFieldlList) {
			for (OcrField oldOcrField : oldOcrFieldList) {
				if(mcOcrFieldTb.getId().equals(oldOcrField.getId())){
					oldOcrFieldList.remove(oldOcrField);
					break;
				}
			}
		}
		oldOcrFieldList.addAll(updateOcrFields);
		oldOcrFieldList.addAll(saveOcrFields);
		HashMap<Integer,OcrField> newFieldMap = new HashMap<Integer,OcrField>();
		for (OcrField mcOcrFieldTb : oldOcrFieldList) {
			newFieldMap.put(mcOcrFieldTb.getTableFieldId(), mcOcrFieldTb);
		}
		if(newFieldMap.size() != oldOcrFieldList.size()){
			return false;
		}
		return true;
	}
	

}
