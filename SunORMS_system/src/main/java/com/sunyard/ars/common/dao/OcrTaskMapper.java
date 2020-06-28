package com.sunyard.ars.common.dao;


import com.sunyard.ars.common.pojo.OcrTaskBean;
import org.apache.ibatis.annotations.Param;

public interface OcrTaskMapper {
	int insert(OcrTaskBean ocrTaskBean);
	OcrTaskBean select();
	int lock(@Param("taskId") String taskId);
	int updateWrong(@Param("taskId")String taskId,@Param("errResult")String errResult); //执行失败后 置为状态4 填入错误原因
	int update(@Param("taskId") String taskId); //执行成功后 置为状态3
}
