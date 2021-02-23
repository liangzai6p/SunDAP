package com.sunyard.dap.dataserve.service.impl;

import com.sunyard.dap.common.model.ReturnT;
import com.sunyard.dap.dataserve.entity.BusiErrorDetailDo;
import com.sunyard.dap.dataserve.mapper.BusiErrorMapper;
import com.sunyard.dap.dataserve.service.BusiErrorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yey.he
 * @since 2020-08-20
 */
@Service
public class BusiErrorServiceImpl extends ServiceImpl<BusiErrorMapper, BusiErrorDetailDo> implements BusiErrorService {

    @Override
    public ReturnT<List> listBranchErrorCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listBranchErrorCount(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listBranchErrorCountBar(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> list = baseMapper.listBranchErrorCount(params);
            List xAxis = new ArrayList();
            List yAxis = new ArrayList();
            List machineErrorRateOrganData = new ArrayList();
            int limit = 0;
            if (list.size() > 10) {
                limit = 9;
            } else {
                limit = list.size() - 1;
            }
            int index = 0;
            for (int i = limit; i >= 0; i--) {
                xAxis.add(list.get(i).get("BRANCH_NAME"));
                yAxis.add(list.get(i).get("ERROR_RATE"));
                machineErrorRateOrganData.add(list.get(i).get("BRANCH_NO"));
                index++;
            }
            Map map = new HashMap();
            map.put("xAxis", xAxis);
            map.put("yAxis", yAxis);
            map.put("machineErrorRateOrganData", machineErrorRateOrganData);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }

    @Override
    public ReturnT<List> listSiteErrorCount(Map<String, Object> params) {
        try {
            return new ReturnT<>(ReturnT.SUCCESS_CODE,"查询成功",baseMapper.listSiteErrorCount(params));
        }catch (Exception e){
            log.error(e.getMessage());
            return ReturnT.listFAIL;
        }
    }

    @Override
    public ReturnT<Map> listSiteErrorCountBar(Map<String, Object> params) {
        try {
            List<HashMap<String, Object>> list = baseMapper.listSiteErrorCount(params);
            List xAxis = new ArrayList();
            List yAxis = new ArrayList();
            List busiCountOrgan = new ArrayList();
            int limit = 0;
            if (list.size() > 10) {
                limit = 9;
            } else {
                limit = list.size() - 1;
            }
            int index = 0;
            for (int i = limit; i >= 0; i--) {
                xAxis.add(list.get(i).get("SITE_NAME"));
                yAxis.add(list.get(i).get("ERROR_RATE"));
                busiCountOrgan.add(list.get(i).get("SITE_NO"));
                index++;
            }
            Map map = new HashMap();
            map.put("xAxis", xAxis);
            map.put("yAxis", yAxis);
            map.put("busiCountOrgan", busiCountOrgan);
            return new ReturnT<Map>(ReturnT.SUCCESS_CODE, "查询成功", map);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ReturnT.mapFAIL;
        }
    }
}
