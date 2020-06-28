package com.sunyard.cop.IF.mybatis.dao;

import java.util.ArrayList;

import com.sunyard.cop.IF.mybatis.pojo.ChatGroup;

public interface ChatGroupMapper {

	public void insertGroup(ChatGroup chatGroup);

	public void updateGroup(ChatGroup chatGroup);

	public ArrayList<ChatGroup> selectGroup(ChatGroup chatGroup);

	public String maxGroupId(ChatGroup chatGroup);

	public String showMembers(ChatGroup chatGroup);

	public void deleteGroup(ChatGroup chatGroup);

	public ArrayList<ChatGroup> showGroupInfor(ChatGroup chatGroup);
}