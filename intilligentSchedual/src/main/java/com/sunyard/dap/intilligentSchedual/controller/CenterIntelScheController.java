package com.sunyard.dap.intilligentSchedual.controller;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.intilligentSchedual.service.CenterIntelScheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jie.zheng
 * @since 2021-02-19
 */
@RestController
@RequestMapping("/centerIntelSche")
@Slf4j
@RefreshScope
public class CenterIntelScheController {
    @Autowired
    private CenterIntelScheService service;

    /*@PostMapping("/intelScheDepUserInfoQuery")
    public ReturnT<Map> intelScheDepUserInfoQuery(@RequestBody Map<String,Object> params){
        return service.intelScheDepUserInfoQuery(params);
    }

    @PostMapping("/centerIntelScheButtonRight")
    public ReturnT<Map> centerIntelScheButtonRight(@RequestBody Map<String,Object> params){
        return service.centerIntelScheButtonRight(params);
    }*/

    /**
     * 智能排班处室表格数据查询
     * @Author jie.zheng
     * @Date 9:41 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/depIntelScheTableDataQuery")
    public ReturnT<Map> depIntelScheTableDataQuery(@RequestBody Map<String,Object> params){
        return service.depIntelScheTableDataQuery(params);
    }

    /**
     * 智能排班XX处室排班人员信息日历数据查询
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/intelScheDepCalDataQuery")
    public ReturnT<Map> intelScheDepCalDataQuery(@RequestBody Map<String,Object> params){
        return service.intelScheDepCalDataQuery(params);
    }

    /**
     * 排班修改
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/intelScheDepModifySave")
    public ReturnT<Map> intelScheDepModifySave(@RequestBody Map<String,Object> params){
        return service.intelScheDepModifySave(params);
    }

    /**
     * 排班人员视图数据查询
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/centerIntelScheOperDataQuery")
    public ReturnT<Map> centerIntelScheOperDataQuery(@RequestBody Map<String,Object> params){
        return service.centerIntelScheOperDataQuery(params);
    }

    /**
     * 重新排班处室信息数据查询
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/intelScheAgainDepSelectDataQuery")
    public ReturnT<Map> intelScheAgainDepSelectDataQuery(@RequestBody Map<String,Object> params){
        return service.intelScheAgainDepSelectDataQuery(params);
    }

    /**
     * 手动触发本月剩余日期排班
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/confireScheAgain")
    public ReturnT<Map> confireScheAgain(@RequestBody Map<String,Object> params){
        return service.confireScheAgain(params);
    }

    /**
     * 待发布通知消息查询
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/scheAgainNoticePublishInfoQuery")
    public ReturnT<Map> scheAgainNoticePublishInfoQuery(@RequestBody Map<String,Object> params){
        return service.scheAgainNoticePublishInfoQuery(params);
    }

    /**
     * 发布通知消息
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/confireNoticePublishSave")
    public ReturnT<Map> confireNoticePublishSave(@RequestBody Map<String,Object> params){
        return service.confireNoticePublishSave(params);
    }

    /**
     * 查询处室值班天数统计
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/depDaysQuery")
    public ReturnT<Map> depDaysQuery(@RequestBody Map<String,Object> params){
        return service.depDaysQuery(params);
    }

    /**
     * 查询人员工作时长列表信息(depDaysQuery)
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/operDailyWorkTimeTableInfoQuery")
    public ReturnT<Map> operDailyWorkTimeTableInfoQuery(@RequestBody Map<String,Object> params){
        return service.operDailyWorkTimeTableInfoQuery(params);
    }

    /**
     * 人员工作时长修改信息保存
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/operWorkTimeModifyInfoSave")
    public ReturnT<Map> operWorkTimeModifyInfoSave(@RequestBody Map<String,Object> params){
        return service.operWorkTimeModifyInfoSave(params);
    }

    /**
     * 智能排班页面权限查询
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/centerIntelScheButtonRight")
    public ReturnT<Map> centerIntelScheButtonRight(@RequestBody Map<String,Object> params){
        return service.centerIntelScheButtonRight(params);
    }

    /**
     * 智能排班XX处室XX日期排班明细信息查询
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/centerInteScheCalDateClassInfoQuery")
    public ReturnT<Map> centerInteScheCalDateClassInfoQuery(@RequestBody Map<String,Object> params){
        return service.centerInteScheCalDateClassInfoQuery(params);
    }

    /**
     * 智能排班XX处室XX日期排班修改信息保存
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/scheduleCalDateModifyInfoSave")
    public ReturnT<Map> scheduleCalDateModifyInfoSave(@RequestBody Map<String,Object> params){
        return service.scheduleCalDateModifyInfoSave(params);
    }

    /**
     * 智能排班XX处室XX日期排班新增信息保存
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/scheduleCalDateAddInfoSave")
    public ReturnT<Map> scheduleCalDateAddInfoSave(@RequestBody Map<String,Object> params){
        return service.scheduleCalDateAddInfoSave(params);
    }

    /**
     * 智能排班XX处室XX日期排班删除信息保存
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/scheduleCalDateDeleteInfoUpdate")
    public ReturnT<Map> scheduleCalDateDeleteInfoUpdate(@RequestBody Map<String,Object> params){
        return service.scheduleCalDateDeleteInfoUpdate(params);
    }

    /**
     * 人员工作时长修改节假日查询判断
     * @Author jie.zheng
     * @Date 10:01 AM 2021-02-22
     * @Param [params]
     * @return com.sunyard.dap.common.model.ReturnT<java.util.Map>
     **/
    @PostMapping("/scheOperDailyWorkDateIsOffDay")
    public ReturnT<Map> scheOperDailyWorkDateIsOffDay(@RequestBody Map<String,Object> params){
        return service.scheOperDailyWorkDateIsOffDay(params);
    }

    @PostMapping("/centerIntelScheDataImport")
    public ReturnT<Map> centerIntelScheDataImport(@RequestBody Map<String,Object> params){
        return service.centerIntelScheDataImport(params);
    }

    @PostMapping("/scheduleExportModelDownload")
    public ReturnT<Map> scheduleExportModelDownload(@RequestBody Map<String,Object> params){
        return service.scheduleExportModelDownload(params);
    }

}
