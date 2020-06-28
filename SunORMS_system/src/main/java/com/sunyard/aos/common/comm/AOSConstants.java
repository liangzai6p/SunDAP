package com.sunyard.aos.common.comm;

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
	public static final String LOGIN_TYPE_PSWD = "1";
	
	/** 登录类型：2-指纹 */
	public static final String LOGIN_TYPE_FINGER = "2";
	
	/** 登录类型：3-人脸识别 */
	public static final String LOGIN_TYPE_FACE = "3";
	
	
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
	public static String DATABASE_TYPE_ORACLE = "0";
	
	/** 数据库类型，1 - DB2 */
	public static String DATABASE_TYPE_DB2 = "1";
	
	/** 数据库类型，2 - MySql */
	public static String DATABASE_TYPE_MYSQL = "2";
	
	/** 数据库默认连接池 */
	public static final String POOLNAME_DEFAULT = "SUNBIZDB";
	
	
	/** 消息类型：登出 */
	public static final String MSG_TYPE_LOGOUT = "logout";
	
	/** 消息类型：系统信息 */
	public static final String MSG_TYPE_MESSAGE = "message";
	
	/** 消息类型：计划任务 */
	public static final String MSG_TYPE_TASK = "task";
	
	/** 消息类型：资料共享 */
	public static final String MSG_TYPE_SHARE = "fileShare";
	
	/** 消息类型：公告 */
	public static final String MSG_TYPE_NOTICE = "notice";
	
	/** 消息类型：提示 */
	public static final String MSG_TYPE_ALERT = "alert";
	
	/** 消息类型：审批 */
	public static final String MSG_TYPE_APPROVAL = "approval";
	
	/** 题目答案分隔符 */
	public static final String QUESTION_SEPARATOR = "，@，";
	
	/** 业务类型父类型标志*/
	public static final String SCOPE_TYPE = "EX1100";
	
	
	/** 文件大小参数*/
	public static final long FILE_SIZE = 2000000;
}
