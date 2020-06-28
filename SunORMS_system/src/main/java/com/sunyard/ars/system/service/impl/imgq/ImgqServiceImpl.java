package com.sunyard.ars.system.service.impl.imgq;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.comm.HttpClient;
import com.sunyard.ars.common.dao.FlowMapper;
import com.sunyard.ars.common.dao.SmFieldDefTbMapper;
import com.sunyard.ars.common.dao.TmpBatchMapper;
import com.sunyard.ars.common.dao.TmpDataMapper;
import com.sunyard.ars.system.bean.sc.BusinessType;
import com.sunyard.ars.system.dao.sc.BusinessTypeMapper;
import com.sunyard.ars.system.dao.sc.SystemParameterMapper;
import com.sunyard.ars.system.dao.sc.VoucherInfoMapper;
import com.sunyard.ars.system.service.imgq.IImgqService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

@Service("imgqService")
@Transactional
public class ImgqServiceImpl extends BaseService  implements IImgqService {
    
    @Resource
	private TmpBatchMapper tmpBatchMapper;
    
    @Resource
	private TmpDataMapper tmpDataMapper;
    
    @Resource
	private FlowMapper flowMapper;
    
    @Resource
    private VoucherInfoMapper scVoucherInfoMapper;
    
    @Resource
    private BusinessTypeMapper businessTypeMapper;
    
    @Resource
    private SmFieldDefTbMapper smFieldDefTbMapper;
    
    @Resource
    private SystemParameterMapper systemParameterMapper;

    @Override
    public ResponseBean execute(RequestBean requestBean) throws Exception{
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
    }

	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map reqMap = new HashMap();
		reqMap = requestBean.getSysMap();
		String oper_type = (String) reqMap.get("oper_type");

