package com.sunyard.ars.system.service.impl.sc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.system.bean.sc.SCDatasource;
import com.sunyard.ars.system.dao.sc.SCDatasourceMapper;
import com.sunyard.cop.IF.common.http.RequestUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.sc.OrganData;
import com.sunyard.ars.system.bean.sc.ServiceReg;
import com.sunyard.ars.system.bean.sc.TableDef;
import com.sunyard.ars.system.dao.sc.OrganDataMapper;
import com.sunyard.ars.system.dao.sc.ServiceRegMapper;
import com.sunyard.ars.system.dao.sc.TableDefMapper;
import com.sunyard.ars.system.service.sc.IOrganDataService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;


@Service("organDataService")
@Transactional
public class OrganDataServiceImpl extends BaseService implements IOrganDataService {
	
	@Resource
	private OrganDataMapper organDataMapper;
	@Resource
	private TableDefMapper tableDefMapper;
	@Resource
	private SCDatasourceMapper scDatasourceMapper;

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
			//获取下拉框初始数据（可用的CAS组，ECM列表,批次表，数据表，流水表）
			otherOperation(requestBean, responseBean);
		}
	}

	@Override
	public void add(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		OrganData organData = (OrganData) requestBean.getParameterList().get(0);
		OrganData odExit = organDataMapper.selectByPrimaryKey(organData.getOrganNo());
		if(odExit != null) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("已存在相同数据！");
		}else  {
			organDataMapper.insert(organData);
			
			String	log="机构号码"+organData.getOrganNo()+"在机构数据表中被创建！";
			//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_1, log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("添加成功！");
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		List delList = requestBean.getParameterList();
		for(int i=0; i<delList.size(); i++) {
			OrganData organData = (OrganData)delList.get(i);
			organDataMapper.deleteByPrimaryKey(organData.getOrganNo());
			String	log="机构号码"+organData.getOrganNo()+"在机构数据表中被删除！";
			//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_2, log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功！");
	}

	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		OrganData organData = (OrganData) requestBean.getParameterList().get(0);
		organDataMapper.updateByPrimaryKey(organData);
		String	log="机构号码"+organData.getOrganNo()+"在机构数据表中被修改！";
		//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_3, log);
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		OrganData organData = (OrganData) requestBean.getParameterList().get(0);
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

		HashMap codeMap = new HashMap();
		codeMap.put("parentOrganNo",organData.getOrganNo());
		codeMap.put("userNo", BaseUtil.getLoginUser().getUserNo());
		codeMap.put("hasPrivOrganFlag", RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));

		Page page = PageHelper.startPage(currentPage, pageSize);
		List resultList = organDataMapper.selectByParentOrgan(codeMap);
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
		//获取ECM列表
		SCDatasource sd = new SCDatasource();
		List<SCDatasource> ecmList = scDatasourceMapper.selectBySelective(sd);


		//所有表列表。
		TableDef td = new TableDef();
		List<TableDef> tableList = tableDefMapper.selectBySelective(td);
		
		Map retMap = new HashMap();
		retMap.put("ECMList", ecmList);
		retMap.put("tableList", tableList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
	}

}
