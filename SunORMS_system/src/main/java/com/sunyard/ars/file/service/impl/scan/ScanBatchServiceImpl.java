
package com.sunyard.ars.file.service.impl.scan;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.sunyard.ars.flows.service.api.SunFlowService;
import javax.annotation.Resource;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.comm.BaseService;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.SysCode;
import com.sunyard.ars.common.dao.FlowMapper;
import com.sunyard.ars.common.dao.TmpBatchMapper;
import com.sunyard.ars.common.dao.TmpDataMapper;

import com.sunyard.ars.common.pojo.TmpBatch;
import com.sunyard.ars.common.pojo.TmpData;
import com.sunyard.ars.common.util.DateUtil;
import com.sunyard.ars.common.util.ECMUtil;
import com.sunyard.ars.common.util.StringUtil;
import com.sunyard.ars.common.util.UUidUtil;
import com.sunyard.ars.file.dao.oa.CheckOffMapper;
import com.sunyard.ars.file.dao.scan.VoucherChkMapper;
import com.sunyard.ars.file.pojo.oa.CheckOff;
import com.sunyard.ars.file.pojo.scan.VoucherChk;
import com.sunyard.ars.file.service.scan.IScanBatchService;
import com.sunyard.ars.flows.service.api.WorkItemInfo;
import com.sunyard.ars.flows.service.impl.api.SunFlowServiceImpl;
import com.sunyard.ars.system.bean.busm.Teller;
import com.sunyard.ars.system.bean.sc.OrganData;
import com.sunyard.ars.system.bean.sc.SCDatasource;
import com.sunyard.ars.system.bean.sc.ServiceReg;

import com.sunyard.ars.system.dao.busm.TellerMapper;
import com.sunyard.ars.system.dao.busm.UserOrganMapper;
import com.sunyard.ars.system.dao.sc.OrganDataMapper;
import com.sunyard.ars.system.dao.sc.SCDatasourceMapper;
import com.sunyard.ars.system.dao.sc.ServiceRegMapper;
import com.sunyard.ars.system.dao.sc.SystemParameterMapper;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.dao.UserMapper;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;
import com.sunyard.cop.IF.mybatis.pojo.User;
import com.sunyard.sunflow.client.FlowClient;
import com.sunyard.sunflow.client.ISunflowClient;
import com.sunyard.sunflow.engine.workflowexception.SunflowException;



@Service("scanBatchService")
@Transactional
public class ScanBatchServiceImpl<V> extends BaseService implements IScanBatchService{
	@Resource
	private TmpBatchMapper tmpBatchMapper;
	
	@Resource
	private TmpDataMapper tmpDataMapper;
	
	@Resource
	private VoucherChkMapper voucherChkMapper;
	
	@Resource
	private CheckOffMapper checkOffMapper;
	
	@Resource
	private SystemParameterMapper systemParameterMapper; 
	
	private SunFlowService sunFlowService = new SunFlowServiceImpl();
	
	@Resource
	private SCDatasourceMapper scDatasourceMapper;
	
	@Resource
	private ServiceRegMapper serviceRegMapper ;
	
	@Resource
	private OrganDataMapper organDataMapper;
	@Resource
	private UserMapper userMapper;
	@Resource
	private TellerMapper tellerMapper;
	@Resource
	private UserOrganMapper userOrganMapper;

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
		
