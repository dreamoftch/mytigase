package com.richmj.constants;

public final class Constant {

	private Constant(){}
	
	/**
	 * 自定义stanze的from
	 */
	public static final String RICHMJ_STANZA_FROM = "richmjFrom";
	
	/**
	 * 自定义stanze的to
	 */
	public static final String RICHMJ_STANZA_TO = "richmjTo";
	
	/**
	 * tigase聊天系统的server host
	 */
	public static String SERVER_HOST = null;
	
	public static final String SYSTEM_ACCOUNT = "richmj";
	
	/**
	 * 自定义聊天消息中type的属性（表示计划书/约见书/推荐客户等消息类型）
	 */
	public static final String CUSTOM_CHAT_RECORD_TYPE_ATTR = "type";
	
	/**
	 * 自定义聊天消息中name的属性（用于搜索聊天记录的属性）
	 */
	public static final String CUSTOM_CHAT_RECORD_NAME_ATTR = "name";
	
}
