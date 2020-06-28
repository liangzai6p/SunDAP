package com.sunyard.ars.system.service.impl.mc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.ExcelUtil;
import com.sunyard.aos.common.util.HttpUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.pojo.DelRelate;
import com.sunyard.ars.system.dao.mc.LabMcVersionMapper;
import com.sunyard.ars.system.dao.mc.ModelMapper;
import com.sunyard.ars.system.dao.mc.RelateMapper;
import com.sunyard.ars.system.dao.mc.ViewMapper;
import com.sunyard.ars.system.dao.sc.SystemParameterMapper;
import com.sunyard.ars.system.pojo.mc.Model;
import com.sunyard.ars.system.pojo.mc.View;
import com.sunyard.ars.system.service.mc.IModelService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service("modelService")
@Transactional
public class ModelServiceImpl extends BaseService  implements IModelService{
	@Resource
	private ViewMapper viewMapper;
	@Resource
	private ModelMapper modelMapper;
	@Resource
	private LabMcVersionMapper labMcVersionMapper;
	@Resource
	private RelateMapper relateMapper;
	@Resource
	private SystemParameterMapper systemParameterMapper;
	
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
		} else if (ARSConstants.OPERATE_ADD.equals(oper_type)) { 
			// 新增
			add(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_MODIFY.equals(oper_type)) { 
			// 修改
			update(requestBean, responseBean);
		} else if(ARSConstants.OPERATE_DELETE.equals(oper_type)) { 
			// 删除
			delete(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_OTHER.equals(oper_type)) {
			// 其他操作
			otherOperation(requestBean, responseBean);
		}else if("getUserModelInfos".equals(oper_type)){
			getUserModelInfos(requestBean, responseBean);
		}else if("getModelInfo".equals(oper_type)){
			getModelInfo(requestBean, responseBean);
		}else if("ARMSMODEL_EXPROT".equals(oper_type)){//预警模型导出
			armsModelExport(requestBean, responseBean);
		}else if("Lab_TO_MCMODELTB".equals(oper_type)){
			labToMcModelTb(requestBean, responseBean);
		}else if("getUserBusiInfo".equals(oper_type)){//获取用户模型业务类型
			getUserBusiInfo(requestBean, responseBean);
		}
	}
	
	/**
	 * 预警模型信息导出
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void armsModelExport(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		String fileName = System.currentTimeMillis()+"ExhibitInfo.xls";
		String title = "监测模型信息";
		Map<Object,Object> retMap = new HashMap<Object, Object>();
		LinkedHashMap<String, String> headMap = new LinkedHashMap<String, String>();
		headMap.put("modelBusiType", "警报组名称");
		headMap.put("name", "警报项名称");
		headMap.put("alarmLevel", "警报级别");
		headMap.put("status", "警报频率");
		headMap.put("selectCondition", "查看风险点");
		
		//获取用户机构
		String userOrganNo  = BaseUtil.getLoginUser().getOrganNo();
		
		HashMap condMap = new HashMap();
		condMap.put("modelType", "0");
		condMap.put("userOrganNo", userOrganNo);
		List<Model> userModelInfos = modelMapper.getUserModelInfos(condMap);
		List allBusInfo = modelMapper.selectAllBusiInfo();
		Map<String,String> busiMap = new HashMap<>();
		for (Object object : allBusInfo) {
			busiMap.put(((Map)object).get("BUSI_NO").toString(), ((Map)object).get("BUSI_NAME").toString());
		}
		List<Map> data = new ArrayList<Map>();
		for (int i = 0; i < userModelInfos.size(); i++) {
			userModelInfos.get(i).setModelBusiType(busiMap.get(userModelInfos.get(i).getModelBusiType()));
			if("1".equals(userModelInfos.get(i).getAlarmLevel())){
				userModelInfos.get(i).setAlarmLevel("一级");
			}
			if("2".equals(userModelInfos.get(i).getAlarmLevel())){
				userModelInfos.get(i).setAlarmLevel("二级");
			}
			if("3".equals(userModelInfos.get(i).getAlarmLevel())){
				userModelInfos.get(i).setAlarmLevel("三级");
			}
			if("4".equals(userModelInfos.get(i).getAlarmLevel())){
				userModelInfos.get(i).setAlarmLevel("提示类");
			}
			userModelInfos.get(i).setStatus("每日");
			ObjectMapper objectMapper = new ObjectMapper();
		    String contents = objectMapper.writeValueAsString(userModelInfos.get(i));
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

	/**
	 * 查询模型
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{

		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		Model model = (Model) requestBean.getParameterList().get(0);
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
		
		//执行查询
		Page page = PageHelper.startPage(currentPage, pageSize);
		List resultList = modelMapper.selectBySelective(model);
		long totalCount = page.getTotal();
		Map retMap = new HashMap();
		retMap.put("pageSize", pageSize);
		retMap.put("allRow", totalCount);
		retMap.put("resultList", resultList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
	
	}
	
	/**
	 * 新增模型
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void add(RequestBean requestBean, ResponseBean responseBean)throws Exception{

		//获取页面参数
		Model model = (Model) requestBean.getParameterList().get(0);
		
		//新增模型
		modelMapper.insertSelective(model);
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("添加成功！");
		
		//添加日志
		String log = "警报规则配置新增模型:" + model.getName();
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
	
	}
	
	/**
	 * 修改模型
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void update(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Model model = (Model) requestBean.getParameterList().get(0);
		
		//修改模型
		modelMapper.updateByPrimaryKeySelective(model);
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
		
		//添加日志
		String log = "警报规则配置修改id为"+model.getId() +"的模型!";
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
	}
	
	/**
	 * 删除模型
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void delete(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		Model model = (Model) requestBean.getParameterList().get(0);
		List delList =(List)sysMap.get("delList");
		
		//删除前查询模型是否被关联
		HashMap condMap =null;
		int count =0;
		for(int j=0;j<DelRelate.model.size();j++){
			condMap = new HashMap<String, Object>();
			condMap.put("relate", DelRelate.model.get(j));
			condMap.put("delList", delList);		
			count = relateMapper.selectRelate(condMap);
			if(count > 0){
				//存在关联信息，不允许删除
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("表被关联，请解除关联关系后再进行删除操作！");
				return;
			}
		}
		
		modelMapper.deleteByPrimaryKey(model.getId());
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功！");
		
		//添加日志
		String log = "警报规则配置删除id为" + model.getId() + "的模型!";
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
	}
		
	/**
	 * 其他操作
	 * @param requestBean
	 * @param responseBean
	 */
	private void otherOperation(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		Map sysMap = requestBean.getSysMap();
		// 获取操作标识
		String oper_type = (String) sysMap.get("oper_type");

		}

	/**
	 * 获取用户模型
	 * @param requestBean
	 * @param responseBean
	 */
	public void getUserModelInfos(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		//执行查询
		//获取页面参数
		Model model = null;
		try {
			 model = (Model) requestBean.getParameterList().get(0);
		}catch (Exception e){
			 model = new Model();
		}
		//获取用户机构
		String userOrganNo  = BaseUtil.getLoginUser().getOrganNo();
		
		HashMap condMap = new HashMap();
		condMap.put("modelLevel", model.getAlarmLevel());
		condMap.put("modelBusiType", model.getModelBusiType());
		condMap.put("modelType", model.getModelType());
		condMap.put("filterType", model.getFilterType());
		condMap.put("userOrganNo", userOrganNo);
		condMap.put("modelIdList", modelMapper.selectFilterModel(BaseUtil.getLoginUser().getUserNo()));
		List userModelInfos = modelMapper.getUserModelInfos(condMap);
		Map retMap = new HashMap();
		retMap.put("userModelInfos", userModelInfos);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * 根据modelId获取模型信息
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	public void  getModelInfo(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Model param_model = (Model) requestBean.getParameterList().get(0);
		Integer modelId  = param_model.getId();
		Model model = modelMapper.getModelInfo(modelId);

		Map retMap = new HashMap();
		retMap.put("modelInfo", model);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	/**
	 * 该方法主要点击模型发布时所要执行的动作
	 * 1.把页面输入的数据insert进mc_model_tb，当页面添加完数据之后把lab_mc_version中的lab_state置为1和把mc_table_tb中的id写进lab_mc_version中的produce_model_id
	 * 2.通过modelid和versionid从lab_rule_tb中查找到所有符合的规则进行拼接，之后再把拼接好的sql语句插入到mc_view_tb中去
	 * 3.把模型实验室中的展现字段插入到MC_exhibit_field_tb中去
	 * 这些执行当有一个执行出错则全部回滚（事物控制）
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly=false)
	private void labToMcModelTb(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取页面参数
		Map sysMap = requestBean.getSysMap();
		int modelId = (int) sysMap.get("modelId");
		int versionId = (int) sysMap.get("versionId");
		String versionSql=(String)sysMap.get("versionSql");
		Model model = (Model) requestBean.getParameterList().get(0);
		// 新增模型
		modelMapper.insertSelective(model);
		int id = model.getId();// 通过该方法获得刚刚插入到mc_model_tb表时生成的id
		// 把新增模型的id和状态写进lab_mc_version中
		labMcVersionMapper.updateModelIdAndLabState(id, modelId, versionId);
		// 查询该模型该版本的所有规则，然后进行组装，再把该sql插入到视图配置表mc_view_tb中去
	    // List ruleList = labMcVersionMapper.getRules(modelId, versionId);
		View view = new View();
		//String sql = "insert into lab_mc_version values(1,1,1,1)";
		view.setName("插入预警数据");
		view.setModelId(modelId);
		view.setDsId(1);
		view.setCompositeType("7");
		view.setIsgroupby("0");
		view.setQueryStr(versionSql);
		view.setSeqno(1);
		// 把规则插入到视图
		viewMapper.insertSelective(view);
		//把模型实验室的展现字段配置插入到模型中去mc_exhibit_field_tb
		labMcVersionMapper.insertExhibitField(modelId, id);
		
		//etl加入任务
		addETLJob(id,sysMap);
		
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("添加成功！");

	}
	
	private void addETLJob(int modelId,Map<String,Object> sysMap) throws UnsupportedEncodingException, Exception{
		String url = systemParameterMapper.selectByPrimaryKey("ETL_JOBADD_URL", "SUNYARD", "AOS", "AOS").getParamValue().toString();

		//jobInfo参数
		String jobGroup = (String)sysMap.get("jobGroup");
		String jobCron = (String)sysMap.get("jobCron");
		String jobDesc = (String)sysMap.get("jobDesc");
		String executorRouteStrategy = (String)sysMap.get("executorRouteStrategy");
		String executorBlockStrategy = (String)sysMap.get("executorBlockStrategy");
		String executorFailStrategy = (String)sysMap.get("executorFailStrategy");
		String glueType = (String)sysMap.get("glueType");
		String jobHandler = (String)sysMap.get("jobHandler");
		String writeType = (String)sysMap.get("writeType");
		String alarmEmail = (String)sysMap.get("alarmEmail");
		String executorParam = (String)sysMap.get("executorParam");
		
		
		
		Map<String,Object> map = new HashMap<>();
		//组装jobInfo
		Map<String,Object> jobMap = new HashMap<>();
		jobMap.put("jobGroup", jobGroup);//jobGroup
		jobMap.put("jobCron", jobCron);//Cron
		jobMap.put("jobDesc", jobDesc);//任务描述
		jobMap.put("executorRouteStrategy", executorRouteStrategy);//路由策略
		jobMap.put("executorBlockStrategy", executorBlockStrategy);//阻塞处理策略
		jobMap.put("executorFailStrategy", executorFailStrategy);//失败处理策略
		jobMap.put("glueType", glueType);//运行模式
		jobMap.put("jobHandler", jobHandler);//JobHandler
		jobMap.put("writeType", writeType);
		
		jobMap.put("alarmEmail", alarmEmail);
		jobMap.put("executorParam", executorParam);
		jobMap.put("glueRemark", "GLUE代码初始化");
		jobMap.put("glueSoure", "");
		jobMap.put("jobStartTime", "");
		
		
		
		//组装jobParam
		List<Map<String,Object>> paramList = new ArrayList<>();
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("param_field", "MODEL_ID");//参数字段
		paramMap.put("param_field_ch", "模型ID");//参数描述
		paramMap.put("param_value", modelId);//参数值
		paramList.add(paramMap);
		//组装发送内容
		map.put("jobMap", jobMap);
		map.put("paramList", paramList);
		
		String response = HttpUtil.sendPost(url, "message=" + URLEncoder.encode(JSON.toJSONString(map), "utf-8"));
		logger.info("response:"+response);
		if("success".equals(response)){
			logger.info("ETL新增模型任务成功");
		}else{
			logger.info("ETL新增模型任务失败，开始回滚");
			throw new Exception("ETL新增模型任务失败");
		}
	}
	
	/**
	 * 获取用户模型业务类型
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getUserBusiInfo(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		//获取用户机构
		String userOrganNo  = BaseUtil.getLoginUser().getOrganNo();
		HashMap condMap = new HashMap();
		condMap.put("userOrganNo", userOrganNo);
		condMap.put("modelIdList", modelMapper.selectFilterModel(BaseUtil.getLoginUser().getUserNo()));
		List userBusiInfos = modelMapper.getUserBusiInfos(condMap);
		Map retMap = new HashMap();
		retMap.put("userBusiInfos", userBusiInfos);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
}
