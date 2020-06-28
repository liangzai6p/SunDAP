package com.sunyard.ars.file.service.impl.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.sunyard.aos.common.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.aos.common.util.ExcelUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.pojo.TmpBatch;
import com.sunyard.ars.common.util.StringUtil;
import com.sunyard.ars.file.dao.scan.VoucherChkMapper;
import com.sunyard.ars.file.pojo.scan.VoucherChk;
import com.sunyard.ars.file.service.file.IFileVoucherChkService;
import com.sunyard.ars.system.bean.sc.BusinessType;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.pojo.User;


@Service("fileVoucherChkService")
@Transactional
public class FileVoucherChkServiceImpl extends BaseService implements IFileVoucherChkService {
	
	@Resource
	private VoucherChkMapper voucherChkMapper; 

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
		}else if("checkIsCommit".equals(oper_type)){
			checkIsCommit(requestBean, responseBean);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void add(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		VoucherChk voucherChk = (VoucherChk) requestBean.getParameterList().get(0);
		
		Date date = new Date();
		String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(date);
		String HHmmssSSS = new SimpleDateFormat("HHmmssSSS").format(date);
		SecureRandom random = new SecureRandom();
		String batchId = yyyyMMdd + HHmmssSSS + random.nextInt(10)
		+ random.nextInt(10) + random.nextInt(10);
		
		voucherChk.setBatchCommit(ARSConstants.BATCH_NOT_COMMIT);
		voucherChk.setBatchId(batchId);
		voucherChk.setRegDate(yyyyMMdd);
		voucherChk.setRegTime(HHmmssSSS.substring(0, 6));
		voucherChk.setIsInvalid(ARSConstants.BATCH_IS_VALID);
		voucherChk.setIsClose("1");
		User user = BaseUtil.getLoginUser();
		voucherChk.setMachineId(BaseUtil.filterSqlParam(StringUtil.getIP(RequestUtil.getRequest())));
		voucherChk.setRegWorker(user.getUserNo());
		voucherChk.setInputWorker(user.getUserNo());
		
		voucherChkMapper.insertSelective(voucherChk);
		Map retMap = new HashMap();
		retMap.put("voucherChk", voucherChk);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("添加成功！");
		
		//添加日志
		String log = "会计稽核模块案例登记新增,批次码为:" + voucherChk.getBatchId() + "的批次。";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_1, log);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		List delList = requestBean.getParameterList();
		for(int i=0; i<delList.size(); i++) {
			VoucherChk voucherChk = (VoucherChk)delList.get(i);
			voucherChkMapper.deleteByPrimaryKey(voucherChk.getId());
			
			//添加日志
			String log = "会计稽核模块案例登记,删除id为:" + voucherChk.getId() + "的批次。";
			addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_1, log);
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功！");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		VoucherChk voucherChk = (VoucherChk) requestBean.getParameterList().get(0);
		voucherChkMapper.updateByPrimaryKeySelective(voucherChk);
		Map retMap = new HashMap();
		voucherChk = voucherChkMapper.selectByPrimaryKey(voucherChk.getId());
		retMap.put("voucherChk", voucherChk);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
		//添加日志
		String log = "会计稽核模块案例登记修改id为:" + voucherChk.getId() + "的批次。";
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_3, log);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
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
		Map condition = (Map)sysMap.get("condition");//查询条件
		Page page = PageHelper.startPage(currentPage, pageSize);
		List resultList = voucherChkMapper.selectByConditionWithTmpBatch(condition);
		long totalCount = page.getTotal();
		Map retMap = new HashMap();
		retMap.put("currentPage", currentPage);
		retMap.put("pageSize", pageSize);
		retMap.put("totalNum", totalCount);
		retMap.put("returnList", resultList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		String otherOperFlag = (String)sysMap.get("other_operator");
		if("isExist".equals(otherOperFlag)) {
			VoucherChk voucherChk = (VoucherChk) requestBean.getParameterList().get(0);
			int num = voucherChkMapper.isExists(voucherChk);
			Map retMap = new HashMap();
			retMap.put("existNum", num);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else if("barCode".equals(otherOperFlag)) {
			VoucherChk voucherChk = (VoucherChk) requestBean.getParameterList().get(0);
			String batchId = voucherChk.getBatchId();
			HttpSession session = RequestUtil.getRequest().getSession();
			String imgPath = session.getServletContext().getRealPath("") + File.separator +ARSConstants.FILE_BATCH_BAR_CODE_IMG_PATH +batchId+".png";
			if("1".equals(ARSConstants.FILE_BATCH_BAR_CODE_TYPE)) {
				linearBarCode(batchId, 0, 0, imgPath);
			}else if("2".equals(ARSConstants.FILE_BATCH_BAR_CODE_TYPE)) {
				Map contentMap = new HashMap(); //只取必要的字段，防止二维码信息过多显示效果不好。
				contentMap.put("id", voucherChk.getId());
				contentMap.put("businessId", voucherChk.getBusinessId());
				contentMap.put("occurDate", voucherChk.getOccurDate());
				contentMap.put("siteNo", voucherChk.getSiteNo());
				contentMap.put("operatorNo", voucherChk.getOperatorNo());
				contentMap.put("regVoucherNum", voucherChk.getRegVoucherNum());
				contentMap.put("batchId", voucherChk.getBatchId());
				contentMap.put("needProcess", voucherChk.getNeedProcess());
				contentMap.put("mixedNpFlag", voucherChk.getMixedNpFlag());
				ObjectMapper objectMapper = new ObjectMapper();
			    String contents = objectMapper.writeValueAsString(contentMap);
				matrixBarCode(contents, 200, 200, imgPath);
			}
			Map retMap = new HashMap();
			retMap.put("imgPath", ARSConstants.FILE_BATCH_BAR_CODE_IMG_PATH+batchId+".png");
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("生成成功");
		}else if("fileExport".contentEquals(otherOperFlag)) {
			Map condition = (Map)sysMap.get("condition");//查询条件
			String fileName = System.currentTimeMillis()+(String)sysMap.get("fileName");
			List resultList = voucherChkMapper.selectByConditionWithTmpBatch(condition);
			String title = "档案批次信息("+condition.get("startDate")+"-"+condition.get("endDate")+")";
			LinkedHashMap<String, String> headMap = new LinkedHashMap<String, String>();
			headMap.put("regDate", "登记日期");			headMap.put("occurDate", "交易日期");			headMap.put("siteNo", "交易机构");
			headMap.put("operatorNo", "交易柜员");			headMap.put("batchId", "批次码                   ");				headMap.put("businessId", "档案类型");
			headMap.put("regVoucherNum", "凭证登记张数");	headMap.put("scanVoucherNum", "实际凭证张数");	//headMap.put("isClose", "是否组包");	
			headMap.put("scanUser", "扫描员");            headMap.put("batchCommit", "是否已扫描");					headMap.put("scanDate", "扫描日期");	
			headMap.put("isInvalid", "是否有效");
			List<Map> data = new ArrayList<Map>();
			for(int i=0; i<resultList.size(); i++) {
				VoucherChk vc = (VoucherChk) resultList.get(i);
				Map map = new HashMap();
				BusinessType bt = vc.getBusinessType() == null ? new BusinessType() : vc.getBusinessType();
				TmpBatch tb = vc.getTmpBatch() == null ? new TmpBatch() : vc.getTmpBatch();
				map.put("regDate", vc.getRegDate());	map.put("occurDate", vc.getOccurDate());map.put("siteNo", vc.getSiteNo());
				map.put("operatorNo",vc.getOperatorNo());map.put("batchId", vc.getBatchId());	map.put("businessId", bt.getBusinessId()+"-"+bt.getBusinessName());
				map.put("regVoucherNum", vc.getRegVoucherNum());	map.put("scanVoucherNum", tb.getBatchTotalPage());	map.put("isClose", "1".equals(vc.getIsClose())?"1-已组包":"0-未组包");	
				map.put("scanUser", tb.getInputWorker());    map.put("batchCommit", "1".equals(vc.getBatchCommit())?"1-已扫描":"0-未扫描");		 map.put("scanDate", tb.getInputDate());	
				map.put("isInvalid", "1".equals(vc.getIsInvalid())?"1-有效":"0-无效");
				data.add(map);
			}
			boolean retFlag = ExcelUtil.createExcelFile(ARSConstants.FILE_EXCEL_PATH, fileName, title, headMap, data);
			if(retFlag) {
				Map retMap = new HashMap();
				//retMap.put("filePath", ARSConstants.FILE_EXCEL_PATH+File.separator+fileName);
				//url地址栏当中出现  /gtp/yyfx/excelfile/\1575353737549DocumentAndBatchInfos.xls 中有 \ 会报错
				String filePath = ARSConstants.FILE_EXCEL_PATH;
				String isExit = filePath.charAt(filePath.length()-1) + "";
				if("/".equals(isExit)) {
					filePath += fileName;
				}else {
					filePath += "/" + fileName;
				}
				retMap.put("filePath", filePath);
				responseBean.setRetMap(retMap);
				responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
				responseBean.setRetMsg("生成成功");
			}else {
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("xls文件生成失败！");
			}
		}
	}
	
	/**
	 * 条形码（一维码）生成
	 * @param contents 条形码内容
	 * @param width 长度
	 * @param heigth 高度
	 * @param imgPath 生成图像地址
	 * @throws WriterException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void linearBarCode(String contents, int width, int heigth, String imgPath) throws Exception {
		int codeWidth = Math.max(75, width);
		int codeHeight = Math.max(25, heigth);
		FileOutputStream fos = null;
		try {
			File file = new File(imgPath);
			File parent = file.getParentFile();
			if(!parent.exists()) {
				parent.mkdirs();
			}
			fos = new FileOutputStream(file);
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.CODE_128, codeWidth, codeHeight);
			MatrixToImageWriter.writeToStream(bitMatrix, "png", fos);
		} finally {
			// TODO: handle finally clause
			if(fos!=null){
				FileUtil.safeClose(fos);
			}
		}
	}
	
	/**
	 * 二维码生成
	 * @param contents 二维码内容
	 * @param width 长度
	 * @param heigth 高度
	 * @param imgPath 生成图像地址
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void matrixBarCode(String contents, int width, int heigth, String imgPath) throws Exception {
		int codeWidth = Math.max(100, width);
		int codeHeight = Math.max(100, heigth);
		Map hints = new HashMap();  
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		FileOutputStream fos = null;
		try {
			File file = new File(imgPath);
			File parent = file.getParentFile();
			if(!parent.exists()) {
				parent.mkdirs();
			}
			fos = new FileOutputStream(file);
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, codeWidth, codeHeight, hints);  
			MatrixToImageWriter.writeToStream(bitMatrix, "png", fos);
		} finally {
			// TODO: handle finally clause
			if(fos!=null){
				FileUtil.safeClose(fos);
			}

		}
	}
	
	/**
	 * 检查批次是否扫描提交
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	public void checkIsCommit(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map sysMap = requestBean.getSysMap();
		// 获取操作标识
		Integer vId =  (Integer) sysMap.get("vId");
		//查询批次
		VoucherChk voucherChk = voucherChkMapper.selectByPrimaryKey(new BigDecimal(vId));
		if ("1".equals(voucherChk.getBatchCommit())) {
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("该批次已扫描提交");
			return;
		} 
		responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
		responseBean.setRetMsg("批次可以处理");
	}

}
