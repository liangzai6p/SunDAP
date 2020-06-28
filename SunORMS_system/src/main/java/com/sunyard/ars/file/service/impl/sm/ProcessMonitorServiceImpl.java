
package com.sunyard.ars.file.service.impl.sm;


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
import com.sunyard.ars.common.dao.FlowMapper;
import com.sunyard.ars.common.dao.TmpBatchMapper;
import com.sunyard.ars.common.dao.TmpDataMapper;
import com.sunyard.ars.file.dao.oa.CheckOffMapper;
import com.sunyard.ars.file.dao.scan.VoucherChkMapper;
import com.sunyard.ars.file.pojo.sm.ProcessTb;
import com.sunyard.ars.file.service.sm.IProcessMonitorService;
import com.sunyard.ars.system.bean.busm.Teller;
import com.sunyard.ars.system.dao.busm.OrganInfoDao;
import com.sunyard.ars.system.dao.busm.TellerMapper;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;



@Service("processMonitorService")
@Transactional
public class ProcessMonitorServiceImpl extends BaseService implements IProcessMonitorService{
	@Resource
	private TmpBatchMapper tmpBatchMapper;
	
	@Resource
	private TmpDataMapper tmpDataMapper;
	
	@Resource
	private CheckOffMapper checkOffMapper;
	
	@Resource
	private VoucherChkMapper voucherChkMapper;
	
	@Resource
	private FlowMapper flowMapper;
	
