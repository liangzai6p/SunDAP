package com.sunyard.ars.system.service.impl.hx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.system.bean.hx.LineTbBean;
import com.sunyard.ars.system.bean.hx.NodeTbBean;
import com.sunyard.ars.system.service.hx.INodeTBService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service
@Transactional
public class INodeTBServiceImp extends BaseService implements INodeTBService {


	@Resource
	private  com.sunyard.ars.system.dao.hx.INodeTBMapper INodeTBMapper;
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);	
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		NodeTbBean Model= (NodeTbBean)requestBean.getParameterList().get(0);
		Map sysMap = requestBean.getSysMap();
		String oper_type = (String) sysMap.get("oper_type");
		List<Map<String,Object>> list=null;
		Map<String, Object> map = new HashMap<String, Object>();
		String log="";
		if(AOSConstants.OPERATE_ADD.equals(oper_type)){
			// 新增
			Map<Object,Object> retMap = new HashMap<Object,Object> ();
			map.put("Bean", Model);
			List<Map<String, Object>> flag = INodeTBMapper.selectIsExist(Model);
			//集合为空！说明添加的节点号和流程号不存在！ 那么可进行添加
			if(flag.isEmpty()){	
				INodeTBMapper.save(map);
				log="节点名称"+Model.getNodeNo()+" 所属流程号"+Model.getFlowChart()+"在流程节点表中被创建！";
				//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_1, log);
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
				responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
				responseBean.setRetMsg("新增成功");
				responseBean.setRetMap(retMap);
			}else{
				responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("新增失败");
				responseBean.setRetMap(retMap);
			}
		}else if(AOSConstants.OPERATE_MODIFY.equals(oper_type)) {
			// 修改
			map.put("Bean", Model);
			INodeTBMapper.update(map);
			
			log="节点名称"+Model.getNodeNo()+" 所属流程号"+Model.getFlowChart()+"在流程节点表中被修改！";
			//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_3, log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("修改成功");
		}else if(AOSConstants.OPERATE_DELETE.equals(oper_type)) {
			// 删除 
			List<?> delList = requestBean.getParameterList();
			for(int i=0; i<delList.size(); i++) {
				Model = (NodeTbBean)delList.get(i);
				map.put("Bean", Model);
				INodeTBMapper.delete(map);
				log="节点名称"+Model.getNodeNo()+" 所属流程号"+Model.getFlowChart()+"在流程节点表中被删除！";
				//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_2, log);
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
			}
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("删除成功");
		}else if(AOSConstants.OPERATE_QUERY.equals(oper_type)) {
			// 查询
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			int pageNum = Integer.parseInt(sysMap.get("currentPage").toString());
			if(-1==pageNum){
				System.out.println("查询全部节点");
				 map.put("Bean", Model);
				 list = INodeTBMapper.select(map);
			}else{
				int initPageNum = (int) sysMap.get("user_pageNum");//15
				Page page = PageHelper.startPage(pageNum, initPageNum);
				map.put("Bean", Model);
				list = getList(INodeTBMapper.select(map), page);
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
}