		if("getECMConfig".equals(oper_type)){
			//选择柜员后，查询机构信息
			getECMConfig(requestBean, responseBean);
		}else if(ARSConstants.OPERATE_ADD.equals(oper_type)){
			//批次提交
			submitBatch(requestBean, responseBean);
		}else if(ARSConstants.OPERATE_MODIFY.equals(oper_type)){
			//批次修改
			
		}else if(ARSConstants.OPERATE_DELETE.equals(oper_type)){
			//批次删除
		}else if("patchQuery".equals(oper_type)){
			//补扫列表查询
			
			patchQuery(requestBean, responseBean);
			
		}else if("patchBatch".equals(oper_type)){
			//开始批次补扫
			patchBatch(requestBean, responseBean);
			
		}else if("patchSubmit".equals(oper_type)){
			//补扫提交
			patchSubmit(requestBean, responseBean);
			
		}else if("patchImageQuery".equals(oper_type)){
			//补扫附件时，查询对应补扫主件
			patchImageQuery(requestBean, responseBean);
		}else if("patchInfoQuery".equals(oper_type)){
			//补扫信息查询
			patchInfoQuery(requestBean, responseBean);
		}else if("initScanParam".equals(oper_type)){
			initScanParam(requestBean, responseBean);
		}
		else if("resumeBatch".equals(oper_type)){
			resumeBatch(requestBean, responseBean);
		}else if("CHECKSITENOBYOPER".equals(oper_type)){
			//选择柜员后，查询机构信息
			checkSiteNoByOper(requestBean, responseBean);
		}else if("checkScanTellerInfo".equals(oper_type)){
			//选择柜员后，查询机构信息
			checkScanTellerInfo(requestBean, responseBean);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getECMConfig(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map retMap = new HashMap();
		//TODO  目前都一样 如果分行分开的话肯定要加表
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		String userNo = (String) sysMap.get("userNo");
		String siteNo = userMapper.selectByUserNo(userNo).getOrganNo();
		// 获取操作标识
		if(siteNo==null||siteNo.equals("")){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("机构为空，无法查询ECM配置信息");
			return;
		}
		OrganData organData = new OrganData();
		organData.setOrganNo(BaseUtil.filterSqlParam(siteNo));
		List<OrganData> selectBySelective = organDataMapper.selectBySelective(organData);
		if(selectBySelective==null||selectBySelective.size()==0){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("请配置机构数据表未配置");
			return;
		}
		String ecmserviceId = selectBySelective.get(0).getEcmserviceId();
		if(ecmserviceId==null||ecmserviceId.equals("")){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("机构未配置ECM信息");
			return;
		}
		SCDatasource source = scDatasourceMapper.selectByPrimaryKey(BaseUtil.filterSqlParam(ecmserviceId));
		ServiceReg serviceReg = serviceRegMapper.selectByPrimaryKey(new BigDecimal(BaseUtil.filterSqlParam(source.getServiceId())));
		String groupName = source.getGroupName();//servergroup
		String modeCode = source.getModeCode();//HD
		String filePartName = source.getFilePartName();//HD_PART
		String serviceIp = serviceReg.getServiceIp();//192.168.199.139
		String servicePort = serviceReg.getServicePort();//8021
		String loginName = serviceReg.getLoginName();//admin
		String loginPass = serviceReg.getLoginPass();//111
		retMap.put("ecm_user", loginName);	
		retMap.put("ecm_pwd", loginPass);
		retMap.put("ecm_ip", serviceIp);
		retMap.put("ecm_port", servicePort);
		retMap.put("objname", modeCode);
		retMap.put("inxname", filePartName);
		retMap.put("dsname", groupName);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void checkSiteNoByOper(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		//获取用户信息
		User user = BaseUtil.getLoginUser();
		String operatorNo = ((TmpBatch) requestBean.getParameterList().get(0)).getOperatorNo();
		HashMap<String, Object> tmphm = new HashMap<String, Object>();
		tmphm.put("operatorNo", operatorNo);
		tmphm.put("userNo", user.getUserNo());
		tmphm.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
		List<HashMap<String, Object>> list = checkOffMapper.getSiteNoByOper(tmphm);
		String tmpOrgan = "";
		String tmpOrganName = "";
		String tmpTeller = "";
		String tmpTellerName = "";
		if(list!=null && list.size()>0){
			HashMap<String, Object> map = list.get(0);
				tmpTellerName=(String) map.get("NAME");
			 tmpOrgan = (String) map.get("INFOS");
			 tmpOrganName = (String) map.get("ORGANNAME");
		}
		
	
		// 拼装返回信息
		Map retMap = new HashMap();		
		retMap.put("tellerNo", tmpTellerName);	
		retMap.put("organNo", tmpOrgan);
		retMap.put("organName", tmpOrganName);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
	}
	private void checkScanTellerInfo(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map retMap = new HashMap();
		Map sysMap = requestBean.getSysMap();
		String siteNo = (String) sysMap.get("siteNo");
		String operatorNo = (String) sysMap.get("operatorNo");
		Boolean object = (Boolean)sysMap.get("cascadeFlag");
		Teller selectTellerInfo = tellerMapper.selectTellerInfo(operatorNo);
		if(selectTellerInfo==null){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("柜员信息错误，无法提交");
			return;
		}
		String parentOrgan = selectTellerInfo.getParentOrgan();
		if(object){
			if(!parentOrgan.equals(siteNo)){
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("柜员信息错误，无法提交");
				return;
			}
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else{
			List<String> privOrganByUserNo = userOrganMapper.getPrivOrganByUserNo(BaseUtil.getLoginUser().getUserNo(),(String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));
			if(!privOrganByUserNo.contains(siteNo)){
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("柜员信息错误，无法提交");
				return;
			}
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void resumeBatch(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		// 拼装返回信息
		Map retMap = new HashMap();
		
		TmpBatch occurBatch = JSON.parseObject((String)sysMap.get("occurBatch"),TmpBatch.class);
		
		TmpBatch batch = tmpBatchMapper.selectByPrimaryKey(occurBatch.getBatchId());
		if(batch == null){
			return;
		}
		Long procinstId = batch.getProcinstId();
		//拿5的状态，同一个流程ID下应该只会有一个
		//userId wkiName  wkiPriid wkiUser   wkiState
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("userId", BaseUtil.getLoginUser().getUserNo());
				hashMap.put("wkiPriid", String.valueOf(procinstId));
				hashMap.put("wkiState", "5");
		List<WorkItemInfo> worksByPriIdForce = sunFlowService.getWorkItemByCondition(hashMap, "2", 5);
		if(worksByPriIdForce!=null && worksByPriIdForce.size()>0) {
			WorkItemInfo workItemInfo = worksByPriIdForce.get(0);
			long workItemId =workItemInfo.getWorkItemId();
			//挂起前进行了申请  所以解挂后状态应该是4
			boolean resumeWorkItem = sunFlowService.resumeWorkItem(BaseUtil.getLoginUser().getUserNo(),workItemId );
			hashMap.put("wkiState", "4");
			List<WorkItemInfo> worksByPriIdForce2 = sunFlowService.getWorkItemByCondition(hashMap, "2", 5);
			if(worksByPriIdForce2!=null && worksByPriIdForce2.size()>0){
				boolean release = sunFlowService.release(BaseUtil.getLoginUser().getUserNo(), workItemId);
			}
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void submitBatch(RequestBean requestBean, ResponseBean responseBean) throws Exception{
        User user = BaseUtil.getLoginUser();
    	// 获取参数集合
		Map sysMap = requestBean.getSysMap();

		// 组织批次信息
		
		TmpBatch tmpbatch  = (TmpBatch) requestBean.getParameterList().get(0);
		
		tmpbatch.setBatchCommit(ARSConstants.BATCH_COMMIT);
		if (ARSConstants.STRING_TYPE_0.equals(ARSConstants.SYSTEM_PARAMETER.get("SCAN_ISSYN_TYPE").getParamValue())) { // 异步
			tmpbatch.setIsInvalid(ARSConstants.BATCH_IS_INVALID);// 无效
		} else {// 同步
			tmpbatch.setIsInvalid(ARSConstants.BATCH_IS_VALID);// 有效
			//写入contentId
			 String siteNo = tmpbatch.getSiteNo();
			 OrganData organData = new OrganData();
				organData.setOrganNo(siteNo);
				List<OrganData> organDataList = organDataMapper.selectBySelective(organData);
				if(organDataList==null||organDataList.size()==0){
					logger.info("查询机构数据表信息异常，批次："+tmpbatch.getBatchId()+"回写content_id失败");
					responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
					responseBean.setRetMsg("查询机构数据表信息异常，批次："+tmpbatch.getBatchId()+"回写content_id失败");
					return;
				}
				String ecmserviceId = organDataList.get(0).getEcmserviceId();
				if(ecmserviceId==null||ecmserviceId.equals("")){
					logger.info("查询ecm信息异常，批次："+tmpbatch.getBatchId()+"回写content_id失败");
					responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
					responseBean.setRetMsg("查询ecm信息异常，批次："+tmpbatch.getBatchId()+"回写content_id失败");
					return;
				}
			SCDatasource source = scDatasourceMapper.selectByPrimaryKey(BaseUtil.filterSqlParam(ecmserviceId));
			ServiceReg serviceReg = serviceRegMapper.selectByPrimaryKey(new BigDecimal(BaseUtil.filterSqlParam(source.getServiceId())));
			ECMUtil ecmUtil  = new ECMUtil(serviceReg.getServiceIp(),Integer.parseInt(serviceReg.getServicePort()),serviceReg.getLoginName(),serviceReg.getLoginPass());
			ecmUtil.set(source.getGroupName(), source.getModeCode(),source.getFilePartName(), source.getIndexName());
			//查询contentId
			String batchXml = ecmUtil.heightQuery(tmpbatch.getBatchId(), tmpbatch.getInputDate());						
			String contentId = ecmUtil.parseHeightQueryXML(batchXml);
			tmpbatch.setContentId(contentId);
		}

//			tmpbatch.setBatchLock(ARSConstants.BATCH_LOCK_FLAG);
		tmpbatch.setImageStatus(ARSConstants.IMAGE_STATUS);
		tmpbatch.setWorker("");
		tmpbatch.setWorkTime("");
		String isNeedProcess = tmpbatch.getNeedProcess();
		
		if (ARSConstants.STRING_TYPE_0.equals(isNeedProcess)) {// 不需要处理的批次直接为98进行轧帐归档
			tmpbatch.setProgressFlag(ARSConstants.PROCESS_FLAG_98);
		} else {
			tmpbatch.setProgressFlag(ARSConstants.PROCESS_FLAG_10); 
		}
		tmpbatch.setInputWorker(user.getUserNo());
		tmpbatch.setMachineId(BaseUtil.filterSqlParam(StringUtil.getIP(RequestUtil.getRequest())));
//		tmpbatch.setInputDate(dateString[0]);
		tmpbatch.setInputTime(DateUtil.getYMD()+"-"+DateUtil.getHMS2());
		
		
		// 组织图像信息
		List<TmpData> tmpDataList = JSON.parseArray((String)sysMap.get("imageList"),TmpData.class);

		for (int i = 0; i < tmpDataList.size(); i++) {
			tmpDataList.get(i).setInccodeinBatch(i + 1);
			tmpDataList.get(i).setBatchId(tmpbatch.getBatchId());
			tmpDataList.get(i).setPsLevel(ARSConstants.PS_LEVEL);
			tmpDataList.get(i).setCheckFlag(ARSConstants.CHECK_FLAG);
			tmpDataList.get(i).setErrorFlag(ARSConstants.ERROR_FLAG);
			tmpDataList.get(i).setIsAudit(ARSConstants.IS_AUDIT);
			tmpDataList.get(i).setSelfDelete(ARSConstants.SELF_DELETE);
			tmpDataList.get(i).setProcessState(ARSConstants.PROCESS_STATE);
			tmpDataList.get(i).setRecFailCause(ARSConstants.REC_FAIL_CAUSE);
			tmpDataList.get(i).setPrimaryInccodein(ARSConstants.PRIMARY_INCCODEIN);
			tmpDataList.get(i).setIsFrontPage(ARSConstants.IS_FRONT_PAGE_NOBACK);
			tmpDataList.get(i).setCopyInccodein(ARSConstants.COPY_INCCODEIN);
			tmpDataList.get(i).setCopyRec(ARSConstants.COPY_REC);
			
			tmpDataList.get(i).setSerialNo(UUidUtil.uuid());
			tmpDataList.get(i).setMemo("");
			tmpDataList.get(i).setRecDate("");
			tmpDataList.get(i).setFlowId("");
			tmpDataList.get(i).setFormName("");
			tmpDataList.get(i).setFormGroup("");
			tmpDataList.get(i).setVouhType("");
			tmpDataList.get(i).setPatchFlag(0);
			//如果单面，背面保存正面图像名
			if(null == tmpDataList.get(i).getBackFileName() || "null".equalsIgnoreCase(tmpDataList.get(i).getBackFileName())){
				tmpDataList.get(i).setBackFileName(tmpDataList.get(i).getFileName());
			}
		}
		// 组织批次登记表信息
		VoucherChk voucherChk = JSON.parseObject((String)sysMap.get("occurVoucher"), VoucherChk.class);
		voucherChk.setScanVoucherNum(String.valueOf(tmpDataList.size()));
		voucherChk.setBatchCommit(ARSConstants.BATCH_COMMIT);
		
		//TODO
		//设置批次表中批次数据表表名，暂时写死BP_TMPDATA_1_TB
		tmpbatch.setTempDataTable(ARSConstants.TMP_DATA_TB); 
		
		
		tmpBatchMapper.insertSelective(tmpbatch);
		for(int j=0;j<tmpDataList.size();j++){
			tmpDataMapper.insertSelective(tmpDataList.get(j),ARSConstants.TMP_DATA_TB);
		}
		voucherChkMapper.updateByPrimaryKeySelective(voucherChk);
		
		if(checkOffMapper.selectByPrimaryKey(tmpbatch.getOccurDate(), tmpbatch.getSiteNo(), tmpbatch.getOperatorNo())==null){
			if(tmpbatch.getOperatorNo().length()<6){//过滤虚拟柜员
			CheckOff record = new CheckOff();
			record.setOccurDate(tmpbatch.getOccurDate());
			record.setSiteNo(tmpbatch.getSiteNo());
			record.setOperatorNo(tmpbatch.getOperatorNo());
			checkOffMapper.insertSelective(record);
		}
		}
		responseBean.setRetCode(String.valueOf(SysCode.SYSCODE_888888));
		responseBean.setRetMsg( SysCode.sysCodeMap.get(SysCode.SYSCODE_888888));
		
		// 添加批次日志信息
		addLogInfo("批次提交,{批次号:"+tmpbatch.getBatchId()+",交易日期："+tmpbatch.getOccurDate()+",机构："+tmpbatch.getSiteNo()+",柜员:"+tmpbatch.getOperatorNo()+"}");
		//添加批次登记信息
		addLogInfo("批次登记表更新,{凭证数："+tmpDataList.size()+",批次状态："+ARSConstants.BATCH_COMMIT+"}");
	
	}
	
	/**
	 * 查询补扫批次
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void patchQuery(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		// 查询方式  0-初次打开补扫列表查询扫描员当日扫描批次   1-按条件查询权限范围内的批次
		String queryType = (String) sysMap.get("queryType");
		String occurDate = (String) sysMap.get("occurDate");
		String siteNo = (String) sysMap.get("siteNo");
		String operatorNo = (String) sysMap.get("operatorNo");
		
		// 拼装返回信息
		Map retMap = new HashMap();
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		
		condMap.put("queryType", queryType);
		condMap.put("occurDate", occurDate);
		condMap.put("siteNo", siteNo);
		condMap.put("operatorNo", operatorNo);
		
		condMap.put("userNo", BaseUtil.getLoginUser().getUserNo());
		condMap.put("inputDate", DateUtil.getYMD());
		condMap.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
		//查询出补扫列表
		List<HashMap<String,Object>> patchList = tmpBatchMapper.getPatchTaskList(condMap);
		retMap.put("patchList", patchList);
		
		responseBean.setRetMap(retMap);
	
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 开始补扫批次，把批次从中心应用释放
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void patchBatch(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		// 拼装返回信息
		Map retMap = new HashMap();
		
		TmpBatch occurBatch = JSON.parseObject((String)sysMap.get("occurBatch"),TmpBatch.class);
		
		TmpBatch batch = tmpBatchMapper.selectByPrimaryKey(occurBatch.getBatchId());
		if(batch == null){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("未找到该批次，请联系管理员");
			return;
		}
		//还没进行OCR识别的补扫  //已经OCR进入业务审核的补扫 
		String needProcess = batch.getNeedProcess();
		String progressFlag = batch.getProgressFlag();//获取流程节
		String batchId = batch.getBatchId();//批次号
		long procinstId = batch.getProcinstId();//工作项ID
		if(ARSConstants.NO_NEED_PROCESS.equals(needProcess)){
			//不需要处理
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			addLogInfo("开始批次补扫,{批次号:"+batch.getBatchId()+"}");// 添加批次日志信息
		}else if(ARSConstants.NEED_PROCESS.equals(needProcess)){
			//需要处理的批次			
			if(procinstId==0){//刚扫描的 还没进入工作流
					int patchBatchLock = tmpBatchMapper.patchBatchLock(BaseUtil.filterSqlParam(batchId));//上锁
					if(patchBatchLock>0){
						responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
						addLogInfo("开始批次补扫,{批次号:"+batch.getBatchId()+"}");// 添加批次日志信息
					}else if(BaseUtil.getLoginUser().getUserNo().equals(batch.getInputWorker())){//同扫同补
						responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
						addLogInfo("开始批次补扫,{批次号:"+batch.getBatchId()+"}");// 添加批次日志信息
					}else{
						responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
						responseBean.setRetMsg("批次正在被使用");
					}
			} else {
			//获取轧账状态,归档清理删除轧账信息
			List<HashMap<String,String>> checkOffList = checkOffMapper.ifRollFlow(BaseUtil.filterSqlParam(batch.getOccurDate()), BaseUtil.filterSqlParam(batch.getSiteNo()), BaseUtil.filterSqlParam(batch.getOperatorNo()));
			String all_checkOff ="";
			if(checkOffList!=null && checkOffList.size()>0){
				all_checkOff = checkOffList.get(0).get("ALL_CHECKOFF");
			}	
			 //已经轧账的补扫 
			if("1".equals(all_checkOff)){
				//如果已经轧账先解除
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("该批次已轧账，请先解除轧账再补扫!");
			//已经归档的补扫
			}else if("2".equals(all_checkOff)||"3".equals(all_checkOff)){
				//业务暂时定为不能补扫
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("该批次已归档!");
			}else if(ARSConstants.PROCESS_FLAG_98.equals(progressFlag)||ARSConstants.PROCESS_FLAG_99.equals(progressFlag)){//归档的批次需要选择主件流水做附件
				//工作流结束的补扫
				retMap.put("needPatchFlow", "true");
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
				addLogInfo("开始批次补扫,{批次号:"+batch.getBatchId()+"}");// 添加批次日志信息
			}else{
				retMap.put("progressFlag", progressFlag);//录流水保存状态
				retMap.put("needPatchFlow", "false");
//				只拿正常的批次2
				//userId wkiName  wkiPriid wkiUser  wkiID wkiState
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("userId", BaseUtil.getLoginUser().getUserNo());
				hashMap.put("wkiPriid", String.valueOf(procinstId));
				hashMap.put("wkiState", "2");
				List<WorkItemInfo> worksByPriId =sunFlowService.getWorkItemByCondition(hashMap, batch.getAreaCode(), 5);
				if(worksByPriId!=null && worksByPriId.size()>0){
					WorkItemInfo workItemInfo = worksByPriId.get(0);
					long workItemId = workItemInfo.getWorkItemId();
					//将当前用户插入的工作流可申请人（需要校验么？）
					ArrayList<String> arrayList = new ArrayList<String>();
					arrayList.add(BaseUtil.getLoginUser().getUserNo());
					sunFlowService.addApplyMan("sa",workItemId ,arrayList);
					//挂起之前进行申请
					sunFlowService.applyWorkItem(BaseUtil.getLoginUser().getUserNo(), workItemId);
					boolean voidFlag=sunFlowService.suspendWorkItem(BaseUtil.getLoginUser().getUserNo(),workItemId);
					System.out.println("正常工作流挂起的补扫"+voidFlag);
					if(voidFlag){
						responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
						addLogInfo("开始批次补扫,{批次号:"+batch.getBatchId()+"}");// 添加批次日志信息
					}else{
						responseBean.setRetMsg("创建补扫失败!");
						responseBean.setRetCode("1");//失败标识 
					}	
				} else {
					//只拿自己挂起的 
					hashMap.put("wkiState", "5");
					hashMap.put("wkiUser", BaseUtil.getLoginUser().getUserNo());
					List<WorkItemInfo> worksByPriIdForce = sunFlowService.getWorkItemByCondition(hashMap, batch.getAreaCode(), 5);
					if(worksByPriIdForce!=null && worksByPriIdForce.size()>0) {
						System.out.println("自己挂起的批次补扫");
						responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
						addLogInfo("开始批次补扫,{批次号:"+batch.getBatchId()+"}");// 添加批次日志信息
					}else{
						responseBean.setRetMsg("该批次正在使用中!");
						responseBean.setRetCode("1");//失败标识 
					}
				}
			}
		}	
		}
		
		//无纸化标记
		retMap.put("mixedNpFlag", voucherChkMapper.getVoucherChkByBatchId(BaseUtil.filterSqlParam(batch.getBatchId())).getMixedNpFlag());
		responseBean.setRetMap(retMap);

	}
	
	
	
	/**
	 * 查询补扫主件图像信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void patchImageQuery(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		// 拼装返回信息
		Map retMap = new HashMap();
		
		//拼装查询参数
		HashMap<String, Object> condMap = new HashMap<String, Object>();
		TmpBatch occurBatch = JSON.parseObject((String)sysMap.get("occurBatch"),TmpBatch.class);
		String patchFlow = (String)sysMap.get("patchFlow");
		int pageNo = (int)sysMap.get("pageNo");
		int pageSize = (int)sysMap.get("pageSize");
		condMap.put("tmpbatch", occurBatch);
		condMap.put("patchFlow", patchFlow);
		condMap.put("pageNo", pageNo);
		condMap.put("pageSize", pageSize);
		
		TmpBatch  batch = tmpBatchMapper.selectByPrimaryKey(occurBatch.getBatchId());
		String dataTb = "bp_tmpdata_1_tb";
		String flowTb = "fl_flow_tb";
		if(batch.getProgressFlag().equals(ARSConstants.PROCESS_FLAG_99)){
			dataTb = dataTb + "_HIS";
			flowTb = flowTb + "_HIS";
		}
		condMap.put("dataTb", dataTb);
		condMap.put("flowTb", flowTb);
		// 定义分页操作
		Page page = PageHelper.startPage(pageNo, pageSize);
		// 查询分页记录
		List list = getList(tmpDataMapper.selectImageListByFlow(condMap), page);
		// 获取总记录数
		long totalCount = page.getTotal();
		
		
		if(totalCount==0){
			retMap.put("checkFlag", -1);
		}else{
			retMap.put("checkFlag", 1);
			retMap.put("pageSize", pageSize);
			retMap.put("allRow", totalCount);
			retMap.put("patchImageList", list);
			
		}
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void patchSubmit(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		// 组织批次信息
		TmpBatch occurBatch = JSON.parseObject((String)sysMap.get("occurBatch"),TmpBatch.class);
		
		String patchSerialNo = (String)sysMap.get("patchSerialNo");
		String progressFlag = (String)sysMap.get("progressFlag");
		
		// 组织图像信息
		List<TmpData> tmpDataList = JSON.parseArray((String)sysMap.get("imageList"),TmpData.class);
		for (int i = 0; i < tmpDataList.size(); i++) {
			tmpDataList.get(i).setInccodeinBatch(i + 1);
			tmpDataList.get(i).setBatchId(occurBatch.getBatchId());
			tmpDataList.get(i).setPsLevel(ARSConstants.PS_LEVEL);
			tmpDataList.get(i).setCheckFlag(ARSConstants.CHECK_FLAG);
			tmpDataList.get(i).setErrorFlag(ARSConstants.ERROR_FLAG);
			tmpDataList.get(i).setIsAudit(ARSConstants.IS_AUDIT);
			tmpDataList.get(i).setSelfDelete(ARSConstants.SELF_DELETE);
			tmpDataList.get(i).setProcessState(ARSConstants.PROCESS_STATE);
			tmpDataList.get(i).setRecFailCause(ARSConstants.REC_FAIL_CAUSE);
			tmpDataList.get(i).setPrimaryInccodein(ARSConstants.PRIMARY_INCCODEIN);
			tmpDataList.get(i).setIsFrontPage(ARSConstants.IS_FRONT_PAGE_NOBACK);
			tmpDataList.get(i).setCopyInccodein(ARSConstants.COPY_INCCODEIN);
			tmpDataList.get(i).setCopyRec(ARSConstants.COPY_REC);
			tmpDataList.get(i).setSerialNo(UUidUtil.uuid());
			tmpDataList.get(i).setMemo("");
			tmpDataList.get(i).setRecDate("");
			tmpDataList.get(i).setFlowId("");
			tmpDataList.get(i).setFormName("");
			tmpDataList.get(i).setFormGroup("");
			tmpDataList.get(i).setVouhType("");
			tmpDataList.get(i).setPatchFlag(0);
			//如果单面，背面保存正面图像名
			if(null == tmpDataList.get(i).getBackFileName() || "null".equalsIgnoreCase(tmpDataList.get(i).getBackFileName())){
				tmpDataList.get(i).setBackFileName(tmpDataList.get(i).getFileName());
			}
		}
			boolean patchBatchCommit = this.patchBatchCommit(occurBatch, tmpDataList ,patchSerialNo,progressFlag);
			if(patchBatchCommit){
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
				responseBean.setRetMsg("补扫成功");
				// 添加批次日志信息
				 addLogInfo("补扫提交,{批次号:"+occurBatch.getBatchId()+"}");
			}else{
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("补扫失败");
			}			
	}
	
	/**
	 * 补扫批次提交
	 * @param tmpbatch 批次信息（批次表）
	 * @param tmpDataList 图像信息（数据表）
	 * @throws Exception 
	 */
	public boolean  patchBatchCommit(TmpBatch tmpbatch, List<TmpData> tmpDataList ,String patchSerialNo,String progressFlag) throws Exception {
		try {
			String batchTb = "bp_tmpbatch_tb";
			TmpBatch oldTmpbatch = tmpBatchMapper.selectByPrimaryKey(tmpbatch.getBatchId());
			String dataTb = oldTmpbatch.getTempDataTable();
			if (ARSConstants.PROCESS_FLAG_99.equals(oldTmpbatch.getProgressFlag())) {
				dataTb =  dataTb + "_HIS";
			}
			dataTb = BaseUtil.filterSqlParam(dataTb);
			tmpbatch.setTempDataTable(dataTb);
			List<TmpData> tmpDataList_DB = tmpDataMapper.selectDataList(BaseUtil.filterSqlParam(tmpbatch.getBatchId()), dataTb);
			tmpDataMapper.updateBeforeSubmit(BaseUtil.filterSqlParam(tmpbatch.getBatchId()),dataTb);//补扫提交前更新对应主键批内码
			boolean isFind = false;
			int newCount = 0;//补扫影像张数
			List<String> copySerialNos =null;
			for (TmpData tmpData : tmpDataList) {
				isFind = false;
				copySerialNos = new ArrayList<String>();
				for(TmpData tmpData_DB : tmpDataList_DB) {
					//防止复制图片也更新成相同的批内码
					if(tmpData.getFileName().equals(tmpData_DB.getFileName()) && tmpData_DB.getCopyInccodein() == 0) {
						//如果批内码有变化需更新相关字段
						if(tmpData.getInccodeinBatch().intValue() != tmpData_DB.getInccodeinBatch().intValue()){
							//拼装查询参数
							HashMap<String, Object> condMap = new HashMap<String, Object>();
							condMap.put("newInccodeinBatch",  tmpData.getInccodeinBatch());
							condMap.put("serialNo",  BaseUtil.filterSqlParam(tmpData_DB.getSerialNo()));
							condMap.put("dataTb", BaseUtil.filterSqlParam(dataTb));
							tmpDataMapper.updateTmpData(condMap);
						}
						isFind = true;
						//break;
					}else if(tmpData.getFileName().equals(tmpData_DB.getFileName()) && tmpData_DB.getCopyInccodein() != 0){
						//处理复制影像的复制批内码,得到该凭证所有复制处理影像的serialNo
						copySerialNos.add(tmpData_DB.getSerialNo());
					}
				}
				if(!isFind) {//未找到说明是新增图像
					newCount ++;
					//补扫时选择主件流水
					if(!StringUtil.checkNull(patchSerialNo) ){
						tmpData.setPrimaryInccodein(1);
						tmpData.setCheckFlag("0");
						tmpData.setPsLevel("0");
						tmpData.setProcessState("100000");
						if("".equals(ARSConstants.SYSTEM_PARAMETER.get("SLAVE_DEFAULT_NAME").getParamValue())){
							tmpData.setFormName("一般附件");
						}else{
							tmpData.setFormName(ARSConstants.SYSTEM_PARAMETER.get("SLAVE_DEFAULT_NAME").getParamValue());
						}
						tmpData.setCopySerialno(patchSerialNo);
					}
					tmpData.setPatchFlag(ARSConstants.IMAGE_PATCH_FLAG);
					tmpDataMapper.insertSelective(tmpData, dataTb);
				}else{
					//处理复制影像的copy_Inccodein
					if(copySerialNos != null && copySerialNos.size()>0){
						tmpDataMapper.updateCopyInccodein(copySerialNos,BaseUtil.filterSqlParam(tmpbatch.getBatchId()),tmpData.getInccodeinBatch(),dataTb);
					}
				}
			}
			//处理复制的图片
			int copyCount = 0;
			for(TmpData tmpData_DB1 : tmpDataList_DB) {
				if(tmpData_DB1.getCopyInccodein() != 0){
					copyCount ++ ;
					tmpDataMapper.updateCopyInccodeinbatch(tmpData_DB1.getSerialNo(),tmpbatch.getBatchId(),copyCount,dataTb);	
				}
			}
			
			//补扫提交后更新复制影像，复制影像批内码变了
			tmpDataMapper.updatePrimary(BaseUtil.filterSqlParam(tmpbatch.getBatchId()), dataTb);
			tmpDataMapper.updateCopySerialNo(BaseUtil.filterSqlParam(tmpbatch.getBatchId()), dataTb);
			
			
			HashMap<String, Object> batchMap = new HashMap<String, Object>();
			batchMap.put("batchId", tmpbatch.getBatchId());
			//批次总数未原张数+补扫张数
			batchMap.put("newBatchTotalPage",String.valueOf(tmpDataList_DB.size() + newCount));
			batchMap.put("batchTb", batchTb);
			tmpBatchMapper.updateTmpBatch(batchMap);
			tmpDataMapper.updateProcessState(BaseUtil.filterSqlParam(tmpbatch.getBatchId()), dataTb);
			tmpBatchMapper.updateBatchTask(tmpbatch.getBatchId());
			//如果未输入流水，走自动识别
			if(StringUtil.checkNull(patchSerialNo) || StringUtil.checkNull(progressFlag)){
				progressFlag = "10";
			}
			if(ARSConstants.NEED_PROCESS.equals(oldTmpbatch.getNeedProcess())){
				//未进入工作流的补扫提交
				if(oldTmpbatch.getProcinstId()==0){
					System.out.println("未进入工作流的补扫提交");
					int patchBatchUnlock = tmpBatchMapper.patchBatchUnlock(BaseUtil.filterSqlParam(oldTmpbatch.getBatchId()));
					return true;
				}
				//已结束的工作流补扫 重新发起工作流
				if(ARSConstants.PROCESS_FLAG_98.equals(oldTmpbatch.getProgressFlag())||ARSConstants.PROCESS_FLAG_99.equals(oldTmpbatch.getProgressFlag())){
					System.out.println("已结束的工作流补扫");
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("batchId", tmpbatch.getBatchId());
					hashMap.put("batchTb", batchTb);
					hashMap.put("newOcrFactorFlag", "0");
					hashMap.put("newProgressFlag","10");
					hashMap.put("newProcinstId",0);
					tmpBatchMapper.updateTmpBatch(hashMap);
					return true;
				}else {
				//需要处理的批次更新状态值 varsir
				//调用工作流接口将任务重新发起
				System.out.println("挂起的工作流补扫");
				//拿自己挂起的
				//userId wkiName  wkiPriid wkiUser   wkiState
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("userId", BaseUtil.getLoginUser().getUserNo());
				hashMap.put("wkiPriid", String.valueOf(oldTmpbatch.getProcinstId()));
			//	hashMap.put("wkiUser", BaseUtil.getLoginUser().getUserNo());  感觉没有必要了
				hashMap.put("wkiState", "5");
				List<WorkItemInfo> worksByPriId = 	sunFlowService.getWorkItemByCondition(hashMap, oldTmpbatch.getAreaCode(), 5);	
				if(worksByPriId!=null && worksByPriId.size()>0){
					long workItemId=worksByPriId.get(0).getWorkItemId();
					boolean resumeWorkItem = sunFlowService.resumeWorkItem(BaseUtil.getLoginUser().getUserNo(),workItemId );
					//挂起前进行了申请  所以解挂后状态应该是4
					boolean falg = sunFlowService.terminateProcessInstance(BaseUtil.getLoginUser().getUserNo(), oldTmpbatch.getProcinstId(), oldTmpbatch.getAreaCode());
					if(falg){
						  logger.info("工作流作废成功");
						  HashMap<String,Object> hashMap1 = new HashMap<String,Object>();
						  hashMap1.put("batchId", tmpbatch.getBatchId());
						  hashMap1.put("batchTb", batchTb);
						  hashMap1.put("newOcrFactorFlag", "0");
						  hashMap1.put("newProgressFlag", "10");
						  hashMap1.put("newProcinstId", 0);
						  tmpBatchMapper.updateTmpBatch(hashMap1);
						  return true;
						  
					}else{
						return false;
					}
					
					
					
					//原来就是10，只需要释放就可以了
/*					if(oldTmpbatch.getProgressFlag().equals("10")){
						boolean release = sunFlowService.release(BaseUtil.getLoginUser().getUserNo(), workItemId);
						System.out.println("解挂后释放工作流");
						return true;
					//如果是30，则需要调整工作流
					}else {
						System.out.println("解挂后更新工作流");
						WorkItemInfo arsWorkItemInfo = new WorkItemInfo();
						arsWorkItemInfo.setWorkItemId(workItemId);
						Map<String, String> workItemAttris = new HashMap<>();
						workItemAttris.put("flowFlag", "10");
						arsWorkItemInfo.setWorkItemAttris(workItemAttris);
						boolean falg = sunFlowService.updateWorkItemInfo(BaseUtil.getLoginUser().getUserNo(), arsWorkItemInfo,"2");
						if(falg){
							System.out.println("工作流修改成功");
							HashMap<String, Object> hashMap1 = new HashMap<String, Object>();
							hashMap1.put("batchId", tmpbatch.getBatchId());
							hashMap1.put("batchTb", batchTb);
							hashMap1.put("newOcrFactorFlag", "0");
							hashMap1.put("newProgressFlag","10");
							tmpBatchMapper.updateTmpBatch(hashMap1);
							return true;
						}else{
							return false;
						}
					}*/
					}else{
						return true;
					}
				}
			}else{
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void patchInfoQuery(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		// 拼装返回信息
		Map retMap = new HashMap();
		// 组织批次信息
		String batchId = (String)sysMap.get("batchId");
		//获取批次信息
		TmpBatch tmpBatch = tmpBatchMapper.selectByPrimaryKey(batchId);
		//拼装查询参数
		HashMap<String, Object> condMap = new HashMap<String, Object>();
		condMap.put("batchId", batchId);
		condMap.put("dataTb", BaseUtil.filterSqlParam(tmpBatch.getTempDataTable()));
		
		List<HashMap<String,Object>> imageList = tmpDataMapper.selectPatchImageList(condMap);
		List<HashMap<String,Object>> imageTaskList = tmpDataMapper.selectPatchTaskImageList(batchId);
		
		//拼装返回List
		List<Map<String,Object>> patchInfoList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = null;
		if(imageList!=null && imageList.size()>0){
			for(int i=0;i<imageList.size();i++){
				map = new HashMap<String,Object>();
				map.put("rn",i+1);
				map.put("ocr_worker", imageList.get(i).get("OCR_WORKER"));
				map.put("desc", "重扫第["+imageList.get(i).get("INCCODEIN_BATCH")+"]张");
				patchInfoList.add(map);
			}
		}
		int size = patchInfoList.size();
		if(imageTaskList!=null && imageTaskList.size()>0){
			for(int j=0;j<imageTaskList.size();j++){
				map = new HashMap<String,Object>();
				map.put("rn",j+1+size);
				map.put("ocr_worker", imageTaskList.get(j).get("OPERATOR_WORKER"));
				map.put("desc", imageTaskList.get(j).get("PATCH_DES"));
				patchInfoList.add(map);
			}
		}
		retMap.put("patchInfoList", patchInfoList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void patchTaskQuery(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		// 拼装返回信息
		Map retMap = new HashMap();
		// 组织批次信息
		User loginUser = BaseUtil.getLoginUser();
		if(loginUser==null){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("查询失败");
			return;
		}
		String organNo = loginUser.getOrganNo();
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(organNo);
		String userNo = loginUser.getUserNo();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("inputWoker", userNo);
		hashMap.put("siteNos", arrayList);
		List patchBatchs = tmpBatchMapper.getPatchBatchs(hashMap);
		int patchTaskCount = tmpDataMapper.getPatchTaskCount(patchBatchs);
		//int patchVoucCount = tmpDataMapper.getPatchVoucCount(patchBatchs);
		int count =patchTaskCount + 0;
		retMap.put("count",count);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
	}
	
	/**
	 * 初始化影像采集系统参数
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initScanParam(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		//获取用户信息
		User user = BaseUtil.getLoginUser();
		
		//获取扫描同步异步
		SysParameter issyn = systemParameterMapper.selectByPrimaryKey("SCAN_ISSYN_TYPE", user.getBankNo(), user.getSystemNo(), user.getProjectNo());
		//获取批次登记方式
		SysParameter scanType = systemParameterMapper.selectByPrimaryKey("SCAN_TYPE", user.getBankNo(), user.getSystemNo(), user.getProjectNo());
	
		// 拼装返回信息
		Map retMap = new HashMap();		
		retMap.put("issyn", issyn);
		retMap.put("scanType", scanType);		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
}
