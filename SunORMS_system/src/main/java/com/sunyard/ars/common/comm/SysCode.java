/*
 * @Syscode.java    2012-12-20 上午9:05:56
 *
 * Copyright (c) 2011 Sunyard System Engineering Co., Ltd.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of 
 * Sunyard System Engineering Co., Ltd. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use it 
 * only in accordance with the terms of the license agreement you entered 
 * into with Sunyard.
 */

package  com.sunyard.ars.common.comm;

import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName: Syscode
 * @Description: TODO 系统错误码
 * @Author：陈慧民
 * @Date： 2012-12-20 上午9:05:56 (创建文件的精确时间)
 */
public class SysCode {
	/* @Fields SYSCODE_888888 : TODO 操作成功 */
	public static final int SYSCODE_888888 = 888888;
	/* 业务配置 */
	// 增加用户失败
	public static final int SYSCODE_100001 = 100001;
	// 修改用户失败
	public static final int SYSCODE_100002 = 100002;
	// 删除用户失败
	public static final int SYSCODE_100003 = 100003;
	// 增加角色失败
	public static final int SYSCODE_100004 = 100004;
	// 修改角色失败
	public static final int SYSCODE_100005 = 100005;
	// 删除角色失败
	public static final int SYSCODE_100006 = 100006;
	// 更新角色权限失败
	public static final int SYSCODE_100007 = 100007;
	// 增加部门失败
	public static final int SYSCODE_100008 = 100008;
	// 修改部门失败
	public static final int SYSCODE_100009 = 100009;
	// 删除部门失败
	public static final int SYSCODE_100010 = 100010;
	// 更新部门权限失败
	public static final int SYSCODE_100011 = 100011;
	// 增加权限失败
	public static final int SYSCODE_100012 = 100012;
	// 修改权限失败
	public static final int SYSCODE_100013 = 100013;
	// 删除权限失败
	public static final int SYSCODE_100014 = 100014;
	// 增加机构失败
	public static final int SYSCODE_100015 = 100015;
	// 修改权限失败
	public static final int SYSCODE_100016 = 100016;
	// 删除权限失败
	public static final int SYSCODE_100017 = 100017;
	// 增加柜员失败
	public static final int SYSCODE_100018 = 100018;
	// 修改柜员失败
	public static final int SYSCODE_100019 = 100019;
	// 删除柜员失败
	public static final int SYSCODE_100020 = 100020;
	/* 影像查询 */
	/* @Fields SYSCODE_120001 : TODO 连接异常 */
	public static final int SYSCODE_120001 = 120001;
	/* @Fields SYSCODE_120002 : TODO 个性化信息获取失败 */
	public static final int SYSCODE_120002 = 120002;
	/* @Fields SYSCODE_120003 : TODO 数据库操作失败 */
	public static final int SYSCODE_120003 = 120003;
	/* @Fields SYSCODE_120004 : TODO 全局配置信息获取失败 */
	public static final int SYSCODE_120004 = 120004;
	/* @Fields SYSCODE_120005 : TODO session失效 */
	public static final int SYSCODE_120005 = 120005;
	/* @Fields SYSCODE_120006 : TODO 操作失败，数据错误！ */
	public static final int SYSCODE_120006 = 120006;
	/* @Fields SYSCODE_120007 : TODO 无图像 */
	public static final int SYSCODE_120007 = 120007;
	/* @Fields SYSCODE_120008 : TODO 无权限 */
	public static final int SYSCODE_120008 = 120008;
	/* @Fields SYSCODE_120009 : TODO 登录失败 */
	public static final int SYSCODE_120009 = 120009;
	/* @Fields SYSCODE_120010 : TODO 无报表可以展示 */
	public static final int SYSCODE_120010 = 120010;
	/* @Fields SYSCODE_120011 : TODO 报表读取失败 */
	public static final int SYSCODE_120011 = 120011;
	/* @Fields SYSCODE_120012 : TODO 报表下载失败 */
	public static final int SYSCODE_120012 = 120012;
	/* @Fields SYSCODE_120013 : TODO 通信失败 */
	public static final int SYSCODE_120013 = 120013;
	/* @Fields SYSCODE_120014 : TODO 报文发送失败 */
	public static final int SYSCODE_120014 = 120014;
	/* @Fields SYSCODE_120015 : TODO 报文接收失败 */
	public static final int SYSCODE_120015 = 120015;
	/* @Fields SYSCODE_120016 : TODO 没有找到影像平台信息 */
	public static final int SYSCODE_120016 = 120016;
	/* @Fields SYSCODE_120017 : TODO 没有找到报文 */
	public static final int SYSCODE_120017 = 120017;
	/* @Fields SYSCODE_120018 : TODO ECM登陆失败 */
	public static final int SYSCODE_120018 = 120018;
	/* @Fields SYSCODE_120019 : TODO 看图来源模块不明 */
	public static final int SYSCODE_120019 = 120019;
	
	/* 报表管理 */
	/* @Fields SYSCODE_130001 : TODO 删除文件失败! */
	public static final int SYSCODE_130001 = 130001;
	/* @Fields SYSCODE_130002 : TODO session失效! */
	public static final int SYSCODE_130002 = 130002;
	/* @Fields SYSCODE_130003 : TODO 下载失败! */
	public static final int SYSCODE_130003 = 130003;
	/* @Fields SYSCODE_130004 : TODO 登录失败! */
	public static final int SYSCODE_130004 = 130004;
	/* 模型配置 */
	/* @Fields SYSCODE_140001 : TODO 删除数据源被关联 */
	public static final int SYSCODE_140001 = 140001;
	/* @Fields SYSCODE_140002 : TODO 删除字段被关联 */
	public static final int SYSCODE_140002 = 140002;
	/* @Fields SYSCODE_140003 : TODO 删除表被关联 */
	public static final int SYSCODE_140003 = 140003;
	/* @Fields SYSCODE_140004 : TODO 表未定义 */
	public static final int SYSCODE_140004 = 140004;
	/* @Fields SYSCODE_140005 : TODO 表删除成功 */
	public static final int SYSCODE_140005 = 140005;
	/* @Fields SYSCODE_140006 : TODO 表创建成功 */
	public static final int SYSCODE_140006 = 140006;
	/* @Fields SYSCODE_140007 : TODO 表结构修改成功 */
	public static final int SYSCODE_140007 = 140007;
	/* @Fields SYSCODE_140008 : TODO 表信息未修改 */
	public static final int SYSCODE_140008 = 140008;
	/* @Fields SYSCODE_140009 : TODO 删除模型被关联 */
	public static final int SYSCODE_140009 = 140009;
	/* @Fields SYSCODE_140010 : TODO 删除视图被关联 */
	public static final int SYSCODE_140010 = 140010;
	/* @Fields SYSCODE_140011 : TODO 删除视图列被关联 */
	public static final int SYSCODE_140011 = 140011;
	/* @Fields SYSCODE_140012 : TODO 删除视图列条件被关联 */
	public static final int SYSCODE_140012 = 140012;
	/* @Fields SYSCODE_140013 : TODO 新增表名重复 */
	public static final int SYSCODE_140013 = 140013;
	/* @Fields SYSCODE_140014 : TODO 表未创建 */
	public static final int SYSCODE_140014 = 140014;
	/* @Fields SYSCODE_140015 : TODO 新增视图名重复 */
	public static final int SYSCODE_140015 = 140015;
	/* @Fields SYSCODE_140016 : TODO 新增数据源名重复 */
	public static final int SYSCODE_140016 = 140016;
	/* @Fields SYSCODE_140017 : TODO 新增系统字段重复 */
	public static final int SYSCODE_140017 = 140017;
	/* @Fields SYSCODE_140018 : TODO 新增字段名重复 */ 
	public static final int SYSCODE_140018 = 140018;
	
