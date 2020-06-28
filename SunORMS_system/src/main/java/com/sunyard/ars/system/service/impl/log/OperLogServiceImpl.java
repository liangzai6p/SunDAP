package com.sunyard.ars.system.service.impl.log;

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
import com.sunyard.ars.common.dao.FlowMapper;
import com.sunyard.ars.common.dao.TmpBatchMapper;
import com.sunyard.ars.common.pojo.Dictionary;
import com.sunyard.ars.system.bean.log.OperLog;
import com.sunyard.ars.system.dao.busm.OrganInfoDao;
import com.sunyard.ars.system.dao.busm.TellerMapper;
import com.sunyard.ars.system.dao.log.OperLogMapper;
import com.sunyard.ars.system.dao.sc.DictionaryMapper;
import com.sunyard.ars.system.service.log.IOperLogService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.pojo.User;

@Service("logService")
@Transactional
public class OperLogServiceImpl extends BaseService  implements IOperLogService{
	
	@Resource
	private OperLogMapper operLogMapper;
	@Resource
	private DictionaryMapper dictionaryMapper;
	@Resource
	private OrganInfoDao organInfoDao;
	@Resource
	private TellerMapper tellerMapper;
	@Resource
	private FlowMapper flowMapper;
	@Resource
	private TmpBatchMapper tmpBatchMapper;
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
		} else if("LOG_EXPORT".equals(oper_type)){
			logExport(requestBean, responseBean);
		}else if("reLogQuery".equals(oper_type)) {
			reLogQuery(requestBean, responseBean);
		}
	}
	
	/**
	 * 查询展现字段
	 * @param requestBean
	 * @param responseBean
	 */
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();	
	//	User user = BaseUtil.getLoginUser();
	//	String currentUserOrgan=user.getOrganNo();
