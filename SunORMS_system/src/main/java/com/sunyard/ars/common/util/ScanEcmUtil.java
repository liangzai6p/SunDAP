package com.sunyard.ars.common.util;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.sunyard.aos.common.comm.GlobalVar;
import com.sunyard.aos.common.util.BaseUtil;
//import com.sunyard.aos.irp.hdyj.common.AdmsConstants;
import com.sunyard.client.SunEcmClientApi;
import com.sunyard.client.bean.ClientBatchBean;
import com.sunyard.client.bean.ClientBatchFileBean;
import com.sunyard.client.bean.ClientHeightQuery;
import com.sunyard.client.impl.SunEcmClientSocketApiImpl;
import com.sunyard.cop.IF.common.SunIFConstant;
import com.sunyard.cop.IF.common.util.xml.jdom.JDomHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ScanEcmUtil {

	private Logger logger = LoggerFactory.getLogger(ScanEcmUtil.class);

	/** xml 操作对象 */
	private JDomHandler domHandler = new JDomHandler();

	/** ecm 操作客户端 */
	private SunEcmClientApi ecmClient;

	private String ip = ""; // ip地址
	private int port = 0; // 服务端口（socket端口）
	private String groupName = ""; // 服务器组名
	private String modelCode = ""; // 索引对象
	private String filePart = ""; // 文档对象
	private String userNo = ""; // 登录用户名
	private String pswd = ""; // 登录密码
	private String startColumn = ""; // 影像批次日期英文名

	/**
	 * 构造函数
	 * 
	 * @author: lewe
	 * @date: 2018年2月5日 下午3:58:37
	 */
	public ScanEcmUtil() {
	}
	public ScanEcmUtil(Map<String, String> ecmMap) {
		ip = ecmMap.get("ecm_ip");
		port = Integer.parseInt(ecmMap.get("ecm_port"));
		userNo = ecmMap.get("ecm_user");
		pswd = ecmMap.get("ecm_pwd");
		
		groupName = ecmMap.get("groupName");
		modelCode = ecmMap.get("modelCode");
		filePart = ecmMap.get("filePart");
		startColumn = ecmMap.get("startColumn");
		// 初始化ecm服务
		ecmClient = new SunEcmClientSocketApiImpl(ip, port);
	}

	/**
	 * 构造函数
	 * 
	 * 初始化指定渠道的 ecm 服务
	 * 
	 * @author: lewe
	 * @date: 2018年2月5日 下午3:58:45
	 * 
	 * @param chanId
	 *            渠道号
	public ScanEcmUtil(String chanId) {
		// 根据渠道号获取关联ecm信息
		HashMap<String, Object> chanMap = GlobalVar.allChannelInfo.get(chanId);
		if (chanMap == null || chanMap.isEmpty()) {
			logger.error("渠道号[" + chanId + "]的详细信息不存在，暂时无法初始化关联ECM服务！");
			return;
		}
		String ecmId = (String) chanMap.get("chan_ecm");
		// 获取ecm服务详细信息
		initEcmById(ecmId);
	}
	 */

	/**
	 * @author:
	 * @date: 2018年5月9日 下午6:58:10
	 * @Description:通过ecm_id初始
	public void initEcmById(String ecmId) {
		HashMap<String, Object> ecmMap = GlobalVar.allEcmInfo.get(ecmId);
		if (ecmMap == null || ecmMap.isEmpty()) {
			logger.error("ECM服务[" + ecmId + "]的详细信息不存在，暂时无法初始化关联ECM服务！");
			return;
		}
		ip = (String) ecmMap.get("ecm_ip");
		port = Integer.parseInt(ecmMap.get("ecm_port") + "");
		groupName = (String) ecmMap.get("group_name");
		modelCode = (String) ecmMap.get("model_code");
		filePart = (String) ecmMap.get("file_part");
		userNo = (String) ecmMap.get("user_no");
		password = (String) ecmMap.get("password");
		startColumn = (String) ecmMap.get("start_date");
		logger.info(ip + ":" + port + ":" + groupName + ":" + modelCode + ":" + filePart + ":" + userNo + ":" + password
				+ ":" + startColumn);
		// 初始化ecm服务
		ecmClient = new SunEcmClientSocketApiImpl(ip, port);

		logger.info("ECM服务[" + ecmId + "]初始化成功，IP：" + ip + "，端口：" + port);
	}
	 */
	
	/**
	 * 登录 ecm 服务
	 */
	private void login() throws Exception {
		if (ecmClient == null) {
			return;
		}
		String resultMsg = ecmClient.login(userNo, pswd);
		logger.info("登录ecm服务，返回信息：" + resultMsg);
	}
	
	/**
	 * 登出 ecm 服务
	 */
	private void logout() throws Exception {
		if (ecmClient == null) {
			return;
		}
		String resultMsg = ecmClient.logout(userNo);
		logger.info("登出ecm服务，返回信息：" + resultMsg);
	}

	/**
	 * 根据后督批次号查询影像批次号
	 * 
	 * @author: zgz
	 * @date: 2018年8月10日
	 * 
	 * @param batchId
	 *            后督批次号
	 * @param startDate
	 *            开始日期
	 * 
	 * @return 影像批次号CONTENT_ID
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getContentId(String batchId, String startDate) throws Exception {
		String contentId = null;

		ClientHeightQuery heightQuery = new ClientHeightQuery();
		// 必要信息,设置用户名
		heightQuery.setUserName(userNo);
		heightQuery.setPassWord(pswd);
		// 必要信息,设置行数
		heightQuery.setLimit(1);
		// 必要信息,设置页码
		heightQuery.setPage(1);
		// 必要信息,设置内容模型名
		heightQuery.setModelCode(modelCode);
		// 必要信息,自定义属性中必须有一个8位数字字段,用以分表,从内容模型模板中获取字段名
		heightQuery.addCustomAtt(startColumn, startDate);
		// 可选信息,设置属性
		heightQuery.addCustomAtt("BUSI_SERIAL_NO", batchId);

		String resultMsg = ecmClient.heightQuery(heightQuery, groupName);

		logger.info("查询ecm批次影像，返回信息：" + resultMsg);
		if (resultMsg.startsWith("0001<<::>><")) { // 查询成功
			domHandler.loadXmlByString(resultMsg.replace("0001<<::>><", "<"));
			List<Element> list = domHandler.getNodes("root/HeightQuery/indexBeans/BatchIndexBean");
			if(list != null && list.size()>0) {
				Element e = list.get(0);
				return e.getAttributeValue("CONTENT_ID");
			}
		}
		return contentId;
	}
	
	/**
	 * 查询影像
	 * 
	 * @author:	lewe
	 * @date:	2018年2月5日 下午4:30:22
	 * 
	 * @param	docId		批次号
	 * @param	startDate	开始日期
	 * 
	 * @return	查询成功：json数组字符串，包含批次下所有影像的格式、类型、名称、地址等信息
	 * 			查询失败：FAIL<<::>>开头的失败信息
	 * 
	 * @throws Exception 
	 */
	public String queryImage(String docId, String startDate) throws Exception {
		
		login();
		
		// 设置查询批次基本信息
		ClientBatchBean cbb = new ClientBatchBean();
		cbb.setModelCode(modelCode);
		cbb.setUser(userNo);
		cbb.setPassWord(pswd);
		cbb.setDownLoad(false);
		cbb.getIndex_Object().setContentID(docId); // 批次号
		if (!BaseUtil.isBlank(startDate)) {
			cbb.getIndex_Object().addCustomMap(startColumn, startDate); // 自定义属性-开始日期
		}

		ClientBatchFileBean cbfb = new ClientBatchFileBean();
		// 设置文档部件名
		cbfb.setFilePartName(filePart);
		cbb.addDocument_Object(cbfb);

		String resultMsg = "";
		resultMsg = ecmClient.queryBatch(cbb, groupName);
		logger.info("查询ecm批次影像，返回信息：" + resultMsg);
		if (resultMsg.startsWith("0001<<::>><")) { // 查询成功
			resultMsg = resultMsg.replace("0001<<::>><", "<");
			resultMsg = getUrlInfo(resultMsg);
		} else { // 查询失败
			resultMsg = "FAIL<<::>>" + resultMsg;
		}
		logout();
		
		return resultMsg;
	}
	
	/**
	 * 从 ecm 返回报文中获取影像 url 信息
	 * 
	 * @author:	lewe
	 * @date:	2018年2月5日 下午4:28:06
	 * 
	 * @param	msg	初始报文
	 * 
	 * @throws	JDOMException 
	 * @throws	IOException
	 * root/BatchBean/document_Objects/BatchFileBean/files/FileBean
	 * @return	json数组字符串
	 */
	@SuppressWarnings("unchecked")
	private String getUrlInfo(String msg) throws IOException, JDOMException {
		JSONArray jsonArr = new JSONArray();
		domHandler.loadXmlByString(msg);
		List<Element> list = domHandler.getNodes(SunIFConstant.ECM_XMLPATH);
		for (Element el : list) {
			String url = el.getAttributeValue(SunIFConstant.ECM_NODEATTR);		// 访问url
			String fileFormat = el.getAttributeValue("FILE_FORMAT");			// 文件格式，jpg、png等
			String fileNo = el.getAttributeValue("FILE_NO");					// 文件编号
			String fileStatus = el.getAttributeValue("FILE_STATUS");			// 文件状态
			String version = el.getAttributeValue("VERSION");					// 文件版本
			Element eloa = el.getChild("otherAtt");
			String fileType = eloa.getChild("FILEFORM").getChildText("string");	// 文件分类，对应影像控件左侧目录树节点name属性
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("file_format", fileFormat.trim());
			jsonObj.put("file_type", fileType.trim());
			jsonObj.put("file_status", fileStatus.trim());
			jsonObj.put("file_no", fileNo.trim());
			jsonObj.put("version", version.trim());
			jsonObj.put("url", url.trim());
			// 封装自定义属性信息
			JSONObject otherAttObj = new JSONObject();
			List<Element> childrenList = eloa.getChildren();
			for (Element ce : childrenList) {
				String cName = ce.getName();
				List<Element> tempList = ce.getChildren();
				if (tempList != null && tempList.size() > 0) {
					String cValue = tempList.get(0).getValue();
					if (!BaseUtil.isBlank(cValue) && !"null".equals(cValue)) { // 剔除空值（包括null字符串）属性
						otherAttObj.put(cName, cValue);
					}
				}
			}
			jsonObj.put("otherAtt", otherAttObj);
			jsonArr.add(jsonObj);
		}
		return String.valueOf(jsonArr);
	}
	
//	public static void main(String[] args) throws Exception {
//		// TODO Auto-generated method stub
//		/*AdmsEcmUtil aeu = new AdmsEcmUtil();
//		String contentId = aeu.getContentId("20180806101302107960", "20180806");
//		if(contentId != null) {
//			System.out.println("get content id : "+contentId);
//		}else {
//			System.out.println("get content id fail ...");
//		}*/
//		
//		try {
//			Map map = null;
//			System.out.println(map.get("111"));
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println(e.toString());
//			System.out.println(e.getMessage());
//			System.out.println(e.getLocalizedMessage());
//		}
//		
//	}

}
