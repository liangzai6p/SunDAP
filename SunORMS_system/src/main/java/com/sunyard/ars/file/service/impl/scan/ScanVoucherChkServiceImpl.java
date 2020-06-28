package com.sunyard.ars.file.service.impl.scan;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;


import com.sunyard.ars.flows.service.api.SunFlowService;
import com.sunyard.ars.flows.service.api.WorkItemInfo;
import com.sunyard.ars.flows.service.impl.api.SunFlowServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.aos.common.comm.BaseService;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.SysCode;
import com.sunyard.ars.common.dao.FlowMapper;
import com.sunyard.ars.common.dao.TmpBatchMapper;
import com.sunyard.ars.common.pojo.TmpBatch;
import com.sunyard.ars.common.util.DateUtil;
import com.sunyard.ars.common.util.StringUtil;
import com.sunyard.ars.file.dao.oa.CheckOffMapper;
import com.sunyard.ars.file.dao.scan.VoucherChkMapper;
import com.sunyard.ars.file.pojo.oa.CheckOff;
import com.sunyard.ars.file.pojo.scan.VoucherChk;
import com.sunyard.ars.file.service.scan.IVoucherChkService;
import com.sunyard.ars.system.dao.busm.TellerMapper;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.pojo.User;

/**
 * 
 * @date   2018年5月24日
 * @Description 影像采集service实现类
 */
@Service("voucherChkService")
@Transactional
public class ScanVoucherChkServiceImpl extends BaseService implements IVoucherChkService{
	@Resource
	private VoucherChkMapper voucherChkMapper;
	
	@Resource
	private CheckOffMapper checkOffMapper;
	
	@Resource
	private TmpBatchMapper tmpBatchMapper;
	@Resource
	TellerMapper tellerMapper;
	@Resource
	FlowMapper flowMapper;
	
	@Override
	public ResponseBean execute(RequestBean requestBean)throws Exception{
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
		
		if(ARSConstants.OPERATE_ADD.equals(oper_type)){
			//批次登记
			addOperation(requestBean, responseBean);
		}else if(ARSConstants.OPERATE_MODIFY.equals(oper_type)){
			//登记批次修改
			modifyOperation(requestBean, responseBean);
		}else if(ARSConstants.OPERATE_DELETE.equals(oper_type)){
			//登记批次删除
			deleteOperation(requestBean, responseBean);
		}else if("voucherInfo".equals(oper_type)){
			//柜员登记时，批次信息确认
			voucherInfo(requestBean, responseBean);
		}
	}
	

