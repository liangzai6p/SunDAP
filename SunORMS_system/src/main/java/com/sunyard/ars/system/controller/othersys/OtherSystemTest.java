package com.sunyard.ars.system.controller.othersys;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunyard.aos.common.util.HttpUtil;

/**
 * 外系统接口测试类
 * @author 20190506e
 *
 */
public class OtherSystemTest {

	private static String url = "http://localhost:8080/SunARS/otherSystem.do";
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String message = getMessage222();
		String retMessage = HttpUtil.sendPost(url, "message="+URLEncoder.encode(message, "utf-8"));
		System.out.println(retMessage);

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static String getTestMessage() throws JsonProcessingException {
		List parameterList = new ArrayList();
		Map<String, Object> flowData = new HashMap<String, Object>();
		flowData.put("BUSI_DATA_DATE", "20180101");
		flowData.put("FLOW_ID", "120220");
		flowData.put("AMT", "12000");
		parameterList.add(flowData); 
		Map<String, Object> sysMap = new HashMap<String, Object>();
		sysMap.put("tranCode", "150001"); 			//-----必输项  交易码
		sysMap.put("codeId", "101"); //实时预警必输
		sysMap.put("doc_id", "task from adms !");
		
		Map<String, Object> msgMap = new HashMap<String, Object>();
		msgMap.put("parameterList", parameterList); //-----必输项
		msgMap.put("sysMap", sysMap);				//-----必输项
		ObjectMapper objectMapper = new ObjectMapper();
		String message = objectMapper.writeValueAsString(msgMap);
		//{"sysMap":{"tranCode":"150001","task_type":"1","doc_id":"task from adms !"},"parameterList":[{"batchId":"aaaaaaaaa","inputDate":"20180101"}]}
		System.out.println(message);
		return message;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static String getMessage110001() throws JsonProcessingException {
		List parameterList = new ArrayList();
		Map<String, Object> flowData = new HashMap<String, Object>();
		/*private String date;
		private String organNo;
		private String tellerNo;
		private String flowNo;
		private String accountNo;
		private Double money;*/
		flowData.put("date", "20180101");
		flowData.put("organNo", "120220");
		flowData.put("tellerNo", "12000");
		flowData.put("flowNo", "100110");
		flowData.put("accountNo", "1200021000000000001");
		flowData.put("money", "1130");
		parameterList.add(flowData); 
		Map<String, Object> sysMap = new HashMap<String, Object>();
		sysMap.put("tranCode", "110001"); 			//-----必输项  交易码
		sysMap.put("codeId", "101"); //实时预警必输
		
		Map<String, Object> msgMap = new HashMap<String, Object>();
		msgMap.put("parameterList", parameterList); //-----必输项
		msgMap.put("sysMap", sysMap);				//-----必输项
		ObjectMapper objectMapper = new ObjectMapper();
		String message = objectMapper.writeValueAsString(msgMap);
		return message;
	}
	
	private static String getMessage222() throws JsonProcessingException {
		String[] data = "2||4056|10651||||0|0|27|20181105|0".split("\\|");
		List parameterList = new ArrayList();
		Map<String, String> map = new HashMap<String, String>();
		map.put("NUM",data[0]);
		map.put("HANDLE_ORGAN",data[1]);
		map.put("BUSI_ORGAN",data[2]);
		map.put("ENTRY_ID",data[3]);
		map.put("NODEAL",data[4]);
		map.put("DEALED",data[5]);
		map.put("NEW_ALARMINFO_VALUE",data[6]);
		map.put("IS_HANDLE",data[7]);
		map.put("IS_TRANSMIT",data[8]);
		map.put("ALL_TASK",data[9]);
		map.put("PROC_DATE",data[10]);
		map.put("IS_TRANS_HANDLED",data[11]);
		parameterList.add(map); 
		Map<String, Object> sysMap = new HashMap<String, Object>();
		sysMap.put("tranCode", "150001"); 			//-----必输项  交易码
		sysMap.put("codeId", "101"); //实时预警必输
		sysMap.put("doc_id", "task from adms !");
		
		Map<String, Object> msgMap = new HashMap<String, Object>();
		msgMap.put("parameterList", parameterList); //-----必输项
		msgMap.put("sysMap", sysMap);				//-----必输项
		ObjectMapper objectMapper = new ObjectMapper();
		String message = objectMapper.writeValueAsString(msgMap);
		//{"sysMap":{"tranCode":"150001","task_type":"1","doc_id":"task from adms !"},"parameterList":[{"batchId":"aaaaaaaaa","inputDate":"20180101"}]}
		System.out.println(message);
		return message;
	}

}
