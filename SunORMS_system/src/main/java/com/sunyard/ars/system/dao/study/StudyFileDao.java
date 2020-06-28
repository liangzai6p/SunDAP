package com.sunyard.ars.system.dao.study;

import java.util.HashMap;
import java.util.List;

import com.sunyard.ars.system.bean.study.SmStudyFileTbBean;
import org.apache.ibatis.annotations.Param;

public interface StudyFileDao {

	
	SmStudyFileTbBean selectStudyFileById(int id);
	
	/**
	 * 
	 * @param map
	 * @return SmStudyFileTbBean的list
	 */
	List<SmStudyFileTbBean> selectStudyFileByConfig(HashMap<String, Object> map);
	
	
	//插入
	int insertStudyFile(@Param("studyFile") SmStudyFileTbBean bean,@Param("dbType")String dbType);
	
	//修改
	int updateStudyFile(SmStudyFileTbBean bean);
	
	
	//根据主键删除
	int deleteStudyFile(int id);
	
	//根据条件删除数据
	int deleteStudyFileByConfig(HashMap<String, Object> map);
	
	
}
