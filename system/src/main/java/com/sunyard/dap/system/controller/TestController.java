package com.sunyard.dap.system.controller;


import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.common.entity.Test;
import com.sunyard.dap.system.service.ITestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yey.he
 * @since 2020-06-03
 */
@RestController
@RequestMapping("/test")
@Slf4j
@RefreshScope
public class TestController {
    @Autowired
    private ITestService iTestService;

    @Value("${server.port}")
    private String serverPort;

    @Value("${sundap.param}")
    private String testParam;

    @GetMapping(value = "/get/{id}")
    public ReturnT<Test> getById(@PathVariable("id")int id){
        Test test = iTestService.getById(id);
        if (test!=null){
            return new ReturnT<Test>(ReturnT.SUCCESS_CODE,"查询成功",test);
        }else {
            return new ReturnT<Test>(ReturnT.FAIL_CODE,"该对象不存在",null);
        }
    }

    @GetMapping(value="/info")
    public ReturnT<String> getInfo(){
        List<Test> list = iTestService.list();
        log.info(list.toString());
        return new ReturnT<String>(ReturnT.SUCCESS_CODE,"查询成功",list.toString());
    }

    @PostMapping(value = "/save")
    public ReturnT saveOrUpdate(@RequestBody Test test){
        boolean flag = iTestService.saveOrUpdate(test);
        if (flag){
            return new ReturnT(ReturnT.SUCCESS_CODE,"保存成功");
        }else {
            return new ReturnT(ReturnT.FAIL_CODE,"保存失败");
        }
    }

    @GetMapping("/port")
    public String getPort(){
        return "port: "+serverPort+" , param: "+testParam;
    }


}

