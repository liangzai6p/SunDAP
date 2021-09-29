package com.sunyard.dap.consumer.modelserver.client;

import com.sunyard.dap.common.model.ReturnT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Component
@FeignClient("sundap-modelserver")
public interface ModelClient {

    @PostMapping("/index/serachIndexName")
    ReturnT<List> serachIndexName(Map<String, Object> params);


    @PostMapping("/index/searchTellerOperation")
    ReturnT<List> searchTellerOperation(Map<String, Object> params);

    @PostMapping("/index/searchTellerCusMoney")
    ReturnT<List> searchTellerCusMoney(Map<String, Object> params);
}
