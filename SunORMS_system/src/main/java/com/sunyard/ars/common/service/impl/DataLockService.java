package com.sunyard.ars.common.service.impl;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.dao.TaskLockMapper;
import com.sunyard.ars.common.pojo.TaskLock;
import com.sunyard.ars.common.service.IDataLockService;
import com.sunyard.ars.common.util.ParamUtil;
import com.sunyard.ars.common.util.date.DateUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 任务锁服务类
 */

@Service("dataLockService")
@Transactional
public class DataLockService extends BaseService implements IDataLockService {
    @Resource
    private TaskLockMapper taskLockMapper;

    @Override
    public ResponseBean execute(RequestBean requestBean) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        return executeAction(requestBean, responseBean);
    }

    @Override
    protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        // 获取参数集合
        Map sysMap = requestBean.getSysMap();
        // 获取操作标识
        String oper_type = (String) sysMap.get("oper_type");
        if ("addLock".equals(oper_type)) {  //入库保存
            addLock(requestBean,responseBean);
        }else if("upLockToDateId".equals(oper_type)){
            upLockToDateId(requestBean,responseBean);
        } else if("upLockToUser".equals(oper_type)){
            upLockToUser(requestBean,responseBean);

        }
    }

    /**
     * 查询枷锁
     * @param requestBean
     * @param responseBean
     */
    public void addLock(RequestBean requestBean, ResponseBean responseBean) throws Exception {
        // 获取参数集合
    	if(BaseUtil.isBlank(BaseUtil.getLoginUser().getUserNo())){
            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
            responseBean.setRetMsg("任务锁定失败,请重新登录操作!");
            return;
    	}
        String  dataId = ParamUtil.getParamValue(requestBean,"dataId");
        String taskType=ParamUtil.getParamValue(requestBean,"taskType");//差错单类型
        Map retMap = new HashMap();
        //查询该任务是否有人处理
        Map<String,Object>   taskLock_old = taskLockMapper.selectLockFormId(dataId);
        if(taskLock_old  != null ){
            if (!BaseUtil.getLoginUser().getUserNo().equals(taskLock_old.get("LOCK_USER_NO"))){
            	long time = (new Date().getTime() - DateUtil.getDate(taskLock_old.get("LOCK_TIME")+"","yyyyMMddHHmmss").getTime())/1000/60;
            	if(time > 35){//大于35分钟解锁
            		taskLockMapper.taskDelLock(dataId);
            	}else{
            		responseBean.setRetMap(retMap);
                    responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
                    //responseBean.setRetMsg("任务已被其他用户锁定");
                    responseBean.setRetMsg("任务已被"+taskLock_old.get("LOCK_USER_NO")+"锁定");
                    return;
            	}
            }else {
                taskLockMapper.taskDelLock(dataId);
            }
        }
        //枷锁
        TaskLock taskLock  =  new TaskLock();
        taskLock.setDataId(dataId);
        taskLock.setLockUserNo(BaseUtil.getLoginUser().getUserNo());
        taskLock.setLockTime(DateUtil.getNewDate("yyyyMMddHHmmss"));
        taskLock.setTaskType(taskType);
        int VNT = taskLockMapper.taskAddLock(taskLock);
        if(VNT == 0){
            responseBean.setRetMap(retMap);
            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
            responseBean.setRetMsg("任务已被其他用户锁定");
        }else {
            responseBean.setRetMap(retMap);
            responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
            responseBean.setRetMsg("任务成功锁定");
        }

    }


    /**
     * 解除锁定
     * @param requestBean
     * @param responseBean
     */
    public void upLockToDateId(RequestBean requestBean, ResponseBean responseBean) {
        // 获取参数集合
        // 获取参数集合
        String  dataId = ParamUtil.getParamValue(requestBean,"dataId");

        int VNT = taskLockMapper.taskDelLock(dataId);
        Map retMap = new HashMap();
        if(VNT == 0){
            responseBean.setRetMap(retMap);
            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
            responseBean.setRetMsg("任务解锁成功");
        }else {
            responseBean.setRetMap(retMap);
            responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
            responseBean.setRetMsg("任务解锁失败");
        }

    }


    /**
     * 解除锁定
     * @param requestBean
     * @param responseBean
     */
    public void upLockToUser(RequestBean requestBean, ResponseBean responseBean) throws Exception{
        // 获取参数集合
        String taskType=ParamUtil.getParamValue(requestBean,"taskType");//差错单类型

        String userNo = BaseUtil.getLoginUser().getUserNo();

        int VNT = taskLockMapper.taskDelLockToUser(userNo,taskType);
        Map retMap = new HashMap();
        if(VNT == 0){
            responseBean.setRetMap(retMap);
            responseBean.setRetCode(ARSConstants.HANDLE_FAIL);
            responseBean.setRetMsg("任务解锁成功");
        }else {
            responseBean.setRetMap(retMap);
            responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
            responseBean.setRetMsg("任务解锁失败");
        }

    }
}
