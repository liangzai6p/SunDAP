package com.sunyard.cop.IF.modelimpl.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.support.IWidgetsService;
import com.sunyard.cop.IF.mybatis.dao.WidgetsMapper;
import com.sunyard.cop.IF.mybatis.pojo.User;
import com.sunyard.cop.IF.mybatis.pojo.Widgets;

@Service("widgetsService")
@SuppressWarnings({ "rawtypes", "unchecked" })
@Transactional
public class WidgetsServiceImpl implements IWidgetsService {

	@Resource
	private WidgetsMapper widgetsMapper;

	public WidgetsServiceImpl() {

	}

	@Override
	public ResponseBean widgetConfig(RequestBean req) throws Exception {
		ResponseBean retBean = new ResponseBean();
		Map queryMap = new HashMap();
		Map requestMaps = req.getSysMap();
		String oper_type = String.valueOf(requestMaps.get("oper_type"));
		// 查询全部组件
		if ("all".equalsIgnoreCase(oper_type)) {
			ArrayList widgetList = getAllWidgets();
			queryMap.put("widgetList", widgetList);
		} else if ("getWidgetsForPage".equalsIgnoreCase(oper_type)) {
			List<Widgets> widgetList = getWidgetsForPage();
			queryMap.put("widgetList", widgetList);
		} else if ("getWidgetsForModel".equalsIgnoreCase(oper_type)) {
			List<Widgets> widgetList = getWidgetsForModel();
			queryMap.put("widgetList", widgetList);
		}
		retBean.setRetCode(SunIFErrorMessage.SUCCESS);
		retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
		retBean.setRetMap(queryMap);
		return retBean;
	}

	/**
	 * 获取所有的组件
	 * 
	 * @return
	 */
	private ArrayList getAllWidgets() {
		User user = BaseUtil.getLoginUser();
		Widgets widgets = new Widgets();
		widgets.setBankNo(user.getBankNo());
		widgets.setSystemNo(user.getSystemNo());
		widgets.setProjectNo(user.getProjectNo());
		return widgetsMapper.selectWidgets(widgets);
	}

	/**
	 * 为page得到要显示的组件列表
	 * 
	 * @return
	 */
	private List getWidgetsForPage() {
		List<Widgets> allWidgets = getAllWidgets();
		List<Widgets> widgetsForPage = new ArrayList<Widgets>();
		for (Widgets widget : allWidgets) {
			// form表单组件，按钮，图片，可在page页显示
			if (("0002".equals(widget.getParentWidget())
					&& !"00021".equals(widget.getWidgetId())
					&& !"000211".equals(widget.getWidgetId()) 
					&& !"00022".equals(widget.getWidgetId()))
					|| "00031".equals(widget.getWidgetId())
					|| "000312".equals(widget.getWidgetId())
					|| "00032".equals(widget.getWidgetId())) {
				widgetsForPage.add(widget);
			}
		}
		return widgetsForPage;
	}

	/**
	 * 为model得到要显示的组件列表
	 * 
	 * @return
	 */
	private List<Widgets> getWidgetsForModel() {
		List<Widgets> allWidgets = getAllWidgets();
		List<Widgets> widgetsForPage = new ArrayList<Widgets>();
		for (Widgets widget : allWidgets) {
			// form表单组件不显示
			if ("0002".equals(widget.getParentWidget())
					&& !"00021".equals(widget.getWidgetId())
					&& !"000211".equals(widget.getWidgetId())
					&& !"00022".equals(widget.getWidgetId())) {
				continue;
			}
			widgetsForPage.add(widget);
		}
		return widgetsForPage;
	}

}
