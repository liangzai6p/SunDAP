package com.sunyard.dap.dataserve.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunyard.dap.common.model.ReturnT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application.yml"})
public class TellerServiceImplTest {
    @Resource
    private TellerServiceImpl TellerServiceImpl;
    @Test
    public void listByBranch() {
        Map params=new HashMap();
        params.put("SITE_NO","1002111101288");
        ReturnT<Page<HashMap<String, Object>>> returnT=TellerServiceImpl.listByBranch(params);
        Page page=returnT.getContent();
        System.out.println(page.getRecords());
    }
}