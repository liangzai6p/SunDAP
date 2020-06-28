package com.sunyard.ars.file.service.impl.scan;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.aos.common.comm.BaseService;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;

import com.sunyard.ars.common.util.ScanEcmUtil;
import com.sunyard.ars.file.dao.scan.ScanEcmMapper;
import com.sunyard.ars.file.service.scan.IScanEcmForUrlService;
import com.sunyard.ars.system.bean.sc.OrganData;
import com.sunyard.ars.system.bean.sc.SCDatasource;
import com.sunyard.ars.system.bean.sc.ServiceReg;
import com.sunyard.ars.system.dao.sc.OrganDataMapper;
import com.sunyard.ars.system.dao.sc.SCDatasourceMapper;
import com.sunyard.ars.system.dao.sc.ServiceRegMapper;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import cn.net.sinodata.image.client.SinoImageClient;
import cn.net.sinodata.image.client.bean.BatchInfo;
import cn.net.sinodata.image.client.bean.ImageInfo;

@Service("scanEcmForUrlService")
@Transactional
public class ScanEcmForUrlServiceImpl extends BaseService implements IScanEcmForUrlService {

	@Resource
	private ScanEcmMapper scanEcmMapper;
	
	@Resource
	private SCDatasourceMapper scDatasourceMapper;
	
