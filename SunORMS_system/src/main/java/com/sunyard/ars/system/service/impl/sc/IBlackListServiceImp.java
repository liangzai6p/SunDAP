package com.sunyard.ars.system.service.impl.sc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.ExcelUtil;
import com.sunyard.aos.common.util.FileUtil;
import com.sunyard.aos.common.util.HttpUtil;
import com.sunyard.aos.common.util.ImportUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.util.DateUtil;
import com.sunyard.ars.system.service.sc.IBlackListService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;

import oracle.net.aso.b;
import oracle.net.aso.n;

import com.sunyard.ars.system.bean.sc.BlackListBean;
import com.sunyard.ars.system.bean.sc.ExchangeManageBean;
import com.sunyard.ars.system.dao.sc.BlackListMapper;
@Service("IBlackListService")
@Transactional
public class IBlackListServiceImp extends BaseService implements IBlackListService{

	@Resource
	private BlackListMapper BlackListMapper; 

	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);	
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		BlackListBean Model = (BlackListBean)requestBean.getParameterList().get(0);
		Map<String,Object> sysMap = requestBean.getSysMap();
		String oper_type = (String) sysMap.get("oper_type");
		Map<String, Object> map = new HashMap<String, Object>();
		String remark="";
		String iswhiteorgray = Model.getIswhiteorgray();
		if ("0".equals(iswhiteorgray)) {
			remark="白";
		}else if ("2".equals(iswhiteorgray)) {
			remark="黑";
		}else{
			remark="未知";
		}
		//日志信息!
		String  log="";
		if(AOSConstants.OPERATE_ADD.equals(oper_type)){//添加
			Model.setBelongorg(BaseUtil.getLoginUser().getOrganNo());
			Model.setTelephone(DateUtil.getTime19());
			map.put("Bean",Model);
			BlackListMapper.save(map);
			log="所属机构："+Model.getBelongorg()+" 类型："+Model.getCardType()
			+" 证件号码"+Model.getCardNo()+" 名称："+Model.getName()+"在："+DateUtil.getTime19()+"在"+remark+"名单中被添加";
			//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG,ARSConstants.OPER_TYPE_1,log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT,ARSConstants.OPER_TYPE_1,log);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("操作成功");
		}else if(AOSConstants.OPERATE_MODIFY.equals(oper_type)) {//修改
			Model.setTelephone(DateUtil.getTime19());
			map.put("Bean", Model);
			BlackListMapper.update(map);
			log="所属机构："+BaseUtil.getLoginUser().getOrganNo()+" 类型："+Model.getCardType()
			+" 证件号码"+Model.getCardNo()+" 名称："+Model.getName()+"在："+DateUtil.getTime19()+"在"+remark+"名单中被修改";
			//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG,ARSConstants.OPER_TYPE_3,log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT,ARSConstants.OPER_TYPE_3,log);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("操作成功");
		}else if(AOSConstants.OPERATE_DELETE.equals(oper_type)) {//删除
			String var = sysMap.get("system").toString();
			if (var.equals("black")) {
				remark="黑";
			}else if (var.equals("white")) {
				remark="白";
			}else {
				remark="未知名单";
			}
			List<?> delList = requestBean.getParameterList();
			for(int i=0; i<delList.size(); i++) {
				Model = (BlackListBean) delList.get(i);
				map.put("Bean", Model);
				BlackListMapper.delete(map);
				log="ID为"+Model.getId()+"的记录在："+DateUtil.getTime19()+"在"+remark+"名单中被删除";
				//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG,ARSConstants.OPER_TYPE_2,log);
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT,ARSConstants.OPER_TYPE_2,log);
			}
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("操作成功");
		}else if(AOSConstants.OPERATE_QUERY.equals(oper_type)) {//查询
			Map<Object,Object> retMap = new HashMap<Object, Object>();
			int pageNum = Integer.parseInt(sysMap.get("currentPage").toString());
			String expDateS = (String) sysMap.get("expDateS");
			String expDateE = (String) sysMap.get("expDateE");
			String isHis = (String) sysMap.get("isHis");
			String tableName = "ARMS_CSB_WHITEGRAY_NAME";
			//导出的时候不分页
			if(pageNum==-1){
				List<Map<String, Object>> allData = BlackListMapper.selectAll();
				retMap.put("returnList", allData);
			}else{
				int initPageNum = (int) sysMap.get("user_pageNum");
				Page page = PageHelper.startPage(pageNum, initPageNum);
				if(!BaseUtil.isBlank(isHis) && "1".equals(isHis)){//查询历史
					tableName += "_HIS";
				}
				map.put("Bean", Model);
				map.put("tableName", tableName);
				map.put("expDateS", expDateS);
				map.put("expDateE", expDateE);
				map.put("userNo", BaseUtil.getLoginUser().getUserNo());
				map.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
				List<Map<String,Object>> list= getList(BlackListMapper.select(map), page);
				long totalCount = page.getTotal();
				// 拼装返回信息
				retMap.put("currentPage", pageNum);
				retMap.put("pageNum", initPageNum);
				retMap.put("totalNum", totalCount);
				retMap.put("returnList", list);
			}
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else if("OPERATE_IMPORT".equals(oper_type)){//导入黑名单
			blackImportData(requestBean,responseBean);
		}else if("OPERATE_EXPROT".equals(oper_type)){//黑名单导出
			blackExportData(Model,responseBean);
		}else if("downLoad".equals(oper_type)){//导出黑名单模板
			Map<Object,Object> retMap = new HashMap<Object, Object>();
			String path="";
			if (sysMap.get("is_white")==null) {
				//为空导出黑名单模板
				path = HttpUtil.getAbsolutePath("temp\\BlackName.xls");
			}else {
				path = HttpUtil.getAbsolutePath("temp\\whiteName.xls");
			}
			//D:\Tomcat7.0\webapps\SunARS\temp
			//System.out.println("yuanPath:"+path);
			String all=path.replace("\\", "/");
			//System.out.println("xianPath:"+all);
			retMap.put("path", all);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
			
		}else if("getWhiteField".equals(oper_type)){//查询白名单类型列表
			getWhiteField(Model,responseBean);
		}else if("WHITE_EXPROT".equals(oper_type)){//白名单导出
			whiteExportData(Model,requestBean,responseBean);
		}else if("WHITE_IMPORT".equalsIgnoreCase(oper_type)){//白名单导入
			whiteImportData(requestBean,responseBean);
		}else if("WHITE_BATCHMODIFY".equals(oper_type)) {//批量修改到期日期
			List<?> modifyList = requestBean.getParameterList();
			for(int i=0; i<modifyList.size(); i++) {
				Model = (BlackListBean) modifyList.get(i);
				Model.setTelephone(DateUtil.getTime19());
				map.put("Bean", Model);
				BlackListMapper.update(map);
				log="ID为"+Model.getId()+"的记录在："+DateUtil.getTime19()+"在白名单中被修改到期日期为"+Model.getExpDate();
				//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG,ARSConstants.OPER_TYPE_3,log);
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT,ARSConstants.OPER_TYPE_3,log);
			}
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("操作成功");
		}else{
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("查询方法");
		}
	}

	/**
	 * 白名单导入
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void whiteImportData(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		Map<String,Object> sysMap = requestBean.getSysMap();
		Integer headerRowNum = (Integer) sysMap.get("headerRowNum");
		Boolean  isDelete=true;
		String path="";
		BlackListBean model =null;
		Map<String,Object> dataMap =null;
		List<BlackListBean> importList = new ArrayList<BlackListBean>();
		List<HashMap<String, String>> list=(List<HashMap<String, String>>) sysMap.get("uploadFileList");//从这里获取path
		String msg = "";
		if (sysMap.get("entryset_name").toString()=="" || sysMap.get("entryset_name")==null) {
			 responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			 responseBean.setRetMsg("请确定预警模型信息"); 
			 for (int j = 0; j < list.size(); j++) {//作用：当返回错误信息时，删除已经上传的临时文件，不然临时文件会越来越多
				 path=list.get(j).get("saveFileName").toString();
				 if (!BaseUtil.isBlank(path)) {
					 FileUtil.deleteFile(path);
					}
			 }
			 return;
		}
		
		//类型1
		boolean  b=false;
		if (sysMap.get("card_name").toString()=="" || sysMap.get("card_name")==null) {	
			b=true;
		}
		
		//类型
		if (sysMap.get("card_name_1").toString()=="" || sysMap.get("card_name_1")==null) {
			if (b) {
				 responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
				 responseBean.setRetMsg("请确定类型或者类型信息"); 
				 for (int j = 0; j < list.size(); j++) {
					 path=list.get(j).get("saveFileName").toString();
					 if (!BaseUtil.isBlank(path)) {
						 FileUtil.deleteFile(path);
						}
				 }
				 return;
			}
		}
		if (sysMap.get("tran_org").toString()=="" || sysMap.get("tran_org")==null) {
			 responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			 responseBean.setRetMsg("请确定部门信息"); 
			 for (int j = 0; j < list.size(); j++) {
				 path=list.get(j).get("saveFileName").toString();
				 if (!BaseUtil.isBlank(path)) {
					 FileUtil.deleteFile(path);
					}
			 }
			 return;
		}
		String entrysetId = sysMap.get("entryset_name").toString();
		String CardType = sysMap.get("card_name").toString();
		String CardType1 = sysMap.get("card_name_1").toString();
		String tranOrg = sysMap.get("tran_org").toString();
		tranOrg =tranOrg.substring(0, tranOrg.indexOf("-"));
		
		boolean b1=false;//号码
		boolean b2=false;//名称
		int num=0;
		boolean b11=false;//号码1
		boolean b22=false;//名称1
		for (int j = 0; j < list.size(); j++) {
			 path=list.get(j).get("saveFileName").toString();
			 List<HashMap<String, String>> dataList = ImportUtil.importExcel(path, headerRowNum, isDelete);
			 for (int i = 0; i < dataList.size()-1; i++) {
				dataMap=new HashMap<String, Object>();
				int  numLine=i+1;
				model=  new BlackListBean();
				//依次封装模型ID,类型,类型1,机构,所属部门，电话号，白名单标示
				model.setEntrysetId(Integer.parseInt(entrysetId));
				model.setCardType(CardType);
				model.setCardType1(CardType1);
				model.setTranOrg(tranOrg);
				model.setBelongorg(BaseUtil.getLoginUser().getOrganNo());
				model.setTelephone(DateUtil.getTime19());
				model.setIswhiteorgray("0");
				//封装文件中的数据
				if(!BaseUtil.isBlank(dataList.get(i).get("0"))){
					model.setCardNo((dataList.get(i).get("0")));////封装号码
				}else{
					msg += "第" + (numLine) + "行号码为空;";
					b1=true; 
				}
				
				if(!BaseUtil.isBlank(dataList.get(i).get("1"))){
					model.setName(dataList.get(i).get("1"));//封装名称
				}else{
					msg += "第" + (numLine) + "行名称为空;";
					b2=true; 
				}
				
				if(!BaseUtil.isBlank(dataList.get(i).get("2"))){
					model.setRemark(dataList.get(i).get("2"));//封装原因
				}else{
					msg += "第" + (numLine) + "行原因为空;";
				}
				
				
				if(!BaseUtil.isBlank(dataList.get(i).get("3"))){
					if(DateUtil.isDate(dataList.get(i).get("3"))){
						model.setExpDate(dataList.get(i).get("3"));//封装到期日期
					}else{
						responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
						responseBean.setRetMsg("第" + (numLine) + "行到期日期格式非法，请检查");
						return;
					}
				}else{
					responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
					responseBean.setRetMsg("第" + (numLine) + "行到期日期为空，请检查");
					return;
				}

				
				if(!BaseUtil.isBlank(dataList.get(i).get("4"))){
					model.setCardNo1(dataList.get(i).get("4"));////封装号码1
				}else{
					msg += "第" + (numLine) + "行封装号码1为空;";
					//号码已经为空
					if (b1) {
						b11=true;
						num=numLine;
						continue;
					}
				}
				
				if(!BaseUtil.isBlank(dataList.get(i).get("5"))){
					model.setName1(dataList.get(i).get("5"));////封装名称1
				}else{
					msg += "第" + (numLine) + "行封装名称1为空;";
					if (b2) {
						b22=true;
						num=numLine;
						continue;
					}
				}
				//判断是否有重复数据
				List<Map<String,Object>> exist = BlackListMapper.selectExist(model);
				if(exist.size()>0) {
					 String  info= "第" + (numLine) + "行数据已存在;";
					 responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
					 responseBean.setRetMsg(info); 
					 return;
				}
				importList.add(model);
			}
		}
		if (b11 && b1) {
			 responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			 responseBean.setRetMsg("文件第"+num+"行：号码和号码1不能同时没数据"); 
			 return;
		}
		if (b22 && b2) {
			 responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			 responseBean.setRetMsg("文件第"+num+"行：名称和名称1不能同时没数据"); 
			 return;
		}
		
		if (importList.size() == 0) {
			 responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			 responseBean.setRetMsg("无效的文件,文件格式不正确或者上传的文件过大！"); 
			 return;
		}
		/*BlackListBean deleteBean = new BlackListBean();//删除旧记录
		deleteBean.setBelongorg(BaseUtil.getLoginUser().getOrganNo());
		deleteBean.setIswhiteorgray("0");
		dataMap.put("Bean",deleteBean);
		BlackListMapper.delete(dataMap);//123
		String log="ID为"+deleteBean.getId()+"的记录在："+DateUtil.getTime19()+"在白名单中被删除";
		addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG,ARSConstants.OPER_TYPE_2,log);*/
		
		String log="";
		for (BlackListBean blackListBean : importList) {//插入新记录
			if(dataMap!=null){
				dataMap.put("Bean", blackListBean);
				BlackListMapper.save(dataMap);
				log="所属机构："+blackListBean.getBelongorg()+" 类型："+blackListBean.getCardType()
						+" 证件号码"+blackListBean.getCardNo()+" 名称："+blackListBean.getName()+"在："+DateUtil.getTime19()+"在白名单中被添加";
				//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG,ARSConstants.OPER_TYPE_1,log);
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT,ARSConstants.OPER_TYPE_1,log);
			}
		}
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("导入成功行数："+importList.size()+"行; "+msg);
	}

	/**
	 * 白名单导出
	 * @param blackInfo
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void whiteExportData(BlackListBean blackInfo,RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		String fileName = System.currentTimeMillis()+"WhiteInfo.xls";
		String title = "白名单信息";
		Map<String,Object> sysMap = requestBean.getSysMap();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<Object,Object> retMap = new HashMap<Object, Object>();
		LinkedHashMap<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("cardType", "类型");
		headMap.put("cardNo", "号码");
		headMap.put("name", "名称");
		headMap.put("cardType1", "类型1");
		headMap.put("cardNo1", "号码1");
		headMap.put("name1", "名称1");
		headMap.put("entrysetName", "预警模型");
		headMap.put("telephone", "维护日期");
		headMap.put("belongorg", "机构");
		headMap.put("remark", "备注");
		headMap.put("tranOrg", "申请机构");
		headMap.put("expDate", "到期日期");
		
		String expDateS = (String) sysMap.get("expDateS");
		String expDateE = (String) sysMap.get("expDateE");
		String isHis = (String) sysMap.get("isHis");
		String tableName = "ARMS_CSB_WHITEGRAY_NAME";
		if(!BaseUtil.isBlank(isHis) && "1".equals(isHis)){//查询历史
			tableName += "_HIS";
		}
		map.put("Bean", blackInfo);
		map.put("tableName", tableName);
		map.put("expDateS", expDateS);
		map.put("expDateE", expDateE);
		map.put("userNo", BaseUtil.getLoginUser().getUserNo());
		map.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
		List<Map<String,Object>> list = BlackListMapper.select(map);
		List<Map> data = new ArrayList<Map>();
		for (int i = 0; i < list.size(); i++) {
			BlackListBean  obj= new BlackListBean();
			String CardType=(String)list.get(i).get("CARD_NAME");
			if(!(BaseUtil.isBlank(CardType))){
				obj.setCardType(CardType);
			}else{
				obj.setCardType("");
			}
			String CardNo=(String)list.get(i).get("CARD_NO");
			if(!(BaseUtil.isBlank(CardNo))){
				obj.setCardNo(CardNo);
			}else {
				obj.setCardNo("");
			}
			String name=(String)list.get(i).get("NAME");
			if(!(BaseUtil.isBlank(name))){
				obj.setName(name);
			}else{
				obj.setName("");
			}
			String CardType1=(String)list.get(i).get("CARD_NAME_1");
			if(!(BaseUtil.isBlank(CardType1))){
				obj.setCardType1(CardType1);
			}else{
				obj.setCardType1("");
			}
			String CardNo1=(String)list.get(i).get("CARD_NO_1");
			if(!(BaseUtil.isBlank(CardNo1))){
				obj.setCardNo1(CardNo1);
			}else {
				obj.setCardNo1("");
			}
			String name1=(String)list.get(i).get("NAME_1");
			if(!(BaseUtil.isBlank(name1))){
				obj.setName1(name1);
			}else{
				obj.setName1("");
			}
			String ENTRYSET_NAME=list.get(i).get("ENTRYSET_NAME").toString();
			if(!(BaseUtil.isBlank(ENTRYSET_NAME))){
				obj.setEntrysetName(ENTRYSET_NAME);
			}else{
				obj.setEntrysetName("0");
			}
			String belongorg=(String)list.get(i).get("BELONGORG");
			if(!(BaseUtil.isBlank(belongorg))){
				obj.setBelongorg(belongorg);
			}else{
				obj.setBelongorg("");
			}
			String  remark=(String)list.get(i).get("REMARK");
			if(!(BaseUtil.isBlank(remark))){
				obj.setRemark(remark);
			}else{
				obj.setRemark("");
			}
			String  tranOrg=(String)list.get(i).get("TRAN_ORG");
			if(!(BaseUtil.isBlank(tranOrg))){
				obj.setTranOrg(tranOrg);
			}else{
				obj.setTranOrg("");
			}
			String  expDate=(String)list.get(i).get("EXP_DATE");
			if(!(BaseUtil.isBlank(expDate))){
				obj.setExpDate(expDate);
			}else{
				obj.setExpDate("");
			}
			String  telephone=(String)list.get(i).get("TELEPHONE");
			if(!(BaseUtil.isBlank(telephone))){
				obj.setTelephone(telephone);
			}else{
				obj.setTelephone("");
			}
			ObjectMapper objectMapper = new ObjectMapper();
		    String contents = objectMapper.writeValueAsString(obj);
			Map mapdata = objectMapper.readValue(contents, Map.class);
			data.add(mapdata);
		}
		System.out.println(data.size());
		boolean retFlag = ExcelUtil.createExcelFile(HttpUtil.getAbsolutePath("temp"), fileName, title, headMap, data);
		if(retFlag) {
			retMap.put("filePath", HttpUtil.getAbsolutePath("temp")+"/"+fileName);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("生成成功");
		}else {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("xls文件生成失败！");
		}
	}

	/**
	 * 黑名单导入
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void blackImportData(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map<String,Object> sysMap = requestBean.getSysMap();
		Integer headerRowNum = (Integer) sysMap.get("headerRowNum");
		Boolean  isDelete=true;
		String path="";
		BlackListBean model =null;
		Map<String,Object> dataMap =null;
		List<HashMap<String, String>> list=(List<HashMap<String, String>>) sysMap.get("uploadFileList");//从这里获取path
		List<BlackListBean> importList = new ArrayList<BlackListBean>();
		String msg = "";
		for (int j = 0; j < list.size(); j++) {
			 path=list.get(j).get("saveFileName").toString();
			 List<HashMap<String, String>> dataList = ImportUtil.importExcel(path, headerRowNum, isDelete);
			 for (int i = 0; i < dataList.size() - 1; i++) {//最后一行为模板说明
				dataMap=new HashMap<String, Object>();
				model=  new BlackListBean();
				//封装客户类型
				if(!BaseUtil.isBlank(dataList.get(i).get("0"))){
					model.setCustType(dataList.get(i).get("0"));
				//封装类型
				}else{
					msg += "第" + i + "行客户类型为空;";
					continue;
				}
				if("01".equals(dataList.get(i).get("0"))){
					if(!BaseUtil.isBlank(dataList.get(i).get("1"))){
						model.setCardType(dataList.get(i).get("1"));
					//封装号码
					}else{
						msg += "第" + i + "行类型为空;";
						continue;
					}
					if(!BaseUtil.isBlank(dataList.get(i).get("2"))){
						model.setCardNo(dataList.get(i).get("2"));
					//封装名称
					}else{
						msg += "第" + i + "行号码为空;";
						continue;
					}
				}else if("02".equals(dataList.get(i).get("0"))){
					if(!BaseUtil.isBlank(dataList.get(i).get("1"))){
						model.setCardType(dataList.get(i).get("1"));
					//封装号码
					}
					if(!BaseUtil.isBlank(dataList.get(i).get("2"))){
						model.setCardNo(dataList.get(i).get("2"));
					//封装名称
					}
				}
				
				if(!BaseUtil.isBlank(dataList.get(i).get("3"))){
					model.setName(dataList.get(i).get("3"));
				//封装备注
				}
				if(!BaseUtil.isBlank(dataList.get(i).get("4"))){
					model.setRemark((dataList.get(i).get("4")));
				}
				model.setBelongorg(BaseUtil.getLoginUser().getOrganNo());
				model.setEntrysetId(10841);
				model.setTelephone(DateUtil.getTime19());
				model.setIswhiteorgray("2");
				importList.add(model);
			}
		}
		if (importList.size() == 0) {
			 responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			 responseBean.setRetMsg("无效的文件,文件格式不正确或者上传的文件过大！"); 
			 return;
		}
		BlackListBean deleteBean = new BlackListBean();//删除旧记录
		deleteBean.setBelongorg(BaseUtil.getLoginUser().getOrganNo());
		deleteBean.setIswhiteorgray("2");
		if(dataMap!=null){
			dataMap.put("Bean",deleteBean);
		}
		BlackListMapper.delete(dataMap);
		String log="ID为"+deleteBean.getId()+"的记录在："+DateUtil.getTime19()+"在黑名单中被删除";
		//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG,ARSConstants.OPER_TYPE_2,log);
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT,ARSConstants.OPER_TYPE_2,log);
		
		
		for (BlackListBean blackListBean : importList) {//插入新记录
			if(dataMap != null){
				dataMap.put("Bean",blackListBean);
			}

			BlackListMapper.save(dataMap);
			log="所属机构："+blackListBean.getBelongorg()+" 类型："+blackListBean.getCardType()
			+" 证件号码"+blackListBean.getCardNo()+" 名称："+blackListBean.getName()
			+"在："+DateUtil.getTime19()+"在黑名单中被添加";
			//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG,ARSConstants.OPER_TYPE_1,log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT,ARSConstants.OPER_TYPE_1,log);
		}
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("导入成功行数："+importList.size()+"行; "+msg);
	}

	/**
	 * 查询白名单类型列表
	 * @param blackInfo
	 * @param responseBean
	 * @throws Exception
	 */
	private void getWhiteField(BlackListBean blackInfo, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		Map<Object,Object> retMap = new HashMap<Object, Object>();
		map.put("modelId", blackInfo.getEntrysetId());
		List whiteFiledList = BlackListMapper.getWhiteFieldList(map);
		retMap.put("fieldList", whiteFiledList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * 黑名单导出
	 * @param model
	 * @param responseBean
	 */
	private void blackExportData(BlackListBean blackInfo, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		String fileName = System.currentTimeMillis()+"BlackInfo.xls";
		String title = "黑名单信息";
		Map<String, Object> map = new HashMap<String, Object>();
		Map<Object,Object> retMap = new HashMap<Object, Object>();
		LinkedHashMap<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("custType", "客户类型");
		headMap.put("cardType", "类型");
		headMap.put("cardNo", "号码");
		headMap.put("name", "名称");
		headMap.put("entrysetName", "预警模型");
		headMap.put("belongorg", "机构");
		headMap.put("remark", "备注");
		map.put("tableName", "ARMS_CSB_WHITEGRAY_NAME");
		map.put("userNo", BaseUtil.getLoginUser().getUserNo());
		map.put("Bean", blackInfo);
		map.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
		List<Map<String,Object>> list = BlackListMapper.select(map);
		List<Map> data = new ArrayList<Map>();
		for (int i = 0; i < list.size(); i++) {
			BlackListBean  obj= new BlackListBean();
			String CustType =(String)list.get(i).get("CUST_TYPE");
			if("01".equals(CustType)){
				obj.setCustType("个人");
			}else if("02".equals(CustType)){
				obj.setCustType("对公");
			}else{
				obj.setCustType("");
			}
			String CardType=(String)list.get(i).get("CARD_TYPE");
			if(!(BaseUtil.isBlank(CardType))){
				obj.setCardType(CardType);
			}else{
				obj.setCardType("");
			}
			
			String CardNo=(String)list.get(i).get("CARD_NO");
			if(!(BaseUtil.isBlank(CardNo))){
				obj.setCardNo(CardNo);
			}else {
				obj.setCardNo("");
			}
			String name=(String)list.get(i).get("NAME");
			if(!(BaseUtil.isBlank(name))){
				obj.setName(name);
			}else{
				obj.setName("");
			}
			
			String entrysetName= list.get(i).get("ENTRYSET_NAME").toString();
			if(!(BaseUtil.isBlank(entrysetName))){
				obj.setEntrysetName(entrysetName);
			}else{
				obj.setEntrysetName("");
			}
			String belongorg=(String)list.get(i).get("BELONGORG");
			if(!(BaseUtil.isBlank(belongorg))){
				obj.setBelongorg(belongorg);
			}else{
				obj.setBelongorg("");
			}
			String  remark=(String)list.get(i).get("REMARK");
			if(!(BaseUtil.isBlank(remark))){
				obj.setRemark(remark);
			}else{
				obj.setRemark("");
			}
			ObjectMapper objectMapper = new ObjectMapper();
		    String contents = objectMapper.writeValueAsString(obj);
			Map mapdata = objectMapper.readValue(contents, Map.class);
			data.add(mapdata);
		}
		boolean retFlag = ExcelUtil.createExcelFile(HttpUtil.getAbsolutePath("temp"), fileName, title, headMap, data);
		if(retFlag) {
			retMap.put("filePath", HttpUtil.getAbsolutePath("temp")+"/"+fileName);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("生成成功");
		}else {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("xls文件生成失败！");
		}
	}	
}

