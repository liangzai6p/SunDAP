package com.sunyard.cop.IF.modelimpl.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFCommonUtil;
import com.sunyard.cop.IF.common.SunIFConstant;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.support.IRoleRightsService;
import com.sunyard.cop.IF.mybatis.dao.MenuTreeMapper;
import com.sunyard.cop.IF.mybatis.dao.RightMapper;
import com.sunyard.cop.IF.mybatis.pojo.Menu;
import com.sunyard.cop.IF.mybatis.pojo.Right;
import com.sunyard.cop.IF.mybatis.pojo.User;

@Service("roleRightsService")
@Transactional
public class RoleRightsImpl extends BaseService implements IRoleRightsService {
	@Resource
	private RightMapper rightDao;

	@Resource
	private MenuTreeMapper menuDao;

	private Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseBean roleRights(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		Map sysMap = requestBean.getSysMap();
		String oper_type = (String) sysMap.get("oper_type");

		if ("QUERY".equalsIgnoreCase(oper_type)) { // 查询角色权限
			ArrayList requestList = (ArrayList) requestBean.getParameterList();
			Right roleRightbean = (Right) requestList.get(0);
			String roleNo = roleRightbean.getRoleNo();
			Map queryMap = new HashMap();
			ArrayList list = this.queryRoleRight(roleNo);
			if (list != null) {
				queryMap.put("list", list);
				responseBean.setRetMap(queryMap);
				responseBean.setRetCode("IF0000");
				responseBean.setRetMsg("查询成功");
			} else {
				responseBean.setRetCode("IF0003");
				responseBean.setRetMsg("查询角色权限失败");
				logger.info("查询角色权限失败");
			}
		} else if ("INIT".equalsIgnoreCase(oper_type)) { // 初始化树操作
			Map initMap = new HashMap();
			ArrayList menuList = this.initMenu();
			initMap.put("menuList", menuList);
			ArrayList buttonList = this.initButton();
			initMap.put("buttonList", buttonList);
			responseBean.setRetMap(initMap);
			responseBean.setRetCode("IF0000");
			responseBean.setRetMsg("初始化成功");

		} else if (SunIFConstant.UPDATE.equalsIgnoreCase(oper_type)) { // 更新角色权限
			ArrayList requestList = (ArrayList) requestBean.getParameterList();
			updateRight(requestList);
			responseBean.setRetCode("IF0000");
			responseBean.setRetMsg("更新成功");
		}if("updateMenu".equals(oper_type)){
			Right rightBean = (Right)requestBean.getParameterList().get(0);
			//删除当前角色拥有的菜单！
			rightDao.deleteByRoleNo(sysMap.get("roleNo").toString());
			//添加客户选择的全部菜单!
			String addStr = sysMap.get("addStr").toString();
			if (!BaseUtil.isBlank(addStr)) {
				String[] addStrsplit = addStr.split(",");
				for (int i = 0; i < addStrsplit.length; i++) {
					Right right= new Right();
					right.setRoleNo(sysMap.get("roleNo").toString());
					right.setMenuId(addStrsplit[i]);
					right.setIsOpen(rightBean.getIsOpen());
					right.setLastModiDate(BaseUtil.getCurrentTimeStr());
					right.setBankNo(sysMap.get("bankNo").toString());
					right.setSystemNo(sysMap.get("systemNo").toString());
					right.setProjectNo(sysMap.get("projectNo").toString());
					rightDao.insertRight(right);
				}
			}
			
			//更新按钮！在菜单之后拥有！
			String item="";
			String value="";
			//新按钮集合封装到Map！
			List<Map<String,Object>> menuBtn = (List<Map<String, Object>>) sysMap.get("menuBtn");
			Map<String,Object> map = new HashMap<String,Object>();
			for (int i = 0; i < menuBtn.size(); i++) {
				item = menuBtn.get(i).get("menuId").toString();
				value = menuBtn.get(i).get("buttonId").toString();
				if (map.get(item)==null) {
					map.put(item, value);
				}else {
					String str= map.get(item).toString();
					str+=","+value;
					map.put(item, str);
				}
			}
			Set<Entry<String,Object>> entrySet = map.entrySet();
			Iterator<Entry<String, Object>> iterator = entrySet.iterator();
			while (iterator. hasNext()) {
				Right right= new Right();
				Entry<String, Object> next = iterator.next();
				right.setRoleNo(sysMap.get("roleNo").toString());
				right.setIsOpen(rightBean.getIsOpen());
				right.setLastModiDate(BaseUtil.getCurrentTimeStr());
				right.setButtonId(next.getValue().toString());
				System.out.println(next.getKey());
				right.setMenuId(next.getKey());
				right.setBankNo(sysMap.get("bankNo").toString());
				right.setSystemNo(sysMap.get("systemNo").toString());
				right.setProjectNo(sysMap.get("projectNo").toString());
				rightDao.updateRight(right);
			}
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("角色更新成功！");
		}
		return responseBean;
	}

