package com.sunyard.ars.risk.controller.arms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunyard.aos.common.util.BaseUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyard.aos.common.controller.ExportController;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.dao.SmFieldDefTbMapper;
import com.sunyard.ars.risk.service.arms.IArmsModelToShowService;
import com.sunyard.ars.system.bean.et.BusiForm;
import com.sunyard.ars.system.dao.mc.ModelMapper;
import com.sunyard.ars.system.init.SystemConstants;
import com.sunyard.ars.system.pojo.mc.Model;
import com.sunyard.ars.system.service.et.IBusiFormService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.spring.aop.ArchivesLog;

/**
 * @author:		 lewe
 * @date:		 2017年3月13日 下午4:25:56
 * @description: TODO(测试导出Controller)
 */
@Controller
public class ExportTestController extends ExportController {
	
	@Resource
	private IBusiFormService busiFormService;
	@Resource
	private IArmsModelToShowService armsModelToShowService;
	@Resource
	private SmFieldDefTbMapper smFieldDefTbMapper;
	@Resource
    private ModelMapper modelMapper;


	/**
	 * @author:		 lewe
	 * @date:		 2017年3月13日 下午5:10:30
	 * @description: TODO(执行导出请求)
	 */
	@RequestMapping(value = "/exportTestExcel.do", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ArchivesLog(operationType="导出", operationName="导出文件")
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			// 导出excel
			exportExcel(request, response);
		} catch (Exception e) {
			logger.error("导出excel出错，" + e.getMessage(), e);
		}
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年3月13日 下午4:28:24
	 * @description: TODO(获取需要导出的数据列表)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected List<Map<String, Object>> getDataList(HttpServletRequest request){
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		try {
			String requestJsonStr = request.getParameter("message");
	        System.out.println("前台发送数据:  "+requestJsonStr);
	        ObjectMapper objectMapper = new ObjectMapper();
	        RequestBean requestBean = (RequestBean)objectMapper.readValue(requestJsonStr, RequestBean.class);
	        List tempLists = requestBean.getParameterList();
	        
	        ResponseBean responseBean = new ResponseBean();
	        String exportMethod = request.getParameter("exportMethod");//导出执行方法
	        if ("processingQuery".equals(exportMethod)) { // 导出监测任务处理单查询数据
	        	if(!"[]".equals(String.valueOf(tempLists)))
		        {
		            List requestList = new ArrayList();
		            int i = 0;
		            for(int n = tempLists.size(); i < n; i++)
		                requestList.add(mapToEntity(tempLists.get(i), BusiForm.class));

		            requestBean.setParameterList(requestList);
		        }
	        	busiFormService.query(requestBean, responseBean);
	        	List<BusiForm> list = (List<BusiForm>) responseBean.getRetMap().get("returnList");
	        	Map<String, Object> map = null;
	        	int i = 1;
	        	for (BusiForm busiForm : list) {
	        		map = new HashMap<String, Object>();
	        		map.put("num", i);
	        		map.put("formId", busiForm.getFormId());
	        		//判断空指针,ALL_ET_CHARTS 和 ALL_ET_NODES 都是hashMap允许key是null
	        		map.put("formType", SystemConstants.ALL_ET_CHARTS.get(busiForm.getFormType()) == null ?
							busiForm.getFormType() : SystemConstants.ALL_ET_CHARTS.get(busiForm.getFormType()).getChartName());
	        		map.put("nodeNo", SystemConstants.ALL_ET_NODES.get(busiForm.getFormType()+"|"+busiForm.getNodeNo()) == null ?
							busiForm.getFormType()+"|"+busiForm.getNodeNo() : SystemConstants.ALL_ET_NODES.get(busiForm.getFormType()+"|"+busiForm.getNodeNo()).getLabelProcess());
	        		map.put("entryName", busiForm.getEntryName());
	        		map.put("checkerNo", busiForm.getCheckerNo());
	        		map.put("checkerName", busiForm.getCheckerName());
	        		map.put("netNo", busiForm.getNetNo());
	        		map.put("workDate", busiForm.getWorkDate());
	        		map.put("backDate", busiForm.getBackDate());
	        		map.put("operationDate", busiForm.getOperationDate());
	        		map.put("accountNo", busiForm.getAccountNo());
	        		map.put("currencyType", busiForm.getCurrencyType());
	        		map.put("accountMoney", busiForm.getAccountMoney());
	        		map.put("flowNo", busiForm.getFlowNo());
	        		map.put("tellerNo", busiForm.getTellerNo());
	        		map.put("tellerNo2", busiForm.getTellerNo2());
	        		map.put("slipType", busiForm.getSlipType());
	        		map.put("slipName", busiForm.getSlipName());
	        		if("1".equals(busiForm.getSlipLevel())){
	        			map.put("slipLevel","低" );
	        		}else if("2".equals(busiForm.getSlipLevel())){
	        			map.put("slipLevel","中" );
	        		}else if("3".equals(busiForm.getSlipLevel())){
	        			map.put("slipLevel","高" );
	        		}
	        		
	        		dataList.add(map);
	        		i++;
				}
	        }else if ("getHistoryModelData".equals(exportMethod)) { // 导出历史模型数据
	        	if(!"[]".equals(String.valueOf(tempLists)))
		        {
		            List requestList = new ArrayList();
		            int i = 0;
		            for(int n = tempLists.size(); i < n; i++)
		                requestList.add(mapToEntity(tempLists.get(i), Object.class));

		            requestBean.setParameterList(requestList);
		        }
	        	//获取导出数据的展现字段
				String modelId = ((Map)requestBean.getParameterList().get(0)).get("modelId").toString();
	        	List<HashMap<String, Object>>  dataFieldMap = smFieldDefTbMapper.getFieldDicByModel(Integer.parseInt(BaseUtil.filterHeader(modelId)));
	        	
	        	responseBean = armsModelToShowService.execute(requestBean);
	        	dataList = (List<Map<String, Object>>) responseBean.getRetMap().get("modelDatas");
	        	int i = 1;
	        	for (Map<String, Object> object : dataList) {
	        		object.put("num", i);
	        		i++;
	        		if(dataFieldMap != null && dataFieldMap.size() > 0){
	        			for (HashMap<String, Object> hashMap : dataFieldMap) {
	        				if(ARSConstants.SYSTEM_DICTIONARY.get(hashMap.get("DICNAME")+"|"+object.get(hashMap.get("NAME"))) != null){
	        					object.put(BaseUtil.filterHeader(hashMap.get("NAME").toString()),
										BaseUtil.filterHeader(ARSConstants.SYSTEM_DICTIONARY.get(hashMap.get("DICNAME")+"|"+object.get(hashMap.get("NAME"))).getContent()));
	        				}
						}
	        		}
				}
	        }else if ("getHistoryRelateModelData".equals(exportMethod)) { // 导出历史模型明细数据
	        	if(!"[]".equals(String.valueOf(tempLists)))
		        {
		            List requestList = new ArrayList();
		            int i = 0;
		            for(int n = tempLists.size(); i < n; i++)
		                requestList.add(mapToEntity(tempLists.get(i), Object.class));

		            requestBean.setParameterList(requestList);
		        }
	        	responseBean = armsModelToShowService.execute(requestBean);

				String modelId = ((Map)requestBean.getParameterList().get(0)).get("modelId").toString();
	        	Model modelInfo = modelMapper.getModelInfo(Integer.parseInt(BaseUtil.filterHeader(modelId)));
	        	//获取导出数据的展现字段 
	        	List<HashMap<String, Object>>  dataFieldMap = smFieldDefTbMapper.getFieldDicByModel(Integer.valueOf(BaseUtil.filterSqlParam(modelInfo.getRelatingId()+"")));
	        	
	        	dataList = (List<Map<String, Object>>) responseBean.getRetMap().get("modelDatas");
	        	//格式化字典
	        	for (Map<String, Object> object : dataList) {
	        		if(dataFieldMap != null && dataFieldMap.size() > 0){
	        			for (HashMap<String, Object> hashMap : dataFieldMap) {
	        				if(ARSConstants.SYSTEM_DICTIONARY.get(hashMap.get("DICNAME")+"|"+object.get(hashMap.get("NAME"))) != null){
	        					object.put(BaseUtil.filterHeader(hashMap.get("NAME").toString()),
										BaseUtil.filterHeader(ARSConstants.SYSTEM_DICTIONARY.get(hashMap.get("DICNAME")+"|"+object.get(hashMap.get("NAME"))).getContent()));
	        				}
						}
	        		}
				}
	        }else if("getModelDataDetails".equals(exportMethod)){
				//导出 警报信息处理机构明细
				if(!"[]".equals(String.valueOf(tempLists))) {
					List requestList = new ArrayList();
					for(int i = 0 , n = tempLists.size(); i < n; i++)
						requestList.add(mapToEntity(tempLists.get(i), Object.class));
					requestBean.setParameterList(requestList);
				}

				responseBean = armsModelToShowService.execute(requestBean);
				dataList = (List<Map<String, Object>>) responseBean.getRetMap().get("modelDatas");

				String modelId = ((Map)requestBean.getParameterList().get(0)).get("relatingId").toString();
				if(!BaseUtil.isBlank(modelId)){

					//获取导出数据的展现字段
					List<HashMap<String, Object>>  dataFieldMap = smFieldDefTbMapper.getFieldDicByModel(Integer.parseInt(modelId));
					//格式化字典
					for (Map<String, Object> object : dataList) {
						if(dataFieldMap != null && dataFieldMap.size() > 0){
							for (HashMap<String, Object> hashMap : dataFieldMap) {
								if(ARSConstants.SYSTEM_DICTIONARY.get(hashMap.get("DICNAME")+"|"+object.get(hashMap.get("NAME"))) != null){
									object.put(BaseUtil.filterHeader(hashMap.get("NAME").toString()),
											BaseUtil.filterHeader(ARSConstants.SYSTEM_DICTIONARY.get(hashMap.get("DICNAME")+"|"+object.get(hashMap.get("NAME"))).getContent()));
								}
							}
						}
					}
				}

				//===============================
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dataList;
	}
	
	protected String transObj2Json(Object object)
        throws JsonProcessingException
    {
        String retJson = "";
        ObjectMapper objectMapper = new ObjectMapper();
        retJson = objectMapper.writeValueAsString(object);
        return retJson;
    }

    protected Object mapToEntity(Object object, Class paramClass)
        throws IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(object);
        Object paramBean = objectMapper.readValue(str, paramClass);
        return paramBean;
    }
}
