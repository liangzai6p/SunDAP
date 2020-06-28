package com.sunyard.ars.system.service.impl.et;

import java.io.File;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.FileUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.dao.TaskLockMapper;
import com.sunyard.ars.common.util.DateUtil;
import com.sunyard.ars.common.util.SqlUtil;
import com.sunyard.ars.common.util.StringUtil;
import com.sunyard.ars.system.bean.et.BusiForm;
import com.sunyard.ars.system.bean.et.BusiNodeBean;
import com.sunyard.ars.system.bean.et.EtProcessTb;
import com.sunyard.ars.system.bean.et.EtRiskSourceDef;
import com.sunyard.ars.system.bean.et.ModelFieldResulet;
import com.sunyard.ars.system.bean.et.ProcessChartBean;
import com.sunyard.ars.system.bean.et.ProcessLineBean;
import com.sunyard.ars.system.bean.et.ProcessNodeBean;
import com.sunyard.ars.system.bean.et.UtilNodeActionBean;
import com.sunyard.ars.system.bean.et.UtilNodeBean;
import com.sunyard.ars.system.dao.busm.OrganInfoDao;
import com.sunyard.ars.system.dao.busm.UserOrganMapper;
import com.sunyard.ars.system.dao.et.BusiFormMapper;
import com.sunyard.ars.system.dao.et.EtDataStatisticsMapper;
import com.sunyard.ars.system.dao.et.EtProcessTbMapper;
import com.sunyard.ars.system.dao.et.EtRiskSourceDefMapper;
import com.sunyard.ars.system.dao.et.ModelDataQueryMapper;
import com.sunyard.ars.system.dao.et.SlipDefMapper;
import com.sunyard.ars.system.dao.mc.ExhibitFieldMapper;
import com.sunyard.ars.system.dao.mc.McModelLineMapper;
import com.sunyard.ars.system.dao.mc.ModelMapper;
import com.sunyard.ars.system.dao.mc.QueryImgMapper;
import com.sunyard.ars.system.dao.sc.SystemParameterMapper;
import com.sunyard.ars.system.init.SystemConstants;
import com.sunyard.ars.system.pojo.mc.McModelLine;
import com.sunyard.ars.system.pojo.mc.Model;
import com.sunyard.ars.system.pojo.mc.QueryImg;
import com.sunyard.ars.system.service.et.IBusiFormService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;
import com.sunyard.cop.IF.mybatis.pojo.User;

@Service("busiFormService")
@Transactional
public class BusiFormServiceImpl extends BaseService implements IBusiFormService {
	
