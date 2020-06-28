package com.sunyard.cop.IF.modelimpl.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.sunyard.aos.common.util.BaseUtil;
import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.chat.IGroupHistoryService;
import com.sunyard.cop.IF.mybatis.dao.ChatGroupMapper;
import com.sunyard.cop.IF.mybatis.dao.GroupHistoryMapper;
import com.sunyard.cop.IF.mybatis.pojo.ChatGroup;
import com.sunyard.cop.IF.mybatis.pojo.GroupHistory;
import com.sunyard.cop.IF.mybatis.pojo.User;
import com.sunyard.cop.IF.spring.websocket.WebSocketSessionUtils;
@Service("grouopHistoryService")
@Transactional
public class GroupHistoryImpl implements IGroupHistoryService  {
	
	@Resource
	private GroupHistoryMapper groupHistoryDao;
	
	@Resource
	private ChatGroupMapper chatGroupDao;
	private ResponseBean retBean = new ResponseBean();
	@Override
	public void insertGroupHistory(GroupHistory groupHistory) {
		insertChatHistory(groupHistory);
		String groupId = groupHistory.getGroupId();
		String chatFrom = groupHistory.getChatFrom();
		ChatGroup chatGroup = new ChatGroup();
		chatGroup.setGroupId(groupId);
		chatGroup.setBankNo(groupHistory.getBankNo());
		chatGroup.setProjectNo(groupHistory.getProjectNo());
		chatGroup.setSystemNo(groupHistory.getSystemNo());
		String members = this.showMembers(chatGroup);
		String[] memberlist = members.split(",");
		JSONObject json = new JSONObject();
		json.put("chatFrom", groupHistory.getChatFrom());
		json.put("chatTo",groupHistory.getGroupId() );
		json.put("chatcontent", groupHistory.getChatContent());
		json.put("sendTime",groupHistory.getSendTime());
		json.put("type", "chat_group");
		for(String member : memberlist ){
			if(!member.equalsIgnoreCase(chatFrom)){
				try {							
					if(WebSocketSessionUtils.hasConnection(member, "1")){
						WebSocketSessionUtils.sendMessage(member, "1", json.toString());
	        		}
	        		if(WebSocketSessionUtils.hasConnection(member, "2")){
						WebSocketSessionUtils.sendMessage(member, "2", json.toString());
	        		}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public ResponseBean groupHistory(RequestBean req)  throws Exception{
		Map requestMaps=new HashMap();
		requestMaps=req.getSysMap();
		String oper_type = String.valueOf(requestMaps.get("oper_type"));
		if("unread".equalsIgnoreCase(oper_type)){
			String loginType = String.valueOf(requestMaps.get("loginType"));
			User user=BaseUtil.getLoginUser();
			String userNo = user.getUserNo();
			ArrayList requestList = (ArrayList) req.getParameterList();
			GroupHistory groupHistory = (GroupHistory) requestList.get(0);
			ArrayList<GroupHistory> list=this.selectUnreadHistry(groupHistory);
		    for (int i=0;i<list.size();i++){
		    	GroupHistory groupHistorys = list.get(i);
		    	JSONObject json = new JSONObject();
				json.put("chatFrom", groupHistorys.getChatFrom());
				json.put("chatTo",groupHistorys.getGroupId() );
				json.put("chatcontent", groupHistorys.getChatContent());
				json.put("sendTime",groupHistorys.getSendTime());
				json.put("type", "chat_group");
				try {
					if(WebSocketSessionUtils.hasConnection(userNo, loginType)){
						WebSocketSessionUtils.sendMessage(userNo, loginType, json.toString());
	        		}
	        		
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
			
		}else if("getTotalPage".equalsIgnoreCase(oper_type)){
			Map queryMap=new HashMap();
			ArrayList requestList = (ArrayList) req.getParameterList();
			GroupHistory groupHistory = (GroupHistory) requestList.get(0);
			int total = this.selectHistoryTotal(groupHistory);
			queryMap.put("total", total);
			retBean.setRetCode(SunIFErrorMessage.SUCCESS);
			retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
			retBean.setRetMap(queryMap);
	    }else if("getHistoryPage".equalsIgnoreCase(oper_type)){
	    	Map queryMap=new HashMap();
			ArrayList requestList = (ArrayList) req.getParameterList();
			GroupHistory groupHistory = (GroupHistory) requestList.get(0);
			int pageNum =  (int) requestMaps.get("pageNum");//当前页数
			int pageSize = (int) requestMaps.get("pageSize");//每页数据量
			PageHelper.startPage(pageNum, pageSize);
			ArrayList<GroupHistory> list = this.selectHistoryPage(groupHistory);
			queryMap.put("list", list);
			retBean.setRetCode(SunIFErrorMessage.SUCCESS);
			retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
			retBean.setRetMap(queryMap);
	    }
		return retBean;
	}
	
	/**
	 * 插入群聊天表
	 * @param groupHistory
	 */
	
	private void insertChatHistory(GroupHistory groupHistory){	
		groupHistoryDao.insertGroupHistory(groupHistory);;
	}
	/**
	 * 查询群成员
	 * @param chatGroup
	 * @return
	 */

	private String showMembers(ChatGroup chatGroup){

		return chatGroupDao.showMembers(chatGroup);
				
	}
	
	
	private ArrayList selectUnreadHistry(GroupHistory groupHistory){
		User user=BaseUtil.getLoginUser();
		groupHistory.setBankNo(user.getBankNo());
		groupHistory.setProjectNo(user.getProjectNo());
		groupHistory.setSystemNo(user.getSystemNo());
		return groupHistoryDao.getUnreadMsg(groupHistory);
	}
	/**
	 *获取半年内的总页数
	 * @param groupHistory
	 * @return
	 */

	private int selectHistoryTotal (GroupHistory groupHistory){
		User user=BaseUtil.getLoginUser();
		groupHistory.setBankNo(user.getBankNo());
		groupHistory.setProjectNo(user.getProjectNo());
		groupHistory.setSystemNo(user.getSystemNo());		
		return groupHistoryDao.getToatalGroupHistory(groupHistory);
	}
	

    private ArrayList selectHistoryPage (GroupHistory groupHistory){
		User user=BaseUtil.getLoginUser();
		groupHistory.setBankNo(user.getBankNo());
		groupHistory.setProjectNo(user.getProjectNo());
		groupHistory.setSystemNo(user.getSystemNo());
		return groupHistoryDao.GroupHistoryPage(groupHistory);
	}
}
