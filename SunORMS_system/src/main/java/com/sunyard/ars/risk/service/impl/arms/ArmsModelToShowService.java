package com.sunyard.ars.risk.service.impl.arms;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.util.ParamUtil;
import com.sunyard.ars.common.util.StringUtil;
import com.sunyard.ars.common.util.DateUtil;
import com.sunyard.ars.risk.bean.arms.ModelFieldResulet;
import com.sunyard.ars.risk.bean.arms.SendSlipBean;
import com.sunyard.ars.risk.comm.ARMSConstants;
import com.sunyard.ars.risk.service.arms.IArmsModelToShowService;
import com.sunyard.ars.system.dao.busm.OrganInfoDao;
import com.sunyard.ars.system.dao.et.BusiFormMapper;
import com.sunyard.ars.system.dao.et.EtDataStatisticsMapper;
import com.sunyard.ars.system.dao.et.ModelDataQueryMapper;
import com.sunyard.ars.system.dao.mc.ExhibitFieldMapper;
import com.sunyard.ars.system.dao.mc.McModelLineMapper;
import com.sunyard.ars.system.dao.mc.ModelMapper;
import com.sunyard.ars.system.dao.mc.QueryImgMapper;
import com.sunyard.ars.system.pojo.mc.McModelLine;
import com.sunyard.ars.system.pojo.mc.Model;
import com.sunyard.ars.system.pojo.mc.QueryImg;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模型展现
 */
