package com.richmj.dao;

import com.richmj.models.RichmjMessage;
import com.richmj.utils.JdbcUtil;

public class RichmjMessageDao {

	public void saveMessage(RichmjMessage message){
		JdbcUtil.insertMessage(message);
	}
	
}
