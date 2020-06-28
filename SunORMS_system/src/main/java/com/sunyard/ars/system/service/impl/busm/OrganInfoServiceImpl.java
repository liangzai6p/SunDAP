package com.sunyard.ars.system.service.impl.busm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sunyard.ars.common.util.SqlUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.system.bean.busm.OrganBean;
import com.sunyard.ars.system.dao.busm.OrganInfoDao;
import com.sunyard.ars.system.service.busm.IOrganInfoService;
import com.sunyard.cop.IF.bean.OrganZTreeBean;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.pojo.User;

/**
 * @author:		zheng.jw
 * @date:		2017年12月15日 下午3:22:45
 * @Description:(机构信息Service实现类)
 */
@Service("organInfoService")
@Transactional
public class OrganInfoServiceImpl extends BaseService implements IOrganInfoService {

	// 数据库接口
	@Resource
	private OrganInfoDao organInfoDao;
	
	/**
	 * @author:		zheng.jw
	 * @date:		2017年12月15日 上午10:16:42
	 * @Description:(执行接口逻辑)
	 */
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	/**
	 * @author:		zheng.jw
	 * @date:		2017年12月15日 上午10:17:00
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
			queryOperation(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_ADD.equals(oper_type)) { 
			// 新增
			addOperation(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_MODIFY.equals(oper_type)) { 
			// 修改
			modifyOperation(requestBean, responseBean);
		} else if(ARSConstants.OPERATE_DELETE.equals(oper_type)) { 
			// 删除
			deleteOperation(requestBean, responseBean);
		} else if ("update".equals(oper_type)) {
			/**
			 * modify by zjw
			 * 20170927
			 * 更新机构树
			 */
			updateOperation(requestBean, responseBean);
		} else if ("detail".equals(oper_type)) {
			/**
			 * modify by zjw
			 * 20170928
			 * 单个机构详情查询
			 */
			detailOperation(requestBean, responseBean);
		}else if("user_organ".equals(oper_type)){
			//用户权限机构
			getOrganList(requestBean, responseBean);
		}else if("getThisLevelOrganLisr".equals(oper_type)){
			getThisLevelOrganLisr(requestBean, responseBean);
		}else if("getLowerOrgans".equals(oper_type)){
			getLowerOrgans(requestBean, responseBean);
		}else if ("selectSiteInfo".equals(oper_type)) {
			selectSiteInfo(requestBean, responseBean);
		}else if("getOrganLevel".equalsIgnoreCase(oper_type)){//获取机构最高级别
			getOrganLevel(requestBean, responseBean);
		}else if("getUserOrganTree".equalsIgnoreCase(oper_type)){//
			getUserOrganTree(requestBean, responseBean);
		}
	}

	/**
	 * 获取机构最高级别
	 * @param requestBean
	 * @param responseBean
	 */
	private void getOrganLevel(RequestBean requestBean, ResponseBean responseBean) {
		// TODO Auto-generated method stub
		OrganBean organBean = (OrganBean) requestBean.getParameterList().get(0);
		String organNo = organBean.getOrgan_no();
		/*String[] organNos = organNo.split(",");
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("organNoList", organNos);
		List<HashMap<String,Object>> list = organInfoDao.getOrganLevel(condMap);*/
		if(organNo == null ){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("输入机构为空，请重新输入");
			return;
		}
		HashMap condMap = new HashMap();
		condMap.put("organNoList",SqlUtil.getSumArrayList(Arrays.asList(organNo.split(","))));
		List<HashMap<String,Object>> list = organInfoDao.getOrganLevel2(condMap);
		if(list == null || list.size() == 0){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("输入机构错误，请重新输入");
		}else{
			Map retmap = new HashMap();
			retmap.put("organLevel", ((Map)list.get(0)).get("ORGAN_LEVEL"));
			responseBean.setRetMap(retmap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}
	}

	//查询机构信息！根据机构级别	
	private void selectSiteInfo(RequestBean requestBean, ResponseBean responseBean) {
		OrganBean organBean = (OrganBean) requestBean.getParameterList().get(0);
		List<HashMap<String,Object>> list = organInfoDao.selectSiteInfo(organBean.getOrgan_no());
		Map retmap = new HashMap();
		retmap.put("list", list);
		responseBean.setRetMap(retmap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * 查询指定指定级别机构
	 * @param requestBean
	 * @param responseBean
	 */
	private void getThisLevelOrganLisr(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// 前台参数集合
		Integer organ_level =
				((OrganBean)requestBean.getParameterList().get(0)).getOrgan_level();
		String userOrganNo = BaseUtil.getLoginUser().getOrganNo();
		
		List<HashMap<String,Object>> list = organInfoDao.getCenterOrganLevel(userOrganNo);
		
		if(list != null && list.size() > 0){
			// 查询机构详情
			List branchs = organInfoDao.getThisLevelOrganLisr(organ_level,BaseUtil.filterSqlParam(((Map)list.get(0)).get("PARENT_ORGAN")+""));
			// 构造返回信息
			Map map = new HashMap();
			map.put("branchs", branchs);
			responseBean.setRetMap(map);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else{
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("查询机构失败");
		}
	}

	/**
	 * 根据选择的分行获取登录用户机构列表
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	public void getLowerOrgans(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// 前台参数集合
		String userNo = BaseUtil.getLoginUser().getUserNo();
		String branchOrgan = (String)requestBean.getSysMap().get("branchOrgan");
		// 查询机构详情
		List organs = organInfoDao.getLowerOrgans(branchOrgan,userNo, (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));
		// 构造返回信息
		Map map = new HashMap();
		map.put("organs", organs);
		responseBean.setRetMap(map);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * @author:		zheng.jw
	 * @date:		2017年12月15日 下午1:43:12
	 * @Description:机构信息查询
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		String organ_no =  (String) sysMap.get("organ_no");
		String parent_organ =  (String) sysMap.get("parent_organ");
		String organ_type =  (String) sysMap.get("organ_type");
		String status =  (String) sysMap.get("organ_status");
		String organ_name =  sysMap.get("organ_name") == null?"":(String)sysMap.get("organ_name");
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("parent_organ", parent_organ);
		condMap.put("organ_type", organ_type);
		condMap.put("organ_no", organ_no);
		condMap.put("status", status);
		condMap.put("organ_name", organ_name);
		condMap = addExtraCondition(condMap);
		
		//获得当前登入用户的机构，只能查询当前机构和以下机构
		User loginUser = BaseUtil.getLoginUser();
		String maxOrganNo = loginUser.getOrganNo();
		if(BaseUtil.isBlank(maxOrganNo)) {
			//获得登入用户失败时，getLoginUser会返回一个默认user
			throw new Exception("获取用户异常!");
		}
		condMap.put("maxOrganNo",maxOrganNo);
		
		// 当前页数
		int pageNum = (int) sysMap.get("currentPage");
		// 每页数量
		int pageSize = 0;
		if (pageNum != -1) {
			int initPageNum = (int) sysMap.get("pageSize");
			if (BaseUtil.isBlank(initPageNum + "")) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageNum;
			}
		}
		// 定义分页操作
		Page page = PageHelper.startPage(pageNum, pageSize);
		// 查询分页记录
		List list = getList(organInfoDao.select(condMap), page);
		// 获取总记录数
		long totalCount = page.getTotal();
		
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("pageSize", pageSize);
		retMap.put("allRow", totalCount);
		retMap.put("organs", list);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * @author:		zheng.jw
	 * @date:		2017年12月15日 下午1:43:54
	 * @Description:新增机构信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取新增数据
		OrganBean organBean = (OrganBean) requestBean.getParameterList().get(0);
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("organBean", organBean);
		condMap = addExtraCondition(condMap);
		
		int temp = getInt(organInfoDao.getTemp(condMap));
		if (temp != 0) {
			//存在，返回提示信息
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			String retMsg = "机构号已存在,请重新输入";
			responseBean.setRetMsg(retMsg);
		} else {
			try {
				// 新增数据
				organInfoDao.insert(condMap);				// 往机构表中插入基本信息
				String	log="部门名称"+organBean.getOrgan_name()+"在机构表中被添加！";
				//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM, ARSConstants.OPER_TYPE_1, log);
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
				organInfoDao.insertOrganParent(condMap);	// 往机构父子表加一条自身对应基本信息
				organInfoDao.insertOrganParents(condMap);	// 往机构父子表加所有对应关系信息
				
				//返回信息
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
				responseBean.setRetMsg("新增成功");
				
				// 添加日志信息
				/*
				 * String logContent = "新增机构信息，||{机构号：" + organBean.getOrgan_no() + "，机构名称：" +
				 * organBean.getOrgan_name() + "，机构等级：" + organBean.getOrgan_level() + "，上级机构号："
				 * + organBean.getParent_organ() +"}"; addLogInfo(logContent);
				 */
			} catch (RuntimeException e) {
				logger.error(e.getMessage());
				throw new RuntimeException("数据库新增失败，请查看日志，确认问题后重新操作！");
			}
		}
	}

	/**
	 * @author:		zheng.jw
	 * @date:		2017年12月15日 下午1:44:10
	 * @Description:修改机构信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void modifyOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取新增数据
		OrganBean organBean = (OrganBean) requestBean.getParameterList().get(0);
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("organBean", organBean);
		condMap = addExtraCondition(condMap);
		
		// 查询原机构信息
		List list = organInfoDao.selectOldOrgan(condMap);
		Map ob = (Map)list.get(0);
		
		// 修改操作
		int result=organInfoDao.update(condMap);
		
		/*if(result>0){//如果sm_organ_tb更新成功
			//更新McOrganTbKj表
			organInfoDao.updateMcOrganTbKj(condMap);
		}*/
		
		String	log="部门名称"+organBean.getOrgan_name()+"在机构表中被修改！";
		//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM, ARSConstants.OPER_TYPE_3, log);
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
		
		//返回信息
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功");
		
		// 添加日志信息 
		//mapper文件中 select * ，用列名大写获取
		/*
		 * String logContent = "修改机构信息，||修改前：{机构号：" + ob.get("ORGAN_NO") + "，机构名称：" +
		 * ob.get("ORGAN_NAME") + "，机构类型：" + ob.get("ORGAN_TYPE") + "，机构状态：" +
		 * ob.get("STATUS") + "}"; logContent += "，||修改后：{机构号：" +
		 * organBean.getOrgan_no() + "，机构名称：" + organBean.getOrgan_name() + "，机构类型：" +
		 * organBean.getOrgan_type() + "，机构状态：" + organBean.getStatus() + "}";
		 * addLogInfo(logContent);
		 */
	}

	/**
	 * @author:		zheng.jw
	 * @date:		2017年12月15日 下午1:44:27
	 * @Description:删除机构信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes"})
	private void deleteOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		List list = requestBean.getParameterList();   //前台删除list
		String organs = "";
		List<String> organNoList = new ArrayList<>();
		// 构造条件参数
		HashMap condMap = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			OrganBean organBean = (OrganBean) list.get(i);
			condMap = addExtraCondition(condMap);
			organs += "'" + organBean.getOrgan_no() + "'";
			if (i < list.size() - 1) {
				organs += ",";
			}
			organNoList.add(organBean.getOrgan_no());
		}
		
		condMap.put("organs",organNoList);
		// 用户表中查看该机构下是否有用户，如果有则不能删除机构
		int temp1 = getInt(organInfoDao.selectUserCount(condMap));
		// 查看是否存在子机构
		int temp2 = getInt(organInfoDao.selectSonOrganCount(condMap));
		
		if (temp1 > 0) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("所选机构下存在用户，无法删除！");
		} else if (temp2 > 1) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("所选机构下存在子机构，无法删除！");
		} else {
			
			// 构造删除语句并执行
			organInfoDao.delete(condMap);				// 删除机构表信息
			String	log="机构ID"+organs+"在机构表中被删除！";
			//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM, ARSConstants.OPER_TYPE_2, log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
			
			organInfoDao.deleteOrganParent(condMap);	// 删除机构父子关系表信息
			
			// 构造返回信息
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("删除成功");
			
			// 添加日志信息
			/*
			 * for (int i = 0;i < list.size();i++) { OrganBean organ = (OrganBean)
			 * list.get(i); String logContent = "删除机构信息，||{机构号：" +organ.getOrgan_no() +
			 * "，机构名称：" + organ.getOrgan_name() + "}"; addLogInfo(logContent); }
			 */
		}
	}
	
	/**
	 * @author:		zheng.jw
	 * @date:		2017年12月15日 下午2:36:55
	 * @Description:单个机构详情查询
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void detailOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取请求参数
		OrganBean organ = (OrganBean)requestBean.getParameterList().get(0);
		String organ_no = organ.getOrgan_no();	
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("organ_no", organ_no);
		condMap = addExtraCondition(condMap);
		
		// 查询机构详情
		List list = organInfoDao.selectOrganDetail(condMap); 
		
		// 构造返回信息
		Map map = new HashMap();
		map.put("returnList", list);
		responseBean.setRetMap(map);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * @author:		zheng.jw
	 * @date:		2017年12月15日 下午2:37:50
	 * @Description:更新机构树
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void updateOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {

		// 获取请求参数
		List list = requestBean.getParameterList();					// 修改机构list
		String logStr = "";
		for (int i = 0 ; i < list.size(); i++) {
			OrganBean organ = (OrganBean)list.get(i);
			String moveOrganNO = organ.getDragOrgan();				// 移动机构
			String oldParOrganNo = organ.getDragParOrgan();			// 移动前父机构
			String newParOrganNO = organ.getDragedNewParOrgan();	// 移动后父机构
			int organ_level = organ.getOrgan_level();        		// 移动机构机构等级
			String last_modi_date = BaseUtil.getCurrentTimeStr();	// 最后修改事件
			
			// 构造条件参数
			HashMap condMap = new HashMap();
			condMap.put("organ", organ);
			condMap.put("last_modi_date", last_modi_date);
			condMap = addExtraCondition(condMap);
			// 更新机构表里父机构字段
			organInfoDao.updateOrganParent(condMap);
			// 删除机构父子表里原机构的关系信息
			organInfoDao.deleteOrganParents(condMap);
			// 取新父机构的原机构关系
			
			String newOrganStr = getString(organInfoDao.selectOrganInfo(condMap));
			newOrganStr = BaseUtil.filterSqlParam(newOrganStr);
			newOrganStr  +=   "," + moveOrganNO + "-" + organ_level;
			String[] OrganArr = newOrganStr.split("\\,");
			// 重构机构父子关系
			for (int j = 0; j < OrganArr.length; j++) {
				condMap.put("parent_organ", OrganArr[j].split("\\-")[0]);
				condMap.put("organ_level", OrganArr[j].split("\\-")[1]);
				organInfoDao.insertResetOrganParents(condMap);
			}
			logStr += "机构号" + moveOrganNO + "的父机构从" + oldParOrganNo + "改到了" + newParOrganNO + "下,||  ";
		}
		// 添加日志信息
		String logContent = "更新机构信息：机构层级关系修改,||{" + logStr + "}";
		//addLogInfo(logContent);
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, logContent);
	}
	
	/**
	 * @author:		zheng.jw
	 * @date:		2017年12月15日 下午2:36:55
	 * @Description:单个机构详情查询
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getOrganList(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		User user = BaseUtil.getLoginUser();
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("userNo", user.getUserNo());
		// 查询机构详情（所有机构包括权限机构has区分）
		//Long startTime = System.currentTimeMillis();
        int cnt = organInfoDao.hasPrivOrgan(condMap);
        if (cnt > 0) {//有权限机构
            RequestUtil.getRequest().getSession().setAttribute("HAS_PRIV_ORGAN_FLAG", "1");
        } else {//无权限机构
            RequestUtil.getRequest().getSession().setAttribute("HAS_PRIV_ORGAN_FLAG", "0");//使用session存放该值,每个用户的不一样
        }
        condMap.put("hasPrivOrganFlag",(String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));
		List tempList = organInfoDao.getOrganList(condMap);
		//Long endTime = System.currentTimeMillis();
		//System.out.println("查询时间："+(endTime - startTime));
		ArrayList allTreeList = new ArrayList();//所有机构集合
		ArrayList privTreeList = new ArrayList();//权限机构树
		ArrayList organList = new ArrayList();//权限机构集合
		HashMap<String, String> organMap = null;
		boolean ishas = false;
		for (Object o : tempList) {
			ishas = false;
			HashMap maps = (HashMap) o;
			OrganZTreeBean oztBean = new OrganZTreeBean();
			/*for (Map.Entry<String, Object> entry: maps.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue() + "";
				if (key.equalsIgnoreCase("organ_count")) {
					if ("".equals(value) || "null".equals(value)) {
						oztBean.setIsParent("false");
					} else {
						oztBean.setIsParent("true");
					}
				}
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
					// 临时存储 机构级别
					oztBean.setReserve(value);
				}
				if(key.equalsIgnoreCase("has") && "1".equals(value)){//判断用户权限机构
					ishas = true;
				}
			}*/
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
					// 临时存储 机构级别
					oztBean.setReserve(value);
				}
				if(key.equalsIgnoreCase("has") && "1".equals(value)){//判断用户权限机构
					ishas = true;
				}
                if(key.equalsIgnoreCase("has") && "0".equals(value)){//无权限机构,使用所属机构
                    ishas = true;
                }
			}
			oztBean.setIcon("");
			allTreeList.add(oztBean);
			if(ishas){
				privTreeList.add(oztBean);
				organMap = new HashMap<String, String>();
				organMap.put("value", oztBean.getId());
				organMap.put("name", oztBean.getName());
				organList.add(organMap);
			}
		}
		/*if (privTreeList.size() == 0 || privTreeList == null) {
			RequestUtil.getRequest().getSession().setAttribute("HAS_PRIV_ORGAN_FLAG", "0");
			privTreeList = allTreeList;
		} else {
			RequestUtil.getRequest().getSession().setAttribute("HAS_PRIV_ORGAN_FLAG", "1");//使用session存放该值,每个用户的不一样
		}*/
		// 构造返回信息
		Map map = new HashMap();
		map.put("allTreeList", allTreeList);
		map.put("privTreeList", privTreeList);
		map.put("organList", organList);
		responseBean.setRetMap(map);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 获取用户所有机构机构树
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getUserOrganTree(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		List tempList = organInfoDao.getAllOrganList();
		ArrayList allTreeList = new ArrayList();//所有机构集合
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
					// 临时存储 机构级别
					oztBean.setReserve(value);
				}
			}
			oztBean.setIcon("");
			allTreeList.add(oztBean);
			if("9999".equals(oztBean.getId())){//
				OrganZTreeBean zhBean = new OrganZTreeBean();
				zhBean.setIsParent("false");
				zhBean.setId("-1");
				zhBean.setName("9999-虚拟总行机构");
				zhBean.setpId("9999");
				zhBean.setOpen("false");
				zhBean.setReserve("2");
				zhBean.setIcon("");
				allTreeList.add(zhBean);
			}
		}
		// 构造返回信息
		Map map = new HashMap();
		map.put("allTreeList", allTreeList);
		responseBean.setRetMap(map);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
}