	/* @Fields SYSCODE_140019 : TODO 表空间不存在 */ 
	public static final int SYSCODE_140019 = 140019;
	
	
	/* @Fields SYSCODE_140020 : TODO 表已经创建无法删除*/ 
	public static final int SYSCODE_140020 = 140020;
	/* @Fields SYSCODE_140019 : TODO 索引表空间不存在 */ 
	public static final int SYSCODE_140021 = 140021;
	/* @Fields SYSCODE_140022 : TODO 索引表空间不存在 */ 
	public static final int SYSCODE_140022 = 140022;
	
	/* @Fields SYSCODE_140023 : TODO 修改参数表数据出错 */ 
	public static final int SYSCODE_140023 = 140023;
	
	/* @Fields SYSCODE_140023 : TODO 表已经创建无法修改 */ 
	public static final int SYSCODE_140024 = 140024;
	/* @Fields SYSCODE_140025 : TODO 表字段重复，无法创建 */ 
	public static final int SYSCODE_140025 = 140025;
	/* @Fields SYSCODE_140026 : TODO 展现字段重复，无法创建 */ 
	public static final int SYSCODE_140026 = 140026;
	
	/* @Fields SYSCODE_140027 : TODO 字段被已创建表关联，无法修改 */ 
	public static final int SYSCODE_140027 = 140027;
	
	/* 风险预警 */
	/* @Fields SYSCODE_150001 : TODO 处理单下发成功 */
	public static final int SYSCODE_150001 = 150001;
	/* @Fields SYSCODE_150002 : TODO 检查记录提交失败 */
	public static final int SYSCODE_150002 = 150002;
	/* @Fields SYSCODE_150003 : TODO 处理单下发失败 */
	public static final int SYSCODE_150003 = 150003;
	/* @Fields SYSCODE_150003 : TODO 预警看图错误提示 */
	public static final int SYSCODE_150004 = 150004;
	/* 重点监督 */
	/* @Fields SYSCODE_160001 : TODO 处理单下发成功 */
	public static final int SYSCODE_160001 = 160001;
	/* @Fields SYSCODE_160002 : TODO 任务已被锁定 */
	public static final int SYSCODE_160002 = 160002;
	/* @Fields SYSCODE_160003 : TODO 通信失败 */
	public static final int SYSCODE_160003 = 160003;
	/* @Fields SYSCODE_160004 : TODO 报文发送失败 */
	public static final int SYSCODE_160004 = 160004;
	/* @Fields SYSCODE_160005 : TODO 报文接收失败 */
	public static final int SYSCODE_160005 = 160005;
	/* @Fields SYSCODE_160006 : TODO 身份核查记录查询失败 */
	public static final int SYSCODE_160006 = 160006;
	/* @Fields SYSCODE_160007 : TODO 该用户尚未设置监控终端IP地址! */
	public static final int SYSCODE_160007 = 160007;
	/* @Fields SYSCODE_160008 : TODO 尚未设置终端信息 */
	public static final int SYSCODE_160008 = 160008;
	/* @Fields SYSCODE_160009 : TODO 任务已处理 */
	public static final int SYSCODE_160009 = 160009;
	
	
	/* @Fields SYSCODE_160010 : TODO 该模型展现字段未配置展现字段 */
	public static final int SYSCODE_160010 = 160010;

	/* 强制补录 */
	public static final int SYSCODE_170000 = 170000;
	public static final int SYSCODE_170001 = 170001;
	
