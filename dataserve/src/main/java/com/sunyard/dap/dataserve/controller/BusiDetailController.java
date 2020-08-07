package com.sunyard.dap.dataserve.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.BusiDetailDO;
import com.sunyard.dap.dataserve.service.BusiDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yey.he
 * @since 2020-08-05
 */
@RestController
@RequestMapping("/busiDetail")
@Slf4j
@RefreshScope
public class BusiDetailController {
    @Autowired
    private BusiDetailService service;

    /**
     * 获取当日交易明细
     * @Author yey.he
     * @Date 2:44 PM 2020/8/6
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.core.metadata.IPage<java.util.HashMap<java.lang.String,java.lang.Object>>>
     **/
    @PostMapping("listRt")
    public ReturnT<Page<HashMap<String, Object>>> listRtDetail(@RequestBody Map<String,Object> params){
        return service.listRtDetail(params);
    }

    /**
     * 获取历史交易明细
     * @Author yey.he
     * @Date 2:44 PM 2020/8/6
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<com.baomidou.mybatisplus.core.metadata.IPage<java.util.HashMap<java.lang.String,java.lang.Object>>>
     **/
    @PostMapping("listHistory")
    public ReturnT<Page<HashMap<String, Object>>> listHistoryDetail(@RequestBody Map<String,Object> params){
        return service.listHistoryDetail(params);
    }
}

