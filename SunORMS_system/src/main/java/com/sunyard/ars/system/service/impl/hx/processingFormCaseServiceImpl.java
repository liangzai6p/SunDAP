package com.sunyard.ars.system.service.impl.hx;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * 预警信息案例的查询 删除 查看详情
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.ExcelUtil;
import com.sunyard.aos.common.util.HttpUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.pojo.Dictionary;
import com.sunyard.ars.common.util.ParamUtil;
import com.sunyard.ars.system.dao.hx.ArmsEntrySettingMapper;
import com.sunyard.ars.system.dao.hx.ArmsFavoriteMapper;
import com.sunyard.ars.system.dao.sc.DictionaryMapper;
import com.sunyard.ars.system.service.hx.IEalyWarningInfoCaseService;
import com.sunyard.ars.system.service.hx.IProcessingFormCaseService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.pojo.User;

@Service("processingFormCaseService")
@Transactional
public class processingFormCaseServiceImpl extends BaseService implements IProcessingFormCaseService {

	@Resource
	private ArmsEntrySettingMapper armsEntrySettingMapper;
	@Resource
	private ArmsFavoriteMapper armsFavoriteMapper;
	@Resource
	private DictionaryMapper dictionaryMapper;

	@Override

	public ResponseBean execute(RequestBean requestBean) throws Exception {
		// TODO Auto-generated method stub
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}
	@SuppressWarnings("rawtypes")
	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		// 获取操作标识
		String oper_type = (String) sysMap.get("oper_type");
		if (ARSConstants.OPERATE_QUERY.equals(oper_type)) {
			// 查询
			query(requestBean, responseBean);
		} else if("favoriteExport".equals(oper_type)){
			favoriteExport(requestBean, responseBean);
		}else if(ARSConstants.OPERATE_DELETE.equals(oper_type)){
			deleteFavorite(requestBean, responseBean);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void deleteFavorite(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		// 获取参数集合
        Map sysMap = requestBean.getSysMap();
        List<Map> delList= (List<Map>) sysMap.get("delList"); 
        List<String> deleteList = new ArrayList<String>();
		for (Map str : delList) {
			deleteList.add((str.get("ID")).toString());
		}
        HashMap condMap = new HashMap();
		condMap.put("deleteList", deleteList);
		condMap.put("favoriteType", "1");
		condMap.put("favoriteUser", BaseUtil.getLoginUser().getUserNo());// 获取当前用户的机构号
        int delFlag = armsFavoriteMapper.deleteFavorite(condMap);
        
        if(delFlag > 0){
        	responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        	responseBean.setRetMsg("删除成功！");
        }else{
        	responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
        	responseBean.setRetMsg("删除失败！");
        }
	}
	public void  favoriteExport(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		Map sysMap = requestBean.getSysMap();
		String fileName = System.currentTimeMillis()+"FavoriteData.xls";
		String title = "入库信息记录";
		//设置展现头
		LinkedHashMap<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("FORM_ID_1", "预警单编号");
		headMap.put("ENTRY_NAME", "警报名称");
		headMap.put("ALARM_LEVEL", "警报级别");
		headMap.put("NET_NO", "机构代码");
		headMap.put("NET_NAME", "机构名称");
		headMap.put("BACK_DATE", "警报下发日期");
		headMap.put("FORM_TYPE", "类型");
		headMap.put("FAVORITE_TIME", "入库日期");
		headMap.put("ISCOMMON", "是否公有");
		headMap.put("FAVORITE_USER", "入库操作员");
		headMap.put("FAVORITE_ORGAN", "入库机构");
		headMap.put("REMARK", "入库备注");
		
		//查询条件开始
		String currentOrganNo = BaseUtil.getLoginUser().getOrganNo();
		String queryOrganNo = (String) sysMap.get("queryOrganNo");
		String inStoreTimeStart = (String) sysMap.get("inStoreTimeStart");
		String inStoreTimeEnd = (String) sysMap.get("inStoreTimeEnd");
		Map<String,Object> paraMap = new HashMap<String,Object>();
		String FORM_ID =(String) sysMap.get("FORM_ID");
		String FAVORITE_USER =  (String)sysMap.get("FAVORITE_USER");
		String ENTRY_NAME =  (String)sysMap.get("ENTRY_NAME");
		String BUSI_NO_CT =  (String)sysMap.get("BUSI_NO_CT");
		String FORM_TYPE =  (String)sysMap.get("FORM_TYPE");
		String XF_DATE_START =  (String)sysMap.get("XF_DATE_START");
		String XF_DATE_END =  (String)sysMap.get("XF_DATE_END");
		String T3_ORGAN_NO =  (String)sysMap.get("T3_ORGAN_NO");
		paraMap.put("FORM_ID", FORM_ID);
		paraMap.put("FAVORITE_USER", FAVORITE_USER);
		paraMap.put("ENTRY_NAME", ENTRY_NAME);
		paraMap.put("BUSI_NO_CT", BUSI_NO_CT);
		paraMap.put("FORM_TYPE", FORM_TYPE);
		paraMap.put("XF_DATE_START", XF_DATE_START.replaceAll("-", ""));
		paraMap.put("XF_DATE_END", XF_DATE_END.replaceAll("-", ""));
		paraMap.put("T3_ORGAN_NO", T3_ORGAN_NO);
		paraMap.put("currentOrganNo", currentOrganNo);
		paraMap.put("queryOrganNo", queryOrganNo);
		paraMap.put("inStoreTimeStart", inStoreTimeStart);
		paraMap.put("inStoreTimeEnd", inStoreTimeEnd);
		
		//导出全部！！！ 根据查询条件查出数据
		List<Map> data = new ArrayList<Map>();
		List<Map<Object, String>> exportlist = armsFavoriteMapper.getAlarmBillData(paraMap);
		Map<Object, String> map=null;
		for (int i = 0; i < exportlist.size(); i++) {
			map =exportlist.get(i);
			/*if((map.containsKey("FORM_ID_2"))) {
				data.add(map);
			}*/
			if("1".equals(map.get("ISCOMMON"))){
				map.put("ISCOMMON", "公有");
			}else{
				map.put("ISCOMMON", "非公有");
			}
			if("1".equals(map.get("ALARM_LEVEL"))){
				map.put("ALARM_LEVEL", "一级");
			}else if("2".equals(map.get("ALARM_LEVEL"))){
				map.put("ALARM_LEVEL", "二级");
			}else if("3".equals(map.get("ALARM_LEVEL"))){
				map.put("ALARM_LEVEL", "三级");
			}else if("4".equals(map.get("ALARM_LEVEL"))){
				map.put("ALARM_LEVEL", "提示");
			}else{
				map.put("ALARM_LEVEL", "");
			}
			if("0".equals(map.get("FORM_TYPE"))){
				map.put("FORM_TYPE", "差错单");
			}else if("2".equals(map.get("FORM_TYPE"))){
				map.put("FORM_TYPE", "提示单");
			}else if("3".equals(map.get("FORM_TYPE"))){
				map.put("FORM_TYPE", "预警单");
			}else{
				map.put("FORM_TYPE", "");
			}
			data.add(map);
		}
		Map<Object,Object> retMap = new HashMap<Object, Object>();
		boolean retFlag = ExcelUtil.createExcelFile(HttpUtil.getAbsolutePath("temp"), fileName, title, headMap, data);
		if(retFlag) {
			retMap.put("filePath", HttpUtil.getAbsolutePath("temp")+"/"+fileName);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("生成成功");
			//添加日志
			String log = "案例库处理单案例导出数据";
			addOperLogInfo(ARSConstants.MODEL_NAME_CASE_LIBRARY, ARSConstants.OPER_TYPE_4_2, log);
		}else {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("xls文件生成失败！");
		}
	}
	/**
	 * 查询符合条件的预警信息案例(依据机构,输入的条件查找)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		int currentPage = (int) sysMap.get("currentPage");
		int pageSize = 0;
		if (currentPage != -1) {
			Integer initPageSize = (Integer) sysMap.get("pageSize");
			if (initPageSize == null) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageSize;
			}
		}
		/*String queryOrganNo = ParamUtil.getParamValue(requestBean, "queryOrganNo");
		String inStoreTimeStart = ParamUtil.getParamValue(requestBean, "inStoreTimeStart");
		String inStoreTimeEnd = ParamUtil.getParamValue(requestBean, "inStoreTimeEnd");
		String FORM_ID = ParamUtil.getParamValue(requestBean, "form_id");
		String FAVORITE_USER = ParamUtil.getParamValue(requestBean, "favorite_user");
		String ENTRY_NAME = ParamUtil.getParamValue(requestBean, "entry_name");
		String BUSI_NO_CT = ParamUtil.getParamValue(requestBean, "busi_no_ct");
		String FORM_TYPE = ParamUtil.getParamValue(requestBean, "form_type");
		String XF_DATE_START = ParamUtil.getParamValue(requestBean, "xf_date_start");
		String XF_DATE_END = ParamUtil.getParamValue(requestBean, "xf_date_end");
		String T3_ORGAN_NO = ParamUtil.getParamValue(requestBean, "t3_organ_no");*/
		String queryOrganNo = (String) sysMap.get("queryOrganNo");
		String inStoreTimeStart = (String) sysMap.get("inStoreTimeStart");
		String inStoreTimeEnd = (String) sysMap.get("inStoreTimeEnd");
		String FORM_ID = (String) sysMap.get("FORM_ID") ;
		String FAVORITE_USER = (String) sysMap.get("FAVORITE_USER") ;
		String ENTRY_NAME = (String) sysMap.get("ENTRY_NAME");
		String BUSI_NO_CT = (String) sysMap.get("BUSI_NO_CT") ;
		String FORM_TYPE = (String) sysMap.get("FORM_TYPE");
		String XF_DATE_START = (String) sysMap.get("XF_DATE_START") ;
		String XF_DATE_END = (String) sysMap.get("XF_DATE_END");
		String T3_ORGAN_NO = (String) sysMap.get("T3_ORGAN_NO");
		Map paraMap = new HashMap();
		paraMap.put("FORM_ID", FORM_ID);
		paraMap.put("FAVORITE_USER", FAVORITE_USER);
		paraMap.put("ENTRY_NAME", ENTRY_NAME);
		paraMap.put("BUSI_NO_CT", BUSI_NO_CT);
		paraMap.put("FORM_TYPE", FORM_TYPE);
		paraMap.put("XF_DATE_START", XF_DATE_START.replaceAll("-", ""));
		paraMap.put("XF_DATE_END", XF_DATE_END.replaceAll("-", ""));
		paraMap.put("T3_ORGAN_NO", T3_ORGAN_NO);
		paraMap.put("queryOrganNo", queryOrganNo);
		paraMap.put("inStoreTimeStart", inStoreTimeStart);
		paraMap.put("inStoreTimeEnd", inStoreTimeEnd);
		paraMap.put("currentOrganNo", BaseUtil.getLoginUser().getOrganNo());// 获取当前用户的机构号
		Map retMap = new HashMap();
		Page page = PageHelper.startPage(currentPage, pageSize);
		List<Map<Object, String>> returnListA = armsFavoriteMapper.getAlarmBillData(paraMap);// 通过条件查询本机构有权限的所有处理单案例信息

		retMap.put("currentPage", currentPage);
		retMap.put("pageSize", pageSize);
		retMap.put("totalNum", page.getTotal());
		retMap.put("returnList", returnListA);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");

	}

}
