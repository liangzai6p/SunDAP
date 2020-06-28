package com.sunyard.ars.system.service.impl.sc;

import java.math.BigDecimal;
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
import com.sunyard.aos.common.util.HttpUtil;
import com.sunyard.aos.common.util.ImportUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.sc.SmDayoffTb;
import com.sunyard.ars.system.bean.sc.FieldDef;
import com.sunyard.ars.system.service.sc.ISmDayoffTbService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.ars.system.dao.sc.SmDayoffTbMapper;

@Service
@Transactional
public class SmDayoffTbServiceImp extends BaseService implements ISmDayoffTbService {

	@Resource
	private SmDayoffTbMapper smDayoffTbMapper;
	
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);	
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
				Map sysMap = requestBean.getSysMap();
				// 获取操作标识
				String oper_type = (String) sysMap.get("oper_type");
				if (ARSConstants.OPERATE_QUERY.equals(oper_type)) {
					// 查询
					//queryOperation(requestBean,responseBean);
				} else if (ARSConstants.OPERATE_MODIFY.equals(oper_type)) {
					// 修改操作
					modifOperation(requestBean,responseBean);
				} else if (ARSConstants.OPERATE_ADD.equals(oper_type)) {
					// 新增
					//addOperation(requestBean,responseBean);
				} else if (ARSConstants.OPERATE_DELETE.equals(oper_type)) {
					// 删除
					//deleteOperation(requestBean,responseBean);
				} else if (ARSConstants.OPERATE_IMPORT.equals(oper_type)) {
					// 导入
					importOperation(requestBean,responseBean);
				}else if ("smDayoffTbQuery".equals(oper_type)) {
					smDayoffTbQuery(requestBean,responseBean);
				}
}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void smDayoffTbQuery(RequestBean requestBean,
			ResponseBean responseBean) throws Exception {
		// 构造条件参数
		//HashMap condMap = new HashMap();
		//condMap = addExtraCondition(condMap);
		List smDayoffTbList = BaseUtil.convertListMapKeyValue(smDayoffTbMapper.smDayoffTbQuery());
		Map retMap = new HashMap();
		
		retMap.put("festivalDatasList", smDayoffTbList);
		// 拼装返回信息
		String path = HttpUtil.getAbsolutePath("temp");
		retMap.put("path", path);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * @author:		zheng.jw
	 * @throws Exception 
	 * @date:		2017年12月20日 下午4:19:16
	 * @Description:导入节假日信息
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void importOperation(RequestBean requestBean,ResponseBean responseBean) throws Exception {
		//获取前台传入参数
//		int failNum = 0 ;
		int successNum = 0;
		String userNo=BaseUtil.getLoginUser().getUserNo();
		List nameList = (List)requestBean.getSysMap().get("uploadFileList");
		Map map = (Map)nameList.get(0);
		String filePath = (String)map.get("saveFileName");
		int headerRowNum = (int)requestBean.getSysMap().get("headerRowNum");
		List<HashMap<String,String>> list = ImportUtil.importExcel(filePath,headerRowNum,true); //导入excel数据
		//String user_no = (String)requestBean.getSysMap().get("user_no");
		// 构造条件
		HashMap condMap = new HashMap();
		//condMap.put("is_lock", user_no);
		//condMap = addExtraCondition(condMap);
		long start_time = System.currentTimeMillis();
		for (int i = 0; i < list.size(); i++) { // 循环插入导入数据
			Map qMap = list.get(i);				// 逐个定义变量，看是否需要处理
//			System.out.println(qMap);
			/*if (BaseUtil.mapIsAllNull(qMap)){
				System.out.println("第" + i + "条数据为空" );
				continue;
			}*/
			//String last_modi_date = BaseUtil.getCurrentTimeStr();
			String  isOpen= qMap.containsKey("1")?qMap.get("1").toString():"";	// 是否节假日
			String offDate = qMap.get("0").toString().replaceAll("\\-", "");  // 日期
			//String is_open = "1";  						 // 默认启用
			//condMap.put("last_modi_date", last_modi_date);
			condMap.put("offDate", offDate);
			condMap.put("isOpen", isOpen);
			condMap.put("isLock",userNo);
			/*//判断节假日表中是否已存在此节假日中的日期
			if (!BaseUtil.isBlank(getString(SmDayoffTbMapper.selectDateName(condMap)))){
				failNum++;
				continue;
			}*/
			if(1 >= smDayoffTbMapper.update(condMap)) {
				successNum++;
			};
			
			//记录日志
			String logContent = "导入节假日信息，||{日期："
					+ offDate + "，是否节假日：" + isOpen
					+ " }";
			//addLogInfo(logContent);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, logContent);
		}
		long end_time = System.currentTimeMillis();
		logger.info( "批量导入用时：" + (end_time - start_time)) ;
		String retMsg = "";
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		if (successNum == 0) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			retMsg = "导入失败,无信息导入（检查模板是否符合规定）";
		} else {
			retMsg = "成功导入" + successNum + "条数据。";
		}
		/*
		else if (failNum == 0) {
			retMsg = "成功导入" + successNum + "条数据。";
		} else {
			retMsg = "成功导入" + successNum + "条数据，有" + failNum + "条数据无法导入。";
		}*/
		
		responseBean.setRetMsg(retMsg);		
	}

	/**
	 * @author:		zheng.jw
	 * @throws Exception 
	 * @date:		2017年12月20日 下午4:05:53
	 * @Description:删除节假日信息
	 */
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	private void deleteOperation(RequestBean requestBean,ResponseBean responseBean) throws Exception {
		// 获取前台传送批量删除信息
		List del_nos = requestBean.getParameterList();
		String date_ids = "";
		List<String> date_idsList = new ArrayList<>();
		for (int i = 0;i < del_nos.size();i++) {
			SmDayoffTb bean = (SmDayoffTb) del_nos.get(i);
			date_ids += "'" + bean.getId() + "'";
			if (i < del_nos.size() - 1) {
				date_ids += ",";
			}
			date_idsList.add(bean.getId());
		}
		// 构造条件
		HashMap condMap = new HashMap();
		condMap.put("date_ids", date_idsList);
		condMap = addExtraCondition(condMap);
		// 执行Sql
		smDayoffTbMapper.delete(condMap);
		// 返回信息
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功");
		
		// 记录日志
		String logContent = "删除节假日信息，||{ 删除了节假日id为：" + date_ids+"的节假日}";
		addLogInfo(logContent);
	}*/

	/**
	 * @author:		zheng.jw
	 * @throws Exception 
	 * @date:		2017年12月20日 下午3:57:59
	 * @Description:节假日修改
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void modifOperation(RequestBean requestBean,ResponseBean responseBean) throws Exception {
		String userNo=BaseUtil.getLoginUser().getUserNo();
		String last_modi_date = BaseUtil.getCurrentTimeStr();
		for(int i = 0; i < requestBean.getParameterList().size();i++){
			SmDayoffTb smDayoffTb =(SmDayoffTb) requestBean.getParameterList().get(i);
			String offDate = smDayoffTb.getOffDate().replaceAll("\\-", "");
			String isOpen = smDayoffTb.getIsOpen();
			// 构造条件参数
			HashMap condMap = new HashMap();
			condMap.put("offDate", offDate);
			condMap.put("isOpen", isOpen);
			condMap.put("isLock", userNo);
			//condMap = addExtraCondition(condMap);
			//修改SQL语句
			smDayoffTbMapper.update(condMap);
			//记录日志
			String logContent = "修改节假日信息，||{ 节假日日期：" + offDate + "修改成节假日状态："
					+ isOpen + "}";
			//addLogInfo(logContent);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, logContent);
		}
		//构建返回map，更新数据
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		Map responseMap = new HashMap();
		responseMap.put("last_modi_date",last_modi_date);
		responseBean.setRetMsg("修改节假日成功！");
		responseBean.setRetMap(responseMap);		
	}

	/**
	 * @author:		zheng.jw
	 * @throws Exception 
	 * @date:		2017年12月20日 下午3:35:40
	 * @Description:新增节假日信息
	 */
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map sysMap = requestBean.getSysMap();
		SmDayoffTb smDayoffTb =(SmDayoffTb) requestBean.getParameterList().get(0);
		String start_date = smDayoffTb.getStart_date().replaceAll("\\-", "");
		String end_date = smDayoffTb.getEnd_date().replaceAll("\\-", "");
		String is_lock = sysMap.get("user_no").toString();						// 新增用户
		String last_modi_date = BaseUtil.getCurrentTimeStr();
		String date_id = BaseUtil.getSerialNumber();
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("smDayoffTb", smDayoffTb);
		condMap.put("start_time", start_date);
		condMap.put("end_time", end_date);
		condMap.put("last_modi_date", last_modi_date);
		condMap.put("date_id", date_id);
		condMap.put("is_lock", is_lock);
		condMap = addExtraCondition(condMap);
		if (!BaseUtil.isBlank(getString(smDayoffTbMapper.selectDateName(condMap)))){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("新增节假日中包含已存在节假日期！");
			return;
		}
		
		//新增SQL语句
		smDayoffTbMapper.insert(condMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		
		//保存日志
		String logContent = "新增节假日信息，||{发布人" + is_lock + " ，节假日ID：" + date_id + "，开始日期：" + start_date + "结束日期："+end_date+"}";
		addLogInfo(logContent);
		//构建返回map，更新数据
		Map responseMap = new HashMap();
		responseMap.put("date_id",date_id);
		responseMap.put("is_lock",is_lock);
		responseMap.put("last_modi_date",last_modi_date);
		responseBean.setRetMsg("新增节假日成功！");
		responseBean.setRetMap(responseMap);		
	}*/

	/**
	 * @author:		zheng.jw
	 * @date:		2017年12月20日 下午2:03:43
	 * @Description:查询用户信息操作
	 */
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		//获取查询条件
		SmDayoffTb SmDayoffTb =(SmDayoffTb) requestBean.getParameterList().get(0);
		String start_time = sysMap.get("start_time").toString().replaceAll("\\-", "");				//查询条件开始时间
		String end_time = sysMap.get("end_time").toString().replaceAll("\\-", "");					//查询条件结束时间
		
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("SmDayoffTb", SmDayoffTb);
		condMap.put("start_time", start_time);
		condMap.put("end_time", end_time);
		condMap = addExtraCondition(condMap);
		
		// 当前页数
		int pageNum = (int) sysMap.get("currentPage");
		// 每页数量
		int pageSize = 0;
		if (pageNum != -1) {
			int initPageNum = (int) sysMap.get("SmDayoffTb_pageNum");
			if (BaseUtil.isBlank(initPageNum + "")) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageNum;
			}
		}
		// 定义分页操作
		Page page = PageHelper.startPage(pageNum, pageSize);
		// 查询分页记录
		List list = getList(smDayoffTbMapper.select(condMap), page);
		// 获取总记录数
		long totalCount = page.getTotal();
		
		// 拼装返回信息
		String path = HttpUtil.getAbsolutePath("template");
		Map retMap = new HashMap();
		retMap.put("path", path);
		retMap.put("currentPage", pageNum);
		retMap.put("pageNum", pageSize);
		retMap.put("totalNum", totalCount);
		retMap.put("returnList", list);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}*/
}
