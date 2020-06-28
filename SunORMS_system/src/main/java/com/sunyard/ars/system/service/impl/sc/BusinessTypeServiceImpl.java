package com.sunyard.ars.system.service.impl.sc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sunyard.aos.common.util.BaseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.sc.BusinessType;
import com.sunyard.ars.system.dao.sc.BusinessTypeMapper;
import com.sunyard.ars.system.service.sc.IBusinessTypeService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;


@Service("businessTypeService")
@Transactional
public class BusinessTypeServiceImpl extends BaseService implements IBusinessTypeService {
	
	@Resource
	private BusinessTypeMapper businessTypeMapper; 

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
		BusinessType businessType = (BusinessType) requestBean.getParameterList().get(0);
		List<BusinessType> serExit = businessTypeMapper.selectBySelective(businessType);
		if(serExit != null && serExit.size()>0) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("已存在相同数据！");
		}else  {
			BigDecimal businessId = businessTypeMapper.getMaxBusinessId();
			if(businessId == null) {
				businessId = new BigDecimal(0);
			}else if(businessId.equals(new BigDecimal(97))) { //过滤98，99
				businessId = businessId.add(new BigDecimal(3));
			}else {
				businessId = businessId.add(new BigDecimal(1));
			}
			String busiId = BaseUtil.filterSqlParam(businessId.toString());
			businessId = new BigDecimal(busiId);

			businessType.setBusinessId(businessId);
			businessTypeMapper.insert(businessType);
			Map retMap = new HashMap();
			retMap.put("businessId", businessId);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("添加成功！");
			String log = "业务类型定义表中新增业务类型名称为" + businessType.getBusinessName() + " 的数据";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		List delList = requestBean.getParameterList();
		for(int i=0; i<delList.size(); i++) {
			BusinessType businessType = (BusinessType)delList.get(i);
			businessTypeMapper.deleteByPrimaryKey(businessType.getBusinessId());
			String log = "删除业务类型定义表中业务类型编号为" + businessType.getBusinessId() + " 的数据!";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功！");
	}

	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		BusinessType businessType = (BusinessType) requestBean.getParameterList().get(0);
		businessTypeMapper.updateByPrimaryKey(businessType);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
		String log = "修改业务类型定义表中业务类型编号为" + businessType.getBusinessId() + " 的数据!";
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		BusinessType businessType = (BusinessType) requestBean.getParameterList().get(0);
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
		List resultList = businessTypeMapper.selectBySelective(businessType);
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

	@Override
	public void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub

	}

}
