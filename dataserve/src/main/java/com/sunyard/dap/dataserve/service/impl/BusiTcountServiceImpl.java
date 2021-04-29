package com.sunyard.dap.dataserve.service.impl;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.BusiTcountDO;
import com.sunyard.dap.dataserve.mapper.BusiTcountMapper;
import com.sunyard.dap.dataserve.service.BusiTcountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yey.he
 * @since 2020-07-14
 */
@Service
@Slf4j
public class BusiTcountServiceImpl extends ServiceImpl<BusiTcountMapper, BusiTcountDO> implements BusiTcountService {
    @Override
    public ReturnT<List> listType(Map<String,Object> params) {
        try {
            if (params.get("QUICK")==null){
                params.put("BRANCH_NO",params.get("BRANCH_NO").toString());
            }
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listType(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }

    }

    @Override
    public ReturnT<Map> listTypePie(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> data=baseMapper.listType(params);
            List<HashMap<String,Object>> seriesDatas1=new ArrayList<>();
            List BQ1=new ArrayList<>();
            Long data21=0L;
            for (HashMap item:
                    data) {
                HashMap temp = new HashMap();
                temp.put("name", item.get("BUSI_NAME") + ":" + item.get("BUSI_COUNT"));
                temp.put("value", item.get("BUSI_COUNT"));
                BQ1.add(item.get("BUSI_NAME") + ":" + item.get("BUSI_COUNT"));
                seriesDatas1.add(temp);
                data21=data21+Long.parseLong(item.get("BUSI_COUNT").toString());
            }
            HashMap returnMap = new HashMap();
            returnMap.put("seriesDatas1", seriesDatas1);
            returnMap.put("data21", data21);
            returnMap.put("BQ1", BQ1);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listBranch(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listBranch(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listSite(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listSite(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listZone(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listZone(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listChannel(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listChannel(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listChannelPie(Map<String, Object> params) {
        try {
            List<HashMap<String,Object>> data=baseMapper.listChannel(params);
            List<HashMap<String,Object>> seriesDatas1=new ArrayList<>();
            List BQ2=new ArrayList<>();
            Long data20=0L;
            for (HashMap item:
                    data) {
                HashMap temp = new HashMap();
                temp.put("name", item.get("CHANNEL_NAME") + ":" + item.get("BUSI_COUNT"));
                temp.put("value", item.get("BUSI_COUNT"));
                BQ2.add(item.get("CHANNEL_NAME") + ":" + item.get("BUSI_COUNT"));
                seriesDatas1.add(temp);
                data20=data20+Long.parseLong(item.get("BUSI_COUNT").toString());
            }
            HashMap returnMap = new HashMap();
            returnMap.put("seriesDatas2", seriesDatas1);
            returnMap.put("data20", data20);
            returnMap.put("BQ2", BQ2);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", returnMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listTypeMonthly(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listTypeMonthly(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }
}
