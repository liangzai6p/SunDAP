package com.sunyard.ars.system.service.impl.busm;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.sunyard.ars.common.util.SqlUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.HttpUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.pojo.Dictionary;
import com.sunyard.ars.system.bean.busm.SmUserBusiTbBean;
import com.sunyard.ars.system.bean.busm.UserBean;
import com.sunyard.ars.system.bean.busm.UserOrgan;
import com.sunyard.ars.system.bean.busm.UserRole;
import com.sunyard.ars.system.dao.busm.OrganInfoDao;
import com.sunyard.ars.system.dao.busm.SmUserBusiTbMapper;
import com.sunyard.ars.system.dao.busm.UserDao;
import com.sunyard.ars.system.dao.busm.UserOrganMapper;
import com.sunyard.ars.system.dao.busm.UserRoleMapper;
import com.sunyard.ars.system.dao.sc.DictionaryMapper;
import com.sunyard.ars.system.dao.sc.SystemParameterMapper;
import com.sunyard.ars.system.service.busm.IUserService;
import com.sunyard.cop.IF.bean.OrganZTreeBean;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFCommonUtil;
import com.sunyard.cop.IF.common.SunIFConstant;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.dao.RoleMapper;
import com.sunyard.cop.IF.mybatis.pojo.User;

import net.sf.json.JSONObject;


