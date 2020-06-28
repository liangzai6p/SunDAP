package com.sunyard.ars.system.service.impl.mc;

import java.util.ArrayList;
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
import com.sunyard.ars.system.dao.mc.ExhibitFieldMapper;
import com.sunyard.ars.system.dao.mc.McFieldMapper;
import com.sunyard.ars.system.dao.mc.McModelLineMapper;
import com.sunyard.ars.system.dao.mc.ModelMapper;
import com.sunyard.ars.system.pojo.mc.ExhibitField;
import com.sunyard.ars.system.pojo.mc.McField;
import com.sunyard.ars.system.pojo.mc.McModelLine;
import com.sunyard.ars.system.pojo.mc.Model;
import com.sunyard.ars.system.service.mc.IMcModelLineService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

@Service("mcModelLineService")
@Transactional
public class McModelLineServiceImpl extends BaseService  implements IMcModelLineService{
	
	@Resource
	private McModelLineMapper mcModelLineMapper;
	
	@Resource
	private ModelMapper modelMapper;
	
	@Resource
	private ExhibitFieldMapper exhibitFieldMapper;
	
	@Resource
	private McFieldMapper mcFiledMapper;
	
	
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
		} else if(ARSConstants.OPERATE_DELETE.equals(oper_type)) { 
			// 删除关联表
			delete(requestBean, responseBean);
		} else if("selectModelFiled".equals(oper_type)){
			//查询模型对应的字段
			selectModelFiled(requestBean, responseBean);
		} else if("selectLineModelFiled".equals(oper_type)){
			//查询关联模型对应的字段
			selectLineModelFiled(requestBean, responseBean);
		} else if("addfiled".equals(oper_type)){
			//添加字段
			addFiled(requestBean, responseBean);
		} else if("deleteFiled".equals(oper_type)){
			//删除字段
			deleteFiled(requestBean, responseBean);
		} else if("queryFiled".equals(oper_type)){
			queryFiled(requestBean, responseBean);
		}
	}
	
	/**
	 * 查询表定义表  只查询模型id和关联模型id
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void query(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		McModelLine mcModelLine = (McModelLine) requestBean.getParameterList().get(0);
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
		List resultList = mcModelLineMapper.selectIdAndLineid(mcModelLine);
		//List resultList = mcModelLineMapper.selectBySelective(mcModelLine);
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
	 * 查询表定义表  只查询模型id和关联模型id
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void queryFiled(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		McModelLine mcModelLine = (McModelLine) requestBean.getParameterList().get(0);
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
		List resultList = mcModelLineMapper.selectBySelective(mcModelLine);
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
	 * 新增表定义表
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes"})
	private void add(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		Boolean flag = false;//是否继续添加标识
		Map sysMap = requestBean.getSysMap();
		McModelLine mcModelLine = (McModelLine) requestBean.getParameterList().get(0);
		//获取关联模型id
		String lid = (String) sysMap.get("lineid");
		if(mcModelLine.getId().toString().equals(lid)){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("不能与自身关联，添加失败！");
		}else{
			//添加前判断关联模型是否已经存在
			McModelLine mml = new McModelLine();
			mml.setLineid(Integer.valueOf(lid));
			List<McModelLine> tableList = mcModelLineMapper.selectBySelective(mml);
			if(tableList!=null && tableList.size()>0){
				//数据库中已存在该表定义表，无法新增
				//得出重复的模型
				McModelLine mc = tableList.get(0);
				Model mcMdel = modelMapper.selectByPrimaryKey(Integer.valueOf(BaseUtil.filterSqlParam(mc.getLineid()+"")));
				responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
				responseBean.setRetMsg(mcMdel.getName() + "模型已存在，无法关联");
				flag = false;
		}else{
			mml.setId(mcModelLine.getId());
			mcModelLineMapper.insertSelective(mml);
			flag = true;
			//添加日志
			String log = "警报规则配置中关联模型配置新增,模型编号:"+mcModelLine.getId() + "关联模型编号:" + mcModelLine.getLineid();
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
		}	
	}
		if(flag == true){
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("添加成功！");
		}
	}
	
	
	/**
	 * 新增字段
	 * @param requestBean
	 * @param responseBean
	 */
	private void addFiled(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		McModelLine mcModelLine = (McModelLine) requestBean.getParameterList().get(0);
		
		//根据字段查询是否重重复
		List<McModelLine> tableList = mcModelLineMapper.selectBySelective(mcModelLine);
		if(tableList!=null && tableList.size()>0){
			//已存在数据，无法添加
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg(mcModelLine.getModelfiled() + "字段已关联，无法继续关联");
		}else{
			//查询原来与之有关的数据
			McModelLine mccc = new McModelLine();
			mccc.setLineid(mcModelLine.getLineid());
			List<McModelLine> tableLists = mcModelLineMapper.selectBySelective(mccc);
			for (int i = 0; i < tableLists.size(); i++) {
				if("".equals(tableLists.get(i).getLinefiled()) || tableLists.get(i).getLinefiled() == null){
					//字段为空则删除
					mcModelLineMapper.deleteByLineFiled(Integer.valueOf(BaseUtil.filterSqlParam(tableLists.get(i).getLineid()+"")));
				}
			}
			//再次查询一次
			List<McModelLine> tableListss = mcModelLineMapper.selectBySelective(mccc);
			if(tableListss != null && tableListss.size() > 0){
				//得出该关联模型的字段下标
				Integer sizes = tableListss.size();
				Integer filednumber = sizes + 1;
				mcModelLine.setFilednumber(filednumber.toString());
			}else{
				mcModelLine.setFilednumber("1");
			}
			mcModelLineMapper.insertSelective(mcModelLine);
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("添加成功！");
			
			//添加日志
			String log = "警报规则配置，模型关联配置的新增 模型id:" + mcModelLine.getId() + 
						"关联模型字段：" + mcModelLine.getModelfiled() + "关联模型id：" + mcModelLine.getLineid()
						+ "关联字段：" + mcModelLine.getLinefiled();
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, log);
		}
		
	}
	
	
	
	/**
	 * 删除关联表
	 * @param requestBean
	 * @param responseBean
	 */
	@SuppressWarnings({ "rawtypes"})
	private void delete(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		Boolean flag = false;
		//获取页面参数
		Map sysMap = requestBean.getSysMap();
		List delList =(List)sysMap.get("delList");
		List<String> idlist = new ArrayList<String>();
		for (int i = 0; i < delList.size(); i++) {
			Map map = (Map) delList.get(i);
			idlist.add(map.get("lineid").toString());
		}
		
		//删除前判断是否存在关联
		for(int j=0;j<idlist.size();j++){
			McModelLine mcl = new McModelLine();
			mcl.setLineid(Integer.valueOf(idlist.get(j)));
			List<McModelLine> tableLists = mcModelLineMapper.selectBySelective(mcl);
			for (int i = 0; i < tableLists.size(); i++) {
				if(tableLists.get(i).getLinefiled()!=null && !"".equals(tableLists.get(i).getLinefiled())){
					
					break;
				}else{
					mcModelLineMapper.deleteByLineFiled(Integer.valueOf(BaseUtil.filterSqlParam(tableLists.get(i).getLineid()+"")));
					flag = true;
					
					
				}
			}
		}
		
		if(flag == false){
			responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("有关联字段，删除失败！");
		}else{
			responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("删除成功！");
		}
			
	}
		
	
	/**
	 * 
	 * @Description: 删除字段
	 * @param @param requestBean
	 * @param @param responseBean
	 * @param @throws Exception   
	 * @return void  
	 * @throws
	 * @author zs
	 * @date 2018年10月19日
	 */
	private void deleteFiled(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		//获取页面参数
		McModelLine mcModelLine = (McModelLine) requestBean.getParameterList().get(0);

		mcModelLineMapper.deleteByfileds(mcModelLine);
		
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("删除成功！");
		
		//添加日志
		//添加日志
		String log = "警报规则配置，模型关联配置的删除操作。 模型id:" + mcModelLine.getId() + 
					"关联模型字段：" + mcModelLine.getModelfiled() + "关联模型id：" + mcModelLine.getLineid()
					+ "关联字段：" + mcModelLine.getLinefiled();
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, log);	
	}
	
	
	/**
	 * 
	 * @Description: 处理模型字段
	 * @param @param requestBean
	 * @param @param responseBean   
	 * @return void  
	 * @throws
	 * @author zs
	 * @date 2018年10月19日
	 */
	@SuppressWarnings({ "rawtypes","unchecked" })
	private void selectModelFiled(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
		//得出前台参数
		Map sysMap = requestBean.getSysMap();
		String modelId = sysMap.get("modelId").toString();
		//得出模型对应的字段
		ExhibitField record = new ExhibitField();
		record.setModelId(Integer.valueOf(modelId));
		//模型对应的字段集合
		List lists = exhibitFieldMapper.selectBySelective(record);
		Map map = new HashMap();
		for (int i = 0; i < lists.size(); i++) {
			//根据字段id得出字段名和字段中文名
			ExhibitField records = (ExhibitField) lists.get(i);
			McField fields = mcFiledMapper.selectByPrimaryKey(Integer.valueOf(BaseUtil.filterSqlParam(records.getTableFieldId()+"")));
			//map key为字段名   value为中文名
			map.put(fields.getName(),fields.getChName());
		}
		Map retMap = new HashMap();
		retMap.put("filedMap", map);
		responseBean.setRetMap(retMap);
		
	}
	
	/**
	 * 
	 * @Description: 处理关联字段
	 * @param @param requestBean
	 * @param @param responseBean   
	 * @return void  
	 * @throws
	 * @author zs
	 * @date 2018年10月19日
	 */
	@SuppressWarnings({ "rawtypes","unchecked" })
	private void selectLineModelFiled(RequestBean requestBean, ResponseBean responseBean)throws Exception{
		
		//得出前台参数
		Map sysMap = requestBean.getSysMap();
		String linefiled = sysMap.get("linefiled").toString();
		//得出模型对应的字段
		ExhibitField record = new ExhibitField();
		record.setModelId(Integer.valueOf(linefiled));
		//模型对应的字段集合
		List lists = exhibitFieldMapper.selectBySelective(record);
		Map map = new HashMap();
		for (int i = 0; i < lists.size(); i++) {
			//根据字段id得出字段名和字段中文名
			ExhibitField records = (ExhibitField) lists.get(i);
			McField fields = mcFiledMapper.selectByPrimaryKey(Integer.valueOf(BaseUtil.filterSqlParam(records.getTableFieldId()+"")));
			//map key为字段名   value为中文名
			map.put(fields.getName(),fields.getChName());
		}
		Map retMap = new HashMap();
		retMap.put("linefiledMap", map);
		responseBean.setRetMap(retMap);
		
	}
	
}
