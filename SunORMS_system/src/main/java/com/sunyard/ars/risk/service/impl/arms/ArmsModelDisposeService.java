package com.sunyard.ars.risk.service.impl.arms;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.dao.TaskLockMapper;
import com.sunyard.ars.common.pojo.TaskLock;
import com.sunyard.ars.common.util.ParamUtil;
import com.sunyard.ars.common.util.StringUtil;
import com.sunyard.ars.common.util.UUidUtil;
import com.sunyard.ars.common.util.date.DateUtil;
import com.sunyard.ars.risk.bean.arms.ArmsFavoriteTb;
import com.sunyard.ars.risk.bean.arms.ModelFieldResulet;
import com.sunyard.ars.risk.bean.arms.SendSlipBean;
import com.sunyard.ars.risk.comm.ARMSConstants;
import com.sunyard.ars.risk.dao.arms.ArmsDataStatisticsMapper;
import com.sunyard.ars.risk.dao.arms.ArmsFavoriteTbMapper;
import com.sunyard.ars.risk.service.arms.IArmsModelDisposeService;
import com.sunyard.ars.system.dao.et.ModelDataQueryMapper;
import com.sunyard.ars.system.dao.mc.ExhibitFieldMapper;
import com.sunyard.ars.system.dao.mc.ModelMapper;
import com.sunyard.ars.system.pojo.mc.Model;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;


@Service("armsModelDisposeService")
@Transactional
public class ArmsModelDisposeService extends BaseService implements IArmsModelDisposeService {

    @Resource
    private ModelMapper modelMapper;

    @Resource
    private ModelDataQueryMapper modelDataQueryMapper;

    @Resource
    private ExhibitFieldMapper exhibitFieldMapper;

    @Resource
    private TaskLockMapper taskLockMapper;

    @Resource
    private ArmsDataStatisticsMapper armsDataStatisticsMapper;

