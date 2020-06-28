package com.sunyard.cop.IF.modelimpl.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.sunyard.aos.common.util.BaseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.cop.IF.bean.RequestBean;
import com.sunyard.cop.IF.bean.ResponseBean;
import com.sunyard.cop.IF.common.SunIFCommonUtil;
import com.sunyard.cop.IF.common.SunIFConstant;
import com.sunyard.cop.IF.common.SunIFErrorMessage;
import com.sunyard.cop.IF.common.http.RequestUtil;
import com.sunyard.cop.IF.model.chat.IChatGroupService;
import com.sunyard.cop.IF.mybatis.dao.ChatGroupMapper;
import com.sunyard.cop.IF.mybatis.dao.GroupHistoryMapper;
import com.sunyard.cop.IF.mybatis.pojo.ChatGroup;
import com.sunyard.cop.IF.mybatis.pojo.GroupHistory;
import com.sunyard.cop.IF.mybatis.pojo.User;
import com.sunyard.cop.IF.spring.websocket.WebSocketSessionUtils;
@Service("chatGrouopService")
@Transactional
public class ChatGroupImpl implements IChatGroupService  {
    
	 @Resource
	  private ChatGroupMapper chatGroupDao;
	 @Resource
	 private GroupHistoryMapper groupHistoryDao;
	  private ResponseBean retBean = new ResponseBean();
	@Override

	public ResponseBean chatGroup(RequestBean req) throws Exception {
		
		Map requestMaps=new HashMap();
		requestMaps=req.getSysMap();
		String oper_type = String.valueOf(requestMaps.get("oper_type"));
		if("insert".equalsIgnoreCase(oper_type)){ 
			ArrayList requestList = (ArrayList) req.getParameterList();
			ChatGroup chatGroup = (ChatGroup) requestList.get(0);
			String createTime = SunIFCommonUtil.getFomateTimeString("yyyyMMddHHmmss");
			chatGroup.setCreateTime(createTime);
			String groupId= BaseUtil.filterSqlParam(this.getMaxGroupID());
			chatGroup.setGroupId(groupId);
			 insertGroup(chatGroup);
			 Map queryMap=new HashMap();
			 queryMap.put("groupId", groupId);
			 retBean.setRetCode(SunIFErrorMessage.SUCCESS);
			 retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
			 retBean.setRetMap(queryMap);			 
		}if(SunIFConstant.UPDATE.equalsIgnoreCase(oper_type)){
			ArrayList requestList = (ArrayList) req.getParameterList();
			ChatGroup chatGroup = (ChatGroup) requestList.get(0);
			String update_type = String.valueOf(requestMaps.get("update_type"));
			this.updateGroup(chatGroup,update_type);
		}else if("select".equalsIgnoreCase(oper_type)){
			Map queryMap=new HashMap();
			ArrayList requestList = (ArrayList) req.getParameterList();
			ChatGroup chatGroup = (ChatGroup) requestList.get(0);
			String group_member=chatGroup.getGroupMember();
			ArrayList list=this.selectGroup(group_member);
			queryMap.put("groupLsit", list);
			 retBean.setRetCode(SunIFErrorMessage.SUCCESS);
			 retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
			 retBean.setRetMap(queryMap);
		}else if ("showMembers".equalsIgnoreCase(oper_type)){
			Map queryMap=new HashMap();
			ArrayList requestList = (ArrayList) req.getParameterList();
			ChatGroup chatGroup = (ChatGroup) requestList.get(0);
			String member = this.showMembers(chatGroup);
			 queryMap.put("member", member);
			 retBean.setRetCode(SunIFErrorMessage.SUCCESS);
			 retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
			 retBean.setRetMap(queryMap);
		}else if("deleteGroup".equalsIgnoreCase(oper_type)){
			ArrayList requestList = (ArrayList) req.getParameterList();
			ChatGroup chatGroup = (ChatGroup) requestList.get(0);
			this.deleteGroup(chatGroup);
		}else if("selectInfor".equalsIgnoreCase(oper_type)){
			Map queryMap=new HashMap();
			ArrayList requestList = (ArrayList) req.getParameterList();
			ChatGroup chatGroup = (ChatGroup) requestList.get(0);
			 ArrayList list= this.selectInfor(chatGroup);
			 queryMap.put("list", list);
			 retBean.setRetCode(SunIFErrorMessage.SUCCESS);
			 retBean.setRetMsg(SunIFErrorMessage.SUCCESS_MSG);
			 retBean.setRetMap(queryMap);
		}
		return retBean;
	}
	/**
	 * 新建群
	 * @param chatGroup
	 */