	/**
	 * 查询角色权限
	 * 
	 * @param roleNo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ArrayList queryRoleRight(String roleNo) {
		User user = BaseUtil.getLoginUser();
		Right right = new Right();
		right.setBankNo(user.getBankNo());
		right.setSystemNo(user.getSystemNo());
		right.setProjectNo(user.getProjectNo());
		right.setRoleNo(roleNo);
		return rightDao.selectRight(right);
	}

	/**
	 * 初始化菜单树（sm_menu_tb）
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ArrayList initMenu() {
		User user = BaseUtil.getLoginUser();
		Menu menu = new Menu();
		menu.setBankNo(user.getBankNo());
		menu.setProjectNo(user.getProjectNo());
		menu.setSystemNo(user.getSystemNo());
		menu.setIsOpen(SunIFConstant.USE);
		return menuDao.initMenu(menu);
	}
	
	/**
	 * 初始化菜单按钮（sm_button_tb）
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList initButton() {
		User user = BaseUtil.getLoginUser();
		Map map = new HashMap<String, Object>();
		map.put("bankNo", user.getBankNo());
		map.put("systemNo", user.getSystemNo());
		map.put("projectNo", user.getProjectNo());
		return menuDao.initButton(map);
	}

	/**
	 * 更新权限
	 */
	@SuppressWarnings("rawtypes")
	private void updateRight(ArrayList requestList) {
		String lastModiDate = SunIFCommonUtil.getFomateTimeString("yyyyMMddHHmmss");
		User user = BaseUtil.getLoginUser();
		for (int i = 0; i < requestList.size(); i++) {
			Right rightBean = (Right) requestList.get(i);
			String oper_nodes = rightBean.getOperNodes();
			rightBean.setBankNo(user.getBankNo());
			rightBean.setProjectNo(user.getProjectNo());
			rightBean.setSystemNo(user.getSystemNo());
			rightBean.setIsLock(user.getUserNo());
			rightBean.setLastModiDate(lastModiDate);
			switch (oper_nodes) {
			case "addNodes":
				rightBean.setIsOpen("1");
				rightDao.insertRight(rightBean);
				String	log="权限ID"+rightBean.getRoleNo()+"在权限表中被创建！";
				//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM, ARSConstants.OPER_TYPE_1, log);
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
				break;
			case "deleteNodes":
				rightDao.deleteRight(rightBean);
				log="权限ID"+rightBean.getRoleNo()+"在权限表中被删除！";
				//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM, ARSConstants.OPER_TYPE_2, log);
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
				break;
			case "updateNodes":
				rightDao.updateRight(rightBean);
				log="权限ID"+rightBean.getRoleNo()+"在权限表中被修改！";
				//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM, ARSConstants.OPER_TYPE_3, log);
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		
	}
}