    @Resource
    private ArmsFavoriteTbMapper armsFavoriteTbMapper;

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
        if ("dealExhibit".equals(oper_type)) {//预警撤销操作
            dealExhibit(requestBean, responseBean);
        } else if("checkPromptArms".equals(oper_type)){ //单笔阅
            checkPromptArms(requestBean, responseBean);
        } else if("batchCheckPromptArms".equals(oper_type)){//批量阅
            batchCheckPromptArms(requestBean, responseBean);
        } else if("saveFavorite".equals(oper_type)){//入库
            saveFavorite(requestBean, responseBean);
        } 
    }

    /**
     * 预警撤销提交
     * @param requestBean
     * @param responseBean
     * @throws Exception
     */
    public  void dealExhibit(RequestBean requestBean, ResponseBean responseBean) throws Exception{

        String loginUserNo = BaseUtil.getLoginUser().getUserNo();
        List<Integer>  modelRowIds =  ( List<Integer>) ((Map) requestBean.getParameterList().get(0)).get("modelRowId");
        Integer modelId = Integer.valueOf(ParamUtil.getParamValue(requestBean, "modelId"));
        String mark = ParamUtil.getParamValue(requestBean, "arms_deal_mark");
        String isBatch = ParamUtil.getParamValue(requestBean, "isBatch");

        modelId = Integer.valueOf(BaseUtil.filterSqlParam(modelId+""));

        String failRowID = "";
        for (int num = 0 ;num < modelRowIds.size() ; num ++){
            Integer modelRowId = modelRowIds.get(num);
            modelRowId = Integer.valueOf(BaseUtil.filterSqlParam(modelRowId+""));
            //获取模型信息
            Model modelInfo = modelMapper.getModelInfo(Integer.valueOf(modelId));
            //获取该笔模型的信息
            Map<String,Object>  modelDate  =  modelDataQueryMapper.getThisModelData(modelId,modelRowId,BaseUtil.filterSqlParam(modelInfo.getTableName()),null);
            if(!"0".equals(String.valueOf(modelDate.get("ISHANDLE")))){ //该模型已经被处理
                failRowID += "任务:"+num+"已经被处理";
                continue;
            }
            int  VNT = modelDataQueryMapper.dealExhibit(modelId,modelRowId,BaseUtil.filterSqlParam(modelInfo.getTableName())
                                ,DateUtil.getNewDate(DateUtil.FORMATE_DATE_yyyyMMdd),BaseUtil.filterSqlParam(BaseUtil.getLoginUser().getUserNo()),BaseUtil.filterSqlParam(mark));
            //同步更新统计表
            if (VNT > 0) {
            	armsDataStatisticsMapper.updateStatistic(VNT,BaseUtil.filterSqlParam(String.valueOf(modelDate.get("DEAL_STATE"))),modelId,BaseUtil.filterSqlParam(String.valueOf(modelDate.get("BANKCODE"))),BaseUtil.filterSqlParam(String.valueOf(modelDate.get("PROC_DATE"))));
            	//添加日志
    			String log = "模型"+modelId+"-"+modelInfo.getTableName()+"预警撤销提交,并同步更新统计表";
    			addOperLogInfo(ARSConstants.MODEL_NAME_RISK_WARNING, ARSConstants.OPER_TYPE_3, log);
            }else {
            	failRowID += "任务:"+num+"更新失败";
            	//添加日志
    			String log = "模型"+modelId+"-"+modelInfo.getTableName()+"预警撤销提交,但没有同步更新统计表";
    			addOperLogInfo(ARSConstants.MODEL_NAME_RISK_WARNING, ARSConstants.OPER_TYPE_3, log);
            }
                


        }

        Map retMap = new HashMap();
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);

        if ("".equals(failRowID))
            if("0".equals(isBatch))
                 responseBean.setRetMsg("撤销成功");
            else
                responseBean.setRetMsg("批量撤销成功");
        else
            responseBean.setRetMsg("部分任务处理失败:"+failRowID);
    }

    /**
     * 提示类预警已阅  单笔
     * @param requestBean
     * @param responseBean
     */
    public void checkPromptArms(RequestBean requestBean, ResponseBean responseBean) throws Exception {

        Integer modelId = Integer.valueOf(ParamUtil.getParamValue(requestBean,"modelId"));
        Integer modelRowId = Integer.valueOf(ParamUtil.getParamValue(requestBean,"modelRowId"));
        String loginUserNo = BaseUtil.getLoginUser().getUserNo();

        modelId = Integer.valueOf(BaseUtil.filterSqlParam(modelId+""));
        modelRowId = Integer.valueOf(BaseUtil.filterSqlParam(modelRowId+""));


        Map retMap = new HashMap();
        //获取模型信息
        Model modelInfo = modelMapper.getModelInfo(Integer.valueOf(modelId));
        
        String tableName = modelInfo.getTableName();//模型表名
        tableName = BaseUtil.filterSqlParam(tableName);

        //获取该笔模型的信息
        Map<String,Object>  modelDate  =  modelDataQueryMapper.getThisModelData(modelId,modelRowId,tableName,null);
        if(modelDate == null) {
        	tableName = tableName + "_HIS";
        	modelDate  =  modelDataQueryMapper.getThisModelData(modelId,modelRowId,tableName,null);
        }
        if(modelDate == null) {
             responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
             responseBean.setRetMsg("该笔数据异常，请联系管理员!");
             return;
        }
        if(!"0".equals(String.valueOf(modelDate.get("CHECK_STATE")))){ //该模型已经被处理
            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
            responseBean.setRetMsg("任务已经被处理");
            return;
        }

        //新增处理标志
        String checkInfo="处理人:"+loginUserNo+" 处理时间:"+DateUtil.getNewDate(DateUtil.FORMATE_FULL);
        //标记已阅状态
        int  VNT = modelDataQueryMapper.checkPromptArms(modelId,modelRowId,tableName,BaseUtil.filterSqlParam(checkInfo));
        //同步更新统计表
        if (VNT > 0)
            armsDataStatisticsMapper.updatePromptStatistic(VNT,modelId,BaseUtil.filterSqlParam(String.valueOf(modelDate.get("BANKCODE"))),BaseUtil.filterSqlParam(String.valueOf(modelDate.get("PROC_DATE"))));

        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg("撤销成功");
    }



    /**
     * 提示类预警已阅  批量
     * @param requestBean
     * @param responseBean
     */
    public void batchCheckPromptArms(RequestBean requestBean, ResponseBean responseBean) throws Exception {

        List<Object> modelDatas = requestBean.getParameterList();

        String loginUserNo = BaseUtil.getLoginUser().getUserNo();
        String failRowID = "";

        for (int num = 0 ;num < modelDatas.size() ; num ++){
            Map<String,Object> modelData = (Map<String,Object>)modelDatas.get(num);
            Integer modelId = Integer.valueOf(modelData.get("modelId").toString());
            Integer modelRowId = Integer.valueOf(modelData.get("modelRowId").toString());

            modelId = Integer.valueOf(BaseUtil.filterSqlParam(modelId+""));
            modelRowId = Integer.valueOf(BaseUtil.filterSqlParam(modelRowId+""));
            Map retMap = new HashMap();
            //获取模型信息
            Model modelInfo = modelMapper.getModelInfo(Integer.valueOf(modelId));
            String tableName = modelInfo.getTableName();//模型表名
            tableName = BaseUtil.filterSqlParam(tableName);
            //获取该笔模型的信息
            Map<String,Object>  modelDate  =  modelDataQueryMapper.getThisModelData(modelId,modelRowId,tableName,null);
            if(modelDate == null) {
            	tableName = tableName + "_HIS";
            	modelDate  =  modelDataQueryMapper.getThisModelData(modelId,modelRowId,tableName,null);
            }
            if(!"0".equals(String.valueOf(modelDate.get("CHECK_STATE")))){ //该模型已经被处理
                failRowID += "任务:"+num+"已经被处理";
            }

            //新增处理标志
            String checkInfo="处理人:"+loginUserNo+" 处理时间:"+DateUtil.getNewDate(DateUtil.FORMATE_FULL);
            //标记已阅状态
            int  VNT = modelDataQueryMapper.checkPromptArms(modelId,modelRowId,tableName,BaseUtil.filterSqlParam(checkInfo));
            //同步更新统计表
            if (VNT > 0)
                armsDataStatisticsMapper.updatePromptStatistic(VNT,modelId,BaseUtil.filterSqlParam(String.valueOf(modelDate.get("BANKCODE"))),BaseUtil.filterSqlParam(String.valueOf(modelDate.get("PROC_DATE"))));
            else
                failRowID += "任务:"+num+"更新失败";


        }

        Map retMap = new HashMap();
        responseBean.setRetMap(retMap);
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);

        if ("".equals(failRowID))
            responseBean.setRetMsg("批量阅成功");
        else
            responseBean.setRetMsg("部分任务处理失败:"+failRowID);
    }

    /**
     * 入库
     * @param requestBean
     * @param responseBean
     */
    public void saveFavorite(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        Map retMap = new HashMap();
        String loginUserNo = BaseUtil.getLoginUser().getUserNo();
        String modelId = ParamUtil.getParamValue(requestBean,"modelId");
        List<String>  modelRowIds = null;
        if(!(((Map) requestBean.getParameterList().get(0)).get("modelRowId") instanceof String)){
        	modelRowIds =  ( List<String>) ((Map) requestBean.getParameterList().get(0)).get("modelRowId");
        }
        String favorite_title = ParamUtil.getParamValue(requestBean,"favorite_title");
        String type = ParamUtil.getParamValue(requestBean,"type");
        String remark = ParamUtil.getParamValue(requestBean,"remark");

        String modelRowId = ParamUtil.getParamValue(requestBean,"modelRowId");
        String formId = ParamUtil.getParamValue(requestBean,"formId");
        //获取模型信息
        //Model modelInfo = modelMapper.getModelInfo(Integer.valueOf(modelId));
        //获取该笔模型的信息
        //Map<String,Object>  modelDate  =  modelDataQueryMapper.getThisModelData(modelId,modelRowId,modelInfo.getTableName(),null);
        //判断是否已经有过入库
//        int checkFavorite = myCollectorDao.checkFavorite(favoriteType, entryID, entryRowId, formId, favoriteUser);
        //判断是否已经有过入库
        StringBuffer failMsg  = new StringBuffer();
        StringBuffer successMsg = new StringBuffer();

        if(BaseUtil.isBlank(formId) && modelRowIds!=null){
        	for (int x = 0 ;x < modelRowIds.size() ; x++){
                int VNT = armsFavoriteTbMapper.checkFavorite(type,modelId,String.valueOf(modelRowIds.get(x)),"");
                if(VNT > 0){
                    failMsg.append("所选数据中第").append(x+1).append("笔预警信息已经入库,入库失败<br>");
                    continue;
                }

                ArmsFavoriteTb armsFavoriteTb   = new ArmsFavoriteTb();
                armsFavoriteTb.setId(UUidUtil.uuid());
                armsFavoriteTb.setEntryId(modelId);
                armsFavoriteTb.setEntryrowId(String.valueOf(modelRowIds.get(x)));
                armsFavoriteTb.setFavoriteUser(loginUserNo);
                armsFavoriteTb.setRemark(remark);
                armsFavoriteTb.setFavoriteTitle(favorite_title);
                armsFavoriteTb.setFavoriteOrgan(BaseUtil.getLoginUser().getOrganNo());
                armsFavoriteTb.setType(type);
                armsFavoriteTb.setFavoriteTime(new Date());
                armsFavoriteTb.setIscommon("0");

                int SAVA_VNT = armsFavoriteTbMapper.insertSelective(armsFavoriteTb);

                if (SAVA_VNT > 0){
                    successMsg.append("所选数据中第").append(x+1).append("笔预警信息成功入库<br>");
                    //添加日志
        			String log = "风险预警添加案例库案例:" + armsFavoriteTb.getId();
        			addOperLogInfo(ARSConstants.MODEL_NAME_RISK_WARNING, ARSConstants.OPER_TYPE_1, log);
                }else {
                    failMsg.append("所选数据中第").append(x+1).append("笔预警信息入库失败<br>");
                }
            }
        }else{
        	int VNT = armsFavoriteTbMapper.checkFavorite(type,"","",formId);
            if(VNT > 0){
                failMsg.append("一笔单据信息已经入库,入库失败<br>");
            }else{
            	ArmsFavoriteTb armsFavoriteTb   = new ArmsFavoriteTb();
                armsFavoriteTb.setId(UUidUtil.uuid());
                armsFavoriteTb.setEntryId(modelId);
                armsFavoriteTb.setEntryrowId(modelRowId);
                armsFavoriteTb.setFavoriteUser(loginUserNo);
                armsFavoriteTb.setRemark(remark);
                armsFavoriteTb.setFavoriteTitle(favorite_title);
                armsFavoriteTb.setFavoriteOrgan(BaseUtil.getLoginUser().getOrganNo());
                armsFavoriteTb.setType(type);
                armsFavoriteTb.setFavoriteTime(new Date());
                armsFavoriteTb.setIscommon("0");
                armsFavoriteTb.setFormId(formId);

                int SAVA_VNT = armsFavoriteTbMapper.insertSelective(armsFavoriteTb);

                if (SAVA_VNT > 0){
                    successMsg.append("一笔单据信息成功入库<br>");
                    //添加日志
        			String log = "风险预警添加案例库案例:" + armsFavoriteTb.getId();
        			addOperLogInfo(ARSConstants.MODEL_NAME_RISK_WARNING, ARSConstants.OPER_TYPE_1, log);
                }else {
                    failMsg.append("一笔单据信息入库失败<br>");
                }
            }  
        }
        responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
        responseBean.setRetMsg(failMsg.toString()+successMsg);
    }

}