//		OperLog operLog = (OperLog)requestBean.getParameterList().get(0);
		Map map = (Map)sysMap.get("mlist");//接收从页面传输的数据
		String startDate = null;
		String endDate = null;
		String operModule = null;
		String operType = null;
		String operNo = null;
		String operOrgan = null;
		String operContent = null;
		for(Object key:map.keySet()){
			if("startDate".equals((String)key)){
				 startDate = (String) map.get(key);
				 startDate = startDate.replace("-","");//更改页面传输过来的日期的格式
			}else if("endDate".equals((String)key)){
				endDate = (String)map.get(key);
				endDate = endDate.replace("-","");
			}else if("operateModel".equals((String)key)){
				operModule = (String)map.get(key);
			}else if("operateType".equals((String)key)){
				 operType = (String)map.get(key);
			}else if("operator".equals((String)key)){
				operNo = (String)map.get(key);
			}else if("operateOrgan".equals((String)key)){
				operOrgan = ((String)map.get(key));
				if(operOrgan!=null && !"".equals(operOrgan)){
				   operOrgan=operOrgan.substring(0, operOrgan.indexOf("-"));
				}
			}else if("operateContent".equals((String)key)){
				operContent = (String)map.get(key);
			}
		}

			HashMap condMap = new HashMap();
			condMap.put("operatorNo", operNo);
			condMap.put("operType", operType);
			condMap.put("operContent", operContent);
			condMap.put("operModule", operModule);
			condMap.put("startDate", startDate);
			condMap.put("endDate", endDate);
			condMap.put("operOrgan", operOrgan);
			condMap.put("userNo", BaseUtil.getLoginUser().getUserNo());
			condMap.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
			int currentPage = (int)sysMap.get("currentPage");
			int pageSize = 0;
			if(currentPage != -1) {
				Integer initPageSize = (Integer) sysMap.get("pageSize");
				if (initPageSize == null) {
					pageSize = ARSConstants.PAGE_NUM;
				} else {
					pageSize = initPageSize;
				}
			}		
			Page page = PageHelper.startPage(currentPage, pageSize);
			//执行查询
			List<OperLog> resultList = operLogMapper.queryBySelective(condMap);//通过模糊查询从SM_OPERLOG_TB中取数据
 
			Map retMap = new HashMap();
			long totalCount = page.getTotal();			
			retMap.put("currentPage", currentPage);
			retMap.put("pageSize", pageSize);
			retMap.put("allRow", totalCount);
			retMap.put("resultList", resultList);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
	
		
	}
	/**
	 * 日志信息后台导出
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void logExport(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		String fileName = System.currentTimeMillis() + "log.xls";// 设置要导出的日志的文件名
		String title = "日志信息";// 设置导出文件的标题
		Map sysMap = requestBean.getSysMap();
		Map<Object, Object> retMap = new HashMap<Object, Object>();
		String startDate = (String) sysMap.get("startDate");// 获取页面传过来的开始日期
		startDate = startDate.replace("-", "");// 更改页面传输过来的日期的格式
		String endDate = (String) sysMap.get("endDate");// 获取页面传过来的结束日期
		endDate = endDate.replace("-", "");// 更改页面传输过来的日期的格式
		String operModule = (String) sysMap.get("operateModel");// 获取页面传过来的模块
		String operType = (String) sysMap.get("operateType");// 获取页面传过来的操作类型
		String operOrgan = (String) sysMap.get("operateOrgan");// 获取页面传过来的操作机构
		if (operOrgan != null && !"".equals(operOrgan)) {// 取传过来的操作机构横杠以前的数字
			operOrgan = operOrgan.substring(0, operOrgan.indexOf("-"));
		}
		String operContent = (String) sysMap.get("operateContent");
		String operNo = (String) sysMap.get("operator");// 获取面传过来的操作员
		HashMap condMap = new HashMap();//组装查询条件
		condMap.put("operatorNo", operNo);
		condMap.put("operType", operType);
		condMap.put("operContent", operContent);
		condMap.put("operModule", operModule);
		condMap.put("startDate", startDate);
		condMap.put("endDate", endDate);
		condMap.put("operOrgan", operOrgan);
		condMap.put("userNo", BaseUtil.getLoginUser().getUserNo());//获取登录用户的用户号
		condMap.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
		List<Map> resultList = operLogMapper.queryByCondition(condMap);
		for (Map operLogMap : resultList) {//遍历查询出来的日志信息
			Dictionary dict_param = new Dictionary();
			dict_param.setFieldName(BaseUtil.filterSqlParam("LOG_OPERTYPE_" + operLogMap.get("OPER_MODULE")));//组装要从数据字典里面查询的FIELD_NAME条件
			List<Dictionary> operTypeList = dictionaryMapper.selectBySelective(dict_param);
			for (Dictionary dictionary : operTypeList) {//遍历从数据字典里面查找出的数据 ,操作类型从数据字典里面查询
				if (operLogMap.get("OPER_TYPE").equals(dictionary.getCode())) {//与从日志表中查找出的OPER_TYPE相比相等就改变value
					operLogMap.put("OPER_TYPE", BaseUtil.filterHeader(dictionary.getContent()));
				}
			}
			Dictionary dict = new Dictionary();
			dict.setFieldName("LOG_OPERMODULE");//设置要从数据字典查询的条件
			List<Dictionary> moduleList = dictionaryMapper.selectBySelective(dict);
			for (Dictionary dictionary : moduleList) {//把操作模块改成中文
				if (operLogMap.get("OPER_MODULE").equals(dictionary.getCode())) {
					operLogMap.put("OPER_MODULE", BaseUtil.filterHeader(dictionary.getContent()));
				}
			}
			operLogMap.put("OPER_TIME",BaseUtil.filterHeader((String) operLogMap.get("OPER_DATE") + "" + (String) operLogMap.get("OPER_TIME")));//拼接操作时间 date+time
		}
		LinkedHashMap<String, String> headMap = new LinkedHashMap<String, String>();//组装要导出的日志文件的头
		headMap.put("OPERATOR_NO", "操作员");
		headMap.put("OPER_MODULE", "操作模块");
		headMap.put("OPER_TYPE", "操作类型");
		headMap.put("OPER_CONTENT", "操作内容");
		headMap.put("OPER_DATE", "操作日期");
		headMap.put("OPER_TIME", "操作时间");
		headMap.put("OPER_ORGAN", "操作机构");
		boolean retFlag = ExcelUtil.createExcelFile(HttpUtil.getAbsolutePath("temp"), fileName, title, headMap,resultList);//生成要导出的文件
		if (retFlag) {
			retMap.put("filePath", HttpUtil.getAbsolutePath("temp") + "/" + fileName);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("生成成功");
			//添加日志
			String log = "导出日志表内容";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_4_2, log);
		} else {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("xls文件生成失败！");
		}

	}
	/**
	 *再监督日志查询
	 * @param requestBean
	 * @param responseBean
	 * @author YQJ
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void reLogQuery(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map sysMap=requestBean.getSysMap();
		String organNoTmp = (String) sysMap.get("organNo");// 接收从页面传输的机构号
		String[] organNo=organNoTmp.split(",");//把从页面传过来的机构转换成数组
		String operator = (String) sysMap.get("operator");// 接收从页面传输的柜员号
		String startDate = (String) sysMap.get("startDate");// 接收从页面传输的开始
		String endDate = (String) sysMap.get("endDate");// 接收从页面传输的结束日期
		startDate = startDate.replace("-", "");// 更改页面传输过来的日期的格式
		endDate = endDate.replace("-", "");// 更改页面传输过来的日期的格式
		int currentPage = (int)sysMap.get("currentPage");
		int pageSize = 0;
		if(currentPage != -1) {
			Integer initPageSize = (Integer) sysMap.get("pageSize");
			if (initPageSize == null) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageSize;
			}
		}		
		Page page = PageHelper.startPage(currentPage, pageSize);
		List returnlist=operLogMapper.reLogQuery(organNo,operator,startDate,endDate);
		long totalCount=page.getTotal();
        Map retMap = new HashMap();
		retMap.put("currentPage", currentPage);
		retMap.put("pageSize", pageSize);
		retMap.put("totalCount", totalCount);
		retMap.put("returnlist", returnlist);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");

		
	}
}
