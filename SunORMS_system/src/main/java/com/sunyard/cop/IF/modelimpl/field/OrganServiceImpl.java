package com.sunyard.cop.IF.modelimpl.field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.cop.IF.bean.OrganZTreeBean;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.field.IOrganService;
import com.sunyard.cop.IF.mybatis.dao.OrganMapper;
import com.sunyard.cop.IF.mybatis.pojo.Organ;
import com.sunyard.cop.IF.mybatis.pojo.User;

/**
 * 机构查询 实现类
 * 
 * @author YZ 2017年3月20日 下午1:44:18
 */
@Service("organService")
@Transactional
public class OrganServiceImpl implements IOrganService {

	private Logger logger = LoggerFactory.getLogger(OrganServiceImpl.class);

	@Resource
	private OrganMapper organMapper;

	/**
	 * 
	 */
	public OrganServiceImpl() {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseBean getOrganization(RequestBean requestBean) throws Exception {
		ResponseBean returnRespBean = new ResponseBean();
		Map sysMap = requestBean.getSysMap();
		String oper_type = "-1";
		if (sysMap.containsKey("oper_type")) {
			oper_type = String.valueOf(sysMap.get("oper_type"));
			User user = BaseUtil.getLoginUser();
			if ("0".equals(oper_type)) {
				logger.info("查询所有机构数据");
				Map returnMap = new HashMap();
				Organ organ = new Organ();
				organ.setBankNo(user.getBankNo());
				organ.setSystemNo(user.getSystemNo());
				organ.setProjectNo(user.getProjectNo());
				ArrayList list = getAllOrgans(organ);
				if (list == null) {
					returnMap.put("organFlag", false);
					returnMap.put("organList", null);
				} else {
					returnMap.put("organFlag", true);
					returnMap.put("organList", list);
				}
				returnRespBean.setRetCode(SunIFErrorMessage.SUCCESS);
				returnRespBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
				returnRespBean.setRetMap(returnMap);
			}
		} else {
			logger.error(" RequestBean中未取到oper_type,请检查发送的数据是否正确 ");
			returnRespBean.setRetCode(SunIFErrorMessage.ILLEGAL_ARGUMENT);
			returnRespBean.setRetMsg(SunIFErrorMessage.ILLEGAL_ARGUMENT_MSG);
		}
		return returnRespBean;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList getAllOrgans(Organ organ) {
		ArrayList list = new ArrayList();
		try {
			ArrayList tempList = organMapper.getAllOrgans(organ);
			for (Object o : tempList) {
				HashMap maps = (HashMap) o;
				OrganZTreeBean oztBean = new OrganZTreeBean();
				for (Object obj : maps.keySet()) {
					String key = obj + "";
					String value = maps.get(key) + "";
					if (key.equalsIgnoreCase("organ_count")) {
						if ("".equals(value) || "null".equals(value)) {
							oztBean.setIsParent("false");
						} else {
							oztBean.setIsParent("true");
						}
					}
					// oztBean.setIcon("groupIcon");
					if (key.equalsIgnoreCase("organ_no")) {
						oztBean.setId(value.trim());
					} else if (key.equalsIgnoreCase("organ_name")) {
						oztBean.setName(value.trim());
					} else if (key.equalsIgnoreCase("parent_organ")) {
						oztBean.setpId(value.trim());
					} else if (key.equalsIgnoreCase("organ_level")) {
						if ("1".equals(value.trim())) {
							oztBean.setOpen("true");
						} else {
							oztBean.setOpen("false");
						}
						// 临时存储 机构级别
						oztBean.setReserve(value);
					}
				}
				oztBean.setIcon("");
				list.add(oztBean);
			}
		} catch (Exception e) {
			list = null;
			logger.error("查询机构数据出错: " + e.getMessage(), e);
		}
		return list;
	}
}
