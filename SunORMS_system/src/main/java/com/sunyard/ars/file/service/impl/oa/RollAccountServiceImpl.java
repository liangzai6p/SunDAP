package com.sunyard.ars.file.service.impl.oa;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.dao.BizTranslistDao;
import com.sunyard.ars.common.dao.FlowMapper;
import com.sunyard.ars.common.dao.PadDao;
import com.sunyard.ars.common.dao.TmpBatchMapper;
import com.sunyard.ars.common.dao.TmpDataMapper;
import com.sunyard.ars.common.pojo.TmpBatch;
import com.sunyard.ars.common.util.SqlUtil;
import com.sunyard.ars.file.dao.ma.VoucherInfoMapper;
import com.sunyard.ars.file.dao.oa.CheckOffMapper;
import com.sunyard.ars.file.pojo.oa.CheckOff;
import com.sunyard.ars.file.service.oa.IRollAccountService;
import com.sunyard.ars.system.bean.sc.OrganData;
import com.sunyard.ars.system.bean.sc.SmTableField;
import com.sunyard.ars.system.bean.sc.TableDef;
import com.sunyard.ars.system.dao.sc.OrganDataMapper;
import com.sunyard.ars.system.dao.sc.SmTableFieldMapper;
import com.sunyard.ars.system.dao.sc.TableDefMapper;
import com.sunyard.cop.IF.bean.OrganZTreeBean;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.pojo.User;

import com.sunyard.license.B;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLDecoder;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Service("rollAccountService")
@Transactional
public class RollAccountServiceImpl extends BaseService implements IRollAccountService {

	@Resource
	private TmpBatchMapper tmpBatchMapper;

	@Resource
	private TmpDataMapper tmpDataMapper;

	@Resource
	private FlowMapper flowMapper;

	@Resource
	private VoucherInfoMapper voucherInfoMapper;

	@Resource
	private CheckOffMapper checkOffMapper;

	@Resource
	private OrganDataMapper organDataMapper;

	@Resource
	private TableDefMapper tableDefMapper;

	@Resource
	private SmTableFieldMapper smTableFieldMapper;
	@Resource
	private PadDao padDao;
	@Resource
	private BizTranslistDao bizTranslistDao;

	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		User user = BaseUtil.getLoginUser();
		Map reqMap = new HashMap();
		reqMap = requestBean.getSysMap();
		String oper_type = (String) reqMap.get("oper_type");

