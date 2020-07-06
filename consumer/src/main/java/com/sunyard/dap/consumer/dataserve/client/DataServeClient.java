package com.sunyard.dap.consumer.dataserve.client;

import com.sunyard.dap.common.model.ReturnT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * @program: SunDAP
 * @description: 数据服务模块
 * @author: yey.he
 * @create: 2020-06-12 16:01
 **/
@Component
@FeignClient("sundap-dataserve")
public interface DataServeClient {
    /**
     * 离线运营日度业务量总数据
     * @Author yey.he
     * @Date 3:26 PM 2020/6/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/busiCount/info")
    ReturnT<List> getInfo(Map<String,Object> params);

    /**
     * 分渠道离线日度运营业务量
     * @Author yey.he
     * @Date 3:27 PM 2020/6/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/busiCount/channel")
    ReturnT<List> getChannelInfo(Map<String,Object> params);

    /**
     * 分行离线日度运营业务量
     * @Author yey.he
     * @Date 3:27 PM 2020/6/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/busiCount/branch")
    ReturnT<List> getBranchInfo(Map<String,Object> params);

    /**
     * 网点离线日度运营业务量
     * @Author yey.he
     * @Date 3:28 PM 2020/6/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/busiCount/site")
    ReturnT<List> getSiteInfo(Map<String,Object> params);

    /**
     * 区域离线日度运营业务量
     * @Author yey.he
     * @Date 3:28 PM 2020/6/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/busiCount/zone")
    ReturnT<List> getZoneInfo(Map<String,Object> params);

    /**
     * 月度离线运营业务量
     * @Author yey.he
     * @Date 11:26 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/busiCount/infoMonthly")
    ReturnT<List> getInfoMonthly(Map<String,Object> params);

    /**
     * 分行月度离线运营业务量
     * @Author yey.he
     * @Date 11:27 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/busiCount/branchMonthly")
    ReturnT<List> getBranchMonthly(Map<String,Object> params);

    /**
     * 区域月度离线运营业务量
     * @Author yey.he
     * @Date 11:28 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/busiCount/zoneMonthly")
    ReturnT<List> getZoneMonthly(Map<String,Object> params);
    /**
     * 网点月度离线运营业务量
     * @Author yey.he
     * @Date 11:28 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/busiCount/siteMonthly")
    ReturnT<List> getSiteMonthly(Map<String,Object> params);

    /**
     * 渠道月度离线运营业务量
     * @Author yey.he
     * @Date 11:28 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/busiCount/channelMonthly")
    ReturnT<List> getChannelMonthly(Map<String,Object> params);

    @PostMapping("/busiRT/totalH")
    ReturnT<List> listHRT(Map<String,Object> params);

    @PostMapping("/busiRT/branchH")
    ReturnT<List> listBranchHRT(Map<String,Object> params);

    @PostMapping("/busiRT/zoneH")
    ReturnT<List> listZoneHRT(Map<String,Object> params);

    @PostMapping("/busiRT/siteH")
    ReturnT<List> listSiteHRT(Map<String,Object> params);

    @PostMapping("/busiRT/channelH")
    ReturnT<List> listChannelHRT(Map<String,Object> params);

    @PostMapping("/busiRT/totalD")
    ReturnT<List> listDRT(Map<String,Object> params);

    @PostMapping("/busiRT/branchD")
    ReturnT<List> listBranchDRT(Map<String,Object> params);

    @PostMapping("/busiRT/zoneD")
    ReturnT<List> listZoneDRT(Map<String,Object> params);

    @PostMapping("/busiRT/siteD")
    ReturnT<List> listSiteDRT(Map<String,Object> params);

    @PostMapping("/busiRT/channelD")
    ReturnT<List> listChannelDRT(Map<String,Object> params);
}
