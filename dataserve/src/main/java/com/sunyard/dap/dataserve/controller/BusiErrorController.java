package com.sunyard.dap.dataserve.controller;


import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.service.BusiCountService;
import com.sunyard.dap.dataserve.service.BusiErrorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yey.he
 * @since 2020-08-20
 */
@RestController
@RequestMapping("/busiError")
@Slf4j
@RefreshScope
public class BusiErrorController {
    @Autowired
    private BusiErrorService service;

    /**
     * 分行业务差错量排名
     * @Author yey.he
     * @Date 5:28 PM 2020/8/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listBranchErrorCount")
    public ReturnT<List> listBranchErrorCount(@RequestBody Map<String,Object> params){
        return service.listBranchErrorCount(params);
    }

}