		if ("QUERYFORM".equalsIgnoreCase(oper_type)) {// 查询版面
			queryAllForm(requestBean, responseBean);
		} else if ("QUERYROLLDATE".equalsIgnoreCase(oper_type)) {// 获取扎帐日期
			queryRollDate(requestBean, responseBean);
		} else if ("QUERYARCHIVEDATE".equalsIgnoreCase(oper_type)) {// 获取归档日期
			queryArchiveDate(requestBean, responseBean);
		} else if ("LOADROLLDATE".equalsIgnoreCase(oper_type)) {// 获取扎帐信息
			loadRollDate(requestBean, responseBean);
		} else if ("LOADROLLINFO".equalsIgnoreCase(oper_type)) {// 加载机构柜员信息列表
			loadRollINFO(requestBean, responseBean);
		} else if ("FORCEREASON".equalsIgnoreCase(oper_type)) {// 查询强制轧账原因
			searchForceReason(requestBean, responseBean);
		} else if ("CHECKROLL".equalsIgnoreCase(oper_type)) {// 验证轧账信息
			checkRollByTeller(requestBean, responseBean);
		} else if ("NORMALROLL".equalsIgnoreCase(oper_type)) {// 轧账
			normalRoll(requestBean, responseBean);
		} else if ("BACKROLL".equalsIgnoreCase(oper_type)) {// 解除轧账
			backRoll(requestBean, responseBean);
		} else if ("FORCEROLL".equalsIgnoreCase(oper_type)) {// 强制轧账
			forceRoll(requestBean, responseBean);
		} else if ("LOADROLLINFOFORMAL".equalsIgnoreCase(oper_type)) {// 加载机构柜员信息列表for mal手工归档
			loadRollINFOFORMAL(requestBean, responseBean);
		} else if ("handArchive".equalsIgnoreCase(oper_type)) {// 手工归档
			handArchive(requestBean, responseBean);
		} else if ("handArchiveQuery".equalsIgnoreCase(oper_type)) {// 手工归档
			handArchiveQuery(requestBean, responseBean);
		} else if ("handArchiveInfoQuery".equalsIgnoreCase(oper_type)) {// 手工归档
			handArchiveInfoQuery(requestBean, responseBean);
		} else if ("getAutoCheckoffLists".equalsIgnoreCase(oper_type)) {// 自动轧账昨日的业务
			getAutoCheckoffLists(requestBean, responseBean);
		} else if ("getAutoCheckoffed".equalsIgnoreCase(oper_type)) {// 自动轧账昨日的业务
			getAutoCheckoffed(requestBean, responseBean);
		} else if ("beforeAutoCheckoff".equalsIgnoreCase(oper_type)) {// 自动轧账昨日的业务
			beforeAutoCheckoff(requestBean, responseBean);
		}
	}

	/**
	 * 加载机构柜员扎帐信息 for mal手工归档
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadRollINFOFORMAL(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		String occurDate = ((Map) requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map) requestBean.getParameterList().get(0)).get("siteNo").toString();
		String[] siteNos = siteNo.split(",");
		List<String> siteNoList = Arrays.asList(siteNos);

		String[] occurDates = occurDate.split(",");
		List<String> occurDateList = Arrays.asList(occurDates);

		HashMap condMap = new HashMap<String, String>();
		condMap.put("occurDateList", SqlUtil.getSumArrayList(occurDateList));
		condMap.put("siteNoList", SqlUtil.getSumArrayList(siteNoList));

		ArrayList list = new ArrayList();// 返回数据集合
		List dateInfoList = checkOffMapper.getArchiveRollInfo(condMap);
		for (Object o : dateInfoList) {
			HashMap malMap = new HashMap<String, String>();
			malMap.put("tellerNo", ((Map) o).get("TELLER_NO"));
			malMap.put("tellerName", ((Map) o).get("TELLER_NAME"));
			list.add(malMap);
		}

		retMap.put("returnList", list);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * 强制轧账
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void forceRoll(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		// 前台参数集合
		Map retMap = new HashMap();
		String occurDate = ((Map) requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map) requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map) requestBean.getParameterList().get(0)).get("operatorNo").toString();
		String forceReason = ((Map) requestBean.getParameterList().get(0)).get("forceReason").toString();
		String forceFlag = ((Map) requestBean.getParameterList().get(0)).get("forceFlag").toString();
		Map sysMap = requestBean.getSysMap();
		String lastEmpLogId = (String) sysMap.get("lastEmpLogId");
		forceReason = URLDecoder.decode(forceReason, "UTF-8");
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("forceFlag", forceFlag);
		condition.put("forceReason", forceReason);
		condition.put("occurDate", occurDate);
		condition.put("siteNo", siteNo);
		condition.put("operatorNo", operatorNo);
		condition.put("allCheckoff", "1");
		condition.put("checkWorker", BaseUtil.getLoginUser().getUserNo());
		condition.put("checkDate", BaseUtil.getCurrentDateStr());
		condition.put("empLogId", lastEmpLogId);
		int updateForceSelective = checkOffMapper.updateForceSelective(condition);
		retMap.put("count", updateForceSelective);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);

		// 添加日志
		String log = "强制扎帐！操作日期:" + occurDate + "交易机构:" + siteNo + "操作人员:" + operatorNo;
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_32, log);
	}

	/**
	 * 解除轧账
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void backRoll(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		// 前台参数集合
		Map retMap = new HashMap();
		int state = -1;
		String occurDate = ((Map) requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map) requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map) requestBean.getParameterList().get(0)).get("operatorNo").toString();
		CheckOff selectByPrimaryKey = checkOffMapper.selectByPrimaryKey(occurDate, siteNo, operatorNo);
		if (!"1".equals(selectByPrimaryKey.getAllCheckoff())) {// 直接不等于1
			retMap.put("count", 0);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			return;
		}
		;
		CheckOff checkOff = new CheckOff();
		checkOff.setOccurDate(occurDate);
		checkOff.setSiteNo(siteNo);
		checkOff.setOperatorNo(operatorNo);
		checkOff.setAllCheckoff("0");
		checkOff.setForceReason("");
		checkOff.setForceFlag("");
		checkOff.setCheckWorker(BaseUtil.getLoginUser().getUserNo());
		checkOff.setCheckDate(BaseUtil.getCurrentDateStr());
		int count = checkOffMapper.updateByPrimaryKeySelective(checkOff);
		retMap.put("count", count);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);

		// 添加日志
		String log = "解除日终扎帐，操作日期:" + occurDate + "交易机构:" + siteNo + "操作人员:" + operatorNo;
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_32, log);
	}

	/**
	 * 轧账
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void normalRoll(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		// 前台参数集合
		Map retMap = new HashMap();
		String occurDate = ((Map) requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map) requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map) requestBean.getParameterList().get(0)).get("operatorNo").toString();
		CheckOff checkOff = new CheckOff();
		checkOff.setOccurDate(occurDate);
		checkOff.setSiteNo(siteNo);
		checkOff.setOperatorNo(operatorNo);
		checkOff.setAllCheckoff("1");
		checkOff.setCheckWorker(BaseUtil.getLoginUser().getUserNo());
		checkOff.setCheckDate(BaseUtil.getCurrentDateStr());
		int count = checkOffMapper.updateByPrimaryKeySelective(checkOff);
		retMap.put("count", count);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);

		// 添加日志
		String log = "操作日期:" + occurDate + "交易机构:" + siteNo + "操作人员:" + operatorNo + ",扎帐。";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_32, log);
	}

	/**
	 * 检查轧账情况
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void checkRollByTeller(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		// 前台参数集合
		Map retMap = new HashMap();
		String occurDate = ((Map) requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map) requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map) requestBean.getParameterList().get(0)).get("operatorNo").toString();
		// 检查轧账状态
		int state = -1;// 未找到状态
		CheckOff checkOff = checkOffMapper.selectByPrimaryKey(occurDate, siteNo, operatorNo);
		if (null != checkOff) {
			if ("1".equals(checkOff.getAllCheckoff())) {
				state = 1; // 已轧账
			} else if ("2".equals(checkOff.getAllCheckoff())) {
				state = 2; // 已归档
			} else {
				if ("0".equals(checkOff.getAllCheckoff())) { // 未轧账
					state = 0;
					// 2.未轧账状态检查流水和图像未处理数
					HashMap condMap = new HashMap();
					condMap.put("occurDate", occurDate);
					condMap.put("siteNo", siteNo);
					condMap.put("operatorNo", operatorNo);
					condMap.put("needProcess", "1");
					condMap.put("isInvalid", "1");
					condMap.put("selfDelete", "0");
					condMap.put("checkFlag", "-1");
					int notDealImage = tmpDataMapper.selectNotDealImage(condMap);
					int notDealFlow = flowMapper.selectFlowToDeal(condMap);
					retMap.put("notDealImage", notDealImage);
					retMap.put("notDealFlow", notDealFlow);

					List bpTmpbatchList = tmpBatchMapper.selectBatchList(condMap);
					List<String> batchList = new ArrayList<String>();
					if (bpTmpbatchList != null && bpTmpbatchList.size() > 0) {
						String dataTableName = "";
						for (Object bpTmpbatchTb : bpTmpbatchList) {
							dataTableName = ((Map) bpTmpbatchTb).get("TEMP_DATA_TABLE").toString();
							batchList.add(BaseUtil.filterSqlParam(((Map) bpTmpbatchTb).get("BATCH_ID") + ""));
							String object = String.valueOf(((Map) bpTmpbatchTb).get("PROGRESS_FLAG"));
							Integer valueOf = Integer.valueOf(object);
							if (valueOf < 98) {
								state = 3;// 有批次还在流程中
							}
						}
						condMap.put("batchList", batchList);
						condMap.put("dataTb", BaseUtil.filterSqlParam(dataTableName));
						int patchCount = tmpDataMapper.selectPatchImageCount(condMap);
						int patchTaskCount = tmpDataMapper.selectPatchTaskImageCount(condMap);
						retMap.put("notDealScan", patchCount + patchTaskCount);// 未补扫数量
					} else {
						state = 4;// 批次未扫描
					}
				}
			}
		}
		retMap.put("state", state);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);

		// 添加日志
		String log = " 验证轧账信息，操作日期:" + occurDate + "交易机构:" + siteNo + "操作人员:" + operatorNo;
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_32, log);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void searchForceReason(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		// 前台参数集合
		Map retMap = new HashMap();
		String occurDate = ((Map) requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map) requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map) requestBean.getParameterList().get(0)).get("operatorNo").toString();

		int state = -1;
		// 检查轧账状态
		CheckOff checkOff = checkOffMapper.selectByPrimaryKey(occurDate, siteNo, operatorNo);
		if (checkOff != null) {
			// ALL_CHECKOFF,CHECK_WORKER,FORCE_REASON,FORCE_FLAG,CHECK_DATE
			if ("0".equals(checkOff.getAllCheckoff())) {
				state = 0;// 未轧账
			} else {
				if (!"".equals(checkOff.getForceFlag())) {
					String reason = "操作员：" + checkOff.getCheckWorker() + ";轧账日期：" + checkOff.getCheckDate() + ";强轧原因："
							+ (checkOff.getForceReason()==null?"无":checkOff.getForceReason());
					retMap.put("forceReason", reason);
					state = 2;// 强轧
				} else {
					state = 1;// 非强轧
				}
			}
		}
		retMap.put("state", state);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);

		// 添加日志
		String log = "查询扎帐原因，操作日期:" + occurDate + "交易机构:" + siteNo + "操作人员:" + operatorNo;
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_32, log);
	}

	/**
	 * 加载机构柜员扎帐信息
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadRollINFO(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		String occurDate = ((Map) requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map) requestBean.getParameterList().get(0)).get("siteNo").toString();
		String[] siteNos = siteNo.split(",");
		for (int i = 0; i < siteNos.length; i++) {
			String string = siteNos[i];
			if (string.indexOf("-") != -1) {
				siteNos[i] = string.substring(0, string.indexOf("-"));
			}
		}
		List<String> siteNoList = Arrays.asList(siteNos);

		HashMap condMap = new HashMap<String, String>();
		condMap.put("occurDate", occurDate);
		condMap.put("siteNoList", SqlUtil.getSumArrayList(siteNoList));

		ArrayList list = new ArrayList();// 返回数据集合
		List dateInfoList = checkOffMapper.getRollInfo(condMap);
		OrganZTreeBean oztBean = null;
		String rollSiteNo = "";
		for (Object o : dateInfoList) {
			oztBean = new OrganZTreeBean();
			if (!rollSiteNo.equals(((Map) o).get("SITE_NO").toString())) {// 不同机构需添加机构节点
				rollSiteNo = ((Map) o).get("SITE_NO").toString();
				oztBean.setId(((Map) o).get("SITE_NO") + "_" + occurDate);// 机构id加上_s防止机构号与柜员号相同
				oztBean.setpId(occurDate);// 机构上级节点为日期
				oztBean.setName(((Map) o).get("SITE_NO") + "-" + ((Map) o).get("ORGAN_NAME"));
				list.add(oztBean);
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("occurDate", occurDate);
				hashMap.put("siteNo", BaseUtil.filterSqlParam(""+((Map) o).get("SITE_NO")));
				hashMap.put("operatorNo", BaseUtil.filterSqlParam(""+((Map) o).get("OPERATOR_NO")));

				int uncheckImgCount = tmpBatchMapper.getUncheckImgCount(hashMap);
				int unchechFlowCount = flowMapper.getUnchechFlowCount(hashMap);

				oztBean = new OrganZTreeBean();
				oztBean.setId(((Map) o).get("OPERATOR_NO").toString());
				oztBean.setpId(((Map) o).get("SITE_NO") + "_" + occurDate);
				oztBean.setName(((Map) o).get("OPERATOR_NO") + "-" + ((Map) o).get("TELLER_NAME") + " 未处理图像数-"
						+ uncheckImgCount + "张 未勾对流水数-" + unchechFlowCount);
				list.add(oztBean);
			} else {// 相同机构添加柜员节点
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				hashMap.put("occurDate", occurDate);
				hashMap.put("siteNo", BaseUtil.filterSqlParam(""+((Map) o).get("SITE_NO")));
				hashMap.put("operatorNo", BaseUtil.filterSqlParam(""+((Map) o).get("OPERATOR_NO")));

				int uncheckImgCount = tmpBatchMapper.getUncheckImgCount(hashMap);
				int unchechFlowCount = flowMapper.getUnchechFlowCount(hashMap);

				oztBean = new OrganZTreeBean();
				oztBean.setId(((Map) o).get("OPERATOR_NO").toString());
				oztBean.setpId(((Map) o).get("SITE_NO") + "_" + occurDate);
				oztBean.setName(((Map) o).get("OPERATOR_NO") + "-" + ((Map) o).get("TELLER_NAME") + " 未处理图像数-"
						+ uncheckImgCount + "张 未勾对流水数-" + unchechFlowCount);
				list.add(oztBean);
			}
		}
		retMap.put("zNodes", list);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}

	/**
	 * 加载日期节点
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadRollDate(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		String occurDate = ((Map) requestBean.getParameterList().get(0)).get("occurDate").toString();
		String[] occurDates = occurDate.split(",");

		ArrayList list = new ArrayList();
		OrganZTreeBean oztBean = null;
		for (int i = 0; i < occurDates.length; i++) {
			oztBean = new OrganZTreeBean();
			oztBean.setId(occurDates[i]);
			oztBean.setpId("0");
			oztBean.setName(occurDates[i]);
			oztBean.setOpen("false");
			oztBean.setIsParent("true");
			list.add(oztBean);
		}
		retMap.put("zNodes", list);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryRollDate(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		ArrayList list = new ArrayList();
		OrganZTreeBean oztBean = new OrganZTreeBean();
		oztBean.setId("1");
		oztBean.setpId("0");
		oztBean.setName("全部");
		oztBean.setOpen("true");
		list.add(oztBean);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		List dateInfoList = checkOffMapper.getCheckOffDate(hashMap);
		for (Object o : dateInfoList) {
			oztBean = new OrganZTreeBean();
			oztBean.setId(((Map) o).get("OCCUR_DATE").toString());
			oztBean.setpId("1");
			oztBean.setName(((Map) o).get("OCCUR_DATE").toString());
			list.add(oztBean);
		}
		retMap.put("oazNodes", list);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryArchiveDate(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		String siteNo = ((Map) requestBean.getParameterList().get(0)).get("siteNo").toString();
		ArrayList list = new ArrayList();
		OrganZTreeBean oztBean = new OrganZTreeBean();
		oztBean.setId("1");
		oztBean.setpId("0");
		oztBean.setName("全部");
		oztBean.setOpen("true");
		list.add(oztBean);

		List dateInfoList = checkOffMapper.getArchiveDate(siteNo);
		List<HashMap<String, Object>> archivedDate = checkOffMapper.getAllArchivedDate(siteNo);
		ArrayList<String> arrayList = new ArrayList<String>();
		for (HashMap<String, Object> hashMap : archivedDate) {
			String string = hashMap.get("CHECK_STATE").toString();
			if (!string.equals("0")) {
				continue;
			}
			arrayList.add(hashMap.get("OCCUR_DATE").toString());
		}
		for (Object o : dateInfoList) {
			if (!((Map) o).get("CHECK_STATE").toString().equals("0")) {
				continue;
			}
			if (arrayList.contains(((Map) o).get("OCCUR_DATE").toString())) {
				continue;
			}
			oztBean = new OrganZTreeBean();
			oztBean.setId(((Map) o).get("OCCUR_DATE").toString());
			oztBean.setpId("1");
			oztBean.setName(((Map) o).get("OCCUR_DATE").toString());
			list.add(oztBean);
		}
		retMap.put("oazNodes", list);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}

	/**
	 * 获取所有版面
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryAllForm(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map retMap = new HashMap();

		String organ_no = ((Map) requestBean.getParameterList().get(0)).get("organ_no").toString();

		Map<String, String> formNameMap = new HashMap<>();
		HashMap condMap = new HashMap<String, String>();
		//condMap.put("organ_no", "");
		if(BaseUtil.isBlank(organ_no)){
			organ_no = BaseUtil.getLoginUser().getOrganNo();
		}
		condMap.put("organ_no", organ_no);
		List voucheList = BaseUtil.convertListMapKeyValue(voucherInfoMapper.selectFormNameMap(condMap));
		for (int i = 0; i < voucheList.size(); i++) {
			Map voucheMap = (Map) voucheList.get(i);
			formNameMap.put((String) voucheMap.get("voucher_name"), "1");
		}
		// 所有凭证版面
		List tempAllInfo = BaseUtil.convertListMapKeyValue(voucherInfoMapper.selectAllFormInfo());

		List formList = new ArrayList();
		for (Object objs : tempAllInfo) {
			String formName = (String) ((Map) objs).get("voucher_name");
			if (null != formNameMap.get(formName)) {// 只有配置的版面才能显示
				formList.add(objs);
			}
		}

		retMap.put("formArr", formList.toArray());
		retMap.put("maFlag", "querySuccess");
		retMap.put("maMsg", "查询成功！");
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}

	// varsir 手动归档
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void handArchive(RequestBean requestBean, ResponseBean responseBean) throws Exception {

		Map retMap = new HashMap();
		String occurDate = ((Map) requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map) requestBean.getParameterList().get(0)).get("siteNo").toString();
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("occurDate", occurDate);
		condition.put("siteNo", siteNo);
		List<CheckOff> archiveCheckoff = checkOffMapper.getArchiveCheckoff(condition);
		if (archiveCheckoff == null) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("未找到归档信息,或该批次尚未完成");
			return;
		}
		// 检查机构有没有
		OrganData organData = organDataMapper.selectByPrimaryKey(siteNo);
		if (organData == null) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("机构数据表未定义，请检查！");
			return;
		}
		TableDef batchTable = tableDefMapper.selectByPrimaryKey(new BigDecimal(BaseUtil.filterSqlParam(organData.getTmpbatchTbId()+"")));
		String batchTableName = batchTable.getTableName();
		TableDef flowTable = tableDefMapper.selectByPrimaryKey(new BigDecimal(BaseUtil.filterSqlParam(organData.getFlowTbId()+"")));
		String flowTableName = flowTable.getTableName();
		TableDef batchDataTable = tableDefMapper.selectByPrimaryKey(new BigDecimal(BaseUtil.filterSqlParam(organData.getTmpdataTbId()+"")));
		String batchDataTableName = batchDataTable.getTableName();
		String hisBatchDataTbName = batchDataTableName + "_HIS";
		String hisFlowTbName = flowTableName + "_HIS";
		boolean flag = true;
		for (CheckOff checkoff : archiveCheckoff) {

			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("occurDate", BaseUtil.filterSqlParam(checkoff.getOccurDate()));
			hashMap.put("operatorNo", BaseUtil.filterSqlParam(checkoff.getOperatorNo()));
			hashMap.put("siteNo", BaseUtil.filterSqlParam(checkoff.getSiteNo()));
			if (checkoff.getAllCheckoff().equals("2")) {
				logger.info(BaseUtil.filterLog("业务日期：" + checkoff.getOccurDate() + "网点：" + checkoff.getSiteNo() + "柜员："
						+ checkoff.getOperatorNo() + "已归档，无需再次归档！"));
				continue;
			}
			logger.info(BaseUtil.filterLog("业务日期：" + checkoff.getOccurDate() + "网点：" + checkoff.getSiteNo() + "柜员："
					+ checkoff.getOperatorNo() + "开始归档！"));
			if (checkOffMapper.checkOnLock(hashMap) <= 0) {
				logger.info(BaseUtil.filterLog("业务日期：" + checkoff.getOccurDate() + "网点：" + checkoff.getSiteNo() + "柜员："
						+ checkoff.getOperatorNo() + "正在被处理,无法归档"));
				continue;
			}
			try {
				hashMap.put("progressFlag", "99"); // 有值则是小于该值
				int hasArchiveBatch = tmpBatchMapper.hasArchiveBatch(hashMap);
				if (hasArchiveBatch > 0) {
					List<TmpBatch> archiveBatchs = tmpBatchMapper.getArchiveBatchs(hashMap);
					if (null != archiveBatchs && archiveBatchs.size() > 0) {
						logger.info("共有" + archiveBatchs.size() + "笔需要归档  " + "开始归档！");
						ArrayList<String> batchIds = new ArrayList<String>();
						for (int i = 0; i < archiveBatchs.size(); i++) {
							batchIds.add(BaseUtil.filterSqlParam(archiveBatchs.get(i).getBatchId()));
						}
						boolean b = false;
						int vouh = tmpBatchMapper.isVouh(batchIds);
						// 查询当前归档的是否是凭证
						if (vouh > 0) {
							// 是否有未走完流程的凭证批次，有则不能归档
							List<TmpBatch> vouh2 = tmpBatchMapper.getVouh(hashMap);
							b = vouh2.size() > 0 ? true : false;
							if (b) {
								logger.info(BaseUtil.filterLog(checkoff.getOperatorNo()) + "柜员、日期下仍有未完成批次！");
								continue;
							}
						}
						// 对数据表归档
						boolean batchDataArchveFlag = dataArchive(batchDataTable, checkoff, archiveBatchs);
						if (batchDataArchveFlag) {
							// 影像表插入到历史表了
							logger.info("数据表:" + BaseUtil.filterLog(hisBatchDataTbName) + "归档成功!");
							boolean flowArchveFlag = true;
							if (vouh > 0) {
								// 对流水表归档
								flowArchveFlag = dataArchive(flowTable, checkoff, null);
								if (flowArchveFlag) {
									// 流水表插入到历史表中了
									logger.info("流水表" + BaseUtil.filterLog(hisFlowTbName) + "归档成功！");
								} else {
									flag = false;
									// 失败要回滚两张表，这时候只需要删除历史表就可以了
									logger.info("流水表" + BaseUtil.filterLog(hisFlowTbName) + "归档失败，进行历史表数据回滚！");
									int deletehisBatchData = tmpBatchMapper.deletehisBatchData(batchIds);
									logger.info("批次历史表回滚成功！");
									int deletehisflowData = flowMapper.deletehisflowData(hashMap);
									logger.info("流水历史表回滚成功！");
									continue;
								}
							}
							if (flowArchveFlag) {
								// TODO再次判断是否还存在未归档并且不在当前归档范围内的，却需要归档的有效批次
								boolean updateBatchFlag = true;
								try {
									int updateBatchArchive = tmpBatchMapper.updateBatchArchive(batchIds);
								} catch (Exception e) {
									updateBatchFlag = false;
								}
								if (updateBatchFlag) {
									logger.info("批次信息更新成功，归档完成！");
								} else {
									flag = false;
									logger.info("批次信息更新失败，归档失败，进行数据回滚！");
									if (vouh > 0) {// 如果对流水表进行归档，则需一起删除流水表数据
										int deletehisBatchData = tmpBatchMapper.deletehisBatchData(batchIds);
										logger.info("批次历史表回滚成功！");
										int deletehisflowData = flowMapper.deletehisflowData(hashMap);
										logger.info("流水历史表回滚成功！");
									} else {// 否则只删除影像数据表数据
										tmpBatchMapper.deletehisBatchData(batchIds);
									}
									continue;
								}
								if (updateBatchFlag) {
									boolean updateCheckoffFlag = true;
									try {
										hashMap.put("archiveDate", BaseUtil.getCurrentDateStr());
										int updateCheckoffArchive = checkOffMapper.updateCheckoffArchive(hashMap);
									} catch (Exception e) {
										updateCheckoffFlag = false;
									}
									if (updateCheckoffFlag) {
										logger.info("扎帐信息更新成功，归档完成！");
									} else {
										flag = false;
										logger.info("扎帐信息更新失败，进行数据回滚！");
										tmpBatchMapper.tmpbatchBackArchive(batchIds);
										logger.info("批次回滚成功！");
										if (vouh > 0) {// 如果对流水表进行归档，则需一起删除流水表数据
											int deletehisBatchData = tmpBatchMapper.deletehisBatchData(batchIds);
											logger.info("批次历史表回滚成功！");
											int deletehisflowData = flowMapper.deletehisflowData(hashMap);
											logger.info("流水历史表回滚成功！");
										} else {// 否则只删除影像数据表数据
											tmpBatchMapper.deletehisBatchData(batchIds);
										}
										continue;
									}
									try {
										// int updatePadArthiveData = padDao.updateArthiveData(hashMap);
										int updateBizArthiveData = bizTranslistDao.updateArthiveData(hashMap);
									} catch (Exception e) {
										logger.info("业务日期：" + checkoff.getOccurDate() + "网点：" + checkoff.getSiteNo()
												+ "柜员：" + checkoff.getOperatorNo() + "PAD表,BIZ表归档失败！");
										continue;
									}
								}
							}
						} else {
							flag = false;
							logger.info("数据表" + BaseUtil.filterLog(hisBatchDataTbName) + "归档失败！进行数据回滚");
							tmpBatchMapper.deletehisBatchData(batchIds);
							continue;
						}
					} else {
						flag = false;
						logger.info(BaseUtil.filterLog("业务日期：" + checkoff.getOccurDate() + "网点：" + checkoff.getSiteNo() + "柜员："
								+ checkoff.getOperatorNo() + "未找到有效归档批次归档失败！"));
						continue;
					}
				} else {
					hashMap.put("archiveDate", BaseUtil.getCurrentDateStr());
					checkOffMapper.updateCheckoffArchive(hashMap);
				}
				logger.info(BaseUtil.filterLog("业务日期：" + checkoff.getOccurDate() + "网点：" + checkoff.getSiteNo() + "柜员："
						+ checkoff.getOperatorNo() + "归档成功"));
			} finally {
				checkOffMapper.checkUnLock(hashMap);
			}

		}
		if (flag) {
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("归档成功");
		} else {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("归档失败");
		}
	}

	private boolean dataArchive(TableDef tableDef, CheckOff checkoff, List<TmpBatch> archiveBatchs) throws Exception{
		List<SmTableField> smTableFields = null;
		boolean executeFlag = true;
		smTableFields = smTableFieldMapper.getTableFieldByTableIdDao(tableDef.getTableId());
		if (smTableFields == null || smTableFields.size() == 0) {
			logger.info(BaseUtil.filterLog("请检查表字段定义，表ID:" + tableDef.getTableId() + " 表名:" + tableDef.getTableName()));
			return false;
		}
		StringBuffer insertSql = new StringBuffer();
		StringBuffer insertFieldSql = new StringBuffer();
		insertSql.append("INSERT INTO " + tableDef.getTableName() + "_HIS" + " ");
		for (int i = 0; i < smTableFields.size(); i++) {
			insertFieldSql.append(smTableFields.get(i).getFieldName());
			// 如果不是最后一位就加逗号
			if (i != smTableFields.size() - 1) {
				insertFieldSql.append(",");
			}
		}
		insertSql.append("(" + insertFieldSql + ") ");

		insertSql.append("SELECT ");

		insertSql.append(insertFieldSql + " ");

		insertSql.append("FROM " + tableDef.getTableName() + " ");

		insertSql.append("WHERE ");

		switch (Integer.parseInt(tableDef.getTableType())) {
		case 1:
			break;
		// 影像表
		case 2:
			if (archiveBatchs.size() > 0) {
				insertSql.append("BATCH_ID IN(");

				for (int i = 0; i < archiveBatchs.size(); i++) {
					insertSql.append("'" + archiveBatchs.get(i).getBatchId() + "'");

					// 如果不是最后一位就加逗号
					if (i != archiveBatchs.size() - 1) {
						insertSql.append(",");

					}
				}
				insertSql.append(")");

			} else {
				return false;
			}
			break;
		// 流水表
		case 3:
			insertSql.append("OCCUR_DATE='" + checkoff.getOccurDate() + "' AND ");

			insertSql.append("SITE_NO='" + checkoff.getSiteNo() + "' AND ");

			insertSql.append("OPERATOR_NO='" + checkoff.getOperatorNo() + "'");

			break;
		default:
			break;
		}
		String sql = insertSql.toString();

		logger.info("归档sql语句   " + BaseUtil.filterLog(sql));
		try {
			int dataArchive = tableDefMapper.dataArchive(sql);
			return true;
		} catch (Exception e) {
			logger.info("归档sql语句执行异常   " + sql);
			return false;
		}
	}

	private void handArchiveQuery(RequestBean requestBean, ResponseBean responseBean) {
		Map retMap = new HashMap();
		String occurDate = ((Map) requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map) requestBean.getParameterList().get(0)).get("siteNo").toString();
		ArrayList<String> siteNos = new ArrayList<String>();
		siteNos.add(siteNo);
		ArrayList<String> occurDates = new ArrayList<String>();
		occurDates.add(occurDate);
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("occurDates", occurDates);
		condition.put("siteNos", siteNos);
		List<HashMap<String, Object>> archiveCheckoff = checkOffMapper.getOrganArchiveInfo(condition);
		retMap.put("returnList", archiveCheckoff);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	private void handArchiveInfoQuery(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		String occurDates = ((Map) requestBean.getParameterList().get(0)).get("occurDates").toString();
		String occurDatee = ((Map) requestBean.getParameterList().get(0)).get("occurDatee").toString();
//		String status = ((Map) requestBean.getParameterList().get(0)).get("status").toString();
		List siteNos = (List) ((Map) requestBean.getParameterList().get(0)).get("siteNo");
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("occurDates", occurDates);
		condition.put("occurDatee", occurDatee);
		condition.put("siteNos", siteNos);
		List<HashMap<String, Object>> archiveInfo = checkOffMapper.getArchiveInfo(condition);

		for (HashMap<String, Object> hashMap : archiveInfo) {
			if (!hashMap.get("CHECK_STATE").toString().equals("0")) {
				continue;
			}
			String odate = BaseUtil.filterSqlParam(hashMap.get("OCCUR_DATE").toString());
			String osite = BaseUtil.filterSqlParam(hashMap.get("SITE_NO").toString());
			Map archivedDate = checkOffMapper.getArchivedDate(odate, osite);
			String checkdateString;
			if (archivedDate == null || archivedDate.get("MAXDATE") == null) {
//					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//				 	try {
//						Date parse = format.parse(odate);
//						GregorianCalendar calendar = new GregorianCalendar();
//						calendar.setTime(parse);
//						calendar.add(java.util.Calendar.MONTH, 1);
//						Date time = calendar.getTime();
//						checkdateString=format.format(time);
//					} catch (ParseException e) {
//						checkdateString ="N/A";
//					}
				checkdateString = "N/A";
			} else {
				checkdateString = archivedDate.get("MAXDATE").toString();
			}
			;
			hashMap.put("ARTHIVE_DATE", checkdateString);
		}
		retMap.put("returnList", archiveInfo);
		responseBean.setRetMap(retMap);
		if (archiveInfo.size() == 0) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("未检索到信息");
			return;
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	private void getAutoCheckoffLists(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		String occurDate = ((Map) requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map) requestBean.getParameterList().get(0)).get("siteNo").toString();
		List<String> siteNos = Arrays.asList(siteNo.split(","));
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("occurDate", occurDate);
		condition.put("siteNos", siteNos);
		List<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		for (String string : siteNos) {
			condition.put("siteNo", string);
			List<HashMap<String, Object>> normalAutoCheckoff = checkOffMapper.getNormalAutoCheckoff(condition);
			if (normalAutoCheckoff != null && normalAutoCheckoff.size() > 0) {
				arrayList.addAll(normalAutoCheckoff);
			}
		}
		System.out.println(arrayList.size());
		List<HashMap<String, Object>> resultList = checkOffMapper.getUncheckCheckoff(condition);
		System.out.println("11" + resultList.size());
		resultList.removeAll(arrayList);
		System.out.println(resultList.size());
		for (HashMap<String, Object> hashMap : resultList) {
			condition.clear();
			int FLAG = 0;
			condition.put("occurDate", BaseUtil.filterSqlParam(hashMap.get("OCCUR_DATE")+""));
			condition.put("siteNo",BaseUtil.filterSqlParam( hashMap.get("SITE_NO")+""));
			condition.put("operatorNo", BaseUtil.filterSqlParam(hashMap.get("OPERATOR_NO")+""));
			// 检查所有流水
			int allFlowCount = flowMapper.selectDealCount(condition);// 所有流水
			int uncheckFlowCount = 0;
			condition.put("checkFlag", "-1");
			uncheckFlowCount = flowMapper.selectDealCount(condition);
			condition.put("selfDelete", "0");
			condition.put("isInvalid", "1");
			condition.put("needProcess", "1");
			int unDealImage = 0;
			unDealImage = tmpDataMapper.selectNotDealImage(condition);// 未处理图像
			if (allFlowCount == 0) {
				// 无流水
				FLAG = 1;
			} else {
				// 有流水
				int allImage = tmpDataMapper.selectNotDealImage(condition);// 所有图像
				// 未处理流水
				if (allImage == 0) {
					// 无图像
					FLAG = 2;
				} else {
					// 有图像
					if (unDealImage != 0 && uncheckFlowCount != 0) {
						// 有剩余图像 有剩余流水无法轧账
						FLAG = 4;
					} else {
						FLAG = 3;
					}
				}

			}
			List<TmpBatch> vouh = tmpBatchMapper.getVouh(condition);
			boolean b = vouh.size() > 0 ? true : false;
			if (b) {
				FLAG = 5;
			}
			hashMap.put("FLAG", FLAG);
			hashMap.put("UN_FLOW", uncheckFlowCount);
			hashMap.put("UN_IMG", unDealImage);
			String id = "" + FLAG + "_" + (uncheckFlowCount) + "_" + occurDate + "_" + hashMap.get("SITE_NO") + "_"
					+ hashMap.get("OPERATOR_NO");
			hashMap.put("FORCE_REASON", id);
			hashMap.put("FORCE_FLOWQB", id);
		}
		Map retMap = new HashMap();
		retMap.put("returnList", resultList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
		//强扎帐页面，查询日志
		//添加日志
		String log = "强制扎帐页面查询信息，查询交易日期:" + occurDate +"交易机构:" + siteNo;
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_32, log);
	}

	private void getAutoCheckoffed(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		List<Object> parameterList = requestBean.getParameterList();
		Map sysMap = requestBean.getSysMap();
		String lastEmpLogId = (String) sysMap.get("lastEmpLogId");
		HashMap<String, Object> condition = new HashMap<String, Object>();
		if (parameterList.size() <= 0) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("未提交柜员强制轧账信息");
			return;
		}
		String Flag;
		String check;
		String occurDate;
		String siteNo;
		String operatorNo;
		String forceReason;
		String forcedFlag;
		ArrayList<String> arrayList = new ArrayList<String>();
		for (Object object : parameterList) {
			check = (String) object;
			String[] split = check.split("_");
			Flag = split[0];
			forcedFlag = split[1];
			occurDate = split[2];
			siteNo = split[3];
			operatorNo = split[4];
			forceReason = split[5];
			forceReason = URLDecoder.decode(forceReason, "UTF-8");
			condition.put("forceFlag", forcedFlag);
			condition.put("forceReason", forceReason);
			condition.put("occurDate", occurDate);
			condition.put("siteNo", siteNo);
			condition.put("operatorNo", operatorNo);
			condition.put("allCheckoff", "1");
			condition.put("checkWorker", BaseUtil.getLoginUser().getUserNo());
			condition.put("checkDate", BaseUtil.getCurrentDateStr());
			condition.put("empLogId", lastEmpLogId);
			checkOffMapper.updateForceSelective(condition);
			
			//日志信息
			String log = "操作日期:" + occurDate + "交易机构:" + siteNo + "操作人员:" + operatorNo
					+ ",强制扎帐原因:" + forceReason;
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_32, log);
		}
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("轧账成功");
	}

	private void beforeAutoCheckoff(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		String occurDate = ((Map) requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map) requestBean.getParameterList().get(0)).get("siteNo").toString();
		List<String> siteNos = Arrays.asList(siteNo.split(","));
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("occurDate", occurDate);
		condition.put("checkWorker",BaseUtil.getLoginUser().getUserNo());
		int count = 0;
		for (String string : siteNos) {
			condition.put("siteNo", string);
			int updateNormalAutoCheckoff = checkOffMapper.updateNormalAutoCheckoff(condition);
			count += updateNormalAutoCheckoff;
		}
		Map retMap = new HashMap();
		retMap.put("count", count);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
		//添加日志
		String log = "一键扎帐，扎帐昨日的任务。操作日期：" + occurDate + "操作机构" + siteNo;
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_32, log);
	}
}
