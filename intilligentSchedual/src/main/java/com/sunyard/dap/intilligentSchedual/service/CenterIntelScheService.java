package com.sunyard.dap.intilligentSchedual.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.intilligentSchedual.entity.CenterIntelScheDO;

import java.util.Map;

public interface CenterIntelScheService extends IService<CenterIntelScheDO> {
    /*ReturnT<List> intelScheDepUserInfoQuery(Map<String,Object> params);

    ReturnT<List> centerIntelScheButtonRight(Map<String,Object> params);*/

    ReturnT<Map> depIntelScheTableDataQuery(Map<String,Object> params);

    ReturnT<Map> intelScheDepCalDataQuery(Map<String,Object> params);

    ReturnT<Map> intelScheDepModifySave(Map<String,Object> params);

    ReturnT<Map> centerIntelScheOperDataQuery(Map<String,Object> params);

    ReturnT<Map> intelScheAgainDepSelectDataQuery(Map<String,Object> params);

    ReturnT<Map> confireScheAgain(Map<String,Object> params);

    ReturnT<Map> scheAgainNoticePublishInfoQuery(Map<String,Object> params);

    ReturnT<Map> confireNoticePublishSave(Map<String,Object> params);

    ReturnT<Map> depDaysQuery(Map<String,Object> params);

    ReturnT<Map> operDailyWorkTimeTableInfoQuery(Map<String,Object> params);

    ReturnT<Map> operWorkTimeModifyInfoSave(Map<String,Object> params);

    ReturnT<Map> centerIntelScheButtonRight(Map<String,Object> params);

    ReturnT<Map> centerInteScheCalDateClassInfoQuery(Map<String,Object> params);

    ReturnT<Map> scheduleCalDateModifyInfoSave(Map<String,Object> params);

    ReturnT<Map> scheduleCalDateAddInfoSave(Map<String,Object> params);

    ReturnT<Map> scheduleCalDateDeleteInfoUpdate(Map<String,Object> params);

    ReturnT<Map> scheOperDailyWorkDateIsOffDay(Map<String,Object> params);

    ReturnT<Map> centerIntelScheDataImport(Map<String,Object> params);

    ReturnT<Map> scheduleExportModelDownload(Map<String,Object> params);
}
