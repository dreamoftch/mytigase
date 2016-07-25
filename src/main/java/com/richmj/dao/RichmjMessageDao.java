package com.richmj.dao;

import com.richmj.models.CustomChatRecord;
import com.richmj.utils.JdbcUtil;

public class RichmjMessageDao {

	public void saveMessage(CustomChatRecord message){
		JdbcUtil.insertMessage(message);
	}
	
}