	/**
	 * 批次登记
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		// 返回信息map
		Map retMap = new HashMap();
		
		// 获取新增数据
		VoucherChk voucherChk  = (VoucherChk) requestBean.getParameterList().get(0);
		String operatorNo = voucherChk.getOperatorNo();
		if(operatorNo.length()==6){
			
		}
		User user = BaseUtil.getLoginUser();
		voucherChk.setInputWorker(user.getUserNo());
		
		//添加voucher对象属性
		//生成batchId
		Date date = new Date();
		String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(date);
		String HHmmssSSS = new SimpleDateFormat("HHmmssSSS").format(date);
		SecureRandom random = new SecureRandom();
		String batchId = yyyyMMdd + HHmmssSSS + random.nextInt(10)
		+ random.nextInt(10) + random.nextInt(10);
		
		voucherChk.setBatchCommit(ARSConstants.BATCH_NOT_COMMIT);
		voucherChk.setBatchId(batchId);
		voucherChk.setRegDate(yyyyMMdd);
		voucherChk.setRegTime(HHmmssSSS.substring(0, 6));
		voucherChk.setIsInvalid(ARSConstants.BATCH_IS_VALID);
		
		voucherChk.setMachineId(BaseUtil.filterSqlParam(StringUtil.getIP(RequestUtil.getRequest())));
	
		
		
		
		//初始化批次信息
		TmpBatch batch = new TmpBatch();
		batch.setBatchId(batchId);
        batch.setInputDate(DateUtil.getNow());
		batch.setOccurDate(voucherChk.getOccurDate());
		batch.setSiteNo(voucherChk.getSiteNo());
		batch.setOperatorNo(voucherChk.getOperatorNo());
		batch.setBusinessId(voucherChk.getBusinessId());
		batch.setNeedProcess(voucherChk.getNeedProcess());
		batch.setBatchTotalPage(voucherChk.getRegVoucherNum());
		
		String checkFlag = (String) sysMap.get("checkFlag");//是否确认登记
		
		Map<String,Object> syscodeMap = register(voucherChk, checkFlag);
		
		if(999999 == Integer.valueOf(syscodeMap.get("syscode")
				.toString())){
			retMap.put("rownum", syscodeMap.get("rownum"));
		}
		else if(989898 == Integer.valueOf(syscodeMap.get("syscode")
				.toString())){
			retMap.put("rownum", syscodeMap.get("rownum"));
		}
		else if (SysCode.SYSCODE_888888 == Integer.valueOf(syscodeMap.get("syscode")
				.toString())){
			retMap.put("rownum", syscodeMap.get("rownum"));
		}else if(SysCode.SYSCODE_300076 == Integer.valueOf(syscodeMap.get("syscode").toString())){
			//无纸化
			VoucherChk vc = (VoucherChk) syscodeMap.get("voucherChk");
			TmpBatch bt = tmpBatchMapper.selectByPrimaryKey(vc.getBatchId());

			String needProcess = bt.getNeedProcess();
			String progressFlag = bt.getProgressFlag();//获取流程节
			batchId = bt.getBatchId();//批次号
			long procinstId = bt.getProcinstId();//工作项ID
			retMap.put("progressFlag", progressFlag);
			if(ARSConstants.NO_NEED_PROCESS.equals(needProcess)){
				//不需要处理
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
				addLogInfo("开始无纸化批次扫描,{批次号:"+bt.getBatchId()+"}");// 添加批次日志信息
			}else if(ARSConstants.NEED_PROCESS.equals(needProcess)) {
				//需要处理的批次
				if (procinstId == 0) {//刚扫描的 还没进入工作流
					int patchBatchLock = tmpBatchMapper.patchBatchLock(BaseUtil.filterSqlParam(batchId));//上锁
					if (patchBatchLock > 0) {
						responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
						addLogInfo("开始无纸化批次扫描,{批次号:" + bt.getBatchId() + "}");// 添加批次日志信息
					} else {
						responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
						responseBean.setRetMsg("批次正在被使用");
					}
				} else {
					//获取轧账状态,归档清理删除轧账信息
					List<HashMap<String, String>> checkOffList = checkOffMapper.ifRollFlow(BaseUtil.filterSqlParam(bt.getOccurDate()), BaseUtil.filterSqlParam(bt.getSiteNo()), BaseUtil.filterSqlParam(bt.getOperatorNo()));
					String all_checkOff = "";
					if (checkOffList != null && checkOffList.size() > 0) {
						all_checkOff = checkOffList.get(0).get("ALL_CHECKOFF");
					}
					//已经轧账的补扫
					if ("1".equals(all_checkOff)) {
						//如果已经轧账先解除
						responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
						responseBean.setRetMsg("该批次已轧账，请先解除轧账再补扫!");
						//已经归档的补扫
					}  else if (ARSConstants.PROCESS_FLAG_99.equals(progressFlag)) {//归档的批次需要选择主件流水做附件
						//工作流结束的补扫
						retMap.put("needPatchFlow", "true");
						responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
						addLogInfo("开始批次补扫,{批次号:" + bt.getBatchId() + "}");// 添加批次日志信息
					} else {
						retMap.put("needPatchFlow", "false");
						//只拿正常的批次2
						SunFlowService sunFlowService = new SunFlowServiceImpl();
						//userId wkiName  wkiPriid wkiUser  wkiID wkiState
						HashMap<String, String> hashMap = new HashMap<String, String>();
						hashMap.put("userId", BaseUtil.getLoginUser().getUserNo());
						hashMap.put("wkiPriid", String.valueOf(procinstId));
						hashMap.put("wkiState", "2");
						List<WorkItemInfo> worksByPriId = sunFlowService.getWorkItemByCondition(hashMap, bt.getAreaCode(), 5);
						if (worksByPriId != null && worksByPriId.size() > 0) {
							WorkItemInfo workItemInfo = worksByPriId.get(0);
							long workItemId = workItemInfo.getWorkItemId();
							//将当前用户插入的工作流可申请人
							ArrayList<String> arrayList = new ArrayList<String>();
							arrayList.add(BaseUtil.getLoginUser().getUserNo());
							sunFlowService.addApplyMan(ARSConstants.SUNFLOW_USER, workItemId, arrayList);
							//挂起之前进行申请
							sunFlowService.applyWorkItem(BaseUtil.getLoginUser().getUserNo(), workItemId);
							boolean voidFlag = sunFlowService.suspendWorkItem(BaseUtil.getLoginUser().getUserNo(), workItemId);
							System.out.println("正常工作流挂起的补扫" + voidFlag);
							if (voidFlag) {
								responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
								addLogInfo("开始批次补扫,{批次号:" + bt.getBatchId() + "}");// 添加批次日志信息
							} else {
								responseBean.setRetMsg("创建补扫失败!");
								responseBean.setRetCode("1");//失败标识
							}
						} else {
							//只拿自己挂起的
							hashMap.put("wkiState", "5");
							hashMap.put("wkiUser", BaseUtil.getLoginUser().getUserNo());
							List<WorkItemInfo> worksByPriIdForce = sunFlowService.getWorkItemByCondition(hashMap, bt.getAreaCode(), 5);
							if (worksByPriIdForce != null && worksByPriIdForce.size() > 0) {
								System.out.println("自己挂起的批次补扫");
								responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
								addLogInfo("开始批次补扫,{批次号:" + bt.getBatchId() + "}");// 添加批次日志信息
							} else {
								responseBean.setRetMsg("该批次正在使用中!");
								responseBean.setRetCode("1");//失败标识
							}
						}
					}
				}
			}


			batch.setInputDate(bt.getInputDate());
			batch.setBatchId(vc.getBatchId());
			batch.setBatchTotalPage(vc.getRegVoucherNum());
			retMap.put("rownum", vc.getId());
		}else{
			VoucherChk vc = (VoucherChk) syscodeMap.get("voucherChk");
			batch.setBatchId(vc.getBatchId());
			batch.setInputDate(vc.getRegDate());
			batch.setBatchTotalPage(vc.getRegVoucherNum());
			retMap.put("rownum", vc.getId());
		}
	
		// 拼装返回信息
		retMap.put("batch", batch);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(syscodeMap.get("syscode").toString());
		responseBean.setRetMsg(SysCode.sysCodeMap.get(syscodeMap.get("syscode")));
		
		// 添加日志信息
		String logContent = "影像采集登记批次，||{批次号：" + batchId
				+ "，交易日期：" + voucherChk.getOccurDate() + "，交易机构："
				+ voucherChk.getSiteNo() + "，交易柜员：" + voucherChk.getOperatorNo() +"}";
		addLogInfo(logContent);

		
	}
	
	/**
	 * 执行批次登记方法
	 * @param voucherChk
	 * @param checkFlag
	 * @return
	 */
	private Map<String, Object> register(VoucherChk voucherChk,String checkFlag){
		Map<String, Object> map = new HashMap<String, Object>();
		if("true".equals(checkFlag)){//如果已经进行登记确认，直接登记批次
			map.put("syscode", SysCode.SYSCODE_888888);
			voucherChkMapper.insertSelective(voucherChk);
			map.put("rownum", voucherChk.getId());
			// 记录操作日志
			String logContent = "登记批次信息，||{批次号：" + voucherChk.getBatchId() + "，业务日期：" + voucherChk.getOccurDate() + "，机构：" + voucherChk.getSiteNo() + "，柜员：" + voucherChk.getOperatorNo() + "}";
			addLogInfo(logContent);
			return map;
		}
		if("1".equals(voucherChk.getNeedProcess())){//如果需要处理的批次验证轧账信息
			//1.检查轧账状态,已轧账需解除轧账
			List<HashMap<String,String>> checkOffList = checkOffMapper.ifRollFlow(voucherChk.getOccurDate(), voucherChk.getSiteNo(), voucherChk.getOperatorNo());
			String all_checkOff ="";
			if(checkOffList!=null && checkOffList.size()>0){
				all_checkOff = checkOffList.get(0).get("ALL_CHECKOFF");
			}
			if("2".equals(all_checkOff)){
				map.put("voucherChk", voucherChk);
				map.put("syscode", 999999);//已归档
				return map;
			}
			if("1".equals(all_checkOff)){
				map.put("voucherChk", voucherChk);
				map.put("syscode", SysCode.SYSCODE_300067);
				return map;
			}
		}
		//单独校验有没有流水 放在验证轧账表之后 每次都提醒
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("occurDate", voucherChk.getOccurDate());
		hashMap.put("siteNo", voucherChk.getSiteNo());
		hashMap.put("operatorNo", voucherChk.getOperatorNo());
		int selectDealCount = flowMapper.selectDealCount(hashMap);
		if(!"noflow".equals(checkFlag)&&selectDealCount==0&&!voucherChk.getOccurDate().equals(DateUtil.getNow())&&voucherChk.getOperatorNo().length()<6){
				map.put("voucherChk", voucherChk);
				map.put("syscode", 989898);//轧账表无数据
				return map;	
		}
		//查询数据库登记信息
		List<VoucherChk> voucherListInDB = voucherChkMapper.getVoucherChk(voucherChk);
		if(voucherListInDB != null && voucherListInDB.size() > 0) {//如果批次登记表中存在该批次
			VoucherChk commitVoucher = null;//是否提交过批次
			boolean otherVoucher = false;//是否有其他柜员登记
			VoucherChk myVoucher = null;
			for (VoucherChk voucherInDB : voucherListInDB) {
				if(ARSConstants.BATCH_COMMIT.equals(voucherInDB.getBatchCommit())) {
					commitVoucher = voucherInDB;
				}else{
					if(!voucherChk.getInputWorker().equals(voucherInDB.getInputWorker())) {
						otherVoucher = true;
					}else{//批次已经被当前用户登记
						myVoucher = voucherInDB;
					}
				}	
			}

			if(commitVoucher != null){//已提交过
				if("1".equals(voucherChk.getMixedNpFlag())){//无纸化批次为补扫，需要批次信息
					map.put("voucherChk", commitVoucher);
					map.put("syscode", SysCode.SYSCODE_300076);
				}else{
					if(myVoucher != null){//自己登记未提交
						map.put("voucherChk", myVoucher);
						map.put("syscode", SysCode.SYSCODE_300071);
					}else if(otherVoucher){//被其他用户登记未提交
						map.put("voucherChk", voucherChk);
						map.put("syscode", SysCode.SYSCODE_300072);
					}else{//没有未提交的批次信息
						map.put("voucherChk", voucherChk);
						map.put("syscode", SysCode.SYSCODE_300073);
					}
				}		
			}else{
				if(myVoucher != null){//自己登记未提交
					map.put("voucherChk", myVoucher);
					map.put("syscode", SysCode.SYSCODE_300074);
				}else if(otherVoucher){//被其他用户登记未提交
					map.put("voucherChk", voucherChk);
					map.put("syscode", SysCode.SYSCODE_300075);
				}
			}
			
			return map;
		}
		voucherChkMapper.insertSelective(voucherChk);
		map.put("rownum", voucherChk.getId());
		map.put("syscode", SysCode.SYSCODE_888888);
		// 记录操作日志
		String logContent = "登记批次信息，||{批次号：" + voucherChk.getBatchId() + "，业务日期：" + voucherChk.getOccurDate() + "，机构：" + voucherChk.getSiteNo() + "，柜员：" + voucherChk.getOperatorNo() + "}";
		addLogInfo(logContent);
					
		return map;
	}
	
