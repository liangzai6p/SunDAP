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
import com.sunyard.ars.system.dao.mc.ViewColsMapper;
import com.sunyard.ars.system.pojo.mc.ViewCols;
import com.sunyard.ars.system.service.mc.IViewColsService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service("viewColsService")
@Transactional
public class ViewColsServiceImpl extends BaseService  implements IViewColsService{
	
	
	@Resource
	private ViewColsMapper viewColsMapper;
	
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
		}else if("viewColsInfo".equals(oper_type)){
			//查询视图列对应mc_field_tb字段
			viewColsInfo(requestBean, responseBean);
		}
	}
	
	/**
	 * 查询视图列
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		ViewCols viewCols = (ViewCols)requestBean.getParameterList().get(0);
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
		List resultList = viewColsMapper.selectBySelective(viewCols);
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
	 * 操作视图列--增删改
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private void submit(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		ViewCols viewCols =null;
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		List<ViewCols> addList = JSON.parseArray((String)sysMap.get("addList"),ViewCols.class);
		List<ViewCols> editList =  JSON.parseArray((String)sysMap.get("editList"),ViewCols.class);
		List<ViewCols> delList =  JSON.parseArray((String)sysMap.get("delList"),ViewCols.class);
		
		viewCols = new ViewCols();
		//删除时需先判断是否存在关联
		if(delList!=null && delList.size()>0){
			//循环拼装list
			List dList = new ArrayList();
			for(ViewCols v : delList){
				dList.add(v.getId());
			}
			
			HashMap<String, Object> condMap=null;
			int count=0;
			for(int i=0;i<DelRelate.viewCols.size();i++){
				condMap = new HashMap<String, Object>();
				condMap.put("relate", DelRelate.viewCols.get(i));
				condMap.put("delList", dList);		
				count = relateMapper.selectRelate(condMap);
				if(count > 0){
					//存在关联信息，不允许删除
					responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
					responseBean.setRetMsg("视图列存在关联，请解除关联关系后再进行删除操作！");
					return;
				}
			}
		}
				
		//执行表字段新增
		if(addList!=null && addList.size()>0){
			for(int j=0;j<addList.size();j++){
				viewCols = addList.get(j);
				viewColsMapper.insertSelective(viewCols);
			}
		}
		
		//执行字段修改
		if(editList!=null && editList.size()>0){
			for(int k=0;k<editList.size();k++){
				viewCols = editList.get(k);
				viewColsMapper.updateByPrimaryKeySelective(viewCols);
			}
		}
		
		//执行字段删除
		if(delList!=null && delList.size()>0){
			for(int h=0;h<delList.size();h++){
				viewCols = delList.get(h);
				viewColsMapper.deleteByPrimaryKey(viewCols.getId());
			}
		}
		
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("操作成功！");
		
	}
	
	
	/**
	 * 新增视图列
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void add(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
	
	/**
	 * 修改视图列
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void update(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
	
	/**
	 * 删除视图列
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
	 * 获取视图列详细信息
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void viewColsInfo(RequestBean requestBean, ResponseBean responseBean){

		//获取页面参数
		ViewCols viewCols = (ViewCols)requestBean.getParameterList().get(0);
		
		//执行查询
		List resultList = viewColsMapper.selectViewColsInfo(viewCols);
		Map retMap = new HashMap();
		retMap.put("resultList", resultList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	
	}
	

}
