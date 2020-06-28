package com.sunyard.ars.system.service.impl.monitor;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.FileUtil;
import com.sunyard.cop.IF.common.SunIFCommonUtil;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.dao.CustomMenuMapper;
import com.sunyard.cop.IF.mybatis.pojo.Menu;
import com.sunyard.cop.IF.mybatis.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.util.HttpUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.monitor.MrModuleBean;
import com.sunyard.ars.system.dao.monitor.MrModuleDao;
import com.sunyard.ars.system.service.monitor.IMrModuleService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * ... 接口实现类
 * 
 * @author:	...
 * @date:	2017年2月28日 上午10:49:50
 */
@Service("mrModuleService")
@Transactional
public class MrModuleServiceImpl extends BaseService implements IMrModuleService {

	// 数据库接口
	@Resource
	private MrModuleDao mrModuleDao;

	@Resource
	private CustomMenuMapper customMenuMapper;

	/**
	 * 执行接口逻辑
	 * 
	 * @author:	...
	 * @date:	2017年2月28日 上午10:53:30
	 */
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	/**
	 * 执行具体操作：增、删、改、查 等，操作成功后记录日志信息
	 * 
	 * @author:	...
	 * @date:	2017年2月28日 下午4:34:57
	 */
	@Override
	@SuppressWarnings({ "rawtypes" })
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		// 获取操作标识
		String oper_type = (String) sysMap.get("oper_type");
		if (AOSConstants.OPERATE_QUERY.equals(oper_type)) {
			// 查询
			queryOperation(requestBean, responseBean);
		} else if ("upload".equals(oper_type)) {
			// 上传
			upLoadOperation(requestBean, responseBean);
		} else if ("createMenu".equals(oper_type)){

			//新增，根据menu的新增来新增，父菜单为报表展现
			createMenu(requestBean, responseBean);

		}else if("queryMenuValid".equals(oper_type)){
			//验证是否可以新增json报表，通过就可以上传
			queryMenuValid(requestBean, responseBean);
		}else if("deleteMenuId".equals(oper_type)){

			//删除文件和菜单
			deleteMenuId(requestBean, responseBean);
		}
	}
	
	/**
	 * 查询 ... 操作
	 * 
	 * @author:	...
	 * @date:	2017年2月28日 下午5:17:47
	 * 
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		MrModuleBean bean = (MrModuleBean) requestBean.getParameterList().get(0);
		Map sysMap = requestBean.getSysMap();
		// 起始时间
		String start_date = (String) sysMap.get("start_date");
		// 结束时间
		String end_date = (String) sysMap.get("end_date");
		// 菜单类型
		String menu_type = (String) sysMap.get("menu_type");
		// 下载路径
		//String path = HttpUtil.getAbsolutePath("json");
		// 下载路径
		String path= ARSConstants.SYSTEM_PARAMETER.get("REPORT_SAVE_FOLDER").getParamValue();
		//path = "D:/report/json";
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("start_date", start_date);
		condMap.put("end_date", end_date);
		condMap.put("bean", bean);
		condMap.put("menu_type", menu_type);
		condMap = addExtraCondition(condMap);
		
		// 当前页数
		int currentPage = (int) sysMap.get("currentPage");
		// 每页数量
		int pageSize = 0;
		if (currentPage != -1) { // -1 表示查询全部数据
			pageSize = (int) sysMap.get("pageSize");
			if (pageSize <= 0) {
				pageSize = AOSConstants.PAGE_NUM;
			}
		}
		// 定义分页操作，pageSize = 0 时查询全部数据（不分页），currentPage <= 0 时查询第一页，currentPage > pages（超过总数时），查询最后一页
		Page page = PageHelper.startPage(currentPage, pageSize);
		
		//判断文件是否存在 存入查到的map,EXISTFLAG标志，true存在
		List<HashMap<String, Object>> resultList = mrModuleDao.select(condMap);
		for(HashMap<String, Object> map : resultList) {
			map.put("EXISTFLAG", "false");
			String urlStr = (String) map.get("MENU_URL");
			if(urlStr != null) {
				//static/html/system/monitor/MonitorModule.html?reportlet=json/test.json 这种形式
				String[] split = urlStr.split("=");
				File file = new File(FileUtil.pathManipulation(path + File.separator + split[1]));
				if(file.exists() && file.isFile()) {
					map.put("EXISTFLAG", "true");
				}

			}
		}
		// 查询分页记录
		//List list = getList(mrModuleDao.select(condMap), page);
		List list = getList(resultList, page);
		// 获取总记录数
		long totalNum = page.getTotal();
		
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("currentPage", currentPage);
		retMap.put("pageSize", pageSize);
		retMap.put("totalNum", totalNum);
		retMap.put("list", list);
		retMap.put("path", path);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
	}
	
	/**
	 * 导入 ... 操作
	 * 
	 * @author:	...
	 * @date:	2017年3月27日 上午10:45:32
	 * 
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	private void upLoadOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取前台参数
		MrModuleBean bean = (MrModuleBean)requestBean.getParameterList().get(0);
		HashMap condMap = new HashMap();
		condMap.put("bean", bean);
		condMap = addExtraCondition(condMap);
		int num = mrModuleDao.upload(condMap);
		if(num > 0 ) {
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("修改成功");
			//添加日志
			String log = "统计分析报表管理中修改模板";
			addOperLogInfo(ARSConstants.MODEL_NAME_RT, ARSConstants.OPER_TYPE_4, log);
		} else {
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("修改失败");
		}
	}


	private void queryMenuValid (RequestBean requestBean, ResponseBean responseBean) throws Exception{

		// 拼装返回信息
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);

		Map<String,Object> sysMap = requestBean.getSysMap();
		String menu_url = (String) sysMap.get("menu_url");
		String menu_type = (String) sysMap.get("menu_type");

		// 下载路径
		//String path = HttpUtil.getAbsolutePath("json");
		// 下载路径
		String path= ARSConstants.SYSTEM_PARAMETER.get("REPORT_SAVE_FOLDER").getParamValue();
		//path = "D:/report/json";
		String fileName = (String) sysMap.get("fileName");

		File file = new File(FileUtil.pathManipulation(path + File.separator + fileName));
		if(file.exists()){
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("新增失败，服务器存有相同名字的文件");
			return;
		}

		User user = BaseUtil.getLoginUser();
		Menu menu = new Menu();
		menu.setProjectNo(user.getProjectNo());
		menu.setSystemNo(user.getSystemNo());
		menu.setIsLock(user.getUserNo());
		menu.setMenuUrl(menu_url);
		menu.setMenuType(menu_type);
		int num = mrModuleDao.selectCount(menu);
		if(num != 0){
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("系统已经创建了相同url的菜单");
			return;
		}

		//查询报表展现这个meun
		HashMap<String, Object> confMap = new HashMap<>();
		addExtraCondition(confMap);
		confMap.put("menu_name","报表展现");
		List<HashMap<String, Object>> menus = mrModuleDao.selectPar(confMap);
		if(menus == null || menus.size() == 0 ){
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("请配置名称为报表展现的菜单");
			return;
		}
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		retMap.put("parentMenu",menus.get(0));
		responseBean.setRetMsg("可以上传文件，和创建该菜单!");

	}


	private void createMenu(RequestBean requestBean, ResponseBean responseBean) throws Exception{

		// 拼装返回信息
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);

		Map<String,Object> sysMap = requestBean.getSysMap();
		String menu_url = (String) sysMap.get("menu_url");
		String menu_type = (String) sysMap.get("menu_type");
		String menu_name = (String) sysMap.get("menu_name");
		String parentId = (String) sysMap.get("parent_Id");
		String menu_desc = (String) sysMap.get("menu_desc");
		String menu_level = (String) sysMap.get("menu_level");


		// 下载路径 和 文件名，创建菜单失败删除文件
		//String path = HttpUtil.getAbsolutePath("json");
		// 下载路径
		String path= ARSConstants.SYSTEM_PARAMETER.get("REPORT_SAVE_FOLDER").getParamValue();
		//path = "D:/Users/qianyun/Library/ApacheTomcat/json";

		String fileName = (String) sysMap.get("fileName");

		User user = BaseUtil.getLoginUser();
		Menu menu = new Menu();
		menu.setBankNo(user.getBankNo());
		menu.setProjectNo(user.getProjectNo());
		menu.setSystemNo(user.getSystemNo());
		menu.setIsLock(user.getUserNo());

		menu.setMenuName(menu_name);
		if(parentId != null){
			menu.setParentId(parentId);
		}else {
			menu.setParentId("#000000");
		}

		menu.setMenuType(menu_type);

		menu.setMenuDesc(menu_desc);
		menu.setMenuUrl(menu_url);
		menu.setMenuLevel(menu_level);
		menu.setMenuBelong("0");
		menu.setMenuOrder("1");
		menu.setIsParent("1");

		try {
			insertMenu(menu);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("新增成功");
		}catch (Exception e){
			logger.error(e.getMessage());
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(e.getMessage());
			File  file = new File(FileUtil.pathManipulation(path + File.separator + fileName));
			if(file.exists() && file.isFile()){
				file.delete();
			}
		}

	}

	/**
	 * 插入菜单
	 */
	private  void insertMenu(Menu menu) throws Exception{

		//设置菜单id
		String menuid = customMenuMapper.getMaxMenuId(menu);;
		DecimalFormat df = new DecimalFormat("000");
		if (!menuid.equalsIgnoreCase("")) {
			menuid = menuid.substring(0, 4) + df.format(Integer.parseInt(menuid.substring(4)) + 1);
		} else {
			menuid = "#100001";
		}
		menuid = BaseUtil.filterSqlParam(menuid);
		menu.setMenuId(menuid);

		//菜单顺序
		String menuOrder = customMenuMapper.getMaxMenuOrder(menu);;
		if ("1".equalsIgnoreCase(menu.getMenuLevel())) {
			if (menuOrder != null && !menuOrder.equalsIgnoreCase("")) {
				menuOrder = Integer.parseInt(menuOrder) + 1 + "";
			} else {
				menuOrder = "0";
			}
		}
		menuOrder = BaseUtil.filterSqlParam(menuOrder);
		menu.setMenuOrder(menuOrder);

		menu.setIsOpen("1");
		menu.setLastModiDate(SunIFCommonUtil.getFomateTimeString("yyyyMMddhhmm"));
		menu.setEditEnable("1");
		menu.setHomeShow("0");
		menu.setMenuAttr("1");

		int insertResult = customMenuMapper.insertCustomMenu(menu);

		if(insertResult > 0){
			String	log="菜单名"+menu.getMenuName()+"在菜单表中被创建！";
			addOperLogInfo(ARSConstants.MODEL_NAME_RT, ARSConstants.OPER_TYPE_1, log);
			//json 报表不加button
		}else{
			throw  new Exception("insert插入方法返回影响行不为1");
		}

	}


	private void deleteMenuId(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		Map<String,Object> sysMap = requestBean.getSysMap();
		String menuId = (String) sysMap.get("menu_id");
		HashMap<String, Object> config = new HashMap<>();
		addExtraCondition(config);
		config.put("menu_id",menuId);
		List<HashMap<String, Object>> menus = mrModuleDao.selectPar(config);
		if(menus == null || menus.size() == 0){
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("不存在该菜单，删除成功");
			return;
		}
		HashMap<String, Object> obj = menus.get(0);
		String menu_url = (String)obj.get("MENU_URL");
		if(menu_url.endsWith(".json")){
			String id = (String)obj.get("MENU_ID");
			id = BaseUtil.filterSqlParam(id);
			if(BaseUtil.isBlank(id)){
				responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("没有传入要删除菜单的id");
				return;
			}
			//删除菜单
			Menu menu = new Menu();
			User user = BaseUtil.getLoginUser();
			menu.setMenuId(id);
			menu.setProjectNo(user.getProjectNo());
			menu.setSystemNo(user.getSystemNo());
			menu.setBankNo(user.getBankNo());
			customMenuMapper.deleteCustomMenu(menu);
			//删除文件
			String fileName = (String) sysMap.get("fileName");
			//String path = HttpUtil.getAbsolutePath("json");
			String path= ARSConstants.SYSTEM_PARAMETER.get("REPORT_SAVE_FOLDER").getParamValue();
			//path = "D:/Users/qianyun/Library/ApacheTomcat/json";
			File file = new File(FileUtil.pathManipulation(path + File.separator + fileName));
			if(file.exists() && file.isFile()){
				file.delete();
			}


			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("删除成功");

		}else {
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("不能删除不是以.json的文件！");
		}

	}

}
