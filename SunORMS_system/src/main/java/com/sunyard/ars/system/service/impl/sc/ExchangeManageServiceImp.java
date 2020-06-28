package com.sunyard.ars.system.service.impl.sc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.system.bean.sc.ExchangeManageBean;
import com.sunyard.ars.system.dao.sc.ExchangeManageMapper;
import com.sunyard.ars.system.service.sc.IExchangeManageService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.mybatis.pojo.User;

@Service
@Transactional
public class ExchangeManageServiceImp extends BaseService implements IExchangeManageService {

	@Resource
	private ExchangeManageMapper exchangeManageMapper;
	
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);	
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		ExchangeManageBean  Model=(ExchangeManageBean)requestBean.getParameterList().get(0);
		Map sysMap = requestBean.getSysMap();
		String oper_type = (String) sysMap.get("oper_type");
		List<Map<String,Object>> list=null;
		Map<String, Object> map = new HashMap<String, Object>();
		String  log="";
		Date date=new Date();
		SimpleDateFormat  sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//添加
		if(AOSConstants.OPERATE_ADD.equals(oper_type)){
			Model.setAddTime(sdf.format(date));
			Integer id = add();
			Model.setId(id);
			map.put("Bean", Model);
			Map<Object, Object> retmap =new HashMap<Object, Object>();
			retmap.put("id", id);
			exchangeManageMapper.save(map);	
			//当添加的时候添加日志
			log="账户号："+Model.getAcctNo()+" 帐户名："+Model.getAcctName()+" 电话："
			+Model.getTelephone()+"在"+sdf.format(date)+"在异常交易账户名单中被添加";
			//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG,ARSConstants.OPER_TYPE_1,log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT,ARSConstants.OPER_TYPE_1,log);
			responseBean.setRetMap(retmap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("操作成功");
			
		}else if(AOSConstants.OPERATE_MODIFY.equals(oper_type)) {
			//修改
			Model.setLastModiTime(sdf.format(date));
			map.put("Bean", Model);
			exchangeManageMapper.update(map);
			
			log="账户号："+Model.getAcctNo()+" 帐户名："+Model.getAcctName()+" 电话："
					+Model.getTelephone()+"在时间："+sdf.format(date)+" 在异常交易账户名单中被修改";
			//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG,ARSConstants.OPER_TYPE_3,log);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT,ARSConstants.OPER_TYPE_3,log);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("操作成功");
		}else if(AOSConstants.OPERATE_DELETE.equals(oper_type)) {
		//删除
			List<?> delList = requestBean.getParameterList();
			for(int i=0; i<delList.size(); i++) {
				Model = (ExchangeManageBean) delList.get(i);
				map.put("Bean", Model);
				exchangeManageMapper.delete(map);
				log="ID为："+Model.getId()+"的记录在时间："+sdf.format(date)+" 在异常交易账户名单中被删除";
				//addOperLogInfo(ARSConstants.MODEL_NAME_SYSCONFIG,ARSConstants.OPER_TYPE_2,log);
				addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT,ARSConstants.OPER_TYPE_2,log);
			}
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("操作成功");
		}else if(AOSConstants.OPERATE_QUERY.equals(oper_type)) {
		//查询
			int pageNum = Integer.parseInt(sysMap.get("currentPage").toString());
			int initPageNum = (int) sysMap.get("user_pageNum");//15
			Page page = PageHelper.startPage(pageNum, initPageNum);
			map.put("Bean", Model);
			list = getList(exchangeManageMapper.select(map), page);
			long totalCount = page.getTotal();
			Map<Object, Object> retMap = new HashMap<Object, Object>();
			retMap.put("currentPage", pageNum);
			retMap.put("pageNum", initPageNum);
			retMap.put("totalNum", totalCount);
			retMap.put("returnList", list);
			responseBean.setRetMap(retMap);
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("查询成功");
		}
	}
		//防止高并发环境下ID发生冲突
		public  synchronized  Integer  add() throws Exception{
			Integer maxId = exchangeManageMapper.getMaxId();
			if(maxId==null){
				maxId=0;
			}
			maxId = maxId+1;
			maxId = Integer.valueOf(BaseUtil.filterSqlParam(maxId.toString()));
			return maxId;
		}
}
