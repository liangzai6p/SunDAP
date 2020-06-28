package com.sunyard.ars.file.service.impl.ma;


import E.B;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.dao.*;
import com.sunyard.ars.common.pojo.TmpBatch;
import com.sunyard.ars.common.pojo.TmpData;
import com.sunyard.ars.file.dao.ma.VoucherInfoMapper;
import com.sunyard.ars.file.dao.scan.VoucherChkMapper;
import com.sunyard.ars.file.pojo.ma.FormInfoDetail;
import com.sunyard.ars.file.pojo.ma.MaParameterBean;
import com.sunyard.ars.file.service.ma.IBatchTaskService;
import com.sunyard.ars.flows.service.api.SunFlowService;
import com.sunyard.ars.flows.service.api.WorkItemInfo;
import com.sunyard.ars.flows.service.impl.api.SunFlowServiceImpl;
import com.sunyard.ars.system.bean.busm.Teller;
import com.sunyard.ars.system.bean.busm.UserBean;
import com.sunyard.ars.system.bean.sc.OrganData;
import com.sunyard.ars.system.dao.busm.OrganInfoDao;
import com.sunyard.ars.system.dao.busm.TellerMapper;
import com.sunyard.ars.system.dao.busm.UserDao;
import com.sunyard.ars.system.dao.sc.OrganDataMapper;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.mybatis.pojo.User;
import com.sunyard.sunflow.client.FlowClient;
import com.sunyard.sunflow.client.ISunflowClient;
import com.sunyard.sunflow.engine.dataclass.WMTAttribute;
import com.sunyard.sunflow.engine.workflowexception.SunflowException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

@Service("batchTaskService")
@Transactional
public class BatchTaskServiceImpl extends BaseService  implements IBatchTaskService {
    
    @Resource
	private TmpBatchMapper tmpBatchMapper;
    
    @Resource
	private TmpDataMapper tmpDataMapper;
    
    @Resource
	private FlowMapper flowMapper;
    
    @Resource
    private VoucherInfoMapper voucherInfoMapper;
    
    @Resource
    private SmFieldDefTbMapper smFieldDefTbMapper;
    
    @Resource
    private OrganInfoDao organInfoDao;
    
    @Resource
    private TellerMapper tellerMapper;
    
    @Resource
    private UserDao userDao;
    
    @Resource
	private VoucherChkMapper voucherChkMapper;
    @Resource
    private OcrCheckPageGxtxsyDao ocrCheckPageGxtxsyDao;
    
    @Resource
    private OrganDataMapper organDataMapper;

    @Resource
	private CommonMapper commonMapper;
    
    private SunFlowService sunFlowService = new SunFlowServiceImpl();

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