/**
 * @author:		zgz
 * @date:		2018年5月
 * @Description:(用户信息Service实现类)
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends BaseService implements IUserService {

	// 数据库接口
	@Resource
	private UserDao userDao;
	
	
	@Resource
	private UserOrganMapper userOrganMapper;
	
	@Resource
	private SystemParameterMapper systemParameterMapper;
	
	
	@Resource
	private OrganInfoDao organInfoDao;
	
	@Resource
	private  UserRoleMapper  userRoleMapper;
	
	@Resource
	private  SmUserBusiTbMapper  smUserBusiTbMapper;
	
	@Resource
	private  RoleMapper  roleMapper;
	
	@Resource
	private  DictionaryMapper  dictionaryMapper;  
	
	/**
	 * @author:		zgz
	 * @date:		2018年5月
	 * @Description:(执行接口逻辑)
	 */
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	/**
	 * @author:		zgz
	 * @date:		2018年5月
	 * @Description:(执行具体操作：增、删、改、查 等，操作成功后记录日志信息)
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
			query(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_ADD.equals(oper_type)) { 
			// 新增
			add(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_MODIFY.equals(oper_type)) { 
			// 修改
			update(requestBean, responseBean);
		} else if(ARSConstants.OPERATE_DELETE.equals(oper_type)) { 
			// 删除
			delete(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_OTHER.equals(oper_type)) {
			// 重置密码
			otherOperation(requestBean, responseBean);
		}else if("user_priv_organ".equals(oper_type)){
			//用户可用权限机构
			getUserPrivOrganList(requestBean, responseBean);
		}else if("user_have_organ".contentEquals(oper_type)) {
			//用于已有权限机构
			getUserPrivOrgans(requestBean, responseBean);
		}else if("systemSignBack".contentEquals(oper_type)) {
			//系统签退
			systemSignBack(requestBean, responseBean);
		}else if("grant".equalsIgnoreCase(oper_type)){//授权方法
			grant(requestBean, responseBean);
		}else if("selectUserList".equalsIgnoreCase(oper_type)){//抄送查询用户
			selectUserList(requestBean, responseBean);
		}else if ("getRole".equals(oper_type)) {
			getRole(requestBean,responseBean);
		}else if ("getBusiRange".equals(oper_type)) {
			getBusiRange(requestBean,responseBean);
		}else if ("getRolesByUserNo".equals(oper_type)) {
			getRolesByUserNo(requestBean,responseBean);
		}
		else if ("getBusiByUserNo".equals(oper_type)) {
			getBusiByUserNo(requestBean,responseBean);
		}else if("getprivOgranOfMine".equals(oper_type)){
			//获取当前人的权限机构! 
			getprivOgranOfMine(requestBean,responseBean);
		}else if("getOrganByorganLevel".equals(oper_type)){
			getOrganByorganLevel(requestBean,responseBean);
		}else if("getChildByOrganNo".equals(oper_type)){
			getChildByOrganNo(requestBean,responseBean);
		}else if("check_all_user_priv_organ".contentEquals(oper_type)) {
			//判断当前用户权限机构是否可用
			checkAllUserPrivOrganCount(requestBean, responseBean);
									 
		}else if("getUsersToApply".equals(oper_type)){
			//查询用户树
			getUsersToApply(requestBean, responseBean);
			
		}
		
	}
	
	
	private void getChildByOrganNo(RequestBean requestBean, ResponseBean responseBean) {
		Map sysMap = requestBean.getSysMap();
		String parentOrganNo = sysMap.get("parentOrganNo").toString();
		List<HashMap<String,Object>> list = organInfoDao.getChildByOrganNo(parentOrganNo);//
		Map map = new HashMap();
		map.put("returnList", list);	
		responseBean.setRetMap(map);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
	}

	private void getOrganByorganLevel(RequestBean requestBean, ResponseBean responseBean){
		Map sysMap = requestBean.getSysMap();
		String organLevel = sysMap.get("organLevel").toString();
		String organ_no = sysMap.get("organ_no").toString();
		List<String> organlevelList=new  ArrayList<String>();
		List<HashMap<String,Object>> allOrgans=null;
		organlevelList.add("2");
		organlevelList.add("3");
		if (organLevel.equals("1")) {
			//查询部门的所有分行
			organlevelList.add("1");
			organlevelList.add("4");
			allOrgans = organInfoDao.selectAllOrgans(organlevelList);
		}else {
			//查询当前人所在行的机构？？？？？？？？？？？？？？？？？
			allOrgans=organInfoDao.getChildByOrganNo(organ_no);//	getChildOrganList
		}
		System.out.println("部门级别"+organLevel);
		System.out.println("分行数据大小"+allOrgans.size());
		Map map = new HashMap();
		map.put("organList", allOrgans);	
		responseBean.setRetMap(map);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	private void getprivOgranOfMine(RequestBean requestBean, ResponseBean responseBean) {
		Map sysMap = requestBean.getSysMap();
		String userNo = (String)sysMap.get("userNo");
		List<HashMap<String,Object>> privList = null;
		if(userNo != null && userNo.length()>0) {
			privList = organInfoDao.getprivOgranOfMine(userNo,(String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//
		}
		Map map = new HashMap();
		map.put("privOrganList", privList);	//已有权限机构
		responseBean.setRetMap(map);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");

	}
	
	private void getBusiByUserNo(RequestBean requestBean, ResponseBean responseBean) {
		UserBean user = (UserBean) requestBean.getParameterList().get(0);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userNo", user.getUserNo());
		List<Map<String,Object>> list = smUserBusiTbMapper.select(map);
		System.out.println("XYZ:"+list);
		Map retmap = new HashMap();
		retmap.put("list", list);	
		responseBean.setRetMap(retmap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	//查询角色根据用户！
	private void getRolesByUserNo(RequestBean requestBean, ResponseBean responseBean) {
		UserBean user = (UserBean) requestBean.getParameterList().get(0);
		System.out.println("XYZ:"+user.getUserNo());
		List<UserRole> list = userRoleMapper.getRolesByUserNo(user.getUserNo());
		Map map = new HashMap();
		map.put("list", list);	
		responseBean.setRetMap(map);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	private void getBusiRange(RequestBean requestBean, ResponseBean responseBean) {
		List<Map<String,Object>> busiRange = dictionaryMapper.getBusiRange();
		Map map = new HashMap();
		map.put("BusiRangeList", busiRange);	
		responseBean.setRetMap(map);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	//获取所有的角色
	private void getRole(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		Map sysMap = requestBean.getSysMap();
		List<Map<String, Object>>  roleList = null;
		List<String> rolelevelList=null;
		if (sysMap.get("is_user")!=null) {
			Dictionary roleDic = new Dictionary();
			roleDic.setFieldName("ROLE_LEVEL");
			List<Dictionary> dictionarys = dictionaryMapper.selectBySelective(roleDic);
			rolelevelList=new ArrayList<String>();
			if(dictionarys != null && dictionarys.size() > 0){
				int organLevel = Integer.parseInt(sysMap.get("user_organ_no").toString());
				for (Dictionary dictionary : dictionarys) {
					if(Integer.parseInt(dictionary.getCode()) >= organLevel){
						rolelevelList.add(BaseUtil.filterSqlParam(dictionary.getCode()));
					}
				}
			}
			roleList = roleMapper.selectRoleByRoleMode(rolelevelList);//如果是总行查询的是全部分级角色！
		}else {
			roleList = roleMapper.selectRoleByRoleMode(rolelevelList);
		}
		
		//for(int i=0;i<3;i++){
		for(int i=0;i<2;i++){
			Map<String , Object>  map= new HashMap<String, Object>();
			if (i==0) {
				map.put("id", 1);
				map.put("name", "菜单角色");
				map.put("open", true);
			}else if(i==1) {
				map.put("id", 2);
				map.put("name", "模型角色");
			}/*else if(i==2) {
				map.put("id", 3);
				map.put("name", "业务表角色");
			}*/
			map.put("pId", "0");
			map.put("nocheck", true);
 			roleList.add(map);
		}
		Map map = new HashMap();
		map.put("roleList", roleList);	
		responseBean.setRetMap(map);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	private void grant(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		UserBean user = (UserBean) requestBean.getParameterList().get(0);
		int checkUser = userDao.checkUser(user.getUserNo(),user.getPassword());
		if(checkUser == 0){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("用户密码错误!");
			return;
		}
		int checkGrantUser = userDao.checkGrantUser(user.getUserNo(),BaseUtil.getLoginUser().getOrganNo());
		if(checkGrantUser == 0){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("该用户无法授权操作!");
			return;
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}


	@SuppressWarnings("rawtypes")
	@Override
	public void add(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		List <Map<String, Object>> parameterList = new ArrayList();
		UserBean user = (UserBean) requestBean.getParameterList().get(0);
		
		UserBean oldUser = userDao.selectUserInfo(user.getUserNo());
		if(null != oldUser){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("用户号已存在,请重新输入!");
			return ;
		}
		
		String lastModiDate = BaseUtil.getCurrentTimeStr();
		user.setLastModiDate(lastModiDate);
		UserRole recordTest=new UserRole();
		recordTest.setUserNo(user.getUserNo());
		
		//添加用户起前盘判断当前用户是否存在角色号  应该是不存的!因为是新增! 处理问题：删除用户后又添加导致角色表有此用户的数据！新添加出现问题
		List<UserRole> selectiveList = userRoleMapper.selectBySelective(recordTest,(String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));
		if (selectiveList.size()>0){
			userRoleMapper.deleteRolesByUserNo(user.getUserNo());
		}
		int addFlag=userDao.insertSelective(user);

		//多选！
		String UserRoleId = sysMap.get("UserRoleId").toString();  
		System.out.println("角色集合ID:"+UserRoleId);
		//封装：sm_user_role_tb
		String[] UserRoleSplit = UserRoleId.split(",");
		for (int i = 0; i < UserRoleSplit.length; i++) {
			UserRole record = new UserRole();
			record.setOrganNo(user.getOrganNo());
			record.setUserNo(user.getUserNo());
			record.setRoleNo(UserRoleSplit[i]);
			record.setIsOpen("1");
			record.setBankNo("SUNYARD");
			record.setSystemNo("AOS");
			record.setProjectNo("AOS");
			userRoleMapper.insertSelective(record );
		}
		//封装业务ID
		String UserBusiId = sysMap.get("UserBusiId").toString();
		System.out.println("业务范围ID:"+UserBusiId);
		String[] UserBusiIdSplit = UserBusiId.split(",");
		for (int i = 0; i < UserBusiIdSplit.length; i++) {
			SmUserBusiTbBean obj= new  SmUserBusiTbBean();
			obj.setUserNo(user.getUserNo());
			obj.setBusiNo(UserBusiIdSplit[i]);
			smUserBusiTbMapper.save(obj);
		}
		//封装：sm_user_organ_tb
		//自己添加成功的前提下：
		String privOrgan = (String)sysMap.get("privOrgan");
		if(privOrgan != null && privOrgan.trim().length()>0) {
			String[] privOrganList = privOrgan.split(",");
			for(String organ : privOrganList) {
				UserOrgan userOrgan = new UserOrgan();
				userOrgan.setUserNo(user.getUserNo());
				userOrgan.setOrganNo(organ);
				userOrganMapper.insert(userOrgan);
			}
		}
		// 添加日志信息
		String logContent = "新增用户信息，||{用户号：" + user.getUserNo() + "}";
		//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM,ARSConstants.OPER_TYPE_1,logContent);
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT,ARSConstants.OPER_TYPE_1,logContent);
        if(addFlag>0){
            Map retMap = new HashMap();
            retMap.put("lastModiDate", lastModiDate);
            responseBean.setRetMap(retMap);
            responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
            responseBean.setRetMsg("添加成功");
        }
		// 本地完成后请求三方接口：externalUserSyn.do  192.168.1.101
		/*String IP="";
		String port="";
		SysParameter IPObject = systemParameterMapper.selectByPrimaryKey("IP", "SUNYARD", "AOS", "AOS");
		SysParameter PortObject=systemParameterMapper.selectByPrimaryKey("PORT","SUNYARD", "AOS", "AOS");
		if(IPObject!=null){
			IP  =IPObject.getParamValue().toString();
		}else{
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("添加用户成功,向运管平台同步失败：参数未配置");
			return ;
		}
		if(PortObject!=null){
			port =PortObject.getParamValue().toString();
		}else{
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("添加用户成功,向运管平台同步失败：参数未配置");
			return ;
		String url="http://"+IP+":"+port+"/externalUserSyn.do";
		}*/
		/*String url = systemParameterMapper.selectByPrimaryKey("SUNAOSURL", "SUNYARD", "AOS", "AOS").getParamValue().toString();
		//获取的是运管系统的编号：1001
		String external_system_no = systemParameterMapper.selectByPrimaryKey("ExternalSystemNo", "SUNYARD", "AOS", "AOS").getParamValue().toString();
		System.out.println("用户同步："+url);
		String msg="";
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("user_no", user.getUserNo());
		parameterMap.put("user_name", user.getUserName());
		parameterMap.put("external_system_no", external_system_no);
		parameterMap.put("password", user.getPassword());
		parameterMap.put("organ_no", user.getOrganNo());
		parameterList.add(parameterMap);
		
		Map<String, Object> sys_Map = new HashMap<String, Object>();
		sys_Map.put("oper_type", "addUserSyn");
		
		Map<String, Object> msgMap = new HashMap<String, Object>();
		msgMap.put("parameterList", parameterList);
		msgMap.put("sysMap", sys_Map);
		msg = new ObjectMapper().writeValueAsString(msgMap);
		String response=null;
		try {
			//请求服务！
			response = HttpUtil.sendPost(url, "message=" + URLEncoder.encode(msg, "utf-8"));
			JSONObject jsonObject = JSONObject.fromObject(response);
			ResponseBean bean = (ResponseBean)JSONObject.toBean(jsonObject, ResponseBean.class);
			if("0".equals(bean.getRetCode())){
				Map<Object, Object> retMap = new HashMap<Object, Object>();
				retMap.put("lastModiDate", lastModiDate);
				responseBean.setRetMap(retMap);
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
				responseBean.setRetMsg("添加成功");
			}else{
				//失败！！！
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("本地添加成功,调度其他服务出现异常(外系统添加失败异常)，请联系管理员");
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("本地添加成功,调度其他服务出现异常，请联系管理员");
		}*/
	}
	@SuppressWarnings("rawtypes")
	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		List userList = requestBean.getParameterList();
		if(userList != null && userList.size()>0) {
			User loginUser = BaseUtil.getLoginUser();
			for (int i = 0; i < userList.size(); i++) {
				UserBean user = (UserBean)userList.get(i);
				userDao.deleteByPrimaryKey(user.getUserNo(), loginUser.getBankNo(), loginUser.getSystemNo(), loginUser.getProjectNo());
				userOrganMapper.deleteByUserNo(user.getUserNo());
				String	log="用户ID"+user.getUserNo()+"在用户表中被删除！";
				//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM, ARSConstants.OPER_TYPE_2, log);
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
			}
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功");
	}

	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		UserBean user = (UserBean) requestBean.getParameterList().get(0);
		String lastModiDate = BaseUtil.getCurrentTimeStr();
		user.setLastModiDate(lastModiDate);
		User loginUser = BaseUtil.getLoginUser();
		user.setBankNo(loginUser.getBankNo());
		user.setSystemNo(loginUser.getSystemNo());
		user.setProjectNo(loginUser.getProjectNo());
		userDao.updateByPrimaryKeySelective(user);
		userOrganMapper.deleteByUserNo(user.getUserNo());
		String privOrgan = (String)sysMap.get("privOrgan");
		if(privOrgan != null && privOrgan.trim().length()>0) {
			String[] privOrganList = privOrgan.split(",");
			for(String organ : privOrganList) {
				UserOrgan userOrgan = new UserOrgan();
				userOrgan.setUserNo(user.getUserNo());
				userOrgan.setOrganNo(organ);
				userOrganMapper.insert(userOrgan);
			}
		}
		//要移除的角色ID
		String  userNo=user.getUserNo();
		userRoleMapper.deleteRolesByUserNo(userNo);
		//要添加的角色ID
		String roleIds =sysMap.get("roleIds").toString();
		if (!BaseUtil.isBlank(roleIds)) {
			String roleIdsArr[]=roleIds.split(",");
			for (int i = 0; i < roleIdsArr.length; i++) {
				UserRole record=new UserRole();
				record.setOrganNo(user.getOrganNo());
				record.setUserNo(user.getUserNo());
				record.setRoleNo(roleIdsArr[i]);
				record.setLastModiDate(lastModiDate);
				record.setIsOpen("1");
				record.setBankNo("SUNYARD");
				record.setSystemNo("AOS");
				record.setProjectNo("AOS");
				userRoleMapper.insertSelective(record);
			}
		}
		//要移除的业务ID
		smUserBusiTbMapper.deleteBusNoByUserno(userNo);
		//要添加的业务ID
		String userBusi_id=sysMap.get("userBusi_id").toString();
		if(!BaseUtil.isBlank(userBusi_id)){
			String []userBusi_idArr= userBusi_id.split(",");
			for (int i = 0; i < userBusi_idArr.length; i++) {
				SmUserBusiTbBean model= new SmUserBusiTbBean();
				model.setUserNo(user.getUserNo());
				model.setBusiNo(userBusi_idArr[i]);
				smUserBusiTbMapper.save(model);
			}
		}
		
		Map<Object, Object> retMap = new HashMap<Object, Object>();
		retMap.put("lastModiDate", lastModiDate);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功");
		// 添加日志信息
		String logContent = "修改用户信息，||{用户号：" + user.getUserNo() + "}";
		//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM,ARSConstants.OPER_TYPE_3,logContent);
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT,ARSConstants.OPER_TYPE_3,logContent);
	}

	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		UserBean user = (UserBean) requestBean.getParameterList().get(0);
		user.setBankNo(BaseUtil.getLoginUser().getUserNo());//传操作用户到无用字段
		
		int currentPage = (int)sysMap.get("currentPage");
		int pageSize = 0;
		if(currentPage != -1) {
			Integer initPageSize = (Integer) sysMap.get("user_pageNum");
			if (initPageSize == null) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageSize;
			}
		}
		// 定义分页操作
		Page page = PageHelper.startPage(currentPage, pageSize);
		// 查询分页记录
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("Bean", user);
		map.put("RoleNo",sysMap.get("roleNo"));
		map.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
		//设置回显 
		List<UserBean> list = userDao.selectBySelective(map);
		//
		String str="";
		for(int  i=0;i<list.size();i++){
			List<Map<String,Object>> list2 = roleMapper.selectRoleNameByUserNoAndModel(BaseUtil.filterSqlParam(list.get(i).getUserNo()));
			for (int j = 0; j < list2.size(); j++) {
				 str += list2.get(j).get("ROLE_NAME").toString()+",";
			}
			//超级系统管理员,一级分行系统管理员,
			//list.get(i).setRoleName(str.replace(",", ""));
			if(!BaseUtil.isBlank(str)) {
				list.get(i).setRoleName(str.substring(0, str.length()-1));
			}else {
				list.get(i).setRoleName(str.replace(",", ""));
			}
			str="";
		}
		// 获取总记录数
		long totalCount = page.getTotal();
		
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("currentPage", currentPage);
		retMap.put("pageNum", pageSize);
		retMap.put("totalNum", totalCount);
		retMap.put("returnList", list);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	@Override
	public void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		String userNoListStr = (String)sysMap.get("userNoList");
		String password = (String)sysMap.get("passWord");
		List<String> userNoList = Arrays.asList(userNoListStr.split(","));  // 删除用户信息list
		if(userNoList != null && userNoList.size()>0) {
			userDao.resetPasswordByUserNoList(userNoList, password);
			//添加日志信息
			User loginUser = BaseUtil.getLoginUser();
			for(String userForgetPass : userNoList) {
				String log = loginUser.getUserNo() + " - " +loginUser.getUserName() + "给用户" + userForgetPass
						+ "重置密码为" + password;
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
			}
			
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("密码重置成功");
	}
	
	/**
	 * 根据用户获取可在tree中显示的用户权限机构列表。
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getUserPrivOrganList(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map sysMap = requestBean.getSysMap();
		String belongOrganNo = (String)sysMap.get("belongOrganNo");
		String userPriv = (String)sysMap.get("userPriv");
		List tempList = null;
		if(belongOrganNo != null && belongOrganNo.length()>0) { //有指定上级机构的查询指定所属机构列表
			if("1".equals(userPriv)){//过滤用户权限机构
				tempList = organInfoDao.getUserPrivChildOrganList(belongOrganNo,BaseUtil.getLoginUser().getUserNo());	
				if(tempList.size() == 0 ||tempList == null) {
					tempList = organInfoDao.getChildOrganList(belongOrganNo,ARSConstants.DB_TYPE);//当权限机构为空(未配置权限机构)的时候那么就直接查找所属机构的子机构
				}
			}else{
				tempList = organInfoDao.getChildOrganList(belongOrganNo,ARSConstants.DB_TYPE);	//
			}
		}else {
			User user = BaseUtil.getLoginUser();
			tempList = organInfoDao.getUserPrivOrganList(user.getUserNo(),(String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));
			if(tempList.size() == 0 ||tempList == null) {
				tempList = organInfoDao.getChildOrganList(user.getOrganNo(),ARSConstants.DB_TYPE);//当权限机构为空(未配置权限机构)的时候那么就直接查找所属机构的子机构
			}
		}
		ArrayList list = new ArrayList();
		for (Object o : tempList) {
			HashMap maps = (HashMap) o;
			OrganZTreeBean oztBean = new OrganZTreeBean();
			for (Object obj : maps.keySet()) {
				String key = obj + "";
				String value = maps.get(key) + "";
				if (key.equalsIgnoreCase("organ_count")) {
					if ("".equals(value) || "null".equals(value)) {
						oztBean.setIsParent("false");
					} else {
						oztBean.setIsParent("true");
					}
				}
				// oztBean.setIcon("groupIcon");
				if (key.equalsIgnoreCase("organ_no")) {
					oztBean.setId(value.trim());
				} else if (key.equalsIgnoreCase("organ_name")) {
					oztBean.setName(value.trim());
				} else if (key.equalsIgnoreCase("parent_organ")) {
					oztBean.setpId(value.trim());
				} else if (key.equalsIgnoreCase("organ_level")) {
					if ("1".equals(value.trim())) {
						oztBean.setOpen("true");
					} else {
						oztBean.setOpen("false");
					}
					//oztBean.setOpen("true");
					// 临时存储 机构级别
					oztBean.setReserve(value);
				}
			}
			oztBean.setIcon("");
			list.add(oztBean);
		}
		Map map = new HashMap();
		map.put("returnList", list); //可选权限机构
		responseBean.setRetMap(map);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 获取用户已有权限机构
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getUserPrivOrgans(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map sysMap = requestBean.getSysMap();
		String userNo = (String)sysMap.get("userNo");
		List<String> privList = null;
		if(userNo != null && userNo.length()>0) {
			privList = userOrganMapper.getPrivOrganByUserNo(userNo,(String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));
		}
		Map map = new HashMap();
		map.put("privOrganList", privList);	//已有权限机构
		responseBean.setRetMap(map);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 系统签退
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void systemSignBack(RequestBean requestBean, ResponseBean responseBean) throws Exception {
	    // 获取登录用户信息
	    User userLogin = BaseUtil.getLoginUser();
		UserBean user = new UserBean();
		user.setUserNo(userLogin.getUserNo());
		user.setBankNo(userLogin.getBankNo());
		user.setSystemNo(userLogin.getSystemNo());
		user.setProjectNo(userLogin.getProjectNo());
		//设置登出时间
		user.setLastLogoutTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		//1-已登陆   0-未登陆
		//设置登录状态为0
		user.setLoginState("0");
		userDao.updateByPrimaryKeySelective(user);
		
		//在删除session中用户信息前添加日志
		String log = "系统签退，签退IP:"+SunIFCommonUtil.getIpAddr(RequestUtil.getRequest());
		addOperLogInfo(ARSConstants.MODEL_NAME_ARS, ARSConstants.OPER_TYPE_6, log);
		
		HttpSession session = RequestUtil.getRequest().getSession();
	    session.removeAttribute("user");
	    // 拼装返回信息
	 	Map retMap = new HashMap();
	 	retMap.put("sunaosURL", SunIFConstant.PARAM_MAP.get("SunAOS_LOAD_URL"));
	    responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("系统签退成功");
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void selectUserList(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		UserBean user = (UserBean) requestBean.getParameterList().get(0);
		List<String> organNoArr = new ArrayList<>();
		List<String> roleNoArr = new ArrayList<>();
		if(!BaseUtil.isBlank(user.getOrganNo())){
			organNoArr = Arrays.asList(user.getOrganNo().split(","));
		}
		if(!BaseUtil.isBlank(user.getBankNo())){//角色号传入无效字段
			roleNoArr = Arrays.asList(user.getBankNo().split(","));
		}
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("userNo", BaseUtil.getLoginUser().getUserNo());
		/*condMap.put("organNoList", organNoArr);
		condMap.put("roleNoList", roleNoArr);*/
		condMap.put("organNoList",SqlUtil.getSumArrayList(organNoArr));
		condMap.put("roleNoList", roleNoArr);
		condMap.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
		// 查询记录
		List<UserBean> list = userDao.selectUserList(condMap);

		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("returnList", list);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	private void checkAllUserPrivOrganCount(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		Map sysMap = requestBean.getSysMap();
		String organNo = (String)sysMap.get("organNo");
		String[] split = organNo.split(",");
		String userNo = BaseUtil.getLoginUser().getUserNo();
		int count=0;
		for (String string : split) {
			HashMap<String, Object> userPrivOrganCount = organInfoDao.getUserPrivOrganCount(userNo, string,(String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));	
			count=count+Integer.valueOf(String.valueOf(userPrivOrganCount.get("RETCOUNT")));
		}
		map.put("returnNum", count-split.length);
		responseBean.setRetMap(map);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	
	private void  getUsersToApply(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		Map sysMap = requestBean.getSysMap();
		String organNo = (String)sysMap.get("organNo");
		String userNo = BaseUtil.getLoginUser().getUserNo();
		String hasUserStr = (String)sysMap.get("hasUsers");
		String [] hasUsers = null;
		if(hasUserStr!=null){
			hasUsers = hasUserStr.split(",");
		}		 
		HashMap<String, Object> condMap = new HashMap<String, Object>();
		condMap.put("organNo", organNo);
		condMap.put("userNo", userNo);
		condMap.put("hasUsers", hasUsers);		
		condMap.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
		List<HashMap> returnList = userDao.getUsersToApply(condMap);
		
		map.put("returnList", returnList);
		responseBean.setRetMap(map);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	
	}
}
