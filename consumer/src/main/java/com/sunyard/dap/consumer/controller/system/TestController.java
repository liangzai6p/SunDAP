package com.sunyard.dap.consumer.controller.system;

import com.sunyard.dap.common.entity.Test;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.consumer.feign.system.TestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: SunDAP
 * @description: consumer-TestController
 * @author: yey.he
 * @create: 2020-06-04 10:47
 **/
@RestController
@Slf4j
@RequestMapping("/client/test")
public class TestController {
    @Resource
    private TestClient testClient;

    @GetMapping(value = "/get/{id}")
    public ReturnT<Test> getById(@PathVariable("id")int id){
        return testClient.getById(id);
    }

    @GetMapping(value="/info")
    public ReturnT<String> getInfo(){
        return testClient.getInfo();
    }

    @GetMapping(value = "/save")
    public ReturnT saveOrUpdate(@RequestBody Test test){
        return testClient.saveOrUpdate(test);
    }

    @GetMapping("/port")
    public String getPort(){
        return testClient.getPort();
    }
}
