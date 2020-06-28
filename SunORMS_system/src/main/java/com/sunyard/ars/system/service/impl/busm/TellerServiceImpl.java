package com.sunyard.ars.system.service.impl.busm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.util.CollectionSpiltUtil;
import com.sunyard.ars.system.bean.busm.Teller;
import com.sunyard.ars.system.dao.busm.TellerMapper;
import com.sunyard.ars.system.service.busm.ITellerService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.pojo.User;

@Service("tellerService")
@Transactional
public class TellerServiceImpl extends BaseService implements ITellerService {
	
	@Resource
	private TellerMapper tellerMapper;
	

	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
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
		}else if("user_teller".equals(oper_type)){
			//获取用户可操作柜员
			getTellerList(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_OTHER.equals(oper_type)) {
			// 其他操作
			otherOperation(requestBean, responseBean);
		} else if (("queryByOrgNos").equals(oper_type)) {
			//查找多个机构的柜员
			queryByOrgNos(requestBean, responseBean);
		} else if(("queryByOrgNos2").equals(oper_type)) {
			queryByOrgNos2(requestBean, responseBean);
		}
	}
	
	@Override
	public void add(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Teller teller = (Teller)requestBean.getParameterList().get(0);
		Teller tellerExit = tellerMapper.selectByPrimaryKey(teller.getTellerNo());
		if(tellerExit != null) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("已存在相同数据！");
		}else {
			tellerMapper.insert(teller);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("添加成功");
			//添加日志
			String log = "柜员信息表中添加柜员" + teller.getTellerNo() + "-" + teller.getTellerName() + "的信息";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_4, log);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		List delList = requestBean.getParameterList();
		for(int i=0; i<delList.size(); i++) {
			Teller teller = (Teller)delList.get(i);
			tellerMapper.deleteByPrimaryKey(teller.getTellerNo());
			//添加日志
			String log = "柜员信息表中删除柜员" + teller.getTellerNo() + "-" + teller.getTellerName();
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功");
		
	}

	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Teller teller = (Teller)requestBean.getParameterList().get(0);
		tellerMapper.updateByPrimaryKey(teller);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("更新成功");
		//添加日志
		String log = "柜员信息表中跟新柜员" + teller.getTellerNo() + "-" + teller.getTellerName() + "的信息!";
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map sysMap = requestBean.getSysMap();
			Teller teller = (Teller)requestBean.getParameterList().get(0);
			int pageNum = (int) sysMap.get("currentPage");
			int pageSize = 0;
			if (pageNum != -1) {
				int initPageNum = (int) sysMap.get("pageNum");
				if (BaseUtil.isBlank(initPageNum + "")) {
					pageSize = ARSConstants.PAGE_NUM;
				} else {
					pageSize = initPageNum;
				}
			}
			Page page = PageHelper.startPage(pageNum, pageSize);
			List<Teller> list = tellerMapper.selectBySelective(teller);
			long totalCount = page.getTotal();
			Map retMap = new HashMap();
			retMap.put("currentPage", pageNum);
			retMap.put("pageNum", pageSize);
			retMap.put("totalNum", totalCount);
			retMap.put("returnList", list);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
	}

	@Override
	public void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getTellerList(RequestBean requestBean, ResponseBean responseBean) throws Exception {
			User user = BaseUtil.getLoginUser();
			Map retMap = new HashMap();
			List<Map> list = tellerMapper.getTellerList(user.getUserNo(),(String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));
			retMap.put("returnList", list);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void queryByOrgNos(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map sysMap = requestBean.getSysMap();
		ArrayList<String> orgNoList = new ArrayList<String>();
		orgNoList = (ArrayList<String>) sysMap.get("orgNoList");
		List<Teller> list = tellerMapper.queryByOrgNos(orgNoList);
		Map retMap = new HashMap();
		retMap.put("returnList", list);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void queryByOrgNos2(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map sysMap = requestBean.getSysMap();
		ArrayList<String> orgNoList = new ArrayList<String>();
		orgNoList = (ArrayList<String>) sysMap.get("orgNoList");
		List<List<String>> resOrgNoList = CollectionSpiltUtil.split(orgNoList, 1000);
		List<Teller> list = tellerMapper.queryByOrgNos2(resOrgNoList);
		Map retMap = new HashMap();
		retMap.put("returnList", list);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
}
