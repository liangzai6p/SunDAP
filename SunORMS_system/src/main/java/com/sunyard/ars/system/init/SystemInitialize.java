package com.sunyard.ars.system.init;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Resource;

import com.sunyard.aos.common.util.FileUtil;
import com.sunyard.ars.common.pojo.Dictionary;
import com.sunyard.ars.common.util.LogUtil;
import com.sunyard.ars.system.dao.sc.DictionaryMapper;
import org.apache.log4j.Logger;
import org.springframework.util.ResourceUtils;

import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.system.bean.et.ModelFieldResulet;
import com.sunyard.ars.system.bean.et.ProcessChartBean;
import com.sunyard.ars.system.bean.et.ProcessLineBean;
import com.sunyard.ars.system.bean.et.ProcessNodeBean;
import com.sunyard.ars.system.bean.et.UtilNodeActionBean;
import com.sunyard.ars.system.bean.et.UtilNodeBean;
import com.sunyard.ars.system.bean.sc.VoucherInfo;
import com.sunyard.ars.system.dao.et.ProcessChartMapper;
import com.sunyard.ars.system.dao.et.ProcessLineMapper;
import com.sunyard.ars.system.dao.et.ProcessNodeMapper;
import com.sunyard.ars.system.dao.mc.McFieldMapper;
import com.sunyard.ars.system.dao.sc.SystemParameterMapper;
import com.sunyard.ars.system.dao.sc.VoucherInfoMapper;
import com.sunyard.ars.system.pojo.mc.McField;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;

/**
 * 系统初始化
 * @author zgz
 *
 */
public class SystemInitialize {

	private final Logger logger = Logger.getLogger(SystemInitialize.class);

	@Resource
	private SystemParameterMapper systemParameterMapper;

	@Resource
	private DictionaryMapper dictionaryMapper;

	@Resource
	private VoucherInfoMapper scVoucherInfoMapper;
	
	@Resource
	private ProcessChartMapper processChartMapper;
	
	@Resource
	private ProcessNodeMapper processNodeMapper;
	
	@Resource
	private ProcessLineMapper processLineMapper;
	
	@Resource
    public McFieldMapper mcFieldMapper ;

	public void systemInit() {
		logger.info("开始系统初始化。。。。");
		SystemConstants.MODEL_MUST_FIELD.clear();
		SystemConstants.CHART_AND_LINE.clear();
		SystemConstants.ALL_ET_CHARTS.clear();
		SystemConstants.ALL_ET_NODES.clear();
		SystemConstants.END_ET_NODES.clear();
		SystemConstants.CHART_AND_LINE.clear();
		SystemConstants.HT_BUSI_NODES.clear();
		SystemConstants.HT_UTIL_NODES.clear();
		SystemConstants.LIST_UTIL_NODES.clear();
		SystemConstants.FLOW_END_NODES.clear();
		// 获取配置文件绝对路径
		ARSConstants.CONFIGPATH = getProjectPath();
		logger.info("系统根目录初始化成功" + ARSConstants.CONFIGPATH);
		sysParamInit();//先加载系统参数，后面某些数据可直接从ARSConstants.SYSTEM_PARAMETER中获取。
		sysDictionaryInit();//先加载系统参数，后面某些数据可直接从ARSConstants.SYSTEM_PARAMETER中获取。

//		backDateInit();
		//loadVoucherInfo();
		
		//加载xml报表文件
		//loadReportXmlFile();
		
		//差错系统初始化
		loadEtConfigInf();
		
		systemRiskInit();
		
		//看图url相关参数初始化
		urlParamInit();
		
		//工作流参数初始化
		sunflowWorkInit();
		
		if(null != ARSConstants.SYSTEM_PARAMETER.get("FILE_EXCEL_PATH")){
			ARSConstants.FILE_EXCEL_PATH = ARSConstants.SYSTEM_PARAMETER.get("FILE_EXCEL_PATH").getParamValue();
		}
		if(null != ARSConstants.SYSTEM_PARAMETER.get("FILE_EXCEL_PATH")){
			SystemConstants.ETUPLOAD_Folder = ARSConstants.SYSTEM_PARAMETER.get("ETUPLOAD_FOLDER").getParamValue();
		}
		
		
		logger.info("系统初始化结束。。。。");
	}

