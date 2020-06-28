package com.sunyard.cop.IF.modelimpl.drag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFCommonUtil;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.drag.IPageModelService;
import com.sunyard.cop.IF.mybatis.dao.PageDetailMapper;
import com.sunyard.cop.IF.mybatis.dao.PageModelMapper;
import com.sunyard.cop.IF.mybatis.pojo.PageDetail;
import com.sunyard.cop.IF.mybatis.pojo.PageModel;
import com.sunyard.cop.IF.mybatis.pojo.User;

/**
 * 拖拽页面模板相关service实现
 * @author zxx
 *
 */
@Service("pageModelService")
@SuppressWarnings({ "rawtypes", "unchecked" })
@Transactional
public class PageModelServiceImpl implements IPageModelService {

	private ResponseBean retBean = new ResponseBean();

	@Resource
	private PageModelMapper pageModelMapper;
	@Resource
	private PageDetailMapper pageDetailMapper;

	public PageModelServiceImpl() {}

	@Override
	public ResponseBean pageModelConfig(RequestBean req) throws Exception {
		Map queryMap = new HashMap();
		Map requestMaps = req.getSysMap();
		String oper_type = String.valueOf(requestMaps.get("oper_type"));
		User user = BaseUtil.getLoginUser();
		if ("getPageModelList".equalsIgnoreCase(oper_type)) {
			// 查询全部有效页面模板
			int pageNum = (requestMaps.get("pageNum")==null)? 1: (int) requestMaps.get("pageNum"); // 当前页数
			int pageSize = (requestMaps.get("pageSize") == null) ? 0:(int) requestMaps.get("pageSize"); // 每页数据
			Page page = PageHelper.startPage(pageNum, pageSize);
			ArrayList pageModelList = getAllPageModels();
			long count = page.getTotal();
			queryMap.put("totalRow", count);
			queryMap.put("pageCurrent", pageNum);
			queryMap.put("list", pageModelList);
		} else if ("savePageModel".equalsIgnoreCase(oper_type)) {
			// 保存页面模板
			PageModel pageModel = savePageModel(req, user);
			queryMap.put("modelId", pageModel.getModelId());
			queryMap.put("modelVersion", pageModel.getModelVersion());
			queryMap.put("lastModiDate", pageModel.getLastModiDate());
		} else if ("queryById".equalsIgnoreCase(oper_type)) {
			// 通过modelId查找有效的模板信息
			PageModel pageModel = queryPageModelById(req, user);
			queryMap.put("pageModelInfo", pageModel);
		} else if ("deleteById".equalsIgnoreCase(oper_type)) {
			// 通过modelId删除模板
			boolean success = deletePageModelById(req, user);
			if (!success) {
				retBean.setRetCode(SunIFErrorMessage.JSON_ERROR);
				retBean.setRetMsg("有页面应用于该模板，不能删除！");
				return retBean;
			}
		} else if ("cloneById".equalsIgnoreCase(oper_type)) {
			// 通过modelId克隆模板
			clonePageModelById(req, user);
		} else if ("queryByIdAndDate".equalsIgnoreCase(oper_type)) {
			// 通过modelId及时间，找到该时间范围内有效的模板
			PageModel pageModel = queryPageModelByIdAndDate(req, user);
			queryMap.put("pageModelInfo", pageModel);
		}
		retBean.setRetCode(SunIFErrorMessage.SUCCESS);
		retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
		retBean.setRetMap(queryMap);
		return retBean;
	}

	/**
	 * 获取所有的页面模板
	 * @return
	 */
	private ArrayList getAllPageModels() {
		User user = BaseUtil.getLoginUser();
		PageModel pageModel = new PageModel();
		pageModel = setPageModelInfoByUser(pageModel, user);
		return pageModelMapper.selectAllActivePageModels(pageModel);
	}

	/**
	 * 保存页面模板
	 * @param req
	 * @param user
	 * @return
	 */
	private PageModel savePageModel(RequestBean req, User user) {
		String curTimeStr = SunIFCommonUtil.getFomateTimeString("");
		PageModel pageModel = (PageModel) req.getParameterList().get(0);
		pageModel = setPageModelInfoByUser(pageModel, user);
		pageModel.setLastModiDate(curTimeStr);
		if (pageModel.getModelId() == null || pageModel.getModelId() == "") {
			pageModel = insertPageModel(pageModel); // 新增一个页面
		} else {
			pageModel = updatePageModel(pageModel); // 修改一个页面
		}
		return pageModel;
	}

