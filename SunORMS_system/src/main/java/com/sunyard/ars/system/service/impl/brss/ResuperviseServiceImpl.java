package com.sunyard.ars.system.service.impl.brss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.dao.FlowMapper;
import com.sunyard.ars.common.dao.TmpBatchMapper;
import com.sunyard.ars.common.dao.TmpDataMapper;
import com.sunyard.ars.common.pojo.TmpData;
import com.sunyard.ars.system.bean.et.BusiForm;
import com.sunyard.ars.system.bean.et.EtProcessTb;
import com.sunyard.ars.system.bean.sc.BusinessType;
import com.sunyard.ars.system.dao.brss.ResuperviseMapper;
import com.sunyard.ars.system.dao.et.BusiFormMapper;
import com.sunyard.ars.system.dao.et.EtProcessTbMapper;
import com.sunyard.ars.system.dao.sc.BusinessTypeMapper;
import com.sunyard.ars.system.dao.sc.VoucherInfoMapper;
import com.sunyard.ars.system.service.brss.IResuperviseService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.mybatis.pojo.User;


@Service("resuperviseService")
@Transactional
public class ResuperviseServiceImpl extends BaseService  implements IResuperviseService{

	@Resource
	private TmpDataMapper tmpDataMapper;
	
	@Resource
	private FlowMapper flowMapper;
	
	@Resource
	private BusinessTypeMapper businessTypeMapper;
	
	@Resource
	private ResuperviseMapper resuperviseMapper;
	
	@Resource
    private VoucherInfoMapper scVoucherInfoMapper;
	
	@Resource
	private TmpBatchMapper tmpBatchMapper;
	
	@Resource
	private BusiFormMapper busiFormMapper;
	
