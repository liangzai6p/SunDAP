package com.sunyard.dap.consumer.feign.system;

import com.sunyard.dap.common.entity.Test;
import com.sunyard.dap.common.model.ReturnT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @program: SunDAP
 * @description: TestClient
 * @author: yey.he
 * @create: 2020-06-04 10:13
 **/
@Component
@FeignClient(value = "sundap-system")
public interface TestClient {
    @GetMapping(value = "/test/get/{id}")
    public ReturnT<Test> getById(@PathVariable("id")int id);

    @GetMapping(value="/test/info")
    public ReturnT<String> getInfo();

    @PostMapping(value = "/test/save")
    public ReturnT saveOrUpdate(@RequestBody Test test);

    @GetMapping("/test/port")
    public String getPort();
}
