package com.sunyard.ars.system.init;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.util.ObjectUtil;
import com.sunyard.ars.system.bean.et.BusiNodeBean;
import com.sunyard.ars.system.bean.et.UtilNodeActionBean;
import com.sunyard.ars.system.bean.et.UtilNodeBean;

public class ReadConfigXML{
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public TreeMap<String, BusiNodeBean> readBusiNode() {
		String text; // 节点描述，显示在左边菜单中
		String index; // 节点索引
		String isCenterUser; // 用户是否为中心用户
		String purview; // 权限字符
		String purviewType; // 权限字符的类型（用固定的常量‘AMEND’）
		String isShowSecondSearch; // 是否显示再查询页面
		List<String> isSSSFormTypeList; // 再查询中，处理单类型的配置。因为每个节点，所包含的类型不同。
		String needOrganCondition = "yes"; //显示任务列表时，是否包含网点号这个条件。
		String isCheckerMatch = "no"; // 显示任务列表时，是否谁登记谁查看。 is_checker_match，								// yes-是 no-否
		String isTellerMatch = "no"; // 显示任务列表时，是否柜员查看自己的任务。 is_teller_match，							// yes-是 no-否
		String isLeaderMatch = "no"; // 显示任务列表时，是否领导查看自己的任务列表。is_leader_match，								// yes-是 no-否
		String isCheckerNotUserNoMatch = "no"; // 显示任务列表时，查看不是自己登记的任务。
		String isLeaderNotSeanMatch = "no"; // 显示任务列表时，是否领导查看自己的任务列表
											// 且没有查看过。is_leader_not_sean_match，
											// yes-是 no-否
		String isLeaderHasSeanMatch = "no"; // 显示任务列表时，是否领导查看自己的任务列表
											// 且已经查看过。is_leader_has_sean_match，
											// yes-是 no-否
		//添加处理单由网点整改,预警员,中心主管的指定人处理 modify by miaoky
		String isAppointNetUser="no";//显示任务列表时，对直接下发到网点的处理单是否指定网点处理人。  yes-是  no-否
		String isAppointNetArms="no";//显示任务列表时，处理单是否指定监察员处理人。  yes-是  no-否
		String isAppointNetManager="no";//显示任务列表时，处理单是否指定中心主管处理人。yes-是  no-否
		String corrFlag="no";
		BusiNodeBean busiNode;
		Document doc = null;
		String filePath = (ARSConstants.CONFIGPATH + File.separator + "busiNodeDef.xml")
				.replaceAll("%20", " ");
		SAXReader oSAXReader = new SAXReader();
		TreeMap<String, BusiNodeBean> nodeTable = new TreeMap<String, BusiNodeBean>(); // 建立构造器
		try {
			filePath = URLDecoder.decode(filePath, "UTF-8");
			doc = oSAXReader.read(new File(filePath));

		} catch (Exception e) {
			logger.error("读取" + filePath + " 文件读取失败!", e);
		} // 读入指定文件

		if(doc!=null){
			Element root = doc.getRootElement();// 获得根节点
			busiNode = new BusiNodeBean();
			text = root.attributeValue("text");

			busiNode.setText(text);

			index = root.attributeValue("index");
			busiNode.setIndex(index);
			try {
				isCenterUser = root.attributeValue("is_center_user");
				busiNode.setIsCenterUser(isCenterUser);
			} catch (Exception e) {
				busiNode.setIsCenterUser("no");
			}
			purview = root.attributeValue("purview");
			busiNode.setPurview(purview);

			purviewType = root.attributeValue("purviewType");
			busiNode.setPurviewType(purviewType);
			nodeTable.put(index, busiNode);

			List<Element> nodesList = root.elements("nodes"); // 读取第二级节点
			for (int i = 0; i < nodesList.size(); i++) {
				Element nodes = (Element) nodesList.get(i);
				busiNode = new BusiNodeBean();
				busiNode.setText(nodes.attributeValue("text"));
				index = nodes.attributeValue("index");
				busiNode.setIndex(index);
				nodeTable.put(index, busiNode);

				List<Element> nodeList = nodes.elements("node"); // 读取第三级节点
				for (int j = 0; j < nodeList.size(); j++) {
					Element node = (Element) nodeList.get(j);
					busiNode = new BusiNodeBean();
					isSSSFormTypeList = new ArrayList<String>();
					busiNode.setText(node.attributeValue("text"));
					index = node.attributeValue("index");
					busiNode.setIndex(index);
					String isTaskList = node.attributeValue("is_task_list");
					isTaskList = isTaskList == null ? "no" : isTaskList;
					busiNode.setIsTaskList(isTaskList);

					purviewType = node.attributeValue("purviewType");
					if(purviewType != null){
						busiNode.setPurviewType(purviewType);
					}else{
						busiNode.setPurviewType("0");
					}

					if (node.element("is_show_second_search") != null) {

						isShowSecondSearch = node.elementText("is_show_second_search");

						String isSSSFormTypeStr = node.element("is_show_second_search").attributeValue("form_type");
						if (isSSSFormTypeStr != null
								&& isSSSFormTypeStr.trim().length() > 0) {
							if (isSSSFormTypeStr.indexOf(",") > 0) {
								String[] isSSSFormTypes = isSSSFormTypeStr
										.split(",");
								for (int iii = 0; iii < isSSSFormTypes.length; iii++) {
									isSSSFormTypeList.add(isSSSFormTypes[iii]);
								}
							} else {
								isSSSFormTypeList.add(isSSSFormTypeStr); // 再查询模块的处理单类型的配置，不同节点的再查询模块处理单类型是不同的
							}
						}
					} else {
						isShowSecondSearch = "no";
					}
					busiNode.setIsShowSecondSearch(isShowSecondSearch);
					busiNode.setIsSSSFormTypeList(isSSSFormTypeList);

					if(node.element("need_organ_condition") != null){
						needOrganCondition = node.elementText("need_organ_condition");
					}else{
						needOrganCondition = "yes";
					}
					busiNode.setNeedOrganCondition(needOrganCondition);
					if (node.element("is_checker_match") != null) {
						isCheckerMatch = node.elementText("is_checker_match");
					} else {
						isCheckerMatch = "no";
					}
					busiNode.setIsCheckerMatch(isCheckerMatch);

					if (node.element("is_teller_match") != null) {
						isTellerMatch = node.elementText("is_teller_match");
					} else {
						isTellerMatch = "no";
					}
					busiNode.setIsTellerMatch(isTellerMatch);

					if (node.element("is_leader_match") != null) {
						isLeaderMatch = node.elementText("is_leader_match");
					} else {
						isLeaderMatch = "no";
					}
					busiNode.setIsLeaderMatch(isLeaderMatch);

					if (node.element("is_checker_not_userno_match") != null) {
						isCheckerNotUserNoMatch = node
								.elementText("is_checker_not_userno_match");
					} else {
						isCheckerNotUserNoMatch = "no";
					}
					busiNode.setIsCheckerNotUserNoMatch(isCheckerNotUserNoMatch);

					if (node.element("is_leader_not_sean_match") != null) {
						isLeaderNotSeanMatch = node
								.elementText("is_leader_not_sean_match");
					} else {
						isLeaderNotSeanMatch = "no";
					}
					busiNode.setIsLeaderNotSeanMatch(isLeaderNotSeanMatch);

					if (node.element("is_leader_has_sean_match") != null) {
						isLeaderHasSeanMatch = node
								.elementText("is_leader_has_sean_match");
					} else {
						isLeaderHasSeanMatch = "no";
					}
					busiNode.setIsLeaderHasSeanMatch(isLeaderHasSeanMatch);
					//添加处理单由网点整改和中心主管的指定人处理 modify by miaoky
					if(node.element("is_appoint_netuser")!=null){
						isAppointNetUser =node.elementText("is_appoint_netuser");
					}else{
						isAppointNetUser="no";
					}
					busiNode.setIsAppointNetUser(isAppointNetUser);

					if(node.element("is_appoint_netarms")!=null){
						isAppointNetArms =node.elementText("is_appoint_netarms");
					}else{
						isAppointNetArms="no";
					}
					busiNode.setIsAppointNetArms(isAppointNetArms);

					if(node.element("is_appoint_netmanager")!=null){
						isAppointNetManager =node.elementText("is_appoint_netmanager");
					}else{
						isAppointNetManager="no";
					}
					busiNode.setIsAppointNetManager(isAppointNetManager);

					if(node.element("corrFlag")!=null){
						corrFlag = node.elementText("corrFlag");
					}else{
						corrFlag = "no";
					}
					busiNode.setCorrFlag(corrFlag);

					List<UtilNodeBean> utilNodes = new ArrayList<UtilNodeBean>();
					if (node.element("util_nodes") != null
							&& node.element("util_nodes").elements() != null) {
						List<Element> utilNodesList = node.element("util_nodes").elements();
						for (int k = 0; k < utilNodesList.size(); k++) {
							Element utilNodeElem = (Element) utilNodesList.get(k);
							UtilNodeBean utilNode = new UtilNodeBean();
							if(utilNodeElem.attributeValue("form_type") != null){
								utilNode.setFormType(utilNodeElem.attributeValue("form_type"));
							}else{
								utilNode.setFormType("all");
							}

							List<UtilNodeActionBean> actionList = new ArrayList<UtilNodeActionBean>();
							if (utilNodeElem.element("action") != null) {
								List<Element> actionElemList = utilNodeElem.elements("action");
								for (int n = 0; n < actionElemList.size(); n++) {
									UtilNodeActionBean nodeActionBean = new UtilNodeActionBean();
									Element actionElem = (Element) actionElemList.get(n);
									String actionText = actionElem.attributeValue("text");
									nodeActionBean.setActionText(actionText);
									String actionMethod = actionElem.attributeValue("method");
									nodeActionBean.setActionMethod(actionMethod);
									String isSeanLeader = actionElem.attributeValue("is_sean_leader");
									if (isSeanLeader != null) {
										nodeActionBean.setIsSeanLeader(isSeanLeader);
									} else {
										nodeActionBean.setIsSeanLeader("no");
									}
									String signFlag = actionElem.attributeValue("signFlag");
									nodeActionBean.setSignFlag(signFlag);
									String onclickMethod = actionElem.attributeValue("onclick");
									nodeActionBean.setOnclickMethod(onclickMethod);
									String actionTarget = actionElem.attributeValue("target");
									nodeActionBean.setTarget(actionTarget);
									actionList.add(nodeActionBean);
								}
							}
							utilNode.setNodeActions(actionList);

							String utilNodeNosStr = utilNodeElem.attributeValue("util_node_no"); // 配置util_node_no的// 12,13,14
							if (utilNodeNosStr != null && utilNodeNosStr.indexOf(",") != -1) {
								String[] utilNodeNos = utilNodeNosStr.split(",");
								for (int y = 0; y < utilNodeNos.length; y++) {
									String utilNodeNo = utilNodeNos[y];
									if (utilNodeNo != null
											&& !"".equals(utilNodeNo)) {
										UtilNodeBean utilNode_ = new UtilNodeBean();
										new ObjectUtil().copyProperties(utilNode, utilNode_);
										utilNode_.setNodeNo(utilNodeNo);
										utilNodes.add(utilNode_);
									}
								}
							} else {
								if(utilNodeNosStr == null){
									utilNode.setNodeNo("all");
								}else{
									utilNode.setNodeNo(utilNodeNosStr);
								}

								utilNodes.add(utilNode);
							}
						}
					}
					busiNode.setUtilNodes(utilNodes);
					nodeTable.put(index, busiNode);
				}
			}
		}
		return nodeTable;
	}

	

}
