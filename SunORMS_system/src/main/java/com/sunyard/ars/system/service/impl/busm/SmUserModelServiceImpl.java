package com.sunyard.ars.system.service.impl.busm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.busm.SmUserModel;
import com.sunyard.ars.system.dao.busm.SmUserModelMapper;
import com.sunyard.ars.system.service.busm.ISmUserModelService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.pojo.User;

@Service("smUserModelService")
@Transactional
public class SmUserModelServiceImpl extends BaseService implements ISmUserModelService {

	@Autowired
	private SmUserModelMapper smUserModelMapper;
	
	
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
		@SuppressWarnings("unchecked")
		Map<Object, Object> sysMap = requestBean.getSysMap();
		// 获取操作标识
		String oper_type = (String) sysMap.get("oper_type");
		if (ARSConstants.OPERATE_QUERY.equals(oper_type)) {
			// 查询
			queryQdUserLeave(requestBean, responseBean);
			return;
		}
		if (ARSConstants.OPERATE_ADD.equals(oper_type)) {
			// 新增
			addQdUserLeave(requestBean, responseBean);
			return;
		}
		if (ARSConstants.OPERATE_MODIFY.equals(oper_type)) {
			// 修改
			modifyQdUserLeave(requestBean, responseBean);
			return;
		}
		if (ARSConstants.OPERATE_DELETE.equals(oper_type)) {
			// 删除
			deleteQdUserLeave(requestBean, responseBean);
			return;
		}
		if ("UPDATE".equals(oper_type)) {
			// 删除
			update(requestBean, responseBean);
			return;
		}if ("saveModel".equals(oper_type)) {
			saveModel(requestBean, responseBean);
			return;
		}
	}

	//批量保存
	private void saveModel(RequestBean requestBean, ResponseBean responseBean) {
		User user = BaseUtil.getLoginUser();
		SmUserModel smUserModel = (SmUserModel) requestBean.getParameterList().get(0);
		Map<Object, Object> sysMap = requestBean.getSysMap();
		SmUserModel record=new SmUserModel();
		
		//删除所有的模型！
		smUserModelMapper.deleteByRoleNo(smUserModel.getRoleNo());
		//添加所有的模型！
		String ModelNodes = sysMap.get("ModelNodes").toString();
		String[] ModelSplitArr = ModelNodes.split(",");
		for (int i = 0; i < ModelSplitArr.length; i++) {
			record.setRoleNo(smUserModel.getRoleNo());
			record.setIsOpen(smUserModel.getIsOpen());
			record.setModelId(ModelSplitArr[i]);
			record.setLastModiDate(BaseUtil.getCurrentTimeStr());
			record.setIsLock(user.getUserNo());
			if (!BaseUtil.isBlank(ModelSplitArr[i])) {
				smUserModelMapper.insertSelective(record);
			}
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("添加成功");
	}

	/**
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 * @author 查询
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryQdUserLeave(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		SmUserModel smUserModel = (SmUserModel) requestBean.getParameterList().get(0);
		List<SmUserModel> smUserModelList = smUserModelMapper.selectBySelective(smUserModel);
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("smUserModelList", smUserModelList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 * @author 新增
	 */
	private void addQdUserLeave(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		SmUserModel smUserModel = (SmUserModel) requestBean.getParameterList().get(0);
		SmUserModel smUserModelExit = smUserModelMapper.selectByPrimaryKey(smUserModel.getRoleNo(),smUserModel.getModelId());
		if (smUserModelExit != null) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("已存在相同数据！");
		} else {
			smUserModelMapper.insert(smUserModel);
			//添加日志
			String	log="添加数据"+smUserModel.getRoleNo()+","+smUserModel.getModelId()+","+smUserModel.getIsLock()+","+smUserModel.getIsOpen()+","+smUserModel.getLastModiDate();
			addOperLogInfo(ARSConstants.MODEL_NAME_QD, ARSConstants.OPER_TYPE_1, log);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("添加成功");
		}
	}

	/**
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 * @author 修改
	 */
	private void modifyQdUserLeave(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		SmUserModel smUserModel = (SmUserModel) requestBean.getParameterList().get(0);
		smUserModelMapper.updateByPrimaryKeySelective(smUserModel);
		//添加日志
		String	log="修改数据"+smUserModel.getRoleNo()+","+smUserModel.getModelId()+","+smUserModel.getIsLock()+","+smUserModel.getIsOpen()+","+smUserModel.getLastModiDate();
		addOperLogInfo(ARSConstants.MODEL_NAME_QD, ARSConstants.OPER_TYPE_3, log);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("更新成功");
	}

	/**
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 * @author guYD  删除
	 */
	private void deleteQdUserLeave(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		List<Object> smUserModelBeanList = requestBean.getParameterList();
		for (int i = 0; i < smUserModelBeanList.size(); i++) {
			SmUserModel smUserModel = (SmUserModel) smUserModelBeanList.get(i);
			//添加日志
			String	log="删除"+smUserModel.getRoleNo()+","+smUserModel.getModelId()+","+smUserModel.getIsLock()+","+smUserModel.getIsOpen()+","+smUserModel.getLastModiDate();
			addOperLogInfo(ARSConstants.MODEL_NAME_QD, ARSConstants.OPER_TYPE_2, log);
			smUserModelMapper.deleteByPrimaryKey(smUserModel.getRoleNo(),smUserModel.getModelId());
			
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功");
	}
	
	private void update(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		List<Object> smUserModelList = requestBean.getParameterList();
		User user = BaseUtil.getLoginUser();
		for(Object obj:smUserModelList) {
			SmUserModel smUserModelTemp = (SmUserModel)obj;
			smUserModelTemp.setIsOpen("1");//默认启用
			smUserModelTemp.setIsLock(user.getUserNo());
			smUserModelTemp.setLastModiDate(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			if (smUserModelTemp.getNodeType().equals("addNodes")) {
				SmUserModel smUserModelExit = smUserModelMapper.selectByPrimaryKey(smUserModelTemp.getRoleNo(),smUserModelTemp.getModelId());
				if (smUserModelExit == null) {
					smUserModelMapper.insert(smUserModelTemp);
					//添加日志
					String	log="添加数据"+smUserModelTemp.getRoleNo()+","+smUserModelTemp.getModelId()+","+smUserModelTemp.getIsLock()+","+smUserModelTemp.getIsOpen()+","+smUserModelTemp.getLastModiDate();
					addOperLogInfo(ARSConstants.MODEL_NAME_QD, ARSConstants.OPER_TYPE_1, log);
				}
				continue;
			}
			if (smUserModelTemp.getNodeType().equals("deleteNodes")) {
				//添加日志
				String	log="删除"+smUserModelTemp.getRoleNo()+","+smUserModelTemp.getModelId()+","+smUserModelTemp.getIsLock()+","+smUserModelTemp.getIsOpen()+","+smUserModelTemp.getLastModiDate();
				addOperLogInfo(ARSConstants.MODEL_NAME_QD, ARSConstants.OPER_TYPE_2, log);
				smUserModelMapper.deleteByPrimaryKey(smUserModelTemp.getRoleNo(),smUserModelTemp.getModelId());
				continue;
			}
			if (smUserModelTemp.getNodeType().equals("updateNodes")) {
				smUserModelMapper.updateByPrimaryKeySelective(smUserModelTemp);
				//添加日志
				String	log="修改数据"+smUserModelTemp.getRoleNo()+","+smUserModelTemp.getModelId()+","+smUserModelTemp.getIsLock()+","+smUserModelTemp.getIsOpen()+","+smUserModelTemp.getLastModiDate();
				addOperLogInfo(ARSConstants.MODEL_NAME_QD, ARSConstants.OPER_TYPE_3, log);
				continue;
			}
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("配置成功");
	}
}