	@Resource
	private QueryImgMapper queryImgMapper;
	@Resource
	private BusiFormMapper busiFormMapper; 
	@Resource
	private EtProcessTbMapper etProcessTbMapper; 
	@Resource
	private SlipDefMapper slipDefMapper;
	@Resource
	private EtRiskSourceDefMapper etRiskSourceDefMapper;
	@Resource
    private ExhibitFieldMapper exhibitFieldMapper;
	@Resource
	private ModelDataQueryMapper modelDataQueryMapper;
	@Resource
	private EtDataStatisticsMapper etDataStatisticsMapper;
	@Resource
    private ModelMapper modelMapper;
	@Resource
	private OrganInfoDao organInfoDao;
	@Resource
	private McModelLineMapper mcModelLineMapper;
	@Resource
    private TaskLockMapper taskLockMapper;

	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		// TODO Auto-generated method stub
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		// 获取操作标识
		String oper_type = (String) sysMap.get("oper_type");
		if (ARSConstants.OPERATE_QUERY.equals(oper_type)) {
			// 查询
			query(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_ADD.equals(oper_type)) { 
			// 新增
			add(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_MODIFY.equals(oper_type)) { 
			// 修改
			update(requestBean, responseBean);
		} else if(ARSConstants.OPERATE_DELETE.equals(oper_type)) { 
			// 删除
			delete(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_OTHER.equals(oper_type)) {
			// 其他操作
			otherOperation(requestBean, responseBean);
		} 
	}

	@SuppressWarnings({ "rawtypes"})
	@Override
	public void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		String operation = (String)sysMap.get("other_operation");
		if("fillBusiInit".equals(operation)) { //填写处理单初始查询
			fillBusiInit(requestBean, responseBean);
		}else if("busiQueryInit".equals(operation)) {
			busiQueryInit(requestBean, responseBean);
		}else if("saveOrUpdateBusiformTb".equalsIgnoreCase(operation)){//新增修改入口
			saveOrUpdateBusiformTb(requestBean, responseBean);
		}else if("getTaskList".equalsIgnoreCase(operation)){//查询列表
			getTaskList(requestBean, responseBean);
		}else if("loginInit".equalsIgnoreCase(operation)){//系统登录初始化数据
			loginInit(requestBean, responseBean);
		}else if("corrSearch".equalsIgnoreCase(operation)){//退回单据查询
			corrSearch(requestBean, responseBean);
		}else if("corrSearchHandle".equalsIgnoreCase(operation)){//可退回单据明细
			corrSearchHandle(requestBean, responseBean);
		}else if("checkOrganNo".equalsIgnoreCase(operation)){//检验机构录入是否正确
			checkOrganNo(requestBean, responseBean);
		}else if("doOverdueRemark".equalsIgnoreCase(operation)){//保存逾期备注
			doOverdueRemark(requestBean, responseBean);
		}else if("showBusiform".equalsIgnoreCase(operation)){//展现单据信息
			showBusiform(requestBean, responseBean);
		}else if("showProcess".equalsIgnoreCase(operation)){//展现处理信息
			showProcess(requestBean, responseBean);
		}else if("checkToSlip".equalsIgnoreCase(operation)){//检查是否已经转发单据
			checkToSlip(requestBean, responseBean);
		}else if("doDeleteBusiformTb".equalsIgnoreCase(operation)){//删除单据
			doDeleteBusiformTb(requestBean, responseBean);
		}else if("revivification".equalsIgnoreCase(operation)){//还原删除单据信息
			revivification(requestBean, responseBean);
		}else if("leaderView".equalsIgnoreCase(operation)){//查看抄送
			leaderView(requestBean, responseBean);
		}else if("doBackDeal".equalsIgnoreCase(operation)){//收回功能
			doBackDeal(requestBean, responseBean);
		}else if("doCorrHandleBack".equalsIgnoreCase(operation)){//退回提交
			doCorrHandleBack(requestBean, responseBean);
		}else if("etDeleteFile".equalsIgnoreCase(operation)){//删除附件
			etDeleteFile(requestBean, responseBean);
		}else if("overdueSearch".equalsIgnoreCase(operation)){//逾期查询
			overdueSearch(requestBean, responseBean);
		}else if("etModifyHandle".equalsIgnoreCase(operation)){//办结修改
			etModifyHandle(requestBean, responseBean);
		}else if("showModify".equalsIgnoreCase(operation)){//打开修改
			showModify(requestBean, responseBean);
		}else if("getSourceList".equalsIgnoreCase(operation)){
			getSourceList(requestBean, responseBean);
		}else if("checkBackDeal".equalsIgnoreCase(operation)){//收回功能前校验
			checkBackDeal(requestBean, responseBean);
		}else if("queryFlowNo".equalsIgnoreCase(operation)) {
			queryFlowNo(requestBean, responseBean);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void overdueSearch(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		Map retMap = new HashMap();
		BusiForm busiformTb = (BusiForm) requestBean.getParameterList().get(0);
		// 构造条件参数
		Map<String, Object> otherCondition = (Map<String, Object>)sysMap.get("otherCondition");
		String state = (String) otherCondition.get("state");

		User user = BaseUtil.getLoginUser();
		// 构造条件参数
		HashMap condMap = new HashMap();
		
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
		Page page = PageHelper.startPage(currentPage, pageSize);
		
		if (busiformTb.getNetNo() != null && busiformTb.getNetNo().indexOf(",") > 0) {
			condMap.put("netNoList", SqlUtil.getSumArrayList(Arrays.asList(busiformTb.getNetNo().split(","))));
			busiformTb.setNetNo(null);
		}
		condMap.put("busiForm", busiformTb);
		condMap.put("state", state);
		condMap.put("userNo", user.getUserNo());
		condMap.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构

		List<BusiForm> taskList = busiFormMapper.getOverBusiFormList(condMap);
		
		if("0".equals(state)){//作业机构
			for (BusiForm busiForm : taskList) {
				String endDate = null;
				String realBackDate = "";
				List listStr = etProcessTbMapper.getEtProcessTbForId(BaseUtil.filterSqlParam(busiForm.getFormId()));
				if(listStr !=null && listStr.size() > 0){
					String tmpDate = ((Map)listStr.get(listStr.size()-1)).get("DEAL_DATE")+"";
					if(busiForm.getBackDate().compareTo(tmpDate) < 0){
						endDate = tmpDate;
					}else{
						endDate = ((Map)listStr.get(0)).get("DEAL_DATE")+"";
					}
					if(listStr.size() == 1){
						realBackDate = "首次："+((Map)listStr.get(0)).get("DEAL_DATE")+"";
					}else if(listStr.size()==2){
						realBackDate = "一次："+((Map)listStr.get(1)).get("DEAL_DATE")+""+"<br/>二次："+((Map)listStr.get(0)).get("DEAL_DATE")+"";
					}else{
						realBackDate = "最后："+((Map)listStr.get(0)).get("DEAL_DATE")+"";
					}
					if(BaseUtil.isBlank(busiForm.getOverDays())){
						busiForm.setOverDays(busiFormMapper.getOverDurDays(BaseUtil.filterSqlParam(busiForm.getBackDate()),BaseUtil.filterSqlParam(endDate)));
					}
					busiForm.setRealBackDate(realBackDate);
				}else{
					if(BaseUtil.isBlank(busiForm.getOverDays())){
						busiForm.setOverDays(busiFormMapper.getOverDurDays1(BaseUtil.filterSqlParam(busiForm.getBackDate())));
					}
					busiForm.setRealBackDate("作业机构未反馈");
				}	
			}
		}
		
		
		retMap.put("currentPage", currentPage);
		retMap.put("pageSize", pageSize);
		retMap.put("totalNum", page.getTotal());
		retMap.put("returnList", taskList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * 系统登录初始化数据方法
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loginInit(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		List<String> belongList = new ArrayList<String>();
		belongList.add("0");
		List slipTypeList = null;
		String qdParameter = (String)requestBean.getSysMap().get("qdParameter");
		if ("qd".equals(qdParameter)) {
			User user = BaseUtil.getLoginUser();
			slipTypeList = slipDefMapper.getTypeByBelongByQd(belongList,user.getOrganNo());
		} else {
			slipTypeList = slipDefMapper.getTypeByBelong(belongList);
		}
		Map retMap = new HashMap();
		retMap.put("slipTypeList", slipTypeList); //差错归类列表
		
		Map<String, TreeMap<String, List<ProcessLineBean>>> lines= SystemConstants.CHART_AND_LINE;
		Map<String,ProcessChartBean> charts = SystemConstants.ALL_ET_CHARTS;
		Map<String, ProcessNodeBean> nodes = SystemConstants.ALL_ET_NODES;
		Map<String,List<Map<String, String>>> etConfigList = new HashMap<String,List<Map<String, String>>>();
		for (Entry<String,ProcessChartBean> entry: charts.entrySet()) {
			if(!etConfigList.containsKey("formTypeList")) {
				etConfigList.put("formTypeList", new ArrayList<Map<String, String>>());
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", entry.getKey());
			map.put("name", entry.getValue().getChartName());
			etConfigList.get("formTypeList").add(map);
		}
		Map<String,List<Map<String, String>>> nodeList = new HashMap<String,List<Map<String, String>>>();
		for (Entry<String, ProcessNodeBean> entry: nodes.entrySet()) {
			if(!nodeList.containsKey(entry.getValue().getFlowChart())) {
				nodeList.put(entry.getValue().getFlowChart(), new ArrayList<Map<String, String>>());
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", entry.getValue().getNodeNo());
			map.put("name", entry.getValue().getLabelProcess());
			map.put("nodeName", entry.getValue().getNodeName());
			map.put("isForAdd", entry.getValue().getIsForAdd());
			nodeList.get(entry.getValue().getFlowChart()).add(map);
		}
		/*Map<String,List<Map<String, String>>> lineList = new HashMap<String,List<Map<String, String>>>();
		for (Entry<String, ProcessNodeBean> entry: lines.entrySet()) {
			if(!nodeList.containsKey(entry.getKey())) {
				nodeList.put(entry.getKey(), new ArrayList<Map<String, String>>());
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", entry.getValue().getNodeNo());
			map.put("name", entry.getValue().getLabelProcess());
			map.put("nodeName", entry.getValue().getNodeName());
			map.put("isForAdd", entry.getValue().getIsForAdd());
			nodeList.get(entry.getKey()).add(map);
		}*/
		TreeMap<String, BusiNodeBean> busiNodes = SystemConstants.HT_BUSI_NODES;
		Map<String,List<Map<String, Object>>> busiNodeList = new HashMap<String,List<Map<String, Object>>>();
		for (Entry<String, BusiNodeBean> entry: busiNodes.entrySet()) {
			if(!busiNodeList.containsKey(entry.getKey())) {
				busiNodeList.put(entry.getKey(), new ArrayList<Map<String, Object>>());
			}
			if(entry.getValue().getUtilNodes() != null){
				for (Object utilNode : entry.getValue().getUtilNodes()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("value", ((UtilNodeBean)utilNode).getFormType()+"|"+((UtilNodeBean)utilNode).getNodeNo());
					map.put("name", ((UtilNodeBean)utilNode));
					busiNodeList.get(entry.getKey()).add(map);
				}
			}
		}
		String organLevel = "0";
		List<HashMap<String, Object>> organInfoMapList = organInfoDao.selectSiteInfo(BaseUtil.getLoginUser().getOrganNo());
		if(organInfoMapList != null && organInfoMapList.size() > 0){
			organLevel = ((Map)organInfoMapList.get(0)).get("ORGAN_LEVEL").toString();
		}
		retMap.put("userOrganLevle", organLevel);
		retMap.put("etFilePath", SystemConstants.ETUPLOAD_Folder+"/"+DateUtil.getNow());
		retMap.put("nodeList", nodeList);
		retMap.put("etConfigList", etConfigList);
		retMap.put("busiNodeList", busiNodeList);
		//retMap.put("utilNodeList", SystemConstants.LIST_UTIL_NODES);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 保存处理单据方法
	 */
	@Override
	public void add(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
		String addThisSys = (String)sysMap.get("addThisSys");
		// 当修改时，或者 来源为0 或 9或1时，从本系统添加或修改处理单
		if ((busiForm.getFormId() != null && busiForm.getFormId().trim().length() > 0)
				|| (SystemConstants.SOURCE_FLAG_0.equals(busiForm.getSourceFlag())
						|| "1".equals(addThisSys))) {
			logger.info("从本系统添加或修改处理单........");
			addFromThisSYS(requestBean, responseBean,busiForm); // 从本系统添加处理单
		} else {
			logger.info("从其他系统批量添加处理单........");
			addFromOtherSYS(requestBean, responseBean,busiForm); // 从其他系统批量添加处理单
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addFromThisSYS(RequestBean requestBean, ResponseBean responseBean, BusiForm busiformTb) throws Exception {
		// TODO Auto-generated method stub
		String isSaveFromThis="FALSE";
		Map sysMap = requestBean.getSysMap();
		// 构造条件参数
		HashMap condMap = new HashMap();
		String saveType = (String)sysMap.get("save_type"); //submit-保存，draft-保存为草稿
		//String riskSourceRemak = (String)sysMap.get("riskSourceRemak"); //riskSourceRemak
		//busiformTb.setRiskSourceRemak(riskSourceRemak);
		String formType = busiformTb.getFormType();
		if (!isSaveDraft(saveType, busiformTb)) { // 判断是否是保存为草稿
			if (SystemConstants.FORM_TYPE_0.equals(formType)) {
				busiformTb.setNodeNo(SystemConstants.NEW_NODE_CODE_0);// 新增
				busiformTb.setXfDate(DateUtil.getToday("yyyyMMdd"));
				busiformTb.setXfTime(DateUtil.getToday("HH:mm:ss"));
			} else if (SystemConstants.FORM_TYPE_1.equals(formType)) {
				busiformTb.setNodeNo(SystemConstants.NEW_NODE_CODE_1);// 新增
			} else if (SystemConstants.FORM_TYPE_2.equals(formType)) {
				busiformTb.setNodeNo(SystemConstants.NEW_NODE_CODE_2);// 新增
				busiformTb.setXfDate(DateUtil.getToday("yyyyMMdd"));
				busiformTb.setXfTime(DateUtil.getToday("HH:mm:ss"));
			} else if (SystemConstants.FORM_TYPE_3.equals(formType)) {
				busiformTb.setNodeNo(SystemConstants.NEW_NODE_CODE_3);// 新增
			}
			isSaveFromThis="FALSE";
		}else{
			isSaveFromThis="TRUE";
		}

		boolean isSave = true;
		if (busiformTb.getFormId() != null && busiformTb.getFormId().trim().length() > 0) {
			isSave = false;
		}
		if (busiformTb.getActiveFlag() == null
				|| busiformTb.getActiveFlag().trim().length() == 0) {
			busiformTb.setActiveFlag(SystemConstants.ACTIVE_FLAG_ACT); // 设置ActiveFlag为启用
		}
		if(!BaseUtil.isBlank(busiformTb.getRiskSourceName()) && BaseUtil.isBlank(busiformTb.getRiskSourceNo())){		
			condMap.put("sourceName", busiformTb.getRiskSourceName());
			List<EtRiskSourceDef> rsdList = etRiskSourceDefMapper.selectBySelective(condMap);
			if(rsdList != null && rsdList.size() > 0){
				busiformTb.setRiskSourceNo(BaseUtil.filterSqlParam(rsdList.get(0).getSourceNo()));
			}
		}
		if (busiformTb.getSourceFlag() == null) {
			busiformTb.setSourceFlag(SystemConstants.SOURCE_FLAG_0);
		}

		if (isSave) {
			busiformTb.setWorkTime(DateUtil.getHMS());
			String addFormIdTempString = getFormId(busiformTb.getFormType());
			busiformTb.setFormId(addFormIdTempString);
			// ---------保存处理完成后，返回提示信息--------------------------------------------------------------
			int n = busiFormMapper.insertSelective(busiformTb);
			if(n > 0){
				this.insertBfProcessTb(busiformTb,busiformTb.getNodeNo()+"-下发处理单","02","下发处理单",busiformTb.getBackDate());
				if(isSaveFromThis.equals("TRUE")){
					responseBean.setRetMsg("保存成功！");
				}else{
					responseBean.setRetMsg("提交成功！"+addFormIdTempString);
					Map retMap = new HashMap();
					retMap.put("fromId", addFormIdTempString);
					responseBean.setRetMap(retMap);
				}
				// 添加日志信息
				String logContent = "新增处理单信息，||{处理单号：" + addFormIdTempString + "}";
				//addOperLogInfo(ARSConstants.MODEL_NAME_ET,ARSConstants.OPER_TYPE_1,logContent);
				addOperLogInfo(ARSConstants.MODEL_NAME_QD,ARSConstants.OPER_TYPE_1,logContent);
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			}else{
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			}
		} else {
			int n = busiFormMapper.updateByPrimaryKeySelective(busiformTb);
			BusiForm newBusiformTb = busiFormMapper.selectByPrimaryKey(busiformTb.getFormId());
			// ---------修改处理完成后，返回当前处理页任务列表--------------------------------------------------------------
			if(n > 0){
				if(isSaveFromThis.equals("TRUE")){
					Map retMap = new HashMap();
					retMap.put("newBusiformTb", newBusiformTb);
					responseBean.setRetMap(retMap);
					responseBean.setRetMsg("保存成功！");
				}else{
					responseBean.setRetMsg("提交成功！");
				}
				// 添加日志信息
				String logContent = "修改处理单信息，||{处理单号：" + newBusiformTb.getFormId() + "}";
				//addOperLogInfo(ARSConstants.MODEL_NAME_ET,ARSConstants.OPER_TYPE_3,logContent);
				addOperLogInfo(ARSConstants.MODEL_NAME_QD,ARSConstants.OPER_TYPE_3,logContent);
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			}else{
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addFromOtherSYS(RequestBean requestBean, ResponseBean responseBean, BusiForm busiformTb) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		// 构造条件参数
		HashMap condMap = new HashMap();
		String is_need_back = (String)sysMap.get("is_need_back"); //是否需要反馈
		Integer is_bf = (Integer)sysMap.get("is_bf");//补发
		String isHis = String.valueOf(sysMap.get("isHis"));
		String nodeNo = String.valueOf(sysMap.get("nodeNo"));
		String dealResult = (String)sysMap.get("dealResult");
		String formType = busiformTb.getFormType();  //处理单类型
		String isHandle = (String)sysMap.get("isHandle");//模型处理状态

		//获取驱动因素的id
		if(!BaseUtil.isBlank(busiformTb.getRiskSourceName()) && BaseUtil.isBlank(busiformTb.getRiskSourceNo())){
			condMap.put("sourceName", busiformTb.getRiskSourceName());
			List<EtRiskSourceDef> rsdList = etRiskSourceDefMapper.selectBySelective(condMap);
			if(rsdList != null && rsdList.size() > 0){
				busiformTb.setRiskSourceNo(rsdList.get(0).getSourceNo());
			}
		}
		//是否是补发(撤销补发不记录状态)
		if(is_bf == 1 && !"99".equals(isHandle)){
			busiformTb.setIsBf(String.valueOf(is_bf));
		}
		if (SystemConstants.FORM_TYPE_0.equals(formType)) {
			busiformTb.setNodeNo(SystemConstants.NEW_NODE_CODE_0);// 新增差错单
			busiformTb.setXfDate(DateUtil.getToday("yyyyMMdd"));
			busiformTb.setXfTime(DateUtil.getToday("HH:mm:ss"));
		} else if (SystemConstants.FORM_TYPE_2.equals(formType)) {
				//为核实单是否需要反馈添加节点选择
				if(null != is_need_back && !"".equals(is_need_back) && is_need_back.equals("1") ){
					busiformTb.setNodeNo(SystemConstants.NEW_NODE_CODE_2);// 新增提示单
				}else{
					busiformTb.setNodeNo(SystemConstants.NEW_NODE_CODE_10);
				}
				busiformTb.setXfDate(DateUtil.getToday("yyyyMMdd"));
				busiformTb.setXfTime(DateUtil.getToday("HH:mm:ss"));
		} else if (SystemConstants.FORM_TYPE_3.equals(formType)) {
				busiformTb.setNodeNo(SystemConstants.NEW_NODE_CODE_3);// 新增预警单
				busiformTb.setXfDate(DateUtil.getToday("yyyyMMdd"));
				busiformTb.setXfTime(DateUtil.getToday("HH:mm:ss"));
		}
		if(BaseUtil.isBlank(busiformTb.getWorkDate())){//预警使用提取时间当做登记时间
			busiformTb.setWorkDate(DateUtil.getToday("yyyyMMdd"));
			busiformTb.setWorkTime(DateUtil.getToday("HH:mm:ss"));
		}
		if (SystemConstants.SOURCE_FLAG_2.equals(busiformTb.getSourceFlag())) {
			busiformTb.setIshandle(busiformTb.getNodeNo());
		}
		if (!StringUtil.checkNull(nodeNo)){//如果前端有传节点号,就直接用
			busiformTb.setNodeNo(nodeNo);
		}
		busiformTb.setFormDescription(busiformTb.getFormDescription()); // 详细描述
		busiformTb.setCheckerNo(BaseUtil.getLoginUser().getUserNo()); // 监督员号
		busiformTb.setCheckerName(BaseUtil.getLoginUser().getUserName()); // 监督员名称
		busiformTb.setCheckerOrganNo(BaseUtil.getLoginUser().getOrganNo()); // 监督员所属机构号
		busiformTb.setActiveFlag(SystemConstants.ACTIVE_FLAG_ACT);

		//组装busiformTbList  批量和单笔
		Map map  = packagArmsFormData(requestBean,responseBean,busiformTb,isHis);
		List<BusiForm> busiForms = (List<BusiForm>) map.get("busiForms");
		List<EtProcessTb> processTbs = (List<EtProcessTb>) map.get("processTbs");
		if(processTbs.size() != 0 && busiForms.size() != processTbs.size()){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("下发单据失败,部分数据已更新,请刷新重试!");
			return;
		}
		
		if(is_bf != 1 && !"".equals(map.get("failRowID"))){//非补发，有处理的数据
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("下发单据失败,存在已处理数据,请刷新重试!");
			return;
		}
		
		List<String> fromIdList= new ArrayList<String>();
		String fromId = null;
		for (int i = 0; i < busiForms.size(); i++) {
			BusiForm busiformTb_ = busiForms.get(i);

			String modelTableName = "";
			if ("0".equals(isHis))
				modelTableName = busiformTb_.getTableName();
			else
				modelTableName = busiformTb_.getTableName()+"_HIS";
			//分批处理批量信息
			//验证数据是否已经被处理		是否需要？
			//当预警信息为提示类且无需反馈时不生成单据
			if("2".equals(busiformTb_.getFormType()) && "0".equals(is_need_back)){
					logger.info("预警信息为提示类且无需反馈时不生成单据,更新预警信息"+BaseUtil.filterLog(busiformTb_.getEntryId()) +","+BaseUtil.filterLog(busiformTb_.getEntryrowId()));
					int VVNT = modelDataQueryMapper.handleModelTask(modelTableName,busiformTb_.getFormType()+"9",BaseUtil.getLoginUser().getUserNo(),
							com.sunyard.ars.common.util.date.DateUtil.getNewDate(com.sunyard.ars.common.util.date.DateUtil.FORMATE_DATE_yyyyMMdd),"",
							busiformTb_.getEntryId(),busiformTb_.getEntryrowId(),is_bf);

				//对于补发的预警信息，不需要再次进行统计更新
				if(is_bf != 1){
					etDataStatisticsMapper.updateStatistic(VVNT,busiformTb_.getDeal_state(),busiformTb_.getEntryId(),
							busiformTb_.getNetNo(),busiformTb_.getProcDate());
				}

				logger.info("提示类更新提示类存储过程sql:{call SP_ARMS_ENTRYDATA_HS(?,?,?)};参数："+BaseUtil.filterLog(busiformTb_.getEntryId())+","+BaseUtil.filterLog(busiformTb_.getNetNo())+","+BaseUtil.filterLog(busiformTb_.getProcDate()));
				etDataStatisticsMapper.spArmsEntrydataHs(BaseUtil.filterSqlParam(busiformTb_.getEntryId()),
						BaseUtil.filterSqlParam(busiformTb_.getNetNo()),BaseUtil.filterSqlParam(busiformTb_.getProcDate()));
			}else {
				fromId = getFormId(busiformTb_.getFormType());
				busiformTb_.setFormId(fromId);
				logger.info("预警信息生成单据,更新预警信息"+BaseUtil.filterLog(busiformTb_.getEntryId()) +","+BaseUtil.filterLog(busiformTb_.getEntryrowId()));
				busiFormMapper.insertSelective(busiformTb_);
				if (!StringUtil.checkNull(nodeNo)){//直接办结插记录
					this.insertBfProcessTb(busiformTb_,dealResult,"-1","监测员审核预警",null);
				}
				if(processTbs.size() != 0){//撤销提示类无需反馈 补发添加补发记录
					processTbs.get(i).setFormId(busiformTb_.getFormId());
					//插入撤销记录
					etProcessTbMapper.insertSelective(processTbs.get(i));
					//插入补发记录
					this.insertBfProcessTb(busiformTb_,busiformTb_.getNodeNo()+"-历史预警补发处理单","01","历史预警补发处理单",busiformTb_.getBackDate());
				}
				
				if (StringUtil.checkNull(nodeNo) && is_bf != 1){//下发非直接办结插入下发流程记录
					this.insertBfProcessTb(busiformTb_,busiformTb_.getNodeNo()+"-预警下发处理单","02","分行下发处理单",busiformTb_.getBackDate());
				}
				
				int VVNT  = modelDataQueryMapper.handleModelTask(modelTableName,busiformTb_.getFormType()+"9",BaseUtil.getLoginUser().getUserNo(),
						com.sunyard.ars.common.util.date.DateUtil.getNewDate(com.sunyard.ars.common.util.date.DateUtil.FORMATE_DATE_yyyyMMdd),fromId,
						busiformTb_.getEntryId(),busiformTb_.getEntryrowId(),is_bf);

				//对于补发的预警信息，不需要再次进行统计更新
				if(is_bf != 1){
					String deal_state = busiformTb_.getDeal_state();
					if("null".equals(deal_state)){
					    // Object o = null ; String.valueof(o) 是字符串的"null",
                        // db2用perparedStatement的setString "null"  decode(?,'1',0,1) 会报错
                        etDataStatisticsMapper.updateStatistic(VVNT,null,busiformTb_.getEntryId(),
                                busiformTb_.getNetNo(),busiformTb_.getProcDate());
                    }else{
                        etDataStatisticsMapper.updateStatistic(VVNT,busiformTb_.getDeal_state(),busiformTb_.getEntryId(),
                                busiformTb_.getNetNo(),busiformTb_.getProcDate());
                    }
				}
			}
			if(fromId!=null){
				fromIdList.add(fromId);
			}
		}
		Map retMap = new HashMap();
		retMap.put("fromIdList",fromIdList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		if("0".equals(is_need_back) ){//无需反馈
			responseBean.setRetMsg("下发成功");
		}else{
			if (!StringUtil.checkNull(nodeNo)){//直接办结
				responseBean.setRetMsg("单据办结成功");
			}else{
				responseBean.setRetMsg("下发单据成功");
			}
		}
	}

	/**
	 * 组装查询信息
	 * @param requestBean
	 * @param responseBean
	 * @param busiformTb
	 * @return
	 */
	private Map packagArmsFormData(RequestBean requestBean, ResponseBean responseBean, BusiForm busiformTb,String isHis) throws Exception{
		Map<String,Object> returnMap = new HashMap<>();
		List<BusiForm>  busiForms  = new ArrayList<>();
		List<EtProcessTb>  processTbs  = new ArrayList<>();
		//风险预警过来的数据只传递modelId,modelRowId   其他由差错系统自行组装
		List<Integer>  modelRowIds= busiformTb.getEntryrowIds();
		String modelId= BaseUtil.filterSqlParam(busiformTb.getEntryId());
		//获取模型信息
		Model  modelInfo  = modelMapper.getModelInfo(Integer.valueOf(modelId));
		//查询数据信息
		String modelTableName = "";
		if ("0".equals(isHis))
			modelTableName = modelInfo.getTableName();
		else
			modelTableName = modelInfo.getTableName()+"_HIS";
		List<HashMap<String, Object>> modelDates = modelDataQueryMapper.getManyModelData(Integer.valueOf(modelId), modelRowIds, BaseUtil.filterSqlParam(modelTableName));
		//确定需要反馈的天数;
		Integer feedbackDays =  modelInfo.getFeedbackDays();
		if (feedbackDays == null) {
			feedbackDays= 1;
		}
		//遍历数据组成差错信息
		BusiForm busiForm  = null;
		EtProcessTb processTb = null;
		String failRowID = "";
		for (int x = 0 ;x < modelDates.size() ; x++){
			busiForm = new BusiForm();
			HashMap<String,Object>  modelDate = modelDates.get(x);
			
			if(!"0".equals(String.valueOf(modelDate.get("ISHANDLE")))){ //该模型已经被处理
                failRowID += "任务:"+BaseUtil.filterSqlParam(modelDate.get("ENTRYROW_ID")+"")+"已经被处理";
            }
			
			busiForm.setFormType(busiformTb.getFormType());  //表单类型
			try{
				busiForm.setWorkDate(BaseUtil.filterSqlParam(String.valueOf(modelDate.get("PROC_DATE"))));
			}catch(Exception e){
				busiForm.setWorkDate(busiformTb.getWorkDate());
			}			
			busiForm.setWorkTime(busiformTb.getWorkTime());
			busiForm.setXfDate(busiformTb.getXfDate());
			busiForm.setXfTime(busiformTb.getXfTime());
			busiForm.setSlipNo(busiformTb.getSlipNo());     //差错类型
			busiForm.setSlipName(busiformTb.getSlipName()); //差错名称
			busiForm.setSlipLevel(busiformTb.getSlipLevel());//差错级别
			busiForm.setSlipType(busiformTb.getSlipType());  //差错类别
			busiForm.setCheckerNo(busiformTb.getCheckerNo());
			busiForm.setCheckerName(busiformTb.getCheckerName());
			busiForm.setCheckerOrganNo(busiformTb.getCheckerOrganNo());
			busiForm.setCheckerOrganName(busiformTb.getCheckerOrganName());
			busiForm.setSourceFlag(busiformTb.getSourceFlag());
			busiForm.setNodeNo(busiformTb.getNodeNo());
			busiForm.setActiveFlag(busiformTb.getActiveFlag());
			busiForm.setIshandle(busiformTb.getIshandle());
			busiForm.setIsBf(busiformTb.getIsBf());
			busiForm.setRiskSourceRemak(busiformTb.getRiskSourceRemak());
			busiForm.setFormDescription(busiformTb.getFormDescription());
			busiForm.setAmerceMoney(busiformTb.getAmerceMoney());
			busiForm.setAmerceScore(busiformTb.getAmerceScore());
			busiForm.setRiskSourceType(busiformTb.getRiskSourceType());
			busiForm.setRiskSourceName(busiformTb.getRiskSourceName());
			busiForm.setRiskSourceNo(busiformTb.getRiskSourceNo());
			busiForm.setBackDate(busiformTb.getBackDate());
			busiForm.setTableName(BaseUtil.filterSqlParam(modelInfo.getTableName()));
			if (StringUtil.checkNull(busiformTb.getSlipName())){
				busiForm.setSlipName(BaseUtil.filterSqlParam(modelInfo.getName()));
			}
			busiForm.setBusiNoCt(BaseUtil.filterSqlParam(modelInfo.getModelBusiType()));
			busiForm.setRiskDetail(BaseUtil.filterSqlParam(modelInfo.getDescription()));
			busiForm.setCcLeader(busiformTb.getCcLeader());
			busiForm.setNetNo(busiformTb.getNetNo());
			busiForm.setNetName(busiformTb.getNetName());
			busiForm.setCcRole(busiformTb.getCcRole());
			busiForm.setCcOrgan(busiformTb.getCcOrgan());
			busiForm.setCcType(busiformTb.getCcType());
			busiForm.setQdSlipMoid(busiformTb.getQdSlipMoid());
			armsModelDataToSendSlip(modelDate,busiForm);
			busiForms.add(busiForm);
			if("99".equals(modelDate.get("FORMTYPE"))){//撤销补发记录
				processTb = new EtProcessTb();
				processTb.setFormType(busiformTb.getFormType());
				processTb.setNodeNo("-1");//设置初始节点
				processTb.setCreateTime(BaseUtil.filterSqlParam(modelDate.get("ALERT_DATE")+"") + " 00:00:00");
				processTb.setDealUserNo(BaseUtil.filterSqlParam(modelDate.get("CHECKER_NO") + ""));
				processTb.setDealUserName(BaseUtil.filterSqlParam(modelDate.get("CHECKER_NO") + ""));
				processTb.setDealOrganNo(BaseUtil.filterSqlParam(modelDate.get("DEAL_ORGAN") + ""));
				processTb.setDealDate(BaseUtil.filterSqlParam(modelDate.get("ALERT_DATE")+""));
				processTb.setDealDescription(BaseUtil.filterSqlParam(modelDate.get("ALERT_CONTENT")+""));
				processTb.setProcessShowLevel("all");
				processTb.setDealResultText("01-预警撤销");
				processTb.setLabelProcess("分行监测员撤销预警");
				processTbs.add(processTb);
			}
			if("29".equals(modelDate.get("FORMTYPE")) && !BaseUtil.isBlank(modelDate.get("CHECK_STATE")+"")){//提示类无需反馈记录
				processTb = new EtProcessTb();
				processTb.setFormType(busiformTb.getFormType());
				processTb.setNodeNo("-1");//设置初始节点
				processTb.setCreateTime(BaseUtil.filterSqlParam(modelDate.get("ALERT_DATE")+"") + " 00:00:00");
				processTb.setDealUserNo(BaseUtil.filterSqlParam(modelDate.get("CHECKER_NO") + ""));
				processTb.setDealUserName(BaseUtil.filterSqlParam(modelDate.get("CHECKER_NO") + ""));
				processTb.setDealOrganNo(BaseUtil.filterSqlParam(modelDate.get("DEAL_ORGAN") + ""));
				processTb.setDealDate(BaseUtil.filterSqlParam(modelDate.get("ALERT_DATE")+""));
				processTb.setDealDescription(BaseUtil.filterSqlParam(modelDate.get("ALERT_CONTENT")+""));
				processTb.setProcessShowLevel("all");
				processTb.setDealResultText("01-提示类下发无需反馈");
				processTb.setLabelProcess("分行监测员提示类下发无需反馈");
				processTbs.add(processTb);
			}
		}
		returnMap.put("failRowID", failRowID);
		returnMap.put("busiForms", busiForms);
		returnMap.put("processTbs", processTbs);
		return returnMap;
	}


	/**
	 * 模型数据转差错
	 * @param modelDate
	 * @param busiForm
	 */
	public void armsModelDataToSendSlip(Map<String,Object> modelDate,BusiForm busiForm){
		try{
			busiForm.setAlarmCreateTime(String.valueOf(modelDate.get("CREATE_DATE")));
		}catch(Exception e){
			busiForm.setAlarmCreateTime("");
		}

		try{
			busiForm.setAccountName(String.valueOf(modelDate.get("ACCT_NAME")== null?"":modelDate.get("ACCT_NAME")));
		}catch(Exception e){
			busiForm.setAccountName("");
		}
		try{
			busiForm.setAccountNo(String.valueOf(modelDate.get("ACCT_NO")== null?"":modelDate.get("ACCT_NO")));

		}catch(Exception e){
			busiForm.setAccountNo("");
		}


		if(BaseUtil.isBlank(busiForm.getNetNo())){
			// 网点号
			try{
				busiForm.setNetNo(String.valueOf(modelDate.get("BANKCODE")));
			}catch(Exception e){
				busiForm.setNetNo("");
			}
		}
		
		if(BaseUtil.isBlank(busiForm.getNetName())){
			//机构名称
			try{
				busiForm.setNetName(String.valueOf(modelDate.get("BANKNAME")));
			}catch(Exception e){
				busiForm.setNetName("");
			}
		}
		
		//警报风险机构
		try{
			busiForm.setRiskOrgan(String.valueOf(modelDate.get("RISK_ORGAN")== null?"":modelDate.get("RISK_ORGAN")));
		}catch(Exception e){
			busiForm.setRiskOrgan("");
		}
		//柜员号
		try{
			busiForm.setTellerNo(String.valueOf(modelDate.get("TELLER") == null?"":modelDate.get("TELLER")));
		}catch(Exception e){
			busiForm.setTellerNo("");
		}

		try{
			busiForm.setCurrencyType(String.valueOf(modelDate.get("CURRENCY")== null?"":modelDate.get("CURRENCY")));
		}catch(Exception e){
			busiForm.setCurrencyType("");
		}
		try{
			busiForm.setEntryName(String.valueOf(modelDate.get("ENTRY_NAME")));
		}catch(Exception e){
			busiForm.setEntryName("");
		}
		try{
			busiForm.setEntryrowId(String.valueOf(modelDate.get("ENTRYROW_ID")));
		}catch(Exception e){
			busiForm.setEntryrowId("");
		}
		try{
			busiForm.setEntryId(String.valueOf(modelDate.get("ENTRY_ID")));
		}catch(Exception e){
			busiForm.setEntryId("");
		}

		try{
			busiForm.setAlarmLevel(String.valueOf(modelDate.get("ENTRY_LEVEL")));
		}catch(Exception e){
			busiForm.setEntryId("");
		}

		try{
			busiForm.setFlowNo(String.valueOf(modelDate.get("FLOW_ID") == null?"":modelDate.get("FLOW_ID")));
		}catch(Exception e){
			busiForm.setFlowNo("");
		}
		try{
			busiForm.setDeal_state(String.valueOf(modelDate.get("DEAL_STATE")));
		}catch(Exception e){
			busiForm.setDeal_state("");
		}
		try{
			busiForm.setProcDate(String.valueOf(modelDate.get("PROC_DATE")));
		}catch(Exception e){
			busiForm.setProcDate("");
		}

		try{
			busiForm.setOperationDate(String.valueOf(modelDate.get("BUSI_DATA_DATE")));
		}catch(Exception e){
			busiForm.setOperationDate("");
		}

	}
	
	/**
	 * 判断是否是保存为草稿   私有方法
	 * 
	 * @param type
	 * @param busiformTb
	 */
	private boolean isSaveDraft(String type,BusiForm busiformTb) {
		if ("draft".equalsIgnoreCase(type)) {
			if (busiformTb.getNodeNo() == null
					|| busiformTb.getNodeNo().trim().length() == 0 ||(busiformTb.getFormType().equals(SystemConstants.FORM_TYPE_3)&&busiformTb.getNodeNo().equals("24"))
                    ||(busiformTb.getFormType().equals(SystemConstants.FORM_TYPE_0)&&busiformTb.getNodeNo().equals("24"))					
			) {
				busiformTb.setNodeNo(SystemConstants.NEW_NODE_CODE_DRAFT);
			}
			return true;
		} else {
			return false;
		}
	}
	
	

	@SuppressWarnings("rawtypes")
	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		List delList = requestBean.getParameterList();
		for(int i=0; i<delList.size(); i++) {
			BusiForm busiForm = (BusiForm)delList.get(i);
			busiFormMapper.deleteByPrimaryKey(busiForm.getFormId());
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功！");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		BusiForm busiFormNew = (BusiForm) requestBean.getParameterList().get(0);
		busiFormMapper.updateByPrimaryKeySelective(busiFormNew);
		BusiForm busiForm = busiFormMapper.selectByPrimaryKey(busiFormNew.getFormId());
		Map retMap = new HashMap();
		retMap.put("newBusiForm", busiForm);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		String queryType = String.valueOf(sysMap.get("queryType"));
		if("myTask".equals(queryType)) {
			queryMyTask(requestBean, responseBean);
		}else {
			BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
			Map<String, Object> otherCondition = (Map<String, Object>)sysMap.get("otherCondition");//日期区间和其他多个值的参数
			//将busiForm属性中“,”分隔的多个值的属性拆分后放入otherCondition
			String netNo = busiForm.getNetNo();//机构
			if(netNo != null && netNo.length()>0) {
				if(netNo.indexOf(",") != -1) {
					otherCondition.put("netNoList", SqlUtil.getSumArrayList(Arrays.asList(netNo.split(","))));
					busiForm.setNetNo(null);
				}
			}
			
			String formType = busiForm.getFormType();
			//说明客户选中的是状态下的三个用户中的一个！
			boolean b = "true".equals((String)sysMap.get("isformTypeOfNull"));
			
			
			if(b) {
				if(!BaseUtil.isBlank(formType)) {
					if(formType.indexOf(",")>0){
						//选择的是主管处理,如果节点存在修改！把42节点放到参数表中，从参数表获取
						otherCondition.put("formTypeList",Arrays.asList(formType.split(",")));
						otherCondition.put("NodeNO", 42);
					}else {
						//核查员审核
						otherCondition.put("formType",formType);
						otherCondition.put("NodeNO", 42);
						
					}
					String nodeNO = busiForm.getNodeNo();//状态号
					if(!BaseUtil.isBlank(nodeNO)) {
						nodeNO =nodeNO.replace(",42", "");
						if(nodeNO.indexOf(",")>=0) {
							otherCondition.put("nodeNoListOfMine", Arrays.asList(nodeNO.split(",")));
						}
					}
					busiForm.setFormType(null);
					busiForm.setNodeNo(null);
				}
			}else {
				//选择了单据类型的情况
				String nodeNO = busiForm.getNodeNo();//状态号
				if(nodeNO != null && nodeNO.length()>0 && nodeNO.indexOf(",") != -1) {
					busiForm.setNodeNo(null);
					otherCondition.put("nodeNoList", Arrays.asList(nodeNO.split(",")));
				}
				
			}
			otherCondition.put("userNo", BaseUtil.getLoginUser().getUserNo());//过滤权限机构
			
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
			otherCondition.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));
			Page page = PageHelper.startPage(currentPage, pageSize);
			if(null == otherCondition.get("orderField")){
				otherCondition.put("orderBy", " ORDER BY FORM_TYPE,PAR_DEAL_DATE,FORM_ID");
			}else{
				otherCondition.put("orderBy", " ORDER BY " + otherCondition.get("orderField") + " " + otherCondition.get("sort") + ",FORM_TYPE,PAR_DEAL_DATE,FORM_ID");
			}
			
			List resultList = busiFormMapper.selectBySelective(busiForm, otherCondition);
			long totalCount = page.getTotal();
			Map retMap = new HashMap();
			retMap.put("currentPage", currentPage);
			retMap.put("pageSize", pageSize);
			retMap.put("totalNum", totalCount);
			retMap.put("returnList", resultList);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}
	}

	/**
	 * 初始化表单添加页面，要在配置文件中配置该方法
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void saveOrUpdateBusiformTb(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		String retCode = ARSConstants.HANDLE_SUCCESS;
		String retMsg = "操作成功";
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("formId", busiForm.getFormId());
		List voucherList = ARSConstants.LIST_VOUCHERINFO; // 取凭证类型列表，供WEB页面调用。
		//List operationClassList = SystemConstants.LIST_OPERATION_CLASS; // 处理对象，在数据库全局变量表中配置，系统启动时赋值(弃用)

		List<String> belongList = new ArrayList<String>();
		belongList.add(SystemConstants.FORM_TYPE_0);
		
		List slipTypeList = null;
		String qdParameter = (String)requestBean.getSysMap().get("qdParameter");
		if ("qd".equals(qdParameter)) {
			User user = BaseUtil.getLoginUser();
			slipTypeList = slipDefMapper.getTypeByBelongByQd(belongList,user.getOrganNo());
		} else {
			slipTypeList = slipDefMapper.getTypeByBelong(belongList);
		}
		
		String entryId = requestBean.getSysMap().get("entryId") == null?"":requestBean.getSysMap().get("entryId")+"";
		
		Map<String,List<Map<String, String>>> riskSourceMap = new HashMap<String,List<Map<String, String>>>();
		List typeList = null;
		if(!BaseUtil.isBlank(entryId)){
			condMap.put("entryId", entryId);
			typeList = etRiskSourceDefMapper.selectModelSourceType(condMap);
			List riskNameList = etRiskSourceDefMapper.selectModelSourceName(condMap);
			for (Object riskSource : riskNameList) {
				if(!riskSourceMap.containsKey(((Map)riskSource).get("SOURCE_TYPE"))){
					riskSourceMap.put(((Map)riskSource).get("SOURCE_TYPE").toString(), new ArrayList<Map<String, String>>());
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", ((Map)riskSource).get("SOURCE_NO")+"|"+((Map)riskSource).get("IS_NEED"));
				map.put("name", ((Map)riskSource).get("SOURCE_NAME").toString());
				riskSourceMap.get(((Map)riskSource).get("SOURCE_TYPE")).add(map);
			}
		}else{
			typeList = etRiskSourceDefMapper.selectSourceType();//获取问题类型列表
			List<EtRiskSourceDef>  riskSourceList = etRiskSourceDefMapper.selectBySelective(condMap);//获取所以值
			for (EtRiskSourceDef riskSource : riskSourceList) {
				if(!riskSourceMap.containsKey(riskSource.getSourceType())){
					riskSourceMap.put(riskSource.getSourceType(), new ArrayList<Map<String, String>>());
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", riskSource.getSourceNo()+"|"+riskSource.getIsNeed());
				map.put("name", riskSource.getSourceName());
				riskSourceMap.get(riskSource.getSourceType()).add(map);
			}
		}
		//List currencyTypeList = sysDAO.getcurrencyType();// List<ParameterBean>
		
		BusiForm busiformTb = null;//返回页面单据信息
		if (busiForm.getFormId() != null && !"".equals(busiForm.getFormId().trim())) {// 如果是修改处理单
			busiformTb = busiFormMapper.selectByPrimaryKey(busiForm.getFormId());
			Map processMap = getDoneBusiProcesses(busiformTb);// 取已处理过的流程信息
			if("false".equals(processMap.get("flag"))){
				retCode = ARSConstants.HANDLE_FAIL;
				retMsg = processMap.get("failMsg")+"";
			}else{
				retMap.put("processList", processMap.get("processList"));
			}
			//添加传统业务的名称
			if(busiformTb.getBusiNoCtName() == null || "".equals(busiformTb.getBusiNoCtName())){
				List list = busiFormMapper.selectBusiInfo(BaseUtil.filterSqlParam(busiformTb.getBusiNoCt()));
				if(list != null && list.size() > 0){
					busiformTb.setBusiNoCtName(((Map)list.get(0)).get("BUSI_NAME")+"");
				}
		    }
			//添加条件用于判断是从修改处理单进入该页面
			retMap.put("awayFrom", "modify");
		}else{//如果是新添加处理单
			busiformTb = new BusiForm();
			busiformTb.setWorkDate(DateUtil.getToday("yyyyMMdd"));
			if (qdParameter != null && !("").equals(qdParameter) && qdParameter.equals("qd")) {
				busiformTb.setBackDate(busiFormMapper.selectWorkDate(2,ARSConstants.DB_TYPE));
			} else {
				busiformTb.setBackDate(busiFormMapper.selectWorkDate(1,ARSConstants.DB_TYPE));
			}
			busiformTb.setBackDate(busiFormMapper.selectWorkDate(1,ARSConstants.DB_TYPE));
			busiformTb.setWorkTime(DateUtil.getToday("HH:mm:ss"));
		}
		busiformTb.setAddFromSYS(SystemConstants.MODEL_NAME); // 页面显示的判断条件上用到
		String backDate = "";
		if((busiformTb.getEntryId() != null && !"".equals(busiformTb.getEntryId())) && !busiformTb.getFormType().equals("0")){
			Model modelInfo = modelMapper.getModelInfo(Integer.valueOf(busiForm.getEntryId()));
			int feedBackDays = modelInfo.getFeedbackDays() == null?1:modelInfo.getFeedbackDays();
			backDate = busiFormMapper.selectWorkDate(Integer.parseInt(BaseUtil.filterSqlParam(feedBackDays+"")),ARSConstants.DB_TYPE);
		}else{
			 if (qdParameter != null && !("").equals(qdParameter) && qdParameter.equals("qd")) {
				 backDate = busiFormMapper.selectWorkDate(2,ARSConstants.DB_TYPE);
				} else {
					backDate = busiFormMapper.selectWorkDate(1,ARSConstants.DB_TYPE);
				}
		}
		if(!"".equals(backDate) && backDate != null){
			busiformTb.setBackDate(backDate);
		}
		if (busiformTb.getSourceFlag() == null)
			busiformTb.setSourceFlag(SystemConstants.SOURCE_FLAG_0); // 从本系统登记的标记
		
		retMap.put("busiformTb", busiformTb);
		retMap.put("typeList", typeList);
		retMap.put("riskSourceMap", riskSourceMap);
		retMap.put("voucherList", voucherList);
		retMap.put("slipTypeList", slipTypeList);
		//retMap.put("currencyTypeList", currencyTypeList);
		retMap.put("userNo", BaseUtil.getLoginUser().getUserNo());
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(retCode);
		responseBean.setRetMsg(retMsg);
	}

	/**
	 * 获取驱动因素列表
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getSourceList(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map retMap = new HashMap();
		String retCode = ARSConstants.HANDLE_SUCCESS;
		String retMsg = "操作成功";
		// 构造条件参数
		/*HashMap condMap = new HashMap();
		List typeList = etRiskSourceDefMapper.selectSourceType();//获取问题类型列表
		Map<String,List<Map<String, String>>> riskSourceMap = new HashMap<String,List<Map<String, String>>>();
		List<EtRiskSourceDef>  riskSourceList = etRiskSourceDefMapper.selectBySelective(condMap);//获取所以值
		for (EtRiskSourceDef riskSource : riskSourceList) {
			if(!riskSourceMap.containsKey(riskSource.getSourceType())){
				riskSourceMap.put(riskSource.getSourceType(), new ArrayList<Map<String, String>>());
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", riskSource.getSourceNo()+"|"+riskSource.getIsNeed());
			map.put("name", riskSource.getSourceName());
			riskSourceMap.get(riskSource.getSourceType()).add(map);
		}*/
		String entryId = requestBean.getSysMap().get("entryId")+"";
		HashMap condMap = new HashMap();
		condMap.put("entryId", entryId);
		List riskType = etRiskSourceDefMapper.selectModelSourceType(condMap);
		List riskNameList = etRiskSourceDefMapper.selectModelSourceName(condMap);
		
		Map<String,List<Map<String, String>>> riskSourceMap = new HashMap<String,List<Map<String, String>>>();
		for (Object riskSource : riskNameList) {
			if(!riskSourceMap.containsKey(((Map)riskSource).get("SOURCE_TYPE"))){
				riskSourceMap.put(((Map)riskSource).get("SOURCE_TYPE").toString(), new ArrayList<Map<String, String>>());
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("value", ((Map)riskSource).get("SOURCE_NO")+"|"+((Map)riskSource).get("IS_NEED"));
			map.put("name", ((Map)riskSource).get("SOURCE_NAME").toString());
			riskSourceMap.get(((Map)riskSource).get("SOURCE_TYPE")).add(map);
		}		
		retMap.put("riskSourceMap", riskSourceMap);
		retMap.put("typeList", riskType);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(retCode);
		responseBean.setRetMsg(retMsg);

	}
	
	/**
	 * 获取我的任务
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryMyTask(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		User user = BaseUtil.getLoginUser();
		Map sysMap = requestBean.getSysMap();
		/*List<String> privOrganList = userOrganMapper.getPrivOrganByUserNo(user.getUserNo());
		BaseUtil.setSessionAttribute("privOrganList", privOrganList);*/
		String menuClass = (String)sysMap.get("menuClass");
		List menuList = new ArrayList();
		for(String mClass : menuClass.split(",")){
			menuList.addAll(busiFormMapper.getMenuList(mClass,user.getUserNo(),ARSConstants.DB_TYPE));
		}
		
		List taskCountList = new ArrayList();
		TreeMap<String, BusiNodeBean> busiNodes = SystemConstants.HT_BUSI_NODES;
		Map taskMenuTree = Collections.synchronizedMap(new TreeMap());
		List taskMenuList = new ArrayList();
		Iterator iterator = busiNodes.keySet().iterator();
		while (iterator.hasNext()) {
			String busiNodeNo = (String) iterator.next();
			BusiNodeBean busiNodeBean = (BusiNodeBean) busiNodes.get(busiNodeNo);
			if ("yes".equalsIgnoreCase(busiNodeBean.getIsTaskList())) {
				taskMenuTree.put(busiNodeBean.getIndex(), busiNodeBean);
			}
		}
		
		for (Object menu : menuList) {
			String url = ((Map)menu).get("MENU_URL")+"";
			int index = url.indexOf("?");
		    if (index != -1) {
		        String[] paramArr = url.substring(index + 1).split("&");
		        for (int i = 0; i < paramArr.length; i++) {
		            String[] arr = paramArr[i].split("=");
		            if ("index".equalsIgnoreCase(arr[0]) && null != taskMenuTree.get(arr[1].trim())) {
		            	((BusiNodeBean)(taskMenuTree.get(arr[1].trim()))).setMethodURL(url);
		            	taskMenuList.add(taskMenuTree.get(arr[1].trim()));
		                break;
		            }
		        }
		    }
		} 
		// 构造条件参数
		HashMap condMap = new HashMap();
		for (int i = 0; i < taskMenuList.size(); i++) {
			condMap.clear();
			BusiNodeBean busiNodeBean = new BusiNodeBean();
			busiNodeBean = (BusiNodeBean) taskMenuList.get(i);
			String busiNodeNo = busiNodeBean.getIndex();
			BusiForm busiformTb = new BusiForm();
			if(busiNodeBean.getMethodURL().indexOf("type") > 0){
				busiformTb.setReturnState(busiNodeBean.getMethodURL().substring(busiNodeBean.getMethodURL().indexOf("type")+5, busiNodeBean.getMethodURL().indexOf("type=")+6));
			}
			busiformTb.setActiveFlag(SystemConstants.ACTIVE_FLAG_ACT); // 只显示有效的单子,对于撤单和删除无效的单子不显示.
			condMap.put("thisPurview_plr", "yes");//20161021 特殊值 ,满足条件说明用于查看抄送信息，抄送信息可忽略权限机构
			if ("yes".equalsIgnoreCase(busiNodeBean.getNeedOrganCondition())) {
				condMap.put("thisPurviewName", "yes");//是否过滤机构
			}
			if ("yes".equalsIgnoreCase(busiNodeBean.getIsCheckerMatch())) {
				busiformTb.setCheckerNo(user.getUserNo()); // 当前登录用户号＝处理单登记人号
			}
			if ("yes".equalsIgnoreCase(busiNodeBean.getIsTellerMatch())) {
				busiformTb.setTellerNo(user.getUserNo()); // 当前登录用户号＝处理单所涉及的柜员号
			}
			/*if ("yes".equalsIgnoreCase(busiNodeBean.getIsLeaderMatch())) {
				busiformTb.setCcLeader(user.getOrganNo()); // 当前登录用户号＝处理单所涉及的抄送领导号
			}*/
			if ("yes".equalsIgnoreCase(busiNodeBean.getIsCheckerNotUserNoMatch())) {
				condMap.put("checkerNo", user.getUserNo()); // 审核员不能审核自己提交的处理单
			}
			if ("yes".equalsIgnoreCase(busiNodeBean.getIsLeaderNotSeanMatch())) {
				condMap.put("roleArray", user.getRoleNo().split(","));
				condMap.put("leaderNotSean", user.getOrganNo());//机构没有查看过的列表
			}
			if ("yes".equalsIgnoreCase(busiNodeBean.getIsLeaderHasSeanMatch())) {
				condMap.put("roleArray", user.getRoleNo().split(","));
				condMap.put("leaderHasSean", user.getOrganNo()); // 机构查看过的列表
			}
			if("yes".equalsIgnoreCase(busiNodeBean.getIsAppointNetUser())){
				condMap.put("isAppointNetUser",user.getUserNo());
			}
			if("yes".equalsIgnoreCase(busiNodeBean.getIsAppointNetArms())){
				condMap.put("isAppointNetArms",user.getUserNo());
			}
			if("yes".equalsIgnoreCase(busiNodeBean.getIsAppointNetManager())){
				condMap.put("isAppointNetManager", user.getUserNo());
			}
			if("yes".equalsIgnoreCase(busiNodeBean.getCorrFlag())){
				condMap.put("corrFlag", user.getUserNo());
			}

			// 业务结点配置中可以对所属单元结点做权限设置<util_node form_type="4" util_node_no="11,12" purview="" >
			List conditionBusiUtilNodes = new ArrayList(); // <utilNodeBean>
															// 转为SQL语句为：form_type=*
															// && node_no in(*,*,*)
			List busi_utilNodes_tmp = busiNodeBean.getUtilNodes();
			if (busi_utilNodes_tmp != null) {
				for (int y = 0; y < busi_utilNodes_tmp.size(); y++) {
					UtilNodeBean utilNodeBean = (UtilNodeBean) busi_utilNodes_tmp.get(y);
					if(!"all".equals(utilNodeBean.getFormType()) && !"all".equals(utilNodeBean.getNodeNo())){
						conditionBusiUtilNodes.add(utilNodeBean);
					}
				}
			}
			condMap.put("conditionBusiUtilNodes", conditionBusiUtilNodes);
			condMap.put("busiForm", busiformTb); // 再查询条件赋值
			condMap.put("userNo", user.getUserNo());//过滤权限机构
			condMap.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
			int taskCount = busiFormMapper.getTaskCount(condMap);

			busiNodeBean.setTaskCount(taskCount);
			taskCountList.add(busiNodeBean);
		
			logger.info("用户任务概述： " + busiNodeBean.getText() + " 任务数： "
					+ busiNodeBean.getTaskCount());
		}
		Map retMap = new HashMap();
		retMap.put("taskCountList", taskCountList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 填写处理单初始数据查询
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void fillBusiInit(RequestBean requestBean, ResponseBean responseBean) throws Exception {	
		List<String> belongList = new ArrayList<String>();
		belongList.add("0");
		List slipTypeList = null;
		String qdParameter = (String)requestBean.getSysMap().get("qdParameter");
		if ("qd".equals(qdParameter)) {
			User user = BaseUtil.getLoginUser();
			slipTypeList = slipDefMapper.getTypeByBelongByQd(belongList,user.getOrganNo());
		} else {
			slipTypeList = slipDefMapper.getTypeByBelong(belongList);
		}
		String batckDate = busiFormMapper.selectWorkDate(3,ARSConstants.DB_TYPE);
		Map retMap = new HashMap();
		retMap.put("slipTypeList", slipTypeList); //差错归类列表
		retMap.put("batckDate", batckDate); //差错归类列表
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 处理单查询初始化
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void busiQueryInit(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		List<String> belongList = new ArrayList<String>();
		belongList.add("0");
		List slipTypeList = null;
		String qdParameter = (String)requestBean.getSysMap().get("qdParameter");
		if ("qd".equals(qdParameter)) {
			User user = BaseUtil.getLoginUser();
			slipTypeList = slipDefMapper.getTypeByBelongByQd(belongList,user.getOrganNo());
		} else {
			slipTypeList = slipDefMapper.getTypeByBelong(belongList);
		}
		HashMap condMap = new HashMap();
		//condMap.put("modelType", "0");
		condMap.put("userOrganNo", BaseUtil.filterSqlParam(BaseUtil.getLoginUser().getOrganNo()));
		condMap.put("modelIdList", modelMapper.selectFilterModel(BaseUtil.filterSqlParam(BaseUtil.getLoginUser().getUserNo())));
		List<Model> modelList = modelMapper.getUserModelInfos(condMap);//获取预警模型信息
		
		Map showSearchMap = new HashMap<>();
		TreeMap<String, BusiNodeBean> busiNodes = SystemConstants.HT_BUSI_NODES;
		Iterator iterator = busiNodes.keySet().iterator();
		while (iterator.hasNext()) {
			String busiNodeNo = (String) iterator.next();
			BusiNodeBean busiNodeBean = (BusiNodeBean) busiNodes.get(busiNodeNo);
			showSearchMap.put(busiNodeBean.getIndex(), busiNodeBean.getIsShowSecondSearch());
		}
		
		Map retMap = new HashMap();
		retMap.put("slipTypeList", slipTypeList); //差错归类列表
		retMap.put("modelList", modelList); //差模型信息列表
		retMap.put("showSearchMap", showSearchMap);//是否显示查询列表
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 获取处理单ID
	 * @param formType 0：差错C 2：核实H 3：预警Y 5:内部差错单N 6: 质检单Z 
	 * @return
	 */
	private String getFormId(String formType) throws Exception{
		SecureRandom random = new SecureRandom();
		String todayStr = DateUtil.getToday("yyyyMMddsss") + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
		String sqlLike = "";
		String nextId = "";
		if("0".equals(formType)) {
			sqlLike = "C" + todayStr;
		} else if ("2".equals(formType)) {
			sqlLike = "H" + todayStr;
		} else if ("3".equals(formType)) {
			sqlLike = "Y" + todayStr;
		} else if ("5".equals(formType)) {
			sqlLike = "N" + todayStr;
		} else if ("6".equals(formType)) {
			sqlLike = "Z" + todayStr;
		}
		String maxFormId = busiFormMapper.getMaxFormId(sqlLike);
		if (maxFormId != null) {
			maxFormId = BaseUtil.filterSqlParam(maxFormId.trim());
			int bachId = Integer.parseInt(maxFormId.substring(
					maxFormId.length() - 4, maxFormId.length())) + 1;
			nextId = sqlLike
					+ String.valueOf(new java.text.DecimalFormat("0000")
							.format(bachId));
		} else {
			nextId = sqlLike + "0001";
		}
		return nextId;
	}

	/**
	 * 查询展现列表
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getTaskList(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		String index = (String)sysMap.get("index");
		String type = (String)sysMap.get("type"); //查询类型，回收站recycler
		String orderField = (String)sysMap.get("orderField");//排序字段
		String sort = (String)sysMap.get("sort");//排序方式，desc asc
		BusiForm busiformTb = (BusiForm) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		TreeMap<String, BusiNodeBean> busiNodes = SystemConstants.HT_BUSI_NODES;
		BusiNodeBean busiNodeBean = (BusiNodeBean) busiNodes.get(index);
		if (busiNodeBean == null) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("busiNodeDef.xml文件配置格式有错误，请联系管理员修正！ ");
		}else{
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
			Page page = PageHelper.startPage(currentPage, pageSize);
			
			setConditionMap(condMap,busiNodeBean,type,busiformTb);
			
			StringBuffer orderby = new StringBuffer(" ORDER BY ");
			if(!BaseUtil.isBlank(orderField)){
				orderby.append(orderField + " " + sort + ",");
			}
			orderby.append("BACK_DATE ASC,ALARM_LEVEL,SLIP_LEVEL,NODE_NO,NET_NO,OPERATION_DATE,FORM_ID");
			condMap.put("orderBy", orderby.toString());
			condMap.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
			List taskList = busiFormMapper.getTaskList(condMap);
			
			Map retMap = new HashMap();
			retMap.put("currentPage", currentPage);
			retMap.put("pageSize", pageSize);
			retMap.put("totalNum", page.getTotal());
			retMap.put("returnList", taskList);
			retMap.put("busiNodeBean", busiNodeBean);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}	
	}
	
	/**
	 * 组装查询条件
	 * @param condMap
	 * @param busiNodeBean
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap setConditionMap(HashMap condMap,BusiNodeBean busiNodeBean,String type,BusiForm busiformTb) throws Exception{
		User user = BaseUtil.getLoginUser();
		/*if("recycler".equalsIgnoreCase(type)){
			busiformTb.setActiveFlag(SystemConstants.ACTIVE_FLAG_STOP);//回收站显示无效的单子
		}else{
			busiformTb.setActiveFlag(SystemConstants.ACTIVE_FLAG_ACT); // 只显示有效的单子,对于撤单和删除无效的单子不显示.
		}*/
		
		if(!BaseUtil.isBlank(busiformTb.getNetNo()) && busiformTb.getNetNo().indexOf(",") > -1){//如果复选机构转成List
			List netNoList = Arrays.asList(busiformTb.getNetNo().split(","));
			condMap.put("netNoList", SqlUtil.getSumArrayList(netNoList));
			busiformTb.setNetNo(null);
		}
		
		condMap.put("thisPurview_plr", "yes");//20161021 特殊值 ,满足条件说明用于查看抄送信息，抄送信息可忽略权限机构
		if ("yes".equalsIgnoreCase(busiNodeBean.getNeedOrganCondition())) {
			condMap.put("thisPurviewName", "yes");//是否过滤机构
		}
		if ("yes".equalsIgnoreCase(busiNodeBean.getIsCheckerMatch())) {
			busiformTb.setCheckerNo(user.getUserNo()); // 当前登录用户号＝处理单登记人号
		}
		if ("yes".equalsIgnoreCase(busiNodeBean.getIsTellerMatch())) {
			busiformTb.setTellerNo(user.getUserNo()); // 当前登录用户号＝处理单所涉及的柜员号
		}
		/*if ("yes".equalsIgnoreCase(busiNodeBean.getIsLeaderMatch())) {
			busiformTb.setCcLeader(user.getOrganNo()); // 当前登录用户号＝处理单所涉及的抄送领导号
		}*/
		if ("yes".equalsIgnoreCase(busiNodeBean.getIsCheckerNotUserNoMatch())) {
			condMap.put("checkerNo", user.getUserNo()); // 审核员不能审核自己提交的处理单
		}
		if ("yes".equalsIgnoreCase(busiNodeBean.getIsLeaderNotSeanMatch())) {
			condMap.put("roleArray", user.getRoleNo().split(","));
			condMap.put("leaderNotSean", user.getOrganNo());//机构没有查看过的列表
		}
		if ("yes".equalsIgnoreCase(busiNodeBean.getIsLeaderHasSeanMatch())) {
			condMap.put("roleArray", user.getRoleNo().split(","));
			condMap.put("leaderHasSean", user.getOrganNo()); // 机构查看过的列表
		}
		if("yes".equalsIgnoreCase(busiNodeBean.getIsAppointNetUser())){
			condMap.put("isAppointNetUser",user.getUserNo());
		}
		if("yes".equalsIgnoreCase(busiNodeBean.getIsAppointNetArms())){
			condMap.put("isAppointNetArms",user.getUserNo());
		}
		if("yes".equalsIgnoreCase(busiNodeBean.getIsAppointNetManager())){
			condMap.put("isAppointNetManager", user.getUserNo());
		}
		if("yes".equalsIgnoreCase(busiNodeBean.getCorrFlag())){
			condMap.put("corrFlag", user.getUserNo());
		}

		// 业务结点配置中可以对所属单元结点做权限设置<util_node form_type="4" util_node_no="11,12" purview="" >
		List conditionBusiUtilNodes = new ArrayList(); // <utilNodeBean>
														// 转为SQL语句为：form_type=*
														// && node_no in(*,*,*)
		List busi_utilNodes_tmp = busiNodeBean.getUtilNodes();
		if (busi_utilNodes_tmp != null) {
			for (int y = 0; y < busi_utilNodes_tmp.size(); y++) {
				UtilNodeBean utilNodeBean = (UtilNodeBean) busi_utilNodes_tmp.get(y);
				if(!"all".equals(utilNodeBean.getFormType()) && !"all".equals(utilNodeBean.getNodeNo())){
					conditionBusiUtilNodes.add(utilNodeBean);
				}
			}
		}
		condMap.put("conditionBusiUtilNodes", conditionBusiUtilNodes);
		condMap.put("busiForm", busiformTb); // 再查询条件赋值
		condMap.put("userNo", user.getUserNo());//过滤权限机构
		return condMap;
	}
	
	/**
	 * 可退回单据明细
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void corrSearchHandle(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		Map sysMap = requestBean.getSysMap();
		Map retMap = new HashMap();
		BusiForm busiformTb = (BusiForm) requestBean.getParameterList().get(0);
		User user = BaseUtil.getLoginUser();
		// 构造条件参数
		HashMap condMap = new HashMap();
		
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
		Page page = PageHelper.startPage(currentPage, pageSize);
		
		if (BaseUtil.isBlank(busiformTb.getOperationDateS())) {
			busiformTb.setOperationDateS(DateUtil.getMonth(-1));
		}
		if (BaseUtil.isBlank(busiformTb.getOperationDateE())) {
			busiformTb.setOperationDateE(DateUtil.getToday("yyyyMMdd"));
		}
		if (busiformTb.getNetNo() != null && busiformTb.getNetNo().indexOf(",") > 0) {
			condMap.put("netNoList", SqlUtil.getSumArrayList(Arrays.asList(busiformTb.getNetNo().split(","))));
			busiformTb.setNetNo(null);
		}
		String cflag="2";
		condMap.put("busiForm", busiformTb);
		condMap.put("cflag", cflag);
		condMap.put("form_state", "");
		condMap.put("branch_state", "");
		condMap.put("bake_user_no", "");
		condMap.put("userNo", user.getUserNo());
		condMap.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构

		List taskList = busiFormMapper.getCorrBusiFormList(condMap);
			
		retMap.put("currentPage", currentPage);
		retMap.put("pageSize", pageSize);
		retMap.put("totalNum", page.getTotal());
		retMap.put("returnList", taskList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");  
	}

	/**
	 * 退回单据查询
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void corrSearch(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		Map retMap = new HashMap();
		BusiForm busiformTb = (BusiForm) requestBean.getParameterList().get(0);
		// 构造条件参数
		Map<String, Object> otherCondition = (Map<String, Object>)sysMap.get("otherCondition");
		String form_state = (String) otherCondition.get("form_state");
		String branch_state = (String) otherCondition.get("branch_state");
		String bake_user_no = (String) otherCondition.get("bake_user_no");
		if (bake_user_no!=null&& bake_user_no.trim().length()>0) {
			bake_user_no=bake_user_no.trim();
		}
		User user = BaseUtil.getLoginUser();
		// 构造条件参数
		HashMap condMap = new HashMap();
		
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
		Page page = PageHelper.startPage(currentPage, pageSize);
		
		if (busiformTb.getNetNo() != null && busiformTb.getNetNo().indexOf(",") > 0) {
			condMap.put("netNoList", SqlUtil.getSumArrayList(Arrays.asList(busiformTb.getNetNo().split(","))));
			busiformTb.setNetNo(null);
		}
		if(!BaseUtil.isBlank(busiformTb.getReturnDateS())){
			busiformTb.setReturnDateS(busiformTb.getReturnDateS() + " 00:00:00");
		}
		if(!BaseUtil.isBlank(busiformTb.getReturnDateE())){
			busiformTb.setReturnDateE(busiformTb.getReturnDateE() + " 24:00:00");
		}
		String cflag="1";
		condMap.put("busiForm", busiformTb);
		condMap.put("cflag", cflag);
		condMap.put("form_state", form_state);
		condMap.put("branch_state", branch_state);
		condMap.put("bake_user_no", bake_user_no);
		condMap.put("userNo", user.getUserNo());
		condMap.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构

		List taskList = busiFormMapper.getCorrBusiFormList(condMap);
		
		retMap.put("currentPage", currentPage);
		retMap.put("pageSize", pageSize);
		retMap.put("totalNum", page.getTotal());
		retMap.put("returnList", taskList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功"); 
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void checkOrganNo(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
		String ccjg = busiForm.getCcLeader();
		String[] netNoList = ccjg.split(",");
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("netNoList", netNoList);
		int checkCount = busiFormMapper.checkOrganNo(condMap);
		if(netNoList.length != checkCount){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("机构录入错误!");
		}else{
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}	
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void doOverdueRemark(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("overRemark",busiForm.getOverdueRemark());
		condMap.put("formId", busiForm.getFormId());
		int updateCount = busiFormMapper.doOverdueRemark(condMap);
		if(updateCount > 0){
			BusiForm newBusiForm = busiFormMapper.selectByPrimaryKey(busiForm.getFormId());
			Map retMap = new HashMap();
			retMap.put("newBusiForm", newBusiForm);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else{
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("机构录入错误!");
		}
	}
	
	/**
	 * 获取单据信息
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void showBusiform(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		User loginUser = BaseUtil.getLoginUser();
		Map retMap = new HashMap();
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		// 构造条件参数
		condMap.put("formId", busiForm.getFormId());

		BusiForm busiformTb = busiFormMapper.selectByPrimaryKey(busiForm.getFormId());
		//如果传了单据节点判断是否和数据库记录相同
		if(!BaseUtil.isBlank(busiForm.getNodeNo()) && !busiForm.getNodeNo().equals(busiformTb.getNodeNo())){
			String msg = SystemConstants.MSG_TASK_HAS_DONE; // 任务已被处理
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(msg);
			return;
		}
		if(!BaseUtil.isBlank(busiForm.getIsSee())){
			busiformTb.setIsSee(busiForm.getIsSee());
			busiForm.setSeeDesc(loginUser.getUserNo() + " " + DateUtil.getToday("yyyyMMdd HH:mm:ss"));
			busiFormMapper.updateByPrimaryKeySelective(busiForm);
		}
		Map processMap = getDoneBusiProcesses(busiformTb);// 取已处理过的流程信息
		if("false".equals(processMap.get("flag"))){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(processMap.get("failMsg")+"");
			return;
		}else{
			retMap.put("processList", processMap.get("processList"));
		}
		
		if(!BaseUtil.isBlank(busiformTb.getEntryId())){//如果模型号不为空，则查询模型展现信息
			try{
				Map entryInfoMap = getEntryInfo(busiformTb.getEntryId(),busiformTb.getEntryrowId());
				retMap.put("modelData", entryInfoMap.get("modelData"));
			    retMap.put("modelFieldResulets", entryInfoMap.get("modelFieldResulets"));
			}catch (Exception e) {
				retMap.put("errmsg", "查询模型数据异常");
			}
		}
		String backDate = busiFormMapper.selectWorkDate(3,ARSConstants.DB_TYPE);
		
		List<QueryImg> queryImgList=null;
		if(!BaseUtil.isBlank(busiformTb.getEntryId())){
			QueryImg queryImg = new QueryImg();
	       queryImg.setMid(Integer.parseInt(BaseUtil.filterSqlParam(busiformTb.getEntryId())));
	       //获取看图分组
	       queryImgList = queryImgMapper.selectBySelective(queryImg);
		}
		retMap.put("queryImgList", queryImgList);
		retMap.put("backDate", backDate);
		retMap.put("busiformTb", busiformTb);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void showProcess(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
		BusiForm busiformTb = busiFormMapper.selectByPrimaryKey(busiForm.getFormId());
		if(!busiForm.getNodeNo().equals(busiformTb.getNodeNo())){
			String msg = SystemConstants.MSG_TASK_HAS_DONE; // 任务已被处理
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(msg);
			return;
		}
		Map processMap = getDoneBusiProcesses(busiformTb);// 取已处理过的流程信息
		if("false".equals(processMap.get("flag"))){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(processMap.get("failMsg")+"");
			return;
		}else{
			retMap.put("processList", processMap.get("processList"));
		}
		Map thisProcessMap = getThisBusiProcess(busiformTb);// 取当前要处理的流程信息
		retMap.put("utilNodeBean", thisProcessMap.get("utilNodeBean"));
		retMap.put("actionsList", thisProcessMap.get("actionsList"));
		
		if(!BaseUtil.isBlank(busiformTb.getEntryId())){//如果模型号不为空，则查询模型展现信息
			try{
				Map entryInfoMap = getEntryInfo(busiformTb.getEntryId(),busiformTb.getEntryrowId());
				retMap.put("modelData", entryInfoMap.get("modelData"));
			    retMap.put("modelFieldResulets", entryInfoMap.get("modelFieldResulets"));
			    if(entryInfoMap.get("modelData") != null){//查询同账号，机构，柜员预警统计,同提取时间
					String occurDate = ((Map)entryInfoMap.get("modelData")).get("PROC_DATE") == null?"":((Map)entryInfoMap.get("modelData")).get("PROC_DATE")+"";
					String siteNo = ((Map)entryInfoMap.get("modelData")).get("BANKCODE") == null?"":(String) ((Map)entryInfoMap.get("modelData")).get("BANKCODE");
					String operatorNo = ((Map)entryInfoMap.get("modelData")).get("OPERATOR_NO") == null?"":(String) ((Map)entryInfoMap.get("modelData")).get("OPERATOR_NO");
					String acctNo = ((Map)entryInfoMap.get("modelData")).get("ACCT_NO") == null?"":(String) ((Map)entryInfoMap.get("modelData")).get("ACCT_NO");
					String tableName = "supervise_statistic_tb";
					HashMap condMap = new HashMap();
					condMap.put("organNo", BaseUtil.filterSqlParam(BaseUtil.getLoginUser().getOrganNo()));
					if(!BaseUtil.isBlank(occurDate)){
						String time = DateUtil.getDateBeforOrAfter(occurDate,10);
						SimpleDateFormat fd = new SimpleDateFormat("yyyyMMdd");
						if(fd.parse(time).before(fd.parse(DateUtil.getNow()))){
							tableName += "_his";
							System.out.println("****************");
						}
					}
					condMap.put("tableName", BaseUtil.filterSqlParam(tableName));
					condMap.put("occurDate", BaseUtil.filterSqlParam(occurDate));
					condMap.put("modelIdList", modelMapper.selectFilterModel(BaseUtil.filterSqlParam(BaseUtil.getLoginUser().getUserNo())));
					if(!BaseUtil.isBlank(siteNo)){//同机构查询
						condMap.put("bankcode", siteNo);
						int sameSiteCount = etDataStatisticsMapper.selectCountSame(condMap);
						retMap.put("sameSiteCount", sameSiteCount);
					}
					if(!BaseUtil.isBlank(operatorNo)){//同柜员查询
						condMap.remove("bankcode");
						condMap.put("operatorNo", BaseUtil.filterSqlParam(operatorNo));
						int sameTellCount = etDataStatisticsMapper.selectCountSame(condMap);					
						retMap.put("sameTellCount", sameTellCount);
					}
					if(!BaseUtil.isBlank(acctNo)){//同账号查询
						condMap.remove("bankcode");
						condMap.remove("operatorNo");
						condMap.put("acctNo", BaseUtil.filterSqlParam(acctNo));
						int sameAcctCount = etDataStatisticsMapper.selectCountSame(condMap);
						retMap.put("sameAcctCount", sameAcctCount);
					}
			    }
			}catch (Exception e) {
				retMap.put("errmsg", "查询模型数据异常");
			}
			HashMap condMap = new HashMap();
			condMap.put("entryId", BaseUtil.filterSqlParam(busiformTb.getEntryId()));
			List riskType = etRiskSourceDefMapper.selectModelSourceType(condMap);
			List riskNameList = etRiskSourceDefMapper.selectModelSourceName(condMap);
			
			Map<String,List<Map<String, String>>> riskSourceMap = new HashMap<String,List<Map<String, String>>>();
			for (Object riskSource : riskNameList) {
				if(!riskSourceMap.containsKey(((Map)riskSource).get("SOURCE_TYPE"))){
					riskSourceMap.put(((Map)riskSource).get("SOURCE_TYPE").toString(), new ArrayList<Map<String, String>>());
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", ((Map)riskSource).get("SOURCE_NO")+"|"+((Map)riskSource).get("IS_NEED"));
				map.put("name", ((Map)riskSource).get("SOURCE_NAME").toString());
				riskSourceMap.get(((Map)riskSource).get("SOURCE_TYPE")).add(map);
			}
			//是否显示音像的判断！
			List<QueryImg> queryImgList=null;
			if(!BaseUtil.isBlank(busiformTb.getEntryId())){
				QueryImg queryImg = new QueryImg();
		       queryImg.setMid(Integer.parseInt( BaseUtil.filterSqlParam(busiformTb.getEntryId())));
		       //获取看图分组
		       queryImgList = queryImgMapper.selectBySelective(queryImg);
			}
			retMap.put("queryImgList", queryImgList);
			retMap.put("riskType", riskType);
			retMap.put("riskSourceMap", riskSourceMap);
			retMap.put("relateList", getRelateEntryInfo(busiformTb.getEntryId()));//查询关联模型信息
		}
		if(SystemConstants.FORM_TYPE_0.equals(busiformTb.getFormType())){//差错单查询差错驱动因素
			HashMap condMap = new HashMap();
			condMap.put("riskType", "1");//查询差错驱动因素
			List<EtRiskSourceDef>  riskSourceList = etRiskSourceDefMapper.selectBySelective(condMap);//获取所以值
			retMap.put("etRiskList", riskSourceList);
		}
		retMap.put("busiformTb", busiformTb);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	

	/**
	 * 处理某一通知单时，取通知单的流程处理信息返回给页面
	 * @param busiformTb
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map getDoneBusiProcesses(BusiForm busiformTb) throws Exception{
		Map retMap = new HashMap();
		retMap.put("flag", "true");
		Hashtable utilNodes = SystemConstants.HT_UTIL_NODES;
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("formId",  BaseUtil.filterSqlParam(busiformTb.getFormId()));
		List<EtProcessTb> processList = etProcessTbMapper.getProcesses(condMap);
		String formType = busiformTb.getFormType();
		String utilNodeNo = busiformTb.getNodeNo();
		UtilNodeBean utilNodeBean = (UtilNodeBean) utilNodes.get(formType + "|" + utilNodeNo);
		if (utilNodeBean == null) {
			String msg = "utilNodes.get(" + formType + "|" + utilNodeNo + ") utilNodeDef.xml文件配置格式有错误，请修正！ ";
			retMap.put("flag", "false");
			retMap.put("failMsg", msg);
			return retMap;
		}
		String onlyShowLastprocess = utilNodeBean.getOnlyShowLastprocess();//是否只显示最后一个过程处理信息
		if ("yes".equalsIgnoreCase(onlyShowLastprocess) && processList != null && processList.size() > 0) {
			processList = processList.subList(processList.size() - 1, processList.size());
		}
		// 如果不是中心用户，则只显示部分全行可见的流程处理信息。只中心可见的就不显示(废弃)
		/*if ("no".equalsIgnoreCase(isCenterUser) && processList != null && processList.size() > 0) {
			List listTmp = new ArrayList();
			for (int i = 0; i < processList.size(); i++) {
				EtProcessTb processTb = (EtProcessTb) processList.get(i);
				String processShowLevel = processTb.getProcessShowLevel();
				if (!"center".equalsIgnoreCase(processShowLevel)) {
					listTmp.add(processTb);
				}
			}
			processList = listTmp;
		}*/
		retMap.put("processList", processList);
		return retMap;
	}
	
	/**
	 * 获取处理处理当前节点方法
	 * @param busiformTb
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map getThisBusiProcess(BusiForm busiformTb) throws Exception{
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		retMap.put("flag", "true");
		Hashtable utilNodes = SystemConstants.HT_UTIL_NODES;
		String formType = busiformTb.getFormType();
		String utilNodeNo = busiformTb.getNodeNo();
		UtilNodeBean utilNodeBean = (UtilNodeBean) utilNodes.get(formType + "|" + utilNodeNo);

		String isShowActionModel = utilNodeBean.getIsShowActionModel(); // 是否显示节点动作处理模块, 默认为“yes-显示 no-不显示”
		if ("no".equalsIgnoreCase(isShowActionModel)){
			retMap.put("utilNodeBean", null);
			return retMap;
		}	
		if (utilNodeBean != null && utilNodeBean.getNodeActions() != null) {
			List actionsList_ = utilNodeBean.getNodeActions();
			List actionsList = new ArrayList();
			// 判断是否配置了特殊方法，若有配置则执行该方法
			for (int i = 0; i < actionsList_.size(); i++) {
				UtilNodeActionBean utilNodeActionBean = (UtilNodeActionBean) actionsList_.get(i);
				String actionMethod = utilNodeActionBean.getActionMethod();
				String slipLevel = busiformTb.getSlipLevel();
				if (actionMethod != null && actionMethod.indexOf("CHECK_LEVEL") != -1) {
					if (slipLevel == null || slipLevel.trim().length() == 0 || slipLevel.trim().length() > 2 || Integer.parseInt(slipLevel.trim()) < 2)
						continue;
				}
				actionsList.add(utilNodeActionBean);
			}
			retMap.put("actionsList", actionsList);
		}
		retMap.put("utilNodeBean", utilNodeBean);
		return retMap;
	}
	
	/**
	 * 非差错单据转差错单前判断是否已经转过差错
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void checkToSlip(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		// 构造条件参数
		condMap.put("formId", busiForm.getFormId());
		// 加任务锁定功能
		List formIdList = busiFormMapper.checkToSlip(condMap);
		if (formIdList != null && formIdList.size() > 0) {
			retMap.put("formId", ((Map)formIdList.get(0)).get("FORM_ID"));
		}
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 获取预警展现信息
	 * @param entryId
	 * @param entryrowId
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map getEntryInfo(String entryId, String entryrowId) throws Exception{
		// TODO Auto-generated method stub
		 //获取模型信息
        Model modelInfo = modelMapper.getModelInfo(Integer.valueOf(entryId));
        //获取该笔模型的信息
        Map<String, Object> modelDate = modelDataQueryMapper.getThisModelData(Integer.valueOf(entryId), Integer.valueOf(entryrowId), BaseUtil.filterSqlParam(modelInfo.getTableName()), null);
        if(null == modelDate){
        	modelDate = modelDataQueryMapper.getThisModelData(Integer.valueOf(entryId), Integer.valueOf(entryrowId), BaseUtil.filterSqlParam(modelInfo.getTableName()+"_HIS"), null);
        }
        //获取该笔模型的展现字段
        //获取展现字段
        List<ModelFieldResulet> modelFieldResulets = getModelFieldResulets(entryId);

        Map retMap = new HashMap();
        retMap.put("modelData", modelDate);
        retMap.put("modelFieldResulets", modelFieldResulets);
        return retMap;
	}

	/**
	 * 获取模型展现字段
	 * @param entryId
	 * @return
	 */
	private List<ModelFieldResulet> getModelFieldResulets(String entryId) throws Exception{
		// TODO Auto-generated method stub
		List<ModelFieldResulet> modelFieldResulets = new ArrayList<ModelFieldResulet>();

        ModelFieldResulet modelFieldResulet = null;
        //配置的查询字段
        List<HashMap<String,Object>> model_fields = exhibitFieldMapper.showExhibitField(Integer.parseInt(BaseUtil.filterSqlParam(entryId)));
        for (int j = 0;j < model_fields.size() ;j++){
            HashMap<String,Object>  model_field = model_fields.get(j);
            modelFieldResulet = new ModelFieldResulet();
            modelFieldResulet.setFieldId(Integer.valueOf(model_field.get("ID").toString()));
            modelFieldResulet.setName(String.valueOf(model_field.get("NAME")));
            modelFieldResulet.setChName(String.valueOf(model_field.get("CH_NAME")));
            modelFieldResulet.setType(Integer.valueOf(model_field.get("TYPE").toString()));
            modelFieldResulet.setRowno(Integer.valueOf(model_field.get("ROWNO").toString()));
            modelFieldResulet.setFormat(Integer.valueOf(model_field.get("FORMAT").toString()));
            modelFieldResulet.setIsfind(Integer.valueOf(model_field.get("ISFIND").toString()));
            modelFieldResulet.setIsdropdown(Integer.valueOf(model_field.get("ISDROPDOWN").toString()));
            modelFieldResulet.setIsimportant(Integer.valueOf(model_field.get("ISIMPORTANT").toString()));
            modelFieldResulet.setHisDic(Integer.valueOf(model_field.get("HASDIC").toString()));
            if(ObjectUtils.isEmpty(model_field.get("DICNAME"))){
                modelFieldResulet.setDicName("");
            }else {
                modelFieldResulet.setDicName(model_field.get("DICNAME").toString());
            }
            if(ObjectUtils.isEmpty(model_field.get("RELATE_ID"))){
                modelFieldResulet.setRelateId(0);
            }else {
                modelFieldResulet.setRelateId(Integer.valueOf(model_field.get("RELATE_ID").toString()));
            }
            if(ObjectUtils.isEmpty(model_field.get("ISTUOMIN"))){
                modelFieldResulet.setIsTuoMin("0");
            }else {
                modelFieldResulet.setIsTuoMin(model_field.get("ISTUOMIN").toString());
            }
            modelFieldResulets.add(modelFieldResulet);
        }

        //针对配置和必须字段进行去重合
        ModelFieldResulet field = null;
        for (int i = 0; i < SystemConstants.MODEL_MUST_FIELD.size(); i++) {
            field = SystemConstants.MODEL_MUST_FIELD.get(i);
            boolean ishave = false;
            for (int j = 0;j < modelFieldResulets.size() ;j++){
                if (modelFieldResulets.get(j).getName().equalsIgnoreCase(field.getName())) {
                    ishave = true;
                    break;
                }
            }
            if(!ishave){
                modelFieldResulets.add(field);
            }
        }
        return modelFieldResulets;
	}
	
	/**
	 * 删除单据方法
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	private void doDeleteBusiformTb(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
		BusiForm busiformTb = busiFormMapper.selectByPrimaryKey(BaseUtil.filterSqlParam(busiForm.getFormId()));
		if(!busiForm.getNodeNo().equals(busiformTb.getNodeNo())){
			String msg = SystemConstants.MSG_TASK_HAS_DONE; // 任务已被处理
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(msg);
			return;
		}
		// 不物理删除，放入回收站中。（修改active_flag标志为0）
		BusiForm newBusiformTb = new BusiForm();
		newBusiformTb.setActiveFlag(SystemConstants.ACTIVE_FLAG_STOP);
		newBusiformTb.setFormId(busiForm.getFormId());
		int n = busiFormMapper.updateByPrimaryKeySelective(newBusiformTb);
		String labelProcess = "停止流程";
		String dealResult = "删除并放入回收站";
		insertProcessTb(busiformTb, labelProcess, dealResult);
		String msg = SystemConstants.MSG_STOP_PROCESS_SUCC;
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(msg);
	}
	
	/**
	 * 还原
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	private void revivification(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
		BusiForm busiformTb = busiFormMapper.selectByPrimaryKey(busiForm.getFormId());
		if(!busiForm.getNodeNo().equals(busiformTb.getNodeNo())){
			String msg = SystemConstants.MSG_TASK_HAS_DONE; // 任务已被处理
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(msg);
			return;
		}
		// 还原。（修改active_flag标志为1）
		BusiForm newBusiformTb = new BusiForm();
		newBusiformTb.setActiveFlag(SystemConstants.ACTIVE_FLAG_ACT);
		newBusiformTb.setFormId(busiForm.getFormId());
		int n = busiFormMapper.updateByPrimaryKeySelective(newBusiformTb);
		String labelProcess = "修改还原";
		String dealResult = "将删除的处理单从回收站还原";
		insertProcessTb(busiformTb, labelProcess, dealResult);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("还原流程操作成功！");
	}

	/**
	 * 查看抄送
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void leaderView(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		User loginUser = BaseUtil.getLoginUser();
		Map retMap = new HashMap();
		// 构造条件参数
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
		BusiForm busiformTb = busiFormMapper.selectByPrimaryKey(busiForm.getFormId());

		Map processMap = getDoneBusiProcesses(busiformTb);// 取已处理过的流程信息
		if("false".equals(processMap.get("flag"))){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(processMap.get("failMsg")+"");
			return;
		}else{
			retMap.put("processList", processMap.get("processList"));
		}
		
		if(!BaseUtil.isBlank(busiformTb.getEntryId())){//如果模型号不为空，则查询模型展现信息
			try{
				Map entryInfoMap = getEntryInfo(busiformTb.getEntryId(),busiformTb.getEntryrowId());
				retMap.put("modelData", entryInfoMap.get("modelData"));
			    retMap.put("modelFieldResulets", entryInfoMap.get("modelFieldResulets"));
			}catch (Exception e) {
				retMap.put("errmsg", "查询模型数据异常");
			}
		}
		BusiForm obj_ = new BusiForm();
		//修改查看人为查看的机构 modify by miaoky
		if("0".equals(busiformTb.getCcType())){
			if (busiformTb.getSeanLeader() == null
					|| busiformTb.getSeanLeader().indexOf(loginUser.getOrganNo()) == -1) {
				String seanLeader = busiformTb.getSeanLeader() == null|| busiformTb.getSeanLeader().trim().length() == 0 ? loginUser.getOrganNo(): busiformTb.getSeanLeader() + "," + loginUser.getOrganNo();
				obj_.setSeanLeader(seanLeader);
			}
		}else if("1".equals(busiformTb.getCcType())){//抄送角色
			String[] roleArr = loginUser.getRoleNo().split(",");
			String seanLeader = "";
			for (String roleId : roleArr) {
				if ((busiformTb.getSeanRole() == null || busiformTb.getSeanRole().indexOf(roleId) == -1)
						&& busiformTb.getCcRole().indexOf(roleId) != -1) {
					seanLeader += "," + roleId;		
				}
			}
			if (busiformTb.getSeanRole() == null || busiformTb.getSeanRole().trim().length() == 0) {
				seanLeader = seanLeader.substring(1);
			}else{
				seanLeader = busiformTb.getSeanRole() + seanLeader;
			}
			obj_.setSeanRole(seanLeader);
		}else if("2".equals(busiformTb.getCcType())){//抄送用户
			if (busiformTb.getSeanLeader() == null
					|| busiformTb.getSeanLeader().indexOf(","+loginUser.getUserNo()+",") == -1) {
				String seanLeader = busiformTb.getSeanLeader() == null|| busiformTb.getSeanLeader().trim().length() == 0 ? ","+loginUser.getUserNo()+",": busiformTb.getSeanLeader() + loginUser.getUserNo() + ",";		
				obj_.setSeanLeader(seanLeader);
			}
		}else if("3".equals(busiformTb.getCcType())){//抄送机构角色
			if (busiformTb.getSeanLeader() == null || busiformTb.getSeanLeader().indexOf(loginUser.getOrganNo()) == -1) {
				String seanLeader = busiformTb.getSeanLeader() == null|| busiformTb.getSeanLeader().trim().length() == 0 ? loginUser.getOrganNo(): busiformTb.getSeanLeader() + "," + loginUser.getOrganNo();		
				obj_.setSeanLeader(seanLeader);
			}
			String[] roleArr = loginUser.getRoleNo().split(",");
			String seanLeader = "";
			for (String roleId : roleArr) {
				if ((busiformTb.getSeanRole() == null || busiformTb.getSeanRole().indexOf(roleId) == -1)
						&& busiformTb.getCcRole().indexOf(roleId) != -1) {
					seanLeader += "," + roleId;		
				}
			}
			if (busiformTb.getSeanRole() == null || busiformTb.getSeanRole().trim().length() == 0) {
				seanLeader = seanLeader.substring(1);
			}else{
				seanLeader = busiformTb.getSeanRole() + seanLeader;
			}
			obj_.setSeanRole(seanLeader);
		}
		if(obj_.getSeanLeader() != null || obj_.getSeanRole() != null){
			obj_.setFormId(BaseUtil.filterSqlParam(busiformTb.getFormId()));
			busiFormMapper.updateByPrimaryKeySelective(obj_);
		}
		
		retMap.put("busiformTb", busiformTb);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 收回功能
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void doBackDeal(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		User loginUser = BaseUtil.getLoginUser();
		Map retMap = new HashMap();
		HashMap condMap = new HashMap();
		// 构造条件参数
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);	
		if(null != loginUser){
			//先进行查询该表单是否正被人处理
			Map<String,Object> taskLock = taskLockMapper.selectLockFormId(busiForm.getFormId());
	        if(taskLock != null ){
	        	responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("该任务正在被人处理，无法收回!");
				return;
	        }
			
			condMap.put("formId", BaseUtil.filterSqlParam(busiForm.getFormId()));
			BusiForm tb = busiFormMapper.selectByPrimaryKey(busiForm.getFormId());
			String dealNo = tb.getDealNo();
			String nodeNo = tb.getNodeNo();
			String formType = tb.getFormType();
			
			//查询单据是否已经办结节点
			if(SystemConstants.FLOW_END_NODES.containsKey(formType + "|"+nodeNo)){
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("该处理单已被办结,无法进行收回");
				return;
			}
			
			//可以手动退回系统自动处理的表单  争议单(不改变处理人，可以有监测员手工收回)
			if(!loginUser.getUserNo().equals(dealNo)){
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("无法对不是自己处理的处理单进行收回");
				return;
			}
			condMap.put("nodeNo", BaseUtil.filterSqlParam(nodeNo));
			condMap.put("userNo", BaseUtil.filterSqlParam(loginUser.getUserNo()));
			List<EtProcessTb> processList = etProcessTbMapper.getProcessByDealResult(condMap);
			//没有流程，或者流程是退回和下发的记录不允许收回
			if(processList == null || processList.size() == 0 
					|| "8888".equals(processList.get(0).getNodeNo()) || "02".equals(processList.get(0).getNodeNo())){
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("无流程可收回");
				return;
			}
			
			//退回流程不能收回
			if(processList.get(0).getDealResultText().indexOf("分行退回") > -1 ||
					processList.get(0).getDealResultText().indexOf("总行退回") > -1){
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("退回流程无法收回!");
				return;
			}
			
			String createDate = DateUtil.getToday("yyyyMMdd HH:mm:ss");
	        String dealDate = createDate.substring(0,8);
	        //准备收回的流程记录
			EtProcessTb newProcess = processList.get(0);
			String preNode = newProcess.getNodeNo();
			//修改收回节点的处理时间
	        condMap.put("backDate", BaseUtil.filterSqlParam(newProcess.getBackDate()));
	        condMap.put("organNo", BaseUtil.filterSqlParam(loginUser.getOrganNo()));
	        etProcessTbMapper.updateBackProcess(condMap);
			
			newProcess.setCreateTime(createDate);
			newProcess.setDealDate(dealDate);
			newProcess.setDealUserNo(loginUser.getUserNo());
			newProcess.setDealUserName(loginUser.getUserName());
			newProcess.setNodeNo("8888");
			newProcess.setDealResultText(preNode + "-收回当前处理流程");//节点更新
			newProcess.setLabelProcess("收回当前处理流程");
			newProcess.setDealDescription("");
			newProcess.setBackDate("");
			List<EtProcessTb> doProcessList = etProcessTbMapper.getProcessByDoNode(BaseUtil.filterSqlParam(newProcess.getFormId()),BaseUtil.filterSqlParam(preNode));
			if(doProcessList != null && doProcessList.size() > 0){
				newProcess.setBackDate(doProcessList.get(0).getBackDate());
			}
			if(!BaseUtil.isBlank(newProcess.getBackDate())){//如果应反馈日期不为空设置默认值
				newProcess.setNextDealDate("99991231");
			}
	        etProcessTbMapper.insertSelective(newProcess);
	        
	        if(!BaseUtil.isBlank(newProcess.getBackDate())){//分行流程收回时如果有原应处理日期得刷新下日期，因触发器会变更该日期
	        	EtProcessTb updateProcess = new EtProcessTb();
		        updateProcess.setFormId(BaseUtil.filterSqlParam(newProcess.getFormId()));
		        updateProcess.setCreateTime(BaseUtil.filterSqlParam(newProcess.getCreateTime()));
		        updateProcess.setBackDate(BaseUtil.filterSqlParam(newProcess.getBackDate()));
		        etProcessTbMapper.updateByPrimaryKeySelective(updateProcess);
	        }
	        
	        //修改单据流程节点
	        busiForm.setNodeNo(preNode);
	        busiFormMapper.updateByPrimaryKeySelective(busiForm);
	        
	        //计算收回流程后该操作员是否逾期，如果逾期需更新单据表逾期信息
	        if(!BaseUtil.isBlank(newProcess.getBackDate()) && newProcess.getBackDate().compareTo(DateUtil.getNow()) < 0){
	        	etProcessTbMapper.updateProcessOverInfo(BaseUtil.filterSqlParam(newProcess.getFormId()));
	        	etProcessTbMapper.updateProcessOverToBusiform(BaseUtil.filterSqlParam(newProcess.getFormId()));
	        }
	        
	        tb.setNodeNo(preNode);
	        retMap.put("newBusiformTb", tb);//修改后的单据信息返回
	        responseBean.setRetMap(retMap);
	        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
    		responseBean.setRetMsg("收回成功！");
		}else{
    		responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
    		responseBean.setRetMsg("收回失败！会话失效，请退出后重新登录系统！");
		}
	}
	
	/**
	 * 退回提交方法
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void doCorrHandleBack(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		User loginUser = BaseUtil.getLoginUser();
		HashMap condMap = new HashMap();
		// 构造条件参数
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
		
		Map sysMap = requestBean.getSysMap();
		String dealResult = (String)sysMap.get("dealResult"); //处理结果
		
		if(null != loginUser){
			//查询用户角色
			String userRole_no = loginUser.getRoleNo();
			String organs = loginUser.getOrganNo();
			String dealDescription = busiForm.getFormDescription();
			String organLevel = "0";
			List<HashMap<String, Object>> organInfoMapList = organInfoDao.selectSiteInfo(organs);
			if(organInfoMapList != null && organInfoMapList.size() > 0){
				organLevel = ((Map)organInfoMapList.get(0)).get("ORGAN_LEVEL").toString();
			}
			BusiForm busiformTb = new BusiForm();
			EtProcessTb processTb = new EtProcessTb();//流程记录
			busiformTb.setFormId(busiForm.getFormId());
			busiformTb.setCorrBackDate(busiForm.getCorrBackDate());
			busiformTb.setBackDate(busiForm.getCorrBackDate());//设置新的要求反馈日期

	        String createDate = DateUtil.getToday("yyyyMMdd HH:mm:ss");

	        BusiForm bf = busiFormMapper.selectByPrimaryKey(busiForm.getFormId());
	        
	        String backDate = busiFormMapper.selectWorkDate(3,ARSConstants.DB_TYPE);
			backDate = BaseUtil.filterSqlParam(backDate);
	        String returnDate = bf.getReturnDate();
	        //记录第一次时间，有总行记总行
	        if (Integer.parseInt(organLevel) >= 2) {//2级	分行处理			
	        	if (BaseUtil.isBlank(returnDate)) {//只有没有退回过的分行处理才用更新
	        		busiformTb.setReturnDate(createDate);//在监督退回日期
	        		busiformTb.setBakeUserNo(loginUser.getUserNo()+","+userRole_no);//设置再监督退回人及其角色
	        	}
			}else{												//总行处理
				condMap.put("formId", busiformTb.getFormId());
				condMap.put("nodeNo", "50");
				List<EtProcessTb> processList = etProcessTbMapper.selectByPrimaryKey(condMap);
				if(processList == null || processList.size() < 1){//没有总行退回记录
					busiformTb.setReturnDate(createDate);
					busiformTb.setBakeUserNo(loginUser.getUserNo()+","+userRole_no);//设置再监督退回人及其角色
				}
			}
	        // modity end
	        String backNode="";//退回节点
	        String backState="";//退回状态
	        if("1".equals(organLevel)||"0".equals(organLevel)){
	        	backNode="50";//主管处理
	        	backState="50-待分行处理_总行退回";
	        	busiformTb.setParBackDate(backDate);//分行逾期日期,如果退回核查没有作用
	        }else{
	        	if("0".equals(dealResult)){//作业机构
	        		if("3".equals(busiForm.getFormType())){
		        		backNode="24";//作业机构处理预警单
		        		backState="24-待作业机构处理_分行退回";
		        	}else{
		        		backNode="21";//作业机构处理提示单或差错单
		        		backState="21-待作业机构处理_分行退回";
		        	}
	        		processTb.setBackDate(busiForm.getCorrBackDate());//退回作业机构流程记录应反馈日期
	        		processTb.setNextDealDate("99991231");
	        	}else{//监测员
	        		if("3".equals(busiForm.getFormType())){
		        		backNode="35";//待监测员处理_分行退回
		        		backState="35-待监测员处理_分行退回";
		        	}else if("0".equals(busiForm.getFormType())){
		        		backNode="27";//监测员处理提示单或差错单
		        		backState="27-待监测员处理_分行退回";
		        	}else{
		        		backNode="25";//监测员处理提示单或差错单
		        		backState="25-待监测员处理_分行退回";
		        	}
	        		busiformTb.setParBackDate(backDate);//分行逾期日期,如果退回核查没有作用
	        	}
	        }
	        busiformTb.setNodeNo(backNode);//节点设置为提示单和差错单的21
	        busiformTb.setReturnState("1");//退回标识
	        busiformTb.setIsSee("0");//0表示未阅
	        busiformTb.setActiveFlag("1");//单据退回后因撤单而失效的单据恢复成有效
	        //数据插入流水表
	        Hashtable utilNodes = SystemConstants.HT_UTIL_NODES;
			UtilNodeBean utilNodeBean = (UtilNodeBean) utilNodes.get(busiForm.getFormType() + "|"+backNode);

			processTb.setFormId(busiForm.getFormId());
			processTb.setFormType(busiForm.getFormType());
			processTb.setNodeNo(backNode);//设置流水节点
			processTb.setCreateTime(createDate);
			processTb.setDealOrganNo(organs);
			processTb.setDealUserNo(loginUser.getUserNo());
			processTb.setDealUserName(loginUser.getUserName());
			processTb.setDealDate(createDate.substring(0, 8));
			processTb.setDealDescription(dealDescription);
			processTb.setProcessShowLevel("all");
			processTb.setDealResultText(backState);
			processTb.setLabelProcess(utilNodeBean.getLabelProcess());
			etProcessTbMapper.insertSelective(processTb);
			busiformTb.setDealNo(BaseUtil.getLoginUser().getUserNo());//设置退回操作的执行人
			busiformTb.setModifyType("0");//退回时把修改办结结果置空
			int n = busiFormMapper.updateByPrimaryKeySelective(busiformTb);
	        if(n < 1){
	        	responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
	    		responseBean.setRetMsg("退回失败！");
	        }else{
	        	responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
	    		responseBean.setRetMsg("退回成功！");
	        }
		}else{
    		responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
    		responseBean.setRetMsg("退回失败！会话失效，请退出后重新登录系统！");
		}
	}
	
	/**
	 * 删除附件方法
	 * @param requestBean
	 * @param responseBean
	 */
	private void etDeleteFile(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);	
		FileUtil.deleteFile(busiForm.getFilePath()+File.separator+busiForm.getFileName()+busiForm.getFileExt());
		BusiForm newBusiForm = new BusiForm();
		newBusiForm.setFormId(busiForm.getFormId());
		busiFormMapper.deleteUploadFile(newBusiForm);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除附件成功！");
	}
	
	/**
	 * 插入节点流转信息
	 * @param busiformTb
	 * @param labelProcess
	 * @param dealResult
	 * @throws Exception
	 */
	private void insertProcessTb(BusiForm busiformTb, String labelProcess, String dealResult) throws Exception{
		// TODO Auto-generated method stub
		EtProcessTb processTb = new EtProcessTb();
		processTb.setFormId(BaseUtil.filterSqlParam(busiformTb.getFormId()));
		processTb.setFormType(BaseUtil.filterSqlParam(busiformTb.getFormType()));
		processTb.setNodeNo(BaseUtil.filterSqlParam(busiformTb.getNodeNo()));
		processTb.setDealUserNo(BaseUtil.getLoginUser().getUserNo());
		processTb.setDealUserName(BaseUtil.getLoginUser().getUserName());
		processTb.setDealOrganNo(BaseUtil.getLoginUser().getOrganNo());
		processTb.setLabelProcess(labelProcess);
		processTb.setProcessShowLevel("all");
		processTb.setDealDate(DateUtil.getNow());
		processTb.setCreateTime(DateUtil.getToday("yyyyMMdd HH:mm:ss"));
		processTb.setDealResultText(dealResult);
		etProcessTbMapper.insertSelective(processTb);
	}
	
	private void insertBfProcessTb(BusiForm busiformTb, String dealResult,String nodeNo,String labelProcess,String backDate) throws Exception{
		//数据插入流水表
        Hashtable utilNodes = SystemConstants.HT_UTIL_NODES;
		UtilNodeBean utilNodeBean = (UtilNodeBean) utilNodes.get(busiformTb.getFormType() + "|"+busiformTb.getNodeNo());
		EtProcessTb processTb = new EtProcessTb();
		processTb.setFormId(busiformTb.getFormId());
		processTb.setFormType(busiformTb.getFormType());
		processTb.setNodeNo(nodeNo);//设置流水节点
		processTb.setCreateTime(DateUtil.getToday("yyyyMMdd HH:mm:ss"));
		processTb.setDealUserNo(BaseUtil.getLoginUser().getUserNo());
		processTb.setDealUserName(BaseUtil.getLoginUser().getUserName());
		processTb.setDealOrganNo(BaseUtil.getLoginUser().getOrganNo());
		processTb.setDealDate(DateUtil.getNow());
		processTb.setDealDescription(busiformTb.getFormDescription());
		processTb.setProcessShowLevel("all");
		processTb.setDealResultText(dealResult);
		if(null != backDate){//撤销补发记录反馈日期
			processTb.setBackDate(backDate);
			processTb.setNextDealDate("99991231");
		}
		if(!BaseUtil.isBlank(busiformTb.getRiskSourceName())){
			processTb.setRiskSourceNo(busiformTb.getRiskSourceNo());
			processTb.setRiskSourceName(busiformTb.getRiskSourceName());
			processTb.setRiskSourceType(busiformTb.getRiskSourceType());
			processTb.setRiskSourceRemark(busiformTb.getRiskSourceRemak());
		}
		processTb.setLabelProcess(labelProcess);
		if(BaseUtil.isBlank(labelProcess)){
			processTb.setLabelProcess(utilNodeBean.getLabelProcess());
		}
		etProcessTbMapper.insertSelective(processTb);
	}
	
	
	/**
	 * 办结修改查询
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void etModifyHandle(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		Map retMap = new HashMap();
		BusiForm busiformTb = (BusiForm) requestBean.getParameterList().get(0);
		User user = BaseUtil.getLoginUser();
		// 构造条件参数
		HashMap condMap = new HashMap();
		List nodeList = busiFormMapper.selectEndModify();
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
		Page page = PageHelper.startPage(currentPage, pageSize);
		
		if (BaseUtil.isBlank(busiformTb.getOperationDateS())) {
			busiformTb.setOperationDateS(DateUtil.getMonth(-1));
		}
		if (BaseUtil.isBlank(busiformTb.getOperationDateE())) {
			busiformTb.setOperationDateE(DateUtil.getToday("yyyyMMdd"));
		}
		if (busiformTb.getNetNo() != null && busiformTb.getNetNo().indexOf(",") > 0) {
			condMap.put("netNoList", SqlUtil.getSumArrayList(Arrays.asList(busiformTb.getNetNo().split(","))));
			busiformTb.setNetNo(null);
		}
		condMap.put("busiForm", busiformTb);
		condMap.put("userNo", user.getUserNo());
		condMap.put("nodeList", nodeList);
		condMap.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构

		List taskList = busiFormMapper.getModifyBusiFormList(condMap);
			
		retMap.put("currentPage", currentPage);
		retMap.put("pageSize", pageSize);
		retMap.put("totalNum", page.getTotal());
		retMap.put("returnList", taskList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 打开修改
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void showModify(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map retMap = new HashMap();
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
		BusiForm busiformTb = busiFormMapper.selectByPrimaryKey(busiForm.getFormId());
		if(!busiForm.getNodeNo().equals(busiformTb.getNodeNo())){
			String msg = SystemConstants.MSG_TASK_HAS_DONE; // 任务已被处理
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(msg);
			return;
		}
		Map processMap = getDoneBusiProcesses(busiformTb);// 取已处理过的流程信息
		if("false".equals(processMap.get("flag"))){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(processMap.get("failMsg")+"");
			return;
		}else{
			retMap.put("processList", processMap.get("processList"));
		}
		Map thisProcessMap = getThisBusiProcess(busiformTb);// 取当前要处理的流程信息
		retMap.put("utilNodeBean", thisProcessMap.get("utilNodeBean"));
		retMap.put("actionsList", thisProcessMap.get("actionsList"));
		
		if(!BaseUtil.isBlank(busiformTb.getEntryId())){//如果模型号不为空，则查询模型展现信息
			try{
				Map entryInfoMap = getEntryInfo(busiformTb.getEntryId(),busiformTb.getEntryrowId());
				retMap.put("modelData", entryInfoMap.get("modelData"));
			    retMap.put("modelFieldResulets", entryInfoMap.get("modelFieldResulets"));
			}catch (Exception e) {
				retMap.put("errmsg", "查询模型数据异常");
			}
			HashMap condMap = new HashMap();
			condMap.put("entryId", BaseUtil.filterSqlParam(busiformTb.getEntryId()));
			List riskType = etRiskSourceDefMapper.selectModelSourceType(condMap);
			List riskNameList = etRiskSourceDefMapper.selectModelSourceName(condMap);
			
			Map<String,List<Map<String, String>>> riskSourceMap = new HashMap<String,List<Map<String, String>>>();
			for (Object riskSource : riskNameList) {
				if(!riskSourceMap.containsKey(((Map)riskSource).get("SOURCE_TYPE"))){
					riskSourceMap.put(((Map)riskSource).get("SOURCE_TYPE").toString(), new ArrayList<Map<String, String>>());
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", ((Map)riskSource).get("SOURCE_NO")+"|"+((Map)riskSource).get("IS_NEED"));
				map.put("name", ((Map)riskSource).get("SOURCE_NAME").toString());
				riskSourceMap.get(((Map)riskSource).get("SOURCE_TYPE")).add(map);
			}
			retMap.put("riskType", riskType);
			retMap.put("riskSourceMap", riskSourceMap);
		}
		
		///是否显示音像的判断！
		List<QueryImg> queryImgList=null;
		if(!BaseUtil.isBlank(busiformTb.getEntryId())) {
			QueryImg queryImg = new QueryImg();
		    queryImg.setMid(Integer.parseInt(BaseUtil.filterSqlParam(busiformTb.getEntryId())));
		    //获取看图分组
		    queryImgList = queryImgMapper.selectBySelective(queryImg);
		}	
		retMap.put("queryImgList", queryImgList);
		
		retMap.put("busiformTb", busiformTb);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 获取管理预警信息
	 * @param entryId
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<McModelLine> getRelateEntryInfo(String entryId) throws Exception{
		// TODO Auto-generated method stub
        //获取该笔模型的信息
		HashMap condMap = new HashMap();
		condMap.put("modelId", Integer.valueOf(BaseUtil.filterSqlParam(entryId)));
		condMap.put("userOrganNo", BaseUtil.getLoginUser().getOrganNo());
		condMap.put("modelIdList", modelMapper.selectFilterModel(BaseUtil.getLoginUser().getUserNo()));
        List<McModelLine> relateModelList = mcModelLineMapper.selectRelateModel(condMap);
        if(null == relateModelList || relateModelList.size() == 0){
        	return null;
        }
        List<McModelLine> relateList = new ArrayList<McModelLine>();
        int relateId = 0;
        for (McModelLine relateMode : relateModelList) {
			if(relateId != relateMode.getLineid()){
				relateId = relateMode.getLineid();
				relateList.add(relateMode);
			}
		}
        return relateList;
	}
	
	/**
	 * 收回前检查是否会造成逾期提示
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void checkBackDeal(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		User loginUser = BaseUtil.getLoginUser();
		Map retMap = new HashMap();
		HashMap condMap = new HashMap();
		// 构造条件参数
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);	
		if(null == loginUser){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
    		responseBean.setRetMsg("收回失败！会话失效，请退出后重新登录系统！");
    		return;
		}
		//先进行查询该表单是否正被人处理
		Map<String,Object> taskLock = taskLockMapper.selectLockFormId(busiForm.getFormId());
        if(taskLock != null ){
        	responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("该任务正在被人处理，无法收回!");
			return;
        }
		
		condMap.put("formId", BaseUtil.filterSqlParam(busiForm.getFormId()));
		BusiForm tb = busiFormMapper.selectByPrimaryKey(busiForm.getFormId());
		String dealNo = tb.getDealNo();
		String nodeNo = tb.getNodeNo();
		String formType = tb.getFormType();
		
		//查询单据是否已经办结节点
		if(SystemConstants.FLOW_END_NODES.containsKey(formType + "|"+nodeNo)){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("该处理单已被办结,无法进行收回");
			return;
		}
		
		//可以手动退回系统自动处理的表单  争议单(不改变处理人，可以有监测员手工收回)
		if(!loginUser.getUserNo().equals(dealNo)){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("无法对不是自己处理的处理单进行收回");
			return;
		}
		condMap.put("nodeNo", BaseUtil.filterSqlParam(nodeNo));
		condMap.put("userNo", BaseUtil.filterSqlParam(loginUser.getUserNo()));
		List<EtProcessTb> processList = etProcessTbMapper.getProcessByDealResult(condMap);
		//没有流程，或者流程是退回和下发的记录不允许收回
		if(processList == null || processList.size() == 0 
				|| "8888".equals(processList.get(0).getNodeNo()) || "02".equals(processList.get(0).getNodeNo())){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("无流程可收回");
			return;
		}
		
		//退回流程不能收回
		if(processList.get(0).getDealResultText().indexOf("分行退回") > -1 ||
				processList.get(0).getDealResultText().indexOf("总行退回") > -1){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("退回流程无法收回!");
			return;
		}
		
        //准备收回的流程记录
		EtProcessTb newProcess = processList.get(0);
		String preNode = newProcess.getNodeNo();//收回前状态
		//查询收回前一节点流程信息
		List<EtProcessTb> doProcessList = etProcessTbMapper.getProcessByDoNode(BaseUtil.filterSqlParam(newProcess.getFormId()),BaseUtil.filterSqlParam(preNode));
		if(doProcessList != null && doProcessList.size() > 0){
			//收回前一个流程是否逾期
			if(!BaseUtil.isBlank(doProcessList.get(0).getBackDate()) && doProcessList.get(0).getBackDate().compareTo(DateUtil.getNow()) < 0){
				retMap.put("overFlag", "1");
	        }
		}
        //计算收回流程后该操作员是否逾期
        if(!BaseUtil.isBlank(newProcess.getBackDate()) && newProcess.getBackDate().compareTo(DateUtil.getNow()) < 0){
        	retMap.put("overFlag", "1");
        }
        
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("收回成功！");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void queryFlowNo(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		Map retMap = new HashMap();
		BigDecimal cnt=BigDecimal.ZERO;
		String formId="";
		BusiForm busiformTb = (BusiForm) requestBean.getParameterList().get(0);
		try {
			Map map=busiFormMapper.queryFlowNo(busiformTb);
			if(map!=null) {
				 cnt=(BigDecimal)map.get("CNT");
				 formId=(String)map.get("FORM_ID");
			}
	        retMap.put("cnt", cnt);
	        retMap.put("formId", formId);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}catch (Exception e) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
		}
		
	}

}
