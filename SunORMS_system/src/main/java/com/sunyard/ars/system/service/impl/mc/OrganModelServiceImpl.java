package com.sunyard.ars.system.service.impl.mc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.dao.mc.ModelMapper;
import com.sunyard.ars.system.dao.mc.OrganModelMapper;
import com.sunyard.ars.system.pojo.mc.Model;
import com.sunyard.ars.system.pojo.mc.OrganModel;
import com.sunyard.ars.system.service.mc.IOrganModelService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service("organModelService")
@Transactional
public class OrganModelServiceImpl extends BaseService  implements IOrganModelService{
	
	@Resource
	private OrganModelMapper organModelMapper;
	
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
		} else if ("queryModelOrgan".equals(oper_type)) {
			queryModelOrgan(requestBean, responseBean);
		} else if ("updateModelOrgan".equals(oper_type)) {
			updateModelOrgan(requestBean, responseBean);
		}
	}
	
	/**
	 * 查询机构模型
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		OrganModel organModel = (OrganModel) requestBean.getParameterList().get(0);
		//查询所有模型
		Model searchModel = new Model();
		//searchModel.setModelType("0");
		searchModel.setDetailType("0");
		searchModel.setStatus("1");
		List<Model> resultList = modelMapper.selectBySelective(searchModel);
		//查询配置的
		List<OrganModel> organModelList = organModelMapper.selectBySelective(organModel);
		
		//拼装选中索引		
		String selectIndex ="";
		for(int i=0;i<resultList.size();i++){
			for(OrganModel oModel : organModelList){
				if(oModel.getModelId().intValue() == resultList.get(i).getId().intValue()){
					selectIndex +=i+",";
				}
			}
		}
		
		if(selectIndex.length()>0){
			selectIndex = selectIndex.substring(0,selectIndex.length()-1);
		}
		
		Map retMap = new HashMap();
		retMap.put("resultList", resultList);
		retMap.put("selectIndex", selectIndex);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 新增机构模型
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void add(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
	
	/**
	 * 修改机构模型
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void update(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		List<Object> organModelList = requestBean.getParameterList();
		
		//先删除当前机构的机构模型
		String organNo = (String)sysMap.get("organNo");
		organModelMapper.deleteOrganModel(organNo);
		
		//插入新的机构模型数据
		if(organModelList!=null && organModelList.size()>0){
			/*for(int i=0;i<organModelList.size();i++){
				organModelMapper.insertSelective((OrganModel)organModelList.get(i));
			}*/
			List<OrganModel> insertList = new ArrayList<>();
			for(int i=0;i<organModelList.size();i++){
				insertList.add((OrganModel)organModelList.get(i));
			}	
			organModelMapper.insertBatchOrganModel(insertList);
		}
		
	}
	
	/**
	 * 删除机构模型
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void delete(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
	}
		
	/**
	 * 其他操作
	 * @param requestBean
	 * @param responseBean
	 */
	private void otherOperation(RequestBean requestBean, ResponseBean responseBean)throws Exception{
			
		}
	

	@SuppressWarnings("rawtypes")
	private void updateModelOrgan(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		List<Object> organModelList = requestBean.getParameterList();
		
		//先删除当前机构的机构模型
		Integer modelId = (Integer)sysMap.get("modelId");
		organModelMapper.deleteOrganModelByModel(modelId);
		
		List<OrganModel> insertList = new ArrayList<>();
		//插入新的机构模型数据
		if(organModelList!=null && organModelList.size()>0){
			for(int i=0;i<organModelList.size();i++){
				insertList.add((OrganModel)organModelList.get(i));
			}	
			organModelMapper.insertBatchOrganModel(insertList);
		}
		
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("保存成功");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryModelOrgan(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		//获取页面参数
		OrganModel organModel = (OrganModel) requestBean.getParameterList().get(0);
		List<OrganModel> modelOrganList = organModelMapper.selectBySelective(organModel);
		Map retMap = new HashMap();
		retMap.put("modelOrganList", modelOrganList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
}
