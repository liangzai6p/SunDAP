
package com.sunyard.ars.file.service.impl.sm;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
import com.sunyard.ars.common.pojo.TmpBatch;
import com.sunyard.ars.file.dao.oa.CheckOffMapper;
import com.sunyard.ars.file.dao.scan.VoucherChkMapper;
import com.sunyard.ars.file.pojo.oa.CheckOff;
import com.sunyard.ars.file.service.sm.IBatchMonitorService;
import com.sunyard.ars.flows.service.api.SunFlowService;
import com.sunyard.ars.flows.service.api.WorkItemInfo;
import com.sunyard.ars.flows.service.impl.api.SunFlowServiceImpl;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.sunflow.engine.context.ProcessInstanceContext;




@Service("batchMonitorService")
@Transactional
public class BatchMonitorServiceImpl extends BaseService implements IBatchMonitorService{
	@Resource
	private TmpBatchMapper tmpBatchMapper;
	
	@Resource
	private TmpDataMapper tmpDataMapper;
	
	@Resource
	private CheckOffMapper checkOffMapper;
	
	@Resource
	private VoucherChkMapper voucherChkMapper;
	
	@Resource
	private FlowMapper flowMapper;
	
	private SunFlowService sunFlowService = new SunFlowServiceImpl();
	
	

	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		// TODO Auto-generated method stub
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}
	
	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		// 获取操作标识
		String oper_type = (String) sysMap.get("oper_type");
		if(ARSConstants.OPERATE_QUERY.equals(oper_type)){
			//批次查询
			query(requestBean, responseBean);
		}else if(ARSConstants.OPERATE_ADD.equals(oper_type)){
			
			
		}else if(ARSConstants.OPERATE_MODIFY.equals(oper_type)){
			//批次修改
			modify(requestBean, responseBean);
		}else if(ARSConstants.OPERATE_DELETE.equals(oper_type)){
			//批次删除
		}else if("queryWrokInfo".equals(oper_type)){
			//查询工作流信息
			queryWorkInfo(requestBean, responseBean);
		}else if ("addApplys".equals(oper_type)) {
			//增加参与者
			addApplys(requestBean, responseBean);
		}else if("release".equals(oper_type)){
			//工作流释放认领
			release(requestBean, responseBean);
		}else if("batchReset".equals(oper_type)){
			batchReset(requestBean, responseBean);
		}
	}
	
	
    @SuppressWarnings("unchecked")
	private void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		Map sysMap = requestBean.getSysMap();
		TmpBatch batch = (TmpBatch) requestBean.getParameterList().get(0);
		HashMap<String, Object> codeMap = new HashMap<String, Object>();
		codeMap.put("batch", batch);
		codeMap.put("userNo", BaseUtil.getLoginUser().getUserNo());
		codeMap.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
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
		// 定义分页操作
		Page page = PageHelper.startPage(currentPage, pageSize);
		List<TmpBatch> list = tmpBatchMapper.selectBySelective(codeMap);
		
		// 获取总记录数
		long totalCount = page.getTotal();
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("currentPage", currentPage);
		retMap.put("pageNum", pageSize);
		retMap.put("totalNum", totalCount);
		retMap.put("returnList", list);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
    }
    
    /**
     * 批次修改
     * @param requestBean
     * @param responseBean
     */
    private void modify(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		Map sysMap = requestBean.getSysMap();
		TmpBatch batch = (TmpBatch) requestBean.getParameterList().get(0);
		String freeFlow = (String)sysMap.get("freeFlow");
		
		
		//1.判断批次是否已轧账，归档
		CheckOff checkOff= checkOffMapper.selectByPrimaryKey(batch.getOccurDate(), batch.getSiteNo(), batch.getOperatorNo());
		
		if(checkOff !=null && checkOff.getAllCheckoff()!=null && checkOff.getAllCheckoff().equals("1")){
			//批次已轧账
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("批次"+batch.getBatchId()+"已轧账，请解除轧账后重试");
		}else if(checkOff !=null && checkOff.getAllCheckoff()!=null && checkOff.getAllCheckoff().equals(batch.getAreaCode())){
			//批次已归档
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("批次"+batch.getBatchId()+"已归档，无法处理");
			
		}else{
			boolean resetFlag = true;//是否处理工作流
			//1.查询旧批次信息
			TmpBatch oldBatch = tmpBatchMapper.selectByPrimaryKey(batch.getBatchId());
			
			//2.判断是否已进入工作流,及批次是否有效
			if(batch.getProcinstId()==0 ||ARSConstants.BATCH_IS_INVALID.equals(batch.getIsInvalid()) || ARSConstants.BATCH_IS_INVALID.equals(oldBatch.getIsInvalid())){
				//判断是否已经生成流程实例
				if(batch.getProcinstId()!=0){
					//直接结束流程
					try{
						logger.info("批次"+batch.getBatchId()+"修改,结束流程："+batch.getProcinstId());
						ProcessInstanceContext processIns = sunFlowService.getProIns(ARSConstants.SUNFLOW_USER, batch.getProcinstId());
						if(4!=processIns.getInstanceState() && 5!=processIns.getInstanceState()){
							resetWorkItem(batch,true);
						}
					}catch(Exception e){
						logger.error("批次修改时,工作流终止发生异常",e);
						responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
						responseBean.setRetMsg("批次"+batch.getBatchId()+"修改时，工作流终止发生异常，请联系管理员");
					}
				}
				resetFlag = false;
				//批次无效改成有效时，曾进过工作流批次，可重新处理工作流
				if(ARSConstants.BATCH_IS_VALID.equals(batch.getIsInvalid()) && ARSConstants.BATCH_IS_INVALID.equals(oldBatch.getIsInvalid()) && batch.getProcinstId()!=0){
					resetFlag = true;
				}
			}
			 
			//3.处理工作流
			if(resetFlag){
				try{
					logger.info("批次"+batch.getBatchId()+"修改,开始执行工作流处理："+batch.getProcinstId());
					resetSunflow(batch);
				}catch(Exception e){
					logger.error("批次修改时,工作流处理发生异常",e);
					responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
					responseBean.setRetMsg("批次"+batch.getBatchId()+"修改时，工作流处理发生异常，请联系管理员");
					return;
				}

			}
			if(ARSConstants.PROCESS_FLAG_10.equals(batch.getProgressFlag()) && !ARSConstants.PROCESS_FLAG_10.equals(oldBatch.getProgressFlag())){
				//批次流程被修改为自动识别状态时，需重置ocr_factor_flag
				batch.setOcrFactorFlag("0");
			}
			
			//4.更新批次信息
			  tmpBatchMapper.updateByPrimaryKeySelective(batch);
			  logger.info("批次"+batch.getBatchId()+"修改批次信息成功");
			  			  
			//5.修改批次登记表  
			  HashMap<String, Object> condMap = new HashMap<String, Object>();
			  condMap.put("ouucrDate", batch.getOccurDate());
			  condMap.put("siteNo", batch.getSiteNo());
			  condMap.put("operatorNo", batch.getOperatorNo());
			  condMap.put("businessId", batch.getBusinessId());
			  condMap.put("newIsInvalid", batch.getIsInvalid());
			  condMap.put("batchId", batch.getBatchId());
			  voucherChkMapper.updateByBatchId(condMap);
			  logger.info("批次"+batch.getBatchId()+"修改登记信息成功");
			  
			//6.释放流水
			  if("1".equals(freeFlow)||ARSConstants.BATCH_IS_INVALID.equals(batch.getIsInvalid())){

				  //6.1执行释放流水
				  HashMap< String, Object> flowMap = new HashMap<String, Object>();
				  flowMap.put("batchId", BaseUtil.filterSqlParam(batch.getBatchId()));
				  flowMap.put("occurDate", BaseUtil.filterSqlParam(oldBatch.getOccurDate()));
				  flowMap.put("siteNo", BaseUtil.filterSqlParam(oldBatch.getSiteNo()));
				  flowMap.put("operatorNo", BaseUtil.filterSqlParam(oldBatch.getOperatorNo()));
				  flowMapper.updateFreeAllFlow(flowMap);
				  logger.info("批次"+batch.getBatchId()+"释放流水信息成功");

				  //6.2执行释放图像
				  HashMap<String, Object> dataMap = new HashMap<String, Object>();
				  dataMap.put("ocrWorker", "");
				  dataMap.put("ocrDate", "");
				  dataMap.put("ocrTime", "");
//				  dataMap.put("newFormName", "");
//				  dataMap.put("newFormId", "");
				  dataMap.put("newFormGroup", "");
				  dataMap.put("newRecDate", "");
				  dataMap.put("newProcessState", "000000");
				  dataMap.put("newPrimaryInccodein", 0);
				  dataMap.put("newPsLevel", "1");
				  dataMap.put("batchId", batch.getBatchId());
				  tmpDataMapper.updateAllTmpData(dataMap);
				  logger.info("批次"+batch.getBatchId()+"释放图像信息成功");

			  }
			  
			
			// 拼装返回信息
			Map retMap = new HashMap<String, Object>();
			retMap.put("batch", batch);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("更新批次成功");
			logger.info("批次"+batch.getBatchId()+"修改完成");
			//添加日志
			String log = "批次监控，修改" + batch.getBatchId() + "的批次";
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_3, log);
		}
		
    }
    
    
    /**
     * 查询工作流信息
     * @param requestBean
     * @param responseBean
     */
    private void queryWorkInfo(RequestBean requestBean, ResponseBean responseBean)throws Exception{
    	Map sysMap = requestBean.getSysMap();
		TmpBatch batch = (TmpBatch) requestBean.getParameterList().get(0);		
		Map retMap = new HashMap();
		
		try{
			ProcessInstanceContext processIns = sunFlowService.getProIns(ARSConstants.SUNFLOW_USER, batch.getProcinstId());
			if(4==processIns.getInstanceState() || 5==processIns.getInstanceState()){
				//流程已结束
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("流程已结束或被终止");

			}else{
				//查询工作项信息
				List<WorkItemInfo> workList = sunFlowService.getWorksByPriId(ARSConstants.SUNFLOW_USER,batch.getProcinstId(), batch.getAreaCode());
				if (workList != null && workList.size() > 0) {
					WorkItemInfo workItemInfo = workList.get(0);
					long workItemId = workItemInfo.getWorkItemId();
					retMap.put("wkiState",workItemInfo.getWorkItemState());
					retMap.put("wkiName", workItemInfo.getWorkItemAttris().get("flowFlag"));
					retMap.put("wkiUser", workItemInfo.getWorkItemUser());
					String [] applys = null;
					//获取工作项参与者
					if(ARSConstants.SUNFLOW_APPLY_WAITING == workItemInfo.getWorkItemState()){
						applys = sunFlowService.getApplyExecutor(workItemId,ARSConstants.SUNFLOW_USER);
					}
					retMap.put("applys",applys);
					responseBean.setRetMap(retMap);
					responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
					responseBean.setRetMsg("查询成功");

					//添加日志
					String log = "查看批次为" + batch.getBatchId() + "的工作流信息";
					addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_4, log);
				}

			}
		}catch(Exception e){
			logger.error("查询批次工作流信息发生异常",e);
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("查询批次工作流信息发生异常，请联系管理员");
		}

		
		
    }
    
    
    /**
     * 增加参与者
     * @param requestBean
     * @param responseBean
     */
    private void addApplys(RequestBean requestBean, ResponseBean responseBean)throws Exception{
    	boolean addFlag = false;
    	Map map = new HashMap();
		Map sysMap = requestBean.getSysMap();
		String batchId = (String)sysMap.get("batchId");
		int procinstId = (int)sysMap.get("procinstId");
		String areaCode = (String)sysMap.get("areaCode");
		String addUsers = (String)sysMap.get("addUsers");
		String [] addUser = addUsers.split(",");

		try{
			//1.查询工作项
			List<WorkItemInfo> workList = sunFlowService.getWorksByPriId(ARSConstants.SUNFLOW_USER,Long.valueOf(procinstId), areaCode);
			//2.增加参与者
			if (workList != null && workList.size() > 0) {
				WorkItemInfo workItemInfo = workList.get(0);
				sunFlowService.addApplyMan(ARSConstants.SUNFLOW_USER, workItemInfo.getWorkItemId(),Arrays.asList(addUser));
				addFlag = true;
			}
		}catch(Exception e){
			logger.error("批次"+batchId+"增加参与者发生异常",e);
		}
    	if(addFlag){
    		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
    		responseBean.setRetMsg("操作成功");
    		
    		//添加日志
			String log = batchId +"批次工作流添加获取用户" + addUsers;
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_1, log);
    	}else{
    		responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
    		responseBean.setRetMsg("批次"+batchId+"增加参与者出现异常,请检查工作流状态");
    	}
		
    }
    
    /**
     * 工作流释放认领
     * @param requestBean
     * @param responseBean
     * @throws Exception
     */
    private void release(RequestBean requestBean, ResponseBean responseBean)throws Exception{
    	boolean releaseFlag = false;
		Map sysMap = requestBean.getSysMap();
		String batchId = (String)sysMap.get("batchId");
		int procinstId = (int)sysMap.get("procinstId");
		String areaCode = (String)sysMap.get("areaCode");
		logger.info("执行工作流释放batchId:"+batchId+",procinstId："+procinstId);
		try{
			//1.查询工作项
			List<WorkItemInfo> workList = sunFlowService.getWorksByPriId(ARSConstants.SUNFLOW_USER,Long.valueOf(procinstId), areaCode);
			//2.释放任务
			if (workList != null && workList.size() > 0) {
				WorkItemInfo workItemInfo = workList.get(0);
				if(ARSConstants.SUNFLOW_RUNNING == workItemInfo.getWorkItemState()){
					sunFlowService.release(workItemInfo.getWorkItemUser(), workItemInfo.getWorkItemId());
					releaseFlag = true;
				}
			}
		}catch(Exception e){
			logger.error("工作流释放发生异常",e);
		}

    	if(releaseFlag){
    		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
    		responseBean.setRetMsg("认领释放操作成功");
    		
    		//添加日志
			String log = "批次工作流释放，批次号为" + batchId;
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_3, log);
    	}else{
    		responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
    		responseBean.setRetMsg("批次"+batchId+"认领释放操作出现异常，请检查工作流状态");
    	}
		
    }


	/**
	 * 批次重置
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	private void batchReset(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		Map sysMap = requestBean.getSysMap();
		String batchId = (String)sysMap.get("batchId");
		TmpBatch batch = tmpBatchMapper.selectByPrimaryKey(batchId);
		//1.判断批次是否已轧账，归档
		CheckOff checkOff= checkOffMapper.selectByPrimaryKey(BaseUtil.filterSqlParam(batch.getOccurDate()), BaseUtil.filterSqlParam(batch.getSiteNo()), BaseUtil.filterSqlParam(batch.getOperatorNo()));

		if(checkOff !=null && checkOff.getAllCheckoff()!=null && checkOff.getAllCheckoff().equals("1")){
			//批次已轧账
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("批次"+batch.getBatchId()+"已轧账，请解除轧账后重试");
		}else if(checkOff !=null && checkOff.getAllCheckoff()!=null && checkOff.getAllCheckoff().equals(batch.getAreaCode())){
			//批次已归档
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("批次"+batch.getBatchId()+"已归档，无法处理");

		}else{
			if(batch.getProcinstId()!=0){
				//直接结束流程
				try{
					logger.info("批次"+batch.getBatchId()+"将被重置，直接结束工作流流程："+batch.getProcinstId());
					ProcessInstanceContext processIns = sunFlowService.getProIns(ARSConstants.SUNFLOW_USER, batch.getProcinstId());
					if(4!=processIns.getInstanceState() && 5!=processIns.getInstanceState()){
						resetWorkItem(batch,true);
					}
				}catch(Exception e){
					logger.error("批次重置时,工作流终止发生异常,强制重置批次",e);

				}
			}

			//重置批次信息
			batch.setProgressFlag(ARSConstants.PROCESS_FLAG_10);
			batch.setIsInvalid(ARSConstants.BATCH_IS_VALID);
			batch.setOcrFactorFlag("0");
			batch.setProcinstId(0l);
			batch.setAreaCode("");
			batch.setWorkTime("");
			tmpBatchMapper.updateByPrimaryKeySelective(batch);
			logger.info("批次"+batch.getBatchId()+"重置批次信息成功");

			//重置流水信息
			HashMap< String, Object> flowMap = new HashMap<String, Object>();
			flowMap.put("batchId", BaseUtil.filterSqlParam(batch.getBatchId()));
			flowMap.put("occurDate", BaseUtil.filterSqlParam(batch.getOccurDate()));
			flowMap.put("siteNo", BaseUtil.filterSqlParam(batch.getSiteNo()));
			flowMap.put("operatorNo", BaseUtil.filterSqlParam(batch.getOperatorNo()));
			flowMapper.updateFreeAllFlow(flowMap);
			logger.info("批次"+batch.getBatchId()+"重置流水信息成功");

			//重置图像信息
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("ocrWorker", "");
			dataMap.put("ocrDate", "");
			dataMap.put("ocrTime", "");
//			dataMap.put("newFormName", "");
//			dataMap.put("newFormId", "");
			dataMap.put("newFormGroup", "");
			dataMap.put("newRecDate", "");
			dataMap.put("newProcessState", "000000");
			dataMap.put("newPrimaryInccodein", 0);
			dataMap.put("newPsLevel", "1");
			dataMap.put("batchId", BaseUtil.filterSqlParam(batch.getBatchId()));
			tmpDataMapper.updateAllTmpData(dataMap);
			logger.info("批次"+batch.getBatchId()+"重置图像信息成功，批次重置完成");


			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("批次"+batchId+"重置成功");

		}

	}
    
    
    /**
     * 重置工作流
     * @param batch
     */
    private void resetSunflow(TmpBatch batch)throws Exception{
    		//查询流程状态
    		ProcessInstanceContext processIns = sunFlowService.getProIns(ARSConstants.SUNFLOW_USER, batch.getProcinstId());
    		if(1==processIns.getInstanceState()){
    			//激活状态
    			    			
    		}else if(3==processIns.getInstanceState()){
    			//挂起
    			//重启流程
    			sunFlowService.resumeProcessInstance(ARSConstants.SUNFLOW_USER, batch.getProcinstId(), batch.getAreaCode());    			
    			
    		}else if(4==processIns.getInstanceState()){
    			//终止时流程无法重启，直接重新发起流程
    			if(!ARSConstants.PROCESS_FLAG_98.equals(batch.getProgressFlag())){
					logger.info("批次"+batch.getBatchId()+"流程被终止，重新发起流程");
    				//重新发起流程
    			    List arsWorkItemInfo = new ArrayList<>();
    			    WorkItemInfo wit = new WorkItemInfo();
					Map<String, String> workItemAttris = new HashMap<>();
					workItemAttris.put("batchId", batch.getBatchId());
					workItemAttris.put("occurDate", batch.getOccurDate());
					workItemAttris.put("siteNo", batch.getSiteNo());
					workItemAttris.put("operatorNo", batch.getOperatorNo());
					wit.setWorkItemAttris(workItemAttris);
					wit.setAreaCode(batch.getAreaCode());
					arsWorkItemInfo.add(wit);					
    			    Map<String,Long> flowIdMap = sunFlowService.createBatchFlow(ARSConstants.SUNFLOW_USER, "ARSBaseFlow", "后督工作流程", arsWorkItemInfo);
    			   
    			    //更新批次表ProcinstId
    			    batch.setProcinstId(flowIdMap.get(batch.getBatchId()));
    			    //重启流程后为识别状态,PROCESS_FLAG为10时，无需调整工作流
    			    if(ARSConstants.PROCESS_FLAG_10.equals(batch.getProgressFlag())){
    			    	return;
    			    }
    			    Thread.sleep(1000);
    			}    			
    		}else if (5==processIns.getInstanceState()) {
				logger.info("批次"+batch.getBatchId()+"流程结束，可重新激活流程");
    			//重新激活流程
    			sunFlowService.reActiveProcessInstance(ARSConstants.SUNFLOW_USER, batch.getProcinstId(),null);
				Thread.sleep(1000);
			}
    		
    		resetWorkItem(batch,false);
    }    
    
    
    /**
     * 重置工作项
     * @param batch
     */
    private void resetWorkItem(TmpBatch batch,boolean endFlag)throws Exception{
   	 //1.查询工作项
	    List<WorkItemInfo> workList = sunFlowService.getWorksByPriId(ARSConstants.SUNFLOW_USER,batch.getProcinstId(), batch.getAreaCode());
   	//2.跳转到指定节点
   	if (workList != null && workList.size() > 0) {
			WorkItemInfo workItemInfo = workList.get(0);
			
			long workItemId = workItemInfo.getWorkItemId();
			String workItemUser = workItemInfo.getWorkItemUser();			

			if(ARSConstants.SUNFLOW_APPLY_WAITING==workItemInfo.getWorkItemState()){
				//2-等待获取
				
			}else if(ARSConstants.SUNFLOW_APPLY == workItemInfo.getWorkItemState()){
				//3-申请中
				sunFlowService.release(workItemInfo.getWorkItemUser(),workItemId);
			}else if (ARSConstants.SUNFLOW_RUNNING == workItemInfo.getWorkItemState()) {
			    //4-运行
				//先释放
				logger.info("批次"+batch.getBatchId()+"已被获取，开始进行释放");
				sunFlowService.release(workItemInfo.getWorkItemUser(),workItemId);
				
			}else if(ARSConstants.SUNFLOW_SUSPEND == workItemInfo.getWorkItemState()){
				//5-挂起
				//解除挂起，再释放
				logger.info("批次"+batch.getBatchId()+"流程被挂起，解除挂起后释放");
				sunFlowService.resumeWorkItem(workItemUser,workItemId);
				sunFlowService.release(workItemUser,workItemId);
				
			}else if (ARSConstants.SUNFLOW_COMPLETE == workItemInfo.getWorkItemState() || ARSConstants.SUNFLOW_TERMINATED == workItemInfo.getWorkItemState()) {
				//已完成或者终止,正常流程不会出现，异常时发生，直接重新发起工作流
				logger.info("工作项已完成或者终止，将终止流程，并重新发起");

    			//终止时流程无法重启，直接重新发起流程
    			if(!ARSConstants.PROCESS_FLAG_98.equals(batch.getProgressFlag())){
    				//重新发起流程
    			    List arsWorkItemInfo = new ArrayList<>();
    			    WorkItemInfo wit = new WorkItemInfo();
					Map<String, String> workItemAttris = new HashMap<>();
					workItemAttris.put("batchId", batch.getBatchId());
					workItemAttris.put("occurDate", batch.getOccurDate());
					workItemAttris.put("siteNo", batch.getSiteNo());
					workItemAttris.put("operatorNo", batch.getOperatorNo());
					wit.setWorkItemAttris(workItemAttris);
					wit.setAreaCode(batch.getAreaCode());
					arsWorkItemInfo.add(wit);					
    			    Map<String,Long> flowIdMap = sunFlowService.createBatchFlow(ARSConstants.SUNFLOW_USER, "ARSBaseFlow", "后督工作流程", arsWorkItemInfo);
    			    //更新批次表ProcinstId
    			    batch.setProcinstId(flowIdMap.get(batch.getBatchId()));
    			    //重启流程后为识别状态,PROCESS_FLAG为10时，无需调整工作流
    			    if(ARSConstants.PROCESS_FLAG_10.equals(batch.getProgressFlag())){
    			    	return;
    			    }
    			    Thread.sleep(1000);
    			    workItemInfo = sunFlowService.getWorksByPriId(ARSConstants.SUNFLOW_USER,batch.getProcinstId(), batch.getAreaCode()).get(0);
    			}    			
				
			}	
			//给工作项增加sa用户
			List<String> addUsers = new ArrayList<String>();
			addUsers.add("sa");
			sunFlowService.addApplyMan(ARSConstants.SUNFLOW_USER, workItemInfo.getWorkItemId(),addUsers);
			
			//流程跳转
			if(endFlag){
				//直接跳转至结束流程
				logger.info("批次"+batch.getBatchId()+"流程跳转至结束");
				Map<String, String> workItemAttris = new HashMap<>();
				workItemAttris.put("flowFlag", ARSConstants.PROCESS_FLAG_98);
				workItemInfo.setWorkItemAttris(workItemAttris);				
				sunFlowService.checkInWorkItemAndJump(ARSConstants.SUNFLOW_USER, ARSConstants.SUNFLOW_WORKITEM_MAP.get(ARSConstants.PROCESS_FLAG_98), workItemInfo, batch.getAreaCode());
			}else{
				 //更新跳转流程
				if(!workItemInfo.getWorkItemAttris().get("flowFlag").equals(batch.getProgressFlag())){
					logger.info("批次"+batch.getBatchId()+"进行流程跳转："+ARSConstants.SUNFLOW_WORKITEM_MAP.get(batch.getProgressFlag()));
					//组装更新内容
					Map<String, String> workItemAttris = new HashMap<>();
					workItemAttris.put("flowFlag", batch.getProgressFlag());
					workItemInfo.setWorkItemAttris(workItemAttris);	
					if(workItemInfo.getWorkItemUser()!=null){
						sunFlowService.checkInWorkItemAndJump(workItemInfo.getWorkItemUser(), ARSConstants.SUNFLOW_WORKITEM_MAP.get(batch.getProgressFlag()), workItemInfo, batch.getAreaCode());
					}else{
						sunFlowService.checkInWorkItemAndJump(ARSConstants.SUNFLOW_USER, ARSConstants.SUNFLOW_WORKITEM_MAP.get(batch.getProgressFlag()), workItemInfo, batch.getAreaCode());
					}
					
					//若跳转值业务审核，则需增加默认可获取用户
					if(ARSConstants.PROCESS_FLAG_30.equals(batch.getProgressFlag())){
						logger.info("批次"+batch.getBatchId()+"跳转至业务审核，增加默认获取用户");
						addDefaultApplys(batch);
					}
					
				}
			}
   	 }
   
    }
    
    /**
     * 增加默认可获取用户
     * @param batch
     * @throws Exception
     */
    private void addDefaultApplys(TmpBatch batch)throws Exception{
    	Vector<String> usersVec = new Vector<String>();
    	HashMap condMap = new HashMap();
		condMap.put("batchId",BaseUtil.filterSqlParam(batch.getBatchId()));
		//condMap.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
		List<HashMap<String,Object>> userList = tmpBatchMapper.getFlowUserList(condMap);
		List<HashMap<String,Object>> userList2 = tmpBatchMapper.getFlowUserList2(condMap);
		if(null != userList && userList.size() > 0){
			for (HashMap<String, Object> userMap : userList) {
				usersVec.add(userMap.get("USER_NO")+"");
			}
		}
		if(null != userList2 && userList2.size() > 0){
			for (HashMap<String, Object> userMap2 : userList2) {
				usersVec.add(userMap2.get("USER_NO")+"");
			}
		}
		usersVec.add("sa");
		List<WorkItemInfo> workList = sunFlowService.getWorksByPriId(ARSConstants.SUNFLOW_USER,batch.getProcinstId(), batch.getAreaCode());
		if (workList != null && workList.size() > 0) {
    		WorkItemInfo workItemInfo = workList.get(0);
    		sunFlowService.addApplyMan(ARSConstants.SUNFLOW_USER, workItemInfo.getWorkItemId(),usersVec);
    	}
    }
	
}
