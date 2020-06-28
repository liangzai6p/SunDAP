package com.sunyard.ars.common.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


import javax.annotation.Resource;

import com.sunyard.aos.common.util.BaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.ars.common.dao.TmpBatchMapper;
import com.sunyard.ars.flows.service.api.SunFlowService;
import com.sunyard.ars.flows.service.api.WorkItemInfo;
import com.sunyard.ars.flows.service.impl.api.SunFlowServiceImpl;

/**   
*    
* 项目名称：ARS   
* 类名称：FlowTaskInit   
* 类描述：   TODO 程序启动 初始化
* 修改备注：   
* @version    
*    
*/ 
public class FlowTaskInit{
	// 日志记录器
	protected Logger logger = LoggerFactory.getLogger(getClass());
	//时间间隔 60m
	private static final long PERIOD_TIME = 120 * 1000;

	private Timer timer = null;
	
	private List<WorkItemInfo> arsWorkItemInfo = null;
	
	private WorkItemInfo workItemInfo = null;
	
	private SunFlowService sunFlowService = new SunFlowServiceImpl();
	

	@Resource
	private TmpBatchMapper tmpBatchMapper;

	/**
	 * @Title: flowInit
	 * @Description: 初始化系统信息
	 * @author juek.wu
	 */
	public void flowInit(){
		timer = new Timer();
		//安排指定的任务在指定的时间开始进行重复的固定延迟执行。
		ConstantUpdate constantUpdate = new ConstantUpdate();
		timer.schedule(constantUpdate,1000,PERIOD_TIME);
		
	}

	class ConstantUpdate  extends TimerTask{
		@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
		@Override
		public void run() {			
			// TODO Auto-generated method stub
			try{
				// 定义分页操作
				Page page = PageHelper.startPage(1, 100);
				List<HashMap<String,Object>>	batchList = tmpBatchMapper.getScanBatch();
				logger.info("本次轮询查询到"+batchList.size()+"笔任务!");
				if(null != batchList && batchList.size() > 0){
					arsWorkItemInfo = new ArrayList<>();
					for (HashMap<String,Object> batchInfo : batchList) {
						//执行批次加锁
						if(tmpBatchMapper.batchToFlowLock(BaseUtil.filterSqlParam(batchInfo.get("BATCH_ID")+""),ARSConstants.SUNFLOW_LOCKTIME)>0){
							//锁定成功才允许加入工作流
							workItemInfo = new WorkItemInfo();
							Map<String, String> workItemAttris = new HashMap<>();
							workItemAttris.put("batchId", batchInfo.get("BATCH_ID")+"");
							workItemAttris.put("occurDate", batchInfo.get("OCCUR_DATE")+"");
							workItemAttris.put("siteNo", batchInfo.get("SITE_NO")+"");
							workItemAttris.put("operatorNo", batchInfo.get("OPERATOR_NO")+"");
							workItemInfo.setWorkItemAttris(workItemAttris);
							workItemInfo.setAreaCode(batchInfo.get("SERVICE_ID")+"");
							arsWorkItemInfo.add(workItemInfo);
						}
					}
					Map<String,Long> flowIdMap = sunFlowService.createBatchFlow("sa", "ARSBaseFlow", "后督工作流程", arsWorkItemInfo);
					// 构造条件参数
					if(flowIdMap.isEmpty()){
						logger.info("工作流未查询到可用批次!");
						return;
					}					
					HashMap condMap = new HashMap();
					condMap.put("flowIdMap", flowIdMap);
					int result = tmpBatchMapper.updateFlowId(condMap);
					logger.info("本次轮询加载"+flowIdMap.size()+"笔任务进入工作流!");
					Set<Map.Entry<String,Long>> entrySet = flowIdMap.entrySet();
					for (Entry<String, Long> entry : entrySet) {
						logger.info("本次轮询加载:"+entry.getKey()+"|"+entry.getValue());
					}
					
				}else{
					logger.info("本次轮询未加载到需要进入工作流的批次任务!");
				}
			}catch (Exception e) {
				// TODO: handle exception
				logger.error("FlowTaskInit本次轮询出现异常，请检查！",e);
			}
		}
	}
	
}