	/**
	 * 插入一个新增的页面模板
	 * @param pageModel
	 * @return
	 */
	private PageModel insertPageModel(PageModel pageModel) {
		pageModel.setModelId(pageModel.getLastModiDate()+SunIFCommonUtil.getRandomStrings(6));
		pageModel.setModelDel("1"); // 默认未删除
		pageModel.setModelVersion("1"); // 初始版本为1
		pageModelMapper.insertPageModel(pageModel);
		return pageModel;
	}

	/**
	 * 修改一个页面模板
	 * @param pageModel
	 * @return
	 */
	private PageModel updatePageModel(PageModel pageModel) {
		// 修改同ID未删除的Model信息，ModelDel=0,expireDate为当前时间
		pageModel.setExpireDate(pageModel.getLastModiDate());
		pageModelMapper.updatePageModelToDel(pageModel);
		// 新增一个Model信息，同ID，version+1
		pageModel.setModelVersion((Integer.parseInt(pageModel.getModelVersion())+1)+"");
		pageModel.setModelDel("1");
		pageModel.setExpireDate(null);
		pageModelMapper.insertPageModel(pageModel);
		return pageModel;
	}

	/**
	 * 根据modelId得到模板信息
	 * @param req
	 * @param user
	 * @return
	 */
	private PageModel queryPageModelById(RequestBean req, User user) {
		PageModel paramPage = (PageModel) req.getParameterList().get(0);
		paramPage = setPageModelInfoByUser(paramPage, user);
		List<PageModel> list = pageModelMapper.queryPageModelById(paramPage);
		return list.size() == 0 ? null : list.get(0);
	}

	/**
	 * 根据modelId及日期得到该时间段内有效模板信息
	 * @param req
	 * @param user
	 * @return
	 */
	private PageModel queryPageModelByIdAndDate(RequestBean req, User user) {
		PageModel paramPage = (PageModel) req.getParameterList().get(0);
		paramPage = setPageModelInfoByUser(paramPage, user);
		List<PageModel> list = pageModelMapper.queryPageModelByIdAndDate(paramPage);
		return list.size() == 0 ? null : list.get(0);
	}

	/**
	 * 根据modelId删除该模板信息(将modelDel=0)
	 * @param req
	 * @param user
	 */
	private boolean deletePageModelById(RequestBean req, User user) {
		PageModel paramPage = (PageModel) req.getParameterList().get(0);
		paramPage = setPageModelInfoByUser(paramPage, user);
		// 查询SM_PAGE_DETAIL_TB表
		PageDetail pageDetail = getPageDetail(paramPage.getModelId(), user);
		Integer count = pageDetailMapper.countByModelId(pageDetail);
		if (count != null && count.intValue() > 0) { // 存在page关联着该model，则不能删除model
			return false;
		}
		// 删除model
		pageModelMapper.updatePageModelToDel(paramPage);
		return true;
	}

	/**
	 * 克隆指定的模板
	 * @param req
	 * @param user
	 */
	private void clonePageModelById(RequestBean req, User user) {
		PageModel paramPage = (PageModel) req.getParameterList().get(0);
		paramPage = setPageModelInfoByUser(paramPage, user);
		// 得到要复制模板
		List<PageModel> list = pageModelMapper.queryPageModelById(paramPage);
		if (list == null || list.size() == 0) {
			return;
		}
		PageModel targetModel = list.get(0); // 要复制的model
		// 赋予新的属性
		targetModel.setLastModiDate(SunIFCommonUtil.getFomateTimeString(""));
		targetModel.setModelName(paramPage.getModelName());
		// 插入model
		insertPageModel(targetModel);
	}

	/**
	 * 构建pageDetail对象
	 * @param modelId 模板ID
	 * @param user
	 * @return
	 */
	private PageDetail getPageDetail(String modelId, User user) {
		PageDetail pageDetail = new PageDetail();
		pageDetail.setModelId(modelId);
		pageDetail.setBankNo(user.getBankNo());
		pageDetail.setProjectNo(user.getProjectNo());
		pageDetail.setSystemNo(user.getSystemNo());
		return pageDetail;
	}

	/**
	 * 通过user给pageModel设置共通部分信息
	 * @param pageModel
	 * @param user
	 * @return
	 */
	private PageModel setPageModelInfoByUser(PageModel pageModel, User user) {
		pageModel.setBankNo(user.getBankNo());
		pageModel.setProjectNo(user.getProjectNo());
		pageModel.setSystemNo(user.getSystemNo());
		return pageModel;
	}

}
