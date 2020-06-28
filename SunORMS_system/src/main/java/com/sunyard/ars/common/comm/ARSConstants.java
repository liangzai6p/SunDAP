package com.sunyard.ars.common.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.sunyard.ars.common.pojo.Dictionary;
import com.sunyard.cop.IF.mybatis.pojo.SysParameter;

/**
 * @author: lewe
 * @date: 2017年2月24日 下午7:21:08
 * @description:TODO(公共常量定义)
 */
public class ARSConstants {

	/** 字符编码 */
	public static final String ENCODE = "UTF-8";

	/** 执行成功标识 0 */
	public static final String HANDLE_SUCCESS = "0";

	/** 执行失败标识 1 */
	public static final String HANDLE_FAIL = "1";

	/** 后督 日志标识 */
	public static final String LOG_AOS = "SunARS";

	/** 公共模块-common 日志标识 */
	public static final String LOG_COMMON = "公共模块";

	/** 新增操作标识码 OP001 */
	public static final String OPERATE_ADD = "OP001";

	/** 删除操作标识码 OP002 */
	public static final String OPERATE_DELETE = "OP002";

	/** 修改操作标识码 OP003 */
	public static final String OPERATE_MODIFY = "OP003";

	/** 查询操作标识码 OP004 */
	public static final String OPERATE_QUERY = "OP004";

	/** 导入操作标识码 OP998 */
	public static final String OPERATE_IMPORT = "OP998";

	/** 其他操作标识码 OP999 */
	public static final String OPERATE_OTHER = "OP999";

	/** 系统超级管理员 */
	public static final String SYS_ADMIN = "admin";

	/** 每页记录数 **/
	public static final int PAGE_NUM = 15;

	/** 日期格式字符串 yyyyMMdd */
	public static final String FORMAT_DATE = "yyyyMMdd";

	/** 日期时间格式字符串 yyyyMMddHHmmss */
	public static final String FORMAT_DATE_TIME = "yyyyMMddHHmmss";

	/** 默认密码有效期（天） */
	public static final int OVER_DATE = 90;

	/** 数据库字典信息的字典码 DB0000 */
	public static final String DB_FIELD_ID = "DB0000";

	/** 数据库类型，0 - Oracle */
	public final static String DATABASE_TYPE_ORACLE = "0";

	/** 数据库类型，1 - DB2 */
	public final static String DATABASE_TYPE_DB2 = "1";

	/** 数据库类型，2 - MySql */
	public final static String DATABASE_TYPE_MYSQL = "2";

	/** 程序运行数据库类型，默认oracle **/
	public static String DB_TYPE = "0";

	/** 数据库默认连接池 */
	public static final String POOLNAME_DEFAULT = "SUNBIZDB";

	/** 文件大小参数 */
	public static final long FILE_SIZE = 2000000;

	public static final String BUNDLE_KEY = "ApplicationResources";
	/**********************************************
	 * ARS常量
	 ********************************************************/
	public final static String STRING_TYPE_0 = "0";
	public final static String STRING_TYPE_1 = "1";
	public final static String STRING_TYPE_2 = "2";
	public final static String STRING_TYPE_3 = "3";
	public final static String STRING_TYPE_4 = "4";
	public final static String STRING_TYPE_5 = "5";

	/******************************************
	 * scan-影像采集常量开始
	 ********************************************************/
	/** 批次是否已经提交 0-否 **/
	public static final String BATCH_NOT_COMMIT = "0";

	/** 批次有效 **/
	public static final String BATCH_IS_VALID = "1";

	/** 批次无效 **/
	public static final String BATCH_IS_INVALID = "0";

	/*** 批次提交 **/
	public static final String BATCH_COMMIT = "1";

	/** 是否开启实物档案流程控制影像采集开关 */
	public static boolean IS_SCAN_AFTER_FM = false;

	/** 扫描控件同步异步判断标识 0-异步 1-同步 **/
	public static String ISSYN_TYPE = "1";

	/*** 图像状态1在临时库，3在历史库;4批次表已经迁移 5,data表也迁移完毕 **/
	public static final String IMAGE_STATUS = "1";

	public static final String IMAGE_STATUS_2 = "2";

