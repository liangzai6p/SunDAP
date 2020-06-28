package com.sunyard.ars.system.service.impl.busm;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.busm.SmRoleTableTB;
import com.sunyard.ars.system.dao.busm.SmRoleTableTbMapper;
import com.sunyard.ars.system.service.busm.SmRoleTableTbService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.dao.ButtonMapper;

@Service("SmRoleTableTbService")
@Transactional
public class SmRoleTableTbServiceImp  extends BaseService implements SmRoleTableTbService {

	@Resource
	private  SmRoleTableTbMapper SmRoleTableTbMapper;
	
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		Map sysMap = requestBean.getSysMap();
		String oper_type = (String) sysMap.get("oper_type");
		SmRoleTableTB  model =(SmRoleTableTB) requestBean.getParameterList().get(0);
		String roleNo = model.getRoleNo();
		if ("saveRoleTable".equals(oper_type)) {
			SmRoleTableTbMapper.deleteByRoleNo(roleNo);
			String[] tableIds = sysMap.get("tableIds").toString().split(",");
			for (int i = 0; i < tableIds.length; i++) {
				SmRoleTableTB  bean= new SmRoleTableTB();
				bean.setRoleNo(roleNo);
				bean.setTableId(tableIds[i]);
				SmRoleTableTbMapper.save(bean);
			}
		}
		responseBean.setRetCode(ARSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("新增成功");
	}
}
