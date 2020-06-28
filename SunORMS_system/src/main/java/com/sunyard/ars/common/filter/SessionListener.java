package com.sunyard.ars.common.filter;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunyard.cop.IF.mybatis.pojo.User;


/**
 * 继承HttpSessionListener接口的类，来监听Session创建和销毁的事件
 */
public class SessionListener implements HttpSessionListener{
	// 日志记录器
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private static int count=0;
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		count++;
		logger.info("SESSION创建："+new java.util.Date()+":"+se.getSession().getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se){

		String userNo = null;
		/*try {
			userNo = BaseUtil.getLoginUser().getUserNo();
		} catch (Exception e) {
			e.printStackTrace();
		} */
		User user = (User) se.getSession().getAttribute("user");
		if(user == null) {
			logger.info("SESSION销毁时异常===>{}","user == null ?"+ user == null);
			return;
		}
		userNo = user.getUserNo();
		count--;
		logger.info("SESSION销毁:" + new java.util.Date() + ":"	+ se.getSession().getId() + " 用户(超时/退出)：" + userNo + " 会话数：" + getCount());

	}

	public static int getCount()
	{
		return(count);
	}
	
}
