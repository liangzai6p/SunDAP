package com.sunyard.ars.system.service.impl.busm;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.busm.HomePage;
import com.sunyard.ars.system.dao.busm.HomePageMapper;
import com.sunyard.ars.system.service.busm.IHomePageService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.mybatis.pojo.User;

@Service("homePageService")
@Transactional
public class HomePageServiceImpl extends BaseService implements IHomePageService {
    
	@Resource
	HomePageMapper homePageMapper;
  
	
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}
    
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		// 获取操作标识
		String oper_type = (String) sysMap.get("oper_type");
		if (ARSConstants.OPERATE_QUERY.equals(oper_type)) {
			// 查询
			query(requestBean, responseBean);
		} else if(ARSConstants.OPERATE_MODIFY.equals(oper_type)){
			modify(requestBean, responseBean);
		} else if(ARSConstants.OPERATE_ADD.equals(oper_type)){
			
		}else if("init".equals(oper_type)){
			init(requestBean, responseBean);
		}else if("parentMenu".equals(oper_type)){
			queryParentMenu(requestBean, responseBean);
		}else if("querAllTaskMenu".equals(oper_type)){
			querAllTaskMenu(requestBean, responseBean);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void querAllTaskMenu(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		String userNo =  BaseUtil.getLoginUser().getUserNo();
		
		String menuUrl = homePageMapper.querAllTaskMenu(userNo); 
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("menu_url", menuUrl);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * 查询用户主页展现信息
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void query(RequestBean requestBean, ResponseBean responseBean){
		Map sysMap = requestBean.getSysMap();
		String userNo =  (String) sysMap.get("user_no");
		String menuClass = (String) sysMap.get("menu_class");
		
		HashMap condMap = new HashMap();		
		condMap.put("userNo", userNo);
		condMap.put("menuClass", menuClass);
		
		
		List<HomePage> list = homePageMapper.selectBySelective(condMap); 
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("homePages", list);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 修改用户主页展现信息
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void modify(RequestBean requestBean, ResponseBean responseBean){
		Map sysMap = requestBean.getSysMap();
		String userNo =  (String) sysMap.get("user_no");
		List<HomePage> homePageList= JSON.parseArray((String)sysMap.get("homePageList"),HomePage.class);
		
		//1.将用户配置的主页信息更新为不展现pageState=0,顺序置空pageNo=''
		HomePage delHomePage = new HomePage();
		delHomePage.setPageState("0");
		delHomePage.setPageNo("");
		delHomePage.setUserNo(userNo);
		homePageMapper.updateBySelective(delHomePage);
		
		//2.将用户页面配置更新进sm_homepage_tb
		if(homePageList !=null && homePageList.size()>0){
			for(HomePage homePage:homePageList){
				homePage.setUserNo(userNo);
				homePageMapper.updateBySelective(homePage);
			}
		}
		
		//更新完毕，拼装返回信息
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
	}
	
	/**
	 * 用户登录初始化主页配置
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception 
	 */
	private void init(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		User user = BaseUtil.getLoginUser();
		String user_no = user.getUserNo();
		Map sysMap = requestBean.getSysMap();
		boolean isPC = (boolean) sysMap.get("isPC");
		switch (user_no) {
		// 超级管理员 生成菜单方式
		case AOSConstants.SYS_ADMIN:
			logger.info("[" + user_no + "]" + " 为超级管理员，初始化超级管理员的主页配置项 ");			
			//添加未在sm_homepage_tb中生成的主页配置项
			addAdminHomePage(user);
			//删除无效的主页配置项
			delAdminHomePage(user,isPC);						
			break;
		// 普通用户
		default:
			String role_no = user.getRoleNo();
			logger.info("[" + user_no + "]" + " 为普通用户，根据对应角色[" + role_no + "]的菜单信息初始化主页配置项 ");
			if (BaseUtil.isBlank(role_no)) {
				logger.info("[" + user_no + "]未配置角色信息！");
			} else {
				//新增用户有权限，但未在sm_homepage_tb中的配置项
				addUserHomePage(user, role_no);				
				//删除未在用户权限内的配置
				delUserHomePage(user, role_no, isPC);
			}
		}
		responseBean.setRetCode(SunIFErrorMessage.SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}
	
	/**
	 * 新增超级管理员可展现的主页配置项
	 * @param user
	 */
	private void addAdminHomePage(User user)throws Exception{
		//组装新增条件
		HashMap condMap = new HashMap(); 
		condMap.put("userNo", user.getUserNo());
		homePageMapper.addAdminHomePage(condMap);
	}
	
	/**
	 * 新增普通用户的主页配置项
	 * @param user
	 * @param role_no
	 */
	private void addUserHomePage(User user,String role_no){
		//组装新增条件
		HashMap condMap = new HashMap(); 
		condMap.put("userNo", user.getUserNo());
		condMap.put("roles", role_no.split("\\,"));
		homePageMapper.addUserHomePage(condMap);
	}
	
	
	/**
	 * 删除超级管理员不需要展现的主页配置
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void delAdminHomePage(User user,boolean isPC)throws Exception{
		//组装删除条件
		HashMap condMap = new HashMap(); 
		condMap.put("userNo", user.getUserNo());
		condMap.put("bankNo", user.getBankNo());
		condMap.put("systemNo", user.getSystemNo());
		condMap.put("projectNo", user.getProjectNo());
		if (isPC) {
			condMap.put("menuBelong", "0");
		} else {
			condMap.put("menuBelong", "1");
		}
		//执行删除
		homePageMapper.delAdminHomePage(condMap);		
	}
	
	/**
	 * 删除用户没有权限或不需要展现的主页配置
	 */
	@SuppressWarnings({ "unchecked", "rawtypes"})
	private void delUserHomePage(User user,String role_no, boolean isPC) throws Exception{
		//组装删除条件
		HashMap condMap = new HashMap(); 
		condMap.put("userNo", user.getUserNo());
		condMap.put("bankNo", user.getBankNo());
		condMap.put("systemNo", user.getSystemNo());
		condMap.put("projectNo", user.getProjectNo());
		if (isPC) {
			condMap.put("menuBelong", "0");
		} else {
			condMap.put("menuBelong", "1");
		}
//		condMap.put("roles", BaseUtil.addSingleQuote(role_no));
		condMap.put("roles", role_no.split("\\,"));
		//执行删除

		homePageMapper.delUserHomePage(condMap);
	}
	
	/**
	 * 查询二级父菜单
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	private void queryParentMenu(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		Map sysMap = requestBean.getSysMap();
		String menuClass = (String) sysMap.get("menu_class");
		String parentMenuclass = homePageMapper.selParentMenu(menuClass);
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("menu_class", parentMenuclass);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
	}
	
}
