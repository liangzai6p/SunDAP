package com.sunyard.dap.common.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @program: SunDAP
 * @description: 通用工具类
 * @author: yey.he
 * @create: 2020-08-06 11:29
 **/
@Slf4j
public class CommonUtil {
    public static void setPageByParams(Page<?> page, Map<String, Object> map){
        boolean flag = false;
        if (map.get("currentPage")!=null && map.get("recordSize")!=null){
            try {
                page.setCurrent(Long.parseLong(map.get("currentPage").toString()));
                page.setSize(Long.parseLong(map.get("recordSize").toString()));
                flag = true;
            }catch (NumberFormatException e){
                log.error("页码转换出错,设定currentPage=1,recordSize=10",e.getMessage());
            }
        }else {
            log.info("未找到页码参数,设定currentPage=1,recordSize=10");
        }

        if (!flag){
            page.setCurrent(1);
            page.setSize(10);
        }
    }
}