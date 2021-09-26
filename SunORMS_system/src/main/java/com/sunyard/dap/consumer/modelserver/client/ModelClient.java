package com.sunyard.dap.consumer.modelserver.client;

import com.sunyard.dap.common.model.ReturnT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@Component
@FeignClient("sundap-modelserver")
public interface ModelClient {

    @PostMapping("/index/serachIndexName")
    ReturnT<List> serachIndexName(Map<String, Object> params);


}
