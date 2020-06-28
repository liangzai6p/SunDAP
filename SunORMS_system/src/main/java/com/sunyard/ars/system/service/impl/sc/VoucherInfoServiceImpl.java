package com.sunyard.ars.system.service.impl.sc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.sc.VoucherInfo;
import com.sunyard.ars.system.dao.sc.VoucherInfoMapper;
import com.sunyard.ars.system.service.sc.IVoucherInfoService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;


@Service("voucherInfoService")
@Transactional
public class VoucherInfoServiceImpl extends BaseService implements IVoucherInfoService {
	
	@Resource
	private VoucherInfoMapper scVoucherInfoMapper; 

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
		}else if("queryOrganByVoucherName".equals(oper_type)) {
			queryOrganByVoucherName(requestBean, responseBean);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void add(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap=requestBean.getSysMap();
		VoucherInfo voucherInfo = (VoucherInfo) requestBean.getParameterList().get(0);
		//List<VoucherInfo> serExit = scVoucherInfoMapper.selectBySelective(voucherInfo);
		int exits = scVoucherInfoMapper.selectVoucherOnlyByName(voucherInfo.getVoucherName());
		if(exits != 0) {
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("存在相同凭证名称！");
		}else  {
			BigDecimal voucherId = scVoucherInfoMapper.getMaxVoucherId();
			if(voucherId == null) {
				voucherId = new BigDecimal(0);
			}
			voucherId = voucherId.add(new BigDecimal(1));
			voucherId = new BigDecimal(BaseUtil.filterSqlParam(voucherId.toString()));
			voucherInfo.setVoucherId(voucherId);
			scVoucherInfoMapper.insert(voucherInfo);
			String voucherName=voucherInfo.getVoucherName();
			String organNo=(String)sysMap.get("organNo");
			if (!BaseUtil.isBlank(organNo)) {
				if (organNo.indexOf(",") > 0) {
					String[] organArray = organNo.split(",");
					for (int i = 0; i < organArray.length; i++) {
						scVoucherInfoMapper.insertOrgan(voucherName, organArray[i]);
					}
				} else {
					scVoucherInfoMapper.insertOrgan(voucherName, organNo);
				}
			}
			Map retMap = new HashMap();
			retMap.put("voucherId", voucherId);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("添加成功！");
			//添加日志信息
			String log = "凭证类型定义表中新增凭证名称为:" + voucherInfo.getVoucherName() + " 的数据!";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void delete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		List delList = requestBean.getParameterList();
		for(int i=0; i<delList.size(); i++) {
			VoucherInfo voucherInfo = (VoucherInfo)delList.get(i);
			scVoucherInfoMapper.deleteByPrimaryKey(voucherInfo.getVoucherId());
			scVoucherInfoMapper.deleteByVoucherName(voucherInfo.getVoucherName());
			//添加日志信息
			String log = "凭证类型定义表中删除凭证id为:" + voucherInfo.getVoucherId() + " 的数据!";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功！");
	}

	@Override
	public void update(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap=requestBean.getSysMap();
		VoucherInfo voucherInfo = (VoucherInfo) requestBean.getParameterList().get(0);
		String voucherName=voucherInfo.getVoucherName();
		String voucherOldName=(String)sysMap.get("voucherOldName");
		if(!voucherName.equals(voucherOldName)){
			//如果要修改名称
			int exits = scVoucherInfoMapper.selectVoucherOnlyByName(voucherName);
			if(exits != 0 ){
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("存在相同凭证名称！");
				return;
			}
		}
		scVoucherInfoMapper.updateByPrimaryKey(voucherInfo);
		if(!voucherName.equals(voucherOldName)) {//当凭证名字没变时不更新,变了则更新
			//更新SmTradeFormTb信息
			scVoucherInfoMapper.updateTradeFormInfo(voucherName,voucherOldName);
			//更新smocrprechecktb信息
			scVoucherInfoMapper.updateOcrpreCheckTb(voucherName,voucherOldName);
			//更新SmFormManProTb信息
			scVoucherInfoMapper.updateSmFormManProInfo(voucherName,voucherOldName);
			//更新 smformInfotb信息
			scVoucherInfoMapper.updateFormInfo(voucherName,voucherOldName);
		}
		scVoucherInfoMapper.deleteByVoucherName(voucherOldName);// 如果既改了凭证名又改了所属机构,那么应该根据老凭证名删除所属机构,如果改了机构,则老凭证名和新的一致,如果改了凭证名,那么先根据老凭证名删掉再插入新凭证名
		String organNo=(String)sysMap.get("organNo");
		if (!BaseUtil.isBlank(organNo)) {
			if (organNo.indexOf(",") > 0) {
				String[] organArray = organNo.split(",");
				for (int i = 0; i < organArray.length; i++) {
					scVoucherInfoMapper.insertOrgan(voucherName, organArray[i]);
				}
			} else {
				scVoucherInfoMapper.insertOrgan(voucherName, organNo);
			}
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
		//添加日志信息
		String log = "修改凭证类型定义表中的数据!";
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		VoucherInfo voucherInfo = (VoucherInfo) requestBean.getParameterList().get(0);
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
		List<Map> resultList = scVoucherInfoMapper.selectVoucherInfoAndCNT(voucherInfo);
		
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

	@Override
	public void otherOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub

	}
	public void queryOrganByVoucherName(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map sysMap = requestBean.getSysMap();
		Map retMap=new HashMap();
		String voucherName=(String)sysMap.get("voucherName");
		List list =new ArrayList();
		List<Map> organList=scVoucherInfoMapper.selectByVocherName(voucherName);
		retMap.put("organList", organList);
		responseBean.setRetMap(retMap);
	}

	
}
