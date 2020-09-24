package com.sunyard.dap.dataserve.service.impl;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.BusiCountDO;
import com.sunyard.dap.dataserve.mapper.BusiCountMapper;
import com.sunyard.dap.dataserve.service.BusiCountService;
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
 * @since 2020-06-12
 */
@Service
@Slf4j
public class BusiCountServiceImpl extends ServiceImpl<BusiCountMapper, BusiCountDO> implements BusiCountService {

    @Override
    public ReturnT<List> allCount(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.allCount(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }

    }

    @Override
    public ReturnT<List> allDaily(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.allDaily(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }

    }

    @Override
    public ReturnT<List> branchDaily(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.branchDaily(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> siteDaily(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.siteDaily(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> zoneDaily(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.zoneDaily(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> channelDaily(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.channelDaily(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> allMonthly(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.allMonthly(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> branchMonthly(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.branchMonthly(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> zoneMonthly(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.zoneMonthly(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> siteMonthly(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.siteMonthly(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> channelMonthly(Map<String,Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.channelMonthly(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> branchCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.branchCount(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }    }

    @Override
    public ReturnT<List> zoneCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.zoneCount(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> siteCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.siteCount(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<List> channelCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.channelCount(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }


}
