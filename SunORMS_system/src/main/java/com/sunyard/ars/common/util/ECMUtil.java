package com.sunyard.ars.common.util;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunyard.ars.common.pojo.FileUrlBean;
import com.sunyard.client.SunEcmClientApi;
import com.sunyard.client.bean.ClientBatchBean;
import com.sunyard.client.bean.ClientBatchFileBean;
import com.sunyard.client.bean.ClientBatchIndexBean;
import com.sunyard.client.bean.ClientFileBean;
import com.sunyard.client.bean.ClientHeightQuery;
import com.sunyard.client.impl.SunEcmClientSocketApiImpl;
import com.sunyard.util.OptionKey;


public class ECMUtil {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private String tableName = "ECM_LOG";
	
	private String ECMIP;
	private int ECMPort;
	private String username;
	private String password;

	private	String groupName;
	private String indexName;// 分表字段

//	private String indexValue;// 分表字段值
	private String modelCode; // 内容模型代码
	private String filePartName;// 文档部件模型代码
	
	
	
	public ECMUtil(){
		
	}
	
	public ECMUtil(String ECMIP, int ECMPort, String username, String password) {
		this.ECMIP = ECMIP;
		this.ECMPort = ECMPort;
		this.username = username;
		this.password = password;
	}
	
	
	public void set(String groupName, String modelCode, String filePartName, String IndexName){
		this.groupName = groupName;
		this.modelCode = modelCode;
		this.filePartName = filePartName;
		this.indexName = IndexName;
	}
	
	public void copy(ECMUtil ser){
		ser = new ECMUtil(this.ECMIP,this.ECMPort,this.username,this.password);
	}
	
	public boolean login() throws Exception {
		SunEcmClientApi clientApi = new SunEcmClientSocketApiImpl(ECMIP, ECMPort);
		String resultMsg = clientApi.login(username, password);
		logger.info("#######登陆返回的信息[" + resultMsg + "]#######", tableName);
		if (resultMsg.equals("SUCCESS"))
			return true;
		return false;
	}

	public void logout() throws Exception {
		SunEcmClientApi clientApi = new SunEcmClientSocketApiImpl(ECMIP, ECMPort);
		String resultMsg = clientApi.logout(username);
		logger.info("#######登出返回的信息[" + resultMsg + "]#######", tableName);
	}