	@Resource
	private ServiceRegMapper serviceRegMapper ;
	@Resource
	private OrganDataMapper organDataMapper;

	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		// TODO Auto-generated method stub
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		// 获取操作标识
		String oper_type = (String) sysMap.get("oper_type");
		// "channel_id" : "ecm",//ecm-公司影像平台，sinoimg-华夏影像系统
		String channel_id = (String) sysMap.get("channel_id");
		if (ARSConstants.OPERATE_QUERY.equals(oper_type) && channel_id.equals("ecm")) {
			// 从Ecm获url
			scanEcmForUrl(requestBean, responseBean);
		} else if (ARSConstants.OPERATE_QUERY.equals(oper_type) && channel_id.equals("sinoimg")) {
			// 从sinoimg获url
			getImageURL(requestBean, responseBean);
			
			//直接从大数据获取url             暂时屏蔽，先弄pad  20181218
//			getImageURLnew(requestBean, responseBean);
		}else if ("QUERYSINOVOUCHER".equals(oper_type)){
			querySinoVoucherData(requestBean, responseBean);
		}else if ("QUERYPADVOUCHER".equals(oper_type)){
			queryPadVoucherData(requestBean, responseBean);
		}

	}
	
	/**
	 * 获取一笔业务的凭证信息(用于查看移动pad系统)
	 * 	請求：
		1	YSY_ID	系统ID	C	12
		2	CONTENT_ID	批次号	C	64
		3	FILE_NO 	文件编号	C	48
		
		應答：
		1	Doc_id	批次号	C	64
		2	CREATEDATE	创建日期	C	12
		3	FILE_NO	文件编号	C	48
		4	URL	文件URL	C	255
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryPadVoucherData(RequestBean requestBean, ResponseBean responseBean) {

	}
	
	/**
	 * 获取一笔业务的凭证信息(用于查看华夏影像系统)
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void querySinoVoucherData(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		Map retMap = new HashMap();
		String batchId = ((Map)requestBean.getParameterList().get(0)).get("batchId").toString();
		String organNo = ((Map)requestBean.getParameterList().get(0)).get("organNo").toString();
		Object tNObject= ((Map)requestBean.getParameterList().get(0)).get("transName");
		String transName = tNObject == null ?"":tNObject.toString().trim()+ "-";
		
		Map sysMap = requestBean.getSysMap();
		sysMap.put("batch_no",batchId);
		sysMap.put("organ_no",organNo);
		//直接从大数据获取url             暂时屏蔽，先弄pad  20181218
//		getImageURLnew(requestBean, responseBean);
		getImageURL(requestBean, responseBean);
		if (responseBean.getRetCode().equals(ARSConstants.HANDLE_FAIL)) {
			return;
		}
		
		List resultlist = new ArrayList();
		retMap = responseBean.getRetMap();
		resultlist = (List) retMap.get("returnList");
		
		List<HashMap<String, Object>> list =  new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < resultlist.size(); i++) {
			HashMap mapt = (HashMap) resultlist.get(i);
			HashMap<String, Object> tmpm = new HashMap<String, Object>();
			
			tmpm.put("FORM_NAME", transName + mapt.get("re_urlname"));
			tmpm.put("URL", mapt.get("re_url"));
			tmpm.put("PS_LEVEL", "1");
			list.add(tmpm);
		}
		
		retMap.put("tmpDataList", list);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void scanEcmForUrl(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		HashMap condMap = new HashMap();
		String flag = "0";//0:正常，  1：正常查询结果为空，  2：查询异常
		/*
		 * "busi_date":"20180808",//业务日期 "organ_no":"9999",//机构号
		 * "teller_no":"10001",//柜员号 "flow_no":"1010101"//流水号
		 */
		List resultlist = new ArrayList();
		try {
			logger.info("Scan2EcmImageClient--业务日期busi_date:" + sysMap.get("busi_date") 
			+ " 流水号flow_no:" + sysMap.get("flow_no") 
			+ " 机构号organ_no:" + sysMap.get("organ_no") 
			+ " 柜员号teller_no:" + sysMap.get("teller_no") );
			condMap.put("occurDate", sysMap.get("busi_date"));// 业务日期
			condMap.put("flowId", sysMap.get("flow_no"));// 流水号
			String siteNo= sysMap.get("organ_no").toString();
			condMap.put("siteNo", siteNo);// 机构号
			condMap.put("operatorNo", sysMap.get("teller_no"));// 柜员号
			List<HashMap<String, Object>> batchdatelist = scanEcmMapper.getBatchIdandInputDate(condMap);
			
			if (batchdatelist == null || batchdatelist.size() < 1) {
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("无对应图像可查看");
				logger.info("未找到该批次，请联系管理员");
				return;
			}
			HashMap<String, Object> map = batchdatelist.get(0);
			String batchId = (String) map.get("BATCH_ID");
			String inputDate = (String) map.get("INPUT_DATE");
			
			// 获取ecm的contantid
			Map<String, String> ecmMap = new HashMap<String, String>();
			ecmMap = getEcmPropertisMap(siteNo);		
			if (ecmMap.isEmpty()) {
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("无对应图像可查看");
				logger.info("未获取ecm服务相关信息，请联系管理员");
				return;
			}
			ScanEcmUtil aeu = new ScanEcmUtil(ecmMap);
			
			String contentId = aeu.getContentId(batchId, inputDate);
			if (contentId == null || contentId.length() < 1) {
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("无对应图像可查看");
				logger.info("获取后督批次：" + BaseUtil.filterLog(batchId) + "对应的ECM影像批次失败，请联系管理员");
				return;
			}
			
			logger.info("获取后督批次" + BaseUtil.filterLog(batchId) + "对应的ECM图像批次号：" + contentId);
			String imageRet = aeu.queryImage(contentId, inputDate);
			if (imageRet.startsWith("FAIL<<::>>")) {
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("无对应图像可查看");
				logger.info("查询ecm批次：" + contentId + "图片失败！请联系管理员");
				return;
			}
			
			condMap.put("batchId", batchId);// 批次
			List<HashMap<String, Object>> fNamesList = scanEcmMapper.getFileNamesList(condMap);
			List<String> fileNamesList = new ArrayList<String>();//存储url名称
			for (int i = 0; i < fNamesList.size(); i++) {
				HashMap<String, Object> mapfile = fNamesList.get(i);
				String FILE_NAME = (String) mapfile.get("FILE_NAME");
				String BACK_FILE_NAME = (String) mapfile.get("BACK_FILE_NAME");
				fileNamesList.add(FILE_NAME);
				fileNamesList.add(BACK_FILE_NAME);
			}
			// 
			String imgString = BaseUtil.formatJson(imageRet);
			List list = (List) BaseUtil.transJson2Obj(imgString, ArrayList.class);
			
			for(String tmpFileName : fileNamesList){
				for (int i = 0; i < list.size(); i++) {
					HashMap mapt = (HashMap) list.get(i);
					String file_format = (String) mapt.get("file_format");
					if ("xml".equalsIgnoreCase(file_format) || "db".equalsIgnoreCase(file_format)) {
						continue;
					}
					
					Map otherAtt = (Map) mapt.get("otherAtt");
					String fileTrueName = String.valueOf(otherAtt.get("TRUENAME"));
					//根据flowid batchid 过滤
					if (tmpFileName.equals(fileTrueName)) {
						String url = (String) mapt.get("url");
						Map tmpm = new HashMap<String, String>();
						tmpm.put("re_urlname", fileTrueName);
						tmpm.put("re_url", url);
						resultlist.add(tmpm);
					}
				}
			}
			if(resultlist.size() == 0) flag = "1";

		} catch (Exception e) {
			logger.info(e.getMessage());
			flag = "2";
		}
		logger.info("查询影像url成功");
		logger.info("Scan2EcmImageClient--返回returnFlag:" + flag );
		if (flag.equals("2")) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("图像无法显示");
		}else if(flag.equals("1")){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("无对应图像可查看");
		}else{
			// 拼装返回信息
			Map retMap = new HashMap();
			retMap.put("returnList", resultlist);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getImageURLnew(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map sysMap = requestBean.getSysMap();
		String batchNo = (String) sysMap.get("batch_no");// 批次号(后督批次或华夏影像系统批次)
//		String siteNo= sysMap.get("organ_no").toString();// 机构号

		if (BaseUtil.isBlank(batchNo) ) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("查询批次号：" + batchNo +  "，图片失败！批次号不能为空！");
			logger.error("查询批次号：" + batchNo +  "，图片失败！批次号不能为空！");
			return;
		}

		List<String> fileNames =  scanEcmMapper.getFileNames(batchNo);
		if(fileNames == null || fileNames.size()<1){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("查询批次号：" + batchNo +  "，图片失败！BIZ_IMAGE, BIZ_IMAGE_METADATA没有对应的图片名称，无法去大数据查询！");
			logger.error("查询批次号：" + batchNo +  "，图片失败！BIZ_IMAGE, BIZ_IMAGE_METADATA没有对应的图片名称，无法去大数据查询！");
			return;
		}
		logger.info("-------->Sino已获得fileNames ");

		List resultlist = new ArrayList();
		/*for (String fileName : fileNames) {
			Map<String, String> remap = querySinoUrlFromData(batchNo,fileName); 
			if(remap != null){
				String sinoUrl = remap.get("URL");
				String sinoFileName = remap.get("FORM_NAME");
				logger.info(sinoUrl);// 影像的下载url
				if(BaseUtil.isBlank(sinoUrl) || BaseUtil.isBlank(sinoFileName)){
					
				}else{
					Map<String, String> map = new HashMap<String, String>();
					map.put("re_urlname", sinoFileName);
					map.put("re_url", sinoUrl);
					resultlist.add(map);
				}
			}
		}*/

		Map retMap = new HashMap();
		retMap.put("returnList", resultlist);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");

	}	
	/**
	 * 通过Tong从大数据查询华夏影像系统的url
	 * @param batchNo
	 * @param fileName
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("null")
	private Map<String, String> querySinoUrlFromData(String batchNo, String fileName) throws Exception  {
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getImageURL(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// private static Map<String,String> getImageURL(String batchNo) {

		Map sysMap = requestBean.getSysMap();
		
		String ip = "";
		String httpPort = "";
		String socketPort = "";
		// String batchNo = "";
		String iclsId = "";
		String temFolder = "";
		String localFolder = "";

		Properties prop = new Properties();

		InputStream in = null;
		String batchNo = "";
		try {
			batchNo = (String) sysMap.get("batch_no");// 批次号(后督批次或华夏影像系统批次)
			String siteNo= sysMap.get("organ_no").toString();// 机构号
			
			if (BaseUtil.isBlank(batchNo) || BaseUtil.isBlank(siteNo)) {
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("无对应图像可查看");
				logger.info("查询批次号：" + batchNo +  "，机构号：" + siteNo + "图片失败！不能为空！");
				return;
			}
			logger.info("Scan2SinoImageClient--批次号batchNo:" + batchNo + "，机构号organ_no：" + siteNo );
			in = getClass().getClassLoader().getResourceAsStream("SinoImageClient.properties");
			//in = new BufferedInputStream(new FileInputStream("../resources/SinoImageClient.properties"));
			prop.load(in);
			ip = prop.getProperty("sinoimage.ip");
			httpPort = prop.getProperty("sinoimage.httpPort");
			socketPort = prop.getProperty("sinoimage.socketPort");
			// batchNo = prop.getProperty("sinoimage.batchNo");
			iclsId = prop.getProperty("sinoimage.iclsId");
			temFolder = prop.getProperty("sinoimage.temFolder");
			localFolder = prop.getProperty("sinoimage.localFolder");
			
			HashMap condMap = new HashMap();
			condMap.put("siteNo", siteNo);
			List<HashMap<String, Object>> iplist = scanEcmMapper.getImageIPConfig(condMap);
			if (iplist != null && iplist.size() > 0) {
				HashMap<String, Object> ipmapHashMap = iplist.get(0);
				ip = (String) ipmapHashMap.get("IP");
				httpPort = (String) ipmapHashMap.get("HTTPPORT");
				socketPort = (String) ipmapHashMap.get("SOCKETPORT");
				logger.info("获取到 （SM_IMG_ORGCONFIG）表中信息： ip:"+BaseUtil.filterLog(ip)+",httpPort:"+BaseUtil.filterLog(httpPort)+",socketPort:"+BaseUtil.filterLog(socketPort));
			}else{
				logger.info("未获取到 （SM_IMG_ORGCONFIG）表中信息： 将使用配置项（SinoImageClient.properties）");
			}
			//iclsId取值处理（ASYNCA、IMMEDA）
			iclsId = batchNo.length() > 21 ? batchNo.substring(16, 22) : iclsId;
			

		} catch (Exception e1) {
			logger.info(e1.getMessage());
			// e1.printStackTrace();
		} finally {
			if(in != null)
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

		List resultlist = new ArrayList();
		SinoImageClient client = null;
		String flag = "0";//0:正常，  1：正常查询结果为空，  2：查询异常
		try {
			client = new SinoImageClient(ip, httpPort, socketPort, temFolder);
			logger.info("已连接服务器");		
			BatchInfo batch = new BatchInfo();
			batch = client.queryBatchByNo(iclsId, batchNo);
			
			List<ImageInfo> images = batch.getImages();
			logger.info("已获得Image");
			
			for (ImageInfo imageInfo : images) {
				Map<String, String> map = new HashMap<String, String>();
				logger.info("Scan2SinoImageClient--返回url:" + imageInfo.getImgPath());// 影像的下载url
//			    map.put(imageInfo.getImgName(), imageInfo.getImgPath());
				map.put("re_urlname", imageInfo.getImgName());
				map.put("re_url", imageInfo.getImgPath() + "&type=0");
				resultlist.add(map);
			}
			if(resultlist.size() == 0) flag = "1";
		} catch (Exception e) {
			logger.info(e.getMessage());
			flag = "2";
		}finally {
			// 关闭socket连接
			if (client != null)
				client.dispose();
			logger.info("关闭连接");
		}

		logger.info("Scan2SinoImageClient--返回returnFlag:" + flag );
		if (flag.equals("2")) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("图像无法显示");
		}else if(flag.equals("1")){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("无对应图像可查看");
		}else{
			Map retMap = new HashMap();
			retMap.put("returnList", resultlist);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}

	}
	/**
	 *  ip = "192.168.199.139";
		port = 8021;
		userNo = "admin";
		password = "111";
		groupName = "server";
		modelCode = "HD";
		filePart = "HD_PART";
		startColumn = "CREATEDATE";
	 * @return 获取ecm服务相关信息
	 */
	private Map<String, String> getEcmPropertisMap(String siteNo) throws Exception{
		OrganData organData = new OrganData();
		organData.setOrganNo(siteNo);
		List<OrganData> selectBySelective = organDataMapper.selectBySelective(organData);
		if(selectBySelective==null||selectBySelective.size()==0){
			return null;
		}
		String ecmserviceId = selectBySelective.get(0).getEcmserviceId();
		if(ecmserviceId==null||ecmserviceId.equals("")){
			return null;
		}
		Map<String, String> retMap = new HashMap<String, String>();
		SCDatasource source = scDatasourceMapper.selectByPrimaryKey(BaseUtil.filterSqlParam(ecmserviceId));
		ServiceReg serviceReg = serviceRegMapper.selectByPrimaryKey(new BigDecimal(BaseUtil.filterSqlParam(source.getServiceId())));
		String groupName = source.getGroupName();//servergroup
		String modeCode = source.getModeCode();//HD
		String filePartName = source.getFilePartName();//HD_PART
		String startColumn = source.getIndexName();
		String serviceIp = serviceReg.getServiceIp();//192.168.199.139
		String servicePort = serviceReg.getServicePort();//8021
		String loginName = serviceReg.getLoginName();//admin
		String loginPass = serviceReg.getLoginPass();//111
		retMap.put("ecm_user", loginName);	
		retMap.put("ecm_pwd", loginPass);
		retMap.put("ecm_ip", serviceIp);
		retMap.put("ecm_port", servicePort);
		retMap.put("groupName", groupName);
		retMap.put("modelCode", modeCode);
		retMap.put("filePart", filePartName);
		retMap.put("startColumn", startColumn);
		return retMap;
	}

}
