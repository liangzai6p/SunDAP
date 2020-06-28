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
import com.sunyard.ars.system.dao.mc.DataSourceMapper;
import com.sunyard.ars.system.dao.mc.RelateMapper;
import com.sunyard.ars.system.pojo.mc.DataSource;
import com.sunyard.ars.system.service.mc.IMcDataSourceService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service("mcDataSourceService")
@Transactional
public class McDataSourceServiceImpl extends BaseService  implements IMcDataSourceService{
	
	@Resource
	private DataSourceMapper dataSourceMapper;
	
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
	 * 查询数据源
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean){
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		DataSource dataSource = (DataSource) requestBean.getParameterList().get(0);
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
		List resultList = dataSourceMapper.selectBySelective(dataSource);
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
	 * 新增数据源
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void add(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		DataSource dataSource = (DataSource) requestBean.getParameterList().get(0);
		
		DataSource ds = dataSourceMapper.selectByName(dataSource.getName());
		if(ds != null) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("数据源已存在！");
		}else  {
			//新增数据源
			dataSourceMapper.insertSelective(dataSource);
			Map retMap = new HashMap();
			retMap.put("dsId", dataSource.getId());
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("添加成功！");
			
			//添加日志
			String log = "警报规则配置添加数据源" + dataSource.getName();
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
		}
		
	}
	
	/**
	 * 修改数据源
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void update(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		DataSource dataSource = (DataSource) requestBean.getParameterList().get(0);
		//查询旧数据
		DataSource oldDs = dataSourceMapper.selectByPrimaryKey(dataSource.getId());
		
		//如果数据源名称改变，需判断修改后的数据源名称是否已存在
		if(!oldDs.getName().equals(dataSource.getName())){
			DataSource ds = dataSourceMapper.selectByName(dataSource.getName());
			if(ds != null) {
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("数据源已存在！");
				return;
			}
		}
		//判断密码是否被修改过
		if(!dataSource.getPwd().equals("****")){
			dataSource.setPwd(dataSource.getPwd());
		}else{
			dataSource.setPwd(oldDs.getPwd());
		}
		
		//修改数据源
		dataSourceMapper.updateByPrimaryKeySelective(dataSource);
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
		
		//添加日志
		String log = "警报规则配置修改id为"+dataSource.getId()+"的数据源";
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
	}
	
	/**
	 * 删除数据源
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void delete(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		List delList =(List)sysMap.get("delList");
						
		HashMap condMap =null;
		int count =0;
		//删除前判断是否存在关联
		for(int j=0;j<DelRelate.datasource.size();j++){
			condMap = new HashMap<String, Object>();
			condMap.put("relate", DelRelate.datasource.get(j));
			condMap.put("delList", delList);		
			count = relateMapper.selectRelate(condMap);
			if(count > 0){
				//存在关联信息，不允许删除
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("表被关联，请解除关联关系后再进行删除操作！");
				return;
			}
		}
		
		//删除数据源
		for(int i=0;i<delList.size();i++){
			dataSourceMapper.deleteByPrimaryKey((int)delList.get(i));
			//添加日志
			String log = "警报规则配置修改id为"+delList.get(i)+"的数据源";
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
}