	/* 系统监控 */
	/* @Fields SYSCODE_160009 : TODO 任务已处理 */
	public static final int SYSCODE_180000 = 180000;
	/* @Fields SYSCODE_180001 : TODO 获取任务列表失败 */
	public static final int SYSCODE_180001 = 180001;
	/* @Fields SYSCODE_180002 : TODO 批次已经扎帐,请解除扎帐*/
	public static final int SYSCODE_180002 = 180002;
	/* @Fields SYSCODE_180003 : TODO 批次已经归档 */
	public static final int SYSCODE_180003= 180003;
	/* @Fields SYSCODE_180004 : TODO 批次修改失败 */
	public static final int SYSCODE_180004= 180004;
	/* 差错管理 */
	/* @Fields SYSCODE_190004 : TODO 对系统没有操作权限! */
	public static final int SYSCODE_190004 = 190004;
	/* @Fields SYSCODE_190005 : TODO 用户名或密码错误! */
	public static final int SYSCODE_190005 = 190005;
	/* @Fields SYSCODE_190006 : TODO 登录失败 */
	public static final int SYSCODE_190006 = 190006;
	/* @Fields SYSCODE_190007 : TODO 任务已被锁定 */
	public static final int SYSCODE_190007 = 190007;
	/* @Fields SYSCODE_190008 : TODO 无操作权限！ */
	public static final int SYSCODE_190008 = 190008;
	/* @Fields SYSCODE_190009 : TODO nodeNo=null，程序存在bug，请联系管理员！ */
	public static final int SYSCODE_190009 = 190009;
	/* @Fields SYSCODE_190010 : TODO 重置成功！ */
	public static final int SYSCODE_190010 = 190010;
	/*
	 * @Fields SYSCODE_190011 : TODO method=doProcessDeal,
	 * formId=null，程序存在bug，请联系管理员！
	 */
	public static final int SYSCODE_190011 = 190011;
	/* @Fields SYSCODE_190012 : TODO 停止流程操作成功！该处理单已放入回收站中！ */
	public static final int SYSCODE_190012 = 190012;
	/* @Fields SYSCODE_190013 : TODO 解锁成功！ */
	public static final int SYSCODE_190013 = 190013;
	/* @Fields SYSCODE_190014 : TODO 信息已处理或正在处理,请不要重复刷新! */
	public static final int SYSCODE_190014 = 190014;
	/* @Fields SYSCODE_190015 : TODO 提交失败！ */
	public static final int SYSCODE_190015 = 190015;
	/* @Fields SYSCODE_190016 : TODO busiNodeNo 为null，程序存在BUG，请与管理员联系！ */
	public static final int SYSCODE_190016 = 190016;
	/* @Fields SYSCODE_190017 : TODO 逾期备注登记成功！！ */
	public static final int SYSCODE_190017 = 190017;
	/* @Fields SYSCODE_190018 : TODO 停止流程操作成功！该处理单已放入回收站中！ */
	public static final int SYSCODE_190018 = 190018;
	/* @Fields SYSCODE_190019 : TODO 请选择上传的文件! */
	public static final int SYSCODE_190019 = 190019;
	/* @Fields SYSCODE_190020 : TODO 请选择上传的目录! */
	public static final int SYSCODE_190020 = 190020;
	/* @Fields SYSCODE_190021 : TODO 选择上传的文件已经存在,如果要替换该文件,请返回选择[覆盖上传]! */
	public static final int SYSCODE_190021 = 190021;
	/* @Fields SYSCODE_190022 : TODO 上传文件失败,请重试! */
	public static final int SYSCODE_190022 = 190022;
	/* @Fields SYSCODE_190023 : TODO 已彻底删除 */
	public static final int SYSCODE_190023 = 190023;
	/* @Fields SYSCODE_190024 : TODO 修改差错标准失败 */
	public static final int SYSCODE_190024 = 190024;
	/* @Fields SYSCODE_190025 : TODO 无法获取批次表数据表信息！ */
	public static final int SYSCODE_190025 = 190025;
	/* 系统配置 */
	/* @Fields SYSCODE_200001 : TODO 增加字段定义失败 */
	public static final int SYSCODE_200001 = 200001;
	/* @Fields SYSCODE_200002 : TODO 修改字段定义失败 */
	public static final int SYSCODE_200002 = 200002;
	/* @Fields SYSCODE_200003 : TODO 删除字段定义失败 */
	public static final int SYSCODE_200003 = 200003;
	/* @Fields SYSCODE_200004 : TODO 增加字典定义失败 */
	public static final int SYSCODE_200004 = 200004;
	/* @Fields SYSCODE_200005 : TODO 修改字典定义失败 */
	public static final int SYSCODE_200005 = 200005;
	/* @Fields SYSCODE_200006 : TODO 删除字典定义失败 */
	public static final int SYSCODE_200006 = 200006;
	/* @Fields SYSCODE_200007 : TODO 增加表字段定义失败 */
	public static final int SYSCODE_200007 = 200007;
	/* @Fields SYSCODE_200008 : TODO 修改表字段定义失败 */
	public static final int SYSCODE_200008 = 200008;
	/* @Fields SYSCODE_200009 : TODO 删除表字段定义失败 */
	public static final int SYSCODE_200009 = 200009;
	/* @Fields SYSCODE_200010 : TODO 增加表定义表失败 */
	public static final int SYSCODE_200010 = 200010;
	/* @Fields SYSCODE_200011 : TODO 修改表定义表失败 */
	public static final int SYSCODE_200011 = 200011;
	/* @Fields SYSCODE_200012 : TODO 删除表定义表失败 */
	public static final int SYSCODE_200012 = 200012;
	/* @Fields SYSCODE_200013 : TODO 增加服务注册失败 */
	public static final int SYSCODE_200013 = 200013;
	/* @Fields SYSCODE_200014 : TODO 修改服务注册失败 */
	public static final int SYSCODE_200014 = 200014;
	/* @Fields SYSCODE_200015 : TODO 删除服务注册失败 */
	public static final int SYSCODE_200015 = 200015;
	/* @Fields SYSCODE_200016 : TODO 增加机构数据失败 */
	public static final int SYSCODE_200016 = 200016;
	/* @Fields SYSCODE_200017 : TODO 修改机构数据失败 */
	public static final int SYSCODE_200017 = 200017;
	/* @Fields SYSCODE_200018 : TODO 删除机构数据失败 */
	public static final int SYSCODE_200018 = 200018;
	/* @Fields SYSCODE_200019 : TODO 增加系统参数失败 */
	public static final int SYSCODE_200019 = 200019;
	/* @Fields SYSCODE_200020 : TODO 修改系统参数失败 */
	public static final int SYSCODE_200020 = 200020;
	/* @Fields SYSCODE_200021 : TODO 删除系统参数失败 */
	public static final int SYSCODE_200021 = 200021;
	/* @Fields SYSCODE_200022 : TODO 增加内外数据失败 */
	public static final int SYSCODE_200022 = 200022;
	/* @Fields SYSCODE_200023 : TODO 修改内外数据失败 */
	public static final int SYSCODE_200023 = 200023;
	/* @Fields SYSCODE_200024 : TODO 删除内外数据失败 */
	public static final int SYSCODE_200024 = 200024;
	/* @Fields SYSCODE_200025 : TODO 增加版面业务配置失败 */
	public static final int SYSCODE_200025 = 200025;
	/* @Fields SYSCODE_200026 : TODO 修改版面业务配置失败 */
	public static final int SYSCODE_200026 = 200026;
	/* @Fields SYSCODE_200027 : TODO 删除版面业务配置失败 */
	public static final int SYSCODE_200027 = 200027;
	/* @Fields SYSCODE_200028 : TODO 增加ocr识别域配置失败 */
	public static final int SYSCODE_200028 = 200028;
	/* @Fields SYSCODE_200029 : TODO 修改ocr识别域配置失败 */
	public static final int SYSCODE_200029 = 200029;
	/* @Fields SYSCODE_200030 : TODO 删除ocr识别域配置失败 */
	public static final int SYSCODE_200030 = 200030;
	/* @Fields SYSCODE_200031 : TODO 增加版面人工处理失败 */
	public static final int SYSCODE_200031 = 200031;
	/* @Fields SYSCODE_200032 : TODO 修改版面人工处理失败 */
	public static final int SYSCODE_200032 = 200032;
	/* @Fields SYSCODE_200033 : TODO 删除版面人工处理失败 */
	public static final int SYSCODE_200033 = 200033;
	/* @Fields SYSCODE_200031 : TODO 增加凭证类型定义失败 */
	public static final int SYSCODE_200034 = 200034;
	/* @Fields SYSCODE_200032 : TODO 修改凭证类型定义失败 */
	public static final int SYSCODE_200035 = 200035;
	/* @Fields SYSCODE_200033 : TODO 删除凭证类型定义失败 */
	public static final int SYSCODE_200036 = 200036;
	/* @Fields SYSCODE_200037 : TODO 增加业务类型定义失败 */
	public static final int SYSCODE_200037 = 200037;

	/* @Fields SYSCODE_200038 : TODO 修改业务类型定义失败 */
	public static final int SYSCODE_200038 = 200038;
	/* @Fields SYSCODE_200039 : TODO 删除业务类型定义失败 */
	public static final int SYSCODE_200039 = 200039;
	/* @Fields SYSCODE_200037 : TODO 增加业务类型定义失败 */
	/* @Fields SYSCODE_200037 : TODO 增加业务类型定义失败 */
	public static final int SYSCODE_200040 = 200040;

