package com.sunyard.ars.system.service.impl.hx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.hx.RiskSourceDef;
import com.sunyard.ars.system.dao.hx.EntryRiskMapper;
import com.sunyard.ars.system.dao.hx.RiskSourceDefMapper;
import com.sunyard.ars.system.service.hx.IRiskEngineElementService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.pojo.User;

/**
 * @author:		 SUNYARD-NGQ
 * @date:		 2018年9月18日 下午2:47:38
 * @description: TODO(风险驱动因素配置 service实现类 )
 */
@Service("riskEngineElementService")
@Transactional
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RiskEngineElementServiceImpl extends BaseService implements IRiskEngineElementService {

/*	// 数据库接口
	
	@Resource
	private UserOrganMapper userOrganMapper;
	
	@Resource
	private OrganInfoDao organInfoDao;*/
	
	// 数据库接口
	@Resource
	private RiskSourceDefMapper riskSourceDefMapper;
	
	@Resource
	private EntryRiskMapper entryRiskMapper;
	
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月18日 下午2:47:38
	 * @description: TODO(执行接口逻辑 )
	 */
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月18日 下午2:47:38
	 * @description: TODO(执行具体操作：增、删、改、查 等，操作成功后记录日志信息 )
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
		} else if("OPERATE_SELECT_QUERY".equals(oper_type)){
			// 初始化下拉框-驱动因子-模型驱动因子管理 下拉框
			operateSelectQuery(requestBean, responseBean);
		} else if("OPERATE_CHECK_QUERY".equals(oper_type)){
			// 初始化下拉框
			operateCheckQuery(requestBean, responseBean);
		}
	}
	
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月26日 下午5:08:17
	 * @description: TODO(检测名称是否存在)
	 */
	@SuppressWarnings("rawtypes")
	private void operateCheckQuery(RequestBean requestBean, ResponseBean responseBean) {
//		Map sysMap = requestBean.getSysMap();
		RiskSourceDef riskSourceDefBean = (RiskSourceDef) requestBean.getParameterList().get(0);
		String sourceName = riskSourceDefBean.getSourceName();
		
		List list = new ArrayList();
		if (!BaseUtil.isBlank(sourceName)) {
			list = riskSourceDefMapper.selectBySourceName(sourceName);// 条件查询sourceName
		}
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("returnList", list);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月26日 下午5:08:17
	 * @description: TODO(初始化下拉框-驱动因子-模型驱动因子管理 下拉框)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void operateSelectQuery(RequestBean requestBean, ResponseBean responseBean) {
//		Map sysMap = requestBean.getSysMap();
		RiskSourceDef riskSourceDefBean = (RiskSourceDef) requestBean.getParameterList().get(0);
		String sourceType = riskSourceDefBean.getSourceType();
		
		List listType = new ArrayList();
		List listName = new ArrayList();
		if (BaseUtil.isBlank(sourceType)) {
			listType = riskSourceDefMapper.selectAllSourceType();// 如果sourceType为空不初始化子类选择框，查询所有的sourceType
			listName = riskSourceDefMapper.selectAllSourceName();// 如果sourceType为空不初始化子类选择框，查询所有的sourceName
		} else {// 如果sourceType不为空初始化子类选择框
			listName = riskSourceDefMapper.selectAllBySourceType(sourceType);// 根据sourceType条件选择行应的sourceName
		}
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("returnList1", listType);
		retMap.put("returnList2", listName);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月18日 下午2:47:38
	 * @description: TODO(执行具体操作：新增，操作成功后记录日志信息 )
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void add(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();// 获取新增信息
		RiskSourceDef riskSourceDefBean = (RiskSourceDef) requestBean.getParameterList().get(0);
		// 当前修改时间
		String lastModiDate = BaseUtil.getCurrentTimeStr();
		// 当前用户信息
		User loginUser = BaseUtil.getLoginUser();
		Integer maxId = add();
		riskSourceDefBean.setSourceNo(maxId.toString());// 设置主键从最大值新增
		riskSourceDefBean.setModiTime(lastModiDate);// 新增操作时间
		riskSourceDefBean.setModiUser(loginUser.getUserNo());// 用户姓名
		riskSourceDefMapper.insert(riskSourceDefBean);// 插入数据库数据
//		riskSourceDefBean.setModiUser(loginUser.getUserName());// 用户姓名
//		riskSourceDefMapper.insertSelective(riskSourceDefBean);// 插入数据库数据
		/*String privOrgan = (String)sysMap.get("privOrgan");
		if(privOrgan != null && privOrgan.trim().length()>0) {
			String[] privOrganList = privOrgan.split(",");
			for(String organ : privOrganList) {
				UserOrgan userOrgan = new UserOrgan();
				userOrgan.setUserNo(user.getUserNo());
				userOrgan.setOrganNo(organ);
				userOrganMapper.insert(userOrgan);
			}
		}*/
		// 返回前端封装信息
		Map<Object, Object> retMap = new HashMap<Object, Object>();
		// 格式化维护修改时间
		String str = lastModiDate;
		str = str.substring(0, 4)+"-"+str.substring(4, 6)+"-"+str.substring(6, 8)+"  "
				+str.substring(8, 10)+":"+str.substring(10, 12)+":"+str.substring(12, 14);
		retMap.put("lastModiDate", str);
		retMap.put("modiUser",loginUser.getUserNo());
//		retMap.put("modiUser",loginUser.getUserName());
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("添加成功");
		
		//添加日志
		String log = "新增驱动因子，因素大类:"+riskSourceDefBean.getSourceType() + " 子类:" + riskSourceDefBean.getSourceName();
		addOperLogInfo(ARSConstants.MODEL_NAME_RISK_WARNING, ARSConstants.OPER_TYPE_1, log);
	}
	//防止高并发环境下ID发生冲突
	public  synchronized  Integer  add() throws Exception{
		Integer maxId = riskSourceDefMapper.getMaxId();
		if(maxId==null){
			maxId=0;
		}
		return maxId;
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月18日 下午2:47:38
	 * @description: TODO(执行具体操作：删除，操作成功后记录日志信息 )
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		List riskSourceDefList = requestBean.getParameterList();// 获取要删除全部行的对象集合
		if(riskSourceDefList != null && riskSourceDefList.size()>0) {
			for(int i=0; i<riskSourceDefList.size(); i++) {
				RiskSourceDef riskSourceDef = (RiskSourceDef)riskSourceDefList.get(i);
				riskSourceDefMapper.deleteByPrimaryKey(riskSourceDef.getSourceNo());// 删除选中数据
				//添加日志
				String log = "删除编号为:"+riskSourceDef.getSourceNo()+"驱动因子";
				addOperLogInfo(ARSConstants.MODEL_NAME_RISK_WARNING, ARSConstants.OPER_TYPE_2, log);
			}
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功");
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月18日 下午2:47:38
	 * @description: TODO(执行具体操作：更新，操作成功后记录日志信息  )
	 */
	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();// 获取前台页面数据
		RiskSourceDef riskSourceDefBean = (RiskSourceDef) requestBean.getParameterList().get(1);// 获取前端修改后的数据
		String lastModiDate = BaseUtil.getCurrentTimeStr();// 获取当前时间，作为最后修改时间
		riskSourceDefBean.setModiTime(lastModiDate);
		User loginUser = BaseUtil.getLoginUser();// 获取当前用户，作为最后修改人
		riskSourceDefBean.setModiUser(loginUser.getUserName());
		
		riskSourceDefMapper.updateByPrimaryKeySelective(riskSourceDefBean);// 根据主键sourceNo唯一标识，更新数据
/*		userOrganMapper.deleteByUserNo(user.getUserNo());
		String privOrgan = (String)sysMap.get("privOrgan");
		if(privOrgan != null && privOrgan.trim().length()>0) {
			String[] privOrganList = privOrgan.split(",");
			for(String organ : privOrganList) {
				UserOrgan userOrgan = new UserOrgan();
				userOrgan.setUserNo(user.getUserNo());
				userOrgan.setOrganNo(organ);
				userOrganMapper.insert(userOrgan);
			}
		}*/
		/*Map<Object, Object> retMap = new HashMap<Object, Object>();
		retMap.put("lastModiDate", lastModiDate);
		responseBean.setRetMap(retMap);*/
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功");
		//添加日志
		String log = "更新编号为:"+riskSourceDefBean.getSourceNo()+"驱动因子";
		addOperLogInfo(ARSConstants.MODEL_NAME_RISK_WARNING, ARSConstants.OPER_TYPE_3, log);
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月18日 下午2:47:38
	 * @description: TODO(执行具体操作：查询，操作成功后记录日志信息 )
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		RiskSourceDef riskSourceDefBean = (RiskSourceDef) requestBean.getParameterList().get(0);// 全部查询数据
		// RiskSourceDef riskSourceDefBean1 = (RiskSourceDef) requestBean.getParameterList().get(1);// 条件查询数据
		
		String riskTypeSel = riskSourceDefBean.getRiskType();
		String sourceTypeSel = riskSourceDefBean.getSourceType();
		String sourceNameSel = riskSourceDefBean.getSourceName();
		String isNeedSel = riskSourceDefBean.getIsNeed();
		
		// 当前页数
		int pageNum = (int) sysMap.get("currentPage");
		// 每页数量
		int pageSize = 0;
		if (pageNum != -1) {
			int initPageNum = (int) sysMap.get("riskEngineElement_pageNum");
			if (BaseUtil.isBlank(initPageNum + "")) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageNum;
			}
		}
		// 定义分页操作
		Page page = PageHelper.startPage(pageNum, pageSize);
		List<RiskSourceDef> list = new ArrayList<>();
		// 查询分页记录
		if (!BaseUtil.isBlank(riskTypeSel) || !BaseUtil.isBlank(sourceTypeSel) || !BaseUtil.isBlank(sourceNameSel) || !BaseUtil.isBlank(isNeedSel)) {// 条件查询数据
			list = riskSourceDefMapper.selectByCondition(riskTypeSel,sourceTypeSel,sourceNameSel,isNeedSel);
		} else {// 查询所有数据, 传入数据不为空
			list = riskSourceDefMapper.selectAllIgno();
//			list = riskSourceDefMapper.selectAll(riskSourceDefBean);
		}
//		List<RiskSourceDef> list = riskSourceDefMapper.selectAll(riskSourceDefBean);
		List listSouTyp = riskSourceDefMapper.selectAllSourceType();// 查询驱动因素大类
		List listSouNam = riskSourceDefMapper.selectAllSourceName();// 查询驱动因素子类
		// 时间格式化&驱动因素类型设置
		if (null != list && list.size() > 0) {
			for (RiskSourceDef riskSourceDef : list) {
				String modiTime = riskSourceDef.getModiTime();
				if (!BaseUtil.isBlank(modiTime)) {
					// 格式化维护修改时间 -yyyy-MM-dd hh:mm:ss
					String str = modiTime;
					str = str.substring(0, 4)+"-"+str.substring(4, 6)+"-"+str.substring(6, 8)+"  "
							+str.substring(8, 10)+":"+str.substring(10, 12)+":"+str.substring(12, 14);
					riskSourceDef.setModiTime(str);
				}
				// 驱动因素类型设置-(0-风险因素,1-差错因素)
				String riskType = riskSourceDef.getRiskType();
				if (!BaseUtil.isBlank(riskType)) {
					if ("0".equals(riskType)) {
						riskSourceDef.setRiskType("风险因素");
					} else {
						riskSourceDef.setRiskType("差错因素");
					}
				}
			}
		}
		
		// 获取总记录数
		 long totalCount = page.getTotal();
		
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("currentPage", pageNum);
		retMap.put("pageNum", pageSize);
		retMap.put("totalNum", totalCount);
		retMap.put("returnList", list);
		retMap.put("returnListSouTyp", listSouTyp);
		retMap.put("returnListSouNam", listSouNam);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月18日 下午2:47:38
	 * @description: TODO(执行具体操作：其他操作，操作成功后记录日志信息  )
	 */
	@Override
	public void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		
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
/*		String belongOrganNo = (String)sysMap.get("belongOrganNo");
		List tempList = null;
		if(belongOrganNo != null && belongOrganNo.length()>0) { //有指定上级机构的查询指定所属机构列表
			tempList = organInfoDao.getChildOrganList(belongOrganNo);
		}else {
			User user = BaseUtil.getLoginUser();
			tempList = organInfoDao.getUserPrivOrganList(user.getUserNo());
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
					// 临时存储 机构级别
					oztBean.setReserve(value);
				}
			}
			oztBean.setIcon("");
			list.add(oztBean);
		}
		Map map = new HashMap();
		map.put("returnList", list); //可选权限机构
		responseBean.setRetMap(map);*/
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
/*		String userNo = (String)sysMap.get("userNo");
		List<String> privList = null;
		if(userNo != null && userNo.length()>0) {
			privList = userOrganMapper.getPrivOrganByUserNo(userNo);
		}
		Map map = new HashMap();
		map.put("privOrganList", privList);	//已有权限机构
		responseBean.setRetMap(map);*/
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
}
