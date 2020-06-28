package com.sunyard.ars.common.comm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.Page;
import com.sunyard.aos.common.dao.BaseDao;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.util.DateUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.pojo.User;

/**
 * @author:		 lewe
 * @date:		 2017年2月28日 下午4:01:22
 * @description: TODO(服务接口基类)
 */
public abstract class BaseService {
	
	// 日志记录器
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	// 数据库接口
	@Resource
	private BaseDao baseDao;


	/**
	 * @author:		 lewe
	 * @date:		 2017年2月28日 下午4:10:06
	 * @description: TODO(执行接口逻辑)
	 */
	protected ResponseBean executeAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前置操作
		beforeAction(requestBean, responseBean);
		// 执行操作
		doAction(requestBean, responseBean);
		// 后置操作
		afterAction(requestBean, responseBean);
		// 返回信息
		setReturnInfo(responseBean);
		return responseBean;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月28日 下午4:10:06
	 * @description: TODO(接口前置操作)
	 */
	protected void beforeAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		
		// 后续扩展其他操作
		
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月28日 下午4:10:10
	 * @description: TODO(接口逻辑操作，必须被子类实现重写)
	 */
	protected abstract void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception;
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月28日 下午4:10:10
	 * @description: TODO(接口后置操作)
	 */
	protected void afterAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {

		// 后续扩展其他操作
		
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月28日 下午7:07:21
	 * @description: TODO(构造返回信息)
	 */
	protected void setReturnInfo(ResponseBean responseBean) {
		if (BaseUtil.isBlank(responseBean.getRetCode())) {
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("执行成功");
		}
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月28日 下午7:27:21
	 * @description: TODO(记录操作日志，包括 机构、柜员、时间、日志内容)
	 * 
	 * @throws Exception
	 */
	@Deprecated
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addLogInfo(String logContent) {
		try {
			User user = BaseUtil.getLoginUser();
			HashMap map = new HashMap();
			map.put("organ_no", user.getOrganNo());
			map.put("user_no", user.getUserNo());
			map.put("content", logContent);
			map.put("log_date", BaseUtil.getCurrentTimeStr());
			map = addExtraCondition(map);
			baseDao.insertLogInfo(map);
			logger.debug("往sm_userlog_tb表写入操作日志：" + logContent);
		} catch (Exception e) {
			logger.error("往sm_userlog_tb表写入操作日志出错！", e);
		}
	}
	
	/**
	 * @author:		 
	 * @date:		 
	 * @description: TODO(记录操作日志，包括 机构、柜员、时间、日志内容)
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addOperLogInfo(String module,String operType,String logContent) {
		try {
			User user = BaseUtil.getLoginUser();
			HashMap map = new HashMap();
			map.put("module", BaseUtil.filterSqlParam(module));
			map.put("operType", BaseUtil.filterSqlParam(operType));
			map.put("organ_no", user.getOrganNo());
			map.put("user_no", user.getUserNo());
			map.put("content", BaseUtil.filterSqlParam(logContent));
			map.put("log_date", DateUtil.getNow());
			map.put("log_time", DateUtil.getHMS2());
			baseDao.insertOperLogInfo(map);
//			logger.debug("往sm_operlog_tb表写入操作日志：" + logContent);
		} catch (Exception e) {
			logger.error("往sm_operlog_tb表写入操作日志出错！", e);
		}
	}
	
	/**
	 * 转换数据查询列表，添加行号 等
	 * 
	 * @author:	lewe
	 * @date:	2017年12月8日 下午2:21:24
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List getList(List list, Page page) throws SQLException{
		if (list == null || list.size() == 0) {
			return new ArrayList();
		}
		// 起始行号
		int startRow = 0;
		if (page != null) {
			startRow = page.getStartRow();
		}
		for (int i = 0; i < list.size(); i++) {
			Map map = BaseUtil.convertMapKeyValue((Map) list.get(i));
			map.put("rn", ++startRow);
			list.set(i, map);
		}
		return list;
	}
	
	/**
	 * 获取列表字符串数据，多个以逗号分隔
	 * 
	 * @author:	lewe
	 * @date:	2017年12月8日 下午2:21:24
	 */
	@SuppressWarnings({ "rawtypes" })
	protected String getString(List list) {
		if (list == null || list.size() == 0) {
			return "";
		}
		String retStr = "";
		for (Object obj : list) {
			Map map = (Map) obj;
			if (map == null || map.isEmpty()) {
				continue;
			}
			Object key = map.keySet().iterator().next();
			Object value = map.get(key);
			retStr = retStr + "," + (value == null ? "" : value.toString());
		}
		if (!BaseUtil.isBlank(retStr)) {
			retStr = retStr.substring(1);
		}
		return retStr;
	}
	
	/**
	 * 获取列表整型数据
	 * 
	 * @author:	lewe
	 * @date:	2017年12月8日 下午2:21:24
	 */
	@SuppressWarnings({ "rawtypes" })
	protected int getInt(List list) {
		if (list == null || list.size() == 0) {
			return 0;
		}
		int retVal = 0;
		for (Object obj : list) {
			Map map = (Map) obj;
			if (map == null || map.isEmpty()) {
				continue;
			}
			Object key = map.keySet().iterator().next();
			Object value = map.get(key);
			if (value == null) {
				continue;
			}
			retVal = Integer.valueOf(value.toString()).intValue();
			break;
		}
		return retVal;
	}
	
	/**
	 * 添加额外条件信息
	 * 
	 * @author:	lewe
	 * @date:	2017年12月8日 下午2:21:24
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected HashMap addExtraCondition(HashMap map) throws Exception {
		if (map == null) {
			map = new HashMap<String, Object>();
		}
		User user = BaseUtil.getLoginUser();
		map.put("user_no", user.getUserNo());
		map.put("bank_no", user.getBankNo());
		map.put("system_no", user.getSystemNo());
		map.put("project_no", user.getProjectNo());
		return map;
	}
}