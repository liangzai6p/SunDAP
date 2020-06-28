package com.sunyard.dap.dataserve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.BusiCountDO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yey.he
 * @since 2020-06-12
 */
public interface BusiCountService extends IService<BusiCountDO>{
    /**
     * 离线运营业务量总数据
     * @Author yey.he
     * @Date 3:31 PM 2020/6/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> list(Map<String,Object> params);

    /**
     * 分行离线运营业务量
     * @Author yey.he
     * @Date 3:31 PM 2020/6/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> listBranch(Map<String,Object> params);

    /**
     * 网点离线运营业务量
     * @Author yey.he
     * @Date 3:31 PM 2020/6/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> listSite(Map<String,Object> params);

    /**
     * 区域离线运营业务量
     * @Author yey.he
     * @Date 3:31 PM 2020/6/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> listZone(Map<String,Object> params);

    /**
     * 分渠道离线运营业务量
     * @Author yey.he
     * @Date 3:31 PM 2020/6/17
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> listChannel(Map<String,Object> params);

    /**
     * 月度离线运营业务量
     * @Author yey.he
     * @Date 11:18 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> listMonthly(Map<String,Object> params);

    /**
     * 分行月度离线运营业务量
     * @Author yey.he
     * @Date 11:18 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> listBranchMonthly(Map<String,Object> params);

    /**
     * 区域月度离线运营业务量
     * @Author yey.he
     * @Date 11:19 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> listZoneMonthly(Map<String,Object> params);

    /**
     * 网点月度离线运营业务量
     * @Author yey.he
     * @Date 11:19 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> listSiteMonthly(Map<String,Object> params);

    /**
     * 渠道月度离线运营业务量
     * @Author yey.he
     * @Date 11:20 AM 2020/6/18
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> listChannelMonthly(Map<String,Object> params);
}