	public static final String IMAGE_STATUS_3 = "3";

	/** 主附件状态 0附件 1主件 */
	public static final String PS_LEVEL = "1";

	/** 流水勾对标志 -1 未勾对流水 0 强制通过（无需勾兑流水） 1流水勾对成功 5保留状态（有细索引数据但和流水无关联） */

	public static final String CHECK_FLAG = "-1";

	/** 是否差错图像 0 非 1 是 */
	public static final String ERROR_FLAG = "0";

	/** 审核标记字段，0代表默认字段，1代表需要业务审核 */
	public static final String IS_AUDIT = "0";

	/** 删除标记 0 正常 1删除 */
	public static final String SELF_DELETE = "0";

	/**
	 * @Fields PROCESS_STATE : 总共6位字符串，默认为000000，第一位作为自动识别，100000代表自动识别成功，200000
	 *         代表自动识别失败，第2位代表人工补录位，010000
	 *         代表人工补录完成，020000代表人工补录未完成，第3位代表审核位，001000代表业务审核完成
	 *         ，002000代表业务审核未完成，第4位000100代表图像已下发补扫
	 *         ，第5位代表归档位，000010代表归档已完成，000020代表归档未完成，第6位预留。
	 */
	public static final String PROCESS_STATE = "000000";

	/** 自动识别失败原因 */
	public static final String REC_FAIL_CAUSE = "";

	/** 所属主件 0主键（默认） 其他附件（值为所属主键批内码） */
	public static final int PRIMARY_INCCODEIN = 0;

	/** 正面： 0双面扫描已扫描（有背面） 1平板扫描仪扫描（无背面） */
	public static final String IS_FRONT_PAGE_NOBACK = "0";

	/** 图像拷贝所属原图的批内码 */
	public static final int COPY_INCCODEIN = 0;

	/** 图像逻辑拷贝标志 0 未拷贝 >0拷贝数 */
	public static final int COPY_REC = 0;

	public static final String TMP_DATA_TB = "BP_TMPDATA_1_TB";

	/** 批次不需要处理 */
	public static final String NO_NEED_PROCESS = "0";
	/** 批次需要处理 */
	public static final String NEED_PROCESS = "1";

	/** 一键设附件默认版面名 */
	//public static String SLAVE_DEFAULT_NAME = "一般附件";

	/** 一键设主件默认版面名 */
	//public static String PRIMARY_DEFAULT_NAME = "通用凭证";

	/** 图像补扫标记 默认0为正常扫描图像标记 1为补扫图像标记 */
	public static final int IMAGE_PATCH_FLAG = 1;

	/** 批次登记打印图形码类型 1-一维码（条形码）， 2-二维码 */
	public static final String FILE_BATCH_BAR_CODE_TYPE = "2";
	/** 批次登记打印图形码图片存放路径（相对工程根目录） */
	public static final String FILE_BATCH_BAR_CODE_IMG_PATH = "barCodeImgs/";
	/** 批次列表导出excel文件保存路径（绝对路径） */
	// public static String FILE_EXCEL_PATH = "/SUNYARD/FILE/excelFiles/";
	public static String FILE_EXCEL_PATH = "/was/SUNYARD/FILE/excelFiles/";

	/***************** 流程节点 begin ********************/
	// 批次清理完成
	public final static String PROCESS_FLAG_99 = "99";
	// 批次处理完成节点
	public final static String PROCESS_FLAG_98 = "98";
	// 补扫核实节点
	public final static String PROCESS_FLAG_40 = "40";
	// 业务审核节点
	public final static String PROCESS_FLAG_30 = "30";
	// 人工节点
	public final static String PROCESS_FLAG_20 = "20";
	// 自动识别
	public final static String PROCESS_FLAG_10 = "10";

	/***************** 流程节点 end ********************/

	/**
	 * 系统参数表数据
	 */
	public static HashMap<String, SysParameter> SYSTEM_PARAMETER = new HashMap<String, SysParameter>();

	/**
	 * 字典表数据
	 */
	public static HashMap<String, Dictionary> SYSTEM_DICTIONARY = new HashMap<String, Dictionary>();