	@Resource
	private OrganInfoDao organInfoDao;
	@Resource
	private TellerMapper tellerMapper;
	
	
	

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
		if(ARSConstants.OPERATE_QUERY.equals(oper_type)){
			//业务进度信息查询
			query(requestBean, responseBean);
		}
	}
	
	
	/**
	 *业务进度查询
	 * @param requestBean
	 * @param responseBean
	 * @author YQJ
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void query(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取页面参数
		Map sysMap = requestBean.getSysMap();
		String userNo = BaseUtil.getLoginUser().getUserNo();//获取登录的用户名
		String organNo = (String) sysMap.get("organNo");// 接收从页面传输的机构号
		String occurDate = (String) sysMap.get("occurDate");// 接收从页面传输的业务日期
		Map tmpMap = new HashMap();
		Teller teller = new Teller();
		List flowList = new ArrayList();
		List<ProcessTb> processList = new ArrayList<ProcessTb>();
		List batchList = new ArrayList();
		long totalCount = 0;
		List<String> tellNoSql = new ArrayList<String>();
		List<String> organsStr=new ArrayList<String>();
		ProcessTb process = null;
		int flag = 0;
		String objNo = "";
		int count = 0;
		int currentPage = (int) sysMap.get("currentPage");//获取当前的页面号
		int pageSize = 0;
		if (currentPage != -1) {
			Integer initPageSize = (Integer) sysMap.get("pageSize");
			if (initPageSize == null) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageSize;
			}
		}
		// startPage放在所执行的查询上面,代表该查询需要分页,使之命名为page1是因为所选的如果不是网点,那么就按照机构分页,如果是网点那么就按照柜员分页,由于所查询出的总条数,因此做一下判断
		Page page1 = PageHelper.startPage(currentPage, pageSize);
		// 执行查询
		List<HashMap<String, Object>> privateOrganList = organInfoDao.belongOrganList(organNo, userNo,(String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//获取所属的机构列表
		totalCount=page1.getTotal();//获取查询出的机构总数
		if (null == privateOrganList || privateOrganList.size() <= 1) {// 按网点柜员统计
			teller.setParentOrgan(organNo);//网点柜员的话机构一样的
			Page page2 = PageHelper.startPage(currentPage, pageSize);//对查询的柜员进行分页查询
			List<Teller> tellerList = tellerMapper.selectBySelective(teller);//查询出柜员列表
			totalCount=page2.getTotal();//获取柜员总数
			for (Teller tell : tellerList) {//遍历柜员集合,然后把遍历出来的数据以ProcessTb对象的形式写进tmpMap中
				 process = new ProcessTb();
				process.setObj_no(tell.getTellerNo());
				process.setObj_name(tell.getTellerName());
				tmpMap.put(tell.getTellerNo(), process);

			}
			if (tellerList != null && tellerList.size() > 0) {//把机构号存入一个list集合中
				for (int i = 0; i < tellerList.size(); i++) {
					tellNoSql.add(BaseUtil.filterSqlParam(tellerList.get(i).getTellerNo()));
					/*if (i != tellerList.size() - 1) {
						tellNoSql.append(tellerList.get(i).getTellerNo());
						tellNoSql.append(",");
					} else {
						tellNoSql.append(tellerList.get(i).getTellerNo());

					}*/
				}

			}/* else {
				//tellNoSql.append("''");
			}*/
			flowList = flowMapper.getFlowCount2(tellNoSql,occurDate);//查询柜员流水信息
			batchList = tmpBatchMapper.getBatchCount2(tellNoSql,occurDate);//查询柜员批次信息
		}else {//按所属机构统计
			for(Map organ : privateOrganList){//遍历查询出来的机构信息然后以ProcessTb对象的形式存放金tmpMap中
				process = new ProcessTb();
				process.setObj_no((String)organ.get("ORGAN_NO"));
				process.setObj_name((String)organ.get("ORGAN_NAME"));
				tmpMap.put((String)organ.get("ORGAN_NO"), process);
			}
			//hjf 20130515
			if(privateOrganList!=null){//拼装查询sql
				for(int i=0;i<privateOrganList.size();i++){
					organsStr.add(BaseUtil.filterSqlParam((String)privateOrganList.get(i).get("ORGAN_NO")));
					/*if(i!=privateOrganList.size()-1){
						organsStr.append(privateOrganList.get(i).get("ORGAN_NO"));
						organsStr.append(",");
					}
					else{
						organsStr.append(privateOrganList.get(i).get("ORGAN_NO"));
						
					}*/
				}
				flowList = flowMapper.getFlowCount(organsStr,occurDate);
				batchList = tmpBatchMapper.getBatchCount(organsStr,occurDate);
				
			}
		}
		for(Object strTmp:flowList){//遍历查询出来的flowList,由于按照柜员和按照机构查询的数据字段不一样,所以做一个判断
			Map strs=(Map)strTmp;
			for (Object key:strs.keySet()) {
				if(key.equals("SITE_NO") || key.equals("OPERATOR_NO")){
					objNo =(String)strs.get(key);
				}
			}
			flag=Integer.parseInt((String)strs.get("CHECK_FLAG"));
			count = Integer.parseInt(strs.get("CNT")+"");
			process = (ProcessTb)tmpMap.get(objNo);
			process.setFlowCount(process.getFlowCount()+count);
			if(flag == -1){
				process.setFlowNoCheckCount(process.getFlowNoCheckCount()+count);
			}
			else{
				process.setFlowCheckCount(process.getFlowCheckCount()+count);
			}
			
			tmpMap.put(objNo, process);
		}
		
		for(Object strTmp:batchList){
			Map strs=(Map)strTmp;
			for (Object key:strs.keySet()) {
				  if(key.equals("SITE_NO") || key.equals("OPERATOR_NO")){
					objNo =(String)strs.get(key);
				}
			}
			flag=Integer.parseInt((String) strs.get("PROGRESS_FLAG"));
			count = Integer.parseInt(strs.get("CNT")+"");
			process = (ProcessTb)tmpMap.get(objNo);
			process.setScanCount(process.getScanCount()+count);
			if(flag == 20){
				process.setBatchTwoCount(process.getBatchTwoCount()+count);
			}
			else{
				if(flag>20){
					process.setBatchOverTwoCount(process.getBatchOverTwoCount()+count);
					process.setBatchOverThreeCount(process.getBatchOverThreeCount()+count);
				}
				else if(flag>30){
					process.setBatchOverThreeCount(process.getBatchOverThreeCount()+count);
				}
			}
			tmpMap.put(objNo, process);
		}
		for(Object key:tmpMap.keySet()){
			processList.add((ProcessTb) tmpMap.get(key));
		}
		Map retMap = new HashMap();
		retMap.put("currentPage", currentPage);
		retMap.put("totalNum", totalCount);
		retMap.put("returnList", processList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
		//添加日志
		String log = "查看业务进度监控信息，机构号为" + organNo + "日期为" + occurDate;
		addOperLogInfo(ARSConstants.MODEL_NAME_ACCOUNT_AUDIT, ARSConstants.OPER_TYPE_4, log);

	}
    
    
    
    

  
    
}
