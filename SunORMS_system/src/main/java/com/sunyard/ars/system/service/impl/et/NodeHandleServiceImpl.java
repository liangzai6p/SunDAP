package com.sunyard.ars.system.service.impl.et;

import java.io.File;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.util.DateUtil;
import com.sunyard.ars.common.util.ObjectUtil;
import com.sunyard.ars.common.util.UUidUtil;
import com.sunyard.ars.system.bean.et.BusiForm;
import com.sunyard.ars.system.bean.et.EtProcessTb;
import com.sunyard.ars.system.bean.et.EtRiskSourceDef;
import com.sunyard.ars.system.bean.et.UtilNodeActionBean;
import com.sunyard.ars.system.bean.et.UtilNodeBean;
import com.sunyard.ars.system.dao.busm.UserOrganMapper;
import com.sunyard.ars.system.dao.et.BusiFormMapper;
import com.sunyard.ars.system.dao.et.EtProcessTbMapper;
import com.sunyard.ars.system.dao.et.EtRiskSourceDefMapper;
import com.sunyard.ars.system.dao.et.SlipDefMapper;
import com.sunyard.ars.system.dao.mc.ModelMapper;
import com.sunyard.ars.system.init.SystemConstants;
import com.sunyard.ars.system.pojo.mc.Model;
import com.sunyard.ars.system.service.et.INodeHandleService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.pojo.User;


@Service("nodeHandleService")
@Transactional
public class NodeHandleServiceImpl extends BaseService implements INodeHandleService {
	
	@Resource
	private BusiFormMapper busiFormMapper; 
	
	@Resource
	private EtProcessTbMapper etProcessTbMapper; 
	
	@Resource
	private SlipDefMapper slipDefMapper;
	
	@Resource
	private UserOrganMapper userOrganMapper;
	
	@Resource
	private EtRiskSourceDefMapper etRiskSourceDefMapper;
	
