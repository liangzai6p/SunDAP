package com.sunyard.ars.system.service.impl.busm;


import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.service.busm.IMenuService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFCommonUtil;
import com.sunyard.cop.IF.common.SunIFConstant;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.dao.ButtonMapper;
import com.sunyard.cop.IF.mybatis.dao.CustomMenuMapper;
import com.sunyard.cop.IF.mybatis.dao.MenuCustompageMapper;
import com.sunyard.cop.IF.mybatis.dao.RightMapper;
import com.sunyard.cop.IF.mybatis.pojo.Button;
import com.sunyard.cop.IF.mybatis.pojo.Menu;
import com.sunyard.cop.IF.mybatis.pojo.Right;
import com.sunyard.cop.IF.mybatis.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

@Service("menuService")
@Transactional
public class MenuServiceImpl extends BaseService implements IMenuService {
    
    @Resource
	private CustomMenuMapper customMenuMapper;

	@Resource
	private ButtonMapper buttonMapper;

	@Resource
	private RightMapper rightMapper;

	@Resource
	private MenuCustompageMapper menupageMapper;
	
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}
    
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doAction(RequestBean requestBean, ResponseBean retBean) throws Exception {
		// TODO Auto-generated method stub
		User user = BaseUtil.getLoginUser();
		Menu menu = new Menu();
		menu.setBankNo(user.getBankNo());
		menu.setProjectNo(user.getProjectNo());
		menu.setSystemNo(user.getSystemNo());
		menu.setIsLock(user.getUserNo());
		Map reqMap = new HashMap();
		reqMap = requestBean.getSysMap();
		String oper_type = (String) reqMap.get("oper_type");
		Map retMap = new HashMap();
		String menuFlag = "menuFlag";
		String menuMsg = "menuMsg";
		if ("QUERYALL".equalsIgnoreCase(oper_type)) {
			ArrayList menulist = this.queryCustomMenu(menu);
			if (menulist != null) {
				retMap.put("menulist", menulist);
				retMap.put(menuFlag, "querySuccess");
				retMap.put(menuMsg, "查询成功！");
			} else {
				retMap.put(menuFlag, "queryFail");
				retMap.put(menuMsg, "查询失败！");
			}
		} else if ("ADDMENU".equalsIgnoreCase(oper_type)) {
			ArrayList reqList = (ArrayList) requestBean.getParameterList();
			Menu addmenu = (Menu) reqList.get(0);
			menu.setMenuName(addmenu.getMenuName());
			menu.setParentId(addmenu.getParentId());
			menu.setMenuDesc(addmenu.getMenuDesc());
			menu.setMenuUrl(addmenu.getMenuUrl());
			menu.setMenuLevel(addmenu.getMenuLevel());
			menu.setMenuBelong("0");
			menu.setMenuOrder("1");
			menu.setAddBtn(addmenu.getAddBtn());
			menu.setIsParent(addmenu.getIsParent());
			menu.setMenuClass(addmenu.getMenuClass());
			menu.setMenuType(addmenu.getMenuType());
			menu.setMenuAttr(addmenu.getMenuAttr());
			menu.setHomeShow(addmenu.getHomeShow());
			if (this.addCustomMenu(menu)) {
				//String newMenuId = getMaxMenuId(menu);
				//menu.setMenuId(newMenuId);  addCustomMenu方法中已经setMenuId了
				ArrayList addList = selectMenuByMenuId(menu);
				if (addList != null) {
					Menu newmenu = (Menu) addList.get(0);
					retMap.put("newMenuId", newmenu.getMenuId());
					retMap.put("newParentId", newmenu.getParentId());
					retMap.put("newMenuLevel", newmenu.getMenuLevel());
					retMap.put("newIsParent", newmenu.getIsParent());
					retMap.put("newEditEnable", newmenu.getEditEnable());
					retMap.put("addResult", "success");
				}
			} else {
				retMap.put("addResult", "fail");
			}
		} else if ("UPDATEMENU".equalsIgnoreCase(oper_type)) {
			ArrayList reqList = (ArrayList) requestBean.getParameterList();
			Menu menubean = (Menu) reqList.get(0);
			menu.setMenuId(menubean.getMenuId());
			menu.setMenuName(menubean.getMenuName());
			menu.setMenuDesc(menubean.getMenuDesc());
			menu.setMenuUrl(menubean.getMenuUrl());
			menu.setAddBtn(menubean.getAddBtn());
			menu.setDelBtn(menubean.getDelBtn());
			menu.setMenuBelong("0");
			menu.setMenuClass(menubean.getMenuClass());
			menu.setMenuType(menubean.getMenuType());
			menu.setMenuAttr(menubean.getMenuAttr());
			menu.setHomeShow(menubean.getHomeShow());
			String updateResult = this.updateCustomMenuWay(menu);
			if ("updateSuccess".equals(updateResult)) {
				retMap.put("updateResult", "success");
			} else if (("updateFail".equals(updateResult))) {
				retMap.put("updateResult", "fail");
			} else if (("noRecode".equals(updateResult))) {
				retMap.put("updateResult", "noRecode");
			}
		} else if ("DELETEPAGE".equalsIgnoreCase(oper_type)) {
			ArrayList reqList = (ArrayList) requestBean.getParameterList();
			Menu menubean = (Menu) reqList.get(0);
			menu.setMenuId(menubean.getMenuId());
			String resultString = this.deleteCustomMenu(menu);
			if ("hasChildren".equals(resultString)) {
				retMap.put("deleteResult", "hasChildren");
			} else if ("success".equals(resultString)) {
				retMap.put("deleteResult", "success");
			} else if ("cantEdit".equals(resultString)) {
				retMap.put("deleteResult", "cantEdit");
			} else if ("basedMenu".equals(resultString)) {
				retMap.put("deleteResult", "basedMenu");
			} else {
				retMap.put("deleteResult", "fail");
			}
		} else if ("UPDATEORDER".equalsIgnoreCase(oper_type)) {
			ArrayList reqList = (ArrayList) requestBean.getParameterList();
			Menu menubean = (Menu) reqList.get(0);
			menu.setMenuId(menubean.getMenuId());
			menu.setMenuOrder(menubean.getMenuOrder());
			if (updateOrder(menu)) {
				retMap.put("orderresult", "success");
				logger.info("成功更新菜单顺序：" + menubean.getMenuOrder());
			} else {
				retMap.put("orderresult", "fail");
			}
		} else if ("QUERYBUTTON".equalsIgnoreCase(oper_type)) {
			ArrayList reqList = (ArrayList) requestBean.getParameterList();
			Menu menubean = (Menu) reqList.get(0);
			menu.setMenuId(menubean.getMenuId());
			ArrayList butlist = this.queryButton(menu);
			retMap.put("butlist", butlist);
			logger.info("成功查询按钮信息");
		}
		retBean.setRetMap(retMap);
		retBean.setRetCode(SunIFErrorMessage.SUCCESS);
		retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}
	
	/** 新增菜单  */
	private boolean addCustomMenu(Menu menu) throws Exception{
		boolean result = false;
		String menuid = getMaxMenuId(menu);
		DecimalFormat df = new DecimalFormat("000");
		if (!menuid.equalsIgnoreCase("")) {
			menuid = menuid.substring(0, 4) + df.format(Integer.parseInt(menuid.substring(4)) + 1);
		} else {
			menuid = "#100001";
		}
		String menuOrder = getMaxMenuOrder(menu);
		if ("1".equalsIgnoreCase(menu.getMenuLevel())) {
			if (menuOrder != null && !menuOrder.equalsIgnoreCase("")) {
				menuOrder = Integer.parseInt(menuOrder) + 1 + "";
			} else {
				menuOrder = "0";
			}
		}
		menu.setMenuId(menuid);
		//menu.setMenuOrder(menuOrder);
		int insertResult = insertCustomMenu(menu);
		if (insertResult > 0) {
			/*if ("#000000".equals(menu.getParentId())) {
				result = true;
			} else {
				int updates = updateParentMenu(menu);
				if (updates > 0) {
					result = true;
				}
			}*/
			String	log="菜单名"+menu.getMenuName()+"在菜单表中被创建！";
			//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM, ARSConstants.OPER_TYPE_1, log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
			result = true;
			String add_btn = menu.getAddBtn();
			if (!add_btn.equalsIgnoreCase("")) {
				result = addButton(menu);
			}
			/*if (result) {
				if (!add_btn.equalsIgnoreCase("")) {
					result = addButton(menu);
				}
			}*/
		}
		return result;
	}

	/** 更新菜单 */
	private String updateCustomMenuWay(Menu menu) {
		String updateFlag = "";
		int count = getMenuIdCount(menu);
		if (count != 1) {
			updateFlag = "noRecode";
		} else {
			boolean result = updateCustomMenu(menu);
			String add_btn = menu.getAddBtn();
			String del_btn = menu.getDelBtn();
			if (result) {
				if (!del_btn.equalsIgnoreCase("")) {
					String[] dellist = del_btn.split(",");
					boolean a = true;
					for (String delresult : dellist) {
						Button button = new Button();
						button.setButtonId(delresult);
						button.setMenuId(menu.getMenuId());
						button.setBankNo(menu.getBankNo());
						button.setProjectNo(menu.getProjectNo());
						button.setSystemNo(menu.getSystemNo());
						boolean d = deleteButton(button);
						if (!d) {
							a = false;
							break;
						}
					}
					if (a) {
						if (!add_btn.equalsIgnoreCase("")) {
							boolean add = addButton(menu);
							if (add) {
								updateFlag = "updateSuccess";
							} else {
								updateFlag = "updateFail";
							}
						} else {
							updateFlag = "updateSuccess";
						}
					} else {
						updateFlag = "updateFail";
					}
				} else {
					if (!add_btn.equalsIgnoreCase("")) {
						boolean add = addButton(menu);
						if (add) {
							updateFlag = "updateSuccess";
						} else {
							updateFlag = "updateFail";
						}
					} else {
						updateFlag = "updateSuccess";
					}
				}
			} else {
				updateFlag = "updateFail";
			}
		}
		return updateFlag;
	}
	
	/** 删除菜单  */
	private String deleteCustomMenu(Menu menu) {
		String deleteFlag = "";
		if (getMenuIdCountByParentId(menu) > 0) {
			deleteFlag = "hasChildren";
		} else {
			if (getEditEnable(menu).equalsIgnoreCase("0")) {
				deleteFlag = "cantEdit";
			} else {
				deleteMenu(menu);
				Right right = new Right();
				right.setMenuId(menu.getMenuId());
				right.setBankNo(menu.getBankNo());
				right.setProjectNo(menu.getProjectNo());
				right.setSystemNo(menu.getSystemNo());
				if (selectMenuIdCount(right) > 0) {
					deleteByMenuId(right);
					boolean del = deleteButtons(menu);
					if (del) {
						deleteFlag = "success";
					} else {
						deleteFlag = "fail";
					}
				} else {
					boolean del = deleteButtons(menu);
					if (del) {
						deleteFlag = "success";
					} else {
						deleteFlag = "fail";
					}
				}
			}
		}
		return deleteFlag;
	}
	
	/** 删除按钮  */
	private boolean deleteButtons(Menu menu) {
		boolean result = true;
		if (buttonMapper.getMenuButtonCount(menu) > 0) {
			Button button = new Button();
			button.setBankNo(menu.getBankNo());
			button.setProjectNo(menu.getProjectNo());
			button.setSystemNo(menu.getSystemNo());
			button.setMenuId(menu.getMenuId());
			result = deleteButton(button);
		}
		return result;
	}
	
	/** 新增按钮  */
	private boolean addButton(Menu menu) {
		String[] addlist = menu.getAddBtn().split(",");
		boolean b = true;
		for (String addresult : addlist) {
			String[] addArr = addresult.split(";");
			int insertresult;
			Button button = new Button();
			button.setButtonId(addArr[0]);
			button.setButtonName(addArr[1]);
			button.setMenuId(menu.getMenuId());
			button.setIsLock(menu.getIsLock());
			button.setIsOpen("1");
			button.setLastModiDate(SunIFCommonUtil.getFomateTimeString("yyyyMMddhhmm"));
			button.setBankNo(menu.getBankNo());
			button.setProjectNo(menu.getProjectNo());
			button.setSystemNo(menu.getSystemNo());
			insertresult = buttonMapper.insertButton(button);
			if (insertresult <= 0) {
				b = false;
				break;
			}
		}
		return b;
	}
	
	@SuppressWarnings({ "rawtypes" })
	private ArrayList queryButton(Menu menu) {
		return buttonMapper.selectButtonByMenuId(menu);
	}
	
	private void deleteByMenuId(Right right) {
		rightMapper.deleteByMenuId(right);
	}

	private Integer selectMenuIdCount(Right right) {
		return rightMapper.selectMenuIdCount(right);
	}

	private void deleteMenu(Menu menu) {
		customMenuMapper.deleteCustomMenu(menu);
		String	log="菜单ID"+menu.getMenuId()+"在菜单表中被删除！";
		//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM, ARSConstants.OPER_TYPE_2, log);
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
	}

	private String getEditEnable(Menu menu) {
		return customMenuMapper.getEditEnable(menu);
	}

	private Integer getMenuIdCountByParentId(Menu menu) {
		menu.setParentId(menu.getMenuId());
		return customMenuMapper.getMenuIdCountByParentId(menu);
	}
	private int updateParentMenu(Menu menu) {
		menu.setIsParent("1");
		menu.setLastModiDate(SunIFCommonUtil.getFomateTimeString("yyyyMMddhhmm"));
		return customMenuMapper.updateParentMenu(menu);
	}

	private int insertCustomMenu(Menu menu) {
		//menu.setIsParent("0");
		menu.setIsOpen("1");
		menu.setLastModiDate(SunIFCommonUtil.getFomateTimeString("yyyyMMddhhmm"));
		menu.setEditEnable("1");
		return customMenuMapper.insertCustomMenu(menu);
	}

	private String getMaxMenuOrder(Menu menu) {
		return customMenuMapper.getMaxMenuOrder(menu);
	}

	private String getMaxMenuId(Menu menu) throws Exception{
		return BaseUtil.filterSqlParam(customMenuMapper.getMaxMenuId(menu));
	}
	
	private boolean deleteButton(Button button) {
		int result = buttonMapper.deleteButton(button);
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	private boolean updateCustomMenu(Menu menu) {
		menu.setLastModiDate(SunIFCommonUtil.getFomateTimeString("yyyyMMddhhmm"));
		int result = customMenuMapper.updateCustomMenu(menu);
		if (result > 0) {
			String	log="菜单名"+menu.getMenuName()+"在菜单表中被修改！";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
			return true;
		} else {
			return false;
		}
	}

	private Integer getMenuIdCount(Menu menu) {
		return customMenuMapper.getMenuIdCount(menu);
	}
	
	@SuppressWarnings("rawtypes")
	private ArrayList selectMenuByMenuId(Menu menu) {
		return customMenuMapper.selectMenuByMenuId(menu);
	}

	private boolean updateOrder(Menu menu) {
		customMenuMapper.updateMenuOreder(menu);
		String	log="菜单ID"+menu.getMenuId()+"在菜单表中被更新排序！";
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
		return true;
	}

	@SuppressWarnings("rawtypes")
	private ArrayList queryCustomMenu(Menu menu) {
		menu.setIsOpen(SunIFConstant.USE);
		return customMenuMapper.selectCustomMenu(menu);
	}
}
