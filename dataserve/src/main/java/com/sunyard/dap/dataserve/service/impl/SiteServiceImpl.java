package com.sunyard.dap.dataserve.service.impl;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.SiteDO;
import com.sunyard.dap.dataserve.mapper.SiteMapper;
import com.sunyard.dap.dataserve.service.SiteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

/**
 * <p>
 * 系统银行信息表 服务实现类
 * </p>
 *
 * @author yey.he
 * @since 2020-08-26
 */
@Service
public class SiteServiceImpl extends ServiceImpl<SiteMapper, SiteDO> implements SiteService {

    @Override
    public ReturnT<List> listBaseInfo(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listBaseInfo(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listSiteGrade(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listSiteGrade(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listTransStatus(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listTransStatus(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listHallInfo(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listHallInfo(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }


    @Override
    public ReturnT<List> listCashInfo(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listCashInfo(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listCashInfoBar(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> data=baseMapper.listCashInfo(params);
            List chart6x = new ArrayList();
            List chart6Y1=new ArrayList<>();
            List chart6Y2=new ArrayList<>();
            for (HashMap m:
                 data) {
                chart6x.add(m.get("HOUR")+":00");
                chart6Y1.add(m.get("IN_COUNT"));
                chart6Y2.add(m.get("OUT_COUNT"));
            }
            HashMap returnMap=new HashMap();
            returnMap.put("chart6x", chart6x);
            returnMap.put("chart6Y1", chart6Y1);
            returnMap.put("chart6Y2", chart6Y2);
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",returnMap);
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listQueHourly(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listQueHourly(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listQueHourlyLine(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> data=baseMapper.listQueHourly(params);
            List chart3X = new ArrayList();
            List chart3Y = new ArrayList();
            for (HashMap m:
                 data) {
                chart3X.add(m.get("HOUR")+":00");
                chart3Y.add(m.get("MAX_WAIT_LENGTH"));
            }
            HashMap returnMap = new HashMap();
            returnMap.put("chart3X",chart3X);
            returnMap.put("chart3Y",chart3Y);
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",returnMap);
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listMacBusiTypeCountHourly(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listMacBusiTypeCountHourly(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listMacBusiTypeCountHourlyLine(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> data=baseMapper.listMacBusiTypeCountHourly(params);
            List chart9X=new ArrayList();
            List chart9Y1=new ArrayList();
            List chart9Y2=new ArrayList();
            List chart9Y3=new ArrayList();
            chart9Y3.add(0);
            for (HashMap m:
                 data) {
                if (m.get("BUSI_NO").toString().equals("BUS007")){
                    chart9X.add(m.get("HOUR")+ ":00");
                    chart9Y1.add(m.get("COUNT"));
                }else if(m.get("BUSI_NO").toString().equals("BUS004")){
                    chart9Y2.add(m.get("COUNT"));
                }else if(m.get("BUSI_NO").toString().equals("BUS001")){
                    chart9Y3.add(m.get("COUNT"));
                }
            }
            HashMap returnMap = new HashMap();
            returnMap.put("chart9X", chart9X);
            returnMap.put("chart9Y1", chart9Y1);
            returnMap.put("chart9Y2", chart9Y2);
            returnMap.put("chart9Y3", chart9Y3);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE,"查询成功",returnMap);
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }
}