	@Resource
    private ModelMapper modelMapper;

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
		if ("doProcessDeal".equalsIgnoreCase(oper_type)) {//流程提交方法s
			doProcessDeal(requestBean, responseBean);
		}else if("dispatchNetNo".equalsIgnoreCase(oper_type)){//转发机构
			dispatchNetNo(requestBean, responseBean);
		}else if("doModifyHandle".equalsIgnoreCase(oper_type)){//办结单据修改提交
			doModifyHandle(requestBean, responseBean);
		}else if("doModifyCheck".equalsIgnoreCase(oper_type)){//监测主管审核办结修改
			doModifyCheck(requestBean, responseBean);
		}
	}

	/**
	 * 流程处理方法
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings("rawtypes")
	private void doProcessDeal(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		User loginUser = BaseUtil.getLoginUser();
		Map sysMap = requestBean.getSysMap();
		Map retMap = new HashMap();
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
		String twoTimesXFdate = (String) sysMap.get("twoTimesXiaFa");
		if(!BaseUtil.isBlank(twoTimesXFdate)){
			busiForm.setBackDate(twoTimesXFdate);
		}
		String realFile = busiForm.getRealName();//获取文件
		if(!BaseUtil.isBlank(realFile)){
			String fileExt = realFile.substring(realFile.lastIndexOf("."));
			busiForm.setRealName(realFile.substring(0,realFile.lastIndexOf(".")));
			busiForm.setFileExt(fileExt);
			busiForm.setFileName(busiForm.getFileName().substring(0,busiForm.getFileName().lastIndexOf(".")));
			busiForm.setFilePath(SystemConstants.ETUPLOAD_Folder+File.separator+DateUtil.getNow());
		}
		// 得到process对象，存入到数据库中，要同时更新通知书表单和过程处理表
		EtProcessTb processTb = new EtProcessTb();
		new ObjectUtil().copyProperties(busiForm,processTb);
		processTb.setDealDescription(busiForm.getFormDescription());
		String dealResultText = (String) sysMap.get("dealResultText");
		processTb.setDealResultText(dealResultText);
		dealThisProcess(requestBean, responseBean, processTb,busiForm);
		//此处判断作业机构核查员和监测员反馈是否一致
		boolean flag = check_node(busiForm.getFormType(),busiForm.getNodeNo(),processTb.getDealResultText());
		if(flag){
			logger.info("作业机构与监测员提交结果不一致，提交主管处理........");
			processTb.setDealDescription("结果不一致，系统自动提交主管处理");
			processTb.setDealOrganNo("0000");
			if("2".equals(busiForm.getFormType())){
				processTb.setDealResultText("45-待主管审核争议提示单-all--");
			}else if("3".equals(busiForm.getFormType())){
				processTb.setDealResultText("45-待主管审核争议预警单-all--");
			}
			processTb.setDealUserName("系统-"+loginUser.getUserName());
			processTb.setDealUserNo(loginUser.getUserNo());
			dealThisProcess(requestBean, responseBean, processTb, busiForm);
		}

		/*if (request.getParameter(MyConstants.MODFIYFIELDS) != null
				&& request.getParameter(MyConstants.MODFIYFIELDS).length() >= 0) {
			// 要修改的字段名和值
			String values = request.getParameter(MyConstants.MODFIYFIELDS
					+ "values");
			if (values != null && values.length() >= 1) {
				values = values.substring(0, values.length() - 1);
				values = values.replaceAll(";", "=").replaceAll(",", " and ");
				logger.info("要修改的字段值信息为：" + values);
				etService.up_test_et(values, formId, 4);
			}
		}*/
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	private  boolean check_node(String formType,String nodeNo,String dealResult){
		String toNodeNo="";
        if(dealResult!=null&&!"".equals(dealResult)){
        	toNodeNo = dealResult.split("-")[0];
        }
        if("2".equals(formType)){
        	if("25".equals(nodeNo)&&"94".equals(toNodeNo)){
        		return true;
        	}
        	if("29".equals(nodeNo)&&("93".equals(toNodeNo))){
        		return true;
        	}
        }
        if("3".equals(formType)){
        	if("30".equals(nodeNo)&&("93".equals(toNodeNo))){
        		return true;
        	}
        	if("35".equals(nodeNo)&&("94".equals(toNodeNo))){
        		return true;
        	}
        }
		return false;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void dealThisProcess(RequestBean requestBean, ResponseBean responseBean, EtProcessTb processTb,
			BusiForm busiForm) throws Exception{
		// TODO Auto-generated method stub
		String backDate = "";
		BusiForm oldBusiform = busiFormMapper.selectByPrimaryKey(busiForm.getFormId());
		/*if((oldBusiform.getEntryId() != null && !"".equals(oldBusiform.getEntryId()))){
			Model modelInfo = modelMapper.getModelInfo(Integer.valueOf(oldBusiform.getEntryId()));
			backDate = busiFormMapper.selectWorkDate(modelInfo.getFeedbackDays() == null?1:modelInfo.getFeedbackDays());
		}else{
			 backDate = busiFormMapper.selectWorkDate(1);
		}*/
		backDate = busiFormMapper.selectWorkDate(3,ARSConstants.DB_TYPE);
		backDate = BaseUtil.filterSqlParam(backDate);
		
		Hashtable utilNodes = SystemConstants.HT_UTIL_NODES;
		String formType = processTb.getFormType();
		String utilNodeNo = processTb.getNodeNo();
		UtilNodeBean utilNodeBean = (UtilNodeBean) utilNodes.get(formType + "|" + utilNodeNo);
		if (utilNodeBean == null) {
			responseBean.setRetMsg("utilNodes.get(" + formType + "|" + utilNodeNo + ")==null;配置文件请检查是否正确配置该单元节点！");
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			return;
		}
		if (utilNodeBean != null && utilNodeBean.getNodeActions() != null) { // 把标签存入数据库processTb
			processTb.setLabelProcess(utilNodeBean.getLabelProcess());
		}
		String isStoreHistory = utilNodeBean.getIsStoreHistory(); // 本节点是否要保留原处理意见 is_store_history varchar(10)
		if(processTb !=null && "0000".equals(processTb.getDealOrganNo())){
			processTb.setCreateTime(DateUtil.getWantDay(1));
		}else{
			processTb.setCreateTime(DateUtil.getToday("yyyyMMdd HH:mm:ss"));
		}
		String dealResult = processTb.getDealResultText();
		if (dealResult == null || dealResult.indexOf("-") < 0 || dealResult.split("-").length < 2) {
			responseBean.setRetMsg("utilNodes配置文件的action方法的显示级别配置文件有误，请检查是否正确配置该单元节点！");
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			return;
		}
		String toNodeNo = dealResult.split("-")[0];
		String dealResultText = dealResult.split("-")[1];
		//String processShowLevel = dealResult.split("-")[2]; // 处理信息查看级别
	
		processTb.setProcessShowLevel("all");
		processTb.setDealResultText(toNodeNo + "-" + dealResultText);
		processTb.setDealUserName(BaseUtil.getLoginUser().getUserName());
		processTb.setDealUserNo(BaseUtil.getLoginUser().getUserNo());
		processTb.setDealDate(DateUtil.getNow());
		processTb.setDealOrganNo(BaseUtil.getLoginUser().getOrganNo());
		
		processTb.setRiskSourceType(busiForm.getRiskSourceType());
		processTb.setRiskSourceName(busiForm.getRiskSourceName());
		processTb.setRiskSourceRemark(busiForm.getRiskSourceRemak());
		if(!BaseUtil.isBlank(busiForm.getRiskSourceName()) && BaseUtil.isBlank(busiForm.getRiskSourceNo())){		
			HashMap condMap = new HashMap();
			condMap.put("sourceName", BaseUtil.filterSqlParam(busiForm.getRiskSourceName()));
			List<EtRiskSourceDef> rsdList = etRiskSourceDefMapper.selectBySelective(condMap);
			if(rsdList != null && rsdList.size() > 0){
				busiForm.setRiskSourceNo(BaseUtil.filterSqlParam(rsdList.get(0).getSourceNo()));
				processTb.setRiskSourceNo(busiForm.getRiskSourceNo());
			}
		}
		if(BaseUtil.isBlank(busiForm.getRiskSourceName()) && BaseUtil.isBlank(busiForm.getRiskSourceType())){
			busiForm.setRiskSourceNo("");
			processTb.setRiskSourceNo("");
		}
		processTb.setBackDate(backDate);//节点加入反馈日期
		// 根据配置文件，决定是否保存原有的流程信息
		if ("yes".equalsIgnoreCase(isStoreHistory)) {
			etProcessTbMapper.insertSelective(processTb);
		} else {
			etProcessTbMapper.updateByPrimaryKeySelective(processTb);
		}

		// 把Busiform的状态节点改为toNodeNo 更新riskType字段 修改处理单处理人
		BusiForm newBusiForm = new BusiForm();
		newBusiForm.setFormId(busiForm.getFormId());
		newBusiForm.setNodeNo(toNodeNo);
		newBusiForm.setDealNo(BaseUtil.getLoginUser().getUserNo());
		newBusiForm.setRiskType(busiForm.getRiskType());
		newBusiForm.setRiskSourceType(busiForm.getRiskSourceType());
		newBusiForm.setRiskSourceName(busiForm.getRiskSourceName());
		newBusiForm.setRiskSourceRemak(busiForm.getRiskSourceRemak());
		newBusiForm.setRiskSourceNo(busiForm.getRiskSourceNo());
		newBusiForm.setFileExt(busiForm.getFileExt());//此处为单据添加附件信息
		newBusiForm.setFileName(busiForm.getFileName());
		newBusiForm.setFilePath(busiForm.getFilePath());
		newBusiForm.setRealName(busiForm.getRealName());
		newBusiForm.setParBackDate(backDate);//分行逾期日期
		int k = busiFormMapper.updateByPrimaryKeySelective(newBusiForm);
		// 判断是否配置了特殊方法，若有配置则执行该方法
		if (utilNodeBean != null && utilNodeBean.getNodeActions() != null) {
			List actionsList = utilNodeBean.getNodeActions();
			for (int i = 0; i < actionsList.size(); i++) {
				UtilNodeActionBean utilNodeActionBean = (UtilNodeActionBean) actionsList.get(i);
				String actionMethod = utilNodeActionBean.getActionMethod();
				if (actionMethod != null && actionMethod.indexOf("TOSLIP") != -1 && toNodeNo.equalsIgnoreCase(utilNodeActionBean.getToNodeNo())) {
					doToSlip(busiForm,oldBusiform); // 转差错,与节点选择有关
				}
				if (actionMethod != null && actionMethod.indexOf("DO_DEGRADE") != -1 && toNodeNo.equalsIgnoreCase(utilNodeActionBean.getToNodeNo())) {
					doDegradeSlip(busiForm); // 降级,与节点选择有关
				}
				if (actionMethod != null && actionMethod.indexOf("DO_CANCELSLIP") != -1 && toNodeNo.equalsIgnoreCase(utilNodeActionBean.getToNodeNo())) {
					doCancelSlip(busiForm); // 确认撤单，登记内部差错生效,与节点选择有关
				}
				if (actionMethod != null && actionMethod.indexOf("MARK_HAS_VIEW") != -1 && toNodeNo.equalsIgnoreCase(utilNodeActionBean.getToNodeNo())) {
					doMarkHasView(busiForm); // 标记已查看
				}
				if (actionMethod != null && actionMethod.indexOf("TWOTIMESISSUED") != -1 && toNodeNo.equalsIgnoreCase(utilNodeActionBean.getToNodeNo())) {
					doUpdateTwoTimesBackDate(busiForm); // 更应反馈日期,与节点选择有关
				}
				if (actionMethod != null && actionMethod.indexOf("THHS") != -1 && toNodeNo.equalsIgnoreCase(utilNodeActionBean.getToNodeNo())) {
					doUpdateTwoTimesBackDate(busiForm); // 更应反馈日期,与节点选择有关
				}
				if (actionMethod != null && actionMethod.indexOf("CHANGE_NETNO") != -1 && toNodeNo.equalsIgnoreCase(utilNodeActionBean.getToNodeNo())) {
					String changeOrganNo = (String) requestBean.getSysMap().get("changeOrganNo");//转发机构
					doChangeOrganNo(busiForm,changeOrganNo); // 修改单据机构
				}
			}
		}
	}
	
	/**
	 * 修改业务机构
	 * @param busiForm
	 */
	private void doChangeOrganNo(BusiForm busiForm,String changeOrganNo) {
		// TODO Auto-generated method stub
		if(!BaseUtil.isBlank(changeOrganNo)){//选择了修改机构
			BusiForm newBusiformTb = new BusiForm();
			newBusiformTb.setFormId(busiForm.getFormId());
			newBusiformTb.setNetNo(changeOrganNo);
			int k = busiFormMapper.updateByPrimaryKeySelective(newBusiformTb);
		}
	}

	/** ---------------------转差错、降级、撤单、转发互动平台、登记到内部差错 处理-----------------------------* */
	/**
	 * 转差错，预警转差错或核实转差错
	 */
	private void doToSlip(BusiForm busiForm,BusiForm oldBusiform) throws Exception{
		// 保留原有的通知单，并要做一个标记，是否转差错。再生成一个新的差错单，走差错流程。
		// 复制一条数据，把类型修改为差错单，状态为，新增。 来源为XX转差错。
		// 对要转的通知单，做一个转差错标记。来源不变，还是显示原核实单或预警单的来源，通过关联编号就可以看出是转差错，还是登记的差错
		BusiForm busiformTb = busiFormMapper.selectByPrimaryKey(busiForm.getFormId());
		
		
		/*转差错的时候要修改的字段信息*/
		busiformTb.setCheckerNo(BaseUtil.getLoginUser().getUserNo());
		busiformTb.setCheckerName(BaseUtil.getLoginUser().getUserName());
		busiformTb.setCheckerNo2(busiformTb.getCheckerNo());
		
		if (SystemConstants.FORM_TYPE_2.equals(busiformTb.getFormType())) {
			busiformTb.setNodeNo("21"); // 状态为，直接下发到网点
			if(SystemConstants.SOURCE_FLAG_1.equals(oldBusiform.getSourceFlag())
					|| SystemConstants.SOURCE_FLAG_5.equals(oldBusiform.getSourceFlag())){
				busiformTb.setSourceFlag(SystemConstants.SOURCE_FLAG_6); // 在下发时，要通过sourceFlag值来判断是否要回写差错编号到核实单中。
			}else{
				busiformTb.setSourceFlag(SystemConstants.SOURCE_FLAG_4); // 在下发时，要通过sourceFlag值来判断是否要回写差错编号到核实单中。
			}	
		} else if (SystemConstants.FORM_TYPE_3.equals(busiformTb.getFormType())){
			//修改预警转差错后直接下发到网点
			busiformTb.setNodeNo("21");
			if(SystemConstants.SOURCE_FLAG_1.equals(oldBusiform.getSourceFlag())){
				busiformTb.setSourceFlag(SystemConstants.SOURCE_FLAG_5); // 在下发时，要通过sourceFlag值来判断是否要回写差错编号到核实单中。
			}else{
				busiformTb.setSourceFlag(SystemConstants.SOURCE_FLAG_3); // 在下发时，要通过sourceFlag值来判断是否要回写差错编号到核实单中。
			}
		}
		
		busiformTb.setFormId(getFormId(SystemConstants.FORM_TYPE_0));
		busiformTb.setSlipNo(busiForm.getSlipNo());
		busiformTb.setSlipType(busiForm.getSlipType());
		busiformTb.setSlipName(busiForm.getSlipName());
		busiformTb.setSlipLevel(busiForm.getSlipLevel());
		busiformTb.setQdSlipMoid(busiForm.getQdSlipMoid());
		busiformTb.setAmerceMoney(Double.parseDouble(busiForm.getAmerceMoneyStr()==null?"0.00":busiForm.getAmerceMoneyStr()));
		busiformTb.setAmerceScore(busiForm.getAmerceScore());

		busiformTb.setWorkDate(DateUtil.getToday("yyyyMMdd"));
		busiformTb.setWorkTime(DateUtil.getToday("HH:mm:ss"));
		//转差错后是直接下发 因此更新下发时间
		busiformTb.setXfDate(DateUtil.getToday("yyyyMMdd"));
		busiformTb.setXfTime(DateUtil.getToday("HH:mm:ss"));
		if(!BaseUtil.isBlank(busiForm.getBackDate())){
			busiformTb.setBackDate(busiForm.getBackDate());
		}else{
			busiformTb.setBackDate(busiFormMapper.selectWorkDate(1,ARSConstants.DB_TYPE));
		}
		busiformTb.setOverdueFlag1("0");
		busiformTb.setOverdueFlag2("0");
		busiformTb.setOverdueFlag3("0");
		busiformTb.setFormType(SystemConstants.FORM_TYPE_0); // 转差错
		busiformTb.setCorrelativeId(busiForm.getFormId());
		//20170317 转差错时设置退回等信息设置为空
		busiformTb.setBakeUserNo(null);
		busiformTb.setCorrBackDate(null);
		busiformTb.setCorrDealDate(null);
		busiformTb.setCorrUserNo(null);
		busiformTb.setReturnDate(null);
		busiformTb.setReturnState(null);
		busiformTb.setBrasrulecontextJm(null);
		busiformTb.setIsBf(null);
		busiformTb.setIsSee(null);
		busiformTb.setSeeDesc(null);
		/*busiformTb.setRiskSourceType(null);//转差错驱动因素不用存 
		busiformTb.setRiskSourceName(null);
		busiformTb.setRiskSourceNo(null);
		busiformTb.setRiskSourceRemak(null);*/
		busiFormMapper.insertSelective(busiformTb);
	}
	
	/**
	 * 差错降级
	 */
	private void doDegradeSlip(BusiForm busiForm) {
		// 得到当前的差错等级，给出从1到当前等级减一的级别选择。
		BusiForm busiformTb = new BusiForm();
		busiformTb.setFormId(busiForm.getFormId());
		busiformTb.setSlipNo(busiForm.getSlipNo());
		busiformTb.setSlipType(busiForm.getSlipType());
		busiformTb.setSlipName(busiForm.getSlipName());
		busiformTb.setSlipLevel(busiForm.getSlipLevel());
		busiformTb.setSlipNoBak(busiForm.getSlipNoBak());
		busiformTb.setAmerceMoney(Double.parseDouble(busiForm.getAmerceMoneyStr()==null?"0.00":busiForm.getAmerceMoneyStr()));
		busiformTb.setAmerceScore(busiForm.getAmerceScore());
		busiformTb.setQdSlipMoid(busiForm.getQdSlipMoid());
		int n = busiFormMapper.updateByPrimaryKeySelective(busiformTb);
	}

	/**
	 * 差错撤单（深发展撤单不用确认）
	 */
	private void doCancelSlip(BusiForm busiForm) {
		// 取得当前的差错单ID值，修改activeFlag 的值。 置为撤单状态，与启用，停用并列。 ACTIVE_FLAG_CANCEL
		BusiForm busiformTb = new BusiForm();
		busiformTb.setFormId(busiForm.getFormId());
		busiformTb.setActiveFlag(SystemConstants.ACTIVE_FLAG_CANCEL);
		busiFormMapper.updateByPrimaryKeySelective(busiformTb);
	}

	
	/**
	 * 在网点整改时，点击查看，修改orverdue_flag_1的值为9。表示已查看。
	 */
	private int doMarkHasView(BusiForm busiForm) {
		BusiForm busiformTb = new BusiForm();
		busiformTb.setFormId(busiForm.getFormId());
		busiformTb.setOverdueFlag1(SystemConstants.OVERDUE_FLAG_1); // 把orverdue_flag_1标记为9，表示已查看。逾期统计时用到
		int n = busiFormMapper.updateByPrimaryKeySelective(busiformTb);
		return n;
	}

	/**
	 * 退回核查时，更新要求反馈日期
	 */
	private int doUpdateTwoTimesBackDate(BusiForm busiForm) {
		BusiForm busiformTb = new BusiForm();
		busiformTb.setFormId(busiForm.getFormId());
		busiformTb.setBackDate(busiForm.getBackDate());
		int n = 0;
		if(!BaseUtil.isBlank(busiForm.getBackDate())){
			n = busiFormMapper.updateByPrimaryKeySelective(busiformTb);
		}
		return n;
	}
	
	/**
	 * 获取处理单ID
	 * @param formType 0：差错C 2：核实H 3：预警Y 5:内部差错单N 6: 质检单Z 
	 * @return
	 */
	private String getFormId(String formType) {
		SecureRandom random = new SecureRandom();
		String todayStr = DateUtil.getToday("yyyyMMddsss") + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
		String sqlLike = "";
		String nextId = "";
		if("0".equals(formType)) {
			sqlLike = "C" + todayStr;
		} else if ("2".equals(formType)) {
			sqlLike = "H" + todayStr;
		} else if ("3".equals(formType)) {
			sqlLike = "Y" + todayStr;
		} else if ("5".equals(formType)) {
			sqlLike = "N" + todayStr;
		} else if ("6".equals(formType)) {
			sqlLike = "Z" + todayStr;
		}
		String maxFormId = busiFormMapper.getMaxFormId(sqlLike);
		if (maxFormId != null) {
			maxFormId = maxFormId.trim();
			int bachId = Integer.parseInt(maxFormId.substring(
					maxFormId.length() - 4, maxFormId.length())) + 1;
			nextId = sqlLike
					+ String.valueOf(new java.text.DecimalFormat("0000")
							.format(bachId));
		} else {
			nextId = sqlLike + "0001";
		}
		return nextId;
	}
	
	/**
	 * 转发机构
	 * @param requestBean
	 * @param responseBean
	 */
	private void dispatchNetNo(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		User loginUser = BaseUtil.getLoginUser();
		Map sysMap = requestBean.getSysMap();
		Map retMap = new HashMap();
		BusiForm newBusiForm = (BusiForm) requestBean.getParameterList().get(0);
		//获取原处理单信息
		BusiForm busiForm = busiFormMapper.selectByPrimaryKey(BaseUtil.filterSqlParam(newBusiForm.getFormId()));
		// 得到process对象，存入到数据库中，要同时更新通知书表单和过程处理表
		EtProcessTb processTb = new EtProcessTb();
		processTb.setFormId(busiForm.getFormId());
		processTb.setFormType(busiForm.getFormType());
		processTb.setNodeNo(busiForm.getNodeNo());
		processTb.setDealUserNo(BaseUtil.getLoginUser().getUserNo());
		processTb.setDealUserName(BaseUtil.getLoginUser().getUserName());
		processTb.setDealOrganNo(BaseUtil.getLoginUser().getOrganNo());
		processTb.setLabelProcess("处理单机构转发");
		processTb.setProcessShowLevel("all");
		processTb.setDealDate(DateUtil.getNow());
		processTb.setCreateTime(DateUtil.getToday("yyyyMMdd HH:mm:ss"));
		processTb.setDealResultText("处理单机构转发");
		processTb.setDealDescription("处理单从原机构："+busiForm.getNetNo()+" 转发给新机构："+newBusiForm.getNetNo());
		
		String backDate = "";
		if((busiForm.getEntryId() != null && !"".equals(busiForm.getEntryId()))){
			 //获取模型信息
	        Model modelInfo = modelMapper.getModelInfo(Integer.valueOf(BaseUtil.filterSqlParam(busiForm.getEntryId())));
	        Integer feedBackDays = modelInfo.getFeedbackDays() == null?1:modelInfo.getFeedbackDays();
			backDate = busiFormMapper.selectWorkDate(Integer.valueOf(BaseUtil.filterSqlParam((feedBackDays+""))),ARSConstants.DB_TYPE);
		}else{
			 backDate = busiFormMapper.selectWorkDate(1,ARSConstants.DB_TYPE);
		}
		if(!"".equals(backDate) && backDate != null){
			backDate = BaseUtil.filterSqlParam(backDate);
			processTb.setBackDate(backDate);
		}
		int n = etProcessTbMapper.insertSelective(processTb);
		int k = busiFormMapper.updateByPrimaryKeySelective(newBusiForm);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("转发成功");
	}
	
	/**
	 * 办结单据修改
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void doModifyHandle(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		Map retMap = new HashMap();
		String modifyHandle_type = (String) sysMap.get("modifyHandle_type");//1：监测主管修改，2：监测员修改
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
		BusiForm busiformTb = busiFormMapper.selectByPrimaryKey(busiForm.getFormId());
		Hashtable utilNodes = SystemConstants.HT_UTIL_NODES;
		String formType = busiformTb.getFormType();
		String utilNodeNo = busiformTb.getNodeNo();
		UtilNodeBean utilNodeBean = (UtilNodeBean) utilNodes.get(formType + "|" + utilNodeNo);
		if (utilNodeBean == null) {
			responseBean.setRetMsg("utilNodes.get(" + formType + "|" + utilNodeNo + ")==null;配置文件请检查是否正确配置该单元节点！");
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			return;
		}
		// 得到process对象，存入到数据库中，要同时更新通知书表单和过程处理表
		EtProcessTb processTb = new EtProcessTb();
		new ObjectUtil().copyProperties(busiForm,processTb);
		processTb.setNodeNo(BaseUtil.filterSqlParam(busiformTb.getNodeNo()));
		processTb.setDealDescription(busiForm.getFormDescription());
		String dealResultText = (String) sysMap.get("dealResultText");
		processTb.setDealResultText(dealResultText);
		if("1".equals(modifyHandle_type)){//监测主管直接修改
			processTb.setLabelProcess("监测主管办结单据修改");
		}else{//监测员修改
			processTb.setLabelProcess("监测员办结单据修改");
		}
		String isStoreHistory = utilNodeBean.getIsStoreHistory(); // 本节点是否要保留原处理意见 is_store_history varchar(10)
		if(processTb !=null && "0000".equals(processTb.getDealOrganNo())){
			processTb.setCreateTime(DateUtil.getWantDay(1));
		}else{
			processTb.setCreateTime(DateUtil.getToday("yyyyMMdd HH:mm:ss"));
		}
		processTb.setProcessShowLevel("all");
		processTb.setDealUserName(BaseUtil.getLoginUser().getUserName());
		processTb.setDealUserNo(BaseUtil.getLoginUser().getUserNo());
		processTb.setDealDate(DateUtil.getNow());
		processTb.setDealOrganNo(BaseUtil.getLoginUser().getOrganNo());
		processTb.setRiskSourceType(busiForm.getRiskSourceType());
		processTb.setRiskSourceName(busiForm.getRiskSourceName());
		processTb.setRiskSourceRemark(busiForm.getRiskSourceRemak());
		if(!BaseUtil.isBlank(busiForm.getRiskSourceName()) && BaseUtil.isBlank(busiForm.getRiskSourceNo())){		
			HashMap condMap = new HashMap();
			condMap.put("sourceName", BaseUtil.filterSqlParam(busiForm.getRiskSourceName()));
			List<EtRiskSourceDef> rsdList = etRiskSourceDefMapper.selectBySelective(condMap);
			if(rsdList != null && rsdList.size() > 0){
				busiForm.setRiskSourceNo(BaseUtil.filterSqlParam(rsdList.get(0).getSourceNo()));
				processTb.setRiskSourceNo(busiForm.getRiskSourceNo());
			}
		}
		// 根据配置文件，决定是否保存原有的流程信息
		if ("yes".equalsIgnoreCase(isStoreHistory)) {
			etProcessTbMapper.insertSelective(processTb);
		} else {
			etProcessTbMapper.updateByPrimaryKeySelective(processTb);
		}

		if("1".equals(modifyHandle_type)){//监测主管直接修改
			// 把Busiform的MODIFY_NODE改为toNodeNo 更新MODIFY_TYPE字段 修改处理单状态
			BusiForm newBusiForm = new BusiForm();
			newBusiForm.setFormId(busiForm.getFormId());
			newBusiForm.setNodeNo(busiForm.getNodeNo());
			newBusiForm.setDealNo(BaseUtil.getLoginUser().getUserNo());
			newBusiForm.setRiskType(busiForm.getRiskType());
			newBusiForm.setRiskSourceType(busiForm.getRiskSourceType());
			newBusiForm.setRiskSourceName(busiForm.getRiskSourceName());
			newBusiForm.setRiskSourceRemak(busiForm.getRiskSourceRemak());
			newBusiForm.setRiskSourceNo(busiForm.getRiskSourceNo());
			busiFormMapper.updateByPrimaryKeySelective(newBusiForm);
		}else if("2".equals(modifyHandle_type)){//监测员修改需监测主管审核
			// 把Busiform的MODIFY_NODE改为toNodeNo 更新MODIFY_TYPE字段 修改处理单状态
			BusiForm newBusiForm = new BusiForm();
			newBusiForm.setFormId(busiForm.getFormId());
			newBusiForm.setModifyNode(busiForm.getNodeNo());
			newBusiForm.setModifyType("1");
			busiFormMapper.updateByPrimaryKeySelective(newBusiForm);
		}
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 监测主管审核办结修改
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void doModifyCheck(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		Map retMap = new HashMap();
		String checkResult = (String) sysMap.get("checkResult");//00：审核通过 01：审核不通过
		BusiForm busiForm = (BusiForm) requestBean.getParameterList().get(0);
		BusiForm busiformTb = busiFormMapper.selectByPrimaryKey(BaseUtil.filterSqlParam(busiForm.getFormId()));
		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("formId", BaseUtil.filterSqlParam(busiformTb.getFormId()));
		condMap.put("nodeNo", BaseUtil.filterSqlParam(busiformTb.getNodeNo()));
		List<EtProcessTb> processList = etProcessTbMapper.selectByPrimaryKey(condMap);//查询修改节点的节点信息
		// 得到process对象，存入到数据库中，要同时更新通知书表单和过程处理表
		EtProcessTb processTb = processList.get(0);
		processTb.setDealDescription(busiForm.getFormDescription());
		if("00".equals(checkResult)){//审核通过
			processTb.setLabelProcess("办结单据修改审核通过");
		}else{//审核不通过
			processTb.setLabelProcess("办结单据修改审核不通过");
		}
		processTb.setCreateTime(DateUtil.getToday("yyyyMMdd HH:mm:ss"));
		processTb.setDealUserName(BaseUtil.getLoginUser().getUserName());
		processTb.setDealUserNo(BaseUtil.getLoginUser().getUserNo());
		processTb.setDealDate(DateUtil.getNow());
		processTb.setDealOrganNo(BaseUtil.getLoginUser().getOrganNo());
		etProcessTbMapper.insertSelective(processTb);

		if("00".equals(checkResult)){//审核通过
			BusiForm newBusiForm = new BusiForm();
			newBusiForm.setFormId(busiformTb.getFormId());
			newBusiForm.setNodeNo(busiformTb.getModifyNode());
			newBusiForm.setDealNo(processTb.getDealUserNo());
			newBusiForm.setRiskSourceType(processTb.getRiskSourceType());
			newBusiForm.setRiskSourceName(processTb.getRiskSourceName());
			newBusiForm.setRiskSourceRemak(processTb.getRiskSourceRemark());
			newBusiForm.setRiskSourceNo(processTb.getRiskSourceNo());
			newBusiForm.setModifyNode("");
			newBusiForm.setModifyType("0");
			busiFormMapper.updateByPrimaryKeySelective(newBusiForm);
		}else{//审核不通过
			BusiForm newBusiForm = new BusiForm();
			newBusiForm.setFormId(busiformTb.getFormId());
			newBusiForm.setModifyType("2");
			busiFormMapper.updateByPrimaryKeySelective(newBusiForm);
		}
		
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
}