		if ("QUERYSELECTINFO".equalsIgnoreCase(oper_type)) {//查询版面
			queryAllSelectInfo(requestBean, responseBean);
		} else if ("QUERFLOWFIELD".equalsIgnoreCase(oper_type)) {//查询展现流水字段信息
			queryFlowField(requestBean, responseBean);
		} else if("QUERYBATCH".equalsIgnoreCase(oper_type)){
			queryBatchDataInfo(requestBean, responseBean);
		} else if("QUERFLOWLIST".equalsIgnoreCase(oper_type)){
			queryFlowData(requestBean, responseBean);
		} else if("QUERYVOUCHER".equalsIgnoreCase(oper_type)){
			queryVoucherData(requestBean, responseBean);
		} else if("queryImage".equals(oper_type)) {
			queryImage(requestBean, responseBean);
		}  else if("QUERDIARY".equalsIgnoreCase(oper_type)){
			querdiary(requestBean, responseBean);
		} else if("QUERYSINOIMG".equalsIgnoreCase(oper_type)){
			querysinoimg(requestBean, responseBean);
		} else if("QUERYPADIMG".equalsIgnoreCase(oper_type)){
			querypadimg(requestBean, responseBean);
		}else if("queryBatchAndImageInfo".equalsIgnoreCase(oper_type)) {//查询影像文件信息
			queryBatchAndImageInfo(requestBean, responseBean);
		}
	}	

	/**
	 * 获取一笔业务的凭证信息
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryVoucherData(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		String batchId = ((Map)requestBean.getParameterList().get(0)).get("batchId").toString();
		String inccodeinBatch = ((Map)requestBean.getParameterList().get(0)).get("inccodeinBatch").toString();
		String dataTb = ((Map)requestBean.getParameterList().get(0)).get("dataTb").toString();
		HashMap condMap = new HashMap<String, String>();
		condMap.put("batchId", batchId);
		condMap.put("inccodeinBatch", inccodeinBatch);
		condMap.put("dataTb", dataTb);
		List list = tmpDataMapper.selectVoucherList(condMap);
		retMap.put("tmpDataList", list);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}

	/**
	 * 获取流水信息
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryFlowData(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		String lSerialNo = ((Map)requestBean.getParameterList().get(0)).get("lSerialNo").toString();
		String dataTb = ((Map)requestBean.getParameterList().get(0)).get("dataTb").toString();
		String queryFields = ((Map)requestBean.getParameterList().get(0)).get("queryFields").toString();
		String flowTb = "FL_FLOW_TB";
		if(dataTb.indexOf("_HIS") > 0){
			flowTb = flowTb + "_HIS";
		}
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("serialNo", lSerialNo);
		condMap.put("occurDate", occurDate);
		condMap.put("siteNo", siteNo);
		condMap.put("operatorNo", operatorNo);
		condMap.put("flowQueryFields", queryFields);
		condMap.put("flowTb", flowTb);
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
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryBatchDataInfo(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		Map sysMap = requestBean.getSysMap();
		//批次查询条件
		String startDate = ((Map)requestBean.getParameterList().get(0)).get("dateS").toString();
		String endDate = ((Map)requestBean.getParameterList().get(0)).get("dateE").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		Object operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo");
		Object needProcess = ((Map)requestBean.getParameterList().get(0)).get("needProcess");
		Object businessId = ((Map)requestBean.getParameterList().get(0)).get("businessId");
		//流水查询条件
		Object amountS = ((Map)requestBean.getParameterList().get(0)).get("amountS");
		Object amountE = ((Map)requestBean.getParameterList().get(0)).get("amountE");
		Object vouhNo = ((Map)requestBean.getParameterList().get(0)).get("vouhNo");
		Object form_name = ((Map)requestBean.getParameterList().get(0)).get("formNo");
		Object acctNo = ((Map)requestBean.getParameterList().get(0)).get("acctNo");
		Object currency_type = ((Map)requestBean.getParameterList().get(0)).get("currencyType");
		Object flow_id = ((Map)requestBean.getParameterList().get(0)).get("flowId");
		Object tx_code = ((Map)requestBean.getParameterList().get(0)).get("txCode");
		Object subject_no = ((Map)requestBean.getParameterList().get(0)).get("subjectNo");
		
		//精确索引字段
		boolean tmpIsHasPrecisionIndex = false;

		//精确查询 若有输入流水信息，则关联流水表查询，否则不查询流水表
		tmpIsHasPrecisionIndex = tmpIsHasPrecisionIndex
				|| !flow_id.equals("") || !acctNo.equals("")
				|| !amountS.equals("") || !amountE.equals("")
				|| !subject_no.equals("") || !vouhNo.equals("")
				|| !currency_type.equals("") || !tx_code.equals("");
		
		
		HashMap condMap = new HashMap<String, String>();
		condMap.put("siteNo", siteNo);
		//获取表名
		List tableStr = tmpBatchMapper.getTableName(condMap);
		if(tableStr != null && tableStr.size() > 0){//如果表名为空，则表定义表中未对该机构定义数据表
			String batchTable = ((Map) tableStr.get(0)).get("BP_BATCH_TB")+"";
			//String dataTable = ((Map) tableStr.get(0)).get("BP_DATA_TB")+"";
			String flowTable = ((Map) tableStr.get(0)).get("FL_FLOW_TB")+"";
			batchTable = BaseUtil.filterSqlParam(batchTable);
			flowTable = BaseUtil.filterSqlParam(flowTable);
			// 获取表名(key),批次号组合(value)
			condMap.put("batchTable", batchTable);
			condMap.put("startDate", startDate);
			condMap.put("endDate", endDate);
			condMap.put("operatorNo", operatorNo);
			condMap.put("form_name", form_name);
			condMap.put("needProcess", needProcess);
			condMap.put("businessId", businessId);
			List batchInfo = tmpBatchMapper.getBatchInfo(condMap);
			//只有当获取的批次信息不为空时，才进行查询
			if(null != batchInfo && batchInfo.size()>0){
				String imageStatus = "";
				String progressFlag = "";
				String dataTb = "";
				Hashtable map = new Hashtable();
				for (Object batch : batchInfo) {
					imageStatus = ((Map)batch).get("IMAGE_STATUS").toString();
					progressFlag = ((Map)batch).get("PROGRESS_FLAG").toString();
					dataTb = ((Map)batch).get("TEMP_DATA_TABLE").toString();

					imageStatus = BaseUtil.filterSqlParam(imageStatus);
					progressFlag = BaseUtil.filterSqlParam(progressFlag);
					dataTb = BaseUtil.filterSqlParam(dataTb);
					
					if (progressFlag.equals(ARSConstants.PROCESS_FLAG_99)
							&& (imageStatus.equals(ARSConstants.IMAGE_STATUS_2) || imageStatus
									.equals(ARSConstants.IMAGE_STATUS_3))) {
						dataTb =  dataTb + "_HIS";
					}
					// 存放所有表名
					if (!map.containsKey(dataTb)) {// 若果第一次写入
						map.put(dataTb, "1");
					} 
				}
				Iterator it = map.keySet().iterator();
				List<String> dataTableList = new ArrayList<String>();
				while(it.hasNext()){
					dataTableList.add((String) it.next()) ;
				}
				condMap.put("dataTableList", dataTableList);
				condMap.put("flowTable", flowTable);
				condMap.put("hisFlowTable", flowTable+"_HIS");
				condMap.put("flow_id", flow_id);
				condMap.put("subject_no", subject_no);
				condMap.put("tx_code", tx_code);
				condMap.put("currency_type", currency_type);
				condMap.put("acctNo", acctNo);
				condMap.put("vouhNo", vouhNo);
				condMap.put("amountE", amountE);
				condMap.put("amountS", amountS);
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
				if(tmpIsHasPrecisionIndex){//流水查询
					retMap.put("imageList", tmpDataMapper.selectImageByFlow(condMap));
				}else{//无流水查询
					retMap.put("imageList", tmpDataMapper.selectImageByNoFlow(condMap));
				}
				long totalRow = page.getTotal();
				if(totalRow == 0){
					retMap.put("isSuccess", false);
					retMap.put("failMsg", "根据查询条件未查询到信息!");
				}else{
					retMap.put("isSuccess", true);
				}
				retMap.put("pageSize", pageSize);
				retMap.put("allRow", totalRow);
			}else{
				retMap.put("isSuccess", false);
				retMap.put("failMsg", "根据查询条件未查询到信息!");
			}
		}else{
			retMap.put("isSuccess", false);
			retMap.put("failMsg", "未对该机构定义临时表信息!");
		}

		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}

	/**
	 * 查询展现流水字段信息
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryFlowField(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		List flowFiedlList = smFieldDefTbMapper.getFieldsByTableId(3);
		String queryFields ="";
		for (Object object : flowFiedlList) {
			queryFields += ((Map)object).get("FIELD_NAME")+",";
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
	
	/**
	 * 获取所有版面
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryAllSelectInfo(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		
		String organ_no = ((Map)requestBean.getParameterList().get(0)).get("organ_no").toString();
		
		Map<String,String> formNameMap = new HashMap<>();
		HashMap condMap = new HashMap<String, String>();
		condMap.put("organ_no", "");
		List voucheList = BaseUtil.convertListMapKeyValue(scVoucherInfoMapper.selectFormMap(condMap));
		for (int i = 0;i < voucheList.size();i++) {
			Map  voucheMap = (Map) voucheList.get(i);
			formNameMap.put((String) voucheMap.get("voucher_name"), "1");
		}
		//所有凭证版面
		List tempAllInfo = BaseUtil.convertListMapKeyValue(scVoucherInfoMapper.selectAllForms());

		List formList = new ArrayList();
		for(Object objs :tempAllInfo){
			String formName = (String) ((Map) objs).get("voucher_name");
			if(null != formNameMap.get(formName)){//只有配置的版面才能显示
				formList.add(objs);
			}
		}
		
		BusinessType businessType = new BusinessType();

		retMap.put("formArr", formList.toArray());
		retMap.put("busiArr", businessTypeMapper.selectBySelective(businessType));
		//retMap.put("currencyTypeArr", currencyTypeArr);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}
	
	/**
	 * 华夏银行影像查询方法。
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryImage(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		Map sysMap = requestBean.getSysMap();
		sysMap.put("oper_type", ARSConstants.OPERATE_QUERY);
		String flow_id = String.valueOf(sysMap.get("flow_no"));
		if(flow_id != null && flow_id.trim().length()<6) {
			int flowLen = flow_id.trim().length();
			for(int i=0; i<(6-flowLen); i++) {
				flow_id = "0"+flow_id.trim();
			}
			sysMap.put("flow_no", flow_id);
		}
		
		/*测试图片接口使用
		 * ObjectMapper objectMapper = new ObjectMapper();
		String message = objectMapper.writeValueAsString(requestBean);
		System.out.println(message);
		Map retMap = new HashMap();
		List returnList = new ArrayList();
		for(int i=0; i<3; i++) {
			Map map = new HashMap();
			map.put("re_url", "http://localhost:8080/SunARS/static/img/tibet-"+(new Random().nextInt(5)+1)+".jpg");
			returnList.add(map);
		}
		retMap.put("returnList", returnList);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("hello world ");
		responseBean.setRetMap(retMap);
		*/
		SysParameter param = ARSConstants.SYSTEM_PARAMETER.get("IMG_QUERY_URL");
		if(param != null) {
			String url = param.getParamValue();
			ObjectMapper objectMapper = new ObjectMapper();
			String message = objectMapper.writeValueAsString(requestBean);
			try {
				String backMessge = HttpClient.sendPost(url, "message="+URLEncoder.encode(message, ARSConstants.ENCODE));
				String[] messageArr = backMessge.split(",@,");
				if(HttpURLConnection.HTTP_OK != Integer.parseInt(messageArr[0])) {
					responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
					responseBean.setRetMsg("系统异常，返回信息："+backMessge);
				}else {
					ResponseBean responseBeanBak = objectMapper.readValue(messageArr[1], ResponseBean.class);
					responseBean.setRetCode(responseBeanBak.getRetCode());
					responseBean.setRetMsg(responseBeanBak.getRetMsg());
					responseBean.setRetMap(responseBeanBak.getRetMap());
				}
			} catch (Exception e) {
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("系统异常，返回信息："+e.toString());
			}
		}else {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("未从系统参数表获取影像查询地址配置：IMG_QUERY_URL");
		}
	}
	/**
	 * 用于查看移动pad系统
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void querypadimg(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		Map retMap = new HashMap();
		Map sysMap = requestBean.getSysMap();
		//批次查询条件
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("dateS").toString();
		Object flowId = ((Map)requestBean.getParameterList().get(0)).get("flowId");
		Object acctNo = ((Map)requestBean.getParameterList().get(0)).get("acctNo");
		Object amountS = ((Map)requestBean.getParameterList().get(0)).get("amountS");
		Object amountE = ((Map)requestBean.getParameterList().get(0)).get("amountE");
		
		HashMap condMap = new HashMap<String, String>();
		condMap.put("occurDate", occurDate);
		condMap.put("flowId", flowId);
		condMap.put("acctNo", acctNo);
		condMap.put("amountS", amountS);
		condMap.put("amountE", amountE);

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
		retMap.put("imageList", tmpDataMapper.selectPadImageByMap(condMap));
		long totalRow = page.getTotal();
		if (totalRow == 0) {
			retMap.put("isSuccess", false);
			retMap.put("failMsg", "根据查询条件未查询到信息!");
		} else {
			retMap.put("isSuccess", true);
		}
		retMap.put("pageSize", pageSize);
		retMap.put("allRow", totalRow);

		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}
	
	/**
	 * 用于查看华夏影像系统
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void querysinoimg(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		Map retMap = new HashMap();
		Map sysMap = requestBean.getSysMap();
		//批次查询条件
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("dateS").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		Object operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo");
		Object flowId = ((Map)requestBean.getParameterList().get(0)).get("flowId");
		Object acctNo = ((Map)requestBean.getParameterList().get(0)).get("acctNo");
		Object amountS = ((Map)requestBean.getParameterList().get(0)).get("amountS");
		Object amountE = ((Map)requestBean.getParameterList().get(0)).get("amountE");
		
		HashMap condMap = new HashMap<String, String>();
		condMap.put("siteNo", siteNo);
		condMap.put("occurDate", occurDate);
		condMap.put("operatorNo", operatorNo);
		condMap.put("flowId", flowId);
		condMap.put("acctNo", acctNo);
		condMap.put("amountS", amountS);
		condMap.put("amountE", amountE);
		
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
		retMap.put("imageList", tmpDataMapper.selectSinoImageByMap(condMap));
		long totalRow = page.getTotal();
		if (totalRow == 0) {
			retMap.put("isSuccess", false);
			retMap.put("failMsg", "根据查询条件未查询到信息!");
		} else {
			retMap.put("isSuccess", true);
		}
		retMap.put("pageSize", pageSize);
		retMap.put("allRow", totalRow);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}
	/**
	 * 查询当日业务量信息
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void querdiary(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
																				 														
							  
		Map retMap = new HashMap();
		ArrayList<String> returnKeys = new ArrayList<String>();
		ArrayList<String> resultValues = new ArrayList<String>();
		
		returnKeys.add("核心");
		resultValues.add("50");
		returnKeys.add("影像");
		resultValues.add("100");
		returnKeys.add("智能柜台");
		resultValues.add("60");
		returnKeys.add("移动PAD");
		resultValues.add("80");
		
		retMap.put("resultTitle", "每日业务量统计");
		retMap.put("returnKeys", returnKeys);
		retMap.put("resultValues", resultValues);
		responseBean.setRetMap(retMap);
														  
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
																															 
   
	}
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	private void queryBatchAndImageInfo(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		Map retMap = new HashMap();
		HashMap condMap = new HashMap();
		String flag = "0";// 0:正常， 1：正常查询结果为空， 2：查询异常
		/*
		 * "busi_date":"20180808",//业务日期 "organ_no":"9999",//机构号
		 * "teller_no":"10001",//柜员号 "flow_no":"1010101"//流水号
		 */
		List resultlist = new ArrayList();

		logger.info(
				"Scan2EcmImageClient--业务日期busi_date:" + sysMap.get("busi_date") + " 流水号flow_no:" + sysMap.get("flow_no")
						+ " 机构号organ_no:" + sysMap.get("organ_no") + " 柜员号teller_no:" + sysMap.get("teller_no"));
		condMap.put("occurDate", sysMap.get("busi_date"));// 业务日期
		condMap.put("flowId", sysMap.get("flow_no"));// 流水号
		String siteNo = sysMap.get("organ_no").toString();
		condMap.put("siteNo", siteNo);// 机构号
		condMap.put("operatorNo", sysMap.get("teller_no"));// 柜员号
		List<HashMap<String, Object>> batchdatelist = tmpBatchMapper.getBatchIdandInputDate(condMap);
		if (batchdatelist == null || batchdatelist.size() < 1) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("无对应图像可查看");
			logger.info("未找到该批次，请联系管理员");
			return;
		}
		HashMap<String, Object> map = batchdatelist.get(0);
		String batchId = (String) map.get("BATCH_ID");
		batchId = BaseUtil.filterSqlParam(batchId);
		String inputDate = (String) map.get("INPUT_DATE");
		condMap.put("batchId", batchId);// 批次
		List<HashMap<String, Object>> fNamesList = tmpBatchMapper.getFileNamesList(condMap);
		List<Map> fileNamesList = new ArrayList<Map>();// 存储url名称
		Map fileNamesMap=new HashMap();
		for (int i = 0; i < fNamesList.size(); i++) {
			HashMap<String, Object> mapfile = fNamesList.get(i);
			mapfile.put("BATCH_ID", batchId);
			mapfile.put("INPUT_DATE", inputDate);
			/*String FILE_NAME = (String) mapfile.get("FILE_NAME");
			String BACK_FILE_NAME = (String) mapfile.get("BACK_FILE_NAME");
			fileNamesList.add(FILE_NAME);
			fileNamesList.add(BACK_FILE_NAME);*/
			fileNamesList.add(mapfile);
		}
		retMap.put("fileNamesList", fileNamesList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
	}

	
}
