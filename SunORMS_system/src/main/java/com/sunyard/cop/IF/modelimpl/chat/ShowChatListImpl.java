package com.sunyard.cop.IF.modelimpl.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.chat.IShowChatList;
import com.sunyard.cop.IF.mybatis.dao.UserMapper;
import com.sunyard.cop.IF.mybatis.pojo.User;
import com.sunyard.cop.IF.spring.websocket.WebSocketSessionUtils;
@Service("showChatListService")
@Transactional
public class ShowChatListImpl  implements IShowChatList{

	
	 @Resource
	 private UserMapper UserDao;
	 
	  private ResponseBean retBean=new ResponseBean();
	@Override
	public ResponseBean showChatList(RequestBean req) throws Exception{
		// TODO Auto-generated method stub
		Map requestMaps = new HashMap();
		requestMaps = req.getSysMap();
		String oper_type = (String) requestMaps.get("oper_type");
		if("searchList".equalsIgnoreCase(oper_type)){
			String serach_type = (String) requestMaps.get("serach_type");
			switch (serach_type){
			    case "friends":
			    	dealList();
				      break;
				default:
					break;
					
				}
		}
		
		return retBean;
	}
	private void dealList(){
		Map queryMap=new HashMap();
		ArrayList list=queryFriends();
		String mapKey = "";
		String mapValue = "";
		Map<String, String> map = new HashMap<String, String>();
		for (Object o : list) {
			HashMap maps = (HashMap) o;
			for (Object obj : maps.keySet()) {
				String key = obj + "";
				String value = maps.get(key) + "";
				if (key.equalsIgnoreCase("user_no")) {
					mapValue = value.trim();
					if (WebSocketSessionUtils.hasConnection(mapValue.split("-")[0],"1")||WebSocketSessionUtils.hasConnection(mapValue.split("-")[0], "2")){
						mapValue = mapValue + "-1";
					} else {
						mapValue = mapValue + "-0";
					}
				} else if (key.equalsIgnoreCase("organ_name")) {
					mapKey = value.trim();
				}
			}
			
			
			if (map.get(mapKey) == null) {
				map.put(mapKey, mapValue);
			} else {
				map.put(mapKey, map.get(mapKey) + "," + mapValue);
			}
		}
		queryMap.put("friendsList", map);
		retBean.setRetCode(SunIFErrorMessage.SUCCESS);
		retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
		retBean.setRetMap(queryMap);
	}
	
	
	
	private ArrayList queryFriends(){
		User user = BaseUtil.getLoginUser();
		return  UserDao.chatSearch(user);
	}
}
