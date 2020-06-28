package com.sunyard.cop.IF.modelimpl.drag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sunyard.aos.common.util.BaseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFCommonUtil;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.drag.IPageDetailService;
import com.sunyard.cop.IF.mybatis.dao.PageDetailMapper;
import com.sunyard.cop.IF.mybatis.dao.PageDetailPartMapper;
import com.sunyard.cop.IF.mybatis.pojo.PageDetail;
import com.sunyard.cop.IF.mybatis.pojo.PageDetailPart;
import com.sunyard.cop.IF.mybatis.pojo.User;

/**
 * 拖拽页面相关service实现
 * @author zxx
 *
 */
@Service("pageDetailService")
@SuppressWarnings({ "rawtypes", "unchecked" })
@Transactional
public class PageDetailServiceImpl implements IPageDetailService {

	private ResponseBean retBean = new ResponseBean();

	@Resource
	private PageDetailMapper pageDetailMapper;
	@Resource
	private PageDetailPartMapper pageDetailPartMapper;

	public PageDetailServiceImpl() {}

	@Override
	public ResponseBean pageConfig(RequestBean req) throws Exception {
		Map queryMap = new HashMap();
		Map requestMaps = req.getSysMap();
		String oper_type = String.valueOf(requestMaps.get("oper_type"));
		User user = BaseUtil.getLoginUser();
		if ("getPageDetaillList".equalsIgnoreCase(oper_type)) {
			// 查询全部页面
			int pageNum = (int) requestMaps.get("pageNum"); // 当前页数
			int pageSize = (int) requestMaps.get("pageSize"); // 每页数据
			Page page = PageHelper.startPage(pageNum, pageSize);
			ArrayList pageList = getAllPages(user);
			long count = page.getTotal();
			queryMap.put("totalRow", count);
			queryMap.put("pageCurrent", pageNum);
			queryMap.put("list", pageList);
		} else if ("savePageDetail".equalsIgnoreCase(oper_type)) {
			// 保存页面
			savePage(req, user);
		} else if ("queryById".equalsIgnoreCase(oper_type)) {
			// 通过pageId查找有效的page信息
			PageDetail pageDetail = queryPageById(req, user);
			queryMap.put("pageInfo", pageDetail);
			List<PageDetailPart> detailParts = new ArrayList<PageDetailPart>();
			if (pageDetail != null) {
				detailParts = getDetailPartsById(
						pageDetail.getPageId(), pageDetail.getPageVersion(), user);
			}
			queryMap.put("detailParts", detailParts);
		} else if ("deleteById".equalsIgnoreCase(oper_type)) {
			// 通过pageId删除page
			deletePageById(req, user);
		} else if ("queryByIdAndDate".equalsIgnoreCase(oper_type)) {
			// 通过pageId及时间，找到该时间范围内有效的页面
			PageDetail pageDetail = queryPageModelByIdAndDate(req, user);
			queryMap.put("pageInfo", pageDetail);
			List<PageDetailPart> detailParts = new ArrayList<PageDetailPart>();
			if (pageDetail != null) {
				detailParts = getDetailPartsById(
						pageDetail.getPageId(), pageDetail.getPageVersion(), user);
			}
			queryMap.put("detailParts", detailParts);
		}
		retBean.setRetCode(SunIFErrorMessage.SUCCESS);
		retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
		retBean.setRetMap(queryMap);
		return retBean;
	}

	/**
	 * 获取所有的页面
	 * @return
	 */
	private ArrayList getAllPages(User user) {
		PageDetail pageDetail=new PageDetail();
		pageDetail = setPageInfoByUser(pageDetail, user);
		return pageDetailMapper.selectAllActivePages(pageDetail);
	}

