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
import com.sunyard.ars.system.dao.mc.McAutoyjFieldTbMapper;
import com.sunyard.ars.system.dao.mc.RelateMapper;
import com.sunyard.ars.system.dao.mc.ViewMapper;
import com.sunyard.ars.system.pojo.mc.McAutoyjFieldTb;
import com.sunyard.ars.system.pojo.mc.View;
import com.sunyard.ars.system.service.mc.IMcAutoyjFieldService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service("mcAutoyjFieldService")
@Transactional
public class McAutoyjFieldServiceImpl extends BaseService  implements IMcAutoyjFieldService{
	
	@Resource
	private McAutoyjFieldTbMapper mcAutoyjFieldTbMapper;
	
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
		} else if ("submit".equals(oper_type)) { 
			// 新增
			submit(requestBean, responseBean);
		} 
	}
	
	/**
	 * 查询自动预警配置字段
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		McAutoyjFieldTb mcAutoyjFieldTb = (McAutoyjFieldTb)requestBean.getParameterList().get(0);
		String modelId=mcAutoyjFieldTb.getModelId();
		//执行查询
		McAutoyjFieldTb resultMcAutoyjFieldTb = mcAutoyjFieldTbMapper.selectByPrimaryKey(modelId);
		Map retMap = new HashMap();
		retMap.put("resultList", resultMcAutoyjFieldTb);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 新增自动预警配置字段
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void submit(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		McAutoyjFieldTb mcAutoyjFieldTb = (McAutoyjFieldTb)requestBean.getParameterList().get(0);
		//执行自动预警配置新增
		mcAutoyjFieldTbMapper.deleteByPrimaryKey(mcAutoyjFieldTb.getModelId());
		mcAutoyjFieldTbMapper.insertSelective(mcAutoyjFieldTb);
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("更新成功！");
		
		//添加日志
		String log = "警报规则配置新增模型id为"+mcAutoyjFieldTb.getModelId()+"的自动预警配置";
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
		
	}
	
	

}
