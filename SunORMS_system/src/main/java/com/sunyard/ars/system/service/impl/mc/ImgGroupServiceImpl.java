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
import com.sunyard.ars.system.dao.mc.QueryImgMapper;
import com.sunyard.ars.system.pojo.mc.QueryImg;
import com.sunyard.ars.system.service.mc.ImgGroupService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service("imgGroupService")
@Transactional
public class ImgGroupServiceImpl extends BaseService  implements ImgGroupService{
	
	@Resource
	private QueryImgMapper queryImgMapper;
	
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
	 * 查询分组
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		QueryImg queryImg = (QueryImg)requestBean.getParameterList().get(0);
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
		List resultList = queryImgMapper.selectBySelective(queryImg);
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
	 * 新增分组
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void add(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		QueryImg queryImg =null;
		//获取页面参数
		List list = requestBean.getParameterList();
		//执行分组新增
		for(int i=0;i<list.size();i++){
			queryImg = (QueryImg)list.get(i);
			queryImgMapper.insertSelective(queryImg);
			
			//添加日志
			String log = "新增警报规则配置模型的看图分组配置";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
		}
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("添加成功！");
		
	}
	
	/**
	 * 修改分组
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void update(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		QueryImg queryImg =null;
		//获取页面参数
		List list = requestBean.getParameterList();
		//执行分组新增
		for(int i=0;i<list.size();i++){
			queryImg = (QueryImg)list.get(i);
			queryImgMapper.updateByPrimaryKeySelective(queryImg);
			
			//添加日志
			String log = "修改警报规则配置模型的看图分组配置";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
		}
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
		
		}
	
	/**
	 * 删除分组
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void delete(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		QueryImg queryImg =null;
		//获取页面参数
		List list = requestBean.getParameterList();
		//执行分组删除
		for(int i=0;i<list.size();i++){
			queryImg = (QueryImg)list.get(i);
			queryImgMapper.deleteByPrimaryKey(queryImg.getId());	
			//添加日志
			String log = "删除警报规则配置模型的看图分组配置";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
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
