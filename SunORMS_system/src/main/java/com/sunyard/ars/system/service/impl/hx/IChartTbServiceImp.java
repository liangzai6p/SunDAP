package com.sunyard.ars.system.service.impl.hx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sunyard.aos.common.util.BaseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.hx.ChartTbBean;
import com.sunyard.ars.system.bean.hx.LineTbBean;
import com.sunyard.ars.system.bean.hx.NodeTbBean;
import com.sunyard.ars.system.service.hx.IChartTbService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service
@Transactional
public class IChartTbServiceImp extends BaseService implements IChartTbService{
	
	@Resource
	private com.sunyard.ars.system.dao.hx.ChartTbMapper ChartTbMapper;
	
	@Resource
	private com.sunyard.ars.system.dao.hx.ILineTbMapper ILineTbMapper; //流程线DAO
	
	@Resource
	private com.sunyard.ars.system.dao.hx.INodeTBMapper INodeTBMapper;//流程节点DAO
	
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);	
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		ChartTbBean Model= (ChartTbBean)requestBean.getParameterList().get(0);
		Map sysMap = requestBean.getSysMap();
		String oper_type = (String) sysMap.get("oper_type");
		List<Map<String,Object>> list=null;
		String log="";
		Map<String, Object> map = new HashMap<String, Object>();
		if(AOSConstants.OPERATE_ADD.equals(oper_type)){
			// 新增
			Integer id = add();
			Model.setChartNo(id);
			map.put("Bean", Model);
			ChartTbMapper.save(map);
			log="流程名称"+Model.getChartName()+"在流程表中被创建！";
			//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_1, log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
			Map<Object,Object> retMap = new HashMap();
			retMap.put("id", id);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("新增成功");
			responseBean.setRetMap(retMap);
		}else if(AOSConstants.OPERATE_MODIFY.equals(oper_type)) {
			// 修改
			map.put("Bean", Model);
			ChartTbMapper.update(map);
			log="流程名称"+Model.getChartName()+"在流程表中被修改！";
			//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_3, log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("修改成功");
		}else if(AOSConstants.OPERATE_DELETE.equals(oper_type)) {
			// 删除   
			List<?> delList = requestBean.getParameterList();
			for(int i=0; i<delList.size(); i++) {
				Model = (ChartTbBean)delList.get(i);
				System.out.println("要删除的数据ID:"+Model.getChartNo());
				//判断当前流程图下是否有流程线
				LineTbBean  line= new LineTbBean();
				line.setFlowChart(Model.getChartNo().toString());
				map.put("Bean", line);
				//判断流程线用到当前要删除的流程图  如果没有，则可以执行删除
				List<Map<String, Object>> LineData = ILineTbMapper.select(map);
				System.out.println("线的大小"+LineData.size());
				if(LineData.isEmpty()){
					NodeTbBean  Node= new NodeTbBean();
					Node.setFlowChart(Model.getChartNo().toString());
					map.put("Bean", Node);
					//判断流程节点是否用到当前要删除的流程图！ 如果没有，则可以执行删除
					List<Map<String, Object>> NodeData = INodeTBMapper.select(map);
					System.out.println("线的大小"+NodeData.size());
					if(NodeData.isEmpty()){
						map.put("Bean", Model);
						ChartTbMapper.delete(map);
						log="流程ID"+Model.getChartNo()+"的记录在流程表中被删除！";
						//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_2, log);
						addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
						responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
						responseBean.setRetMsg("删除成功");
					}else{
						responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
						responseBean.setRetMsg("删除失败！当前流程图下有节点。");
					}
				}else{
					responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
					responseBean.setRetMsg("删除失败！当前流程图下有流程线。");
				}
			}
		}else if(AOSConstants.OPERATE_QUERY.equals(oper_type)) {
			// 查询
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			int pageNum = Integer.parseInt(sysMap.get("currentPage").toString());
			if(pageNum==-1){
				list = ChartTbMapper.select(map);
			}else{
				int initPageNum = (int) sysMap.get("user_pageNum");//15
				Page page = PageHelper.startPage(pageNum, initPageNum);
				list = getList(ChartTbMapper.select(map), page);
				long totalCount = page.getTotal();
				retMap.put("currentPage", pageNum);
				retMap.put("pageNum", initPageNum);
				retMap.put("totalNum", totalCount);
			}
			retMap.put("returnList", list);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}	
	}
	//防止高并发环境下ID发生冲突
		public  synchronized  Integer  add() throws Exception{
			Integer maxId = ChartTbMapper.getMaxId();
			if(maxId==null){
				maxId=0;
			}
			maxId = maxId+1;
			maxId = Integer.valueOf(BaseUtil.filterSqlParam(maxId.toString()));
			return maxId;
		}
}
