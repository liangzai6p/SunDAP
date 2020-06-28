package com.sunyard.ars.system.service.impl.busm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.busm.RoleBean;
import com.sunyard.ars.system.dao.busm.SmRoleTableTbMapper;
import com.sunyard.ars.system.dao.busm.SmUserModelMapper;
import com.sunyard.ars.system.dao.busm.SysRoleDao;
import com.sunyard.ars.system.service.busm.IRoleService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.dao.ButtonMapper;
import com.sunyard.cop.IF.mybatis.pojo.Button;
import com.sunyard.cop.IF.mybatis.pojo.Right;
import com.sunyard.cop.IF.mybatis.pojo.User;

/**
 * @author:		 wwx
 * @date:		 2017年12月19日 下午5:14:35
 * @description: (IRoleService实现类)
 */
@Service("roleService")
@Transactional
public class RoleServiceImpl extends BaseService implements IRoleService {


	// 数据库接口
	@Resource
	private SysRoleDao sysroleDao;
	
	@Resource
	private  ButtonMapper   buttonMapper;
	
	@Resource
	private  SmRoleTableTbMapper SmRoleTableTbMapper;
	
	@Autowired
	private SmUserModelMapper smUserModelMapper;
	
	
	@Resource
	private com.sunyard.cop.IF.mybatis.dao.RightMapper   rightMapper;
	/**
	 * @author:		 wwx
	 * @date:		 2017年12月19日 下午5:15:01
	 * @description: (执行接口逻辑)
	 */
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	/**
	 * @author:		 wwx
	 * @date:		 2017年12月19日 下午5:15:14
	 * @description: (执行具体操作：增、删、改、查 等，操作成功后记录日志信息)
	 */
	@Override
	@SuppressWarnings({ "rawtypes" })
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		// 获取操作标识
		String oper_type = (String) sysMap.get("oper_type");
		if (ARSConstants.OPERATE_QUERY.equals(oper_type)) {
			// 查询
			queryOperation(requestBean, responseBean);
			return;
		}if (ARSConstants.OPERATE_ADD.equals(oper_type)) { 
			// 新增
			addOperation(requestBean, responseBean);
			return;
		}
		if (ARSConstants.OPERATE_MODIFY.equals(oper_type)) { 
			// 修改
			modifyOperation(requestBean, responseBean);
			return;
		}
		if(ARSConstants.OPERATE_DELETE.equals(oper_type)) { 
			// 删除
			deleteOperation(requestBean, responseBean);
			return;
		}if (("selectByRoleNo").equals(oper_type)) {
			selectByRoleNo(requestBean, responseBean);
			return;
		}if (("selectAllByRoleNo").equals(oper_type)) {
			selectAllByRoleNo(requestBean, responseBean);
			return;
		}
		if (("getMenuByroleNo").equals(oper_type)) {
			getMenuByroleNo(requestBean, responseBean);
			return;
		}
		if ("getTablesByRoleNo".equals(oper_type)) {
			getTablesByRoleNo(requestBean, responseBean);
		}
		
	}
	
	private void getTablesByRoleNo(RequestBean requestBean, ResponseBean responseBean) {
		RoleBean role = (RoleBean) requestBean.getParameterList().get(0);
		String roleNo=role.getRole_no();
		List<Map<String,Object>> list = SmRoleTableTbMapper.getTablesByRoleNo(roleNo);
		Map retMap = new HashMap();
		retMap.put("returnlist", list);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
	}

	private void getMenuByroleNo(RequestBean requestBean, ResponseBean responseBean) {
		RoleBean role = (RoleBean) requestBean.getParameterList().get(0);
		List<Map<String, Object>> list = rightMapper.getMenuByroleNo(role.getRole_no());
		List<Map<String,Object>> list2 = rightMapper.getButtonByroleNo(role.getRole_no());
		List<Map<String,Object>> list3=new ArrayList<Map<String,Object>>();
		String  MENU_ID="";
		String  BUTTON_ID="";
		List<Map<String,Object>> list4=new ArrayList<Map<String,Object>>();
		list4.addAll(list);//保存个人菜单
		for (int i = 0; i < list2.size(); i++) {
			MENU_ID = list2.get(i).get("MENU_ID").toString();
			BUTTON_ID =list2.get(i).get("BUTTON_ID").toString();
			String[] split = BUTTON_ID.split(",");
			for (int j = 0; j < split.length; j++) {
				Button mdoel =new Button();
				mdoel.setMenuId(MENU_ID);
				mdoel.setButtonId(split[j]);
				String button_name = buttonMapper.select(mdoel);
				Map<String, Object> e= new HashMap<String, Object>();
				e.put("ID", split[j]);
				e.put("NAME", split[j]+"_"+button_name+"_"+MENU_ID);
				e.put("PID", MENU_ID);
				list3.add(e);//返回的是当前人的在各个菜单下拥有的按钮
				list.add(e);
			}
		}
		Map retMap = new HashMap();
		retMap.put("returnlist", list);
		retMap.put("PersonMenuList", list4);
		retMap.put("buttonlist", list3);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	//查询一条记录信息！
	private void selectAllByRoleNo(RequestBean requestBean, ResponseBean responseBean) {
		RoleBean role = (RoleBean) requestBean.getParameterList().get(0);
		if(BaseUtil.isBlank(role.getRole_no())){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("ID失效查询失败");
		}
		RoleBean bean = sysroleDao.selectAllByRoleNo(role.getRole_no());
		Map retMap = new HashMap();
		retMap.put("returnlist", bean);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * @author:		 wwx
	 * @date:		 2017年12月19日 下午5:38:50
	 * @description: (查询角色信息操作)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		String role_no = (String) sysMap.get("role_no");
		String is_open = (String) sysMap.get("is_open");
		String role_mode = (String) sysMap.get("role_mode");
		String role_level = (String) sysMap.get("role_level");
		List<String> levelList = null;
		if(!BaseUtil.isBlank(role_level)){
			levelList = Arrays.asList(role_level.split(","));
		}
		//改变查询条件！只是查询菜单类角色
		role_mode="1";
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("role_no", role_no);
		condMap.put("is_open", is_open);
		condMap.put("role_mode", role_mode);
		condMap.put("levelList", levelList);
		int currentPage = (int)sysMap.get("currentPage");
		int pageSize = 0;
		if(currentPage != -1) {
			Integer initPageSize = (Integer) sysMap.get("pageSize");
			if (initPageSize == null) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageSize;
			}
		}
		
		// 定义分页操作，pageSize = 0 时查询全部数据（不分页），pageNum <= 0 时查询第一页，pageNum > pages（超过总数时），查询最后一页
		Page page = PageHelper.startPage(currentPage, pageSize);
		// 查询分页记录
		List list = getList(sysroleDao.select(condMap), page);
		// 获取总记录数
		long totalCount = page.getTotal();
		
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("pageNum", currentPage);
		retMap.put("pageSize", pageSize);
		retMap.put("totalNum", totalCount);
		retMap.put("roles", list);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * @author:		 wwx
	 * @date:		 2017年12月19日 下午5:43:56
	 * @description: (新增角色操作)
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void addOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		
		// 获取新增数据
		RoleBean role = (RoleBean) requestBean.getParameterList().get(0);
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("role", role);
		condMap = addExtraCondition(condMap);
		
		// 判断是否已存在
		int roleNum = sysroleDao.selectRole(condMap);
		if (roleNum > 0) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("角色号已存在");
			setReturnInfo(responseBean);
			return;
		} else {
			// 新增数据
			sysroleDao.insert(condMap);
			String	log="角色名称"+role.getRole_name()+"在角色表中被创建！";
			//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM, ARSConstants.OPER_TYPE_1, log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
			// 记录操作日志
			/*
			 * String logContent = "新增角色信息，||{角色号：" + role.getRole_no() + "，角色名称：" +
			 * role.getRole_name() + "，角色类别：" + role.getRole_mode() + "，启用标志：" +
			 * role.getIs_open() + "}"; addLogInfo(logContent);
			 */
			
			// 拼装返回信息
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("新增成功");
		}
	}

	/**
	 * @author:		 wwx
	 * @date:		 2017年12月20日 上午9:37:24
	 * @description: (修改角色信息操作)
	 */
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	private void modifyOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		
		// 获取修改数据
		RoleBean role = (RoleBean) requestBean.getParameterList().get(0);
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("role", role);
		condMap = addExtraCondition(condMap);
		// 修改数据
		sysroleDao.update(condMap);
		String	log="角色名称"+role.getRole_name()+"在角色表中被修改！";
		//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM, ARSConstants.OPER_TYPE_3, log);
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
		// 记录操作日志
		/*
		 * String logContent = "，||修改后：{角色号：" + role.getRole_no() + " ，角色名称：" +
		 * role.getRole_name() + " ，角色类别：" + role.getRole_mode() + " ，启用标志：" +
		 * role.getIs_open() +"}"; addLogInfo(logContent);
		 */
		// 拼装返回信息
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功");
	}

	/**
	 * @author:		 wwx
	 * @date:		 2017年12月20日 上午10:32:07
	 * @description: (删除角色操作)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void deleteOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取删除数据
		List del_nos = requestBean.getParameterList();
		
		// 构造条件参数
		int len = del_nos.size();
		String[] roleinfo = new String[len];
		for (int i = 0;i < len;i++) {
			RoleBean n = (RoleBean) del_nos.get(i);
			roleinfo[i] = n.getRole_no();
		}
		HashMap condMap = new HashMap();
		condMap.put("role_nos", roleinfo);
		condMap = addExtraCondition(condMap);
		try {
			for (int i = 0; i < roleinfo.length; i++) {
				rightMapper.deleteByRoleNo(roleinfo[i]);
				smUserModelMapper.deleteByRoleNo(roleinfo[i]);
				SmRoleTableTbMapper.deleteByRoleNo(roleinfo[i]);
			}
		} catch (Exception e) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("删除角色关联菜单时失败! 但角色已经删除");
		}
		
		// 删除角色信息
		sysroleDao.delete(condMap);
		String	log="角色ID"+roleinfo+"在角色表中被删除！";
		//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM, ARSConstants.OPER_TYPE_2, log);
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
		
		
		// 记录操作日志
		/*
		 * for (int i = 0;i < del_nos.size();i++) { RoleBean roleInfo = (RoleBean)
		 * del_nos.get(i); String logContent = "删除角色信息，||{角色号："+ roleInfo.getRole_no() +
		 * "，角色名：" + roleInfo.getRole_name() +"}"; addLogInfo(logContent); }
		 */
		
		// 拼装返回信息
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void selectByRoleNo(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		HashMap<String, Object> parameterMap = new HashMap<String, Object>();
		String roleNo = (String) requestBean.getSysMap().get("roleNo");
		User user = BaseUtil.getLoginUser();
		parameterMap.put("roleNo", roleNo);
		parameterMap.put("bankNo", user.getBankNo());
		parameterMap.put("projectNo", user.getProjectNo());
		parameterMap.put("systemNo", user.getSystemNo());
		// 查询分页记录
		String roleMode = sysroleDao.selectByRoleNo(parameterMap);
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("roleMode", roleMode);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
}