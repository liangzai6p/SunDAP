package com.sunyard.ars.common.comm;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sunyard.ars.common.dao.TmpBatchMapper;
import com.sunyard.ars.common.util.SpringContextUtils;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.sunflow.engine.dataclass.WMTAttribute;
import com.sunyard.sunflow.engine.process.ProcessContext;

/*分配参与者
 * */
public class GetUser {
	// 日志记录器
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private Vector<String> UsersVec = new Vector<String>();

	/**
	 * 参与者分配，按一般规则获取具有权限处理该任务的相关柜员.
	 * 
	 * @param batchId
	 *            批次号
	 * @param siteNo
	 *            机构号
	 * @param node
	 *            流程节点
	 * @return Vector 满足条件的用户集合
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final Vector<String> getUserNo(WMTAttribute[] attrs ,ProcessContext context) {
		try{
			if (!UsersVec.isEmpty()){
				UsersVec.clear();
			}
			long inTime = System.currentTimeMillis();
			String flowFlag = "";
			String batchId = "";
			//变量flowFlag 
			for(int i = 0; i < attrs.length; i ++){
			     if ("flowFlag".equalsIgnoreCase(attrs[i].getAttributeName())){
			    	 flowFlag = attrs[i].getStringValue(); 
			     }else if("batchId".equalsIgnoreCase(attrs[i].getAttributeName())){
			    	 batchId = attrs[i].getStringValue(); 
			     }
			}
//			String node = "业务审核";		
			logger.info("批次号：" + batchId);
			HashMap condMap = new HashMap();
			condMap.put("batchId", batchId);
			//condMap.put("hasPrivOrganFlag", "1");//如果没有权限机构则查询所属机构
//			condMap.put("node", node);
			TmpBatchMapper tmpBatchMapper = (TmpBatchMapper)SpringContextUtils.getContext().getBean("tmpBatchMapper");
			List<HashMap<String,Object>> userList = tmpBatchMapper.getFlowUserList(condMap);//权限机构
			List<HashMap<String,Object>> userList2 = tmpBatchMapper.getFlowUserList2(condMap);//所属机构,排除已在权限机构中的用户
			if(null != userList && userList.size() > 0){
				for (HashMap<String, Object> userMap : userList) {
					UsersVec.add(userMap.get("USER_NO")+"");
				}
			}
			if(null != userList2 && userList2.size() > 0){
				for (HashMap<String, Object> userMap2 : userList2) {
					UsersVec.add(userMap2.get("USER_NO")+"");
				}
			}
			UsersVec.add("sa");
			long outTime = System.currentTimeMillis();
			logger.info("查询用户时间：" + (outTime - inTime) + "ms");
		}catch (SQLException e) {
			logger.error("查询工作流用户时发生异常！", e);
		} catch (Exception e) {
			logger.error("查询工作流用户时发生异常！", e);
		}
		return UsersVec;
	}
}
