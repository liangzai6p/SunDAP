package com.sunyard.cop.IF.modelimpl.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.support.IAppearanceQueryService;
import com.sunyard.cop.IF.mybatis.dao.RoleMapper;
import com.sunyard.cop.IF.mybatis.pojo.Role;
import com.sunyard.cop.IF.mybatis.pojo.User;

/**
 * 外表查询 服务
 */
@Service("appearanceQueryService")
@Transactional
public class AppearanceQueryServiceImpl implements IAppearanceQueryService {

	@Resource
	private RoleMapper roleDao;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ResponseBean appearanceQuery(RequestBean req) throws Exception {
		ResponseBean retBean = new ResponseBean();
		Map sysMap = req.getSysMap();
		String oper_type = String.valueOf(sysMap.get("oper_type"));
		if ("role_no".equalsIgnoreCase(oper_type)) {
			Map queryMap = new HashMap();
			ArrayList array = queryRole();
			if (array != null) {
				queryMap.put("roleNoList", array);
				retBean.setRetCode("IF0000");
				retBean.setRetMsg("查询成功");
				retBean.setRetMap(queryMap);
			} else {
				retBean.setRetCode("IF0001");
				retBean.setRetMsg("查询失败");
			}
			return retBean;
		}
		if ("queryRoleByRoleType".equalsIgnoreCase(oper_type)) {
			String roleModel = String.valueOf(sysMap.get("roleModel"));
			Map queryMap = new HashMap();
			ArrayList array = queryRoleByRoleType(roleModel);
			if (array != null) {
				queryMap.put("roleNoList", array);
				retBean.setRetCode("IF0000");
				retBean.setRetMsg("查询成功");
				retBean.setRetMap(queryMap);
			} else {
				retBean.setRetCode("IF0001");
				retBean.setRetMsg("查询失败");
			}
			return retBean;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	private ArrayList queryRole() {
		User user = BaseUtil.getLoginUser();
		Role role = new Role();
		role.setBankNo(user.getBankNo());
		role.setSystemNo(user.getSystemNo());
		role.setProjectNo(user.getProjectNo());
		return roleDao.selectRole(role);
	}
	
	
	/**
	 * 查询角色根据角色类型
	 * @param roleMode
	 * @return
	 */
	private ArrayList<Role> queryRoleByRoleType(String roleMode) {
		User user = BaseUtil.getLoginUser();
		Role role = new Role();
		role.setBankNo(user.getBankNo());
		role.setSystemNo(user.getSystemNo());
		role.setProjectNo(user.getProjectNo());
		role.setIsOpen("1");//只查询启用的
		role.setRoleMode(roleMode);
		return roleDao.queryRoleByRoleType(role);
	}
}
