package com.sunyard.dap.intilligentSchedual.util;

/**
 * @author:		lewe
 * @date:		2017年2月24日 下午7:21:08
 * @description:TODO(公共常量定义)
 */
public class AOSConstants {
	
	/** 字符编码 */
	public static final String ENCODE = "UTF-8";
	
	/** 执行成功标识 0 */
	public static final String HANDLE_SUCCESS = "0";
	
	/** 执行失败标识 1 */
	public static final String HANDLE_FAIL = "1";
	
	/** 运营管理平台  日志标识 */
	public static final String LOG_AOS = "SunAOS";
	
	/** 公共模块-common 日志标识 */
	public static final String LOG_COMMON = "公共模块";
	
	
	/** 应用服务类型：通用处理 0 */
	public static final String SERVER_TYPE_COMMON = "0";
	
	/** 应用服务类型：跑批服务 1 */
	public static final String SERVER_TYPE_BATCH = "1";
	
	/** 应用服务类型：影像识别 2 */
	public static final String SERVER_TYPE_IRP = "2";
	
	
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
	
	
	/** 登录标志 1 */
	public static final String LOGIN_FLAG = "1";
	
	/** 登出标志 0 */
	public static final String LOGOUT_FLAG = "0";
	
	/** 重新登录（断线重连）标志 2 */
	public static final String RELOGIN_FLAG = "2";
	
	/** 登录终端：1-PC */
	public static final String LOGIN_TERMINAL_PC = "1";
	
	/** 登录终端：2-移动端 */
	public static final String LOGIN_TERMINAL_MOBILE = "2";
	
	/** 登录类型：1-密码 */
	public static final String LOGIN_TYPE_PASSWORD = "1";
	
	/** 登录类型：2-指纹 */
	public static final String LOGIN_TYPE_FINGER = "2";
	
	/** 登录类型：4-人脸识别 */
	public static final String LOGIN_TYPE_FACE = "4";
	
	/** 登录类型：3-域登录 */
	public static final String LOGIN_TYPE_AREA = "3";
	
	/** 系统超级管理员 */
	public static final String SYS_ADMIN = ",admin,admin1,admin2,";
	
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
	public static String DATABASE_TYPE_ORACLE = "0";
	
	/** 数据库类型，1 - DB2 */
	public static String DATABASE_TYPE_DB2 = "1";
	
	/** 数据库类型，2 - MySql */
	public static String DATABASE_TYPE_MYSQL = "2";
	
	/** 数据库默认连接池 */
	public static final String POOLNAME_DEFAULT = "SUNBIZDB";
	
	
	/** 消息类型：初始化服务 */
	public static final String MSG_TYPE_INIT = "init";
	
	/** 消息类型：登出 */
	public static final String MSG_TYPE_LOGOUT = "logout";
	
	/** 消息类型：系统信息 */
	public static final String MSG_TYPE_MESSAGE = "message";
	
	/** 消息类型：计划任务 */
	public static final String MSG_TYPE_TASK = "task";
	
	/** 消息类型：流程任务 */
	public static final String MSG_TYPE_FLOW = "flow";
	
	/** 消息类型：资料共享 */
	public static final String MSG_TYPE_SHARE = "fileShare";
	
	/** 消息类型：公告 */
	public static final String MSG_TYPE_NOTICE = "notice";
	
	/** 消息类型：自定义监控预警 */
	public static final String MSG_TYPE_MONITOEWARN = "monitorWarn";
	
	/** 消息类型：岗位到期预警 */
	public static final String MSG_TYPE_POSTTURNWARN = "postTurnWarn";
	
	/** 消息类型：提示 */
	public static final String MSG_TYPE_ALERT = "alert";
	
	/** 消息类型：审批 */
	public static final String MSG_TYPE_APPROVAL = "approval";
	
	/** 消息类型：自动年检 */
	public static final String MSG_TYPE_ANNUAL = "annual";
	
	/** 消息类型：退信登记 */
	public static final String MSG_TYPE_RECONDEAL = "reconDeal";
	
	/** 消息类型：强制登出 */
	public static final String MSG_TYPE_FORCEDLOGOUT = "forcedLogout";
	
	/** 消息类型：强制登出 */
	public static final String MSG_TYPE_RECONCOMP = "reconComp";
	
	/** 消息类型：撤销流程 */
	public static final String MSG_TYPE_CANCELFLOW = "cancelFlow";
	
	/** 题目答案分隔符 */
	public static final String QUESTION_SEPARATOR = "，@，";
	
