package com.sunyard.ars.system.service.impl.mc;

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
import com.sunyard.ars.common.dao.CommonMapper;
import com.sunyard.ars.system.dao.mc.LabRuleMapper;
import com.sunyard.ars.system.pojo.mc.LabRule;
import com.sunyard.ars.system.service.mc.ILabRuleService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service("labRuleService")
@Transactional
public class LabRuleServiceImpl extends BaseService  implements ILabRuleService{
	
    @Resource
    private CommonMapper commonMapper;
    @Resource
    private LabRuleMapper labRuleMapper;
    
	
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
		if("executeSelect".equals(oper_type)){
			//执行查询sql
			executeSelect(requestBean,responseBean); 
		}else if ("QUERYLABRULE".equals(oper_type)) { 
			// 新增
			queryLabRule(requestBean, responseBean);
		}
		
		
	}
	
	/**
	 * 执行sql查询并返回结果集
	 * @param requestBean
	 * @param responseBean
	 */	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void executeSelect(RequestBean requestBean, ResponseBean responseBean)throws Exception{
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
		
		//执行查询
		Page page = PageHelper.startPage(currentPage, pageSize);
		List<Map<String, Object>> resultList =  commonMapper.executeSelect(sysMap.get("sql").toString());
		
		long totalCount = page.getTotal();
		Map retMap = new HashMap();
		retMap.put("pageSize", pageSize);
		retMap.put("allRow", totalCount);
		retMap.put("resultList", resultList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
	}

	/**
	 * 查询版本的规则信息
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryLabRule(RequestBean requestBean, ResponseBean responseBean)throws Exception{		
		LabRule labRule=(LabRule)requestBean.getParameterList().get(0);
		List labRuleList= labRuleMapper.queryLabRule(labRule);
		
		
		
		Map retMap = new HashMap();
		retMap.put("labRuleList", labRuleList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
}
