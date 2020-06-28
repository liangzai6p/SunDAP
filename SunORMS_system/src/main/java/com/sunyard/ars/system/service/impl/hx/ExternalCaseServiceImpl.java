package com.sunyard.ars.system.service.impl.hx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sunyard.ars.common.util.date.DateUtil;
import com.sunyard.ars.system.dao.hx.AdFavoriteMapper;
import com.sunyard.cop.IF.common.http.RequestUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.HttpUtil;
import com.sunyard.aos.common.util.ImportUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.hx.AdFavorite;
import com.sunyard.ars.system.service.hx.IExternalCaseService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.pojo.User;

import javax.annotation.Resource;

/**
 * @author:		 SUNYARD-NGQ
 * @date:		 2018年9月11日 上午11:27:44
 * @description: TODO(操作外部案例服务接口实现类 )
 */
@Service("externalCaseService")
@Transactional
public class ExternalCaseServiceImpl extends BaseService implements IExternalCaseService {

	/**
	 * 数据库接口，外部案例
	 */
	@Resource
	private AdFavoriteMapper adFavoriteMapper;
	
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月11日 上午11:27:44
	 * @description: TODO(执行接口逻辑 )
	 */
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}
	
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月11日 上午11:27:44
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
        } 

	}
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月11日 上午11:27:44
	 * @description: TODO(执行具体操作：增加，操作成功后记录日志信息)
	 */
	@Override
	public void add(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
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

			//添加日志 
			String log = "案例库的外部案例添加单据id为" + adFavorite.getFormId()+"的数据!";
			addOperLogInfo(ARSConstants.MODEL_NAME_CASE_LIBRARY, ARSConstants.OPER_TYPE_1, log);
		}
		
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月11日 上午11:27:44
	 * @description: TODO(执行具体操作：删除，操作成功后记录日志信息)
	 */
	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		List adFavoriteNoList = requestBean.getParameterList();// 取出页面选中要删除的数据
		if(adFavoriteNoList != null && adFavoriteNoList.size()>0) {// 判断list是否为空
			// User loginUser = BaseUtil.getLoginUser();// 获取当前使用的用户对象，设置权限
			for(int i=0; i<adFavoriteNoList.size(); i++) {
				AdFavorite adFavorite = (AdFavorite)adFavoriteNoList.get(i);// 遍历所有的选中数据
				adFavoriteMapper.deleteByPrimaryKey(adFavorite.getFormId());// 根据主键formId删除数据
				//添加日志 
				String log = "案例库的外部案例删除单据id为" + adFavorite.getFormId()+"的数据!";
				addOperLogInfo(ARSConstants.MODEL_NAME_CASE_LIBRARY, ARSConstants.OPER_TYPE_2, log);
			}
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功");
	}
	
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月11日 上午11:27:44
	 * @description: TODO(执行具体操作：修改，操作成功后记录日志信息)
	 */
	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
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
			//添加日志 
			String log = "案例库的外部案例修改单据id为" + adFavoriteNew.getFormId()+"的数据!";
			addOperLogInfo(ARSConstants.MODEL_NAME_CASE_LIBRARY, ARSConstants.OPER_TYPE_3, log);
		}
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月11日 上午11:27:44
	 * @description: TODO(执行具体操作：查询，操作成功后记录日志信息)
	 */
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		User currentUser = BaseUtil.getLoginUser();
		String organNo = currentUser.getOrganNo();
		Map sysMap = requestBean.getSysMap();// 获取前端传回的数据封装对象sysMap
		AdFavorite adFavoriteBean = (AdFavorite) requestBean.getParameterList().get(0);// 获取数据对象
		String isShare = adFavoriteBean.getIsShare();
		String formId = adFavoriteBean.getFormId();// 获取数据的formId,主键
		String organInfo = adFavoriteBean.getOrganName();
		String transDate = adFavoriteBean.getTransDate();
		
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
		Page page = PageHelper.startPage(pageNum, pageSize);
		List list = new ArrayList();// 初始化list对象
		AdFavorite externalCaseDetail = null;// 定义对象
		String hasPrivOrganFlag = (String) RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG");//如果没有权限机构则查询所属机构


		if (!BaseUtil.isBlank(formId)) {// formId不为空：查看案例详情逻辑，则查询指定主键的所有的数据
//			externalCaseDetail = adFavoriteMapper.selectByPrimaryKey(formId);
			list = adFavoriteMapper.selectByPrimaryKey(formId);
		} else if(!BaseUtil.isBlank(organInfo) || !BaseUtil.isBlank(transDate)){
//			String organNo = organInfo.split("-")[0].trim();
//			String organName = organInfo.split("-")[1].trim();
			list = adFavoriteMapper.selectByCondition(organInfo,transDate,currentUser.getUserNo(),hasPrivOrganFlag);
		} else {// 查询所有的数据库列表初始化数据
			list = adFavoriteMapper.selectByConditionSelective(currentUser.getUserNo(),hasPrivOrganFlag);
			/*if ("1".equals(isShare)) {// 查询所有的数据库列表初始化数据(1-全行共享)
			} else{// 查询所有的数据库列表初始化数据(0-分行内部查看)
				list = adFavoriteMapper.selectByConditionSelective(adFavoriteBean,organNo);
			}*/
		}
		// 获取总记录数
		long totalCount = page.getTotal();
		
		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("currentPage", pageNum);
		retMap.put("pageNum", pageSize);
		retMap.put("totalNum", totalCount);
		retMap.put("returnList", list);
		retMap.put("externalCaseDetail", externalCaseDetail);
		String path = HttpUtil.getAbsolutePath("temp");
		retMap.put("path", path);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	//防止高并发环境下ID发生冲突
	public  synchronized  Integer  add() throws Exception{
		Integer maxId = adFavoriteMapper.getMaxId();
		if(maxId==null){
			maxId=0;
		}
		return maxId+1;
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月11日 上午11:27:44
	 * @description: TODO(执行具体操作：导入文件，操作成功后记录日志信息)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void importOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取导入参数
		Map sysMap = requestBean.getSysMap();
		Integer headerRowNum = (Integer) sysMap.get("headerRowNum");
		Boolean  isDelete=true;
		String path="";
		AdFavorite  model=null;
		Map<String,Object> dataMap=null;
		List<HashMap<String, String>> list=(List<HashMap<String, String>>) sysMap.get("uploadFileList");//从这里获取path
		for (int j = 0; j < list.size(); j++) {
			 path=list.get(j).get("saveFileName").toString();
			 List<HashMap<String, String>> dataList = ImportUtil.importExcel(path, headerRowNum, isDelete);
			 for (int i = 0; i < dataList.size()-1; i++) {
					dataMap=new HashMap<String, Object>();
					model=  new AdFavorite();
//					Integer maxId = add();
//					++maxId;
//					model.setFormId(maxId+"");
					saveAll(model, dataList, i);
//					dataMap.put("Bean",model);
//					adFavoriteMapper.save(dataMap);
					adFavoriteMapper.insert(model);
			 }
		}
		
		//添加日志 
		String log = "案例库的外部案例导入数据!";
		addOperLogInfo(ARSConstants.MODEL_NAME_CASE_LIBRARY, ARSConstants.OPER_TYPE_1_2, log);
	}

	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年10月8日 上午10:12:18
	 * @description: TODO(向数据库中设置数据 )
	 */
	public void saveAll(AdFavorite model, List<HashMap<String, String>> dataList, int i) throws Exception {
		// 风险类型id
		if(!BaseUtil.isBlank(dataList.get(i).get("0"))){
			model.setRiskTypeId(dataList.get(i).get("0"));
		}
		// 风险类别名称
		if(!BaseUtil.isBlank(dataList.get(i).get("1"))){
			model.setRiskTypeName(dataList.get(i).get("1"));
		}
		// 机构号
		if(!BaseUtil.isBlank(dataList.get(i).get("2"))){
			model.setOrganNo(dataList.get(i).get("2"));
		}
		// 机构名称
		if(!BaseUtil.isBlank(dataList.get(i).get("3"))){
			model.setOrganName(dataList.get(i).get("3"));
		}
		//封装客户类型
		if(!BaseUtil.isBlank(dataList.get(i).get("4"))){
			 model.setTransDate(dataList.get(i).get("4"));
		}
		// 交易时间
		if(!BaseUtil.isBlank(dataList.get(i).get("5"))){
			model.setTransTime(dataList.get(i).get("5"));
		}
		// 客户号
		if(!BaseUtil.isBlank(dataList.get(i).get("6"))){
			model.setCustNo(dataList.get(i).get("6"));
		}
		// 客户名
		if(!BaseUtil.isBlank(dataList.get(i).get("7"))){
			model.setCustName(dataList.get(i).get("7"));
		}
		// 账号
		if(!BaseUtil.isBlank(dataList.get(i).get("8"))){
			model.setAcctNo(dataList.get(i).get("8"));
		}
		// 账户名称
		if(!BaseUtil.isBlank(dataList.get(i).get("9"))){
			model.setAcctName(dataList.get(i).get("9"));
		}
		// 客户经理号
		if(!BaseUtil.isBlank(dataList.get(i).get("10"))){
			model.setPrimAoNo(dataList.get(i).get("10"));
		}
		// 客户经理名称
		if(!BaseUtil.isBlank(dataList.get(i).get("11"))){
			model.setPrimAoName(dataList.get(i).get("11"));
		}
		// 对手客户号
		if(!BaseUtil.isBlank(dataList.get(i).get("12"))){
			model.setAdvsCustNo(dataList.get(i).get("12"));
		}
		//对手客户名称
		if(!BaseUtil.isBlank(dataList.get(i).get("13"))){
			model.setAdvsCustNanme(dataList.get(i).get("13"));
		}
		// 对手账号
		if(!BaseUtil.isBlank(dataList.get(i).get("14"))){
			model.setAdvsAcctNo(dataList.get(i).get("14"));
		}
		// 对手账户名
		if(!BaseUtil.isBlank(dataList.get(i).get("15"))){
			model.setAdvsAcctName((dataList.get(i).get("15")));
		}
		// 经办柜员号
		if(!BaseUtil.isBlank(dataList.get(i).get("16"))){
			model.setTellerNo(dataList.get(i).get("16"));
		}
		// 经办柜员名称
		if(!BaseUtil.isBlank(dataList.get(i).get("17"))){
			model.setTellerName(dataList.get(i).get("17"));
		}
		// 授权柜员号
		if(!BaseUtil.isBlank(dataList.get(i).get("18"))){
			model.setTellerNo1(dataList.get(i).get("18"));
		}
		// 授权柜员名称
		if(!BaseUtil.isBlank(dataList.get(i).get("19"))){
			model.setTellerName1(dataList.get(i).get("19"));
		}
		// 交易码
		if(!BaseUtil.isBlank(dataList.get(i).get("20"))){
			model.setTxCode((dataList.get(i).get("20")));
		}
		// 交易名称
		if(!BaseUtil.isBlank(dataList.get(i).get("21"))){
			model.setTxName(dataList.get(i).get("21"));
		}
		// 流水号
		if(!BaseUtil.isBlank(dataList.get(i).get("22"))){
			model.setFlowId(dataList.get(i).get("22"));
		}
		// 借贷方向
		if(!BaseUtil.isBlank(dataList.get(i).get("23"))){
			model.setCdFlag(dataList.get(i).get("23"));
		}
		// 币种
		if(!BaseUtil.isBlank(dataList.get(i).get("24"))){
			model.setCurrency(dataList.get(i).get("24"));
		}
		// 交易金额
		if(!BaseUtil.isBlank(dataList.get(i).get("25"))){
			BigDecimal transAmo = new BigDecimal(dataList.get(i).get("25"));
			model.setTransAmount(transAmo);
		}
		// 事件金额
		if(!BaseUtil.isBlank(dataList.get(i).get("26"))){
			BigDecimal eventAmount = new BigDecimal(dataList.get(i).get("26"));
			model.setEventAmount(eventAmount);
		}
		// 损失金额
		if(!BaseUtil.isBlank(dataList.get(i).get("27"))){
			BigDecimal lostAmount = new BigDecimal(dataList.get(i).get("27"));
			model.setLostAmount(lostAmount);
		}
		// 事件描述
		if(!BaseUtil.isBlank(dataList.get(i).get("28"))){
			model.setEventContext(dataList.get(i).get("28"));
		}
		// 事件处理过程
		if(!BaseUtil.isBlank(dataList.get(i).get("29"))){
			model.setDealDescription(dataList.get(i).get("29"));
		}
		// 风险分析
		if(!BaseUtil.isBlank(dataList.get(i).get("30"))){
			model.setRiskContext(dataList.get(i).get("30"));
		}
		// 预警id
		if(!BaseUtil.isBlank(dataList.get(i).get("31"))){
			model.setWarnId(dataList.get(i).get("31"));
		}
		// 入库人员
		/*if(!BaseUtil.isBlank(dataList.get(i).get("32"))){
			model.setAddUser(dataList.get(i).get("32"));
		}
		// 入库时间
		if(!BaseUtil.isBlank(dataList.get(i).get("33"))){
			model.setAddTime(dataList.get(i).get("33"));
		}*/
		// 入库备注
		if(!BaseUtil.isBlank(dataList.get(i).get("32"))){
			model.setAddRemark(dataList.get(i).get("32"));
		}
		// 状态
		if(!BaseUtil.isBlank(dataList.get(i).get("33"))){
			model.setStatus((dataList.get(i).get("33")));
		}
		// 是否共享
		if(!BaseUtil.isBlank(dataList.get(i).get("34"))){
			model.setIsShare(dataList.get(i).get("34"));
		}

        model.setAddUser(BaseUtil.getLoginUser().getUserNo());//入库人员是当前登录人员
        model.setAddTime(DateUtil.getNewDate(DateUtil.FORMATE_DATE));//入库日期是当前系统日期
        model.setBankCode(BaseUtil.getLoginUser().getOrganNo());//入库机构时当前登录机构
        // 入库机构
		/*if(!BaseUtil.isBlank(dataList.get(i).get("37"))){
			model.setBankCode(dataList.get(i).get("37"));
		}*/
	}
	/**
	 * @author:		 SUNYARD-NGQ
	 * @date:		 2018年9月11日 上午11:27:44
	 * @description: TODO(执行具体操作：其他待定 等，操作成功后记录日志信息)
	 */
	@Override
	public void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
