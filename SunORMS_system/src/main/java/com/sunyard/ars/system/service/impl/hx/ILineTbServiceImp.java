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
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.system.bean.hx.LineTbBean;
import com.sunyard.ars.system.service.hx.ILineTbService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service
@Transactional
public class ILineTbServiceImp extends BaseService implements ILineTbService {

	@Resource
	private  com.sunyard.ars.system.dao.hx.ILineTbMapper ILineTbMapper;
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);	
	}

	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		LineTbBean Model= (LineTbBean)requestBean.getParameterList().get(0);
		Map sysMap = requestBean.getSysMap();
		String oper_type = (String) sysMap.get("oper_type");
		List<Map<String,Object>> list=null;
		String log="";
		Map<String, Object> map = new HashMap<String, Object>();
		if(AOSConstants.OPERATE_ADD.equals(oper_type)){
			// 新增
			Integer id = add();
			Model.setId(id);
			map.put("Bean", Model);
			ILineTbMapper.save(map);
			log="流程线内容："+Model.getNodeContent()+" 流程号："+Model.getFlowChart()+" 在流程线中被创建";
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
			ILineTbMapper.update(map);
			log="流程线内容："+Model.getNodeContent()+" 流程号："+Model.getFlowChart()+" 在流程线中被修改";
			//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_3, log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("修改成功");
		}else if(AOSConstants.OPERATE_DELETE.equals(oper_type)) {
			// 删除 
			List<?> delList = requestBean.getParameterList();
			for(int i=0; i<delList.size(); i++) {
				Model = (LineTbBean)delList.get(i);
				System.out.println("ID:"+Model.getId());
				map.put("Bean", Model);
				ILineTbMapper.delete(map);
				log="流程线ID为："+Model.getId()+" 的记录在流程线表中被删除";
				//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG, ARSConstants.OPER_TYPE_2, log);
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
			}
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("删除成功");
		}else if(AOSConstants.OPERATE_QUERY.equals(oper_type)) {
			// 查询
			int pageNum = Integer.parseInt(sysMap.get("currentPage").toString());
			int initPageNum = (int) sysMap.get("user_pageNum");//15
			Page page = PageHelper.startPage(pageNum, initPageNum);
			map.put("Bean", Model);
			list = getList(ILineTbMapper.select(map), page);
			long totalCount = page.getTotal();
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			retMap.put("currentPage", pageNum);
			retMap.put("pageNum", initPageNum);
			retMap.put("totalNum", totalCount);
			retMap.put("returnList", list);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}	
	}
	//防止高并发环境下ID发生冲突
			public  synchronized  Integer  add()throws Exception{
				Integer maxId = ILineTbMapper.getMaxId();
				if(maxId==null){
					maxId=0;
				}
				maxId = maxId+1;
				maxId = Integer.valueOf(BaseUtil.filterSqlParam(maxId.toString()));
				return maxId;
			}
}