	private void insertGroup(ChatGroup chatGroup){	
		User user=BaseUtil.getLoginUser();
		chatGroup.setBankNo(user.getBankNo());
		chatGroup.setProjectNo(user.getProjectNo());
		chatGroup.setSystemNo(user.getSystemNo());	
		String groupMember=chatGroup.getGroupMember();
		String materNo= chatGroup.getMasterNo();
		String isOk= chatGroup.getIsOk();
		String msg="addgroup_"+chatGroup.getGroupId()+"_"+chatGroup.getGroupName()+"_"+isOk+"_"+materNo;
		chatGroupDao.insertGroup(chatGroup);
		String[] memberlist=groupMember.split(",");
		for (String member : memberlist) {
				try {
				    if(WebSocketSessionUtils.hasConnection(member, "1")){						
					  WebSocketSessionUtils.sendMessage(member, "1", msg);						
	        		}
	        		if(WebSocketSessionUtils.hasConnection(member, "2")){
						WebSocketSessionUtils.sendMessage(member, "2", msg);
	        		}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
		}
	}
    /**
     * 修改群成员
     * @param groupId
     * @param groupMember
     */

	private void updateGroup(ChatGroup chatGroup,String update_type){	
		User user=BaseUtil.getLoginUser();
		chatGroup.setBankNo(user.getBankNo());
		chatGroup.setProjectNo(user.getProjectNo());
		chatGroup.setSystemNo(user.getSystemNo());
		String groupId = chatGroup.getGroupId();
		String groupName = chatGroup.getGroupName();
		String isOk = chatGroup.getIsOk();
		String masterNo = chatGroup.getMasterNo();
		chatGroupDao.updateGroup(chatGroup);		
		String userNo = user.getUserNo();
		if("add_".equalsIgnoreCase(update_type.substring(0,4))){
			String msg="addgroup_"+groupId+"_"+groupName+"_"+isOk+"_"+masterNo;
			String addMember = update_type.split("_")[1];
			String[] memberlist=addMember.split(",");			
			for (String member : memberlist) {			
				if(!member.equalsIgnoreCase(userNo)){
					try {
					    if(WebSocketSessionUtils.hasConnection(member, "1")){						
							WebSocketSessionUtils.sendMessage(member, "1", msg);
		        		}
		        		if(WebSocketSessionUtils.hasConnection(member, "2")){
							WebSocketSessionUtils.sendMessage(member, "2", msg);
		        		}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}else if("deletegroup_".equalsIgnoreCase(update_type.substring(0,12))){
			String msg="deleteroup_"+groupId+"_"+groupName;
			String addMember = update_type.split("_")[1];
			String[] memberlist=addMember.split(",");
			for (String member : memberlist) {	
				try {
				    if(WebSocketSessionUtils.hasConnection(member, "1")){						
				    	WebSocketSessionUtils.sendMessage(member, "1", msg);
	        		}
	        		if(WebSocketSessionUtils.hasConnection(member, "2")){
	        			WebSocketSessionUtils.sendMessage(member, "2", msg);
	        		}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			
		}
	}
	/**
	 * 查询所在所有群
	 * @param group_member
	 * @return
	 */

	private ArrayList  selectGroup(String group_member){
		ChatGroup chatGroup =new ChatGroup();
		chatGroup.setGroupMember("%"+group_member+"%");
		User user=BaseUtil.getLoginUser();
		chatGroup.setBankNo(user.getBankNo());
		chatGroup.setProjectNo(user.getProjectNo());
		chatGroup.setSystemNo(user.getSystemNo());
		return chatGroupDao.selectGroup(chatGroup);
		
	}
	

	private String getMaxGroupID(){
		ChatGroup chatGroup =new ChatGroup();
		User user=BaseUtil.getLoginUser();
		chatGroup.setBankNo(user.getBankNo());
		chatGroup.setProjectNo(user.getProjectNo());
		chatGroup.setSystemNo(user.getSystemNo());
		return chatGroupDao.maxGroupId(chatGroup);
	}
	/**
	 *展现所有群成员
	 * @param chatGroup
	 * @return
	 */

	private String showMembers(ChatGroup chatGroup){
		User user=BaseUtil.getLoginUser();
		chatGroup.setBankNo(user.getBankNo());
		chatGroup.setProjectNo(user.getProjectNo());
		chatGroup.setSystemNo(user.getSystemNo());
		return chatGroupDao.showMembers(chatGroup);
				
	}
	/**
	 * 解散群
	 * @param chatGroup
	 */

    private void deleteGroup (ChatGroup chatGroup){
		User user=BaseUtil.getLoginUser();
		chatGroup.setBankNo(user.getBankNo());
		chatGroup.setProjectNo(user.getProjectNo());
		chatGroup.setSystemNo(user.getSystemNo()); 
		String members = chatGroupDao.showMembers(chatGroup);
		chatGroupDao.deleteGroup(chatGroup);
	    GroupHistory groupHistory = new GroupHistory();
	    groupHistory.setBankNo(user.getBankNo());
	    groupHistory.setProjectNo(user.getProjectNo());
	    groupHistory.setSystemNo(user.getSystemNo()); 
	    groupHistory.setGroupId(chatGroup.getGroupId());
	    groupHistoryDao.deleteGroupHistory(groupHistory);
		String[] memberlist=members.split(",");
		String groupId = chatGroup.getGroupId();
		String groupName = chatGroup.getGroupName();
		String msg="deleteroup_"+groupId+"_"+groupName;
		for (String member : memberlist) {	
			try {
			    if(WebSocketSessionUtils.hasConnection(member, "1")){						
					WebSocketSessionUtils.sendMessage(member, "1", msg);
        		}
        		if(WebSocketSessionUtils.hasConnection(member, "2")){
					WebSocketSessionUtils.sendMessage(member, "2", msg);
        		}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	/**
	 * 查询群资料
	 * @param chatGroup
	 * @return
	 */
	
	private  ArrayList selectInfor (ChatGroup chatGroup){
		User user=BaseUtil.getLoginUser();
		chatGroup.setBankNo(user.getBankNo());
		chatGroup.setProjectNo(user.getProjectNo());
		chatGroup.setSystemNo(user.getSystemNo()); 		
		return chatGroupDao.showGroupInfor(chatGroup);
	}
}
