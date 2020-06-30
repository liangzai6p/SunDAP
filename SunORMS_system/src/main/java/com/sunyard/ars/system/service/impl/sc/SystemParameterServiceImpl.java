package com.sunyard.ars.system.service.impl.sc;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.dao.sc.SystemParameterMapper;
import com.sunyard.ars.system.init.SystemInitialize;
import com.sunyard.ars.system.service.sc.ISystemParameterService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;
import com.sunyard.cop.IF.mybatis.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("systemParameterService")
@Transactional
public class SystemParameterServiceImpl extends BaseService implements ISystemParameterService {
	
	@Resource
	private SystemParameterMapper systemParameterMapper;
	
	@Resource
	private SystemInitialize systemInit;

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void add(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		SysParameter sp = (SysParameter) requestBean.getParameterList().get(0);
		User user = BaseUtil.getLoginUser();
		String lastModiDate = BaseUtil.getCurrentTimeStr();
		String modifyUser = user.getUserNo();
		sp.setBankNo(user.getBankNo());
		sp.setSystemNo(user.getSystemNo());
		sp.setProjectNo(user.getProjectNo());
		sp.setModifyUser(modifyUser);;
		sp.setLastModiDate(lastModiDate);
		SysParameter spExit = systemParameterMapper.selectByPrimaryKey(sp.getParamItem(), sp.getBankNo(), sp.getSystemNo(), sp.getProjectNo());
		if(spExit != null) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("已存在相同数据！");
		}else  {
			systemParameterMapper.insert(sp);
			String	log="参数项"+sp.getParamItem()+"参数描述"+sp.getParamDesc()+"在系统参数表中被创建！";
			//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_1, log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
			Map retMap = new HashMap();
			retMap.put("lastModiDate", lastModiDate);
			retMap.put("modifyUser", modifyUser);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("添加成功！");
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		List delList = requestBean.getParameterList();
		User user = BaseUtil.getLoginUser();
		for(int i=0; i<delList.size(); i++) {
			SysParameter sp = (SysParameter)delList.get(i);
			sp.setBankNo(user.getBankNo());
			sp.setSystemNo(user.getSystemNo());
			sp.setProjectNo(user.getProjectNo());
			systemParameterMapper.deleteByPrimaryKey(sp.getParamItem(), sp.getBankNo(), sp.getSystemNo(), sp.getProjectNo());
			String	log="参数项"+sp.getParamItem()+"在系统参数表中被删除！";
			//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_2, log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功！");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		SysParameter sp = (SysParameter) requestBean.getParameterList().get(0);
		User user = BaseUtil.getLoginUser();
		String lastModiDate = BaseUtil.getCurrentTimeStr();
		String modifyUser = user.getUserNo();
		sp.setBankNo(user.getBankNo());
		sp.setSystemNo(user.getSystemNo());
		sp.setProjectNo(user.getProjectNo());
		sp.setModifyUser(modifyUser);;
		sp.setLastModiDate(lastModiDate);
		systemParameterMapper.updateByPrimaryKey(sp);
		String	log="参数项"+sp.getParamItem()+"参数描述"+sp.getParamDesc()+"在系统参数表中被修改！";
		//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_3, log);
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
		Map retMap = new HashMap();
		retMap.put("lastModiDate", lastModiDate);
		retMap.put("modifyUser", modifyUser);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		SysParameter sp = (SysParameter) requestBean.getParameterList().get(0);
		User user = BaseUtil.getLoginUser();
		sp.setBankNo(user.getBankNo());
		sp.setSystemNo(user.getSystemNo());
		sp.setProjectNo(user.getProjectNo());
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
		
		Page page = PageHelper.startPage(currentPage, pageSize);
		List resultList = systemParameterMapper.selectBySelective(sp);
		long totalCount = page.getTotal();
		Map retMap = new HashMap();
		retMap.put("currentPage", currentPage);
		retMap.put("pageSize", pageSize);
		retMap.put("totalNum", totalCount);
		retMap.put("returnList", resultList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		String otherOperation = (String)sysMap.get("other_operation");
		if("refresh".equals(otherOperation)) {
			//从系统参数表加载的内容需重新初始化 
			systemInit.systemInit();
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("刷新成功");
		}else if("getSysParamFromMemory".equals(otherOperation)) {
			String paramItem = (String)sysMap.get("param_item");
			Map retMap  = new HashMap();
			retMap.put("sysParam", ARSConstants.SYSTEM_PARAMETER.get(paramItem));
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("参数信息查询成功!");
			responseBean.setRetMap(retMap);
		}
	}

}
