package com.sunyard.ars.system.service.impl.busm;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;

import com.sunyard.ars.system.bean.busm.EmpLog;
import com.sunyard.ars.system.bean.busm.UserBean;
import com.sunyard.ars.system.bean.busm.UserRole;
import com.sunyard.ars.system.dao.busm.EmpDao;
import com.sunyard.ars.system.dao.busm.EmpLogDao;

import com.sunyard.ars.system.dao.busm.UserDao;
import com.sunyard.ars.system.dao.busm.UserRoleMapper;
import com.sunyard.ars.system.service.busm.IEmpService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;


@Service("empService")
@Transactional
public class EmpServiceImpl  extends BaseService implements IEmpService {

	@Resource
	private EmpDao empDao;
	@Resource
	private UserDao userDao;
	@Resource
	private EmpLogDao empLogDao;
	@Resource
	private UserRoleMapper userRoleMapper;
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		// TODO Auto-generated method stub
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}
	@Override
	@SuppressWarnings({ "rawtypes" })
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		
		// 获取操作标识
		String oper_type = (String) sysMap.get("oper_type");
		if ("passwordEmp".equals(oper_type)) {
			// 查询
			passwordEmp(requestBean, responseBean);
		}else if ("empLogQuery".equals(oper_type)) {
			empLogQuery(requestBean, responseBean);
		}else if ("getempMoudle".equals(oper_type)) {
			getempMoudle(requestBean, responseBean);
		}else if ("getempMethod".equals(oper_type)) {
			getempMethod(requestBean, responseBean);
		}
	}
	@SuppressWarnings("rawtypes")
	private void getempMethod(RequestBean requestBean, ResponseBean responseBean) {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		Object object = sysMap.get("empMoudle");
		String empMoudle="";
		if(object!=null){
			empMoudle=String.valueOf(object);
		}
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("empMoudle", empMoudle);
		List<String> getempMethod = empLogDao.getempMethod(hashMap);
		Map retMap = new HashMap();
		retMap.put("returnList", getempMethod);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	@SuppressWarnings("rawtypes")
	private void getempMoudle(RequestBean requestBean, ResponseBean responseBean) {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		List<String> getempMoudle = empLogDao.getempMoudle();
		Map retMap = new HashMap();
		retMap.put("returnList", getempMoudle);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	@SuppressWarnings("rawtypes")
//TODO可以把授权的ID回传回前端
	private void passwordEmp(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map sysMap = requestBean.getSysMap();
		String empPwd = (String)sysMap.get("passWord");
		String userNo = (String)sysMap.get("userNo");
		String empMM =(String)sysMap.get("empMM");
		if(empPwd==null||"".equals(empPwd)){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("密码不能为空");
			return;
		}
		String[] split = empMM.split("\\|");
		String empMoudle =split[0];
		String empMethod =split[1];
		UserBean selectByPrimaryKey = userDao.selectByPrimaryKey(userNo, "SUNYARD", "AOS", "AOS");
		if(selectByPrimaryKey==null){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("授权用户错误");
			return;
		}
		if(userNo.equals(BaseUtil.getLoginUser().getUserNo())){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("无法自我授权");
			return;
		}
		UserRole userRole = new UserRole();
		userRole.setUserNo(userNo);
		userRole.setRoleNo("00");
//		userRole.setRoleNo("999902");
		List<UserRole> selectBySelective = userRoleMapper.selectBySelective(userRole, (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));
		if(selectBySelective!=null&&selectBySelective.size()>0){
			if(selectByPrimaryKey.getPassword().equals(empPwd)){
				Date date = new Date();
				String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(date);
				String HHmmssSSS = new SimpleDateFormat("HHmmssSSS").format(date);
				SecureRandom random = new SecureRandom();
				String logId = yyyyMMdd + HHmmssSSS + random.nextInt(1000);
				EmpLog empLog = new EmpLog();
				empLog.setLogId(logId);
				empLog.setEmpFrom(userNo);
				empLog.setEmpTo(BaseUtil.getLoginUser().getUserNo());
				empLog.setEmpMoudle(empMoudle);
				empLog.setEmpMethod(empMethod);
				empLog.setEmpDate(yyyyMMdd);
				empLog.setEmpTime(HHmmssSSS);
				empLog.setEmpSiteNo(BaseUtil.getLoginUser().getOrganNo());
				empLogDao.add(empLog);
				Map retMap = new HashMap();
				retMap.put("logId", logId);
				responseBean.setRetMap(retMap);
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
				responseBean.setRetMsg("授权通过");
			}else {
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("授权未通过");
			}
			return;
		}
			String organNo = BaseUtil.getLoginUser().getOrganNo();
			List<HashMap<String, Object>> passwordEmp = empDao.passwordEmp(organNo,userNo);
			if(passwordEmp!=null&&passwordEmp.size()>0){
				if(selectByPrimaryKey.getPassword().equals(empPwd)){
					Date date = new Date();
					String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(date);
					String HHmmssSSS = new SimpleDateFormat("HHmmssSSS").format(date);
					SecureRandom random = new SecureRandom();
					String logId = yyyyMMdd + HHmmssSSS + random.nextInt(1000);
					EmpLog empLog = new EmpLog();
					empLog.setLogId(logId);
					empLog.setEmpFrom(userNo);
					empLog.setEmpTo(BaseUtil.getLoginUser().getUserNo());
					empLog.setEmpMoudle(empMoudle);
					empLog.setEmpMethod(empMethod);
					empLog.setEmpDate(yyyyMMdd);
					empLog.setEmpTime(HHmmssSSS);
					empLog.setEmpSiteNo(BaseUtil.getLoginUser().getOrganNo());
					empLogDao.add(empLog);
					Map retMap = new HashMap();
					retMap.put("logId", logId);
					responseBean.setRetMap(retMap);
					responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
					responseBean.setRetMsg("授权通过");
					
					String log = "用户" + userNo +"授权给" + BaseUtil.getLoginUser().getUserNo() + empMM;
					addOperLogInfo(ARSConstants.MODEL_NAME_ARS, ARSConstants.OPER_TYPE_7, log);
					
				}else {
					responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
					responseBean.setRetMsg("授权未通过");
				}
			}else{
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("授权未通过");
			}
		}


	@SuppressWarnings("rawtypes")
	private void empLogQuery(RequestBean requestBean, ResponseBean responseBean) {
		Map sysMap = requestBean.getSysMap();
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
		Map condition = (Map)sysMap.get("condition");//查询条件
		Page page = PageHelper.startPage(currentPage, pageSize); 
		ArrayList<EmpLog> query = empLogDao.query(condition);
		long totalCount = page.getTotal();
		Map retMap = new HashMap();
		retMap.put("currentPage", currentPage);
		retMap.put("pageSize", pageSize);
		retMap.put("totalNum", totalCount);
		retMap.put("returnList", query);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

}
