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
import com.sunyard.ars.system.bean.sc.FieldComp;
import com.sunyard.ars.system.bean.sc.TableDef;
import com.sunyard.ars.system.bean.sc.TableField;
import com.sunyard.ars.system.dao.sc.FieldCompMapper;
import com.sunyard.ars.system.dao.sc.SCTableFieldMapper;
import com.sunyard.ars.system.dao.sc.TableDefMapper;
import com.sunyard.ars.system.service.sc.IFieldCompService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;


@Service("fieldCompService")
@Transactional
public class FieldCompServiceImpl extends BaseService implements IFieldCompService {
	
	@Resource
	private FieldCompMapper fieldCompMapper; 
	@Resource
	private TableDefMapper tableDefMapper;
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
		FieldComp fieldComp = (FieldComp) requestBean.getParameterList().get(0);
		FieldComp fieldExit = fieldCompMapper.selectByPrimaryKey(fieldComp.getOutSourceId(), fieldComp.getOuterField());
		if(fieldExit != null ) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("已存在相同数据！");
		}else  {
			fieldCompMapper.insert(fieldComp);
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
			FieldComp fieldComp = (FieldComp)delList.get(i);
			fieldCompMapper.deleteByPrimaryKey(fieldComp.getOutSourceId(), fieldComp.getOuterField());
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功！");
	}

	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		FieldComp fieldComp = (FieldComp) requestBean.getParameterList().get(0);
		fieldCompMapper.updateByPrimaryKey(fieldComp);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		FieldComp fieldComp = (FieldComp) requestBean.getParameterList().get(0);
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
		List resultList = fieldCompMapper.selectBySelective(fieldComp);
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
			List<TableDef> tableList = tableDefMapper.selectBySelective(null);
			TableField tf = new TableField();
			tf.setTableId(new BigDecimal(2));
			List<TableField> tmpDataTableFieldList = sCTableFieldMapper.selectBySelective(tf);
			Map retMap = new HashMap();
			retMap.put("tableList", tableList);
			retMap.put("tmpDataTableFieldList", tmpDataTableFieldList);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else if("getField".equals(opType)) {
			String tableId = (String) sysMap.get("tableId");
			TableField tf = new TableField();
			tf.setTableId(new BigDecimal(tableId));
			List<TableField> tableFieldList = sCTableFieldMapper.selectBySelective(tf);
			Map retMap = new HashMap();
			retMap.put("tableFieldList", tableFieldList);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("未知其他操作类型。");
		}
	}

}
