package com.sunyard.dap.dataserve.service.impl;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.BusiTcountDO;
import com.sunyard.dap.dataserve.mapper.BusiTcountMapper;
import com.sunyard.dap.dataserve.service.BusiTcountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listType(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
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
    public ReturnT<List> listTypeMonthly(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listTypeMonthly(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }
}
