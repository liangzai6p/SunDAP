package com.sunyard.ars.common.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sunyard.ars.common.pojo.TmpData;

public interface TmpDataMapper {

	TmpData selectByPrimaryKey(String serialNo);
	
	List<TmpData> selectByBatchId(HashMap<String,Object> map);
	
    int insert(TmpData record);

    /**
     * 根据影像信息插入图像表
     * 表名作为参数传入
     */
    int insertSelective(@Param("tmpdata")TmpData tmpdata,@Param("dataTb")String dataTb);

    int updateByPrimaryKeySelective(TmpData record);
        
    List<HashMap<String,Object>> selectImageListByFlow(HashMap<String,Object> map);
    
    /**
     * 获取图像表信息
     * @param batchId
     * @param dataTb
     * @return
     */
    List<TmpData> selectDataList(@Param("batchId")String batchId,@Param("dataTb")String dataTb);
    
    /**
     * 补扫提交前更新对应主键图像序号
     * @param batchId
     * @param dataTb
     * @return
     */
    int updateBeforeSubmit(@Param("batchId")String batchId,@Param("dataTb")String dataTb);
    
    /**
     * 按条件更新图像表
     * @param map
     * @return
     */
    int updateTmpData(HashMap<String,Object> map);
    
	/**
	 * 更新复制图像的复制批内码(补扫图像提交用到)
	 * @param serialNos  复制影像的serialNo
	 * @param batchId
	 * @param inccodeinBatch  复制批内码
	 * @param dataTableName
	 * @return
	 * @throws Exception
	 */
	public int updateCopyInccodein(@Param("serialNos")List<String> serialNos, @Param("batchId")String batchId,@Param("inccodeinBatch")Integer inccodeinBatch, @Param("dataTb")String dataTb);

    
	/**
	 * 更新复制图像的批内码(补扫图像提交用到)
	 * @param serialNo
	 * @param batchId
	 * @param copyCount
	 * @param dataTableName
	 * @return
	 * @throws Exception
	 */
	public int updateCopyInccodeinbatch(@Param("serialNo")String serialNo,@Param("batchId")String batchId,@Param("copyCount")Integer copyCount, @Param("dataTb")String dataTb);
    
    /**
     * 补扫时更新主件批内码
     * @return
     */
	public int updatePrimary(@Param("batchId")String batchId,@Param("dataTb")String dataTb);
	
	/**
	 * 主件批内码更新完毕后重置copySerialNo
	 * @param batchId
	 * @param dataTb
	 * @return
	 */
	public int updateCopySerialNo(@Param("batchId")String batchId,@Param("dataTb")String dataTb);
	
	/**
	 * 取消图像补扫标识(补扫图像提交用到)
	 * @param batchId
	 * @param dataTb
	 * @return
	 */
	int updateProcessState(@Param("batchId")String batchId,@Param("dataTb")String dataTb);
	
	/**
	 * 查询有标记补扫标志的影像
	 * @param map
	 * @return
	 */
	List<HashMap<String,Object>> selectPatchImageList(HashMap<String,Object> map);
	
	/**
	 * 查询补扫任务信息
	 * @param map
	 * @return
	 */
	List<HashMap<String,Object>> selectPatchTaskImageList(@Param("batchId")String batchId);
   

	/**
     * 根据传入条件查询图像数量
     * @param 
     * @return
     */
    int selectImgCount(HashMap<String, Object> map);
    
    int updateTmpDataInfo(HashMap<String, Object> condMap);
    /**
     * 根据传入条件查询图像信息
     * @param map
     * @return
     */
    public List<HashMap<String, Object>> selectBySelective(HashMap<String, Object> map);

    /**
     * 设主件更新后面附件主件号
     * @param condMap
     * @return
     */
	int updatePsLevelUp(HashMap<String, Object> condMap);

	/**
	 * 插入复制影像
	 * @param condMap
	 * @return
	 */
	int insertCopyImage(HashMap<String, Object> condMap);

	/**
	 * 手工指定主件
	 * @param condMap
	 * @return
	 */
	int updateHandleUp(HashMap<String, Object> condMap);

	/**
	 * 设附件更新凭证及其对应附件信息
	 * @param condMap
	 * @return
	 */
	int updatePsLevelDownAll(HashMap<String, Object> condMap);

	/**
	 * 释放所有勾兑图像
	 * @param condMap
	 * @return
	 */
	int updateAllTmpData(HashMap<String, Object> condMap);
	
	/**
	 * 通过条件查询影像信息
	 * @param map
	 * @return
	 */
	public List<HashMap<String, Object>> selectImageList(HashMap<String, Object> map);
	public List<HashMap<String, Object>> selectImageListByBatchList(HashMap<String, Object> condMap);

	/**
	 * 无流水影像查询
	 * @return
	 */
	public List<HashMap<String, Object>> selectImageByNoFlow(HashMap<String, Object> condMap);
	
	/**
	 * 流水影像查询
	 * @return
	 */
	public List<HashMap<String, Object>> selectImageByFlow(HashMap<String, Object> condMap);

	/**
	 * 查看华夏影像系统,影像查询
	 * @return
	 */
	public List<HashMap<String, Object>> selectSinoImageByMap(HashMap<String, Object> condMap);
	/**
	 * 查看移动pad系统,影像查询
	 * @return
	 */
	public List<HashMap<String, Object>> selectPadImageByMap(HashMap<String, Object> condMap);

	/**
	 * 获取一笔业务主附件信息
	 * @param condMap
	 * @return
	 */
	public List<HashMap<String, Object>> selectVoucherList(HashMap<String, Object> condMap);
	/**
	 * 按条件查询图像
	 * @param condMap
	 * @return
	 */
	public List<TmpData>selectDataBySelective(HashMap<String, Object> condMap);
	/**
	 * 根据条件获取指定列的图像信息
	 * @param condMap
	 * @return
	 */
	public List<HashMap<String, Object>> getImageInfoList(HashMap<String, Object> condMap);

	/**
	 * 获取未处理图像数量
	 * @param condMap
	 * @return
	 */
	public int selectNotDealImage(HashMap<String, Object> condMap);

	/**
	 * 获取标记重扫数量
	 * @param condMap
	 * @return
	 */
	public int selectPatchImageCount(HashMap<String, Object> condMap);

	/**
	 * 获取下发补扫任务数量
	 * @param condMap
	 * @return
	 */
	public int selectPatchTaskImageCount(HashMap<String, Object> condMap);	
	//删除已归档的凭证
		int deleteArchiveBatchData(HashMap<String, Object> condMap);
		//获取需要补扫图片数量
		int getPatchVoucCount(List list);
		//获取补扫任务数量
		int getPatchTaskCount(List list);
		int updateErrorFlag(@Param("serialNo")String serialNo);


	public List<TmpData> selByInccodein(HashMap<String, Object> condMap);

    /**
     * 看图时查询影像信息
     * @param condMap
     * @return
     */
	public List<TmpData>selectDataNps(HashMap<String, Object> condMap);
}