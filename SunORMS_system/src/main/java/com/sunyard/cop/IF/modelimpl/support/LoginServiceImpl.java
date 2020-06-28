/**
 * 
 */
package com.sunyard.cop.IF.modelimpl.support;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.dao.BaseDao;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.HttpUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.util.DateUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFCommonUtil;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.support.ILoginService;
import com.sunyard.cop.IF.mybatis.dao.LoginHistoryMapper;
import com.sunyard.cop.IF.mybatis.dao.OrganMapper;
import com.sunyard.cop.IF.mybatis.dao.UserMapper;
import com.sunyard.cop.IF.mybatis.pojo.LoginHistory;
import com.sunyard.cop.IF.mybatis.pojo.Organ;
import com.sunyard.cop.IF.mybatis.pojo.User;
import com.sunyard.cop.IF.mybatis.pojo.UserRole;

/**
 * 登录服务实现类
 * 
 * @author YZ 2017年3月15日 上午9:15:29
 */
@Service("loginService")
@Transactional
public class LoginServiceImpl implements ILoginService {

	private Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Resource
	private UserMapper userMapper;

	@Resource
	private LoginHistoryMapper loginHistory;
	
	@Resource
	private OrganMapper organMapper;
	
	@Resource
	private BaseDao baseDao;
	
