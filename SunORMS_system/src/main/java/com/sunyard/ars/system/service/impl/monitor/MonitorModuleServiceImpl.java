package com.sunyard.ars.system.service.impl.monitor;

import java.lang.reflect.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sunyard.aos.common.util.ExcelUtil;
import com.sunyard.aos.common.util.HttpUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.ImportUtil;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.dao.monitor.MonitorModuleDao;
import com.sunyard.ars.system.service.monitor.IMonitorModuleService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * 监控自定义接口实现类
 *
 * @author: lx
 * @date: 2019年1月18日 上午10:49:50
 */
@Service("monitorModuleService")
@Transactional
public class MonitorModuleServiceImpl extends BaseService implements
		IMonitorModuleService {

	// 数据库接口
	@Resource
	private MonitorModuleDao monitorModuleDao;
	protected Connection conn;
	protected PreparedStatement ps;
	protected ResultSet rs;

	/**
	 * 执行接口逻辑
	 *
	 * @author: ...
	 * @date: 2017年2月28日 上午10:53:30
	 */
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	/**
	 * 执行具体操作：增、删、改、查 等，操作成功后记录日志信息
	 *
	 * @author: ...
	 * @date: 2017年2月28日 下午4:34:57
	 */
	@Override
	@SuppressWarnings({ "rawtypes" })
	protected void doAction(RequestBean requestBean, ResponseBean responseBean)
			throws Exception {
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		// 获取存储过程标识
		String ser_flag = (String) sysMap.get("ser_flag");
		if (!BaseUtil.isBlank(ser_flag)) {
			procedureOperation(requestBean, responseBean);
		} else {
			// 获取操作标识
			String oper_type = (String) sysMap.get("oper_type");
			if (AOSConstants.OPERATE_QUERY.equals(oper_type)) {
				// 查询
				queryOperation(requestBean, responseBean);
			} else if (AOSConstants.OPERATE_OTHER.equals(oper_type)) {
				querySecondOperation(requestBean, responseBean);
			}else if ("getValueBySearchBarSql".equals(oper_type)) {
				getValueBySearchBarSql(requestBean, responseBean);
			}else if("reportExport".equals(oper_type)) {
				reportExport(requestBean, responseBean);
			}

		}

	}

	/**
	 * 调用存储过程
	 *
	 * lx
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void procedureOperation(RequestBean requestBean,
									ResponseBean responseBean) throws Exception {
		// 获取前台数据
		Map sysMap = requestBean.getSysMap();
		// 获取操作类型
		String oper_type = (String) sysMap.get("oper_type");
		// 分页标识1
		String pageFlag1 = "0";
		// 分页标识2
		String pageFlag2 = "0";
		// 分页标识3
		String pageFlag3 = "0";
		// 分页标识4
		String pageFlag4 = "0";
		// 当前页1
		int currentPage1 = 0;
		// 当前页2
		int currentPage2 = 0;
		// 当前页3
		int currentPage3 = 0;
		// 当前页4
		int currentPage4 = 0;
		// 页记录数1
		int pageSize1 = 0;
		// 页记录数2
		int pageSize2 = 0;
		// 页记录数3
		int pageSize3 = 0;
		// 页记录数4
		int pageSize4 = 0;
		if (!AOSConstants.OPERATE_OTHER.equals(oper_type)) {
			// 分页标识1
			pageFlag1 = (String) sysMap.get("pageFlag1");
			// 分页标识2
			pageFlag2 = (String) sysMap.get("pageFlag2");
			// 分页标识3
			pageFlag3 = (String) sysMap.get("pageFlag3");
			// 分页标识4
			pageFlag4 = (String) sysMap.get("pageFlag4");
			currentPage1 = (int) sysMap.get("currentPage1");
			// 当前页2
			currentPage2 = (int) sysMap.get("currentPage2");
			// 当前页3
			currentPage3 = (int) sysMap.get("currentPage3");
			// 当前页4
			currentPage4 = (int) sysMap.get("currentPage4");
			// 页记录数1
			if (currentPage1 != -1) {
				pageSize1 = (int) sysMap.get("pageSize1");
				pageSize1 = getPageSize(pageSize1);
			}
			// 页记录数2
			if (currentPage2 != -1) {
				pageSize2 = (int) sysMap.get("pageSize2");
				pageSize2 = getPageSize(pageSize2);
			}
			// 页记录数3
			if (currentPage3 != -1) {
				pageSize3 = (int) sysMap.get("pageSize3");
				pageSize3 = getPageSize(pageSize3);
			}
			// 页记录数4
			if (currentPage4 != -1) {
				pageSize4 = (int) sysMap.get("pageSize4");
				pageSize4 = getPageSize(pageSize4);
			}
		}

		// 获取存储过程名称
		String procedureName = (String) sysMap.get("ser_flag");

		// 获取一级展示栏入参
		String firstShowParam = (String) sysMap.get("firstShowParam");
		// 获取入参配置
		String param_in = (String) sysMap.get("param_in");
		// 获取预警标识
		String warn_flag = (String) sysMap.get("warnFlag");
		// 获取模板类型
		String module_type = (String) sysMap.get("module_type");
		// 获取查询栏参数值
		List vaList = (List) sysMap.get("vaList");
		// 获取查询栏对应的入参名
		List cdList = (List) sysMap.get("cdList");
		// 后台返回map
		HashMap retMap = new HashMap();
		if (AOSConstants.OPERATE_OTHER.equals(oper_type)) {
			// 获取出参配置
			String param_out = "#{o_ret,mode=OUT,jdbcType=VARCHAR},#{o_msg,mode=OUT,jdbcType=VARCHAR},"
					+ "#{o_sql,mode=OUT,jdbcType=VARCHAR}";
			String sql = "";
			if (BaseUtil.isBlank(param_in)) {
				sql = procedureName + "(#{i_param,mode=IN,jdbcType=VARCHAR},"
						+ param_out + ")";
			} else {
				sql = procedureName + "(#{i_param,mode=IN,jdbcType=VARCHAR},"
						+ param_in + "," + param_out + ")";
			}

			HashMap condMap = new HashMap();
			// 设置入参
			for (int i = 0; i < vaList.size(); i++) {
				condMap.put(cdList.get(i), vaList.get(i));
				// System.out.println("key:" + cdList.get(i) + " value:"
				// + vaList.get(i));
			}
			condMap.put("i_param", firstShowParam);

			// 设置出参
			condMap.put("o_ret", "");
			condMap.put("o_msg", "");
			condMap.put("o_sql", "");
			condMap.put("sql", sql);
			// System.out.println("sql:" + sql);
			monitorModuleDao.callProcedure(condMap);
			condMap.put("select_sql", condMap.get("o_sql"));
			condMap = addExtraCondition(condMap);
			List list = getList(monitorModuleDao.select(condMap), null);
			retMap.put("list", list);
		} else if (AOSConstants.OPERATE_QUERY.equals(oper_type)) {
			// 获取出参配置
			String param_out = monitorModule_getParamOutSql(module_type,
					warn_flag);
			String sql = procedureName + "(" + param_in + "," + param_out + ")";
			HashMap condMap = new HashMap();
			// 设置入参
			for (int i = 0; i < vaList.size(); i++) {
				condMap.put(cdList.get(i), vaList.get(i));
				// System.out.println("key:" + cdList.get(i) + " value:"
				// + vaList.get(i));
			}
			// 设置出参
			condMap.put("o_ret", "");
			condMap.put("o_msg", "");
			condMap.put("o_sql", "");
			condMap.put("o_sql1", "");
			condMap.put("o_sql2", "");
			condMap.put("o_sql3", "");
			condMap.put("o_sql4", "");
			condMap.put("o_warnSql", "");
			condMap.put("sql", sql);
			// System.out.println("sql:" + sql);
			// System.out.println("前i_organ_no：" + condMap.get("i_organ_no"));
			// System.out.println(("前i_data_type:" +
			// condMap.get("i_data_type")));
			monitorModuleDao.callProcedure(condMap);
			condMap = addExtraCondition(condMap);
			// System.out.println("i_organ_no:" + condMap.get("i_organ_no"));
			// System.out.println("i_data_type:" + condMap.get("i_data_type"));
			// System.out.println("o_msg:" + condMap.get("o_msg"));
			// 集合群
			List list1 = new ArrayList();
			List list2 = new ArrayList();
			List list3 = new ArrayList();
			List list4 = new ArrayList();
			// 总记录数
			long totalNum1 = 0;
			long totalNum2 = 0;
			long totalNum3 = 0;
			long totalNum4 = 0;
			if (!BaseUtil.isBlank((String) condMap.get("o_sql"))) {
				condMap.put("select_sql", condMap.get("o_sql"));
				if ("1".equals(pageFlag1)) {
					// 定义分页操作，pageSize = 0 时查询全部数据（不分页），currentPage <= 0
					// 时查询第一页，currentPage > pages（超过总数时），查询最后一页
					Page page = PageHelper.startPage(currentPage1, pageSize1);
					// 查询分页记录
					list1 = getList(monitorModuleDao.select(condMap), page);
					totalNum1 = page.getTotal();
				} else {
					list1 = getList(monitorModuleDao.select(condMap), null);
				}
			}
			if (!BaseUtil.isBlank((String) condMap.get("o_sql1"))) {
				condMap.put("select_sql1", condMap.get("o_sql1"));
				if ("1".equals(pageFlag1)) {
					// 定义分页操作，pageSize = 0 时查询全部数据（不分页），currentPage <= 0
					// 时查询第一页，currentPage > pages（超过总数时），查询最后一页
					Page page = PageHelper.startPage(currentPage1, pageSize1);
					// 查询分页记录
					list1 = getList(monitorModuleDao.select1(condMap), page);
					totalNum1 = page.getTotal();
				} else {
					list1 = getList(monitorModuleDao.select1(condMap), null);
				}
			}
			if (!BaseUtil.isBlank((String) condMap.get("o_sql2"))) {
				condMap.put("select_sql2", condMap.get("o_sql2"));
				if ("1".equals(pageFlag2)) {
					// 定义分页操作，pageSize = 0 时查询全部数据（不分页），currentPage <= 0
					// 时查询第一页，currentPage > pages（超过总数时），查询最后一页
					Page page = PageHelper.startPage(currentPage2, pageSize2);
					// 查询分页记录
					list2 = getList(monitorModuleDao.select2(condMap), page);
					totalNum2 = page.getTotal();
				} else {
					list2 = getList(monitorModuleDao.select2(condMap), null);
				}
			}
			if (!BaseUtil.isBlank((String) condMap.get("o_sql3"))) {
				condMap.put("select_sql3", condMap.get("o_sql3"));
				if ("1".equals(pageFlag3)) {
					// 定义分页操作，pageSize = 0 时查询全部数据（不分页），currentPage <= 0
					// 时查询第一页，currentPage > pages（超过总数时），查询最后一页
					Page page = PageHelper.startPage(currentPage3, pageSize3);
					// 查询分页记录
					list3 = getList(monitorModuleDao.select3(condMap), page);
					totalNum3 = page.getTotal();
				} else {
					list3 = getList(monitorModuleDao.select3(condMap), null);
				}
			}
			if (!BaseUtil.isBlank((String) condMap.get("o_sql4"))) {
				condMap.put("select_sql4", condMap.get("o_sql4"));
				if ("1".equals(pageFlag4)) {
					// 定义分页操作，pageSize = 0 时查询全部数据（不分页），currentPage <= 0
					// 时查询第一页，currentPage > pages（超过总数时），查询最后一页
					Page page = PageHelper.startPage(currentPage4, pageSize4);
					// 查询分页记录
					list4 = getList(monitorModuleDao.select4(condMap), page);
					totalNum4 = page.getTotal();
				} else {
					list4 = getList(monitorModuleDao.select4(condMap), null);
				}
			}
			// 判断是否需要预警
			String warnSql = (String) condMap.get("o_warnSql");
			// System.out.println("warnSql：" + warnSql);
			condMap.put("warn_sql", warnSql);
			String warnFlag = "0";
			if (!BaseUtil.isBlank(warnSql)) {
				List warnList = getList(monitorModuleDao.selectWarn(condMap),
						null);
				if (warnList.size() > 0) { // 预警推送
					warnFlag = "1";
				} else { // 不预警
					warnFlag = "0";
				}
			}

			retMap.put("list1", list1);
			retMap.put("list2", list2);
			retMap.put("list3", list3);
			retMap.put("list4", list4);
			retMap.put("totalNum1", totalNum1);
			retMap.put("totalNum2", totalNum2);
			retMap.put("totalNum3", totalNum3);
			retMap.put("totalNum4", totalNum4);
			retMap.put("warnFlag", warnFlag);
		}

		responseBean.setRetMap(retMap);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("执行成功");
	}

	/**
	 * 总记录数赋值函数
	 *
	 * lx
	 */
	public int getPageSize(int pageSize) {
		if (pageSize <= 0) {
			pageSize = AOSConstants.PAGE_NUM;
		}
		return pageSize;
	}

	/**
	 * 拼接出参配置sql
	 *
	 * lx
	 *
	 * @param monitor_type
	 * @return
	 */
	public String monitorModule_getParamOutSql(String monitor_type,
											   String warnFlag) {
		// 预警标识
		String warnSql = "";
		if ("1".equals(warnFlag)) {
			warnSql = "#{o_warnSql,jdbcType=VARCHAR,mode=OUT},";
		}
		// 返回值
		String sql = "#{o_ret,mode=OUT,jdbcType=VARCHAR},#{o_msg,mode=OUT,jdbcType=VARCHAR},"
				+ warnSql;
		if ("single".equals(monitor_type) || "".equals(monitor_type)) {
			sql += "#{o_sql,mode=OUT,jdbcType=VARCHAR}";
		} else if ("multi1".equals(monitor_type)
				|| "multi2".equals(monitor_type)) {
			sql += "#{o_sql1,mode=OUT,jdbcType=VARCHAR},"
					+ "#{o_sql2,mode=OUT,jdbcType=VARCHAR}";
		} else if ("multi3".equals(monitor_type)) {
			sql += "#{o_sql1,mode=OUT,jdbcType=VARCHAR},"
					+ "#{o_sql2,mode=OUT,jdbcType=VARCHAR},"
					+ "#{o_sql3,mode=OUT,jdbcType=VARCHAR}";
		} else {
			sql += "#{o_sql1,mode=OUT,jdbcType=VARCHAR},"
					+ "#{o_sql2,mode=OUT,jdbcType=VARCHAR},"
					+ "#{o_sql3,mode=OUT,jdbcType=VARCHAR},"
					+ "#{o_sql4,mode=OUT,jdbcType=VARCHAR}";
		}
		return sql;
	}

	/**
	 * 获取存储过程参数集合
	 */
	public HashMap getProcedureMap(Map sysMap) {
		HashMap condMap = new HashMap();
		// 模板类型
		String module_type = (String) sysMap.get("module_type");
		// 数据数组
		List vaList = (List) sysMap.get("vaList");
		// 数据类型数组
		List dtList = (List) sysMap.get("dtList");
		// 表字段数据数组
		List cdList = (List) sysMap.get("cdList");
		for (int i = 0; i < vaList.size(); i++) {
			String key_name = (String) cdList.get(i);
			String value = (String) vaList.get(i);
			String mode = "";
			String type = (String) dtList.get(i);
			if ("out".equals(type)) {
				mode = "out";
			} else {
				mode = "in";
			}
			condMap.put("data", value);
			condMap.put("mode", mode);
			condMap.put("type", type);

		}
		return condMap;
	}

	/**
	 * 查询二级展示栏数据 lx
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void querySecondOperation(RequestBean requestBean,
									  ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		// 前台数据数组
		List vaList = (List) sysMap.get("vaList");
		// 表字段数据数组
		List cdList = (List) sysMap.get("cdList");
		// 前台配置sql1
		String sql = (String) sysMap.get("sql");
		// 获取一级展示栏入参
		String firstShowParam = (String) sysMap.get("firstShowParam");
		// 构造条件参数
		HashMap condMap = new HashMap();
		// 设置入参
		for (int i = 0; i < vaList.size(); i++) {
			condMap.put(cdList.get(i), vaList.get(i));
			System.out.println("key:" + cdList.get(i) + " value:"
					+ vaList.get(i));
		}
		condMap.put("i_param", firstShowParam);
		condMap = addExtraCondition(condMap);
		List list = new ArrayList();
		// 总记录数
		long totalNum1 = 0;
		if(!BaseUtil.isBlank(sql)) {
			condMap.put("select_sql", sql);
			list = getList(monitorModuleDao.select(condMap), null);
		}
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("list", list);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * 查询 操作
	 *
	 * @author: lx
	 * @date: 2018年12月27日 10:01:47
	 *
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryOperation(RequestBean requestBean,
								ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		// 获取操作类型
		String oper_type = (String) sysMap.get("oper_type");
		// 分页标识1
		String pageFlag1 = "0";
		// 分页标识2
		String pageFlag2 = "0";
		// 分页标识3
		String pageFlag3 = "0";
		// 分页标识4
		String pageFlag4 = "0";
		// 当前页1
		int currentPage1 = 0;
		// 当前页2
		int currentPage2 = 0;
		// 当前页3
		int currentPage3 = 0;
		// 当前页4
		int currentPage4 = 0;
		// 页记录数1
		int pageSize1 = 0;
		// 页记录数2
		int pageSize2 = 0;
		// 页记录数3
		int pageSize3 = 0;
		// 页记录数4
		int pageSize4 = 0;
		if (!AOSConstants.OPERATE_OTHER.equals(oper_type)) {
			// 分页标识1
			pageFlag1 = (String) sysMap.get("pageFlag1");
			// 分页标识2
			pageFlag2 = (String) sysMap.get("pageFlag2");
			// 分页标识3
			pageFlag3 = (String) sysMap.get("pageFlag3");
			// 分页标识4
			pageFlag4 = (String) sysMap.get("pageFlag4");
			currentPage1 = (int) sysMap.get("currentPage1");
			// 当前页2
			currentPage2 = (int) sysMap.get("currentPage2");
			// 当前页3
			currentPage3 = (int) sysMap.get("currentPage3");
			// 当前页4
			currentPage4 = (int) sysMap.get("currentPage4");
			// 页记录数1
			if (currentPage1 != -1) {
				pageSize1 = (int) sysMap.get("pageSize1");
				pageSize1 = getPageSize(pageSize1);
			}
			// 页记录数2
			if (currentPage2 != -1) {
				pageSize2 = (int) sysMap.get("pageSize2");
				pageSize2 = getPageSize(pageSize2);
			}
			// 页记录数3
			if (currentPage3 != -1) {
				pageSize3 = (int) sysMap.get("pageSize3");
				pageSize3 = getPageSize(pageSize3);
			}
			// 页记录数4
			if (currentPage4 != -1) {
				pageSize4 = (int) sysMap.get("pageSize4");
				pageSize4 = getPageSize(pageSize4);
			}
		}
		// 前台数据数组
		List vaList = (List) sysMap.get("vaList");
		// 表字段数据数组
		List cdList = (List) sysMap.get("cdList");
		// 前台配置sql1
		String sql1 = (String) sysMap.get("sql1");
		// 前台配置sql2
		String sql2 = (String) sysMap.get("sql2");
		// 前台配置sql3
		String sql3 = (String) sysMap.get("sql3");
		// 前台配置sql4
		String sql4 = (String) sysMap.get("sql4");
		// 构造条件参数
		HashMap condMap = new HashMap();
		// 设置入参
		for (int i = 0; i < vaList.size(); i++) {
			condMap.put(cdList.get(i), vaList.get(i));
			System.out.println("key:" + cdList.get(i) + " value:"
					+ vaList.get(i));
		}
		// 集合群
		List list1 = new ArrayList();
		List list2 = new ArrayList();
		List list3 = new ArrayList();
		List list4 = new ArrayList();
		// 总记录数
		long totalNum1 = 0;
		long totalNum2 = 0;
		long totalNum3 = 0;
		long totalNum4 = 0;

		condMap = addExtraCondition(condMap);
		// 区域1
		if (!BaseUtil.isBlank(sql1)) {
			condMap.put("select_sql1", sql1);
			if ("1".equals(pageFlag1)) {
				Page page = PageHelper.startPage(currentPage1, pageSize1);
				// 查询分页记录
				list1 = getList(monitorModuleDao.select1(condMap), page);
				totalNum1 = page.getTotal();
			} else {
				list1 = getList(monitorModuleDao.select1(condMap), null);
			}
		}
		// 区域2
		if (!BaseUtil.isBlank(sql2)) {
			condMap.put("select_sql2", sql2);
			if ("1".equals(pageFlag2)) {
				Page page = PageHelper.startPage(currentPage2, pageSize2);
				// 查询分页记录
				list2 = getList(monitorModuleDao.select2(condMap), page);
				totalNum2 = page.getTotal();
			} else {
				list2 = getList(monitorModuleDao.select2(condMap), null);
			}
		}
		// 区域3
		if (!BaseUtil.isBlank(sql3)) {
			condMap.put("select_sql3", sql3);
			if ("1".equals(pageFlag3)) {
				Page page = PageHelper.startPage(currentPage3, pageSize3);
				// 查询分页记录
				list3 = getList(monitorModuleDao.select3(condMap), page);
				totalNum3 = page.getTotal();
			} else {
				list3 = getList(monitorModuleDao.select3(condMap), null);
			}
		}
		// 区域4
		if (!BaseUtil.isBlank(sql4)) {
			condMap.put("select_sql4", sql4);
			if ("1".equals(pageFlag4)) {
				Page page = PageHelper.startPage(currentPage4, pageSize4);
				// 查询分页
				list4 = getList(monitorModuleDao.select4(condMap), page);
				totalNum4 = page.getTotal();
			} else {
				list4 = getList(monitorModuleDao.select4(condMap), null);
			}
		}
		// 判断是否需要预警
		String warnSql = (String) sysMap.get("warnSql");
		// System.out.println("warnSql：" + warnSql);
		condMap.put("warn_sql", warnSql);
		String warnFlag = "0";
		if (!BaseUtil.isBlank(warnSql)) {
			List warnList = getList(monitorModuleDao.selectWarn(condMap), null);
			if (warnList.size() > 0) { // 预警推送
				warnFlag = "1";
			} else { // 不预警
				warnFlag = "0";
			}
		}

		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("list1", list1);
		retMap.put("list2", list2);
		retMap.put("list3", list3);
		retMap.put("list4", list4);
		retMap.put("totalNum1", totalNum1);
		retMap.put("totalNum2", totalNum2);
		retMap.put("totalNum3", totalNum3);
		retMap.put("totalNum4", totalNum4);
		retMap.put("warnFlag", warnFlag);

		responseBean.setRetMap(retMap);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");

	}


	/**
	 * 查询栏sql
	 * @param requestBean 请求bean
	 * @param responseBean 响应bean
	 * @throws Exception 抛出异常
	 */
	private void getValueBySearchBarSql(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		String sql = requestBean.getSysMap().get("sql").toString();
		HashMap map = new HashMap();
		map.put("sql", sql);
		map=addExtraCondition(map);
		List<HashMap<String, Object>> datas = monitorModuleDao.getValueBySearchBarSql(map);
		Map<Object, Object> list = new HashMap<>();
		list.put("list", datas);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMap(list);
	}
	/**
	 * 导出报表
	 * @param requestBean 请求bean
	 * @param responseBean 响应bean
	 * @throws Exception 抛出异常
	 */
	private void reportExport(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		// 获取操作类型
		String oper_type = (String) sysMap.get("oper_type");
        String title=(String) sysMap.get("title");
        String fileName = System.currentTimeMillis() + "report.xls";// 设置要导出的报表的后台文件名

        ArrayList columns=(ArrayList)sysMap.get("columns");
		// 分页标识1
		String pageFlag1 = "0";
		// 分页标识2
		String pageFlag2 = "0";
		// 分页标识3
		String pageFlag3 = "0";
		// 分页标识4
		String pageFlag4 = "0";
		// 当前页1
		int currentPage1 = -1;//
		// 当前页2
		int currentPage2 = -1;
		// 当前页3
		int currentPage3 = -1;
		// 当前页4
		int currentPage4 = -1;
		// 页记录数1
		int pageSize1 = 0;
		// 页记录数2
		int pageSize2 = 0;
		// 页记录数3
		int pageSize3 = 0;
		// 页记录数4
		int pageSize4 = 0;
		if (!AOSConstants.OPERATE_OTHER.equals(oper_type)) {
			// 分页标识1
			pageFlag1 = (String) sysMap.get("pageFlag1");
			// 分页标识2
			pageFlag2 = (String) sysMap.get("pageFlag2");
			// 分页标识3
			pageFlag3 = (String) sysMap.get("pageFlag3");
			// 分页标识4
			pageFlag4 = (String) sysMap.get("pageFlag4");
			//currentPage1 = (int) sysMap.get("currentPage1");
			// 当前页2
			//currentPage2 = (int) sysMap.get("currentPage2");
			// 当前页3
			//currentPage3 = (int) sysMap.get("currentPage3");
			// 当前页4
			//currentPage4 = (int) sysMap.get("currentPage4");
			// 页记录数1
			if (currentPage1 != -1) {
				pageSize1 = (int) sysMap.get("pageSize1");
				pageSize1 = getPageSize(pageSize1);
			}
			// 页记录数2
			if (currentPage2 != -1) {
				pageSize2 = (int) sysMap.get("pageSize2");
				pageSize2 = getPageSize(pageSize2);
			}
			// 页记录数3
			if (currentPage3 != -1) {
				pageSize3 = (int) sysMap.get("pageSize3");
				pageSize3 = getPageSize(pageSize3);
			}
			// 页记录数4
			if (currentPage4 != -1) {
				pageSize4 = (int) sysMap.get("pageSize4");
				pageSize4 = getPageSize(pageSize4);
			}
		}
		// 前台数据数组
		List vaList = (List) sysMap.get("vaList");
		// 表字段数据数组
		List cdList = (List) sysMap.get("cdList");
		// 前台配置sql1
		String sql1 = (String) sysMap.get("sql1");
		// 前台配置sql2
		String sql2 = (String) sysMap.get("sql2");
		// 前台配置sql3
		String sql3 = (String) sysMap.get("sql3");
		// 前台配置sql4
		String sql4 = (String) sysMap.get("sql4");
		// 构造条件参数
		HashMap condMap = new HashMap();
		// 设置入参
		for (int i = 0; i < vaList.size(); i++) {
			condMap.put(cdList.get(i), vaList.get(i));
			System.out.println("key:" + cdList.get(i) + " value:"
					+ vaList.get(i));
		}
		// 集合群
		List list1 = new ArrayList();
		List list2 = new ArrayList();
		List list3 = new ArrayList();
		List list4 = new ArrayList();
		// 总记录数
		long totalNum1 = 0;
		long totalNum2 = 0;
		long totalNum3 = 0;
		long totalNum4 = 0;

		condMap = addExtraCondition(condMap);
		// 区域1
		if (!BaseUtil.isBlank(sql1)) {
			condMap.put("select_sql1", sql1);
			if ("1".equals(pageFlag1)) {
				Page page = PageHelper.startPage(currentPage1, pageSize1);
				// 查询分页记录
				list1 = getList(monitorModuleDao.select1(condMap), page);
				totalNum1 = page.getTotal();
			} else {
				list1 = getList(monitorModuleDao.select1(condMap), null);
			}
		}
		// 区域2
		if (!BaseUtil.isBlank(sql2)) {
			condMap.put("select_sql2", sql2);
			if ("1".equals(pageFlag2)) {
				Page page = PageHelper.startPage(currentPage2, pageSize2);
				// 查询分页记录
				list2 = getList(monitorModuleDao.select2(condMap), page);
				totalNum2 = page.getTotal();
			} else {
				list2 = getList(monitorModuleDao.select2(condMap), null);
			}
		}
		// 区域3
		if (!BaseUtil.isBlank(sql3)) {
			condMap.put("select_sql3", sql3);
			if ("1".equals(pageFlag3)) {
				Page page = PageHelper.startPage(currentPage3, pageSize3);
				// 查询分页记录
				list3 = getList(monitorModuleDao.select3(condMap), page);
				totalNum3 = page.getTotal();
			} else {
				list3 = getList(monitorModuleDao.select3(condMap), null);
			}
		}
		// 区域4
		if (!BaseUtil.isBlank(sql4)) {
			condMap.put("select_sql4", sql4);
			if ("1".equals(pageFlag4)) {
				Page page = PageHelper.startPage(currentPage4, pageSize4);
				// 查询分页
				list4 = getList(monitorModuleDao.select4(condMap), page);
				totalNum4 = page.getTotal();
			} else {
				list4 = getList(monitorModuleDao.select4(condMap), null);
			}
		}
		// 判断是否需要预警
		String warnSql = (String) sysMap.get("warnSql");
		// System.out.println("warnSql：" + warnSql);
		condMap.put("warn_sql", warnSql);
		String warnFlag = "0";
		if (!BaseUtil.isBlank(warnSql)) {
			List warnList = getList(monitorModuleDao.selectWarn(condMap), null);
			if (warnList.size() > 0) { // 预警推送
				warnFlag = "1";
			} else { // 不预警
				warnFlag = "0";
			}
		}
		LinkedHashMap<String, String> headMap = new LinkedHashMap<String, String>();
        for( int i=0;i<columns.size();i++){
            Map map = (Map)columns.get(i);

            headMap.putAll((Map)columns.get(i));
        }
		// 拼装返回信息
        List<Map> data = new ArrayList<Map>();
		Map map=new HashMap();
		for (int i = 0; i < list1.size(); i++) {
			map = (Map) list1.get(i);
			data.add(map);
		}
		Map<Object,Object> retMap = new HashMap<Object, Object>();
        String path=HttpUtil.getAbsolutePath("temp");
		boolean retFlag = ExcelUtil.createExcelFile(HttpUtil.getAbsolutePath("temp"), fileName, title, headMap, data);
		if(retFlag) {
			retMap.put("filePath", HttpUtil.getAbsolutePath("temp")+"/"+fileName);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("生成成功");
			//添加日志
			String log = "报表:"+title+"导出数据";
			addOperLogInfo(ARSConstants.MODEL_NAME_CASE_LIBRARY, ARSConstants.OPER_TYPE_4_2, log);
		}else {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("xls文件生成失败！");
		}

	}
}
