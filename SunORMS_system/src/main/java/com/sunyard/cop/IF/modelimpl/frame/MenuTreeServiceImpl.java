package com.sunyard.cop.IF.modelimpl.frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.frame.IMenuTreeService;
import com.sunyard.cop.IF.mybatis.dao.MenuTreeMapper;
import com.sunyard.cop.IF.mybatis.pojo.Menu;
import com.sunyard.cop.IF.mybatis.pojo.User;

@Service("menuTreeService")
@Transactional
public class MenuTreeServiceImpl implements IMenuTreeService {

	@Resource
	private MenuTreeMapper menuTreeDao;

	private Logger logger = LoggerFactory.getLogger(MenuTreeServiceImpl.class);

	public MenuTreeServiceImpl() {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseBean menuTreeService(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		Map sysMap = requestBean.getSysMap();
		boolean isPC = (boolean) sysMap.get("isPC");

		if (sysMap.containsKey("oper_type")) {
			User user = BaseUtil.getLoginUser();
			String user_no = user.getUserNo();
			switch (user_no) {
			// 超级管理员 生成菜单方式
			case AOSConstants.SYS_ADMIN:
				logger.info("[" + user_no + "]" + " 为超级管理员，查询所有菜单信息 ");
				ArrayList list = getAdminMenu(isPC);
				Map tempMap = new HashMap();
				if (list == null) {
					tempMap.put("menuFlag", false);
					tempMap.put("menuList", null);
				} else {
					tempMap.put("menuFlag", true);
					tempMap.put("menuList", BaseUtil.convertListMapKeyValue(list));
				}
				responseBean.setRetCode(SunIFErrorMessage.SUCCESS);
				responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
				responseBean.setRetMap(tempMap);
				break;
			// 普通用户
			default:
				String role_no = user.getRoleNo();
				logger.info("[" + user_no + "]" + " 为普通用户，查询对应角色[" + role_no + "]的菜单信息 ");
				Map deTempMap = new HashMap();
				if (BaseUtil.isBlank(role_no)) {
					deTempMap.put("menuFlag", false);
					deTempMap.put("menuList", null);
				} else {
					ArrayList<Map> deList = defaultQueryMenu(role_no, isPC);
//					deList = dealListMapButtin(deList);
					if (deList != null && deList.size() > 0) {
						deTempMap.put("menuFlag", true);
						deTempMap.put("menuList", BaseUtil.convertListMapKeyValue(deList));
					} else {
						deTempMap.put("menuFlag", false);
						deTempMap.put("menuList", null);
					}
				}
				responseBean.setRetCode(SunIFErrorMessage.SUCCESS);
				responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
				responseBean.setRetMap(deTempMap);
			}
		} else {
			logger.error(" RequestBean中未取到oper_type，请检查发送的数据是否正确 ");
			responseBean.setRetCode(SunIFErrorMessage.ILLEGAL_ARGUMENT);
			responseBean.setRetMsg(SunIFErrorMessage.ILLEGAL_ARGUMENT_MSG);
		}
		return responseBean;
	}

	/**
	 * 查询管理员菜单信息（所有菜单）
	 */
	@SuppressWarnings("rawtypes")
	private ArrayList getAdminMenu(boolean isPC) throws Exception {
		User user = BaseUtil.getLoginUser();
		Menu menu = new Menu();
		menu.setBankNo(user.getBankNo());
		menu.setSystemNo(user.getSystemNo());
		menu.setProjectNo(user.getProjectNo());
		if (isPC) {
			menu.setMenuBelong("0");
		} else {
			menu.setMenuBelong("1");
		}
		return menuTreeDao.selectAdminMenu(menu);
	}

	/**
	 * 查询指定角色的菜单信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ArrayList<Map> defaultQueryMenu(String role_no, boolean isPC) {
		Map<String, Object> params = new HashMap();
		User user = BaseUtil.getLoginUser();
		Menu menu = new Menu();
		menu.setBankNo(user.getBankNo());
		menu.setSystemNo(user.getSystemNo());
		menu.setProjectNo(user.getProjectNo());
		if (isPC) {
			menu.setMenuBelong("0");
		} else {
			menu.setMenuBelong("1");
		}
		params.put("menu", menu);
		params.put("roles", BaseUtil.addSingleQuote(role_no));
		return menuTreeDao.selectdefaultQueryMenu(params);
	}
	
	/**
	 * 为配置了按钮权限的页面查询页面配置的所有按钮。
	 * @param deList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList<Map> dealListMapButtin(ArrayList<Map> deList) throws Exception {
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		ArrayList<String> menuList = new ArrayList<String>();
		for(int i=0; i<deList.size(); i++) {
			Map menuMap = deList.get(i);
			String menuId = (String)menuMap.get("MENU_ID");
			String buttonIds = (String)menuMap.get("BUTTON_ID");
			if(buttonIds != null && buttonIds.length() >0) {
				indexList.add(i);
				menuList.add(menuId);
			}
		}
		if(menuList.size()==0){
			return deList;
		}

		User user = BaseUtil.getLoginUser();
		Map parMap = new HashMap();
		parMap.put("menuIds", menuList);
		parMap.put("bankNo",user.getBankNo());
		parMap.put("systemNo",user.getSystemNo());
		parMap.put("projectNo",user.getProjectNo());
		ArrayList<Map> buttonList = menuTreeDao.selectButtonByMenu(parMap);
		Map<String, String> menuButtonMap = new HashMap<String, String>();
		for(Map map : buttonList) {
			String menuId = (String)map.get("MENU_ID");
			String buttonId = (String)map.get("BUTTON_ID");
			if(menuButtonMap.containsKey(menuId)) {
				menuButtonMap.put(menuId, menuButtonMap.get(menuId)+","+buttonId);
			}else {
				menuButtonMap.put(menuId, buttonId);
			}
		}
		for(Integer index : indexList) {
			Map map = deList.get(index);
			map.put("ALL_BUTTON", BaseUtil.filterHeader(menuButtonMap.get(map.get("MENU_ID"))));
		}
		return deList;
	}
}