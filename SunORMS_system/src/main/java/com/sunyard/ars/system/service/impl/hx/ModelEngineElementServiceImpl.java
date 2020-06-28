package com.sunyard.ars.system.service.impl.hx;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.sunyard.ars.system.bean.hx.EntryRisk;
import com.sunyard.ars.system.bean.hx.ModeEngineEle;
import com.sunyard.ars.system.dao.hx.ArmsEntrySettingMapper;
import com.sunyard.ars.system.dao.hx.EntryRiskMapper;
import com.sunyard.ars.system.dao.hx.ModelEngineEleMapper;
import com.sunyard.ars.system.dao.hx.RiskSourceDefMapper;
import com.sunyard.ars.system.service.hx.IModelEngineElementService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.pojo.User;

/**
 * @author:		 SUNYARD-NGQ
 * @date:		 2018年9月20日 下午7:03:00
 * @description: TODO(模型驱动服务接口实现类 )
 */
@Service("modelEngineElementService")
@Transactional
public class ModelEngineElementServiceImpl extends BaseService implements IModelEngineElementService{

	// 数据库接口
	@Resource
	private EntryRiskMapper entryRiskMapper;
	@Resource
	private RiskSourceDefMapper riskSourceDefMapper;
	@Resource
	private ModelEngineEleMapper modelEngineEleMapper;
	
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月20日 下午7:03:00
	 * @description: TODO(执行接口逻辑 )
	 */
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月20日 下午7:03:00
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
		} else if (ARSConstants.OPERATE_DELETE.equals(oper_type)) { 
			// 删除
			delete(requestBean, responseBean);
		} else if ("sel".equals(oper_type)) { 
			// 下拉框赋值
//			selectChoice(requestBean, responseBean);
		} else if ("rolelist".equals(oper_type)) {
			// 查询role_no
//			rolelist(requestBean, responseBean);
		}
	}
	
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月20日 下午7:03:00
	 * @Description:(执行具体操作：新增，操作成功后记录日志信息)
	 */
	@Override
	public void add(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		EntryRisk entryRiskBean = (EntryRisk) requestBean.getParameterList().get(0);// 获取前端的数据
		// 先确认数据库中是否已经存在相同数据
		List<EntryRisk> list = entryRiskMapper.selectBySelective(entryRiskBean);
		if(null != list && list.size()>0) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("已存在相同数据！");
		}else {// 新增数据
			Integer maxId = add();
			entryRiskBean.setId(maxId);// 设置主键从最大值新增
			// String modiTime = BaseUtil.getCurrentDateStr();// 获取当前时间，作为修改维护时间  当前日期字符串，格式为 yyyyMMdd
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String modiTime = format.format(new Date());
			entryRiskBean.setCreatetime(modiTime);// 作为创建维护时间
			User loginUser = BaseUtil.getLoginUser();
			entryRiskBean.setOptuser(loginUser.getUserNo());// 作为维护人员（柜员）
			entryRiskMapper.insert(entryRiskBean);// 插入数据库信息
//			entryRiskBean.setOptuser(loginUser.getUserName());// 作为维护人员（柜员）
//			entryRiskMapper.insertSelective(entryRiskBean);// 插入数据库信息
			// 设置返回前端的信息
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			// 页面重载时，加载的参数
			retMap.put("createTime", modiTime);
			retMap.put("optuser", loginUser.getUserNo());
//			retMap.put("optuser", loginUser.getUserName());
			retMap.put("id",entryRiskBean.getId());
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("添加成功");
			//添加日志
			String log = "添加id为" + entryRiskBean.getId() + "模型驱动因子";
			addOperLogInfo(ARSConstants.MODEL_NAME_RISK_WARNING, ARSConstants.OPER_TYPE_1, log);
		}
		
	}
	
	//防止高并发环境下ID发生冲突 
	public  synchronized  Integer  add() throws Exception{
		Integer maxId = entryRiskMapper.getMaxId();
		if(maxId==null){
			maxId=0;
		}
		maxId = Integer.valueOf(BaseUtil.filterSqlParam(maxId.toString()));
		return maxId;
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月20日 下午7:03:00
	 * @Description:(执行具体操作：删除，操作成功后记录日志信息)
	 */
	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		List modelEngineList = requestBean.getParameterList();// 获取前端选中的数据集合
		if(modelEngineList != null && modelEngineList.size()>0) {
			for(int i=0; i<modelEngineList.size(); i++) {
				EntryRisk entryRisk = (EntryRisk)modelEngineList.get(i);// 遍历获取选中的实体类
				entryRiskMapper.deleteByPrimaryKey(entryRisk.getId());// 根据主键删除数据
				//添加日志
				String log = "删除id为" + entryRisk.getId() + "模型驱动因子";
				addOperLogInfo(ARSConstants.MODEL_NAME_RISK_WARNING, ARSConstants.OPER_TYPE_3, log);
			}
		}
		// 设置返回信息
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功");
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月20日 下午7:03:00
	 * @Description:(执行具体操作：修改更新，操作成功后记录日志信息)
	 */
	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		// 待开发
		
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月20日 下午7:03:00
	 * @Description:(执行具体操作：查询，操作成功后记录日志信息)
	 */
	@SuppressWarnings("unused")
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		// 获取前端数据信息
		Map sysMap = requestBean.getSysMap();
		ModeEngineEle modeEngineEleBean = (ModeEngineEle) requestBean.getParameterList().get(0);
		/*EntryRisk entryRiskBean = (EntryRisk) requestBean.getParameterList().get(0);*/
		String entryid = modeEngineEleBean.getEntryid();
//		String sourceNo = modeEngineEleBean.getSourceNo();
		String sourceType = modeEngineEleBean.getSourceType();
		String sourceName = modeEngineEleBean.getSourceName();
		//String name = modeEngineEleBean.getName();
		
		// 当前页数
		int pageNum = (int)sysMap.get("currentPage");
		// 每页数量
		int pageSize = 0;
		if (pageNum != -1) {
			int initPageNum = (int) sysMap.get("modelEngine_pageNum");
			if (BaseUtil.isBlank(initPageNum + "")) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageNum;
			}
		}
		// 定义分页操作
		Page page = PageHelper.startPage(pageNum, pageSize);
		// 从list中查询结果，必须放在page对象后面到totalCount前面，分页才可以获取到总数totalCount，否则分页失效begin
		List list = new ArrayList<>();
		if (isNotEmpty(modeEngineEleBean) && (!BaseUtil.isBlank(sourceType) || !BaseUtil.isBlank(entryid) || !BaseUtil.isBlank(sourceName))) {
			list = modelEngineEleMapper.selectByCondition(entryid,sourceType,sourceName,BaseUtil.getLoginUser().getOrganNo());// 条件联合查询所有数据
//			list = entryRiskMapper.selectByCondition(entryid,sourceNo);// 条件联合查询所有数据
		} else {
			list = modelEngineEleMapper.selectBySelectiveTables(BaseUtil.getLoginUser().getOrganNo());// 查询所有数据
		}
		// 从list中查询结果，必须放在page对象后面到totalCount前面，分页才可以获取到总数totalCount，否则分页失效end
		
		// 获取总记录数
		long totalCount = page.getTotal();
		
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("currentPage", pageNum);
		retMap.put("pageNum", pageSize);
		retMap.put("totalNum", totalCount);
		retMap.put("returnList", list);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月27日 下午1:28:04
	 * @description: TODO(判断实体对象是否为空 )
	 */
	public static boolean isEmpty(Object obj) {
		if (null == obj) {
			return true;
		}
		if ((obj instanceof List)) {
			return ((List) obj).size() == 0;
		}
		if ((obj instanceof String)) {
			return ((String) obj).trim().equals("");
		}
		return false;
	}
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月27日 下午1:34:49
	 * @description: TODO(判断对象不为空 )
	 */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月20日 下午7:03:00
	 * @Description:(执行具体操作：其他，操作成功后记录日志信息)
	 */
	@Override
	public void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
