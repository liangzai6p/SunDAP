package com.sunyard.ars.system.service.impl.busm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.busm.ArmsUserTaltTbBean;
import com.sunyard.ars.system.bean.busm.RoleBean;
import com.sunyard.ars.system.bean.busm.UserBean;
import com.sunyard.ars.system.bean.busm.UserRole;
import com.sunyard.ars.system.dao.busm.ArmsUserTaltTbMapper;
import com.sunyard.ars.system.dao.busm.UserRoleMapper;
import com.sunyard.ars.system.service.busm.LosePriService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.mybatis.dao.RoleMapper;
import com.sunyard.cop.IF.mybatis.dao.UserMapper;

@Service
@Transactional

public class IosePriServiceImp extends BaseService implements LosePriService {

	@Resource
	private com.sunyard.ars.system.dao.busm.UserDao UserDao;

	@Resource
	private ArmsUserTaltTbMapper  ArmsUserTaltTbMapper;

	@Resource
	private	 UserRoleMapper  UserRoleMapper;

	@Resource
	private	 com.sunyard.ars.system.dao.busm.SysRoleDao  SysRoleDao;


	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		ArmsUserTaltTbBean Model=(ArmsUserTaltTbBean)requestBean.getParameterList().get(0);
		Map sysMap = requestBean.getSysMap();
		String oper_type = (String) sysMap.get("oper_type");
		List<Map<String,Object>> list=null;//结果集合！
		Map<String, Object> map = new HashMap<String, Object>();//参数map

