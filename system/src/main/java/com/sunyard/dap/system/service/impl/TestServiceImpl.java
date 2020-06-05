package com.sunyard.dap.system.service.impl;

import com.sunyard.dap.common.entity.Test;
import com.sunyard.dap.system.mapper.ITestMapper;
import com.sunyard.dap.system.service.ITestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yey.he
 * @since 2020-06-03
 */
@Service
public class TestServiceImpl extends ServiceImpl<ITestMapper, Test> implements ITestService {

}