	/* @Fields SYSCODE_200038 : TODO 修改业务类型定义失败 */
	public static final int SYSCODE_200041 = 200041;
	/* @Fields SYSCODE_200039 : TODO 删除业务类型定义失败 */
	public static final int SYSCODE_200042 = 200042;

	/* @Fields SYSCODE_200037 : TODO 增加后督与用户关系数据失败 */
	public static final int SYSCODE_200043 = 200043;
	/* @Fields SYSCODE_200037 : TODO 修改后督与用户关系数据失败 */
	public static final int SYSCODE_200044 = 200044;
	/* @Fields SYSCODE_200037 : TODO 删除后督与用户关系数据失败 */
	public static final int SYSCODE_200045 = 200045;
	
	/* @Fields SYSCODE_200037 : TODO 增加模块定义失败 */
	public static final int SYSCODE_200046 = 200046;
	/* @Fields SYSCODE_200037 : TODO 修改模块定义失败 */
	public static final int SYSCODE_200047 = 200047;
	/* @Fields SYSCODE_200037 : TODO 删除模块定义失败 */
	public static final int SYSCODE_200048 = 200048;
	
	/* @Fields SYSCODE_200049 : TODO 增加系统来源失败 */
	public static final int SYSCODE_200049 = 200049;
	/* @Fields SYSCODE_200050 : TODO 修改系统来源失败 */
	public static final int SYSCODE_200050 = 200050;
	/* @Fields SYSCODE_200051 : TODO 删除系统来源失败 */
	public static final int SYSCODE_200051 = 200051;
	
	/* 影像采集 */
	/* @Fields SYSCODE_300002 : TODO 中心应用连接异常 */
	public static final int SYSCODE_300002 = 300002;
	/* @Fields SYSCODE_300003 : TODO 个性化信息获取失败 */
	public static final int SYSCODE_300003 = 300003;
	/* @Fields SYSCODE_300004 : TODO 数据库操作失败 */
	public static final int SYSCODE_300004 = 300004;
	/* @Fields SYSCODE_300005 : TODO 全局配置信息获取失败 */
	public static final int SYSCODE_300005 = 300005;
	/* @Fields SYSCODE_300007 : TODO 本地任务读取失败 */
	public static final int SYSCODE_300007 = 300007;
	/* @Fields SYSCODE_300009 : TODO 批次已提交日结 */
	public static final int SYSCODE_300009 = 300009;
	/* @Fields SYSCODE_300010 : TODO 批次登记失败 */
	public static final int SYSCODE_300010 = 300010;
	/* @Fields SYSCODE_300012 : TODO 批次信息登记失败 */
	public static final int SYSCODE_300012 = 300012;
	/* @Fields SYSCODE_300013 : TODO 补扫批次不存在 */
	public static final int SYSCODE_300013 = 300013;
	/* @Fields SYSCODE_300015 : TODO 待修改批次不存在 */
	public static final int SYSCODE_300015 = 300015;
	/* @Fields SYSCODE_300020 : TODO 批次删除失败 */
	public static final int SYSCODE_300020 = 300020;
	/* @Fields SYSCODE_300021 : TODO 扫描张数和登记张数不一致 */
	public static final int SYSCODE_300021 = 300021;
	/* @Fields SYSCODE_300022 : TODO 图像上传失败 */
	public static final int SYSCODE_300022 = 300022;
	/* @Fields SYSCODE_300024 : TODO 批次已经失效 */
	public static final int SYSCODE_300024 = 300024;
	/* @Fields SYSCODE_300025 : TODO 补扫批次提交失败 */
	public static final int SYSCODE_300025 = 300025;
	/* @Fields SYSCODE_300027 : TODO 批次日结失败 */
	public static final int SYSCODE_300027 = 300027;
	/* @Fields SYSCODE_300028 : TODO 图像删除失败 */
	public static final int SYSCODE_300028 = 300028;
	/* @Fields SYSCODE_300033 : TODO 个性化信息保存失败 */
	public static final int SYSCODE_300033 = 300033;
	/* @Fields SYSCODE_300034 : TODO 登记批次已经存在 */
	public static final int SYSCODE_300034 = 300034;
	/* @Fields SYSCODE_300035 : TODO 批次登记用户和当前用户不符 */
	public static final int SYSCODE_300035 = 300035;
	/* @Fields SYSCODE_300036 : TODO 当前机构在机构数据表中没有注册 */
	public static final int SYSCODE_300036 = 300036;
	/* @Fields SYSCODE_300037 : TODO 批次表在表定义表中没有注册 */
	public static final int SYSCODE_300037 = 300037;
	/* @Fields SYSCODE_300039 : TODO 批次数据保存失败 */
	public static final int SYSCODE_300039 = 300039;
	/* @Fields SYSCODE_300041 : TODO 获取补扫批次失败，该任务已被处理*/
	public static final int SYSCODE_300041 = 300041;
	/* @Fields SYSCODE_300042 : TODO 批次修改失败 */
	public static final int SYSCODE_300042 = 300042;
	/* @Fields SYSCODE_300043 : TODO 批次数据删除失败 */
	public static final int SYSCODE_300043 = 300043;
	/* @Fields SYSCODE_300044 : TODO 补扫批次登记失败 */
	public static final int SYSCODE_300044 = 300044;
	/* @Fields SYSCODE_300045 : TODO 补扫批次获取失败 */
	public static final int SYSCODE_300045 = 300045;
	/* @Fields SYSCODE_300046 : TODO 系统密钥为空 */
	public static final int SYSCODE_300046 = 300046;
	/* @Fields SYSCODE_300047 : TODO 中心应用服务信息为空 */
	public static final int SYSCODE_300047 = 300047;
	/* @Fields SYSCODE_300048 : TODO 影像平台服务信息为空 */
	public static final int SYSCODE_300048 = 300048;
	/* @Fields SYSCODE_300049 : TODO 快捷键获取失败 */
	public static final int SYSCODE_300049 = 300049;
	/* @Fields SYSCODE_300050 : TODO 影像平台更新属性发生异常 */
	public static final int SYSCODE_300050 = 300050;
	/* @Fields SYSCODE_300051 : TODO 影像平台更新属性失败 */
	public static final int SYSCODE_300051 = 300051;
	/* @Fields SYSCODE_300052 : TODO 影像平台连接失败失败 */
	public static final int SYSCODE_300052 = 300052;
	/* @Fields SYSCODE_300053 : TODO 快捷键保存失败 */
	public static final int SYSCODE_300053 = 300053;
	/* @Fields SYSCODE_300056 : TODO 批次已被登记 */
	public static final int SYSCODE_300056 = 300056;
	/* @Fields SYSCODE_300057 : TODO 获取机构服务注册信息失败 */
	public static final int SYSCODE_300057 = 300057;
	/* @Fields SYSCODE_300058 : TODO 获取传输平台密钥失败 */
	public static final int SYSCODE_300058 = 300058;
	/* @Fields SYSCODE_300059 : TODO 设置快捷键失败 */
	public static final int SYSCODE_300059 = 300059;
	/* @Fields SYSCODE_300060 : TODO 该批次已被登记为凭证类型 */
	public static final int SYSCODE_300060 = 300060;
	/* @Fields SYSCODE_300061 : TODO 同一操作员在不同PC机上登记已经登记过且未提交的批次 */
	public static final int SYSCODE_300061 = 300061;
	/* @Fields SYSCODE_300062 : TODO 提交批次时批次登记为无效 */
	public static final int SYSCODE_300062 = 300062;
	/* @Fields SYSCODE_300063 : TODO 提交批次时批次登记信息不存在 */
	public static final int SYSCODE_300063 = 300063;
	/* @Fields SYSCODE_300064 : 业务类型为凭证的相同批次已提交过 */
	public static final int SYSCODE_300064 = 300064;
	/* @Fields SYSCODE_300065 :  当前流水的同一批次已提交过*/
	public static final int SYSCODE_300065 = 300065;
	//该批次已锁定，不能修改
	public static final int SYSCODE_300066 = 300066;
	/* @Fields SYSCODE_300067 :  登记的批次要素已经轧账*/
	public static final int SYSCODE_300067 = 300067;
	/* @Fields SYSCODE_300068 :  登记的批次要素已经归档*/
	public static final int SYSCODE_300068 = 300068;
	/* @Fields SYSCODE_300069 :  未有未提交的批次需要初始化*/
	public static final int SYSCODE_300069 = 300069;
	/* @Fields SYSCODE_300070 :  已有无纸化批次登记提交*/
	public static final int SYSCODE_300070 = 300070;
	/* @Fields SYSCODE_300070 :  已提交，有自己登记批次未提交*/
	public static final int SYSCODE_300071 = 300071;
	/* @Fields SYSCODE_300070 :  已提交，其他用户登记未提交*/
	public static final int SYSCODE_300072 = 300072;
	/* @Fields SYSCODE_300070 :  已提交批次*/
	public static final int SYSCODE_300073 = 300073;
	/* @Fields SYSCODE_300070 :  自己登记未提交*/
	public static final int SYSCODE_300074 = 300074;
	/* @Fields SYSCODE_300070 :  被其他用户登记未提交*/
	public static final int SYSCODE_300075 = 300075;
	/* @Fields SYSCODE_300070 :  被其他用户登记未提交*/
	public static final int SYSCODE_300076 = 300076;
	
