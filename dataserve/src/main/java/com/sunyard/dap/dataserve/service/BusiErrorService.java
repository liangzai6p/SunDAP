package com.sunyard.dap.dataserve.service;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.BusiErrorDetailDo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yey.he
 * @since 2020-08-20
 */
public interface BusiErrorService extends IService<BusiErrorDetailDo> {
    ReturnT<List> listBranchErrorCount(Map<String,Object> params);

    ReturnT<List> listSiteErrorCount(Map<String,Object> params);


}
