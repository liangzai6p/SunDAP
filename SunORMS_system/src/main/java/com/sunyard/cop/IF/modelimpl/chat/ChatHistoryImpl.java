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
import com.sunyard.cop.IF.common.SunIFCommonUtil;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.chat.IChatHistoryService;
import com.sunyard.cop.IF.mybatis.dao.IChatHistoryMapper;
import com.sunyard.cop.IF.mybatis.pojo.ChatHistory;
import com.sunyard.cop.IF.mybatis.pojo.User;
import com.sunyard.cop.IF.spring.websocket.WebSocketSessionUtils;
@Service("chatHistoryService")
@Transactional
public class ChatHistoryImpl implements IChatHistoryService {
	@Resource
	 private IChatHistoryMapper   chatHistoryDao;
	 private ResponseBean retBean = new ResponseBean();
	@Override
	public void insertHistory(ChatHistory chatHistory) {
		// TODO Auto-generated method stub
		insertChatHistory(chatHistory);
	}

	

	@Override
	public ResponseBean insertHistory(RequestBean req) throws Exception {
		Map requestMaps=new HashMap();
		requestMaps=req.getSysMap();
		String oper_type = String.valueOf(requestMaps.get("oper_type"));
		if("insert".equalsIgnoreCase(oper_type)){ 
			ArrayList requestList = (ArrayList) req.getParameterList();
			ChatHistory historyBean=(ChatHistory) requestList.get(0);
			String send_time = historyBean.getSendTime();
			String chatId=send_time+SunIFCommonUtil.getRandomStrings(10);
			historyBean.setChatId(chatId);
			User user=BaseUtil.getLoginUser();
			historyBean.setBankNo(user.getBankNo());
			historyBean.setProjectNo(user.getProjectNo());
			historyBean.setSystemNo(user.getSystemNo());
			insertChatHistory(historyBean);
			retBean.setRetCode(SunIFErrorMessage.SUCCESS);
			retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
		}else if("update".equalsIgnoreCase(oper_type)){//更新消息状态
			ArrayList requestList = (ArrayList) req.getParameterList();
			ChatHistory chistoryBean=(ChatHistory) requestList.get(0);
			updateChatHistory(chistoryBean);
		}else if("updateStatus".equalsIgnoreCase(oper_type)){//更新消息状态
			ArrayList requestList = (ArrayList) req.getParameterList();
			ChatHistory chistoryBean=(ChatHistory) requestList.get(0);
			this.updateStatus(chistoryBean);
		}else if("getUnreadMSG".equalsIgnoreCase(oper_type)){//获取未读消息
			String loginType = String.valueOf(requestMaps.get("loginType"));
			ArrayList requestList = (ArrayList) req.getParameterList();
			ChatHistory chistoryBean=(ChatHistory) requestList.get(0);
			String chatTo=chistoryBean.getChatTo();
			ArrayList<ChatHistory> list=this.getUnreadMsg(chistoryBean);		
			for(int i=0;i<list.size();i++){
				JSONObject json = new JSONObject();
				chistoryBean=list.get(i);
				json.put("chatFrom", chistoryBean.getChatFrom());
				json.put("chatTo",chistoryBean.getChatTo() );
				json.put("chatcontent",chistoryBean.getChatcontent() );
				json.put("sendTime",chistoryBean.getSendTime() );
				json.put("type", "chat_one");
				try {
					if(WebSocketSessionUtils.hasConnection(chatTo, loginType)){
						WebSocketSessionUtils.sendMessage(chatTo, loginType, json.toString());
	        		}
	        		
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else if("getTotalPage".equalsIgnoreCase(oper_type)){//获取历史消息总数
			Map queryMap=new HashMap();
			ArrayList requestList = (ArrayList) req.getParameterList();
			ChatHistory chistoryBean=(ChatHistory) requestList.get(0);
			int total = this.getTotalPage(chistoryBean);
			queryMap.put("total", total);
			retBean.setRetCode(SunIFErrorMessage.SUCCESS);
			retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
			retBean.setRetMap(queryMap);
		}else if("getHistoryPage".equalsIgnoreCase(oper_type)){//历史消息分页
			Map queryMap=new HashMap();
			ArrayList requestList = (ArrayList) req.getParameterList();
			ChatHistory chistoryBean=(ChatHistory) requestList.get(0);
			int pageNum =  (int) requestMaps.get("pageNum");//当前页数
			int pageSize = (int) requestMaps.get("pageSize");//每页数据量			
			PageHelper.startPage(pageNum, pageSize);
			ArrayList<ChatHistory> list = this.getHistoryPage(chistoryBean);
			queryMap.put("list", list);
			retBean.setRetCode(SunIFErrorMessage.SUCCESS);
			retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
			retBean.setRetMap(queryMap);
		}
		return retBean;
	}
    /**
     * 插入聊天记录
     * @param chatHistory
     */

	private void insertChatHistory(ChatHistory chatHistory){		 
		chatHistoryDao.insertChatHistory(chatHistory);
	}
	/**
	 * 修改消息状态（未读变已读）,根据chatFrom chatTo
	 * @param chatHistory
	 */
	
	private void updateChatHistory(ChatHistory chatHistory){
		User user=BaseUtil.getLoginUser();
		chatHistory.setBankNo(user.getBankNo());
		chatHistory.setProjectNo(user.getProjectNo());
		chatHistory.setSystemNo(user.getSystemNo());
		chatHistoryDao.updateChatHistory(chatHistory);
	}
	/**
	 * 修改消息状态（未读变已读）,根据chatID
	 * 
	 * @param chatHistory
	 */
	
	private void updateStatus(ChatHistory chatHistory){
		User user=BaseUtil.getLoginUser();
		chatHistory.setBankNo(user.getBankNo());
		chatHistory.setProjectNo(user.getProjectNo());
		chatHistory.setSystemNo(user.getSystemNo());
		chatHistoryDao.updateHistory(chatHistory);
	}
	/**
	 * 获取未读消息
	 * @param chatHistory
	 * @return
	 */
	
	private ArrayList getUnreadMsg(ChatHistory chatHistory){
		User user=BaseUtil.getLoginUser();
		chatHistory.setBankNo(user.getBankNo());
		chatHistory.setProjectNo(user.getProjectNo());
		chatHistory.setSystemNo(user.getSystemNo());
		chatHistory.setStatus("0");
		return chatHistoryDao.getUnreadMsg(chatHistory);
	}

	/**
	 * 获取半年内的聊天记录总数
	 * @param chatHistory
	 * @return
	 */
	
	private int getTotalPage (ChatHistory chatHistory){
		User user=BaseUtil.getLoginUser();
		chatHistory.setBankNo(user.getBankNo());
		chatHistory.setProjectNo(user.getProjectNo());
		chatHistory.setSystemNo(user.getSystemNo());		
		return chatHistoryDao.getTotalPage(chatHistory);
	}
	
	
	private ArrayList  getHistoryPage(ChatHistory chatHistory){
		User user=BaseUtil.getLoginUser();	
		chatHistory.setBankNo(user.getBankNo());
		chatHistory.setSystemNo(user.getSystemNo());
		chatHistory.setProjectNo(user.getProjectNo());
		return chatHistoryDao.ChatHistoryPage(chatHistory);
	}
}
