package com.sunyard.ars.system.init;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
//import com.sunyard.ars.system.bean.et.BusiNodeBean;
//import com.sunyard.ars.system.bean.et.ModelFieldResulet;
//import com.sunyard.ars.system.bean.et.ProcessChartBean;
//import com.sunyard.ars.system.bean.et.ProcessLineBean;
//import com.sunyard.ars.system.bean.et.ProcessNodeBean;

public class SystemConstants {
	
	/**
	 * 报表文件上传路径（绝对路径）
	 */
	public static String RTUPLOAD_Folder ="/SUNYARD/RT/rptdesign_file";
	/**
	 * 报表导出文件临时目录
	 */
	public static String RTEXPORT_TEMP_Folder ="/SUNYARD/RT/export_file";
	
	/**
	 * 报表可用统计方式
	 */
	public static final String STATISTICSEXP="SUM,AVG,MAX,MIN";

	/********************差错系统常量********************************************/
	/** ******** 模块名 字符串常量*************** */
	public static String MODEL_NAME = "差错管理";
	/**差错结束节点*/
//	public static Map<String,ProcessNodeBean> END_ET_NODES = new HashMap<String, ProcessNodeBean>();

	/**差错所有节点*/
//	public static Map<String,ProcessNodeBean> ALL_ET_NODES = new HashMap<String, ProcessNodeBean>();
	
	/**差错所有流程线*/
//	public static Map<String,ProcessChartBean> ALL_ET_CHARTS = new HashMap<String, ProcessChartBean>();
	/***流程线与流程节线集合*/
//	public static Map<String, TreeMap<String, List<ProcessLineBean>>> CHART_AND_LINE= new HashMap<String, TreeMap<String, List<ProcessLineBean>>>();
	
	/** 业务节点配置表 * */
//	public static TreeMap<String, BusiNodeBean> HT_BUSI_NODES = new TreeMap<String, BusiNodeBean>();

	/** 单元节点配置表 <formType+"|"+nodeNo, utilNodeBean> * */
	public static Hashtable HT_UTIL_NODES = new Hashtable(); // <formType+"|"+nodeNo,utilNodeBean>
	/** 单元节点状态配置列表 <nodeObject> * */
	public static List LIST_UTIL_NODES = new ArrayList();
	
	/** activeFlag的值9 撤单 */
	public static final String ACTIVE_FLAG_CANCEL = "9";
	/** activeFlag的值1 有效 */
	public static final String ACTIVE_FLAG_ACT = "1";
	/** activeFlag的值0 停止流程 */
	public static final String ACTIVE_FLAG_STOP = "0";
	
	/** ******** 通知书来源 值为固定不能随便更改*************** */
	/** 差错系统手动登记 * */
	public static final String SOURCE_FLAG_0 = "0"; // 差错系统手动登记
	/** 质检* */
	public static final String SOURCE_FLAG_1 = "1"; // 质检
	/** 风险预警 * */
	public static final String SOURCE_FLAG_2 = "2"; // 风险预警
	/** 预警单转差错单 * */
	public static final String SOURCE_FLAG_3 = "3"; // 预警单转差错单
	/** 核实单转差错单 * */
	public static final String SOURCE_FLAG_4 = "4"; // 核实单转差错单
	/** 预警单转差错单(质检)* */
	public static final String SOURCE_FLAG_5 = "5"; // 预警单转差错单(质检)
	/** 核实单转差错单(质检) * */
	public static final String SOURCE_FLAG_6 = "6"; // 核实单转差错单(质检)
	
	public static final String SOURCE_FLAG_7 = "7";//业务审核
	
	public static final String SOURCE_FLAG_8 = "8";//人工轧账
	
	public static final String SOURCE_FLAG_9 = "9";//影像监督
	
	/** ******** 通知书类型代码 值为固定不能随便更改*************** */
	/** 差错单 * */
	public static final String FORM_TYPE_0 = "0"; // 差错单
	/** 提示单 * */
	public static final String FORM_TYPE_1 = "1";
	/** 核实单 * */
	public static final String FORM_TYPE_2 = "2";
	/** 预警单 * */
	public static final String FORM_TYPE_3 = "3";
	/** 业务情况说明书 * */
	public static final String FORM_TYPE_4 = "4";
	/** 内部差错单 * */
	public static final String FORM_TYPE_5 = "5";
	
	/** ******** 新增节点代码 值为固定暂时不能随便更改*************** */
	/** 核实转差错单待修改节点代码 * */
	public static String NEW_NODE_CODE_7 = "19";
	/** 预警转差错单待修改节点代码 * */
	public static String NEW_NODE_CODE_8 = "20";
	/** 差错单新增节点代码 * */
	public static String NEW_NODE_CODE_0 = "21";
	/** 处理单草稿节点代码 * */
	public static String NEW_NODE_CODE_DRAFT = "-1";
	/** 提示单新增节点代码 * */
	public static String NEW_NODE_CODE_1 = "21";
	/** 核实单新增节点代码 * */
	public static String NEW_NODE_CODE_2 = "21";
	/** 核实单节点22不生成单据 * */
	public static String NEW_NODE_CODE_10 = "22";
	/** 预警单新增节点代码 * */
	public static String NEW_NODE_CODE_3 = "24";
	/** 业务情况说明书新增节点代码 * */
	public static String NEW_NODE_CODE_4 = "27";
	/** 内部差错新增节点代码 * */
	public static String NEW_NODE_CODE_5 = "21";
	/** 内部差错草稿节点代码 * */
	public static String NEW_NODE_CODE_6 = "11";
	/** 内部差错复核通过，草稿确认 * */
	public static String NEW_NODE_CODE_9 = "91";
	/** 处理单删除状态 * */
	public static String NEW_NODE_CODE_11 = "11";
	
	/** *消息：任务已被锁定提示*** */
	public static final String MSG_TASK_HAS_LOCKED = "这笔任务正在被其他人处理，已被锁定！";
	/** *消息：任务已被处理锁定提示*** */
	public static final String MSG_TASK_HAS_DONE = "这笔任务已经被其他人处理，请刷新任务列表！";
	/** *消息：任务操作已经过期*** */
	public static final String MSG_TASK_HAS_TIMEOUT = "任务操作已经过期！！打开任务后请及时处理！";
	/** *消息：逾期备注登记成功*** */
	public static final String MSG_ADD_OVERDUE_REMARK_SUCC = "逾期备注登记成功！！";
	/** *消息：停止流程操作成功*** */
	public static final String MSG_STOP_PROCESS_SUCC = "停止流程操作成功！该处理单已放入回收站中！";
	/** *消息：修改有效期程操作成功*** */
	public static final String MSG_CHANGE_ACTIVEDATE_SUCC = "修改有效期操作成功！！";
	
//	public static List<ModelFieldResulet> MODEL_MUST_FIELD = new ArrayList<>();

	/** 在网点整改时，点击查看，修改orverdue_flag_1的值为9。表示已查看。 */
	public static final String OVERDUE_FLAG_1 = "9"; // 在程序中，严禁使用魔法数
	
	/**
	 * 差错附件上传路径（绝对路径）
	 */
	public static String ETUPLOAD_Folder ="/SUNYARD/ET/upload_file";
	
	/**
	 * 差错流程结束节点 <flow_type|node_no,1>
	 */
	public static Hashtable<String,String> FLOW_END_NODES = new Hashtable<>(); 
}
