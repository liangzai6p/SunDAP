package com.sunyard.dap.dataserve.controller;


import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.service.BusiRtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yey.he
 * @since 2020-07-03
 */
@RestController
@RequestMapping("/busiRT")
@Slf4j
@RefreshScope
public class BusiRtController {
    @Autowired
    private BusiRtService service;
    
    /**
     * 实时小时总运营业务量
     * @Author yey.he 
     * @Date 10:33 AM 2020/7/6
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/totalH")
    public ReturnT<List> listHRT(@RequestBody Map<String,Object> params){
        return service.listHRT(params);
    }

    /**
     * 分行实时小时运营业务量
     * @Author yey.he 
     * @Date 10:33 AM 2020/7/6
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/branchH")
    public ReturnT<List> listBranchHRT(@RequestBody Map<String,Object> params){
        return service.listBranchHRT(params);
    }

    /**
     * 区域实时小时运营业务量
     * @Author yey.he 
     * @Date 10:33 AM 2020/7/6
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/zoneH")
    public ReturnT<List> listZoneHRT(@RequestBody Map<String,Object> params){
        return service.listZoneHRT(params);
    }

    /**
     * 网点实时小时运营业务量
     * @Author yey.he 
     * @Date 10:33 AM 2020/7/6
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/siteH")
    public ReturnT<List> listSiteHRT(@RequestBody Map<String,Object> params){
        return service.listSiteHRT(params);
    }

    /**
     * 渠道实时小时运营业务量
     * @Author yey.he 
     * @Date 10:34 AM 2020/7/6
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/channelH")
    public ReturnT<List> listChannelHRT(@RequestBody Map<String,Object> params){
        return service.listChannelHRT(params);
    }

    /**
     * 当日实时总运营业务量
     * @Author yey.he 
     * @Date 10:34 AM 2020/7/6
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/totalD")
    public ReturnT<List> listDRT(@RequestBody Map<String,Object> params){
        return service.listDRT(params);
    }

    /**
     * 当日实时分行运营业务量
     * @Author yey.he 
     * @Date 10:34 AM 2020/7/6
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/branchD")
    public ReturnT<List> listBranchDRT(@RequestBody Map<String,Object> params){
        return service.listBranchDRT(params);
    }

    /**
     * 当日实时区域运营业务量
     * @Author yey.he 
     * @Date 10:34 AM 2020/7/6
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/zoneD")
    public ReturnT<List> listZoneDRT(@RequestBody Map<String,Object> params){
        return service.listZoneDRT(params);
    }

    /**
     * 当日实时网点运营业务量
     * @Author yey.he 
     * @Date 10:35 AM 2020/7/6
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/siteD")
    public ReturnT<List> listSiteDRT(@RequestBody Map<String,Object> params){
        return service.listSiteDRT(params);
    }

    /**
     * 当日实时渠道运营业务量
     * @Author yey.he 
     * @Date 10:35 AM 2020/7/6
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/channelD")
    public ReturnT<List> listChannelDRT(@RequestBody Map<String,Object> params){
        return service.listChannelDRT(params);
    }

    /**
     * 实时交易明细状态查询
     * @Author yey.he
     * @Date 11:34 AM 2020/7/20
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/listByState")
    public ReturnT<List> listByState(@RequestBody Map<String,Object> params){
        return service.listByState(params);
    }

    /**
     * 实时交易状态统计
     * @Author yey.he
     * @Date 5:01 PM 2020/8/6
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    @PostMapping("/countState")
    public ReturnT<List> countState(@RequestBody Map<String,Object> params){
        return service.countState(params);
    }

}

