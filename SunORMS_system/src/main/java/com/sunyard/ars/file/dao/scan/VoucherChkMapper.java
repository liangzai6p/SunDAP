package com.sunyard.ars.file.dao.scan;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.file.pojo.scan.VoucherChk;

public interface VoucherChkMapper {
    
    int deleteByPrimaryKey(BigDecimal id);

    
    int insert(VoucherChk record);

    
    int insertSelective(VoucherChk record);

    
    VoucherChk selectByPrimaryKey(BigDecimal id);

    
    int updateByPrimaryKeySelective(VoucherChk record);

    
    int updateByPrimaryKey(VoucherChk record);
    
    /**
     * 查询当前凭证登记信息是否存在
     * @return
     */
    List<VoucherChk> getVoucherChk(VoucherChk record);
    
    VoucherChk getVoucherChkByBatchId(@Param("batchId")String batchId);
    
    /**
     * 根据查询条件查询列表(返回VoucherChk不包含TmpBatch)。
     * @param condition
     * @return
     */
    @SuppressWarnings("rawtypes")
    List<VoucherChk> selectByCondition(Map condition);
    
    /**
     * 根据查询条件查询列表(返回VoucherChk包含TmpBatch)。
     * @param condition
     * @return
     */
    @SuppressWarnings("rawtypes")
	List<VoucherChk> selectByConditionWithTmpBatch(Map condition);
    
    int isExists(VoucherChk record);
    
    int updateByBatchId(HashMap<String, Object> condMap);
    //查询已下发流水但未扫描的柜员
    public List<HashMap<String, Object>> getUnScanedTeller(@Param("userNo") String userNo,@Param("SoccurDate") String SoccurDate,@Param("EoccurDate") String EoccurDate);
}