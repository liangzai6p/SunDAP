package com.sunyard.dap.intilligentSchedual.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.intilligentSchedual.entity.CenterIntelScheDO;
import com.sunyard.dap.intilligentSchedual.entity.ResponseDO;
import com.sunyard.dap.intilligentSchedual.mapper.CenterIntelScheMapper;
import com.sunyard.dap.intilligentSchedual.service.CenterIntelScheService;
import com.sunyard.dap.intilligentSchedual.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.sunyard.dap.intilligentSchedual.util.MonitorIntelScheUtil.headArrayReplace;

@Service("CenterIntelScheService")
@Slf4j
public class CenterIntelScheServiceImpl extends ServiceImpl<CenterIntelScheMapper, CenterIntelScheDO> implements CenterIntelScheService {
    ResponseDO responseDO = new ResponseDO();
    /*@Override
    public ReturnT<List> intelScheDepUserInfoQuery(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.intelScheDepUserInfoQuery(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<List> centerIntelScheButtonRight(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.centerIntelScheButtonRight(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }*/

    @Override
    public ReturnT<Map> depIntelScheTableDataQuery(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("depIntelScheTableDataQuery-Start");
            // 前台参数集合
            Map sysMap = params;
            String queryMonth = sysMap.get("query_month")+"";
            String centerNo = sysMap.get("center_no")+"";
            String depNo = sysMap.get("dep_no")+"";

            //获取月第一天
            String firstData = MonitorIntelScheUtil.getMonthFirstDate(queryMonth);
            //获取月最后一天
            String lastDate = MonitorIntelScheUtil.getMonthLastDate(queryMonth);
            queryMonth = queryMonth.replaceAll("-", "");
            //获取日期数组
            String [] headArray = headArrayReplace(MonitorUtil.getTimes(firstData, lastDate));

            HashMap condMap = new HashMap();
            condMap.put("queryMonth", queryMonth+"%");
            condMap.put("centerNo", centerNo);
            condMap.put("depNo", depNo);

            //查询数据
            List dataList = BaseUtil.convertListMapKeyValue(baseMapper.depIntelScheTableDataQuery(condMap));

            List<String> depItermList  = new ArrayList();
            Map depItermMap = new HashMap();
            for(int i=0;i<dataList.size();i++){
                Map dataMap = (Map)dataList.get(i);
                String centerItermNo = dataMap.get("center_no")+"";
                String centerItermName = dataMap.get("center_name")+"";
                String depItermNo = dataMap.get("dep_no")+"";
                String depItermName = dataMap.get("dep_name")+"";

                depItermMap.put(centerItermNo+"-"+depItermNo, centerItermName+"-"+depItermName);
                if(!depItermList.contains(centerItermNo+"-"+depItermNo)){
                    depItermList.add(centerItermNo+"-"+depItermNo);
                }
            }
            //智能排班结果处室表格展示数据格式化
            List dataMapList = intelScheTableDataFormat(headArray,dataList,depItermMap,depItermList,queryMonth);
            retMap.put("tableHeadArray", headArray);
            retMap.put("tableDataValue", dataMapList);
            log.debug("depIntelScheTableDataQuery-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }


    }

    @Override
    public ReturnT<Map> intelScheDepCalDataQuery(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("intelScheDepCalDataQuery-Start");
            // 前台参数集合
            Map sysMap = params;
            String queryMonth = sysMap.get("query_month")+"";
            String centerNo = sysMap.get("center_no")+"";
            String centerName = sysMap.get("center_name")+"";
            String depNo = sysMap.get("dep_no")+"";
            String depName = sysMap.get("dep_name")+"";
            //获取月第一天
            String firstData = MonitorIntelScheUtil.getMonthFirstDate(queryMonth);
            //获取月最后一天
            String lastDate = MonitorIntelScheUtil.getMonthLastDate(queryMonth);
            queryMonth = queryMonth.replaceAll("-", "");
            //获取日期数组
            String [] headArray = headArrayReplace(MonitorUtil.getTimes(firstData, lastDate));

            HashMap condMap = new HashMap();
            condMap.put("queryMonth", queryMonth+"%");
            condMap.put("centerNo", centerNo);
            condMap.put("depNo", depNo);

            //根据处室查询该处室下的排班人员信息
            List depUserList = BaseUtil.convertListMapKeyValue(baseMapper.intelScheDepUserInfoQuery(condMap));
            //查询数据
            List dataList = BaseUtil.convertListMapKeyValue(baseMapper.intelScheDepCalDataQuery(condMap));

            Map centerDepInfoMap = new HashMap();
            centerDepInfoMap.put("centerNo", centerNo);
            centerDepInfoMap.put("centerName", centerName);
            centerDepInfoMap.put("depNo", depNo);
            centerDepInfoMap.put("depName", depName);
            //智能排班结果处室表格展示数据格式化
            List dataMapList = intelScheCalDataFormat(headArray,dataList,centerDepInfoMap);
            retMap.put("calDataValue", dataMapList);
            retMap.put("depUserList", depUserList);
            log.debug("intelScheDepCalDataQuery-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> intelScheDepModifySave(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("intelScheDepModifySave-Start");
            // 前台参数集合
            Map sysMap = params;
            String depNo = sysMap.get("dep_no")+"";
            String depName = sysMap.get("dep_name")+"";
            String scheDate = sysMap.get("sche_date")+"";
            String modifyClassName = sysMap.get("modifyClassName")+"";
            String scheCalShowClassFlag = sysMap.get("scheCalShowClassFlag")+"";
            String create_user_no = sysMap.get("user_no")+"";
            String create_user_name = sysMap.get("user_name")+"";
            String scheModifyOldData = sysMap.get("scheModifyOldData")+"";

            //排班人员信息查询
            List userInfoList = BaseUtil.convertListMapKeyValue(baseMapper.intelScheUserInfoQuery(null));
            Map userInfoMap = new HashMap();
            Map noticeUserMapInfo = new HashMap();
            for(int i=0;i<userInfoList.size();i++){
                Map curMap = (Map)userInfoList.get(i);
                String userNo = curMap.get("user_no")+"";
                String userName = curMap.get("user_name")+"";
                userInfoMap.put(userName.replaceAll("\\s*", ""), userNo);
                noticeUserMapInfo.put(userNo, userName);
            }
            List userList = new ArrayList();
            List scheList = new ArrayList();
            //排班手动修改旧值信息封装
            if(modifyClassName.indexOf(",")>-1){
                String [] aClassArray = modifyClassName.split(",");
                for(int i=0;i<aClassArray.length;i++){
                    Map curModifyMap = new HashMap();
                    String userName = aClassArray[i].replaceAll("\\s*", "");
                    String userNo = userInfoMap.get(userName)+"";
                    if(!userList.contains(userNo)){
                        userList.add(userNo);
                    }
                    curModifyMap.put("scheDate", scheDate);
                    curModifyMap.put("user_no", userNo);
                    //可能有空格的姓名
                    String saveUserName = noticeUserMapInfo.get(userNo)+"";
                    curModifyMap.put("user_name", saveUserName);
                    curModifyMap.put("dep_no", depNo);
                    curModifyMap.put("depart_name", depName);
                    curModifyMap.put("organ_name", depName);
                    if("1".equals(scheCalShowClassFlag)){
                        curModifyMap.put("sche_result", "1");
                    }else{
                        curModifyMap.put("sche_result", "2");
                    }
                    scheList.add(curModifyMap);
                }
            }else if(!"".equals(modifyClassName) && !"null".equals(modifyClassName)){
                Map curModifyMap = new HashMap();
                String userName = modifyClassName.replaceAll("\\s*", "");
                String userNo = userInfoMap.get(userName)+"";
                if(!userList.contains(userNo)){
                    userList.add(userNo);
                }
                curModifyMap.put("scheDate", scheDate);
                curModifyMap.put("user_no", userNo);
                //可能有空格的姓名
                String saveUserName = noticeUserMapInfo.get(userNo)+"";
                curModifyMap.put("user_name", saveUserName);
                curModifyMap.put("dep_no", depNo);
                curModifyMap.put("depart_name", depName);
                curModifyMap.put("organ_name", depName);
                if("1".equals(scheCalShowClassFlag)){
                    curModifyMap.put("sche_result", "1");
                }else{
                    curModifyMap.put("sche_result", "2");
                }
                scheList.add(curModifyMap);
            }

            //排班手动修改旧值信息封装
            List scheOldList = new ArrayList();
            if(!"".equals(scheModifyOldData)){
                if(scheModifyOldData.indexOf(",")>-1){
                    String [] aClassArray = scheModifyOldData.split(",");
                    for(int i=0;i<aClassArray.length;i++){
                        Map curOldMap = new HashMap();
                        String userName = aClassArray[i].replaceAll("\\s*", "");
                        String userNo = userInfoMap.get(userName)+"";
                        if(!userList.contains(userNo)){
                            userList.add(userNo);
                        }
                        curOldMap.put("scheDate", scheDate);
                        curOldMap.put("user_no", userNo);
                        //可能有空格的姓名
                        String saveUserName = noticeUserMapInfo.get(userNo)+"";
                        curOldMap.put("user_name", saveUserName);
                        curOldMap.put("dep_no", depNo);
                        curOldMap.put("depart_name", depName);
                        curOldMap.put("organ_name", depName);
                        if("1".equals(scheCalShowClassFlag)){
                            curOldMap.put("sche_result", "1");
                        }else{
                            curOldMap.put("sche_result", "2");
                        }
                        scheOldList.add(curOldMap);
                    }
                }else{
                    Map curOldMap = new HashMap();
                    String userName = scheModifyOldData.replaceAll("\\s*", "");
                    String userNo = userInfoMap.get(userName)+"";
                    if(!userList.contains(userNo)){
                        userList.add(userNo);
                    }
                    curOldMap.put("scheDate", scheDate);
                    curOldMap.put("user_no", userNo);
                    //可能有空格的姓名
                    String saveUserName = noticeUserMapInfo.get(userNo)+"";
                    curOldMap.put("user_name", saveUserName);
                    curOldMap.put("dep_no", depNo);
                    curOldMap.put("depart_name", depName);
                    curOldMap.put("organ_name", depName);
                    if("1".equals(scheCalShowClassFlag)){
                        curOldMap.put("sche_result", "1");
                    }else{
                        curOldMap.put("sche_result", "2");
                    }
                    scheOldList.add(curOldMap);
                }
            }

            //排班修改原来旧值信息修改
            for(int i=0;i<scheOldList.size();i++){
                HashMap condMap = (HashMap)scheOldList.get(i);
                baseMapper.intelScheDepModifyOldValUpdate(condMap);
            }

            //排班修改新值信息保存
            for(int i=0;i<scheList.size();i++){
                HashMap condMap = (HashMap)scheList.get(i);
                baseMapper.intelScheDepModifySave(condMap);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String todayStr = sdf.format(new Date());
            SimpleDateFormat sdft = new SimpleDateFormat("yyyyMMdd HH:mm:SS");
            String ceateTime = sdft.format(new Date());
            String publishInfo = scheDate+"当日排班已更新,请注意查看本人排班信息!";
            List noticeUserList = new ArrayList();

            for(int j=0;j<userList.size();j++){
                String userNo = userList.get(j)+"";
                HashMap userMap = new HashMap();
                String userName = noticeUserMapInfo.get(userNo)+"";
                userMap.put("user_no", userNo);
                userMap.put("user_name", userName);
                userMap.put("dep_no", depNo);
                userMap.put("dep_name", depName);
                String notice_content = todayStr+":"+depName+","+publishInfo;
                userMap.put("notice_content", notice_content);
                userMap.put("notice_state", "0");
                userMap.put("create_user_no", create_user_no);
                userMap.put("create_user_name", create_user_name);
                userMap.put("create_date", todayStr);
                userMap.put("create_time", ceateTime);
                noticeUserList.add(userMap);
            }

            /*排班修改通知消息保存*/
            for(int i=0;i<noticeUserList.size();i++){
                HashMap condMap = (HashMap)noticeUserList.get(i);
                baseMapper.intelScheAgainUserNoticeInfoSave(condMap);
            }
            
            log.debug("intelScheDepModifySave-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> centerIntelScheOperDataQuery(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("centerIntelScheOperDataQuery-Start");
            // 前台参数集合
            Map sysMap = params;
            String queryMonth = sysMap.get("operMonth")+"";
            String operName = sysMap.get("operName")+"";
            //获取月第一天
            String monthFirstData = MonitorIntelScheUtil.getMonthFirstDate(queryMonth);
            String monthLastData = MonitorIntelScheUtil.getMonthLastDate(queryMonth);
            //去横杠
            monthFirstData = monthFirstData.replaceAll("-", "");
            monthLastData = monthLastData.replaceAll("-", "");
            queryMonth = queryMonth.replaceAll("-", "");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String curDayStr = sdf.format(new Date());
            String curDayMonth = curDayStr.substring(0, 6);

            HashMap condMap = new HashMap();
            condMap.put("queryMonth", queryMonth+"%");
            condMap.put("queryMonthStr", queryMonth);
            condMap.put("curDayMonth", curDayMonth);
            condMap.put("operName", operName);
            condMap.put("monthFirstData", monthFirstData);
            condMap.put("monthLastData", monthLastData);
            condMap.put("curDayStr", curDayStr);

            //排班人员信息查询
            List userInfoList = BaseUtil.convertListMapKeyValue(baseMapper.intelScheUserInfoQuery(null));
            Map userInfoMap = new HashMap();
            Map noticeUserMapInfo = new HashMap();
            for(int i=0;i<userInfoList.size();i++){
                Map curMap = (Map)userInfoList.get(i);
                String userNo = curMap.get("user_no")+"";
                String userName = curMap.get("user_name")+"";
                userInfoMap.put(userName.replaceAll("\\s*", ""), userNo);//避免多个表中名字带空格
                noticeUserMapInfo.put(userNo, userName);
            }

            String scheUserNo = userInfoMap.get(operName.replaceAll("\\s*", ""))+"";
            condMap.put("scheUserNo", scheUserNo);
            //人员岗位基本信息查询
            List userDutyInfoList = BaseUtil.convertListMapKeyValue(baseMapper.centerIntelScheOperBaseInfoQuery(condMap));
            String userNo = "未知编号";
            String dutyName = "未知岗位";
            if(userDutyInfoList != null && userDutyInfoList.size()>0){
                Map userMap = (Map)userDutyInfoList.get(0);
                userNo = userMap.get("teller_no")+"";
                dutyName = userMap.get("duty_name")+"";
            }

            //String scheUserName =
            //封装查询条件
            condMap.put("operNo", userNo);
            //人员工作时间统计查询
            List userWorkList = BaseUtil.convertListMapKeyValue(baseMapper.centerIntelScheOperWorkTotalQuery(condMap));
            String planWorkTime = "0";
            String actWorkTime = "0";
            String totalWorkTime="0";
            if(userWorkList != null && userWorkList.size()>0){
                Map workTimeMap = (Map)userWorkList.get(0);
                planWorkTime = workTimeMap.get("planworktime")+"";
                actWorkTime = workTimeMap.get("actworktime")+"";
                totalWorkTime = workTimeMap.get("totalworktime")+"";
            }

            //人员排班数据查询数据
            List dataList = BaseUtil.convertListMapKeyValue(baseMapper.centerIntelScheOperDataQuery(condMap));

            retMap.put("userNo", userNo);
            retMap.put("dutyName", dutyName);
            retMap.put("planWorkTime", planWorkTime);
            retMap.put("actWorkTime", actWorkTime);
            retMap.put("totalWorkTime", totalWorkTime);
            retMap.put("operScheDataValue", dataList);

            log.debug("centerIntelScheOperDataQuery-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> intelScheAgainDepSelectDataQuery(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("intelScheAgainDepSelectDataQuery-Start");
            // 前台参数集合
            Map sysMap = params;
            String centerNo = sysMap.get("center_no")+"";
            String depNo = sysMap.get("dep_no")+"";

            HashMap condMap = new HashMap();
            condMap.put("centerNo", centerNo);
            condMap.put("depNo", depNo);
            //查询数据
            List dataList = BaseUtil.convertListMapKeyValue(baseMapper.intelScheAgainDepSelectDataQuery(condMap));
            retMap.put("scheAgainDepList", dataList);

            log.debug("intelScheAgainDepSelectDataQuery-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> confireScheAgain(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("confireScheAgain-Start");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String todayStr = sdf.format(new Date());
            SimpleDateFormat sdft = new SimpleDateFormat("yyyyMMdd HH:mm:SS");
            String ceateTime = sdft.format(new Date());
            // 前台参数集合
            Map sysMap = params;
            List scheDepArray = (List)sysMap.get("scheDepArray");
            String create_user_no = sysMap.get("user_no")+"";
            String create_user_name = sysMap.get("user_name")+"";
            Map commonInfoMap = new HashMap();
            commonInfoMap.put("todayStr", todayStr);
            commonInfoMap.put("ceateTime", ceateTime);
            commonInfoMap.put("createUserNo", create_user_no);
            commonInfoMap.put("createUserName", create_user_name);
            String publishInfo = "本月剩余日期排班已更新,请注意查看本人排班信息!";
            //排班请求参数类
            List scheItermList = new ArrayList();
            List noticeUserList = new ArrayList();
            for(int i=0;i<scheDepArray.size();i++){
                String depNo = scheDepArray.get(i)+"";
                if("CNSD20001".equals(depNo)){
                    scheItermList.add("单证与贸易融资处A");
                    HashMap condMap = new HashMap();
                    condMap.put("depNo", "CNSD20001");
                    String curDepNo = "CNSD20001";
                    String curDepName = "单证与贸易融资处A";
                    noticeUserList  = scheAgainNoticeUserList(noticeUserList,condMap,curDepNo,curDepName,commonInfoMap,publishInfo);
                }else if("CNXX20001".equals(depNo)){
                    scheItermList.add("综合处A");
                    HashMap condMap = new HashMap();
                    condMap.put("depNo", "CNXX20001");
                    String curDepNo = "CNXX20001";
                    String curDepName = "综合处A";
                    noticeUserList  = scheAgainNoticeUserList(noticeUserList,condMap,curDepNo,curDepName,commonInfoMap,publishInfo);
                }else if("CNHS20001".equals(depNo)){
                    scheItermList.add("支付结算处A_本币");
                    scheItermList.add("支付结算处A_外币");
                    HashMap condMap = new HashMap();
                    condMap.put("depNo", "CNHS20001");
                    String curDepNo = "CNHS20001";
                    String curDepName = "支付结算处A";
                    noticeUserList  = scheAgainNoticeUserList(noticeUserList,condMap,curDepNo,curDepName,commonInfoMap,publishInfo);
                }else if("CNDK20001".equals(depNo)){
                    scheItermList.add("贷款与票据处理处A");
                    HashMap condMap = new HashMap();
                    condMap.put("depNo", "CNDK20001");
                    String curDepNo = "CNDK20001";
                    String curDepName = "贷款与票据处理处A";
                    noticeUserList  = scheAgainNoticeUserList(noticeUserList,condMap,curDepNo,curDepName,commonInfoMap,publishInfo);
                }else if("CNQS20001".equals(depNo)){
                    scheItermList.add("清算处A");
                    HashMap condMap = new HashMap();
                    condMap.put("depNo", "CNQS20001");
                    String curDepNo = "CNQS20001";
                    String curDepName = "清算处A";
                    noticeUserList  = scheAgainNoticeUserList(noticeUserList,condMap,curDepNo,curDepName,commonInfoMap,publishInfo);
                }
            }
            //调用排班方法排班返回结果类
            ResponseDO scheResponse = MonitorIntelScheUtil.intelScheScheduleForecast(scheItermList);
            String retCode = scheResponse.getRetCode();
            String retMsg = scheResponse.getRetMsg();

            HashMap<String, Object> logCondMap = new HashMap<String, Object>();
            logCondMap.put("run_type", "10");
            logCondMap.put("run_content",retCode+"-"+retMsg);
            baseMapper.busiForeShcduleRunLogSave(logCondMap);

            if("IF0000".equals(retCode)) {
                log.debug("重新排班成功,人员通知信息保存!");
                for (int i = 0; i < noticeUserList.size(); i++) {
                    HashMap condMap = (HashMap) noticeUserList.get(i);
                    baseMapper.intelScheAgainUserNoticeInfoSave(condMap);
                }
            }

            log.debug("confireScheAgain-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> scheAgainNoticePublishInfoQuery(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("scheAgainNoticePublishInfoQuery-Start");
            // 前台参数集合
            Map sysMap = params;
            String userNo = sysMap.get("user_no")+"";
            HashMap condMap = new HashMap();
            condMap.put("userNo", userNo);
            //排班人员信息查询
            List noticeList = BaseUtil.convertListMapKeyValue(baseMapper.scheAgainNoticePublishInfoQuery(condMap));
            retMap.put("noticeList", noticeList);

            log.debug("scheAgainNoticePublishInfoQuery-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> confireNoticePublishSave(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("confireNoticePublishSave-Start");
            // 前台参数集合
            Map sysMap = params;
            String userNo = sysMap.get("user_no")+"";

            List publishList =  (List)sysMap.get("publishDataList");

            for(int i=0;i<publishList.size();i++){
                HashMap publishMap = (HashMap)publishList.get(i);
                //HashMap condMap = new HashMap();
                //condMap.put("userNo", userNo);
                //重新排班消息发布状态修改
                baseMapper.confireNoticePublishSave(publishMap);
            }

            log.debug("confireNoticePublishSave-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "修改成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> depDaysQuery(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("centerDepDaysQuery-Start");
            // 前台参数集合
            Map sysMap = params;
            String start_month = sysMap.get("start_month")+"";
            String end_month = sysMap.get("end_month")+"";
            String dep_no = sysMap.get("dep_no")+"";

            HashMap condMap = new HashMap();
            condMap.put("start_month", start_month+"01");
            condMap.put("end_month", end_month+"31");
            condMap.put("dep_no", dep_no);
            //查询数据
            List dataList = BaseUtil.convertListMapKeyValue(baseMapper.centerIntelDepDaysQuery(condMap));
            retMap.put("retList", dataList);
            log.debug("centerDepDaysQuery-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> operDailyWorkTimeTableInfoQuery(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("operDailyWorkTimeTableInfoQuery-Start");

            // 前台参数集合
            Map sysMap = params;
            String queryStartDate = sysMap.get("queryStartDate")+"";
            String queryEndDate = sysMap.get("queryEndDate")+"";
            String operWorkTimeUserNo = sysMap.get("operWorkTimeUserNo")+"";
            queryStartDate = queryStartDate.replaceAll("-", "");
            queryEndDate = queryEndDate.replaceAll("-", "");
            HashMap condMap = new HashMap();
            condMap.put("queryStartDate", queryStartDate);
            condMap.put("queryEndDate", queryEndDate);
            condMap.put("operWorkTimeUserNo", operWorkTimeUserNo);
            //查询数据
            List dataList = BaseUtil.convertListMapKeyValue(baseMapper.operDailyWorkTimeTableInfoQuery(condMap));
            retMap.put("operWorkTimeList", dataList);

            log.debug("operDailyWorkTimeTableInfoQuery-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> operWorkTimeModifyInfoSave(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("operWorkTimeModifyInfoSave-Start");
            // 前台参数集合
            Map sysMap = params;
            String work_date = sysMap.get("work_date")+"";
            String user_no = sysMap.get("user_no")+"";
            String user_name = sysMap.get("user_name")+"";
            String work_time = sysMap.get("work_time")+"";
            String week_time = sysMap.get("week_time")+"";

            HashMap condMap = new HashMap();
            condMap.put("work_date", work_date);
            condMap.put("user_no", user_no);
            condMap.put("user_name", user_name);
            condMap.put("work_time", work_time);
            condMap.put("week_time", week_time);
            //查询数据
            baseMapper.operWorkTimeModifyInfoSave(condMap);

            return new ReturnT<>(ReturnT.SUCCESS_CODE, "修改成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> centerIntelScheButtonRight(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("centerIntelScheButtonRight-Start");
            // 前台参数集合
            Map sysMap = params;
            String role_no = sysMap.get("role_no")+"";
            HashMap condMap = new HashMap();
            condMap.put("role_no", role_no);
            //查询数据
            String userActButtonRight = baseMapper.centerIntelScheButtonRight(condMap);
            retMap.put("userActButtonRight", userActButtonRight);
            log.debug("centerIntelScheButtonRight-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> centerInteScheCalDateClassInfoQuery(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("centerInteScheCalDateClassInfoQuery-Start");

            // 前台参数集合
            Map sysMap = params;
            String queryDate = sysMap.get("sche_date")+"";
            String queryDepNo = sysMap.get("dep_no")+"";
            String ShowFlag = sysMap.get("scheCalShowClassFlag")+"";//展示班次
            if(!"1".equals(ShowFlag)){//正常情况下展示第一班,只有单证处有：第一、第二班
                ShowFlag = "2";
            }
            HashMap condMap = new HashMap();
            condMap.put("queryDate", queryDate);
            condMap.put("queryDepNo", queryDepNo);
            condMap.put("ShowFlag", ShowFlag);
            //查询数据
            List dataList = BaseUtil.convertListMapKeyValue(baseMapper.centerInteScheCalDateClassInfoQuery(condMap));
            retMap.put("calDateScheList", dataList);

            //人员对应岗位信息查询
            List userRoleNameList = BaseUtil.convertListMapKeyValue(baseMapper.centerInteScheCalDateUserRoleNameInfoQuery(condMap));
            retMap.put("calDateUserRoleNameList", userRoleNameList);

            log.debug("centerInteScheCalDateClassInfoQuery-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> scheduleCalDateModifyInfoSave(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("scheduleCalDateModifyInfoSave-Start");
            // 前台参数集合
            Map sysMap = params;
            String scheDate = sysMap.get("sche_date")+"";
            String scheDepNo = sysMap.get("dep_no")+"";
            String ShowFlag = sysMap.get("scheCalShowClassFlag")+"";//展示班次
            String scheOldUserNo = sysMap.get("scheOldUserNo")+"";
            String scheOldUserName = sysMap.get("scheOldUserName")+"";
            String scheOldRoleName = sysMap.get("scheOldRoleName")+"";
            String scheOldDepartName = sysMap.get("scheOldDepartName")+"";
            String scheNewUserNo = sysMap.get("scheNewUserNo")+"";
            String scheNewUserName = sysMap.get("scheNewUserName")+"";
            String scheNewOrganName = sysMap.get("scheNewOrganName")+"";
            String scheNewDepartName = sysMap.get("scheNewDepartName")+"";
            String scheNewRoleName = sysMap.get("scheNewRoleName")+"";
            String scheNewBrefRole = sysMap.get("scheNewBrefRole")+"";
            String createUserNo = sysMap.get("createUserNo")+"";
            String createUserName = sysMap.get("createUserName")+"";

            HashMap condMap = new HashMap();
            condMap.put("scheDate", scheDate);
            condMap.put("scheDepNo", scheDepNo);
            condMap.put("ShowFlag", ShowFlag);
            condMap.put("scheOldUserNo", scheOldUserNo);
            condMap.put("scheOldRoleName", scheOldRoleName);
            condMap.put("scheOldDepartName", scheOldDepartName);
            condMap.put("scheNewUserNo", scheNewUserNo);
            condMap.put("scheNewUserName", scheNewUserName);
            condMap.put("scheNewOrganName", scheNewOrganName);
            condMap.put("scheNewDepartName", scheNewDepartName);
            condMap.put("scheNewRoleName", scheNewRoleName);
            condMap.put("scheBriefRole", scheNewBrefRole);
            condMap.put("createUserNo", createUserNo);
            condMap.put("createUserName", createUserName);

            baseMapper.scheduleCalDateModifyOldValUpdate(condMap);
            baseMapper.scheduleCalDateModifyNewValUpdateSave(condMap);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String todayStr = sdf.format(new Date());
            SimpleDateFormat sdft = new SimpleDateFormat("yyyyMMdd HH:mm:SS");
            String ceateTime = sdft.format(new Date());
            String publishInfo = scheDate+"当日排班已更新,请注意查看本人排班信息!";

            List noticeInfoList = new ArrayList();
            HashMap oldUserMap = new HashMap();
            HashMap newUserMap = new HashMap();
            //原排班人员通知消息
            oldUserMap.put("user_no", scheOldUserNo);
            oldUserMap.put("user_name", scheOldUserName);
            oldUserMap.put("dep_no", scheDepNo);
            oldUserMap.put("dep_name", scheNewOrganName);
            String notice_content = todayStr+":"+scheNewOrganName+","+publishInfo;
            oldUserMap.put("notice_content", notice_content);
            oldUserMap.put("notice_state", "0");
            oldUserMap.put("create_user_no", createUserNo);
            oldUserMap.put("create_user_name", createUserName);
            oldUserMap.put("create_date", todayStr);
            oldUserMap.put("create_time", ceateTime);
            noticeInfoList.add(oldUserMap);
            //新排班人员通知消息
            newUserMap.put("user_no", scheNewUserNo);
            newUserMap.put("user_name", scheNewUserName);
            newUserMap.put("dep_no", scheDepNo);
            newUserMap.put("dep_name", scheNewOrganName);
            String notice_content1 = todayStr+":"+scheNewOrganName+","+publishInfo;
            newUserMap.put("notice_content", notice_content1);
            newUserMap.put("notice_state", "0");
            newUserMap.put("create_user_no", createUserNo);
            newUserMap.put("create_user_name", createUserName);
            newUserMap.put("create_date", todayStr);
            newUserMap.put("create_time", ceateTime);
            noticeInfoList.add(newUserMap);

            for(int i=0;i<noticeInfoList.size();i++){
                HashMap dataMap = (HashMap)noticeInfoList.get(i);
                baseMapper.intelScheAgainUserNoticeInfoSave(dataMap);
            }

            log.debug("scheduleCalDateModifyInfoSave-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "修改成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> scheduleCalDateAddInfoSave(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("scheduleCalDateAddInfoSave-Start");
            // 前台参数集合
            Map sysMap = params;
            String scheAddDate = sysMap.get("scheAddDate")+"";
            String ShowFlag = sysMap.get("scheCalShowClassFlag")+"";//展示班次
            String scheAddDepNo = sysMap.get("scheAddDepNo")+"";
            String scheAddDepName = sysMap.get("scheAddDepName")+"";
            String scheAddDepartName = sysMap.get("scheAddDepartName")+"";
            String scheAddRoleName = sysMap.get("scheAddRoleName")+"";
            String scheAddBrefRole = sysMap.get("scheAddBrefRole")+"";
            String scheAddUserNo = sysMap.get("scheAddUserNo")+"";
            String scheAddUserName = sysMap.get("scheAddUserName")+"";
            String createUserNo = sysMap.get("createUserNo")+"";
            String createUserName = sysMap.get("createUserName")+"";

            HashMap condMap = new HashMap();
            condMap.put("scheDate", scheAddDate);
            condMap.put("ShowFlag", ShowFlag);
            condMap.put("scheDepNo", scheAddDepNo);
            condMap.put("scheNewOrganName", scheAddDepName);
            condMap.put("scheNewDepartName", scheAddDepartName);
            condMap.put("scheNewRoleName", scheAddRoleName);
            condMap.put("scheBriefRole", scheAddBrefRole);
            condMap.put("scheNewUserNo", scheAddUserNo);
            condMap.put("scheNewUserName", scheAddUserName);
            condMap.put("createUserNo", createUserNo);
            condMap.put("createUserName", createUserName);

            baseMapper.scheduleCalDateModifyNewValUpdateSave(condMap);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String todayStr = sdf.format(new Date());
            SimpleDateFormat sdft = new SimpleDateFormat("yyyyMMdd HH:mm:SS");
            String ceateTime = sdft.format(new Date());
            String publishInfo = scheAddDate+"当日排班已更新,请注意查看本人排班信息!";

            HashMap addUserMap = new HashMap();
            //新排班人员通知消息
            addUserMap.put("user_no", scheAddUserNo);
            addUserMap.put("user_name", scheAddUserName);
            addUserMap.put("dep_no", scheAddDepNo);
            addUserMap.put("dep_name", scheAddDepName);
            String notice_content = todayStr+":"+scheAddDepName+","+publishInfo;
            addUserMap.put("notice_content", notice_content);
            addUserMap.put("notice_state", "0");
            addUserMap.put("create_user_no", createUserNo);
            addUserMap.put("create_user_name", createUserName);
            addUserMap.put("create_date", todayStr);
            addUserMap.put("create_time", ceateTime);

            baseMapper.intelScheAgainUserNoticeInfoSave(addUserMap);

            log.debug("scheduleCalDateAddInfoSave-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "新增成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> scheduleCalDateDeleteInfoUpdate(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("scheduleCalDateDeleteInfoUpdate-Start");
            // 前台参数集合
            Map sysMap = params;
            String scheDate = sysMap.get("scheDelDate")+"";
            String ShowFlag = sysMap.get("scheCalShowClassFlag")+"";//展示班次
            String scheDepNo = sysMap.get("scheDelOrganNo")+"";
            String scheDelOrganName = sysMap.get("scheDelOrganName")+"";
            String scheOldDepartName = sysMap.get("scheDelDepartName")+"";
            String scheOldRoleName = sysMap.get("scheDelRoleName")+"";
            String scheOldUserNo = sysMap.get("scheDelUserNo")+"";
            String scheDelUserName = sysMap.get("scheDelUserName")+"";
            String createUserNo = sysMap.get("createUserNo")+"";
            String createUserName = sysMap.get("createUserName")+"";

            HashMap condMap = new HashMap();
            condMap.put("scheDate", scheDate);
            condMap.put("scheDepNo", scheDepNo);
            condMap.put("ShowFlag", ShowFlag);
            condMap.put("scheOldUserNo", scheOldUserNo);
            condMap.put("scheOldRoleName", scheOldRoleName);
            condMap.put("scheOldDepartName", scheOldDepartName);
            condMap.put("createUserNo", createUserNo);
            condMap.put("createUserName", createUserName);
            //虚拟删除,只改变数据的状态
            baseMapper.scheduleCalDateModifyOldValUpdate(condMap);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String todayStr = sdf.format(new Date());
            SimpleDateFormat sdft = new SimpleDateFormat("yyyyMMdd HH:mm:SS");
            String ceateTime = sdft.format(new Date());
            String publishInfo = scheDate+"当日排班已更新,请注意查看本人排班信息!";
            HashMap addUserMap = new HashMap();
            //排班人员通知消息
            addUserMap.put("user_no", scheOldUserNo);
            addUserMap.put("user_name", scheDelUserName);
            addUserMap.put("dep_no", scheDepNo);
            addUserMap.put("dep_name", scheDelOrganName);
            String notice_content = todayStr+":"+scheDelOrganName+","+publishInfo;
            addUserMap.put("notice_content", notice_content);
            addUserMap.put("notice_state", "0");
            addUserMap.put("create_user_no", createUserNo);
            addUserMap.put("create_user_name", createUserName);
            addUserMap.put("create_date", todayStr);
            addUserMap.put("create_time", ceateTime);
            //通知消息保存
            baseMapper.intelScheAgainUserNoticeInfoSave(addUserMap);

            log.debug("scheduleCalDateDeleteInfoUpdate-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "删除成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> scheOperDailyWorkDateIsOffDay(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("scheOperDailyWorkDateIsOffDay-Start");
            // 前台参数集合
            Map sysMap = params;
            String scheWorkDate = sysMap.get("scheWorkDate")+"";
            HashMap condMap = new HashMap();
            condMap.put("scheWorkDate", scheWorkDate);
            //查询数据
            List dataList = BaseUtil.convertListMapKeyValue(baseMapper.scheOperDailyWorkDateIsOffDay(condMap));

            retMap.put("offDateList", dataList);

            log.debug("scheOperDailyWorkDateIsOffDay-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> centerIntelScheDataImport(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            // 获取导入文件列表信息，包含每个文件在服务器的实际存储名称（带路径）
            List<Map<String, String>> fileInfoList = (List<Map<String, String>>) params.get("uploadFileList");
            // 获取表头行数信息
            String headerRowNumStr = params.get("headerRowNum")+"";
            List excelDataList = new ArrayList();
            if (BaseUtil.isBlank(headerRowNumStr)) {
                headerRowNumStr = "1";
            }
            HashMap condMap = new HashMap();
            //查询数据
            List userRoleList = BaseUtil.convertListMapKeyValue(baseMapper.scheExportUserRoleNameInfoQuery(condMap));

            String emptyStr = "";
            // 遍历解析每个文件，执行导入操作
            for (Map<String, String> fileMap : fileInfoList) {
                String filePath = fileMap.get("saveFileName");
                // 获取导入数据（这种数据导入的文件一般都是临时存储，导入完成即可删除，故此处调用接口时传递参数 true ）
                List<HashMap<String, String>> dataList = ImportUtil.importExcel(filePath, Integer.parseInt(headerRowNumStr), true);
                if (dataList == null) {
                    log.debug("dataList为空");
                    throw new Exception("导入文件格式有误！");
                }
                String rowEmpty = "";

                //判断导入表格数据行数是否小于1
                if(dataList.size()<1){
                    log.debug("导入记录数为空");
                    responseDO.setRetMsg("导入记录数为空");
                    responseDO.setRetCode("1");
                    return new ReturnT<>(ReturnT.SUCCESS_CODE, responseDO.getRetMsg(), retMap);
                }
                // 导入数据的实际处理
                for (int i=0;i<dataList.size();i++) {

                    HashMap<String, String> dataMap = dataList.get(i);
                    String departmentStr = "";
                    String scheDate = "";
                    String scheOrganNo = "";
                    String scheOrganName = "";
                    String scheDepartName = "";
                    //String scheUserNo = "";
                    String scheUserName = "";
                    //String scheRoleName = "";
                    String scheClassNo = "";
                    String scheBriefRole = "";

                    //校验导入行是否有空数据(有空记录行数)
                    for(String key:dataMap.keySet()){
                        if("0".equals(key)){
                            scheDate = dataMap.get(key);
                        }
						/*if("1".equals(key)){
							scheOrganNo = dataMap.get(key);
						}*/
                        if("1".equals(key)){
                            departmentStr = dataMap.get(key);
                        }
						/*if("3".equals(key)){
							scheUserNo = dataMap.get(key);
						}*/
                        if("2".equals(key)){
                            scheUserName = dataMap.get(key);
                        }
                        if("3".equals(key)){
                            scheClassNo = dataMap.get(key);
                        }
						/*if("6".equals(key)){
							scheRoleName = dataMap.get(key);
						}*/
                        if("4".equals(key)){
                            scheBriefRole = dataMap.get(key);
                        }
                    }
                    if(departmentStr.indexOf("-")>-1){
                        scheOrganNo = departmentStr.split("-")[1];
                        scheDepartName = departmentStr.split("-")[0];
                    }
                    if(!"".equals(scheDate) && !"".equals(scheOrganNo) && !"".equals(scheDepartName) &&
                            !"".equals(scheUserName) && !"".equals(scheClassNo) && !"".equals(scheBriefRole)){
                        //带横杠说明是支付结算处，需要把"_"之后的截取掉；例如：支付结算处A_本币，则处室名为：支付结算处A
                        if(scheDepartName.indexOf("_")>-1){
                            scheOrganName = scheDepartName.substring(0, scheDepartName.length()-3);
                        }else{
                            scheOrganName = scheDepartName;
                        }

                        String [] userNameArray = {};
                        if(scheUserName.indexOf(",")>-1){//英文逗号
                            userNameArray = scheUserName.split(",");
                            for(int k=0;k<userNameArray.length;k++){
                                String scheUserNo = "excel导入";
                                String userName = userNameArray[k];
                                String scheRoleName = "excel导入";
                                HashMap<String, String> actDataMap = new HashMap<String, String>();
                                for(int j=0;j<userRoleList.size();j++){
                                    Map queryRoleMap = (Map)userRoleList.get(j);
                                    String queryOrganNo = queryRoleMap.get("organ_no")+"";
                                    String queryOrganName = queryRoleMap.get("organ_name")+"";
                                    String queryUserName = queryRoleMap.get("namea")+"";
                                    String queryRoleName = queryRoleMap.get("role")+"";
                                    //判断导入人员是否在排班岗位信息表、以及人员对应的岗位是否正确，否则人员将不会被导入。
                                    if(scheOrganNo.equals(queryOrganNo) && scheDepartName.equals(queryOrganName) &&
                                            queryUserName.equals(userName) && queryRoleName.equals(scheBriefRole)){
                                        scheUserNo = queryRoleMap.get("user_no")+"";
                                        scheRoleName = queryRoleMap.get("role_name")+"";

                                        actDataMap.put("scheDate", scheDate);
                                        actDataMap.put("scheDepNo", scheOrganNo);
                                        actDataMap.put("scheNewOrganName", scheOrganName);
                                        actDataMap.put("scheNewDepartName", scheDepartName);
                                        actDataMap.put("ShowFlag", scheClassNo);
                                        actDataMap.put("scheNewUserNo", scheUserNo);
                                        actDataMap.put("scheNewUserName", userName);
                                        actDataMap.put("scheNewRoleName", scheRoleName);
                                        actDataMap.put("scheBriefRole", scheBriefRole);
                                        excelDataList.add(actDataMap);

                                    }
                                }

                            }
                        }else if(scheUserName.indexOf("，")>-1){//中文逗号
                            userNameArray = scheUserName.split("，");
                            for(int k=0;k<userNameArray.length;k++){
                                HashMap<String, String> actDataMap = new HashMap<String, String>();
                                String scheUserNo = "excel导入";
                                String userName = userNameArray[k];
                                String scheRoleName = "excel导入";
                                for(int j=0;j<userRoleList.size();j++){
                                    Map queryRoleMap = (Map)userRoleList.get(j);
                                    String queryOrganNo = queryRoleMap.get("organ_no")+"";
                                    String queryOrganName = queryRoleMap.get("organ_name")+"";
                                    String queryUserName = queryRoleMap.get("namea")+"";
                                    String queryRoleName = queryRoleMap.get("role")+"";
                                    //判断导入人员是否在排班岗位信息表、以及人员对应的岗位是否正确，否则人员将不会被导入。
                                    if(scheOrganNo.equals(queryOrganNo) && scheDepartName.equals(queryOrganName) &&
                                            queryUserName.equals(userName) && queryRoleName.equals(scheBriefRole)){
                                        scheUserNo = queryRoleMap.get("user_no")+"";
                                        scheRoleName = queryRoleMap.get("role_name")+"";

                                        actDataMap.put("scheDate", scheDate);
                                        actDataMap.put("scheDepNo", scheOrganNo);
                                        actDataMap.put("scheNewOrganName", scheOrganName);
                                        actDataMap.put("scheNewDepartName", scheDepartName);
                                        actDataMap.put("ShowFlag", scheClassNo);
                                        actDataMap.put("scheNewUserNo", scheUserNo);
                                        actDataMap.put("scheNewUserName", userName);
                                        actDataMap.put("scheNewRoleName", scheRoleName);
                                        actDataMap.put("scheBriefRole", scheBriefRole);
                                        excelDataList.add(actDataMap);
                                    }
                                }

                            }
                        }else{
                            HashMap<String, String> actDataMap = new HashMap<String, String>();
                            String scheUserNo = "excel导入";
                            String userName = "";
                            String scheRoleName = "excel导入";
                            for(int k=0;k<userRoleList.size();k++){
                                Map queryRoleMap = (Map)userRoleList.get(k);
                                String queryOrganNo = queryRoleMap.get("organ_no")+"";
                                String queryOrganName = queryRoleMap.get("organ_name")+"";
                                String queryUserName = queryRoleMap.get("namea")+"";
                                String queryRoleName = queryRoleMap.get("role")+"";
                                //判断导入人员是否在排班岗位信息表、以及人员对应的岗位是否正确，否则人员将不会被导入。
                                if(scheOrganNo.equals(queryOrganNo) && scheDepartName.equals(queryOrganName) &&
                                        queryUserName.equals(scheUserName) && queryRoleName.equals(scheBriefRole)){
                                    scheUserNo = queryRoleMap.get("user_no")+"";
                                    scheRoleName = queryRoleMap.get("role_name")+"";
                                    userName = scheUserName;

                                    actDataMap.put("scheDate", scheDate);
                                    actDataMap.put("scheDepNo", scheOrganNo);
                                    actDataMap.put("scheNewOrganName", scheOrganName);
                                    actDataMap.put("scheNewDepartName", scheDepartName);
                                    actDataMap.put("ShowFlag", scheClassNo);
                                    actDataMap.put("scheNewUserNo", scheUserNo);
                                    actDataMap.put("scheNewUserName", userName);
                                    actDataMap.put("scheNewRoleName", scheRoleName);
                                    actDataMap.put("scheBriefRole", scheBriefRole);
                                    excelDataList.add(actDataMap);
                                }
                            }

                        }
                    }else{
                        rowEmpty += (i+2)+",";
                    }
                }
                if(!"".equals(rowEmpty)){
                    emptyStr += "文件："+ filePath.substring(filePath.lastIndexOf("_")+1) +",第"+rowEmpty+"行数据为空。";
                }

            }
            //判断上传文件是否有空字段
            if(!"".equals(emptyStr)){
                log.debug("打印空字段信息："+emptyStr);
                responseDO.setRetMsg(emptyStr+"有空值字段,或者整行全为空(将整行表格删除,仅清除内容无效),导入失败");
                responseDO.setRetCode("1");
                return new ReturnT<>(ReturnT.SUCCESS_CODE, responseDO.getRetMsg(), retMap);
            }

            //保存前先将数据更新为不排班
            List<String> oldDataList = new ArrayList();
            for(int i=0;i<excelDataList.size();i++){
                HashMap curMap = (HashMap)excelDataList.get(i);
                String scheDate = curMap.get("scheDate")+"";
                String scheDepNo = curMap.get("scheDepNo")+"";
                String scheNewDepartName = curMap.get("scheNewDepartName")+"";
                String ShowFlag = curMap.get("ShowFlag")+"";
                String scheBriefRole = curMap.get("scheBriefRole")+"";
                String dataStr = scheDate+"-"+scheDepNo+"-"+scheNewDepartName+"-"+ShowFlag+"-"+scheBriefRole;
                if(!oldDataList.contains(dataStr)){
                    oldDataList.add(dataStr);
                }
            }
            for(int i=0;i<oldDataList.size();i++){
                String [] dataInfo = oldDataList.get(i).split("-");
                HashMap updateData = new HashMap();
                String scheDate = dataInfo[0];
                String scheOrganNo = dataInfo[1];
                String scheDepartName = dataInfo[2];
                String scheClassNo = dataInfo[3];
                String scheBriefRole = dataInfo[4];

                updateData.put("scheDate", scheDate);
                updateData.put("scheDepNo", scheOrganNo);
                updateData.put("scheNewDepartName", scheDepartName);
                updateData.put("ShowFlag", scheClassNo);
                updateData.put("scheBriefRole", scheBriefRole);
                //导入前先将旧值更新为不排班状态
                baseMapper.scheduleExportOldValUpdate(updateData);
            }

            //excel文件数据保存到数据库
            for(int i=0;i<excelDataList.size();i++){
                HashMap saveMap = (HashMap)excelDataList.get(i);
                baseMapper.scheduleCalDateImportValUpdateSave(saveMap);
            }
            responseDO.setRetMsg("excel导入成功");
            responseDO.setRetCode(AOSConstants.HANDLE_SUCCESS);
            log.debug("排班信息excel导入成功");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, responseDO.getRetMsg(), retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    @Override
    public ReturnT<Map> scheduleExportModelDownload(Map<String, Object> params) {
        try {
            // 拼装返回信息
            Map retMap = new HashMap();
            log.debug("scheduleExportModelDownload-Start");
            // 前台参数集合
            String path = HttpUtil.getAbsolutePath("template");
            retMap.put("modelPath", path);
            log.debug("scheduleExportModelDownload-End");
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", retMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }

    }

    /**
     * 智能排班结果处室表格展示数据格式化
     *
     * @author:	OCK
     * @date:	2020年2月28日 上午10:53:30
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List intelScheTableDataFormat(String [] headArray,List dataList,Map depItermMap,List depItermList,String queryMonth){
        List resultList = new ArrayList();
        try{
            for(int i=0;i<depItermList.size();i++){
                Map resultMap = new HashMap();
                String centerDepNo = depItermList.get(i)+"";
                String centerDepName = depItermMap.get(centerDepNo)+"";
                String curCenterNo = centerDepNo.split("-")[0];
                String curCenterName = centerDepName.split("-")[0];
                String curDepNo = centerDepNo.split("-")[1];
                String curDepName = centerDepName.split("-")[1];
                resultMap.put("department_name", "总行运营管理部");
                resultMap.put("center_no", curCenterNo);
                resultMap.put("center_name", curCenterName);
                resultMap.put("dep_no", curDepNo);
                resultMap.put("dep_name", curDepName);
                resultMap.put("data_month", queryMonth);
                for(int j=0;j<headArray.length;j++){
                    String curDate = headArray[j];
                    String dataValue = "0";
                    for(int k=0;k<dataList.size();k++){
                        Map dataMap = (Map)dataList.get(k);
                        String actDepNo = dataMap.get("dep_no")+"";
                        String actDate = dataMap.get("work_date")+"";
                        if(actDepNo.equals(curDepNo) && actDate.equals(curDate)){
                            dataValue = dataMap.get("total_sche")+"";
                            break;
                        }
                    }
                    resultMap.put("day_"+curDate, dataValue);
                }
                resultList.add(resultMap);
            }
        }catch(Exception e){
            log.error("intelScheTableDataFormat数据组装格式化异常",e);
        }
        return resultList;
    }

    /**
     * 智能排班结果处室日历展示数据格式化
     *
     * @author:	OCK
     * @date:	2017年2月28日 上午10:53:30
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List intelScheCalDataFormat(String [] headArray,List dataList,Map centerDepInfoMap){
        List resultList = new ArrayList();
        try{
            for(int i=0;i<headArray.length;i++){
                Map dataMap = new HashMap();
                String curHeadDate = headArray[i];
                String curCenterNo = centerDepInfoMap.get("centerNo")+"";
                String curCenterName = centerDepInfoMap.get("centerName")+"";
                String curDepNo = centerDepInfoMap.get("depNo")+"";
                String curDepName = centerDepInfoMap.get("depName")+"";
                String intelScheClassA = "";
                String intelScheClassB = "";
                //获取A班的人员信息
                for(int j=0;j<dataList.size();j++){
                    Map curMap = (Map)dataList.get(j);
                    String actCenterNo = curMap.get("center_no")+"";
                    String actDepNo = curMap.get("dep_no")+"";
                    String actWorkDate = curMap.get("work_date")+"";
                    String actResult = curMap.get("result")+"";
                    String actUserName = curMap.get("user_name")+"";
                    if(curCenterNo.equals(actCenterNo) && curDepNo.equals(actDepNo)
                            && actWorkDate.equals(curHeadDate) && "1".equals(actResult)
                            && intelScheClassA.indexOf(actUserName) < 0){
                        intelScheClassA +=actUserName+",";
                    }
                }
                //获取B班的人员信息
                for(int j=0;j<dataList.size();j++){
                    Map curMap = (Map)dataList.get(j);
                    String actCenterNo = curMap.get("center_no")+"";
                    String actDepNo = curMap.get("dep_no")+"";
                    String actWorkDate = curMap.get("work_date")+"";
                    String actResult = curMap.get("result")+"";
                    String actUserName = curMap.get("user_name")+"";
                    if(curCenterNo.equals(actCenterNo) && curDepNo.equals(actDepNo)
                            && actWorkDate.equals(curHeadDate) && "2".equals(actResult)
                            && intelScheClassB.indexOf(actUserName) < 0){
                        intelScheClassB +=actUserName+",";
                    }
                }
                //如果A班有排版人员则去掉最后一个空格
                if(intelScheClassA.length()>2){
                    intelScheClassA = intelScheClassA.substring(0, intelScheClassA.length()-1);
                }
                //如果A班有排版人员则去掉最后一个空格
                if(intelScheClassB.length()>2){
                    intelScheClassB = intelScheClassB.substring(0, intelScheClassB.length()-1);
                }

                dataMap.put("sche_date", curHeadDate);
                dataMap.put("center_no", curCenterNo);
                dataMap.put("center_name", curCenterName);
                dataMap.put("dep_no", curDepNo);
                dataMap.put("dep_name", curDepName);
                dataMap.put("intelScheClassA", intelScheClassA);
                dataMap.put("intelScheClassB", intelScheClassB);

                resultList.add(dataMap);
            }
        }catch(Exception e){
            log.error("intelScheCalDataFormat智能排班日历数据组装格式化异常",e);
        }
        return resultList;
    }

    /**
     * 自动排班人员信息
     *
     * @author:	OCK
     * @date:	2017年2月28日 上午10:53:30
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List scheAgainNoticeUserList(List noticeUserList,HashMap condMap,String curDepNo,String curDepName,Map commonInfoMap,String publishInfo){
        try{
            //处室下所有排班用户查询
            List userList = BaseUtil.convertListMapKeyValue(baseMapper.intelScheAgainDepUserQuery(condMap));
            for(int j=0;j<userList.size();j++){
                Map userMap = (Map)userList.get(j);
                userMap.put("dep_no", curDepNo);
                userMap.put("dep_name", curDepName);
                String notice_content = commonInfoMap.get("todayStr")+":"+curDepName+","+publishInfo;
                userMap.put("notice_content", notice_content);
                userMap.put("notice_state", "0");
                userMap.put("create_user_no", commonInfoMap.get("createUserNo"));
                userMap.put("create_user_name", commonInfoMap.get("createUserName"));
                userMap.put("create_date", commonInfoMap.get("todayStr"));
                userMap.put("create_time", commonInfoMap.get("ceateTime"));
                noticeUserList.add(userMap);
            }
        }catch(Exception e){
            log.error("scheAgainNoticeUserList智能排班日历数据组装格式化异常",e);
        }
        return noticeUserList;
    }
}
