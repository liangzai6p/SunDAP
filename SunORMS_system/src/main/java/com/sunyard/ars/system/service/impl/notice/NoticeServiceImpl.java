package com.sunyard.ars.system.service.impl.notice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.comm.AOSConstants;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.ars.common.comm.ARSConstants;
import com.sunyard.ars.common.comm.BaseService;
import com.sunyard.ars.system.bean.notice.NoticeBean;
import com.sunyard.ars.system.dao.notice.NoticeDao;
import com.sunyard.ars.system.service.notice.INoticeService;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;

/**
 * @author: wwx
 * @date: 2017年12月18日 下午2:43:27
 * @description: TODO(INoticeService实现类)
 */
@Service("noticeService")
@Transactional
public class NoticeServiceImpl extends BaseService implements INoticeService {

	// 数据库接口
	@Resource
	private NoticeDao noticeDao;

	/**
	 * @author: wwx
	 * @date: 2017年12月18日 下午2:44:56
	 * @description: TODO(执行接口逻辑)
	 */
	@Override
	public ResponseBean execute(RequestBean requestBean) throws Exception {
		ResponseBean responseBean = new ResponseBean();
		return executeAction(requestBean, responseBean);
	}

	/**
	 * @author: wwx
	 * @date: 2017年12月18日 下午2:46:20
	 * @description: TODO(执行具体操作：增、删、改、查 等，操作成功后记录日志信息)
	 */
	@Override
	@SuppressWarnings({ "rawtypes" })
	protected void doAction(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取参数集合
		Map sysMap = requestBean.getSysMap();
		// 获取操作标识
		String oper_type = (String) sysMap.get("oper_type");
		if (AOSConstants.OPERATE_QUERY.equals(oper_type)) {
			String method = (String) requestBean.getSysMap().get("method");
			if ("read".equals(method)) {
				// 阅读详情
				readQueryOperation(requestBean, responseBean);
			} else if ("noticeInfo".equals(method)) {
				// 查看公告
				noticeInfoQueryOperation(requestBean, responseBean);
			}else if (("initIndexPage").equals(method)) {
				// 首页展示公告信息列表查询，以时间排序
				initIndexPageNoticeInfoQueryOperation(requestBean, responseBean);
			} else if("readNum".equals(method)){
				// 更新阅读量
				ReadNumChangeOperation(requestBean, responseBean);
			}else {
				// 发布公告查询
				queryOperation(requestBean, responseBean);
			} 
		} else if (AOSConstants.OPERATE_ADD.equals(oper_type)) {
			// 新增
			addOperation(requestBean, responseBean);
		} else if (AOSConstants.OPERATE_MODIFY.equals(oper_type)) {
			// 更新阅读状态
			modifyOperation(requestBean, responseBean);
		} else if (AOSConstants.OPERATE_DELETE.equals(oper_type)) {
			// 删除
			deleteOperation(requestBean, responseBean);
		}
		
	}

