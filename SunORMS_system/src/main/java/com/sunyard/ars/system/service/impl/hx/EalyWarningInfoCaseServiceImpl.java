package com.sunyard.ars.system.service.impl.hx;
/**
 * 预警信息案例的查询 删除 查看详情
 */

import com.sunyard.ars.system.bean.et.ModelFieldResulet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.util.ParamUtil;
import com.sunyard.ars.system.dao.hx.ArmsFavoriteMapper;
import com.sunyard.ars.system.dao.mc.ExhibitFieldMapper;
import com.sunyard.ars.system.dao.mc.ModelMapper;
import com.sunyard.ars.system.pojo.mc.Model;
import com.sunyard.ars.system.service.hx.IEalyWarningInfoCaseService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.pojo.User;

@Service("ealyWarningInfoCaseService")
@Transactional
public class EalyWarningInfoCaseServiceImpl extends BaseService implements IEalyWarningInfoCaseService {

	@Resource
	private ModelMapper modelMapper;
	@Resource
	private ArmsFavoriteMapper armsFavoriteMapper;
	@Resource
    private ExhibitFieldMapper exhibitFieldMapper;

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
		if (ARSConstants.OPERATE_QUERY.equals(oper_type)) {// 查询预警案例库统计列表
			showAlarmInfoList(requestBean, responseBean);
		} else if("getArmsShowField".equals(oper_type)){//获取预警案例库展现字段
			getArmsShowField(requestBean, responseBean);
		} else if("showAlarmInfoDetail".equals(oper_type)){//查询预警案例库明细列表
			showAlarmInfoDetailList(requestBean, responseBean);
		} else if("deleteFavorite".equals(oper_type)){//删除案例库
			deleteFavorite(requestBean, responseBean);
		} else if("setCommon".equals(oper_type)){//案例库公有化
			setCommon(requestBean, responseBean);
		}
	}

	/**
	 * 
	 * 查询符合条件的预警信息案例(依据机构,输入的条件查找)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void showAlarmInfoList(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		String modelName = (String) sysMap.get("modelName");
		String startAddTime = (String) sysMap.get("startAddTime");
		String endAddTime = (String) sysMap.get("endAddTime");
		int currentPage = (int) sysMap.get("currentPage");
		int pageSize = 0;
		if (currentPage != -1) {
			Integer initPageSize = (Integer) sysMap.get("pageSize");
			if (initPageSize == null) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageSize;
			}
		}

		User user = BaseUtil.getLoginUser();// 通过session获取当前用户对象
		String organNo = user.getOrganNo();// 获取当前用户的机构号
		Page page = PageHelper.startPage(currentPage, pageSize);
		
		HashMap condMap = new HashMap();
		condMap.put("modelName", modelName);
		condMap.put("startAddTime", startAddTime);
		condMap.put("endAddTime", endAddTime);
		condMap.put("organNo", organNo);
		//数据库类型
		condMap.put("dbType",ARSConstants.DB_TYPE);
		List<Map<Object, String>> returnList = armsFavoriteMapper.getAlarmInfoList(condMap);// 通过条件查询本机构有权限的所有预警案例信息
		long totalCount = page.getTotal();
		Map retMap = new HashMap();
		retMap.put("currentPage", currentPage);
		retMap.put("pageSize", pageSize);
		retMap.put("totalNum", totalCount);
		retMap.put("returnList", returnList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * 获取预警案例库具体明细列表
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getArmsShowField(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
        // 获取参数集合
        String modelId = ParamUtil.getParamValue(requestBean, "modelId");

        Model model = modelMapper.getModelInfo(Integer.parseInt(modelId));
        //获取展现字段
        List<ModelFieldResulet> modelFieldResulets = new ArrayList<ModelFieldResulet>();

        ModelFieldResulet modelFieldResulet = null;
        //配置的查询字段
        List<HashMap<String, Object>> model_fields = exhibitFieldMapper.showExhibitField(Integer.valueOf(modelId));
        for (int j = 0; j < model_fields.size(); j++) {
            HashMap<String, Object> model_field = model_fields.get(j);
            modelFieldResulet = new ModelFieldResulet();
            modelFieldResulet.setFieldId(Integer.valueOf(model_field.get("ID").toString()));
            modelFieldResulet.setName(String.valueOf(model_field.get("NAME")));
            modelFieldResulet.setChName(String.valueOf(model_field.get("CH_NAME")));
            modelFieldResulet.setType(Integer.valueOf(model_field.get("TYPE").toString()));
            modelFieldResulet.setRowno(Integer.valueOf(model_field.get("ROWNO").toString()));
            modelFieldResulet.setFormat(Integer.valueOf(model_field.get("FORMAT").toString()));
            modelFieldResulet.setIsfind(Integer.valueOf(model_field.get("ISFIND").toString()));
            modelFieldResulet.setIsdropdown(Integer.valueOf(model_field.get("ISDROPDOWN").toString()));
            modelFieldResulet.setIsimportant(Integer.valueOf(model_field.get("ISIMPORTANT").toString()));
            modelFieldResulet.setHisDic(Integer.valueOf(model_field.get("HASDIC").toString()));
            if (ObjectUtils.isEmpty(model_field.get("DICNAME"))) {
                modelFieldResulet.setDicName("");
            } else {
                modelFieldResulet.setDicName(model_field.get("DICNAME").toString());
            }
            if (ObjectUtils.isEmpty(model_field.get("RELATE_ID"))) {
                modelFieldResulet.setRelateId(0);
            } else {
                modelFieldResulet.setRelateId(Integer.valueOf(model_field.get("RELATE_ID").toString()));
            }
            modelFieldResulet.setIsTuoMin(model_field.get("ISTUOMIN") == null?"0":model_field.get("ISTUOMIN").toString());
            modelFieldResulets.add(modelFieldResulet);
        }
        
        Map retMap = new HashMap();
        retMap.put("model", model);
        retMap.put("modelFieldResulets", modelFieldResulets);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 获取预警案例库列表
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void showAlarmInfoDetailList(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		Map sysMap = requestBean.getSysMap();
		String modelId = ParamUtil.getParamValue(requestBean, "modelId");
		String startAddTime = ParamUtil.getParamValue(requestBean, "startAddTime");
		String endAddTime = ParamUtil.getParamValue(requestBean, "endAddTime");
		String querySql = ParamUtil.getParamValue(requestBean, "querySql");
		
        Model model = modelMapper.getModelInfo(Integer.parseInt(modelId));

		int currentPage = (int) sysMap.get("currentPage");
		int pageSize = 0;
		if (currentPage != -1) {
			Integer initPageSize = (Integer) sysMap.get("pageSize");
			if (initPageSize == null) {
				pageSize = ARSConstants.PAGE_NUM;
			} else {
				pageSize = initPageSize;
			}
		}

		Page page = PageHelper.startPage(currentPage, pageSize);
		
		HashMap condMap = new HashMap();
		condMap.put("tableName", BaseUtil.filterSqlParam(model.getTableName()));
		condMap.put("startAddTime", BaseUtil.filterSqlParam(startAddTime));
		condMap.put("endAddTime", BaseUtil.filterSqlParam(endAddTime));
		condMap.put("queryStr", BaseUtil.filterSqlParam(querySql));
		condMap.put("entryId", BaseUtil.filterSqlParam(modelId));
		condMap.put("organNo", BaseUtil.filterSqlParam(BaseUtil.getLoginUser().getOrganNo()));// 获取当前用户的机构号
		List<Map<Object, String>> returnList = armsFavoriteMapper.getAlarmInfoDetailList(condMap);
		long totalCount = page.getTotal();
		Map retMap = new HashMap();
		retMap.put("currentPage", currentPage);
		retMap.put("pageSize", pageSize);
		retMap.put("totalNum", totalCount);
		retMap.put("modelDatas", returnList);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}
	
	/**
	 * 删除案例库
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void deleteFavorite(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
		// 获取参数集合
        Map sysMap = requestBean.getSysMap();
        List<Object> delList= requestBean.getParameterList(); 
        List<String> deleteList = new ArrayList<String>();
        for (Object object : delList) {
        	deleteList.add(((Map)object).get("id").toString());
		}
        
        HashMap condMap = new HashMap();
		condMap.put("deleteList", deleteList);
		condMap.put("favoriteType", sysMap.get("favoriteType"));
		condMap.put("favoriteUser", BaseUtil.getLoginUser().getUserNo());// 获取当前用户的机构号
        int delFlag = armsFavoriteMapper.deleteFavorite(condMap);
        
        if(delFlag > 0){
        	responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        	responseBean.setRetMsg("删除成功！");
        }else{
        	responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
        	responseBean.setRetMsg("删除失败！");
        }
	}
	
	/**
	 * 案例库公有方法
	 * @param requestBean
	 * @param responseBean
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setCommon(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        // 获取参数集合
        Map sysMap = requestBean.getSysMap();
        List<Object> parameterList= requestBean.getParameterList(); 
        List<String> commSetList = new ArrayList<String>();
        for (Object object : parameterList) {
        	commSetList.add(((Map)object).get("armsFavoriteId").toString());
		}
        String commonRemark = (String) sysMap.get("commonRemark");//公有备注

        HashMap condMap = new HashMap();
		condMap.put("commSetList", commSetList);
		condMap.put("commonRemark", commonRemark);
		
        int commonFlag = armsFavoriteMapper.setCommon(condMap);
        
        if(commonFlag>0){
        	responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        	responseBean.setRetMsg("设置公有成功！");
        }else{
        	responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
        	responseBean.setRetMsg("设置公有失败！");
        }
    }
}