	/**
	 * 
	 */
	public LoginServiceImpl() {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseBean userService(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		Map sysMap = requestBean.getSysMap();
		Map responseMap = new HashMap();
		if (sysMap.containsKey("oper_type")) {
			String loginUserStr = "loginUser";
			String loginFlag = "loginFlag";
			String loginMsg = "loginMsg";
			User user = (User) requestBean.getParameterList().get(0);
            User findUser = findUserExits(user.getUserNo());
            if ( findUser != null) {
			/*if (findUserExits(user.getUserNo()) != null) {*/
				String loginTerminal = user.getLoginTerminal();
				String loginType = user.getLoginType();
				User loginUser = null;
				if (AOSConstants.LOGIN_TYPE_PSWD.equals(loginType)) { // 密码登录
					loginUser = getUserByPassword(user);
				} else if (AOSConstants.LOGIN_TYPE_FINGER.equals(loginType)) { // 指纹登录
					
				} else if (AOSConstants.LOGIN_TYPE_FACE.equals(loginType)) { // 人脸识别
					
				} else if("4".equals(loginType)){//断点登录
					loginUser = findUserExits(user.getUserNo());
				}
				if (loginUser != null) {
					// 用户状态
					String userStatus = loginUser.getUserStatus();
					loginUser.setLoginType(loginType);
					loginUser.setLoginTime(BaseUtil.getCurrentTimeStr());
					loginUser.setLoginTerminal(loginTerminal);
					loginUser.setLastLoginTime(loginUser.getLoginTime());
					if (userStatus.equals("1")) { // 启用
						String roleStr = getUserRole(loginUser);
						loginUser.setRoleNo(roleStr);
						responseMap.put(loginUserStr, loginUser);
						responseMap.put(loginFlag, "loginSuccess");
						responseMap.put(loginMsg, "登录成功");
                        // 错误记数
                        int error_count = 0;
                        String user_no = loginUser.getUserNo();
                        HashMap condMap = new HashMap();
                        condMap.put("user_no", user_no);
                        condMap.put("error_count", error_count);
                        // 错误记数清零
                        userMapper.updateStateAndErrorCount(condMap);
						// 查询用户所属机构信息
						Map map = new HashMap<String, Object>();
						map.put("organ_no", BaseUtil.filterSqlParam(loginUser.getOrganNo()));
						map.put("bank_no", BaseUtil.filterSqlParam(loginUser.getBankNo()));
						map.put("system_no", BaseUtil.filterSqlParam(loginUser.getSystemNo()));
						map.put("project_no", BaseUtil.filterSqlParam(loginUser.getProjectNo()));
						Organ loginOrgan = organMapper.getOrganByNo(map);
						responseMap.put("loginOrgan", loginOrgan);
						// 判断用户密码是否过期
						String overDueFlag = getOverdueFlag(loginUser);
						responseMap.put("isOverFlag", overDueFlag);
						insertHistory(loginUser);
					} else if (userStatus.equals("0")) { // 停用
						responseMap.put(loginUserStr, null);
						responseMap.put(loginFlag, "loginFail");
						responseMap.put(loginMsg, "用户已停用");
					} else if (userStatus.equals("2")) { // 锁定
						responseMap.put(loginUserStr, null);
						responseMap.put(loginFlag, "loginFail");
						responseMap.put(loginMsg, "用户已锁定");
					} else {
						responseMap.put(loginUserStr, null);
						responseMap.put(loginFlag, "loginFail");
						responseMap.put(loginMsg, "登陆失败，用户状态异常");
					}
				} else {
					responseMap.put(loginUserStr, null);
					responseMap.put(loginFlag, "loginFail");
					responseMap.put(loginMsg, "用户或密码错误");


                    String error_count_String = findUser.getErrorCount();
                    int error_count = Integer.parseInt(error_count_String);
                    error_count = error_count+1;
                    // 设置可输入错误次数
                    int error_count_set = Integer.parseInt(userMapper.selectDefaultErrorCount());
                    // 剩余错误次数
                    int error_count_left = error_count_set - error_count;
                    // 用户名
                    String user_no = findUser.getUserNo();
                    // 用户状态2——锁定
                    String user_status = "2";
                    // 更新参数
                    HashMap condMap = new HashMap();
                    if(error_count == error_count_set){
                        // 账户锁定
                        condMap.put("user_no", user_no);
                        condMap.put("user_status", user_status);
                        condMap.put("error_count", error_count);
                        responseMap.put(loginMsg, "密码错误次数已达上限！用户已锁定！");
                        // 更新错误次数及状态
                        userMapper.updateStateAndErrorCount(condMap);
                    }else if(error_count > error_count_set){
                        responseMap.put(loginMsg, "用户已锁定！");
                    }else{
                        condMap.put("user_no", user_no);
                        condMap.put("error_count", error_count);
                        responseMap.put(loginMsg, "密码错误次数："+error_count+",剩余次数："+error_count_left);
                        // 更新错误次数及状态
                        userMapper.updateStateAndErrorCount(condMap);
                    }
				}
			} else {
				responseMap.put(loginUserStr, null);
				responseMap.put(loginFlag, "loginFail");
				responseMap.put(loginMsg, "用户或密码错误");
			}
			responseBean.setRetCode(SunIFErrorMessage.SUCCESS);
			responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
			responseBean.setRetMap(responseMap);
		} else {
			logger.error(" RequestBean中未取到oper_type,请检查发送的数据是否正确 ");
			responseBean.setRetCode(SunIFErrorMessage.ILLEGAL_ARGUMENT);
			responseBean.setRetMsg(SunIFErrorMessage.ILLEGAL_ARGUMENT_MSG);
		}
		return responseBean;
	}

	
	/**
	 * 查询用户是否存在
	 */
	private User findUserExits(String userNo) throws Exception{
		return userMapper.selectByUserNo(BaseUtil.filterSqlParam(userNo));
	}

	/**
	 * 根据用户号、密码查询用户信息
	 */
	private User getUserByPassword(User user) {
		return userMapper.findUserByPassword(user);
	}
	
	/**
	 * 判断用户密码是否过期
	 * @throws Exception 
	 */
	private String getOverdueFlag(User user) throws Exception {
		String overdueFlag = "0";
		try {
			// 用户最后修改时间
			String last_modi_date = user.getLastModiDate();
			if (BaseUtil.isBlank(last_modi_date)) {
				last_modi_date = "20180101000000";
			}
			int overDateNum = AOSConstants.OVER_DATE;
			// 查询密码有效期限
			String overDateStr = BaseUtil.getSysParamValue(baseDao, "OVERDATE");
			if (!BaseUtil.isBlank(overDateStr)) {
				overDateNum = Integer.parseInt(overDateStr);
			}
			SimpleDateFormat sdf = new SimpleDateFormat(AOSConstants.FORMAT_DATE_TIME);
			Date curDate = new Date();
			Date lastDate = sdf.parse(last_modi_date);
			int diffDays = BaseUtil.getDiffDays(lastDate, curDate);
			if (diffDays >= overDateNum) {
				overdueFlag = "1";	// 密码已过期
				logger.info("密码已使用时长 " + diffDays + " 天，大于密码有效期 " + overDateNum + " 天，提示修改密码！");
			}
		} catch (ParseException e) {
			logger.error("判断用户密码是否过期出错：" + e.getMessage(), e);
		}
		return overdueFlag;
	}
	
	/**
	 * 添加登录历史记录信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void insertHistory(User loginUser) {
		// 这个方法加上异常处理机制， 此方法用于记录登陆信息，不影响登陆，发生异常也不能影响使用
		try {
            // 校验单一登录限制
            String single_login = loginUser.getSingleLogin();
            if ("1".equals(single_login)) { // 启用单一登录
                // 校验用户是否已在其他服务器登录（集群）
                checkExistLogin(loginUser);
            }
			
			// 更新用户登录状态、登录服务器信息
			updateUserLoginInfo(loginUser);
			
			HashMap map = new HashMap();
			map.put("module", ARSConstants.MODEL_NAME_ARS);
			map.put("operType", ARSConstants.OPER_TYPE_5);
			map.put("organ_no", BaseUtil.filterSqlParam(loginUser.getOrganNo()));
			map.put("user_no", BaseUtil.filterSqlParam(loginUser.getUserNo()));
			map.put("content", "系统登录，登录IP:"+SunIFCommonUtil.getIpAddr(RequestUtil.getRequest()));
			map.put("log_date", DateUtil.getNow());
			map.put("log_time", DateUtil.getHMS2());
			baseDao.insertOperLogInfo(map);
			/*LoginHistory history = new LoginHistory();
			history.setBankNo(loginUser.getBankNo());
			history.setProjectNo(loginUser.getProjectNo());
			history.setSystemNo(loginUser.getSystemNo());
			history.setUserNo(loginUser.getUserNo());
			history.setLoginTime(loginUser.getLastLoginTime());
			history.setLoginType(loginUser.getLoginType());
			history.setLoginTerminal(loginUser.getLoginTerminal());
			history.setLoginIp(SunIFCommonUtil.getIpAddr(RequestUtil.getRequest()));
			history.setLoginMac("");
			loginHistory.insertRecord(history);*/
		} catch (Exception e) {
			logger.error("记录登录历史信息出错：" + e.getMessage(), e);
		}
	}

	/**
	 * 查询用户角色信息，多个角色以逗号分隔
	 */
	@SuppressWarnings("rawtypes")
	private String getUserRole(User loginUser) throws Exception{
		UserRole userRole = new UserRole();
		userRole.setBankNo(BaseUtil.filterSqlParam(loginUser.getBankNo()));
		userRole.setSystemNo(BaseUtil.filterSqlParam(loginUser.getSystemNo()));
		userRole.setProjectNo(BaseUtil.filterSqlParam(loginUser.getProjectNo()));
		userRole.setUserNo(BaseUtil.filterSqlParam(loginUser.getUserNo()));
		String result = "";
		ArrayList list = userMapper.getUserRole(userRole);
		if (!list.isEmpty()) {
			for (int i = 0, n = list.size(); i < n; i++) {
				result = result + list.get(i) + ",";
			}
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}
	
	/**
	 * 校验当前用户是否已在其他服务器登录（集群）
	 */
	@SuppressWarnings("rawtypes")
	private void checkExistLogin(User loginUser) {
		try {
			String user_no = loginUser.getUserNo();
			String login_terminal = loginUser.getLoginTerminal();
			String login_server = "";
			if (AOSConstants.LOGIN_FLAG.equals(loginUser.getLoginState())) {
				if (AOSConstants.LOGIN_TERMINAL_PC.equals(login_terminal)) {
					login_server = loginUser.getLoginPCServer();
				} else {
					login_server = loginUser.getLoginMobileServer();
				}
			}
			String current_server = HttpUtil.getCurrentServerInfo() + HttpUtil.getCurrentContextPath();
			if (!BaseUtil.isBlank(login_server) && !login_server.equals(current_server)) {
				// 表明当前用户已在其他服务器登录，需发送http请求到对应服务器，再通过websocket推送消息到前台提醒，使其被迫下行线
				String url = "http://" + login_server + "/aosMain.do";
				logger.info("消息推送URL：" + BaseUtil.filterLog(url));
				Map<String, Object> sysMap = new HashMap<String, Object>();
				sysMap.put("oper_type", AOSConstants.MSG_TYPE_LOGOUT);
				sysMap.put("user_no", user_no);
				sysMap.put("login_terminal", login_terminal);

				Map<String, Object> msgMap = new HashMap<String, Object>();
				msgMap.put("parameterList", new ArrayList());
				msgMap.put("sysMap", sysMap);

				ObjectMapper mapper = new ObjectMapper();
				String msg = mapper.writeValueAsString(msgMap);

				HttpUtil.sendPostThread(url, "message=" + URLEncoder.encode(msg, HttpUtil.CHAR_SET));
			}
		} catch (Exception e) {
			logger.error("判断当前用户是否已在其他服务器登录时出错：" + e.getMessage(), e);
		} finally {
			
		}
	}
	
	/**
	 * 更新用户登录信息（登录状态、登录服务器）
	 * @throws Exception 
	 */
	private void updateUserLoginInfo(User loginUser) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_no", BaseUtil.filterSqlParam(loginUser.getUserNo()));
		map.put("last_login_time", BaseUtil.filterSqlParam(loginUser.getLastLoginTime()));
		map.put("login_state", "1");
		map.put("login_terminal", BaseUtil.filterSqlParam(loginUser.getLoginTerminal()));
		map.put("login_server", BaseUtil.filterSqlParam(HttpUtil.getCurrentServerInfo() + HttpUtil.getCurrentContextPath()));
		userMapper.updateUserLoginInfo(map);
	}
}