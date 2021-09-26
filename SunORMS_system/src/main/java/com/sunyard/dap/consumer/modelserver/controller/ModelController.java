package com.sunyard.dap.consumer.modelserver.controller;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.consumer.modelserver.client.ModelClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/sundap/index")
public class ModelController {

    @Autowired
    private ModelClient client;

    @PostMapping("/serachIndexName")
    public ReturnT<List> serachIndexName(@RequestBody Map<String, Object> params){
        return client.serachIndexName(params);
    }


}