	/**
	 * @author: wj
	 * @throws Exception
	 * @date: 2019年6月18日 下午5:08:11
	 * @description: TODO(更新阅读量)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void ReadNumChangeOperation(RequestBean requestBean,
			ResponseBean responseBean) throws Exception {
		Map sysMap = requestBean.getSysMap();
		String notice_id = (String) sysMap.get("notice_id");
		String user_no = (String) sysMap.get("user_no");
		HashMap condMap = new HashMap();
		condMap.put("user_no", user_no);
		condMap.put("notice_id", notice_id);
		condMap = addExtraCondition(condMap);
		noticeDao.updateReadNum(condMap);
		int read_num = noticeDao.selecthaveReadNum(condMap);
		Map retMap = new HashMap<Object, Object>();
		retMap.put("read_num", read_num);
		// 拼装返回信息
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * @author: wwx
	 * @throws Exception
	 * @date: 2017年12月19日 下午3:08:11
	 * @description: TODO(查询可查看公告操作)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void noticeInfoQueryOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		String user_no = (String) sysMap.get("user_no");
		String notice_id = (String) sysMap.get("notice_id");
		String read_state = (String) sysMap.get("read_state");

		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("user_no", user_no);
		condMap.put("notice_id", notice_id);
		condMap.put("read_state", read_state);
		condMap = addExtraCondition(condMap);

		// 查询信息 notice_id为空为查询公告信息 不为空为查公告详情 并拼装返回数据
		Map retMap = new HashMap<Object, Object>();
		if (BaseUtil.isBlank(notice_id)) {
			if (!BaseUtil.isBlank(read_state)) {
				List notices = getList(noticeDao.selectNoticeInfo(condMap),null);
				int notice_nums = noticeDao.selectNoticeNum(condMap);
				retMap.put("notices", notices);
				retMap.put("notice_nums", notice_nums);
			} else {
				responseBean.setRetMsg("未获取到参数：read_state");
				responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
				return;
			}
		} else {
			List notice = getList(noticeDao.selectNoticeDetails(condMap),null);
			String read_time = BaseUtil.getCurrentTimeStr();
			// 更新阅读状态
			if ("0".equals(read_state)) {
				condMap.put("read_time", read_time);
				noticeDao.update(condMap);
			}
			retMap.put("notice", notice);

			// 记录操作日志
			String logContent = "公告阅读信息，||{ 公告ID：" + notice_id + "，阅读人："
					+ user_no + "，阅读时间：" + read_time + " }";
			//addLogInfo(logContent);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_4, logContent);
		}

		// 拼装返回信息
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * @author: wwx
	 * @throws Exception
	 * @date: 2017年12月19日 下午3:08:13
	 * @description: TODO(查询阅读详情操作)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void readQueryOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();

		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("notice_id", sysMap.get("notice_id"));
		condMap.put("read_state", sysMap.get("read_state"));
		condMap = addExtraCondition(condMap);

		// 当前页数
		int pageNum = (int) sysMap.get("currentPage");
		// 每页数量
		int pageSize = (int) sysMap.get("pageSize");
		// 定义分页操作，pageSize = 0 时查询全部数据（不分页），pageNum <= 0 时查询第一页，pageNum >
		// pages（超过总数时），查询最后一页
		Page page = PageHelper.startPage(pageNum, pageSize);
		// 查询分页记录
		List noticeread = getList(noticeDao.selectRead(condMap), page);
		int allReadNum = noticeDao.selecthaveReadNum(condMap);
		// 获取总记录数
		long totalCount = page.getTotal();

		// 查询阅读人数占比情况
		List readInfo = noticeDao.selectReadNum(condMap);
		// 未阅人数
		int unreadnum = 0;
		// 已阅人数
		int readnum = 0;
		for (int i = 0; i < readInfo.size(); i++) {
			Map numInfo = (Map) readInfo.get(i);
			String read_state = (String) numInfo.get("READ_STATE");
			if ("0".equals(read_state)) {
				unreadnum = Integer.parseInt(numInfo.get("NUM").toString());
			} else if ("1".equals(read_state)) {
				readnum = Integer.parseInt(numInfo.get("NUM").toString());
			}
		}

		// 拼装返回信息
		Map retMap = new HashMap<Object, Object>();
		retMap.put("noticeread", noticeread);
		retMap.put("allReadNum", allReadNum);
		retMap.put("pageSize", pageSize);
		retMap.put("allRow", totalCount);
		retMap.put("readnum", readnum);
		retMap.put("unreadnum", unreadnum);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * @author: wwx
	 * @date: 2017年12月19日 上午10:40:19
	 * @description: TODO(查询发布公告操作)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void queryOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();

		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("organ_no", sysMap.get("organ_no"));
		condMap.put("notice_keyword", sysMap.get("notice_keyword"));
		condMap.put("publish_time1", sysMap.get("publish_time1"));
		condMap.put("publish_time2", sysMap.get("publish_time2"));
		condMap.put("publish_user", sysMap.get("publish_user"));
		condMap = addExtraCondition(condMap);

		// 当前页数
		int pageNum = (int) sysMap.get("currentPage");
		// 每页数量
		int pageSize = (int) sysMap.get("pageSize");
		// 定义分页操作，pageSize = 0 时查询全部数据（不分页），pageNum <= 0 时查询第一页，pageNum >
		// pages（超过总数时），查询最后一页
		Page page = PageHelper.startPage(pageNum, pageSize);
		// 查询分页记录
		List list = getList(noticeDao.selectNotice(condMap), page);
		// 获取总记录数
		long totalCount = page.getTotal();

		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("pageNum", pageNum);
		retMap.put("pageSize", pageSize);
		retMap.put("allRow", totalCount);
		retMap.put("notices", list);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

	/**
	 * @author: wwx
	 * @date: 2017年12月19日 上午10:50:32
	 * @description: TODO(发布公告操作)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();

		// 获取新增数据
		NoticeBean notice = (NoticeBean) requestBean.getParameterList().get(0);
		String organs = notice.getOrgans();
		String roles = notice.getRoles();
		String notice_id = BaseUtil.getSerialNumber();
		notice.setNotice_id(notice_id);
		notice.setRead_state("0");

		// 构造条件参数
		HashMap condMap = new HashMap();
		if (!BaseUtil.isBlank(organs)) {
			String[] organinfo = organs.split(",");
			condMap.put("organinfo", organinfo);
		}
		if (!BaseUtil.isBlank(roles)) {
			String[] roleinfo = roles.split(",");
			condMap.put("roleinfo", roleinfo);
		}
		condMap.put("notice", notice);
		condMap = addExtraCondition(condMap);

		// 新增公告信息
		noticeDao.insert(condMap);
		
		String[] organinfo = (String[])condMap.get("organinfo");
		if(organinfo != null && organinfo.length>1000) {
			int length = organinfo.length;
			int loopTime = length%1000 == 0 ? (length / 1000) : (length / 1000) + 1;
			for(int i=0; i<loopTime; i++) {
				int end = (i+1)*1000 > length ? length : (i+1)*1000;
				String[] organTemp = Arrays.copyOfRange(organinfo, i*1000, end);
				condMap.put("organinfo", organTemp);
				noticeDao.insert2(condMap);
			}
		}else {
			// 新增公告用户关联信息
			noticeDao.insert2(condMap);
		}
/*
		// 查询在线可阅用户信息
		List users = getList(noticeDao.selectUsers(condMap),null);
		// 推送公告信息
		if (users.size() > 0) {
			// 推送公告信息
			Map data = new HashMap();
			data.put("notice_id", notice.getNotice_id());
			data.put("notice_title", notice.getNotice_title());
			data.put("title", notice.getNotice_title());
			data.put("notice_content", notice.getNotice_content());
			data.put("file_url", notice.getFile_url());
			data.put("publish_user", notice.getPublish_user());
			data.put("user_name", sysMap.get("user_name"));
			data.put("publish_organ", notice.getPublish_organ());
			data.put("publish_time", notice.getPublish_time());
			data.put("pub_time", notice.getPublish_time());
			data.put("initType", AOSConstants.MSG_TYPE_NOTICE);
			HttpUtil.sendToDoMessage(users, data, AOSConstants.MSG_TYPE_MESSAGE);
		}
*/	
		// 记录操作日志
		String logContent = "新增公告信息，||{ 公告ID：" + notice.getNotice_id()
				+ "，公告标题：" + notice.getNotice_title() + "，发布人："
				+ notice.getPublish_user() + "-" + sysMap.get("user_name")
				+ " }";
		//addLogInfo(logContent);
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_1, logContent);

		// 拼装返回信息
		Map retMap = new HashMap();
		retMap.put("notice_id", notice_id);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("新增成功");
	}

	/**
	 * @author: wwx
	 * @date: 2017年12月19日 下午2:31:41
	 * @description: TODO(更新阅读状态操作)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void modifyOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();
		// 构造条件参数
		HashMap condMap = new HashMap();
		String read_time = BaseUtil.getCurrentTimeStr();
		condMap.put("user_no", sysMap.get("user_no"));
		condMap.put("notice_id", sysMap.get("notice_id"));
		condMap.put("read_time", read_time);
		condMap = addExtraCondition(condMap);

		// 修改数据
		noticeDao.update(condMap);

		// 记录操作日志
		String logContent = "公告阅读信息，，||{ 公告ID：" + sysMap.get("notice_id")
				+ "，阅读人：" + sysMap.get("user_no") + "，阅读时间：" + read_time + " }";
		//addLogInfo(logContent);
		addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_3, logContent);
		
		// 拼装返回信息
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("修改成功");
	}

	/**
	 * @author: wwx
	 * @date: 2017年12月19日 下午2:33:13
	 * @description: TODO(删除公告信息操作)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void deleteOperation(RequestBean requestBean, ResponseBean responseBean) throws Exception {
		// 获取删除数据
		List del_nos = requestBean.getParameterList();

		// 构造条件参数
		int len = del_nos.size();
		String[] notice_ids = new String[len];
		for (int i = 0; i <len ; i++) {
			NoticeBean n = (NoticeBean) del_nos.get(i);
			notice_ids[i] = n.getNotice_id();
		}
		HashMap condMap = new HashMap();
		condMap.put("notice_ids", notice_ids);
		condMap = addExtraCondition(condMap);

		// 删除公告信息
		int del1 = noticeDao.deleteNotice(condMap);
		// 删除公告用户关联信息
		int del2 = noticeDao.deleteUsers(condMap);

		// 记录操作日志
		for (int i = 0; i < del_nos.size(); i++) {
			NoticeBean notice = (NoticeBean) del_nos.get(i);
			String logContent = "删除公告信息，||{公告ID：" + notice.getNotice_id()
					+ "，公告标题：" + notice.getNotice_title() + "}";
			//addLogInfo(logContent);
			addOperLogInfo(ARSConstants.MODEL_NAME_SYS_MANAGEMENT, ARSConstants.OPER_TYPE_2, logContent);
		}

		// 拼装返回信息
		if (del1 > 0 && del2 >= 0) {
			responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
			responseBean.setRetMsg("删除成功");
		} else {
			responseBean.setRetCode(AOSConstants.HANDLE_FAIL);
			responseBean.setRetMsg("删除失败");
		}
	}
	
	/**
	 * @description: TODO(首页查询公告信息操作)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initIndexPageNoticeInfoQueryOperation(RequestBean requestBean,
			ResponseBean responseBean) throws Exception {
		// 前台参数集合
		Map sysMap = requestBean.getSysMap();

		// 构造条件参数
		HashMap condMap = new HashMap();
		condMap.put("organ_no", sysMap.get("organ_no"));// 当前用户机构号
		condMap.put("user_no", sysMap.get("user_no"));// 当前用户用户号

		condMap = addExtraCondition(condMap);
		// 执行查询操作
		List noticeInfo = getList(
				noticeDao.initIndexPageNoticeInfoQuery(condMap), null);

		// 拼装返回信息
		Map retMap = new HashMap<Object, Object>();
		retMap.put("notice_info_list", noticeInfo);
		responseBean.setRetMap(retMap);
		responseBean.setRetCode(AOSConstants.HANDLE_SUCCESS);
		responseBean.setRetMsg("查询成功");
	}

}