	/**
	 * 凭证类型列表 MapKey:voucherId-编号, voucherNumber-代码, voucherName-名称
	 */
	public static ArrayList<Map<String, String>> LIST_VOUCHERINFO = new ArrayList<Map<String, String>>();

//	
//	/**
//	 * 币种列表
//	 * MapKey:currencyNo-代码, currencyEname-英文名, currencyCname-中文名
//	 */
//	public static ArrayList<Map<String,String>> LIST_CURRENCY_TYPE = new ArrayList<Map<String,String>>();
//	
	/** 处理单逾期天数 **/
	public static String BACK_DATE_CC = ""; // 差错单逾期天数
	public static String BACK_DATE_HS = ""; // 核实单预期天数
	public static String BACK_DATE_ZJ = ""; // 质检单预期天数

	/*******************************
	 * mc-模型配置常量开始
	 ********************************************/
	/* @Fields TABLE_TYPE_PARAMETER : TODO 参数表类型 */
	public static final int TABLE_TYPE_PARAMETER = 0;

	/* @Fields TABLE_TYPE_MIDDLE : TODO 中间表类型 */
	public static final int TABLE_TYPE_MIDDLE = 1;

	/* @Fields TABLE_TYPE_ARMS_MODEL : TODO 风险预警模型表类型 */
	public static final int TABLE_TYPE_ARMS_MODEL = 2;

	/* @Fields TABLE_TYPE_SUPERVISE_MODEL : TODO 重点监督模型表类型 */
	public static final int TABLE_TYPE_SUPERVISE_MODEL = 3;

	/* @Fields MODEL_TYPE_FB : TODO 数据处理模型表类型 */
	public static final int TABLE_TYPE_DATASOURCE = 4;

	/* @Fields HIS_TABLE_SUFFIX : TODO 历史表后缀 */
	public static final String HIS_TABLE_SUFFIX = "_HIS";

	/* @Fields FIELD_TYPE_INT_SQL : TODO 表字段INT型 */
	public static final String FIELD_TYPE_INT_SQL = "INTEGER";

	/* @Fields FIELD_TYPE_NUMBER_SQL : TODO 表字段NUMBER型，系统重启生效 */
	public static final String FIELD_TYPE_NUMBER_SQL = "NUMBER";

	/* @Fields FIELD_TYPE_VARCHAR_SQL : TODO 表字段VARCHAR型 */
	public static final String FIELD_TYPE_VARCHAR_SQL = "VARCHAR";

	/* @Fields FIELD_TYPE_CHAR_SQL : TODO 表字段CHAR型 */
	public static final String FIELD_TYPE_CHAR_SQL = "CHAR";

	/* @Fields FIELD_TYPE_TIME_SQL : TODO 表字段DATE型 */
	public static final String FIELD_TYPE_TIME_SQL = "DATE";

	/* @Fields FIELD_TYPE_DATE_SQL : TODO 表字段DATE型 */
	public static final String FIELD_TYPE_DATE_SQL = "DATE";

	/* @Fields FIELD_NOT_NULL_SQL : TODO 表字段不为空 */
	public static final String FIELD_NOT_NULL_SQL = "NOT NULL";

	/* @Fields FIELD_NOT_NULL_FLAG : TODO 字段不为空标志 */
	public static final int FIELD_NOT_NULL_FLAG = 0;

	/* @Fields FIELD_NULL_FLAG : TODO 字段为空标志 */
	public static final int FIELD_NULL_FLAG = 1;

	/* @Fields FIELD_TYPE_INT : TODO 字段类型int */
	public static final int FIELD_TYPE_INT = 0;

	/* @Fields FIELD_TYPE_NUMBER : TODO 字段类型number */
	public static final int FIELD_TYPE_NUMBER = 1;

	/* @Fields FIELD_TYPE_CHAR : TODO 字段类型char */
	public static final int FIELD_TYPE_CHAR = 2;

	/* @Fields FIELD_TYPE_VARCHAR : TODO 字段类型varchar */
	public static final int FIELD_TYPE_VARCHAR = 3;

	/* @Fields FIELD_TYPE_DATE : TODO 字段类型date */
	public static final int FIELD_TYPE_DATE = 4;

