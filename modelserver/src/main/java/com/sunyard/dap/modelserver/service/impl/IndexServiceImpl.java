package com.sunyard.dap.modelserver.service.impl;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.common.util.CommonUtil;
import com.sunyard.dap.common.util.DapJsonUtil;
import com.sunyard.dap.common.util.FitUtils;
import com.sunyard.dap.modelserver.entity.MoAllIndex;
import com.sunyard.dap.modelserver.mapper.MoAllIndexMapper;
import com.sunyard.dap.modelserver.service.IndexServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @program: SunDAP
 * @description: 拟合运算实现类
 * @author: yey.he
 * @create: 2020-09-11 11:38
 **/
@Service
public class IndexServiceImpl extends ServiceImpl<MoAllIndexMapper, MoAllIndex> implements IndexServer {


    @Override
    public ReturnT<List> serachIndexName( Map<String, Object> params) {
        return new ReturnT<List>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.serachIndexName(params));
    }


}