@Service("armsModelToShowService")
@Transactional
public class ArmsModelToShowService extends BaseService implements IArmsModelToShowService {
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private ExhibitFieldMapper exhibitFieldMapper;
    @Resource
    private ModelDataQueryMapper modelDataQueryMapper;
    @Resource
    private BusiFormMapper busiFormMapper;
    @Resource
    private OrganInfoDao organInfoDao;
    @Resource
	private McModelLineMapper mcModelLineMapper;
    @Resource
	private EtDataStatisticsMapper etDataStatisticsMapper;
    @Resource
	private QueryImgMapper queryImgMapper;
    @Override
    public ResponseBean execute(RequestBean requestBean) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        return executeAction(requestBean, responseBean);
    }

    @Override
    protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        // 获取参数集合
        Map sysMap = requestBean.getSysMap();
        // 获取操作标识
        String oper_type = (String) sysMap.get("oper_type");

        if ("getShowField".equals(oper_type)) {//查询显示字段
            getShowField(requestBean, responseBean);
        } else if ("getModelData".equals(oper_type)) {
            getModelData(requestBean, responseBean);
        } else if ("getModelDataToOne".equals(oper_type)) {
            getModelDataToOne(requestBean, responseBean);
        } else if ("showExhibitQueryField".equals(oper_type)) {
            showExhibitQueryField(requestBean, responseBean);
        } else if ("getHistoryModelData".equals(oper_type)) {
            getHistoryModelData(requestBean, responseBean);
        } else if ("getRelatingModelInfo".equals(oper_type)) {
            getRelatingModelInfo(requestBean, responseBean);
        } else if("getRelatingModelData".equals(oper_type)) {
            getRelatingModelData(requestBean, responseBean);
        }else if("getModelDataToMany".equals(oper_type)){
            getModelDataToMany(requestBean, responseBean);
        } else if("getRelatingModelDataToMany".equals(oper_type)){
            getRelatingModelDataToMany(requestBean, responseBean);           
        }else if("eWICDetailDelete".equals(oper_type)){
        	eWICDetailDelete(requestBean, responseBean);
        }else if("eWICDetailSetCommon".equals(oper_type)) {
        	eWICDetailSetCommon(requestBean, responseBean);
        } else if("getModelFieldResulets".equals(oper_type)){
            getModelFieldResulets(requestBean, responseBean);
        }else if("getModelDetailData".equals(oper_type)) {
        	getModelDetailData(requestBean, responseBean);
        }else if("getRelateSearchField".equals(oper_type)) {//获取关联模型查询字段信息
        	getRelateSearchField(requestBean, responseBean);
        }else if("getRelateModelData".equals(oper_type)){
        	getRelateModelData(requestBean, responseBean);
        }else if("getRelateModelAllData".equals(oper_type)){
        	getRelateModelAllData(requestBean, responseBean);
        }else if("getModelInfoByPKId".equals(oper_type)){//根据模型主键查询唯一的模型数据
        	getModelInfoByPKId(requestBean, responseBean);
        }else if ("getPromptModelData".equals(oper_type)) {//查询提示类数据
        	getPromptModelData(requestBean, responseBean);
        }else if("getHisRelatingModelDataToMany".equals(oper_type)){//历史警报明细查询
            getHisRelatingModelDataToMany(requestBean, responseBean);           
        }else if("getHisRelateModelAllData".equals(oper_type)){//所有历史警报明细查询
        	getHisRelateModelAllData(requestBean, responseBean);
        }else {
            Map retMap = new HashMap();
            responseBean.setRetMap(retMap);
            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
            responseBean.setRetMsg("交易请求未找到");
        }

    }

    /**
     * 查询提示类数据
     * @param requestBean
     * @param responseBean
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void getPromptModelData(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
    	// 获取参数集合
        Map sysMap = requestBean.getSysMap();
        int currentPage = (int) sysMap.get("currentPage");
        int pageSize = 0;
        if (currentPage != -1) {
            Integer initPageSize = (Integer) sysMap.get("pageSize");
            if (initPageSize == null) {
                pageSize = ARSConstants.PAGE_NUM;
            } else {
                pageSize = initPageSize;
            }
        } else {
        	pageSize = 0;
        }
        //组装统计页面查询字段
        String tableName = ParamUtil.getParamValue(requestBean, "tableName");
        String fields = ParamUtil.getParamValue(requestBean, "arms_show_model_query");
        //统计查询字段
        Map<String, String> queryTjParamMap = getTjQueryMap(requestBean);
        //展现字段查询字段
        List<Map<String, String>> customQueryFieldMap = getcustomQueryFieldMap(requestBean);
        //其他查询字段
        Map<String, String> otherQueryFieldMap = getOtherQueryFieldMap(requestBean);

        List<Map<String, String>> arms_relate_field_lists = (List<Map<String, String>>) ((Map) requestBean.getParameterList().get(0)).get("arms_relate_field_lists");
        
        
        StringBuffer his_query_sql_count = new StringBuffer();
        his_query_sql_count.append("SELECT COUNT(1) NUM FROM (");
        his_query_sql_count.append(getQueryModelDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,arms_relate_field_lists,"0"));
        his_query_sql_count.append(" UNION ALL ");
        his_query_sql_count.append(getQueryModelDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,arms_relate_field_lists,"1"));
        his_query_sql_count.append(")");
        Integer datas_count = modelDataQueryMapper.getModelDataCount(his_query_sql_count.toString());
        List<Map<String, Object>> datas = null;
        if(datas_count > 0){
        	 StringBuffer his_query_sql = new StringBuffer();
        	if (pageSize != 0) {//正规分页
                //同查临时表和历史表
                //原来是oracle，db2和oracle的分页不同
                switch (ARSConstants.DB_TYPE){
                    case ARSConstants.DATABASE_TYPE_DB2:
                        his_query_sql.append("SELECT * FROM (SELECT ALL_PAGE.*,ROWNUMBER() OVER() ROW_ID FROM (SELECT TMP_PAGE.* FROM (");
                        his_query_sql.append(getQueryModelDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,arms_relate_field_lists,"0"));
                        his_query_sql.append(" UNION ALL ");
                        his_query_sql.append(getQueryModelDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,arms_relate_field_lists,"1"));
                        his_query_sql.append(")TMP_PAGE ").append(" ORDER BY TMP_PAGE.CREATE_DATE DESC,TMP_PAGE.HANDLE_ORGAN,TMP_PAGE.ENTRYROW_ID) ALL_PAGE ")
                                .append(" ) WHERE ROW_ID <= ").append(currentPage*pageSize).append(" AND ROW_ID >").append((currentPage-1)*pageSize);
                        break;
                    default:
                        his_query_sql.append("SELECT * FROM (SELECT ALL_PAGE.*,ROWNUM ROW_ID FROM (SELECT TMP_PAGE.* FROM (");
                        his_query_sql.append(getQueryModelDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,arms_relate_field_lists,"0"));
                        his_query_sql.append(" UNION ALL ");
                        his_query_sql.append(getQueryModelDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,arms_relate_field_lists,"1"));
                        his_query_sql.append(")TMP_PAGE ").append(" ORDER BY TMP_PAGE.CREATE_DATE DESC,TMP_PAGE.HANDLE_ORGAN,TMP_PAGE.ENTRYROW_ID) ALL_PAGE WHERE ROWNUM <=").append(currentPage*pageSize).append(") WHERE ROW_ID >").append((currentPage-1)*pageSize);
                        break;
                }
        	} else {//全部查询（现在只用于页面导出全部按钮使用）
                //同查临时表和历史表
                his_query_sql.append("SELECT TMP_PAGE.* FROM (");
                his_query_sql.append(getQueryModelDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,arms_relate_field_lists,"0"));
                his_query_sql.append(" UNION ALL ");
                his_query_sql.append(getQueryModelDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,arms_relate_field_lists,"1"));
                his_query_sql.append(")TMP_PAGE ").append(" ORDER BY TMP_PAGE.CREATE_DATE DESC,TMP_PAGE.HANDLE_ORGAN,TMP_PAGE.ENTRYROW_ID");
        	}
            datas = modelDataQueryMapper.getModelData(his_query_sql.toString());
        }

        Map retMap = new HashMap();
        retMap.put("currentPage", currentPage);
        retMap.put("pageNum", pageSize);
        retMap.put("totalNum", datas_count);
        retMap.put("modelDatas", datas);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
	}

	/**
     * 历史预警数据查看
     * @param requestBean
     * @param responseBean
     * @throws Exception
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getHistoryModelData(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		//日志信息 ,记录modeld,
		String logStr = ""; 
		String logType = "";
		String modelId = ParamUtil.getParamValue(requestBean, "modelId");
        // 获取参数集合
        Map sysMap = requestBean.getSysMap();
        int currentPage = (int) sysMap.get("currentPage");
        int pageSize = 0;
        if (currentPage != -1) {
            Integer initPageSize = (Integer) sysMap.get("pageSize");
            if (initPageSize == null) {
                pageSize = ARSConstants.PAGE_NUM;
            } else {
                pageSize = initPageSize;
            }
        } else {
        	pageSize = 0;
        }
        //组装统计页面查询字段
        String tableName = ParamUtil.getParamValue(requestBean, "tableName");
        String fields = ParamUtil.getParamValue(requestBean, "arms_show_model_query");
        //统计查询字段
        Map<String, String> queryTjParamMap = getTjQueryMap(requestBean);
        //展现字段查询字段
        List<Map<String, String>> customQueryFieldMap = getcustomQueryFieldMap(requestBean);
        //其他查询字段
        Map<String, String> otherQueryFieldMap = getOtherQueryFieldMap(requestBean);

        StringBuffer his_query_sql_count = new StringBuffer();
        his_query_sql_count.append("SELECT COUNT(1) NUM FROM (");
        his_query_sql_count.append(getQueryModelHistoryDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,"0"));
        his_query_sql_count.append(" UNION ALL ");
        his_query_sql_count.append(getQueryModelHistoryDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,"1"));
        his_query_sql_count.append(")");
        Integer datas_count = modelDataQueryMapper.getModelDataCount(his_query_sql_count.toString());
        List<Map<String, Object>> datas = null;
        if(datas_count > 0){
        	 StringBuffer his_query_sql = new StringBuffer();
        	if (pageSize != 0) {//正规分页
                //同查临时表和历史表
               switch (ARSConstants.DB_TYPE){
                   case ARSConstants.DATABASE_TYPE_DB2:
                       his_query_sql.append("SELECT * FROM (SELECT ALL_PAGE.*,ROWNUMBER() OVER() ROW_ID FROM (SELECT TMP_PAGE.* FROM (");
                       his_query_sql.append(getQueryModelHistoryDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,"0"));
                       his_query_sql.append(" UNION ALL ");
                       his_query_sql.append(getQueryModelHistoryDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,"1"));
                       his_query_sql.append(")TMP_PAGE ").append(" ORDER BY TMP_PAGE.CREATE_DATE DESC,TMP_PAGE.HANDLE_ORGAN,TMP_PAGE.ENTRYROW_ID) ALL_PAGE ")
                               .append(" ) WHERE ROW_ID <= ").append(currentPage*pageSize).append(" AND ROW_ID >").append((currentPage-1)*pageSize);
                       break;
                   default:
                       his_query_sql.append("SELECT * FROM (SELECT ALL_PAGE.*,ROWNUM ROW_ID FROM (SELECT TMP_PAGE.* FROM (");
                       his_query_sql.append(getQueryModelHistoryDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,"0"));
                       his_query_sql.append(" UNION ALL ");
                       his_query_sql.append(getQueryModelHistoryDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,"1"));
                       his_query_sql.append(")TMP_PAGE ").append(" ORDER BY TMP_PAGE.CREATE_DATE DESC,TMP_PAGE.HANDLE_ORGAN,TMP_PAGE.ENTRYROW_ID) ALL_PAGE WHERE ROWNUM <=").append(currentPage*pageSize).append(") WHERE ROW_ID >").append((currentPage-1)*pageSize);
                       break;
               }

                logStr = "查询模型id为" + modelId + "历史警报信息";
                logType = ARSConstants.OPER_TYPE_4;
        	} else {//全部查询（现在只用于页面导出全部按钮使用）
                //同查临时表和历史表
                his_query_sql.append("SELECT TMP_PAGE.* FROM (");
                his_query_sql.append(getQueryModelHistoryDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,"0"));
                his_query_sql.append(" UNION ALL ");
                his_query_sql.append(getQueryModelHistoryDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,"1"));
                his_query_sql.append(")TMP_PAGE ").append(" ORDER BY TMP_PAGE.CREATE_DATE DESC,TMP_PAGE.HANDLE_ORGAN,TMP_PAGE.ENTRYROW_ID");
                
                logStr = "导出模型id为" + modelId + "历史警报信息";
                logType = ARSConstants.OPER_TYPE_4_2;
        	}
            datas = modelDataQueryMapper.getModelData(his_query_sql.toString());
        }

        Map retMap = new HashMap();
        retMap.put("currentPage", currentPage);
        retMap.put("pageNum", pageSize);
        retMap.put("totalNum", datas_count);
        retMap.put("modelDatas", datas);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
        
        addOperLogInfo(ARSConstants.MODEL_NAME_RISK_WARNING, ARSConstants.OPER_TYPE_4_2, logStr);
    }


    /**
     * 获取模型基础属性
     *
     * @param requestBean
     * @param responseBean
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void getShowField(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        // 获取参数集合
        Map sysMap = requestBean.getSysMap();
        // 获取参数集合
        String modelId = ParamUtil.getParamValue(requestBean, "modelId");
        Model model = modelMapper.getModelInfo(Integer.parseInt(modelId));
        //获取展现字段
        List<ModelFieldResulet> modelFieldResulets = getModelFieldResulets(Integer.valueOf(modelId));

        QueryImg queryImg = new QueryImg();
        queryImg.setMid(Integer.parseInt(modelId));
        //获取看图分组
        List<QueryImg> queryImgList = queryImgMapper.selectBySelective(queryImg);
        
        Map retMap = new HashMap();
        retMap.put("model", model);
        retMap.put("modelFieldResulets", modelFieldResulets);
        retMap.put("queryImgList", queryImgList);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");

    }

    /**
     * 获取关联数据
     * @param requestBean
     * @param responseBean
     * @throws Exception
     */
    public void getRelatingModelInfo(RequestBean requestBean, ResponseBean responseBean) throws  Exception{

        // 获取参数集合
        String relatingId = ParamUtil.getParamValue(requestBean, "relatingId");
        Model model = modelMapper.getModelInfo(Integer.parseInt(relatingId));
        List<ModelFieldResulet> modelFieldResulets = new ArrayList<ModelFieldResulet>();
        if (model != null && model.getId() != null) {
        	modelFieldResulets = getModelFieldResulets(Integer.valueOf(model.getId()));
        } 
        Map retMap = new HashMap();
        retMap.put("model", model);
        retMap.put("modelFieldResulets", modelFieldResulets);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
    }

    /**
     * 获取关联数据
     * @param requestBean
     * @param responseBean
     * @throws Exception
     */
    public void getRelatingModelDataToMany(RequestBean requestBean, ResponseBean responseBean) throws  Exception{
        // 获取参数集合
        String modelId = ParamUtil.getParamValue(requestBean, "modelId");
        List<Integer>  modelRowIds =  ( List<Integer>) ((Map) requestBean.getParameterList().get(0)).get("modelRowIds");
        String relatingId = ParamUtil.getParamValue(requestBean, "relatingId");
        Model model = modelMapper.getModelInfo(Integer.parseInt(relatingId));
        List<HashMap<String,Object>> modeldatas =   modelDataQueryMapper.getRelatingModelDataToMany(BaseUtil.filterSqlParam(model.getTableName()),BaseUtil.filterSqlParam(modelId),modelRowIds);
        Map retMap = new HashMap();
        retMap.put("modeldatas", modeldatas);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
    }

    /**
     * 获取关联模型数据
     * @param requestBean
     * @param responseBean
     * @throws Exception
     */
    public void getRelatingModelData(RequestBean requestBean, ResponseBean responseBean) throws  Exception{
        // 获取参数集合
        Map sysMap = requestBean.getSysMap();
        // 获取参数集合
        String modelId = ParamUtil.getParamValue(requestBean, "modelId");
        String modelRowId = ParamUtil.getParamValue(requestBean, "modelRowId");
        String relatingId = ParamUtil.getParamValue(requestBean, "relatingId");

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

        Model model = modelMapper.getModelInfo(Integer.parseInt(relatingId));
        Page page = PageHelper.startPage(currentPage, pageSize);
        List<HashMap<String,Object>> modeldatas =   modelDataQueryMapper.getRelatingModelData(BaseUtil.filterSqlParam(model.getTableName()),BaseUtil.filterSqlParam(modelId),BaseUtil.filterSqlParam(modelRowId));

        Map retMap = new HashMap();

        retMap.put("currentPage", currentPage);
        retMap.put("pageNum", pageSize);
        retMap.put("totalNum", page.getTotal());
        retMap.put("modeldatas", modeldatas);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
    }


    /**
     * 获取展现字段
     *
     * @param
     * @return
     * @throws Exception
     */
    public void getModelFieldResulets(RequestBean requestBean, ResponseBean responseBean) throws  Exception{
        // 获取参数集合
        String modelId = ParamUtil.getParamValue(requestBean, "modelId");
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
            modelFieldResulets.add(modelFieldResulet);
        }

        //针对配置和必须字段进行去重合
        ModelFieldResulet field = null;
        for (int i = 0; i < ARMSConstants.MODEL_MUST_FIELD.size(); i++) {
            field = ARMSConstants.MODEL_MUST_FIELD.get(i);
            boolean ishave = false;
            for (int j = 0; j < modelFieldResulets.size(); j++) {
                if (modelFieldResulets.get(j).getName().equalsIgnoreCase(field.getName())) {
                    ishave = true;
                    break;
                }
            }
            if (!ishave) {
                modelFieldResulets.add(field);
            }
        }
        Map retMap = new HashMap();

        retMap.put("modelFieldResulets", modelFieldResulets);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
    }

    /**
     * 获取展现字段
     *
     * @param modelId
     * @return
     * @throws Exception
     */
    public List<ModelFieldResulet> getModelFieldResulets(Integer modelId) throws Exception {
        modelId = Integer.valueOf(BaseUtil.filterSqlParam(modelId+""));
        List<ModelFieldResulet> modelFieldResulets = new ArrayList<ModelFieldResulet>();

        ModelFieldResulet modelFieldResulet = null;
        //配置的查询字段
        List<HashMap<String, Object>> model_fields = exhibitFieldMapper.showExhibitField(modelId);
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
           if(ObjectUtils.isEmpty(model_field.get("ISTUOMIN"))){
            	modelFieldResulet.setIsTuoMin(0);
            }else {
            	modelFieldResulet.setIsTuoMin(Integer.valueOf(model_field.get("ISTUOMIN").toString()));
            }
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
            modelFieldResulets.add(modelFieldResulet);
        }

        //针对配置和必须字段进行去重合
        ModelFieldResulet field = null;
        for (int i = 0; i < ARMSConstants.MODEL_MUST_FIELD.size(); i++) {
            field = ARMSConstants.MODEL_MUST_FIELD.get(i);
            boolean ishave = false;
            for (int j = 0; j < modelFieldResulets.size(); j++) {
                if (modelFieldResulets.get(j).getName().equalsIgnoreCase(field.getName())) {
                    ishave = true;
                    break;
                }
            }
            if (!ishave) {
                modelFieldResulets.add(field);
            }
        }

        return modelFieldResulets;
    }

    /**
     * 获取指定条件的预警数据
     *
     * @param requestBean
     * @param responseBean
     */
    @SuppressWarnings("unchecked")
	public void getModelData(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        // 获取参数集合
        Map sysMap = requestBean.getSysMap();
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

        //组装统计页面查询字段
        String tableName = ParamUtil.getParamValue(requestBean, "tableName");
        String fields = ParamUtil.getParamValue(requestBean, "arms_show_model_query");

        //统计查询字段
        Map<String, String> queryTjParamMap = getTjQueryMap(requestBean);
        //展现字段查询字段
        List<Map<String, String>> customQueryFieldMap = getcustomQueryFieldMap(requestBean);
        //其他查询字段
        Map<String, String> otherQueryFieldMap = getOtherQueryFieldMap(requestBean);
        
        List<Map<String, String>> arms_relate_field_lists = (List<Map<String, String>>) ((Map) requestBean.getParameterList().get(0)).get("arms_relate_field_lists");

        String querySql = getQueryModelDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, otherQueryFieldMap,arms_relate_field_lists,"0");
        querySql += " order by ENTRYROW_ID,proc_date,bankcode,teller";
        Page page = PageHelper.startPage(currentPage, pageSize);
        List<Map<String, Object>> datas = modelDataQueryMapper.getModelData(querySql);
        long totalCount = page.getTotal();


        Map retMap = new HashMap();
        retMap.put("currentPage", currentPage);
        retMap.put("pageNum", pageSize);
        retMap.put("totalNum", totalCount);
        retMap.put("modelDatas", datas);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
    }

    /**
     * 查询单笔数据
     *
     * @param requestBean
     * @param responseBean
     * @return
     */
    public void getModelDataToOne(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        Integer modelId = Integer.valueOf(ParamUtil.getParamValue(requestBean, "modelId"));
        Integer modelRowId = Integer.valueOf(ParamUtil.getParamValue(requestBean, "modelRowId"));
        String operatorType = ParamUtil.getParamValue(requestBean, "operatorType");
        modelId = Integer.valueOf(BaseUtil.filterSqlParam(modelId+""));
        modelRowId = Integer.valueOf(BaseUtil.filterSqlParam(modelRowId+""));

        //获取模型信息
        Model modelInfo = modelMapper.getModelInfo(Integer.valueOf(modelId));
        //获取该笔模型的信息
        Map<String, Object> modelDate = modelDataQueryMapper.getThisModelData(modelId, modelRowId, BaseUtil.filterSqlParam(modelInfo.getTableName()), null);
        if(modelDate == null) {
        	modelDate = modelDataQueryMapper.getThisModelData(modelId, modelRowId, BaseUtil.filterSqlParam(modelInfo.getTableName()+"_HIS"), null);
        }
        //获取该笔模型的展现字段
        //获取展现字段
        List<ModelFieldResulet> modelFieldResulets = getModelFieldResulets(modelId);
        SendSlipBean sendSlipBean = new SendSlipBean();
        if(!"3".equals(operatorType)){//撤销
            //提供反馈日期
            //确定需要反馈的天数;
            Integer feedbackDays =  modelInfo.getFeedbackDays();
            if (feedbackDays == null) {
                feedbackDays= 1;
            }

            feedbackDays =  Integer.valueOf(BaseUtil.filterSqlParam(feedbackDays+""));
            String backDate = busiFormMapper.selectWorkDate(feedbackDays,ARSConstants.DB_TYPE);
            String workDate = DateUtil.getNewDate(DateUtil.FORMATE_DATE_yyyyMMdd);

            sendSlipBean.setBackDate(backDate);
            sendSlipBean.setWorkDate(workDate);
            sendSlipBean.setProc_date( modelDate.get("PROC_DATE")+"");;

        }

        Map retMap = new HashMap();
        retMap.put("modelData", modelDate);
        retMap.put("modelFieldResulets", modelFieldResulets);
        retMap.put("modelInfo", modelInfo);
        retMap.put("sendSlipBean", sendSlipBean);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
    }

    /**
     * 查询多笔数据
     *
     * @param requestBean
     * @param responseBean
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void getModelDataToMany(RequestBean requestBean, ResponseBean responseBean) throws Exception {
    	Map retMap = new HashMap();
    	Integer modelId = Integer.valueOf(ParamUtil.getParamValue(requestBean, "modelId"));
        List<Integer>  modelRowIds =  ( List<Integer>) ((Map) requestBean.getParameterList().get(0)).get("modelRowId");
        String formType = ParamUtil.getParamValue(requestBean, "formType");
        String isBatch = ParamUtil.getParamValue(requestBean, "isBatch");
        String isHis = ParamUtil.getParamValue(requestBean, "isHis");
        //获取模型信息
        Model modelInfo = modelMapper.getModelInfo(Integer.valueOf(modelId));
        //获取该笔模型的信息
        String tableName = BaseUtil.filterSqlParam(modelInfo.getTableName());
        modelId = Integer.valueOf(BaseUtil.filterSqlParam(modelId+""));

        /*if ("1".equals(isHis))
            tableName = modelInfo.getTableName()+"_HIS";
        else
            tableName = modelInfo.getTableName();*/
        List<HashMap<String, Object>> modelDate = modelDataQueryMapper.getManyModelData(modelId, modelRowIds, tableName);
        modelDate.addAll(modelDataQueryMapper.getManyModelData(modelId, modelRowIds, tableName+"_HIS"));
        //获取该笔模型的展现字段
        //获取展现字段
        List<ModelFieldResulet> modelFieldResulets = getModelFieldResulets(modelId);
        SendSlipBean sendSlipBean = new SendSlipBean();
        //提供反馈日期
        //确定需要反馈的天数;
        Integer feedbackDays =  modelInfo.getFeedbackDays();
        if (feedbackDays == null) {
            feedbackDays= 1;
        }

        feedbackDays = Integer.valueOf(BaseUtil.filterSqlParam(feedbackDays+""));

        String backDate = busiFormMapper.selectWorkDate(feedbackDays,ARSConstants.DB_TYPE);
        sendSlipBean.setBackDate(backDate);
        
        if("0".equals(isBatch)){ //单笔
            if(!"1".equals(formType)){//撤销
            	 sendSlipBean.setWorkDate(modelDate.get(0).get("PROC_DATE")+"");
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                  SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
                  sendSlipBean.setCreate_date_new(DateUtil.addDateTimeRow(modelDate.get(0).get("CREATE_DATE")+""));
                  sendSlipBean.setProc_date( modelDate.get(0).get("BUSI_DATA_DATE")+"");
                  sendSlipBean.setWorkTime(DateUtil.addTimeRow(modelDate.get(0).get("CREATE_DATE")+""));
                  sendSlipBean.setBankcode(modelDate.get(0).get("BANKCODE")+"");
                  sendSlipBean.setBankname(modelDate.get(0).get("BANKNAME")+"");
            }
            
            String occurDate = modelDate.get(0).get("PROC_DATE") == null?"":modelDate.get(0).get("PROC_DATE")+"";
			String siteNo = modelDate.get(0).get("BANKCODE") == null?"":(String) modelDate.get(0).get("BANKCODE");
			String operatorNo = modelDate.get(0).get("OPERATOR_NO") == null?"":(String) modelDate.get(0).get("OPERATOR_NO");
			String acctNo = modelDate.get(0).get("ACCT_NO") == null?"":(String) modelDate.get(0).get("ACCT_NO");
			String statisticTableName = "supervise_statistic_tb";
			HashMap condMap = new HashMap();
			condMap.put("organNo", BaseUtil.getLoginUser().getOrganNo());
			if(!BaseUtil.isBlank(occurDate)){
				String time = com.sunyard.ars.common.util.DateUtil.getDateBeforOrAfter(occurDate,10);
				SimpleDateFormat fd = new SimpleDateFormat("yyyyMMdd");
				if(fd.parse(time).before(fd.parse(DateUtil.getNow()))){
					statisticTableName += "_his";
				}
			}
			condMap.put("tableName", BaseUtil.filterSqlParam(statisticTableName));
			condMap.put("occurDate", BaseUtil.filterSqlParam(occurDate));
			condMap.put("modelIdList", modelMapper.selectFilterModel(BaseUtil.getLoginUser().getUserNo()));
			if(!BaseUtil.isBlank(siteNo)){//同机构查询
				condMap.put("bankcode", BaseUtil.filterSqlParam(siteNo));
				int sameSiteCount = etDataStatisticsMapper.selectCountSame(condMap);
				retMap.put("sameSiteCount", sameSiteCount);
			}
			if(!BaseUtil.isBlank(operatorNo)){//同柜员查询
				condMap.remove("bankcode");
				condMap.put("operatorNo", BaseUtil.filterSqlParam(operatorNo));
				int sameTellCount = etDataStatisticsMapper.selectCountSame(condMap);					
				retMap.put("sameTellCount", sameTellCount);
			}
			if(!BaseUtil.isBlank(acctNo)){//同账号查询
				condMap.remove("bankcode");
				condMap.remove("operatorNo");
				condMap.put("acctNo", BaseUtil.filterSqlParam(acctNo));
				int sameAcctCount = etDataStatisticsMapper.selectCountSame(condMap);
				retMap.put("sameAcctCount", sameAcctCount);
			}
			
			HashMap condMapOther = new HashMap();
			condMapOther.put("modelId", Integer.valueOf(BaseUtil.filterSqlParam(modelDate.get(0).get("ENTRY_ID").toString())));
			condMapOther.put("userOrganNo", BaseUtil.getLoginUser().getOrganNo());
			condMapOther.put("modelIdList", modelMapper.selectFilterModel(BaseUtil.getLoginUser().getUserNo()));
			//获取该笔模型的信息
	        List<McModelLine> relateModelList = mcModelLineMapper.selectRelateModel(condMapOther);
	        if(null != relateModelList && relateModelList.size() > 0){
	        	List<McModelLine> relateList = new ArrayList<McModelLine>();
		        int relateId = 0;
		        for (McModelLine relateMode : relateModelList) {
					if(relateId != relateMode.getLineid()){
						relateId = relateMode.getLineid();
						relateList.add(relateMode);
					}
				}
				retMap.put("relateList", relateList);//查询关联模型信息
	        }
        }
        retMap.put("modelData", modelDate);
        retMap.put("modelFieldResulets", modelFieldResulets);
        retMap.put("modelInfo", modelInfo);
        retMap.put("sendSlipBean", sendSlipBean);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
    }



    public void showExhibitQueryField(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        Integer modelId = Integer.valueOf(ParamUtil.getParamValue(requestBean, "modelId"));
        List<ModelFieldResulet> modelFieldResulets = new ArrayList<ModelFieldResulet>();
        //配置的查询字段
        ModelFieldResulet modelFieldResulet = null;
        List<HashMap<String, Object>> model_fields = exhibitFieldMapper.showExhibitQueryField(modelId);
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
            modelFieldResulets.add(modelFieldResulet);
        }

        //获取模型信息
        Model modelInfo = modelMapper.getModelInfo(Integer.valueOf(modelId));

        Map retMap = new HashMap();
        retMap.put("modelFieldResulets", modelFieldResulets);
        retMap.put("modelInfo", modelInfo);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
    }


    /**
     * 从统计页面的查询字段
     */
    public Map<String, String> getTjQueryMap(RequestBean requestBean) {
        List<Map<String, String>> arms_show_tj_query_field_lists = (List<Map<String, String>>) ((Map) requestBean.getParameterList().get(0)).get("arms_show_tj_query_field_lists");
        Map<String, String> queryTjParamMap = new HashMap<>();
        for (int i = 0; i < arms_show_tj_query_field_lists.size(); i++) {
             queryTjParamMap = arms_show_tj_query_field_lists.get(i);
        }



        return queryTjParamMap;
    }

    /**
     * 从自定义查询页面的查询字段
     */
    public List<Map<String, String>> getcustomQueryFieldMap(RequestBean requestBean) {
        //组装自定义展现字段查询字段
        List<Map<String, String>> arms_show_query_custom_lists = (List<Map<String, String>>) ((Map) requestBean.getParameterList().get(0)).get("custom_cuery_field_value");
        return arms_show_query_custom_lists;
    }


    /**
     * 从其他查询字段
     */
    public Map<String, String> getOtherQueryFieldMap(RequestBean requestBean) {
        //其他查询字段
        Map<String, String> otherQueryFieldMap = new HashMap<>();

        return otherQueryFieldMap;
    }


    /**
     * 组装查询SQL
     *
     * @param requestBean
     * @param responseBean
     * @param fields
     * @param tableName
     * @param queryTjParamMap
     * @param customQueryFieldMap
     * @param otherQueryFieldMap
     * @return
     * @throws Exception
     */
    public String getQueryModelDataSql(RequestBean requestBean,
                                       ResponseBean responseBean,
                                       String fields,
                                       String tableName,
                                       Map<String, String> queryTjParamMap,
                                       List<Map<String, String>> customQueryFieldMap,
                                       Map<String, String> otherQueryFieldMap,
                                       List<Map<String, String>> relateFieldLists,
                                       String history_presentOrHis)
            throws Exception {


        StringBuffer querySql = new StringBuffer();
        //查询字段值  暂时不考虑格式化问题
        querySql.append("SELECT ");
        querySql.append(arrangeFields(fields));
        querySql.append(" , CREATE_DATE, BUSI_DATA_DATE,MONITOR_ORGAN,ENTRY_ID,RELATE_EXHIBITID,ENTRYROW_ID,RELATE_ENTRYROWID,ISHANDLE,FORMTYPE,MONITORID,DECODE(ISHANDLE,'99',CHECKER_NO,'') CHECKER_NO,ALERT_DATE,ALERT_CONTENT,HANDLE_ORGAN,OPERATOR_NO,RISK_ORGAN,END_DATE,\n");
        querySql.append(" DEAL_ORGAN,DEAL_STATE,DEAL_INFO,DECODE(CHECK_STATE, '0', '未阅', '1','已阅') CHECK_STATE,CREATE_DATE CREATE_DATE_NEW \n ");
        querySql.append(" FROM ");
        //querySql.append(tableName + " A ");
        if ("0".equals(history_presentOrHis)) {
			querySql.append(tableName + " A ");
		} else {
			querySql.append(tableName + "_HIS A ");
		}
        
        //拼装统计过滤条件(统计跳转传递参数)
        String statisticsCondition = getStatisticsCondition(queryTjParamMap);

        //必带的过滤字段(传递必要查询字段条件)
        StringBuffer mustCondition = new StringBuffer();
        String loginUserNo = BaseUtil.getLoginUser().getUserNo();
        String loginOrganNo = BaseUtil.getLoginUser().getOrganNo();
        if(((String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG")).equals("0")) {
        	mustCondition.append(
        			" where EXISTS (select 1 from sm_organ_tb sot, sm_organ_parent_tb sopt where exists (select 1 from sm_users_tb sut where sut.user_no = '"
        					+ loginUserNo
        					+ "' and sot.organ_no = sut.organ_no) and sot.organ_no = sopt.parent_organ and sopt.organ_no=A.BANKCODE) ");
        }else {
        	mustCondition.append(" WHERE  EXISTS (SELECT 1 FROM SM_USER_ORGAN_TB O WHERE O.ORGAN_NO=A.BANKCODE AND O.USER_NO = '" + loginUserNo + "') ");
        }
  
        String list_flag = ParamUtil.getParamValue(requestBean, "list_flag");
        if (!StringUtil.checkNull(list_flag))//是否是明细查询
            mustCondition.append(" AND A.LIST_FLAG = '").append(list_flag).append("'");

        String is_prompt_type = ParamUtil.getParamValue(requestBean, "isPrompt_type");
        if (!StringUtil.checkNull(is_prompt_type))
            mustCondition.append(" AND A.CHECK_STATE = '").append(is_prompt_type).append("'");

        String ishandleCondition = "";
        String ishandle = ParamUtil.getParamValue(requestBean, "ishandle");
        if (!StringUtil.checkNull(ishandle)) {//处理字段过滤
            if (ishandle.indexOf("|") > -1) {
                ishandleCondition = "  A.ISHANDLE IN(" + ishandle.replace("|", ",") + ")";
            } else {
                ishandleCondition = "  A.ISHANDLE = " + ishandle + "";
            }
        }
        
        String transmitCondition = "";
        String transmit = ParamUtil.getParamValue(requestBean, "transmit");
        if (!StringUtil.checkNull(transmit)) {//可下发过滤
            if ("1".equals(transmit)) {  //可下发
//                transmitCondition = "  A.HANDLE_ORGAN <> A.DEAL_ORGAN ";
                transmitCondition = "  A.HANDLE_ORGAN=A.DEAL_ORGAN AND HANDLE_ORGAN = '" + loginOrganNo + "' ";
            }
//            else if("2".equals(transmit)){
//                transmitCondition = "  A.HANDLE_ORGAN=A.DEAL_ORGAN AND HANDLE_ORGAN = '"+loginOrganNo+"' ";
//            }
        }
        querySql.append(mustCondition);
        
        //组装页面查询条件
        String customCondition = getCustomCondition(customQueryFieldMap);
        if (!StringUtil.checkNull(customCondition)) {
            customCondition = customCondition.substring(0, customCondition.length() - 4);
            querySql.append(" AND ").append(customCondition);
        }
        //组装关联模型查询条件
        String relateCondition = getRelateCondition(relateFieldLists);
        if (!StringUtil.checkNull(relateCondition)) {
        	relateCondition = relateCondition.substring(0, relateCondition.length() - 4);
            querySql.append(" AND ").append(relateCondition);
        }
        
        if (!StringUtil.checkNull(statisticsCondition))
            querySql.append(" AND ").append(statisticsCondition);
        if (!StringUtil.checkNull(ishandleCondition))
            querySql.append(" AND ").append(ishandleCondition);
        if (!StringUtil.checkNull(transmitCondition))
            querySql.append(" AND ").append(transmitCondition);

        return querySql.toString();
    }

    /**
     * 历史数据
     *
     * @param requestBean
     * @param responseBean
     * @param fields
     * @param tableName
     * @param queryTjParamMap
     * @param customQueryFieldMap
     * @param otherQueryFieldMap
     * @return
     */
    public String getQueryModelHistoryDataSql(RequestBean requestBean, ResponseBean responseBean,
                                              String fields, String tableName,
                                              Map<String, String> queryTjParamMap,
                                              List<Map<String, String>> customQueryFieldMap,
                                              Map<String, String> otherQueryFieldMap,String history_presentOrHis) throws Exception {


        StringBuffer querySql = new StringBuffer();
        //查询字段值  暂时不考虑格式化问题
        querySql.append("SELECT ");
        querySql.append(arrangeFields(fields));
        querySql.append(",CHECK_INFO , CREATE_DATE, BUSI_DATA_DATE,A.MONITOR_ORGAN,A.ENTRY_ID,A.RELATE_EXHIBITID,A.ENTRYROW_ID,A.RELATE_ENTRYROWID,A.ISHANDLE,A.FORMTYPE,A.MONITORID,DECODE(A.ISHANDLE,'99',A.CHECKER_NO,'') CHECKER_NO,A.ALERT_DATE,A.ALERT_CONTENT,A.HANDLE_ORGAN,A.OPERATOR_NO,A.RISK_ORGAN,A.END_DATE,\n");
        querySql.append(" A.DEAL_ORGAN,A.DEAL_STATE,A.DEAL_INFO,DECODE(A.CHECK_STATE, '0', '未阅', '1','已阅') CHECK_STATE,CREATE_DATE CREATE_DATE_NEW , ");
        querySql.append(" S.RISK_SOURCE_TYPE,S.RISK_SOURCE_NAME,DECODE(A.ISHANDLE, '99','已撤销', '0','进行中', '-1',B.NODE_NAME,'其他') FORMTYPE1, '").append(history_presentOrHis).append("' IS_HIS");
        querySql.append(" FROM ");

		if ("0".equals(history_presentOrHis)) {
			querySql.append(tableName + " A ");
		} else {
			querySql.append(tableName + "_HIS A ");
		}
            

        querySql.append(" LEFT JOIN ET_BUSIFORM_TB S ON A.MONITORID = S.FORM_ID LEFT JOIN ARMS_WORKFLOW_NODE B ON S.NODE_NO = B.NODE_CODE AND S.FORM_TYPE = B.FORM_TYPE ");


        //拼装统计过滤条件
        String statisticsCondition = getStatisticsCondition(queryTjParamMap);
        //必带的过滤字段
        StringBuffer mustCondition = new StringBuffer();
        String loginUserNo = BaseUtil.getLoginUser().getUserNo();
        if(((String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG")).equals("0")) {
        	mustCondition.append(
        			" where EXISTS (select 1 from sm_organ_tb sot, sm_organ_parent_tb sopt where exists (select 1 from sm_users_tb sut where sut.user_no = '"
        					+ loginUserNo
        					+ "' and sot.organ_no = sut.organ_no) and sot.organ_no = sopt.parent_organ and sopt.organ_no=A.BANKCODE) ");
        }else {
        	mustCondition.append(" WHERE  EXISTS (SELECT 1 FROM SM_USER_ORGAN_TB O WHERE O.ORGAN_NO=A.BANKCODE AND O.USER_NO = '" + loginUserNo + "') ");
        }
        
        //是否是明细查询
        String list_flag = ParamUtil.getParamValue(requestBean, "list_flag");
        String modelLevel = ParamUtil.getParamValue(requestBean, "modelLevel");
        if (!StringUtil.checkNull(list_flag))
            mustCondition.append(" AND A.LIST_FLAG = '").append(list_flag).append("'");
        String is_prompt_type = ParamUtil.getParamValue(requestBean, "isPrompt_type");
        if (!StringUtil.checkNull(is_prompt_type))
            mustCondition.append(" AND A.CHECK_STATE = '").append(is_prompt_type).append("'");
        //处理字段过滤  历史查询
        String ishandleCondition = "";
        if("4".equals(modelLevel)){
             ishandleCondition  = " (A.ISHANDLE='99' OR A.ISHANDLE='-1') ";
        } else {
             ishandleCondition = " (A.ISHANDLE='99' OR A.ISHANDLE='-1' OR A.DEAL_STATE='1') ";
        }
        querySql.append(mustCondition);
        String customCondition = getCustomCondition(customQueryFieldMap);
        //页面展现字段SQL
        if (!StringUtil.checkNull(customCondition)) {
            customCondition = customCondition.substring(0, customCondition.length() - 4);
            querySql.append(" AND ").append(customCondition);
        }
        if (!StringUtil.checkNull(statisticsCondition))
            querySql.append(" AND ").append(statisticsCondition);
        if (!StringUtil.checkNull(ishandleCondition))
            querySql.append(" AND ").append(ishandleCondition);

        return querySql.toString();
    }


    public String getCustomCondition(List<Map<String, String>> customQueryFieldMap) {
        StringBuffer customCondition = new StringBuffer();
        if (customQueryFieldMap != null && customQueryFieldMap.size() > 0) {
            for (int i = 0; i < customQueryFieldMap.size(); i++) {
                Map<String, String> field = customQueryFieldMap.get(i);
                String fieldName = field.get("fieldName");
                String fieldType = field.get("fieldType");
                String fieldValue = field.get("fieldValue");
                //字段有值则加入SQL
                if (!StringUtil.checkNull(fieldValue)) {
                    if ("12345".contains(fieldType)) {
                        customCondition.append(fieldName).append(ARMSConstants.OPERATOR_MAPS.get(fieldType)).append("'").append(fieldValue).append("' AND ");
                    } else if ("6".contains(fieldType)) {
                        customCondition.append(fieldName).append(ARMSConstants.OPERATOR_MAPS.get(fieldType)).append("('").append(fieldValue).append("') AND ");
                    } else {
                        customCondition.append(fieldName).append(ARMSConstants.OPERATOR_MAPS.get(fieldType)).append(" '%").append(fieldValue).append("%' AND ");
                    }
                }


            }
        }

        return customCondition.toString();
    }


    /**
     * 从统计页面的查询条件
     *
     * @param queryTjParamMap
     * @return
     */
    public String getStatisticsCondition(Map<String, String> queryTjParamMap) {
        StringBuffer statisticsCondition = new StringBuffer();

        if (!StringUtil.checkNull(queryTjParamMap.get("branch_organ"))) { //分行级别
            statisticsCondition.append(" AND A.BANKCODE IN(SELECT ORGAN_NO FROM SM_ORGAN_PARENT_TB WHERE PARENT_ORGAN = '" + queryTjParamMap.get("branch_organ") + "') ");
        }
        if (!StringUtil.checkNull(queryTjParamMap.get("query_busi_organ"))) { //选择机构
        	String busiOrganStr = "";
        	String[] busiOrganArray = queryTjParamMap.get("query_busi_organ").split(",");
			for (int i = 0; i < busiOrganArray.length; i++) {
				busiOrganStr += ",'" + busiOrganArray[i] + "'";
			}
            statisticsCondition.append(" AND A.BANKCODE IN (").append(busiOrganStr.substring(1)).append(") ");
        }
        if (!StringUtil.checkNull(queryTjParamMap.get("armsStartDate")) && !StringUtil.checkNull(queryTjParamMap.get("armsEndDate"))) { //开始日期
            statisticsCondition.append(" AND A.PROC_DATE BETWEEN '" + queryTjParamMap.get("armsStartDate") + "' AND '" + queryTjParamMap.get("armsEndDate") + "' ");
        }

        if(!StringUtil.checkNull(queryTjParamMap.get("startCreateDay"))){
            statisticsCondition.append(" AND A.CREATE_DATE >='").append(queryTjParamMap.get("startCreateDay")+"000000").append("' ");
        }
        if(!StringUtil.checkNull(queryTjParamMap.get("endCreateDay"))){
            statisticsCondition.append(" AND A.CREATE_DATE <='").append(queryTjParamMap.get("endCreateDay")+"999999").append("' ");
        }
        if(!StringUtil.checkNull(queryTjParamMap.get("startAlertDay"))){
            statisticsCondition.append(" AND A.ALERT_DATE >= '").append(queryTjParamMap.get("startAlertDay")).append("' ");
        }
        if(!StringUtil.checkNull(queryTjParamMap.get("endAlertDay"))){
            statisticsCondition.append(" AND A.ALERT_DATE <= '").append(queryTjParamMap.get("endAlertDay")).append("' ");
        }
        if(!StringUtil.checkNull(queryTjParamMap.get("history_formType"))){
            statisticsCondition.append(" AND A.FORMTYPE = '").append(queryTjParamMap.get("history_formType")).append("' ");
        }
        if(!StringUtil.checkNull(queryTjParamMap.get("history_monitorId"))){
            statisticsCondition.append(" AND A.MONITORID LIKE '%").append(queryTjParamMap.get("history_monitorId")).append("%' ");
        }


        String datestart = "";
        String dateend = "";
        String years = queryTjParamMap.get("year");
        String quarters = queryTjParamMap.get("quarters");
        if (!StringUtil.checkNull(years)){
            if ("".equals(quarters)) {
                datestart = years + "0101";
                dateend = years + "1231";
            } else if ("1".equals(quarters)) {
                datestart = years + "0101";
                dateend = years + "0331";
            } else if ("2".equals(quarters)) {
                datestart = years + "0401";
                dateend = years + "0630";
            } else if ("3".equals(quarters)) {
                datestart = years + "0701";
                dateend = years + "0930";
            } else if ("4".equals(quarters)) {
                datestart = years + "1001";
                dateend = years + "1231";
            }
            statisticsCondition.append(" AND A.PROC_DATE BETWEEN '").append(datestart).append("' AND '").append(dateend).append("' ");
        }
        String returnStr = statisticsCondition.toString();
        if(!StringUtil.checkNull(returnStr)){
        	returnStr = returnStr.substring(4);
        }
        return returnStr;
    }

    /**
     * 删除案例库
     */
    
    public void eWICDetailDelete(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        // 获取参数集合
        Map sysMap = requestBean.getSysMap();
       
        String tableName = (String) sysMap.get("tableName");
        int delFlag=0;
        List<Map> delList= (List) sysMap.get("delList");
        for(int i=0;i<delList.size();i=i+2){
        	Map delEntryIdMap=delList.get(i);
        	String entryId = (String)delEntryIdMap.get("ENTRY_ID");
        	
        	Map delEntryRowIdMap=delList.get(i+1);
    	//	int entryRowId = (int)delEntryRowIdMap.get("ENTRYROW_ID");	
        	String entryRowId = (String)delEntryRowIdMap.get("ENTRYROW_ID");
    		/*String querySql = getDeleteEWICDetailDataSql(tableName,entryId, entryRowId);
            delFlag = modelDataQueryMapper.eWICDetailDelete(querySql);*/
        	 String loginUserNo = BaseUtil.getLoginUser().getUserNo();
            delFlag=modelDataQueryMapper.eWICDetailDelete(entryId,entryRowId,loginUserNo);
        }
		
        if(delFlag>0){
        	responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        	responseBean.setRetMsg("删除成功！");
        }else{
        	responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
        	responseBean.setRetMsg("删除失败！");
        }
    }
	    /**
	     * 组装删除sql
	     */
 /*   public String getDeleteEWICDetailDataSql(String tableName,String entryId,String entryRowId) throws Exception {
         String loginUserNo = BaseUtil.getLoginUser().getUserNo();
        StringBuffer querySql = new StringBuffer();
		querySql.append("DELETE ");
        querySql.append(" FROM ");
        querySql.append(" ARMS_FAVORITE_TB ");
		querySql.append(" where ENTRY_ID='"+entryId+"' AND ENTRYROW_ID = "+entryRowId+" AND FAVORITE_USER='"+loginUserNo+"'");
		return querySql.toString();
		}*/
    
    /**
     * 获得案例库的详细数据
     */
    
    public void getModelDetailData(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        // 获取参数集合
        Map sysMap = requestBean.getSysMap();
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
        String tableName = ParamUtil.getParamValue(requestBean, "tableName");
        String eWIC_show_model_query = ParamUtil.getParamValue(requestBean, "eWIC_show_model_query");
        String entryId = ParamUtil.getParamValue(requestBean, "modelId");
        String querySql = getQueryModelDetailDataSql(eWIC_show_model_query, tableName,entryId);
        Page page = PageHelper.startPage(currentPage, pageSize);
        
        List<Map<String, Object>> datas = modelDataQueryMapper.getModelData(querySql);
        
        long totalCount = page.getTotal();
        Map retMap = new HashMap();
        retMap.put("currentPage", currentPage);
        retMap.put("pageNum", pageSize);
        retMap.put("totalNum", totalCount);
        retMap.put("modelDatas", datas);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
    }
    /**
     * 组装查询案例库数据的sql
     */
    public String getQueryModelDetailDataSql(String eWIC_show_model_query,String tableName,String entryId) throws Exception {
    	StringBuffer querySql = new StringBuffer();
    	StringBuffer fields=new StringBuffer();
    	String[] eWIC_show_model_query_array = eWIC_show_model_query.split(",");//把前台传过来的查询字段重组
    	for(String eWIC_show_model_query_str:eWIC_show_model_query_array){
    		fields.append("T2." + eWIC_show_model_query_str + ",");
    	}
    	int favoriteType=0;//单据类型,0是警报信息
        String loginUserNo = BaseUtil.getLoginUser().getUserNo();
        String loginOrganNo = BaseUtil.getLoginUser().getOrganNo();
        if("9999".equals(loginOrganNo)){//9999代表总行机构
            querySql.append("SELECT ");
            querySql.append(arrangeFields(fields.toString()));
            /*querySql.append(" T1.ID,T1.FAVORITE_TITLE, T1.ENTRYROW_ID, T1.REMARK REMARK_MANUAL, T1.REMARK_COMMON, T1.ISCOMMON, T2.MONITORID, T2.FORMTYPE, T2.ENTRY_ID ENTYR_ID_2 FROM ARMS_FAVORITE_TB T1 LEFT JOIN ");
            querySql.append(tableName);
            querySql.append(" T2 ON T1.ENTRY_ID = T2.ENTRY_ID AND T1.ENTRYROW_ID = T2.ENTRYROW_ID WHERE T1.TYPE = '" + favoriteType + "' AND T1.ENTRY_ID = '" + entryId + "' ORDER BY T2.HANDLE_ORGAN, T2.BANKCODE, T2.CREATE_DATE DESC, T2.ENTRYROW_ID DESC");*/
            querySql.append(" T1.ID,T1.FAVORITE_USER,T1.FAVORITE_TITLE, T1.ENTRYROW_ID, T1.REMARK REMARK_MANUAL, T1.REMARK_COMMON, T1.ISCOMMON, T2.MONITORID, T2.FORMTYPE, T2.ENTRY_ID ENTRY_ID_TMP FROM ARMS_FAVORITE_TB T1 , ");
            querySql.append(tableName);
            querySql.append(" T2 where T1.ENTRY_ID = T2.ENTRY_ID AND T1.ENTRYROW_ID = T2.ENTRYROW_ID AND T1.TYPE = '" + favoriteType + "' AND T1.ENTRY_ID = '" + entryId + "' ORDER BY T2.HANDLE_ORGAN, T2.BANKCODE, T2.CREATE_DATE DESC, T2.ENTRYROW_ID DESC");
        } else{
            querySql.append("SELECT ");
            querySql.append(eWIC_show_model_query);
            /*querySql.append(" ,T1.ID,T1.FAVORITE_TITLE, T1.ENTRYROW_ID, T1.REMARK REMARK_MANUAL, T1.REMARK_COMMON, T1.ISCOMMON, T2.MONITORID, T2.FORMTYPE, T2.ENTRY_ID ENTYR_ID_2 FROM ARMS_FAVORITE_TB T1 LEFT JOIN ");
            querySql.append(tableName);
            querySql.append(" T2 ON T1.ENTRY_ID = T2.ENTRY_ID AND T1.ENTRYROW_ID = T2.ENTRYROW_ID WHERE T1.TYPE = '" + favoriteType + "'  AND (T1.FAVORITE_ORGAN = '" + loginOrganNo + "' OR T1.ISCOMMON='1') AND T1.ENTRY_ID = '" + entryId + "' ORDER BY T2.HANDLE_ORGAN, T2.BANKCODE, T2.CREATE_DATE DESC, T2.ENTRYROW_ID DESC");*/
            querySql.append(" ,T1.ID,T1.FAVORITE_USER,T1.FAVORITE_TITLE, T1.ENTRYROW_ID, T1.REMARK REMARK_MANUAL, T1.REMARK_COMMON, T1.ISCOMMON, T2.MONITORID, T2.FORMTYPE, T2.ENTRY_ID ENTRY_ID_TMP FROM ARMS_FAVORITE_TB T1 , ");
            querySql.append(tableName);
            querySql.append(" T2 where T1.ENTRY_ID = T2.ENTRY_ID AND T1.ENTRYROW_ID = T2.ENTRYROW_ID AND T1.TYPE = '" + favoriteType + "'  AND (T1.FAVORITE_ORGAN = '" + loginOrganNo + "' OR T1.ISCOMMON='1') AND T1.ENTRY_ID = '" + entryId + "' ORDER BY T2.HANDLE_ORGAN, T2.BANKCODE, T2.CREATE_DATE DESC, T2.ENTRYROW_ID DESC");

        }
		return querySql.toString();
		}
    
    
    public void eWICDetailSetCommon(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        // 获取参数集合
        Map sysMap = requestBean.getSysMap();
       
        String eWICDetailSetRemarkCommonTextareaVal = (String) sysMap.get("eWICDetailSetRemarkCommonTextareaVal");
        int delFlag=0;
        List<Map> eWICDetailSRCDList= (List) sysMap.get("eWICDetailSRCDList");
        for(int i=0;i<eWICDetailSRCDList.size();i++){
        	Map eWICDetailSRCDMap=eWICDetailSRCDList.get(i);
        	String armsFavoriteId = (String)eWICDetailSRCDMap.get("armsFavoriteId");
        	
    		/*String querySql = geteWICDetailSetCommonSql(eWICDetailSetRemarkCommonTextareaVal,armsFavoriteId);
            delFlag = modelDataQueryMapper.eWICDetailSetCommon(querySql);*/
        	delFlag = modelDataQueryMapper.eWICDetailSetCommon(eWICDetailSetRemarkCommonTextareaVal,armsFavoriteId);
        }		
        if(delFlag>0){
        	responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        	responseBean.setRetMsg("设置公有成功！");
        }else{
        	responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
        	responseBean.setRetMsg("设置公有失败！");
        }
    }
	    
   /* public String geteWICDetailSetCommonSql(String eWICDetailSetRemarkCommonTextareaVal,int armsFavoriteId) throws Exception {
         String loginUserNo = BaseUtil.getLoginUser().getUserNo();
        StringBuffer querySql = new StringBuffer();
		querySql.append("UPDATE ");
        querySql.append(" ARMS_FAVORITE_TB ");
		querySql.append(" SET REMARK_COMMON='"+eWICDetailSetRemarkCommonTextareaVal+"' , ISCOMMON = 1 WHERE ID = "+armsFavoriteId+"");
		return querySql.toString();
		}*/
    
    /**
     * 获取关联模型查询字段与取查询值
     * @param requestBean
     * @param responseBean
     */
    private void getRelateSearchField(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
    	//组装统计页面查询字段
        String modelId = ParamUtil.getParamValue(requestBean, "modelId");
        String modelRowId = ParamUtil.getParamValue(requestBean, "modelRowId");
        String relateId = ParamUtil.getParamValue(requestBean, "relateId");
        modelId = BaseUtil.filterSqlParam(modelId);
        modelRowId = BaseUtil.filterSqlParam(modelRowId);

        Model model = modelMapper.getModelInfo(Integer.parseInt(modelId));
        McModelLine searchCond = new McModelLine();
        searchCond.setId(Integer.parseInt(modelId));
        searchCond.setLineid(Integer.parseInt(relateId));
        List<McModelLine> relateFieldList = mcModelLineMapper.selectBySelective(searchCond);
        String fieldCondStr = "";
        for (McModelLine mcModelLine : relateFieldList) {
        	fieldCondStr += "," + mcModelLine.getModelfiled();
		}
        fieldCondStr = fieldCondStr.substring(1);
        List modelDataList = modelDataQueryMapper.selectByPrimaryKey(fieldCondStr,BaseUtil.filterSqlParam(model.getTableName()),Integer.parseInt(modelId),Integer.parseInt(modelRowId));
        if(modelDataList == null || modelDataList.size() == 0){
        	modelDataList = modelDataQueryMapper.selectByPrimaryKey(fieldCondStr,BaseUtil.filterSqlParam(model.getTableName()+"_HIS"),Integer.parseInt(modelId),Integer.parseInt(modelRowId));
        }
        if(modelDataList == null || modelDataList.size() == 0){
        	responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
        	responseBean.setRetMsg("预警数据有误！");
        	return;
        }
        List<String[]> relateModelValue = new ArrayList<String[]>();
        String[] fieldValue = null;
        for (McModelLine mcModelLine : relateFieldList) {
        	fieldValue = new String[2];
        	fieldValue[0] = mcModelLine.getLinefiled();
        	if(modelDataList.get(0) == null){
        		fieldValue[1] = "**##";//如果是空查询不出关联模型数据,传入无效数据
        	}else{
        		fieldValue[1] = ((Map)modelDataList.get(0)).get(mcModelLine.getModelfiled().toUpperCase()) + "";
        	}
        	relateModelValue.add(fieldValue);
		}
        Map retMap = new HashMap();
        retMap.put("relateModelValue", relateModelValue);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
    	responseBean.setRetMsg("查询成功！");
    }
    
    public String getRelateCondition(List<Map<String, String>> relateFieldLists) {
        StringBuffer relateCondition = new StringBuffer();
        if (relateFieldLists != null && relateFieldLists.size() > 0) {
            for (int i = 0; i < relateFieldLists.size(); i++) {
                Map<String, String> field = relateFieldLists.get(i);
                String fieldName = field.get("fieldName");
                String fieldValue = field.get("fieldValue");
                //字段有值则加入SQL
                if (!StringUtil.checkNull(fieldValue)) {
                	if("busi_data_date".equalsIgnoreCase(fieldName)){
                    	relateCondition.append(fieldName).append(ARMSConstants.OPERATOR_MAPS.get("1")).append("to_date('").append(fieldValue).append("','yyyymmdd'").append(") AND ");
                    }else{
                    	relateCondition.append(fieldName).append(ARMSConstants.OPERATOR_MAPS.get("1")).append("'").append(fieldValue).append("' AND ");
                    }
                }
            }
        }
        return relateCondition.toString();
    }
    
    /**
     * 获取指定条件的预警数据
     *
     * @param requestBean
     * @param responseBean
     */
    @SuppressWarnings("unchecked")
	public void getRelateModelData(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        // 获取参数集合
        Map sysMap = requestBean.getSysMap();
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

        //组装统计页面查询字段
        String tableName = ParamUtil.getParamValue(requestBean, "tableName");
        String fields = ParamUtil.getParamValue(requestBean, "arms_show_model_query");

        //统计查询字段
        Map<String, String> queryTjParamMap = getTjQueryMap(requestBean);
        //展现字段查询字段
        List<Map<String, String>> customQueryFieldMap = getcustomQueryFieldMap(requestBean);
        //其他查询字段
        //Map<String, String> otherQueryFieldMap = getOtherQueryFieldMap(requestBean);
        
        List<Map<String, String>> arms_relate_field_lists = (List<Map<String, String>>) ((Map) requestBean.getParameterList().get(0)).get("arms_relate_field_lists");

        StringBuffer his_query_sql_count = new StringBuffer();
        his_query_sql_count.append("SELECT COUNT(1) NUM FROM (");
        his_query_sql_count.append(getQueryModelRelateDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap,arms_relate_field_lists,"0"));
        his_query_sql_count.append(" UNION ALL ");
        his_query_sql_count.append(getQueryModelRelateDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap,arms_relate_field_lists,"1"));
        his_query_sql_count.append(")");
        Integer datas_count = modelDataQueryMapper.getModelDataCount(his_query_sql_count.toString());
        List<Map<String, Object>> datas = null;
        if(datas_count > 0){
            StringBuffer his_query_sql = new StringBuffer();
            //同查临时表和历史表
            his_query_sql.append("SELECT * FROM(SELECT TMP_PAGE.*,ROWNUM ROW_ID FROM(");
            his_query_sql.append(getQueryModelRelateDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, arms_relate_field_lists,"0"));
            his_query_sql.append(" UNION ALL ");
            his_query_sql.append(getQueryModelRelateDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap, arms_relate_field_lists,"1"));
            his_query_sql.append(")TMP_PAGE WHERE ROWNUM <= ").append(currentPage*pageSize).append(" ORDER BY TMP_PAGE.CREATE_DATE DESC,TMP_PAGE.HANDLE_ORGAN,TMP_PAGE.ENTRYROW_ID) WHERE ROW_ID > ").append((currentPage-1)*pageSize);
            datas = modelDataQueryMapper.getModelData(his_query_sql.toString());
        }

        Map retMap = new HashMap();
        retMap.put("currentPage", currentPage);
        retMap.put("pageNum", pageSize);
        retMap.put("totalNum", datas_count);
        retMap.put("modelDatas", datas);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
    }
    
    /**
     * 关联模型数据
     *
     * @param requestBean
     * @param responseBean
     * @param fields
     * @param tableName
     * @param queryTjParamMap
     * @param customQueryFieldMap
     * @param relateFieldLists
     * @return
     */
    public String getQueryModelRelateDataSql(RequestBean requestBean, ResponseBean responseBean,
                                              String fields, String tableName,
                                              Map<String, String> queryTjParamMap,
                                              List<Map<String, String>> customQueryFieldMap,
                                              List<Map<String, String>> relateFieldLists,String history_presentOrHis) throws Exception {
    	StringBuffer querySql = new StringBuffer();
        //查询字段值  暂时不考虑格式化问题
        querySql.append("SELECT ");
        querySql.append(arrangeFields(fields));
        querySql.append(" ,CREATE_DATE, BUSI_DATA_DATE,MONITOR_ORGAN,ENTRY_ID,RELATE_EXHIBITID,ENTRYROW_ID,RELATE_ENTRYROWID,ISHANDLE,FORMTYPE,MONITORID,DECODE(ISHANDLE,'99',CHECKER_NO,'') CHECKER_NO,ALERT_DATE,ALERT_CONTENT,HANDLE_ORGAN,OPERATOR_NO,RISK_ORGAN,END_DATE,");
        querySql.append(" DEAL_ORGAN,DEAL_STATE,DEAL_INFO,DECODE(CHECK_STATE, '0', '未阅', '1','已阅') CHECK_STATE,CREATE_DATE CREATE_DATE_NEW  ");
        querySql.append(" FROM ");
        if ("0".equals(history_presentOrHis)){
        	querySql.append(tableName + " A");
        }else{
            querySql.append(tableName + "_HIS A");
        }
               
        //拼装统计过滤条件(统计跳转传递参数)
        String statisticsCondition = getStatisticsCondition(queryTjParamMap);

        //必带的过滤字段(传递必要查询字段条件)
        StringBuffer mustCondition = new StringBuffer();
        String loginUserNo = BaseUtil.getLoginUser().getUserNo();
        String loginOrganNo = BaseUtil.getLoginUser().getOrganNo();
        if(((String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG")).equals("0")) {
        	mustCondition.append(
        			" where EXISTS (select 1 from sm_organ_tb sot, sm_organ_parent_tb sopt where exists (select 1 from sm_users_tb sut where sut.user_no = '"
        					+ loginUserNo
        					+ "' and sot.organ_no = sut.organ_no) and sot.organ_no = sopt.parent_organ and sopt.organ_no=A.BANKCODE) ");
        }else {
        	mustCondition.append(" WHERE  EXISTS (SELECT 1 FROM SM_USER_ORGAN_TB O WHERE O.ORGAN_NO=A.BANKCODE AND O.USER_NO = '" + loginUserNo + "') ");
        }
        String list_flag = ParamUtil.getParamValue(requestBean, "list_flag");
        if (!StringUtil.checkNull(list_flag))//是否是明细查询
            mustCondition.append(" AND A.LIST_FLAG = '").append(list_flag).append("'");

        String is_prompt_type = ParamUtil.getParamValue(requestBean, "isPrompt_type");
        if (!StringUtil.checkNull(is_prompt_type))
            mustCondition.append(" AND A.CHECK_STATE = '").append(is_prompt_type).append("'");
        
        querySql.append(mustCondition);
        
        //组装页面查询条件
        String customCondition = getCustomCondition(customQueryFieldMap);
        if (!StringUtil.checkNull(customCondition)) {
            customCondition = customCondition.substring(0, customCondition.length() - 4);
            querySql.append(" AND ").append(customCondition);
        }
        //组装关联模型查询条件
        String relateCondition = getRelateCondition(relateFieldLists);
        if (!StringUtil.checkNull(relateCondition)) {
        	relateCondition = relateCondition.substring(0, relateCondition.length() - 4);
            querySql.append(" AND ").append(relateCondition);
        }
        
        if (!StringUtil.checkNull(statisticsCondition))
            querySql.append(" AND ").append(statisticsCondition);
        String modelId = ParamUtil.getParamValue(requestBean, "modelId");
        if(!StringUtil.checkNull(modelId)){
        	querySql.append(" AND ENTRY_ID ='").append(modelId).append("'");
        }
        return querySql.toString();
    }
    
    /**
     * 获取所有明细模型
     * @param requestBean
     * @param responseBean
     */
    private void getRelateModelAllData(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
    	 //组装统计页面查询字段
        String tableName = ParamUtil.getParamValue(requestBean, "tableName");
        String fields = ParamUtil.getParamValue(requestBean, "arms_show_model_query");

        //统计查询字段
        Map<String, String> queryTjParamMap = getTjQueryMap(requestBean);
        //展现字段查询字段
        List<Map<String, String>> customQueryFieldMap = getcustomQueryFieldMap(requestBean);

        String querySql = getQueryRelateModelDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap);
        List<Map<String, Object>> datas = modelDataQueryMapper.getModelData(querySql);

        Map retMap = new HashMap();
        retMap.put("modelDatas", datas);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
	}
    
    /**
     * 组装查询SQL
     *
     * @param requestBean
     * @param responseBean
     * @param fields
     * @param tableName
     * @param queryTjParamMap
     * @param customQueryFieldMap
     * @return
     * @throws Exception
     */
    /**
     * @date 2020-03-26
     * 原来字段date类型现在改成varchar ，去掉to_char
     */
    public String getQueryRelateModelDataSql(RequestBean requestBean,
                                       ResponseBean responseBean,
                                       String fields,
                                       String tableName,
                                       Map<String, String> queryTjParamMap,
                                       List<Map<String, String>> customQueryFieldMap)throws Exception {
        StringBuffer querySql = new StringBuffer();
        //查询字段值  暂时不考虑格式化问题
        querySql.append("SELECT ");
        querySql.append(arrangeFields(fields));
        querySql.append(" ,CREATE_DATE CREATE_DATE,BUSI_DATA_DATE BUSI_DATA_DATE,MONITOR_ORGAN,ENTRY_ID,RELATE_EXHIBITID,ENTRYROW_ID,RELATE_ENTRYROWID,ISHANDLE,FORMTYPE,MONITORID,DECODE(ISHANDLE,'99',CHECKER_NO,'') CHECKER_NO,ALERT_DATE,ALERT_CONTENT,HANDLE_ORGAN,OPERATOR_NO,RISK_ORGAN,END_DATE,\n");
        querySql.append(" DEAL_ORGAN,DEAL_STATE,DEAL_INFO,DECODE(CHECK_STATE, '0', '未阅', '1','已阅') CHECK_STATE,CREATE_DATE CREATE_DATE_NEW \n ");
        querySql.append(" FROM ");
        querySql.append(tableName + " B ");
        
        //组装统计表过滤条件
        StringBuffer tongQuerySql = new StringBuffer("SELECT 1 FROM " + tableName + " A WHERE A.ENTRYROW_ID = B.RELATE_ENTRYROWID ");
        //必带的过滤字段(传递必要查询字段条件)
        String loginUserNo = BaseUtil.getLoginUser().getUserNo();
        String loginOrganNo = BaseUtil.getLoginUser().getOrganNo();
        if(((String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG")).equals("0")) {
        	tongQuerySql.append(
        			" and EXISTS (select 1 from sm_organ_tb sot, sm_organ_parent_tb sopt where exists (select 1 from sm_users_tb sut where sut.user_no = '"
        					+ loginUserNo
        					+ "' and sot.organ_no = sut.organ_no) and sot.organ_no = sopt.parent_organ and sopt.organ_no=A.BANKCODE) ");
        }else {
        	tongQuerySql.append(" AND EXISTS (SELECT 1 FROM SM_USER_ORGAN_TB O WHERE O.ORGAN_NO=A.BANKCODE AND O.USER_NO = '" + loginUserNo + "') ");
        }
        tongQuerySql.append(" AND A.LIST_FLAG = '0'");
        
        //拼装统计过滤条件(统计跳转传递参数)
        String statisticsCondition = getStatisticsCondition(queryTjParamMap);
        if (!StringUtil.checkNull(statisticsCondition)){
        	tongQuerySql.append(" AND " + statisticsCondition);
        }
        String ishandle = ParamUtil.getParamValue(requestBean, "ishandle");
        if (!StringUtil.checkNull(ishandle)) {//处理字段过滤
            if (ishandle.indexOf("|") > -1) {
            	tongQuerySql.append(" AND A.ISHANDLE IN(" + ishandle.replace("|", ",") + ")");
            } else {
            	tongQuerySql.append(" AND A.ISHANDLE = '" + ishandle + "'");
            }
        }
        //组装页面查询条件
        String customCondition = getCustomCondition(customQueryFieldMap);
        if (!StringUtil.checkNull(customCondition)) {
            customCondition = customCondition.substring(0, customCondition.length() - 4);
            tongQuerySql.append(" AND " + customCondition);
        }
        querySql.append("  WHERE EXISTS (" + tongQuerySql.toString() + ") AND B.LIST_FLAG = '1' ORDER BY B.RELATE_ENTRYROWID,B.PROC_DATE");
        return querySql.toString();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void getModelInfoByPKId(RequestBean requestBean, ResponseBean responseBean) throws Exception{
        String modelId = ParamUtil.getParamValue(requestBean, "modelId");
        Model model = modelMapper.getModelInfo(Integer.parseInt(modelId));
        Map retMap = new HashMap();
        retMap.put("model", model);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
    }
    
    /**
     * 获取历史警报关联数据
     * @param requestBean
     * @param responseBean
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void getHisRelatingModelDataToMany(RequestBean requestBean, ResponseBean responseBean) throws  Exception{
        Map paramMap = (Map)requestBean.getParameterList().get(0);

        // 获取参数集合
//        String modelId = ParamUtil.getParamValue(requestBean, "modelId");
//        List<Integer>  modelRowIds =  ( List<Integer>) ((Map) requestBean.getParameterList().get(0)).get("modelRowIds");
//        String relatingId = ParamUtil.getParamValue(requestBean, "relatingId");
        String modelId = (String)paramMap.get("modelId");
        List<Integer>  modelRowIds =  ( List<Integer>) paramMap.get("modelRowIds");
        Integer relatingId = (Integer) paramMap.get("relatingId");

        Model model = modelMapper.getModelInfo(relatingId);
        List<HashMap<String,Object>> modeldatas =   modelDataQueryMapper.getHisRelatingModelDataToMany(BaseUtil.filterSqlParam(model.getTableName()),modelId,modelRowIds);
        Map retMap = new HashMap();
        retMap.put("modeldatas", modeldatas);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
    }
    
    /**
     * 获取所有历史明细模型
     * @param requestBean
     * @param responseBean
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void getHisRelateModelAllData(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// TODO Auto-generated method stub
    	//组装统计页面查询字段
        String tableName = ParamUtil.getParamValue(requestBean, "tableName");
        String fields = ParamUtil.getParamValue(requestBean, "arms_show_model_query");
        //统计查询字段
        Map<String, String> queryTjParamMap = getTjQueryMap(requestBean);
        //展现字段查询字段
        List<Map<String, String>> customQueryFieldMap = getcustomQueryFieldMap(requestBean);

        StringBuffer his_query_sql = new StringBuffer();
        //同查临时表和历史表
        his_query_sql.append("SELECT TMP_PAGE.* FROM (");
        his_query_sql.append(getQueryHisRelateModelDataSql(requestBean, responseBean, fields, tableName, queryTjParamMap, customQueryFieldMap));
        his_query_sql.append(" UNION ALL ");
        his_query_sql.append(getQueryHisRelateModelDataSql(requestBean, responseBean, fields, tableName+"_HIS", queryTjParamMap, customQueryFieldMap));
        his_query_sql.append(")TMP_PAGE ").append(" ORDER BY TMP_PAGE.CREATE_DATE DESC,TMP_PAGE.HANDLE_ORGAN,TMP_PAGE.RELATE_ENTRYROWID");

        List<Map<String, Object>> datas = modelDataQueryMapper.getModelData(his_query_sql.toString());

        Map retMap = new HashMap();
        retMap.put("modelDatas", datas);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
	}

    /**
     * date字段变成了varchar 修改  to_char 去掉
      */
    public String getQueryHisRelateModelDataSql(RequestBean requestBean,ResponseBean responseBean,
            String fields,String tableName,Map<String, String> queryTjParamMap,List<Map<String, String>> customQueryFieldMap)throws Exception {
		StringBuffer querySql = new StringBuffer();
		//查询字段值  暂时不考虑格式化问题
		querySql.append("SELECT ");
		querySql.append(arrangeFields(fields));
		querySql.append(" ,CREATE_DATE CREATE_DATE,BUSI_DATA_DATE BUSI_DATA_DATE,MONITOR_ORGAN,ENTRY_ID,RELATE_EXHIBITID,ENTRYROW_ID,ISHANDLE,FORMTYPE,MONITORID,DECODE(ISHANDLE,'99',CHECKER_NO,'') CHECKER_NO,ALERT_DATE,ALERT_CONTENT,HANDLE_ORGAN,OPERATOR_NO,RISK_ORGAN,END_DATE,\n");
		querySql.append(" DEAL_ORGAN,DEAL_STATE,DEAL_INFO,DECODE(CHECK_STATE, '0', '未阅', '1','已阅') CHECK_STATE,CREATE_DATE CREATE_DATE_NEW \n ");
		querySql.append(" FROM ");
		querySql.append(tableName + " B ");
		
		//组装统计表过滤条件
		StringBuffer tongQuerySql = new StringBuffer("SELECT 1 FROM " + tableName + " A WHERE A.ENTRYROW_ID = B.RELATE_ENTRYROWID ");
		//必带的过滤字段(传递必要查询字段条件)
		String loginUserNo = BaseUtil.getLoginUser().getUserNo();
		 if(((String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG")).equals("0")) {
			 tongQuerySql.append(
					 " AND EXISTS (select 1 from sm_organ_tb sot, sm_organ_parent_tb sopt where exists (select 1 from sm_users_tb sut where sut.user_no = '"
							 + loginUserNo
							 + "' and sot.organ_no = sut.organ_no) and sot.organ_no = sopt.parent_organ and sopt.organ_no=A.BANKCODE) ");
	        }else {
	        	tongQuerySql.append(" AND EXISTS (SELECT 1 FROM SM_USER_ORGAN_TB O WHERE O.ORGAN_NO=A.BANKCODE AND O.USER_NO = '" + loginUserNo + "') ");
	        }
		tongQuerySql.append(" AND A.LIST_FLAG = '0'");
		String is_prompt_type = ParamUtil.getParamValue(requestBean, "isPrompt_type");
		if (!StringUtil.checkNull(is_prompt_type)){
			tongQuerySql.append(" AND A.CHECK_STATE = '").append(is_prompt_type).append("'");
		}
		
		String modelLevel = ParamUtil.getParamValue(requestBean, "modelLevel");
		if("4".equals(modelLevel)){
			tongQuerySql.append(" AND (A.ISHANDLE='99' OR A.ISHANDLE='-1') ");
       } else {
    	   tongQuerySql.append(" AND (A.ISHANDLE='99' OR A.ISHANDLE='-1' OR A.DEAL_STATE='1') ");
       }
		//拼装统计过滤条件(统计跳转传递参数)
		String statisticsCondition = getStatisticsCondition(queryTjParamMap);
		if (!StringUtil.checkNull(statisticsCondition)){
			tongQuerySql.append(" AND " + statisticsCondition);
		}
		//组装页面查询条件
		String customCondition = getCustomCondition(customQueryFieldMap);
		if (!StringUtil.checkNull(customCondition)) {
			customCondition = customCondition.substring(0, customCondition.length() - 4);
			tongQuerySql.append(" AND " + customCondition);
		}
		querySql.append(" WHERE EXISTS (" + tongQuerySql.toString() + ") AND B.LIST_FLAG = '1' ");
		return querySql.toString();
	}

    /**
     * 整理展现字段,去除系统中已设置的默认查询字段
     * @param fields
     * @return
     */
	public String arrangeFields(String fields){
        fields = ","+fields+",";
        fields =  fields.replace(",CREATE_DATE,",",");
        fields =  fields.replace(",BUSI_DATA_DATE,",",");
        fields =  fields.replace(",MONITOR_ORGAN,",",");
        fields =  fields.replace(",ENTRY_ID,","");
        fields =  fields.replace(",RELATE_EXHIBITID,",",");
        fields =  fields.replace(",ENTRYROW_ID,",",");
        fields =  fields.replace(",ISHANDLE,",",");
        fields =  fields.replace(",FORMTYPE,",",");
        fields =  fields.replace(",CHECKER_NO,",",");
        fields =  fields.replace(",ALERT_CONTENT,",",");
        fields =  fields.replace(",HANDLE_ORGAN,",",");
        fields =  fields.replace(",OPERATOR_NO,",",");
        fields =  fields.replace(",RISK_ORGAN,",",");
        fields =  fields.replace(",END_DATE,",",");
        fields =  fields.replace(",DEAL_ORGAN,",",");
        fields =  fields.replace(",DEAL_STATE,",",");
        fields =  fields.replace(",DEAL_INFO,",",");
        fields =  fields.replace(",CHECK_STATE,",",");



        fields =  fields.replace(",A.CREATE_DATE,",",");
        fields =  fields.replace(",A.BUSI_DATA_DATE,",",");
        fields =  fields.replace(",A.MONITOR_ORGAN,",",");
        fields =  fields.replace(",A.ENTRY_ID,","");
        fields =  fields.replace(",A.RELATE_EXHIBITID,",",");
        fields =  fields.replace(",A.ENTRYROW_ID,",",");
        fields =  fields.replace(",A.ISHANDLE,",",");
        fields =  fields.replace(",A.FORMTYPE,",",");
        fields =  fields.replace(",A.CHECKER_NO,",",");
        fields =  fields.replace(",A.ALERT_CONTENT,",",");
        fields =  fields.replace(",A.HANDLE_ORGAN,",",");
        fields =  fields.replace(",A.OPERATOR_NO,",",");
        fields =  fields.replace(",A.RISK_ORGAN,",",");
        fields =  fields.replace(",A.END_DATE,",",");
        fields =  fields.replace(",A.DEAL_ORGAN,",",");
        fields =  fields.replace(",A.DEAL_STATE,",",");
        fields =  fields.replace(",A.DEAL_INFO,",",");
        fields =  fields.replace(",A.CHECK_STATE,",",");


        fields =  fields.replace(",T2.CREATE_DATE,",",");
        fields =  fields.replace(",T2.BUSI_DATA_DATE,",",");
        fields =  fields.replace(",T2.MONITOR_ORGAN,",",");
        fields =  fields.replace(",T2.ENTRY_ID,","");
        fields =  fields.replace(",T2.RELATE_EXHIBITID,",",");
        fields =  fields.replace(",T2.ENTRYROW_ID,",",");
        fields =  fields.replace(",T2.ISHANDLE,",",");
        fields =  fields.replace(",T2.FORMTYPE,",",");
        fields =  fields.replace(",T2.CHECKER_NO,",",");
        fields =  fields.replace(",T2.ALERT_CONTENT,",",");
        fields =  fields.replace(",T2.HANDLE_ORGAN,",",");
        fields =  fields.replace(",T2.OPERATOR_NO,",",");
        fields =  fields.replace(",T2.RISK_ORGAN,",",");
        fields =  fields.replace(",T2.END_DATE,",",");
        fields =  fields.replace(",T2.DEAL_ORGAN,",",");
        fields =  fields.replace(",T2.DEAL_STATE,",",");
        fields =  fields.replace(",T2.DEAL_INFO,",",");
        fields =  fields.replace(",T2.CHECK_STATE,",",");

        fields= fields.substring(1,fields.length()-1);

        return fields;
    }

}
