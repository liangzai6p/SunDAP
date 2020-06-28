package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;
import com.sunyard.cop.IF.mybatis.pojo.Widgets;

/**
 * 组件相关
 * 
 * @author zxx
 *
 */
public interface WidgetsMapper {

	/**
	 * 得到所有的组件
	 * 
	 * @param widget
	 * @return
	 */
	public ArrayList<Widgets> selectWidgets(Widgets widgets);
}