	/* 人工处理，业务审核 */
	public static final int SYSCODE_500000 = 500000;
	public static final int SYSCODE_500001 = 500001;
	public static final int SYSCODE_500028 = 500028;
	public static final int SYSCODE_500029 = 500029;
	public static final int SYSCODE_500037 = 500037;
	public static final int SYSCODE_500040 = 500040;
	public static final int SYSCODE_500041 = 500041;
	public static final int SYSCODE_500043 = 500043;
	public static final int SYSCODE_500046 = 500046;
	public static final int SYSCODE_500048 = 500048;
	public static final int SYSCODE_500049 = 500049;
	public static final int SYSCODE_500050 = 500050;
	public static final int SYSCODE_500051 = 500051;
	public static final int SYSCODE_500052 = 500052;
	public static final int SYSCODE_500053 = 500053;
	public static final int SYSCODE_500054 = 500054;
	public static final int SYSCODE_500056 = 500056;
	public static final int SYSCODE_500057 = 500057;
	public static final int SYSCODE_500058 = 500058;
	public static final int SYSCODE_500059 = 500059;
	public static final int SYSCODE_500060 = 500060;
	public static final int SYSCODE_500061 = 500061;
	public static final int SYSCODE_500062 = 500062;
	public static final int SYSCODE_500063 = 500063;
	public static final int SYSCODE_500064 = 500064;
	public static final int SYSCODE_500065 = 500065;
	public static final int SYSCODE_500066 = 500066;
	public static final int SYSCODE_500067 = 500067;
	public static final int SYSCODE_500068 = 500068;
	/* 人工查账 */
	public static final int SYSCODE_700000 = 700000;
	public static final int SYSCODE_700001 = 700001;
	/* @Fields SYSCODE_999999 : TODO 系统错误 */
	public static final int SYSCODE_999999 = 999999;
	public static final Map<Integer, String> sysCodeMap = new HashMap<Integer, String>();

	//数据处理
	/* @Fields SYSCODE_240001 : 任务ID已经存在 */
	public static final int SYSCODE_240001 = 240001;
	/* @Fields SYSCODE_240002 : 任务已经被关联 */
	public static final int SYSCODE_240002 = 240002;
	/* @Fields SYSCODE_240002 : 任务已经被关联 */
	public static final int SYSCODE_240003 = 240003;
	/* @Fields SYSCODE_240004 : 任务已经被关联 */
	public static final int SYSCODE_240004 = 240004;

	
	//学习案例库
	/* @Fields SYSCODE_250001 : 学习案例文件存在 */
	public static final int SYSCODE_250001 = 250001;
	/**
	 * 学习库上传文件大小超过限制。
	 */
	public static final int SYSCODE_250002 = 250002;
	
	
	
	//实物档案
	/* @Fields SYSCODE_800001 : 实物档案中该分行/总行未定义档案代码 */
	public static final int SYSCODE_800001 = 800001;
	
