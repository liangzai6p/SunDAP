package com.sunyard.ars.system.service.impl.mc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sunyard.aos.common.util.BaseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.pojo.DelRelate;
import com.sunyard.ars.system.dao.mc.McFieldMapper;
import com.sunyard.ars.system.dao.mc.RelateMapper;
import com.sunyard.ars.system.pojo.mc.DataSource;
import com.sunyard.ars.system.pojo.mc.McField;
import com.sunyard.ars.system.service.mc.IMcFieldService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service("mcFieldService")
@Transactional
public class McFieldServiceImpl extends BaseService  implements IMcFieldService{
	
	@Resource
	private McFieldMapper fieldMapper;
	
	@Resource
	private RelateMapper relateMapper;
	
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
		}
	}
	
	/**
	 * 查询字段
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		McField field = (McField) requestBean.getParameterList().get(0);
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
		
		//执行查询
		Page page = PageHelper.startPage(currentPage, pageSize);
		List resultList = fieldMapper.selectBySelective(field);
		long totalCount = page.getTotal();
		Map retMap = new HashMap();
		retMap.put("pageSize", pageSize);
		retMap.put("allRow", totalCount);
		retMap.put("resultList", resultList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
		
	}
	
	/**
	 * 新增字段
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void add(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		McField field = (McField) requestBean.getParameterList().get(0);
		//新增前判断该字段是否已经存在
		McField selField = new McField();
		selField.setName(field.getName());
		selField.setTableType(field.getTableType());
		List<McField> fieldList = fieldMapper.selectBySelective(selField);
		if(fieldList!=null && fieldList.size()>0){
			//数据库中已存在该字段，无法新增
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("字段已存在，无法新增");
			return;
		}
		
		//新增字段
		fieldMapper.insertSelective(field);
		Map retMap = new HashMap();
		retMap.put("fieldId", field.getId());
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("添加成功！");
		
		//添加日志
		String log = "警报规则配置新增"+field.getChName()+"的字段";
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
	}
	
	/**
	 * 修改字段
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void update(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		McField field = (McField) requestBean.getParameterList().get(0);
		
		//查询旧记录
		McField oldField = fieldMapper.selectByPrimaryKey(field.getId());
		//修改前判断，配置了该字段的表是否已经创建
		List<String> tableNameList = fieldMapper.selectTableNames(field.getId());
		for(int i=0;i<tableNameList.size();i++){
			int count =	fieldMapper.selectTableCol(ARSConstants.DB_TYPE, BaseUtil.filterSqlParam((tableNameList.get(i)+"").toUpperCase()),BaseUtil.filterSqlParam(oldField.getName()));
			if(count > 0){
				//配置了该字段的表已经创建，无法修改
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("配置了该字段的表已经创建，无法修改！");
				return;
			}
		}
		
		//如果字段名称改变，需判断修改后的字段名称是否已存在
		if(!oldField.getName().equals(field.getName())){
			McField selField = new McField();
			selField.setName(field.getName());
			List<McField> list = fieldMapper.selectBySelective(selField);
			if(list != null && list.size()>0) {
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("字段已存在！");
				return;
			}
		}
		
		//修改字段
		fieldMapper.updateByPrimaryKeySelective(field);
		Map retMap = new HashMap();
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功！");
		//添加日志
		String log = "警报规则配置修改id为:"+field.getId()+"的字段";
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, log);
	}
	
	/**
	 * 删除字段
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void delete(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		List delList =(List)sysMap.get("delList");
		
		HashMap condMap =null;
		int count =0;
		//删除前判断是否存在关联
		for(int j=0;j<DelRelate.field.size();j++){
			condMap = new HashMap<String, Object>();
			condMap.put("relate", DelRelate.field.get(j));
			condMap.put("delList", delList);		
			count = relateMapper.selectRelate(condMap);
			if(count > 0){
				//存在关联信息，不允许删除
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("表被关联，请解除关联关系后再进行删除操作！");
				return;
			}
		}
		
		//删除字段
		for(int i=0;i<delList.size();i++){
			fieldMapper.deleteByPrimaryKey((int)delList.get(i));
			//添加日志
			String log = "警报规则配置删除id为:"+delList.get(i)+"的字段";
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);
		}
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("删除成功！");
	}
		
	/**
	 * 其他操作
	 * @param requestBean
	 * @param responseBean
	 */
	private void otherOperation(RequestBean requestBean, ResponseBean responseBean)throws Exception{
			
	}
	

}
