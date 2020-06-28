package com.sunyard.ars.system.service.impl.othersys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.system.bean.othersys.SmRealarmsSet;
import com.sunyard.ars.system.dao.mc.McTableMapper;
import com.sunyard.ars.system.dao.mc.ModelMapper;
import com.sunyard.ars.system.dao.othersys.SmRealarmsSetMapper;
import com.sunyard.ars.system.pojo.mc.McTable;
import com.sunyard.ars.system.pojo.mc.Model;
import com.sunyard.ars.system.service.othersys.ISmRealarmsSetService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;


@Service("smRealarmsSetService")
@Transactional
public class SmRealarmsSetServiceImpl extends BaseService implements
		ISmRealarmsSetService {
	
	@Resource
	private SmRealarmsSetMapper smRealarmsSetMappper;
	
	@Resource
	private McTableMapper mcTableMapper;
	
	@Resource
	private ModelMapper modelMapper;

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
		}else if (ARSConstants.OPERATE_ADD.equals(oper_type)) { 
			// 新增
			add(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_MODIFY.equals(oper_type)) { 
			// 修改
			update(requestBean, responseBean);
		} else if(ARSConstants.OPERATE_DELETE.equals(oper_type)) { 
			// 删除
			delete(requestBean, responseBean);
		}else if("selectByTableName".equals(oper_type)){
			//根据tableType查出字段
			selectByTableName(requestBean, responseBean);
		}
		else if("selectByModelId".equals(oper_type)){
			//根据modelType和IsCurModel查出字段
			selectByModelId(requestBean, responseBean);
		}
	}

	@Override
	public void add(RequestBean requestBean, ResponseBean responseBean)
			throws Exception {
		SmRealarmsSet realarms = (SmRealarmsSet) requestBean.getParameterList().get(0);
		SmRealarmsSet realCodeId=smRealarmsSetMappper.selectByPrimaryKey(realarms.getCodeId());
		if(realCodeId != null) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("已存在相同数据！");
		}else  {
			smRealarmsSetMappper.insert(realarms);
			String	log="编号"+realarms.getCodeId()+"在表中被创建！";
			//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_1, log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("添加成功！");
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean)
			throws Exception {
		List delList = requestBean.getParameterList();
		for(int i=0; i<delList.size(); i++) {
			SmRealarmsSet realarms = (SmRealarmsSet)delList.get(i);
			smRealarmsSetMappper.deleteByPrimaryKey(realarms.getCodeId());
			String	log="编号"+realarms.getCodeId()+"的记录在表中被删除！";
			//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_2, log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功！");
	}

	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean)
			throws Exception {
		SmRealarmsSet realarms = (SmRealarmsSet) requestBean.getParameterList().get(0);
		smRealarmsSetMappper.updateByPrimaryKeySelective(realarms);
		String	log="编号"+realarms.getCodeId()+"在表中被修改！";
		//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_3, log);
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean)
			throws Exception {
		Map sysMap = requestBean.getSysMap();
		SmRealarmsSet realarms = (SmRealarmsSet) requestBean.getParameterList().get(0);
		int currentPage = (int)sysMap.get("currentPage");
		int pageSize = 0;
		if(currentPage != -1) {
			Integer initPageSize = (Integer) sysMap.get("pageNum");
			if (initPageSize == null) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageSize;
			}
		}
		
		Page page = PageHelper.startPage(currentPage, pageSize);
		List resultList = smRealarmsSetMappper.selectSmRealarmsSetList(realarms);
		
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

	//表名列表
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<McTable> selectByTableName(RequestBean requestBean, ResponseBean responseBean){	
		McTable mct=new McTable();
		mct.setTableType("4");
		List<McTable> lis=mcTableMapper.selectBySelective(mct);
		
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("returnList", lis);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		return null;
		
	}
	
	//模型号列表
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Model> selectByModelId(RequestBean requestBean, ResponseBean responseBean){	
		Model mcl=new Model();
		mcl.setModelType("0");
		mcl.setIsCurModel("1");
		List<Model> lis = modelMapper.selectSmRealarmsModelId(mcl);
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("returnList", lis);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		return null;
		
	}
}
