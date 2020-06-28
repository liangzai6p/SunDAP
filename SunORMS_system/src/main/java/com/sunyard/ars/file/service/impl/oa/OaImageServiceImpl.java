package com.sunyard.ars.file.service.impl.oa;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.dao.FlowMapper;
import com.sunyard.ars.common.dao.TmpBatchMapper;
import com.sunyard.ars.common.dao.TmpDataMapper;
import com.sunyard.ars.common.pojo.TmpBatch;
import com.sunyard.ars.common.util.SqlUtil;
import com.sunyard.ars.file.dao.oa.CheckOffMapper;
import com.sunyard.ars.file.service.oa.IOaImageService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

@Service("oaImageService")
@Transactional
public class OaImageServiceImpl extends BaseService  implements IOaImageService {
	@Resource
	private TmpBatchMapper tmpBatchMapper;
	 
	@Resource
	private TmpDataMapper tmpDataMapper;
	
	@Resource
	private FlowMapper flowMapper;
	
	@Resource
	private CheckOffMapper checkOffMapper;
 
    
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
		if ("QUERYBATCH".equalsIgnoreCase(oper_type)) {//查询批次信息
			queryBatchList(requestBean,responseBean);
		}else if("QUERYBATCHBYID".equalsIgnoreCase(oper_type)) {//查询批次信息
			queryBatchListById(requestBean,responseBean);
		} else if("QUERYIMAGE".equalsIgnoreCase(oper_type)){//查询图像信息
			queryImageList(requestBean,responseBean);
		} else if ("PSLEVELUP".equalsIgnoreCase(oper_type)) {//设为主件
			setPsLevelUp(requestBean, responseBean);
		} else if ("PSLEVELDOWN".equalsIgnoreCase(oper_type)) {//设为附件
			setPsLevelDown(requestBean, responseBean);
		} else if ("HANDLEUP".equalsIgnoreCase(oper_type)) {//手工指定主件
			doHandleUp(requestBean, responseBean);
		} else if ("MARKDELETE".equalsIgnoreCase(oper_type)) {//标记删除
			markDelete(requestBean, responseBean);
		} else if ("BACKDELETE".equalsIgnoreCase(oper_type)) {//撤销标记删除
			backDelete(requestBean, responseBean);
		} else if ("FORCEPASS".equalsIgnoreCase(oper_type)) {//强过
			imageForcePass(requestBean, responseBean);
		} else if ("UPDATEFORMNAME".equalsIgnoreCase(oper_type)) {//修改版面
			updateFormName(requestBean, responseBean);
		} else if("updateErrorFlag".equals(oper_type)) {
			updateErrorFlag(requestBean, responseBean);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryImageList(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		Map sysMap = requestBean.getSysMap();
	//	String batchId = ((Map)requestBean.getParameterList().get(0)).get("batchId").toString();
		String tableName = ((Map)requestBean.getParameterList().get(0)).get("tableName").toString();
		List batchIds = (List)((Map)requestBean.getParameterList().get(0)).get("batchIds");
		String formName = ((Map)requestBean.getParameterList().get(0)).get("formName").toString();
		String imageCheckFlag = ((Map)requestBean.getParameterList().get(0)).get("imageCheckFlag").toString();
		List<String> imageList = null;
		if(!imageCheckFlag.equals("")){
			imageList = changeStringToList(imageCheckFlag);
		}
		// 构造条件参数
		HashMap condMap = new HashMap();
		//condMap.put("batchId", batchId);
		condMap.put("batchList", batchIds);
		condMap.put("dataTb", tableName);
		condMap.put("formName", formName);
		condMap.put("imageCheckFlag", imageCheckFlag);
		condMap.put("imageList", imageList);
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
	//	List list = tmpDataMapper.selectImageList(condMap);
		List list = tmpDataMapper.selectImageListByBatchList(condMap);
		// 获取总记录数
		long totalCount = page.getTotal();	
		
		retMap.put("pageSize", pageSize);
		retMap.put("allRow", totalCount);
		retMap.put("images", list);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryBatchList(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		// 拼装返回信息
		Map retMap = new HashMap();
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		

		String formName = ((Map)requestBean.getParameterList().get(0)).get("formName").toString();
		String imageCheckFlag = ((Map)requestBean.getParameterList().get(0)).get("imageCheckFlag").toString();
		List<String> imageList = null;
		if(!imageCheckFlag.equals("")){
			imageList = changeStringToList(imageCheckFlag);
		}
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("occurDate", occurDate);
		condMap.put("siteNo", siteNo);
		condMap.put("operatorNo", operatorNo);
		condMap.put("needProcess", "1");
		condMap.put("formName", formName);
		condMap.put("imageCheckFlag", imageCheckFlag);
		condMap.put("imageList", imageList);
		//1.查找批次
		List batchList = tmpBatchMapper.selectBatchList(condMap);
		if(batchList == null || batchList.size() == 0){
			retMap.put("isSuccess", false);
		}else{
			retMap.put("isSuccess", true);
			//2.检查轧账状态
			List all_checkOff = checkOffMapper.getCheckOffState(condMap);
			//如果一天有多个批次,把批次id保存下来,先取第一批,有个处理状态值progressFlag需要，所以也做了保存
			//业务需求一次查询出所有批次的所有的影像
			if(batchList.size() > 0){
				List batchInfoList = new ArrayList();
				List batchIds = new ArrayList();
				for(Object tempBatch:batchList){
					HashMap<String, String> hashMap = new HashMap<String, String>();
					String tempBatchId = ((Map) tempBatch).get("BATCH_ID")+"";
					//hashMap.put("BATCH_ID", tempBatchId);
					String tempProgressFlag = ((Map) tempBatch).get("PROGRESS_FLAG")+"";
					//hashMap.put("PROGRESS_FLAG", tempProgressFlag);
					batchInfoList.add(tempBatchId+","+tempProgressFlag);
					batchIds.add(tempBatchId);
				}
				retMap.put("batchInfoList", batchInfoList);
				retMap.put("batchIds", batchIds);
			}
			//不管几个批次都先查第一个 ?
			
			retMap.put("tableName", ((Map)batchList.get(0)).get("TEMP_DATA_TABLE"));
			//retMap.put("inputDate", ((Map)batchList.get(0)).get("INPUT_DATE"));
		}
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
		//
		String log = "图像操作，查询批次信息";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_29, log);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryBatchListById(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		// 拼装返回信息
		Map retMap = new HashMap();
		String batchId = ((Map)requestBean.getParameterList().get(0)).get("batchId").toString();
		TmpBatch tmpBatch = tmpBatchMapper.selectByPrimaryKey(BaseUtil.filterSqlParam(batchId));
		if(tmpBatch==null){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("未查询到该批次");
			return;
		}
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("occurDate", tmpBatch.getOccurDate());
		condMap.put("siteNo", tmpBatch.getSiteNo());
		condMap.put("operatorNo", tmpBatch.getOperatorNo());
		condMap.put("needProcess", "1");
		
		//1.查找批次
		List batchList = tmpBatchMapper.selectBatchList(condMap);
		if(batchList == null || batchList.size() == 0){
			retMap.put("isSuccess", false);
		}else{
			retMap.put("isSuccess", true);
			//2.检查轧账状态
			List all_checkOff = checkOffMapper.getCheckOffState(condMap);
			//如果一天有多个批次,把批次id保存下来,先取第一批,有个处理状态值progressFlag需要，所以也做了保存
			if(batchList.size() > 1){
				List batchInfoList = new ArrayList();
				for(Object tempBatch:batchList){
					HashMap<String, String> hashMap = new HashMap<String, String>();
					String tempBatchId = ((Map) tempBatch).get("BATCH_ID")+"";
					hashMap.put("BATCH_ID", tempBatchId);
					String tempProgressFlag = ((Map) tempBatch).get("PROGRESS_FLAG")+"";
					hashMap.put("PROGRESS_FLAG", tempProgressFlag);
					//batchInfoList.add(tempBatchId+","+tempProgressFlag);
					batchInfoList.add(hashMap);
				}
				retMap.put("batchInfoList", batchInfoList);
			}
			//不管几个批次都先查第一个 ?
			retMap.put("batchId", tmpBatch.getBatchId());
			retMap.put("tableName", ((Map)batchList.get(0)).get("TEMP_DATA_TABLE"));
			retMap.put("inputDate", tmpBatch.getInputDate());
		}
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	
	/**
	 * 修改版面信息
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateFormName(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		String formName = ((Map)requestBean.getParameterList().get(0)).get("formName").toString();
		String serialNo = ((Map)requestBean.getParameterList().get(0)).get("serialNo").toString();
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("formName", formName);
		condMap.put("serialNo", serialNo);
		tmpDataMapper.updateTmpDataInfo(condMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		
		String log = "图像操作，修改序号为"+serialNo+"图像的版面名称为"+formName;
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_29, log);
	}

	/**
	 * 凭证强过功能
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void imageForcePass(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		String serialNo = ((Map)requestBean.getParameterList().get(0)).get("serialNo").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("checkFlag", "1");
		condMap.put("serialNo", serialNo);
		int dealedCount = tmpDataMapper.selectImgCount(condMap);
	
		//检查该图像勾对情况
		if(dealedCount > 0){
			//勾对过的释放原先勾对的流水
			condMap.clear();
			condMap.put("checkFlag", "-1");
			condMap.put("lserialNo", "");
			String flowId = BaseUtil.filterSqlParam(tmpDataMapper.selectByPrimaryKey(serialNo).getFlowId());
			if(flowId.indexOf(",")>0){
				List<String> flowList = Arrays.asList(flowId.split(","));
				List<List<String>> list = SqlUtil.getSumArrayList(flowList);
				condMap.remove("flowId");
				condMap.put("flowList", list);
			}else{
				condMap.put("flowId", flowId);
			}
			condMap.put("operatorNo", operatorNo);
			condMap.put("siteNo", siteNo);
			condMap.put("occurDate", occurDate);
			flowMapper.updateFlow(condMap);
		}
		condMap.clear();
		condMap.put("checkFlag", "2");
		condMap.put("psLevel", "1");
		condMap.put("primaryInccodein", 0);
		condMap.put("flowId", "");
		condMap.put("processState", 2);
		condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
		condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
		condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
		condMap.put("serialNo", serialNo);
		tmpDataMapper.updateTmpDataInfo(condMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		
		//添加日志
		String log = "图像操作，强过图像序号为"+serialNo+"图像";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_29, log);
	}

	/**
	 * 撤销删除
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void backDelete(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		String serialNo = ((Map)requestBean.getParameterList().get(0)).get("serialNo").toString();
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("selfDelete", "0");
		condMap.put("serialNo", serialNo);
		tmpDataMapper.updateTmpDataInfo(condMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		
		//图像操作日志
		String log = "图像操作。序号为" + serialNo + "的图像撤销删除标志!";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_29, log);
	}

	/**
	 * 标记删除
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void markDelete(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		String serialNo = ((Map)requestBean.getParameterList().get(0)).get("serialNo").toString();
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("selfDelete", "1");
		condMap.put("serialNo", serialNo);
		tmpDataMapper.updateTmpDataInfo(condMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		
		//
		String log  = "图像操作，序号为"+ serialNo +"的图像为标记删除!";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_29, log);
	}

	/**
	 * 手工指定主件
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void doHandleUp(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		String serialNo = ((Map)requestBean.getParameterList().get(0)).get("serialNo").toString();
		String batchId = ((Map)requestBean.getParameterList().get(0)).get("batchId").toString();
		String inccodeinBatch = ((Map)requestBean.getParameterList().get(0)).get("inccodeinBatch").toString();
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("batchId", batchId);
		condMap.put("serialNo", serialNo);
		condMap.put("inccodeinBatch", inccodeinBatch);
		condMap.put("processState", "2");
		int result = tmpDataMapper.updateHandleUp(condMap);
		if(result == 0){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("选择的批内码不是主件或已标记删除!");
		}else{
		
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			
			String log = "主附件调整，手动指定主件，批次号:" + batchId + "批内码:" + inccodeinBatch ;
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_30, log);
		}
	}

	/**
	 * 设为附件
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setPsLevelDown(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		String serialNo = ((Map)requestBean.getParameterList().get(0)).get("serialNo").toString();
		String batchId = ((Map)requestBean.getParameterList().get(0)).get("batchId").toString();
		String inccodeinBatch = ((Map)requestBean.getParameterList().get(0)).get("inccodeinBatch").toString();
		String operatorNo = ((Map)requestBean.getParameterList().get(0)).get("operatorNo").toString();
		String siteNo = ((Map)requestBean.getParameterList().get(0)).get("siteNo").toString();
		String occurDate = ((Map)requestBean.getParameterList().get(0)).get("occurDate").toString();

		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("checkFlag", "1");
		condMap.put("serialNo", serialNo);
		
		//如果是主件，检查是否勾对过流水
		int dealedCount = tmpDataMapper.selectImgCount(condMap);
		if(dealedCount > 0){
			//勾对过的凭证释放原先勾对的流水
			condMap.clear();
			condMap.put("checkFlag", "-1");
			condMap.put("lserialNo", "");
			String flowId = tmpDataMapper.selectByPrimaryKey(serialNo).getFlowId();
			flowId = BaseUtil.filterSqlParam(flowId);
			if(flowId.indexOf(",")>0){
				List<String> flowList = Arrays.asList(flowId.split(","));
				List<List<String>> list = SqlUtil.getSumArrayList(flowList);
				condMap.remove("flowId");
				condMap.put("flowList", list);
			}else{
				condMap.put("flowId", BaseUtil.filterSqlParam(flowId));
			}
			condMap.put("operatorNo", operatorNo);
			condMap.put("siteNo", siteNo);
			condMap.put("occurDate", occurDate);
			flowMapper.updateFlow(condMap);
		}
		//将该凭证及其附件信息全部登记为附件
		condMap.clear();
		condMap.put("batchId", batchId);
		condMap.put("serialNo", serialNo);
		condMap.put("inccodeinBatch", inccodeinBatch);
		condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
		condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
		condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
		tmpDataMapper.updatePsLevelDownAll(condMap);
		
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		

		String log = "主附件调整设置为附件，批次号:" + batchId +"批内码:" + inccodeinBatch;
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_30, log);
	}

	/**
	 * 设为主件
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setPsLevelUp(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		String serialNo = ((Map)requestBean.getParameterList().get(0)).get("serialNo").toString();
		String batchId = ((Map)requestBean.getParameterList().get(0)).get("batchId").toString();
		String inccodeinBatch = ((Map)requestBean.getParameterList().get(0)).get("inccodeinBatch").toString();
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("psLevel", "0");
		condMap.put("serialNo", serialNo);
		List dataList = tmpDataMapper.selectBySelective(condMap);
		if(dataList != null && dataList.size() > 0){
			condMap.clear();
			//传递设主件需要的参数信息
			condMap.put("psLevel", "1");
			condMap.put("primaryInccodein", 0);
			condMap.put("checkFlag", "-1");
			condMap.put("flowId", "");
			condMap.put("ocrWorker", BaseUtil.getLoginUser().getUserNo());
			condMap.put("ocrDate", BaseUtil.getCurrentDateStr());
			condMap.put("ocrTime", BaseUtil.getCurrentDateByFormat("HH:mm:ss"));
			condMap.put("serialNo", serialNo);
			tmpDataMapper.updateTmpDataInfo(condMap);
			//如果操作附件指定了主件号，更新其后面相同主件号的附件  指定附件号为当前设置的主件
			int primaryInccodein = ((BigDecimal) ((Map)dataList.get(0)).get("PRIMARY_INCCODEIN")).intValue();
			if(primaryInccodein > 0){
				condMap.clear();
				condMap.put("batchId", batchId);
				condMap.put("inccodeinBatch", inccodeinBatch);
				condMap.put("primaryInccodein", Integer.valueOf(BaseUtil.filterSqlParam(primaryInccodein+"")));
				condMap.put("checkFlag", "0");
				condMap.put("flowId", "");
				tmpDataMapper.updatePsLevelUp(condMap);
				
				String log = "主附件调整操作，把批次号:"+batchId+"批内码:" +inccodeinBatch+"设置为主键";
				addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_30, log);
				
			}
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		}else{
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("该凭证已经是主件,请刷新记录!");
		}
	}
	/**
	 * 更新差错标识
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateErrorFlag(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map reqMap = requestBean.getSysMap();
		String serialNo = (String) reqMap.get("serialNo");
		tmpDataMapper.updateErrorFlag(serialNo);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("更新成功");
	}
	
	private List<String> changeStringToList(String imageCheckFlag) {
		List<String> imageList = new ArrayList<String>();
		if(imageCheckFlag.indexOf("，")>-1 ){
			imageList = Arrays.asList(imageCheckFlag.split("，"));
		}else{
			imageList.add(imageCheckFlag);
		}
		return imageList;
	}
	

}
