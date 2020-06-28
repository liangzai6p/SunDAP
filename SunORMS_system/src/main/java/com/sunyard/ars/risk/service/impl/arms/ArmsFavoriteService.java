package com.sunyard.ars.risk.service.impl.arms;

import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.common.util.ParamUtil;
import com.sunyard.ars.risk.dao.arms.ArmsFavoriteTbMapper;
import com.sunyard.ars.risk.service.arms.IArmsFavoriteService;
import com.sunyard.ars.risk.service.arms.IArmsModelDisposeService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service("armsFavoriteService")
@Transactional
public class ArmsFavoriteService extends BaseService implements IArmsFavoriteService {

    @Resource
    private ArmsFavoriteTbMapper  armsFavoriteTbMapper;

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

    }


}