	static {
		sysCodeMap.put(SYSCODE_888888, "操作成功");
		sysCodeMap.put(SYSCODE_100001, "增加用户失败");
		sysCodeMap.put(SYSCODE_100002, "修改用户失败");
		sysCodeMap.put(SYSCODE_100003, "删除用户失败");
		sysCodeMap.put(SYSCODE_100004, "增加角色失败");
		sysCodeMap.put(SYSCODE_100005, "修改角色失败");
		sysCodeMap.put(SYSCODE_100006, "删除角色失败");
		sysCodeMap.put(SYSCODE_100007, "更新角色权限失败");
		sysCodeMap.put(SYSCODE_100008, "增加部门失败");
		sysCodeMap.put(SYSCODE_100009, "修改部门失败");
		sysCodeMap.put(SYSCODE_100010, "删除部门失败");
		sysCodeMap.put(SYSCODE_100011, "更新部门权限失败");
		sysCodeMap.put(SYSCODE_100012, "增加权限失败");
		sysCodeMap.put(SYSCODE_100013, "修改权限失败");
		sysCodeMap.put(SYSCODE_100014, "删除权限失败");
		sysCodeMap.put(SYSCODE_100015, "增加机构失败");
		sysCodeMap.put(SYSCODE_100016, "修改机构失败");
		sysCodeMap.put(SYSCODE_100017, "删除机构失败");
		sysCodeMap.put(SYSCODE_100018, "增加柜员失败");
		sysCodeMap.put(SYSCODE_100019, "修改柜员失败");
		sysCodeMap.put(SYSCODE_100020, "删除柜员失败");
		sysCodeMap.put(SYSCODE_120001, "数据库连接异常");
		sysCodeMap.put(SYSCODE_120002, "个性化信息获取失败");
		sysCodeMap.put(SYSCODE_120003, "数据库操作失败");
		sysCodeMap.put(SYSCODE_120004, "全局配置信息获取失败");
		sysCodeMap.put(SYSCODE_120005, "会话已过期，请重新登录");
		sysCodeMap.put(SYSCODE_120006, "操作失败，数据错误!");
		sysCodeMap.put(SYSCODE_120007, "无图像!");
		sysCodeMap.put(SYSCODE_120008, "操作无权限!");
		sysCodeMap.put(SYSCODE_120009, "用户名或密码错误，登录失败!");
		sysCodeMap.put(SYSCODE_120010, "无报表可以展示!");
		sysCodeMap.put(SYSCODE_120011, "报表读取失败，请检查!");
		sysCodeMap.put(SYSCODE_120012, "报表文件下载失败，请与系统管理员联系!");
		sysCodeMap.put(SYSCODE_120013, "通信失败，请与系统管理员联系!");
		sysCodeMap.put(SYSCODE_120014, "报文发送异常，请与系统管理员联系!");
		sysCodeMap.put(SYSCODE_120015, "报文接收异常，请与系统管理员联系!");
		sysCodeMap.put(SYSCODE_120016, "没有找到影像平台信息");
		sysCodeMap.put(SYSCODE_120017, "没有找到报文");
		sysCodeMap.put(SYSCODE_120018, "ECM登陆失败!");
		sysCodeMap.put(SYSCODE_120019, "看图来源模块不明!");
		sysCodeMap.put(SYSCODE_130001, "删除文件失败!");
		sysCodeMap.put(SYSCODE_130002, "session已失效，请重新登录!");
		sysCodeMap.put(SYSCODE_130003, "下载文件失败!");
		sysCodeMap.put(SYSCODE_130004, "登录失败!");
		sysCodeMap.put(SYSCODE_140001, "删除数据源被关联");
		sysCodeMap.put(SYSCODE_140002, "删除字段被关联");
		sysCodeMap.put(SYSCODE_140003, "删除表被关联");
		sysCodeMap.put(SYSCODE_140004, "表未定义");
		sysCodeMap.put(SYSCODE_140005, "表删除成功");
		sysCodeMap.put(SYSCODE_140006, "表创建成功");
		sysCodeMap.put(SYSCODE_140007, "表结构修改成功");
		sysCodeMap.put(SYSCODE_140008, "表信息未修改");
		sysCodeMap.put(SYSCODE_140009, "删除模型被关联");
		sysCodeMap.put(SYSCODE_140010, "删除视图被关联");
		sysCodeMap.put(SYSCODE_140011, "删除视图列被关联");
		sysCodeMap.put(SYSCODE_140012, "删除视图列条件被关联");
		sysCodeMap.put(SYSCODE_140013, "新增表名重复");
		sysCodeMap.put(SYSCODE_140014, "表未创建");
		sysCodeMap.put(SYSCODE_140015, "新增视图名重复");
		sysCodeMap.put(SYSCODE_140016, "新增数据源名重复");
		sysCodeMap.put(SYSCODE_140017, "新增系统字段重复");
		sysCodeMap.put(SYSCODE_140018, "新增字段名重复");
		sysCodeMap.put(SYSCODE_140019, "表空间不存在");
		sysCodeMap.put(SYSCODE_140020, "表已经创建，无法删除");
		sysCodeMap.put(SYSCODE_140021, "索引表空间不存在");
		sysCodeMap.put(SYSCODE_140022, "文件数据为空，请核实！");
		sysCodeMap.put(SYSCODE_150001, "处理单下发成功");
		sysCodeMap.put(SYSCODE_150002, "检查记录提交失败");
		sysCodeMap.put(SYSCODE_150003, "处理单下发失败");
		sysCodeMap.put(SYSCODE_150004, "看图查询条件有为空的现象");
		sysCodeMap.put(SYSCODE_160001, "处理单下发成功");
		sysCodeMap.put(SYSCODE_160002, "该任务已被锁定");
		sysCodeMap.put(SYSCODE_160003, "通信失败，请与系统管理员联系!");
		sysCodeMap.put(SYSCODE_160004, "报文发送异常，请与系统管理员联系!");
		sysCodeMap.put(SYSCODE_160005, "报文接收异常，请与系统管理员联系!");
		sysCodeMap.put(SYSCODE_160006, "查询身份核查记录失败，请与系统管理员联系!");
		sysCodeMap.put(SYSCODE_160007, "用户尚未设置监控终端IP地址!");
		sysCodeMap.put(SYSCODE_160008, "尚未设置终端信息");
		sysCodeMap.put(SYSCODE_160009, "该任务已被处理");
		sysCodeMap.put(SYSCODE_160010, "请到模型配置配置模型展现字段");
		sysCodeMap.put(SYSCODE_170000, "");
		sysCodeMap.put(SYSCODE_170001, "没有找到图像（主键）对应的数据表");
		sysCodeMap.put(SYSCODE_180000, "");
		sysCodeMap.put(SYSCODE_180001, "获取批次列表失败，请检查中心应用服务是否正常运转!");
		sysCodeMap.put(SYSCODE_180002, "批次已经扎帐请解除扎帐再修改");
		sysCodeMap.put(SYSCODE_180003, "批次已经归档无法修改");
		sysCodeMap.put(SYSCODE_180004, "批次修改失败,请刷新批次列表!");
		sysCodeMap.put(SYSCODE_190004, "对系统没有操作权限！");
		sysCodeMap.put(SYSCODE_190005, "用户名或密码错误！");
		sysCodeMap.put(SYSCODE_190006, "登录失败!");
		sysCodeMap.put(SYSCODE_190007, "任务已被锁定!");
		sysCodeMap.put(SYSCODE_190008, "无操作权限！");
		sysCodeMap.put(SYSCODE_190009, "nodeNo=null，程序存在bug，请联系管理员！");
		sysCodeMap.put(SYSCODE_190010, "重置成功！");
		sysCodeMap.put(SYSCODE_190011,
				"method=doProcessDeal, formId=null，程序存在bug，请联系管理员！");
		sysCodeMap.put(SYSCODE_190012, "停止流程操作成功！该处理单已放入回收站中！");
		sysCodeMap.put(SYSCODE_190013, "解锁成功！");
		sysCodeMap.put(SYSCODE_190014, "信息已处理或正在处理,请不要重复刷新!");
		sysCodeMap.put(SYSCODE_190015, "提交失败！");
		sysCodeMap.put(SYSCODE_190016, "busiNodeNo 为null，程序存在BUG，请与管理员联系！");
		sysCodeMap.put(SYSCODE_190017, "逾期备注登记成功！！");
		sysCodeMap.put(SYSCODE_190018, "停止流程操作成功！该处理单已放入回收站中！");
		sysCodeMap.put(SYSCODE_190019, "请选择上传的文件!");
		sysCodeMap.put(SYSCODE_190020, "请选择上传的目录!");
		sysCodeMap.put(SYSCODE_190021, "选择上传的文件已经存在,如果要替换该文件,请返回选择[覆盖上传]!");
		sysCodeMap.put(SYSCODE_190022, "上传文件失败,请重试!");
		sysCodeMap.put(SYSCODE_190023, "已彻底删除!");
		sysCodeMap.put(SYSCODE_190024, "修改差错标准失败!");
		sysCodeMap.put(SYSCODE_190025, "无法获取批次表数据表信息，请联系管理员！");
		sysCodeMap.put(SYSCODE_200001, "增加字段定义失败");
		sysCodeMap.put(SYSCODE_200002, "修改字段定义失败");
		sysCodeMap.put(SYSCODE_200003, "删除字段定义失败");
		sysCodeMap.put(SYSCODE_200004, "增加字典定义失败");
		sysCodeMap.put(SYSCODE_200005, "修改字典定义失败");
		sysCodeMap.put(SYSCODE_200006, "删除字典定义失败");
		sysCodeMap.put(SYSCODE_200007, "增加表字段定义失败");
		sysCodeMap.put(SYSCODE_200008, "修改表字段定义失败");
		sysCodeMap.put(SYSCODE_200009, "删除表字段定义失败");
		sysCodeMap.put(SYSCODE_200010, "增加表定义表失败");
		sysCodeMap.put(SYSCODE_200011, "修改表定义表失败");
		sysCodeMap.put(SYSCODE_200012, "删除表定义表失败");
		sysCodeMap.put(SYSCODE_200013, "增加服务注册失败");
		sysCodeMap.put(SYSCODE_200014, "修改服务注册失败");
		sysCodeMap.put(SYSCODE_200015, "删除服务注册失败");
		sysCodeMap.put(SYSCODE_200016, "增加机构数据失败");
		sysCodeMap.put(SYSCODE_200017, "修改机构数据失败");
		sysCodeMap.put(SYSCODE_200018, "删除机构数据失败");
		sysCodeMap.put(SYSCODE_200019, "增加系统参数失败");
		sysCodeMap.put(SYSCODE_200020, "修改系统参数失败");
		sysCodeMap.put(SYSCODE_200021, "删除系统参数失败");
		sysCodeMap.put(SYSCODE_200022, "增加内外数据失败");
		sysCodeMap.put(SYSCODE_200023, "修改内外数据失败");
		sysCodeMap.put(SYSCODE_200024, "删除内外数据失败");
		sysCodeMap.put(SYSCODE_200025, "增加版面业务配置失败");
		sysCodeMap.put(SYSCODE_200026, "修改版面业务配置失败");
		sysCodeMap.put(SYSCODE_200027, "删除版面业务配置失败");
		sysCodeMap.put(SYSCODE_200028, "增加ocr识别域配置失败");
		sysCodeMap.put(SYSCODE_200029, "修改ocr识别域配置失败");
		sysCodeMap.put(SYSCODE_200030, "删除ocr识别域配置失败");
		sysCodeMap.put(SYSCODE_200031, "增加版面人工处理失败");
		sysCodeMap.put(SYSCODE_200032, "修改版面人工处理失败");
		sysCodeMap.put(SYSCODE_200033, "删除版面人工处理失败");
		sysCodeMap.put(SYSCODE_200034, "增加凭证类型定义失败");
		sysCodeMap.put(SYSCODE_200035, "修改凭证类型定义失败");
		sysCodeMap.put(SYSCODE_200036, "删除凭证类型定义失败");
		sysCodeMap.put(SYSCODE_200037, "增加业务类型定义失败");
		sysCodeMap.put(SYSCODE_200038, "修改业务类型定义失败");
		sysCodeMap.put(SYSCODE_200039, "删除业务类型定义失败");
		sysCodeMap.put(SYSCODE_200040, "增加内容管理平台归档对照表失败");
		sysCodeMap.put(SYSCODE_200041, "修改内容管理平台归档对照表失败");
		sysCodeMap.put(SYSCODE_200042, "删除内容管理平台归档对照表失败");
		sysCodeMap.put(SYSCODE_200043, "增加后督与用户关系数据失败");
		sysCodeMap.put(SYSCODE_200044, "修改后督与用户关系数据失败");
		sysCodeMap.put(SYSCODE_200045, "删除后督与用户关系数据失败");
		sysCodeMap.put(SYSCODE_200046, "增加模块定义失败");
		sysCodeMap.put(SYSCODE_200047, "修改模块定义失败");
		sysCodeMap.put(SYSCODE_200048, "删除模块定义失败");
		sysCodeMap.put(SYSCODE_200049, "增加系统来源失败");
		sysCodeMap.put(SYSCODE_200050, "修改系统来源失败");
		sysCodeMap.put(SYSCODE_200051, "删除系统来源失败");



		sysCodeMap.put(SYSCODE_240001, "任务ID已经存在");
		sysCodeMap.put(SYSCODE_240002, "任务已经被关联,不能删除");
		sysCodeMap.put(SYSCODE_240004, "TMS连接异常,请联系管理人员");

		sysCodeMap.put(SYSCODE_250001, "该类型下的文件名重复，请核对好重新添加文件");
		sysCodeMap.put(SYSCODE_250002, "文件大小超过最大限制,请重新上传。");

		sysCodeMap.put(SYSCODE_300002, "中心应用连接异常");
		sysCodeMap.put(SYSCODE_300003, "个性化信息获取失败");
		sysCodeMap.put(SYSCODE_300004, "数据库操作失败");
		sysCodeMap.put(SYSCODE_300005, "全局配置信息获取失败");
		sysCodeMap.put(SYSCODE_300007, "本地任务读取失败");
		sysCodeMap.put(SYSCODE_300009, "批次已提交");
		sysCodeMap.put(SYSCODE_300010, "批次登记失败");
		sysCodeMap.put(SYSCODE_300012, "批次信息登记失败");
		sysCodeMap.put(SYSCODE_300013, "补扫批次不存在");
		sysCodeMap.put(SYSCODE_300015, "待修改批次不存在");
		sysCodeMap.put(SYSCODE_300020, "批次删除失败");
		sysCodeMap.put(SYSCODE_300021, "扫描张数和登记张数不一致");
		sysCodeMap.put(SYSCODE_300022, "图像上传失败");
		sysCodeMap.put(SYSCODE_300024, "批次已经失效");
		sysCodeMap.put(SYSCODE_300025, "补扫批次提交失败");
		sysCodeMap.put(SYSCODE_300027, "批次日结失败");
		sysCodeMap.put(SYSCODE_300028, "图像删除失败");
		sysCodeMap.put(SYSCODE_300033, "个性化信息保存失败");
		sysCodeMap.put(SYSCODE_300034, "批次已由当前用户登记");
		sysCodeMap.put(SYSCODE_300035, "当前批次已由其它用户登记");
		sysCodeMap.put(SYSCODE_300036, "当前机构在机构数据表中没有注册");
		sysCodeMap.put(SYSCODE_300037, "批次表在表定义表中没有注册");
		sysCodeMap.put(SYSCODE_300039, "批次数据保存失败");
		sysCodeMap.put(SYSCODE_300041, "获取补扫批次失败，该任务已被他人获取");
		sysCodeMap.put(SYSCODE_300042, "批次修改失败");
		sysCodeMap.put(SYSCODE_300043, "批次数据删除失败");
		sysCodeMap.put(SYSCODE_300044, "补扫批次登记失败");
		sysCodeMap.put(SYSCODE_300045, "补扫任务队列为空!");
		sysCodeMap.put(SYSCODE_300046, "系统密钥为空");
		sysCodeMap.put(SYSCODE_300047, "中心应用服务信息为空");
		sysCodeMap.put(SYSCODE_300048, "影像平台服务信息为空");
		sysCodeMap.put(SYSCODE_300049, "快捷键获取失败");
		sysCodeMap.put(SYSCODE_300050, "影像平台更新属性发生异常");
		sysCodeMap.put(SYSCODE_300051, "影像平台更新属性失败");
		sysCodeMap.put(SYSCODE_300052, "影像平台连接失败失败");
		sysCodeMap.put(SYSCODE_300053, "快捷键保存失败");
		sysCodeMap.put(SYSCODE_300056, "批次已被登记");
		sysCodeMap.put(SYSCODE_300057, "获取机构服务注册信息失败");
		sysCodeMap.put(SYSCODE_300058, "获取传输平台密钥失败");
		sysCodeMap.put(SYSCODE_300059, "设置快捷键失败");
		sysCodeMap.put(SYSCODE_300060, "该批次已被登记为凭证类型");
		sysCodeMap.put(SYSCODE_300061, "同一操作员在不同PC机上登记已经登记过且未提交的批次");
		sysCodeMap.put(SYSCODE_300062, "提交批次时批次登记为无效");
		sysCodeMap.put(SYSCODE_300063, "提交批次时批次登记信息不存在");
		sysCodeMap.put(SYSCODE_300064, "业务类型为凭证的相同批次已提交过");
		sysCodeMap.put(SYSCODE_300065, "当前流水的同一批次已提交过");
		sysCodeMap.put(SYSCODE_300066, "该批次已锁定，不能修改");
		sysCodeMap.put(SYSCODE_300067, "该柜员当天业务已经轧账，无法登记!");
		sysCodeMap.put(SYSCODE_300068, "该柜员当天业务已经归档，无法登记!");
		sysCodeMap.put(SYSCODE_300069, "未有未提交的批次需要初始化!");
		sysCodeMap.put(SYSCODE_300070, "已有包含无纸化的批次登记!");
		sysCodeMap.put(SYSCODE_300071, "已提交过相同要素批次，当前用户有未提交批次!");
		sysCodeMap.put(SYSCODE_300072, "已提交过相同要素批次，其他用户有未提交批次");
		sysCodeMap.put(SYSCODE_300073, "已提交过相同要素批次");
		sysCodeMap.put(SYSCODE_300074, "当前用户有未提交批次!");
		sysCodeMap.put(SYSCODE_300075, "其他用户有未提交批次");
		sysCodeMap.put(SYSCODE_300076, "已有包含无纸化的批次已提交!");
		
		sysCodeMap.put(SYSCODE_500001, "数据库操作异常");
		sysCodeMap.put(SYSCODE_500028, "设置快捷键成功");
		sysCodeMap.put(SYSCODE_500029, "设置快捷键失败");
		sysCodeMap.put(SYSCODE_500037, "修改后的批次已经存在");
		sysCodeMap.put(SYSCODE_500040, "批次尚未处理完成");
		sysCodeMap.put(SYSCODE_500041, "本批次还有流水未勾对？");
		sysCodeMap.put(SYSCODE_500043, "勾兑成功");
		sysCodeMap.put(SYSCODE_500046, "释放流水失败");
		sysCodeMap.put(SYSCODE_500048, "修改批注数据库操作异常");
		sysCodeMap.put(SYSCODE_500049, "标记图像为业务审核失败");
		sysCodeMap.put(SYSCODE_500050, "数据异常，该凭证信息索引丢失");
		sysCodeMap.put(SYSCODE_500051, "主件设置成功");
		sysCodeMap.put(SYSCODE_500052, "设为附件成功");
		sysCodeMap.put(SYSCODE_500053, "设为附件设置失败");
		sysCodeMap.put(SYSCODE_500054, "该凭证不符合条件（主件或未勾兑），不能调整为附件");
		sysCodeMap.put(SYSCODE_500056, "设置失败");
		sysCodeMap.put(SYSCODE_500057, "该凭证为主件，请先调整为附件再指定主件");
		sysCodeMap.put(SYSCODE_500058, "图像标记删除成功");
		sysCodeMap.put(SYSCODE_500059, "标记删除失败");
		sysCodeMap.put(SYSCODE_500060, "图像撤销标记删除成功");
		sysCodeMap.put(SYSCODE_500061, "撤销删除失败");
		sysCodeMap.put(SYSCODE_500063, "图像标记核实补扫失败");
		sysCodeMap.put(SYSCODE_500062, "标记核实补扫成功");
		sysCodeMap.put(SYSCODE_500064, "下发差错成功");
		sysCodeMap.put(SYSCODE_500065, "修改版面名称失败");
		sysCodeMap.put(SYSCODE_500066, "找不到勾对流水");
		sysCodeMap.put(SYSCODE_500067, "根据勾对要素查询出多条流水，检查流水数据或者勾对配置");
		sysCodeMap.put(SYSCODE_500068, "下发流水差错成功");
		sysCodeMap.put(SYSCODE_700000, "");
		sysCodeMap.put(SYSCODE_700001, "数据库操作异常");
		sysCodeMap.put(SYSCODE_800001, "同步实物档案异常，该分行/总行未定义档案代码");
		sysCodeMap.put(SYSCODE_999999, "发生系统错误，请检查!");
		sysCodeMap.put(SYSCODE_140023, "修改参数表数据出错!");
		sysCodeMap.put(SYSCODE_140024, "表已经创建无法修改!");
		sysCodeMap.put(SYSCODE_140025, "表字段有重复，请检查!");
		sysCodeMap.put(SYSCODE_140026, "展现字段重复，无法创建!");
		sysCodeMap.put(SYSCODE_140027, "字段被已创建表关联，无法修改");
	}
}
