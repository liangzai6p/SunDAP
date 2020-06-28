package com.sunyard.ars.system.service.impl.hx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.comm.BaseService;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.system.dao.hx.ArmsEntrySettingMapper;
import com.sunyard.ars.system.service.hx.IArmsEntrySettingService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * @author:		 SUNYARD-NGQ
 * @date:		 2018年9月21日 下午1:43:58
 * @description: TODO(警报实体类设置服务接口实现类 )
 */
@Service("armsEntrySettingService")
@Transactional
public class ArmsEntrySettingServiceImpl extends BaseService implements IArmsEntrySettingService {

	/**
	 * 数据库接口
	 */
	@Autowired
	private ArmsEntrySettingMapper armsEntrySettingMapper;
	
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月21日 下午1:43:58
	 * @description: TODO(执行接口逻辑 )
	 */
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}
	
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月21日 下午1:43:58
	 * @description: TODO(执行具体操作：增、删、改、查 等，操作成功后记录日志信息)
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
		}  else if (ARSConstants.OPERATE_IMPORT.equals(oper_type)) {
            // 导入
            importOperation(requestBean,responseBean);
        } else if ("sel".equals(oper_type)) { 
			// 下拉框赋值
			// selectChoice(requestBean, responseBean);
		} else if ("rolelist".equals(oper_type)) {
			// 查询role_no
			// rolelist(requestBean, responseBean);
		}

	}
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月21日 下午1:43:58
	 * @description: TODO(执行具体操作：增加，操作成功后记录日志信息)
	 */
	@Override
	public void add(RequestBean requestBean, ResponseBean responseBean) throws Exception {
/*		// TODO Auto-generated method stub
		AdFavorite adFavorite = (AdFavorite) requestBean.getParameterList().get(0);// 获取前台页面实体类对象
//		AdFavorite adFavoriteA = adFavoriteMapper.selectByPrimaryKey(adFavorite.getFormId());
		List<AdFavorite> list = adFavoriteMapper.selectBySelective(adFavorite);// 查询数据库中是否有相同数据
		if(null != list && list.size() > 0) {// 如果list 非空则得出数据库中有相同数据
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("已存在相同数据！");
		}else {
			// String lastModiDate = BaseUtil.getCurrentTimeStr();// 数据修改时间 
			// adFavoriteA.setLastModiDate(lastModiDate);// 设置数据库修改时间
			adFavoriteMapper.insertSelective(adFavorite);// 向数据库插入数据
			Map<Object, Object> retMap = new HashMap<Object, Object>();// 新建返回前端的map对象
			// retMap.put("lastModiDate", lastModiDate);// 返回前端页面显示修改时间
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("添加成功");
		}*/
		
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月21日 下午1:43:58
	 * @description: TODO(执行具体操作：删除，操作成功后记录日志信息)
	 */
	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
/*		// TODO Auto-generated method stub
		List adFavoriteNoList = requestBean.getParameterList();// 取出页面选中要删除的数据
		if(adFavoriteNoList != null && adFavoriteNoList.size()>0) {// 判断list是否为空
			// User loginUser = BaseUtil.getLoginUser();// 获取当前使用的用户对象，设置权限
			for(int i=0; i<adFavoriteNoList.size(); i++) {
				AdFavorite adFavorite = (AdFavorite)adFavoriteNoList.get(i);// 遍历所有的选中数据
				adFavoriteMapper.deleteByPrimaryKey(adFavorite.getFormId());// 根据主键formId删除数据
			}
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功");*/
	}
	
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月21日 下午1:43:58
	 * @description: TODO(执行具体操作：修改，操作成功后记录日志信息)
	 */
	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean) throws Exception {
/*		// TODO Auto-generated method stub
		 AdFavorite adFavoriteOld = (AdFavorite) requestBean.getParameterList().get(0);// 获取选中行的封装对象
		AdFavorite adFavoriteNew = (AdFavorite) requestBean.getParameterList().get(1);// 获取修改后的数据封装对象
		List<AdFavorite> list = adFavoriteMapper.selectBySelective(adFavoriteNew);// 从数据库中查出所有数据
		if(list != null && list.size()>0) {// 从数据库中查出判断是否有相同数据，若list不是空，就有相同数据，则给出提示
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("已存在相同数据！");
		}else {// 数据库中没有相同数据，存入数据库中
			// 设置修改时间
			// String lastModiDate = BaseUtil.getCurrentTimeStr();// 获取当前时间
			// adFavoriteNew.setLastModiDate(lastModiDate);// 记录数据库修改时间
			adFavoriteMapper.updateByPrimaryKey(adFavoriteNew);// 更新数据库数据
			Map<Object, Object> retMap = new HashMap<Object, Object>();// 新建map，封装传回前端的数据
			// retMap.put("lastModiDate", lastModiDate);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("修改成功");
		}*/
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月21日 下午1:43:58
	 * @description: TODO(执行具体操作：查询，操作成功后记录日志信息)
	 */
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
//		User currentUser = BaseUtil.getLoginUser();
//		String organNo = currentUser.getOrganNo();
		Map sysMap = requestBean.getSysMap();// 获取前端传回的数据封装对象sysMap
//		AdFavorite adFavoriteBean = (AdFavorite) requestBean.getParameterList().get(0);// 获取数据对象
//		String isShare = adFavoriteBean.getIsShare();
//		String formId = adFavoriteBean.getFormId();// 获取数据的formId,主键
		// 当前页数
		int pageNum = (int)sysMap.get("currentPage");// 获取当前页
		// 每页数量
		int pageSize = 0;
		if (pageNum != -1) {
			int initPageNum = (int) sysMap.get("externalCase_pageNum");// 获取初始化页码
			if (BaseUtil.isBlank(initPageNum + "")) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageNum;
			}
		}
		// 定义分页操作
		List list = new ArrayList();// 初始化list对象
		list = armsEntrySettingMapper.selectAllName();