	/**
	 * 登记批次信息修改
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	private void modifyOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		// 返回信息map
		Map retMap = new HashMap();
		
		// 获取修改数据
		VoucherChk voucherChk  = (VoucherChk) requestBean.getParameterList().get(0);
		
		//执行更新
		voucherChkMapper.updateByPrimaryKeySelective(voucherChk);
		
		//返回信息
		responseBean.setRetCode(String.valueOf(SysCode.SYSCODE_888888));
		responseBean.setRetMsg("修改成功");
		
		// 添加日志信息
		String logContent = "修改登记批次信息，{等级批次id:"+voucherChk.getId()+",登记批次isInvalid："+voucherChk.getIsInvalid()+"}";
		
		addLogInfo(logContent);
	}
	/**
	 * 登记批次信息删除
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "rawtypes" })
	private void deleteOperation(RequestBean requestBean, ResponseBean responseBean) {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		// 返回信息map
		Map retMap = new HashMap();
		
		// 获取修改数据
		VoucherChk voucherChk  = (VoucherChk) requestBean.getParameterList().get(0);
		voucherChkMapper.deleteByPrimaryKey(voucherChk.getId());
		//返回信息
		responseBean.setRetCode(String.valueOf(SysCode.SYSCODE_888888));
		responseBean.setRetMsg("修改成功");
		
		// 添加日志信息
		String logContent = "删除登记批次信息，{修改批次id:"+voucherChk.getId()+",修改批次isInvalid："+voucherChk.getIsInvalid()+"}";
		
		addLogInfo(logContent);
	}
	
	/**
	 * 获取批次登记信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void voucherInfo(RequestBean requestBean, ResponseBean responseBean) throws Exception {
       // 前台参数集合
		Map sysMap = requestBean.getSysMap();
		// 返回信息map
		Map retMap = new HashMap();
		
		// 获取修改数据
		String batchId = (String) sysMap.get("batchId");
		
		VoucherChk voucherChk = voucherChkMapper.getVoucherChkByBatchId(batchId);

		TmpBatch batch = tmpBatchMapper.selectByPrimaryKey(batchId);
     
		String retCode="-1";
		String retMsg="";
		
		/*
		 * 实物档案流程控制扫描
		 */
		if(ARSConstants.IS_SCAN_AFTER_FM && null != voucherChk){
			
		}
		
		
		if (null == voucherChk) {
			retCode="-1";
			retMsg="没有找到对应的批次登记信息";
			
		} else if (batch != null) {
			if("1".equals(voucherChk.getMixedNpFlag())){
				retMap.put("voucherChk", voucherChk);
				retMap.put("inputDate",  batch.getInputDate());
				retCode="1";

				String needProcess = batch.getNeedProcess();
				String progressFlag = batch.getProgressFlag();//获取流程节
				long procinstId = batch.getProcinstId();//工作项ID
				retMap.put("progressFlag", progressFlag);
				if(ARSConstants.NO_NEED_PROCESS.equals(needProcess)){
					//不需要处理
					addLogInfo("开始无纸化批次扫描,{批次号:"+batch.getBatchId()+"}");// 添加批次日志信息
				}else if(ARSConstants.NEED_PROCESS.equals(needProcess)) {
					//需要处理的批次
					if (procinstId == 0) {//刚扫描的 还没进入工作流
						int patchBatchLock = tmpBatchMapper.patchBatchLock(BaseUtil.filterSqlParam(batch.getBatchId()));//上锁
						if (patchBatchLock > 0) {
							addLogInfo("开始无纸化批次扫描,{批次号:" + batch.getBatchId() + "}");// 添加批次日志信息
						} else {
							retCode="-1";
							retMsg="批次正在被使用";
						}
					} else {
						//获取轧账状态,归档清理删除轧账信息
						List<HashMap<String, String>> checkOffList = checkOffMapper.ifRollFlow(BaseUtil.filterSqlParam(batch.getOccurDate()), BaseUtil.filterSqlParam(batch.getSiteNo()), BaseUtil.filterSqlParam(batch.getOperatorNo()));
						String all_checkOff = "";
						if (checkOffList != null && checkOffList.size() > 0) {
							all_checkOff = checkOffList.get(0).get("ALL_CHECKOFF");
						}
						//已经轧账的补扫
						if ("1".equals(all_checkOff)) {
							//如果已经轧账先解除
							retCode="-1";
							retMsg="该批次已轧账，请先解除轧账再补扫!";
							//已经归档的补扫
						}  else if (ARSConstants.PROCESS_FLAG_99.equals(progressFlag)) {//归档的批次需要选择主件流水做附件
							//工作流结束的补扫
							retMap.put("needPatchFlow", "true");
							addLogInfo("开始批次补扫,{批次号:" + batch.getBatchId() + "}");// 添加批次日志信息
						} else {
							retMap.put("needPatchFlow", "false");
							//只拿正常的批次2
							SunFlowService sunFlowService = new SunFlowServiceImpl();
							//userId wkiName  wkiPriid wkiUser  wkiID wkiState
							HashMap<String, String> hashMap = new HashMap<String, String>();
							hashMap.put("userId", BaseUtil.getLoginUser().getUserNo());
							hashMap.put("wkiPriid", String.valueOf(procinstId));
							hashMap.put("wkiState", "2");
							List<WorkItemInfo> worksByPriId = sunFlowService.getWorkItemByCondition(hashMap, batch.getAreaCode(), 5);
							if (worksByPriId != null && worksByPriId.size() > 0) {
								WorkItemInfo workItemInfo = worksByPriId.get(0);
								long workItemId = workItemInfo.getWorkItemId();
								//将当前用户插入的工作流可申请人
								ArrayList<String> arrayList = new ArrayList<String>();
								arrayList.add(BaseUtil.getLoginUser().getUserNo());
								sunFlowService.addApplyMan(ARSConstants.SUNFLOW_USER, workItemId, arrayList);
								//挂起之前进行申请
								sunFlowService.applyWorkItem(BaseUtil.getLoginUser().getUserNo(), workItemId);
								boolean voidFlag = sunFlowService.suspendWorkItem(BaseUtil.getLoginUser().getUserNo(), workItemId);
								if (voidFlag) {
									addLogInfo("开始批次补扫,{批次号:" + batch.getBatchId() + "}");// 添加批次日志信息
								} else {
									retMsg="创建补扫失败!";
									retCode="-1";
								}
							} else {
								//只拿自己挂起的
								hashMap.put("wkiState", "5");
								hashMap.put("wkiUser", BaseUtil.getLoginUser().getUserNo());
								List<WorkItemInfo> worksByPriIdForce = sunFlowService.getWorkItemByCondition(hashMap, batch.getAreaCode(), 5);
								if (worksByPriIdForce != null && worksByPriIdForce.size() > 0) {
									addLogInfo("开始批次补扫,{批次号:" + batch.getBatchId() + "}");// 添加批次日志信息
								} else {
									retMsg="该批次正在使用中!";
									retCode="-1";
								}
							}
						}
					}
				}


			}else{
				retCode="-1";
				retMsg="该批次已扫描提交";
			}	
		} else {
			retMap.put("voucherChk", voucherChk);
			retMap.put("inputDate",  DateUtil.getYMD());
			retCode="0";
		}
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(retCode);
		responseBean.setRetMsg(retMsg);

	}
	
}
