package com.sunyard.ars.risk.service.impl.arms;


import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.util.DateUtil;
import com.sunyard.ars.common.util.StringUtil;
import com.sunyard.ars.risk.bean.arms.ArmsDataStatistics;
import com.sunyard.ars.risk.dao.arms.ArmsDataStatisticsMapper;
import com.sunyard.ars.risk.service.arms.IArmsDataStatisticsService;
import com.sunyard.ars.system.dao.mc.McTableMapper;
import com.sunyard.ars.system.dao.mc.ModelMapper;
import com.sunyard.ars.system.pojo.mc.Model;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("armsDataStatisticsService")
@Transactional
public class ArmsDataStatisticsService extends BaseService implements IArmsDataStatisticsService {

    @Resource
    private ArmsDataStatisticsMapper armsDataStatisticsMapper;
    @Resource
	private ModelMapper modelMapper;
    @Resource
	private McTableMapper mcTableMapper; 

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

        if (ARSConstants.OPERATE_QUERY.equals(oper_type)) {
            // 查询统计信息
            getMonitorData(requestBean, responseBean);
        }else if("promptStatisticsQuery".equals(oper_type)){
            promptStatisticsQuery(requestBean, responseBean);
        }else if("relateStatisticsQuery".equals(oper_type)){//关联模型统计查询
        	relateStatisticsQuery(requestBean, responseBean);
        }else if("refreshArmsCount".equals(oper_type)){//刷新模型统计信息
        	refreshArmsCount(requestBean, responseBean);
        }
    }

    /**
     * 刷新模型统计信息
     * @param requestBean
     * @param responseBean
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void refreshArmsCount(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
    	// 前台参数集合
        ArmsDataStatistics armsDataStatistics = (ArmsDataStatistics) requestBean.getParameterList().get(0);

        HashMap<String, Object> query_map = new HashMap<>();
        
        //模型过滤条件
        query_map.put("modelLevel",armsDataStatistics.getModel_level());
        query_map.put("modelBusiType",armsDataStatistics.getModel_busi_type());
        query_map.put("model_name",armsDataStatistics.getModel_name());
        List<HashMap<String, Object>> modelIdList = modelMapper.selectFilterModel(BaseUtil.filterSqlParam(BaseUtil.getLoginUser().getUserNo()));
        if(!StringUtil.checkNull(armsDataStatistics.getModel_name())){
        	HashMap<String, Object> queryModel = new HashMap<>();
        	queryModel.put("MODEL_ID", BaseUtil.filterSqlParam(armsDataStatistics.getModel_name()));
        	modelIdList.add(queryModel);
        }
        query_map.put("modelIdList", modelIdList);
        query_map.put("userOrganNo",BaseUtil.getLoginUser().getOrganNo());
        query_map.put("modelType", "0");
        List allModel = modelMapper.getUserModelInfos(query_map);
        //数据过滤条件
        query_map.clear();
        query_map.put("allModel", allModel);
        query_map.put("branch_organ",armsDataStatistics.getBranch_organ());
        if(!StringUtil.checkNull(armsDataStatistics.getQuery_busi_organ())){
        	query_map.put("query_busi_organ", Arrays.asList(armsDataStatistics.getQuery_busi_organ().split(",")));
        }else {
        	query_map.put("query_busi_organ", null);
        }
        query_map.put("armsStartDate",armsDataStatistics.getArmsStartDate());
        query_map.put("armsEndDate",armsDataStatistics.getArmsEndDate());
        
        //执行删除
        armsDataStatisticsMapper.deleteArmsCount(query_map);
        armsDataStatisticsMapper.deleteArmsHsCount(query_map);
        //执行插入
        for (Object object : allModel) {
            Integer tableId = ((Model)object).getTableId();
        	String tableName = mcTableMapper.selectByPrimaryKey(Integer.valueOf(BaseUtil.filterSqlParam(tableId+""))).getTableName();
        	query_map.put("tableName",BaseUtil.filterSqlParam(tableName));
        	//执行插入
            armsDataStatisticsMapper.insertArmsCount(query_map);
            if("4".equals(((Model)object).getAlarmLevel())){//提示类刷新提示类统计信息
            	armsDataStatisticsMapper.insertArmsHsCount(query_map);
            }
		}
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("刷新成功");
	}

	/**
     * 同机构，柜员，账号关联模型统计查询
     * @param requestBean
     * @param responseBean
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void relateStatisticsQuery(RequestBean requestBean, ResponseBean responseBean) throws Exception{
		// TODO Auto-generated method stub
    	// 前台参数集合
        Map sysMap = requestBean.getSysMap();
        ArmsDataStatistics armsDataStatistics = (ArmsDataStatistics) requestBean.getParameterList().get(0);

        //查询字段组装成Map  不去判断空值，在xml中统一判断
        Map<String,Object> query_map = new HashMap<>();
        String tableName = "supervise_statistic_tb";
		if(!BaseUtil.isBlank(armsDataStatistics.getProc_date())){
			String time = DateUtil.getDateBeforOrAfter(armsDataStatistics.getProc_date(),10);
			SimpleDateFormat fd = new SimpleDateFormat("yyyyMMdd");
			if(fd.parse(time).before(fd.parse(DateUtil.getNow()))){
				tableName += "_his";
			}
		}
		query_map.put("tableName",tableName);
        if(!StringUtil.checkNull(armsDataStatistics.getQuery_busi_organ())){
            query_map.put("query_busi_organ", Arrays.asList(armsDataStatistics.getQuery_busi_organ().split(",")));
        }else {
            query_map.put("query_busi_organ", null);
        }
        query_map.put("model_busi_type",armsDataStatistics.getModel_busi_type());
        query_map.put("model_name",armsDataStatistics.getModel_name());
        query_map.put("model_level",armsDataStatistics.getModel_level());
        query_map.put("procDate",armsDataStatistics.getProc_date());
        query_map.put("bankCode",armsDataStatistics.getBankCode());
        query_map.put("operatorNo",armsDataStatistics.getOperatorNo());
        query_map.put("acctNo",armsDataStatistics.getAcctNo());
        query_map.put("organNo",BaseUtil.getLoginUser().getOrganNo());
        query_map.put("modelIdList", modelMapper.selectFilterModel(BaseUtil.filterSqlParam(BaseUtil.getLoginUser().getUserNo())));
        //执行查询
        List<HashMap<String,Object>> armsPromptTjDatas = armsDataStatisticsMapper.relateStatisticsQuery(query_map);

        Map retMap = new HashMap();
        retMap.put("armsTjDatas", armsPromptTjDatas);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
	}

	/**
     * 统计查询SQL获取
     * @param requestBean
     * @param responseBean
     * @throws Exception
     */
    public  void getMonitorData(RequestBean requestBean,ResponseBean responseBean)  throws Exception{
        // 前台参数集合
        ArmsDataStatistics armsDataStatistics = (ArmsDataStatistics) requestBean.getParameterList().get(0);

          //查询字段组装成Map  不去判断空值，在xml中统一判断
          Map<String,Object> query_map = new HashMap<>();
          query_map.put("branch_organ",armsDataStatistics.getBranch_organ());
          if(!StringUtil.checkNull(armsDataStatistics.getQuery_busi_organ())){
              query_map.put("query_busi_organ", Arrays.asList(armsDataStatistics.getQuery_busi_organ().split(",")));
          }else {
              query_map.put("query_busi_organ", null);
          }
          query_map.put("model_level",armsDataStatistics.getModel_level());
          query_map.put("model_busi_type",armsDataStatistics.getModel_busi_type());
          query_map.put("model_name",armsDataStatistics.getModel_name());
          query_map.put("armsStartDate",armsDataStatistics.getArmsStartDate());
          query_map.put("armsEndDate",armsDataStatistics.getArmsEndDate());
          query_map.put("userNo",BaseUtil.getLoginUser().getUserNo());
          query_map.put("organNo",BaseUtil.getLoginUser().getOrganNo());
          
          query_map.put("modelIdList", modelMapper.selectFilterModel(BaseUtil.getLoginUser().getUserNo()));
          query_map.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
        //执行查询
        List<HashMap<String,Object>> armsTjDatas = armsDataStatisticsMapper.getMonitorData(query_map);
        Map retMap = new HashMap();
        retMap.put("armsTjDatas", armsTjDatas);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");
        
        //添加风险预警日志
		String log = "警报信息处理_机构统计查询";
		addOperLogInfo(ARSConstants.MODEL_NAME_RISK_WARNING, ARSConstants.OPER_TYPE_4, log);

    }


    public  void promptStatisticsQuery(RequestBean requestBean,ResponseBean responseBean)  throws Exception{
        // 前台参数集合
        Map sysMap = requestBean.getSysMap();
        ArmsDataStatistics armsDataStatistics = (ArmsDataStatistics) requestBean.getParameterList().get(0);

        //查询字段组装成Map  不去判断空值，在xml中统一判断
        Map<String,Object> query_map = new HashMap<>();
        query_map.put("branch_organ",armsDataStatistics.getBranch_organ());
        if(!StringUtil.checkNull(armsDataStatistics.getQuery_busi_organ())){
            query_map.put("query_busi_organ", Arrays.asList(armsDataStatistics.getQuery_busi_organ().split(",")));
        }else {
            query_map.put("query_busi_organ", null);
        }
        query_map.put("model_busi_type",armsDataStatistics.getModel_busi_type());
        query_map.put("model_name",armsDataStatistics.getModel_name());
        query_map.put("armsStartDate",armsDataStatistics.getArmsStartDate());
        query_map.put("armsEndDate",armsDataStatistics.getArmsEndDate());
        query_map.put("userNo",BaseUtil.getLoginUser().getUserNo());
        query_map.put("organNo",BaseUtil.getLoginUser().getOrganNo());

        query_map.put("modelIdList", modelMapper.selectFilterModel(BaseUtil.getLoginUser().getUserNo()));
        query_map.put("hasPrivOrganFlag", (String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));//如果没有权限机构则查询所属机构
        //执行查询
        List<HashMap<String,Object>> armsPromptTjDatas = armsDataStatisticsMapper.promptStatisticsQuery(query_map);

        Map retMap = new HashMap();
        retMap.put("armsTjDatas", armsPromptTjDatas);
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("查询成功");

    }
}
