package com.richmj.dao;

import com.richmj.models.CustomChatRecord;
import com.richmj.models.CustomGroupChatRecord;
import com.richmj.utils.JdbcUtil;

public class RichmjMessageDao {

	/**
	 * 保存单聊消息
	 * @param message
	 */
	public void saveChatMessage(CustomChatRecord message){
		JdbcUtil.insertChatMessage(message);
	}
	
	/**
	 * 保存群聊消息
	 * @param message
	 */
	public void saveGroupChatMessage(CustomGroupChatRecord message){
		JdbcUtil.insertGroupChatMessage(message);
	}
	
}
