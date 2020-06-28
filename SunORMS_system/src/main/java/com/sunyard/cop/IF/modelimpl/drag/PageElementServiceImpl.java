package com.sunyard.cop.IF.modelimpl.drag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;



import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.drag.IPageElementService;
import com.sunyard.cop.IF.mybatis.dao.PageElementMapper;
import com.sunyard.cop.IF.mybatis.pojo.PageElement;
import com.sunyard.cop.IF.mybatis.pojo.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 拖拽页面要素实现类
 * @author wqq
 *
 */
@Service("pageElementService")
@Transactional
public class PageElementServiceImpl implements IPageElementService{

	private ResponseBean retBean = new ResponseBean();
	
	public PageElementServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Resource
	private PageElementMapper elementMapper;
	
	@Override
	public ResponseBean elementService(RequestBean req) throws Exception {
		User user = BaseUtil.getLoginUser();
		PageElement element = new PageElement();
		element.setBankNo(user.getBankNo());
		element.setProjectNo(user.getProjectNo());
		element.setSystemNo(user.getSystemNo());
		
		Map reqMap = new HashMap();
		reqMap = req.getSysMap();
		String operType = (String) reqMap.get("operType");
		Map retMap = new HashMap();
		
		if("QUERYALL".equalsIgnoreCase(operType)){
			if(reqMap.get("pageNum")!=null){
				int pageNum = (int) reqMap.get("pageNum"); // 当前页数
				int pageSize = (int) reqMap.get("pageSize"); // 每页数据
				Page page = PageHelper.startPage(pageNum, pageSize);
				ArrayList elementList = this.selectAllElement(element);
				if(elementList!=null){
					long count = page.getTotal();
					retMap.put("totalRow", count);
					retMap.put("pageCurrent", pageNum);
					retMap.put("list", elementList);
				}
			}else{
				ArrayList elementList = this.selectAllElement(element);
				if(elementList!=null){
					retMap.put("elementList", elementList);
				}
			}
			
		}else if("addElement".equalsIgnoreCase(operType)){
			ArrayList reqList =(ArrayList) req.getParameterList();
			PageElement addElement = (PageElement) reqList.get(0);
			element.setElementId(addElement.getElementId());
			element.setLabelName(addElement.getLabelName());
			element.setElementName(addElement.getElementName());
			element.setElementType(addElement.getElementType());
			element.setElementHtml(addElement.getElementHtml());
			element.setElementJs(addElement.getElementJs());
			elementMapper.addElement(element);
		}else if("editElement".equalsIgnoreCase(operType)){
			ArrayList reqList =(ArrayList) req.getParameterList();
			PageElement editElement = (PageElement) reqList.get(0);
			element.setElementId(editElement.getElementId());
			element.setLabelName(editElement.getLabelName());
			element.setElementName(editElement.getElementName());
			element.setElementType(editElement.getElementType());
			element.setElementHtml(editElement.getElementHtml());
			element.setElementJs(editElement.getElementJs());
			elementMapper.editElement(element);
		}else if("delElement".equalsIgnoreCase(operType)){
			ArrayList reqList =(ArrayList) req.getParameterList();
			PageElement delElement = (PageElement) reqList.get(0);
			element.setElementId(delElement.getElementId());
			elementMapper.delElement(element);
		}
		
		retBean.setRetCode(SunIFErrorMessage.SUCCESS);
		retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
		retBean.setRetMap(retMap);
		return retBean;
	}
	
	/**
	 * 查询所有要素
	 * @param element
	 * @return
	 */
	private ArrayList selectAllElement(PageElement element){
		return elementMapper.selectAllElement(element);
	}

}
