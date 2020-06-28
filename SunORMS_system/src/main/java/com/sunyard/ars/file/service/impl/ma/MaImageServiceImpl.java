package com.sunyard.ars.file.service.impl.ma;

import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.util.SqlUtil;
import com.sunyard.ars.common.util.UUidUtil;
import com.sunyard.ars.file.pojo.ma.MaParameterBean;
import com.sunyard.ars.file.service.ma.IMaImageService;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.dao.FlowMapper;
import com.sunyard.ars.common.dao.TmpBatchMapper;
import com.sunyard.ars.common.dao.TmpDataMapper;
import com.sunyard.ars.common.pojo.TmpData;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

@Service("maImageService")
@Transactional
public class MaImageServiceImpl extends BaseService implements IMaImageService {
	@Resource
	private TmpBatchMapper tmpBatchMapper;

	@Resource
	private TmpDataMapper tmpDataMapper;

	@Resource
	private FlowMapper flowMapper;

	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
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
		if ("PSLEVELUP".equalsIgnoreCase(oper_type)) {// 设为主件
			setPsLevelUp(requestBean, responseBean);
		} else if ("PSLEVELDOWN".equalsIgnoreCase(oper_type)) {// 设为附件
			setPsLevelDown(requestBean, responseBean);
		} else if ("HANDLEUP".equalsIgnoreCase(oper_type)) {// 手工指定主件
			doHandleUp(requestBean, responseBean);
		} else if ("MARKDELETE".equalsIgnoreCase(oper_type)) {// 标记删除
			markDelete(requestBean, responseBean);
		} else if ("BACKDELETE".equalsIgnoreCase(oper_type)) {// 撤销标记删除
			backDelete(requestBean, responseBean);
		} else if ("MARKSCANAGAIN".equalsIgnoreCase(oper_type)) {// 标记重扫
			markScanAgain(requestBean, responseBean);
		} else if ("BACKSCANAGAIN".equalsIgnoreCase(oper_type)) {// 撤销标记重扫
			backScanAgain(requestBean, responseBean);
		} else if ("ADDSCANTASK".equalsIgnoreCase(oper_type)) {// 添加补扫任务
			addScanTask(requestBean, responseBean);
		} else if ("GETSCANTASK".equalsIgnoreCase(oper_type)) {// 获取补扫任务
			getScanTaskList(requestBean, responseBean);
		} else if ("FORCEPASS".equalsIgnoreCase(oper_type)) {// 强过
			imageForcePass(requestBean, responseBean);
		} else if ("PSLEVELUPBYONCE".equalsIgnoreCase(oper_type)) {// 一键设主件
			setPsLevelUpByOnce(requestBean, responseBean);
		} else if ("PSLEVELDOWNBYONCE".equalsIgnoreCase(oper_type)) {// 一键设附件
			setPsLevelDownByOnce(requestBean, responseBean);
		} else if ("COPYIMAGE".equalsIgnoreCase(oper_type)) {// 复制影像
			copyImage(requestBean, responseBean);
		} else if ("UPDATEFORMNAME".equalsIgnoreCase(oper_type)) {// 修改版面
			updateFormName(requestBean, responseBean);
		} else if ("backForcePass".equalsIgnoreCase(oper_type)) {// 修改版面
			backForcePass(requestBean, responseBean);
		} else if ("updateErrorFlag".equals(oper_type)) {
			updateErrorFlag(requestBean, responseBean);
		}
	}

	/**
	 * 修改版面信息
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateFormName(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		MaParameterBean tmpData = (MaParameterBean) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("formName", tmpData.getFormName());
		condMap.put("processState", "2");
		condMap.put("serialNo", tmpData.getSerialNo());
		tmpDataMapper.updateTmpDataInfo(condMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
	}

	/**
	 * 复制影像功能
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void copyImage(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		MaParameterBean tmpData = (MaParameterBean) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("batchId", tmpData.getBatchId());
		condMap.put("addCount", 1);
		tmpBatchMapper.updateTotalPage(condMap);
		// 构造条件参数
		condMap.clear();
		String uuid = UUidUtil.uuid();
		condMap.put("serialNo", tmpData.getSerialNo());
		condMap.put("copySerialNo", uuid);
		condMap.put("inccodeinBatch", tmpData.getInccodeinBatch());
		tmpDataMapper.insertCopyImage(condMap);
		TmpData tmpdata = tmpDataMapper.selectByPrimaryKey(uuid);
		retMap.put("tmpdata", tmpdata);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		
		//日志
		String log = "负责图像序号为" + tmpData.getSerialNo() + "批内码为" + tmpData.getInccodeinBatch()+
				"插入图像序号为" + uuid + "的数据";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_29, log);
	}

	/**
	 * 一键设附件功能
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setPsLevelDownByOnce(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		MaParameterBean tmpData = (MaParameterBean) requestBean.getParameterList().get(0);
		Map retMap = new HashMap();
		// 构造条件参数
		HashMap condMap = new HashMap();
		// 是否设置了默认附件名
		//String sDefaultName = ARSConstants.SLAVE_DEFAULT_NAME;
        String sDefaultName = ARSConstants.SYSTEM_PARAMETER.get("SLAVE_DEFAULT_NAME").getParamValue();
        if (null == sDefaultName || "".equals(sDefaultName.trim())) {
			responseBean.setRetCode("1");
			responseBean.setRetMsg("该模块未设置附件默认版面名,字段SLAVE_DEFAULT_NAME!");
		} else {
			// 构造条件参数
			condMap.put("checkFlag", "1");
			condMap.put("serialNo", tmpData.getSerialNo());

			// 如果是主件，检查是否勾对过流水
			int dealedCount = tmpDataMapper.selectImgCount(condMap);
			if (dealedCount > 0) {
				// 勾对过的凭证释放原先勾对的流水
				condMap.clear();
				condMap.put("checkFlag", "-1");
				condMap.put("lserialNo", "");
				condMap.put("serialNo", tmpData.getSerialNo());
				condMap.put("operatorNo", tmpData.getOperatorNo());
				condMap.put("siteNo", tmpData.getSiteNo());
				condMap.put("occurDate", tmpData.getOccurDate());
				String flowId = tmpDataMapper.selectByPrimaryKey(tmpData.getSerialNo()).getFlowId();
				flowId = BaseUtil.filterSqlParam(flowId);
				if (flowId.indexOf(",") > 0) {
					List<String> flowList = Arrays.asList(flowId.split(","));
					List<List<String>> list = SqlUtil.getSumArrayList(flowList);
					condMap.remove("flowId");
					condMap.put("flowList", list);
				} else {
					condMap.put("flowId", flowId);
				}
				flowMapper.updateFlow(condMap);
			}
			// 将该凭证及其附件信息全部登记为附件
			condMap.clear();
			condMap.put("batchId", tmpData.getBatchId());
			condMap.put("serialNo", tmpData.getSerialNo());
			condMap.put("inccodeinBatch", tmpData.getInccodeinBatch());
			condMap.put("processState", "2");
			condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
			condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
			condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
			tmpDataMapper.updatePsLevelDownAll(condMap);
			// 更新版面名称
			condMap.clear();
			condMap.put("serialNo", tmpData.getSerialNo());
			condMap.put("formName", sDefaultName);
			tmpDataMapper.updateTmpDataInfo(condMap);
			retMap.put("sDefaultName", sDefaultName);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			
			// 添加日志
			String log = "图像序号为" + tmpData.getSerialNo() + "的凭证，一键设置为附件!";
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_30, log);
		}

	}

	/**
	 * 一键设主件功能
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setPsLevelUpByOnce(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		MaParameterBean tmpData = (MaParameterBean) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		Map retMap = new HashMap();
		// 是否设置了默认主件名
		//String pDefaultName = ARSConstants.PRIMARY_DEFAULT_NAME;
        String pDefaultName = ARSConstants.SYSTEM_PARAMETER.get("PRIMARY_DEFAULT_NAME").getParamValue();
		if (null == pDefaultName || "".equals(pDefaultName.trim())) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("该模块未设置主件默认版面名,字段PRIMARY_DEFAULT_NAME!");
		} else {
			// 检查是否是附件状态，返回所属主件批内码
			condMap.put("psLevel", "0");
			condMap.put("serialNo", tmpData.getSerialNo());
			List dataList = tmpDataMapper.selectBySelective(condMap);
			if (dataList != null && dataList.size() > 0) {
				condMap.clear();
				// 传递设主件需要的参数信息
				condMap.put("psLevel", "1");
				condMap.put("primaryInccodein", 0);
				condMap.put("checkFlag", "-1");
				condMap.put("flowId", "");
				condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
				condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
				condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
				condMap.put("formName", pDefaultName);
				condMap.put("processState", "2");// 业务审核未置为1
				condMap.put("serialNo", tmpData.getSerialNo());
				tmpDataMapper.updateTmpDataInfo(condMap);
				// 如果操作附件指定了主件号，更新其后面相同主件号的附件 指定附件号为当前设置的主件
				int primaryInccodein = ((BigDecimal) ((Map) dataList.get(0)).get("PRIMARY_INCCODEIN")).intValue();
				if (primaryInccodein > 0) {
					condMap.clear();
					condMap.put("batchId", tmpData.getBatchId());
					condMap.put("inccodeinBatch", tmpData.getInccodeinBatch());
					condMap.put("primaryInccodein", Integer.parseInt(BaseUtil.filterSqlParam(String.valueOf(primaryInccodein))));
					condMap.put("checkFlag", "0");
					condMap.put("flowId", "");
					tmpDataMapper.updatePsLevelUp(condMap);
				}
				retMap.put("pDefaultName", pDefaultName);
				responseBean.setRetMap(retMap);
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
				
				// 添加日志
				String log = "图像序号为" + tmpData.getSerialNo() + "的凭证，一键设置为主件!";
				addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_30, log);
				
			} else {
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("该凭证已经是主件,请刷新记录!");
			}
		}
	}

	/**
	 * 凭证强过功能
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void imageForcePass(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		MaParameterBean tmpData = (MaParameterBean) requestBean.getParameterList().get(0);

		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("checkFlag", "1");
		condMap.put("serialNo", tmpData.getSerialNo());
		int dealedCount = tmpDataMapper.selectImgCount(condMap);
		// 检查该图像勾对情况
		if (dealedCount > 0) {
			// 勾对过的释放原先勾对的流水
			condMap.clear();
			condMap.put("checkFlag", "-1");
			condMap.put("lserialNo", "");
			condMap.put("serialNo", tmpData.getSerialNo());
			condMap.put("operatorNo", tmpData.getOperatorNo());
			condMap.put("siteNo", tmpData.getSiteNo());
			condMap.put("occurDate", tmpData.getOccurDate());
			String flowId = tmpDataMapper.selectByPrimaryKey(tmpData.getSerialNo()).getFlowId();
			flowId = BaseUtil.filterSqlParam(flowId);
			if (flowId.indexOf(",") > 0) {
				List<String> flowList = Arrays.asList(flowId.split(","));
				List<List<String>> list = SqlUtil.getSumArrayList(flowList);
				condMap.remove("flowId");
				condMap.put("flowList", list);
			} else {
				condMap.put("flowId", flowId);
			}
			flowMapper.updateFlow(condMap);
		}
		condMap.clear();
		condMap.put("checkFlag", "2");
		condMap.put("psLevel", "1");
		condMap.put("primaryInccodein", 0);
		condMap.put("flowId", "");
		condMap.put("processState", 2);
		if (tmpData.getFormName() != null && !"".equals(tmpData.getFormName())) {
			condMap.put("formName", tmpData.getFormName());
		}
		condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
		condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
		condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
		condMap.put("serialNo", tmpData.getSerialNo());
		tmpDataMapper.updateTmpDataInfo(condMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);

		// 日志信息
		String log = "图像序号为" + tmpData.getSerialNo() + "凭证，强过！";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_29, log);

	}

	/**
	 * 撤销凭证强过功能
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void backForcePass(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		MaParameterBean tmpData = (MaParameterBean) requestBean.getParameterList().get(0);

		// 构造条件参数
		HashMap condMap = new HashMap();
		// 检查该图像勾对情况
		condMap.put("checkFlag", "-1");
		condMap.put("psLevel", "1");
		condMap.put("primaryInccodein", 0);
		condMap.put("processState", 5);
		condMap.put("serialNo", tmpData.getSerialNo());
		tmpDataMapper.updateTmpDataInfo(condMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		
		// 日志信息
		String log = "图像序号为" + tmpData.getSerialNo() + "凭证，取消强过！";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_29, log);
		
	}

	/**
	 * 获取补扫任务列表
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getScanTaskList(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		MaParameterBean tmpData = (MaParameterBean) requestBean.getParameterList().get(0);
		retMap.put("patchTaskList", tmpBatchMapper.selectPatchTask(tmpData.getBatchId()));
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		// 日志信息
		String log = "批次为" + tmpData.getBatchId() + "凭证，获得补扫任务！";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_4, log);
	}

	/**
	 * 添加补扫任务
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addScanTask(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		MaParameterBean tmpData = (MaParameterBean) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("batchId", tmpData.getBatchId());
		condMap.put("status", "0");// 初始状态
		condMap.put("operatorWorker", BaseUtil.getLoginUser().getUserNo());
		condMap.put("patchDes", tmpData.getPatchDes());
		int result = tmpBatchMapper.insertPatchTask(condMap);
		if (result > 0) {
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);

			// 日志信息
			String log = "批次为" + tmpData.getBatchId() + "凭证，增加补扫任务！";
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_29, log);

		} else {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
		}
	}

	/**
	 * 撤销重扫
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void backScanAgain(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		MaParameterBean tmpData = (MaParameterBean) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("selfDelete", "0");
		condMap.put("processState", "3");// 重扫位置为0
		condMap.put("serialNo", tmpData.getSerialNo());
		tmpDataMapper.updateTmpDataInfo(condMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);

		// 日志信息
		String log = "图像序号为" + tmpData.getBatchId() + "凭证，撤销标记重扫标记！";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_29, log);
	}

	/**
	 * 重扫
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void markScanAgain(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		MaParameterBean tmpData = (MaParameterBean) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("selfDelete", "1");
		condMap.put("processState", "4");// 重扫位置为1
		condMap.put("serialNo", tmpData.getSerialNo());
		tmpDataMapper.updateTmpDataInfo(condMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);

		// 日志信息
		String log = "图像序号为" + tmpData.getBatchId() + "凭证，添加重扫操作！";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_29, log);
	}

	/**
	 * 撤销删除
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void backDelete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		MaParameterBean tmpData = (MaParameterBean) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("selfDelete", "0");
		condMap.put("processState", "2");
		condMap.put("serialNo", tmpData.getSerialNo());
		tmpDataMapper.updateTmpDataInfo(condMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);

		// 日志信息
		String log = "图像序号为" + tmpData.getBatchId() + "凭证，撤销删除标记操作！";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_29, log);
	}

	/**
	 * 标记删除
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void markDelete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		MaParameterBean tmpData = (MaParameterBean) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("selfDelete", "1");
		condMap.put("processState", "2");
		condMap.put("serialNo", tmpData.getSerialNo());
		tmpDataMapper.updateTmpDataInfo(condMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);

		// 日志信息
		String log = "图像序号为" + tmpData.getBatchId() + "凭证标志差错！";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_29, log);
	}

	/**
	 * 手工指定主件
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void doHandleUp(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		MaParameterBean tmpData = (MaParameterBean) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("batchId", tmpData.getBatchId());
		condMap.put("serialNo", tmpData.getSerialNo());
		condMap.put("inccodeinBatch", tmpData.getInccodeinBatch());
		condMap.put("processState", "2");
		int result = tmpDataMapper.updateHandleUp(condMap);
		if (result == 0) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("选择的批内码不是主件或已标记删除!");
		} else {
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);

			// 日志信息
			String log = "批次号为" + tmpData.getBatchId() + "图像序号为" + tmpData.getBatchId() + "批内码为"
					+ tmpData.getInccodeinBatch() + "凭证手工设置为附件！";
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_30, log);
		}
	}

	/**
	 * 设为附件
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setPsLevelDown(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		MaParameterBean tmpData = (MaParameterBean) requestBean.getParameterList().get(0);

		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("checkFlag", "1");
		condMap.put("serialNo", tmpData.getSerialNo());

		// 如果是主件，检查是否勾对过流水
		int dealedCount = tmpDataMapper.selectImgCount(condMap);
		if (dealedCount > 0) {
			// 勾对过的凭证释放原先勾对的流水
			condMap.clear();
			condMap.put("checkFlag", "-1");
			condMap.put("lserialNo", "");
			String flowId = tmpDataMapper.selectByPrimaryKey(tmpData.getSerialNo()).getFlowId();
			flowId = BaseUtil.filterSqlParam(flowId);
			if (flowId.indexOf(",") > 0) {
				List<String> flowList = Arrays.asList(flowId.split(","));
				List<List<String>> list = SqlUtil.getSumArrayList(flowList);
				condMap.remove("flowId");
				condMap.put("flowList", list);
			} else {
				condMap.put("flowId", flowId);
			}
			condMap.put("operatorNo", tmpData.getOperatorNo());
			condMap.put("siteNo", tmpData.getSiteNo());
			condMap.put("occurDate", tmpData.getOccurDate());
			flowMapper.updateFlow(condMap);
		}
		// 将该凭证及其附件信息全部登记为附件
		condMap.clear();
		condMap.put("batchId", tmpData.getBatchId());
		condMap.put("serialNo", tmpData.getSerialNo());
		condMap.put("inccodeinBatch", tmpData.getInccodeinBatch());
		condMap.put("processState", "2");
		condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
		condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
		condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
		tmpDataMapper.updatePsLevelDownAll(condMap);
		// 更新版面名称
		if (null != tmpData.getFormName() && !"".equals(tmpData.getFormName())) {
			condMap.clear();
			condMap.put("serialNo", tmpData.getSerialNo());
			condMap.put("formName", tmpData.getFormName());
			tmpDataMapper.updateTmpDataInfo(condMap);
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);

		// 日志信息
		String log = "批次号为" + tmpData.getBatchId() + "图像序号为" + tmpData.getBatchId() + "批内码为"
				+ tmpData.getInccodeinBatch() + "凭证设置为附件！";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_30, log);

	}

	/**
	 * 设为主件
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setPsLevelUp(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		MaParameterBean tmpData = (MaParameterBean) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("psLevel", "0");
		condMap.put("serialNo", tmpData.getSerialNo());
		List dataList = tmpDataMapper.selectBySelective(condMap);
		if (dataList != null && dataList.size() > 0) {
			condMap.clear();
			// 传递设主件需要的参数信息
			condMap.put("psLevel", "1");
			condMap.put("primaryInccodein", 0);
			condMap.put("checkFlag", "-1");
			condMap.put("flowId", "");
			condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
			condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
			condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
			condMap.put("formName", tmpData.getFormName());
			condMap.put("processState", "2");// 业务审核未置为1
			condMap.put("serialNo", tmpData.getSerialNo());
			tmpDataMapper.updateTmpDataInfo(condMap);
			// 如果操作附件指定了主件号，更新其后面相同主件号的附件 指定附件号为当前设置的主件
			int primaryInccodein = ((BigDecimal) ((Map) dataList.get(0)).get("PRIMARY_INCCODEIN")).intValue();
			if (primaryInccodein > 0) {
				condMap.clear();
				condMap.put("batchId", tmpData.getBatchId());
				condMap.put("inccodeinBatch", tmpData.getInccodeinBatch());
				condMap.put("primaryInccodein", Integer.parseInt(BaseUtil.filterSqlParam(String.valueOf(primaryInccodein))));
				condMap.put("checkFlag", "0");
				condMap.put("flowId", "");
				tmpDataMapper.updatePsLevelUp(condMap);
			}
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			// 添加日志
			String log = "图像序号为" + tmpData.getSerialNo() + "的凭证设置为主件!";
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_30, log);
		} else {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("该凭证已经是主件,请刷新记录!");
		}
	}

	/**
	 * 更新差错标识
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateErrorFlag(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map reqMap = requestBean.getSysMap();
		String serialNo = (String) reqMap.get("serialNo");
		tmpDataMapper.updateErrorFlag(serialNo);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("更新成功");
	}

}
