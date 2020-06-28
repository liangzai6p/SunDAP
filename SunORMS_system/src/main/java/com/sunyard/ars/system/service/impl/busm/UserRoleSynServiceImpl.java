package com.sunyard.ars.system.service.impl.busm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.busm.UserBean;
import com.sunyard.ars.system.bean.busm.UserRole;
import com.sunyard.ars.system.common.CommonSystemConstants;
import com.sunyard.ars.system.dao.busm.ArmsUserTaltTbMapper;
import com.sunyard.ars.system.dao.busm.OrganInfoDao;
import com.sunyard.ars.system.dao.busm.UserDao;
import com.sunyard.ars.system.dao.busm.UserRoleMapper;
import com.sunyard.ars.system.service.busm.IUserRoleSynService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;

@Service("userRoleSynService")
@Transactional
public class UserRoleSynServiceImpl extends BaseService implements IUserRoleSynService{

	//SM_USER_ROLE_TB
	@Resource
	private UserRoleMapper userRoleMapper;
	
	//SM_USERS_TB
	@Resource
	private UserDao userDao;
	
	@Resource
	private ArmsUserTaltTbMapper armsUserTaltTbMapper;
	
	@Resource
	private OrganInfoDao organInfoDao;
	
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		List<Object> list =  requestBean.getParameterList();
		StringBuilder returnStrBuilder = new StringBuilder();
		// 使用数据库中默认值进行查询
		/*BANK_NO	N	VARCHAR2(50)	N	'SUNYARD'		银行号
		SYSTEM_NO	N	VARCHAR2(50)	N	'AOS'		系统号
		PROJECT_NO	N	VARCHAR2(50)	N	'AOS'		项目号*/
		if (null != sysMap && !(sysMap.isEmpty()) && null != sysMap.get("oper_type") && (("userRoleSyn").equals(sysMap.get("oper_type"))) && null != sysMap.get("user_info") && (!("").equals(sysMap.get("user_info")))) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.clear();
			tempMap = (Map<String, Object>) sysMap.get("user_info");
			if (null != tempMap && !(tempMap.isEmpty())) {
				String password = (String) tempMap.get("password");
				String user_name = (String) tempMap.get("user_name");
				String organ_no = (String) tempMap.get("organ_no");
				String user_no = (String) tempMap.get("user_no");
				if (null != user_no && !(("").equals(user_no))) {
					Integer insertFlag = armsUserTaltTbMapper.selectCountByUserNo(user_no);
					UserBean userBean = userDao.selectByPrimaryKey(user_no, "SUNYARD", "AOS", "AOS");
					// 表示传入用户不存在，需要新增
					if (insertFlag < 1 && (userBean == null || userBean.getUserNo() == null)) {
						Integer addFlagInteger = 0;
						UserBean needInsertUserBean = new UserBean();
						needInsertUserBean.setUserNo(user_no);
						needInsertUserBean.setOrganNo(organ_no);
						needInsertUserBean.setUserName(user_name);
						needInsertUserBean.setPassword(password);
						//设置默认密码登录
						needInsertUserBean.setLoginMode("1");
						//设置默认用户状态为正常
						needInsertUserBean.setUserStatus("1");
						addFlagInteger = userDao.insertSelective(needInsertUserBean);
						if (addFlagInteger > 0) {
							//同步添加用户对应机构权限
							addOrDeleteUserOrgan(organ_no, user_no,true);
							returnStrBuilder.append("已将数据"+needInsertUserBean.toString()+"添加成功;");
						} else {
							returnStrBuilder.append("数据"+needInsertUserBean.toString()+"未添加;");
						}
					}
					if (null != list && !(list.isEmpty())) {
						for (Object tempObject:list) {
							Map<String,Object> tempMapBytempObject = new HashMap<String, Object>();
							tempMapBytempObject = (Map<String, Object>)(tempObject);
							String mapping_no = (String) tempMapBytempObject.get("mapping_no");
							String oper_type = (String) tempMapBytempObject.get("oper_type");
							// 表示添加
							if (null != mapping_no && !(("").equals(mapping_no)) && null != oper_type && !(("").equals(oper_type))) {
								Integer addOrDeleteFlag = 0;
								if (oper_type.equals(CommonSystemConstants.QD_OCR_CLIENT_USER_ROLE_OPER_TYPE_ADD)) {
									addOrDeleteFlag = addOrDeleteUserROle(organ_no,user_no,mapping_no,true);
									if(addOrDeleteFlag > 0) {
										returnStrBuilder.append("已将数据"+"organNo:"+organ_no+"userNo:"+user_no+"roleNo:"+mapping_no+"添加成功;");
									} else {
										returnStrBuilder.append("数据"+"organNo:"+organ_no+"userNo:"+user_no+"roleNo:"+mapping_no+"添加失败;");
									}
									// 表示删除
								} else if(oper_type.equals(CommonSystemConstants.QD_OCR_CLIENT_USER_ROLE_OPER_TYPE_DELETE)) {
									addOrDeleteFlag = addOrDeleteUserROle(organ_no,user_no,mapping_no,false);
									if (addOrDeleteFlag > 0) {
										returnStrBuilder.append("已将数据"+"organNo:"+organ_no+"userNo:"+user_no+"roleNo:"+mapping_no+"成功删除;");
									} else {
										returnStrBuilder.append("数据"+"organNo:"+organ_no+"userNo:"+user_no+"roleNo:"+mapping_no+"删除失败;");
									}
								}
							}
						}
					}
					responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
					responseBean.setRetMsg(returnStrBuilder.toString());
					return;
				}
			}
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("调用成功");
		return;
	}
	
	/**
	 * 添加或删除指定数据
	 * 用户权限机构
	 * flag为ture表示添加,否则为删除
	 * @param organNo
	 * @param userNo
	 * @param roleNo
	 * @param flag
	 */
	private int addOrDeleteUserROle(String organNo,String userNo,String roleNo,Boolean flag) {
		//flag为ture表示添加,否则为删除
		if (flag) {
			UserRole needInsertUserRole = new UserRole();
			needInsertUserRole.setOrganNo(organNo);
			needInsertUserRole.setUserNo(userNo);
			needInsertUserRole.setRoleNo(roleNo);
			needInsertUserRole.setLastModiDate(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			needInsertUserRole.setBankNo("SUNYARD");
			needInsertUserRole.setSystemNo("AOS");
			needInsertUserRole.setProjectNo("AOS");
			List<UserRole> roleList = userRoleMapper.selectBySelective(needInsertUserRole,(String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));
			if (null == roleList || roleList.isEmpty()) {
				return userRoleMapper.insertSelective(needInsertUserRole);
			}
			return 0;
		} else {
			return userRoleMapper.deleteByUserNoAndRoleNo(userNo, roleNo);
		}
	}
	
	private int addOrDeleteUserOrgan(String organNo,String userNo,Boolean flag) {
		//flag为ture表示添加,否则为删除
		if (flag) {
			return organInfoDao.addUserOrgan(organNo,userNo);
		} else {
			return organInfoDao.deleteUserAllOrgan(userNo);
		}
	}
}
