package com.sunyard.dap.dataserve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.BusiCount;

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
public interface BusiCountService extends IService<BusiCount>{
    /**
     * 离线运营业务量总数据
     * @Author yey.he
     * @Date 3:31 PM 2020/6/17
     * @Param []
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> info(Map<String,Object> params);

    /**
     * 分行离线运营业务量
     * @Author yey.he
     * @Date 3:31 PM 2020/6/17
     * @Param []
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> branchView(Map<String,Object> params);

    /**
     * 网点离线运营业务量
     * @Author yey.he
     * @Date 3:31 PM 2020/6/17
     * @Param []
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> siteView(Map<String,Object> params);

    /**
     * 区域离线运营业务量
     * @Author yey.he
     * @Date 3:31 PM 2020/6/17
     * @Param []
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> zoneView(Map<String,Object> params);

    /**
     * 分渠道离线运营业务量
     * @Author yey.he
     * @Date 3:31 PM 2020/6/17
     * @Param []
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> channelView(Map<String,Object> params);

    /**
     * 月度离线运营业务量
     * @Author yey.he
     * @Date 11:18 AM 2020/6/18
     * @Param []
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> infoMonthlyView(Map<String,Object> params);

    /**
     * 分行月度离线运营业务量
     * @Author yey.he
     * @Date 11:18 AM 2020/6/18
     * @Param []
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> branchMonthlyView(Map<String,Object> params);

    /**
     * 区域月度离线运营业务量
     * @Author yey.he
     * @Date 11:19 AM 2020/6/18
     * @Param []
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> zoneMonthlyView(Map<String,Object> params);

    /**
     * 网点月度离线运营业务量
     * @Author yey.he
     * @Date 11:19 AM 2020/6/18
     * @Param []
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> siteMonthlyView(Map<String,Object> params);

    /**
     * 渠道月度离线运营业务量
     * @Author yey.he
     * @Date 11:20 AM 2020/6/18
     * @Param []
     * @return com.sunyard.dap.common.model.ReturnT<java.util.List>
     **/
    ReturnT<List> channelMonthlyView(Map<String,Object> params);
}