		if(AOSConstants.OPERATE_QUERY.equals(oper_type)){
			//获取当前部门下的所有人员  填充下拉框
			map.put("userNo", sysMap.get("userNo"));
			map.put("organNo",sysMap.get("organNo"));
			list = UserDao.selectUser(map);
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			retMap.put("returnList", list);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else if("OPERATE_LOSEPRI".equals(oper_type)){
			//1:获取当前人的ID  sunaos
			Date  now= new Date();
			SimpleDateFormat sdfB =new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat sdfA =new SimpleDateFormat("yyyy-MM-dd");
			Date sdate = sdfA.parse(Model.getEffectDate());//获取开始日期
			Date edate = sdfA.parse(Model.getExpDate());//获取结束日期
			String  organNo= sysMap.get("organNo").toString();
			String  userNo= Model.getUserNo().toString();
			//查询目标是否有转授权的角色！ 如果有！此次收权失败！不允许多人给同一个人的情况
			/*UserRole ta=new UserRole();
			ta.setUserNo(Model.getTargetUserNo());
			ta.setIsTransmit("1");
			List<UserRole> isExist = UserRoleMapper.selectIsExist(ta);
			if(isExist.size()>0){
				responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("不允许多人给相同人重复转授权");
				return ;
			}*/

			// （一个时间段当中，一个用户只能转一个授权   并  一个用户只能被转一次）
			//查询备份表当中，是否有别人转授权给目标用户，同一时间段当中最多只有一条记录   （一个用户只能被转一次，相同时间段）
			HashMap<String, Object> config = new HashMap<String, Object>();
			config.put("targetNo", Model.getTargetUserNo());
			//笔误 把map.put("status", "0");的 config写成了 map
			config.put("status", "0");
			List<Map<String, Object>> selectList = ArmsUserTaltTbMapper.select(config);
			if(selectList != null && selectList.size() > 0) {
				for (int i = 0; i < selectList.size(); i++) {
					String startDate = selectList.get(i).get("EFFECT_DATE").toString();
					String endDate = selectList.get(i).get("EXP_DATE").toString();
					if(!(sdate.compareTo(sdfA.parse(endDate))>=0)){
						if(edate.compareTo(sdfA.parse(startDate))>0){
							//一段时间是 左闭右开 ，结束时间和开始时间可以重合，开始时间和开始时间不能重回
							responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
							String otherUser = selectList.get(i).get("USER_NO").toString();
							String targetUser = selectList.get(i).get("TARGET_USER_NO").toString();
							responseBean.setRetMsg( otherUser + "授权给" + targetUser + ",日期为["
									+ startDate +" 到 "+ endDate +"),故当前时间段不能转授权");
							return ;
						}
					}
				}
			}


			//查询当前用户的所有角色！ 不包含转授权的角色！
			UserRole record=new UserRole();
			record.setUserNo(userNo);
			record.setIsTransmit("1");
			List<UserRole> userNoList = UserRoleMapper.selectBySelectiveOfMine(record);//获取的是!=1 而且也允许null的角色
			//从备份表中获取当前人的角色集合！
			map.put("userNo", userNo);
			map.put("status", "0");
			list = ArmsUserTaltTbMapper.select(map);
			// 如果备份表中的角色>0  说明当前用户存在转授权！ 就需要判断是否与已经存在的时间段发生冲突
			if(list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					/*	如果已有11--15的记录   不允许插入15--18的记录！
					因为15当天凌晨一点转授权人正在回收角色！
					如果15--18的记录存在且先执行！等于是转授权人没有角色可以转出

					如果业务一定要将15--18的记录插入，在条件A上加个=号即可！
					但是定时转授权可能会出现问题！ 那就更改查询条件！
					根据生效开始日期ASC方式排序！确保11-15的记录一定能先执行收回角色然后转出！
					*/

					String startDate = list.get(i).get("EFFECT_DATE").toString();
					String endDate = list.get(i).get("EXP_DATE").toString();
					/*if(!(sdate.compareTo(sdfA.parse(endDate))>0)){
						if(edate.compareTo(sdfA.parse(startDate))>=0){
							//条件A  参数开始日期<=已有记录的结束时间
							//条件B	 参数结束日期>=已有记录的开始日期  //俩个IF如果成立！必然和当前时间段重复！
							responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
							responseBean.setRetMsg("所选时间段内已有转授权记录,故不能转授权");
							return ;
						}
					}*/

					if(!(sdate.compareTo(sdfA.parse(endDate))>=0)){
						if(edate.compareTo(sdfA.parse(startDate))>0){
							responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
							responseBean.setRetMsg("所选时间段内已有转授权记录,故不能转授权");
							return ;
						}
					}

				}
			}
			//sdate 如果开始日期与当前时间一致！那么立刻生效
			if(sdfA.format(now).equals(Model.getEffectDate())){
				//获取目标人的角色集合，不包含转授权角色
				UserRole target=new UserRole();
				target.setUserNo(Model.getTargetUserNo());
				target.setIsTransmit("1");
				List<UserRole> targetList = UserRoleMapper.selectBySelective(target,(String)RequestUtil.getRequest().getSession().getAttribute("HAS_PRIV_ORGAN_FLAG"));
				for (int i = 0; i < userNoList.size(); i++) {
					String roleNo = userNoList.get(i).getRoleNo();
					roleNo = BaseUtil.filterSqlParam(roleNo);
					//考虑被转授权角色==0的情况
					if(targetList.size()==0) {
						UserRole   tar= new UserRole();
						tar.setOrganNo(organNo);
						tar.setUserNo(Model.getTargetUserNo());
						tar.setRoleNo(roleNo);
						tar.setIsOpen("1");
						tar.setIsTransmit("1");
						tar.setLastModiDate(sdfB.format(now));
						UserRoleMapper.insertSelective(tar);
						UserRoleMapper.deleteByUserNoAndRoleNo(BaseUtil.filterSqlParam(userNo), BaseUtil.filterSqlParam(roleNo));
					}else {
						for (int j = 0; j < targetList.size(); j++) {
							String roleNo2 = targetList.get(j).getRoleNo();
							if(roleNo.equals(roleNo2)){
								//当前人有此角色  将当前人的角色删除！
								UserRoleMapper.deleteByUserNoAndRoleNo(BaseUtil.filterSqlParam(userNo), BaseUtil.filterSqlParam(roleNo));
								//给目标添加角色  此时当前人有角色！所以不需要添加！ 并且退出当前循环
								break;
							}else{
								//如果当前人的角色和目标角色不相同！而且比较到最后一个都不相同！
								if(j==targetList.size()-1){
									UserRole   tar= new UserRole();
									tar.setOrganNo(organNo);
									tar.setUserNo(Model.getTargetUserNo());
									tar.setRoleNo(roleNo);
									tar.setIsOpen("1");
									tar.setIsTransmit("1");
									tar.setLastModiDate(sdfB.format(now));
									UserRoleMapper.insertSelective(tar);
									UserRoleMapper.deleteByUserNoAndRoleNo(BaseUtil.filterSqlParam(userNo), BaseUtil.filterSqlParam(roleNo));
								}
							}
						}
					}
				}
			}
			//获取当前人的所有角色！
			String  role_str="";
			for (int i = 0; i < userNoList.size(); i++) {
				if(i==userNoList.size()-1){
					role_str+=BaseUtil.filterSqlParam(userNoList.get(i).getRoleNo());
				}else{
					role_str+=BaseUtil.filterSqlParam(userNoList.get(i).getRoleNo())+",";
				}
			}
			//封装当前人角色集合到备份表中！ 在指定时间内内给目标角色！
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String[] split = sdf.format(new Date()).split("\\s+");
			Model.setRoleNo(role_str);
			Model.setTransdate(split[0]);
			Model.setTranstime(split[1]);
			Model.setStatus("0");//0 有效  1收回  99 失效
			Model.setBankcode(organNo);
			map.put("Bean", Model);
			ArmsUserTaltTbMapper.save(map);

			//日志信息
			String log =  userNo + "向用户" + Model.getTargetUserNo() + "转授权，日期为[ " + sdate + " ,到"
					+ edate +" )";
			addOperLogInfo(ARSConstants.MODEL_NAME_ARS, ARSConstants.OPER_TYPE_7, log);

		}else if("OPERATE_GET".equals(oper_type)){
			//准备回收权限的数据！ 填充下拉框
			String status="0";//查询未回收的数据！
			list = ArmsUserTaltTbMapper.selectGroup(sysMap.get("userNo").toString(),status);
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			retMap.put("returnList", list);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}else if("OPERATE_GETPRI".equals(oper_type)){
		/*	String userNo = sysMap.get("userNo").toString();
			String organNo = sysMap.get("organNo").toString();*/
			Date  now= new Date();
			SimpleDateFormat sdfA =new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdfB =new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat sdfC=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String[] split3 = sdfC.format(now).split("\\s+");
			String targetStr = sysMap.get("target").toString();
			String[] split = targetStr.split(":");
			map.put("targetNo", split[0]);
			map.put("startTime", split[1]);
			map.put("status", "0");
			list = ArmsUserTaltTbMapper.select(map);//集合==1
			System.out.println("收权时间："+split[1]);
			//获取当前时间
			Date nowDate = sdfA.parse(sdfA.format(new Date()));
			Date date = sdfA.parse(split[1]);
			Boolean  b=nowDate.compareTo(date)>=0;//当前时间 > =开始时间点
			if(b){
				//回收目标用户的IsTrans=1角色
				UserRoleMapper.deleteByUserNoAndRoleNoAndIsTrans1(split[0]);
				//恢复目标用户的角色
				for(int i=0;i<list.size();i++){
					String status = list.get(i).get("STATUS").toString();
					String userNo = list.get(i).get("USER_NO").toString();
					String OrganNo=UserDao.selectUserInfo(BaseUtil.filterSqlParam(userNo)).getOrganNo();
					String[] split2 = list.get(i).get("ROLE_NO").toString().split(",");

					//查询当前人是否有角色，如果没有 才执行添加 避免报主键重复错误
					//转授权角色  在收回角色的时候是没有任何角色号的！
					/*List<UserRole> collectionRoles = UserRoleMapper.getRolesByUserNo(userNo);
					if(collectionRoles.size()==0) {
						for (int j = 0; j < split2.length; j++) {
							UserRole  r=new UserRole();
							r.setOrganNo(OrganNo);
							r.setUserNo(userNo);
							r.setRoleNo(split2[j]);
							r.setIsOpen("1");
							r.setLastModiDate(sdfB.format(now));
							UserRoleMapper.insertSelective(r);
						}
					}*/
					//别人转给他授权，不需要查询当前人是否有角色
					//插入IS_TRANSMIT 为 null 的角色 ，转授权插入的IS_TRANSMIT 是1
					for (int j = 0; j < split2.length; j++) {
						UserRole  r=new UserRole();
						r.setOrganNo(BaseUtil.filterSqlParam(OrganNo));
						r.setUserNo(BaseUtil.filterSqlParam(userNo));
						r.setRoleNo(BaseUtil.filterSqlParam(split2[j]));
						r.setIsOpen("1");
						r.setLastModiDate(BaseUtil.filterSqlParam(sdfB.format(now)));
						UserRoleMapper.insertSelective(r);
					}
					status="1";
					list.get(i).put("STATUS", status);
					list.get(i).put("REVDATE", split3[0]);
					list.get(i).put("REVTIME", split3[1]);
					ArmsUserTaltTbMapper.update(list.get(i));
				}
			}else{
				//当前时间<开始时间！此时当前用户的角色还未删除！
				//直接就将状态==1！  就不需要进入定时了!
				System.out.println("当前日期小于"+split[1]);
				for(int i=0;i<list.size();i++){
					String status = list.get(i).get("STATUS").toString();
					status="1";
					list.get(i).put("STATUS", status);
					list.get(i).put("REVDATE", split3[0]);
					list.get(i).put("REVTIME", split3[1]);
					ArmsUserTaltTbMapper.update(list.get(i));
				}
			}
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("收权成功");

			String log = "回收" + split[0] + "的授权";
			addOperLogInfo(ARSConstants.MODEL_NAME_ARS, ARSConstants.OPER_TYPE_8, log);

		}if("queryUserInfoFromPri".equals(oper_type)){
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			String userNo=(String)sysMap.get("userNo");
			if(BaseUtil.isBlank(userNo)) {
				responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
				responseBean.setRetMsg("查询是否为转授权用户失败");
			}else {
				Integer integer = ArmsUserTaltTbMapper.queryUserInfoFromPri(userNo);
				retMap.put("size", integer);
			}
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		}
	}
}
