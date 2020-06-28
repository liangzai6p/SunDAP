package com.sunyard.ars.file.service.impl.oa;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.dao.FlowMapper;
import com.sunyard.ars.common.dao.SmFieldDefTbMapper;
import com.sunyard.ars.common.dao.TmpDataMapper;
import com.sunyard.ars.common.pojo.Flow;
import com.sunyard.ars.common.util.SqlUtil;
import com.sunyard.ars.file.dao.oa.FlowAllMapper;
import com.sunyard.ars.file.service.oa.IOaFlowService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

@Service("oaFlowService")
@Transactional
public class OaFlowServiceImpl extends BaseService  implements IOaFlowService {
    
    @Resource
	private FlowMapper flowMapper;
    @Resource
   	private FlowAllMapper flowAllMapper;

    @Resource
	private TmpDataMapper tmpDataMapper;
    
    @Resource
    private SmFieldDefTbMapper smFieldDefTbMapper;
    
    @Override
    public ResponseBean execute(RequestBean requestBean) throws Exception{
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
    }

	@Override
	@SuppressWarnings("rawtypes")
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map reqMap = new HashMap();
		reqMap = requestBean.getSysMap();
		String oper_type = (String) reqMap.get("oper_type");
		if ("QUERFLOWFIELD".equalsIgnoreCase(oper_type)) {//查询展现流水字段信息
			queryFlowField(requestBean, responseBean);
		} else if("QUERFLOWLIST".equalsIgnoreCase(oper_type)){//查询流水列表
			queryFlowList(requestBean,responseBean);
		} else if("NORMALDEAL".equalsIgnoreCase(oper_type)){//流水勾兑
			normalDeal(requestBean,responseBean);
		}else if("TgNormalDeal".equalsIgnoreCase(oper_type)){//流水勾兑
			TgNormalDeal(requestBean,responseBean);
		}
		else if("allFlowQuery".equalsIgnoreCase(oper_type)){//流水查询
			allFlowQuery(requestBean,responseBean);
		}else if("updateFlowErrorFlag".equalsIgnoreCase(oper_type)) {
			updateFlowErrorFlag(requestBean, responseBean);
		}
	}
	


	/**
	 * 勾兑流水方法
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void normalDeal(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		String seqId = ((Map)requestBean.getParameterList().get(0)).get("seq_id").toString();
		String serialNo = ((Map)requestBean.getParameterList().get(0)).get("serial_no").toString();
		
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("operatorNo", operatorNo);
		condMap.put("siteNo", siteNo);
		condMap.put("occurDate", occurDate);
		// 拼装返回信息
		Map retMap = new HashMap();
		//1.检查该图像勾对情况
		condMap.put("checkFlag", "1");
		condMap.put("serialNo", serialNo);

		Flow selectByPrimaryKey2 = flowMapper.selectByPrimaryKey(new BigDecimal(seqId));
		String occurDate2 = selectByPrimaryKey2.getOccurDate();
		String operatorNo2 = selectByPrimaryKey2.getOperatorNo();
		String siteNo2 = selectByPrimaryKey2.getSiteNo();
		if(!occurDate.equals(occurDate2)||!operatorNo.equals(operatorNo2)||!siteNo.equals(siteNo2)){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("禁止该操作");
		}else{
		int dealedCount = tmpDataMapper.selectImgCount(condMap);
		//如果图像已勾对流水
		if(dealedCount > 0){
			//勾对过的凭证释放原先勾对的流水
			condMap.put("checkFlag", "-1");
			condMap.put("lserialNo", "");
			condMap.put("lserialNoWhere", serialNo);//add，凭证对应的流水
			
			flowMapper.updateFlow(condMap);
			condMap.remove("lserialNoWhere");//move，凭证对应的流水
		}
		//2.检查该流水勾对情况
		condMap.put("checkFlag", "1");
		condMap.put("seqId", seqId);
		List lserialNoList = flowMapper.checkPluralFlowIsDealed(condMap);
		String flowId = null;
		if(lserialNoList != null && lserialNoList.size() > 0){
			flowId = ((Map)lserialNoList.get(0)).get("FLOW_ID").toString();
			
			String flowLSERIAL_NO = ((Map)lserialNoList.get(0)).get("LSERIAL_NO").toString();
			//释放这条流水原先勾对的图像
			if(flowMapper.selectDealCount(condMap) == 0){
				//释放这条流水原先勾对的图像
				condMap.put("checkFlag", "-1");
				condMap.put("flowId", "");
				condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
				condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
				condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
				condMap.put("serialNo", BaseUtil.filterSqlParam(flowLSERIAL_NO));//add,新流水对应的凭证id
				tmpDataMapper.updateTmpDataInfo(condMap);
				condMap.remove("serialNo");//move,新流水对应的凭证id
				
			}
		}
		
		//勾对流水和图像(套勾)
		condMap.put("checkFlag", "1");
		condMap.put("psLevel", "1");
		condMap.put("flowId", BaseUtil.filterSqlParam(selectByPrimaryKey2.getFlowId()));
		condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
		condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
		condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
		condMap.put("processState", "2");
		condMap.put("serialNo", serialNo);//add,勾选的凭证id
		tmpDataMapper.updateTmpDataInfo(condMap);//更新图像已勾兑
		
		condMap.put("lserialNo", serialNo);
		condMap.remove("serialNo");
		condMap.remove("seqId");
		flowMapper.updateFlow(condMap);//更新流水已勾兑
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
		String log = "流水操作勾兑流水操作，图像序号" + serialNo +"勾兑流水" + flowId;
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_31, log);
		}
	}

	
	
	
	/**
	 * 查询展现流水字段信息
	 * @param requestBean
	 * @param retBean
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void TgNormalDeal(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		// 拼装返回信息
		Map retMap = new HashMap();
		// 前台参数集合
		String serialNo = ((Map)requestBean.getParameterList().get(0)).get("serial_no").toString();			
		String flowId = ((Map)requestBean.getParameterList().get(0)).get("flowId").toString();
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();	
		String freedSerialNo = "";//释放图像序号
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("operatorNo", operatorNo);
		condMap.put("siteNo", siteNo);
		condMap.put("occurDate", occurDate);
		
		//1.检查该图像勾对情况
		condMap.put("checkFlag", "1");
		condMap.put("serialNo", serialNo);
		int dealedCount = tmpDataMapper.selectImgCount(condMap);
		if(dealedCount > 0){
			//如果图像有勾对流水
			//勾对过的凭证释放原先勾对的流水
			//如果图像有勾对流水
			//勾对过的凭证释放原先勾对的流水
			condMap.put("checkFlag", "-1");
			condMap.put("lserialNo", "");
			condMap.put("lserialNoWhere", serialNo);//add，凭证对应的流水
			
			flowMapper.updateFlow(condMap);
			condMap.remove("lserialNoWhere");//move，凭证对应的流水
		}
	
			List<String> flowList = Arrays.asList(flowId.split(","));
			List<List<String>> list = SqlUtil.getSumArrayList(flowList);
		
			// 构造查询表语句

			//2.检查该流水勾对情况
			condMap.put("checkFlag", "1");
			condMap.put("flowList", list);
			List lserialNoList = flowMapper.checkPluralFlowIsDealed(condMap);
			if(lserialNoList != null && lserialNoList.size() > 0){
				//释放这条流水原先勾对的图像
				for (Object lserialNo : lserialNoList) {
					String freelserialNo = ((Map)lserialNo).get("LSERIAL_NO").toString();
					condMap.put("checkFlag", "-1");
					condMap.put("flowId", "");
					condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
					condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
					condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
					condMap.put("serialNo",BaseUtil.filterSqlParam(freelserialNo));
					
					tmpDataMapper.updateTmpDataInfo(condMap);
					freedSerialNo += ((Map)lserialNo).get("LSERIAL_NO")+",";
				}
				condMap.remove("serialNo");
				freedSerialNo = freedSerialNo.substring(0, freedSerialNo.length()-1);
			}
			//3.勾对
			condMap.put("serialNo", serialNo);
			condMap.put("checkFlag", "1");
			condMap.put("psLevel", "1");
			condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
			condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
			condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
			condMap.put("processState", "2");
			condMap.put("flowId",flowId);
			tmpDataMapper.updateTmpDataInfo(condMap);//更新图像已勾兑
			condMap.remove("flowId");
			condMap.put("lserialNo", serialNo);
			
			flowMapper.updateFlow(condMap);//更新流水已勾兑
		if(!"".equals(freedSerialNo)){
			//传递到页面已经释放的图像序号
			retMap.put("freedSerialNo", freedSerialNo);
		}
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("勾兑成功");
		
		String log = "流水操作勾套流水,勾兑"+serialNo+"的图像";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_31, log);
		
	}
	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryFlowList(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		String queryFields = ((Map)requestBean.getParameterList().get(0)).get("queryFields").toString();
		
		String flowNo = ((Map)requestBean.getParameterList().get(0)).get("flowNo").toString();
		String flowCheckFlag = ((Map)requestBean.getParameterList().get(0)).get("flowCheckFlag").toString();
		String account = ((Map)requestBean.getParameterList().get(0)).get("account").toString();
		String amount = ((Map)requestBean.getParameterList().get(0)).get("amount").toString();
		String clientName = ((Map)requestBean.getParameterList().get(0)).get("clientName").toString();
		String txCode = ((Map)requestBean.getParameterList().get(0)).get("txCode").toString();
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("occurDate", occurDate);
		condMap.put("siteNo", siteNo);
		condMap.put("operatorNo", operatorNo);
		condMap.put("flowQueryFields", queryFields);
		condMap.put("flowTb", "fl_flow_tb");
		
		condMap.put("flowId", flowNo);
		condMap.put("amount", amount);
		condMap.put("account", account);
		condMap.put("checkFlag", flowCheckFlag);
		condMap.put("clientName", clientName);
		condMap.put("txCode", txCode);
		// 当前页数
		int pageNum = (int) sysMap.get("currentPage");
		// 每页数量
		int pageSize = 0;
		if (pageNum != -1) {
			int initPageNum = (int) sysMap.get("pageSize");
			if (BaseUtil.isBlank(initPageNum + "")) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageNum;
			}
		}
		// 定义分页操作
		Page page = PageHelper.startPage(pageNum, pageSize);
		// 查询分页记录
		List list = flowMapper.getFlowInfoList(condMap);
		for (Object object : list) {
			if(((Map)object).get("CHECK_FLAG").equals("-1")){
				((Map)object).put("CHECK_FLAG", "未勾对");
			}else{
				((Map)object).put("CHECK_FLAG", "已勾对");
			}
		}
		// 获取总记录数
		long totalCount = page.getTotal();
		
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("pageSize", pageSize);
		retMap.put("allRow", totalCount);
		retMap.put("flows", list);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
		String log = "流水操作，查询业务日期:" + occurDate + "网点:" + siteNo + "柜员:"+operatorNo+
				"...,有关的流水信息";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT,ARSConstants.OPER_TYPE_31,log);
		
	}

	/**
	 * 查询展现流水字段信息
	 * @param requestBean
	 * @param retBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryFlowField(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		List flowFiedlList = smFieldDefTbMapper.getFieldsByTableId(3);
		String queryFields ="";
		for (int i = 0; i < flowFiedlList.size(); i++) {
			if(((Map)flowFiedlList.get(i)).get("FIELD_NAME").equals("CD_FLAG")){
				flowFiedlList.remove(i);
				i--;//必须加上i--,list中去掉一个元素之后,list改变,其下标也要改变,不然会丢失所去掉元素的后一个元素;
				continue;
			}
			queryFields += ((Map)flowFiedlList.get(i)).get("FIELD_NAME")+",";
		}
		if(queryFields.toUpperCase().indexOf("SEQ_ID") < 0){//流水序号必要字段
			queryFields += "SEQ_ID,";
			Map map = new HashMap<>();
			map.put("FIELD_NAME", "SEQ_ID");
			map.put("ELSE_NAME", "流水序号");
			flowFiedlList.add(map);
		}
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("flowFiedlList", flowFiedlList);
		retMap.put("queryFields", queryFields.substring(0, queryFields.length()-1));
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	//用于流水的总结查询
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void allFlowQuery(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
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
		String siteNo = (String)condition.get("siteNo");
		String[] siteNos = siteNo.split(",");
		List<String> siteNosList = Arrays.asList(siteNos);
		List<List<String>> list = SqlUtil.getSumArrayList(siteNosList);
		condition.put("siteNos",list);
		//业务需求过滤无效批次
		Page page = PageHelper.startPage(currentPage, pageSize);			
		List resultList = flowAllMapper.select(condition);
		if(resultList!=null&&resultList.size()>0){
			long totalCount = page.getTotal();
			Map retMap = new HashMap();
			retMap.put("totalNum", totalCount);
			retMap.put("returnList", resultList);
			List flowFiedlList = smFieldDefTbMapper.getFieldsByTableId(3);
			retMap.put("returnFields", flowFiedlList);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else{
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("查询失败");
		}
	
	}
	/**
	 * 根据seqId更新流水差错标志
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateFlowErrorFlag(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		Integer seqId=(Integer)sysMap.get("seqId");
	    flowMapper.updateFlowErrorFlag(seqId);
	    responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("更新成功");
	}
}
