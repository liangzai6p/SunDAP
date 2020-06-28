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
import com.sunyard.ars.common.pojo.DelRelate;
import com.sunyard.ars.system.dao.mc.RelateMapper;
import com.sunyard.ars.system.dao.mc.ViewConditionMapper;
import com.sunyard.ars.system.dao.mc.ViewConditionMapper;
import com.sunyard.ars.system.pojo.mc.ViewCondition;
import com.sunyard.ars.system.service.mc.IViewConditionService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service("viewConditionService")
@Transactional
public class ViewConditionServiceImpl extends BaseService  implements IViewConditionService{
	
	
	@Resource
	private ViewConditionMapper viewConditionMapper;
	
	@Resource
	private RelateMapper relateMapper;
	
	
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
		} else if("submit".equals(oper_type)){
			//提交操作
			submit(requestBean, responseBean);
		}
	}
	
	/**
	 * 查询视图条件
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		ViewCondition viewCondition = (ViewCondition)requestBean.getParameterList().get(0);
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
		List resultList = viewConditionMapper.selectBySelective(viewCondition);
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
	 * 操作视图条件--增删改
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private void submit(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		ViewCondition viewCondition =null;
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		List<ViewCondition> addList = JSON.parseArray((String)sysMap.get("addList"),ViewCondition.class);
		List<ViewCondition> editList =  JSON.parseArray((String)sysMap.get("editList"),ViewCondition.class);
		List<ViewCondition> delList =  JSON.parseArray((String)sysMap.get("delList"),ViewCondition.class);
		
		viewCondition = new ViewCondition();
				
		//执行表字段新增
		if(addList!=null && addList.size()>0){
			for(int j=0;j<addList.size();j++){
				viewCondition = addList.get(j);
				viewConditionMapper.insertSelective(viewCondition);
			}
		}
		
		//执行字段修改
		if(editList!=null && editList.size()>0){
			for(int k=0;k<editList.size();k++){
				viewCondition = editList.get(k);
				viewConditionMapper.updateByPrimaryKeySelective(viewCondition);
			}
		}
		
		//执行字段删除
		if(delList!=null && delList.size()>0){
			for(int h=0;h<delList.size();h++){
				viewCondition = delList.get(h);
				viewConditionMapper.deleteByPrimaryKey(viewCondition.getId());
			}
		}
		
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("操作成功！");
		
	}
	
	
	/**
	 * 新增视图条件
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void add(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
	
	/**
	 * 修改视图条件
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void update(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
	
	/**
	 * 删除视图条件
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
