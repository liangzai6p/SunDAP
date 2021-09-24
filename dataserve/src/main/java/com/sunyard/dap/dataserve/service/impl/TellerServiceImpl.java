package com.sunyard.dap.dataserve.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.common.util.CommonUtil;
import com.sunyard.dap.dataserve.entity.DmTellerTb;
import com.sunyard.dap.dataserve.mapper.TellerMapper;
import com.sunyard.dap.dataserve.service.TellerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yey.he
 * @since 2020-08-14
 */
@Service
public class TellerServiceImpl extends ServiceImpl<TellerMapper, DmTellerTb> implements TellerService {

    @Override
    public ReturnT<Page<HashMap<String, Object>>> listTeller(Map<String, Object> params) {
        Page<HashMap> page = new Page<>();
        CommonUtil.setPageByParams(page,params);
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listTeller(page,params));
        }catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"查询失败",null);
        }
    }

    @Override
    public ReturnT<Page<HashMap<String, Object>>> listTellerAssess(Map<String, Object> params) {
        Page<HashMap> page = new Page<>();
        CommonUtil.setPageByParams(page,params);
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listTellerAssess(page,params));
        }catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"查询失败",null);
        }
    }

    @Override
    public ReturnT<Page<HashMap<String, Object>>> listTellerAssessCompreScore(Map<String, Object> params) {
        Page<HashMap> page = new Page<>();
        CommonUtil.setPageByParams(page,params);
        try {
        Page<HashMap<String, Object>> content=baseMapper.listTellerAssess(page,params);
        List<HashMap<String,Object>> list=content.getRecords();
        List<HashMap<String,Object>> score=new ArrayList<>();
        for (HashMap<String,Object> map:
             list) {
            HashMap<String,Object> scoreMap=new HashMap<String,Object>();
            scoreMap.put("COMPRE_SCORE",map.get("COMPRE_SCORE"));
            score.add(scoreMap);
        }
        content.setRecords(score);

            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",content);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"查询失败",null);
        }
    }

    @Override
    public ReturnT<Map> listTellerAssessChartDate(Map<String, Object> params) {
        Page<HashMap> page = new Page<>();
        CommonUtil.setPageByParams(page,params);
        try {
        Page<HashMap<String, Object>> content=baseMapper.listTellerAssess(page,params);
        List data1=new ArrayList();
        List data2=new ArrayList();
        List data3=new ArrayList();
        List data4=new ArrayList();
        List data5=new ArrayList();
        List data6=new ArrayList();
        List data7=new ArrayList();
        List<HashMap<String,Object>> list=content.getRecords();
        data1.add(list.get(0).get("EXP"));
        data1.add(list.get(0).get("SKILLS"));
        data1.add(list.get(0).get("BEHAVIOR"));
        data1.add(list.get(0).get("TASK"));
        data1.add(list.get(0).get("OCCUP_QUAL"));
        int i=0;//用于计数
        for (HashMap<String,Object> map:
                list) {
            data2.add(map.get("COMPRE_SCORE"));
            if (i==2){
                Float f=(Float.parseFloat(map.get("ATTENDANCE_DAY").toString())/31)*100;
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);//用于保留两位小数
                data4.add(df.format(f));

            }else{
                Float f=(Float.parseFloat(map.get("ATTENDANCE_DAY").toString())/30)*100;
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);//用于保留两位小数
                data4.add(df.format(f));
            }
            data5.add(map.get("ROLLBACK_RATE"));
            data6.add(map.get("ERROR_RATE"));
            data3.add(map.get("TRANS_COUNT"));
            data7.add(map.get("CUS_SATISFACTION"));
            i++;
        }
        HashMap<String, Object> chatData = new HashMap<>();
        chatData.put("data1",data1);
        chatData.put("data2",data2);
        chatData.put("data3",data3);
        chatData.put("data4",data4);
        chatData.put("data5",data5);
        chatData.put("data6",data6);
        chatData.put("data7",data7);

        return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",chatData);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"查询失败",null);
        }
    }

    @Override
    public ReturnT<Page<HashMap<String, Object>>> listTellerGrade(Map<String, Object> params) {
        Page<HashMap> page = new Page<>();
        CommonUtil.setPageByParams(page,params);
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listTellerGrade(page,params));
        }catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"查询失败",null);
        }
    }

    @Override
    public ReturnT<List> listStatus(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listStatus(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listBranchOffline(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listBranchOffline(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listBranchOfflineBarChart(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> tellerListBranchOffline = baseMapper.listBranchOffline(params);
            List yAxis=new ArrayList();
            List xAxis=new ArrayList();
            List channelOrgan=new ArrayList();
            /*HashMap temp = new HashMap(); //数据排序代码
            for (int i = 0; i < tellerListBranchOffline.size()-1; i++) { //由于数据库已经做好了排序所以此时放弃数据排序
                for (int j = 0; j < tellerListBranchOffline.size()-i-1; j++) {
                    if (Integer.parseInt(tellerListBranchOffline.get(j).get("COUNT").toString()) < Integer.parseInt(tellerListBranchOffline.get(j+1).get("COUNT").toString())) {
                        temp = tellerListBranchOffline.get(j);
                        tellerListBranchOffline.set(j,tellerListBranchOffline.get(j+1));
                        tellerListBranchOffline.set(j+1,temp);
                    }
                }
            }*/
            for (int i = 9; i >= 0; i--) {
                yAxis.add(tellerListBranchOffline.get(i).get("COUNT"));
                xAxis.add(tellerListBranchOffline.get(i).get("BRANCH_NAME"));
                channelOrgan.add(tellerListBranchOffline.get(i).get("BRANCH_NO"));
            }
            HashMap returnMap = new HashMap();
            returnMap.put("yAxis", yAxis);
            returnMap.put("xAxis", xAxis);
            returnMap.put("channelOrgan", channelOrgan);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE,"查询成功",returnMap);
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listSiteOffline(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listSiteOffline(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listSiteOfflineBar(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> tellerListBranchOffline = baseMapper.listSiteOffline(params);
            List yAxis=new ArrayList();
            List xAxis=new ArrayList();
            List busiCountOrgan=new ArrayList();
            /*HashMap temp = new HashMap(); //数据排序代码
            for (int i = 0; i < tellerListBranchOffline.size()-1; i++) { //由于数据库已经做好了排序所以此时放弃数据排序
                for (int j = 0; j < tellerListBranchOffline.size()-i-1; j++) {
                    if (Integer.parseInt(tellerListBranchOffline.get(j).get("COUNT").toString()) < Integer.parseInt(tellerListBranchOffline.get(j+1).get("COUNT").toString())) {
                        temp = tellerListBranchOffline.get(j);
                        tellerListBranchOffline.set(j,tellerListBranchOffline.get(j+1));
                        tellerListBranchOffline.set(j+1,temp);
                    }
                }
            }*/
            for (int i = 9; i >= 0; i--) {
                yAxis.add(tellerListBranchOffline.get(i).get("COUNT"));
                xAxis.add(tellerListBranchOffline.get(i).get("SITE_NAME"));
                busiCountOrgan.add(tellerListBranchOffline.get(i).get("SITE_NO"));
            }
            HashMap returnMap = new HashMap();
            returnMap.put("yAxis", yAxis);
            returnMap.put("xAxis", xAxis);
            returnMap.put("busiCountOrgan", busiCountOrgan);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE,"查询成功",returnMap);
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listRoleStatus(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listRoleStatus(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listRoleStatusBar(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> data = baseMapper.listRoleStatus(params);
            List chart101Y = new ArrayList();
            List chart101X1=new ArrayList<>();
            List chart101X2=new ArrayList<>();
            for (HashMap m:
                    data) {
                chart101Y.add(m.get("ROLE_NAME"));
                chart101X1.add(m.get("ONLINE_COUNT"));
                chart101X2.add(m.get("OFFLINE_COUNT"));
            }
            HashMap returnMap=new HashMap();
            returnMap.put("chart101Y", chart101Y);
            returnMap.put("chart101X1", chart101X1);
            returnMap.put("chart101X2", chart101X2);
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",returnMap);
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<Page<HashMap<String, Object>>> listTellerRank(Map<String, Object> params) {
        Page<HashMap> page = new Page<>();
        CommonUtil.setPageByParams(page,params);
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listTellerRank(page,params));
        }catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"查询失败",null);
        }    }

    @Override
    public ReturnT<Map> listTellerRankBar(Map<String, Object> params) {
        Page<HashMap> page = new Page<>();
        CommonUtil.setPageByParams(page,params);
        try {
            Page<HashMap<String,Object>> dataPage=baseMapper.listTellerRank(page,params);
            List<HashMap<String,Object>> data=dataPage.getRecords();
            String[] x=new String[data.size()];
            String[] y=new String[data.size()];
            List tellerListTellerRankNum = new ArrayList();
            int index = data.size()-1;
            for (HashMap m:
                 data) {
//                chart4X.add(index,m.get("COMPRE_SCORE"));
//                chart4Y.add(index,m.get("TELLER_NAME"));
                x[index]=m.get("COMPRE_SCORE").toString();
                y[index]=m.get("TELLER_NAME").toString();
                tellerListTellerRankNum.add(m.get("TELLER_NO"));
                index--;
            }
            List chart4X = Arrays.asList(x);
            List chart4Y = Arrays.asList(y);
            HashMap returnMap = new HashMap();
            returnMap.put("chart4X",chart4X);
            returnMap.put("chart4Y",chart4Y);
            returnMap.put("tellerListTellerRankNum",tellerListTellerRankNum);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE,"查询成功",returnMap);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"查询失败",null);
        }
    }

    @Override
    public ReturnT<List> listByBranch(Map<String, Object> params) {
        try {
          //存放下拉列表框的data
            List<HashMap<String,Object>> list= baseMapper.listByBranch(params);
            List<HashMap<String,Object>> items=new ArrayList<>();
            for (HashMap m: list) {//提取柜员姓名和站点银行编号
                HashMap item = new HashMap();
                item.put("name",m.get("TELLER_NAME"));
                item.put("value",m.get("TELLER_NO"));
                items.add(item);
            }
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",items);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"查询失败",null);
        }
    }

    @Override
    public ReturnT<Page<HashMap<String, Object>>> listTellerErrorDetails(Map<String, Object> params) {
        Page<HashMap> page = new Page<>();
        CommonUtil.setPageByParams(page,params);
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listTellerErrorDetails(page,params));
        }catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"查询失败",null);
        }
    }
}
