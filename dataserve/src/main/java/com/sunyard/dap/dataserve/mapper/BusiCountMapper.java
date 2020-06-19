package com.sunyard.dap.dataserve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunyard.dap.dataserve.entity.BusiCount;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yey.he
 * @since 2020-06-12
 */
public interface BusiCountMapper extends BaseMapper<BusiCount>{

    /**
     * 查询所有记录
     * @Author yey.he
     * @Date 9:25 AM 2020/6/19
     * @Param [params]
     * @return java.util.List<java.util.HashMap<java.lang.String,java.lang.Object>>
     **/
    List<HashMap<String,Object>> info(@Param("params") Map<String,Object> params);

    /**
     * 分行离线运营业务量
     * @Author yey.he
     * @Date 11:05 AM 2020/6/18
     * @Param []
     * @return java.util.List<java.util.HashMap<java.lang.String,java.lang.Object>>
     **/
    List<HashMap<String,Object>> branchView(@Param("params") Map<String,Object> params);

    /**
     * 网点离线运营业务量
     * @Author yey.he
     * @Date 11:06 AM 2020/6/18
     * @Param []
     * @return java.util.List<java.util.HashMap<java.lang.String,java.lang.Object>>
     **/
    List<HashMap<String,Object>> siteView(@Param("params") Map<String,Object> params);

    /**
     * 区域离线运营业务量
     * @Author yey.he
     * @Date 11:06 AM 2020/6/18
     * @Param []
     * @return java.util.List<java.util.HashMap<java.lang.String,java.lang.Object>>
     **/
    List<HashMap<String,Object>> zoneView(@Param("params") Map<String,Object> params);

    /**
     * 渠道离线运营业务量
     * @Author yey.he
     * @Date 11:06 AM 2020/6/18
     * @Param []
     * @return java.util.List<java.util.HashMap<java.lang.String,java.lang.Object>>
     **/
    List<HashMap<String,Object>> channelView(@Param("params") Map<String,Object> params);

    /**
     * 月度离线运营业务量
     * @Author yey.he
     * @Date 11:06 AM 2020/6/18
     * @Param []
     * @return java.util.List<java.util.HashMap<java.lang.String,java.lang.Object>>
     **/
    List<HashMap<String,Object>> infoMonthlyView(@Param("params") Map<String,Object> params);

    /**
     * 分行月度离线运营业务量
     * @Author yey.he
     * @Date 11:06 AM 2020/6/18
     * @Param []
     * @return java.util.List<java.util.HashMap<java.lang.String,java.lang.Object>>
     **/
    List<HashMap<String,Object>> branchMonthlyView(@Param("params") Map<String,Object> params);

    /**
     * 网点月度离线运营业务量
     * @Author yey.he
     * @Date 11:07 AM 2020/6/18
     * @Param []
     * @return java.util.List<java.util.HashMap<java.lang.String,java.lang.Object>>
     **/
    List<HashMap<String,Object>> siteMonthlyView(@Param("params") Map<String,Object> params);

    /**
     * 区域月度离线运营业务量
     * @Author yey.he
     * @Date 11:07 AM 2020/6/18
     * @Param []
     * @return java.util.List<java.util.HashMap<java.lang.String,java.lang.Object>>
     **/
    List<HashMap<String,Object>> zoneMonthlyView(@Param("params") Map<String,Object> params);

    /**
     * 渠道月度离线运营业务量
     * @Author yey.he
     * @Date 11:07 AM 2020/6/18
     * @Param []
     * @return java.util.List<java.util.HashMap<java.lang.String,java.lang.Object>>
     **/
    List<HashMap<String,Object>> channelMonthlyView(@Param("params") Map<String,Object> params);

}
