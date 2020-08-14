package com.sunyard.dap.dataserve.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.common.util.CommonUtil;
import com.sunyard.dap.dataserve.entity.BusiDetailDO;
import com.sunyard.dap.dataserve.entity.MacDO;
import com.sunyard.dap.dataserve.mapper.MacMapper;
import com.sunyard.dap.dataserve.service.MacService;
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
 * @since 2020-08-12
 */
@Service
public class MacServiceImpl extends ServiceImpl<MacMapper, MacDO> implements MacService {

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
    public ReturnT<List> listFaultByTime(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listFaultByTime(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Page<HashMap<String, Object>>> listDetail(Map<String, Object> params) {
        Page<HashMap> page = new Page<>();
        CommonUtil.setPageByParams(page,params);
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listDetail(page,params));
        }catch (Exception e){
            log.error(e.getMessage());
            return new ReturnT<>(ReturnT.FAIL_CODE,"查询失败",null);
        }
    }

    @Override
    public ReturnT<List> listBranchFaultRate(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listBranchFaultRate(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }    }
}
