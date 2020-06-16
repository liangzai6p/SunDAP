package com.sunyard.dap.consumer.feign.dataserve;

import com.sunyard.dap.common.model.ReturnT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @program: SunDAP
 * @description: 离线运营业务量
 * @author: yey.he
 * @create: 2020-06-12 16:01
 **/
@Component
@FeignClient("sundap-dataserve")
public interface DmBusiOfflineClient {
    @GetMapping("/dmBusiOffline/info")
    public ReturnT<List> getInfo();

    @GetMapping("/dmBusiOffline/channel")
    public ReturnT<List> getChannelInfo();

    @GetMapping("/dmBusiOffline/branch")
    public ReturnT<List> getBranchInfo();

    @GetMapping("/dmBusiOffline/site")
    public ReturnT<List> getSiteInfo();

    @GetMapping("/dmBusiOffline/zone")
    public ReturnT<List> getZoneInfo();
}