	@Resource
	private EtProcessTbMapper etProcessTbMapper;
	
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map reqMap = new HashMap();
		reqMap = requestBean.getSysMap();
		String oper_type = (String) reqMap.get("oper_type");
		if("querySelectInfo".equals(oper_type)) {
			querySelectInfo(requestBean,responseBean);
		} else if("QueryBatch".equalsIgnoreCase(oper_type)){
			queryBatchDataInfo(requestBean, responseBean);
		} else if("queryWrongDetail".equals(oper_type)) {
			//查询差错详情
			queryWrongDetail(requestBean, responseBean);
		} else if("queryBusiForm".equals(oper_type)){
			//查询一个差错详情，页面查看按钮
			queryOneBusiform(requestBean, responseBean);
		} if("upDateWrong".equals(oper_type)) {
			upDataImfWrong(requestBean, responseBean);
		} 
		
	}

	


	private void queryOneBusiform(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		String formId = ((Map)requestBean.getParameterList().get(0)).get("formId").toString();
		BusiForm busiForm = busiFormMapper.selectByPrimaryKey(formId);
		
		HashMap<String, Object> config = new HashMap<String,Object>();
		config.put("formId", formId);
		List<EtProcessTb> processes = etProcessTbMapper.getProcesses(config);
		/*
		 * if(processes.size()==0) { config.put("formId", "CCD2017012107203970");
		 * processes = etProcessTbMapper.getProcesses(config); //数据库没有数据先测一下 }
		 */
		//返回结果map
		Map<Object,Object> retMap = new HashMap<Object,Object>();
		retMap.put("busiForm", busiForm);
		retMap.put("etProcessList",processes);
		responseBean.setRetMap(retMap);
		responseBean.setRetMsg("查询成功!");
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
	}

	@SuppressWarnings({ "unused", "rawtypes"})
	private void upDataImfWrong(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		
		String serialNo = ((Map)requestBean.getParameterList().get(0)).get("serialNo").toString();
		String errorFlag =  ((Map)requestBean.getParameterList().get(0)).get("errorFlag").toString();
		TmpData tmpData = new TmpData();
		tmpData.setSerialNo(serialNo);
		tmpData.setErrorFlag(errorFlag);
		tmpDataMapper.updateByPrimaryKeySelective(tmpData);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改图像差错标志成功!");
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	private void queryWrongDetail(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		//返回结果map
		Map<Object,Object> retMap = new HashMap<Object,Object>();
		
		Map sysMap = requestBean.getSysMap();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		//图像序号
		String serialNo = ((Map)requestBean.getParameterList().get(0)).get("serialNo").toString();
		String flowId = ((Map)requestBean.getParameterList().get(0)).get("flowId").toString();
		
		HashMap<String, Object> condMap = new HashMap<String, Object>();
		condMap.put("siteNo", siteNo);
		//获取表名
		List<HashMap<String,Object>> tableStr = tmpBatchMapper.getTableName(condMap);
		if(tableStr == null || tableStr.size() == 0) {
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("表定义中未对该机构定义数据表!");
			return;
		}
		String batchTable = ((Map) tableStr.get(0)).get("BP_BATCH_TB")+"";
		String dataTable = ((Map) tableStr.get(0)).get("BP_DATA_TB")+"";
		String flowTable = ((Map) tableStr.get(0)).get("FL_FLOW_TB")+"";
		batchTable = BaseUtil.filterSqlParam(batchTable);
		dataTable = BaseUtil.filterSqlParam(dataTable);
		flowTable = BaseUtil.filterSqlParam(flowTable);

		condMap.put("batchTable", batchTable);
		condMap.put("occurDate", occurDate);
		condMap.put("operatorNo", operatorNo);
		List<Map<String, Object>> batchInfo = resuperviseMapper.selectBatchInfo(condMap);
		if(batchInfo != null && batchInfo.size() > 0) {
			String imageStatus = batchInfo.get(0).get("IMAGE_STATUS").toString();
			String progressFlag = batchInfo.get(0).get("PROGRESS_FLAG").toString();
			dataTable = batchInfo.get(0).get("TEMP_DATA_TABLE").toString();
			//"99" 图像处理标志  
			//图像状态:"2"已归档 "3" 已清理
			if(progressFlag.equals("99") && (imageStatus.equals("2") || imageStatus.equals("3") )) {
				
				switch (ARSConstants.archiveMode) {
				case ARSConstants.SINGLE_ARCHVE_MODE:
					dataTable = dataTable + "_HIS";
					break;
				case ARSConstants.MORE_ARCHVE_MODE:
					dataTable = dataTable + "_" + occurDate;
					break;
				default:
					break;
				}
			}
			
		}
		if(dataTable == null || dataTable.equals("")) {
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("表定义中未对该机构定义数据表!");
			return;
		}
		
		//....
		/*
		 * BusiForm busiForm = new BusiForm(); busiForm.setFlowNo(flowId);
		 * busiForm.setTellerNo(operatorNo); busiForm.setNetNo(siteNo);
		 * busiForm.setOperationDate(occurDate);
		 * 
		 * List<BusiForm> busiFormList = busiFormMapper.selectBySelective(busiForm, new
		 * HashMap<String,Object>());
		 */
		
		HashMap<String, Object> confi = new HashMap<String,Object>();
		confi.put("siteNo", siteNo);
		confi.put("flowNo", flowId);
		confi.put("occurDate", occurDate);
		confi.put("operatorNo", operatorNo);
		
		int pageNum = (int) sysMap.get("currentPage");
		// 每页数量
		int pageSize = 0;
		if (pageNum != -1) {
			
			String initPageNum = (String) sysMap.get("pageSize");
			/*if (BaseUtil.isBlank(initPageNum +"")) {
				pageSize = ARSConstants.PAGE_NUM;
			}*/ 
			if (initPageNum == null || initPageNum.trim().equals("null") || initPageNum.trim().equals("")) {
				pageSize = ARSConstants.PAGE_NUM;
			}else {
				pageSize = Integer.parseInt(initPageNum);
			}
		}
		// 定义分页操作
		Page page = PageHelper.startPage(pageNum, pageSize);
		
		List<Map<String, Object>> busiFormList = resuperviseMapper.selectBusiFormBySelective(confi);
		retMap.put("busiFormList", busiFormList);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询差错成功!");
		retMap.put("pageSize", pageSize);
		retMap.put("allRow", page.getTotal());
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryBatchDataInfo(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map retMap = new HashMap();
		Map sysMap = requestBean.getSysMap();
		
		String startDate = ((Map)requestBean.getParameterList().get(0)).get("dateS").toString();
		String endDate = ((Map)requestBean.getParameterList().get(0)).get("dateE").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		String businessId = ((Map)requestBean.getParameterList().get(0)).get("businessId").toString();
		
		//流水查询条件
		String amountS = ((Map)requestBean.getParameterList().get(0)).get("amountS").toString();
		String amountE = ((Map)requestBean.getParameterList().get(0)).get("amountE").toString();
		String vouhNo = ((Map)requestBean.getParameterList().get(0)).get("vouhNo").toString();
		String acctNo = ((Map)requestBean.getParameterList().get(0)).get("acctNo").toString();
		String currency_type = ((Map)requestBean.getParameterList().get(0)).get("currencyType").toString();
		String flow_id = ((Map)requestBean.getParameterList().get(0)).get("flowId").toString();
		String subject_no = ((Map)requestBean.getParameterList().get(0)).get("subjectNo").toString();
		
		
		String perStr = ((Map)requestBean.getParameterList().get(0)).get("per").toString();
		String isFirstStr = ((Map)requestBean.getParameterList().get(0)).get("FirstFlag").toString();
		//SUPERVISE_DEAL 0-未进过重点监督，1-经过重点监督  isSuperEt
		String superviseDeal = ((Map)requestBean.getParameterList().get(0)).get("isSuperEt").toString();
		//精确索引字段   
		//!Constants.BUSINESS_ID_FINANCIAL_SCAN.equals(businessId)
		/*
		 * boolean tmpIsHasPrecisionIndex = false;
		 * 
		 * tmpIsHasPrecisionIndex = tmpIsHasPrecisionIndex || !flow_id.equals("") ||
		 * !acctNo.equals("") || !amountS.equals("") || !amountE.equals("") ||
		 * !subject_no.equals("") || !vouhNo.equals("") || !currency_type.equals("") ;
		 */
		
		
		HashMap condMap = new HashMap<String, String>();
		condMap.put("siteNo", siteNo);
		//获取表名
		List tableStr = tmpBatchMapper.getTableName(condMap);
		if(tableStr == null || tableStr.size() == 0) {
			retMap.put("isSuccess", false);
			retMap.put("failMsg", "表定义中未对该机构定义数据表!");
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
			return;
		}
		
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
		condMap.put("businessId", businessId);
		
		List batchInfo = tmpBatchMapper.getBatchInfo(condMap);
		if(batchInfo == null || batchInfo.size() == 0) {
			retMap.put("isSuccess", false);
			retMap.put("failMsg", "不存在批次信息");
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
			return;
		}
		
		String imageStatus = "";
		String progressFlag = "";
		String dataTb = "";
		Hashtable batchMap = new Hashtable();
		for (Object batch : batchInfo) {
			imageStatus = ((Map)batch).get("IMAGE_STATUS").toString();
			progressFlag = ((Map)batch).get("PROGRESS_FLAG").toString();
			dataTb = ((Map)batch).get("TEMP_DATA_TABLE").toString();
			
			if (progressFlag.equals(ARSConstants.PROCESS_FLAG_99)
					&& (imageStatus.equals(ARSConstants.IMAGE_STATUS_2) || imageStatus
							.equals(ARSConstants.IMAGE_STATUS_3))) {
				dataTb =  dataTb + "_HIS";
			}
			// 存放所有表名
			if (!batchMap.containsKey(dataTb)) {// 若果第一次写入
				batchMap.put(dataTb, ((Map)batch).get("BATCH_ID").toString());
			} else {
				batchMap.put(dataTb, ((Map)batch).get("BATCH_ID").toString() + ","+batchMap.get(dataTb));
			}
		}
		
		
		Iterator it = batchMap.keySet().iterator();
		List<String> dataTableList = new ArrayList<String>();
		while(it.hasNext()){
			dataTableList.add((String) it.next()) ;
		}
		
		condMap.put("dataTableList", dataTableList);
		condMap.put("flowTable", flowTable);
		condMap.put("hisFlowTable", flowTable+"_HIS");
		condMap.put("flow_id", flow_id);
		condMap.put("subject_no", subject_no);
		condMap.put("currency_type", currency_type);
		condMap.put("acctNo", acctNo);
		condMap.put("vouhNo", vouhNo);
		condMap.put("amountE", amountE);
		condMap.put("amountS", amountS);
		condMap.put("superviseDeal",superviseDeal); //是否重点监督
		
		int per = Integer.parseInt(perStr);
		
		if( per == 100 ) {
			//不用操作临时表
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
			retMap.put("imageList", tmpDataMapper.selectImageByFlow(condMap));
			long totalRow = page.getTotal();
			if(totalRow == 0){
				retMap.put("isSuccess", false);
				retMap.put("failMsg", "根据查询条件未查询到信息!");
			}else{
				retMap.put("isSuccess", true);
			}
			retMap.put("pageSize", pageSize);
			retMap.put("allRow", totalRow);
			
		}else if(per != 100 && "false".equals(isFirstStr)) {
			//不为100，第一次查询（分页）需要先删再把批次插入到临时表当中
			condMap.put("per", per + "");
			User loginUser = BaseUtil.getLoginUser();
			resuperviseMapper.deleteBrssTempSampleByUserNo(loginUser.getUserNo());
			condMap.put("batchMap", batchMap);
			List<Map<String, Object>> flowIdList = resuperviseMapper.selectFlowIdByConfig(condMap);
			if(flowIdList == null || flowIdList.size() == 0) {
				//比例不对
				retMap.put("isSuccess", false);
				retMap.put("failMsg", "请调整抽样比例");
				responseBean.setRetMap(retMap);
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
				responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
				return;
			}
			
			List<String> flowList = new ArrayList<String>();
			HashMap<String, Object> brssTempInsertInfo = new HashMap<String,Object>();
			for(int i = 0; i < flowIdList.size();i++) {
				brssTempInsertInfo.put("sampleId", i+"");
				brssTempInsertInfo.put("flowId", flowIdList.get(i).get("FLOW_ID").toString());
				brssTempInsertInfo.put("userNo", loginUser.getUserNo());
				brssTempInsertInfo.put("lastModifyTime", BaseUtil.getCurrentTimeStr());
				resuperviseMapper.insertToBrssTempSample(brssTempInsertInfo);
				flowList.add(flowIdList.get(i).get("FLOW_ID").toString());
			}
			
			condMap.put("flowList",flowList);
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
			List<Map<String, Object>> imageList = resuperviseMapper.selectImageBySomeFlow(condMap);
			retMap.put("imageList", imageList);
			long totalRow = page.getTotal();
			if(totalRow == 0){
				retMap.put("isSuccess", false);
				retMap.put("failMsg", "根据查询条件未查询到信息!");
			}else{
				retMap.put("isSuccess", true);
			}
			retMap.put("pageSize", pageSize);
			retMap.put("allRow", totalRow);
			
		}else {
			//从临时样品表中取得所需流水，再去查询
			List<String> flowList = new ArrayList<String>();
			List<Map<String, Object>> selectFlowlist = resuperviseMapper.selectBrssTempSampleByUserNo(BaseUtil.getLoginUser().getUserNo());
			for(int i = 0;i < selectFlowlist.size();i++) {
				flowList.add(BaseUtil.filterSqlParam(selectFlowlist.get(i).get("FLOW_ID").toString()));
			}
			if(flowList.size() == 0) {
				//比例不对
				retMap.put("isSuccess", false);
				retMap.put("failMsg", "临时表中没有流水数据");
				responseBean.setRetMap(retMap);
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
				responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
				return;
			}
			condMap.put("flowList",flowList);
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
			List<Map<String, Object>> imageList = resuperviseMapper.selectImageBySomeFlow(condMap);
			retMap.put("imageList", imageList);
			long totalRow = page.getTotal();
			if(totalRow == 0){
				retMap.put("isSuccess", false);
				retMap.put("failMsg", "根据查询条件未查询到信息!");
			}else{
				retMap.put("isSuccess", true);
			}
			retMap.put("pageSize", pageSize);
			retMap.put("allRow", totalRow);
		}
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
		
	}

	//查询页面的初始化信息
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void querySelectInfo(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		//货币种类
		List<Map<String, Object>> currentTypeInfoList = resuperviseMapper.selectCurrentTypeInfo();
		
		//查询业务类型
		BusinessType businessType = new BusinessType();
		List<BusinessType> businessTypeList = businessTypeMapper.selectBySelective(businessType);
		
		retMap.put("busiArr", businessTypeList );
		retMap.put("currencyTypeArr", currentTypeInfoList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}
	
	

}
