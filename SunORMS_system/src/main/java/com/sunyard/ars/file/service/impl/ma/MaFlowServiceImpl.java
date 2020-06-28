package com.sunyard.ars.file.service.impl.ma;


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
import com.sunyard.ars.file.service.ma.IMaFlowService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


import javax.annotation.Resource;

@Service("maFlowService")
@Transactional
public class MaFlowServiceImpl extends BaseService  implements IMaFlowService {
    
    @Resource
	private FlowMapper flowMapper;

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
		if ("QUERYPULSYFLOW".equalsIgnoreCase(oper_type)) {//查询剩余流水
			selectPlusFlow(requestBean, responseBean);
		} else if ("QUERYFLOW".equalsIgnoreCase(oper_type)) {//查询流水
			selectFlow(requestBean, responseBean);
		} else if ("GETFLOWSBYSEQ".equalsIgnoreCase(oper_type)) {//查询流水
			getFlowBySeqId(requestBean, responseBean);
		} else if("QUERYPLURALFLOW".equalsIgnoreCase(oper_type)){//查询套勾流水
			selectTFLow(requestBean, responseBean);
		} else if ("QUERFLOWFIELD".equalsIgnoreCase(oper_type)) {//查询展现流水字段信息
			queryFlowField(requestBean, responseBean);
		} else if ("FREEFLOW".equalsIgnoreCase(oper_type)) {//释放流水
			freeImageAndFlow(requestBean, responseBean);
		} else if ("FREEALLFLOW".equalsIgnoreCase(oper_type)) {//释放所有流水
			freeAllImageAndFlow(requestBean, responseBean);
		} else if ("CHECKFLOW".equalsIgnoreCase(oper_type)){//检查录入流水信息 无流水号字段
			checkFlow(requestBean, responseBean);
		} else if ("CHECKFLOWOTHER".equalsIgnoreCase(oper_type)){//检查录入流水信息 有流水号字段
			checkFlowOther(requestBean, responseBean);
		} else if ("DEALNORMAL".equalsIgnoreCase(oper_type)) {//勾兑流水
			dealNormal(requestBean, responseBean);
		} else if ("SEARCHCHOICESFLOW".equalsIgnoreCase(oper_type)){//单条勾兑流水查询
			searchChoicesFlow(requestBean, responseBean);
		}else if("updateFlowErrorFlag".equalsIgnoreCase(oper_type)) {
			updateFlowErrorFlag(requestBean, responseBean);
		}
	}
	/**
	 * 用主键查询出流水号
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	private void getFlowBySeqId(RequestBean requestBean, ResponseBean responseBean) {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		String seqId = ((Map)requestBean.getParameterList().get(0)).get("seqId").toString();
		Flow selectByPrimaryKey = flowMapper.selectByPrimaryKey(new BigDecimal(seqId));
		if(selectByPrimaryKey==null){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("未查询到流水");
			return;
		}
		Map retMap = new HashMap();
		int code = 0;
		if (!"-1".equals(selectByPrimaryKey.getCheckFlag())){
			code = 1;
		}
		retMap.put("flowId", selectByPrimaryKey.getFlowId());
		retMap.put("code", code);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * 单条勾兑时查询流水信息
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void searchChoicesFlow(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		String fieldValueTyles = ((Map)requestBean.getParameterList().get(0)).get("fieldValueTyles").toString();
		
		String queryFields = ((Map)requestBean.getParameterList().get(0)).get("queryFields").toString();
		String[] fields = fieldValueTyles.split(";");
		// 构造查询表语句
		List dataList = new ArrayList();
		HashMap dataMap = null;
		for(String field:fields){
			dataMap = new HashMap(); 
			String[] strs = field.split("\\|");
			dataMap.put("fieldName", strs[0]);
			dataMap.put("fieldValue", strs[1]);
		}
		dataList.add(dataMap);
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("occurDate", occurDate);
		condMap.put("siteNo", siteNo);
		condMap.put("operatorNo", operatorNo);
		condMap.put("flowQueryFields", queryFields);
		condMap.put("list", dataList);
		condMap.put("flowTb", "fl_flow_tb");
		// 查询记录
		List list = flowMapper.getSingleFlowInfoList(condMap);
		
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("flows", list);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * 套勾流水查询方法
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void selectTFLow(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		String accountNo = ((Map)requestBean.getParameterList().get(0)).get("accountNo").toString();
	
		String flowId = ((Map)requestBean.getParameterList().get(0)).get("flowId").toString();
		String txCode = ((Map)requestBean.getParameterList().get(0)).get("txCode").toString();
		String amount = ((Map)requestBean.getParameterList().get(0)).get("amount").toString();
		
		String queryFields = ((Map)requestBean.getParameterList().get(0)).get("queryFields").toString();
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("occurDate", occurDate);
		condMap.put("siteNo", siteNo);
		condMap.put("operatorNo", operatorNo);
		if(accountNo!=null&&!"".equals(accountNo)){
			condMap.put("accountLike", accountNo+"%");
		}
		if(amount!=null&&!"".equals(amount)){
			condMap.put("amountLike", amount+"%");
		}
		if(flowId!=null&&!"".equals(flowId)){
			condMap.put("flowIdLike", flowId+"%");
		}
		condMap.put("checkFlag", "-1");
		condMap.put("txCode", txCode);
		condMap.put("flowQueryFields", queryFields);
		condMap.put("flowTb", "fl_flow_tb");
		// 查询记录
		List list = flowMapper.getFlowInfoList(condMap);
		
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("flows", list);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * 勾兑流水
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void dealNormal(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		// 拼装返回信息
		Map retMap = new HashMap();
		// 前台参数集合
		String serialNo = ((Map)requestBean.getParameterList().get(0)).get("serialNo").toString();
		String formName = ((Map)requestBean.getParameterList().get(0)).get("formName").toString();
		formName = URLDecoder.decode(formName,"UTF-8");
		String flowId = ((Map)requestBean.getParameterList().get(0)).get("flowID").toString();
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		
		String flowValues = ((Map)requestBean.getParameterList().get(0)).get("flowValues").toString();//勾对值
		Object seqId = ((Map)requestBean.getParameterList().get(0)).get("seqId");//图像序号
		String pluralFlowFlag = ((Map)requestBean.getParameterList().get(0)).get("pluralFlowFlag").toString();//套勾标志
		String isMulti = ((Map)requestBean.getParameterList().get(0)).get("isMulti").toString();//单勾多勾标志
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
			condMap.put("checkFlag", "-1");
			condMap.put("lserialNo", "");
			condMap.put("lserialNoWhere", serialNo);//add，凭证对应的流水
			
			flowMapper.updateFlow(condMap);
			condMap.remove("lserialNoWhere");//move，凭证对应的流水
		}
		if("true".equalsIgnoreCase(pluralFlowFlag)){//套勾
			List<String> flowList = Arrays.asList(flowId.split(","));
			List<List<String>> list = SqlUtil.getSumArrayList(flowList);
			String[] fields = flowValues.split(";");
			// 构造查询表语句
			List dataList = new ArrayList();
			HashMap dataMap = null;
			for(String field:fields){
				dataMap = new HashMap(); 
				String[] strs = field.split("\\|");
				dataMap.put("fieldName", strs[0]);
				dataMap.put("fieldValue", strs[1]);
				if(!"flow_id".equalsIgnoreCase(strs[0])){//流水号单独组装
					dataList.add(dataMap);
				}
			}
			//2.检查该流水勾对情况
			condMap.put("checkFlag", "1");
			condMap.put("flowList", list);
			condMap.put("list", dataList);
			if(dataList.size()==0){
				condMap.remove("list");
			}
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
			condMap.put("formName", formName);
			condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
			condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
			condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
			condMap.put("processState", "2");
			condMap.put("flowId",flowId);
			tmpDataMapper.updateTmpDataInfo(condMap);//更新图像已勾兑
			condMap.remove("flowId");
			condMap.put("lserialNo", serialNo);
			
			flowMapper.updateFlow(condMap);//更新流水已勾兑
			
		}else{//单勾、多勾
			//单勾
			
			if("0".equals(isMulti) && seqId != null){
				//2.检查该流水勾对情况
				
				condMap.put("checkFlag", "1");
				condMap.put("seqId", seqId);
				condMap.put("flowId", flowId);
				List lserialNoList = flowMapper.checkPluralFlowIsDealed(condMap);
				if(lserialNoList != null && lserialNoList.size() > 0){
					//判断勾对流水已经勾对的图像是否存在套勾了其他流水，有其他流水则不释放图像
					String lserialNo = ((Map)lserialNoList.get(0)).get("LSERIAL_NO").toString();
					lserialNo = BaseUtil.filterSqlParam(lserialNo);
					if(flowMapper.selectDealCount(condMap) == 0){
						//释放这条流水原先勾对的图像
						condMap.put("checkFlag", "-1");
						condMap.put("flowId", "");
						condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
						condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
						condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
						condMap.put("serialNo",lserialNo);
						tmpDataMapper.updateTmpDataInfo(condMap);
						condMap.remove("serialNo");
						freedSerialNo = lserialNo;
					}
				}
				//3.勾对
				condMap.put("serialNo", serialNo);
				condMap.put("checkFlag", "1");
				condMap.put("psLevel", "1");
				condMap.put("flowId", flowId);
				condMap.put("formName", formName);
				condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
				condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
				condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
				condMap.put("processState", "2");
				tmpDataMapper.updateTmpDataInfo(condMap);//更新图像已勾兑
				
				condMap.put("lserialNo", serialNo);
				condMap.remove("serialNo");
				condMap.remove("flowId");
				flowMapper.updateFlow(condMap);//更新流水已勾兑
			}else{
				//多勾
				//2.检查该流水勾对情况
				condMap.put("checkFlag", "1");
				condMap.put("flowId", flowId);
				List lserialNoList = flowMapper.checkPluralFlowIsDealed(condMap);
				if(lserialNoList != null && lserialNoList.size() > 0){
					//释放这条流水原先勾对的图像
					
					for (Object lserialNo : lserialNoList) {
						String freelserialNo = ((Map)lserialNo).get("LSERIAL_NO").toString();
						freelserialNo = BaseUtil.filterSqlParam(freelserialNo);
						//判断勾对流水已经勾对的图像是否存在套勾了其他流水，有其他流水则不释放图像
						if(flowMapper.selectDealCount(condMap) == 0){
							//释放这条流水原先勾对的图像
							condMap.put("checkFlag", "-1");
							condMap.put("flowId", "");
							condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
							condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
							condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
							condMap.put("serialNo",freelserialNo);
							tmpDataMapper.updateTmpDataInfo(condMap);
							freedSerialNo += freelserialNo+",";
						}
					}
					condMap.remove("serialNo");
					if(!"".equals(freedSerialNo)){
						freedSerialNo = freedSerialNo.substring(0, freedSerialNo.length()-1);
					}
				}
				//3.勾对
				condMap.put("flowId", flowId);
				condMap.put("serialNo", serialNo);
				condMap.put("checkFlag", "1");
				condMap.put("psLevel", "1");
				condMap.put("formName", formName);
				condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
				condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
				condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
				condMap.put("processState", "2");
				tmpDataMapper.updateTmpDataInfo(condMap);//更新图像已勾兑
				
				condMap.put("lserialNo", serialNo);
				condMap.remove("serialNo");
				flowMapper.updateFlow(condMap);//更新流水已勾兑
			}	
		}
		if(!"".equals(freedSerialNo)){
			//传递到页面已经释放的图像序号
			retMap.put("freedSerialNo", freedSerialNo);
		}
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("勾兑成功");
	}
	//有流水号
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void checkFlowOther(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		// 前台参数集合
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		String fieldValueTyles = ((Map)requestBean.getParameterList().get(0)).get("fieldValueTyles").toString();
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		// 构造查询表语句
		List dataList = new ArrayList();
		//查询条件
		HashMap dataMap = null; 
		
		String[] fields = fieldValueTyles.split(";");
		for(String field:fields){
			dataMap = new HashMap(); 
			String[] strs = field.split("\\|");
			dataMap.put("fieldName", strs[0]);
			dataMap.put("fieldValue", strs[1]);
			dataList.add(dataMap);
	
		}
		condMap.put("list", dataList);
		condMap.put("operatorNo", operatorNo);
		condMap.put("siteNo", siteNo);
		condMap.put("occurDate", occurDate);
		List flowIdList = flowMapper.getCheckFlowList(condMap);
		//判断 1条流水返回是否勾对，多条流水判断有无勾对过的流水
		// 拼装返回信息
		Map retMap = new HashMap();
		int code = 0;
		boolean isSuccess = false;
		String msg="查询成功";
		if(flowIdList == null || flowIdList.size() <= 0){
			code = -1;
			msg="没有找到匹配流水";
		}else{
			String flowId = String.valueOf(((Map)flowIdList.get(0)).get("FLOW_ID"));
			retMap.put("flowId", flowId);
			isSuccess = true;
			if(!"-1".equals(((Map)flowIdList.get(0)).get("CHECK_FLAG"))){
				code = 1;//单条流水已经勾兑
			}else{
				code = 0;
			}
		}
		retMap.put("code", code);
		retMap.put("isSuccess", isSuccess);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(msg);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void checkFlow(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		// 前台参数集合
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		String fieldValueTyles = ((Map)requestBean.getParameterList().get(0)).get("fieldValueTyles").toString();
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		// 构造查询表语句
		List dataList = new ArrayList();
		//查询条件
		HashMap dataMap = null; 
		
		String[] fields = fieldValueTyles.split(";");
		for(String field:fields){
			dataMap = new HashMap(); 
			String[] strs = field.split("\\|");
			dataMap.put("fieldName", strs[0]);
			dataMap.put("fieldValue", strs[1]);
			dataList.add(dataMap);
	
		}
		condMap.put("list", dataList);
		condMap.put("operatorNo", operatorNo);
		condMap.put("siteNo", siteNo);
		condMap.put("occurDate", occurDate);
		List list = flowMapper.getCheckFlowList(condMap);
		//判断 1条流水返回是否勾对，多条流水判断有无勾对过的流水
		int code = 0;
		Map retMap = new HashMap();
		if (list.size() == 0){
			code = -1;//未查找到流水
		}else {
			if(list.size() == 1){
				String flowId=String.valueOf( ((Map)list.get(0)).get("FLOW_ID"));
				retMap.put("flowId", flowId);
					code = 1;//单条流水未勾对
				if(!"-1".equals(((Map)list.get(0)).get("CHECK_FLAG"))){
					code = 2;//单条流水已经勾兑
				}
			}else if(list.size() > 1){
				// 查询出多条流水
				HashSet<String> hashSet = new HashSet<String>();
				boolean flag=true;
				for(int i = 0; i < list.size();i ++){
					hashSet.add(String.valueOf( ((Map)list.get(i)).get("FLOW_ID")));
					if(!"-1".equals(((Map)list.get(i)).get("CHECK_FLAG"))){
						flag=false;//多条存在未勾对
					}
				}
				
				if(hashSet.size()==1){
					//多流水	
					String flowId=String.valueOf( ((Map)list.get(0)).get("FLOW_ID"));
					retMap.put("flowId", flowId);
					if(flag){
						code = 3;//未勾对
					}else {
						code = 4;//已勾对
					}
				}else{
					code = 5;//多条流水考虑优化补录字段
				}
		}
		}
		// 拼装返回信息
		
		retMap.put("code", code);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void selectFlow(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		String accountNo = ((Map)requestBean.getParameterList().get(0)).get("accountNo").toString();
		String amount = ((Map)requestBean.getParameterList().get(0)).get("amount").toString();
		String flowId = ((Map)requestBean.getParameterList().get(0)).get("flowId").toString();
		String checkFlag = ((Map)requestBean.getParameterList().get(0)).get("checkFlag").toString();
		String serialNo = ((Map)requestBean.getParameterList().get(0)).get("serialNo").toString();
		
		String queryFields = ((Map)requestBean.getParameterList().get(0)).get("queryFields").toString();
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		if(accountNo!=null&&!"".equals(accountNo)){
			condMap.put("accountLike", accountNo+"%");
		}
		if(amount!=null&&!"".equals(amount)){
			condMap.put("amountLike", amount+"%");
		}
		if(flowId!=null&&!"".equals(flowId)){
			condMap.put("flowIdLike", flowId+"%");
		}
		condMap.put("occurDate", occurDate);
		condMap.put("siteNo", siteNo);
		condMap.put("operatorNo", operatorNo);


		condMap.put("checkFlag", checkFlag);
		condMap.put("serialNo", serialNo);
		condMap.put("flowQueryFields", queryFields);
		condMap.put("flowTb", "fl_flow_tb");
		
		
		
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

	/**
	 * 释放所有流水
	 * @param requestBean
	 * @param retBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void freeAllImageAndFlow(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		String batchId = ((Map)requestBean.getParameterList().get(0)).get("batchId").toString();
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("batchId", batchId);
		condMap.put("operatorNo", operatorNo);
		condMap.put("siteNo", siteNo);
		condMap.put("occurDate", occurDate);
		condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
		condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
		condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
		condMap.put("oldCheckFlag", "1");
		flowMapper.updateFreeAllFlow(condMap);
		tmpDataMapper.updateAllTmpData(condMap);
		
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
	}

	/**
	 * 释放流水
	 * @param requestBean
	 * @param retBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void freeImageAndFlow(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		String serialNo = ((Map)requestBean.getParameterList().get(0)).get("serialNo").toString();
		//检查该图像勾对情况
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("checkFlag", "1");
		condMap.put("serialNo", serialNo);
		int dealedCount = tmpDataMapper.selectImgCount(condMap);
		if(dealedCount > 0){
			condMap.clear();
			condMap.put("checkFlag", "-1");
			condMap.put("lserialNo", "");
			condMap.put("serialNo", serialNo);
			condMap.put("operatorNo", operatorNo);
			condMap.put("siteNo", siteNo);
			condMap.put("occurDate", occurDate);
			condMap.put("flowId", "");
			condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
			condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
			condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
			String flowId = tmpDataMapper.selectByPrimaryKey(serialNo).getFlowId();
			tmpDataMapper.updateTmpDataInfo(condMap);
			//如果图像有勾对流水
			//勾对过的凭证释放原先勾对的流水
			condMap.remove("flowId");
			condMap.put("lserialNo", "");
			condMap.put("lserialNoWhere", serialNo);//add，凭证对应的流水
			flowMapper.updateFlow(condMap);
			condMap.remove("lserialNoWhere");//move，凭证对应的流水
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			
			String log = "流水操纵释流水，释放图像"+serialNo +"的流水";
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_31, log);
		}else{
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
		}
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
		for (Object object : flowFiedlList) {
			queryFields += ((Map)object).get("FIELD_NAME")+",";
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
	 * 查询剩余流水方法
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void selectPlusFlow(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		String queryFields = ((Map)requestBean.getParameterList().get(0)).get("queryFields").toString();
//		String flowId = ((Map)requestBean.getParameterList().get(0)).get("flowId").toString();
		String flowId = null;
		Object o = ((Map)requestBean.getParameterList().get(0)).get("flowId");
		if (o!=null&&!"".equals(o)) {
			flowId = o.toString() + "%";
		}
		String account = null;
		Object oaccount = ((Map)requestBean.getParameterList().get(0)).get("account");
		if (oaccount!=null&&!"".equals(oaccount)) {
			account = oaccount.toString() + "%";
		}
		String amount = null;
		Object oamount = ((Map)requestBean.getParameterList().get(0)).get("amount");
		if (oamount!=null&&!"".equals(oamount)) {
			amount = oamount.toString() + "%";
		}
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("checkFlag", "-1");
		condMap.put("occurDate", occurDate);
		condMap.put("siteNo", siteNo);
		condMap.put("operatorNo", operatorNo);
		condMap.put("flowQueryFields", queryFields);
		condMap.put("flowTb", "fl_flow_tb");
		condMap.put("flowIdLike", flowId);
		condMap.put("accountLike", account);
		condMap.put("amountLike", amount);
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
