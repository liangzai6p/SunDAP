package com.sunyard.ars.system.dao.sc;



import java.math.BigDecimal;
import java.util.List;

import com.sunyard.ars.system.bean.sc.SmTableField;

public interface SmTableFieldMapper {

    /**
     * @Title: getTableFieldByTableIdDao
     * @Description: 根据表ID获取表字段信息
     * @param tableId
     * @return List<SmTableField>
     */
    List<SmTableField> getTableFieldByTableIdDao(BigDecimal tableId);
}
