package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;

import com.sunyard.cop.IF.mybatis.pojo.ChatHistory;

public interface IChatHistoryMapper {
	public void insertChatHistory(ChatHistory chathistory);

	public void updateChatHistory(ChatHistory chathistory);

	public void updateHistory(ChatHistory chathistory);

	public ArrayList<ChatHistory> getUnreadMsg(ChatHistory chathistory);

	public ArrayList<ChatHistory> getChatHistoryList(ChatHistory chathistory);

	public int getTotalPage(ChatHistory chathistory);

	public ArrayList<ChatHistory> ChatHistoryPage(ChatHistory chathistory);
}