	private void sysDictionaryInit() {
		logger.info("开始加载字典配置。。。");
		List<Dictionary> dictionarys = dictionaryMapper.selectBySelective(null);
		for (Dictionary dy : dictionarys) {
			ARSConstants.SYSTEM_DICTIONARY.put(dy.getFieldName()+"|"+dy.getCode(), dy);
		}
		logger.info("加载字典配置完成。。。");
	}

	/**
	 * @Title: getProjectPath
	 * @Description: 获取项目根路径
	 * @return String
	 */
	public String getProjectPath() {
		String root = "";
		String configPath = null;
		try {
//			root = SystemInitialize.class.getResource("").getPath();
//			// 解决这个类如果打成jar就有问题了，因为路径中不存在classes
//			if (root.indexOf("WEB-INF") >= 0) {
//				root = URLDecoder.decode((root.substring(0,root.indexOf("WEB-INF") - 1) + File.separator + "WEB-INF"),
//						"UTF-8").replaceAll("%20"," ");
//			}
			root = ResourceUtils.getURL("classpath:").getPath().replaceAll("%20"," ");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		configPath = root + File.separator + "config";
		return configPath;
	}
	
	/**
	 * 将系统参数配置表SM_SYSPARAMETER_TB表数据加载到内存（ARSConstants.SYSTEM_PARAMETER）。
	 */
	public void sysParamInit() {
		logger.info("开始加载系统参数配置。。。");
		List<SysParameter> sysList = systemParameterMapper.selectBySelective(null);
		for (SysParameter sp : sysList) {
			ARSConstants.SYSTEM_PARAMETER.put(sp.getParamItem(), sp);
		}
		logger.info("统参数配置加载完成。。。");
	}
	
	/**
	 * 初始化差错单、核实单等逾期天数
	 */
//	public void backDateInit() {
//		logger.info("开始初始化逾期天数。。。");
//		ARSConstants.BACK_DATE_CC = ARSConstants.SYSTEM_PARAMETER.get("BACK_DATE_CC").getParamValue();
//		ARSConstants.BACK_DATE_HS = ARSConstants.SYSTEM_PARAMETER.get("BACK_DATE_HS").getParamValue();
//		ARSConstants.BACK_DATE_ZJ = ARSConstants.SYSTEM_PARAMETER.get("BACK_DATE_ZJ").getParamValue();
//		logger.info("逾期天数初始化完成。。。");
//	}
	
	/**
	 * 
	 */
	public void urlParamInit() {
		logger.info("开始初始化看图url参数。。。");
		ARSConstants.URL_USE_TIMES = ARSConstants.SYSTEM_PARAMETER.get("URL_USE_TIMES")==null?1:Integer.valueOf(ARSConstants.SYSTEM_PARAMETER.get("URL_USE_TIMES").getParamValue());
		ARSConstants.URL_OVER_TIME = ARSConstants.SYSTEM_PARAMETER.get("URL_OVER_TIME")==null?10:Long.valueOf(ARSConstants.SYSTEM_PARAMETER.get("URL_OVER_TIME").getParamValue());
		logger.info("看图url参数初始化完成。。。");
	}
	
	/**
	 * 工作流参数初始化
	 */
	public void sunflowWorkInit(){
		//后督progress_flag与工作项名称映射
		ARSConstants.SUNFLOW_WORKITEM_MAP = new HashMap<String, String>();
		ARSConstants.SUNFLOW_WORKITEM_MAP.put("10", "ocr");
		ARSConstants.SUNFLOW_WORKITEM_MAP.put("20", "mp");
		ARSConstants.SUNFLOW_WORKITEM_MAP.put("30", "ma");
		ARSConstants.SUNFLOW_WORKITEM_MAP.put("98", "结束节点");

		//工作流锁定超时初始化
		ARSConstants.SUNFLOW_LOCKTIME = ARSConstants.SYSTEM_PARAMETER.get("SUNFLOW_LOCKTIME")==null?10:Integer.valueOf(ARSConstants.SYSTEM_PARAMETER.get("SUNFLOW_LOCKTIME").getParamValue());
	}
	
	/**
	 * 初始化差错流程信息
	 */
	public void loadEtConfigInf() {
		//获取所有流程线
		HashMap<String,Object> condMap = new HashMap<String,Object>();
		condMap.put("chartStart", "1");
		List<ProcessChartBean> chartList = processChartMapper.selectBySelective(condMap);
		for (ProcessChartBean smProcessChartBean : chartList) {
			SystemConstants.ALL_ET_CHARTS.put(smProcessChartBean.getChartNo()+"", smProcessChartBean);
		}
		//所有流线节点
		List<ProcessNodeBean> nodeList = processNodeMapper.selectBySelective(condMap);
		for (ProcessNodeBean smProcessNodeBean : nodeList) {//key值用单据类型|节点
			SystemConstants.ALL_ET_NODES.put(smProcessNodeBean.getFlowChart()+"|"+smProcessNodeBean.getNodeNo(), smProcessNodeBean);
		}
		//结束节点集合,没有后续流程线
		List<ProcessNodeBean> endNodeList = processNodeMapper.selectEndNodeList();
		for (ProcessNodeBean processNodeBean : endNodeList) {//key值用单据类型|节点
			SystemConstants.END_ET_NODES.put(processNodeBean.getFlowChart()+"|"+processNodeBean.getNodeNo(), processNodeBean);
		}

		// 获取流程线
		List<ProcessLineBean> lineList = processLineMapper.selectBySelective(condMap);
		for (ProcessLineBean smProcessLineBean : lineList) {
			TreeMap<String, List<ProcessLineBean>> lineTrees = SystemConstants.CHART_AND_LINE 
					.get(smProcessLineBean.getFlowChart());
			if (null == lineTrees) {
				lineTrees = new TreeMap<String, List<ProcessLineBean>>();
				List<ProcessLineBean> linesList = new ArrayList<ProcessLineBean>();
				// 把该流线放入List中
				linesList.add(smProcessLineBean);
				lineTrees.put(smProcessLineBean.getBoforeNodeNo(), linesList);
				SystemConstants.CHART_AND_LINE.put(smProcessLineBean.getFlowChart(), lineTrees);
			} else { // 该流程线已经处理过
				// 判断前置节点是否有过登记
				List<ProcessLineBean> linesList = lineTrees
						.get(smProcessLineBean.getBoforeNodeNo());
				if (null == linesList || linesList.size() == 0) {
					linesList = new ArrayList<ProcessLineBean>();
					linesList.add(smProcessLineBean);
					lineTrees.put(smProcessLineBean.getBoforeNodeNo(),
							linesList);
					SystemConstants.CHART_AND_LINE.put(smProcessLineBean.getFlowChart(), lineTrees);
				} else {
					linesList.add(smProcessLineBean);
					lineTrees.put(smProcessLineBean.getBoforeNodeNo(),
							linesList);
					SystemConstants.CHART_AND_LINE.put(smProcessLineBean.getFlowChart(), lineTrees);
				}
			}
		}
		List<HashMap<String,Object>> flowEndNodes = processNodeMapper.selectFlowEndNodes();
		for (HashMap<String, Object> flowEndNode : flowEndNodes) {
			SystemConstants.FLOW_END_NODES.put(flowEndNode.get("FORM_TYPE")+"|"+flowEndNode.get("NODE_CODE"), "1");
		}
		/** 业务节点配置表 * */
		SystemConstants.HT_BUSI_NODES = new ReadConfigXML().readBusiNode();
		logger.info(" 已初始化：业务节点配置表");
		/** 单元节点配置表 * */
		SystemConstants.HT_UTIL_NODES = readUtilNodefromDB();
		logger.info(" 已初始化：单元节点配置表");
		SystemConstants.LIST_UTIL_NODES = setUtilNodesList();
	}
	
	/**
	 * 加载凭证类型定义表数据
	 */
	public void loadVoucherInfo() {
		logger.info("开始加载凭证类型列表。。。");
		List<VoucherInfo> voucherList = scVoucherInfoMapper.selectBySelective(null);
		for(VoucherInfo vi : voucherList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("voucherId", String.valueOf(vi.getVoucherId()));
			map.put("voucherNumber", String.valueOf(vi.getVoucherNumber()));
			map.put("voucherName", vi.getVoucherName());
			ARSConstants.LIST_VOUCHERINFO.add(map);
		}
		logger.info("凭证类型列表加载完成。。。");
	}

	/**
	 * 
	 * @Title: readUtilNodefromDB
	 * 
	 * @Description: 从数据库中获取单元节点配置
	 * 
	 * @param @return 设定文件
	 * 
	 * @return Hashtable<String,UtilNodeBean> 返回类型
	 * 
	 * @throws
	 */
	public Hashtable<String, UtilNodeBean> readUtilNodefromDB() {
		Hashtable<String, UtilNodeBean> nodeTable = new Hashtable<String, UtilNodeBean>();

		Map<String, TreeMap<String, List<ProcessLineBean>>> Lists= SystemConstants.CHART_AND_LINE;
		Map<String,ProcessChartBean> charts = SystemConstants.ALL_ET_CHARTS;
		Map<String, ProcessNodeBean> nodes = SystemConstants.ALL_ET_NODES;
		Map<String, ProcessNodeBean> endNodes = SystemConstants.END_ET_NODES;
		
		Iterator<Entry<String, ProcessChartBean>> it = charts.entrySet().iterator();
		
		while(it.hasNext()){
			ProcessChartBean chart = it.next().getValue();
			String chartNo = chart.getChartNo() + ""; // 通知书类型
			String chartName = chart.getChartName(); // 通知书名称

			TreeMap<String, List<ProcessLineBean>> Treelines = Lists
					.get(chartNo);

			if(Treelines == null || Treelines.size() == 0){
				continue;
			}
			
			Iterator<Entry<String, List<ProcessLineBean>>> lineBf = Treelines
					.entrySet().iterator();
			while (lineBf.hasNext()) {
				Entry<String, List<ProcessLineBean>> eet = lineBf.next();
				String fbNode = eet.getKey().toString();
				List<ProcessLineBean> lines = eet.getValue();
				UtilNodeBean utilNodeBean = new UtilNodeBean(); // 新建一个节点对象；
				utilNodeBean.setFormType(chartNo);
				utilNodeBean.setFormName(chartName);
				// 这个地方进行下处理,只需要分析前置节点就可以
				utilNodeBean.setNodeNo(fbNode);
				// 取出fbNode节点信息
				ProcessNodeBean node = nodes.get(chartNo+"|"+fbNode);
				if(node == null){
					logger.error("获取流程线:"+chartNo+",节点:"+fbNode+"前置节点报错，请检查");
					continue;
				}
				utilNodeBean.setNodeName(node.getNodeName());
				utilNodeBean.setNodeDescription(node.getNodeDescription());
				utilNodeBean.setStateText(node.getStateText());
				String isStoreHistory = node.getIsStoreHistory();
				if (null == isStoreHistory || "".equals(isStoreHistory)) { // 默认"yes"
					isStoreHistory = "yes";
				}
				utilNodeBean.setIsStoreHistory(isStoreHistory);
				String onlyShowLastprocess = node.getOnlyShowLastprocess();
				if (null == onlyShowLastprocess
						|| "".equals(onlyShowLastprocess)) { // 默认no
					onlyShowLastprocess = "no";
				}
				utilNodeBean.setOnlyShowLastprocess(onlyShowLastprocess);
				utilNodeBean.setLabelProcess(node.getLabelProcess());
				List<UtilNodeActionBean> nodeActions = new ArrayList<UtilNodeActionBean>();
				if (null != lines && lines.size() > 0) {
					for (int k = 0; k < lines.size(); k++) {
						ProcessLineBean line = lines.get(k);
						UtilNodeActionBean utilNodeActionBean = new UtilNodeActionBean();
						utilNodeActionBean.setToNodeNo(line.getAfterNodeNo());
						utilNodeActionBean.setActionText(line.getNodeContent());

						String isSelected = line.getIsDefaultSelect();
						if ("0".equals(isSelected)) {
							isSelected = "no";
						} else {
							isSelected = "yes";
						}
						utilNodeActionBean.setIsSelected(isSelected);
						utilNodeActionBean.setActionMethod(line
								.getTransferMethod());
						utilNodeActionBean.setDeptMatch("no");
						utilNodeActionBean.setProcessShowLevel("all"); // 暂时默认全部显示
						nodeActions.add(utilNodeActionBean);
					}
					utilNodeBean.setIsShowActionModel("yes");
				} else {
					utilNodeBean.setIsShowActionModel("no");
				}
				utilNodeBean.setNodeActions(nodeActions);
				nodeTable.put(chartNo + "|" + fbNode, utilNodeBean);
			}
			//添加结束节点
			Iterator<Entry<String, ProcessNodeBean>> nodeIt = endNodes.entrySet().iterator();
			while (nodeIt.hasNext()) {
				Entry<String, ProcessNodeBean> nodeEntry = nodeIt.next();
				String nodeKey = nodeEntry.getKey().toString();
				if(chartNo.equals(nodeKey.split("|")[0])){
					ProcessNodeBean node = nodeEntry.getValue();
					UtilNodeBean utilNodeBean = new UtilNodeBean(); // 新建一个节点对象；
					utilNodeBean.setFormType(chartNo);
					utilNodeBean.setFormName(chartName);
					// 这个地方进行下处理,只需要分析前置节点就可以
					utilNodeBean.setNodeNo(node.getNodeNo());
					utilNodeBean.setNodeName(node.getNodeName());
					utilNodeBean.setNodeDescription(node.getNodeDescription());
					utilNodeBean.setStateText(node.getStateText());
					String isStoreHistory = node.getIsStoreHistory();
					if (null == isStoreHistory || "".equals(isStoreHistory)) { // 默认"yes"
						isStoreHistory = "yes";
					}
					utilNodeBean.setIsStoreHistory(isStoreHistory);
					String onlyShowLastprocess = node.getOnlyShowLastprocess();
					if (null == onlyShowLastprocess
							|| "".equals(onlyShowLastprocess)) { // 默认no
						onlyShowLastprocess = "no";
					}
					utilNodeBean.setOnlyShowLastprocess(onlyShowLastprocess);
					utilNodeBean.setLabelProcess(node.getLabelProcess());
					List<UtilNodeActionBean> nodeActions = new ArrayList<UtilNodeActionBean>();
					utilNodeBean.setIsShowActionModel("no");
					utilNodeBean.setNodeActions(nodeActions);
					nodeTable.put(chartNo + "|" + node.getNodeNo(), utilNodeBean);
				}
			}
		}
		return nodeTable;
	}
	
	private List setUtilNodesList() {
		List list = new ArrayList();
		Iterator iterator = SystemConstants.HT_UTIL_NODES.keySet().iterator(); // (formType+"|"+nodeNo,
																			// utilNodeBean);
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			UtilNodeBean utilNodeBean = (UtilNodeBean) SystemConstants.HT_UTIL_NODES
					.get(key);
			list.add(utilNodeBean);
		}
		return list;
	}
	
	public void systemRiskInit() {
        List<McField> fields  = mcFieldMapper.getArmsMustFiedl();
        ModelFieldResulet modelFieldResulet = null;
        for (int i = 0 ; i < fields.size() ;i++){
            modelFieldResulet = new ModelFieldResulet();
            modelFieldResulet.setFieldId(fields.get(i).getId());
            modelFieldResulet.setName(fields.get(i).getName());
            modelFieldResulet.setChName(fields.get(i).getChName());
            modelFieldResulet.setType(Integer.parseInt(fields.get(i).getType()));
            modelFieldResulet.setRowno(100);
            modelFieldResulet.setFormat(0);
            modelFieldResulet.setIsfind(0);
            modelFieldResulet.setIsdropdown(0);
            modelFieldResulet.setIsimportant(0);
            modelFieldResulet.setRelateId(0);
            modelFieldResulet.setIsShow("0");//查询字段不显示
            SystemConstants.MODEL_MUST_FIELD.add(modelFieldResulet);
        }
    }
}