	public String upload(String batchId , String filePath, String indexValue) {	
		String contentId = "";
		File imagefile = new File(filePath);
		FileUtil.deleteXml(imagefile);
		List<File> imagefiles = Arrays.asList(imagefile.listFiles());
		if (imagefile == null || imagefiles == null || imagefiles.size() == 0) {
			logger.info("路径下文件不存在");
			return "FAIL";
		}
		/*
		 * 进行排序
		 */
		Collections.sort(imagefiles, new Comparator<File>(){
		    @Override
		    public int compare(File o1, File o2) {
		    if(o1.isDirectory() && o2.isFile())
		        return -1;
		    if(o1.isFile() && o2.isDirectory())
		            return 1;
		    return o1.getName().compareTo(o2.getName());
		    }
		});
		ClientBatchBean clientBatchBean = new ClientBatchBean();
		clientBatchBean.setModelCode(modelCode);
		clientBatchBean.setUser(username);
		clientBatchBean.setPassWord(password);
		clientBatchBean.setBreakPoint(false); // 是否作为断点续传上传
		clientBatchBean.setOwnMD5(false); // 是否为批次下的文件添加MD5码
		// =========================设置索引对象信息开始=========================
		ClientBatchIndexBean clientBatchIndexBean = new ClientBatchIndexBean();
		clientBatchIndexBean.setAmount("");
//		clientBatchIndexBean.setContentID(contentId);
		// 索引自定义属性
		clientBatchIndexBean.addCustomMap("BUSI_SERIAL_NO", batchId);
//		clientBatchIndexBean.addCustomMap("CREATEDATE", DateUtil.getNow());
		clientBatchIndexBean.addCustomMap(indexName, indexValue);
		// =========================设置索引对象信息结束=========================

		// =========================设置文档部件信息开始=========================
		ClientBatchFileBean clientBatchFileBean = new ClientBatchFileBean();
		clientBatchFileBean.setFilePartName(filePartName);
		// =========================设置文档部件信息结束=========================
		Document sortdoc = DocumentHelper.createDocument();
		Element sortElem = sortdoc.addElement("root"); 
		Element sortnodeElem=sortElem.addElement("node");
		sortnodeElem.addAttribute("name", "KJ_IMAGE");
		// =========================添加文件=========================
		for (int i = 1; i < imagefiles.size() + 1; i++) {
			File imageFile = imagefiles.get(i-1);
			String imagepath = imageFile.getPath();
			String fileName = imageFile.getName();
			logger.info(batchId+"<><><>文件部件添加:"+fileName);
//			String fileFormat = fileName.substring(fileName.lastIndexOf(".")+1);
			
//			if(!Contants.IMAGE_FORMAT.equalsIgnoreCase(fileFormat)){
//				break;
//			}
			// 添加FileBean
			ClientFileBean fileBean = new ClientFileBean();
			fileBean.setFileName(imageFile.getPath());
			fileBean.setFileFormat(fileName.substring(fileName.lastIndexOf(".")+1)); //
			//fileBean.setFilesize(imageFile.length() + "");
			// fileBean.setMd5Str(MD5Util.getHash(imagepath,"MD5"));
			fileBean.addOtherAtt("SHOWNAME", batchId + "-" + i);// 00016-ft
			// sort_20160315043417619.xml
			fileBean.addOtherAtt("FILEFORM", "KJ_IMAGE");// KJ_IMAGE// SORT_
			fileBean.addOtherAtt("TRUENAME", fileName);
			fileBean.addOtherAtt("FILEATTR", "1"); // 0,2
			fileBean.addOtherAtt("FILEMD5", MD5Util.getHash(imagepath, "MD5"));

			clientBatchFileBean.addFile(fileBean);

			Element sortitemElem = sortnodeElem.addElement("item");
			sortitemElem.addAttribute("filename", fileName);
		}
		
		// =======================添加排序文档============================
		// 生产排序报文
		String sortfileName = "sort_" + System.currentTimeMillis() + ".xml";
		XmlUtil.createXML(sortdoc, filePath + File.separator + sortfileName);
		ClientFileBean sortFileBean = new ClientFileBean();
		sortFileBean.setFileName(filePath + File.separator + sortfileName);
		sortFileBean.setFileFormat("xml"); //
		// sortFileBean.setFilesize(new File(sortfileName).length()+"");
		// sortFileBean.setMd5Str(MD5Util.getHash(filePath + File.separator +
		// batchId+File.separator+sortfileName, "MD5"));
		sortFileBean.addOtherAtt("FILEMD5", MD5Util.getHash(filePath
				+ File.separator + sortfileName, "MD5"));

		sortFileBean.addOtherAtt("FILEATTR", "0"); // 0,2
		sortFileBean.addOtherAtt("FILEFORM", "SORT_");// KJ_IMAGE
		sortFileBean.addOtherAtt("SHOWNAME", sortfileName);// KJ_IMAGE
		sortFileBean.addOtherAtt("TRUENAME", sortfileName);// KJ_IMAGE

		clientBatchFileBean.addFile(sortFileBean);

		// =========================添加文件=========================
		clientBatchBean.setIndex_Object(clientBatchIndexBean);
		clientBatchBean.addDocument_Object(clientBatchFileBean);

		SunEcmClientApi clientApi = new SunEcmClientSocketApiImpl(ECMIP, ECMPort);
		try {
			String resultMsg = clientApi.upload(clientBatchBean, groupName);
			logger.info("#######上传批次返回的信息[" + resultMsg + "]#######");
			logger.info("#######上传批次返回的信息[" + resultMsg + "]#######");
			if (resultMsg.contains("SUCCESS")){
				logger.info("上传成功");
				contentId = resultMsg.replace("SUCCESS<<::>>", "");
			} else if (resultMsg.contains("FAIL")){
				logger.info("上传失败");
				return "FAIL";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL";
		}
		return contentId;
	}
	
	
	
	public String update(String filePath, String contentID, String indexValue) {
		ClientBatchBean clientBatchBean = new ClientBatchBean();
		clientBatchBean.setModelCode(modelCode);
		clientBatchBean.setUser(username);
		clientBatchBean.setPassWord(password);
		clientBatchBean.getIndex_Object().setContentID(contentID);
		clientBatchBean.getIndex_Object().addCustomMap(indexName, indexValue);
		ClientBatchFileBean batchFileBean = new ClientBatchFileBean();
		batchFileBean.setFilePartName(filePartName);
		// // 新增一个文件
		File imagefile = new File(filePath);
		FileUtil.deleteXml(imagefile);
//		String occurDate = imagefile.getParentFile().getParentFile().getName();
		List<File> imagefiles = Arrays.asList(imagefile.listFiles());
		if (imagefile == null || imagefiles == null || imagefiles.size() == 0) {
			logger.info("路径下文件不存在");
			return "FAIL";
		}
		for (int i = 1; i < imagefiles.size() + 1; i++) {
			File imageFile = imagefiles.get(i-1);
			String imagepath = imageFile.getPath();
			String fileName = imageFile.getName();
			logger.info(contentID+"<><><>文件部件添加:"+fileName);
			// 添加FileBean
			ClientFileBean fileBean = new ClientFileBean();
			fileBean.setOptionType(OptionKey.U_ADD);
			fileBean.setFileName(imageFile.getPath());
			fileBean.setFileFormat(fileName.substring(fileName.lastIndexOf(".")+1)); //
			//fileBean.setFilesize(imageFile.length() + "");
			// fileBean.setMd5Str(MD5Util.getHash(imagepath,"MD5"));
			fileBean.addOtherAtt("SHOWNAME", i + "");// 00016-ft
			// sort_20160315043417619.xml
			fileBean.addOtherAtt("FILEFORM", "KJ_IMAGE");// KJ_IMAGE// SORT_
			fileBean.addOtherAtt("TRUENAME", fileName);
			fileBean.addOtherAtt("FILEATTR", "1"); // 0,2
			fileBean.addOtherAtt("FILEMD5", MD5Util.getHash(imagepath, "MD5"));
			batchFileBean.addFile(fileBean);
		}
		// // 替换一个文件
		// ClientFileBean clientFileBean2 = new ClientFileBean();
		// clientFileBean2.setOptionType(OptionKey.U_REPLACE);
		// clientFileBean2.setFileNO("B73A7B76-96A8-1094-806F-7730DCFFC024");
		// clientFileBean2.setFileName("D:\\1.jpg");
		// clientFileBean2.setFileFormat("jpg");
		// batchFileBean.addFile(clientFileBean2);
//
//		// 删除一个文件
//		ClientFileBean clientFileBean3 = new ClientFileBean();
//		clientFileBean3.setOptionType(OptionKey.U_DEL);
//		clientFileBean3.setFileNO("8186E0C6-FEC5-1CD5-7559-B8B6F687674F");
//		batchFileBean.addFile(clientFileBean3);
//
//		ClientFileBean clientFileBean4 = new ClientFileBean();
//		clientFileBean4.setOptionType(OptionKey.U_DEL);
//		clientFileBean4.setFileNO("E114898E-9DFE-1E39-8ED4-5C36807455B4");
//		batchFileBean.addFile(clientFileBean4);

		// // 修改文档部件字段
		// ClientFileBean clientFileBean = new ClientFileBean();
		// clientFileBean.setOptionType(OptionKey.U_MODIFY);
		// clientFileBean.setFileNO("B7F0E665-EB2E-A68C-0443-333C2BC80DC4");
		// clientFileBean.addOtherAtt("IMAGEPAGEID", "1");
		// batchFileBean.addFile(clientFileBean);
		// //
		clientBatchBean.addDocument_Object(batchFileBean);
		String resultMsg = "FAIL";
		SunEcmClientApi clientApi = new SunEcmClientSocketApiImpl(ECMIP, ECMPort);
		try {
			resultMsg = clientApi.update(clientBatchBean, groupName,true);
			logger.info("#######更新批次返回的信息[" + resultMsg + "]#######");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMsg;
	}
	
	
	
	public String queryBatch(String contentID, String indexValue) {
		String resultMsg = null;
		ClientBatchBean clientBatchBean = new ClientBatchBean();
		clientBatchBean.setModelCode(modelCode);
		clientBatchBean.setUser(username);
		clientBatchBean.setPassWord(password);
		clientBatchBean.setDownLoad(false);
		clientBatchBean.getIndex_Object().setVersion("0");
		clientBatchBean.getIndex_Object().setContentID(contentID);
		clientBatchBean.getIndex_Object().addCustomMap(indexName, indexValue);
		
		try {
			SunEcmClientApi clientApi = new SunEcmClientSocketApiImpl(ECMIP, ECMPort);
			resultMsg = clientApi.queryBatch(clientBatchBean, groupName);
			logger.info("#######查询批次返回的信息[" + resultMsg + "]#######");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(resultMsg != null){
			resultMsg = resultMsg.replace("0001<<::>>", "");
		}
		return resultMsg;
	}
	
	
	public String queryFile(String contentID, String indexValue,String fileName) {
		String resultMsg = null;
		ClientBatchBean clientBatchBean = new ClientBatchBean();
		clientBatchBean.setModelCode(modelCode);
		clientBatchBean.setUser(username);
		clientBatchBean.setPassWord(password);
		clientBatchBean.setDownLoad(false);
		clientBatchBean.getIndex_Object().setVersion("0");
		clientBatchBean.getIndex_Object().setContentID(contentID);
		clientBatchBean.getIndex_Object().addCustomMap(indexName, indexValue);
		
		//增加file_part条件
		ClientBatchFileBean batchFileBean = new ClientBatchFileBean();
		batchFileBean.setFilePartName(filePartName);
		
		batchFileBean.addFilter("TRUENAME", fileName);		
		clientBatchBean.addDocument_Object(batchFileBean); // 文档部件关联批次
		
		try {
			SunEcmClientApi clientApi = new SunEcmClientSocketApiImpl(ECMIP, ECMPort);
			resultMsg = clientApi.queryBatch(clientBatchBean, groupName);
			logger.info("#######查询批次返回的信息[" + resultMsg + "]#######");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(resultMsg != null){
			resultMsg = resultMsg.replace("0001<<::>>", "");
		}
		return resultMsg;
	}
	

	
	
	
	/*
	 * 高级查询（后督）
	 */
	public String heightQuery(String batchId, String inputDate) {
		String resultMsg = "FAIL";
		ClientHeightQuery heightQuery = new ClientHeightQuery();
		heightQuery.setUserName(username);
		heightQuery.setPassWord(password);
		heightQuery.setLimit(10);
		heightQuery.setPage(1);
		heightQuery.setModelCode(modelCode);
		heightQuery.addCustomAtt(indexName, inputDate);
//		heightQuery.addCustomAtt(indexName, "20181127");
		heightQuery.addCustomAtt("BUSI_SERIAL_NO", batchId);
		
		SunEcmClientApi clientApi = new SunEcmClientSocketApiImpl(ECMIP, ECMPort);
		try {
			resultMsg = clientApi.heightQuery(heightQuery, groupName);
			logger.info("#######调用高级搜索返回的信息[" + resultMsg + "]#######");
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMsg = resultMsg.replace("0001<<::>>", "");
		return resultMsg;
	}
	
	
	/*
	 * 高级查询（通用）
	 */
	public String heightQueryCommon(String fieldName, String batchId, String indexValue) {
		String resultMsg = "FAIL";
		ClientHeightQuery heightQuery = new ClientHeightQuery();
		heightQuery.setUserName(username);
		heightQuery.setPassWord(password);
		heightQuery.setLimit(10);
		heightQuery.setPage(1);
		heightQuery.setModelCode(modelCode);
		heightQuery.addCustomAtt(indexName, indexValue);
		heightQuery.addCustomAtt(fieldName, batchId);
		SunEcmClientApi clientApi = new SunEcmClientSocketApiImpl(ECMIP, ECMPort);
		try {
			resultMsg = clientApi.heightQuery(heightQuery, groupName);
			logger.info("#######调用高级搜索返回的信息[" + resultMsg + "]#######");
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMsg = resultMsg.replace("0001<<::>>", "");
		return resultMsg;
	}
	
	
	
	
	/*
	 * 解析高级查询返回的XML，提取CONTENT_ID
	 */
	public String  parseHeightQueryXML(String XML) throws DocumentException {
		String contentId = "";
		Document document = XmlUtil.stringConvertDoc(XML);
		System.out.println(XML);
		Element root = document.getRootElement();
		Element HeightQuery = root.element("HeightQuery");
		Element indexBeans = HeightQuery.element("indexBeans");
		Element BatchIndexBean = indexBeans.element("BatchIndexBean");
		contentId = BatchIndexBean.attributeValue("CONTENT_ID");
		return contentId;
	}
	
	
	/*
	 * 解析高级查询返回的XML，提取URL
	 */
	@SuppressWarnings("unchecked")
	public String  parseXMLforUrl(String XML) throws DocumentException {
		String url = "";
		Document document = XmlUtil.stringConvertDoc(XML);
		Element root = document.getRootElement();
		Element BatchBean = root.element("BatchBean");
		Element document_Objects = BatchBean.element("document_Objects");
		Element BatchFileBean = document_Objects.element("BatchFileBean");
		List<Element> files = BatchFileBean.elements("files");
		if(files!=null && files.size()>0){
			Element FileBean = files.get(0).element("FileBean");
			url = FileBean.attributeValue("URL");
		}
		
		return url;
	}
	
	
	
	
	/*
	 * 解析高级查询返回的XML，提取URLs
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object>  parseXMLforUrls(String XML,String fileName) throws DocumentException {
		Map<String,Object> map = new HashMap();
		FileUrlBean fileUrl =null;
		Document document = XmlUtil.stringConvertDoc(XML);
		Element root = document.getRootElement();
		Element BatchBean = root.element("BatchBean");
		Element document_Objects = BatchBean.element("document_Objects");
		Element BatchFileBean = document_Objects.element("BatchFileBean");
		String trueName="";
//		List<Element> files = BatchFileBean.elements("files");
		List<Element> fileBeans = BatchFileBean.element("files").elements("FileBean");
		if(fileBeans!=null && fileBeans.size()>0){
			for(Element fileBean : fileBeans){
				fileUrl = new FileUrlBean();
				fileUrl.setFileName(fileBean.element("otherAtt").element("TRUENAME").elementText("string"));
				fileUrl.setUrl(fileBean.attributeValue("URL"));
				trueName = fileBean.element("otherAtt").element("TRUENAME").elementText("string");
				if(trueName.equals(fileName)){
					fileUrl.setUseTime(1);
				}else{
					fileUrl.setUseTime(0);
				}				
				map.put(trueName, fileUrl);
			}
		}		
		return map;
	}

//	public static void main(String[] args) throws Exception {
//		ECMUtil ecmUtil = new ECMUtil("172.1.1.188", 8023, "admin", "111");
//		ecmUtil.set("hc", "ADMS", "ADMS_PART", "CREATEDATE");
	
//		ecmUtil.login();
////		ecmUtil.heightQuery("20180206141342928107", "20180206", "2018020614134292810700004_ft.jpg");
//		
//		String msg = ecmUtil.queryFile("201802_38_D9508BBE-958C-C36B-3470-D0427915C7AD-1", "20180206","2018020614134292810700004_ft.jpg");
//		String url = ecmUtil.parseXMLforUrl(msg);
//		System.out.println("url:"+url);
//	}

}