	/** 业务类型父类型标志*/
	public static final String SCOPE_TYPE = "EX1100";
	
	/** 支持预览文件类型*/
	public static final String FILE_TYPE = "docx,doc,txt,xlsx,xls,ppt,pptx";
	
	/** 支持转pdf图片类型*/
	public static final String IMG_TYPE = "jpg,png,jpeg,gif,bmp";
	
	/** 文件大小参数*/
	public static final long FILE_SIZE = 2000000;
	
	/** 交易码父类型标志 */
	public static final String TRANS_NO = "ES2100";
	
	/** 凭证编码父类型标志 */
	public static final String VOUCH_NO = "ES2200";
	
	/** 数字类型的判断字符串 */
	public static final String INT_TYPE = ",INT,LONG,NUMBER,NUM,INTEGER,";
	
	/** 超长字符串类型的判断字符串 */
	public static final String CLOB_TYPE = ",CLOB,TEXT,BLOB,";
	
	/** 浮点类型的判断字符串 */
	public static final String DOUBLE_TYPE = ",FLOAT,DOUBLE,DECIMAL,NUMERIC,BINARY_DOUBLE,BINARY_FLOAT,";
	
	
	/** 三方数据接口标识：手机号码实名认证 TD001 */
	public static final String TDATA_MOBILE_AUTH = "TD001";
	
	/** 三方数据接口标识：查询企业基本信息 TD002 */
	public static final String TDATA_ENT_BASE_INFO = "TD002";
	
	/** 三方数据接口标识：查询企业严重违法失信信息 TD003 */
	public static final String TDATA_ENT_DISCREDIT_INFO = "TD003";
	
	/** 三方数据接口标识：查询企业行政处罚信息 TD004 */
	public static final String TDATA_ENT_PENALTY_INFO = "TD004";
	
	/** 三方数据接口标识：查询企业经营异常信息 TD005 */
	public static final String TDATA_ENT_ABNORMAL_INFO = "TD005";
	
	/** 三方数据接口标识：查询企业失信人与被执行人信息 TD006 */
	public static final String TDATA_ENT_EXECUTOR_INFO = "TD006";
	
	/** 三方数据接口标识：查询企业股权穿透信息 TD007 */
	public static final String TDATA_ENT_STOCK_INFO = "TD007";
	
	/** 三方数据接口标识：查询企业最终受益人信息 TD008 */
	public static final String TDATA_BENEFICIARY_INFO = "TD008";
	
	/** 扫描影像对应字典号 */
	public static final String SCAN_FILE = "AM1700";
	
	/** 法人证件类型对应字典号 */
	public static final String LEGAL_TYPE = "AM7704";
	
	/** 注册资金币种对应字典号 */
	public static final String MONEY_TYPE = "AM7300";
	
	/** 开户携带对应字典号 */
	public static final String OPEN_DOC = "AM1800";
	
	/** 外表数据源xml配置文件 */
	public static final String EXTERNAL_TABLE = "ExternalTable.xml";

	//要素模块添加方式
	/** 直接添加 **/
	public static final String GENERAL = "0";
	/** Excel导入 **/
	public static final String IMPORT_EXCEL = "1";
	/**库表导入**/
	public static final String IMPORT_TABLE = "2";
	// 自定义参数
	/** 自定义预警启用标识 **/
	public static final String MONITOR_WARN = "1";
	// 前端展示类型
	/** 文本框 **/
	public static final String TEXT_BOX = "1";
	/** 日期框 **/
	public static final String DATE_BOX = "2";
	/** 普通下拉框 **/
	public static final String NORMAL_DROP_DOWN_BOX = "3";
	/** 树形下拉框 **/
	public static final String TREE_DROP_DOWN_BOX = "4";
	/** 复选下拉框 **/
	public static final String MULITIPILE_DROP_DOWN_BOX = "5";
	
	/** 新增角色关联申请 **/
	public static final String EXTERNAL_ADD = "0";
	/** 删除用户角色关联申请 **/
	public static final String EXTERNAL_DELETE = "1";

	/** 正式员工 **/
	public static final String IS_BANKER = "1";
	/** 派遣员工 **/
	public static final String IS_NOT_BANKER = "2";
	
	/**中心端待处理业务排队行领导 权限**/
	public static final String LEADER_SHIP = "1";
	/**中心端待处理业务排队中心号前缀**/
	public static final String CENTER_SHIP = "CNYW2000";
	
	

}