	/* @Fields FIELD_TYPE_TIME : TODO 字段类型time */
	public static final int FIELD_TYPE_TIME = 5;

	/* @Fields DEFULT_STRING_VALUE : TODO 字符串默认值 */
	public static final String DEFULT_STRING_VALUE = "";

	/* @Fields DEFULT_INT_VALUE : TODO 数字默认值 */
	public static final int DEFULT_INT_VALUE = 0;

	/* @Fields DEFULT_DOUBLE_VALUE : TODO */
	public static final Double DEFULT_DOUBLE_VALUE = 0.00;

	/* @Fields DEFULT_LONG_VALUE : TODO 长整数默认值 */
	public static final Long DEFULT_LONG_VALUE = 0l;

	/* @Fields FIELD_DEFAULT_VALUE_SQL : TODO 默认值SQL */
	public static final String FIELD_DEFAULT_VALUE_SQL = "DEFAULT";

	/* @Fields PARAMETER_TABLE_ROW_ID_FILED : TODO 数据行号字段名 */
	public static final String PARAMETER_TABLE_ROW_ID_FILED = "ROW_ID";

	/**
	 * 必须的系统字典
	 */
	public static String[] MUST_MODEL_FIELD = { "MODEL_ID", "MODELROW_ID", "OCCUR_DATE", "SERIAL_NO", "IMAGE_STATE",
			"ISHANDLE", "ERROR_FLAG", "SITE_NO" };

	/* @Fields CONFIGPATH : 配置文件路径，系统重启生效 */
	public static String CONFIGPATH = "C:\\Code\\SunARS\\WebRoot\\WEB-INF\\config";

	/******************* 日志相关参数 **********************************/
	/*
	 * public final static String MODEL_NAME_BUSM = "BUSM"; public final static
	 * String MODEL_NAME_SYSCONFIG = "SC"; public final static String MODEL_NAME_LOG
	 * = "LOG"; public final static String MODEL_NAME_MC = "MC"; public final static
	 * String MODEL_NAME_SUPERVISE = "SUPERVISE"; public final static String
	 * MODEL_NAME_ARMS = "ARMS"; public final static String MODEL_NAME_ET = "ET";
	 * public final static String MODEL_NAME_ARS = "ARS";
	 */

	/** 系统功能 */
	public final static String MODEL_NAME_ARS = "ARS";
	/** 风险预警 risk early warning (包含 警报展现 、检测任务) */
	public final static String MODEL_NAME_RISK_WARNING = "RISK_WARNING";
	/** 会计稽核 account audit */
	public final static String MODEL_NAME_ACCOUNT_AUDIT = "ACC_AUDIT";
	/** 质量检测 */
	public final static String MODEL_NAME_QD = "QD";
	/** 统计分析(报表) */
	public final static String MODEL_NAME_RT = "RT";
	/** 案例库 */
	public final static String MODEL_NAME_CASE_LIBRARY = "CASE_LIBRARY";
	/** 系统运行 (系统管理) */
	public final static String MODEL_NAME_SYS_MANAGEMENT = "SYS_MANAGEMENT";
	/** 数据处理 etl执行器 */
	public final static String MODEL_NAME_ETL = "ETL_EXECUTE";

	// 日志操作类型 增加
	public final static String OPER_TYPE_1 = "1";
	// 日志操作类型 删除
	public final static String OPER_TYPE_2 = "2";
	// 日志操作类型 修改
	public final static String OPER_TYPE_3 = "3";
	// 日志操作类型 查看
	public final static String OPER_TYPE_4 = "4";
	// 日志操作类型 登录
	public final static String OPER_TYPE_5 = "5";
	// 日志操作类型 登出
	public final static String OPER_TYPE_6 = "6";
	// 日志操作类型 转授权
	public final static String OPER_TYPE_7 = "7";
	// 日志操作类型 收回授权
	public final static String OPER_TYPE_8 = "8";

	/**
	 * ******************************************************** ** 风险预警 ------警报信息操作
	 * 入库操作在案例库*******
	 */
	/** 日志操作类型 下发差错 */
	public final static String OPER_TYPE_9 = "9";

	/** 下发提示单 */
	public final static String OPER_TYPE_10 = "12";

