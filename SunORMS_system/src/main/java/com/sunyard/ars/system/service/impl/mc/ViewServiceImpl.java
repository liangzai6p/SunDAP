package com.sunyard.ars.system.service.impl.mc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.pojo.DelRelate;
import com.sunyard.ars.system.dao.mc.RelateMapper;
import com.sunyard.ars.system.dao.mc.ViewMapper;
import com.sunyard.ars.system.pojo.mc.View;
import com.sunyard.ars.system.service.mc.IViewService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service("viewService")
@Transactional
public class ViewServiceImpl extends BaseService  implements IViewService{
	
	@Resource
	private ViewMapper viewMapper;
	
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
		} 
	}
	
	/**
	 * 查询视图
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		View view = (View)requestBean.getParameterList().get(0);
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
		List resultList = viewMapper.selectViewBySelective(view);
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
	 * 新增视图
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void add(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		View view = (View)requestBean.getParameterList().get(0);
		//执行视图新增
		viewMapper.insertSelective(view);
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		retMap.put("id", view.getId());
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("添加成功！");
		//添加日志
		String log = "警报规则配置模型的视图配置新增相应预警项id为"+view.getName()+"的视图";
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
		
	}
	
	/**
	 * 修改视图
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void update(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		View view =null;
		//获取页面参数
		List list = requestBean.getParameterList();
		//执行视图修改
		for(int i=0;i<list.size();i++){
			view = (View)list.get(i);
			viewMapper.updateByPrimaryKeySelective(view);
			//添加日志
			String log = "警报规则配置模型的视图配置更新相应预警项id为"+view.getModelId()+"的视图";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
		}
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
		
		
		}
	
	/**
	 * 删除视图
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void delete(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		Map sysMap = requestBean.getSysMap();
		View view =null;
		//获取页面参数
		List list = requestBean.getParameterList();
		List delList = (List)sysMap.get("delList");
		//删除前判断视图是否存在关联
		HashMap condMap =null;
		int count =0;
		for(int j=0;j<DelRelate.view.size();j++){
			condMap = new HashMap<String, Object>();
			condMap.put("relate", DelRelate.view.get(j));
			condMap.put("delList", delList);		
			count = relateMapper.selectRelate(condMap);
			if(count > 0){
				//存在关联信息，不允许删除
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("表被关联，请解除关联关系后再进行删除操作！");
				return;
			}
		}
		//执行视图删除
		for(int i=0;i<list.size();i++){
			view = (View)list.get(i);
			viewMapper.deleteByPrimaryKey(view.getId());
			//添加日志
			String log = "警报规则配置模型的视图配置删除相应预警项id为"+view.getModelId()+"的视图";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
		}
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
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
	

}
