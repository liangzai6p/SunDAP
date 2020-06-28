package com.sunyard.cop.IF.modelimpl.support;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFConstant;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.support.IUserPasswordConfigService;
import com.sunyard.cop.IF.mybatis.dao.UserPasswordConfigMapper;
import com.sunyard.cop.IF.mybatis.pojo.User;

@Service("userPasswordConfigService")
@Transactional
public class UserPasswordConfigServiceImpl implements IUserPasswordConfigService {
	
	@Resource
	private UserPasswordConfigMapper userPasswordConfigDao;
	
	private Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseBean passwordService(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		Map sysMap = requestBean.getSysMap();
		if (sysMap.containsKey("oper_type")) {
			String oper_type = String.valueOf(sysMap.get("oper_type"));
			// User user = (User) requestBean.getParameterList().get(0);
			if ("check".equalsIgnoreCase(oper_type)) {
				Map retMap = new HashMap();
				String userNo = String.valueOf(sysMap.get("userNo"));
				User u = new User();
				u.setUserNo(userNo);
				String password = checkPassword(u);
				if (password != null) {
					//retMap.put("password", AESUtil.Decrypt(password, ""));
					retMap.put("password", password);
					responseBean.setRetMap(retMap);
					responseBean.setRetCode("IF0000");
					responseBean.setRetMsg("检查密码成功");
				} else {
					
				}
			} else if ("update".equalsIgnoreCase(oper_type)) {
				String oldPass = String.valueOf(sysMap.get("oldPass"));
				String userNo = String.valueOf(sysMap.get("userNo"));
				User u = new User();
				u.setUserNo(userNo);
				//u.setPassword(AESUtil.Encrypt(oldPass, ""));
				u.setPassword(oldPass);
				int count = selectChangeUser(u);
				// 需要修改密码的用户初始密码错误
				if (1 != count) {
					responseBean.setRetCode("IF0003");
					responseBean.setRetMsg("用户旧密码错误!");
				}
				// 密码正确，修改密码
				else {
					String newPass = String.valueOf(sysMap.get("newPass"));
					// 先判断新密码和旧密码是否相同
					if (newPass.equals(oldPass)) {
						responseBean.setRetCode("IF0003");
						responseBean.setRetMsg("用户新密码与旧密码不能相同！");
					} else {
						//updatePassword(userNo, AESUtil.Encrypt(newPass, ""));
						updatePassword(userNo, newPass);
						responseBean.setRetCode("IF0000");
						responseBean.setRetMsg("用户密码修改成功");
					}
				}
			} else if ("reset".equalsIgnoreCase(oper_type)) {

				String sq_userNo = String.valueOf(sysMap.get("sq_userNo"));
				String sq_password = String.valueOf(sysMap.get("sq_password"));
				String userNo = String.valueOf(sysMap.get("userNo"));
				// 系统参数表的密码重置授权岗
				String grant_post = SunIFConstant.PARAM_MAP.get("GRANT_POST");
				// 根据用户表、系统参数表，只有授权岗才允许修改
				// 查询授权者是否存在
				User u = new User();
				u.setUserNo(sq_userNo);
				//u.setPassword(AESUtil.Encrypt(sq_password, ""));
				u.setPassword(sq_password);
				int count = selectGrantUser(u, grant_post);
				// 授权者不存在或密码错误
				if (1 != count) {
					responseBean.setRetCode("IF0003");
					responseBean.setRetMsg("重置密码失败，授权者不存在或密码错误");
				} else {
					// 查询需要重置密码的用户是否存在
					int countUser = selectResetUser(userNo);
					// 需要重置密码的用户不存在
					if (1 != countUser) {
						responseBean.setRetCode("IF0004");
						responseBean.setRetMsg("待重置用户不存在!");
					} else {
						String default_password = SunIFConstant.PARAM_MAP.get("default_password");
						//updatePassword(userNo, AESUtil.Encrypt(default_password, ""));
						updatePassword(userNo, default_password);
						responseBean.setRetCode("IF0000");
						responseBean.setRetMsg("用户重置密码成功");
					}
				}
			}
		} else {
			logger.error(" RequestBean中未取到oper_type,请检查发送的数据是否正确 ");
			responseBean.setRetCode(SunIFErrorMessage.ILLEGAL_ARGUMENT);
			responseBean.setRetMsg(SunIFErrorMessage.ILLEGAL_ARGUMENT_MSG);
		}
		return responseBean;
	}

	private String checkPassword(User user) {
		User u = BaseUtil.getLoginUser();
		user.setBankNo(u.getBankNo());
		user.setSystemNo(u.getSystemNo());
		user.setProjectNo(u.getProjectNo());
		return userPasswordConfigDao.checkPassword(user);
	}

	private int selectChangeUser(User user) {
		User u = BaseUtil.getLoginUser();
		user.setBankNo(u.getBankNo());
		user.setSystemNo(u.getSystemNo());
		user.setProjectNo(u.getProjectNo());
		return userPasswordConfigDao.selectChangeUser(user);
	};

	private int selectResetUser(String userNo) {
		User u = BaseUtil.getLoginUser();

		User user = new User();
		user.setUserNo(userNo);
		user.setBankNo(u.getBankNo());
		user.setSystemNo(u.getSystemNo());
		user.setProjectNo(u.getProjectNo());
		
		return userPasswordConfigDao.selectResetUser(user);
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private int selectGrantUser(User user, String grant_post) {
		User u = BaseUtil.getLoginUser();
		
		user.setBankNo(u.getBankNo());
		user.setSystemNo(u.getSystemNo());
		user.setProjectNo(u.getProjectNo());

		Map<String, Object> params = new HashMap();
		params.put("user", user);
		params.put("grant_post", grant_post);
		
		return userPasswordConfigDao.selectGrantUser(params);
	};

	private int updatePassword(String userNo, String newPass) {
		User u = BaseUtil.getLoginUser();
		
		User user = new User();
		user.setUserNo(userNo);
		user.setPassword(newPass);
		user.setLastModiDate(BaseUtil.getCurrentTimeStr());
		user.setBankNo(u.getBankNo());
		user.setSystemNo(u.getSystemNo());
		user.setProjectNo(u.getProjectNo());

		return userPasswordConfigDao.updatePassword(user);
	};

}
