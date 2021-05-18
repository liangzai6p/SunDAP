package com.sunyard.dap.dataserve.service;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.SmBanksTb;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统银行信息表 服务类
 * </p>
 *
 * @author xiao.xie
 * @since 2021-03-01
 */
public interface ISmBanksTbService extends IService<SmBanksTb> {

    ReturnT<List> listBank(Map<String, Object> params);

    ReturnT<List> listBankDiaLogData(Map<String, Object> params);

    ReturnT<List> listBranchBankDiaLogData(Map<String, Object> params);

    ReturnT<List> listsubBranchBankDiaLogData(Map<String, Object> params);
}