	/** 下发预警单 */
	public final static String OPER_TYPE_11 = "13";

	/** 下发办结 */
	public final static String OPER_TYPE_12 = "14";

	/**
	 * *****************警报信息操作 end***************************
	 */

	/**
	 * ******************************* 会计稽核 业务审核操作日志 start
	 * **********************************
	 */

	/** 业务审核 任务操作 */
	public final static String OPER_TYPE_28 = "30";
	/** 图像操作 */
	public final static String OPER_TYPE_29 = "31";
	/** 主附件操作 */
	public final static String OPER_TYPE_30 = "32";
	/** 流水操作 */
	public final static String OPER_TYPE_31 = "33";
	/** 扎帐操作 */
	public final static String OPER_TYPE_32 = "34";
	/**
	 * ******************************* 会计稽核 业务审核操作日志 end
	 * **********************************
	 */

	/** 导出数据，查看 */
	public final static String OPER_TYPE_4_2 = "10";

	/** 导入数据，新增 */
	public final static String OPER_TYPE_1_2 = "11";

	/******************************************
	 * OCR识别结果解析常量
	 *******************************************************/
	/** OCR识别成功 */
	public static final String OCR_SUCCESS = "1";
	/** OCR识别失败 */
	public static final String OCR_FAIL = "0";
	/** 勾对成功--主件 */
	public static final String CHECK_FLAG_SUCCESS_PS = "1";
	/** 勾对成功--附件 */
	public static final String CHECK_FLAG_SUCCESS_PL = "0";
	/** 未勾对 */
	public static final String CHECK_FLAG_FAIL = "-1";
	/** 主附件状态 0附件 */
	public static final String PL_LEVEL = "0";
	/** 识别失败默认版面 **/
	public static String OCRFAIL_FORMNAME = "未知版面";
	/** 附件默认版面 **/
	public static String PL_FORMNAME = "一般附件";

	/**********************************************
	 * ECM看图常量
	 ******************************************************/
	/** ARS系统默认系统来源表id */
	public static String DATA_SOURCE_ARS = "0";


	/** URL使用次数 **/
	public static int URL_USE_TIMES = 10;

	/** URL超时时间--单位：分钟 **/
	public static long URL_OVER_TIME = 10;

	/**********************************************
	 * 工作流常量
	 ***********************************************************/
	/** 1-初始 **/
	public static int SUNFLOW_INITIAL = 1; // 初始
	/** 2-等待申请 */
	public static int SUNFLOW_APPLY_WAITING = 2;// 等待申请
	/** 3-申请中 */
	public static int SUNFLOW_APPLY = 3;// 申请中
	/** 4-运行 */
	public static int SUNFLOW_RUNNING = 4;// 运行
	/** 5-挂起 **/
	public static int SUNFLOW_SUSPEND = 5;// 挂起
	/** 6-完成 **/
	public static int SUNFLOW_COMPLETE = 6;// 完成
	/** 7-终止 **/
	public static int SUNFLOW_TERMINATED = 7;// 终止

	public static String SUNFLOW_USER = "sa";

	/** 后督progressFlag与工作项名称映射 **/
	public static Map<String, String> SUNFLOW_WORKITEM_MAP;

	/**工作流加载任务锁定超时时间，单位分钟**/
	public static int SUNFLOW_LOCKTIME = 10;

	/***************** 归档参数 begin ********************/

	// 归档模式
	public static int archiveMode = 0;
	// 单表归档
	public static final int SINGLE_ARCHVE_MODE = 0;
	// 多表归档
	public static final int MORE_ARCHVE_MODE = 1;

	// 下划线
	public static final String UNDERLINE = "_";

	/***************** 归档参数 end ********************/

	/********************SC系统配置 begin******************/
	/*0-本系统*/
	public static final String DATA_SOURCE_TYPE_ARS="0";
	/*1-外系统*/
	public static final String DATA_SOURCE_TYPE_OTHER="1";

	/********************SC系统配置 end**********************/

	public static String REGEX_HEADER = "[`~]";

	public static String REGEX_LOG = "[`~]";

	public static String REGEX_SPLIT = "[`~]";

	public static String REGEX_SQLPARAM = "[`~]";

}