//		AdFavorite externalCaseDetail = null;// 定义对象
		Page page = PageHelper.startPage(pageNum, pageSize);
//		if (!BaseUtil.isBlank(formId)) {// formId不为空：查看案例详情逻辑，则查询指定主键的所有的数据
//			externalCaseDetail = adFavoriteMapper.selectByPrimaryKey(formId);
//		} else {// 查询所有的数据库列表初始化数据
//			list = adFavoriteMapper.selectByConditionSelective(organNo);
//			if ("1".equals(isShare)) {// 查询所有的数据库列表初始化数据(1-全行共享)
//			} else{// 查询所有的数据库列表初始化数据(0-分行内部查看)
//				list = adFavoriteMapper.selectByConditionSelective(adFavoriteBean,organNo);
//			}
//		}
		// 获取总记录数
		long totalCount = page.getTotal();
		
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("currentPage", pageNum);
		retMap.put("pageNum", pageSize);
		retMap.put("totalNum", totalCount);
		retMap.put("returnList", list);
//		retMap.put("externalCaseDetail", externalCaseDetail);
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月21日 下午1:43:58
	 * @description: TODO(执行具体操作：导入文件，操作成功后记录日志信息)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void importOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		// 获取导入参数
/*		int failNum = 0;
		int successNum = 0;
		List nameList = (List) requestBean.getSysMap().get("uploadFileList");
		Map map = (Map) nameList.get(0);
		String filePath = (String) map.get("saveFileName");
		int headerRowNum = (int) requestBean.getSysMap().get("headerRowNum");
		String module_no = "#" + requestBean.getSysMap().get("module_no");
		String table_name = (String) requestBean.getSysMap().get("table_name");
		List pkList = (List) requestBean.getSysMap().get("pkList");
		String procedure_name =(String)requestBean.getSysMap().get("procedure_name");
		List columnInfoList = (List)requestBean.getSysMap().get("columnInfoList");
		List queryList = (List) requestBean.getSysMap().get("queryList");
		HashMap condMap = new HashMap();
		condMap.put("table_name", table_name);
//		condMap.put("module_no", module_no);
		String add_way = (String) requestBean.getSysMap().get("add_way");
		condMap.put("add_way", add_way);
		// 查询所有记录 page
		Page page = PageHelper.startPage(0, 0);
		// 查询表头信息

		List columnList = getList(cmModularDao.onlyColumns(condMap), page);
		List<HashMap<String, String>> list = ImportUtil.importExcel(filePath,
				headerRowNum, true); // 获取excel信息
		String user_no = (String) requestBean.getSysMap().get("user_no");
		long start_time = System.currentTimeMillis();
		for (int i = 0; i < list.size(); i++) {// 遍历list
			//用存储过程时
			List inList =new ArrayList();
			if (!BaseUtil.isBlank(procedure_name)) {

				HashMap xmap = new HashMap();
				//基础参数_操作类型
				xmap.put("name", "I_OPERATE");
				xmap.put("data", AOSConstants.OPERATE_IMPORT);
				xmap.put("columntype","VARCHAR");
				inList.add(xmap);

				//用户
				HashMap map2 = new HashMap();
				map2.put("name","I_USER_NO");
				map2.put("data", BaseUtil.getLoginUser().getUserNo());
				map2.put("columntype","VARCHAR");
				inList.add(map2);

				//机构
				HashMap map3 = new HashMap();
				map3.put("name","I_ORGAN_NO");
				map3.put("data", BaseUtil.getLoginUser().getOrganNo());
				map3.put("columntype","VARCHAR");
				inList.add(map3);

				//操作的表名
				HashMap map4 = new HashMap();
				map4.put("name","I_TABLE_NAME");
				map4.put("data",table_name);
				map4.put("columntype","VARCHAR");
				inList.add(map4);

				//查询入参
				for (int j = 0;j < queryList.size(); j++) {
					HashMap diaMap = (HashMap) queryList.get(j);
					HashMap dataMap =new HashMap();
					dataMap.put("name", diaMap.get("elem_english"));
					dataMap.put("data","");
					String type = searchType(columnInfoList,(String) diaMap.get("elem_english"), null);
					dataMap.put("columntype", type);
					if ("2".equals(diaMap.get("show_type"))) {
						HashMap dataMap2 =new HashMap();
						dataMap2.put("name",diaMap.get("elem_english"));
						dataMap2.put("data","");
						dataMap2.put("columntype", type);
						inList.add(dataMap2);
					}
					inList.add(dataMap);
				}

				//新增
				Map<String, String> qMap = list.get(i);// 获取qmap
				if (qMap.size() != columnList.size()) {
					failNum++;
					continue;
				}
				if (mapIsAllNull(qMap)) {
					continue;
				}
				List dataList = new ArrayList();
				for (Map.Entry<String, String> entry : qMap.entrySet()) {
					int idx = Integer.parseInt(entry.getKey());
					if (idx >= 0) {
						HashMap dataMap = new HashMap();
						HashMap tempMap = new HashMap();
						tempMap = (HashMap) columnList.get(idx);
						dataMap.put("name", tempMap.get("name"));
						dataMap.put("data", entry.getValue());
						String type = searchType(columnInfoList,(String) tempMap.get("name"), null);
						dataMap.put("columntype", type);
						inList.add(dataMap);
						dataList.add(dataMap);
					}
				}
				HashMap modiMap = new HashMap();
				modiMap.put("name", "I_K_LAST_MODI_DATE");
				modiMap.put("data",BaseUtil.getCurrentTimeStr());
				modiMap.put("columntype","VARCHAR");
				inList.add(modiMap);
				HashMap totalMap = new HashMap();
				if (!"2".equals(add_way)) {
					HashMap customNoMap = new HashMap();
					customNoMap.put("name", "I_K_CUSTOM_NO");
					customNoMap.put("data",BaseUtil.getSerialNumber());
					customNoMap.put("columntype","VARCHAR");
					inList.add(customNoMap);
				}else {
					for (int j = 0;j < pkList.size(); j++) {
						HashMap dataMap =new HashMap();
						dataMap.put("name", pkList.get(j));
						dataMap.put("data","");
						String type = searchType(columnInfoList,(String)pkList.get(j), null);
						dataMap.put("columntype", type);
						inList.add(dataMap);
					}
				}
				totalMap.put("inList", inList);
				totalMap.put("procedure_name", procedure_name);
				//回参
				totalMap.put("O_RET","0");
				totalMap.put("O_MSG", "0");
				totalMap.put("O_SQL", "O_SQL");
				cmModularDao.userProcedure(totalMap);
				if (!"0".equals((String) totalMap.get("O_RET"))) {
					throw new RuntimeException((String) totalMap.get("O_MSG"));
				}
				 successNum ++ ;
			}else {

				Map<String, String> qMap = list.get(i);// 获取qmap
				if (qMap.size() != columnList.size()) {
					failNum++;
					continue;
				}
				if (mapIsAllNull(qMap)) {
					continue;
				}
				condMap.put("custom_no", BaseUtil.getSerialNumber());

				List dataList = new ArrayList();
				for (Map.Entry<String, String> entry : qMap.entrySet()) {
					int idx = Integer.parseInt(entry.getKey());
					if (idx >= 0) {
						HashMap dataMap = new HashMap();
						HashMap tempMap = new HashMap();
						tempMap = (HashMap) columnList.get(idx);
						dataMap.put("name", tempMap.get("name"));
						dataMap.put("data", entry.getValue());
						dataList.add(dataMap);
					}
				}
				Map map2 = new HashMap();
				map2.put("name", "last_modi_date");
				map2.put("data", BaseUtil.getCurrentTimeStr());
				dataList.add(map2);
				condMap.put("list", dataList);
				List pkDataList = new ArrayList();
				List noPkDataList = new ArrayList();
				if ("2".equals(add_way)) {
					for (int j = 0; j < pkList.size(); j++) {
						for (int k = 0; k < dataList.size(); k++) {
							if (((Map) dataList.get(k)).get("name").equals(pkList.get(j))) {
								pkDataList.add((Map) dataList.get(k));
							}
						}
					}
					for (int j = 0; j < dataList.size(); j++) {
						int x = 0;
						for (int k = 0; k < pkList.size(); k++) {
							if (((Map) dataList.get(j)).get("name").equals(pkList.get(k))) {
								x++;
							}
						}
						if (x == 0) {
							noPkDataList.add((Map) dataList.get(j));
						}
					}
					condMap.put("pkDataList", pkDataList);
					if (noPkDataList.size() == 0) {
						cmModularDao.insert(condMap);
					}else {
						condMap.put("noPkDataList", noPkDataList);
						cmModularDao.importInsert(condMap);
					}
				}else {
					//新增sql
					 cmModularDao.insert(condMap);
				}
				 successNum ++ ;
			}


		}
		long end_time = System.currentTimeMillis();
		logger.info(user_no +"导入花费" + (end_time - start_time)) ;
		String retMsg = "";
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		if (successNum == 0) {
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			retMsg = "导入数据为空";
		} else if (failNum == 0) {
			retMsg = "导入成功"*/
	}
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月21日 下午1:43:58
	 * @description: TODO(执行具体操作：其他待定 等，操作成功后记录日志信息)
	 */
	@Override
	public void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