		if ("QUERYBATCH".equalsIgnoreCase(oper_type)) {//查询批次任务
			queryBatch(requestBean, responseBean);
		} else if("CHOOSEBATCH".contentEquals(oper_type)) {
			//查询折线图补录数据
			chooseBatch(requestBean, responseBean);
		}else if ("QUERYFORM".equalsIgnoreCase(oper_type)) {//获取所有版面
			queryAllForm(requestBean, responseBean);
		} else if ("QUERYDETAIL".equalsIgnoreCase(oper_type)){//获取详细信息
			queryDetail(requestBean, responseBean);
		} else if ("QUERYBATCHINFO".equalsIgnoreCase(oper_type)){//获取详细信息
			queryBatchInfo(requestBean, responseBean);
		} else if ("GETTASKLIST".equalsIgnoreCase(oper_type)){//获取详细信息
			queryBatchList(requestBean, responseBean);
		} else if ("COMMITBATCH".equalsIgnoreCase(oper_type)){//提交批次
			commitBatch(requestBean, responseBean);
		} else if ("UPDATEBATCH".equalsIgnoreCase(oper_type)){//修改批次
			updateBatch(requestBean, responseBean);
		} else if ("VOIDBATCH".equalsIgnoreCase(oper_type)){//作废批次
			voidBatch(requestBean, responseBean);
		} else if ("FREEBATCH".equalsIgnoreCase(oper_type)){//释放批次
			freeBatch(requestBean, responseBean);
		} else if ("JUMPBATCH".equalsIgnoreCase(oper_type)){//跳过批次
			jumpBatch(requestBean, responseBean);
		} else if("QUERYCOUNT".equalsIgnoreCase(oper_type)){//查询未处理图像与流水数量
			queryImageFlowCount(requestBean, responseBean);
		} else if("LOADTABLE".equalsIgnoreCase(oper_type)){//切换选项卡
			loadTableImage(requestBean, responseBean);
		}else if("freeMyFlow".contentEquals(oper_type)) {
			//查询折线图补录数据
			freeMyFlow(requestBean, responseBean);
		}else if("queryByBatchFlow".equalsIgnoreCase(oper_type)) {
			//查询批次，流水信息
			queryByBatchFlow(requestBean, responseBean);
		}
	}	

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadTableImage(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		ArrayList reqList = (ArrayList) requestBean.getParameterList();
		MaParameterBean maParameterBean =  (MaParameterBean) reqList.get(0);
		HashMap condMap = new HashMap();
		condMap.put("batchId", maParameterBean.getBatchId());
		condMap.put("queryType", maParameterBean.getProcessState());
		List<TmpData> tmpDataList = tmpDataMapper.selectByBatchId(condMap);
		retMap.put("tmpDataList", tmpDataList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateBatch(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		ArrayList reqList = (ArrayList) requestBean.getParameterList();
		MaParameterBean maParameterBean =  (MaParameterBean) reqList.get(0);
		//修改批次表、登记表和流水表
		// 构造条件参数
		TmpBatch tmpbatch = tmpBatchMapper.selectByPrimaryKey(maParameterBean.getBatchId());
		HashMap condMap = new HashMap();
		condMap.put("batchTb", "BP_TMPBATCH_TB");
		condMap.put("newOccurDate", maParameterBean.getOccurDate());
		condMap.put("newOperatorNo", maParameterBean.getOperatorNo());
		condMap.put("newSiteNo", maParameterBean.getSiteNo());
		condMap.put("batchId", maParameterBean.getBatchId());
		condMap.put("newOcrFactorFlag","0");
		condMap.put("newProgressFlag","10");
		tmpBatchMapper.updateTmpBatch(condMap);
		//voucherChkMapper.updateByBatchId(condMap);
		
		condMap.put("operatorNo", maParameterBean.getOperatorNo());
		condMap.put("siteNo", maParameterBean.getSiteNo());
		condMap.put("occurDate", maParameterBean.getOccurDate());
		voucherChkMapper.updateByBatchId(condMap);
		flowMapper.updateFreeAllFlow(condMap);
		
		//condMap.put("newPsLevel", "1");
		condMap.put("newRecFailCause", "");
		condMap.put("newRecDate", "");
		condMap.put("newProcessState", "000000");
		condMap.put("newIsAudit", "0");
		condMap.put("newFormName", "");
		condMap.put("newFormGroup", "");
		condMap.put("newPsLevel", "0");
		condMap.put("newPrimaryInccodein", 0);
		condMap.put("oldCheckFlag", "1");//释放图像BUG修复 20190415
		tmpDataMapper.updateAllTmpData(condMap);
		//修改工作流节点
		WorkItemInfo arsWorkItemInfo = new WorkItemInfo();
		arsWorkItemInfo.setWorkItemId(maParameterBean.getWorkId());
		Map<String, String> workItemAttris = new HashMap<>();
		workItemAttris.put("flowFlag", "10");//修改流程变量为10自动识别
		arsWorkItemInfo.setWorkItemAttris(workItemAttris);

		
		boolean falg = sunFlowService.updateWorkItemInfo(BaseUtil.getLoginUser().getUserNo(), arsWorkItemInfo,tmpbatch.getAreaCode());
		if(falg){
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
//			condMap.clear();
//			condMap.put("operatorNo", tmpbatch.getOperatorNo());
//			condMap.put("siteNo", tmpbatch.getSiteNo());
//			condMap.put("occurDate", tmpbatch.getOccurDate());
//			if(flowMapper.selectDealCount(condMap)==0&&tmpBatchMapper.existsBatch(condMap)==0){
//				String sqlString2="DELETE FROM FL_CHECKOFF_TB WHERE OPERATOR_NO= '"+tmpbatch.getOperatorNo()+"'"+" AND SITE_NO = '"+tmpbatch.getSiteNo()+"'"+" AND OCCUR_DATE = '"+tmpbatch.getOccurDate()+"'";
//				commonMapper.executeUpdate(sqlString2);
//			}
			
			//添加日志
			String log = "任务批次修改，批次号为" + maParameterBean.getBatchId();
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_28, log);
		}else{
			responseBean.setRetMsg("修改批次失败!");
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked"})
	private void voidBatch(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		ArrayList reqList = (ArrayList) requestBean.getParameterList();
		MaParameterBean maParameterBean =  (MaParameterBean) reqList.get(0);
		//修改批次表、登记表和流水表
		// 构造条件参数
		HashMap condMap = new HashMap();
		TmpBatch batch = tmpBatchMapper.selectByPrimaryKey(maParameterBean.getBatchId());
		condMap.put("batchTb", "BP_TMPBATCH_TB");
		condMap.put("newIsInvalid", "0");
		condMap.put("batchId", maParameterBean.getBatchId());
		condMap.put("operatorNo", maParameterBean.getOperatorNo());
		condMap.put("siteNo", maParameterBean.getSiteNo());
		condMap.put("occurDate", maParameterBean.getOccurDate());
		tmpBatchMapper.updateTmpBatch(condMap);
		voucherChkMapper.updateByBatchId(condMap);
		flowMapper.updateFreeAllFlow(condMap);
//		condMap.put("newPsLevel", "1");
		condMap.put("newRecFailCause", "");
		condMap.put("newRecDate", "");
		condMap.put("newProcessState", "000000");
		condMap.put("newIsAudit", "0");
		condMap.put("newFormName", "");
		condMap.put("newFormGroup", "");
		condMap.put("newPsLevel", "0");
		condMap.put("newPrimaryInccodein", 0);
		condMap.put("oldCheckFlag", "1");//释放图像BUG修复 20190415
		tmpDataMapper.updateAllTmpData(condMap);

		//终止工作流流程
		boolean voidFlag = sunFlowService.terminateProcessInstance(BaseUtil.getLoginUser().getUserNo(),maParameterBean.getProcinstId(),batch.getAreaCode());
		if(voidFlag){
//			condMap.clear();//判删轧账表
//			condMap.put("operatorNo", selectByPrimaryKey.getOperatorNo());
//			condMap.put("siteNo", selectByPrimaryKey.getSiteNo());
//			condMap.put("occurDate", selectByPrimaryKey.getOccurDate());
//		if(flowMapper.selectDealCount(condMap)==0&&tmpBatchMapper.existsBatch(condMap)==0){
//			String sqlString2="DELETE FROM FL_CHECKOFF_TB WHERE OPERATOR_NO= '"+selectByPrimaryKey.getOperatorNo()+"'"+" AND SITE_NO = '"+selectByPrimaryKey.getSiteNo()+"'"+" AND OCCUR_DATE = '"+selectByPrimaryKey.getOccurDate()+"'";
//			commonMapper.executeUpdate(sqlString2);
//		}
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			
			//添加日志
			String log = "批次作废，批次号为" + maParameterBean.getBatchId();
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_28, log);
		}else{
			responseBean.setRetMsg("作废批次失败!");
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
		}
	}

	@SuppressWarnings({ "rawtypes"})
	private void freeBatch(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		ArrayList reqList = (ArrayList) requestBean.getParameterList();
		MaParameterBean maParameterBean =  (MaParameterBean) reqList.get(0);
		boolean releaseFlag = sunFlowService.release(BaseUtil.getLoginUser().getUserNo(),maParameterBean.getWorkId());
		if(releaseFlag){
			String log = "任务批次释放，批次号为" + maParameterBean.getBatchId();
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_28, log);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		}else{
			responseBean.setRetMsg("释放批次失败!");
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
		}
	}

	@SuppressWarnings({ "rawtypes"})
	private void jumpBatch(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		ArrayList reqList = (ArrayList) requestBean.getParameterList();
		MaParameterBean maParameterBean =  (MaParameterBean) reqList.get(0);
		TmpBatch batch = tmpBatchMapper.selectByPrimaryKey(maParameterBean.getBatchId());
		boolean prioFlag = sunFlowService.modifyProcessInstancePrio(BaseUtil.getLoginUser().getUserNo(), maParameterBean.getProcinstId(), 40, batch.getAreaCode());
		boolean releaseFlag = false;
		if(prioFlag){
			releaseFlag = sunFlowService.release(BaseUtil.getLoginUser().getUserNo(),maParameterBean.getWorkId());

		}
		if(releaseFlag){
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			
			//添加日志
			String log = "跳过批次任务，批次号为" + maParameterBean.getBatchId();
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_28, log);
			
		}else{
			responseBean.setRetMsg("跳过批次失败!");
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
		}
	}

	@SuppressWarnings({ "rawtypes"})
	private void commitBatch(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		ArrayList reqList = (ArrayList) requestBean.getParameterList();
		MaParameterBean maParameterBean =  (MaParameterBean) reqList.get(0);
		WorkItemInfo arsWorkItemInfo = new WorkItemInfo();
		arsWorkItemInfo.setWorkItemId(maParameterBean.getWorkId());
		Map<String, String> workItemAttris = new HashMap<>();
		workItemAttris.put("flowFlag", "98");//修改流程变量为98完成
		arsWorkItemInfo.setWorkItemAttris(workItemAttris);
		TmpBatch batch = tmpBatchMapper.selectByPrimaryKey(maParameterBean.getBatchId());
		boolean falg = sunFlowService.updateWorkItemInfo(BaseUtil.getLoginUser().getUserNo(), arsWorkItemInfo,batch.getAreaCode());
		if(falg){
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			String batchId = maParameterBean.getBatchId();
			batchId = BaseUtil.filterSqlParam(batchId);
			TmpBatch selectByPrimaryKey = tmpBatchMapper.selectByPrimaryKey(batchId);
			selectByPrimaryKey.setProgressFlag("98");
			tmpBatchMapper.updateByPrimaryKeySelective(selectByPrimaryKey);
			ocrCheckPageGxtxsyDao.callOcrCheckPageGxtxsyDao(batchId);
			
			//添加日志
			String log = "任务批次提交，批次号为" + maParameterBean.getBatchId();;
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_28, log);
		}else{
			responseBean.setRetMsg("提交批次失败!");
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
		}	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryBatchList(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		Map retMap = new HashMap();
		User user = BaseUtil.getLoginUser();

		Map reqMap =  requestBean.getSysMap();
		String batchIdNow = (String) reqMap.get("batchId");

		//获取用户区域号
		OrganData organData = organDataMapper.selectByPrimaryKey(user.getOrganNo());
		List<WorkItemInfo> workList = sunFlowService.getWorksByUserId(BaseUtil.getLoginUser().getUserNo(),"ma",String.valueOf(organData.getServiceId()),100);
		List<TmpBatch> batchList = new ArrayList<TmpBatch>();
		TmpBatch batch = null;
		TmpBatch selectByPrimaryKey = null;
		if(workList != null && workList.size() > 0){
			for (WorkItemInfo work : workList) {
				batch = new TmpBatch();
				String batchId = work.getWorkItemAttris().get("batchId");
				selectByPrimaryKey = tmpBatchMapper.selectByPrimaryKey(batchId);
				if(selectByPrimaryKey!=null){
					batch.setBatchId(selectByPrimaryKey.getBatchId());
					batch.setOccurDate(selectByPrimaryKey.getOccurDate());
					batch.setSiteNo(selectByPrimaryKey.getSiteNo());
					batch.setOperatorNo(selectByPrimaryKey.getOperatorNo());
					batch.setProgressFlag("等待业务审核");
					batchList.add(batch);
				}
				
			}
		}
		//新增可以查询到自己已获取的
		/*List<WorkItemInfo> workItemList = sunFlowService.getWorkItemByUser(BaseUtil.getLoginUser().getUserNo(),"ma",String.valueOf(organData.getServiceId()),100);
		if(workItemList != null && workItemList.size() > 0){
			for (WorkItemInfo workItemInfo : workItemList) {
					batch = new TmpBatch();
					String batchId = workItemInfo.getWorkItemAttris().get("batchId");
					selectByPrimaryKey = tmpBatchMapper.selectByPrimaryKey(batchId);
					if(selectByPrimaryKey!=null){
						batch.setBatchId(selectByPrimaryKey.getBatchId());
						batch.setOccurDate(selectByPrimaryKey.getOccurDate());
						batch.setSiteNo(selectByPrimaryKey.getSiteNo());
						batch.setOperatorNo(selectByPrimaryKey.getOperatorNo());
						batch.setProgressFlag("等待业务审核");
						if(!batchList.contains(batch)){
							batchList.add(batch);
						}
				}
			}
		}*/
		retMap.put("batchList", batchList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		
		//添加日志
		String log = "获取任务列表";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_28, log);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryBatchInfo(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		ArrayList reqList = (ArrayList) requestBean.getParameterList();
		MaParameterBean maParameterBean =  (MaParameterBean) reqList.get(0);
		
//		String operatorName = tellerMapper.selectTellerInfo(maParameterBean.getOperatorNo()).getTellerName();
//		String siteName = organInfoDao.selectSiteInfo(maParameterBean.getSiteNo()).get(0).get("ORGAN_NAME").toString();
//		String inputWorkerName = userDao.selectUserInfo(maParameterBean.getInputWorker()).getUserName();
		Teller selectTellerInfo = tellerMapper.selectTellerInfo(maParameterBean.getOperatorNo());
		String operatorName =selectTellerInfo==null?"NULL":selectTellerInfo.getTellerName();
		HashMap<String, Object> hashMap = organInfoDao.selectSiteInfo(maParameterBean.getSiteNo()).get(0);
		String siteName =hashMap==null?"NULL":hashMap.get("ORGAN_NAME").toString();
		UserBean selectUserInfo = userDao.selectUserInfo(maParameterBean.getInputWorker());
		String inputWorkerName =selectUserInfo==null?"NULL":selectUserInfo.getUserName();//优化空指针
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("selfDelete", "0");
		condMap.put("checkFlag", "-1");
		condMap.put("batchId", maParameterBean.getBatchId());
		//查询未处理图像数
		int imageToDeal = tmpDataMapper.selectImgCount(condMap);
		
		condMap.clear();
		condMap.put("occurDate", maParameterBean.getOccurDate());
		condMap.put("siteNo", maParameterBean.getSiteNo());
		condMap.put("operatorNo", maParameterBean.getOperatorNo());
		int flowToDeal = flowMapper.selectFlowToDeal(condMap);
		
		retMap.put("imageToDeal",imageToDeal);
		retMap.put("flowToDeal",flowToDeal);
		retMap.put("operatorName", operatorName);
		retMap.put("siteName", siteName);
		retMap.put("inputWorkerName", inputWorkerName);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
	}

	/**
	 * 获取详细信息
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void queryDetail(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		Map retMap = new HashMap();
		ArrayList reqList = (ArrayList) requestBean.getParameterList();
		MaParameterBean maParameterBean =  (MaParameterBean) reqList.get(0);
		String occurDate = maParameterBean.getOccurDate();
		String siteNo = maParameterBean.getSiteNo();
		String operatorNo = maParameterBean.getOperatorNo();
		String serialNo = maParameterBean.getSerialNo();
		String formName = maParameterBean.getFormName();

		// 构造条件参数
		HashMap condMap = new HashMap();
		//组装查询字段
		String str ="";
		String flowQueryFields ="";//流水查询字段
		String imageQueryFields ="";//图像查询字段
		List<String> elseNameList = new ArrayList();
		List<String> fieldNameList = new ArrayList();
		List<String> imageFieldNameList = new ArrayList();
		
		str += "DISTINCT(FLOW_ID),";
		elseNameList.add("流水号");
		fieldNameList.add("FLOW_ID");
		//获取勾对版面勾对字段
		List flowFields = smFieldDefTbMapper.getCheckFields(BaseUtil.filterSqlParam(formName));
		if(flowFields != null && flowFields.size() > 0){
			//获取图像表的英文和中文字段结果集
			String checkFields = BaseUtil.filterSqlParam(((Map)flowFields.get(0)).get("CHECK_FIELDS").toString());
			condMap.put("checkFields", checkFields.split(";"));
			List flowFieldsInfo = smFieldDefTbMapper.getFlowFieldsInfo(condMap);
			for(Object obj : flowFieldsInfo){
				if(!"flow_id".equalsIgnoreCase(((Map)obj).get("FIELD_NAME").toString())){
					str += ((Map)obj).get("FIELD_NAME") +",";
					elseNameList.add(((Map)obj).get("ELSE_NAME").toString());
					fieldNameList.add(((Map)obj).get("FIELD_NAME").toString());
				}
			}
		}
		flowQueryFields = str.substring(0, str.lastIndexOf(","));	
		
		condMap = new HashMap();
		condMap.put("occurDate",BaseUtil.filterSqlParam(occurDate));
		condMap.put("siteNo", BaseUtil.filterSqlParam(siteNo));
		condMap.put("operatorNo", BaseUtil.filterSqlParam(operatorNo));
		condMap.put("serialNo", BaseUtil.filterSqlParam(serialNo));
		condMap.put("flowQueryFields", BaseUtil.filterSqlParam(flowQueryFields));
		condMap.put("flowTb", "fl_flow_tb");
		//获取流水字段结果值
		List queryResults = flowMapper.getFlowInfoList(condMap);
		
		str = "";
		List fields = smFieldDefTbMapper.getFieldsByTableId(2);
		for (Object field : fields) {
			if(!"flow_id".equalsIgnoreCase(((Map)field).get("FIELD_NAME").toString())){
				str += ((Map)field).get("FIELD_NAME") +",";
				elseNameList.add(((Map)field).get("ELSE_NAME").toString());
				imageFieldNameList.add(((Map)field).get("FIELD_NAME").toString());
			}
		}
		imageQueryFields = str.substring(0, str.lastIndexOf(","));
		condMap.put("imageQueryFields", BaseUtil.filterSqlParam(imageQueryFields));
		List imageQueryResult = tmpDataMapper.getImageInfoList(condMap);
		
		//拼装结果集
		Object[] queryResult = new Object[elseNameList.size()];
		int index = fieldNameList.size();
		
		//流水
		if(index == 1){//只有流水号
			if(queryResults.size() > 1){
				queryResult[0] = ((Map)queryResults.get(0)).get(fieldNameList.get(0)) + ",......";
			}else if(queryResults.size() == 1){
				queryResult[0] = ((Map)queryResults.get(0)).get(fieldNameList.get(0));
			}
		}else{
			if(queryResults.size() > 1){
				queryResult[0] = ((Map)queryResults.get(0)).get(fieldNameList.get(0)) + ",......";
				for(int i = 1; i < fieldNameList.size();i++){
					queryResult[i] = ((Map)queryResults.get(0)).get(fieldNameList.get(i));
				}
			}else if(queryResults.size() == 1){
				for(int i = 0; i < fieldNameList.size();i++){
					queryResult[i] = ((Map)queryResults.get(0)).get(fieldNameList.get(i));
				}
			}
		}

		//图像
		for(int i = 0; i < imageFieldNameList.size();i++){
			queryResult[i + index] = ((Map)imageQueryResult.get(0)).get(imageFieldNameList.get(i));
		}
		for (int i = 0; i < queryResult.length;i++) {
			if(queryResult[i] == null){
				queryResult[i] = "";
			}
		}
		retMap.put("namelist",elseNameList);
		retMap.put("queryResult", queryResult);
		responseBean.setRetMap(retMap);
		retMap.put("maFlag", "querySuccess");
		retMap.put("maMsg", "查询成功！");
		responseBean.setRetCode(SunIFErrorMessage.SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}

	/**
	 * 获取所有版面
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryAllForm(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		ArrayList reqList = (ArrayList) requestBean.getParameterList();
		MaParameterBean maParameterBean =  (MaParameterBean) reqList.get(0);
		String organ_no = maParameterBean.getSiteNo();
		Map<String,String> formNameMap =  this.getFormNameMap(organ_no);  
		//所有凭证版面
		List tempAllInfo = BaseUtil.convertListMapKeyValue(voucherInfoMapper.selectAllFormInfo());
		if(tempAllInfo == null){
			retMap.put("maFlag", "queryFail");
			retMap.put("maMsg", "查询失败！");
		}else{
			//获得所有存在附件标志的版面(还需过滤)
			List tempForm = BaseUtil.convertListMapKeyValue(voucherInfoMapper.selectAttachmentForm());
			Map attachMap = new HashMap();
			for(Object objs :tempForm){
				String formName = (String) ((Map) objs).get("form_name");
//				int attachmentFlag = Integer.parseInt(((Map) objs).get("attachment_flag").toString()) ;
//				//子版面全部为附件标识的版面为附件版面（SQL中已经过滤重复和从0排序，保证先put后remove）
//				if(attachmentFlag == 0){
//					attachMap.put(formName,attachmentFlag);
//				}else{
//				//存在主件标识就移除
//					attachMap.remove(formName);
//				}
				Long attachmentFlag =Long.parseLong(((Map) objs).get("attachment_flag").toString()) ;
				//子版面全部为附件标识的版面为附件版面（SQL中已经过滤重复和从0排序，保证先put后remove）
				if(attachmentFlag == 0){
					attachMap.put(formName,attachmentFlag);
				}else{
				//存在主件标识就移除
					attachMap.remove(formName);
				}
			}
			
			/*过滤规则:子版面全部为附件的默认为附件
			 * 应用:页面上选择此版面提交时将凭证置为附件*/
			List<FormInfoDetail> allFormInfo = new ArrayList<FormInfoDetail>();
			Map<String,String> flowFieldMap = new HashMap<String, String>();
			List flowFieldList = smFieldDefTbMapper.selectFlowFieldName();
			for (Object object : flowFieldList) {
				flowFieldMap.put((String) ((Map) object).get("FIELD_NAME"), (String) ((Map) object).get("ELSE_NAME"));
			}
		
			for(Object objs :tempAllInfo){
				FormInfoDetail formInfoDetail = new FormInfoDetail();
			//	formInfoDetail.setVoucherNumber(Long.parseLong(((Map) objs).get("voucher_number").toString()) );
				formInfoDetail.setVoucherNumber(Long.parseLong(((Map)objs).get("voucher_number").toString()));//处理超过8位
				String formName = (String) ((Map) objs).get("voucher_name");
				String checkFields = (String) ((Map) objs).get("check_fields");
				String fieldElseName = "";
				formInfoDetail.setVoucherName(formName);
				
				//套勾、单勾
				if(((Map) objs).get("is_multi") != null){
					formInfoDetail.setIsMulti(Integer.parseInt(((Map) objs).get("is_multi").toString()) );
					
				}
				//补录字段
				if(checkFields != null){
					String[] checkFieldArr = checkFields.split(";");
					for (String checkField : checkFieldArr) {
						fieldElseName += ";" + flowFieldMap.get(checkField);
					}
					formInfoDetail.setCheckFields(checkFields);
					
				}
				if(fieldElseName.indexOf(";") > -1){
					formInfoDetail.setElseName(fieldElseName.substring(1));
				}
				//从附件map中获取
				if(attachMap.get(formName) != null){
					formInfoDetail.setAttachmentFlag(0);
				}
				if(null != formNameMap.get(formName)){//只有配置的版面才能显示
					allFormInfo.add(formInfoDetail);
				}
				
			}
			retMap.put("formArr", allFormInfo.toArray());
			retMap.put("maFlag", "querySuccess");
			retMap.put("maMsg", "查询成功！");
		}
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(SunIFErrorMessage.SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}

	/**
	 * 获取批次方法
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void queryBatch(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		User user = BaseUtil.getLoginUser();
		//获取用户区域号
		OrganData organData = organDataMapper.selectByPrimaryKey(user.getOrganNo());
		
		Map retMap = new HashMap();
		String batchId = null;//批次号
		long flowId = 0;//流程实例ID
		long workId = 0;//工作项ID
		ArrayList reqList = (ArrayList) requestBean.getParameterList();
		//查询是否存在该用户获取批次未释放
		List<WorkItemInfo> workItemList = sunFlowService.getWorkItemByUser(BaseUtil.getLoginUser().getUserNo(),"ma",String.valueOf(organData.getServiceId()),100);
		if(null != workItemList && workItemList.size() > 0){//如果该用户有未释放的批次，优先获取处理
			batchId = workItemList.get(0).getWorkItemAttris().get("batchId");
			workId = workItemList.get(0).getWorkItemId();
			flowId = workItemList.get(0).getProcinstId();			
		}else{
			WorkItemInfo processWork = sunFlowService.applyWork(BaseUtil.getLoginUser().getUserNo(),String.valueOf(organData.getServiceId()), "ma");
			if(null != processWork){
				batchId = processWork.getWorkItemAttris().get("batchId");//通过工作流获取到批次号
				workId = processWork.getWorkItemId();
				flowId = processWork.getProcinstId();	
			}
		}
		
//		batchId = "20190516161935603935";
		if(batchId != null){
			TmpBatch tmpBatch = tmpBatchMapper.selectByPrimaryKey(batchId);
			if(tmpBatch!=null){ //30 tmpBatch.getProgressFlag().equals(ARSConstants.PROCESS_FLAG_30)
				HashMap condMap = new HashMap();
				condMap.put("batchId", batchId);
				List<TmpData> tmpDataList = tmpDataMapper.selectByBatchId(condMap);
				retMap.put("tmpBatch", tmpBatch);
				retMap.put("tmpDataList", tmpDataList);
				retMap.put("procinstId", flowId);
				retMap.put("workId", workId);
				responseBean.setRetMap(retMap);
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
				
				//添加日志
				String log = "用户获取批次任务，batchId："+batchId;
				addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_4, log);
			}else{
				responseBean.setRetMsg("未查询到待处理的批次任务!");
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			}
		}else{
			responseBean.setRetMsg("未查询到待处理的批次任务!");
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
		}
	}
	
	
	/**
	 * 获取批次方法
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void chooseBatch(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		User user = BaseUtil.getLoginUser();
		//获取用户区域号
		OrganData organData = organDataMapper.selectByPrimaryKey(user.getOrganNo());
		
		Map retMap = new HashMap();
		String batchId = null;//批次号
		long flowId = 0;//流程实例ID
		long workId = 0;//工作项ID
		Map sysMap = requestBean.getSysMap();
		batchId = sysMap.get("batchId").toString();
		TmpBatch selectByPrimaryKey = tmpBatchMapper.selectByPrimaryKey(batchId);
		Long procinstId = selectByPrimaryKey.getProcinstId();
		//查询是否存在该用户获取批次未释放
		//userId wkiName  wkiPriid wkiUser  wkiState
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("userId", BaseUtil.getLoginUser().getUserNo());
		hashMap.put("wkiName", "ma");
		hashMap.put("wkiPriid", String.valueOf(procinstId));
		hashMap.put("wkiState", "2");
		List<WorkItemInfo> workItemList = sunFlowService.getWorkItemByCondition(hashMap, String.valueOf(organData.getServiceId()), 5);
		if(null != workItemList && workItemList.size() > 0){//如果该用户有未释放的批次，优先获取处理
			batchId = workItemList.get(0).getWorkItemAttris().get("batchId");
			workId = workItemList.get(0).getWorkItemId();
			flowId = workItemList.get(0).getProcinstId();
			sunFlowService.applyWorkItem( BaseUtil.getLoginUser().getUserNo(),workId );
		}else{
			hashMap.put("wkiState", "4");
			hashMap.put("wkiUser", BaseUtil.getLoginUser().getUserNo());
			List<WorkItemInfo> workItemList2 = sunFlowService.getWorkItemByCondition(hashMap, String.valueOf(organData.getServiceId()), 5);
			if(null != workItemList2 && workItemList2.size() > 0){
				batchId = workItemList2.get(0).getWorkItemAttris().get("batchId");
				workId = workItemList2.get(0).getWorkItemId();
				flowId = workItemList2.get(0).getProcinstId();
			}else{
			 batchId = null;
			}
		}
		System.out.println("batchId"+batchId);
		if(batchId != null){
			TmpBatch tmpBatch = tmpBatchMapper.selectByPrimaryKey(batchId);
			if(tmpBatch!=null){ //30 tmpBatch.getProgressFlag().equals(ARSConstants.PROCESS_FLAG_30)
				HashMap condMap = new HashMap();
				condMap.put("batchId", batchId);
				List<TmpData> tmpDataList = tmpDataMapper.selectByBatchId(condMap);
				retMap.put("tmpBatch", tmpBatch);
				retMap.put("tmpDataList", tmpDataList);
				retMap.put("procinstId", flowId);
				retMap.put("workId", workId);
				responseBean.setRetMap(retMap);
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);

				//添加日志
				String log = "用户获取批次任务，batchId："+batchId;
				addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_4, log);
			}else{
				responseBean.setRetMsg("未查询到待处理的批次任务!");
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			}
		}else{
			responseBean.setRetMsg("该批次正在处理中!");
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, String> getFormNameMap(String organ_no) throws Exception{
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<>();
		HashMap condMap = new HashMap<String, String>();
		condMap.put("organ_no", organ_no);
		List voucheList = BaseUtil.convertListMapKeyValue(voucherInfoMapper.selectFormNameMap(condMap));
		for (int i = 0;i < voucheList.size();i++) {
			Map  voucheMap = (Map) voucheList.get(i);
			map.put((String) voucheMap.get("voucher_name"), "1");
		}
		return map;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryImageFlowCount(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		ArrayList reqList = (ArrayList) requestBean.getParameterList();
		MaParameterBean maParameterBean =  (MaParameterBean) reqList.get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("selfDelete", "0");
		condMap.put("checkFlag", "-1");
		condMap.put("batchId", maParameterBean.getBatchId());
		//查询未处理图像数
		int imageToDeal = tmpDataMapper.selectImgCount(condMap);
		
		condMap.clear();
		condMap.put("occurDate", maParameterBean.getOccurDate());
		condMap.put("siteNo", maParameterBean.getSiteNo());
		condMap.put("operatorNo", maParameterBean.getOperatorNo());
		int flowToDeal = flowMapper.selectFlowToDeal(condMap);
		
		retMap.put("imageToDeal",imageToDeal);
		retMap.put("flowToDeal",flowToDeal);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
	}
	
	@SuppressWarnings("rawtypes")
	private void freeMyFlow(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		//获取用户区域号
		OrganData organData = organDataMapper.selectByPrimaryKey(BaseUtil.getLoginUser().getOrganNo());
		//userId wkiName  wkiPriid wkiUser   wkiState
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("userId", BaseUtil.getLoginUser().getUserNo());
		hashMap.put("wkiUser", BaseUtil.getLoginUser().getUserNo());
		hashMap.put("wkiState", "5");
		//获取挂起后未放下的批次
		List<WorkItemInfo> workItemByCondition = sunFlowService.getWorkItemByCondition(hashMap, String.valueOf(organData.getServiceId()), 100);
		if(workItemByCondition!=null&&workItemByCondition.size()>0){
			for (WorkItemInfo workItemInfo : workItemByCondition) {
				long workItemId = workItemInfo.getWorkItemId();
				boolean resumeWorkItem = sunFlowService.resumeWorkItem(BaseUtil.getLoginUser().getUserNo(),workItemId );
			}
		}
		hashMap.put("wkiState", "4");
		//获取未释放的批次
		List<WorkItemInfo> workItemList = sunFlowService.getWorkItemByCondition(hashMap, String.valueOf(organData.getServiceId()), 100);
		if(workItemList!=null&&workItemList.size()>0){
			long workItemId = workItemList.get(0).getWorkItemId();
			sunFlowService.release(BaseUtil.getLoginUser().getUserNo(),workItemId);
		}
		
	}
	
	/**
	 * 根据批次号查询信息，如果有流水号，查询关联流水信息
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	private void queryByBatchFlow(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		String batchId = (String)sysMap.get("batchId");
		String flowId = (String)sysMap.get("flowId");
		TmpBatch batchInfo = tmpBatchMapper.selectByPrimaryKey(batchId);
		Map retMap = new HashMap();
		retMap.put("batchInfo", batchInfo);
		if(!BaseUtil.isBlank(flowId)) {
			HashMap<String, Object> condMap = new HashMap();
			condMap.put("occurDate",batchInfo.getOccurDate());
			condMap.put("siteNo", batchInfo.getSiteNo());
			condMap.put("operatorNo", batchInfo.getOperatorNo());
			condMap.put("flowId", flowId);
			condMap.put("flowQueryFields", "*");
			condMap.put("flowTb", "fl_flow_tb");
			//获取流水字段结果值
			List queryResults = flowMapper.getFlowInfoList(condMap);
			if(queryResults != null && queryResults.size()>0) {
				retMap.put("flowInfo", queryResults.get(0));
			}
		}
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		
	}
}
