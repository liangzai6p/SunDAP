package com.sunyard.dap.intilligentSchedual.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.intilligentSchedual.entity.ResponseDO;
import com.sunyard.dap.intilligentSchedual.entity.modelTrainingDO;
import com.sunyard.dap.intilligentSchedual.mapper.modelTrainingMapper;
import com.sunyard.dap.intilligentSchedual.service.modelTrainingService;
import com.sunyard.dap.intilligentSchedual.util.BaseUtil;
import com.sunyard.dap.intilligentSchedual.util.PropertiesUtil;
import com.sunyard.dap.intilligentSchedual.util.SystemIntelScheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class modelTrainingServiceImpl extends ServiceImpl<modelTrainingMapper , modelTrainingDO> implements modelTrainingService {

    /**
     * 查询结果信息
     *
     * @param params
     * @return
     */
    @Override
    public ReturnT<Map> select(Map<String, Object> params) {
        try {
            log.debug("QueryOperation-Start");
            HashMap<String, Object> condMap = new HashMap<String, Object>();
            Map retMap = new HashMap();
            String start_date  = (String)params.get("start_date");
            String end_date = (String)params.get("end_date");

            if(start_date.indexOf("-")>-1){
                start_date = start_date.replaceAll("-", "");
            }
            if(end_date.indexOf("-")>-1){
                end_date = end_date.replaceAll("-", "");
            }
            condMap.put("start_date", start_date);
            condMap.put("end_date", end_date);
            // 当前页数
            int pageNum = (int) params.get("currentPage");
            // 每页数量
            int pageSize = (int) params.get("pageSize");
            // pageNum <= 0 时查询第一页，pageNum > pages（超过总数时），查询最后一页
            Page page = PageHelper.startPage(pageNum, pageSize);
            List<HashMap<String,Object>> returnList = BaseUtil.getList( BaseUtil.convertListMapKeyValue(baseMapper.select(condMap)),page);
            // 获取总记录数
            long totalCount = page.getTotal();
            retMap.put("pageNum", pageNum);
            retMap.put("pageSize", pageSize);
            retMap.put("allRow", totalCount);
            retMap.put("returnList", returnList);
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",retMap);
//            log.debug("QueryOperation-End");
        } catch (Exception e) {
            // addLogInfo(logContent);
            log.error("模型训练结果查询异常", e);
            return ReturnT.mapFAIL;
        }
    }

    /**
     * 中心端模型训练
     *
     * @param params
     * @return
     */
    @Override
    public ReturnT<Map> centerTraining(Map<String, Object> params){
        String busiForeType = "";
        try {
            log.debug("CenterTrainingOperation-Start");
            ResponseDO trainResponseDO = new ResponseDO();
            HashMap<String, Object> condMap = new HashMap<String, Object>();
            String start_date  = (String)params.get("start_date");
            String end_date = (String)params.get("end_date");
            String training_type = (String)params.get("training_type");
            condMap.put("start_date", start_date.replaceAll("-", ""));
            condMap.put("end_date", end_date.replaceAll("-", ""));
            condMap.put("training_type", training_type);

            Date date = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
            String run_date = sdf1.format(date);
            String run_time = sdf2.format(date);
            //客服中心话务模型训练
            if("3".equals(training_type)){
                busiForeType = "客服中心话务模型训练";
                String trainStartDate = start_date.replaceAll("-", "/");
                String trainEndtDate = end_date.replaceAll("-", "/");
                trainResponseDO = serverCenterCalledModelTrain(trainStartDate,trainEndtDate);
            }else{
                //作业中心、渠道模型训练
                busiForeType = "作业中心、渠道模型训练";
                List<HashMap<String,Object>> items = BaseUtil.convertListMapKeyValue(baseMapper.selectItemName(condMap));
                if(items.size()>0){
                    String trainStartDate = start_date.replaceAll("-", "");
                    String trainEndtDate = end_date.replaceAll("-", "");
                    trainResponseDO = modelTest(trainStartDate, trainEndtDate, items);
                }else{
                    return ReturnT.mapFAIL;
                }
            }

            String returnCode = trainResponseDO.getRetCode();
            String returnMsg = trainResponseDO.getRetMsg();
            condMap.put("run_type", training_type);
            condMap.put("run_content",returnCode+"-"+returnMsg);
            condMap.put("run_date", run_date);
            condMap.put("run_time", run_time);
            baseMapper.insert(condMap);

            log.debug("CenterTrainingOperation-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"模型训练操作成功",null);
        } catch (Exception e) {
            log.error(busiForeType+"异常", e);
            return ReturnT.mapFAIL;
        }

    }

    /**
     * @param params
     * @return
     */
    @Override
    public ReturnT<Map> busiForecast(Map<String, Object> params){
        String busiForeType = "";
        try{
            log.debug("busiForecastMethed-Start");
            ResponseDO  foreResponseDO = new ResponseDO();
            HashMap<String, Object> condMap = new HashMap<String, Object>();
            String start_date  = params.get("start_date")+"";
            String end_date = params.get("end_date")+"";
            String forecast_month = params.get("forecast_month")+"";
            String busiFore_type = params.get("busiFore_type")+"";

            String foreStartDate = SystemIntelScheUtil.getMonthFirstDate(forecast_month);
            String foreEndDate = SystemIntelScheUtil.getMonthLastDate(forecast_month);
            int dayTotal = SystemIntelScheUtil.getDayTotalStartDateEndDate(foreStartDate, foreEndDate, "yyyy-MM-dd");

            Date date = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
            String run_date = sdf1.format(date);
            String run_time = sdf2.format(date);

            if("4".equals(busiFore_type)){//作业中心业务量预测
                busiForeType = "作业中心业务量预测";
                start_date = start_date.replaceAll("-", "");
                end_date = end_date.replaceAll("-", "");
                foreStartDate = foreStartDate.replaceAll("-", "");

                foreResponseDO = centerbusiForecast(start_date,end_date,foreStartDate,dayTotal);

            }else{//客服中心话务量预测
                busiForeType = "客服中心话务量预测";
                start_date = start_date.replaceAll("-", "/");
                end_date = end_date.replaceAll("-", "/");
                foreStartDate = foreStartDate.replaceAll("-", "");
                foreResponseDO = serviceCenterbusiForecast(start_date,end_date,foreStartDate,dayTotal);
            }

            String returnCode = foreResponseDO.getRetCode();
            String returnMsg = foreResponseDO.getRetMsg();
            condMap.put("run_type", busiFore_type);
            condMap.put("run_content",returnCode+"-"+returnMsg);
            condMap.put("run_date", run_date);
            condMap.put("run_time", run_time);
            baseMapper.insert(condMap);

            log.debug("busiForecastMethed-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"业务量预测操作成功",null);
        }catch(Exception e){
            log.error(busiForeType+"异常", e);
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<Map> serverCallSchedule(Map<String, Object> params){
        try{
            log.debug("serverCallScheduleMethod-Start");
            ResponseDO  scheResponseDO = new ResponseDO();
            HashMap<String, Object> condMap = new HashMap<String, Object>();
            String scheMonth  = params.get("scheMonth")+"";

            String scheStartDate = SystemIntelScheUtil.getMonthFirstDate(scheMonth);
            String scheforeEndDate = SystemIntelScheUtil.getMonthLastDate(scheMonth);
            int dayTotal = SystemIntelScheUtil.getDayTotalStartDateEndDate(scheStartDate, scheforeEndDate, "yyyy-MM-dd");

            Date date = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
            String run_date = sdf1.format(date);
            String run_time = sdf2.format(date);

            scheStartDate = scheStartDate.replaceAll("-", "");

            scheResponseDO = serverIntelScheduleMethod(scheStartDate,dayTotal);

            String returnCode = scheResponseDO.getRetCode();
            String returnMsg = scheResponseDO.getRetMsg();
            condMap.put("run_type", "7");
            condMap.put("run_content",returnCode+"-"+returnMsg);
            condMap.put("run_date", run_date);
            condMap.put("run_time", run_time);
            baseMapper.insert(condMap);

            log.debug("serverCallScheduleMethod-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"客服中心排班操作成功",null);
        }catch(Exception e){
            log.error("客服中心排班执行异常", e);
            return ReturnT.mapFAIL;
        }
    }

    /**
     * @param params
     * @return
     */
    @Override
    public ReturnT<Map> centerScheduleMethod(Map<String, Object> params) {
        try{
            log.debug("centerScheduleMethod-Start");
            ResponseDO  scheResponseDO = new ResponseDO();
            HashMap<String, Object> condMap = new HashMap<String, Object>();
            String start_date  = params.get("start_date")+"";
            String end_date = params.get("end_date")+"";
            String worktime_startdate = params.get("worktime_startdate")+"";
            String worktime_enddate = params.get("worktime_enddate")+"";
            String hsSourse_startdate = params.get("hsSourse_startdate")+"";
            String hsSourse_enddate = params.get("hsSourse_enddate")+"";
            List depArray = (List)params.get("depArray");

            List<String> depScheArray = new ArrayList<String>();

            for(int i=0;i<depArray.size();i++){
                String depStr = depArray.get(i)+"";
                if("支付结算处A".equals(depStr)){
                    depScheArray.add("支付结算处A_本币");
                    depScheArray.add("支付结算处A_外币");
                }else{
                    depScheArray.add(depStr);
                }
            }
            int dayTotal = SystemIntelScheUtil.getDayTotalStartDateEndDate(start_date, end_date, "yyyy-MM-dd");
            Date date = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss");
            String run_date = sdf1.format(date);
            String run_time = sdf2.format(date);

            start_date = start_date.replaceAll("-", "");
            end_date = end_date.replaceAll("-", "");
            worktime_startdate = worktime_startdate.replaceAll("-", "");
            worktime_enddate = worktime_enddate.replaceAll("-", "");
            hsSourse_startdate = hsSourse_startdate.replaceAll("-", "");
            hsSourse_enddate = hsSourse_enddate.replaceAll("-", "");

            scheResponseDO = centerIntelScheduleMethod(start_date,end_date,worktime_startdate,worktime_enddate,hsSourse_startdate,hsSourse_enddate,depScheArray,dayTotal);

            String returnCode = scheResponseDO.getRetCode();
            String returnMsg = scheResponseDO.getRetMsg();
            condMap.put("run_type", "6");
            condMap.put("run_content",returnCode+"-"+returnMsg);
            condMap.put("run_date", run_date);
            condMap.put("run_time", run_time);
            baseMapper.insert(condMap);

            log.debug("centerScheduleMethod-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"作业中心排班操作成功",null);
        }catch(Exception e){
            log.error("作业中心排班执行异常", e);
            return ReturnT.mapFAIL;
        }
    }

    public static ResponseDO serverIntelScheduleMethod(String scheStartDate,int dayTotal){
        List list = new ArrayList();
        list.add("CNKF20001");
        list.add(scheStartDate);//客服中心排班的开始日期
        list.add(dayTotal);//客服中心排班总天数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("parameterList", list);
        String foreScheServerAddress = PropertiesUtil.getValue("foreScheServerAddress");//IP地址
        String foreScheServerPort = PropertiesUtil.getValue("foreScheServerPort");//端口号
        String serverStr = "http://"+foreScheServerAddress+":"+foreScheServerPort;//http://25.66.133.59:8090

        ResponseDO  ResponseDO = SystemIntelScheUtil.sendPost(serverStr+"/call/callScheduleForecast",jsonObject);

        return ResponseDO;
    }

    //作业中心、渠道模型训练
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static ResponseDO modelTest(String start_date,String end_date,List<HashMap<String,Object>> items){
        List itermList = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            itermList.add(items.get(i).get("item_name"));
        }
        List list = new ArrayList();
        list.add(start_date);//模型训练使用源数据的开始日期
        list.add(end_date);//模型训练使用源数据的结束日期
        list.add(itermList);//模型训练类目集合
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("parameterList", list);
        String foreScheServerAddress = PropertiesUtil.getValue("foreScheServerAddress");//IP地址
        String foreScheServerPort = PropertiesUtil.getValue("foreScheServerPort");//端口号
        String serverStr = "http://"+foreScheServerAddress+":"+foreScheServerPort;//http://25.66.133.59:8090
        ResponseDO  ResponseDO = SystemIntelScheUtil.sendPost(serverStr+"/task/modelTrain",jsonObject);

        return ResponseDO;
    }
    //客服中心预测量模型训练
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static ResponseDO serverCenterCalledModelTrain(String start_date,String end_date){
        List list = new ArrayList();
        list.add(start_date);//模型训练使用源数据的开始日期
        list.add(end_date);//模型训练使用源数据的结束日期
        list.add("1");//客服中心模型训练扩展字段，1：话务量模型
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("parameterList", list);
        String foreScheServerAddress = PropertiesUtil.getValue("foreScheServerAddress");//IP地址
        String foreScheServerPort = PropertiesUtil.getValue("foreScheServerPort");//端口号
        String serverStr = "http://"+foreScheServerAddress+":"+foreScheServerPort;//http://25.66.133.59:8090
        ResponseDO  ResponseDO = SystemIntelScheUtil.sendPost(serverStr+"/call/callModelTrain",jsonObject);

        return ResponseDO;
    }
    //作业中心业务量预测(处室、渠道)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static ResponseDO centerbusiForecast(String startDate,String endDate,String foreStartDate,int totalDay){
        List list = new ArrayList();
        list.add(startDate);//预测使用源数据的开始日期
        list.add(endDate);//预测使用源数据的结束日期
        list.add(foreStartDate);//预测开始日期
        list.add(totalDay+"");//预测天数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("parameterList", list);
        String foreScheServerAddress = PropertiesUtil.getValue("foreScheServerAddress");//IP地址
        String foreScheServerPort = PropertiesUtil.getValue("foreScheServerPort");//端口号
        String serverStr = "http://"+foreScheServerAddress+":"+foreScheServerPort;//http://25.66.133.59:8090
        ResponseDO  ResponseDO = SystemIntelScheUtil.sendPost(serverStr+"/task/taskForecast",jsonObject);

        return ResponseDO;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static ResponseDO serviceCenterbusiForecast(String startDate,String endDate,String foreStartDate,int totalDay){
        List list = new ArrayList();
        list.add(startDate);//预测使用源数据的开始日期
        list.add(endDate);//预测使用源数据的结束日期
        list.add(foreStartDate);//预测开始日期
        list.add(totalDay+"");//预测天数
        list.add("1");//预测业务类型，扩展字段，目前只有 1：话务预测
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("parameterList", list);
        String foreScheServerAddress = PropertiesUtil.getValue("foreScheServerAddress");//IP地址
        String foreScheServerPort = PropertiesUtil.getValue("foreScheServerPort");//端口号
        String serverStr = "http://"+foreScheServerAddress+":"+foreScheServerPort;//http://25.66.133.59:8090
        ResponseDO  ResponseDO = SystemIntelScheUtil.sendPost(serverStr+"/call/callForecast",jsonObject);

        return ResponseDO;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static ResponseDO centerIntelScheduleMethod(String startDate,String endDate,String workTimeStartDate,
                                                                                     String workTimeEndEate,String hsSourseStartDate,String hsSourseEndDate,List depArray,int dayTotal){

        List list = new ArrayList();
        list.add(startDate);//排班的开始日期
        list.add(endDate);//排班的结束日期
        list.add(workTimeStartDate);//历史工作时长开始日期
        list.add(workTimeEndEate);//历史工作时长结束日期
        list.add(hsSourseStartDate);//排班源数据开始日期
        list.add(hsSourseEndDate);//排班源数据结束日期
        list.add(dayTotal);//排班天数
        list.add(depArray);//排班处室

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("parameterList", list);
        String foreScheServerAddress = PropertiesUtil.getValue("foreScheServerAddress");//IP地址
        String foreScheServerPort = PropertiesUtil.getValue("foreScheServerPort");//端口号
        String serverStr = "http://"+foreScheServerAddress+":"+foreScheServerPort;//http://25.66.133.59:8090
        ResponseDO  ResponseDO=SystemIntelScheUtil.sendPost(serverStr+"/task/scheduleForecast",jsonObject);

        return ResponseDO;
    }
}
