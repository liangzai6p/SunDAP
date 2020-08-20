package com.sunyard.dap.dataserve.service.impl;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.BusiErrorDetailDo;
import com.sunyard.dap.dataserve.mapper.BusiErrorMapper;
import com.sunyard.dap.dataserve.service.BusiErrorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yey.he
 * @since 2020-08-20
 */
@Service
public class BusiErrorServiceImpl extends ServiceImpl<BusiErrorMapper, BusiErrorDetailDo> implements BusiErrorService {

    @Override
    public ReturnT<List> listBranchErrorCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listBranchErrorCount(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }
}
