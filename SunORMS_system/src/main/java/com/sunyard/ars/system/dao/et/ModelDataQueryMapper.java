package com.sunyard.ars.system.dao.et;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ModelDataQueryMapper {
    List<Map<String,Object>> getModelData(@Param("queryDataSql") String queryDataSql);
    Integer getModelDataCount(@Param("queryDataSql") String queryDataSql);

    /**
     * 查询预警数据信息
     * @param modelId
     * @param modelRowId
     * @param tableName
     * @param ishandle
     * @return
     */
    Map<String,Object> getThisModelData(@Param("modelId") Integer modelId,
                                        @Param("modelRowId") Integer modelRowId,
                                        @Param("tableName") String tableName,
                                        @Param("ishandle") String ishandle);



    /**
     * 查询预警数据信息
     * @param modelId
     * @param modelRowId
     * @param tableName
     * @param ishandle
     * @return
     */
    int getThisModelDataCount(@Param("modelId") Integer modelId,
                                        @Param("modelRowId") Integer modelRowId,
                                        @Param("tableName") String tableName,
                                        @Param("ishandle") String ishandle);


    /**
     * 已阅处理
     * @param modelId
     * @param modelRowId
     * @param tableName
     * @param check_info
     * @return
     */
    int checkPromptArms(@Param("modelId") Integer modelId,
                        @Param("modelRowId") Integer modelRowId,
                        @Param("tableName") String tableName,
                        @Param("checkInfo") String check_info);

    /**
     * 查询明细
     * @param tableName
     * @param modelId
     * @param modelRowId
     * @return
     */
    List<HashMap<String,Object>>  getRelatingModelData(@Param("tableName") String tableName,
                                                       @Param("modelId") String modelId,
                                                       @Param("modelRowId") String modelRowId);


    List<HashMap<String, Object>> getRelatingModelDataToMany(@Param("tableName") String tableName,
                                                             @Param("modelId") String modelId,
                                                             @Param("modelRowIds") List<Integer> modelRowIds);


    List<HashMap<String, Object>> getManyModelData(@Param("modelId") Integer modelId,
                                                   @Param("modelRowIds") List<Integer> modelRowIds,
                                                   @Param("tableName") String tableName);



    /**
     * 预警撤销
     * @param modelId
     * @param modelRowId
     * @param tableName
     * @param mark
     * @return
     */
    int dealExhibit(@Param("modelId") Integer modelId,
                    @Param("modelRowId") Integer modelRowId,
                    @Param("tableName") String tableName,
                    @Param("alertDate") String alertDate,
                    @Param("alertUserNo") String alertUserNo,
                    @Param("mark") String mark);


    /**
     *预警处理
     * @param formType
     * @param checkerNo
     * @param alertDate
     * @param formId
     * @param modelId
     * @param modelRowId
     * @return
     */
    int handleModelTask(@Param("tableName") String tableName,
                        @Param("formType") String formType,
                        @Param("checkerNo") String checkerNo,
                        @Param("alertDate") String alertDate,
                        @Param("formId") String formId,
                        @Param("modelId") String modelId,
                        @Param("modelRowId") String modelRowId,
                        @Param("isBf") Integer isBf);

    /**
     * 取消撤销预警单
     * @param modelId
     * @param modelRowId
     * @param tableName
     * @return
     */
    int armsDealBack(@Param("modelId")Integer modelId,
                     @Param("modelRowId")Integer modelRowId,
                     @Param("tableName")String tableName);

    /**
     * 删除案例
     * @param queryDataSql
     * @return
     *//*entryId,entryRowId,loginUserNo*/
    int eWICDetailDelete(@Param("entryId") String entryId,@Param("entryRowId") String entryRowId,@Param("loginUserNo") String loginUserNo);
    /**
     * 设为公有案例
     * @param queryDataSql
     * @return
     */
    int eWICDetailSetCommon(@Param("eWICDetailSetRemarkCommonTextareaVal") String eWICDetailSetRemarkCommonTextareaVal,@Param("armsFavoriteId") String armsFavoriteId);
	
    /**
     * 
     * @param fieldCondStr
     * @param tableName
     * @param parseInt
     * @param parseInt2
     * @return
     */
    List<HashMap<String,Object>> selectByPrimaryKey(@Param("fields")String fieldCondStr, @Param("tableName")String tableName, @Param("modelId")int modelId, @Param("modelRowId")int modelRowId);
    
    List<HashMap<String, Object>> getHisRelatingModelDataToMany(@Param("tableName") String tableName,
            @Param("modelId") String modelId,
            @Param("modelRowIds") List<Integer> modelRowIds);
}
