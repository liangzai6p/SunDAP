package com.sunyard.dap.dataserve.service.impl;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.SmBanksTb;
import com.sunyard.dap.dataserve.mapper.ISmBanksTbMapper;
import com.sunyard.dap.dataserve.service.ISmBanksTbService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统银行信息表 服务实现类
 * </p>
 *
 * @author xiao.xie
 * @since 2021-03-01
 */
@Service
public class SmBanksTbServiceImpl extends ServiceImpl<ISmBanksTbMapper, SmBanksTb> implements ISmBanksTbService {

    @Override
    public ReturnT<List> listBank(Map<String, Object> params) {
        try {
            //搜索处理
            if (!StringUtils.isEmpty(params.get("Keyword"))) {
                String keyword = new StringBuilder().append("%").append(params.get("Keyword"))
                        .append("%").toString();
                params.put("keyword",keyword);
            }
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", baseMapper.listBank(params));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listBankDiaLogData(Map<String, Object> params) {
        try {
            //搜索处理
            if (!StringUtils.isEmpty(params.get("Keyword"))) {
                String keyword = new StringBuilder().append("%").append(params.get("Keyword"))
                        .append("%").toString();
                params.put("keyword",keyword);
            }
            List<HashMap<String,Object>> bankData=baseMapper.listBank(params);
            List<HashMap<String,Object>> selectData=new ArrayList<>();
            HashMap<String,String> temp1 = new HashMap();
            for (HashMap item: bankData) {
                HashMap temp = new HashMap();
                String name=item.get("SITE_NO").toString()+"-"+item.get("SITE_NAME").toString();
                temp.put("name",name);
                temp.put("value",item.get("SITE_NO"));
                String name1=item.get("BRANCH_NO").toString()+"-"+item.get("BRANCH_NAME").toString();
                temp1.put(name1,item.get("BRANCH_NO").toString());
                selectData.add(temp);
            }
            for (Map.Entry<String,String> entry: temp1.entrySet()) {
                HashMap temp = new HashMap();
                temp.put("name",entry.getKey());
                temp.put("value",entry.getValue());
                selectData.add(temp);
            }
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", selectData);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listBranchBankDiaLogData(Map<String, Object> params) {
        try {
            //搜索处理
            if (!StringUtils.isEmpty(params.get("Keyword"))) {
                String keyword = new StringBuilder().append("%").append(params.get("Keyword"))
                        .append("%").toString();
                params.put("keyword",keyword);
            }
            List<HashMap<String,Object>> bankData=baseMapper.listBank(params);
            List<HashMap<String,Object>> selectData=new ArrayList<>();
            HashMap<String,String> temp1 = new HashMap();
            for (HashMap item: bankData) {
                String name1=item.get("BRANCH_NO").toString()+"-"+item.get("BRANCH_NAME").toString();
                temp1.put(name1,item.get("BRANCH_NO").toString());
            }
            for (Map.Entry<String,String> entry: temp1.entrySet()) {
                HashMap temp = new HashMap();
                temp.put("name",entry.getKey());
                temp.put("value",entry.getValue());
                selectData.add(temp);
            }
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", selectData);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> listsubBranchBankDiaLogData(Map<String, Object> params) {
        try {
            //搜索处理
            if (!StringUtils.isEmpty(params.get("Keyword"))) {
                String keyword = new StringBuilder().append("%").append(params.get("Keyword"))
                        .append("%").toString();
                params.put("keyword",keyword);
            }
            List<HashMap<String,Object>> bankData=baseMapper.listBank(params);
            List<HashMap<String,Object>> selectData=new ArrayList<>();
            for (HashMap item: bankData) {
                HashMap temp = new HashMap();
                String name=item.get("SITE_NO").toString()+"-"+item.get("SITE_NAME").toString();
                temp.put("name",name);
                temp.put("value",item.get("SITE_NO"));
                selectData.add(temp);
            }
            return new ReturnT<>(ReturnT.SUCCESS_CODE, "查询成功", selectData);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

}
