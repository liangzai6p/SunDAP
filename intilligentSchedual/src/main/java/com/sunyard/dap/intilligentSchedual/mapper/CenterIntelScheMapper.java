package com.sunyard.dap.intilligentSchedual.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sunyard.dap.intilligentSchedual.entity.CenterIntelScheDO;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jie.zheng
 * @since 2021-02-19
 */
public interface CenterIntelScheMapper extends BaseMapper<CenterIntelScheDO> {
/*
    List<HashMap<String,Object>> intelScheDepUserInfoQuery(@Param("params") Map<String,Object> params);

    List<HashMap<String,Object>> centerIntelScheButtonRight(@Param("params") Map<String,Object> params);
*/
    //智能排班结果处室表格数据查询
    List<HashMap<String, Object>> depIntelScheTableDataQuery(@Param("params") Map<String,Object> params);
    //智能排班结果处室日历数据查询
    List<HashMap<String, Object>> intelScheDepCalDataQuery(@Param("params") Map<String,Object> params);
    //根据处室查询该处室下的排班人员信息
    List<HashMap<String, Object>> intelScheDepUserInfoQuery(@Param("params") Map<String,Object> params);
    //排班人员信息查询	
    List<HashMap<String, Object>> intelScheUserInfoQuery(@Param("params") Map<String,Object> params);
    //排班手动修改
    Void intelScheDepModifySave(@Param("params") Map<String,Object> params);
    //排班手动修改原值修改
    Void intelScheDepModifyOldValUpdate(@Param("params") Map<String,Object> params);
    //排班明细导出查询
    List<HashMap<String, Object>> intelScheDataExportQuery(@Param("params") Map<String,Object> params);
    //智能排班人员视图排班数据查询
    List<HashMap<String, Object>> centerIntelScheOperDataQuery(@Param("params") Map<String,Object> params);
    //人员基本信息查询
    List<HashMap<String, Object>> centerIntelScheOperBaseInfoQuery(@Param("params") Map<String,Object> params);
    //人员工作时长统计
    List<HashMap<String, Object>> centerIntelScheOperWorkTotalQuery(@Param("params") Map<String,Object> params);
    //重新排班处室信息查询
    List<HashMap<String, Object>> intelScheAgainDepSelectDataQuery(@Param("params") Map<String,Object> params);
    //重新排班处室用户查询
    List<HashMap<String, Object>> intelScheAgainDepUserQuery(@Param("params") Map<String,Object> params);
    //重新排班通知信息保存
    Void intelScheAgainUserNoticeInfoSave(@Param("params") Map<String,Object> params);
    //重新排班待发布通知消息查询
    List<HashMap<String, Object>> scheAgainNoticePublishInfoQuery(@Param("params") Map<String,Object> params);
    //重新排班发布通知消息状态修改
    Void confireNoticePublishSave(@Param("params") Map<String,Object> params);
    //处室人员值班天数统计查询
    List<HashMap<String, Object>> centerIntelDepDaysQuery(@Param("params") Map<String,Object> params);
    //人员工作时长列表信息查询
    List<HashMap<String, Object>> operDailyWorkTimeTableInfoQuery(@Param("params") Map<String,Object> params);
    //人员工作时长修改信息保存
    Void operWorkTimeModifyInfoSave(@Param("params") Map<String,Object> params);
    //人员页面按钮权限查询
    public String centerIntelScheButtonRight(@Param("params") Map<String,Object> params);
    //XX处室当日排班人员信息列表查询
    List<HashMap<String, Object>>  centerInteScheCalDateClassInfoQuery(@Param("params") Map<String,Object> params);
    //XX处室排班人员对应岗位信息查询
    List<HashMap<String, Object>> centerInteScheCalDateUserRoleNameInfoQuery(@Param("params") Map<String,Object> params);
    //XX处室当日排班人员修改原值状态更新
    Void scheduleCalDateModifyOldValUpdate(@Param("params") Map<String,Object> params);
    //XX处室当日排班人员新增/修改信息保存
    Void scheduleCalDateModifyNewValUpdateSave(@Param("params") Map<String,Object> params);
    //排班信息excel保存
    Void scheduleCalDateImportValUpdateSave(@Param("params") Map<String,Object> params);
    //判断XX日期是否为节假日
    List<HashMap<String, Object>> scheOperDailyWorkDateIsOffDay(@Param("params") Map<String,Object> params);
    //排班处室岗位信息查询
    List<HashMap<String, Object>> roleDepartNameInfoQuery(@Param("params") Map<String,Object> params);
    //排班人员岗位信息查询
    List<HashMap<String, Object>> scheExportUserRoleNameInfoQuery(@Param("params") Map<String,Object> params);
    //导入前先将旧值更新为不排班状态
    Void scheduleExportOldValUpdate(@Param("params") Map<String,Object> params);
    //业务量预测智能排班执行日志信息保存
    Void busiForeShcduleRunLogSave(@Param("params") Map<String,Object> params);
}
