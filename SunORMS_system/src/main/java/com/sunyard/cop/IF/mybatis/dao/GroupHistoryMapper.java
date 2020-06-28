package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;

import com.sunyard.cop.IF.mybatis.pojo.GroupHistory;

public interface GroupHistoryMapper {

	public void insertGroupHistory(GroupHistory groupHistory);

	public ArrayList<GroupHistory> getGroupMsg(GroupHistory groupHistory);

	public ArrayList<GroupHistory> getUnreadMsg(GroupHistory groupHistory);

	public int getToatalGroupHistory(GroupHistory groupHistory);

	public ArrayList<GroupHistory> GroupHistoryPage(GroupHistory groupHistory);

	public void deleteGroupHistory(GroupHistory groupHistory);
}