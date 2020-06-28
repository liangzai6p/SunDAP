package com.sunyard.ars.system.service.impl.et;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.ExcelUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.et.SlipDef;
import com.sunyard.ars.system.dao.et.SlipDefMapper;
import com.sunyard.ars.system.service.et.ISlipDefService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;


@Service("slipDefService")
@Transactional
public class SlipDefServiceImpl extends BaseService implements ISlipDefService {
	
	@Resource
	private SlipDefMapper slipDefMapper; 

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
		SlipDef slipDef = (SlipDef) requestBean.getParameterList().get(0);
		Map sysMap = requestBean.getSysMap();
		String organNo = (String) sysMap.get("organNo");
		SlipDef serExit = slipDefMapper.selectByPrimaryKey(slipDef.getSlipNo());
		if(serExit != null ) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("已存在相同编号差错！");
		}else  {
			String date = BaseUtil.getCurrentDateStr();
			slipDef.setCreateDate(date);
			slipDef.setBusinessAssociation(organNo);
			slipDefMapper.insert(slipDef);
			Map retMap = new HashMap();
			retMap.put("createDate", date);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("添加成功！");
			//添加日志
			String log = "新增编号为"+slipDef.getSlipNo() + "的差错定义";
			addOperLogInfo(ARSConstants.MODEL_NAME_RISK_WARNING, ARSConstants.OPER_TYPE_1, log);

		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		List delList = requestBean.getParameterList();
		for(int i=0; i<delList.size(); i++) {
			SlipDef slipDef = (SlipDef)delList.get(i);
			slipDefMapper.deleteByPrimaryKey(slipDef.getSlipNo());
			//添加日志
			String log = "删除编号为"+slipDef.getSlipNo() + "的差错定义";
			addOperLogInfo(ARSConstants.MODEL_NAME_RISK_WARNING, ARSConstants.OPER_TYPE_2, log);
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功！");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		String date = BaseUtil.getCurrentDateStr();
		String operUserNo = BaseUtil.getLoginUser().getUserNo();
		SlipDef slipDefNew = (SlipDef) requestBean.getParameterList().get(0);
		SlipDef slipOld = slipDefMapper.selectByPrimaryKey(slipDefNew.getSlipNo());
		
		//停用/启用
		if(slipDefNew.getActiveFlag() != null && slipDefNew.getActiveFlag() != "") {
			if(!slipOld.getActiveFlag().equals(slipDefNew.getActiveFlag())) {
				if("1".equals(slipDefNew.getActiveFlag())) {
					slipDefNew.setCreateDate(date);
					slipDefNew.setStopDate("");
					slipDefNew.setRemark("["+date+"用户"+operUserNo+"将状态设置为启用]");
				}else {
					slipDefNew.setStopDate(date);
					slipDefNew.setRemark("["+date+"用户"+operUserNo+"将状态设置为停用]");
				}
			}
		}else{
			//修改处罚金额、分数
			slipDefNew.setRemark("["+date+"用户"+operUserNo+"修改名称从"+BaseUtil.filterSqlParam(slipOld.getSlipName())+"到"+slipDefNew.getSlipName()+
					";修改级别从"+BaseUtil.filterSqlParam(slipOld.getSlipLevel())+"到"+slipDefNew.getSlipLevel()+";修改归类从"+BaseUtil.filterSqlParam(slipOld.getSlipType())+"到"+slipDefNew.getSlipType()+"]");
		}
		slipDefMapper.updateByPrimaryKeySelective(slipDefNew);
		SlipDef slipDef = slipDefMapper.selectByPrimaryKey(slipDefNew.getSlipNo());
		Map retMap = new HashMap();
		retMap.put("newSlipDef", slipDef);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
		
		//添加日志
		String log = "修改编号为"+slipDef.getSlipNo() + "的差错定义";
		addOperLogInfo(ARSConstants.MODEL_NAME_RISK_WARNING, ARSConstants.OPER_TYPE_3, log);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		//定义总行分行参数
		String flag = "";
		SysParameter sysPar = ARSConstants.SYSTEM_PARAMETER.get("PARENT_ORGAN_NO");
		String parentOrgan = sysPar.getParamValue();
		Map sysMap = requestBean.getSysMap();
		String organNo = (String) sysMap.get("organNo");
		SlipDef slipDef = (SlipDef) requestBean.getParameterList().get(0);
		HashMap<String, Object> maps = new HashMap<String, Object>();
		maps.put("organNo", organNo);//当前登陆机构
	    maps.put("parentOrgan", parentOrgan);//父类机构	
		maps.put("slipDef", slipDef);
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
		List resultList = new ArrayList<SlipDef>();
		Page page = PageHelper.startPage(currentPage, pageSize);
//		slipDef.setSlipWorkType("0");
		
		if(organNo!=null && organNo!="" && organNo.equals(parentOrgan)){
			//总行查询
			slipDef.setBusinessAssociation(parentOrgan);
			resultList = slipDefMapper.selectBySelective(slipDef);
			flag = "parentOrgan";
		}else{
			//分行查询
			resultList = slipDefMapper.selectByOrgan(maps);
			flag = "childOrgan";
		}
		
		long totalCount = page.getTotal();
		Map retMap = new HashMap();
		retMap.put("currentPage", currentPage);
		retMap.put("pageSize", pageSize);
		retMap.put("totalNum", totalCount);
		retMap.put("returnList", resultList);
		retMap.put("flag", flag);
		retMap.put("parentOrgan", parentOrgan);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		SlipDef slipDef = (SlipDef) requestBean.getParameterList().get(0);
		Map sysMap = requestBean.getSysMap();
		String operator = (String)sysMap.get("other_operator");
		if("slipExport".equals(operator)) {
			List resultList = slipDefMapper.selectBySelective(slipDef);
			//String fileName = System.currentTimeMillis()+(String)sysMap.get("fileName");
			String fileName = System.currentTimeMillis()+"slipDef.xls";
			String title = "差错定义信息";
			LinkedHashMap<String, String> headMap = new LinkedHashMap<String, String>();
			headMap.put("slipType", "类别      ");
			headMap.put("slipNo", "编号 ");
			headMap.put("slipName", "差错名称                                  ");
			headMap.put("slipLevel", "差错级别");
			//headMap.put("amerceMoney", "处罚金额");
			//headMap.put("amerceScore", "处罚分数");
			headMap.put("createDate", "启用日期");
			headMap.put("stopDate", "停用日期");
			headMap.put("activeFlag", "状态");
			//headMap.put("remark", "备注");
			List<Map> data = new ArrayList<Map>();
			for(int i=0; i<resultList.size(); i++) {
				SlipDef sd = (SlipDef)resultList.get(i);
				if(sd.getStopDate() == null) {
					sd.setStopDate("");
				}
				if("0".equals(sd.getActiveFlag())) {
					sd.setActiveFlag("停用");
				}else {
					sd.setActiveFlag("启用");
				}
				if("1".equals(sd.getSlipLevel())) {
					sd.setSlipLevel("低");
				}else if("2".equals(sd.getSlipLevel())) {
					sd.setSlipLevel("中");
				}else {
					sd.setSlipLevel("高");
				}
				ObjectMapper objectMapper = new ObjectMapper();
			    String contents = objectMapper.writeValueAsString(sd);
				Map map = objectMapper.readValue(contents, Map.class);
				data.add(map);
			}
			boolean retFlag = ExcelUtil.createExcelFile(ARSConstants.FILE_EXCEL_PATH, fileName, title, headMap, data);
			if(retFlag) {
				Map retMap = new HashMap();
				retMap.put("filePath", ARSConstants.FILE_EXCEL_PATH+fileName);
				responseBean.setRetMap(retMap);
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
				responseBean.setRetMsg("生成成功");
				//添加日志
				String log = "差错定义生成xls文件，导出列表";
				addOperLogInfo(ARSConstants.MODEL_NAME_RISK_WARNING, ARSConstants.OPER_TYPE_3, log);
			}else {
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("xls文件生成失败！");
			}
		}
	}

}
