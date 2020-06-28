package com.sunyard.ars.system.service.impl.sc;

import java.math.BigDecimal;
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
import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.comm.BaseService;
import com.sunyard.aos.common.util.ExcelUtil;
import com.sunyard.aos.common.util.HttpUtil;
import com.sunyard.ars.system.service.sc.IAccnoLifeCircleService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.system.bean.sc.AccnoLifeCircleBean;
import com.sunyard.ars.system.bean.sc.BlackListBean;
import com.sunyard.ars.system.dao.sc.IAccnoLifeCircleMapper;

@Service
@Transactional
public class IAccnoLifeCircleServiceImp extends BaseService implements IAccnoLifeCircleService{

	@Resource
	private IAccnoLifeCircleMapper IAccnoLifeCircleMapper;
	
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);	
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		AccnoLifeCircleBean  Model=(AccnoLifeCircleBean)requestBean.getParameterList().get(0);
		Map sysMap = requestBean.getSysMap();
		String oper_type = (String) sysMap.get("oper_type");
		List<Map<String,Object>> list=null;
		Map<String, Object> map = new HashMap<String, Object>();
		if(AOSConstants.OPERATE_QUERY.equals(oper_type)){
			int pageNum = Integer.parseInt(sysMap.get("currentPage").toString());
			int initPageNum = (int) sysMap.get("user_pageNum");
			Page page = PageHelper.startPage(pageNum, initPageNum);
			map.put("Bean", Model);
			map.put("startTime", sysMap.get("startTime"));
			map.put("endTime", sysMap.get("endTime"));
			list = getList(IAccnoLifeCircleMapper.select(map), page);
			long totalCount = page.getTotal();
			// 拼装返回信息
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			retMap.put("currentPage", pageNum);
			retMap.put("pageNum", initPageNum);
			retMap.put("totalNum", totalCount);
			retMap.put("returnList", list);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else if("CardInfo".equals(oper_type)){
			//证件信息
			String cust_no=sysMap.get("value").toString();
			list = IAccnoLifeCircleMapper.selectCardInfo(cust_no);
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			retMap.put("returnList", list);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else if("PersonInfo".equals(oper_type)){
			//人员信息
			String cust_no=sysMap.get("value").toString();
			list = IAccnoLifeCircleMapper.selectPsersonInfo(cust_no);
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			retMap.put("returnList", list);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else if("signInfo".equals(oper_type)){
			//签约信息
			String acct_no=sysMap.get("value").toString();
			list = IAccnoLifeCircleMapper.selectSignInfo(acct_no);
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			retMap.put("returnList", list);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else if("acctNoDJ".equals(oper_type)){
			//账户冻结
			String acct_no=sysMap.get("value").toString();
			list = IAccnoLifeCircleMapper.selectAcctNoDJInfo(acct_no);
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			retMap.put("returnList", list);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else if("forceDisInfo".equals(oper_type)){
			//强行划扣信息
			String acct_no=sysMap.get("value").toString();
			list = IAccnoLifeCircleMapper.selectForceHQInfo(acct_no);
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			retMap.put("returnList", list);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else if("OPERATE_EXPROT".equals(oper_type)){
			String fileName = "accnoInfo.xls";
			String title = "账户生命周期信息";
			LinkedHashMap<String, String> headMap = new LinkedHashMap<String, String>();
			headMap.put("organName", "部门名称");
			headMap.put("acctNo", "账户");
			headMap.put("shortName", "名称");
			headMap.put("currBal", "余额");
			map.put("Bean", Model);
			list = IAccnoLifeCircleMapper.select(map);
			List<Map> data = new ArrayList<Map>();
			for (int i = 0; i < list.size(); i++) {
				AccnoLifeCircleBean  obj= new AccnoLifeCircleBean();
				String organName =(String)list.get(i).get("ORGAN_NAME");
				if(!("null".equals(organName))){
					obj.setOrganName(organName);
				}else{
					obj.setOrganName("暂无数据");
				}
				
				String acctNo=(String)list.get(i).get("ACCT_NO");
				if(!("null".equals(acctNo))){
					obj.setAcctNo(acctNo);
				}else{
					obj.setAcctNo("暂无数据");
				}
				
				String shortName=(String)list.get(i).get("SHORT_NAME");
				if(!("null".equals(shortName))){
					obj.setShortName(shortName);
				}else{
					obj.setShortName("暂无数据");
				}
				
				BigDecimal currBal=(BigDecimal) list.get(i).get("CURR_BAL");
				obj.setCurrBal(currBal);
//				if(!("null".equals(currBal))){
//					obj.setCurrBal(currBal);
//				}else{
//					obj.setCurrBal(null);
//				}
				ObjectMapper objectMapper = new ObjectMapper();
			    String contents = objectMapper.writeValueAsString(obj);
				Map mapdata = objectMapper.readValue(contents, Map.class);
				data.add(mapdata);
			}
			boolean retFlag = ExcelUtil.createExcelFile(HttpUtil.getAbsolutePath("temp"), fileName, title, headMap, data);
			if(retFlag) {
				Map retMap = new HashMap();
				retMap.put("filePath", HttpUtil.getAbsolutePath("temp")+"/"+fileName);
				responseBean.setRetMap(retMap);
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
				responseBean.setRetMsg("生成成功");
			}else {
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("xls文件生成失败！");
			}
		}else if("OPERATE_QUERYSEE".equals(oper_type)){
			int pageNum = Integer.parseInt(sysMap.get("currentPage").toString());
			int initPageNum = (int) sysMap.get("user_pageNum");
			Page page = PageHelper.startPage(pageNum, initPageNum);
			map.put("startTime", sysMap.get("startTime"));
			map.put("endTime", sysMap.get("endTime"));
			map.put("acctNo", sysMap.get("accno"));
			map.put("field", sysMap.get("field"));
			list = getList(IAccnoLifeCircleMapper.selectSeeData(map), page);
			long totalCount = page.getTotal();
			// 拼装返回信息
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			retMap.put("currentPage", pageNum);
			retMap.put("pageNum", initPageNum);
			retMap.put("totalNum", totalCount);
			retMap.put("returnList", list);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
			
		}else{
			System.out.println("齐噶uideshuju");
		}
	}

}
