package com.sunyard.ars.system.service.impl.sc;

import java.math.BigDecimal;
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
import com.sunyard.ars.system.bean.sc.FormInfo;
import com.sunyard.ars.system.bean.sc.FormManPro;
import com.sunyard.ars.system.bean.sc.PageBase;
import com.sunyard.ars.system.bean.sc.TableField;
import com.sunyard.ars.system.dao.sc.FormInfoMapper;
import com.sunyard.ars.system.dao.sc.FormManProMapper;
import com.sunyard.ars.system.dao.sc.PageBaseMapper;
import com.sunyard.ars.system.dao.sc.SCTableFieldMapper;
import com.sunyard.ars.system.service.sc.IFormManProService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;


@Service("formManProService")
@Transactional
public class FormManProServiceImpl extends BaseService implements IFormManProService {
	
	@Resource
	private FormManProMapper formManProMapper;
	@Resource
	private FormInfoMapper formInfoMapper;
	@Resource
	private PageBaseMapper pageBaseMapper;
	@Resource
	private SCTableFieldMapper sCTableFieldMapper; 

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

	@Override
	public void add(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		FormManPro formManPro = (FormManPro) requestBean.getParameterList().get(0);
		FormManPro fieldExit = formManProMapper.selectByPrimaryKey(formManPro.getFormName());
		if(fieldExit != null ) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("已存在相同数据！");
		}else  {
			formManProMapper.insert(formManPro);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("添加成功！");
			//添加日志信息
			String log = "版面人工处理表中新增表单名称为:" + formManPro.getFormName() + " 的数据!";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		List delList = requestBean.getParameterList();
		for(int i=0; i<delList.size(); i++) {
			FormManPro formManPro = (FormManPro)delList.get(i);
			formManProMapper.deleteByPrimaryKey(formManPro.getFormName());
			//添加日志信息
			String log = "版面人工处理表中删除表单名称为:" + formManPro.getFormName() + " 的数据!";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功！");
	}

	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		FormManPro formManPro = (FormManPro) requestBean.getParameterList().get(0);
		formManProMapper.updateByPrimaryKey(formManPro);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
		//添加日志信息
		String log = "版面人工处理表中修改表单名称为:" + formManPro.getFormName() + " 的数据!";
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		FormManPro formManPro = (FormManPro) requestBean.getParameterList().get(0);
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
		List resultList = formManProMapper.selectBySelective(formManPro);
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
		String opType = (String) sysMap.get("ohter_option");
		if("init".equals(opType)) {
			List<FormInfo> allFormInfo = formInfoMapper.getAllFormInfo();
			List<FormInfo> leftFormInfo = formInfoMapper.getLeftFormInfo();
			TableField tf = new TableField();
			tf.setTableId(new BigDecimal(3));
			tf.setfIsIndex("1");
			tf.setIndexType("1");
			List<TableField> fieldList = sCTableFieldMapper.selectBySelective(tf);
			Map retMap = new HashMap();
			retMap.put("allFormInfo", allFormInfo);
			retMap.put("leftFormInfo", leftFormInfo);
			retMap.put("fieldList", fieldList);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else if("getFormSub".equals(opType)) {
			String formName = (String) sysMap.get("formName");
			List<PageBase> formSubList = pageBaseMapper.getFormSubByFormName(formName);
			Map retMap = new HashMap();
			retMap.put("formSubList", formSubList);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("未知其他操作类型。");
		}
	}

}