	/**
	 * 保存页面
	 * @param req
	 * @param user
	 * @return
	 */
	private void savePage(RequestBean req, User user) {
		List<Object> paramList = req.getParameterList();
		// 保存pageDetail
		PageDetail pageDetail = (PageDetail) paramList.get(0);
		pageDetail = setPageInfoByUser(pageDetail, user);
		String curTimeStr = SunIFCommonUtil.getFomateTimeString("");
		pageDetail.setLastModiDate(curTimeStr);
		if (pageDetail.getPageId() == null) { // 新增
			pageDetail.setPageId(curTimeStr + SunIFCommonUtil.getRandomStrings(6));
		} else { // 修改,更新旧版本
			// pageDel=0,expireDate为当前时间
			pageDetail.setExpireDate(curTimeStr);
			pageDetailMapper.updatePageToDel(pageDetail);
		}
		// 保存pageDetail
		pageDetail.setPageDel("1"); // 默认未删除
		pageDetail.setPageVersion((Integer.parseInt(pageDetail.getPageVersion())+1)+"");
		pageDetail.setExpireDate(null);
		pageDetailMapper.insertPage(pageDetail);
		// 保存pageDetailPart
		for (int i = 1; i < paramList.size(); i++) {
			savePageDetailPart((PageDetail)paramList.get(i), pageDetail);
		}
	}

	/**
	 * 根据pageId得到页面信息
	 * @param req
	 * @param user
	 * @return
	 */
	private PageDetail queryPageById(RequestBean req, User user) {
		PageDetail paramPage = (PageDetail) req.getParameterList().get(0);
		paramPage = setPageInfoByUser(paramPage, user);
		List<PageDetail> list = pageDetailMapper.queryPageById(paramPage);
		return list.size() == 0 ? null : list.get(0);
	}

	/**
	 * 根据pageId及日期得到该时间段内有效页面信息
	 * @param req
	 * @param user
	 * @return
	 */
	private PageDetail queryPageModelByIdAndDate(RequestBean req, User user) {
		PageDetail paramPage = (PageDetail) req.getParameterList().get(0);
		paramPage = setPageInfoByUser(paramPage, user);
		List<PageDetail> list = pageDetailMapper.queryPageByIdAndDate(paramPage);
		return list.size() == 0 ? null : list.get(0);
	}

	/**
	 * 根据pageId得到页面组成部分信息
	 * @param req
	 * @param user
	 * @return
	 */
	private List<PageDetailPart> getDetailPartsById(String pageId, String pageVersion, User user) throws Exception {
		PageDetailPart partParam = new PageDetailPart();
		pageId = BaseUtil.filterSqlParam(pageId);
		partParam.setPageId(pageId);
		partParam.setPageVersion(pageVersion);
		partParam.setBankNo(user.getBankNo());
		partParam.setProjectNo(user.getProjectNo());
		partParam.setSystemNo(user.getSystemNo());
		return pageDetailPartMapper.queryByPageId(partParam);
	}

	/**
	 * 保存PageDetailPart对象
	 * @param partObj
	 * @param pageDetail
	 */
	private void savePageDetailPart(PageDetail partObj, PageDetail pageDetail) {
		PageDetailPart part = new PageDetailPart();
		part.setPageId(pageDetail.getPageId());
		part.setPageVersion(pageDetail.getPageVersion());
		part.setContainerId(partObj.getContainerId());
		part.setContent(partObj.getContent());
		part.setBankNo(pageDetail.getBankNo());
		part.setProjectNo(pageDetail.getProjectNo());
		part.setSystemNo(pageDetail.getSystemNo());
		pageDetailPartMapper.insert(part);
	}

	/**
	 * 根据pageId删除该Page信息(将pageDel=0)
	 * @param req
	 * @param user
	 */
	private void deletePageById(RequestBean req, User user) {
		PageDetail paramPage = (PageDetail) req.getParameterList().get(0);
		paramPage = setPageInfoByUser(paramPage, user);
		pageDetailMapper.updatePageToDel(paramPage);
	}

	/**
	 * 通过user给page设置共通部分信息
	 * @param page
	 * @param user
	 * @return
	 */
	private PageDetail setPageInfoByUser(PageDetail page, User user) {
		page.setBankNo(user.getBankNo());
		page.setProjectNo(user.getProjectNo());
		page.setSystemNo(user.getSystemNo());
		return page;
	}

}
