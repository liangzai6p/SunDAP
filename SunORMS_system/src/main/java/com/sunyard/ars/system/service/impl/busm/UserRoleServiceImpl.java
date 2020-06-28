package com.sunyard.ars.system.service.impl.busm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.system.bean.busm.UserRole;
import com.sunyard.ars.system.dao.busm.UserRoleMapper;
import com.sunyard.ars.system.service.busm.IUserRoleService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.pojo.User;

/**
 * @author:		zheng.jw
 * @date:		2017年12月19日 上午9:26:56
 * @Description:(用户权限角色的配置Service实现类)
 */
@Service("userRoleService")
@Transactional
public class UserRoleServiceImpl extends BaseService implements IUserRoleService {

	// 数据库接口
	@Resource
	private UserRoleMapper userRoleMapper;
	
	/**
	 * @author:		zheng.jw
	 * @date:		2017年12月19日 上午9:27:35
	 * @Description:(执行接口逻辑)
	 */
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	/**
	 * @author:		zheng.jw
	 * @date:		2017年12月19日 上午9:28:39
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
	
	@Override
	public void add(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		UserRole userRoleBean = (UserRole) requestBean.getParameterList().get(0);
		List<UserRole> list = userRoleMapper.selectBySelective(userRoleBean,(String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));
		if(list != null && list.size()>0) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("已存在相同数据！");
		}else {
			String lastModiDate = BaseUtil.getCurrentTimeStr();
			userRoleBean.setLastModiDate(lastModiDate);
			userRoleMapper.insertSelective(userRoleBean);
			String	log="角色ID"+userRoleBean.getUserNo()+"角色ID"+userRoleBean.getRoleNo()+"在用户角色表中被创建！";
			//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM, ARSConstants.OPER_TYPE_1, log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			retMap.put("lastModiDate", lastModiDate);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("添加成功");
		}
		
	}

	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		List userRoleNoList = requestBean.getParameterList();
		if(userRoleNoList != null && userRoleNoList.size()>0) {
			User loginUser = BaseUtil.getLoginUser();
			for(int i=0; i<userRoleNoList.size(); i++) {
				UserRole userRole = (UserRole)userRoleNoList.get(i);
				userRoleMapper.deleteByPrimaryKey(userRole.getUserNo(), userRole.getRoleNo(), userRole.getIsOpen(), 
						loginUser.getBankNo(), loginUser.getSystemNo(), loginUser.getProjectNo());
				String	log="角色ID"+userRole.getUserNo()+"角色ID"+userRole.getRoleNo()+"在用户角色表中被删除！";
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
		UserRole userRoleOld = (UserRole) requestBean.getParameterList().get(0);
		UserRole userRoleNew = (UserRole) requestBean.getParameterList().get(1);
		List<UserRole> list = userRoleMapper.selectBySelective(userRoleNew,(String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));
		if(list != null && list.size()>0) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("已存在相同数据！");
		}else {
			String lastModiDate = BaseUtil.getCurrentTimeStr();
			userRoleNew.setLastModiDate(lastModiDate);
			User loginUser = BaseUtil.getLoginUser();
			userRoleNew.setBankNo(loginUser.getBankNo());
			userRoleNew.setSystemNo(loginUser.getSystemNo());
			userRoleNew.setProjectNo(loginUser.getProjectNo());
			userRoleMapper.updateUserRoleSelective(userRoleOld, userRoleNew);
			String	log="角色ID"+userRoleOld.getUserNo()+"角色ID"+userRoleOld.getRoleNo()+"在用户角色表中被修改！";
			//addOperLogInfo(ARSConstants.MODEL_NAME_BUSM, ARSConstants.OPER_TYPE_3, log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			retMap.put("lastModiDate", lastModiDate);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("修改成功");
		}
	}

	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		UserRole userRoleBean = (UserRole) requestBean.getParameterList().get(0);
		userRoleBean.setBankNo(BaseUtil.getLoginUser().getUserNo());//传操作用户到无用字段
		// 当前页数
		int pageNum = (int)sysMap.get("currentPage");
		// 每页数量
		int pageSize = 0;
		if (pageNum != -1) {
			int initPageNum = (int) sysMap.get("userRole_pageNum");
			if (BaseUtil.isBlank(initPageNum + "")) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageNum;
			}
		}
		// 定义分页操作
		Page page = PageHelper.startPage(pageNum, pageSize);
		List list = userRoleMapper.selectBySelective(userRoleBean,(String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));
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

	@Override
	public void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
