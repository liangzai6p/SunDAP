package com.sunyard.dap.modelserver.controller;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.modelserver.service.IndexServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/index")
@Slf4j
@RefreshScope
public class IndexController {
    @Autowired
    private IndexServer indexServer;

    @PostMapping("serachIndexName")
    public ReturnT<List> serachIndexName(@RequestBody  Map<String, Object> params){
        return indexServer.serachIndexName(params);
    }

}
