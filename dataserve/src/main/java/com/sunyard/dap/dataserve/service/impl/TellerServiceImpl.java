package com.sunyard.dap.dataserve.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.common.util.CommonUtil;
import com.sunyard.dap.dataserve.entity.DmTellerTb;
import com.sunyard.dap.dataserve.mapper.TellerMapper;
import com.sunyard.dap.dataserve.service.TellerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ReturnT<List> listSiteOffline(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listSiteOffline(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
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
    public ReturnT<Page<HashMap<String, Object>>> listTellerRank(Map<String, Object> params) {
        Page<HashMap> page = new Page<>();
        CommonUtil.setPageByParams(page,params);
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listTellerRank(page,params));
        }catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"查询失败",null);
        }    }
}
